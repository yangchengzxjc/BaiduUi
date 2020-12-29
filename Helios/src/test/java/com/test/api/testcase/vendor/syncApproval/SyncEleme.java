package com.test.api.testcase.vendor.syncApproval;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicConstant.TmcChannel;
import com.hand.basicObject.Employee;
import com.hand.basicObject.component.FormComponent;
import com.hand.basicObject.itinerary.DiningItinerary;
import com.hand.basicObject.supplierObject.syncApproval.syncCtrip.CtripApprovalEntity;
import com.hand.utils.GsonUtil;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.*;
import com.test.api.method.ApplicationMethod.TravelApplicationPage;
import com.test.api.method.VendorMethod.SyncService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.json.Json;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;

@Slf4j
public class SyncEleme extends BaseTest {

    private TravelApplication travelApplication;
    private Employee employee;
    private ExpenseReportComponent expenseReportComponent;
    private ExpenseReport expenseReport;
    private TravelApplicationPage travelApplicationPage;
    private Vendor vendor;
    private SyncService syncService;
    private Approve approve;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("14082978000") String phoneNumber, @Optional("hly123456") String pwd, @Optional("stage") String env) {
        employee = getEmployee(phoneNumber, pwd, env);
        travelApplication = new TravelApplication();
        expenseReportComponent = new ExpenseReportComponent();
        expenseReport = new ExpenseReport();
        travelApplicationPage = new TravelApplicationPage();
        syncService = new SyncService();
        vendor = new Vendor();
        approve = new Approve();
    }

    @DataProvider(name = "eleme")
    public Object[][] testData() {
        return new Object[][]{
                {"懿消费商(xiao/feishang)", "饿了么餐补申请单", "测试饿了么", "上海", 50.0, "餐补", "餐补"},
        };
    }

    @Test(description = "消费商-饿了么用餐", dataProvider = "eleme")
    public void elemeTest1(String userName,
                           String formName,
                           String cause,
                           String cityName,
                           Double amount,
                           String expenseName,
                           String sceneName) throws HttpStatusException, InterruptedException {

//        // 测试数据抽取到 dataProvider
//        String userName = "懿消费商(xiao/feishang)";
//        String formName = "饿了么餐补申请单";
//        String cause = "测试饿了么";
//        Double amount = 50.0;
//        String expenseName = "餐补";
//        String cityName = "上海";
//        String sceneName = "餐补"; // 用餐场景
        String startDate = UTCTime.getNowStartUtcDate();
        String endDate = UTCTime.getUTCDateEnd(5);
        String formOID = expenseReport.getFormOID(employee, formName, "101");

        // 表单控件必填项：事由、开始日期、结束日期、参与人默认自己
        FormComponent component = new FormComponent();
        component.setCause(cause);
        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(startDate);
        component.setEndDate(endDate);

        // 表单添加参与人员
        JsonArray array = new JsonArray();
        array.add(expenseReportComponent.getParticipant(employee, formOID, userName));
        component.setParticipant(array.toString());

        // 保存申请单 拿到申请单oid
        String applicationOID = travelApplication.createTravelApplication(employee, formName, component).get("applicationOID");

        //添加用餐行程
        ArrayList<DiningItinerary> diningItineraries = new ArrayList<>();
//        Long diningSceneId = 1218722645100748802L;
        Long diningSceneId = travelApplication.getDiningSceneId(employee, formOID, sceneName);
        DiningItinerary diningItinerary = travelApplicationPage.setDiningItinerary(employee, diningSceneId, cityName, startDate, endDate, 1.00, "");
        diningItineraries.add(diningItinerary);
        travelApplication.addItinerary(employee, applicationOID, diningItineraries);

        // 添加预算费用
        JsonObject budgetExpense = travelApplication.addBudgetExpense(employee, amount, true, expenseName, formName);
        JsonArray budgetExpenses = new JsonArray();
        budgetExpenses.add(budgetExpense);
        String budgetDetail = travelApplication.addBudgetDetail(budgetExpenses, amount);

        // 提交申请单
        travelApplication.submitApplication(employee, applicationOID, budgetDetail);

        //审批单审批
        JsonObject approvalRes = approve.approval(employee, applicationOID, 1001);
        int approvalSuccessNum = approvalRes.get("successNum").getAsInt();
        String approvalFailReason = approvalRes.get("failReason").toString();
        if (approvalSuccessNum != 1 && !approvalFailReason.contains("无须审批")) {
            log.info("审批结果：{}", approvalRes);
            throw new RuntimeException("审批单审批失败");
        } else {
            JsonObject dining = travelApplication.getItinerary(employee, applicationOID, "DINING").get(0).getAsJsonObject();
            // 获取审批单中的 travelApplication
            JsonObject traveApplicationDetail = travelApplication.getApplicationDetail(employee, applicationOID);

            // 查询同步实体 todo-同步实体支持用餐行程
            CtripApprovalEntity ctripApprovalEntity = syncService.setCtripApprovalEntity(traveApplicationDetail, dining, null, null, null, null);
            JsonObject syncEntityJson = new JsonParser().parse(GsonUtil.objectToString(ctripApprovalEntity)).getAsJsonObject();
            log.info("同步实体syncEntityJson数据为：{}", syncEntityJson);

            //查询tmc 同步的数据
            JsonObject tmcRequestData = vendor.getTMCPlanRequestDTO(employee, TmcChannel.ELEME.getTmcChannel(), dining.get("approvalNumber").getAsString());
            log.info("查询的tmcRequestData数据为：{}", tmcRequestData);

            assert GsonUtil.compareJsonObject(syncEntityJson, tmcRequestData, new HashMap<>());
        }
    }
}

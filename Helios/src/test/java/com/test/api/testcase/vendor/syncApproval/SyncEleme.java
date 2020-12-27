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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

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

    @Test(description = "消费商-饿了么用餐")
    public void elemeTest1() throws HttpStatusException, InterruptedException {

        String userName = "懿消费商(xiao/feishang)";
        String formName = "饿了么餐补申请单";
        String formOID = expenseReport.getFormOID(employee, formName, "101");
        String startDate = UTCTime.getNowStartUtcDate();
        String endDate = UTCTime.getUTCDateEnd(5);

        // 表单控件必填项：事由、开始日期、结束日期、参与人默认自己
        FormComponent component = new FormComponent();
        component.setCause("饿了么用餐测试");
        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(startDate);
        component.setEndDate(endDate);
        // 表单添加参与人员  参与人员的value 是一段json数组
        JsonArray array = new JsonArray();
        array.add(expenseReportComponent.getParticipant(employee, formOID, userName));
        component.setParticipant(array.toString());

        // 保存申请单 拿到申请单oid
        String applicationOID = travelApplication.createTravelApplication(employee, formName, component).get("applicationOID");

        //添加用餐行程
        ArrayList<DiningItinerary> diningItineraries = new ArrayList<>();
        // 查询用餐类型 @applicationOID todo
        Long diningSceneId = 1218722645100748802L;
        DiningItinerary diningItinerary = travelApplicationPage.setDiningItinerary(employee, diningSceneId, "上海", startDate, endDate, 1.00, "");
        diningItineraries.add(diningItinerary);
        travelApplication.addItinerary(employee, applicationOID, diningItineraries);
        // 提交申请单 todo-添加预算费用
        travelApplication.submitApplication(employee, applicationOID, "50");

        //审批单审批
        if (approve.approveal(employee, applicationOID, 1001) != 1) {
            throw new RuntimeException("审批单审批失败");
        } else {
            sleep(3000);
            JsonObject dining = travelApplication.getItinerary(employee, applicationOID, "DINING").get(0).getAsJsonObject();

            // 获取审批单中的 travelApplication
            JsonObject traveApplicationDetail = travelApplication.getApplicationDetail(employee, applicationOID);

            // 查询同步实体
            CtripApprovalEntity ctripApprovalEntity = syncService.setCtripApprovalEntity(traveApplicationDetail, dining, null, null, null, null);
            JsonObject syncEntityJson = new JsonParser().parse(GsonUtil.objectToString(ctripApprovalEntity)).getAsJsonObject();

            //查询tmc 同步的数据
            JsonObject syncData = vendor.getTMCPlan(employee, TmcChannel.ELEME.getTmcChannel(), dining.get("approvalNum").getAsString());
            log.info("查询的数据为：{}", syncData);
            JsonObject tmcRequestData = syncData.getAsJsonObject("tmcRequest");
//            JsonObject tmcResponse = syncData.getAsJsonObject("response");
            assert GsonUtil.compareJsonObject(syncEntityJson, tmcRequestData, new HashMap<>());
        }
    }
}

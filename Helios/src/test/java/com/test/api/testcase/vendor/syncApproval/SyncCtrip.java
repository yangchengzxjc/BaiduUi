package com.test.api.testcase.vendor.syncApproval;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.basicObject.itinerary.FlightItinerary;
import com.hand.basicObject.supplierObject.syncApproval.syncCtrip.ExtendFieldList;
import com.hand.basicconstant.SupplierOID;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.ApplicationMethod.TravelApplicationPage;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.TravelApplication;
import com.test.api.method.Vendor;
import com.test.api.method.VendorMethod.SyncService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * @Author peng.zhang
 * @Date 2020/9/18
 * @Version 1.0
 **/
public class SyncCtrip extends BaseTest {

    private TravelApplication travelApplication;
    private Employee employee;
    private ExpenseReportComponent expenseReportComponent;
    private ExpenseReport expenseReport;
    private TravelApplicationPage travelApplicationPage;
    private Vendor vendor;
    private SyncService syncService;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("14082978000") String phoneNumber, @Optional("hly123456") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        travelApplication =new TravelApplication();
        expenseReportComponent = new ExpenseReportComponent();
        expenseReport =new ExpenseReport();
        travelApplicationPage =new TravelApplicationPage();
        syncService = new SyncService();
        vendor =new Vendor();
    }

    @Test(description = "消费商-携程机票TMC")
    public void vendorTest3() throws HttpStatusException {
        FormComponent component =new FormComponent();
        component.setCause("携程机票单据同步服务");
        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(UTCTime.getNowStartUtcDate());
        component.setEndDate(UTCTime.getUTCDateEnd(5));
        component.setCostCenter("成本中心NO1");
        //添加参与人员  参与人员的value 是一段json数组。
        JsonArray array = new JsonArray();
        array.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"差旅申请单-消费平台"),"懿消费商(xiao/feishang)"));
        component.setParticipant(array.toString());
        //创建申请单
        String applicationOID = travelApplication.createTravelApplication(employee,"差旅申请单-消费平台",component).get("applicationOID");
        //添加飞机行程 供应商为中集
        ArrayList<FlightItinerary> flightItineraries =new ArrayList<>();
        //单程机票无返回时间
        FlightItinerary flightItinerary=travelApplicationPage.addFlightItinerary(employee,1001, SupplierOID.CTRIP_AIR,"西安市","北京",null,UTCTime.getNowStartUtcDate());
        flightItineraries.add(flightItinerary);
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        travelApplication.submitApplication(employee,applicationOID,"");
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JsonObject filght = travelApplication.getItinerary(employee,applicationOID,"FLIGHT").get(0).getAsJsonObject();
        //获取审批单中的travelApplication
        JsonObject traveApplicationDetail = travelApplication.getApplicationDetail(employee,applicationOID);
        String CostCenter = travelApplication.getCostCenterCustom(employee,"差旅申请单-消费平台").get("costCenterCustom");
        String CostCenter1 = new JsonParser().parse(CostCenter).getAsJsonObject().getAsJsonObject("costCenter1").get("value").getAsString();
        //初始化扩展字段
        ExtendFieldList extendFieldList = syncService.setExtendFieldList("CostCenter1",CostCenter1);
        //机票初始化机票
    }
}

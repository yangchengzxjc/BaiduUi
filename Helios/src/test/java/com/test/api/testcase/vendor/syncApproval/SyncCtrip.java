package com.test.api.testcase.vendor.syncApproval;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.basicObject.itinerary.FlightItinerary;
import com.hand.basicObject.supplierObject.syncApproval.syncCtrip.CtripApprovalEntity;
import com.hand.basicObject.supplierObject.syncApproval.syncCtrip.ExtendFieldList;
import com.hand.basicObject.supplierObject.syncApproval.syncCtrip.FlightEndorsementDetail;
import com.hand.basicObject.supplierObject.syncApproval.syncCtrip.Passenger;
import com.hand.basicConstant.Supplier;
import com.hand.basicConstant.TmcChannel;
import com.hand.utils.GsonUtil;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.ApplicationMethod.TravelApplicationPage;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.TravelApplication;
import com.test.api.method.Vendor;
import com.test.api.method.VendorMethod.SyncService;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;

/**
 * @Author peng.zhang
 * @Date 2020/9/18
 * @Version 1.0
 **/
@Slf4j
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
    public void ctripTest1() throws HttpStatusException {
        FormComponent component =new FormComponent();
        component.setCause("携程机票单据同步服务");
        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(UTCTime.getNowStartUtcDate());
        component.setEndDate(UTCTime.getUTCDateEnd(5));
        component.setCostCenter("成本中心NO1");
        //添加参与人员  参与人员的value 是一段json数组。
        JsonArray array = new JsonArray();
        array.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"差旅申请单-消费平台","101"),"懿消费商(xiao/feishang)"));
        component.setParticipant(array.toString());
        //创建申请单
        String applicationOID = travelApplication.createTravelApplication(employee,"差旅申请单-消费平台",component).get("applicationOID");
        //添加飞机行程 供应商携程机票
        ArrayList<FlightItinerary> flightItineraries =new ArrayList<>();
        //单程机票无返回时间
        FlightItinerary flightItinerary=travelApplicationPage.addFlightItinerary(employee,1001, Supplier.CTRIP_AIR.getSupplierOID(),"西安市","北京",null,UTCTime.getNowStartUtcDate());
        flightItineraries.add(flightItinerary);
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        travelApplication.submitApplication(employee,applicationOID,"");
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JsonObject flight = travelApplication.getItinerary(employee,applicationOID,"FLIGHT").get(0).getAsJsonObject();
        //获取审批单中的travelApplication
        JsonObject traveApplicationDetail = travelApplication.getApplicationDetail(employee,applicationOID);
        String CostCenter = travelApplication.getCostCenterCustom(employee,"差旅申请单-消费平台").get("costCenterCustom");
        String CostCenter1 = new JsonParser().parse(CostCenter).getAsJsonObject().getAsJsonObject("costCenter1").get("value").getAsString();
        //初始化扩展字段
        JsonObject floatDay = new JsonObject();
        floatDay.addProperty("start",4);
        floatDay.addProperty("end",4);
        ExtendFieldList extendFieldList = syncService.setExtendFieldList("CostCenter1",CostCenter1);
        ArrayList<ExtendFieldList> extendFieldLists = new ArrayList<>();
        extendFieldLists.add(extendFieldList);
        //机票初始化机票
        ArrayList<Passenger> passengers =new ArrayList<>();
        passengers.add(syncService.setPassenger(employee,traveApplicationDetail.getAsJsonArray("applicationParticipants").get(0).getAsJsonObject()));
        FlightEndorsementDetail flightEndorsementDetail = syncService.setFlightEndorsementDetail(flight,floatDay,passengers);
        ArrayList<FlightEndorsementDetail> flightEndorsementDetails =new ArrayList<>();
        flightEndorsementDetails.add(flightEndorsementDetail);
        //同步实体
        CtripApprovalEntity ctripApprovalEntity = syncService.setCtripApprovalEntity(traveApplicationDetail,flight,extendFieldLists,flightEndorsementDetails,null,null);
        JsonObject syncEntityJson = new JsonParser().parse(GsonUtil.objectToString(ctripApprovalEntity)).getAsJsonObject();
        //查询tmc 同步的数据
        JsonObject ctripData = vendor.getTMCPlan(employee, TmcChannel.CTRIP.getTmcChannel(),flight.get("approvalNum").getAsString());
        log.info("查询的数据为：{}",ctripData);
        JsonObject cimccTmcRequestData = ctripData.getAsJsonObject("tmcRequest");
        JsonObject tmcResponse = ctripData.getAsJsonObject("response");
        assert GsonUtil.compareJsonObject(syncEntityJson,cimccTmcRequestData,new HashMap<>());
    }

    @Test(description = "消费商-携程机票-多参与人")
    public void ctripTest2() throws HttpStatusException {
        FormComponent component =new FormComponent();
        component.setCause("携程机票单据同步服务");
        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(UTCTime.getNowStartUtcDate());
        component.setEndDate(UTCTime.getUTCDateEnd(5));
        component.setCostCenter("成本中心NO1");
        //添加参与人员  参与人员的value 是一段json数组。
        component.setParticipant(new String []{employee.getFullName(),"曾任康"});
        //创建申请单
        String applicationOID = travelApplication.createTravelApplication(employee,"差旅申请单-消费平台",component).get("applicationOID");
        //添加飞机行程 供应商携程机票
        ArrayList<FlightItinerary> flightItineraries =new ArrayList<>();
        //单程机票无返回时间
        FlightItinerary flightItinerary=travelApplicationPage.addFlightItinerary(employee,1001, Supplier.CTRIP_AIR.getSupplierOID(),"西安市","北京",null,UTCTime.getNowStartUtcDate());
        flightItineraries.add(flightItinerary);
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        travelApplication.submitApplication(employee,applicationOID,"");
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JsonObject flight = travelApplication.getItinerary(employee,applicationOID,"FLIGHT").get(0).getAsJsonObject();
        //获取审批单中的travelApplication
        JsonObject traveApplicationDetail = travelApplication.getApplicationDetail(employee,applicationOID);
        String CostCenter = travelApplication.getCostCenterCustom(employee,"差旅申请单-消费平台").get("costCenterCustom");
        String CostCenter1 = new JsonParser().parse(CostCenter).getAsJsonObject().getAsJsonObject("costCenter1").get("value").getAsString();
        //初始化扩展字段
        JsonObject floatDay = new JsonObject();
        floatDay.addProperty("start",4);
        floatDay.addProperty("end",4);
        ExtendFieldList extendFieldList = syncService.setExtendFieldList("CostCenter1",CostCenter1);
        ArrayList<ExtendFieldList> extendFieldLists = new ArrayList<>();
        extendFieldLists.add(extendFieldList);
        //机票初始化机票
        ArrayList<Passenger> passengers =new ArrayList<>();
        passengers.add(syncService.setPassenger(employee,traveApplicationDetail.getAsJsonArray("applicationParticipants").get(0).getAsJsonObject()));
        passengers.add(syncService.setPassenger(employee,traveApplicationDetail.getAsJsonArray("applicationParticipants").get(1).getAsJsonObject()));
        FlightEndorsementDetail flightEndorsementDetail = syncService.setFlightEndorsementDetail(flight,floatDay,passengers);
        ArrayList<FlightEndorsementDetail> flightEndorsementDetails =new ArrayList<>();
        flightEndorsementDetails.add(flightEndorsementDetail);
        CtripApprovalEntity ctripApprovalEntity = syncService.setCtripApprovalEntity(traveApplicationDetail,flight,extendFieldLists,flightEndorsementDetails,null,null);
        JsonObject syncEntityJson = new JsonParser().parse(GsonUtil.objectToString(ctripApprovalEntity)).getAsJsonObject();
        log.info("封装的数据为：{}",syncEntityJson);
        //查询tmc 同步的数据
        JsonObject ctripData = vendor.getTMCPlan(employee, TmcChannel.CTRIP.getTmcChannel(),flight.get("approvalNum").getAsString());
        log.info("查询的数据为：{}",ctripData);
        JsonObject cimccTmcRequestData = ctripData.getAsJsonObject("tmcRequest");
        JsonObject tmcResponse = ctripData.getAsJsonObject("response");
        assert GsonUtil.compareJsonObject(syncEntityJson,cimccTmcRequestData,new HashMap<>());
    }
}

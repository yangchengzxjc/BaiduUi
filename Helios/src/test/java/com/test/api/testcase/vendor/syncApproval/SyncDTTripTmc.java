package com.test.api.testcase.vendor.syncApproval;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.basicObject.itinerary.FlightItinerary;
import com.hand.basicObject.supplierObject.syncApproval.syncPlatformEntity.BookClerk;
import com.hand.basicObject.supplierObject.syncApproval.syncPlatformEntity.Participant;
import com.hand.basicObject.supplierObject.syncApproval.syncPlatformEntity.SyncEntity;
import com.hand.basicObject.supplierObject.syncApproval.syncPlatformEntity.TravelFlightItinerary;
import com.hand.basicconstant.SupplierOID;
import com.hand.utils.GsonUtil;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.ApplicationMethod.TravelApplicationPage;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.TravelApplication;
import com.test.api.method.Vendor;
import com.test.api.method.VendorMethod.SyncApproval;
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
public class SyncDTTripTmc extends BaseTest {

    private TravelApplication travelApplication;
    private Employee employee;
    private ExpenseReportComponent expenseReportComponent;
    private ExpenseReport expenseReport;
    private TravelApplicationPage travelApplicationPage;
    private Vendor vendor;
    private SyncApproval syncApproval;


    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("14082978000") String phoneNumber, @Optional("hly123456") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        travelApplication =new TravelApplication();
        expenseReportComponent = new ExpenseReportComponent();
        expenseReport =new ExpenseReport();
        travelApplicationPage =new TravelApplicationPage();
        vendor =new Vendor();
        syncApproval =new SyncApproval();
    }

    @Test(description = "消费商-大唐消费商单程-国内")
    public void setSyncApprovalTest1() throws HttpStatusException {
        FormComponent component =new FormComponent();
        component.setCause("大唐TMC审批单同步数据校验");
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
        //添加飞机行程 供应商为大唐
        ArrayList<FlightItinerary> flightItineraries =new ArrayList<>();
        //单程机票无返回时间
        FlightItinerary flightItinerary=travelApplicationPage.addFlightItinerary(employee,1001, SupplierOID.dttrip,"西安市","北京",null,component.getStartDate());
        flightItineraries.add(flightItinerary);
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        travelApplication.submitApplication(employee,applicationOID,"");
        //获取大唐机票行程详情
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JsonObject filght = travelApplication.getItinerary(employee,applicationOID,"FLIGHT").get(0).getAsJsonObject();
        log.info("申请单的行程信息:{}",filght);
        //获取审批单中的travelApplication
        JsonObject traveApplicationDetail = travelApplication.getApplicationDetail(employee,applicationOID);
        BookClerk bookClerk = syncApproval.setBookClerk(employee,traveApplicationDetail.get("travelApplication").getAsJsonObject());
        //只有一个参与人
        Participant participant = syncApproval.setParticipant(employee,traveApplicationDetail.get("applicationParticipants").getAsJsonArray().get(0).getAsJsonObject());
        ArrayList<Participant> participants =new ArrayList<>();
        participants.add(participant);
        JsonObject floatDay = new JsonObject();
        floatDay.addProperty("start",4);
        floatDay.addProperty("end",4);
        TravelFlightItinerary travelFlightItinerary = syncApproval.setTravelFlight(filght,floatDay);
        ArrayList<TravelFlightItinerary> travelFlightItineraries = new ArrayList<>();
        travelFlightItineraries.add(travelFlightItinerary);
        SyncEntity syncEntity = syncApproval.setSyncEntity(bookClerk,traveApplicationDetail,filght,participants,travelFlightItineraries,null,null);
        JsonObject syncEntityJson = new JsonParser().parse(GsonUtil.objectToString(syncEntity)).getAsJsonObject();
        log.info("封装的数据为：{}",syncEntityJson);
        //查询tmc 同步的数据
        JsonObject tmcdata = vendor.getTMCPlan(employee,"supplyDttripTmcService",filght.get("approvalNum").getAsString());
        log.info("查询的数据为：{}",tmcdata);
        String tmcRequest = tmcdata.get("request").getAsString();
        String tmcResponse = tmcdata.get("response").getAsString();
        JsonObject DttripTmcRequestData = new JsonParser().parse(tmcRequest).getAsJsonObject();
        assert GsonUtil.compareJsonObject(syncEntityJson,DttripTmcRequestData,new HashMap<>());
    }


    @Test(description = "消费商-大唐消费商往返-国内")
    public void setSyncApprovalTest2() throws HttpStatusException {
        FormComponent component =new FormComponent();
        component.setCause("大唐TMC审批单同步数据校验");
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
        //添加飞机行程 供应商为大唐
        ArrayList<FlightItinerary> flightItineraries =new ArrayList<>();
        //往返行程
        FlightItinerary flightItinerary=travelApplicationPage.addFlightItinerary(employee,1002, SupplierOID.dttrip,"西安市","北京",UTCTime.getUtcStartDate(5),component.getStartDate());
        flightItineraries.add(flightItinerary);
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        travelApplication.submitApplication(employee,applicationOID,"");
        //获取大唐机票行程详情
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JsonObject filght = travelApplication.getItinerary(employee,applicationOID,"FLIGHT").get(0).getAsJsonObject();
        log.info("申请单的行程信息:{}",filght);
        //获取审批单中的travelApplication
        JsonObject traveApplicationDetail = travelApplication.getApplicationDetail(employee,applicationOID);
        BookClerk bookClerk = syncApproval.setBookClerk(employee,traveApplicationDetail.get("travelApplication").getAsJsonObject());
        //只有一个参与人
        Participant participant = syncApproval.setParticipant(employee,traveApplicationDetail.get("applicationParticipants").getAsJsonArray().get(0).getAsJsonObject());
        ArrayList<Participant> participants =new ArrayList<>();
        participants.add(participant);
        JsonObject floatDay = new JsonObject();
        floatDay.addProperty("start",4);
        floatDay.addProperty("end",4);
        TravelFlightItinerary travelFlightItinerary = syncApproval.setTravelFlight(filght,floatDay);
        ArrayList<TravelFlightItinerary> travelFlightItineraries = new ArrayList<>();
        travelFlightItineraries.add(travelFlightItinerary);
        SyncEntity syncEntity = syncApproval.setSyncEntity(bookClerk,traveApplicationDetail,filght,participants,travelFlightItineraries,null,null);
        JsonObject syncEntityJson = new JsonParser().parse(GsonUtil.objectToString(syncEntity)).getAsJsonObject();
        log.info("封装的数据为：{}",syncEntityJson);
        //查询tmc 同步的数据
        JsonObject tmcdata = vendor.getTMCPlan(employee,"supplyDttripTmcService",filght.get("approvalNum").getAsString());
        log.info("查询的数据为：{}",tmcdata);
        String tmcRequest = tmcdata.get("request").getAsString();
        String tmcResponse = tmcdata.get("response").getAsString();
        JsonObject DttripTmcRequestData = new JsonParser().parse(tmcRequest).getAsJsonObject();
        assert GsonUtil.compareJsonObject(syncEntityJson,DttripTmcRequestData,new HashMap<>());

    }
}

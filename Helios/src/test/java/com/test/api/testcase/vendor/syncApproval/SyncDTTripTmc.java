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
import com.hand.basicConstant.SupplierOID;
import com.hand.basicConstant.TmcChannel;
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
    private SyncService syncService;
    private Approve approve;

    /**
     * 申请单表单配置描述： 控件：成本中心  需在表单设置->表单管理中消费商管控中开启成本中心1， 选择系统字段 成本中心 OID
     * 行程字段全部开启
     */

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("14082978000") String phoneNumber, @Optional("hly123456") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        travelApplication =new TravelApplication();
        expenseReportComponent = new ExpenseReportComponent();
        expenseReport =new ExpenseReport();
        travelApplicationPage =new TravelApplicationPage();
        vendor =new Vendor();
        syncService =new SyncService();
        approve = new Approve();
    }


    @Test(description = "消费商-大唐消费商单程-国内")
    public void dttSyncApprovalTest1() throws HttpStatusException {
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
        FlightItinerary flightItinerary=travelApplicationPage.addFlightItinerary(employee,1001, SupplierOID.DTTRIP.getSupplierOID(),"西安市","北京",null,component.getStartDate());
        flightItineraries.add(flightItinerary);
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        travelApplication.submitApplication(employee,applicationOID,"");
        int successNum = approve.approveal(employee,applicationOID,1001);
        if(successNum !=1){
            throw new RuntimeException("审批单为通过");
        }else{
            //获取大唐机票行程详情
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            JsonObject filght = travelApplication.getItinerary(employee,applicationOID,"FLIGHT").get(0).getAsJsonObject();
            log.info("申请单的行程信息:{}",filght);
            //获取审批单中的travelApplication
            JsonObject traveApplicationDetail = travelApplication.getApplicationDetail(employee,applicationOID);
            BookClerk bookClerk = syncService.setBookClerk(employee,traveApplicationDetail.get("travelApplication").getAsJsonObject());
            //只有一个参与人
            Participant participant = syncService.setParticipant(employee,traveApplicationDetail.get("applicationParticipants").getAsJsonArray().get(0).getAsJsonObject());
            ArrayList<Participant> participants =new ArrayList<>();
            participants.add(participant);
            JsonObject floatDay = new JsonObject();
            floatDay.addProperty("start",4);
            floatDay.addProperty("end",4);
            TravelFlightItinerary travelFlightItinerary = syncService.setTravelFlight(filght,floatDay);
            ArrayList<TravelFlightItinerary> travelFlightItineraries = new ArrayList<>();
            travelFlightItineraries.add(travelFlightItinerary);
            SyncEntity syncEntity = syncService.setSyncEntity(employee,"FLIGHT",travelApplication,bookClerk,traveApplicationDetail,filght,participants,travelFlightItineraries,null,null);
            JsonObject syncEntityJson = new JsonParser().parse(GsonUtil.objectToString(syncEntity)).getAsJsonObject();
            log.info("封装的数据为：{}",syncEntityJson);
            //查询tmc 同步的数据
            JsonObject tmcdata = vendor.getTMCPlan(employee, TmcChannel.DT.getTmcChannel(),filght.get("approvalNum").getAsString());
            log.info("查询的数据为：{}",tmcdata);
            JsonObject dttripTmcRequestData = tmcdata.getAsJsonObject("tmcRequest");
            JsonObject tmcResponse = tmcdata.getAsJsonObject("response");
            assert GsonUtil.compareJsonObject(syncEntityJson,dttripTmcRequestData,new HashMap<>());
        }
    }

    @Test(description = "消费商-大唐消费商往返-国内")
    public void dttSyncApprovalTest2() throws HttpStatusException {
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
        FlightItinerary flightItinerary=travelApplicationPage.addFlightItinerary(employee,1002, SupplierOID.DTTRIP.getSupplierOID(),"西安市","北京",UTCTime.utcToBJDate(component.getEndDate(),-1)+"T16:00:00Z",component.getStartDate());
        flightItineraries.add(flightItinerary);
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        travelApplication.submitApplication(employee,applicationOID,"");
        int successNum = approve.approveal(employee,applicationOID,1001);
        if(successNum != 1){
            throw new RuntimeException("审批单未通过");
        }else{
            //获取大唐机票行程详情
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            JsonObject filght = travelApplication.getItinerary(employee,applicationOID,"FLIGHT").get(0).getAsJsonObject();
            log.info("申请单的行程信息:{}",filght);
            //获取审批单中的travelApplication
            JsonObject traveApplicationDetail = travelApplication.getApplicationDetail(employee,applicationOID);
            BookClerk bookClerk = syncService.setBookClerk(employee,traveApplicationDetail.get("travelApplication").getAsJsonObject());
            //只有一个参与人
            Participant participant = syncService.setParticipant(employee,traveApplicationDetail.get("applicationParticipants").getAsJsonArray().get(0).getAsJsonObject());
            ArrayList<Participant> participants =new ArrayList<>();
            participants.add(participant);
            JsonObject floatDay = new JsonObject();
            floatDay.addProperty("start",4);
            floatDay.addProperty("end",4);
            TravelFlightItinerary travelFlightItinerary = syncService.setTravelFlight(filght,floatDay);
            ArrayList<TravelFlightItinerary> travelFlightItineraries = new ArrayList<>();
            travelFlightItineraries.add(travelFlightItinerary);
            SyncEntity syncEntity = syncService.setSyncEntity(employee,"FLIGHT",travelApplication,bookClerk,traveApplicationDetail,filght,participants,travelFlightItineraries,null,null);
            JsonObject syncEntityJson = new JsonParser().parse(GsonUtil.objectToString(syncEntity)).getAsJsonObject();
            log.info("封装的数据为：{}",syncEntityJson);
            //查询tmc 同步的数据
            JsonObject tmcdata = vendor.getTMCPlan(employee,TmcChannel.DT.getTmcChannel(),filght.get("approvalNum").getAsString());
            log.info("查询的数据为：{}",tmcdata);
            JsonObject dttripTmcRequestData = tmcdata.getAsJsonObject("tmcRequest");
            JsonObject tmcResponse = tmcdata.getAsJsonObject("response");
            assert GsonUtil.compareJsonObject(syncEntityJson,dttripTmcRequestData,new HashMap<>());
        }

    }


    @Test(description = "消费商-大唐消费商单程-国内-参与人为多人")
    public void dttSyncApprovalTest3() throws HttpStatusException {
        FormComponent component =new FormComponent();
        component.setCause("大唐TMC审批单同步数据校验");
        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(UTCTime.getNowStartUtcDate());
        component.setEndDate(UTCTime.getUTCDateEnd(5));
        component.setCostCenter("成本中心NO1");
        //添加参与人员  参与人员的value 是一段json数组。
        JsonArray array = new JsonArray();
        array.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"差旅申请单-消费平台"),"曾任康"));
        array.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"差旅申请单-消费平台"),"S0001"));
        component.setParticipant(array.toString());
        //创建申请单
        String applicationOID = travelApplication.createTravelApplication(employee,"差旅申请单-消费平台",component).get("applicationOID");
        //添加飞机行程 供应商为大唐
        ArrayList<FlightItinerary> flightItineraries =new ArrayList<>();
        //单程机票无返回时间
        FlightItinerary flightItinerary=travelApplicationPage.addFlightItinerary(employee,1001,SupplierOID.DTTRIP.getSupplierOID(),"西安市","北京",null,component.getStartDate());
        flightItineraries.add(flightItinerary);
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        travelApplication.submitApplication(employee,applicationOID,"");
        int successNum = approve.approveal(employee,applicationOID,1001);
        if(successNum !=1){
            throw new RuntimeException("审批单未通过");
        }else{
            //获取大唐机票行程详情
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            JsonObject filght = travelApplication.getItinerary(employee,applicationOID,"FLIGHT").get(0).getAsJsonObject();
            log.info("申请单的行程信息:{}",filght);
            //获取审批单中的travelApplication
            JsonObject traveApplicationDetail = travelApplication.getApplicationDetail(employee,applicationOID);
            BookClerk bookClerk = syncService.setBookClerk(employee,traveApplicationDetail.get("travelApplication").getAsJsonObject());
            //多个参与人
            Participant participant1 = syncService.setParticipant(employee,traveApplicationDetail.get("applicationParticipants").getAsJsonArray().get(0).getAsJsonObject());
            Participant participant2 = syncService.setParticipant(employee,traveApplicationDetail.get("applicationParticipants").getAsJsonArray().get(1).getAsJsonObject());
            ArrayList<Participant> participants =new ArrayList<>();
            participants.add(participant1);
            participants.add(participant2);
            JsonObject floatDay = new JsonObject();
            floatDay.addProperty("start",4);
            floatDay.addProperty("end",4);
            TravelFlightItinerary travelFlightItinerary = syncService.setTravelFlight(filght,floatDay);
            ArrayList<TravelFlightItinerary> travelFlightItineraries = new ArrayList<>();
            travelFlightItineraries.add(travelFlightItinerary);
            SyncEntity syncEntity = syncService.setSyncEntity(employee,"FLIGHT",travelApplication,bookClerk,traveApplicationDetail,filght,participants,travelFlightItineraries,null,null);
            JsonObject syncEntityJson = new JsonParser().parse(GsonUtil.objectToString(syncEntity)).getAsJsonObject();
            log.info("封装的数据为：{}",syncEntityJson);
            //查询tmc 同步的数据
            JsonObject tmcdata = vendor.getTMCPlan(employee, TmcChannel.DT.getTmcChannel(),filght.get("approvalNum").getAsString());
            log.info("查询的数据为：{}",tmcdata);
            JsonObject dttripTmcRequestData = tmcdata.getAsJsonObject("tmcRequest");
            JsonObject tmcResponse = tmcdata.getAsJsonObject("response");
            assert GsonUtil.compareJsonObject(syncEntityJson,dttripTmcRequestData,new HashMap<>());
        }

    }
}

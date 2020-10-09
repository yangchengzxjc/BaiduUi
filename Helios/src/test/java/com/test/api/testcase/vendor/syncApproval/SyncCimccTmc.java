package com.test.api.testcase.vendor.syncApproval;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.basicObject.itinerary.FlightItinerary;
import com.hand.basicObject.itinerary.HotelItinerary;
import com.hand.basicObject.itinerary.TrainItinerary;
import com.hand.basicObject.supplierObject.syncApproval.syncPlatformEntity.*;
import com.hand.basicconstant.SupplierOID;
import com.hand.basicconstant.TmcChannel;
import com.hand.utils.GsonUtil;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.*;
import com.test.api.method.ApplicationMethod.TravelApplicationPage;
import com.test.api.method.VendorMethod.SyncService;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;

/**
 * @Author peng.zhang
 * @Date 2020/9/24
 * @Version 1.0
 **/
@Slf4j
public class SyncCimccTmc extends BaseTest {

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
     *
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
        approve =new Approve();
    }

    @DataProvider(name = "TMC")
    public Object[][] tmcData() {
        return new Object[][]{
                {TmcChannel.CIMCC.getTmcChannel(),TmcChannel.CIMCC.getSupplierOID()},
        };
    }

    @Test(description = "审批单同步-TMC-机票票单程-国内",dataProvider = "TMC")
    public void cimccSyncApprovalTest1(String tmcChannel,String supplierOID) throws HttpStatusException, InterruptedException {
        FormComponent component =new FormComponent();
        component.setCause(tmcChannel+"审批单同步数据校验");
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
        //添加飞机行程 供应商中集
        ArrayList<FlightItinerary> flightItineraries =new ArrayList<>();
        //单程机票无返回时间
        FlightItinerary flightItinerary=travelApplicationPage.addFlightItinerary(employee,1001, supplierOID,"西安市","北京",null,component.getStartDate());
        flightItineraries.add(flightItinerary);
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        travelApplication.submitApplication(employee,applicationOID,"");
        //审批单审批
        if(approve.approveal(employee,applicationOID,1001)!=1){
            throw new RuntimeException("审批单审批失败");
        }else{
            sleep(1000);
            //获取大唐机票行程详情
            JsonObject filght = travelApplication.getItinerary(employee,applicationOID,"FLIGHT").get(0).getAsJsonObject();
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
            JsonObject tmcdata = vendor.getTMCPlan(employee, tmcChannel,filght.get("approvalNum").getAsString());
            log.info("查询的数据为：{}",tmcdata);
            JsonObject cimccTmcRequestData = tmcdata.getAsJsonObject("tmcRequest");
            JsonObject tmcResponse = tmcdata.getAsJsonObject("response");
            assert GsonUtil.compareJsonObject(syncEntityJson,cimccTmcRequestData,new HashMap<>());
        }

    }

    @Test(description = "审批单同步-中集TMC-火车行程",dataProvider = "TMC")
    public void cimccSyncApprovalTest2(String tmcChannel,String supplierOID) throws HttpStatusException, InterruptedException {
        FormComponent component =new FormComponent();
        component.setCause(tmcChannel+"火车供应商同步测试");
        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(UTCTime.getNowStartUtcDate());
        component.setEndDate(UTCTime.getUTCDateEnd(7));
        component.setCostCenter("成本中心NO1");
        //添加参与人员  参与人员的value 是一段json数组。
        JsonArray array = new JsonArray();
        array.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"差旅申请单-消费平台"),"懿消费商(xiao/feishang)"));
        component.setParticipant(array.toString());
        //创建申请单
        String applicationOID = travelApplication.createTravelApplication(employee,"差旅申请单-消费平台",component).get("applicationOID");
        //初始化火车行程
        ArrayList<TrainItinerary> trainItineraries =new ArrayList<>();
        TrainItinerary trainItinerary = travelApplicationPage.setTrainItinerary(employee,"深圳","上海",component.getStartDate(),supplierOID,"二等座");
        trainItineraries.add(trainItinerary);
        // 添加行程
        travelApplication.addItinerary(employee,applicationOID,trainItineraries);
        //申请单提交
        travelApplication.submitApplication(employee,applicationOID,"");
        int successNum = approve.approveal(employee,applicationOID,1001);
        if(successNum!=1){
            throw new RuntimeException("审批单审批失败");
        }else{
            sleep(1000);
            JsonObject train = travelApplication.getItinerary(employee,applicationOID,"TRAIN").get(0).getAsJsonObject();
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
            TravelTrainItinerary travelTrainItinerary = syncService.setTravelTrainItinerary(train,floatDay);
            ArrayList<TravelTrainItinerary> travelTrainItineraries =new ArrayList<>();
            travelTrainItineraries.add(travelTrainItinerary);
            SyncEntity syncEntity = syncService.setSyncEntity(employee,"TRAIN",travelApplication,bookClerk,traveApplicationDetail,train,participants,null,null,travelTrainItineraries);
            JsonObject syncEntityJson = new JsonParser().parse(GsonUtil.objectToString(syncEntity)).getAsJsonObject();
            log.info("封装的数据为：{}",syncEntityJson);
            //查询tmc 同步的数据
            JsonObject tmcdata = vendor.getTMCPlan(employee,tmcChannel,train.get("approvalNum").getAsString());
            log.info("查询的数据为：{}",tmcdata);
            JsonObject cimccTmcRequestData = tmcdata.getAsJsonObject("tmcRequest");
            JsonObject tmcResponse = tmcdata.getAsJsonObject("response");
            assert GsonUtil.compareJsonObject(syncEntityJson,cimccTmcRequestData,new HashMap<>());
        }
    }

    @Test(description = "审批单同步-中集TMC-酒店行程",dataProvider = "TMC")
    public void cimccSyncApprovalTest3(String tmcChannel,String supplierOID) throws HttpStatusException, InterruptedException {
        FormComponent component =new FormComponent();
        component.setCause(tmcChannel+"供应商酒店同步测试");
        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(UTCTime.getNowStartUtcDate());
        component.setEndDate(UTCTime.getUTCDateEnd(5));
        component.setCostCenter("成本中心NO1");
        //添加参与人员  参与人员的value 是一段json数组。
        JsonArray array = new JsonArray();
        array.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"差旅申请单-消费平台"),"懿消费商(xiao/feishang)"));
        component.setParticipant(array.toString());
//        创建申请单
        String applicationOID = travelApplication.createTravelApplication(employee,"差旅申请单-消费平台",component).get("applicationOID");
        //初始化酒店行程  中集
        ArrayList<HotelItinerary> hotelItineraries =new ArrayList<>();
        //酒店行程  开始日期要跟申请单的表头一样
        HotelItinerary hotelItinerary =new HotelItinerary("北京",1,component.getStartDate(),UTCTime.utcToBJDate(component.getEndDate(),-1)+"T16:00:00Z",supplierOID,expenseReportComponent.getCityCode(employee,"北京"),new BigDecimal(400).setScale(1));
        hotelItineraries.add(hotelItinerary);
        // 添加行程
        travelApplication.addItinerary(employee,applicationOID,hotelItineraries);
        //申请单提交
        travelApplication.submitApplication(employee,applicationOID,"");
        int successNum = approve.approveal(employee,applicationOID,1001);
        if(successNum !=1){
            throw new RuntimeException("审批单审批失败");
        }else{
            sleep(1000);
            JsonObject hotel = travelApplication.getItinerary(employee,applicationOID,"HOTEL").get(0).getAsJsonObject();
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
            TravelHotelItinerary travelHotelItinerary = syncService.setTravelHotelItinerary(hotel,floatDay);
            ArrayList<TravelHotelItinerary> travelHotelItineraries =new ArrayList<>();
            travelHotelItineraries.add(travelHotelItinerary);
            SyncEntity syncEntity = syncService.setSyncEntity(employee,"HOTEL",travelApplication,bookClerk,traveApplicationDetail,hotel,participants,null,travelHotelItineraries,null);
            JsonObject syncEntityJson = new JsonParser().parse(GsonUtil.objectToString(syncEntity)).getAsJsonObject();
            log.info("封装的数据为：{}",syncEntityJson);
            //查询tmc 同步的数据
            JsonObject tmcdata = vendor.getTMCPlan(employee,tmcChannel,hotel.get("approvalNumber").getAsString());
            log.info("查询的数据为：{}",tmcdata);
            JsonObject cimccTmcRequestData = tmcdata.getAsJsonObject("tmcRequest");
            JsonObject tmcResponse = tmcdata.getAsJsonObject("response");
            assert GsonUtil.compareJsonObject(syncEntityJson,cimccTmcRequestData,new HashMap<>());
        }
    }

    @Test(description = "审批单同步-中集TMC-机票票往返-国内",dataProvider = "TMC")
    public void cimccSyncApprovalTest4(String tmcChannel,String supplierOID) throws HttpStatusException {
        FormComponent component =new FormComponent();
        component.setCause(tmcChannel+"TMC审批单同步数据校验-往返");
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
        //添加飞机行程 供应商中集
        ArrayList<FlightItinerary> flightItineraries =new ArrayList<>();
        //单程机票无返回时间
        FlightItinerary flightItinerary=travelApplicationPage.addFlightItinerary(employee,1002, supplierOID,"西安市","北京",UTCTime.utcToBJDate(component.getEndDate(),-1)+"T16:00:00Z",component.getStartDate());
        flightItineraries.add(flightItinerary);
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        travelApplication.submitApplication(employee,applicationOID,"");
        int successNum = approve.approveal(employee,applicationOID,1001);
        if(successNum != 1){
            throw new RuntimeException("审批单未审批通过");
        }else{
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            JsonObject filght = travelApplication.getItinerary(employee,applicationOID,"FLIGHT").get(0).getAsJsonObject();
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
            JsonObject tmcdata = vendor.getTMCPlan(employee,tmcChannel,filght.get("approvalNum").getAsString());
            log.info("查询的数据为：{}",tmcdata);
            JsonObject cimccTmcRequestData = tmcdata.getAsJsonObject("tmcRequest");
            JsonObject tmcResponse = tmcdata.getAsJsonObject("response");
            assert GsonUtil.compareJsonObject(syncEntityJson,cimccTmcRequestData,new HashMap<>());
        }

    }

    @Test(description = "审批单同步-TMC-机票票单程-火车-酒店在一个申请单",dataProvider = "TMC")
    public void cimccSyncApprovalTest5(String tmcChannel,String supplierOID) throws HttpStatusException {
        FormComponent component =new FormComponent();
        component.setCause(tmcChannel+"TMC审批单同步数据校验");
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
        //添加飞机行程 供应商中集
        ArrayList<FlightItinerary> flightItineraries =new ArrayList<>();
        FlightItinerary flightItinerary=travelApplicationPage.addFlightItinerary(employee,1001,supplierOID,"西安市","北京",null,component.getStartDate());
        flightItineraries.add(flightItinerary);
        //初始化酒店行程  中集
        ArrayList<HotelItinerary> hotelItineraries =new ArrayList<>();
        //酒店行程  开始日期要跟申请单的表头一样
        HotelItinerary hotelItinerary =new HotelItinerary("北京",1,component.getStartDate(),UTCTime.utcToBJDate(component.getEndDate(),-1)+"T16:00:00Z",supplierOID,expenseReportComponent.getCityCode(employee,"北京"),new BigDecimal(400).setScale(1));
        hotelItineraries.add(hotelItinerary);
        //初始化火车行程
        ArrayList<TrainItinerary> trainItineraries =new ArrayList<>();
        TrainItinerary trainItinerary = travelApplicationPage.setTrainItinerary(employee,"南京市","上海",component.getStartDate(),supplierOID,"二等座");
        trainItineraries.add(trainItinerary);
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        travelApplication.addItinerary(employee,applicationOID,hotelItineraries);
        travelApplication.addItinerary(employee,applicationOID,trainItineraries);
        travelApplication.submitApplication(employee,applicationOID,"");
        int successNum = approve.approveal(employee,applicationOID,1001);
        if(successNum != 1){
            throw new RuntimeException("审批单未通过");
        }else{
            //获取行程详情
            try{
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            JsonObject filght = travelApplication.getItinerary(employee,applicationOID,"FLIGHT").get(0).getAsJsonObject();
            JsonObject hotel = travelApplication.getItinerary(employee,applicationOID,"HOTEL").get(0).getAsJsonObject();
            JsonObject train = travelApplication.getItinerary(employee,applicationOID,"TRAIN").get(0).getAsJsonObject();
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
            //飞机行程
            TravelFlightItinerary travelFlightItinerary = syncService.setTravelFlight(filght,floatDay);
            ArrayList<TravelFlightItinerary> travelFlightItineraries = new ArrayList<>();
            travelFlightItineraries.add(travelFlightItinerary);
            //酒店行程
            TravelHotelItinerary travelHotelItinerary = syncService.setTravelHotelItinerary(hotel,floatDay);
            ArrayList<TravelHotelItinerary> travelHotelItineraries = new ArrayList<>();
            travelHotelItineraries.add(travelHotelItinerary);
            //火车行程
            TravelTrainItinerary travelTrainItinerary = syncService.setTravelTrainItinerary(train,floatDay);
            ArrayList<TravelTrainItinerary> travelTrainItineraries =new ArrayList<>();
            travelTrainItineraries.add(travelTrainItinerary);
            SyncEntity syncEntityFlight = syncService.setSyncEntity(employee,"FLIGHT",travelApplication,bookClerk,traveApplicationDetail,filght,participants,travelFlightItineraries,null,null);
            SyncEntity syncEntityHotel = syncService.setSyncEntity(employee,"HOTEL",travelApplication,bookClerk,traveApplicationDetail,hotel,participants,null,travelHotelItineraries,null);
            SyncEntity syncEntityTrain = syncService.setSyncEntity(employee,"TRAIN",travelApplication,bookClerk,traveApplicationDetail,train,participants,null,null,travelTrainItineraries);
            JsonObject syncEntityFlightJson = new JsonParser().parse(GsonUtil.objectToString(syncEntityFlight)).getAsJsonObject();
            JsonObject syncEntityTrainJson = new JsonParser().parse(GsonUtil.objectToString(syncEntityTrain)).getAsJsonObject();
            JsonObject syncEntityHotelJson = new JsonParser().parse(GsonUtil.objectToString(syncEntityHotel)).getAsJsonObject();
            log.info("封装的数据为：{}",syncEntityFlightJson);
            //查询tmc 同步的数据
            JsonObject flightTmcdata = vendor.getTMCPlan(employee,tmcChannel,filght.get("approvalNum").getAsString());
            JsonObject trainTmcdata = vendor.getTMCPlan(employee, tmcChannel,train.get("approvalNum").getAsString());
            JsonObject hotelTmcdata = vendor.getTMCPlan(employee, tmcChannel,hotel.get("approvalNumber").getAsString());
            log.info("查询的数据为：{}",flightTmcdata);
            JsonObject cimccTmcRequestData1 = flightTmcdata.getAsJsonObject("tmcRequest");
            JsonObject tmcResponse = flightTmcdata.getAsJsonObject("response");
            assert GsonUtil.compareJsonObject(syncEntityFlightJson,cimccTmcRequestData1,new HashMap<>());
            assert GsonUtil.compareJsonObject(syncEntityTrainJson,trainTmcdata.getAsJsonObject("tmcRequest"),new HashMap<>());
            assert GsonUtil.compareJsonObject(syncEntityHotelJson,hotelTmcdata.getAsJsonObject("tmcRequest"),new HashMap<>());
        }

    }


    @Test(description = "审批单同步-中集TMC-机票票单程-多参与人-统一定票",dataProvider = "TMC")
    public void cimccSyncApprovalTest6(String tmcChannel,String supplierOID) throws HttpStatusException {
        FormComponent component =new FormComponent();
        component.setCause(tmcChannel+"TMC审批单同步数据校验");
        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(UTCTime.getNowStartUtcDate());
        component.setEndDate(UTCTime.getUTCDateEnd(5));
        component.setCostCenter("成本中心NO1");
        //添加参与人员  参与人员的value 是一段json数组。
        JsonArray array = new JsonArray();
        array.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"差旅申请单-消费平台"),"懿消费商(xiao/feishang)"));
        array.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"差旅申请单-消费平台"),"曾任康"));
        component.setParticipant(array.toString());
        //创建申请单
        String applicationOID = travelApplication.createTravelApplication(employee,"差旅申请单-消费平台",component).get("applicationOID");
        //添加飞机行程 供应商中集
        ArrayList<FlightItinerary> flightItineraries =new ArrayList<>();
        //单程机票无返回时间
        FlightItinerary flightItinerary=travelApplicationPage.addFlightItinerary(employee,1001,supplierOID,"西安市","北京",null,component.getStartDate());
        flightItineraries.add(flightItinerary);
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        travelApplication.submitApplication(employee,applicationOID,"");
        int successNum = approve.approveal(employee,applicationOID,1001);
        if(successNum !=1){
            throw new RuntimeException("审批单未通过");
        }else{
            //获取大唐机票行程详情
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            JsonObject filght = travelApplication.getItinerary(employee,applicationOID,"FLIGHT").get(0).getAsJsonObject();
            //获取审批单中的travelApplication
            JsonObject traveApplicationDetail = travelApplication.getApplicationDetail(employee,applicationOID);
            BookClerk bookClerk = syncService.setBookClerk(employee,traveApplicationDetail.get("travelApplication").getAsJsonObject());
            //只有一个参与人
            Participant participant2 = syncService.setParticipant(employee,traveApplicationDetail.get("applicationParticipants").getAsJsonArray().get(1).getAsJsonObject());
            Participant participant1 = syncService.setParticipant(employee,traveApplicationDetail.get("applicationParticipants").getAsJsonArray().get(0).getAsJsonObject());
            ArrayList<Participant> participants =new ArrayList<>();
            participants.add(participant2);
            participants.add(participant1);
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
            JsonObject tmcdata = vendor.getTMCPlan(employee,tmcChannel,filght.get("approvalNum").getAsString());
            log.info("查询的数据为：{}",tmcdata);
            JsonObject cimccTmcRequestData = tmcdata.getAsJsonObject("tmcRequest");
            JsonObject tmcResponse = tmcdata.getAsJsonObject("response");
            assert GsonUtil.compareJsonObject(syncEntityJson,cimccTmcRequestData,new HashMap<>());
        }
    }
}

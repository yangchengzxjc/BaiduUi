package com.test.api.testcase.vendor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.basicObject.itinerary.FlightItinerary;
import com.hand.basicObject.itinerary.HotelItinerary;
import com.hand.basicObject.itinerary.TrainItinerary;
import com.hand.basicObject.supplierObject.SettlementBody;
import com.hand.basicObject.supplierObject.TrainOrderInfo.TrainBaseOrder;
import com.hand.basicObject.supplierObject.TrainOrderInfo.TrainPassengerInfo;
import com.hand.basicObject.supplierObject.TrainOrderInfo.TrainSequenceInfo;
import com.hand.basicObject.supplierObject.TrainOrderInfo.TrainTicketInfo;
import com.hand.basicObject.supplierObject.airOrderInfo.*;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelBaseOrder;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelOrderInfoEntity;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelPassengerInfo;
import com.hand.basicObject.supplierObject.syncApproval.syncPlatformEntity.*;
import com.hand.basicconstant.TmcChannel;
import com.hand.utils.GsonUtil;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.*;
import com.test.api.method.ApplicationMethod.TravelApplicationPage;
import com.test.api.method.VendorMethod.FlightOrder;
import com.test.api.method.VendorMethod.HotelOrder;
import com.test.api.method.VendorMethod.SyncService;
import com.test.api.method.VendorMethod.TrainOrder;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;

/**
 * @Author peng.zhang
 * @Date 2020/10/10
 * @Version 1.0
 **/
@Slf4j
public class SyncPlanWorkFlowTest extends BaseTest {

    private TravelApplication travelApplication;
    private Employee employee;
    private ExpenseReportComponent expenseReportComponent;
    private ExpenseReport expenseReport;
    private TravelApplicationPage travelApplicationPage;
    private Vendor vendor;
    private SyncService syncService;
    private Approve approve;
    private InfraStructure infraStructure;
    private FlightOrder flightOrder;
    private HotelOrder hotelOrder;
    private TrainOrder trainOrder;

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
        infraStructure =new InfraStructure();
        flightOrder =new FlightOrder();
        hotelOrder = new HotelOrder();
        trainOrder = new TrainOrder();
    }

    @DataProvider(name = "TMC")
    public Object[][] tmcData() {
        return new Object[][]{
                {TmcChannel.CIMCC.getTmcChannel(),TmcChannel.CIMCC.getSupplierOID(),TmcChannel.CIMCC.getSupplierName(),TmcChannel.CIMCC.getSupplierCode()},

        };
    }

    @Test(description = "机票同步服务工作流",dataProvider = "TMC")
    public void workFlowTest1(String tmcChannel,String supplierOID,String supplierName,String supplierCode) throws HttpStatusException, InterruptedException {
        FormComponent component =new FormComponent();
        component.setCause(tmcChannel+"审批单同步数据校验");
        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(UTCTime.getUtcStartDate(1));
        component.setEndDate(UTCTime.getUTCDateEnd(7));
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
            //获取机票行程详情
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
            JsonObject tmcRequestData = tmcdata.getAsJsonObject("tmcRequest");
            JsonObject tmcResponse = tmcdata.getAsJsonObject("response");
            //订单号
            String orderNo = RandomNumber.getTimeNumber();
            ArrayList<String> bookerDepartments =new ArrayList<>();
            bookerDepartments.add(traveApplicationDetail.getAsJsonObject("applicant").get("departmentName").getAsString());
            //机票价格
            BigDecimal ticketPrice =new BigDecimal(1000).setScale(2);
            //燃油费
            BigDecimal oilFee = new BigDecimal(50).setScale(2);
            //基建费
            BigDecimal tax =new BigDecimal(20).setScale(2);
            //服务费
            BigDecimal serverFee = new BigDecimal(20).setScale(2);
            //电子客票号
            String ticketKey = RandomNumber.getTimeNumber(10);
            bookerDepartments.add(employee.getDepartmentName());
            //票号
            String ticketNo = RandomNumber.getTimeNumber(13);
            BigDecimal amount = ticketPrice.add(oilFee).add(tax).add(serverFee);
            //订单基本数据
            AirBaseOrder airBaseOrder = flightOrder.setAirBaseOrder(employee,"B",orderNo,supplierName,supplierCode,tmcRequestData,traveApplicationDetail.getAsJsonObject("applicant"),amount);
            // 机票信息
            AirTicketInfo airTicketInfo = flightOrder.setAirTicketInfo(ticketKey,"1",ticketNo,null,"已使用",ticketPrice,oilFee,tax,serverFee);
            ArrayList<AirTicketInfo> airTicketInfos =new ArrayList<>();
            airTicketInfos.add(airTicketInfo);
            //航程信息
            AirFlightInfo airFlightInfo = flightOrder.setAirFlightInfo(orderNo,tmcRequestData.getAsJsonArray("travelFlightsList").get(0).getAsJsonObject());
            ArrayList<AirFlightInfo> airFlightInfos =new ArrayList<>();
            airFlightInfos.add(airFlightInfo);
            //乘机人信息
            //审批单中的乘客信息
            AirPassengerInfo airPassengerInfo = flightOrder.setAirPassengerInfo(employee,orderNo,"1",tmcRequestData,traveApplicationDetail.getAsJsonArray("applicationParticipants").get(0).getAsJsonObject());
            ArrayList<AirPassengerInfo> airPassengerInfos = new ArrayList<>();
            airPassengerInfos.add((airPassengerInfo));
            //行程单打印以及配送信息
            AirTicketPrint airTicketPrint = flightOrder.setAirTicketPrint(ticketKey,ticketNo,tmcRequestData.getAsJsonArray("participantList").get(0).getAsJsonObject().get("name").getAsString());
            ArrayList<AirTicketPrint> airTicketPrints =new ArrayList<>();
            airTicketPrints.add(airTicketPrint);
            //保险信息
            AirInsurance airInsurance = flightOrder.setAirInsurance(ticketKey,"1",1);
            ArrayList<AirInsurance> airInsurances =new ArrayList<>();
            airInsurances.add(airInsurance);
            //机票订单数据封装
            AirOrderInfoEntity airOrderInfoEntity =AirOrderInfoEntity.builder()
                    .airBaseOrder(airBaseOrder)
                    .airTicketInfo(airTicketInfos)
                    .airFlightInfo(airFlightInfos)
                    .airPassengerInfo(airPassengerInfos)
                    .airTicketPrint(airTicketPrints)
                    .airInsurance(airInsurances)
                    .build();
            //推送的数据封装成一个json字符串
            String hotelOrderData =GsonUtil.objectToString(airOrderInfoEntity);
            //转成jsonobject对象
            JsonObject flightOrderDataObject =new JsonParser().parse(hotelOrderData).getAsJsonObject();
            //订单推送
            vendor.pushOrderData(employee,"flight",airOrderInfoEntity,"cimccTMC","200428140254184788","");
            SettlementBody settlementBody = SettlementBody.builder()
                    .companyOid(employee.getCompanyOID())
                    .orderNo(orderNo)
                    .page(1)
                    .size(10)
                    .build();
            //查询订单数据
            JsonObject  flightOrderData = vendor.queryOrderData(employee,"flight",settlementBody);
            log.info("flight order Data:{}",flightOrderData);
            //先对比需要删除的数据
            assert flightOrderData.getAsJsonObject("airBaseOrder").get("flightWay").getAsString().equals(flightOrderDataObject.getAsJsonObject("airBaseOrder").get("flightWay").getAsString());
            assert flightOrderData.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().get("isPolicy").getAsString().equals(flightOrderDataObject.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().get("isPolicy").getAsString());
            assert flightOrderData.getAsJsonArray("airFlightInfo").get(0).getAsJsonObject().get("tpm").getAsString().equals(flightOrderDataObject.getAsJsonArray("airFlightInfo").get(0).getAsJsonObject().get("rpm").getAsString());
            //先删除航程类型字段 因为单程映射会重复 删除完后单独比较   协议价  里程数
            flightOrderDataObject.getAsJsonObject("airBaseOrder").remove("flightWay");
            flightOrderDataObject.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().remove("isPolicy");
            flightOrderDataObject.getAsJsonArray("airFlightInfo").get(0).getAsJsonObject().remove("tpm");
            //不需要检查的字段为 机票保险中的ticketKey
            flightOrderDataObject.getAsJsonArray("airInsurance").get(0).getAsJsonObject().remove("ticketKey");
            //映射数据
            HashMap<String,String> mapping =new HashMap<>();
            mapping.put("S","BS");
            mapping.put("N","国内航班");
            mapping.put("yClassStandardPrice","yclassStandardPrice");
            mapping.put("flight","flightNo");
            mapping.put("employeeId","preEmployeeId");
            mapping.put(employee.getDepartmentName(),"产品三部");
            assert GsonUtil.compareJsonObject(flightOrderDataObject,flightOrderData,mapping);
            //对比预订人的oid 推送数据未推送此字段单独来比较
            assert flightOrderData.getAsJsonObject("airBaseOrder").get("preEmployeeOid").getAsString().equals(employee.getUserOID());
            //结算数据对比
        }
    }

    @Test(description = "审批单同步-中集TMC-酒店行程",dataProvider = "TMC")
    public void workFlowTest2(String tmcChannel,String supplierOID,String supplierName,String supplierCode) throws HttpStatusException, InterruptedException {
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
            JsonObject tmcRequestData = tmcdata.getAsJsonObject("tmcRequest");
            JsonObject tmcResponse = tmcdata.getAsJsonObject("response");
            //酒店订单基本信息
            ArrayList<String> bookerDepartments =new ArrayList<>();
            bookerDepartments.add(traveApplicationDetail.getAsJsonObject("applicant").get("departmentName").getAsString());
            //订单号
            String orderNo = RandomNumber.getTimeNumber();
            HotelBaseOrder hotelBaseOrder =hotelOrder.setHotelBaseOrder(employee,"B",orderNo,supplierName,supplierCode,tmcRequestData,traveApplicationDetail.getAsJsonObject("applicant"));
            //乘客信息
            JsonObject participantObject = traveApplicationDetail.getAsJsonArray("applicationParticipants").get(0).getAsJsonObject();
            HotelPassengerInfo hotelPassengerInfo =hotelOrder.setHotelPassengerInfo(employee,orderNo,"1",tmcRequestData,participantObject);
            ArrayList<HotelPassengerInfo> hotelPassengerInfos =new ArrayList<>();
            hotelPassengerInfos.add(hotelPassengerInfo);
            HotelOrderInfoEntity hotelOrderInfoEntity = HotelOrderInfoEntity.builder()
                    .hotelOrderBase(hotelBaseOrder)
                    .hotelOrderPassengerInfos(hotelPassengerInfos)
                    .build();
            //推送的数据封装成一个json字符串
            String hotelOrderData = GsonUtil.objectToString(hotelOrderInfoEntity);
            //转成jsonobject对象
            JsonObject hotelOrderDataObject =new JsonParser().parse(hotelOrderData).getAsJsonObject();
            //订单推送
            vendor.pushOrderData(employee,"hotel",hotelOrderInfoEntity,"cimccTMC","200428140254184788","");
            SettlementBody settlementBody = SettlementBody.builder()
                    .companyOid(employee.getCompanyOID())
                    .orderNo(orderNo)
                    .page(1)
                    .size(10)
                    .build();
            //查询订单数据
            JsonObject  hotelOrder = vendor.queryOrderData(employee,"hotel",settlementBody);
            log.info("hotel order Data:{}",hotelOrder);
            //进行数据对比
            HashMap<String,String> mapping =new HashMap<>();
            mapping.put("hotelOrderPassengerInfos","hotelPassengerInfo");
            mapping.put("employeeId","preEmployeeId");
            mapping.put("hotelOrderBase","hotelBaseOrder");
            //进行部门对比   部门可能开启全路径 映射的话不好处理也不好对比  这块单独对比
            assert hotelOrderDataObject.getAsJsonObject("hotelOrderBase").getAsJsonArray("bookerDepartments").contains(hotelOrder.getAsJsonObject("hotelOrderBase").getAsJsonArray("bookerDepartments"));
            assert hotelOrderDataObject.getAsJsonObject("hotelOrderBase").get("departmentName").getAsString().contains(hotelOrder.getAsJsonObject("hotelOrderBase").get("departmentName").getAsString());
            assert hotelOrderDataObject.getAsJsonArray("hotelOrderPassengerInfos").get(0).getAsJsonObject().get("departmentName").getAsString().contains(hotelOrder.getAsJsonArray("hotelOrderPassengerInfos").get(0).getAsJsonObject().get("departmentName").getAsString());
            assert hotelOrderDataObject.getAsJsonArray("hotelOrderPassengerInfos").get(0).getAsJsonObject().getAsJsonArray("passengerDepartments").contains(hotelOrder.getAsJsonArray("hotelOrderPassengerInfos").get(0).getAsJsonObject().getAsJsonArray("passengerDepartments"));
            //进行数据对比
            assert GsonUtil.compareJsonObject(hotelOrderDataObject,hotelOrder,mapping);
        }
    }


    @Test(description = "审批单同步-中集TMC-火车行程",dataProvider = "TMC")
    public void workFlowTest3(String tmcChannel,String supplierOID,String supplierName,String supplierCode) throws HttpStatusException, InterruptedException {
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
            JsonObject tmcRequestData = tmcdata.getAsJsonObject("tmcRequest");
            JsonObject tmcResponse = tmcdata.getAsJsonObject("response");
            //订单号
            String orderNo = RandomNumber.getTimeNumber();
            ArrayList<String> bookerDepartments =new ArrayList<>();
            //车票价
            BigDecimal ticketPrice =new BigDecimal(690).setScale(2);
            //服务费
            BigDecimal servicePrice = new BigDecimal(20).setScale(2);
            BigDecimal totalAmount =ticketPrice.add(servicePrice);
            //电子客票号
            String trainElectronic = RandomNumber.getTimeNumber(10);
            bookerDepartments.add(employee.getDepartmentName());
            //火车订单基本信息
            TrainBaseOrder trainBaseOrder =trainOrder.setTrainBaseOrder(employee,supplierName,supplierCode,totalAmount,"B",orderNo,tmcRequestData,traveApplicationDetail.getAsJsonObject("applicant"));
            //订单车票信息
            TrainTicketInfo trainTicketInfo = trainOrder.setTrainTicketInfo(orderNo,"D1234","1",trainElectronic,"",ticketPrice,"05车07C");
            ArrayList<TrainTicketInfo> trainTicketInfos =new ArrayList<>();
            trainTicketInfos.add(trainTicketInfo);
            //订单车次
            TrainSequenceInfo trainSequenceInfo = trainOrder.setTrainSequenceInfo(orderNo,"1",tmcRequestData);
            ArrayList<TrainSequenceInfo> trainSequenceInfos =new ArrayList<>();
            trainSequenceInfos.add(trainSequenceInfo);
            //订单乘客信息
            JsonObject applicationParticipant = traveApplicationDetail.getAsJsonArray("applicationParticipants").get(0).getAsJsonObject();
            TrainPassengerInfo trainPassengerInfo =trainOrder.setTrainPassengerInfo(employee,orderNo,"1",tmcRequestData,applicationParticipant);
            ArrayList<TrainPassengerInfo> trainPassengerInfos =new ArrayList<>();
            trainPassengerInfos.add(trainPassengerInfo);
        }
    }


}

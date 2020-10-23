package com.test.api.method.VendorMethod;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.airOrderInfo.*;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.test.api.method.InfraStructure;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/15
 * @Version 1.0
 **/
public class FlightOrder {

    private InfraStructure infraStructure;

    public FlightOrder(){
        infraStructure = new InfraStructure();
    }

    /**
     * 航程信息
     * @param orderNo
     * @return
     */
    public AirFlightInfo setAirFlightInfo(String orderNo) {
        //航程信息
        AirFlightInfo airFlightInfo = AirFlightInfo.builder()
                .orderNo(orderNo)
                .sequence("1")
                .flight("MU2160")
                .airLineCode("MU")
                .airLineName("上海东方航空公司")
                .takeoffTime(UTCTime.getBeijingTime(-5, 0, 0))
                .arrivalTime(UTCTime.getBeijingTime(-5, 2, 10))
                .classType("Y")
                .subClass("A")
                .dcityName("西安")
                .dcityCode("CHN061001000")
                .dportName("西安咸阳国际机场")
                .dportCode("XIY")
                .dairportName("T3航站楼")
                .acityName("上海")
                .acityCode("CHN031000000")
                .aportName("上海虹桥国际机场")
                .aportCode("SHA")
                .aairportName("T2航站楼")
                .stopCity("")
                .airPort("")
                .stopTime(0)
                .flightTime(3)
                .tpm(1345)
                .craftType("空客320")
                .build();
        return airFlightInfo;
    }

    /**
     * 航程信息
     * @param orderNo
     * @return
     */
    public AirFlightInfo setAirFlightInfo(String orderNo,JsonObject travelFlight) {
        //航程信息
        HashMap<String,String> airport =new HashMap<>();
        airport.put("西安市","西安咸阳国际机场");
        airport.put("北京","首都国际机场");
        airport.put("上海","上海虹桥国际机场");
        airport.put("南京市","南京禄口国际机场");
        //机场映射
        HashMap<String,String> airportCode = new HashMap<>();
        airportCode.put("西安咸阳国际机场","XIY");
        airportCode.put("首都国际机场","PEK");
        airportCode.put("上海虹桥国际机场","SHA");
        airportCode.put("南京禄口国际机场","NKG");
        //舱位映射
        HashMap<Integer,String> classType = new HashMap<>();
        classType.put(3,"Y");
        classType.put(1,"F");
        classType.put(2,"C");
        classType.put(0,"未知");
        classType.put(4,"SY");
        //航程中的起飞时间为审批单同步的起飞时间 到达时间为2小时后
        String takeoffTime = UTCTime.BJDateMdy(travelFlight.get("takeOffBegin").getAsString(),4)+" "+UTCTime.getTime(0,0);
        String arrivalTime = UTCTime.BJDateMdy(travelFlight.get("arrivalEnd").getAsString(),-4)+" "+UTCTime.getTime(2,30);
        AirFlightInfo airFlightInfo = AirFlightInfo.builder()
                .orderNo(orderNo)
                .sequence("1")
                .flight("MU2160")
                .airLineCode("MU")
                .airLineName("上海东方航空公司")
                .takeoffTime(takeoffTime)
                .arrivalTime(arrivalTime)
                .classType(classType.get(travelFlight.get("seatClass").getAsInt()))
                .subClass("A")
                .dcityName(travelFlight.get("fromCity").getAsString())
                .dcityCode(travelFlight.get("fromCityCode").getAsString())
                .dportName(airport.get(travelFlight.get("fromCity").getAsString()))
                .dportCode(airportCode.get(airport.get(travelFlight.get("fromCity").getAsString())))
                .dairportName("T3航站楼")
                .acityName(travelFlight.get("toCity").getAsString())
                .acityCode(travelFlight.get("toCityCode").getAsString())
                .aportName(airport.get(travelFlight.get("toCity").getAsString()))
                .aportCode(airportCode.get(airport.get(travelFlight.get("toCity").getAsString())))
                .aairportName("T2航站楼")
                .stopCity("")
                .airPort("")
                .stopTime(0)
                .flightTime(2)
                .tpm(1345)
                .craftType("空客320")
                .build();
        return airFlightInfo;
    }

    /**
     * 乘机人信息
     * @param orderNo
     * @param passengerNo
     * @param passengerAttribute
     * @param passengerName
     * @param passengerNum
     * @param bookerDepartments
     * @param departmentName
     * @param passengerPhone
     * @param passengerEmail
     * @return
     */
    public AirPassengerInfo setAirPassengerInfo(String orderNo, String passengerNo, String passengerAttribute, String passengerName, String passengerNum, List<String> bookerDepartments,String departmentName,String deptCode,String passengerPhone,String passengerEmail){
        //乘机人信息
        AirPassengerInfo airPassengerInfo = AirPassengerInfo.builder()
                .orderNo(orderNo)
                .passengerNo(passengerNo)
                .passengerType("AUT")
                .passengerAttribute(passengerAttribute)
                .passengerName(passengerName)
                .passengerNum(passengerNum)
                .departmentName(departmentName)
                .departmentCode(deptCode)
                .nationlityName("中国")
                .certificateType("IDC")
                .certificateNum("610"+System.currentTimeMillis())
                .passengerPhone(passengerPhone)
                .passengerEmail(passengerEmail)
                .passengerSex("M")
                .passengerCostCenter("管理综合部1")
                .passengerDepartments(bookerDepartments)
                .build();
        return airPassengerInfo;
    }

    /**
     * 乘机人信息
     * @param orderNo
     * @param passengerNo
     * @return
     */
    public AirPassengerInfo setAirPassengerInfo(Employee employee,String orderNo, String passengerNo,JsonObject tmcRequestData,JsonObject applicationParticipant) throws HttpStatusException {
        JsonObject tmcParticipant = tmcRequestData.getAsJsonArray("participantList").get(0).getAsJsonObject();
        //查询乘机人的信息
        JsonObject participantInfo = infraStructure.getEmployeeDetail(employee,applicationParticipant.get("participantOID").getAsString());
        //乘机人信息
        ArrayList<String> bookerDepartments =new ArrayList<>();
        bookerDepartments.add(participantInfo.get("departmentName").getAsString());
        //身份证信息
        JsonObject cardInfo = infraStructure.queryUserCard(employee,applicationParticipant.get("participantOID").getAsString(),"身份证");
        //查询部门code
        String deptCode = infraStructure.getDeptCode(employee,participantInfo.get("departmentOID").getAsString());
        AirPassengerInfo airPassengerInfo = AirPassengerInfo.builder()
                .orderNo(orderNo)
                .passengerNo(passengerNo)
                .passengerType("AUT")
                .passengerAttribute("I")
                .passengerName(tmcParticipant.get("name").getAsString())
                .passengerNum(tmcParticipant.get("employeeID").getAsString())
                .passengerDepartments(bookerDepartments)
                .departmentName(participantInfo.get("departmentName").getAsString())
                .departmentCode(deptCode)
                .nationlityName("中国")
                .certificateType("IDC")
                .certificateNum(cardInfo.get("originalCardNo").getAsString())
                .passengerPhone(tmcParticipant.get("mobile").getAsString())
                .passengerEmail(tmcParticipant.get("email").getAsString())
                .passengerSex(tmcParticipant.get("gender").getAsString())
                .passengerCostCenter(tmcRequestData.get("costCenter1").getAsString())
                .build();
        return airPassengerInfo;
    }

    /**
     * 机票改签信息
     * @param orderNo
     * @return
     */
    public AirChangeInfo setAirChangeInfo(String orderNo){

        //订单改签信息  有改签则传
        AirChangeInfo airChangeInfo = AirChangeInfo.builder()
                .orderNo(orderNo)
                .changeAmount(new BigDecimal(0).setScale(2))
                .changeType("VR")
                .changeReason("机场和时间选择")
                .changeMethod("")
                .changeFee(new BigDecimal(100).setScale(2))
                .changeRate(new BigDecimal(10).setScale(2))
                .changeDifference(new BigDecimal(200).setScale(2))
                .changeServiceFee(new BigDecimal(20).setScale(2))
                .build();
        return airChangeInfo;
    }

    /**
     * 订单退票
     * @param orderNo
     * @return
     */
    public AirRefundInfo setAirRefundInfo(String orderNo){
        //订单退票 有退票则必传
        AirRefundInfo trainRefundInfo = AirRefundInfo.builder()
                .orderNo(orderNo)
                .refundAmount(new BigDecimal(500).setScale(2))
                .refundType("VR")
                .refundReason("旅程有变化")
                .refundMethod("")
                .refundFee(new BigDecimal(100).setScale(2))
                .refundRate(new BigDecimal(10).setScale(2))
                .refundServiceFee(new BigDecimal(20).setScale(2))
                .build();
        return trainRefundInfo;
    }

    /**
     * 保险信息
     * @param ticketKey
     * @param sequence
     * @param insuaranceQuantity
     * @return
     */
    public AirInsurance setAirInsurance(String ticketKey,String sequence,Integer insuaranceQuantity){

        //保险信息
        AirInsurance airInsurance = AirInsurance.builder()
                .ticketKey(ticketKey)
                .sequence(sequence)
                .insuranceFee(new BigDecimal(50).setScale(2))
                .insuranceStatus("P")
                .insuranceStatusDec("已出保")
                .insuaranceUnitPrice(new BigDecimal(50).setScale(2))
                .insuaranceQuantity(insuaranceQuantity)
                .insuaranceName("中国平安人身意外险")
                .insuaranceCompanyName("中国平安保险集团")
                .build();
        return airInsurance;
    }

    /**
     * //行程单打印以及配送信息
     * @param ticketKey
     * @param ticketNo
     * @param passengerName
     * @return
     */
    public AirTicketPrint setAirTicketPrint(String ticketKey,String ticketNo,String passengerName){
        //行程单打印以及配送信息
        AirTicketPrint airTicketPrint = AirTicketPrint.builder()
                .ticketKey(ticketKey)
                .ticketNo(ticketNo)
                .passengerName(passengerName)
                .printNo(RandomNumber.getTimeNumber(7))
                .printTime(UTCTime.getBeijingTime(1,6,0))
                .expressNo(RandomNumber.getTimeNumber(12))
                .expressCompany("中通快递")
                .expressFee(new BigDecimal(0).setScale(2))
                .expressServiceFee(new BigDecimal(0).setScale(2))
                .build();
        return airTicketPrint;
    }

    /**
     * 机票信息
     * @param ticketKey
     * @param passengerNo  乘客编号
     * @param ticketNo  订单号
     * @param ticketPrice 票价
     * @param oilFee 油费
     * @param tax  基建
     * @param serverFee
     * @return
     */
    public AirTicketInfo setAirTicketInfo(String ticketKey,String passengerNo,String ticketNo,String ticketStatusName,BigDecimal ticketPrice,BigDecimal oilFee,BigDecimal tax,BigDecimal serverFee){
        // 机票信息
        HashMap<String,String> status =new HashMap<>();
        status.put("已使用","USED");
        status.put("已退票","REFUNDED");
        status.put("已改签","EXCHANGEd");
        AirTicketInfo airTicketInfo = AirTicketInfo.builder()
                .ticketKey(ticketKey)
                .passengerNo(passengerNo)
                .finalTicketTime(UTCTime.getBeijingTime(-10,1,0))
                .passengerType("AUT")
                .ticketPNR(RandomNumber.getUUID(5))
                .ticketNo(ticketNo)
                .ticketStatusName(ticketStatusName)
                .ticketStatusCode(status.get(ticketStatusName))
                .isPolicy("N")
                .ticketPrice(ticketPrice)
                .variance(new BigDecimal(0).setScale(2))
                .priceRate(8.0)
                .oilFee(oilFee)
                .tax(tax)
                .serverFee(serverFee)
                .postServiceFee(new BigDecimal(0).setScale(2))
                .classType("Y")
                .subClass("A")
                .rerNotes("起飞前24小时免费")
                .refNotes("起飞前24小时免费")
                .endNotes("起飞前24小时免费")
                .yClassStandardPrice("1000.00")
                .build();
        return airTicketInfo;
    }

    /**
     * 飞机基本订单
     * @param employee
     * @param orderType
     * @param orderNo
     * @param supplierName
     * @param supplierCode
     * @param tmcdata   审批单同步信息
     * @param amount
     */
    public AirBaseOrder setAirBaseOrder(Employee employee,String orderType, String orderNo, String supplierName, String supplierCode,JsonObject tmcdata, JsonObject applicat,BigDecimal amount){
        //订单基本信息
        HashMap<Integer,String> flightWay = new HashMap<>();
        flightWay.put(1001,"S");
        flightWay.put(1002,"R");
        AirBaseOrder airBaseOrder = AirBaseOrder.builder()
                .orderType(orderType)
                .orderNo(orderNo)
                .supplierName(supplierName)
                .supplierCode(supplierCode)
                .approvalCode(tmcdata.get("approvalCode").getAsString())
                .orderStatus("已出票")
                .orderStatusCode("S")
                //这块数据取值待定。
                .tenantCode(tmcdata.get("tenantId").getAsString())
                .tenantName(employee.getTenantName())
                .employeeId(tmcdata.getAsJsonObject("bookClerk").get("employeeID").getAsString())
                .supplierAccount("")
                .preEmployName(tmcdata.getAsJsonObject("bookClerk").get("name").getAsString())
                .companyOid(tmcdata.get("companyId").getAsString())
                //取值待定
                .companyName(applicat.get("companyName").getAsString())
                .companyCode(employee.getCompanyCode())
                .departmentName(applicat.get("departmentName").getAsString())
                .departmentOid(applicat.get("departmentOID").getAsString())
                .bookChannel("Online-API")
                .bookType("C")
                .payType("COPAY")
                .createTime(UTCTime.getBeijingTime(0,0,0))
                .payTime(UTCTime.getBeijingTime(0,0,2))
                .successTime(UTCTime.getBeijingTime(0,0,4))
                .flightClass("N")
                .flightWay(flightWay.get(tmcdata.getAsJsonArray("travelFlightsList").get(0).getAsJsonObject().get("itineraryType").getAsInt()))
                .paymentType("M")
                .accountType("C")
                .currency("CNY")
                .amount(amount)
                .contactName(tmcdata.getAsJsonObject("bookClerk").get("name").getAsString())
                .contactPhone(tmcdata.getAsJsonObject("bookClerk").get("mobile").getAsString())
                .contactEmail(employee.getEmail())
                .remark(tmcdata.get("remark").getAsString())
                .build();
        return airBaseOrder;
    }

    /**
     * 机票订单超标信息
     * @param ticketKey
     * @param ticketNo
     * @return
     */
    public AirExceedInfo setAirExceedInfo(String ticketKey,String ticketNo){
        AirExceedInfo airExceedInfo = AirExceedInfo.builder()
                .ticketKey(ticketKey)
                .ticketNo(ticketNo)
                .violationContentCode("RDCFH")
                .violationContentName("低价优先")
                .violationReasonCode("yykq")
                .violationReasonName("低价原因")
                .lowFlight("MU1254")
                .lowClass("Y")
                .lowPrice(new BigDecimal(500).setScale(2))
                .lowRate(new BigDecimal(7))
                .lowDTime("22:00:00")
                .build();
        return airExceedInfo;

    }
}

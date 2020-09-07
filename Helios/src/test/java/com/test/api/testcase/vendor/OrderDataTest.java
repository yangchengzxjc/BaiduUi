package com.test.api.testcase.vendor;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SettlementBody;
import com.hand.basicObject.supplierObject.TrainOrderInfo.*;
import com.hand.basicObject.supplierObject.airOrderInfo.*;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelBaseOrder;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelExceedInfo;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelOrderInfoEntity;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelPassengerInfo;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.InfraStructure;
import com.test.api.method.Vendor;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @Author peng.zhang
 * @Date 2020/9/4
 * @Version 1.0
 **/
@Slf4j
public class OrderDataTest extends BaseTest {

    private Employee employee;
    private Vendor vendor;
    private InfraStructure infraStructure;


    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("yaliang.wang@cimc.com") String phoneNumber, @Optional("111111") String pwd, @Optional("stage") String env){
        employee =getEmployee(phoneNumber,pwd,env);
        vendor =new Vendor();
        infraStructure =new InfraStructure();
    }

    @Test(description = "酒店订单-1人预定-已提交状态-公司支付-未超标")
    public void hotelOrderDataTest1() throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        ArrayList<String> bookerDepartments =new ArrayList<>();
        bookerDepartments.add(employee.getDepartmentName());
        HotelBaseOrder hotelBaseOrder = HotelBaseOrder.builder()
                .orderType("B")
                .orderNo(orderNo)
                .supplierName("")
                .supplierCode("cimccTMC")
                .approvalCode("TA"+System.currentTimeMillis())
                .orderStatusName("已提交")
                .orderStatusCode("Submitted")
                .tenantCode(employee.getTenantCode())
                .tenantName(employee.getTenantName())
                .employeeId(employee.getEmployeeID())
                .bookerDepartments(bookerDepartments)
                .employeeName(employee.getFullName())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(employee.getDepartmentName())
                .bookChannel("Online-APP")
                .bookType("C")
                .payType("COPAY")
                .createTime(UTCTime.getBeijingTime(0,0))
                .payTime(UTCTime.getBeijingTime(0,0))
                .successTime(UTCTime.getBeijingTime(0,0))
                .hotelClass("N")
                .paymentType("M")
                .accountType("C")
                .balanceType("P")
                .guaranteeType("N")
                .currency("CNY")
                .totalAmount(new BigDecimal(1000).setScale(2))
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .hotelType("AGR")
                .hotelName("爱丽丝酒店")
                .hotelStar(3)
                .startTime(UTCTime.getBeijingTime(3,0))
                .endTime(UTCTime.getBeijingDate(5)+" 12:00:00")
                .lastCancelTime(UTCTime.getBeijingTime(2,0))
                .cityName("上海")
                .cityHeliosCode("CHN031000000")
                .passengerName(employee.getFullName())
                .roomName("商务套房")
                .roomQuantity(1)
                .roomDays(5)
                .variance(new BigDecimal(0).setScale(2))
                .build();
        HotelPassengerInfo hotelPassengerInfo =HotelPassengerInfo.builder()
                .orderNo(orderNo)
                .passengerNo("1")
                .passengerAttribute("I")
                .passengerName(employee.getFullName())
                .passengerNum(employee.getEmployeeID())
                .departmentName(employee.getDepartmentName())
                .passengerDepartments(bookerDepartments)
                .build();
        //超标信息
//        HotelExceedInfo hotelExceedInfo =HotelExceedInfo.builder()
//                .orderNo(orderNo)
//                .violationContentCode("")
//                .violationContentName("")
//                .violationReasonName("")
//                .violationReasonCode("")
//                .build();
//        ArrayList<HotelExceedInfo> hotelExceedInfos =new ArrayList<>();
//        hotelExceedInfos.add(hotelExceedInfo);
        ArrayList<HotelPassengerInfo> hotelPassengerInfos =new ArrayList<>();
        hotelPassengerInfos.add(hotelPassengerInfo);
        HotelOrderInfoEntity hotelOrderInfoEntity = HotelOrderInfoEntity.builder()
                .hotelBaseOrder(hotelBaseOrder)
                .hotelOrderPassengerInfos(hotelPassengerInfos)
                .build();
        //订单推送
        JsonObject info=vendor.pushOrderData(employee,"hotel",hotelOrderInfoEntity,"cimccTMC","200428140254184788","");
        log.info("推送的数据响应:{}",info);
    }

    @Test(description = "火车票1人预定,不改签-不退票")
    public void trainOrderDataTest2() throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        ArrayList<String> bookerDepartments =new ArrayList<>();
        //车票价
        BigDecimal ticketPrice =new BigDecimal(230).setScale(2);
        //电子客票号
        String trainElectronic = RandomNumber.getTimeNumber(10);
        bookerDepartments.add(employee.getDepartmentName());
        //订单基本信息
        TrainBaseOrder trainBaseOrder = TrainBaseOrder.builder()
                .orderType("B")
                .orderNo(orderNo)
                .originalOrderNum("")
                .supplierName("")
                .supplierCode("cimccTMC")
                .approvalCode("TA"+System.currentTimeMillis())
                .orderStatusName("已出票")
                .orderStatusCode("TD")
                .tenantCode(employee.getTenantCode())
                .tenantName(employee.getTenantName())
                .employeeNum(employee.getEmployeeID())
                .employeeName(employee.getFullName())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(employee.getDepartmentName())
                .bookChannel("Online-AP")
                .bookType("C")
                .payType("COPAY")
                .createTime(UTCTime.getBeijingTime(0,0))
                .payTime(UTCTime.getBeijingTime(0,0))
                .successTime(UTCTime.getBeijingTime(0,0))
                .paymentType("M")
                .accountType("C")
                .currency("CNY")
                .totalAmount(new BigDecimal(500).setScale(2))
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .remark("")
                .build();
        //订单车票信息
        TrainTicketInfo trainTicketInfo = TrainTicketInfo.builder()
                .orderNo(orderNo)
                .passengerNo("1")
                .sequenceNo("1")
                .trainNum("D1234")
                .trainElectronic(trainElectronic)
                .passengerType("AUD")
                .ticketPrice(ticketPrice)
                .servicePrice(new BigDecimal(20).setScale(2))
                .seatNum("05车07C")
                .seatType("209")
                .build();
        ArrayList<TrainTicketInfo> trainTicketInfos =new ArrayList<>();
        trainTicketInfos.add(trainTicketInfo);
        //订单车次
        TrainSequenceInfo trainSequenceInfo =TrainSequenceInfo.builder()
                .orderNo(orderNo)
                .sequenceNo("1")
                .trainNum("D123")
                .departureTime(UTCTime.getBeijingTime(3,0))
                .arriveTime(UTCTime.getBeijingTime(3,4))
                .dCityName("西安")
                .dCityCode("CHN061001000")
                .dStationName("西安北站")
                .aCityName("上海")
                .aCityCode("CHN031000000")
                .aStationName("虹桥火车站")
                .build();
        ArrayList<TrainSequenceInfo> trainSequenceInfos =new ArrayList<>();
        trainSequenceInfos.add(trainSequenceInfo);
        //订单乘客信息
        TrainPassengerInfo trainPassengerInfo = TrainPassengerInfo.builder()
                .orderNo(orderNo)
                .passengerNo("1")
                .passengerType("AUD")
                .passengerAttribute("I")
                .passengerName(employee.getFullName())
                .passengerNum(employee.getEmployeeID())
                .passengerDepartments(bookerDepartments)
                .departmentName(employee.getDepartmentName())
                .nationlityName("中国")
                .certificateType("IDC")
                .certificateNum("6101599468129501")
                .passengerPhone(employee.getMobile())
                .passengerEmail(employee.getEmail())
                .passengerSex("M")
                .build();
        ArrayList<TrainPassengerInfo> trainPassengerInfos =new ArrayList<>();
        trainPassengerInfos.add(trainPassengerInfo);
        //订单改签信息  有改签则传
//        TrainChangeInfo trainChangeInfo = TrainChangeInfo.builder()
//                .orderNo(orderNo)
//                .changeAmount(new BigDecimal(0).setScale(2))
//                .changeType("")
//                .changeReason("")
//                .changeMethod("")
//                .changeFee(new BigDecimal(0).setScale(2))
//                .changeRate(new BigDecimal(0).setScale(2))
//                .changeServiceFee(new BigDecimal(0).setScale(2))
//                .dCityName("西安")
//                .dCityCode("CHN061001000")
//                .dStationName("西安北站")
//                .aCityName("上海")
//                .aCityCode("CHN031000000")
//                .aStationName("虹桥火车站")
//                .build();
//        //订单退票 有退票则必传
//        TrainRefundInfo trainRefundInfo = TrainRefundInfo.builder()
//                .orderNo(orderNo)
//                .refundAmount(new BigDecimal(200).setScale(2))
//                .refundType("")
//                .refundReason("旅程修改")
//                .refundMethod("")
//                .refundFee(new BigDecimal(100).setScale(2))
//                .refundRate(new BigDecimal(20).setScale(2))
//                .refundServiceFee(new BigDecimal(100).setScale(2))
//                .build();
        TrainOrderInfoEntity trainOrderInfoEntity = TrainOrderInfoEntity.builder()
                .trainOrderBase(trainBaseOrder)
                .trainOrderTicketInfos(trainTicketInfos)
                .trainOrderSequenceInfos(trainSequenceInfos)
                .trainOrderPassengerInfos(trainPassengerInfos)
                .build();
        //订单推送
        JsonObject info=vendor.pushOrderData(employee,"train",trainOrderInfoEntity,"cimccTMC","200428140254184788","");
        log.info("推送的数据响应:{}",info);
        SettlementBody settlementBody = SettlementBody.builder()
                .companyOid(employee.getCompanyOID())
                .orderNo(orderNo)
                .page(1)
                .size(10)
                .build();
        //查询订单数据
        JsonObject  trainOrder = vendor.queryOrderData(employee,"train",settlementBody);
        log.info("train order Data:{}",trainOrder);
    }

    @Test(description = "机票订单-单程-月结-不改签-不退票")
    public void flightOrderDataTest3() throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        ArrayList<String> bookerDepartments =new ArrayList<>();
        bookerDepartments.add(employee.getDepartmentName());
        //机票价格
        BigDecimal ticketPrice =new BigDecimal(1022).setScale(2);
        //燃油费
        BigDecimal oilFee = new BigDecimal(50).setScale(2);
        //基建费
        BigDecimal tax =new BigDecimal(20).setScale(2);
        //服务费
        BigDecimal serverFee = new BigDecimal(20).setScale(2);
        //电子客票号
        String trainElectronic = RandomNumber.getTimeNumber(10);
        bookerDepartments.add(employee.getDepartmentName());
        //票号
        String ticketNo = RandomNumber.getTimeNumber(13);
        BigDecimal amount =ticketPrice.add(oilFee).add(tax).add(serverFee);
        //订单基本信息
        AirBaseOrder airBaseOrder = AirBaseOrder.builder()
                .orderType("B")
                .orderNo(orderNo)
                .originalOrderNo("")
                .supplierName("")
                .supplierCode("cimccTMC")
                .approvalCode("TA"+System.currentTimeMillis())
                .orderStatus("已出票")
                .orderStatusCode("S")
                .tenantCode(employee.getTenantCode())
                .tenantName(employee.getTenantName())
                .employeeId(employee.getEmployeeID())
                .supplierAccount("")
                .preEmployName(employee.getFullName())
                .companyOid(employee.getCompanyOID())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(employee.getDepartmentName())
                .departmentOid(employee.getDepartmentOID())
                .bookChannel("Online-API")
                .bookType("C")
                .payType("COPAY")
                .createTime(UTCTime.getBeijingTime(-10,0))
                .payTime(UTCTime.getBeijingTime(-10,0))
                .successTime(UTCTime.getBeijingTime(-10,0))
                .flightClass("N")
                .flightWay("S")
                .paymentType("M")
                .accountType("C")
                .currency("CNY")
                .amount(amount)
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .build();
        // 机票信息
        AirTicketInfo airTicketInfo = AirTicketInfo.builder()
                .ticketKey(trainElectronic)
                .passengerNo("1")
                .passengerType("AUT")
                .ticketPNR(RandomNumber.getUUID(5))
                .ticketNo(ticketNo)
                .ticketStatusName("已使用")
                .ticketStatusCode("USED")
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
                .endNotes("")
                .yClassStandardPrice("1500")
                .build();
        ArrayList<AirTicketInfo> airTicketInfos =new ArrayList<>();
        airTicketInfos.add(airTicketInfo);
        //航程信息
        AirFlightInfo airFlightInfo = AirFlightInfo.builder()
                .orderNo(orderNo)
                .sequence("1")
                .flight("MU2160")
                .airLineCode("MU")
                .airLineName("上海东方航空公司")
                .takeoffTime(UTCTime.getBeijingTime(-5,0))
                .arrivalTime(UTCTime.getBeijingTime(-5,2))
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
                .stopTime("")
                .flightTime("")
                .tpm(1345)
                .craftType("空客320")
                .build();
        ArrayList<AirFlightInfo> airFlightInfos =new ArrayList<>();
        airFlightInfos.add(airFlightInfo);
        //乘机人信息
        AirPassengerInfo airPassengerInfo = AirPassengerInfo.builder()
                .orderNo(orderNo)
                .passengerNo("1")
                .passengerType("AUT")
                .passengerAttribute("I")
                .passengerName(employee.getFullName())
                .passengerNum(employee.getEmployeeID())
                .passengerDepartments(bookerDepartments)
                .departmentName(employee.getDepartmentName())
                .nationlityName("中国")
                .certificateType("IDC")
                .certificateNum("610"+System.currentTimeMillis())
                .passengerPhone(employee.getPhoneNumber())
                .passengerEmail(employee.getEmail())
                .passengerSex("M")
                .passengerDepartments(bookerDepartments)
                .build();
        ArrayList<AirPassengerInfo> airPassengerInfos =new ArrayList<>();
        airPassengerInfos.add((airPassengerInfo));
        //订单改签信息  有改签则传
//        AirChangeInfo airChangeInfo = AirChangeInfo.builder()
//                .orderNo(orderNo)
//                .changeAmount(new BigDecimal(0).setScale(2))
//                .changeType("")
//                .changeReason("")
//                .changeMethod("")
//                .changeFee(new BigDecimal(0).setScale(2))
//                .changeRate(new BigDecimal(0).setScale(2))
//                .changeServiceFee(new BigDecimal(0).setScale(2))
//                .changeDifference(new BigDecimal(200).setScale(2))
//                .changeServiceFee(new BigDecimal(50).setScale(2))
//                .build();
        //订单退票 有退票则必传
//        AirRefundInfo trainRefundInfo = AirRefundInfo.builder()
//                .orderNo(orderNo)
//                .refundAmount(new BigDecimal(200).setScale(2))
//                .refundType("")
//                .refundReason("旅程修改")
//                .refundMethod("")
//                .refundFee(new BigDecimal(100).setScale(2))
//                .refundRate(new BigDecimal(20).setScale(2))
//                .refundServiceFee(new BigDecimal(100).setScale(2))
//                .build();
        //行程单打印以及配送信息
        AirTicketPrint airTicketPrint = AirTicketPrint.builder()
                .ticketKey(trainElectronic)
                .ticketNo(ticketNo)
                .passengerName(employee.getFullName())
                .printNo(RandomNumber.getTimeNumber(7))
                .printTime(UTCTime.getBeijingTime(-5,6))
                .expressNo(RandomNumber.getTimeNumber(14))
                .expressCompany("中通快递")
                .expressFee(new BigDecimal(0).setScale(2))
                .expressServiceFee(new BigDecimal(0).setScale(2))
                .build();
        ArrayList<AirTicketPrint> airTicketPrints =new ArrayList<>();
        airTicketPrints.add(airTicketPrint);
        //保险信息
        AirInsurance airInsurance = AirInsurance.builder()
                .ticketKey(trainElectronic)
                .sequence("1")
                .insuranceFee(new BigDecimal(50).setScale(2))
                .insuranceStatus("P")
                .insuranceStatusDec("已出保")
                .insuaranceUnitPrice(new BigDecimal(50).setScale(2))
                .insuaranceQuantity(1)
                .insuaranceName("中国平安人身意外险")
                .insuaranceCompanyName("中国平安保险集团")
                .build();
        ArrayList<AirInsurance> airInsurances =new ArrayList<>();
        airInsurances.add(airInsurance);
        AirOrderInfoEntity airOrderInfoEntity =AirOrderInfoEntity.builder()
                .airBaseOrder(airBaseOrder)
                .airTicketInfo(airTicketInfos)
                .airFlightInfo(airFlightInfos)
                .airPassengerInfo(airPassengerInfos)
                .airTicketPrint(airTicketPrints)
                .airInsurance(airInsurances)
                .build();
        //订单推送
        JsonObject info=vendor.pushOrderData(employee,"flight",airOrderInfoEntity,"cimccTMC","200428140254184788","");
        log.info("推送的数据响应:{}",info);
        SettlementBody settlementBody = SettlementBody.builder()
                .companyOid(employee.getCompanyOID())
                .orderNo(orderNo)
                .page(1)
                .size(10)
                .build();
        //查询订单数据
        JsonObject  trainOrder = vendor.queryOrderData(employee,"flight",settlementBody);
        log.info("train order Data:{}",trainOrder);
    }
}

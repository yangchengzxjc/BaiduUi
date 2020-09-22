package com.test.api.method.VendorMethod;

import com.hand.basicObject.supplierObject.airOrderInfo.*;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/15
 * @Version 1.0
 **/
public class FlightOrder {

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
                .arrivalTime(UTCTime.getBeijingTime(-5, 2, 0))
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
                .flightTime("3h")
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
    public AirPassengerInfo setAirPassengerInfo(String orderNo, String passengerNo, String passengerAttribute, String passengerName, String passengerNum, List<String> bookerDepartments,String departmentName,String passengerPhone,String passengerEmail){
        //乘机人信息
        AirPassengerInfo airPassengerInfo = AirPassengerInfo.builder()
                .orderNo(orderNo)
                .passengerNo(passengerNo)
                .passengerType("AUT")
                .passengerAttribute(passengerAttribute)
                .passengerName(passengerName)
                .passengerNum(passengerNum)
                .passengerDepartments(bookerDepartments)
                .departmentName(departmentName)
                .nationlityName("中国")
                .certificateType("IDC")
                .certificateNum("610"+System.currentTimeMillis())
                .passengerPhone(passengerPhone)
                .passengerEmail(passengerEmail)
                .passengerSex("M")
                .passengerDepartments(bookerDepartments)
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
                .printTime(UTCTime.getBeijingTime(-5,6,0))
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
    public AirTicketInfo setAirTicketInfo(String ticketKey,String passengerNo,String ticketNo,BigDecimal ticketPrice,BigDecimal oilFee,BigDecimal tax,BigDecimal serverFee){
        // 机票信息
        AirTicketInfo airTicketInfo = AirTicketInfo.builder()
                .ticketKey(ticketKey)
                .passengerNo(passengerNo)
                .finalTicketTime(UTCTime.getBeijingTime(-10,1,0))
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
                .yClassStandardPrice("1000.00")
                .build();
        return airTicketInfo;
    }
}

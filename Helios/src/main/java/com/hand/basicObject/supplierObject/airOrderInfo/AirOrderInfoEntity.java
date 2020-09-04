package com.hand.basicObject.supplierObject.airOrderInfo;

import lombok.Builder;

import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/4
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class AirOrderInfoEntity {
    //订单基本信息
    private AirBaseOrder airBaseOrder;
    //订单机票信息
    private List<AirTicketInfo> airTicketInfo;
    //订单航程信息
    private List<AirFlightInfo> airFlightInfo;
    //订单乘客信息
    private List<AirPassengerInfo> airPassengerInfo;
    //订单改签信息
    private List<AirChangeInfo> airChangeInfo;
    //订单退票信息
    private List<AirRefundInfo> airRefundInfo;
    //行程单打印以及配送信息
    private List<AirTicketPrint> airTicketPrint;
    // 保险信息
    private List<AirInsurance> airInsurance;
    // 超标信息
    private List<AirExceedInfo> airExceedInfo;



}

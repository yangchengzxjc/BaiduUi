package com.hand.basicObject.supplierObject.airOrderInfo;

import lombok.Builder;

/**
 * @Author peng.zhang
 * @Date 2020/9/4
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class AirTicketPrint {
    //消费商机票唯一标识
    private String ticketKey;
    //机票票号
    private String ticketNo;
    //乘机人姓名
    private String passengerName;
    private String printNo;
    private String printTime;
    private String expressNo;
    private String expressCompany;
    private String expressFee;
    private String expressServiceFee;

}

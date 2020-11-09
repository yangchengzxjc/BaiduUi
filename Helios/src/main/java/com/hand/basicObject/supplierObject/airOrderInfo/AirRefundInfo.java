package com.hand.basicObject.supplierObject.airOrderInfo;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * @Author peng.zhang
 * @Date 2020/9/4
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class AirRefundInfo {

    //关联的订单号
    private String orderNo;
    private String ticketKey;
    //退款金额
    private BigDecimal refundAmount;
    //退款类型   预留字段
    private String refundType;
    //退票原因
    private String refundReason;
    //退票方式    预留字段
    private String refundMethod;
    //退票费
    private BigDecimal refundFee;
    //退票费率
    private BigDecimal refundRate;
    //退票服务费
    private BigDecimal refundServiceFee;

}

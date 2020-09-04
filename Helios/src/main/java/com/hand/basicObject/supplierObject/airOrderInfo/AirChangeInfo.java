package com.hand.basicObject.supplierObject.airOrderInfo;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * @Author peng.zhang
 * @Date 2020/9/4
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class AirChangeInfo {

    //关联的订单号
    private String orderNo;
    //改签需付金额
    private BigDecimal changeAmount;
    //改签类型
    private String changeType;
    //改签原因
    private String changeReason;
    //改签方式
    private String changeMethod;
    //改签费
    private BigDecimal changeFee;
    //改签费率%
    private BigDecimal changeRate;
    //商旅改签服务费
    private BigDecimal changeServiceFee;
}

package com.hand.basicObject.supplierObject.TrainOrderInfo;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * @Author peng.zhang
 * @Date 2020/9/3
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class TrainChangeInfo{

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
    //出发城市名称
    private String dCityName;
    //出发城市code
    private String dCityCode;
    //出发火车站名称
    private String dStationName;
    //到达城市名称
    private String aCityName;
    //到达城市code
    private String aCityCode;
    //到达火车站名
    private String aStationName;


}

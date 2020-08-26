package com.hand.basicObject.supplierObject.trainSettlementInfo;

import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/8/25
 * @Version 1.0
 **/
public class TrainBaseOrder {

    //订单号
    private String orderNo;
    //订单状态
    private String orderStatus;
    //预定来源
    private String bookerSource;
    //预定人姓名
    private String bookerName;
    //预定人职级
    private String bookerRank;
    //订单类型
    private String orderType;
    //支付方式
    private String payType;
    //预定时间
    private String bookerTime;
    //退票状态
    private String refundStatus;
    //改签状态
    private String changeStatus;
    //订单成本中心1
    private String orderCostCenter1;
    //订单成本中心2
    private String orderCostCenter2;
    //订单成本中心3
    private String orderCostCenter3;
    //订单成本中心4
    private String orderCostCenter4;
    //订单成本中心5
    private String orderCostCenter5;
    //订单成本中心6
    private String orderCostCenter6;
    //出行目的
    private String tripPurpose;
    //项目号
    private String itemNumber;
    //预定人部门集合，最大10
    private List<String> departments;
}

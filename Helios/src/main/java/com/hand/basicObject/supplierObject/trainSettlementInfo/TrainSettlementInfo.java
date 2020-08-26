package com.hand.basicObject.supplierObject.trainSettlementInfo;

import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/8/25
 * @Version 1.0
 **/
public class TrainSettlementInfo {
    //结算基本信息
    private TrainBaseSettlement trainBaseSettlement;
    //订单基本信息
    private TrainBaseOrder trainBaseOrder;
    //订单乘客列表
    private List<TrainPassengerInfo> trainPassengerInfos;
    //票张详情列表
    private List<TrainTicketDetail> trainTicketDetails;
    //乘客票张关联
    private List<TrainPassengerTicketCorrelation> trainPassengerTicketCorrelations;
}

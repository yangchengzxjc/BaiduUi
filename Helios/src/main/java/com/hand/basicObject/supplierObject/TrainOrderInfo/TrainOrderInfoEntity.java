package com.hand.basicObject.supplierObject.TrainOrderInfo;

import lombok.Builder;

import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/3
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class TrainOrderInfoEntity {
    //订单基本信息
    private TrainBaseOrder trainOrderBase;
    //订单车票信息
    private List<TrainTicketInfo> trainOrderTicketInfos;
    //订单车次信息
    private List<TrainSequenceInfo> trainOrderSequenceInfos;
    //订单乘客信息
    private List<TrainPassengerInfo> trainOrderPassengerInfos;
    //订单改签信息
    private List<TrainChangeInfo> trainOrderChangeInfos;
    //订单退票信息
    private List<TrainRefundInfo> trainOrderRefundInfos;
    //订单超标原因
    private List<TrainExceedInfo> trainExceedInfos;
}

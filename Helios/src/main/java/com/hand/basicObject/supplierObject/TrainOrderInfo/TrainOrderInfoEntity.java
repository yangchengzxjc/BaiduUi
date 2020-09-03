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
    private TrainBaseOrder trainBaseOrder;
    //订单车票信息
    private List<TrainTicketInfo> trainTicketInfos;
    //订单车次信息
    private List<TrainSequenceInfo> trainSequenceInfos;
    //订单乘客信息
    private List<TrainPassengerInfo> trainPassengerInfos;
    //订单改签信息
    private List<TrainChangeInfo> trainChangeInfos;
    //订单退票信息
    private List<TrainRefundInfo> trainRefundInfos;
    //订单超标原因
    private List<TrainExceedInfo> trainExceedInfos;
}

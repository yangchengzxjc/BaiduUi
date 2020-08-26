package com.hand.basicObject.supplierObject.trainSettlementInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author peng.zhang
 * @Date 2020/8/25
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainPassengerTicketCorrelation {

    //订单号
    private String orderNo;
    //出行人编号
    private String passengerNo;
    //车次编号
    private String trainNo;
    //车票编号
    private String ticketNo;
    //退票状态
    private String refundTicketStatus;
    //退款状态
    private String refundStatus;
    //改签状态   改签时需提供
    private String changeStatus;
    //  车票类型   原车次/改签车次
    private String ticketType;
}

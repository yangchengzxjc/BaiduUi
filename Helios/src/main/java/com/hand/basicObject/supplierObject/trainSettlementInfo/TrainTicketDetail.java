package com.hand.basicObject.supplierObject.trainSettlementInfo;

/**
 * @Author peng.zhang
 * @Date 2020/8/25
 * @Version 1.0
 **/
public class TrainTicketDetail {

    //车次编号
    private String trainNo;
    //车票编号
    private String ticketNo;
    //列车类型  G 高铁  D 动车 T 特快  K 快车   C   城际铁路  空 未知车型
    private String trainType;
    //出发城市
    private String departureCity;
    //到达城市
    private String arrivalCity;
    //出发时间
    private String departureTime;
    //出发车站名
    private String departureStationName;
    //到达时间
    private String arriveTime;
    //到达车站名
    private String arriveStationName;
    // 12306 电子订单号
    private String electronicOrderNo;
    //车票类型   原车次/改签车次
    private String ticketType;
    // 座位号
    private String seatNum;
    //座位类型  201：硬座
    //203：软座
    //205：特等座
    //207：一等座
    //209：二等座
    //214：软卧
    //218：高级软包
    //221：商务座
    //227：无座
    //224：硬卧
    //225：软卧
    //226：高级软卧
    //301：一等软座
    //302：二等软座
    //303：一人软包
    //304：动卧
    //307：高级动卧
    //311：一等卧
    //314：二等卧
    private String seatType;
    //出票实际价格
    private String ticketActualFee;
    //改签状态    改签时必填
    private String changeStatus;
    //退改预估手续费用
    private String refundChangeCommission;

}

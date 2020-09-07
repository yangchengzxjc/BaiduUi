package com.hand.basicObject.supplierObject.TrainOrderInfo;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * @Author peng.zhang
 * @Date 2020/9/3
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class TrainTicketInfo {
    //车票对应的订单号
    private String orderNo;
    // 乘客乘客序号
    private String passengerNo;
    // 对应车次的序号
    private String sequenceNo;
    //  车次号
    private String trainNum;
    // 火车票电子客票
    private String trainElectronic;
    //改签前的车票电子票号   改签时有值
    private String preTrainElectronic;
    //乘客类型    AUD：成人票   CHD：儿童票
    private String passengerType;
    //车票票价
    private BigDecimal ticketPrice;
    //车票服务费
    private BigDecimal servicePrice;
    //座位号
    private String seatNum;
    //座位类型
    private String seatType;

    //201：硬座
    //
    //203：软座
    //
    //205：特等座
    //
    //207：一等座
    //
    //209：二等座
    //
    //211：硬卧（停用）
    //
    //214：软卧（停用）
    //
    //218：高级软包（停用）
    //
    //221：商务座
    //
    //227：无座
    //
    //224：硬卧
    //
    //225：软卧
    //
    //226：高级软卧
    //
    //301：一等软座
    //
    //302：二等软座
    //
    //303：一人软包
    //
    //304：动卧
    //
    //307：高级动卧
    //
    //308：一等软卧
    //
    //309：二等软卧
    //
    //311：一等卧（新增）
    //
    //314：二等卧（新增）

}

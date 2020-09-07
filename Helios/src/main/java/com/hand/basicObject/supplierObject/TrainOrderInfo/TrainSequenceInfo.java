package com.hand.basicObject.supplierObject.TrainOrderInfo;

import lombok.Builder;

/**
 * @Author peng.zhang
 * @Date 2020/9/3
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class TrainSequenceInfo {

    //车次对应的订单号
    private String orderNo;
    //火车票车次序号
    private String sequenceNo;
    //车次号
    private String trainNum;
    //出发时间
    private String departureTime;
    //到达时间
    private String arriveTime;
    //出发城市名称
    private String dCityName;
    //出发城市时间code
    private String dCityCode;
    //出发火车站名称
    private String dStationName;
    //到达城市名称
    private String aCityName;
    //到达城市时间code
    private String aCityCode;
    //到达火车站名称
    private String aStationName;

}

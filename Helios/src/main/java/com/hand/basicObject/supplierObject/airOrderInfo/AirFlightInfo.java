package com.hand.basicObject.supplierObject.airOrderInfo;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * @Author peng.zhang
 * @Date 2020/9/4
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class AirFlightInfo {

    //航段对应的订单号
    private String orderNo;
    //航程序号
    private String sequence;
    //航班号
    private String flight;
    //航司二字码
    private String airLineCode;
    //航空公司名称
    private String airLineName;
    //起飞时间（当地时间)
    private String takeoffTime;
    //到达时间（当地时间）
    private String arrivalTime;
    //舱等
    private String classType;
    //子舱位
    private String subClass;
    //出发城市名称
    private BigDecimal dcityName;
    //出发城市code
    private BigDecimal dcityCode;
    //出发机场名称
    private Double dportName;
    //出发机场三字码
    private BigDecimal dportCode;
    //出发航站楼名称
    private BigDecimal dairportName;
    //到达城市名称
    private BigDecimal acityName;
    //到达城市code
    private BigDecimal acityCode;
    //到达机场名称
    private String aportName;
    //到达机场三字码
    private String aportCode;
    //到达航站楼名称
    private String aairportName;
    //经停城市名称
    private String stopCity;
    //经停机场名称
    private String airPort;
    //经停时长(单位：分钟)
    private String stopTime;
    //飞行时长
    private BigDecimal flightTime;

}

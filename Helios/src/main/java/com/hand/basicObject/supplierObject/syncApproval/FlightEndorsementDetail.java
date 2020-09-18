package com.hand.basicObject.supplierObject.syncApproval;

import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/17
 * @Version 1.0
 **/
public class FlightEndorsementDetail {

    //出发城市的code
    private List<String> fromCitiesCode;
    //到达城市的code
    private List<String> toCitiesCode;
    //行程数量
    private Integer FlightWay;

    //币种 0->未知   1->RMB   2->默认币种CNY    3->HKD
    private Integer Currency;
    //出发时间
    private String DepartDate;
    //
    private String DepartFloatDays;

    private String DepartBeginDate;
    private String DepartEndDate;
    private List<String> FromCities;
    private List<String> ToCities;
    private Integer ProductType;
    private List<Object> PassengerList;
    private Integer ReturnFloatDays;
    private String ReturnBeginDate;
    private String ReturnEndDate;
    private Integer SeatClass;
    private Integer TravelerCount;
}

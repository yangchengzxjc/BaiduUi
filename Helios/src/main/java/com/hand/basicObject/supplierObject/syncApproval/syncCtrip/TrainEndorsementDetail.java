package com.hand.basicObject.supplierObject.syncApproval.syncCtrip;

import lombok.Data;

import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/17
 * @Version 1.0
 **/
@Data
public class TrainEndorsementDetail {

    //出发城市的code
    private List<String> fromCitiesCode;
    //到达城市的code
    private List<String> toCitiesCode;
    //行程数量
    //币种 0->未知   1->RMB   2->默认币种CNY    3->HKD
    private Integer Currency;
    private Integer TripType;
    //出发时间
    private String DepartDate;
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
    private List<Integer> SeatType;
    private Integer TravelerCount;
}

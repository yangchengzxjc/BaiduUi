package com.hand.basicObject.itinerary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author peng.zhang
 * @Date 2020/7/7
 * @Version 1.0
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FlightItinerary {
    //飞机行程 是否往返  往返1002   单程1001
    int itineraryType;
    //出发城市
    String fromCity;
    //到达城市
    String toCity;
    //出发时间   2020-07-06T16:00:00Z
    String startDate;
    //返回时间
    String endDate;
    //供应商OID
    String supplierOID;
    //起飞时间
    String takeOffBeginTime;
    //落地时间
    String takeOffEndTime;
    //返回起飞时间
    String arrivalBeginTime;
    //返回落地时间
    String arrivalEndTime;
    //账户
    String discount;
    //舱等 经济舱、公务舱、头等舱、超级经济舱
    String seatClass;
    //出发城市code
    String fromCityCode;
    // 到达城市code
    String toCityCode;

//    [
//    {
//        "itineraryType":1002,
//            "fromCity":"上海",
//            "toCity":"西安",
//            "startDate":"2020-07-06T16:00:00Z",
//            "endDate":"2020-07-06T16:00:00Z",
//            "remark":"www",
//            "supplierOID":null,
//            "discount":"",
//            "fromCityCode":"CHN031000000",
//            "toCityCode":"CHN061001000"
//    }
//]
}

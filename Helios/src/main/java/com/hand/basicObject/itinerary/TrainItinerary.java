package com.hand.basicObject.itinerary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author peng.zhang
 * @Date 2020/7/9
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
public class TrainItinerary {
    //出发城市
    String fromCity;
    //到达城市
    String toCity;
    //出发时间   2020-07-06T16:00:00Z
    String startDate;
    //供应商
    String supplierOID;
    //出发城市code
    String fromCityCode;
    // 到达城市code
    String toCityCode;

}

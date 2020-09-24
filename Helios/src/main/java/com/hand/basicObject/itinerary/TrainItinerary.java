package com.hand.basicObject.itinerary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author peng.zhang
 * @Date 2020/7/9
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainItinerary {
    //出发城市
    private String fromCity;
    //到达城市
    private String toCity;
    //出发时间   2020-07-06T16:00:00Z
    private String startDate;
    //供应商
    private String supplierOID;
    //出发城市code
    private String fromCityCode;
    // 到达城市code
    private String toCityCode;
    //价格
    private BigDecimal ticketPrice;
    private String seatClassCode;
    private String seatClass;


}

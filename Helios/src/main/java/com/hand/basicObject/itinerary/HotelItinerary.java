package com.hand.basicObject.itinerary;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author peng.zhang
 * @Date 2020/7/7
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
public class HotelItinerary {
    //入住城市
    private String cityName;
    //房间数量
    private Integer roomNumber;
    //入住日期   格式为：2020-07-06T16:00:00Z
    private String fromDate;
    //退房日期   格式为：2020 -08-24T16:00:00Z
    private String leaveDate;
    //供应商OID
    private String supplierOID;
    //入住城市code
    private String cityCode;
    private BigDecimal maxPrice;


//  例：[
//    {
//        "cityName":"上海",
//            "roomNumber":1,
//            "fromDate":"2020-07-06T16:00:00Z",
//            "leaveDate":"2020-07-07T16:00:00Z",
//            "remark":"111",
//            "supplierOID":null,
//            "cityCode":"CHN031000000"
//    }
//]
}

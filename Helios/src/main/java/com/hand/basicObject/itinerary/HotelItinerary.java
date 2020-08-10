package com.hand.basicObject.itinerary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author peng.zhang
 * @Date 2020/7/7
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelItinerary {
    //入住城市
    String cityName;
    //房间数量
    int roomNumber;
    //入住日期   2020-07-06T16:00:00Z
    String fromDate;
    //退房日期
    String leaveDate;
    //供应商OID
    String supplierOID;
    //入住城市code
    String cityCode;


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

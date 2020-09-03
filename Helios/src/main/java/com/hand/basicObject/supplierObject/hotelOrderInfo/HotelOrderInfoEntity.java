package com.hand.basicObject.supplierObject.hotelOrderInfo;

import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/3
 * @Version 1.0
 **/
public class HotelOrderInfoEntity {

    //订单基本信息
    private HotelBaseOrder hotelBaseOrder;
    //订单超标信息
    private List<HotelExceedInfo> HotelExceedInfo;
    //订单入住信息
    private List<HotelPassengerInfo> hotelPassengerInfos;




}

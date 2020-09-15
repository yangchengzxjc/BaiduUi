package com.test.api.method.VendorMethod;

import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelExceedInfo;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelPassengerInfo;

import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/14
 * @Version 1.0
 **/
public class HotelOrder {


    /**
     * 酒店订单超标
     * @param orderNo
     * @return
     */
    public HotelExceedInfo setHotelExceedInfo(String orderNo){
        //超标信息
        HotelExceedInfo hotelExceedInfo =HotelExceedInfo.builder()
                .orderNo(orderNo)
                .violationContentCode("A123")
                .violationContentName("酒店价格标准")
                .violationReasonName("距离原因")
                .violationReasonCode("B123")
                .build();
        return hotelExceedInfo;
    }

    /**
     * 订单乘客信息
     * @param orderNo
     * @param passengerNo
     * @param passengerAttribute
     * @param passengerName
     * @param passengerNum
     * @param departmentName
     * @param passengerDepartments
     * @return
     */
    public HotelPassengerInfo setHotelPassengerInfo(String orderNo, String passengerNo, String passengerAttribute, String passengerName,
                                                    String passengerNum, String departmentName, List<String> passengerDepartments) {
        HotelPassengerInfo hotelPassengerInfo = HotelPassengerInfo.builder()
                .orderNo(orderNo)
                .passengerNo(passengerNo)
                .passengerAttribute(passengerAttribute)
                .passengerName(passengerName)
                .passengerNum(passengerNum)
                .departmentName(departmentName)
                .passengerDepartments(passengerDepartments)
                .build();
        return hotelPassengerInfo;
    }

}

package com.test.api.method.VendorMethod;

import com.google.gson.JsonObject;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelBaseOrder;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelExceedInfo;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelPassengerInfo;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    /**
     * 酒店订单
     * @param employee
     * @param orderType
     * @param orderNo
     * @param supplierName
     * @param supplierCode
     * @param tmcdata
     * @param applicat
     * @param city
     * @param passengerName
     * @param cityCode 城市code
     * @return
     */
    public HotelBaseOrder setHotelBaseOrder(Employee employee,String orderType, String orderNo, String supplierName, String supplierCode, JsonObject tmcdata,JsonObject applicat,String city,String passengerName,String cityCode){
        ArrayList<String> bookerDepartments = new ArrayList<>();
        bookerDepartments.add(applicat.get("departmentName").getAsString());
        HotelBaseOrder hotelBaseOrder = HotelBaseOrder.builder()
                .orderType(orderType)
                .orderNo(orderNo)
                .supplierName(supplierName)
                .supplierCode(supplierCode)
                .approvalCode(tmcdata.get("approvalCode").getAsString())
                .orderStatusName("已提交")
                .orderStatusCode("Submitted")
                .tenantCode(tmcdata.get("tenantId").getAsString())
                .tenantName(employee.getTenantName())
                .employeeId(tmcdata.getAsJsonObject("bookClerk").get("employeeID").getAsString())
                .bookerDepartments(bookerDepartments)
                .employeeName(tmcdata.getAsJsonObject("bookClerk").get("name").getAsString())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(applicat.get("departmentName").getAsString())
                .bookChannel("Online-APP")
                .bookType("C")
                .payType("COPAY")
                .createTime(UTCTime.getBeijingTime(0,0,0))
                .payTime(UTCTime.getBeijingTime(0,0,2))
                .successTime(UTCTime.getBeijingTime(0,0,2))
                .hotelClass("N")
                .paymentType("M")
                .accountType("C")
                .balanceType("P")
                .guaranteeType("N")
                .currency("CNY")
                .totalAmount(new BigDecimal(1000).setScale(2))
                .contactName(tmcdata.getAsJsonObject("bookClerk").get("name").getAsString())
                .contactPhone(tmcdata.getAsJsonObject("bookClerk").get("mobile").getAsString())
                .contactEmail(employee.getEmail())
                .hotelType("AGR")
                .hotelName("爱丽丝酒店")
                .hotelPhone("010-123456")
                .hotelAddress("上海梅川路25弄")
                .hotelStar(3)
                .startTime(UTCTime.getBeijingTime(3,0,0))
                .endTime(UTCTime.getBeijingDate(5)+" 12:00:00")
                .lastCancelTime(UTCTime.getBeijingTime(2,0,0))
                .cityName(city)
                .cityHeliosCode(cityCode)
                //为字符串  多个乘客使用,隔开
                .passengerName(passengerName)
                .roomName("商务套房")
                .roomQuantity(1)
                .roomDays(5)
                .variance(new BigDecimal(0).setScale(2))
                .build();
        return hotelBaseOrder;
    }


}

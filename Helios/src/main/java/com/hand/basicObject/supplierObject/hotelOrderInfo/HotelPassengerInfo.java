package com.hand.basicObject.supplierObject.hotelOrderInfo;

import lombok.Builder;

import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/3
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class HotelPassengerInfo {

    private String orderNo;
    //入住人序号
    private  String passengerNo;
    //乘客属性   I：内部员工   O：外部人员
    private String passengerAttribute;
    //入住人姓名
    private String passengerName;
    //入住人工号
    private String passengerNum;
    //入住人部门编码
    private String departmentCode;
    //入住人部门名称
    private String departmentName;
    //成本中心1
    private String passengerCostCenter;
    //乘客部门集合
    private List<String> passengerDepartments;
}

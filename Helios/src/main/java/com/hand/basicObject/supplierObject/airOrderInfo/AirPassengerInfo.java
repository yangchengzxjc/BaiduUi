package com.hand.basicObject.supplierObject.airOrderInfo;

import lombok.Builder;

import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/4
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class AirPassengerInfo {

    //乘客关联的订单号
    private String orderNo;
    //乘客序号
    private String passengerNo;
    //乘客类型
    private String passengerType;
    //乘客属性    I：内部员工
    //O：外部人员
    private String passengerAttribute;
    //乘客姓名
    private String passengerName;
    //乘客工号
    private String passengerNum;
    private String departmentCode;
    private String departmentName;
    private String nationlityName;
    //证件类型
    private String certificateType;
    private String certificateNum;
    private String passengerPhone;
    private String passengerEmail;
    private String passengerSex;
    private String passengerCostCenter;
    private List<String> passengerDepartments;
}

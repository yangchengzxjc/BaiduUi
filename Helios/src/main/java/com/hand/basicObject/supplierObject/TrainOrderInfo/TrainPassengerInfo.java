package com.hand.basicObject.supplierObject.TrainOrderInfo;

import lombok.Builder;

import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/3
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class TrainPassengerInfo  {
    //订单号
    private String orderNo;
    //乘客序号
    private String passengerNo;
    //乘客类型  AUD：成人   CHD儿童
    private String passengerType;
    //乘客属性    I:内部员工   O：外部人员
    private String passengerAttribute;
    //乘客姓名
    private String passengerName;
    //乘客工号
    private String passengerNum;
    //乘客部门集合
    private List<String> passengerDepartments;
    //乘客成本中心1
    private String passengerCostCenter;
    //乘客成本中心2
    private String passengerCostCenter2;
    //乘客成本中心3
    private String passengerCostCenter3;
    //乘客成本中心4
    private String passengerCostCenter4;
    //乘客成本中心5
    private String passengerCostCenter5;
    //乘客成本中心6
    private String passengerCostCenter6;
    //乘客部门编码
    private String departmentCode;
    //乘客部门名称
    private String departmentName;
    //乘客国籍名称
    private String nationlityName;
    //乘客证件类型   IDC：身份证
    //HRC：回乡证
    //MOC：军人证
    //PAS：护照
    //MTP：台胞证
    //EEP：港澳通行证
    //OTHER：其他
    private String certificateType;
    //乘客证件号
    private String certificateNum;
    //乘客手机号
    private String passengerPhone;
    //乘客邮箱
    private String passengerEmail;
    //乘客性别    M：男  F：女
    private String passengerSex;

}

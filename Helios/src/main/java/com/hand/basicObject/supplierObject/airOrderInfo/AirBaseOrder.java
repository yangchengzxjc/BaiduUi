package com.hand.basicObject.supplierObject.airOrderInfo;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * @Author peng.zhang
 * @Date 2020/9/4
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class AirBaseOrder {
    //订单类型   B：预订
    //R：退票
    //C：改签
    private String orderType;
    //订单号
    private String orderNo;
    //原订单号
    private String originalOrderNo;
    //供应商行程号
    private String tripId;
    //供应商名称
    private String supplierName;
    //供应商代码，区分不同供应商
    private String supplierCode;
    //甄选订单号
    private String platformOrderId;
    //关联审批单号
    private String approvalCode;
    //订单状态描述
    private String orderStatus;
    //订单状态编码
    private String orderStatusCode;
    //预定人集团编码
    private String tenantCode;
    //预定人集团名称
    private String tenantName;
    //预订人工号
    private String employeeId;
    //供应商员工账号
    private String supplierAccount;
    //预订人姓名
    private String preEmployName;
    //预订人公司名称
    private String companyName;
    //预订人公司编码
    private String companyCode;
    //预订人公司oid
    private String companyOid;
    //预订人部门名称
    private String departmentName;
    //预订人部门编码
    private String departmentCode;
    //预定人部门oid
    private String departmentOid;
    //预订渠道
    private String bookChannel;
    //预订类型
    private String bookType;
    //明细支付方式   CCARD：信用卡支付
    //OTHER：第三方支付
    //ALIPAY：支付宝支付
    //WXPAY：微信支付
    //COPAY：公司账户支付
    private String payType;
    //创建时间/订单生成时间
    private String createTime;
    //支付时间/预订时间
    private String payTime;
    //出票时间
    private String successTime;
    //国内/国际
    private String flightClass;
    //航程类型
    private String flightWay;
    //结算方式
    private String paymentType;
    //支付账户类型
    private String accountType;
    //订单成本中心1
    private String costCenter;
    //订单成本中心2
    private String costCenter2;
    //订单成本中心3
    private String costCenter3;
    //订单成本中心4
    private String costCenter4;
    //订单成本中心5
    private String costCenter5;
    //订单成本中心6
    private String costCenter6;
    //自定义字段1（项目号）
    private String defineFlag;
    //自定义字段2（出行目的）
    private String defineFlag2;
    //币种
    private String currency;
    //订单总价
    private BigDecimal amount;
    //订单联系人姓名
    private String contactName;
    //订单联系人电话
    private String contactPhone;
    //订单联系人邮箱
    private String contactEmail;
    //订单备注
    private String remark;

}

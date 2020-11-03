package com.hand.basicObject.supplierObject.carOrderInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class CarBaseInfo {
    //供应商名称
    private String supplierName;
    //供应商代码
    private String supplierCode;
    //供应商公司唯一标识private String corpId;
    //公司名称
    private String companyName;
    //公司code
    private String companyCode;
    //公司oid
    private String companyOid;
    //汇联易集团id
    private String tenantId;
    //汇联易集团编码
    private String tenantCode;
    //汇联易集团名称
    private String tenantName;
    //审批单号
    private String approvalCode;
    //消费商内部审核单号
    private String outApprovalCode;
    //结算对应的订单号
    private String orderNo;
    //订单类型  B：预订,R：退票,C：改签
    private String operateType;
    //订单状态
    private String orderStatusName;
    //订单状态编码
    private String orderStatusCode;
    //预定来源  C：企业版  P：个人版（用于区分订单预定渠道）
    private String bookerSource;
    //预定人名字
    private String bookClerkName;
    //预定人工号
    private String bookClerkEmployeeId;
    //预订人部门名称
    private String departmentName;
    //预订人部门编码
    private String departmentCode;
    //预订类型 C：因公，P：因私
    private String bookType;
    //支付方式  CCARD：信用卡支付,OTHER：第三方支付,ALIPAY：支付宝支付,WXPAY：微信支付,COPAY：公司账户支付,MIXPAY：混付
    private String payType;
    //创建时间
    private String createTime;
    //支付/成交时间
    private String payTime;
    //支付方式 N：现付，M：月结 ，X：混付
    private String paymentType;
    //支付账户类型  C：公司账户，P：个人账户
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
    //出行目的
    private String tripPurpose;
    //项目号
    private String itemNumber;
    //自定义字段1
    private String attribute1;
    //自定义字段2
    private String attribute2;
    //币种 CNY：人民币，默认为人民币
    private String currency;
    //订单总价
    private String totalAmount;
    //订单实付金额
    private String actualPrice;
    //出发地纬度
    private String flat;
    //出发地经度
    private String flng;
    //目的地纬度
    private String tlat;
    //目的地经度
    private String tlng;
    //实际出发地（司机点击开始的位置）
    private String actualStartName;
    //实际目的地（司机点击结束的位置）
    private String actualEndName;
    //实际出发地纬度（司机点击开始的位置）
    private String actualFlat;
    //实际出发地经度（司机点击开始的位置
    private String actualFlng;
    //实际目的地纬度（司机点击结束的位置）
    private String actualTlat;
    //实际目的地经度（司机点击结束的位置）
    private String actualTlng;
    //用车方式
    private String useCarType;
    //车型
    private String requireLevel;
    //出发地地址
    private String startName;
    //目的地地址
    private String endName;
    //出发时间
    private String departureTime;
    //开始计价时间
    private String beginChargeTime;
    //结束计价时间
    private String finishTime;
    //退款时间
    private String refundTime;
    //退款金额
    private String refundPrice;
    //开票状态
    private String invoiceStatus;
    //订单授权状态
    private String orderApprovalStatus;
    //总里程
    private String normalDistance;
    //价格加密信息
    private String encryptedInfo;
    //订单联系人姓名
    private String contactName;
    //订单联系人手机号
    private String contactPhone;
    //订单联系人邮箱
    private String contactEmail;
    //订单备注
    private String remark;
    //违约金
    private String penaltyFee;
    //预定服务费
    private String serviceFee;
    //快递费
    private String expressFee;
    //后收服务费
    private String postServiceFee;
    //预定费用明细
    private Price price;
    //公司支付费用
    private BigDecimal companyPay;
    //个人支付费用
    private BigDecimal personalPay;
    //明细最晚更新时间
    private String lastUpdateTime;
}

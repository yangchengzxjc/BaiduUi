package com.hand.basicObject.supplierObject.TrainOrderInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author peng.zhang
 * @Date 2020/9/3
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class TrainBaseOrder {
    //订单类型  B预定  R退票 C改签
    private String orderType;
    //订单号
    private String orderNo;
    //原订单号   正常预订时为空，退票或改签时有值
    private String originalOrderNum;
    //供应商名称
    private String supplierName;
    //供应商代码，区分不同供应商    携程商旅：CTRIP
    //
    //美亚商旅：MEIYA
    //
    //泛嘉：FANJIA
    private String supplierCode;
    //关联审批单号
    private String approvalCode;
    //订单状态   订单状态描述
    private String orderStatusName;
    //订单状态编码    订单状态编码
    private String orderStatusCode;
    //集团编码
    private String tenantCode;
    //集团名称
    private String tenantName;
    //预订人工号
    private String employeeNum;
    //预订人姓名
    private String employeeName;
    //预订人公司名称
    private String companyName;
    //预订人公司编码
    private String companyCode;
    //预订人部门名称
    private String departmentName;
    //预订人部门编码
    private String departmentCode;
    //预订渠道          Online-PC        网页预定
    //Online-APP      消费商APP预定
    //Online-WX       微信渠道预定
    //Online-Other    其他
    //Online-API       使用消费商分销接口
    //Offline             线下预定
    private String bookChannel;
    //预订类型    C：因公，P：因私
    private String bookType;
    //支付方式     CCARD：信用卡支付
    //OTHER：第三方支付
    //ALIPAY：支付宝支付
    //WXPAY：微信支付
    //COPAY：公司账户支付
    private String payType;
    //创建时间
    private String createTime;
    //支付/成交时间
    private String payTime;
    //出票时间
    private String successTime;
    //支付方式   N：现付，M：月结
    private String paymentType;
    //支付账户类型    C：公司账户，P：个人账户
    private String accountType;
    //订单成本中心1
    private String costCenter;
    //订单成本中心1
    private String costCenter2;
    //订单成本中心3
    private String costCenter3;
    //订单成本中心4
    private String costCenter4;
    //订单成本中心5
    private String costCenter5;
    //订单成本中心6
    private String costCenter6;
    //自定义字段1
    private String attribute1;
    //自定义字段2
    private String attribute2;
    //币种
    private String currency;
    //订单总价    1、正常预订时为正
    //2、改签时为改签需付差价
    //3、退票时为退客户金额，为负数
    private BigDecimal totalAmount;
    //订单联系人姓名
    private String contactName;
    //订单联系人手机号
    private String contactPhone;
    //订单联系人邮箱
    private String contactEmail;
    //订单备注
    private String remark;

}

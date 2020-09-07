package com.hand.basicObject.supplierObject.hotelOrderInfo;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/3
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class HotelBaseOrder {
    //订单类型   B：预订  R：退票
    private String orderType;
    //订单号
    private String orderNo;
    //原订单号   正常预订时为空，退票（取消）时有值
    private String originalOrderNum;
    //供应商名称
    private String supplierName;
    //供应商代码，区分不同供应商
    private String supplierCode;
    //甄选订单号   甄选订单号，甄选渠道预订特有
    private String zxOrderNum;
    //关联审批单号
    private String approvalCode;
    //订单状态
    // 订单状态描述
    //预订单：
    //Submitted：已提交 //用户最终完成订单提交
    //Wait：确认中 //等待酒店确认
    //Cancelled：已取消// 酒店拒单&用户手动取消&用户未付款自动取消
    //Confirmed：已确认 // 酒店已确认订单（生成结算数据）
    //Success: 已成交//订单完成
    //取消订单：
    //CWait：取消中
    //CSuccess：取消成功（生成结算数据）
    //CFail：取消失败
    private String orderStatusName;
    //订单状态编码
    private String orderStatusCode;
    //集团ID
    private String tenantCode;
    //集团名称
    private String tenantName;
    //预订人工号
    private String employeeId;
    //预定人部门集合
    private List<String> bookerDepartments;
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
    //预订渠道  Online-PC
    //Online-APP
    //Online-WX
    //Online-Other
    //Online-API
    //Offline
    //甄选平台模式的机票订单来源为API
    //甄选自营模式的订单来源为：APP/PC
    private String bookChannel;
    //预订类型    C：因公，P：因私
    private String bookType;
    //明细支付方式     CCARD：信用卡支付
    //OTHER：第三方支付
    //ALIPAY：支付宝支付
    //WXPAY：微信支付
    //COPAY：公司账户支付
    //MIXPAY：混付
    //混合支付存在两种支付方式：COPAY ALIPAY（旧值）
    private String payType;
    //创建时间
    private String createTime;
    //支付时间
    private String payTime;
    //出票时间
    private String successTime;
    //国内/国际    I：国际酒店，N：国内酒店
    private String hotelClass;
    //房型结算类型   N：现付 ，C：预存 ，M：月结
    private String paymentType;
    //订单支付类型     C：公司账户，P：个人账户  H：混合支付
    private String accountType;
    //房型支付类型         N:前台现付   P:在线预付
    private String balanceType;
    //担保类型    Y：需担保   N：无需担保
    private String guaranteeType;
    //订单成本中心1
    private String costCenter1;
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
    //自定义字段1
    private String attribute1;
    //自定义字段2
    private String attribute2;
    //币种
    private String currency;
    //订单总价      正常预订为正   取消单时金额为负
    private BigDecimal totalAmount;
    //订单联系人姓名
    private String contactName;
    //订单联系人电话
    private String contactPhone;
    //订单联系人邮箱
    private String contactEmail;
    //订单备注
    private String remark;
    //酒店类型     MEM：会员酒店   AGR：协议酒
    private String hotelType;
    //酒店名称
    private String hotelName;
    //酒店星级
    private Integer hotelStar;
    //酒店电话
    private String hotelPhone;
    //酒店地址
    private String hotelAddress;
    //入住时间
    private String startTime;
    //离店时间
    private String endTime;
    //最晚取消时间
    private String lastCancelTime;
    //订单取消时间
    private String cancelTime;
    //城市名称
    private String cityName;
    //汇联易城市代码
    private String cityHeliosCode;
    //入住人姓名
    private String passengerName;
    //房型名称
    private String roomName;
    //房间数
    private Integer roomQuantity;
    //间夜数
    private Integer roomDays;
    //混付公司账户支付金额
    private BigDecimal companyPayAmount;
    //混付个人账户支付总金额
    private BigDecimal personalPayAmount;
    //差额
    private BigDecimal variance;

}

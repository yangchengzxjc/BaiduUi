package com.hand.basicObject.supplierObject.hotelOrderSettlementInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/8/26
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class HotelOrderSettlementInfo {
    //结算明细主键
    private String recordId;
    //消费商名称
    private String supplierName;
    //消费商代码
    private String supplierCode;
    //消费商公司唯一标识
    private String corpId;
    //公司名称
    private String companyName;
    //公司code
    private String companyCode;
    //公司的oid
    private String companyOid;
    // 租户ID
    private String tenantId;
    //租户编码
    private String tenantCode;
    //租户名称
    private String tenantName;
    //汇联易审批单号
    private String approvalCode;
    //批次号 消费商约定的应结账期的标示。用于客户统计最终的应付明细。批次生成逻辑：supplierCode_corpId_产品_结算开始日期，账期内一致
    //举例：CCTRIP_testcomapny_flight_20200501
    private String batchNo;
    //订单号
    private String orderNo;
    //结算生成时间
    private String createTime;
    //预定日期  格式：yyyy-MM-dd HH:mm:s
    private String orderDate;
    //订单明细类型   O预定；R退订
    private String detailType;
    // 酒店 类型 I国际/N国内
    private String hotelClass;
    //支付类型   1公司支付  2个人支付 （如果酒店是混付需要单独拆开的两条数据，一条明细数据只有一种支付方式）
    private String payType;
    //酒店支付类型   前台现付/预付
    private String balanceType;
    //房间夜数
    private String quantity;
    //房间数量
    private Integer roomQuantity;
    //房费总额
    private BigDecimal roomTotalRate;
    //每日单价
    private BigDecimal price;
    //服务费
    private BigDecimal serviceFee;
    //手续费
    private BigDecimal serviceChargeFee;
    //结算币种
    private String currency;
    //预定来源
    private String reservationSource;
    // 实付金额  （展示公司付部分，现付协议酒店，服务费月结只展示服务费）
    //
    //应付=房费+服务费+手续费。月结适用
    private BigDecimal amount;
    //差额  消费商和客户约定的差额
    private BigDecimal variance;
    //订单类型  月结/现付
    private String orderType;
    //酒店类型  协议/会员
    private String hotelType;
    //入住时间
    private String startTime;
    //离店日期
    private String endTime;
    //酒店名称（中文）
    private String hotelName;
    //酒店名称（英文）
    private String hotelNameEN;
    //房型名称
    private String roomName;
    //城市名称
    private String cityName;
    //酒店所在城市的名称（英文）
    private String cityNameEN;
    //酒店星级
    private String star;
    //是否包含餐食
    private Boolean meals;
    //预定人姓名
    private String bookClerkName;
    //预订人工号
    private String bookClerkEmployeeId;
    //预定卡部门合集
    private List<String> bookClerkDepts;
    //入住人信息列表
    private List<PassengerInfo> passengerList;
    // 成本中心
    private String costCenter1;
    private String costCenter2;
    private String costCenter3;
    private String costCenter4;
    private String costCenter5;
    private String costCenter6;
}

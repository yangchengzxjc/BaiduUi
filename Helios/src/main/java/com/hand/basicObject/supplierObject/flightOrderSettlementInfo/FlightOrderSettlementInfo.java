package com.hand.basicObject.supplierObject.flightOrderSettlementInfo;

import lombok.*;

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
@Builder(toBuilder=true)
public class FlightOrderSettlementInfo {
    //77个参数
    //结算明细主键	  此条结算信息的唯一标识
    private String recordId;
    // 供应商名称 否必填
    private String supplierName;
    //供应商代码  否必填
    private String supplierCode;
    //供应商公司唯一标识
    private String corpId;
    //公司名称 消费商内的账单所属汇联易公司
    private String companyName;
    //公司code  消费商内的账单所属汇联易公司编码
    private String companyCode;
    //公司oid
    private String companyOid;
    //租户ID
    private String tenantId;
    //租户code
    private String tenantCode;
    //租户名称
    private String tenantName;
    //审批单号
    private String approvalCode;
    //批次号  消费商约定的应结账期的标示。用于客户统计最终的应付明细。批次生成逻辑：supplierCode_corpId_产品_结算开始日期，账期内一致
    //举例：CCTRIP_testcomapny_flight_20200501
    private String accBalanceBatchNo;
    //订单号
    private String orderNo;
    //原单号
    private String originalOrderNo;
    //结算数据生成时间
    private String createTime;
    //预定日期
    private String orderDate;
    //抵扣金额
    private BigDecimal deductibleFee;
    //不可抵扣金额
    private BigDecimal nondeductibleFee;
    //退改签类型   出票/改签/退票
    private String detailType;
    // 订单类型   1公司支付     2个人支付
    private Integer orderType;
    //预定来源  线上/线下
    private String reservationSource;
    //机票价格
    private BigDecimal price;
    //差额 消费商提供的客户约定的票价差额
    private BigDecimal variance;
    //民航基金税
    private BigDecimal tax;
    //燃油费
    private BigDecimal oilFee;
    //机票配送费
    private BigDecimal sendTicketFee;
    //保险费
    private BigDecimal insuranceFee;
    //预定服务费
    private BigDecimal serviceFee;
    //后收服务费
    private BigDecimal postServiceFee;
    //返点
    private BigDecimal rebate;
    //退票服务费
    private BigDecimal refundServiceFee;
    //退票费
    private BigDecimal refundFee;
    //应收金额
    private BigDecimal amount;
    //改签费用
    private BigDecimal rebookQueryFee;
    //改签手续费   航司收取
    private BigDecimal dateChangeFee;
    //改签差价
    private BigDecimal priceDifferential;
    //改签服务费  供应商收取
    private BigDecimal reBookingServiceFee;
    // 币种
    private String currency;
    //航程编号  针对多段机票
    private String sequence;
    //起飞时间  格式：yyyy-MM-dd HH:mm:ss
    private String takeOffTime;
    //到达时间
    private String arrivalTime;
    //出发城市名称
    private String dcityName;
    //出发城市code
    private String dcityCode;
    //到达城市名称
    private String acityName;
    //到达城市code
    private String acityCode;
    //出发机场
    private String dportName;
    //到达机场
    private String aportName;
    //航司名称
    private String airlineName;
    //航班号
    private String flightNo;
    //折扣
    private String priceRate;
    //物理仓位   F：头等  B 公务   SY：超级经济   Y：经济
    private String className;
    //差标
    private String travelingStandard;
    //预定人员姓名
    private String bookClerkName;
    //预定人员工号
    private String bookClerkEmployeeId;
    // 预定卡部门  有顺序时为空的请传空字符串，保证顺序正确。
    //
    //如：部门1 = xx公司、部门3 = xx项目
    //
    //传值时需要传 ["xx公司","","xx项目"]
    private List<String> bookClerkDept;
    //乘客姓名
    private String passengerName;
    //乘客工号
    private String passengerEmployeeId;
    //乘客部门
    private List<String> passengerDept;
    //员工成本中心 否
    private String passengerCostCenter1;
    private String passengerCostCenter2;
    private String passengerCostCenter3;
    private String passengerCostCenter4;
    private String passengerCostCenter5;
    private String passengerCostCenter6;
    //  机票票号   13位
    private String ticketNo;
    //改签原票号   第一次改签的票号
    private String firstTicketNo;
    // 改签上一次票号
    private String lastTicketNo;
    //票号状态
    private String ticketNoStatus;
    //机票类型	  I国际/N国内
    private String flightClass;
    //行程单打印状态	机票行程单信息
    private String printStatus;
    //行程单打印时间
    private String printTime;
    //单据成本中心
    private String costCenter1;
    private String costCenter2;
    private String costCenter3;
    private String costCenter4;
    private String costCenter5;
    private String costCenter6;
    private List<AirExceedInfo> airExceedInfo;
}

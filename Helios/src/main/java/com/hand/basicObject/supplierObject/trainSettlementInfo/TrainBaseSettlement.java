package com.hand.basicObject.supplierObject.trainSettlementInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author peng.zhang
 * @Date 2020/8/26
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder=true)
public class TrainBaseSettlement {

    //结算明细主键
    private String recordId;
    //供应商名称
    private String supplierName;
    //供应商代码
    private String supplierCode;
    //审批单号
    private String approvalCode;
    //批次号
    private String accBalanceBatchNo;
    //结算对应的订单号
    private String orderNo;
    //订单类型   B：预定  R：退票  C:改签
    private String operateType;
    //票面价  两位小数
    private BigDecimal ticketFee;
    //服务费
    private BigDecimal serviceFee;
    //退票费
    private BigDecimal refundFee;
    //保险费
    private BigDecimal insuranceFee;
    //纸质出票费
    private BigDecimal paperFare;
    //快递费
    private BigDecimal expressFee;
    //改签服务费   含后收服务费用
    private BigDecimal changeServiceFee;
    //后收服务费
    private BigDecimal postServiceFee;
    //实际应付
    private BigDecimal amount;
    //抵扣金额
    private BigDecimal deductibleFee;
    //不可抵扣金额
    private BigDecimal nondeductibleFee;
    //明细支付类型      月结/现付
    private String payType;
    //明细最晚更新时间
    private String lastUpdateTime;
    //订单类型    月结火车票/现付火车票
    private String orderType;
    //预定人名字
    private String bookClerkName;
    //预定人工号
    private String bookClerkEmployeeId;
    //结算币种
    private String currency;

}

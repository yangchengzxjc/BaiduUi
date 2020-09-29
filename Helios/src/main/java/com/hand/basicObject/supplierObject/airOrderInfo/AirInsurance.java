package com.hand.basicObject.supplierObject.airOrderInfo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author peng.zhang
 * @Date 2020/9/4
 * @Version 1.0
 **/
@Builder(toBuilder = true)
@Data
public class AirInsurance {
    private String ticketKey;
    private String sequence;
    //总保费
    private BigDecimal insuranceFee;
    //保单状态
    private String insuranceStatus;
    private String insuranceStatusDec;
    //单价
    private BigDecimal insuaranceUnitPrice;
    //保险数量
    private Integer insuaranceQuantity;
    //保险名称
    private String insuaranceName;
    //保险公司名称
    private String insuaranceCompanyName;
}

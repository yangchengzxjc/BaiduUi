package com.hand.basicObject.supplierObject.carOrderInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Price {
    //小费
    private BigDecimal gratuity;
    //动态加价
    private BigDecimal dynamicPrice;
    //起步价格
    private BigDecimal startupPrice;
    //正常行驶距离费
    private BigDecimal mileAgeFee;
    //低速费
    private BigDecimal lowSpeedFee;
    //远途费
    private BigDecimal emptyCarCharge;
    //夜间费
    private BigDecimal nightCharge;
    //路桥费
    private BigDecimal tollCharge;
    //高速费
    private BigDecimal highwayFee;
    //停车费
    private BigDecimal parkFee;
    //等候费
    private BigDecimal waitFee;
    //补足费
    private BigDecimal limitPay;
    //其他费用
    private BigDecimal otherFee;
    //时长费
    private BigDecimal normalTimeFee;
    //服务费
    private BigDecimal discountAfterFee;
    //红包
    private BigDecimal redPacket;
    //调度费
    private BigDecimal dispatchFee;
    //临时加价
    private BigDecimal peakFee;
    //洗车费
    private BigDecimal cleanCarFee;
    //跨城费
    private BigDecimal crossCityFee;
    //优惠金额
    private BigDecimal promotionPay;
}

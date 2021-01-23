package com.hand.basicObject.supplierObject;

import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author peng.zhang
 * @Date 2021/1/19
 * @Version 1.0
 **/
@Data
public class DiDi {

    private BigDecimal actualPrice = RandomNumber.getDoubleNumber(20,150);

    private String beginChargeTime = UTCTime.getBeijingTime(-1,0,0);

    private String budgetCenterId = "0";

    private String budgetItemId = "0";

    private String callPhone;

    private String carType = "快车";

    private String city = "西安市";

    private String companyId;

    private BigDecimal companyPay = new BigDecimal(0);

    private BigDecimal companyRealPay = new BigDecimal(0);

    private int companyRealRefund = 0;

    private String departureTime = UTCTime.getBeijingTime(-1,0,0);

    private String endName = "泰华金茂国际";

    private String finishTime = UTCTime.getBeijingTime(-1,0,30);

    private double flat = 33.33;

    private double flng = 33.33;

    private String isCarpool = "1";

    private boolean isInvoice = false;

    private int normalDistance = 20;

    private String orderCreateTime = UTCTime.getBeijingTime(-1,0,-2);

    private String orderId = "helo" +RandomNumber.getTimeNumber(8);

    private String orderSource = "4";

    private String passengerPhone;

    private String payTime = UTCTime.getBeijingTime(-1,0,30);

    private String payType ;

    private BigDecimal personalPay ;

    private BigDecimal personalRealPay;

    private Double personalRealRefund =0.0;

    private int refundPrice =0;

    private String remark = "滴滴费用测试";

    private String requireLevel= "600";

    private String startName = "大雁塔南广场";

    private String status = "2";

    private double tlat = 33.33;

    private double tlng = 33.33;

    private BigDecimal totalPrice;

    private String type = "0";

    private String useCarConfigId = "1";

    private String useCarType = "3";

    private double voucherPay = 0;
}

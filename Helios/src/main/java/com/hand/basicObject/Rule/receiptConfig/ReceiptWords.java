package com.hand.basicObject.Rule.receiptConfig;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Tolerate;

/**
 * @Author peng.zhang
 * @Date 2021/1/12
 * @Version 1.0
 **/
@Data
@Builder
public class ReceiptWords {
    //发票代码
    private String receiptCode;
    //发票号码
    private String receiptNumber;
    //开票日期
    private String receiptDate;
    //币种
    private String currency;
    //税率
    private double taxRate;
    //税额
    private long taxAmount;
    //不含税金额
    private long feeWithoutTax;
    //校验码
    private String checkCode;
    //价税合计
    private long free;
    //归属人
    private String receiptOwner;
    //人员信息完整性
    private String showUserInfo;
    //是否内部员工
    private String internalStaff;
    //身份证号
    private String idCardNo;
    //是否国内（不含港澳台）旅客运输
    private String domesticPassengers;

    @Tolerate
    public ReceiptWords(){

    }
}

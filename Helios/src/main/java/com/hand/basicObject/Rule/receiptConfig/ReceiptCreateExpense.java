package com.hand.basicObject.Rule.receiptConfig;

import com.google.gson.JsonObject;
import lombok.Data;

/**
 * @Author peng.zhang
 * @Date 2021/1/12
 * @Version 1.0
 **/
@Data
public class ReceiptCreateExpense {

    //优先级
    private int priority =1;
    //发票重复
    private JsonObject duplicatedReceipt;
    //多发票行
    private JsonObject multipleInvoiceLines;
    //连号
    private JsonObject consecutiveReceipt;
    //抬头有误
    private JsonObject invalidTitleReceipt;
    // 发票状态
    private JsonObject cancelledReceipt;
    //是否生成费用
    private JsonObject canGenerateInvoice;
    // 配置租户的code
    private String levelCode;
    private String levelOrgId;
    private String levelOrgName;
}

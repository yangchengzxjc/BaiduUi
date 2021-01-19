package com.hand.basicObject.Rule.receiptConfig;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;

/**
 * @Author peng.zhang
 * @Date 2021/1/19
 * @Version 1.0
 **/
@Data
public class PriceSeperationTax {

    private int priority = 1;

    private JsonObject invoiceLabel;

    private JsonObject businessType;

    private JsonObject allReceiptTaxed;

    private JsonObject enableTax;

    private JsonArray expenseTypeLimit;

    private String levelCode = "TENANT";

    private String levelOrgId;

    private String levelOrgName;
}

package com.test.api.method.ExpenseMethod;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.receiptConfig.ReceiptWords;
import com.test.api.method.ExpenseReportInvoice;

/**
 * @Author peng.zhang
 * @Date 2021/1/12
 * @Version 1.0
 **/
public class ReceiptMethodPage {


    /**
     * 根据费用的invoiceOID 获取费用详情返回费用的发票字段
     * @param employee
     * @param invoiceOID
     * @return
     * @throws HttpStatusException
     */
    public ReceiptWords getInvoiceReceipt(Employee employee, String invoiceOID) throws HttpStatusException {
        ExpenseReportInvoice expenseReportInvoice = new ExpenseReportInvoice();
        JsonObject receiptInfo = expenseReportInvoice.getInvoice(employee,invoiceOID).getAsJsonArray("receiptList").get(0).getAsJsonObject();
        ReceiptWords receiptWords = ReceiptWords.builder()
                .feeWithoutTax(receiptInfo.get("feeWithoutTax").getAsLong())
                .free(receiptInfo.get("fee").getAsLong())
                .taxAmount(receiptInfo.get("tax").getAsLong())
                .taxRate(receiptInfo.get("taxRate").getAsDouble())
                .idCardNo(receiptInfo.get("idCardNo").getAsString())
                .receiptOwner(receiptInfo.get("receiptOwner").getAsString())
                .domesticPassengers(receiptInfo.get("domesticPassengers").getAsString())
                .internalStaff(receiptInfo.get("internalStaff").getAsString())
                .showUserInfo(receiptInfo.get("showUserInfo").getAsString())
                .build();
        return receiptWords;

    }
}

package com.test.api.method.ExpenseMethod;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.receiptConfig.ReceiptCreateExpense;
import com.hand.basicObject.Rule.receiptConfig.ReceiptWords;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.ReceiptControlConfig;

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

    /**
     * 发票生成费用管控项字段设置  目前支持 重复 多发票行 连号 抬头有误
     */
    public JsonObject receiptCreateExpenseControl(String istrue){
        JsonObject controlConfig = new JsonObject();
        if(istrue.equals("Y")){
            controlConfig.addProperty("id","Y");
            controlConfig.addProperty("code","Y");
            controlConfig.addProperty("name","是");
            controlConfig.addProperty("description","是");
        }
        if(istrue.equals("N")){
            controlConfig.addProperty("id","N");
            controlConfig.addProperty("code","N");
            controlConfig.addProperty("name","否");
            controlConfig.addProperty("description","否");
        }
        if(istrue.equals("-")){
            controlConfig.addProperty("id","-");
            controlConfig.addProperty("code","-");
            controlConfig.addProperty("name","未知");
            controlConfig.addProperty("description","未知");
        }
        return controlConfig;
    }

    /**
     * 发票场景化配置 发票重复是否生成费用
     * @param iscan 可选"Y","N"
     */
    public String duplicatedReceipt(Employee employee,String iscan) throws HttpStatusException {
        ReceiptControlConfig receiptControlConfig = new ReceiptControlConfig();
        ReceiptCreateExpense receiptCreateExpense1 = new ReceiptCreateExpense();
        receiptCreateExpense1.setDuplicatedReceipt(receiptCreateExpenseControl("Y"));
        return receiptControlConfig.receiptCreateExpense(employee,receiptCreateExpense1,iscan);
    }

    /**
     * 发票场景化配置 发票状态 未知 可生成费用
     * @return
     */
    public String cancelledReceipt(Employee employee) throws HttpStatusException {
        ReceiptControlConfig receiptControlConfig = new ReceiptControlConfig();
        ReceiptCreateExpense receiptCreateExpense1 = new ReceiptCreateExpense();
        receiptCreateExpense1.setCancelledReceipt(receiptCreateExpenseControl("-"));
        return receiptControlConfig.receiptCreateExpense(employee,receiptCreateExpense1,"Y");
    }

    /**
     * 发票场景化配置 发票连号是否生成费用
     * @param iscan 可选"Y","N"
     */
    public String consecutiveReceipt(Employee employee,String iscan) throws HttpStatusException {
        ReceiptControlConfig receiptControlConfig = new ReceiptControlConfig();
        ReceiptCreateExpense receiptCreateExpense1 = new ReceiptCreateExpense();
        receiptCreateExpense1.setConsecutiveReceipt(receiptCreateExpenseControl("Y"));
        return receiptControlConfig.receiptCreateExpense(employee,receiptCreateExpense1,iscan);
    }

    /**
     * 发票场景化配置 发票抬头有误是否生成费用
     * @param iscan 可选"Y","N"
     */
    public String invalidTitleReceipt(Employee employee,String iscan) throws HttpStatusException {
        ReceiptControlConfig receiptControlConfig = new ReceiptControlConfig();
        ReceiptCreateExpense receiptCreateExpense1 = new ReceiptCreateExpense();
        receiptCreateExpense1.setInvalidTitleReceipt(receiptCreateExpenseControl("Y"));
        return receiptControlConfig.receiptCreateExpense(employee,receiptCreateExpense1,iscan);
    }
}

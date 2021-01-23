package com.hand.basicConstant;

/**
 * @Author peng.zhang
 * @Date 2021/1/5
 * @Version 1.0
 **/


public class ReceiptConfig {

    //费用标签包含公司已付的免贴票  只需传入租户id
    public static final String companyPayNoReceipt = "{\"configRule\":{\"configItemId\":\"1\",\"ruleName\":null,\"description\":null,\"priority\":\"1.00\",\"value\":\"Y\",\"valueDesc\":\"发票、费用上标记为免贴票。员工无需贴发票\",\"valueName\":\"启用\"},\"configRuleFactors\":[{\"factorCode\":\"TENANT\",\"factorValue\":\"%s\",\"factorValueDesc\":null},{\"factorCode\":\"SET_OF_BOOKS\",\"factorValue\":null,\"factorValueDesc\":null},{\"factorCode\":\"COMPANY\",\"factorValue\":null,\"factorValueDesc\":null},{\"factorCode\":\"INVOICE_LABEL\",\"factorValue\":\"COMPANY\",\"factorValueDesc\":\"公司已付\"},{\"factorCode\":\"IS_RECEIPT_PDF_EXIST\",\"factorValue\":\"N\",\"factorValueDesc\":\"N\"},{\"factorCode\":\"ALL_RECEIPT_CHECKPASS\",\"factorValue\":\"N\",\"factorValueDesc\":\"N\"},{\"factorCode\":\"ALL_RECEIPT_VAT\",\"factorValue\":\"N\",\"factorValueDesc\":\"N\"},{\"factorCode\":\"INVOICE_RECEIPT_FREE\",\"factorValue\":null,\"factorValueDesc\":null}]}";
    //费用标签包含无票 的免贴票  只需传入租户id
    public static final String noReceiptNoReceipt = "{\"configRule\":{\"configItemId\":\"1\",\"ruleName\":null,\"description\":null,\"priority\":\"1.00\",\"value\":\"Y\",\"valueDesc\":\"发票、费用上标记为免贴票。员工无需贴发票\",\"valueName\":\"启用\"},\"configRuleFactors\":[{\"factorCode\":\"TENANT\",\"factorValue\":\"%s\",\"factorValueDesc\":null},{\"factorCode\":\"SET_OF_BOOKS\",\"factorValue\":null,\"factorValueDesc\":null},{\"factorCode\":\"COMPANY\",\"factorValue\":null,\"factorValueDesc\":null},{\"factorCode\":\"INVOICE_LABEL\",\"factorValue\":\"W_O_RECEIPT\",\"factorValueDesc\":\"无票\"},{\"factorCode\":\"IS_RECEIPT_PDF_EXIST\",\"factorValue\":\"N\",\"factorValueDesc\":\"N\"},{\"factorCode\":\"ALL_RECEIPT_CHECKPASS\",\"factorValue\":\"N\",\"factorValueDesc\":\"N\"},{\"factorCode\":\"ALL_RECEIPT_VAT\",\"factorValue\":\"N\",\"factorValueDesc\":\"N\"},{\"factorCode\":\"INVOICE_RECEIPT_FREE\",\"factorValue\":null,\"factorValueDesc\":null}]}";
    //电子票免贴票
    public static final String eletronictNoPasteReceipt = "{\"configRule\":{\"configItemId\":\"1\",\"ruleName\":null,\"description\":null,\"priority\":\"1.00\",\"value\":\"Y\",\"valueDesc\":\"发票、费用上标记为免贴票。员工无需贴发票\",\"valueName\":\"启用\"},\"configRuleFactors\":[{\"factorCode\":\"TENANT\",\"factorValue\":\"%s\",\"factorValueDesc\":null},{\"factorCode\":\"SET_OF_BOOKS\",\"factorValue\":null,\"factorValueDesc\":null},{\"factorCode\":\"COMPANY\",\"factorValue\":null,\"factorValueDesc\":null},{\"factorCode\":\"INVOICE_LABEL\",\"factorValue\":null,\"factorValueDesc\":null},{\"factorCode\":\"IS_RECEIPT_PDF_EXIST\",\"factorValue\":\"Y\",\"factorValueDesc\":\"Y\"},{\"factorCode\":\"ALL_RECEIPT_CHECKPASS\",\"factorValue\":\"Y\",\"factorValueDesc\":\"Y\"},{\"factorCode\":\"ALL_RECEIPT_VAT\",\"factorValue\":\"Y\",\"factorValueDesc\":\"Y\"},{\"factorCode\":\"INVOICE_RECEIPT_FREE\",\"factorValue\":null,\"factorValueDesc\":null}]}";
    //报销单抬头不一致设置
    public static final String expenseHeaderConfig = "{\"configRule\":{\"configItemId\":\"4\",\"ruleName\":null,\"description\":null,\"priority\":\"1.00\",\"value\":\"Y\",\"valueDesc\":\"检查报销单内发票抬头和报销单抬头是否一致，不一致则显示“抬头不一致”标签，不影响报销单提交\\n            注：不启用费用抬头管控的发票不参与报销单抬头检查\",\"valueName\":\"启用\"},\"configRuleFactors\":[{\"factorCode\":\"SET_OF_BOOKS\",\"factorValue\":null,\"factorValueDesc\":null},{\"factorCode\":\"COMPANY\",\"factorValue\":null,\"factorValueDesc\":null},{\"factorCode\":\"TENANT\",\"factorValue\":\"%s\",\"factorValueDesc\":null},{\"factorCode\":\"EXPENSE_REPORT_TITLE_VALIDATE\",\"factorValue\":null,\"factorValueDesc\":null}]}";
    // 场景化的抬头不一致配置
    public static final String expenseTitleConfig = "{\"receiptCheckOptId\":\"%s\",\"invoiceTitleValidate\":%s,\"receiptTypeList\":[{\"id\":\"04\",\"code\":\"04\",\"name\":\"增值税普通发票\"},{\"id\":\"10\",\"code\":\"10\",\"name\":\"增值税普通发票(电子)\"},{\"id\":\"11\",\"code\":\"11\",\"name\":\"增值税普通发票(卷式)\"},{\"id\":\"01\",\"code\":\"01\",\"name\":\"增值税专用发票\"},{\"id\":\"08\",\"code\":\"08\",\"name\":\"增值税专用发票(电子)\"},{\"id\":\"14\",\"code\":\"14\",\"name\":\"增值税电子普通发票（通行费）\"}],\"titleValidateDetail\":\"Y\",\"titleValidateDetailDesc\":\"启用抬头、税号校验\",\"titleValidateRange\":\"TENANT\",\"corporateTitleValidateRange\":\"DEFAULT\",\"titleValidateMode\":[\"FULL_HALF_WIDTH_NO_CHECK\"],\"expenseReportTitleValidate\":%s,\"noInvoiceTitleValidate\":[\"INSURANCE\",\"COMMUNICATION\"]}";
    //发票连号配置开启
    public static final String receiptConsecutive = "{\"receiptCheckOptId\":\"%s\",\"receiptConsecutiveControl\":%s,\"consecutiveAbsolute\":1,\"consecutiveCheck\":\"COMPANY\"}";
   // 他人发票校验
    public static final String checkOtherReceipt = "{\"createdDate\":\"2020-11-24T09:43:17Z\",\"lastModifiedDate\":\"2020-11-24T09:43:17Z\",\"createdBy\":\"%s\",\"lastModifiedBy\":\"%s\",\"receiptCheckOptId\":1660,\"tenantId\":\"%s\",\"levelCode\":\"TENANT\",\"levelOrgId\":\"%s\",\"checkNonEmployee\":%s,\"receiptOwnerCheck\":%s,\"receiptDuplicateCheck\":true,\"receiptExpenseMapping\":true,\"formallySubmitOverTimeCheck\":true,\"enableOverTimeCheckForChooseRole\":\"ALL\",\"publicUsage\":false,\"deleted\":false,\"levelOrgName\":\"%s\",\"readOnly\":false,\"companyNameList\":[\"%s\"],\"companies\":[\"%s\"],\"system\":false}";

}

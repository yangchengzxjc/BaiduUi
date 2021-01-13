package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.api.ReceiptControlApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicConstant.ReceiptConfig;
import com.hand.basicConstant.Receript;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.receiptConfig.ReceiptCreateExpense;
import com.hand.basicObject.Rule.receiptConfig.ReceiptOverTime;
import com.hand.utils.GsonUtil;
import com.hand.utils.UTCTime;
import lombok.extern.slf4j.Slf4j;

import java.net.CacheRequest;
import java.text.ParseException;

/**
 * @Author peng.zhang
 * @Date 2021/1/5
 * @Version 1.0
 **/
@Slf4j
public class ReceiptControlConfig {

    private ReceiptControlApi receiptControlApi;

    public ReceiptControlConfig(){
        receiptControlApi = new ReceiptControlApi();
    }

    /**
     * 免贴票管控配置
     * @param employee
     * @param invoiceLabel
     */
    public void noPasteReceipt(Employee employee,String invoiceLabel) throws HttpStatusException {
        receiptControlApi.noPasteReceipt(employee,invoiceLabel);
    }

    /**
     * 发票管控配置
     * @param employee
     * @param configRule
     */
    public void receiptConfig(Employee employee,String configRule) throws HttpStatusException {
        receiptControlApi.receiptConfig(employee,configRule);
    }

    /**
     * 删除所有的发票规则
     * @param employee
     * @param itemId
     * @return
     * @throws HttpStatusException
     */
    public void deleteConfigItem(Employee employee,String itemId) throws HttpStatusException {
        //查询所有的规则
        JsonArray configRules = receiptControlApi.getConfigItem(employee,itemId);
        if(GsonUtil.isNotEmpt(configRules)){
            for(int i= 0; i<configRules.size();i++){
                String configItemId = configRules.get(i).getAsJsonObject().getAsJsonObject("configRule").get("id").getAsString();
                receiptControlApi.deleteConfig(employee,configItemId);
            }
        }else{
            throw new RuntimeException("无可删除的规则");
        }
    }

    /**
     * 获取发票配置详情
     * @param employee
     * @param itemId
     * @throws HttpStatusException
     */
    public void getReceiptConfigItem(Employee employee,String itemId) throws HttpStatusException {
        JsonArray configRules = receiptControlApi.getConfigItem(employee,itemId);
    }

    /**
     * 发票连号 配置规则 默认连号绝对值为1
     * @param employee
     * @param configRule
     * @param isOpen 是否开启
     * @throws HttpStatusException
     */
    public void receiptConsecutiveConfig(Employee employee, String configRule, boolean isOpen) throws HttpStatusException {
        int receiptCheckOptId = getReceiptCheckOptId(employee);
        receiptControlApi.receiptConsecutiveConfig(employee,String.format(configRule,receiptCheckOptId,isOpen));
    }

    /**
     * 他人发票归属人校验
     * @param employee
     * @param checkNonEmployee
     * @param receiptOwnerCheck
     * @throws HttpStatusException
     */
    public void checkOtherReceiptConfig(Employee employee,boolean checkNonEmployee, boolean receiptOwnerCheck) throws HttpStatusException {
        JsonObject config= receiptControlApi.getReceiptConfig(employee).get(0).getAsJsonObject();
        config.addProperty("checkNonEmployee",checkNonEmployee);
        config.addProperty("receiptOwnerCheck",receiptOwnerCheck);
        receiptControlApi.checkOtherReceiptConfig(employee,config);
    }

    /**
     * 获取receiptCheckOptId
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public Integer getReceiptCheckOptId(Employee employee) throws HttpStatusException {
        JsonArray configlist = receiptControlApi.getReceiptConfig(employee);
        return configlist.get(0).getAsJsonObject().get("receiptCheckOptId").getAsInt();
    }

    /**
     * 场景化的发票抬头校验
     * @param employee
     * @param invoiceTitleValidate
     * @param expenseReportTitleValidate
     * @return
     */
    public void receiptTitleConfig (Employee employee,boolean invoiceTitleValidate,boolean expenseReportTitleValidate) throws HttpStatusException {
        String receiptCheckOptId = String.valueOf(getReceiptCheckOptId(employee));
        JsonObject configBody = new JsonParser().parse(String.format(ReceiptConfig.expenseTitleConfig,receiptCheckOptId,invoiceTitleValidate,expenseReportTitleValidate)).getAsJsonObject();
        receiptControlApi.configExpenseTitle(employee,configBody);
    }

    /**
     * 发票逾期设置
     * @param employee
     * @param receiptOverTime
     * @throws HttpStatusException
     */
    public String receiptOverTime(Employee employee, ReceiptOverTime receiptOverTime) throws HttpStatusException {
        receiptOverTime.setCompanyOid(employee.getCompanyOID());
        if(!receiptOverTime.isStaticCalibrationFlag()){
            receiptOverTime.setDynamicRuleFailureMonth(receiptOverTime.getDynamicRuleFailureTime().split("-")[0]);
            receiptOverTime.setDynamicRuleFailureDay(receiptOverTime.getDynamicRuleFailureTime().split("-")[1]);
        }
        String configId = receiptControlApi.receiptOverTime(employee,String.valueOf(getReceiptCheckOptId(employee)),receiptOverTime).get("id").getAsString();
        return configId;
    }

    /**
     * 删除逾期校验规则
     * @param employee
     * @param configId
     * @throws HttpStatusException
     */
    public void deleteReceiptConfig(Employee employee,String configId) throws HttpStatusException {
        receiptControlApi.deleteReceiptConfig(employee,configId);
    }

    /**
     * 发票生成费用配置
     * @param employee
     * @param receiptCreateExpense
     * @param createInvoice 是否生成费用 仅可选"Y","N"
     * @return
     * @throws HttpStatusException
     */
    public String receiptCreateExpense(Employee employee, ReceiptCreateExpense receiptCreateExpense,String createInvoice) throws HttpStatusException {
        receiptCreateExpense.setLevelOrgName(employee.getTenantName());
        receiptCreateExpense.setLevelCode("TENANT");
        receiptCreateExpense.setLevelOrgId(employee.getTenantId());
        receiptCreateExpense.setPriority(1);
//        if(receiptCreateExpense.getDuplicatedReceipt()!=null){
//            receiptCreateExpense.setCanGenerateInvoice(canGenerateInvoice(employee,receiptCreateExpense.getDuplicatedReceipt().get("code").getAsString(),"3006"));
//        }
//        if(receiptCreateExpense.getConsecutiveReceipt()!=null){
//            receiptCreateExpense.setCanGenerateInvoice(canGenerateInvoice(employee,receiptCreateExpense.getConsecutiveReceipt().get("code").getAsString(),"3006"));
//        }
//        if(receiptCreateExpense.getInvalidTitleReceipt()!=null){
//            receiptCreateExpense.setCanGenerateInvoice(canGenerateInvoice(employee,receiptCreateExpense.getInvalidTitleReceipt().get("code").getAsString(),"3006"));
//        }
//        if(receiptCreateExpense.getMultipleInvoiceLines()!=null){
//            receiptCreateExpense.setCanGenerateInvoice(canGenerateInvoice(employee,receiptCreateExpense.getMultipleInvoiceLines().get("code").getAsString(),"3006"));
//        }
//        if(receiptCreateExpense.getCancelledReceipt()!=null){
//            //默认发票任何状态都可以生成费用
//            receiptCreateExpense.setCanGenerateInvoice(canGenerateInvoice(employee,"Y","3006"));
//        }
        if(createInvoice.equals("Y") || createInvoice.equals("N")){
            receiptCreateExpense.setCanGenerateInvoice(canGenerateInvoice(employee,createInvoice,"3006"));
        }else{
            throw new RuntimeException("createInvoice参数错误请检查");
        }
        String receiptToInvoiceOptId = receiptControlApi.receiptCreateExpense(employee,receiptCreateExpense).get("receiptToInvoiceOptId").getAsString();
        return receiptToInvoiceOptId;
    }

    /**
     * 删除发票生成费用规则
     */
    public void deleteReceiptCreateExpense(Employee employee,String receiptToInvoiceOptId) throws HttpStatusException {
        receiptControlApi.deleteReceiptCreateExpenseConfig(employee,receiptToInvoiceOptId);
    }

    /**
     * 返回对应的
     * @param employee
     * @param isCan
     * @param listCode
     * @return
     * @throws HttpStatusException
     */
    public JsonObject canGenerateInvoice(Employee employee,String isCan,String listCode) throws HttpStatusException {
        JsonArray dataList = receiptControlApi.getDataList(employee,listCode);
        return GsonUtil.getJsonValue(dataList,"code",isCan);
    }

    /**
     * 构造一个假的发票
     * @param invoiceNumber
     * @param checkCode
     * @param invoiceDate
     * @return
     * @throws ParseException
     */
    public String getHandReceipt(String invoiceNumber,String checkCode,String invoiceDate) throws ParseException {
        return String.format(Receript.HANDRECEIPT4,invoiceNumber,checkCode,invoiceNumber,invoiceDate,UTCTime.getTimeStamp(invoiceDate));
    }
}

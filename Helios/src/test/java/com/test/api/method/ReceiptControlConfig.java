package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.ReceiptControlApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.utils.GsonUtil;

/**
 * @Author peng.zhang
 * @Date 2021/1/5
 * @Version 1.0
 **/
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
}

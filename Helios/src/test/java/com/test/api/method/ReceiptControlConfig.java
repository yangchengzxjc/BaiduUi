package com.test.api.method;

import com.google.gson.JsonArray;
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
}

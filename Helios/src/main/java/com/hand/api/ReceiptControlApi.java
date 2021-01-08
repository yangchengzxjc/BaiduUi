package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicConstant.ApiPath;
import com.hand.basicConstant.HeaderKey;
import com.hand.basicConstant.ReceiptConfig;
import com.hand.basicConstant.ResourceId;
import com.hand.basicObject.Employee;
import com.hand.utils.UTCTime;

import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2021/1/5
 * @Version 1.0
 **/
public class ReceiptControlApi extends BaseRequest{


    /**
     * 免贴票配置
     * @param employee
     * @param expenseLabel
     * @throws HttpStatusException
     */
    public void noPasteReceipt(Employee employee,String expenseLabel) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.NO_PASTE_RECEIPT;
        HashMap<String,String> parm = new HashMap<>();
        parm.put("roleType","TENANT");
        JsonObject body = new JsonObject();
        if(expenseLabel.equals("公司已付")){
            body = new JsonParser().parse(String.format(ReceiptConfig.companyPayNoReceipt,employee.getTenantId())).getAsJsonObject();
        }
        if(expenseLabel.equals("无票")){
            body = new JsonParser().parse(String.format(ReceiptConfig.noReceiptNoReceipt,employee.getTenantId())).getAsJsonObject();
        }
        if(expenseLabel.equals("电子票")){
            body = new JsonParser().parse(String.format(ReceiptConfig.eletronictNoPasteReceipt,employee.getTenantId())).getAsJsonObject();
        }
        doPost(url,getHeader(employee.getAccessToken()),parm,body.toString(),null,employee);
    }


    /**
     * 发票管控配置
     * @param employee
     * @param
     * @throws HttpStatusException
     */
    public void receiptConfig(Employee employee,String configRule) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.NO_PASTE_RECEIPT;
        HashMap<String,String> parm = new HashMap<>();
        parm.put("roleType","TENANT");
        JsonObject body = new JsonParser().parse(String.format(configRule,employee.getTenantId())).getAsJsonObject();
        doPost(url,getHeader(employee.getAccessToken()),parm,body.toString(),null,employee);
    }

    /**
     * 查询配置的发票管控项
     * @param employee
     * @param itemId "1" 为 免贴票管控 "2"报销单打印管控 以此类推
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getConfigItem(Employee employee,String itemId) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.GET_RECEIPT_CONFIG;
        JsonObject body = new JsonObject();
        JsonArray itemIds = new JsonArray();
        itemIds.add(itemId);
        body.addProperty("page","0");
        body.addProperty("size","1000");
        body.add("itemIds",itemIds);
        body.addProperty("tenantId",employee.getTenantId());
        body.addProperty("getDetail","Y");
        HashMap<String,String> parm = new HashMap<>();
        parm.put("roleType","TENANT");
        String res = doPost(url,getHeader(employee.getAccessToken()),parm,body.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject().getAsJsonArray("rows").get(0).getAsJsonObject().getAsJsonArray("configRules");
    }

    /**
     * 删除发票配置
     * @param employee
     * @param configItemId
     * @throws HttpStatusException
     */
    public void deleteConfig(Employee employee,String configItemId) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + String.format(ApiPath.DELETE_CONFIG,configItemId);
        HashMap<String,String> parm = new HashMap<>();
        parm.put("roleType","TENANT");
        doDlete(url,getHeader(employee.getAccessToken()),parm,new JsonObject(),employee);
    }

    /**
     * 发票连号配置规则
     * @param employee
     * @param receiptConfig 配置规则
     */
    public void receiptConsecutiveConfig(Employee employee,String receiptConfig) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.NEW_RECEIPT_CONFIG;
        HashMap<String,String> parm = new HashMap<>();
        parm.put("roleType","TENANT");
        JsonObject body = new JsonParser().parse(receiptConfig).getAsJsonObject();
        doPost(url,getHeader(employee.getAccessToken(), HeaderKey.INVOICE_CONTROL, ResourceId.INVOICE_CONTROL),parm,body.toString(),null,employee);
    }


    /**
     * 他人发票校验
     * @param employee
     * @param checkNonEmployee
     * @param receiptOwnerCheck
     */
    public void checkOtherReceiptConfig(Employee employee,JsonObject config) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.OTHER_RECEIPT_CONFIG;
        HashMap<String,String> parm = new HashMap<>();
        parm.put("roleType","TENANT");
//        JsonObject body = new JsonParser().parse(String.format(ReceiptConfig.checkOtherReceipt,employee.getUserOID(),employee.getUserOID(),employee.getTenantId(),employee.getTenantId(),checkNonEmployee,receiptOwnerCheck,employee.getTenantName(),employee.getCompanyName(),employee.getCompanyId())).getAsJsonObject();
        doPost(url,getHeader(employee.getAccessToken(), HeaderKey.INVOICE_CONTROL, ResourceId.INVOICE_CONTROL),parm,config.toString(),null,employee);
    }

    /**
     * 获取发票场景配置规则
     * @param employee
     * @return
     */
    public JsonArray getReceiptConfig(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.OTHER_RECEIPT_CONFIG;
        HashMap<String,String> parm = new HashMap<>();
        parm.put("roleType","TENANT");
        parm.put("page","0");
        parm.put("size","20");
        parm.put("companyIds","");
        String response = doGet(url,getHeader(employee.getAccessToken(), HeaderKey.INVOICE_CONTROL, ResourceId.INVOICE_CONTROL),parm,employee);
        return new JsonParser().parse(response).getAsJsonArray();
    }

}

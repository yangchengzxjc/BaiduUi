package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.api.VendorApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SettlementBody;
import com.hand.utils.GsonUtil;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;

/**
 * @Author peng.zhang
 * @Date 2020/8/26
 * @Version 1.0
 **/
public class Vendor {

    private VendorApi vendorApi;

    public Vendor(){
        vendorApi =new VendorApi();
    }

    /**
     *消费商数据推送
     * @param employee
     * @param type  结算类型  可选 filght train hotel
     * @param object  机票  酒店  火车等结算对象
     * @param appName  汇联易消费商注册的应用名称
     * @param corpId  消费商开通的公司Id
     * @param passWord
     * @return
     * @throws HttpStatusException
     */
    public String pushSettlementData(Employee employee, String type,ArrayList<T> object,String appName,String corpId,String passWord) throws HttpStatusException {
        String info =GsonUtil.objectToString(object);
        JsonArray listOrderSettlementInfo =new JsonParser().parse(info).getAsJsonArray();
        return vendorApi.pushSettlementData(employee,type,listOrderSettlementInfo,appName,corpId,passWord).get("message").getAsString();
    }

    /**
     * 内部使用的查询结算数据的接口
     * @return
     */
    public JsonObject internalQuerySettlement(Employee employee, String type, SettlementBody settlementBody, String appName, String corpId, String passWord) throws HttpStatusException {
        return vendorApi.internalGetSettlementData(employee,type,settlementBody,appName,corpId,passWord);
    }
}

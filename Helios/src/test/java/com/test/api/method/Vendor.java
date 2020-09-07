package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.api.VendorApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SettlementBody;
import com.hand.utils.GsonUtil;

import java.util.List;

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
     *消费商结算数据推送
     * @param employee
     * @param type  结算类型  可选 filght train hotel
     * @param object  机票  酒店  火车等结算对象
     * @param appName  汇联易消费商注册的应用名称
     * @param corpId  消费商开通的公司Id
     * @param passWord
     * @return
     * @throws HttpStatusException
     */
    public <T> JsonObject pushSettlementData(Employee employee, String type, List<T> object, String appName, String corpId, String passWord) throws HttpStatusException {
        String info =GsonUtil.objectToString(object);
        JsonArray listOrderSettlementInfo =new JsonParser().parse(info).getAsJsonArray();
        return vendorApi.pushSettlementData(employee,type,listOrderSettlementInfo,appName,corpId);
    }

    /**
     * tmc订单推送
     * @param employee
     * @param orderType
     * @param object  订单对象
     * @param appName
     * @param corpId
     * @param passWord
     * @param <T>
     * @return
     * @throws HttpStatusException
     */
    public <T> JsonObject pushOrderData(Employee employee,String orderType,T object, String appName, String corpId,String passWord) throws HttpStatusException {
        //将数据序列化为JSON 字符串
        String orderString =GsonUtil.objectToString(object);
        //转化为 JsonObject
        JsonObject orderData =new JsonParser().parse(orderString).getAsJsonObject();
        return vendorApi.pushOrderData(employee,orderType,orderData,appName,corpId,passWord);
    }

    /**
     * 内部使用的查询结算数据的接口
     * @return
     */
    public JsonObject internalQuerySettlement(Employee employee, String type, SettlementBody settlementBody) throws HttpStatusException {
        return vendorApi.internalGetSettlementData(employee,type,settlementBody).getAsJsonObject("body").get("list").getAsJsonArray().get(0).getAsJsonObject();
    }

    /**
     * 内部使用查询订单数据接口
     * @param employee
     * @param orderType
     * @param settlementBody
     * @return
     * @throws HttpStatusException
     */
    public JsonObject queryOrderData(Employee employee,String orderType,SettlementBody settlementBody) throws HttpStatusException {
        return vendorApi.queryInternalOrderData(employee,orderType,settlementBody);
    }
}

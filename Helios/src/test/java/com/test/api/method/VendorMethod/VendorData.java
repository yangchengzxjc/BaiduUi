package com.test.api.method.VendorMethod;

import com.google.gson.JsonObject;
import com.test.api.method.Vendor;

/**
 * @Author peng.zhang
 * @Date 2020/11/4
 * @Version 1.0
 **/
public class VendorData {

    private Vendor vendor;

    public VendorData(){
        vendor = new Vendor();
    }

    /**
     * 初始化读取到的结算数据
     * @param
     * @return
     */
    public JsonObject setSettlementData(JsonObject readVendorData,String appName){
//        readVendorData.addProperty("");
        return readVendorData;
    }
}

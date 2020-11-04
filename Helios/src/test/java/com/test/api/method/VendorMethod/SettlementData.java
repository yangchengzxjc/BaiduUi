package com.test.api.method.VendorMethod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.utils.DocumnetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * @Author peng.zhang
 * @Date 2020/10/29
 * @Version 1.0
 **/
@Slf4j
public class SettlementData {

    /**
     * 读取模板数据
     * @param path
     * @param dataLine
     * @param orderNo
     * @param accBalanceBatchNo
     * @return
     */
    public JsonObject readerData(String path,int dataLine,String orderNo,String accBalanceBatchNo){
        ArrayList vendorDataList = DocumnetUtil.fileReader(path,dataLine);
        StringBuilder vendorDataString =new StringBuilder();
        for(int i=0; i<vendorDataList.size();i++){
            vendorDataString.append(vendorDataList.get(i));
        }
        JsonObject vendorObject = new JsonParser().parse(vendorDataString.toString().trim()).getAsJsonObject();
        log.info("读取到的消费商数据为:{}",vendorObject);
        vendorObject.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject().addProperty("accBalanceBatchNo",accBalanceBatchNo);
        vendorObject.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject().addProperty("orderNo",orderNo);
        return vendorObject;
    }
}

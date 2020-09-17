package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.api.VendorApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SettlementBody;
import com.hand.basicObject.supplierObject.employeeInfoDto.UserCardInfoDTO;
import com.hand.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/8/26
 * @Version 1.0
 **/
@Slf4j
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
        JsonObject respons = vendorApi.internalGetSettlementData(employee,type,settlementBody);
        JsonObject orderData =new JsonObject();
        if(respons.get("success").getAsBoolean()){
            orderData = respons.getAsJsonObject("body").get("list").getAsJsonArray().get(0).getAsJsonObject();
        }
        return orderData;
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
        JsonObject respons = vendorApi.queryInternalOrderData(employee,orderType,settlementBody);
        JsonObject orderData =new JsonObject();
        if(respons.get("success").getAsBoolean()){
            orderData = respons.getAsJsonObject("body").get("list").getAsJsonArray().get(0).getAsJsonObject();
        }
        return orderData;
    }

    public String trainTypeMapping(String trainNum){
        StringBuffer  newTrainNum=new StringBuffer(trainNum);
        char  keyword=newTrainNum.charAt(0);
        log.info("keyword:{}",keyword);
        String trainType="";
        switch (keyword){
            case 'G':
                trainType="高铁";
                break;
            case 'D':
                trainType="动车";
                break;
            case 'T':
                trainType = "特快";
                break;
            case 'K':
                trainType = "快车";
                break;
            case 'C':
                trainType ="城际铁路";
                break;
            case ' ':
                trainType= "未知车型";
                break;
        }
        return trainType;
    }

    /**
     * 查询国际国内 两舱设置
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonObject queryBookClass(Employee employee) throws HttpStatusException {
        return vendorApi.queryBookClass(employee);
    }

    /**
     * 根据查询出的证件List 封装成openApi中的证件对象
     * @param userCardInfo
     * @returnuserCardInfos
     */
    public ArrayList addUserCardInfoDTO(JsonArray userCardInfo){

        ArrayList<UserCardInfoDTO> userCardInfos =new ArrayList<>();
        for (int i = 0; userCardInfo.size()>i ; i++) {
            JsonObject jsb = userCardInfo.get(i).getAsJsonObject();
            UserCardInfoDTO userCardInfoDTO = new UserCardInfoDTO();
            userCardInfoDTO.setCardNo(jsb.get("originalCardNo").getAsString());
            userCardInfoDTO.setCardType(jsb.get("cardType").getAsString());
            userCardInfoDTO.setCardTypeName(jsb.get("cardTypeName").getAsString());
            if (jsb.get("cardExpiredTime").isJsonNull()){}
            else{ userCardInfoDTO.setIDCardTimelimit(jsb.get("cardExpiredTime").getAsString());}
            userCardInfoDTO.setFirstName(jsb.get("firstName").getAsString());
            userCardInfoDTO.setLastName(jsb.get("lastName").getAsString());

            userCardInfos.add(userCardInfoDTO);
        }
        return userCardInfos;
    }
}

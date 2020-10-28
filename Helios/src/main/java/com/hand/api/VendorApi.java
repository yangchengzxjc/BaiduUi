package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SettlementBody;
import com.hand.basicconstant.ApiPath;
import com.hand.utils.Md5Util;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author peng.zhang
 * @Date 2020/6/18
 * @Version 1.0
 **/
@Slf4j
public class VendorApi extends BaseRequest{


    /**
     *  商旅TMC 用户调用请求头 （包含签名）
     * @return
     */
    public HashMap<String,String> getHeaderSignature(String appName,String corpId){
        HashMap<String, String> headersdatas = new HashMap<>();
        headersdatas.put("appName",appName);
        headersdatas.put("corpId",corpId);
        headersdatas.put("format","JSON");
        headersdatas.put("signType","Md5");
        // 加密后的数据为：Md5[appName+Md5[password]+corpId]   加密算法
        headersdatas.put("signature","0ab0ca2870a369b96132ebf0b11c290b");
        headersdatas.put("version","1.0");
        return  headersdatas;
    }

    /**
     * 获取经过MD5加密后的算法  算法如下：Md5[appName+Md5[password]+corpId]
     * @param appName  汇联易消费商注册的应用名称
     * @param corpId   消费商开通的公司Id
     * @return
     */
    public String getMD5Secret(String appName,String corpId,String passWord){
        String md5Password = Md5Util.getMd5(passWord);
        return Md5Util.getMd5(appName+md5Password+corpId);
    }

    /**
     *结算接口  推送结算数据到落库
     * @param employee
     * @param settlementType 标识什么结算数据类型  机票flight  酒店hotel  和 火车 train
     * @param appName 汇联易消费商注册的应用名称
     * @param corpId 消费商开通的公司Id
     */
    public JsonObject pushSettlementData(Employee employee,String settlementType,JsonArray listOrderSettlementInfo,String appName,String corpId) throws HttpStatusException {
        String url = employee.getEnvironment().getZhenxuanOpenURL()+ String.format(ApiPath.PUSHTMCSEETLEMRNTDATA,settlementType);
        JsonObject body =new JsonObject();
        if(settlementType.equals("flight")){
            body.add("flightSettlementList",listOrderSettlementInfo);
        }
        if(settlementType.equals("hotel")){
            body.add("hotelSettlementList",listOrderSettlementInfo);
        }
        if(settlementType.equals("train")){
            body.add("trainSettlementInfos",listOrderSettlementInfo);
        }
        log.info("请求头:{}",getHeaderSignature(appName,corpId));
        String res = doPost(url,getHeaderSignature(appName,corpId),null,body.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     *  供内部查询结算数据使用
     * @param employee
     * @param type
     * @param settlementBody
     * @return
     * @throws HttpStatusException
     */
    public JsonObject internalGetSettlementData(Employee employee, String type, SettlementBody settlementBody) throws HttpStatusException {
        String url =employee.getEnvironment().getUrl()+String.format(ApiPath.QUERYVENDORDATA,type);
        JsonObject body =new JsonObject();
        body.addProperty("accBalanceBatchNo",settlementBody.getAccBalanceBatchNo());
        body.addProperty("dateFrom",settlementBody.getDateFrom());
        body.addProperty("dateTo",settlementBody.getDateTo());
        body.addProperty("orderNo",settlementBody.getOrderNo());
        body.addProperty("companyOid",settlementBody.getCompanyOid());
        body.addProperty("supplierCode",settlementBody.getSupplierCode());
        body.addProperty("recordId",settlementBody.getRecordId());
        body.addProperty("page",settlementBody.getPage());
        body.addProperty("size",settlementBody.getSize());
        String res =doPost(url,getHeader(employee.getAccessToken()),null,body.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 订单数据推送
     * @param employee
     * @param orderType 标识什么订单数据类型  机票flight  酒店hotel  和 火车 train
     * @param appName 汇联易消费商注册的应用名称
     * @param corpId 消费商开通的公司Id
     */
    public JsonObject pushOrderData(Employee employee,String orderType,JsonObject orderBody,String appName,String corpId,String tmcPassword) throws HttpStatusException {
        String url = employee.getEnvironment().getZhenxuanOpenURL()+ String.format(ApiPath.PUSHTMCORDERDATA,orderType);
        String res = doPost(url,getHeaderSignature(appName,corpId),null,orderBody.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 内部查询订单数据接口请求
     * @param employee
     * @param orderType
     * @param settlementBody
     * @return
     * @throws HttpStatusException
     */
    public JsonObject queryInternalOrderData(Employee employee,String orderType,SettlementBody settlementBody) throws HttpStatusException {
        String url =employee.getEnvironment().getUrl()+String.format(ApiPath.QUERYORDERDATA,orderType);
        JsonObject body =new JsonObject();
        body.addProperty("dateFrom",settlementBody.getDateFrom());
        body.addProperty("dateTo",settlementBody.getDateTo());
        body.addProperty("orderNo",settlementBody.getOrderNo());
        body.addProperty("companyOid",settlementBody.getCompanyOid());
        body.addProperty("supplierCode",settlementBody.getSupplierCode());
        body.addProperty("page",settlementBody.getPage());
        body.addProperty("size",settlementBody.getSize());
        String res =doPost(url,getHeader(employee.getAccessToken()),null,body.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 查询国内国际 两舱设置
     * @param employee 新增的员工
     * @param companyOID
     * @return
     * @throws HttpStatusException
     */
    public JsonObject queryBookClass(Employee employee,String companyOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.QUERYBOOKCLASS;
        Map<String, String> datas = new HashMap<>();
        datas.put("companyOID",companyOID);
        String res = doGet(url,getHeader(employee.getAccessToken()),datas,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    public JsonObject queryBookClass(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.QUERYBOOKCLASS;
        Map<String, String> datas = new HashMap<>();
        datas.put("companyOID",employee.getCompanyOID());
        String res = doGet(url,getHeader(employee.getAccessToken()),datas,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * TMC 人员信息查询
     * @param employee
     * @param tmcChannel
     * @param hlyUserMobile
     * @return
     */
    public JsonObject tmcUserInfo(Employee employee,String tmcChannel,String hlyUserMobile) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.TMCUSER;
        Map<String, String> datas = new HashMap<>();
        datas.put("tmcChannel",tmcChannel);
        datas.put("hlyUserMobile",hlyUserMobile);
        String res = doGet(url,getHeader(employee.getAccessToken()),datas,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * TMC 人员信息查询
     * @param employee
     * @param tmcChannel
     * @param approvalNo
     * @return
     */
    public JsonObject tmcPlanInfo(Employee employee,String tmcChannel,String approvalNo) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.TMCAPPLICATION;
        Map<String, String> datas = new HashMap<>();
        datas.put("tmcChannel",tmcChannel);
        datas.put("approvalNo",approvalNo);
        String res = doGet(url,getHeader(employee.getAccessToken()),datas,employee);
        return new JsonParser().parse(res).getAsJsonArray().get(0).getAsJsonObject();
    }

    /**
     * sso  单点登录
     * @param employee
     * @param body
     * @param appName
     * @param corpId
     * @return
     * @throws HttpStatusException
     */
    public JsonObject vendorSSO(Employee employee,JsonObject body,String appName,String corpId) throws HttpStatusException {
        String url = employee.getEnvironment().getZhenxuanOpenURL()+ApiPath.SSOLOGIN;
        String res = doPost(url,getHeaderSignature(appName,corpId),null,body.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }
}

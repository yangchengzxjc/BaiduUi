package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.TravelSubsidyConfig;
import com.hand.basicconstant.ApiPath;

import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2020/7/15
 * @Version 1.0
 **/
public class TravelSubsidyAPi extends BaseRequest{

    /**
     * 查询所有的公司的差补设置
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getAllTravelSubsidyConfig(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.TRAVEL_SUBSIDY_CONFIG;
        HashMap<String,String> mapParams = new HashMap<>();
        mapParams.put("page","0");
        mapParams.put("size","0");
        mapParams.put("levelCode","");
        mapParams.put("companyIds","");
        String res = doGet(url, getHeader(employee.getAccessToken()), null, employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 编辑修改差补规则保存
     * @param employee
     * @param subsidyConfigDetail  获取到的差补规则的详情
     * @param travelSubsidyConfig  需要设置的差补规则的配置
     * @return
     * @throws HttpStatusException
     */
    public JsonObject saveTravelSubsidyConfig(Employee employee, JsonObject subsidyConfigDetail, TravelSubsidyConfig travelSubsidyConfig) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.TRAVEL_SUBSIDY_CONFIG;
        subsidyConfigDetail.addProperty("requestControl",travelSubsidyConfig.getRequestControl());
        subsidyConfigDetail.addProperty("reportControl",travelSubsidyConfig.getReportControl());
        subsidyConfigDetail.addProperty("amountModify",travelSubsidyConfig.getAmountModify());
        subsidyConfigDetail.addProperty("reportAttachDisable",travelSubsidyConfig.getReportAttachDisable());
        subsidyConfigDetail.addProperty("travelAutoCalculate",travelSubsidyConfig.isTravelAutoCalculate());
        String res = doPut(url, getHeader(employee.getAccessToken()), null,subsidyConfigDetail.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 默认的差补设置用于设置完成后 恢复环境
     * @param employee
     * @param subsidyConfigDetail
     * @return
     * @throws HttpStatusException
     */
    public JsonObject defaultSubsidyConfig(Employee employee, JsonObject subsidyConfigDetail) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.TRAVEL_SUBSIDY_CONFIG;
        subsidyConfigDetail.addProperty("requestControl",1001);
        subsidyConfigDetail.addProperty("reportControl",1003);
        subsidyConfigDetail.addProperty("amountModify",1001);
        subsidyConfigDetail.addProperty("reportAttachDisable","true");
        subsidyConfigDetail.addProperty("travelAutoCalculate",false);
        String res = doPut(url, getHeader(employee.getAccessToken()), null,subsidyConfigDetail.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 获取差补规则可用的表单列表
     * @param employee
     */
    public JsonObject travelSubsidyRule(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.TRAVEL_SUBSIDY_FORM;
        JsonObject body =new JsonObject();
        body.addProperty("formName","");
        body.addProperty("companyId",(String) null);
        body.addProperty("setOfBooksId",employee.getSetOfBookId());
        body.addProperty("valid",true);
        String res = doPost(url, getHeader(employee.getAccessToken()), null,body.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }
    

}

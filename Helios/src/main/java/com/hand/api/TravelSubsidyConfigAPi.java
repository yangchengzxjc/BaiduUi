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
public class TravelSubsidyConfigAPi extends BaseRequest{

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
     * 编辑修改差补设置管控
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
    public JsonObject getTravelSubsidyForm(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.TRAVEL_SUBSIDY_FORM;
        JsonObject body =new JsonObject();
        body.addProperty("formName","");
        body.addProperty("companyId",(String) null);
        body.addProperty("setOfBooksId",employee.getSetOfBookId());
        body.addProperty("valid",true);
        String res = doPost(url, getHeader(employee.getAccessToken()), null,body.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 获取差补规则基本配置
     * @param employee
     * @param formOID
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getSubsidyBaseConfig(Employee employee,String formOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.TRAVEL_RULE_BASE_CONFIG;
        HashMap<String,String> map =new HashMap<>();
        map.put("formOID",formOID);
        String res = doGet(url, getHeader(employee.getAccessToken()), map,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 查询行程字段（值列表）
     * @param employee
     * @throws HttpStatusException
     */
    public JsonArray getItineraryFields(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.CUSTOM_ENUMERATIONS;
        HashMap<String,String> map =new HashMap<>();
        map.put("page","0");
        map.put("size","200");
        String res = doGet(url, getHeader(employee.getAccessToken()), map,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 差补基本设置
     * @param employee
     * @param subsidyCity 是否开启差补城市
     * @param travelFields   申请单字段 出差时长或者出差天数 需要传一个布尔类型的数组
     * @param specialRules    特殊控制 包含出差当天 返回当天 节假日 周末等字段
     * @param itineraryFields   行程字段 值列表
     * @param subsidySelection   差补类型默认全选
     * @param applicationAutoSubsidy  申请单自动差补
     */
    public JsonObject editSubsidyBaseConfig(Employee employee,JsonObject subsidyBaseConfig,boolean subsidyCity,boolean []travelFields,boolean [] specialRules,
                                      JsonArray itineraryFields,boolean subsidySelection,boolean applicationAutoSubsidy) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.TRAVEL_RULE_BASE_CONFIG;
        JsonArray dimensionTypes = subsidyBaseConfig.get("dimensionTypes").getAsJsonArray();
        //先获取到dimensionTypes 循环遍历
        for(int i=0;i<dimensionTypes.size();i++){
            //在获取到每个先获取到dimensionTypes中的dimensionDetails 每个
           JsonArray dimensionDetails = dimensionTypes.get(i).getAsJsonObject().get("dimensionDetails").getAsJsonArray();
           for(int j =0;j<dimensionDetails.size();j++){
                if(dimensionDetails.get(j).getAsJsonObject().get("name").getAsString().equals("ALLOWANCE_CITY")){
                  dimensionDetails.get(j).getAsJsonObject().addProperty("selected",subsidyCity);
                }
                if(dimensionDetails.get(j).getAsJsonObject().get("name").getAsString().equals("TRAVEL_HOURS")){
                    dimensionDetails.get(j).getAsJsonObject().addProperty("selected",travelFields[j]);
                }
                if(dimensionDetails.get(j).getAsJsonObject().get("name").getAsString().equals("TRAVEL_DAYS")){
                   dimensionDetails.get(j).getAsJsonObject().addProperty("selected",travelFields[j]);
                }
               if(dimensionDetails.get(j).getAsJsonObject().get("name").getAsString().equals("DEPARTURE_DAY")){
                   dimensionDetails.get(j).getAsJsonObject().addProperty("selected",specialRules[j]);
               }
               if(dimensionDetails.get(j).getAsJsonObject().get("name").getAsString().equals("RETURN_DAY")){
                   dimensionDetails.get(j).getAsJsonObject().addProperty("selected",specialRules[j]);
               }
               if(dimensionDetails.get(j).getAsJsonObject().get("name").getAsString().equals("HOLIDAY")){
                   dimensionDetails.get(j).getAsJsonObject().addProperty("selected",specialRules[j]);
               }
               if(dimensionDetails.get(j).getAsJsonObject().get("name").getAsString().equals("HOLIDAY_WEEKEND")){
                   dimensionDetails.get(j).getAsJsonObject().addProperty("selected",specialRules[j]);
               }
           }
           if(dimensionTypes.get(i).getAsJsonObject().get("name").getAsString().equals("ITINERARY_FIELDS")){
               dimensionTypes.get(i).getAsJsonObject().add("itineraryFieldList",itineraryFields);
           }
        }
        subsidyBaseConfig.add("dimensionTypes",dimensionTypes);
        subsidyBaseConfig.addProperty("subsidySelection",subsidySelection);
        subsidyBaseConfig.addProperty("applicationAutoSubsidy",applicationAutoSubsidy);
        String res = doPost(url, getHeader(employee.getAccessToken()), null,subsidyBaseConfig.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 恢复默认的差补基础设置
     * @param employee
     * @param subsidyBaseConfig
     * @return
     * @throws HttpStatusException
     */
    public JsonObject defaultSubsidyBaseConfig(Employee employee,JsonObject subsidyBaseConfig) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.TRAVEL_RULE_BASE_CONFIG;
        JsonArray dimensionTypes = subsidyBaseConfig.get("dimensionTypes").getAsJsonArray();
        //先获取到dimensionTypes 循环遍历
        for(int i=0;i<dimensionTypes.size();i++){
            //在获取到每个先获取到dimensionTypes中的dimensionDetails 每个
            JsonArray dimensionDetails = dimensionTypes.get(i).getAsJsonObject().get("dimensionDetails").getAsJsonArray();
            for(int j =0;j<dimensionDetails.size();j++){
                if(dimensionDetails.get(j).getAsJsonObject().get("name").getAsString().equals("ALLOWANCE_CITY")){
                    dimensionDetails.get(j).getAsJsonObject().addProperty("selected",true);
                }
                if(dimensionDetails.get(j).getAsJsonObject().get("name").getAsString().equals("TRAVEL_HOURS")){
                    dimensionDetails.get(j).getAsJsonObject().addProperty("selected",false);
                }
                if(dimensionDetails.get(j).getAsJsonObject().get("name").getAsString().equals("TRAVEL_DAYS")){
                    dimensionDetails.get(j).getAsJsonObject().addProperty("selected",false);
                }
                if(dimensionDetails.get(j).getAsJsonObject().get("name").getAsString().equals("DEPARTURE_DAY")){
                    dimensionDetails.get(j).getAsJsonObject().addProperty("selected",false);
                }
                if(dimensionDetails.get(j).getAsJsonObject().get("name").getAsString().equals("RETURN_DAY")){
                    dimensionDetails.get(j).getAsJsonObject().addProperty("selected",false);
                }
                if(dimensionDetails.get(j).getAsJsonObject().get("name").getAsString().equals("HOLIDAY")){
                    dimensionDetails.get(j).getAsJsonObject().addProperty("selected",false);
                }
                if(dimensionDetails.get(j).getAsJsonObject().get("name").getAsString().equals("HOLIDAY_WEEKEND")){
                    dimensionDetails.get(j).getAsJsonObject().addProperty("selected",false);
                }
            }
            if(dimensionTypes.get(i).getAsJsonObject().get("name").getAsString().equals("ITINERARY_FIELDS")){
                dimensionTypes.get(i).getAsJsonObject().add("itineraryFieldList",new JsonArray());
            }
        }
        subsidyBaseConfig.add("dimensionTypes",dimensionTypes);
        subsidyBaseConfig.addProperty("subsidySelection",false);
        subsidyBaseConfig.addProperty("applicationAutoSubsidy",false);
        String res = doPost(url, getHeader(employee.getAccessToken()), null,subsidyBaseConfig.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 获取申请单下的所有差补规则的列表
     * @param employee
     * @param formOID
     */
    public JsonArray getSubsidyRuleList(Employee employee,String formOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.SUBSIDY_RULE_List;
        HashMap<String,String> map =new HashMap<>();
        map.put("formOID", formOID);
        String res = doGet(url, getHeader(employee.getAccessToken()), map,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 获取规则详情
     * @param employee
     * @param ruleId   差补规则的id
     */
    public JsonObject getSubsidyRuleDetail(Employee employee, String ruleId) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.SUBSIDY_RULE_DETAIL;
        HashMap<String,String> map =new HashMap<>();
        map.put("id",ruleId);
        String res = doGet(url, getHeader(employee.getAccessToken()), map,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 编辑更新保存差补规则
     * @param employee
     * @param newSubsidyRule  修改后的差补详情规则
     */
    public JsonObject saveSubsidyRule(Employee employee, JsonObject newSubsidyRule) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.SUBSIDY_RULE_DETAIL;
        HashMap<String,String> map =new HashMap<>();
        map.put("setOfBooksId",employee.getSetOfBookId());
        String res = doPut(url, getHeader(employee.getAccessToken()), map,newSubsidyRule.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 删除差补规则（根据规则的id）
     * @param employee
     * @param ruleID
     * @throws HttpStatusException
     */
    public void deleteSubsidyRule(Employee employee,String ruleID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.SUBSIDY_RULE_DETAIL;
        HashMap<String,String> map = new HashMap<>();
        map.put("id",ruleID);
        doDlete(url, getHeader(employee.getAccessToken()), map,new JsonObject(),employee);
    }
}

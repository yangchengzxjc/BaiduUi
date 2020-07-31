package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hand.api.TravelSubsidyConfigAPi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.TravelSubsidyConfig;
import com.hand.utils.GsonUtil;

/**
 * @Author peng.zhang
 * @Date 2020/7/21
 * @Version 1.0
 **/
public class TravelSubsidy {

    private TravelSubsidyConfigAPi subsidyConfigAPi;

    public TravelSubsidy(){
        subsidyConfigAPi =new TravelSubsidyConfigAPi();
    }
    /**
     *根据公司名称获取差补设置
     * @return
     */
    public JsonObject getCompanySubsidyConfig(Employee employee, String companyName) throws HttpStatusException {
        JsonArray configList = subsidyConfigAPi.getAllTravelSubsidyConfig(employee);
        JsonObject config=new JsonObject();
        for(int i=0;i<configList.size();i++){
            if(configList.get(i).getAsJsonObject().get("levelOrgName").getAsString().equals(companyName)){
                config =configList.get(i).getAsJsonObject();
            }
        }
        return config;
    }

    /**
     * 修改差补设置 修改方法：先获取到已经该公司的设置详情，然后根据需要的设置规则来配
     * @param employee
     * @param companyName  需要修改的公司的名称
     * @param config 设置规则的对象，具体的值到该对象查看
     * @throws HttpStatusException
     */
    public JsonObject changeSubsidyConfig(Employee employee, String companyName, TravelSubsidyConfig config) throws HttpStatusException {
       return subsidyConfigAPi.saveTravelSubsidyConfig(employee,getCompanySubsidyConfig(employee,companyName),config);
    }

    /**
     * 配置完成后如果需要恢复默认请调用
     * @param employee
     * @param subsidyDetail
     * @throws HttpStatusException
     */
    public void defaultSubConfig(Employee employee,JsonObject subsidyDetail) throws HttpStatusException {
        subsidyConfigAPi.defaultSubsidyConfig(employee,subsidyDetail);
    }

    /**
     * 根据申请单的名称获取可用的表单OID
     * @param employee
     * @param forName
     * @return
     * @throws HttpStatusException
     */
    public String getSubFormOID(Employee employee,String forName) throws HttpStatusException {
        JsonArray array = subsidyConfigAPi.getTravelSubsidyForm(employee).get("setForms").getAsJsonArray();
        return GsonUtil.getJsonValue(array,"formName",forName,"formOID");
    }

    /**
     * 获取表单的差补基础设置的详情
     * @param employee
     * @param formName
     * @throws HttpStatusException
     */
    public JsonObject getSubsidyBaseConfig(Employee employee,String formName) throws HttpStatusException {
        return subsidyConfigAPi.getSubsidyBaseConfig(employee,getSubFormOID(employee,formName));
    }

    /**
     * @param employee
     * @param formName
     * @param subsidyCity  是否开启差补城市
     * @param travelFields   申请单字段 出差时长或者出差天数 需要传一个布尔类型的数组
     * @param specialRules    特殊控制 包含出差当天 返回当天 节假日 周末等字段
     * @param itineraryFields   行程字段 值列表
     * @param subsidySelection   差补类型默认全选
     * @param applicationAutoSubsidy  申请单自动差补
     * @throws HttpStatusException
     */
    public void saveSubsidyBaseConfig(Employee employee,String formName,boolean subsidyCity,boolean []travelFields, boolean []specialRules,
                                      JsonArray itineraryFields,boolean subsidySelection,boolean applicationAutoSubsidy) throws HttpStatusException {
        subsidyConfigAPi.editSubsidyBaseConfig(employee,getSubsidyBaseConfig(employee,formName),subsidyCity,travelFields,specialRules,itineraryFields,subsidySelection,applicationAutoSubsidy);
    }

    /**
     * 恢复默认的差补基本设置
     * @param employee
     * @param subsidyBaseConfig    差补基本设置详情
     * @throws HttpStatusException
     */
    public void dafaultSubBaseConfig(Employee employee, JsonObject subsidyBaseConfig) throws HttpStatusException {
        subsidyConfigAPi.defaultSubsidyBaseConfig(employee,subsidyBaseConfig);
    }

    /**
     * 根据规则的name获取规则的id
     * @param employee
     * @param ruleName
     * @param forName
     */
    public String getRuleId(Employee employee, String ruleName,String forName) throws HttpStatusException {
        JsonArray ruleList = subsidyConfigAPi.getSubsidyRuleList(employee,getSubFormOID(employee,forName));
        return GsonUtil.getJsonValue(ruleList,"name",ruleName,"id");
    }

    /**
     * 根据规则的name获取travelSubsidiesRuleOID
     * @param employee
     * @param ruleName   规则名称 不可重复
     * @param formName   表单的名称
     */
    public String getRuleOID(Employee employee, String ruleName,String formName) throws HttpStatusException {
        JsonArray ruleList = subsidyConfigAPi.getSubsidyRuleList(employee,getSubFormOID(employee,formName));
        return GsonUtil.getJsonValue(ruleList,"name",ruleName,"travelSubsidiesRuleOID");
    }

    /**
     * 编辑保存差补规则
     * @param employee
     * @param newSubsidyRule
     * @throws HttpStatusException
     */
    public void editSubsidyRule(Employee employee,JsonObject newSubsidyRule) throws HttpStatusException {
        subsidyConfigAPi.saveSubsidyRule(employee,newSubsidyRule);
    }

    /**
     * 根据已有的规则编辑新的规则的json 数据
     * @param employee
     * @param ruleName
     * @param formName
     * @param standardAmount
     * @param departureStandardAmount
     * @param returnAmount
     * @param holidayAmount
     * @param weekendAmount
     * @param userGroups
     * @param cityGroupCode  城市组的code 多个的话直接传俩个城市组的code的字符串
     * @param hours  出差时长[1,2]
     * @param days 出差天数 如果出差天数是一个日期的话就传[1,2]字符串
     * @return
     * @throws HttpStatusException
     */
    public JsonObject newSubsidyRule(Employee employee,String ruleName,String formName,double standardAmount,double departureStandardAmount,double returnAmount, double holidayAmount,String weekendAmount,String userGroups,String cityGroupCode,String hours,String days) throws HttpStatusException {
        //获取已经配置好的差补规则的基础设置
        JsonObject subsidyRule =subsidyConfigAPi.getSubsidyRuleConfig(employee,getSubFormOID(employee,formName));
        //获取当前差补规则
        JsonObject oldSuBsidyDetail = subsidyConfigAPi.getSubsidyRuleDetail(employee,getRuleId(employee,ruleName,formName));
        oldSuBsidyDetail.addProperty("standardAmount",standardAmount);
        JsonArray travelSubsidiesConditions = oldSuBsidyDetail.getAsJsonArray("travelSubsidiesConditions");
        //检查获取的默认规则是否有差补城市
        JsonArray defaultDimensions =subsidyRule.getAsJsonArray("defaultDimensions");
        //检查申清单字段（出差时长 出差天数）
        JsonArray travelFieldDimension = subsidyRule.getAsJsonArray("travelFieldDimension");
        //检查特殊控制
        JsonArray specialDimension =subsidyRule.getAsJsonArray("specialDimension");
        //设置人员组
        for (JsonElement array:travelSubsidiesConditions
             ) {if(array.getAsJsonObject().get("messageKey").getAsString().equals("USER_GROUPS")){
                 array.getAsJsonObject().addProperty("value",userGroups);
             }
        }
        //存在城市的话 需要设置城市组
        for (JsonElement array: defaultDimensions
             ) {if(array.getAsString().equals("ALLOWANCE_CITY")){
                 //先判断下该详情中是否有差补城市 已经存在的话就不要新增
                 if(travelSubsidiesConditions.toString().contains("ALLOWANCE_CITY")){
                     for(int i=0;i<travelSubsidiesConditions.size();i++){
                         if(travelSubsidiesConditions.get(i).getAsJsonObject().get("messageKey").equals("ALLOWANCE_CITY")){
                             travelSubsidiesConditions.get(i).getAsJsonObject().addProperty("value",cityGroupCode);
                         }
                     }
                 }else{
                     JsonObject allowanceCity = new JsonObject();
                     allowanceCity.addProperty("messageKey","ALLOWANCE_CITY");
                     allowanceCity.addProperty("fieldType",101);
                     allowanceCity.addProperty("restriction",1006);
                     allowanceCity.addProperty("values",cityGroupCode);
                     travelSubsidiesConditions.add(allowanceCity);
                 }
             }
        }
        //存在出差时长以及天数
        for (JsonElement array: travelFieldDimension
             ) {
            //判断TRAVEL_HOURS是否配置
            if(array.getAsString().equals("TRAVEL_HOURS")){
                //判断详情中时候有该jsonObject
                if(travelSubsidiesConditions.toString().contains("TRAVEL_HOURS")){
                    for(int i=0;i<travelSubsidiesConditions.size();i++){
                        if(travelSubsidiesConditions.get(i).getAsJsonObject().get("messageKey").equals("TRAVEL_HOURS")){
                            travelSubsidiesConditions.get(i).getAsJsonObject().addProperty("value",hours);
                        }
                    }
                }else{
                    JsonObject travelHour = new JsonObject();
                    travelHour.addProperty("messageKey","TRAVEL_HOURS");
                    travelHour.addProperty("fieldType",101);
                    travelHour.addProperty("restriction",1008);
                    travelHour.addProperty("values",hours);
                    travelSubsidiesConditions.add(travelHour);
                }
            }if(array.getAsString().equals("TRAVEL_DAYS")){
                if(travelSubsidiesConditions.toString().contains("TRAVEL_HOURS")) {
                    for (int i = 0; i < travelSubsidiesConditions.size(); i++) {
                        if (travelSubsidiesConditions.get(i).getAsJsonObject().get("messageKey").equals("TRAVEL_HOURS")) {
                            travelSubsidiesConditions.get(i).getAsJsonObject().addProperty("value", hours);
                        }
                    }
                }else{
                    JsonObject travelDay = new JsonObject();
                    travelDay.addProperty("messageKey","TRAVEL_DAYS");
                    travelDay.addProperty("fieldType",101);
                    if(days.contains("[") || days.contains("(")|| days.contains("]") || days.contains(")")){
                        travelDay.addProperty("restriction",1008);
                    }
                    travelDay.addProperty("values",days);
                    travelSubsidiesConditions.add(travelDay);
                }
            }
        }
        //判断存在特殊控制的情况
        for(int i=0;i<specialDimension.size();i++){
            //判断出差当日
            if(specialDimension.get(i).getAsString().equals("DEPARTURE_DAY")){
                oldSuBsidyDetail.add("departureConditions",new JsonArray());
                oldSuBsidyDetail.addProperty("departureStandardAmount",departureStandardAmount);
                oldSuBsidyDetail.addProperty("departureReferenceStandard",true);
                oldSuBsidyDetail.addProperty("departureOperator",1001);
                oldSuBsidyDetail.addProperty("departureOperatorDivisor",1);
                oldSuBsidyDetail.addProperty("departureAmount",departureStandardAmount);
            }
            if(specialDimension.get(i).getAsString().equals("RETURN_DAY")){
                oldSuBsidyDetail.add("returnConditions",new JsonArray());
                oldSuBsidyDetail.addProperty("departureStandardAmount",returnAmount);
                oldSuBsidyDetail.addProperty("returnReferenceStandard",true);
                oldSuBsidyDetail.addProperty("returnOperator",1001);
                oldSuBsidyDetail.addProperty("returnOperatorDivisor",1);
                oldSuBsidyDetail.addProperty("departureAmount",returnAmount);
            }
            if(specialDimension.get(i).getAsString().equals("HOLIDAY")){
                oldSuBsidyDetail.addProperty("holidayReferenceStandard",true);
                oldSuBsidyDetail.addProperty("holidayOperator",1001);
                oldSuBsidyDetail.addProperty("holidayOperatorDivisor",1);
                oldSuBsidyDetail.addProperty("holidayAmount",holidayAmount);
            }
            if(specialDimension.get(i).getAsString().equals("HOLIDAY_WEEKEND")){
                oldSuBsidyDetail.addProperty("weekendReferenceStandard",true);
                oldSuBsidyDetail.addProperty("weekendOperator",1001);
                oldSuBsidyDetail.addProperty("weekendOperatorDivisor",1);
                oldSuBsidyDetail.addProperty("weekendAmount",weekendAmount);
            }
        }
        return oldSuBsidyDetail;
    }



}

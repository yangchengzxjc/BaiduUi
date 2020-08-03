package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hand.api.ReimbStandardApi;
import com.hand.api.TravelSubsidyConfigAPi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.TravelSubsidyConfig;
import com.hand.utils.GsonUtil;
import com.sun.javafx.runtime.async.AbstractRemoteResource;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author peng.zhang
 * @Date 2020/7/21
 * @Version 1.0
 **/
@Slf4j
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
        JsonArray setForms = subsidyConfigAPi.getTravelSubsidyForm(employee).get("setForms").getAsJsonArray();
        JsonArray unsetForms = subsidyConfigAPi.getTravelSubsidyForm(employee).get("unsetForms").getAsJsonArray();
        String formOID="";
        if(!GsonUtil.getJsonValue(setForms,"formName",forName,"formOID").equals("")){
            formOID = GsonUtil.getJsonValue(setForms,"formName",forName,"formOID");
        }else{
            formOID = GsonUtil.getJsonValue(unsetForms,"formName",forName,"formOID");
        }
        return formOID;
    }

    /**
     * 获取表单的差补基础设置的详情
     * @param employee
     * @param formName
     * @throws HttpStatusException
     */
    public JsonObject getSubsidyBaseConfig(Employee employee,String formName) throws HttpStatusException {
        return subsidyConfigAPi.getSubsidyBaseConfigDetail(employee,getSubFormOID(employee,formName));
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
        JsonObject object =subsidyConfigAPi.editSubsidyRule(employee,newSubsidyRule);
        log.info("差旅规则编辑:{}",object);
    }

    public void createSubsidyRule(Employee employee,JsonObject subsidyRule) throws HttpStatusException {
        subsidyConfigAPi.createSubsidyRule(employee,subsidyRule);
    }

    /**
     * 根据已有的规则编辑新的规则的json 数据   目前支持配置：1.人员组，城市组，出差天数，出差时长， 出差当天  返回当天
     * 周末差补（引用标准差补*） 节假日差补（引用标准差补*）出差当天的规则暂时没支持。
     * @param employee
     * @param ruleName 规则名称
     * @param formName
     * @param standardAmount  标准金额
     * @param departureStandardAmount  出发当天的金额
     * @param returnAmount    返回当天的金额
     * @param holidayAmount  节假日补贴金额
     * @param weekendAmount 周末的补贴金额
     * @param userGroups      "" 为通用
     * @param cityGroupCode  城市组的code 多个的话直接传俩个城市组的code的字符串  ""为通用
     * @param hours  出差时长[1,2]   如果设置中没有此设置则传""
     * @param days 出差天数 如果出差天数是一个日期的话就传[1,2]字符串
     * @return
     * @throws HttpStatusException
     */
    public JsonObject newSubsidyRule(Employee employee,String ruleName,String expenseName,String formName,double standardAmount,double departureStandardAmount,double returnAmount, double holidayAmount,double weekendAmount,String userGroups,String cityGroupCode,String hours,String days) throws HttpStatusException {
        String formOID = getSubFormOID(employee,formName);
        //获取已经配置好的差补规则的基础设置
        JsonObject subsidyRule =subsidyConfigAPi.getSubsidyRuleConfig(employee,formOID);
        //获取当前差补规则
//        JsonObject oldSuBsidyDetail = subsidyConfigAPi.getSubsidyRuleDetail(employee,getRuleId(employee,ruleName,formName));
        JsonObject ruleObject =new JsonObject();
        ruleObject.addProperty("standardAmount",standardAmount);
        ruleObject.addProperty("enabled",true);
        ruleObject.addProperty("formOID",formOID);
        ruleObject.addProperty("name",ruleName);
        ruleObject.addProperty("expenseTypeOID",getSubsidyExpenseOID(employee,formOID,expenseName));
        ruleObject.addProperty("expenseTypeName",expenseName);
        ruleObject.addProperty("currencyCode","CNY");

        JsonArray travelSubsidiesConditions = new JsonArray();
        //检查获取的默认规则是否有差补城市
        JsonArray defaultDimensions =subsidyRule.getAsJsonArray("defaultDimensions");
        //检查申清单字段（出差时长 出差天数）
        JsonArray travelFieldDimension = subsidyRule.getAsJsonArray("travelFieldDimension");
        //检查特殊控制
        JsonArray specialDimension =subsidyRule.getAsJsonArray("specialDimension");
        //设置人员组
        JsonObject userGroup =new JsonObject();
        userGroup.addProperty("messageKey","USER_GROUPS");
        userGroup.addProperty("fieldType",101);
        userGroup.addProperty("restriction",1006);
        userGroup.addProperty("values",userGroups);
        travelSubsidiesConditions.add(userGroup);
        //存在城市的话 需要设置城市组
        for (JsonElement array: defaultDimensions
             ) {if(array.getAsString().equals("ALLOWANCE_CITY")){
                 //先判断下该详情中是否有差补城市 已经存在的话就不要新增
                 JsonObject allowanceCity = new JsonObject();
                 allowanceCity.addProperty("messageKey","ALLOWANCE_CITY");
                 allowanceCity.addProperty("fieldType",101);
                 allowanceCity.addProperty("restriction",1006);
                 allowanceCity.addProperty("values",cityGroupCode);
                 travelSubsidiesConditions.add(allowanceCity);
             }
        }
        //存在出差时长以及天数
        for (JsonElement array: travelFieldDimension
             ) {
            //判断TRAVEL_HOURS是否配置
            if(array.getAsString().equals("TRAVEL_HOURS")){
                //判断详情中时候有该jsonObject
                JsonObject travelHour = new JsonObject();
                travelHour.addProperty("messageKey","TRAVEL_HOURS");
                travelHour.addProperty("fieldType",101);
                if(days.contains("[") || days.contains("(")|| days.contains("]") || days.contains(")")){
                    travelHour.addProperty("restriction",1008);
                }
                travelHour.addProperty("values",hours);
                travelSubsidiesConditions.add(travelHour);
                }
            if(array.getAsString().equals("TRAVEL_DAYS")){
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
        ruleObject.add("travelSubsidiesConditions",travelSubsidiesConditions);
        //判断存在特殊控制的情况
        for(int i=0;i<specialDimension.size();i++){
            //判断出差当日
            if(specialDimension.get(i).getAsString().equals("DEPARTURE_DAY")){
                ruleObject.add("departureConditions",new JsonArray());
                ruleObject.addProperty("departureStandardAmount",departureStandardAmount);
                ruleObject.addProperty("departureReferenceStandard",true);
                ruleObject.addProperty("departureOperator",1001);
                ruleObject.addProperty("departureOperatorDivisor",1);
                ruleObject.addProperty("departureAmount",departureStandardAmount);
            }
            if(specialDimension.get(i).getAsString().equals("RETURN_DAY")){
                ruleObject.add("returnConditions",new JsonArray());
                ruleObject.addProperty("returnStandardAmount",returnAmount);
                ruleObject.addProperty("returnReferenceStandard",true);
                ruleObject.addProperty("returnOperator",1001);
                ruleObject.addProperty("returnOperatorDivisor",1);
                ruleObject.addProperty("returnAmount",returnAmount);
            }
            if(specialDimension.get(i).getAsString().equals("HOLIDAY")){
                ruleObject.addProperty("holidayReferenceStandard",true);
                ruleObject.addProperty("holidayOperator",1001);
                ruleObject.addProperty("holidayOperatorDivisor",1);
                ruleObject.addProperty("holidayAmount",holidayAmount);
            }
            if(specialDimension.get(i).getAsString().equals("HOLIDAY_WEEKEND")){
                ruleObject.addProperty("weekendReferenceStandard",true);
                ruleObject.addProperty("weekendOperator",1001);
                ruleObject.addProperty("weekendOperatorDivisor",1);
                ruleObject.addProperty("weekendAmount",weekendAmount);
            }
        }
        return ruleObject;
    }

    /**
     * 根据人员组的name获取人员组的OID
     * @param employee
     * @param userGroupName
     * @return
     * @throws HttpStatusException
     */
    public String getUserGroupOID(Employee employee,String userGroupName) throws HttpStatusException {
        ReimbStandardApi reimbStandardApi =new ReimbStandardApi();
        JsonArray array = reimbStandardApi.getUserGroups(employee,employee.getSetOfBookId());
        String userGroupOID = "";
        if(GsonUtil.isNotEmpt(array)){
            userGroupOID = GsonUtil.getJsonValue(array,"name",userGroupName,"userGroupOID");
        }else{
            log.info("人员组数据为空,请检查参数");
        }
        return userGroupOID;
    }

    /**
     * 获取表单可用的费用类型
     * @param employee
     * @param formOID
     * @return
     */
    public String getSubsidyExpenseOID(Employee employee,String formOID,String expenseTypeName) throws HttpStatusException {
        JsonArray expenseTypes = subsidyConfigAPi.getFromDetail(employee,formOID).getAsJsonObject("expenseTypesDTO").getAsJsonArray("expenseTypes");
        String expenseTypeOID ="";
        if(GsonUtil.isNotEmpt(expenseTypes)){
            expenseTypeOID = GsonUtil.getJsonValue(expenseTypes,"name",expenseTypeName,"expenseTypeOID");
        }else{
            log.info("该表单费用类型为空,请检查参数或设置");
        }
        return expenseTypeOID;
    }
}

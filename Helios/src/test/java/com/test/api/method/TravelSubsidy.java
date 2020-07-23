package com.test.api.method;

import com.google.gson.JsonArray;
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
     * @param forName   表单的名称
     */
    public String getRuleOID(Employee employee, String ruleName,String forName) throws HttpStatusException {
        JsonArray ruleList = subsidyConfigAPi.getSubsidyRuleList(employee,getSubFormOID(employee,forName));
        return GsonUtil.getJsonValue(ruleList,"name",ruleName,"travelSubsidiesRuleOID");
    }
}

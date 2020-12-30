package com.test.api.method.ExpenseControlMethod;

import com.google.gson.JsonArray;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.StandardCondition;
import com.hand.basicObject.Rule.StandardControlItem;
import com.hand.basicObject.Rule.StandardRules;
import com.hand.basicObject.Rule.StandardRulesItem;
import com.test.api.method.ReimbStandard;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Author peng.zhang
 * @Date 2020/12/3
 * @Version 1.0
 **/
public class StandardControl{

    /**
     * 初始化 基本标准 人员组和城市组为空（通用）
     * @param employee
     * @param ruleOID
     */
    public  StandardRulesItem setStandardRulesItem(Employee employee, boolean isEdit,StandardRules rules,String ruleOID) throws HttpStatusException {
        StandardRulesItem standardRulesItem = new StandardRulesItem();
        ReimbStandard reimbStandard = new ReimbStandard();
        standardRulesItem.setAmount(new BigDecimal(200));
        standardRulesItem.setRuleOID(ruleOID);
        reimbStandard.addStandard(employee,isEdit,rules,standardRulesItem,new String[]{},new String[]{});
        return standardRulesItem;
    }

    /**
     * 初始化 基本标准 人员组和城市组可选
     * @param employee
     * @param rules
     * @param ruleOID
     * @param userGroupsName
     * @param cityGroupsName
     * @throws HttpStatusException
     */
    public StandardRulesItem setStandardRulesItem(Employee employee,Integer amount,boolean isEdit,StandardRules rules,String ruleOID,String [] userGroupsName,String []cityGroupsName) throws HttpStatusException {
        StandardRulesItem standardRulesItem = new StandardRulesItem();
        ReimbStandard reimbStandard = new ReimbStandard();
        standardRulesItem.setAmount(new BigDecimal(amount));
        standardRulesItem.setRuleOID(ruleOID);
        reimbStandard.addStandard(employee,isEdit,rules,standardRulesItem,userGroupsName,cityGroupsName);
        return standardRulesItem;
    }

    /**
     * 条件为：乘上对应的条件
     * @param fieldCode
     * @return
     */
    public StandardControlItem setStandardCondition(String fieldCode){
        StandardControlItem controlItem = new StandardControlItem();
        StandardCondition standardCondition =new StandardCondition();
        standardCondition.setOperatorId(1004);
        standardCondition.setRightFieldCode(fieldCode);
        ArrayList<StandardCondition> conditions =new ArrayList<>();
        conditions.add(standardCondition);
        controlItem.setConditions(conditions);
        return controlItem;
    }

    /**
     * 初始化管控项
     * @param controlItem
     * @param valueType
     * @param fieldValue
     * @param dataType
     * @return
     */
    public StandardControlItem setStandControlItem(String controlItem,Integer valueType,Object fieldValue,String dataType){
        StandardControlItem standardControlItem = new StandardControlItem();
        standardControlItem.setControlItem(controlItem);
        standardControlItem.setValueType(valueType);
        standardControlItem.setFieldValue(fieldValue);
        standardControlItem.setDataType(dataType);
        return standardControlItem;
    }

    /**
     * 初始化账套级规则
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public String setStandardRules(Employee employee,String controlModeType) throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = new StandardRules();
        ReimbStandard reimbStandard = new ReimbStandard();
        rules.setName("auto test period control");
        rules.setControlModeType(controlModeType);
        return reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
    }

    /**
     * 初始化账套级规则
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public String setStandardRules(Employee employee,String controlModeType,String [] userGroups,String [] formName,String ... expenseTypeName ) throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = new StandardRules();
        ReimbStandard reimbStandard = new ReimbStandard();
        rules.setName("auto test period control");
        rules.setControlModeType(controlModeType);
        return reimbStandard.addReimbstandard(employee,rules,userGroups,formName,expenseTypeName);
    }

    /**
     * 初始化一个汇总校验 取就高的规则
     * @return
     * @throws HttpStatusException
     */
    public StandardRules setSummaryHighRule(){
        //新建账套级规则
        StandardRules rules = new StandardRules();
        rules.setName("auto test period control");
        rules.setControlModeType("SUMMARY");
        rules.setParticipantsEnable(true);
        //取就高
        rules.setParticipantsMode("HIGH");
        return rules;
    }

    /**
     * 初始化一个汇总校验 取和值的规则
     * @return
     * @throws HttpStatusException
     */
    public StandardRules setSummarySumRule(){
        //新建账套级规则
        StandardRules rules = new StandardRules();
        rules.setName("auto test period control");
        rules.setControlModeType("SUMMARY");
        rules.setParticipantsEnable(true);
        //取就高
        rules.setParticipantsMode("SUM");
        return rules;
    }

    /**
     * 初始化一个汇总校验的规则
     * @return
     * @throws HttpStatusException
     */
    public StandardRules setSummaryRule(String participantsMode,String setType){
        //新建账套级规则
        StandardRules rules = new StandardRules();
        rules.setName("auto test period control");
        rules.setControlModeType("SUMMARY");
        rules.setParticipantsEnable(true);
        if(setType.equals("费用大类")){
            rules.setSetType("EXPENSE_TYPE_CATEGORY");
        }
        //取就高
        rules.setParticipantsMode(participantsMode);
        return rules;
    }

    /**
     * 初始化一个单条管控规则
     * @param participantsMode  费用参与人标准 取值为：就高或和值
     * @param setType 设置方式：  费用类型  或者 费用大类
     * @return
     */
    public StandardRules setSingleRule(String participantsMode,String setType){
        //新建账套级规则
        StandardRules rules = new StandardRules();
        rules.setName("auto test single control");
        //开启单条管控
        rules.setControlModeType("SINGLE");
        //费用参与人标准开启
        rules.setParticipantsEnable(true);
        if(setType.equals("费用大类")){
            rules.setSetType("EXPENSE_TYPE_CATEGORY");
        }
        rules.setParticipantsMode(participantsMode);
        return rules;
    }

    /**
     * 初始化一个单条管控 费用参与人管控关闭规则
     * @param setType 设置方式：费用类型  或者 费用大类
     * @return
     */
    public StandardRules setSingleRule(String setType){
        //新建账套级规则
        StandardRules rules = new StandardRules();
        rules.setName("auto test single control");
        //开启单条管控
        rules.setControlModeType("SINGLE");
        //费用参与人标准开启
        rules.setParticipantsEnable(false);
        if(setType.equals("费用大类")){
            rules.setSetType("EXPENSE_TYPE_CATEGORY");
        }
        return rules;
    }

    /**
     * 初始化周期管控的规则
     * @param controlType
     * @param setType
     * @return
     */
    public StandardRules setPeriod(String controlType,String setType){
        StandardRules rules = new StandardRules();
        rules.setName("autoTest period control");
        rules.setControlType(controlType);
        if(setType.equals("费用大类")){
            rules.setSetType("EXPENSE_TYPE_CATEGORY");
        }
        return rules;
    }

    /**
     * 非金额管控规则初始化
     * @param noAmountType
     * @param setType
     * @return
     */
    public StandardRules setNoAmountRule(String noAmountType,String setType){
        StandardRules rules = new StandardRules();
        rules.setName("autoTest noAmount control");
        if(noAmountType.equals("飞机")){
            rules.setNonAmountCtrlItem("PLANE_CABIN");
        }
        if(noAmountType.equals("火车")){
            rules.setNonAmountCtrlItem("TRAIN_SEAT_CLASS");
        }
        if(noAmountType.equals("轮船")){
            rules.setNonAmountCtrlItem("SHIP_CABIN");
        }
        if(setType.equals("费用大类")){
            rules.setSetType("EXPENSE_TYPE_CATEGORY");
        }
        return rules;
    }

    /**
     * 非金额管控规则初始化 参与人标准开启
     * @param noAmountType
     * @param setType
     * @return
     */
    public StandardRules setNoAmountRule(String noAmountType,String setType,String participantMode){
        StandardRules rules = new StandardRules();
        rules.setName("autoTest noAmount control");
        if(noAmountType.equals("飞机")){
            rules.setNonAmountCtrlItem("PLANE_CABIN");
        }
        if(noAmountType.equals("火车")){
            rules.setNonAmountCtrlItem("TRAIN_SEAT_CLASS");
        }
        if(noAmountType.equals("轮船")){
            rules.setNonAmountCtrlItem("SHIP_CABIN");
        }
        if(setType.equals("费用大类")){
            rules.setSetType("EXPENSE_TYPE_CATEGORY");
        }
        rules.setParticipantsEnable(true);
        rules.setParticipantsMode(participantMode);
        return rules;
    }

    /**
     * 非金额的标准初始化
     * @param employee
     * @param rules
     * @param ruleOID
     * @param cabin
     * @return
     * @throws HttpStatusException
     */
    public StandardRulesItem setNoAmountStandard(Employee employee,StandardRules rules,boolean isEdit,String ruleOID,String[] userGroup,String ...cabin) throws HttpStatusException {
        StandardRulesItem standardRulesItem = new StandardRulesItem();
        ReimbStandard reimbStandard = new ReimbStandard();
        if(rules.getNonAmountCtrlItem().equals("PLANE_CABIN")){
            standardRulesItem.setCustomEnumerationItems(reimbStandard.getCustomEnumerationItems(employee,"舱等",cabin));
        }
        if(rules.getNonAmountCtrlItem().equals("TRAIN_SEAT_CLASS")){
            standardRulesItem.setCustomEnumerationItems(reimbStandard.getCustomEnumerationItems(employee,"座等",cabin));
        }
        if(rules.getNonAmountCtrlItem().equals("SHIP_CABIN")){
            standardRulesItem.setCustomEnumerationItems(reimbStandard.getCustomEnumerationItems(employee,"座次",cabin));
        }
        standardRulesItem.setRuleOID(ruleOID);
        reimbStandard.addStandard(employee,isEdit,rules,standardRulesItem,userGroup,new String[]{});
        return standardRulesItem;
    }
}

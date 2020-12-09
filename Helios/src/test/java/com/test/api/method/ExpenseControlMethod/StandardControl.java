package com.test.api.method.ExpenseControlMethod;

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
    public StandardRules setSummaryHighRule() throws HttpStatusException {
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
     * 初始化一个汇总校验 取就高的规则
     * @return
     * @throws HttpStatusException
     */
    public StandardRules setSummarySumRule() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = new StandardRules();
        rules.setName("auto test period control");
        rules.setControlModeType("SUMMARY");
        rules.setParticipantsEnable(true);
        //取就高
        rules.setParticipantsMode("SUM");
        return rules;
    }
}

package com.test.api.method.ExpenseControlMethod;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.StandardRules;
import com.hand.basicObject.Rule.StandardRulesItem;
import com.test.api.method.ReimbStandard;

import java.math.BigDecimal;

/**
 * @Author peng.zhang
 * @Date 2020/12/3
 * @Version 1.0
 **/
public class StandardControl {

    /**
     * 初始化 基本标准 人员组和城市组为空（通用）
     * @param employee
     * @param ruleOID
     * @param defaultStandardOID
     */
    public  StandardRulesItem setStandardRulesItem(Employee employee, StandardRules rules,String ruleOID, String defaultStandardOID) throws HttpStatusException {
        StandardRulesItem standardRulesItem = new StandardRulesItem();
        ReimbStandard reimbStandard = new ReimbStandard();
        standardRulesItem.setStandardOID(defaultStandardOID);
        standardRulesItem.setAmount(new BigDecimal(200));
        standardRulesItem.setRuleOID(ruleOID);
        reimbStandard.addStandard(employee,rules,standardRulesItem,new String[]{},new String[]{});
        return standardRulesItem;
    }

    /**
     * 初始化 基本标准 人员组和城市组可选
     * @param employee
     * @param rules
     * @param ruleOID
     * @param defaultStandardOID
     * @param userGroupsName
     * @param cityGroupsName
     * @throws HttpStatusException
     */
    public StandardRulesItem setStandardRulesItem(Employee employee, StandardRules rules,String ruleOID, String defaultStandardOID,String [] userGroupsName,String []cityGroupsName) throws HttpStatusException {
        StandardRulesItem standardRulesItem = new StandardRulesItem();
        ReimbStandard reimbStandard = new ReimbStandard();
        standardRulesItem.setStandardOID(defaultStandardOID);
        standardRulesItem.setAmount(new BigDecimal(200));
        standardRulesItem.setRuleOID(ruleOID);
        reimbStandard.addStandard(employee,rules,standardRulesItem,userGroupsName,cityGroupsName);
        return standardRulesItem;
    }
}

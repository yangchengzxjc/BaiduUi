package com.test.api.testcase.expenseControl.RemimbStandard;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.StandardRules;
import com.test.BaseTest;
import com.test.api.method.BusinessMethod.ExpenseReportPage;
import com.test.api.method.ExpenseControlMethod.StandardControl;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.ReimbStandard;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2020/12/28
 * @Version 1.0
 **/
public class NoAmountControlTest extends BaseTest {

    private Employee employee;
    private ReimbStandard reimbStandard;
    private HashMap<String, String> map;
    private ExpenseReportPage expenseReportPage;
    private ExpenseReport expenseReport;
    private StandardControl standardControl;
    private ExpenseReportInvoice invoice;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("rr123456") String pwd, @Optional("stage") String env){
        employee = getEmployee(phoneNumber,pwd,env);
        reimbStandard =new ReimbStandard();
        expenseReport = new ExpenseReport();
        expenseReportPage = new ExpenseReportPage();
        map = new HashMap<>();
        standardControl = new StandardControl();
        invoice = new ExpenseReportInvoice();
    }

    @Test(description = "非金额管控飞机舱等管控")
    public void noAmountControlTest01() throws HttpStatusException {
        StandardRules rules = standardControl.setNoAmount("飞机","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String[]{"自动化测试-日常报销单"},"飞机舱等");
        standardControl.setNoAmountStandard(employee,rules,ruleOID,"经济舱");
    }
}

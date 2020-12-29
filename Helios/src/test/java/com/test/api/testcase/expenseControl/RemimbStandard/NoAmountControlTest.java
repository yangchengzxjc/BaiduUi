package com.test.api.testcase.expenseControl.RemimbStandard;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.StandardRules;
import com.hand.basicObject.Rule.StandardRulesItem;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.BusinessMethod.ExpenseReportPage;
import com.test.api.method.ExpenseControlMethod.StandardControl;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.ReimbStandard;
import org.testng.Assert;
import org.testng.annotations.*;

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
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("rr123456") String pwd, @Optional("stage") String env) {
        employee = getEmployee(phoneNumber, pwd, env);
        reimbStandard = new ReimbStandard();
        expenseReport = new ExpenseReport();
        expenseReportPage = new ExpenseReportPage();
        map = new HashMap<>();
        standardControl = new StandardControl();
        invoice = new ExpenseReportInvoice();
    }

    @Test(description = "非金额管控-飞机舱等管控-不包含-经济舱")
    public void noAmountControlTest01() throws HttpStatusException {
        //新建非金额管控
        StandardRules rules = standardControl.setNoAmount("飞机", "费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee, rules, new String[]{}, new String[]{"自动化测试-日常报销单"}, "飞机舱等");
        //设置飞机舱等
        standardControl.setNoAmountStandard(employee, rules, ruleOID, "经济舱");
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", new String[]{employee.getFullName()}).get("expenseReportOID");
        //新建费用
        String invoiceOID1 = expenseReportPage.setairPlanInvoice(employee, "飞机舱等", reportOID1, "商务舱");
        map.put("ruleOID", ruleOID);
        map.put("reportOID1", reportOID1);
        map.put("invoiceOID1", invoiceOID1);
        String label = String.format("%s{飞机舱等标准为经济舱}", rules.getMessage());
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, reportOID1, "5001"), label);
    }

    @Test(description = "非金额管控-飞机舱等管控-不包含-商务舱")
    public void noAmountControlTest02() throws HttpStatusException {
        //新建非金额管控
        StandardRules rules = standardControl.setNoAmount("飞机", "费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee, rules, new String[]{}, new String[]{"自动化测试-日常报销单"}, "飞机舱等");
        //设置飞机舱等
        standardControl.setNoAmountStandard(employee, rules, ruleOID, "商务舱");
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", new String[]{employee.getFullName()}).get("expenseReportOID");
        //新建费用
        String invoiceOID1 = expenseReportPage.setairPlanInvoice(employee, "飞机舱等", reportOID1, "经济舱");
        map.put("ruleOID", ruleOID);
        map.put("reportOID1", reportOID1);
        map.put("invoiceOID1", invoiceOID1);
        String label = String.format("%s{飞机舱等标准为商务舱}", rules.getMessage());
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, reportOID1, "5001"), label);
    }

    @Test(description = "非金额管控-费用大类-飞机舱等管控-不包含-商务舱")
    public void noAmountControlTest03() throws HttpStatusException {
        //新建非金额管控
        StandardRules rules = standardControl.setNoAmount("飞机", "费用大类");
        String ruleOID = reimbStandard.addReimbstandard(employee, rules, new String[]{}, new String[]{"自动化测试-日常报销单"}, "机票");
        //设置飞机舱等
        standardControl.setNoAmountStandard(employee, rules, ruleOID, "商务舱");
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", new String[]{employee.getFullName()}).get("expenseReportOID");
        //新建费用
        String invoiceOID1 = expenseReportPage.setairPlanInvoice(employee, "飞机舱等", reportOID1, "经济舱");
        map.put("ruleOID", ruleOID);
        map.put("reportOID1", reportOID1);
        map.put("invoiceOID1", invoiceOID1);
        String label = String.format("%s{飞机舱等标准为商务舱}", rules.getMessage());
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, reportOID1, "5001"), label);
    }



    @AfterMethod
    public void cleanEnv() throws HttpStatusException {
        for (String s : map.keySet()) {
            switch (s) {
                case "ruleOID":
                    reimbStandard.deleteReimbStandardRules(employee, map.get("ruleOID"));
                    break;
                case "reportOID1":
                    expenseReport.deleteExpenseReport(employee, map.get("reportOID1"));
                    break;
                case "reportOID2":
                    expenseReport.deleteExpenseReport(employee, map.get("reportOID2"));
                    break;
                case "invoiceOID1":
                    invoice.deleteInvoice(employee, map.get("invoiceOID1"));
                    break;
                case "invoiceOID2":
                    invoice.deleteInvoice(employee, map.get("invoiceOID2"));
                    break;
            }
        }
    }
}

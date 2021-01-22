package com.test.api.testcase.expenseControl.RemimbStandard;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.StandardRules;
import com.hand.basicObject.component.FormDetail;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.BusinessMethod.ExpenseReportPage;
import com.test.api.method.ExpenseControlMethod.StandardControl;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.ReimbStandard;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2020/12/28
 * @Version 1.0
 **/
@Slf4j
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
    public void beforeClass(@Optional("14082971222") String phoneNumber, @Optional("zp123456") String pwd, @Optional("stage") String env) {
        employee = getEmployee(phoneNumber, pwd, env);
        reimbStandard = new ReimbStandard();
        expenseReport = new ExpenseReport();
        expenseReportPage = new ExpenseReportPage();
        map = new HashMap<>();
        standardControl = new StandardControl();
        invoice = new ExpenseReportInvoice();
    }

    @BeforeMethod
    public void cleanMap(){
        if(map.size()!=0){
            map.clear();
        }
    }

    @Test(description = "非金额管控-飞机舱等管控-不包含-经济舱")
    public void noAmountControlTest01() throws HttpStatusException {
        //新建非金额管控
        StandardRules rules = standardControl.setNoAmountRule("飞机", "费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee, rules, new String[]{}, new String[]{"自动化测试-日常报销单"}, "airplan-autotest");
        //设置飞机舱等
        standardControl.setNoAmountStandard(employee, rules, true,ruleOID, new String[]{},"经济舱");
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setAirTrainShipInvoice(employee, "airplan-autotest", formDetail.getReportOID(), new String[]{""},"商务舱");
        map.put("ruleOID", ruleOID);
        map.put("reportOID1", formDetail.getReportOID());
        map.put("invoiceOID1", invoiceOID1);
        String label = String.format("%s{飞机舱等标准为经济舱}", rules.getMessage());
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001"), label);
    }

    @Test(description = "非金额管控-飞机舱等管控-不包含-商务舱")
    public void noAmountControlTest02() throws HttpStatusException {
        //新建非金额管控
        StandardRules rules = standardControl.setNoAmountRule("飞机", "费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee, rules, new String[]{}, new String[]{"自动化测试-日常报销单"}, "airplan-autotest");
        //设置飞机舱等
        standardControl.setNoAmountStandard(employee, rules, true,ruleOID, new String[]{},"商务舱");
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setAirTrainShipInvoice(employee, "airplan-autotest", formDetail.getReportOID(), new String[]{employee.getFullName()},"经济舱");
        map.put("ruleOID", ruleOID);
        map.put("reportOID1", formDetail.getReportOID());
        map.put("invoiceOID1", invoiceOID1);
        String label = String.format("%s{飞机舱等标准为商务舱}", rules.getMessage());
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001"), label);
    }

    @Test(description = "非金额管控-费用大类-飞机舱等管控-不包含-商务舱")
    public void noAmountControlTest03() throws HttpStatusException {
        //新建非金额管控
        StandardRules rules = standardControl.setNoAmountRule("飞机", "费用大类");
        String ruleOID = reimbStandard.addReimbstandard(employee, rules, new String[]{}, new String[]{"自动化测试-日常报销单"}, "机票");
        //设置飞机舱等
        standardControl.setNoAmountStandard(employee, rules,true,ruleOID,new String[]{},"商务舱");
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setAirTrainShipInvoice(employee, "airplan-autotest", formDetail.getReportOID(), new String[]{employee.getFullName()},"经济舱");
        map.put("ruleOID", ruleOID);
        map.put("reportOID1", formDetail.getReportOID());
        map.put("invoiceOID1", invoiceOID1);
        String label = String.format("%s{飞机舱等标准为商务舱}", rules.getMessage());
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001"), label);
    }

    @Test(description = "非金额管控-飞机舱等管控-费用参与人管控开启-就高-不包含-基本标准")
    public void noAmountControlTest04() throws HttpStatusException {
        //新建非金额管控
        StandardRules rules = standardControl.setNoAmountRule("飞机", "费用类型","HIGH");
        String ruleOID = reimbStandard.addReimbstandard(employee, rules, new String[]{}, new String[]{"自动化测试-日常报销单"}, "airplan-autotest");
        //设置飞机舱等
        standardControl.setNoAmountStandard(employee, rules, true,ruleOID, new String[]{"auto_test_employee006"},"经济舱");
        standardControl.setNoAmountStandard(employee, rules, false,ruleOID, new String[]{"auto_test_oneself"},"商务舱");
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setAirTrainShipInvoice(employee, "airplan-autotest", formDetail.getReportOID(), new String[]{employee.getFullName(),"员工0006"},"头等舱");
        map.put("ruleOID", ruleOID);
        map.put("reportOID1", formDetail.getReportOID());
        map.put("invoiceOID1", invoiceOID1);
        String label = String.format("%s{飞机舱等标准为商务舱}", rules.getMessage());
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001"), label);
    }

    @Test(description = "非金额管控-飞机舱等管控-费用参与人管控开启-就低-不包含-基本标准")
    public void noAmountControlTest05() throws HttpStatusException {
        //新建非金额管控
        StandardRules rules = standardControl.setNoAmountRule("飞机", "费用类型","LOW");
        String ruleOID = reimbStandard.addReimbstandard(employee, rules, new String[]{}, new String[]{"自动化测试-日常报销单"}, "airplan-autotest");
        //设置飞机舱等
        standardControl.setNoAmountStandard(employee, rules, true,ruleOID, new String[]{"auto_test_employee006"},"经济舱");
        standardControl.setNoAmountStandard(employee, rules, false,ruleOID, new String[]{"auto_test_oneself"},"商务舱");
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setAirTrainShipInvoice(employee, "airplan-autotest", formDetail.getReportOID(), new String[]{employee.getFullName(),"员工0006"},"头等舱");
        map.put("ruleOID", ruleOID);
        map.put("reportOID1", formDetail.getReportOID());
        map.put("invoiceOID1", invoiceOID1);
        String label = String.format("%s{飞机舱等标准为经济舱}", rules.getMessage());
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001"), label);
    }

    @Test(description = "非金额管控-费用类型-火车管控-不包含-基本标准()二等座")
    public void noAmountControlTest06() throws HttpStatusException {
        //新建非金额管控
        StandardRules rules = standardControl.setNoAmountRule("火车", "费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee, rules, new String[]{}, new String[]{"自动化测试-日常报销单"}, "train-autotest");
        //设置火车座等
        standardControl.setNoAmountStandard(employee, rules, true,ruleOID,new String[]{},"二等座");
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setAirTrainShipInvoice(employee, "train-autotest", formDetail.getReportOID(), new String[]{employee.getFullName()},"一等座");
        map.put("ruleOID", ruleOID);
        map.put("reportOID1", formDetail.getReportOID());
        map.put("invoiceOID1", invoiceOID1);
        String label = String.format("%s{火车座等标准为二等座}", rules.getMessage());
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001"), label);
    }

    @Test(description = "非金额管控-费用大类-火车管控-不包含-二等座")
    public void noAmountControlTest07() throws HttpStatusException{
        //新建非金额管控
        StandardRules rules = standardControl.setNoAmountRule("火车", "费用大类");
        String ruleOID = reimbStandard.addReimbstandard(employee, rules, new String[]{}, new String[]{"自动化测试-日常报销单"}, "火车");
        //设置火车座等
        standardControl.setNoAmountStandard(employee, rules, true,ruleOID,new String[]{},"二等座");
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setAirTrainShipInvoice(employee, "train-autotest", formDetail.getReportOID(), new String[]{employee.getFullName()},"一等座");
        map.put("ruleOID", ruleOID);
        map.put("reportOID1", formDetail.getReportOID());
        map.put("invoiceOID1", invoiceOID1);
        String label = String.format("%s{火车座等标准为二等座}", rules.getMessage());
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001"), label);
    }

    @Test(description = "非金额管控-费用类型-火车管控-费用参与人管控开启：就高-不包含-基本标准(二等座)")
    public void noAmountControlTest08() throws HttpStatusException {
        //新建非金额管控
        StandardRules rules = standardControl.setNoAmountRule("火车", "费用类型","HIGH");
        String ruleOID = reimbStandard.addReimbstandard(employee, rules, new String[]{}, new String[]{"自动化测试-日常报销单"}, "train-autotest");
        //设置火车座等
        standardControl.setNoAmountStandard(employee, rules, true,ruleOID,new String[]{"auto_test_employee006"},"二等座");
        standardControl.setNoAmountStandard(employee, rules, false,ruleOID,new String[]{"auto_test_oneself"},"一等座");
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setAirTrainShipInvoice(employee, "train-autotest", formDetail.getReportOID(), new String[]{employee.getFullName(),"员工0006"},"二等座");
        map.put("ruleOID", ruleOID);
        map.put("reportOID1", formDetail.getReportOID());
        map.put("invoiceOID1", invoiceOID1);
        String label = String.format("%s{火车座等标准为一等座}", rules.getMessage());
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001"), label);
    }

    @Test(description = "非金额管控-费用类型-火车管控-费用参与人管控开启：就低-不包含-基本标准(二等座)")
    public void noAmountControlTest09() throws HttpStatusException {
        //新建非金额管控
        StandardRules rules = standardControl.setNoAmountRule("火车", "费用类型","LOW");
        String ruleOID = reimbStandard.addReimbstandard(employee, rules, new String[]{}, new String[]{"自动化测试-日常报销单"}, "train-autotest");
        //设置火车座等
        standardControl.setNoAmountStandard(employee, rules, true,ruleOID,new String[]{"auto_test_employee006"},"二等座");
        standardControl.setNoAmountStandard(employee, rules, false,ruleOID,new String[]{"auto_test_oneself"},"一等座");
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setAirTrainShipInvoice(employee, "train-autotest", formDetail.getReportOID(), new String[]{employee.getFullName(),"员工0006"},"一等座");
        map.put("ruleOID", ruleOID);
        map.put("reportOID1", formDetail.getReportOID());
        map.put("invoiceOID1", invoiceOID1);
        String label = String.format("%s{火车座等标准为二等座}", rules.getMessage());
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001"), label);
    }

    @Test(description = "非金额管控-费用类型-轮船管控-不包含-基本标准")
    public void noAmountControlTest10() throws HttpStatusException{
        //新建非金额管控
        StandardRules rules = standardControl.setNoAmountRule("轮船", "费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee, rules, new String[]{}, new String[]{"自动化测试-日常报销单"}, "ship-autotest");
        //设置轮船座次
        standardControl.setNoAmountStandard(employee, rules, true,ruleOID,new String[]{},"二等B");
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setAirTrainShipInvoice(employee, "ship-autotest", formDetail.getReportOID(), new String[]{employee.getFullName()},"一等舱");
        map.put("ruleOID", ruleOID);
        map.put("reportOID1", formDetail.getReportOID());
        map.put("invoiceOID1", invoiceOID1);
        String label = String.format("%s{轮船座次标准为二等B}", rules.getMessage());
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001"), label);
    }

    @Test(description = "非金额管控-费用大类-轮船管控-不包含-基本标准")
    public void noAmountControlTest11() throws HttpStatusException{
        //新建非金额管控
        StandardRules rules = standardControl.setNoAmountRule("轮船", "费用大类");
        String ruleOID = reimbStandard.addReimbstandard(employee, rules, new String[]{}, new String[]{"自动化测试-日常报销单"}, "轮船");
        //设置轮船座次
        standardControl.setNoAmountStandard(employee, rules, true,ruleOID,new String[]{},"二等B");
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setAirTrainShipInvoice(employee, "ship-autotest", formDetail.getReportOID(), new String[]{employee.getFullName()},"一等舱");
        map.put("ruleOID", ruleOID);
        map.put("reportOID1", formDetail.getReportOID());
        map.put("invoiceOID1", invoiceOID1);
        String label = String.format("%s{轮船座次标准为二等B}", rules.getMessage());
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001"), label);
    }

    @Test(description = "非金额管控-费用类型-轮船管控费用参与人管控开启:就高-不包含-基本标准")
    public void noAmountControlTest12() throws HttpStatusException{
        //新建非金额管控
        StandardRules rules = standardControl.setNoAmountRule("轮船", "费用类型","HIGH");
        String ruleOID = reimbStandard.addReimbstandard(employee, rules, new String[]{}, new String[]{"自动化测试-日常报销单"}, "ship-autotest");
        //设置轮船座次
        standardControl.setNoAmountStandard(employee, rules, true,ruleOID,new String[]{"auto_test_employee006"},"二等B");
        standardControl.setNoAmountStandard(employee, rules, false,ruleOID,new String[]{"auto_test_oneself"},"一等舱");
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setAirTrainShipInvoice(employee, "ship-autotest", formDetail.getReportOID(), new String[]{employee.getFullName(),"员工0006"},"二等B");
        map.put("ruleOID", ruleOID);
        map.put("reportOID1", formDetail.getReportOID());
        map.put("invoiceOID1", invoiceOID1);
        String label = String.format("%s{轮船座次标准为一等舱}", rules.getMessage());
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001"), label);
    }

    @Test(description = "非金额管控-费用类型-轮船管控费用参与人管控开启:就低-不包含-基本标准")
    public void noAmountControlTest13() throws HttpStatusException{
        //新建非金额管控
        StandardRules rules = standardControl.setNoAmountRule("轮船", "费用类型","LOW");
        String ruleOID = reimbStandard.addReimbstandard(employee, rules, new String[]{}, new String[]{"自动化测试-日常报销单"}, "ship-autotest");
        //设置轮船座次
        standardControl.setNoAmountStandard(employee, rules, true,ruleOID,new String[]{"auto_test_employee006"},"二等B");
        standardControl.setNoAmountStandard(employee, rules, false,ruleOID,new String[]{"auto_test_oneself"},"一等舱");
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setAirTrainShipInvoice(employee, "ship-autotest", formDetail.getReportOID(), new String[]{employee.getFullName(),"员工0006"},"一等舱");
        map.put("ruleOID", ruleOID);
        map.put("reportOID1", formDetail.getReportOID());
        map.put("invoiceOID1", invoiceOID1);
        String label = String.format("%s{轮船座次标准为二等B}", rules.getMessage());
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001"), label);
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

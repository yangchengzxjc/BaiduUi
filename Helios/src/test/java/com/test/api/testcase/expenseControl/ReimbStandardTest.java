package com.test.api.testcase.expenseControl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.basicObject.FormComponent;
import com.hand.basicObject.InvoiceComponent;
import com.hand.basicObject.Rule.StandardRules;
import com.hand.utils.UTCTime;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.Infra.SetOfBooksMethod.SetOfBooksDefine;
import com.test.api.method.ReimbStandard;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import lombok.extern.slf4j.Slf4j;

import org.testng.annotations.*;

@Slf4j
public class ReimbStandardTest extends BaseTest {
    private Employee employee;
    private ReimbStandard reimbStandard;
    private SetOfBooksDefine setOfBooksDefine;
    private ExpenseReport expenseReport;
    private ExpenseReportComponent expenseReportComponent;
    private ExpenseReportInvoice expenseReportInvoice;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("rr123456") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        reimbStandard =new ReimbStandard();
        expenseReport =new ExpenseReport();
        expenseReportComponent =new ExpenseReportComponent();
        expenseReportInvoice =new ExpenseReportInvoice();
    }

    @Test(description = "用于测试调试使用")
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void login(@Optional("14082978625") String phoneNumber, @Optional("") String pwd, @Optional("stage") String env){
        log.info("token1:{}",employee.getAccessToken());
    }

    @Test(description = "创建报销标准规则")
    public void Test01()throws HttpStatusException{
        StandardRules rules = new StandardRules();
        rules.setName("自动化测试报销标准");
        rules.setControlType("DAY");
        reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
    }

    @Test(description = "报销标准规则校验")
    public void Test02()throws HttpStatusException{
    }

    @Test(description = "删除单条报销标准规则")
    public void Test03() throws HttpStatusException{

    }

    @Test(description = "周期管控规则编辑")
    public void Test04()throws HttpStatusException{
    }

    @Test(description = "删除周期管控规则")
    public void Test05()throws HttpStatusException{
    }
}

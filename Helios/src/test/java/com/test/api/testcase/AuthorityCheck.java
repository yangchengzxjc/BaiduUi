package com.test.api.testcase;


import com.test.BaseTest;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

/**
 * @Author peng.zhang
 * @Date 2020/6/19
 * @Version 1.0
 **/
@Slf4j
public class AuthorityCheck extends BaseTest{

    private ExpenseReport expenseReport;
    private ExpenseReportInvoice expenseReportInvoice;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        expenseReport =new ExpenseReport();
        expenseReportInvoice =new ExpenseReportInvoice();
    }

    @Test(priority = 2,description = "用户登录")
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void login(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
//
//        log.info("token2:{}",employee.getAccessToken());
//        log.info("companyOID:{}",employee.getCompanyOID());
    }
}

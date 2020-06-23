package com.test.api.testcase;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.ExpenseComponent;
import com.hand.basicObject.InvoiceComponent;
import com.test.BaseTest;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

/**
 * @Author peng.zhang
 * @Date 2020/6/5
 * @Version 1.0
 **/
@Slf4j
public class ExpenseReportTest extends BaseTest{

    private ExpenseReport expenseReport;
    private ExpenseReportInvoice expenseReportInvoice;
    private Employee employee;
    private ExpenseComponent component;
    //feiyong
    private InvoiceComponent invoiceComponent;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        expenseReport =new ExpenseReport();
        component =ExpenseComponent.builder().cause("自动化测试").build();
        invoiceComponent= new InvoiceComponent();
        expenseReportInvoice =new ExpenseReportInvoice();
        employee=getEmployee(phoneNumber,pwd,env);
    }

    @Test(priority = 1,description = "用于测试调试使用")
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void login(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        log.info("token1:{}",employee.getAccessToken());
        component.setCause("");
        log.info("事由1:{}",component.getCause());
//        log.info("companyOID:{}",employee.getCompanyOID());
    }

    @Test(priority = 1,description = "创建报销单并添加费用")
    public void test1() throws HttpStatusException {
        //新建报销单
        String expenseReportOID = expenseReport.createExpenseReport(employee,"yuuki的测试表单",component).get("expenseReportOID");
        //新建费用
        expenseReportInvoice.createExpenseInvoice(employee,invoiceComponent,"火车",expenseReportOID,100).get("invoiceOID");
        //报销单提交
        assert expenseReport.expenseReportSubmit(employee,expenseReportOID).equalsIgnoreCase("true");
    }

    @Test(description = "报销单成功导入结算费用以及账本中的费用")
    public void importPublicBook() throws HttpStatusException {
        //新建报销单
        String expenseReportOID = expenseReport.createExpenseReport(employee, "yuuki的测试表单",component).get("expenseReportOID");
        //导入结算费用
        assert expenseReport.importInvoice(employee, expenseReportOID, 1, false).equals("true");
        //导入账本中的费用
        assert expenseReport.importInvoice(employee, expenseReportOID, 1, true).equals("true");
    }
}

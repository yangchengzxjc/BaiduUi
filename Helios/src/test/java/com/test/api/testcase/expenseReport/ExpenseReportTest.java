package com.test.api.testcase.expenseReport;

import com.google.gson.JsonArray;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.component.FormComponent;
import com.hand.basicObject.component.InvoiceComponent;
import com.test.BaseTest;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
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
    private FormComponent component;
    private InvoiceComponent invoiceComponent;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        expenseReport =new ExpenseReport();
        component = FormComponent.builder().cause("自动化测试").build();
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

    @Test(priority = 1,description = "创建报销单并添加费用-删除费用-在账本中能够查找到费用")
    public void test1() throws HttpStatusException {
        //新建报销单
        String expenseReportOID = expenseReport.createExpenseReport(employee,"yuuki的测试表单",component).get("expenseReportOID");
        //新建费用
        String invoiceOID = expenseReportInvoice.createExpenseInvoice(employee,invoiceComponent,"火车",expenseReportOID,100.00,new JsonArray()).get("invoiceOID");
        //报销单提交
        assert expenseReport.expenseReportSubmit(employee,expenseReportOID).equalsIgnoreCase("true");
        //报销单撤回
        expenseReport.withdraw(employee,expenseReportOID);
        //删除费用
        expenseReport.removeInvoice(employee,expenseReportOID,invoiceOID);
        //在账本中查找退回
        assert expenseReportInvoice.getExpenseItem(employee).toString().contains(invoiceOID);
        expenseReport.deleteExpenseReport(employee,expenseReportOID);
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

package com.test.api.testcase.invoice;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.test.BaseTest;
import com.test.api.method.ExpenseMethod.BookInvoicePage;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * @Author peng.zhang
 * @Date 2020/6/28
 * @Version 1.0
 **/
public class BookInvoiceTest extends BaseTest {
    private ExpenseReport expenseReport;
    private ExpenseReportInvoice expenseReportInvoice;
    private Employee employee;
    private FormComponent component;
    private BookInvoicePage bookInvoicePage;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        expenseReport =new ExpenseReport();
        expenseReportInvoice =new ExpenseReportInvoice();
        employee=getEmployee(phoneNumber,pwd,env);
        bookInvoicePage =new BookInvoicePage();
    }


    @Test(description = "账本费用转交")
    public void transferInvoice() throws HttpStatusException {
        assert bookInvoicePage.transferInvoice(employee).equals("true");
    }

}

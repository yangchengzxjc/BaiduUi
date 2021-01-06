package com.test.api.testcase.invoice.receipt;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import com.test.api.method.BusinessMethod.ExpenseReportPage;
import com.test.api.method.ExpenseControlMethod.StandardControl;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.ReceiptControlConfig;
import com.test.api.method.ReimbStandard;
import org.testng.annotations.*;

import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2021/1/5
 * @Version 1.0
 **/
public class ReceiptControlTest extends BaseTest {

    private Employee employee;
    private ExpenseReportPage expenseReportPage;
    private ExpenseReport expenseReport;
    private ExpenseReportInvoice invoice;
    private ReceiptControlConfig receiptControlConfig;
    private HashMap<String,String> items;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("rr123456") String pwd, @Optional("stage") String env) {
        employee = getEmployee(phoneNumber, pwd, env);
        expenseReport = new ExpenseReport();
        expenseReportPage = new ExpenseReportPage();
        invoice = new ExpenseReportInvoice();
        receiptControlConfig = new ReceiptControlConfig();
        items = new HashMap<>();
    }

    @BeforeMethod
    public void cleanMap(){
        if(items.size()!=0){
            items.clear();
        }
    }

    @Test(description = "免贴票管控-费用公司已付标签-免贴票")
    public void noPasteReceiptTest01() throws HttpStatusException {
        //新建发票管控规则公司已付-免贴票
        receiptControlConfig.noPasteReceipt(employee,"公司已付");
    }

    @AfterMethod
    public void cleanConfig() throws HttpStatusException {
        receiptControlConfig.deleteConfigItem(employee,items.get(""));
    }
}

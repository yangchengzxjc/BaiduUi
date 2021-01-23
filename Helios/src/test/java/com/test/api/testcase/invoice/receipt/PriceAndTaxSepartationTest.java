package com.test.api.testcase.invoice.receipt;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import com.test.api.method.BusinessMethod.ExpenseReportPage;
import com.test.api.method.ExpenseMethod.ReceiptMethodPage;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.ReceiptControlConfig;
import org.testng.annotations.*;

import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2021/1/19
 * @Version 1.0
 **/
public class PriceAndTaxSepartationTest extends BaseTest {

    private Employee employee;
    private ExpenseReportPage expenseReportPage;
    private ExpenseReport expenseReport;
    private ReceiptControlConfig receiptControlConfig;
    private ExpenseReportInvoice expenseReportInvoice;
    private HashMap<String,String> map;
    private ReceiptMethodPage receiptMethodPage;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082971222") String phoneNumber, @Optional("zp123456") String pwd, @Optional("stage") String env) {
        employee = getEmployee(phoneNumber, pwd, env);
        expenseReportPage = new ExpenseReportPage();
        receiptControlConfig = new ReceiptControlConfig();
        map = new HashMap<>();
        expenseReport = new ExpenseReport();
        expenseReportInvoice = new ExpenseReportInvoice();
        receiptMethodPage = new ReceiptMethodPage();
    }

    @BeforeMethod
    public void cleanMap(){
        if(map.size()!=0){
            map.clear();
        }
    }

    @Test(description = "价税分离-")
    public void priceAndTaxSepartationTest01() throws HttpStatusException {
        //测试新建规则
        receiptMethodPage.expenseTypePriceTax(employee,"autotest","03");
    }
}

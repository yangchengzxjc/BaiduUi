package com.test.api.testcase.invoice.receipt;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicConstant.Receript;
import com.hand.basicObject.Employee;
import com.hand.basicObject.component.FormDetail;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.BusinessMethod.ExpenseReportPage;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.ReceiptControlConfig;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2021/1/5
 * @Version 1.0
 **/
public class NoPasteReceiptControlTest extends BaseTest {

    private Employee employee;
    private ExpenseReportPage expenseReportPage;
    private ExpenseReport expenseReport;
    private ReceiptControlConfig receiptControlConfig;
    private ExpenseReportInvoice expenseReportInvoice;
    private HashMap<String,String> map;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("rr123456") String pwd, @Optional("stage") String env) {
        employee = getEmployee(phoneNumber, pwd, env);
        expenseReportPage = new ExpenseReportPage();
        receiptControlConfig = new ReceiptControlConfig();
        map = new HashMap<>();
        expenseReport = new ExpenseReport();
        expenseReportInvoice = new ExpenseReportInvoice();
    }

    @BeforeMethod
    public void cleanMap(){
        if(map.size()!=0){
            map.clear();
        }
    }

    @Test(description = "免贴票管控-费用公司已付标签-免贴票")
    public void noPasteReceiptTest01() throws HttpStatusException {
        //新建发票管控规则公司已付-免贴票
        receiptControlConfig.noPasteReceipt(employee,"公司已付");
        FormDetail formDetail= expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        //新建公司已付的费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"交通",formDetail.getReportOID(),true);
        map.put("invoiceOID1",invoiceOID1);
        Assert.assertEquals("免贴票",expenseReportInvoice.checkInvoiceLabelName(employee,invoiceOID1,"INVOICE_FREE"));
    }

    @Test(description = "免贴票管控-电子票-免贴票")
    public void noPasteReceiptTest02() throws HttpStatusException {
        //新建发票管控规则公司已付-免贴票
        receiptControlConfig.noPasteReceipt(employee,"电子票");
        FormDetail formDetail= expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        //新建公司已付的费用
        FormDetail invoice = expenseReportPage.setReceiptInvoice(employee,"交通",formDetail.getReportOID(), Receript.receipt4);
        map.put("invoiceOID1",invoice.getInvoiceOID());
        Assert.assertEquals("免贴票",expenseReportInvoice.checkInvoiceLabelName(employee,invoice.getInvoiceOID(),"INVOICE_FREE"));
    }

    @Test(description = "免贴票管控-费用无票标签-免贴票")
    public void noPasteReceiptTest03() throws HttpStatusException {
        //新建发票管控规则公司已付-免贴票
        receiptControlConfig.noPasteReceipt(employee,"无票");
        FormDetail formDetail= expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        //新建公司已付的费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"无票费用",formDetail.getReportOID(),false);
        map.put("invoiceOID1",invoiceOID1);
        Assert.assertEquals("免贴票",expenseReportInvoice.checkInvoiceLabelName(employee,invoiceOID1,"INVOICE_FREE"));
    }

    @AfterMethod
    public void cleanEnv() throws HttpStatusException {
        //删除发票管控中的免贴票管控
        receiptControlConfig.deleteConfigItem(employee,"1");
        for (String s : map.keySet()){
            switch (s){
                case "reportOID":
                    expenseReport.deleteExpenseReport(employee, map.get("reportOID"));
                    break;
                case "invoiceOID1":
                    expenseReportInvoice.deleteInvoice(employee, map.get("invoiceOID1"));
                    break;
                case "invoiceOID2":
                    expenseReportInvoice.deleteInvoice(employee, map.get("invoiceOID2"));
                    break;
            }
        }
    }
}

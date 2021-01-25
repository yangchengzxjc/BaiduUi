package com.test.api.testcase.invoice.receipt;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicConstant.Receript;
import com.hand.basicObject.Employee;
import com.hand.basicObject.component.FormDetail;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.BusinessMethod.ExpenseReportPage;
import com.test.api.method.ExpenseMethod.ReceiptMethodPage;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.ReceiptControlConfig;
import org.testng.Assert;
import org.testng.annotations.*;

import java.math.BigDecimal;
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

    @DataProvider(name = "receipt")
    public Object[][] receipt(){
        return new Object[][]{
                {"机票行程单","机票",Receript.airReceipt},
                {"增值税专用发票","机票",Receript.zengzhuanRecwipt},
                {"火车票","火车",Receript.trainReceipt},
                {"客运发票","交通",Receript.busReceipt},
                {"电子普通发票-客运服务","交通",Receript.carReceipt},
                {"增值税普通发票（电子）（通行费）","交通",Receript.tollReceipt}
        };
    }

    @Test(description = "可抵扣的发票-价税分离",dataProvider = "receipt")
    public void priceAndTaxSepartationTest01(String desc, String expenseName ,String receipt) throws HttpStatusException {
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setReceiptInvoice(employee,expenseName,formDetail.getReportOID(), receipt);
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        Assert.assertEquals("可抵扣",expenseReportInvoice.checkInvoiceLabelName(employee,invoice1.getInvoiceOID(),"DEDUCTIBLE"));
    }

    @Test(description = "可抵扣的费用-价税分离(税率为3%)-校验条件-费用类型")
    public void priceAndTaxSepartationTest02() throws HttpStatusException {
        //新建价税分离规则
        String separationInvoiceOptId = receiptMethodPage.expenseTypePriceTax(employee,"autotest","03");
        map.put("separationInvoiceOptId",separationInvoiceOptId);
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        String invoiceOID = expenseReportPage.setInvoice(employee,"autotest",formDetail.getReportOID());
        map.put("invoiceOID1",invoiceOID);
        Assert.assertEquals("可抵扣",expenseReportInvoice.checkInvoiceLabelName(employee,invoiceOID,"DEDUCTIBLE"));
        //校验不含税金额
        Assert.assertEquals("194.1700",expenseReportInvoice.getExpenseKeyWords(employee,invoiceOID,"expenseAmount"));
        //检查税额
        Assert.assertEquals("5.83",expenseReportInvoice.getExpenseKeyWords(employee,invoiceOID,"expenseAmount"));
    }


    @AfterMethod
    public void cleanEnv() throws HttpStatusException {
        for (String s : map.keySet()) {
            switch (s){
                case "reportOID":
                    if (expenseReport.getReportStatus(employee, map.get("reportOID")) == 1002) {
                        expenseReport.withdraw(employee, map.get("reportOID"));
                    }
                    expenseReport.deleteExpenseReport(employee, map.get("reportOID"));
                    break;
                case "invoiceOID1":
                    expenseReportInvoice.deleteInvoice(employee, map.get("invoiceOID1"));
                    break;
                case "invoiceOID2":
                    expenseReportInvoice.deleteInvoice(employee, map.get("invoiceOID2"));
                    break;
                case "separationInvoiceOptId":
                    receiptControlConfig.deleteSeparationConfig(employee,map.get("separationInvoiceOptId"));
                    break;
            }
        }
    }
}

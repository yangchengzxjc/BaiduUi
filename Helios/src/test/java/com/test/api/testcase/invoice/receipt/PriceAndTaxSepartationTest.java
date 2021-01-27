package com.test.api.testcase.invoice.receipt;

import com.google.gson.JsonObject;
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
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2021/1/19
 * @Version 1.0
 **/
@Slf4j
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

    /**
     * 发票税额计算方式
     * 发票税额 = 发票价税合计 / （1 + 税率）* 税率
     *
     * 不足额报销税额 = 报销金额 / 发票价税合计 * 发票税额
     *
     */

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

    @DataProvider(name = "expenseLabel")
    public Object[][] expenseLabel(){
        return new Object[][]{
                {"机票","机票",Receript.airReceipt},
                {"火车票","火车",Receript.trainReceipt},
                {"客运","交通",Receript.busReceipt},
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
        Assert.assertEquals("5.83",expenseReportInvoice.getExpenseKeyWords(employee,invoiceOID,"tax"));
    }

    @Test(description = "可抵扣的费用-价税分离(税率为3%)-校验条件-费用类型(消费商费用-滴滴费用)")
    public void priceAndTaxSepartationTest03() throws HttpStatusException {
        //推送一笔滴滴费用
        expenseReportInvoice.pushDiDi(employee,false);
        ArrayList <String> invoiceOIDs = expenseReportInvoice.getInvoiceOIDInBook(employee,"滴滴出行");
        map.put("invoiceOID1", invoiceOIDs.get(0));
        //新建价税分离规则
        String separationInvoiceOptId = receiptMethodPage.expenseTypePriceTax(employee,"autotest","03");
        map.put("separationInvoiceOptId",separationInvoiceOptId);
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        expenseReport.importInvoice(employee,formDetail.getReportOID(),invoiceOIDs);
        Assert.assertEquals("可抵扣",expenseReportInvoice.checkInvoiceLabelName(employee,invoiceOIDs.get(0),"DEDUCTIBLE"));
        JsonObject invoiceDetail = expenseReportInvoice.getInvoice(employee,invoiceOIDs.get(0));
        //校验不含税金额
        BigDecimal nonVatBaseAmount = invoiceDetail.get("amount").getAsBigDecimal().divide(new BigDecimal(1.03),2, RoundingMode.HALF_UP);
        Assert.assertEquals(nonVatBaseAmount,invoiceDetail.get("nonVatBaseAmount").getAsBigDecimal().setScale(2,RoundingMode.HALF_UP));
        //校验税率
        Assert.assertEquals(nonVatBaseAmount.multiply(new BigDecimal(0.03)).setScale(2, RoundingMode.HALF_UP),invoiceDetail.get("taxAmount").getAsBigDecimal().setScale(2,RoundingMode.HALF_UP));
    }

    @Test(description = "可抵扣的费用-价税分离(税率为3%)-校验条件-费用标签(无票)")
    public void priceAndTaxSepartationTest04() throws HttpStatusException {
        //新建价税分离规则
        String separationInvoiceOptId = receiptMethodPage.involiceLabelPriceTax(employee,"无票","03");
        map.put("separationInvoiceOptId",separationInvoiceOptId);
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        String invoiceOID = expenseReportPage.setInvoice(employee,"无票费用",formDetail.getReportOID());
        map.put("invoiceOID1",invoiceOID);
        Assert.assertEquals("可抵扣",expenseReportInvoice.checkInvoiceLabelName(employee,invoiceOID,"DEDUCTIBLE"));
        //校验不含税金额
        Assert.assertEquals("194.1700",expenseReportInvoice.getExpenseKeyWords(employee,invoiceOID,"expenseAmount"));
        //检查税额
        Assert.assertEquals("5.83",expenseReportInvoice.getExpenseKeyWords(employee,invoiceOID,"tax"));
    }

    @Test(description = "可抵扣的费用-价税分离-校验条件-费用标签（火车票）")
    public void priceAndTaxSepartationTest05() throws HttpStatusException {
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setReceiptInvoice(employee,"火车",formDetail.getReportOID(),Receript.trainReceipt);
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        Assert.assertEquals("可抵扣",expenseReportInvoice.checkInvoiceLabelName(employee,invoice1.getInvoiceOID(),"DEDUCTIBLE"));
        //校验不含税金额
        JsonObject invoiceDetail = expenseReportInvoice.getInvoice(employee,invoice1.getInvoiceOID());
        //此时获取的税额是没有进行小数点左移两位的
        BigDecimal receiptTax = invoiceDetail.getAsJsonArray("receiptList").get(0).getAsJsonObject().get("tax").getAsBigDecimal();
        // 发票的价税合计
        BigDecimal fee = invoiceDetail.getAsJsonArray("receiptList").get(0).getAsJsonObject().get("fee").getAsBigDecimal();
        //费用的税额
        BigDecimal invoiceTax = expenseReportInvoice.getInvoiceTax(new BigDecimal(5),expenseReportInvoice.getAmount(fee),expenseReportInvoice.getAmount(receiptTax));
        //校验税额
        Assert.assertEquals(invoiceTax,invoiceDetail.get("taxAmount").getAsBigDecimal().setScale(2,RoundingMode.HALF_UP));
        //校验不含税金额
        Assert.assertEquals(expenseReportInvoice.getFeeWithoutTax(new BigDecimal(5),invoiceTax),invoiceDetail.get("nonVatBaseAmount").getAsBigDecimal().setScale(2,RoundingMode.HALF_UP));
    }

    @Test(description = "可抵扣的费用-价税分离-校验条件-费用标签（机票）")
    public void priceAndTaxSepartationTest06() throws HttpStatusException {
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setReceiptInvoice(employee,"机票",formDetail.getReportOID(),Receript.airReceipt);
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        Assert.assertEquals("可抵扣",expenseReportInvoice.checkInvoiceLabelName(employee,invoice1.getInvoiceOID(),"DEDUCTIBLE"));
        //校验不含税金额
        JsonObject invoiceDetail = expenseReportInvoice.getInvoice(employee,invoice1.getInvoiceOID());
        //此时获取的税额是没有进行小数点左移两位的
        BigDecimal receiptTax = invoiceDetail.getAsJsonArray("receiptList").get(0).getAsJsonObject().get("tax").getAsBigDecimal();
        // 发票的价税合计
        BigDecimal fee = invoiceDetail.getAsJsonArray("receiptList").get(0).getAsJsonObject().get("fee").getAsBigDecimal();
        //费用的税额
        BigDecimal invoiceTax = expenseReportInvoice.getInvoiceTax(new BigDecimal(5),expenseReportInvoice.getAmount(fee),expenseReportInvoice.getAmount(receiptTax));
        //校验税额
        Assert.assertEquals(invoiceTax,invoiceDetail.get("taxAmount").getAsBigDecimal().setScale(2,RoundingMode.HALF_UP));
        //校验不含税金额
        Assert.assertEquals(expenseReportInvoice.getFeeWithoutTax(new BigDecimal(5),invoiceTax),invoiceDetail.get("nonVatBaseAmount").getAsBigDecimal().setScale(2,RoundingMode.HALF_UP));
    }

    @Test(description = "可抵扣的费用-价税分离-校验条件-费用标签（客运发票）")
    public void priceAndTaxSepartationTest07() throws HttpStatusException {
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setReceiptInvoice(employee,"交通",formDetail.getReportOID(),Receript.busReceipt);
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        Assert.assertEquals("可抵扣",expenseReportInvoice.checkInvoiceLabelName(employee,invoice1.getInvoiceOID(),"DEDUCTIBLE"));
        //校验不含税金额
        JsonObject invoiceDetail = expenseReportInvoice.getInvoice(employee,invoice1.getInvoiceOID());
        //此时获取的税额是没有进行小数点左移两位的
        BigDecimal receiptTax = invoiceDetail.getAsJsonArray("receiptList").get(0).getAsJsonObject().get("tax").getAsBigDecimal();
        log.info("发票的税额{}",receiptTax);
        // 发票的价税合计
        BigDecimal fee = invoiceDetail.getAsJsonArray("receiptList").get(0).getAsJsonObject().get("fee").getAsBigDecimal();
        log.info("发票的价税合计{}",fee);
        //费用的税额
        BigDecimal invoiceTax = expenseReportInvoice.getInvoiceTax(new BigDecimal(5),expenseReportInvoice.getAmount(fee),expenseReportInvoice.getAmount(receiptTax));
        //校验税额
        log.info("费用的税额为：{}",invoiceTax);
        Assert.assertEquals(invoiceTax,invoiceDetail.get("taxAmount").getAsBigDecimal().setScale(2,RoundingMode.HALF_UP));
        //校验不含税金额
        Assert.assertEquals(expenseReportInvoice.getFeeWithoutTax(new BigDecimal(5),invoiceTax),invoiceDetail.get("nonVatBaseAmount").getAsBigDecimal().setScale(2,RoundingMode.HALF_UP));
    }


//    @AfterMethod
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

package com.test.api.testcase.monitor;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicConstant.Receript;
import com.hand.basicObject.Employee;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.*;
import com.test.api.method.ApplicationMethod.TravelApplicationPage;
import com.test.api.method.BusinessMethod.ExpenseReportPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * @Author peng.zhang
 * @Date 2020/12/15
 * @Version 1.0
 **/
public class SmokeTest extends BaseTest {

    private Employee employee;
    private ExpenseReport expenseReport;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("16900000001") String phoneNumber, @Optional("hly123") String pwd, @Optional("console") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        expenseReport = new ExpenseReport();
    }

    @Test(description = "核心功能")
    public void smokeTest01() throws HttpStatusException {
        //新建差旅申请单
        TravelApplicationPage travelApplicationPage =new TravelApplicationPage();
        ExpenseReportPage expenseReportPage = new ExpenseReportPage();
        String applicatioOID = travelApplicationPage.setTravelApplication(employee,"差旅申请单-自动化测试",UTCTime.getUTCDateEnd(-2));
        //关联报销单
        String reportOID = expenseReportPage.setTravelReport(employee,"差旅报销单-自动化测试",applicatioOID);
        // 新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID,UTCTime.getUtcTime(-2,0));
        //新建费用账本导入
        String invoiceOID2 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", "",UTCTime.getUtcTime(-2,0));
        //导入费用
        ArrayList<String> invoices = new ArrayList<>();
        invoices.add(invoiceOID2);
        expenseReport.importInvoice(employee,reportOID,invoices);
        //提交报销单
        expenseReport.expenseReportSubmit(employee,reportOID);
        //审批报销单
        Approve approve = new Approve();
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        approve.approveal(employee,reportOID,1002);
        //审核成功
        assert approve.auditPass(employee,reportOID,1002)==0;
    }

    @Test(description = "发票查验")
    public void smokeTest02() throws HttpStatusException {
        ExpenseReportInvoice invoice = new ExpenseReportInvoice();
        String msg = invoice.receptVerify(employee, Receript.handRecept);
        Assert.assertEquals(msg,"查验成功，发票一致");
    }

    @Test(description = "ocr发票识别并查验")
    public void smokeTest03() throws HttpStatusException {
        ExpenseReportInvoice invoice = new ExpenseReportInvoice();
        String msg = invoice.ocrReceptVerify(employee,Receript.ocrReceipt);
        Assert.assertEquals(msg,"查验成功，发票一致");
    }
}

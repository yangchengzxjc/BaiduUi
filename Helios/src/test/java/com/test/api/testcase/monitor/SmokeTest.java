package com.test.api.testcase.monitor;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicConstant.Receript;
import com.hand.basicObject.Employee;
import com.hand.basicObject.component.FormDetail;
import com.hand.basicObject.component.LoanBillComponent;
import com.hand.utils.DButil;
import com.hand.utils.GsonUtil;
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
    private String environment;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082971221") String phoneNumber, @Optional("zp123456") String pwd, @Optional("console") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        expenseReport = new ExpenseReport();
        environment = env;
    }

    @Test(description = "核心功能+外部接口回调")
    public void smokeTest01() throws HttpStatusException {
        //新建差旅申请单
        TravelApplicationPage travelApplicationPage =new TravelApplicationPage();
        ExpenseReportPage expenseReportPage = new ExpenseReportPage();
        String applicatioOID = travelApplicationPage.setTravelApplication(employee,"差旅申请单-自动化测试",UTCTime.getUTCDateEnd(-2));
        //关联报销单
        FormDetail formDetail = expenseReportPage.setTravelReport(employee,"差旅报销单-自动化测试",applicatioOID);
        // 新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", formDetail.getReportOID(),UTCTime.getUtcTime(-2,0));
        //新建费用账本导入
        String invoiceOID2 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", "",UTCTime.getUtcTime(-2,0));
        //导入费用
        ArrayList<String> invoices = new ArrayList<>();
        invoices.add(invoiceOID2);
        expenseReport.importInvoice(employee,formDetail.getReportOID(),invoices);
        //提交报销单
        expenseReport.expenseReportSubmit(employee,formDetail.getReportOID());
        //测试报销单提交回调  只能在生产环境
        if(environment.equals("console")){
            DButil.Business business = new DButil.Business();
            try {
                business = DButil.dbConnection(formDetail.getBusinessCode());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Assert.assertEquals(formDetail.getBusinessCode(),business.getBusinessCode());
            Assert.assertEquals("EXPENSE_REPORT_SUBMIT",business.getApiCode());
        }
        //审批报销单
        Approve approve = new Approve();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        approve.approveal(employee,formDetail.getReportOID(),1002);
        //审核成功
        assert approve.auditPass(employee,formDetail.getReportOID(),1002) == 0;
    }

    @Test(description = "发票查验")
    public void smokeTest02() throws HttpStatusException {
        ExpenseReportInvoice invoice = new ExpenseReportInvoice();
        String msg = invoice.receptVerify(employee, Receript.handRecept);
        //发票每日只能查验5次， 做一个兜底 选用备用发票
        if(!msg.equals("查验成功，发票一致")){
            msg = invoice.receptVerify(employee, Receript.handReceipt2);
        }
        Assert.assertEquals(msg,"查验成功，发票一致");
    }

    @Test(description = "员工ocr发票识别并查验")
    public void smokeTest03() throws HttpStatusException {
        ExpenseReportInvoice invoice = new ExpenseReportInvoice();
        String msg = invoice.ocrReceptVerify(employee,Receript.ocrReceipt);
        if(!msg.equals("查验成功，发票一致")){
            msg = invoice.ocrReceptVerify(employee,Receript.scanOcrReceipt);
        }
        Assert.assertEquals(msg,"查验成功，发票一致");
    }

    @Test(description = "票小蜜：财务scan ocr 识别")
    public void smokeTest04() throws HttpStatusException {
        ExpenseReportInvoice invoice = new ExpenseReportInvoice();
        String message = invoice.scanOcr(employee,Receript.scanOcrReceipt,"6285295");
        Assert.assertEquals(message,"发票识别成功");
        Approve approve = new Approve();
        approve.deleteReceipt(employee,"05614dba-7ab8-44b4-96c7-5da069b4572f");
    }

    @Test(description = "员工ocr识别发票")
    public void smokeTest05() throws HttpStatusException {
        ExpenseReportInvoice invoice = new ExpenseReportInvoice();
        boolean msg = invoice.ocr(employee,Receript.ocrReceipt);
        Assert.assertTrue(msg);
    }

    @Test(description = "单行个人借款单提交-撤回-删除")
    public void smokeTest06() throws HttpStatusException {
        ExpenseReportPage expenseReportPage = new ExpenseReportPage();
        ExpenseReport expenseReport = new ExpenseReport();
        FormDetail formDetail = expenseReportPage.setDefaultLoanBill(employee,"个人借款单",UTCTime.getFormStartDate(1));
        //新建借款行
        expenseReportPage.setLoanLine(employee,formDetail,"个人借款单",true);
        //提交借款单
        expenseReport.submitLoanBill(employee,formDetail.getReportOID());
        //审批借款单
        Approve approve = new Approve();
        int successNumber = approve.approveal(employee,formDetail.getReportOID(),3001);
        Assert.assertEquals(successNumber,1);
        //审核拒绝
        int fail = approve.auditReject(employee,formDetail.getReportOID(),3001);
        Assert.assertEquals(fail,0);
        expenseReport.deleteLoanBill(employee,formDetail.getReportOID());
    }

    @Test(description = "单行个人借款单-对公预付提交-撤回-删除")
    public void smokeTest07() throws HttpStatusException {
        ExpenseReportPage expenseReportPage = new ExpenseReportPage();
        ExpenseReport expenseReport = new ExpenseReport();
        FormDetail formDetail = expenseReportPage.setDefaultLoanBill(employee,"个人借款单",UTCTime.getFormStartDate(1));
        //新建借款行
        expenseReportPage.setLoanLine(employee,formDetail,"个人借款单",false);
        //提交借款单
        expenseReport.submitLoanBill(employee,formDetail.getReportOID());
        //审批借款单
        Approve approve = new Approve();
        int successNumber = approve.approveal(employee,formDetail.getReportOID(),3001);
        Assert.assertEquals(successNumber,1);
        //审核拒绝
        int fail = approve.auditReject(employee,formDetail.getReportOID(),3001);
        Assert.assertEquals(fail,0);
        expenseReport.deleteLoanBill(employee,formDetail.getReportOID());
    }



}

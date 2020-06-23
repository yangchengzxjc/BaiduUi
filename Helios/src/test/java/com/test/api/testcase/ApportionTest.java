package com.test.api.testcase;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * @Author peng.zhang
 * @Date 2020/6/22
 * @Version 1.0
 **/
public class ApportionTest extends BaseTest {

    private ExpenseReport expenseReport;
    private ExpenseReportInvoice expenseReportInvoice;
    private Employee employee;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        expenseReport =new ExpenseReport();
        expenseReportInvoice =new ExpenseReportInvoice();
        employee=getEmployee(phoneNumber,pwd,env);
    }

    @Test(description = "配置:报销单配置了部门控件参与分摊,表头有分摊项-费用未开启分摊->导入的费用不标记必填未输")
    public void apportionTest1() throws HttpStatusException {
        //新建火车费用（非分摊费用）
        expenseReportInvoice.createExpenseInvoice(employee,"火车","",100).get("invoiceOID");
        //新建报销单
        String expenseReportOID = expenseReport.createExpenseReport(employee,"yuuki的测试表单","","").get("expenseReportOID");
        //导入非分摊费用
        expenseReport.importInvoice(employee, expenseReportOID,"火车",employee.getFullName(), 1, true);
        assert expenseReport.invoiceLabel(employee,expenseReportOID).contains("无标签");
    }

    @Test(description = "配置:报销单配置了部门控件参与分摊,表头有分摊项->导入的费用分摊项为空，标记必填未输")
    public void apportionTest2() throws HttpStatusException {
        //新建分摊费用类型
        expenseReportInvoice.createExpenseInvoice(employee,"分摊费用类型","",100).get("invoiceOID");
        //新建报销单
        String expenseReportOID = expenseReport.createExpenseReport(employee,"yuuki的测试表单","","").get("expenseReportOID");
        //导入分摊费用
        expenseReport.importInvoice(employee, expenseReportOID,"分摊费用类型",employee.getFullName(), 1, true);
        assert expenseReport.invoiceLabel(employee,expenseReportOID).contains("费用必填字段为空");
    }

    @Test(description = "配置:报销单配置了部门控件参与分摊,表头有分摊项->导入的费用分摊项不为空，不标记必填未输")
    public void apportionTest3() throws HttpStatusException {
        //新建分摊费用类型
        expenseReportInvoice.createExpenseInvoice(employee,"分摊费用类型","",100).get("invoiceOID");
        //新建报销单
        String expenseReportOID = expenseReport.createExpenseReport(employee,"yuuki的测试表单",employee.getDepartmentOID(),"").get("expenseReportOID");
        //导入分摊费用
        expenseReport.importInvoice(employee, expenseReportOID,"分摊费用类型",employee.getFullName(), 1, true);
        assert expenseReport.invoiceLabel(employee,expenseReportOID).contains("无标签");
    }

    @Test(description = "配置:报销单表头无分摊项->导入的费用分摊项为空，不标记必填未输")
    public void apportionTest4() throws HttpStatusException {
        //新建分摊费用类型
        expenseReportInvoice.createExpenseInvoice(employee,"分摊费用类型","",100).get("invoiceOID");
        //新建报销单
        String expenseReportOID = expenseReport.createExpenseReport(employee,"日常报销单-测试",employee.getDepartmentOID(),"").get("expenseReportOID");
        //导入分摊费用
        expenseReport.importInvoice(employee, expenseReportOID,"分摊费用类型",employee.getFullName(), 1, true);
        assert expenseReport.invoiceLabel(employee,expenseReportOID).contains("无标签");
    }
}

package com.test.api.testcase.invoice;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.component.FormComponent;
import com.hand.basicObject.component.FormDetail;
import com.hand.basicObject.component.InvoiceComponent;
import com.test.BaseTest;
import com.test.api.method.ExpenseMethod.ApporationInvoicePage;
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
    private FormComponent formComponent;
    private ApporationInvoicePage apporationInvoicePage;
    private InvoiceComponent invoiceComponent;


    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        expenseReport =new ExpenseReport();
        formComponent =new FormComponent("测试导入费用分摊费用标签");
        expenseReportInvoice =new ExpenseReportInvoice();
        employee=getEmployee(phoneNumber,pwd,env);
        apporationInvoicePage = new ApporationInvoicePage();
        invoiceComponent =new InvoiceComponent();
    }

    @Test(priority = 1,description = "配置:报销单配置了部门控件参与分摊,表头有分摊项-费用未开启分摊->导入的费用不标记必填未输")
    public void apportionTest1() throws HttpStatusException {
        //新建火车费用（非分摊费用）
        expenseReportInvoice.createExpenseInvoice(employee,"火车","",100).get("invoiceOID");
        //新建报销单
        FormDetail formDetail = expenseReport.createExpenseReport(employee,"yuuki的测试表单",formComponent);
        //导入非分摊费用
        expenseReport.importInvoice(employee, formDetail.getReportOID(),"火车",employee.getFullName(), 1, true);
        assert expenseReport.invoiceLabel(employee,formDetail.getReportOID()).contains("无标签");
        //报销单删除
        expenseReport.deleteExpenseReport(employee,formDetail.getReportOID());
    }

    @Test(priority = 2,description = "配置:报销单配置了部门控件参与分摊,表头有分摊项->导入的费用分摊项为空，标记必填未输")
    public void apportionTest2() throws HttpStatusException {
        //新建分摊费用类型
        expenseReportInvoice.createExpenseInvoice(employee,"分摊费用类型","",100).get("invoiceOID");
        //新建报销单
        FormDetail formDetail = expenseReport.createExpenseReport(employee,"yuuki的测试表单","","");
        //导入分摊费用
        expenseReport.importInvoice(employee, formDetail.getReportOID(),"分摊费用类型",employee.getFullName(), 1, true);
        assert expenseReport.invoiceLabel(employee, formDetail.getReportOID()).contains("费用必填字段为空");
    }

    @Test(priority = 3,description = "配置:报销单配置了部门控件参与分摊,表头有分摊项->导入的费用分摊项不为空，不标记必填未输")
    public void apportionTest3() throws HttpStatusException {
        //表单头需要部门
        formComponent.setDepartment(employee.getDepartmentOID());
        //新建分摊费用类型
        expenseReportInvoice.createExpenseInvoice(employee,"分摊费用类型","",100).get("invoiceOID");
        //新建报销单
        FormDetail formDetail = expenseReport.createExpenseReport(employee,"yuuki的测试表单",formComponent);
        //导入分摊费用
        expenseReport.importInvoice(employee, formDetail.getReportOID(),"分摊费用类型",employee.getFullName(), 1, true);
        assert expenseReport.invoiceLabel(employee,formDetail.getReportOID()).contains("无标签");
    }

    @Test(priority = 4, description = "配置:报销单表头无分摊项->导入的费用分摊项为空，不标记必填未输")
    public void apportionTest4() throws HttpStatusException {
        //新建分摊费用类型
        expenseReportInvoice.createExpenseInvoice(employee,"分摊费用类型","",100).get("invoiceOID");
        //新建报销单
        FormDetail formDetail = expenseReport.createExpenseReport(employee,"日常报销单-测试",formComponent);
        //导入分摊费用
        expenseReport.importInvoice(employee, formDetail.getReportOID(),"分摊费用类型",employee.getFullName(), 1, true);
        assert expenseReport.invoiceLabel(employee,formDetail.getReportOID()).contains("无标签");
    }

    @Test(description = "报销单内新建分摊费用 - 按照部门进行分摊")
    public void createApporationTest5() throws HttpStatusException {
        //表单控件初始化部门信息
        formComponent.setDepartment(employee.getDepartmentOID());
        formComponent.setCause("测试报销单分摊费用");
        //新建报销单
        FormDetail formDetail = expenseReport.createExpenseReport(employee,"yuuki的测试表单",formComponent);
        //分摊行  这块分摊比例必须等于100%  否则费用创建会报错
        JsonObject defaultApporation = apporationInvoicePage.defaultApporationLine(employee,formDetail.getReportOID(),"",0.34,34.00);
        JsonObject ordinaryApportion = apporationInvoicePage.ordinaryApporationLine(employee,formDetail.getReportOID(),"0001",0.66,66);
        JsonArray expenseApportion = expenseReportInvoice.createrExpenseApporation(defaultApporation,ordinaryApportion);
        //初始化费用控件
        invoiceComponent.setCompanyPay(false);
        invoiceComponent.setCurrencyCode("CNY");
        expenseReportInvoice.createExpenseInvoice(employee,invoiceComponent,"分摊费用类型",formDetail.getReportOID(),100.00,expenseApportion);
    }

}

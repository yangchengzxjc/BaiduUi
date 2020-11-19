package com.test.api.method.BusinessMethod;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.basicObject.InvoiceComponent;
import com.hand.utils.UTCTime;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.ExpenseReportInvoice;
import lombok.extern.slf4j.Slf4j;

import javax.xml.ws.spi.Invoker;
import java.util.ArrayList;

/**
 * @Author peng.zhang
 * @Date 2020/6/17
 * @Version 1.0
 **/
@Slf4j
public class ExpenseReportPage {

    private ExpenseReport expenseReport;

    private ExpenseReportInvoice expenseReportInvoice;
    public ExpenseReportPage(){
        expenseReport =new ExpenseReport();
        expenseReportInvoice = new ExpenseReportInvoice();
    }

    /**
     * 创建一个报销单 控件有部门,开始结束日期,参与人,事由
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public String  setDailyReport(Employee employee,String endData,String formName,String participant) throws HttpStatusException {
        //新建报销单
        FormComponent component=new FormComponent();
        component.setCompany(employee.getCompanyOID());
        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(UTCTime.getFormStartDate(0));
        component.setEndDate(endData);
        //添加参与人员  参与人员的value 是一段json数组。
        JsonArray array = new JsonArray();
        ExpenseReportComponent expenseReportComponent =new ExpenseReportComponent();
        array.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,formName),participant));
        log.info(array.toString());
        component.setParticipant(array.toString());
        component.setCause("报销单提交管控规则校验");
        return expenseReport.createExpenseReport(employee,formName,component).get("expenseReportOID");
    }

    /**
     * 新建费用  不参与分摊
     * @param employee
     * @param expenseName
     * @param expenseReportOID
     * @return
     * @throws HttpStatusException
     */
    public String setInvoice(Employee employee,String expenseName,String expenseReportOID) throws HttpStatusException {
        ExpenseReportComponent expenseReportComponent =new ExpenseReportComponent();
        String cityCode =expenseReportComponent.getCityCode(employee,"西安市");
        InvoiceComponent invoiceComponent =new InvoiceComponent();
        invoiceComponent.setCity(cityCode);
        JsonObject startAndEndDate = new JsonObject();
        startAndEndDate.addProperty("startDate",UTCTime.getNowStartUtcDate());
        startAndEndDate.addProperty("endDate",UTCTime.getUTCDateEnd(2));
        startAndEndDate.addProperty("duration",2);
        invoiceComponent.setStartAndEndData(startAndEndDate.toString());
        return expenseReportInvoice.createExpenseInvoice(employee,invoiceComponent,"自动化测试-报销标准",expenseReportOID,200.00,new JsonArray()).get("invoiceOID");
    }
}
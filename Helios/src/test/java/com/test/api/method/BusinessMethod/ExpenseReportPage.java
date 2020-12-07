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

import java.util.ArrayList;
import java.util.HashMap;


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
    public HashMap<String,String> setDailyReport(Employee employee, String endData, String formName, String []participant) throws HttpStatusException {
        //新建报销单
        FormComponent component=new FormComponent();
        component.setCompany(employee.getCompanyOID());
        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(UTCTime.getFormStartDate(-3));
        component.setEndDate(endData);
        component.setParticipant(participant);
        component.setCause("invoice control");
        return expenseReport.createExpenseReport(employee,formName,component);
    }

    /**
     * 新建费用  不参与分摊   开始结束日期控件不为空
     * @param employee
     * @param expenseName
     * @param expenseReportOID
     * @return
     * @throws HttpStatusException
     */
    public String setInvoice(Employee employee,String expenseName,String expenseReportOID) throws HttpStatusException {
        ExpenseReportComponent expenseReportComponent =new ExpenseReportComponent();
        String cityCode =expenseReportComponent.getCityCode(employee,"上海");
        InvoiceComponent invoiceComponent =new InvoiceComponent();
        invoiceComponent.setCity(cityCode);
        JsonObject startAndEndDate = new JsonObject();
        startAndEndDate.addProperty("startDate",UTCTime.getFormStartDate(0));
        startAndEndDate.addProperty("endDate",UTCTime.getFormDateEnd(3));
        startAndEndDate.addProperty("duration",3);
        invoiceComponent.setStartAndEndData(startAndEndDate.toString());
        return expenseReportInvoice.createExpenseInvoice(employee,invoiceComponent,expenseName,expenseReportOID,250.00,new JsonArray()).get("invoiceOID");
    }

    /**
     * 新建费用  不参与分摊   开始结束日期控件不为空
     * @param employee
     * @param expenseName
     * @param expenseReportOID
     * @return
     * @throws HttpStatusException
     */
    public String setInvoice(Employee employee,String expenseName,String expenseReportOID,String [] participant) throws HttpStatusException {
        ExpenseReportComponent expenseReportComponent =new ExpenseReportComponent();
        String cityCode =expenseReportComponent.getCityCode(employee,"上海");
        InvoiceComponent invoiceComponent =new InvoiceComponent();
        invoiceComponent.setCity(cityCode);
        JsonObject startAndEndDate = new JsonObject();
        startAndEndDate.addProperty("startDate",UTCTime.getFormStartDate(0));
        startAndEndDate.addProperty("endDate",UTCTime.getFormDateEnd(3));
        startAndEndDate.addProperty("duration",3);
        invoiceComponent.setStartAndEndData(startAndEndDate.toString());
        invoiceComponent.setParticipants(participant);
        return expenseReportInvoice.createExpenseInvoice(employee,invoiceComponent,expenseName,expenseReportOID,250.00,new JsonArray()).get("invoiceOID");
    }

    /**
     * 新建费用  不参与分摊  开始结束日期控件为空的
     * @param employee
     * @param expenseName
     * @param expenseReportOID
     * @return
     * @throws HttpStatusException
     */
    public String setInvoice(Employee employee,String expenseName,String expenseReportOID,String createDate) throws HttpStatusException {
        ExpenseReportComponent expenseReportComponent =new ExpenseReportComponent();
        String cityCode =expenseReportComponent.getCityCode(employee,"西安");
        InvoiceComponent invoiceComponent =new InvoiceComponent();
        invoiceComponent.setCity(cityCode);
        invoiceComponent.setCreatedDate(createDate);
        return expenseReportInvoice.createExpenseInvoice(employee,invoiceComponent,expenseName,expenseReportOID,200.00,new JsonArray()).get("invoiceOID");
    }

    /**
     * 差旅报销单新建-  控件 部门  参与人
     * @param employee
     * @param formName
     * @param applicationOID
     * @return
     * @throws HttpStatusException
     */
    public String setTravelReport(Employee employee,String formName,String applicationOID) throws HttpStatusException {
        //表单初始化
        FormComponent component = new FormComponent();
        component.setDepartment(employee.getDepartmentOID());
        component.setCause("差旅报销单");
        component.setApplicationOID(applicationOID);
        ArrayList<String> applicationOIDs =new ArrayList<>();
        applicationOIDs.add(applicationOID);
        //  参与人
        component.setParticipant(expenseReport.getValueFromApplication(employee,applicationOIDs,"参与人员"));
        return expenseReport.createTravelExpenseReport(employee,false,formName,component).get("expenseReportOID");
    }
}
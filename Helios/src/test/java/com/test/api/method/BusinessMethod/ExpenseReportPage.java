package com.test.api.method.BusinessMethod;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.component.FormComponent;
import com.hand.basicObject.component.FormDetail;
import com.hand.basicObject.component.InvoiceComponent;
import com.hand.basicObject.component.LoanBillComponent;
import com.hand.utils.UTCTime;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.ReimbStandard;
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
     * 创建一个报销单 控件有部门,开始结束日期,参与人,事由
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public HashMap<String,String> setDailyReport(Employee employee,String formName, String []participant) throws HttpStatusException {
        //新建报销单
        FormComponent component=new FormComponent();
        component.setCompany(employee.getCompanyOID());
        component.setDepartment(employee.getDepartmentOID());
        if(UTCTime.isMonthTail(UTCTime.getBeijingDay(0))){
            component.setStartDate(UTCTime.getFormStartDate(-6));
            component.setEndDate(UTCTime.getFormStartDate(0));
        }else{
            component.setStartDate(UTCTime.getFormStartDate(0));
            component.setEndDate(UTCTime.getFormStartDate(6));
        }
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
    public String setInvoice(Employee employee,String expenseName,String expenseReportOID,boolean isCompanyPay) throws HttpStatusException {
        ExpenseReportComponent expenseReportComponent =new ExpenseReportComponent();
        String cityCode =expenseReportComponent.getCityCode(employee,"上海");
        InvoiceComponent invoiceComponent =new InvoiceComponent();
        invoiceComponent.setCity(cityCode);
        invoiceComponent.setCompanyPay(isCompanyPay);
        JsonObject startAndEndDate = new JsonObject();
        startAndEndDate.addProperty("startDate",UTCTime.getFormStartDate(0));
        startAndEndDate.addProperty("endDate",UTCTime.getFormDateEnd(3));
        startAndEndDate.addProperty("duration",3);
        invoiceComponent.setStartAndEndData(startAndEndDate.toString());
        return expenseReportInvoice.createExpenseInvoice(employee,invoiceComponent,expenseName,expenseReportOID,250.00,new JsonArray()).get("invoiceOID");
    }

    /**
     * 新建机票费用包含舱等信息
     * @param employee
     * @param expenseName
     * @param expenseReportOID
     * @param cabin
     * @return
     * @throws HttpStatusException
     */
    public String setAirTrainShipInvoice(Employee employee, String expenseName, String expenseReportOID, String participants[], String cabin) throws HttpStatusException {
        ExpenseReportComponent expenseReportComponent =new ExpenseReportComponent();
        String setOffcityCode = expenseReportComponent.getCityCode(employee,"上海");
        String arriveCityCode = expenseReportComponent.getCityCode(employee,"西安");
        InvoiceComponent invoiceComponent =new InvoiceComponent();
        ReimbStandard reimbStandard = new ReimbStandard();
        JsonArray cabinArray =new JsonArray();
        if(expenseName.equals("train-autotest")){
            cabinArray = reimbStandard.getCustomEnumerationItems(employee,"座等",cabin);
        }
        if(expenseName.equals("airplan-autotest")){
            cabinArray = reimbStandard.getCustomEnumerationItems(employee,"舱等",cabin);
        }
        if(expenseName.equals("ship-autotest")){
            cabinArray = reimbStandard.getCustomEnumerationItems(employee,"座次",cabin);
        }
        if(cabinArray.size()!=0){
            invoiceComponent.setCabin(cabinArray.get(0).getAsJsonObject().get("value").getAsString());
        }
        invoiceComponent.setArriveCity(arriveCityCode);
        invoiceComponent.setSetoffCity(setOffcityCode);
        invoiceComponent.setCompanyPay(false);
        JsonObject startAndEndDate = new JsonObject();
        startAndEndDate.addProperty("startDate",UTCTime.getFormStartDate(0));
        startAndEndDate.addProperty("endDate",UTCTime.getFormDateEnd(3));
        startAndEndDate.addProperty("duration",3);
        invoiceComponent.setStartAndEndData(startAndEndDate.toString());
        invoiceComponent.setParticipants(participants);
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
    public String setInvoice(Employee employee,String expenseName,String expenseReportOID,String [] participant,double amount) throws HttpStatusException {
        ExpenseReportComponent expenseReportComponent =new ExpenseReportComponent();
        String cityCode =expenseReportComponent.getCityCode(employee,"上海");
        InvoiceComponent invoiceComponent =new InvoiceComponent();
        invoiceComponent.setCity(cityCode);
        JsonObject startAndEndDate = new JsonObject();
        if(UTCTime.isMonthTail(UTCTime.getBeijingDay(0))){
            startAndEndDate.addProperty("startDate",UTCTime.getFormStartDate(-6));
            startAndEndDate.addProperty("endDate",UTCTime.getFormDateEnd(-3));
        }else{
            startAndEndDate.addProperty("startDate",UTCTime.getFormStartDate(0));
            startAndEndDate.addProperty("endDate",UTCTime.getFormDateEnd(3));
        }
        startAndEndDate.addProperty("duration",3);
        invoiceComponent.setStartAndEndData(startAndEndDate.toString());
        invoiceComponent.setParticipants(participant);
        return expenseReportInvoice.createExpenseInvoice(employee,invoiceComponent,expenseName,expenseReportOID,amount,new JsonArray()).get("invoiceOID");
    }

    /**
     * 新建费用  不参与分摊  开始结束日期控件为空的 费用的createDate可以选择
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
     * 新建费用  不参与分摊  替票开关开启
     * @param employee
     * @param expenseName
     * @param expenseReportOID
     * @return
     * @throws HttpStatusException
     */
    public String setInvoice(Employee employee,String expenseName,String expenseReportOID) throws HttpStatusException {
        ExpenseReportComponent expenseReportComponent =new ExpenseReportComponent();
        String cityCode =expenseReportComponent.getCityCode(employee,"西安");
        InvoiceComponent invoiceComponent =new InvoiceComponent();
        invoiceComponent.setCity(cityCode);
        invoiceComponent.setInvoiceInstead(true);
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
    public FormDetail setTravelReport(Employee employee,String formName,String applicationOID) throws HttpStatusException {
        //表单初始化
        FormComponent component = new FormComponent();
        component.setDepartment(employee.getDepartmentOID());
        component.setCause("差旅报销单");
        component.setApplicationOID(applicationOID);
        ArrayList<String> applicationOIDs =new ArrayList<>();
        applicationOIDs.add(applicationOID);
        //  参与人
        component.setParticipant(expenseReport.getValueFromApplication(employee,applicationOIDs,"参与人员"));
        return expenseReport.createTravelExpenseReport(employee,false,formName,component);
    }

    /**
     * 新建借款单 表单为默认的表单-个人借款单呢
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public FormDetail setDefaultLoanBill(Employee employee, String formName, String rebackDate) throws HttpStatusException {
        ExpenseReport expenseReport = new ExpenseReport();
        FormComponent component =new FormComponent();
        component.setCause("借款");
        component.setDepartment(employee.getDepartmentOID());
        component.setCurrencyCode("CNY");
        component.setRebackDate(rebackDate);
        return expenseReport.createLoanReport(employee,formName,component);
    }

    /**
     * 个人借款单 - 新建借款行
     * @param employee
     * @param formDetail
     * @param formName
     * @throws HttpStatusException
     */
    public void setLoanLine(Employee employee,FormDetail formDetail,String formName,boolean isOneself) throws HttpStatusException {
        LoanBillComponent loanBillComponent = new LoanBillComponent();
        loanBillComponent.setAmount(0.1);
        loanBillComponent.setLoanBillId(formDetail.getId());
        loanBillComponent.setPlanedRepaymentDate(UTCTime.getBeijingDate(1));
        expenseReport.createLoanLine(employee,formName,loanBillComponent,isOneself);
    }

    /**
     * 创建费用 陪同人数 招待人数 开始结束日期控件
     * @param employee
     * @param expenseName
     * @param expenseReportOID
     * @param accompanying 陪同人数
     * @param hospitalized 招待人数
     * @param participants
     * @param amount
     * @return
     * @throws HttpStatusException
     */
    public String setInvoice(Employee employee,String expenseName,String expenseReportOID,int accompanying,int hospitalized,Object participants,int amount) throws HttpStatusException {
        ExpenseReportComponent expenseReportComponent =new ExpenseReportComponent();
        InvoiceComponent invoiceComponent =new InvoiceComponent();
        invoiceComponent.setParticipants(participants);
        invoiceComponent.setAccompanying(accompanying);
        invoiceComponent.setHospitalized(hospitalized);
        JsonObject startAndEndDate = new JsonObject();
        startAndEndDate.addProperty("startDate",UTCTime.getFormStartDate(0));
        startAndEndDate.addProperty("endDate",UTCTime.getFormDateEnd(2));
        startAndEndDate.addProperty("duration",2);
        invoiceComponent.setStartAndEndData(startAndEndDate.toString());
        return expenseReportInvoice.createExpenseInvoice(employee,invoiceComponent,expenseName,expenseReportOID,amount,new JsonArray()).get("invoiceOID");
    }

}
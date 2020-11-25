package com.test.api.method.BusinessMethod;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.basicObject.InvoiceComponent;
import com.hand.utils.GsonUtil;
import com.hand.utils.UTCTime;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.ExpenseReportInvoice;
import lombok.extern.slf4j.Slf4j;

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
        component.setStartDate(UTCTime.getFormStartDate(-3));
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
     * 新建费用费用控件  不参与分摊   开始结束日期不为空
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
        return expenseReportInvoice.createExpenseInvoice(employee,invoiceComponent,expenseName,expenseReportOID,200.00,new JsonArray()).get("invoiceOID");
    }

    /**
     * 新建费用报销单  不参与分摊  时间控件为空的
     * @param employee
     * @param expenseName
     * @param expenseReportOID
     * @return
     * @throws HttpStatusException
     */
    public String setInvoice(Employee employee,String expenseName,String expenseReportOID,String createDate) throws HttpStatusException {
        ExpenseReportComponent expenseReportComponent =new ExpenseReportComponent();
        String cityCode =expenseReportComponent.getCityCode(employee,"西安市");
        InvoiceComponent invoiceComponent =new InvoiceComponent();
        invoiceComponent.setCity(cityCode);
        invoiceComponent.setCreatedDate(createDate);
        JsonObject startAndEndDate = new JsonObject();
        invoiceComponent.setStartAndEndData(startAndEndDate.toString());
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

    /**
     * 报销单费控标签检查
     * @param employee
     * @param expenseReportOID
     * @param expectValue
     * @throws HttpStatusException
     */
    public boolean checkSubmitLabel(Employee employee,String expenseReportOID,String externalPropertyName,String expectValue) throws HttpStatusException {
        JsonObject result = expenseReport.expenseReportSubmitCheck(employee,expenseReportOID);
        log.info("校验的结果:{}",result);
        JsonArray checkResultList = result.get("checkResultList").getAsJsonArray();
        if(GsonUtil.isNotEmpt(checkResultList)){
            String message = GsonUtil.getJsonValue(checkResultList,"externalPropertyName",externalPropertyName).get("message").getAsString();
            if(message.contains(expectValue)){
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }

    /**
     * 费用内的标签信息
     * @param employee
     * @param invoiceOID
     * @param expectValue
     * @return
     */
    public boolean checkInvoiceLabel(Employee employee,String invoiceOID,String type,String expectValue) throws HttpStatusException {
        JsonObject result = expenseReportInvoice.getInvoice(employee,invoiceOID);
        JsonArray invoiceLabel = result.get("invoiceLabels").getAsJsonArray();
        if(GsonUtil.isNotEmpt(invoiceLabel)){
            String toast = GsonUtil.getJsonValue(invoiceLabel,"type",type).get("toast").getAsString();
            if(toast.equals(expectValue)){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
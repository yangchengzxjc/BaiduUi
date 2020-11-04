package com.test.api.testcase.ReimbStand;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.basicObject.FormComponent;
import com.hand.basicObject.InvoiceComponent;
import com.hand.utils.UTCTime;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.Infra.SetOfBooksMethod.SetOfBooksDefine;
import com.test.api.method.ReimbStandard;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import lombok.extern.slf4j.Slf4j;

import org.testng.annotations.*;

@Slf4j
public class ReimbStandardTest extends BaseTest {
    private Employee employee;
    private ReimbStandard reimbStandard;
    private SetOfBooksDefine setOfBooksDefine;
    private ExpenseReport expenseReport;
    private ExpenseReportComponent expenseReportComponent;
    private ExpenseReportInvoice expenseReportInvoice;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        reimbStandard =new ReimbStandard();
        expenseReport =new ExpenseReport();
        expenseReportComponent =new ExpenseReportComponent();
        expenseReportInvoice =new ExpenseReportInvoice();
    }

    @Test(description = "用于测试调试使用")
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void login(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        log.info("token1:{}",employee.getAccessToken());
    }

    @Test(description = "创建报销标准规则")
    public void Test01()throws HttpStatusException{
        setOfBooksDefine = new SetOfBooksDefine();
        //获取账套id
        String setOfBooksId= setOfBooksDefine.getSetOfBooksId(employee,"DEFAULT_SOB","默认账套","reimbursement-standard");
        JsonArray userGroups = reimbStandard.userGroups(reimbStandard.getUserGroups(employee,"租户级  stage测试员",setOfBooksId));
        JsonArray expenseTypes = reimbStandard.expenseTypes(reimbStandard.getExpenseType(employee,"自动化测试-报销标准", setOfBooksId));
        //新建规则
        String rulesOid=reimbStandard.addReimbstandard(employee,"测试25","WARN","SET_OF_BOOK",
                setOfBooksId,"SINGLE","SINGLE","该费用超标啦",userGroups,expenseTypes,
                new  JsonArray(),new JsonArray());
        log.info("ruleOID:{}",rulesOid);
        //获取默认管控信息
        JsonArray controlItems = reimbStandard.getControlItems(employee,rulesOid);
        log.info("controlItems:{}",controlItems);
        //获取默认基本标准
        JsonArray item = reimbStandard.getItem(employee,rulesOid);
        log.info("item:{}",item);
        //修改基本标准
        String standardOid=item.get(0).getAsJsonObject().get("standardOID").getAsString();
        String items= reimbStandard.addItems(employee,standardOid,rulesOid, 100,userGroups,new JsonArray());
        log.info("items:{}",items);
    }

    @Test(description = "报销标准规则校验")
    public void Test02()throws HttpStatusException{
        //新建报销单
        FormComponent component=new FormComponent();
        InvoiceComponent invoiceComponent = new InvoiceComponent();
        component.setCompany(employee.getCompanyOID());
        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(UTCTime.getNowUtcTime());
        component.setEndDate(UTCTime.getUtcTime(3,0));
        //添加参与人员  参与人员的value 是一段json数组。
        JsonArray array = new JsonArray();
        array.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"自动化测试-日常报销单"),"懿佳欢_stage"));
        array.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"自动化测试-日常报销单"),"17900001005"));
        log.info(array.toString());
        component.setParticipant(array.toString());
        component.setCause("报销标准规则校验");
        log.info(String.valueOf(component));
        String expenseReportOID =expenseReport.createExpenseReport(employee,"自动化测试-日常报销单",component).get("expenseReportOID");
        log.info(expenseReportOID);
        //添加费用参与人员
        JsonArray data =new JsonArray();
        data.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"自动化测试-日常报销单"),"懿佳欢_stage"));
        invoiceComponent.setParticipants(data.toString());
        String cityCode =expenseReportComponent.getCityCode(employee,"西安市");
        invoiceComponent.setCity(cityCode);
        JsonObject startAndEndDate = new JsonObject();
        startAndEndDate.addProperty("startDate",UTCTime.getNowStartUtcDate());
        startAndEndDate.addProperty("endDate",UTCTime.getUTCDateEnd(2));
        startAndEndDate.addProperty("duration",2);
        invoiceComponent.setStartAndEndData(startAndEndDate.toString());
        String invoiceOID = expenseReportInvoice.createExpenseInvoice(employee,invoiceComponent,"自动化测试-报销标准",expenseReportOID,200.00,new JsonArray()).get("invoiceOID");
        log.info(invoiceOID);
        String time =UTCTime.getBeijingDay(0);
        String message =String.format("该费用超标啦 %s 自动化测试-报销标准 标准为：CNY 100.00，已使用：CNY 200.00，超标：CNY 100.00。",time);
        log.info("需要断言的:{}",message);
        assert expenseReport.expenseReportSubmitCheck(employee,expenseReportOID).toString().contains(message);
        expenseReport.expenseReportSubmit(employee,expenseReportOID);
        expenseReport.withdraw(employee,expenseReportOID);
        expenseReport.removeInvoice(employee,expenseReportOID,invoiceOID);
        expenseReportInvoice.deleteInvoice(employee,invoiceOID);
        expenseReport.deleteExpenseReport(employee,expenseReportOID);
    }

    @Test(description = "删除单条报销标准规则")
    public void Test03() throws HttpStatusException{
        JsonArray rules= reimbStandard.getRules(employee,"测试25");
        String rulesOid=rules.get(0).getAsJsonObject().get("ruleOID").getAsString();
        log.info(rulesOid);
        //删除规则
       reimbStandard.deleteReimbStandardRules(employee,rulesOid);
    }

    @Test(description = "周期管控规则编辑")
    public void Test04()throws HttpStatusException{
        setOfBooksDefine = new SetOfBooksDefine();
        //获取账套id
        String setOfBooksId= setOfBooksDefine.getSetOfBooksId(employee,"DEFAULT_SOB","默认账套","reimbursement-standard");
        JsonArray userGroups = reimbStandard.userGroups(reimbStandard.getUserGroups(employee,"租户级  stage测试员",setOfBooksId));
        JsonArray expenseType = reimbStandard.expenseTypes(reimbStandard.getExpenseType(employee,"自动化测试-报销标准", setOfBooksId));
        JsonArray formType = reimbStandard.formTypes(reimbStandard.getFormType(employee,"自动化测试-日常报销单",setOfBooksId));
        JsonArray company = reimbStandard.companyGroups(reimbStandard.getCompany(employee,"甄滙_STAGE_TestCompany",setOfBooksId));
        //新建规则
        String rulesOid = reimbStandard.addReimbstandard(employee,"周期管控-编辑01","WARN","SET_OF_BOOK",
                reimbStandard.getSetOfBookId(employee,"默认账套"),"DAY","PERIOD",
                "周期管控规则编辑",userGroups,expenseType,formType,company);
        log.info("ruleOID:{}",rulesOid);
    }

    @Test(description = "删除周期管控规则")
    public void Test05()throws HttpStatusException{
        JsonArray periodRules=reimbStandard.getRules(employee,"周期管控-编辑01");
        log.info("periodRules:{}",periodRules);
        String periodRulesOid=periodRules.get(0).getAsJsonObject().get("ruleOID").getAsString();
        log.info(periodRulesOid);
        //删除规则
        reimbStandard.deleteReimbStandardRules(employee,periodRulesOid);
    }
}

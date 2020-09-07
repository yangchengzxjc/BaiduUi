package com.test.api.testcase.ReimbStand;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.ReimbStandardApi;
import com.hand.basicObject.FormComponent;
import com.hand.utils.UTCTime;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.ReimbStandard;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import com.test.api.method.SetOfBooksDefine;
import lombok.extern.slf4j.Slf4j;

import org.testng.annotations.*;

@Slf4j
public class ReimbStandardTest extends BaseTest {
    private Employee employee;
    private ReimbStandardApi reimbStandardApi;
    private ReimbStandard reimbStandard;
    private SetOfBooksDefine setOfBooksDefine;
    private ExpenseReport expenseReport;
    private ExpenseReportComponent expenseReportComponent;


    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        reimbStandardApi =new ReimbStandardApi();
        reimbStandard =new ReimbStandard();
        expenseReport =new ExpenseReport();
        expenseReportComponent =new ExpenseReportComponent();
    }

    @Test(priority = 1,description = "用于测试调试使用")
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void login(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        log.info("token1:{}",employee.getAccessToken());
    }

    @Test(priority = 1,description = "查询某个账套详情")
    public void test1() throws HttpStatusException {
        setOfBooksDefine =new SetOfBooksDefine();
        JsonObject setBooks = setOfBooksDefine.getSetOfBooksDetail(employee,"DEFAULT_SOB","默认账套");
        System.out.println(setBooks);
    }
    @Test(priority = 1,description = "查询某个账套的id")
    public void test2()throws HttpStatusException{
        setOfBooksDefine = new SetOfBooksDefine();
       String A= setOfBooksDefine.getSetOfBooksId(employee,"DEFAULT_SOB","默认账套");
        log.info("账套id："+A);

    }
    @Test(priority = 1,description = "查询公司")
    public void test3()throws HttpStatusException{
        JsonArray companyList = new JsonArray();
        companyList.add(reimbStandardApi.getEnabledCompany(employee, reimbStandard.getSetOfBookId(employee,"默认账套")));
        log.info("companyList:{}",companyList);
    }

    @Test(priority = 2,description = "报销标准")
    public void createRules()throws HttpStatusException{
        JsonArray userGroups = reimbStandard.userGroups(reimbStandard.getUserGroups(employee,"租户级  stage测试员"));
        JsonArray expenseType = reimbStandard.expenseType(reimbStandard.getExpenseType(employee,"摊销费用"));
        //新建规则
        String rulesOid=reimbStandard.addReimbstandard(employee,"测试25","WARN","SET_OF_BOOK",
                reimbStandard.getSetOfBookId(employee,"默认账套"),"SINGLE","SINGLE",
                "该费用超标啦",userGroups,expenseType,new  JsonArray(),new JsonArray());
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
        //删除规则
//        reimbStandard.deleteReimbStandardRules(employee,rulesOid);
    }

    @Test(priority = 1,description = "报销标准规则校验")
    public void checkRules()throws HttpStatusException{
        //新建报销单
        FormComponent component=new FormComponent();
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
    }

}

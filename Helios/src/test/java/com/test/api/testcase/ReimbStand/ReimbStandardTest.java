package com.test.api.testcase.ReimbStand;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.ReimbStandardApi;
import com.test.api.method.RuleDetails;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import lombok.extern.slf4j.Slf4j;

import org.testng.annotations.*;

@Slf4j
public class ReimbStandardTest extends BaseTest {
    /*
    * 创建报销标准规则
    * @param employee
    * @param UserGroups 人员组
    *
    */

    private Employee employee;
    private ReimbStandardApi reimbStandardApi;
    private RuleDetails ruleDetails;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        reimbStandardApi =new ReimbStandardApi();
        ruleDetails =new RuleDetails();
    }

    @Test(priority = 1,description = "用于测试调试使用")
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void login(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        log.info("token1:{}",employee.getAccessToken());
//        log.info("companyOID:{}",employee.getCompanyOID());
    }

    @Test(priority = 1,description = "查询所有账套")
    public void test1() throws HttpStatusException {
//        JsonArray array = new JsonArray();
//        JsonObject object= new JsonObject();
//        object.add("userGroups",reimbStandardApi.GetSetOfBooks(employee));
//        array.add(object);
//        log.info("获取到所有的账套："+array);
//        ruleDetails.getSetOfBookId(employee,"默认账套");
        JsonArray setBooks = reimbStandardApi.getSetOfBooks(employee);
        System.out.println(setBooks);
    }
    @Test(priority = 1,description = "查询某个账套的id")
    public void test2()throws HttpStatusException{
        String A = ruleDetails.getSetOfBookId(employee,"默认账套");
        log.info("账套id："+A);

    }
    @Test(priority = 1,description = "查询公司")
    public void test3()throws HttpStatusException{
        reimbStandardApi.getEnabledCompany(employee,ruleDetails.getSetOfBookId(employee,"默认账套"));
        System.out.println(reimbStandardApi.getEnabledCompany(employee,ruleDetails.getSetOfBookId(employee,"默认账套")));
    }

    @Test(priority = 1,description = "新建单条管控规则")
    public void createRules()throws HttpStatusException{
        JsonArray array1 = new JsonArray();
        array1.add(ruleDetails.getUserGroups(employee,"租户级 stage测试员"));
        log.info("array1:{}",array1);
        JsonArray array2 = new JsonArray();
        array2.add(ruleDetails.getExpenseType(employee,"摊销费用"));

        reimbStandardApi.creatReimbStandardRules(employee,"测试","WARN","SET_OF_BOOK",
                ruleDetails.getSetOfBookId(employee,"默认账套"),"SINGLE","SINGLE",
                "该费用超标了", new JsonArray() , array2,new JsonArray(),new JsonArray());
    }
}

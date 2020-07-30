package com.test.api.testcase.ReimbStand;

import com.google.gson.JsonArray;
import com.hand.api.ReimbStandardApi;
import com.test.api.method.ReimbStandard;
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
    private ReimbStandard reimbStandard;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        reimbStandardApi =new ReimbStandardApi();
        reimbStandard =new ReimbStandard();
    }

    @Test(priority = 1,description = "用于测试调试使用")
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void login(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        log.info("token1:{}",employee.getAccessToken());
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
        String A = reimbStandard.getSetOfBookId(employee,"默认账套");
        log.info("账套id："+A);

    }
    @Test(priority = 1,description = "查询公司")
    public void test3()throws HttpStatusException{
        JsonArray companyList = new JsonArray();
        companyList.add(reimbStandardApi.getEnabledCompany(employee, reimbStandard.getSetOfBookId(employee,"默认账套")));
        log.info("companyList:{}",companyList);
    }

    @Test(priority = 1,description = "新建报销标准规则")
    public void createRules()throws HttpStatusException{
        JsonArray userGroups = reimbStandard.userGroups(reimbStandard.getUserGroups(employee,"租户级  stage测试员"));
        JsonArray expenseType = reimbStandard.expenseType(reimbStandard.getExpenseType(employee,"摊销费用"));
        reimbStandard.addReimbstandard(employee,"测试8","WARN","SET_OF_BOOK",
                reimbStandard.getSetOfBookId(employee,"默认账套"),"SINGLE","SINGLE",
                "该费用超标",userGroups,expenseType,new  JsonArray(),new JsonArray());
    }
//    @Test(priority = 1,description = "删除报销标准规则")
//    public void deleteRules()throws HttpStatusException{
//
//    }

}

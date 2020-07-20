package com.test.api.testcase;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.TravelSubsidyAPi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

/**
 * @Author peng.zhang
 * @Date 2020/6/19
 * @Version 1.0
 **/
@Slf4j
public class AuthorityCheck extends BaseTest{

    private TravelSubsidyAPi travelSubsidyAPi;
    private Employee employee;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
       travelSubsidyAPi =new TravelSubsidyAPi();
       employee =new Employee();
       employee=getEmployee(phoneNumber,pwd,env);
    }

    @Test(priority = 2,description = "用户登录")
    public void test1() throws HttpStatusException {
        JsonObject subsidyConfig = travelSubsidyAPi.getSubsidyBaseRuleConfig(employee,"419f1cac-7d5c-489c-af77-baa8c9cf0b4a");
        boolean [] travelFields ={false,true};
        boolean [] specialRules ={true,true,true,true};
        travelSubsidyAPi.editSubsidyBaseRuleConfig(employee,subsidyConfig,false,travelFields,specialRules,new JsonArray(),true,false);
    }
}

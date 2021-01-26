package com.test.api.testcase.expenseControl.SubmissionControl;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

@Slf4j
public class ExpenseSubmissionRules extends BaseTest {
    private Employee employee;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
    }

    public void Test01()throws HttpStatusException {

    }



}

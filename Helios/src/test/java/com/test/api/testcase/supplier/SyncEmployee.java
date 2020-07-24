package com.test.api.testcase.supplier;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import com.test.api.method.Infra.EmployeeManagePage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SyncEmployee extends BaseTest {
    private Employee employee;
    private EmployeeManagePage employeeManagePage;


    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978000") String phoneNumber, @Optional("hly123456") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        employeeManagePage =new EmployeeManagePage();
//        System.out.println(employee);
    }

    @Test(description = "新增员正常流程")
    public void addEmployee() throws HttpStatusException {
        employeeManagePage.addEmployee(employee,"甄滙消费商测试公司1","测试部门A","0002","测试接口新建","职务01","级别A");
    }




}

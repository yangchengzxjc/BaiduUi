package com.test.api.testcase.infraStructure;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.test.BaseTest;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.Infra.EmployeeManagePage;
import com.test.api.method.InfraStructure;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * @Author peng.zhang
 * @Date 2020/7/3
 * @Version 1.0
 **/
@Slf4j
public class EmployeeManage extends BaseTest {

    private Employee employee;
    private EmployeeManagePage employeeManagePage;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        employeeManagePage =new EmployeeManagePage();
    }

    @Test(description = "新增员工")
    public void addEmployee() throws HttpStatusException {
        employeeManagePage.addEmployee(employee,"甄滙_上海通用公司","上海通用总裁办","112233","测试工程师");
    }
}

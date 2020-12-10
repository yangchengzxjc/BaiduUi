package com.test;

import com.hand.basicConstant.Environment;
import com.test.api.method.EmployeeAccount;
import com.test.api.method.EmployeeLogin;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import com.hand.basicObject.Employee;

import java.util.Properties;

@Slf4j
public class BaseTest {
    public static Properties properties;
    public static int multilingual;
    private Employee employee;
    private EmployeeLogin employeeLogin;
    private EmployeeAccount employeeAccount;

    public Employee getEmployee(String phoneNumber, String pwd, String env) {
        employeeLogin = new EmployeeLogin();
        employeeAccount = new EmployeeAccount();
        employee = new Employee(phoneNumber, pwd, Environment.getEnv(env));
        employee.setAccessToken(employeeLogin.getToken(employee));
        employee = employeeAccount.setEmployeeInfo(employee);
        return employee;
    }

//    @BeforeTest(alwaysRun = true)
////    @Parameters({"phoneNumber", "passWord", "environment"})
//    public void beforeMethod(St){
//        log.info("测试开始,登录的用户={},环境信息={}",phoneNumber,env);
////        employeeLogin =new EmployeeLogin();
////        employeeAccount =new EmployeeAccount();
////        employee=new Employee(phoneNumber,pwd,Environment.getEnv(env));
////        employee.setAccessToken(employeeLogin.getToken(employee));
////        employee=employeeAccount.setEmployeeInfo(employee);
////        log.info("用户的手机号：{}",employee.getPhoneNumber());
//    }


    @AfterTest(alwaysRun = true)
    public void end() {
        log.info("测试结束");
    }

    /**
     * @param employee 员工employee对象
     * @param env      环境 stage/service ..
     * @param username 用户名
     * @param pass     密码
     * @param language 语言
     */
    public void setEmployee(Employee employee, String env, String username, String pass, String language) {
        employee.setPhoneNumber(username);
        employee.setPassWord(pass);
        employee.setLanguage(language);
        employee.setEnv(env);
        employee.setEnvironment(Environment.getEnv(env));
    }

}

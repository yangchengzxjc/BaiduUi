//package com.test.api.testcase;
//
//import com.hand.basicObject.Employee;
//import com.hand.basicconstant.Environment;
//import com.test.api.method.EmployeeAccount;
//import com.test.api.method.EmployeeLogin;
//import lombok.Data;
//import org.apache.logging.log4j.core.tools.picocli.CommandLine;
//import org.testng.annotations.Parameters;
//
///**
// * @Author peng.zhang
// * @Date 2020/6/19
// * @Version 1.0
// **/
//public class BeforeMethod{
//
//    private static Employee employee;
////    private BeforeMethod  beforeMethod =new BeforeMethod();
//    private BeforeMethod(){
//    }
//
//    public static  Employee getInstance(String phoneNumber, String pwd, String env){
//        EmployeeLogin employeeLogin =new EmployeeLogin();
//        EmployeeAccount employeeAccount =new EmployeeAccount();
//        if(employee==null){
//            employee =new Employee();
//            employee.setEnvironment(Environment.getEnv(env));
//            employee.setPassWord(pwd);
//            employee.setUserName(phoneNumber);
//            employee.setAccessToken(employeeLogin.getToken(employee));
//            employee=employeeAccount.setEmployeeInfo(employee);
//        }
//        return employee;
//    }
//
//}

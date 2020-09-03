package com.hand.basicObject;

import com.hand.basicconstant.Environment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Employee {
    //登录名
    private  String phoneNumber;
    //登录密码
    private  String passWord;
    //员工名
    private String fullName;
    //员工工号
    private String employeeID;
    //userOID
    private String userOID;
    //accessToken
    private String accessToken;
    //公司corporationOID
    private String corporationOID;
    //公司OID
    private String companyOID;
    //当前环境变量(eg:sit,stage,service...)
    private String env;
    //多语言
    private String language;
    //枚举环境变量
    private Environment environment;
    //部门OID
    private String departmentOID;
    //财务审核ResourceId
    private String financeResourceId=null;
    //审批ResourceId
    private String approveResourceId=null;
    //tenantId
    private String tenantId;
    //部门名
    private String departmentName;
    //公司名
    private String companyName;
    //用户Id
    private String userId;
    //岗位id
    private String jobId;
    //账套id
    private String setOfBookId;


    public Employee(String username, String password, Environment environment) {
        this.phoneNumber = username;
        passWord = password;
        this.environment =environment;
    }
}

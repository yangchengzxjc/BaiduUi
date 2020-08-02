package com.test.api.testcase.supplier;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.EmployeeDTO;
import com.hand.basicconstant.CardType;
import com.test.BaseTest;
import com.test.api.method.EmployeeLogin;
import com.test.api.method.Infra.EmployeeManagePage;
import com.test.api.method.InfraStructure;
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
        EmployeeDTO employeeDTO = new EmployeeDTO();
        JsonObject empObject = employeeManagePage.addEmployee(employee,"甄滙消费商测试公司1","测试部门A","0002","测试接口新建","职务01","级别A");
        String userOID=empObject.get("userOID").getAsString();
        JsonObject userCardInfo=employeeManagePage.addUserCard(employee,userOID,CardType.CHINA_ID,"身份证名字",true);

//        if (empObject.get("status").toString() == "1001") {
//            employeeDTO.setStatus("1");
//            }
//        else{
//            employeeDTO.setStatus("0");
//        }
//        employeeDTO.setFullName(empObject.get("fullName").toString());
//        employeeDTO.setEmployeeID(empObject.get("employeeID").toString());
//        employeeDTO.setMobile(empObject.get("mobile").toString());
//        employeeDTO.setEmail(empObject.get("email").toString());
//        employeeDTO.setName(userCardInfo.get("lastName").toString()); // 身份证名字

    }

    @Test(description = "离职员工正常流程")
    public void deleteEmployee() {

    }

}

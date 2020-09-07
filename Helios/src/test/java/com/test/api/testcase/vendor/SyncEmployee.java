package com.test.api.testcase.vendor;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.infrastructure.employee.InfraEmployee;
import com.hand.basicObject.supplierObject.EmployeeDTO;
import com.hand.basicconstant.CardType;
import com.test.BaseTest;
import com.test.api.method.Infra.EmployeeMethod.EmployeeManagePage;
import com.test.api.method.Infra.EmployeeMethod.InfraStructure;
import org.testng.Assert;
import org.testng.annotations.*;

public class SyncEmployee extends BaseTest {
    private Employee employee;
    private EmployeeManagePage employeeManagePage;
    private InfraStructure infraStructure;
    private InfraEmployee infraEmployee;


    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978000") String phoneNumber, @Optional("hly123456") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        employeeManagePage =new EmployeeManagePage();
        infraStructure =new InfraStructure();
        infraEmployee = new InfraEmployee();
    }

    @Test(description = "新增员正常流程,")
    public void addEmployeeTest01() throws HttpStatusException {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        JsonObject empObject = employeeManagePage.addEmployee(employee,infraEmployee, "","","","","","","甄滙消费商测试公司1","测试部门A","0002","测试接口新建","职务01","级别A");
        String userOID=empObject.get("userOID").getAsString();
        JsonObject userCardInfo=employeeManagePage.addUserCard(employee,userOID,CardType.CHINA_ID,"身份证名字",true);
        if (empObject.get("status").toString().equals("1001")) {
            employeeDTO.setStatus("1");
            }
        else{
            employeeDTO.setStatus("0");
        }
        employeeDTO.setFullName(empObject.get("fullName").toString());
        employeeDTO.setEmployeeID(empObject.get("employeeID").toString());
        employeeDTO.setMobile(empObject.get("mobile").toString());
        employeeDTO.setEmail(empObject.get("email").toString());
        if (userCardInfo.get("lastName").toString() != null) {
            employeeDTO.setName(userCardInfo.get("lastName").toString());
        }
        else {
            employeeDTO.setName(empObject.get("fullName").toString());//优先身份证名字 没有就取系统名字
        }
        if (userCardInfo.get("cardType").toString().equals("102")){
            employeeDTO.setEnFirstName(userCardInfo.get("firstName").toString());
            employeeDTO.setEnLastName(userCardInfo.get("lastName").toString());
        }
        else {
            employeeDTO.setEnFirstName(null);
            employeeDTO.setEnLastName(null);
        }
        employeeDTO.setNationality(null);
        employeeDTO.setGender(empObject.get("genderCode").toString());
        employeeDTO.setRankName(empObject.get("rank").toString());
    }

    @Test(description = "离职员工正常流程")
    public void deleteEmployeeTest02() throws HttpStatusException {
        //判断员工是否在职
        if(infraStructure.getUserDetail(employee,"zhang58062@hui.com").get("status").getAsInt() != 1001){
            //员工入职
            infraStructure.rehire(employee,"zhang58062@hui.com");
        }
        //员工离职
        int statusCode = infraStructure.leaveEmployee(employee,"zhang58062@hui.com");
        Assert.assertEquals(200,statusCode);
        //获取离职员工的status   1003 是离职状态
        int status = infraStructure.getUserDetail(employee,"zhang58062@hui.com").get("status").getAsInt();
        //对离职员工做一个断言
        Assert.assertEquals(1003,status);
    }

    @Test(description = "员工重新入职")
    public void rehireTest03() throws HttpStatusException {
        //先判断员工是否是离职的状态
        if(infraStructure.getUserDetail(employee,"zhang58062@hui.com").get("status").getAsInt() != 1003){
            infraStructure.leaveEmployee(employee,"zhang58062@hui.com");
        }
        //员工再次入职
        infraStructure.rehire(employee,"zhang58062@hui.com");
        int status = infraStructure.getUserDetail(employee,"zhang58062@hui.com").get("status").getAsInt();
        //对离职员工做一个断言
        Assert.assertEquals(1001,status);
    }

}

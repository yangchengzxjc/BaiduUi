package com.test.api.testcase.vendor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.infrastructure.employee.InfraEmployee;
import com.hand.basicObject.supplierObject.employeeInfoDto.EmployeeDTO;
import com.hand.basicObject.supplierObject.employeeInfoDto.UserCardInfoDTO;
import com.hand.basicconstant.CardType;
import com.hand.utils.GsonUtil;
import com.test.BaseTest;
import com.test.api.method.Infra.EmployeeMethod.EmployeeManagePage;
import com.test.api.method.InfraStructure;
import com.test.api.method.Vendor;
import org.openqa.selenium.json.Json;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

public class SyncEmployee extends BaseTest {
    private Employee employee;
    private EmployeeManagePage employeeManagePage;
    private InfraStructure infraStructure;
    private InfraEmployee infraEmployee;
    private Vendor vendor;


    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978000") String phoneNumber, @Optional("hly123456") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        employeeManagePage =new EmployeeManagePage();
        infraStructure =new InfraStructure();
        infraEmployee = new InfraEmployee();
        vendor =new Vendor();
    }

    @Test(description = "新增员正常流程,")
    public void addEmployeeTest01() throws HttpStatusException {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        JsonObject empObject = employeeManagePage.addEmployee(employee, "测试接口新建Q57","","M0057","M0057@163.COM","人员类型01","","甄滙消费商测试公司1","测试部门A","0002","测试接口新建","职务01","级别A");
        String userOID=empObject.get("userOID").getAsString();
        JsonObject bookClass = vendor.queryBookClass(employee);
        JsonObject departCode = infraStructure.searchDepartmentDetail(employee,empObject.get("departmentOID").getAsString());
        JsonObject userCardInfo=employeeManagePage.addUserCard(employee,userOID,CardType.CHINA_ID,"身份证名字",true);//新增身份证 启用 名字：身份证名字
        JsonArray userCardInfos = infraStructure.queryUserCard(employee);
        ArrayList cardList =vendor.addUserCardInfoDTO(userCardInfos);


        if (empObject.get("status").toString().equals("1001")) {
            employeeDTO.setStatus("1");
            }
        else{
            employeeDTO.setStatus("0");
        }
        employeeDTO.setFullName(empObject.get("fullName").getAsString());
        employeeDTO.setEmployeeID(empObject.get("employeeID").getAsString());
        employeeDTO.setMobile(empObject.get("mobile").getAsString());
        employeeDTO.setEmail(empObject.get("email").getAsString());
        if (userCardInfo.get("lastName").toString() != null) {
            employeeDTO.setName(userCardInfo.get("lastName").getAsString());
        }
        else {
            employeeDTO.setName(empObject.get("fullName").getAsString());//优先身份证名字 没有就取系统名字
        }
        if (userCardInfo.get("cardType").toString().equals("102")){
            employeeDTO.setEnFirstName(userCardInfo.get("firstName").getAsString());
            employeeDTO.setEnLastName(userCardInfo.get("lastName").getAsString());
        }
        else {
            employeeDTO.setEnFirstName(null);
            employeeDTO.setEnLastName(null);
        }
        employeeDTO.setNationality(null);
        employeeDTO.setGender(empObject.get("genderCode").getAsString());
        employeeDTO.setRankName(empObject.get("rank").getAsString());
        employeeDTO.setIsBookClass(bookClass.get("isBookClass").getAsString());
        employeeDTO.setIntlBookClassBlock(bookClass.get("intlBookClassBlock").getAsString());
        employeeDTO.setTenantId(employee.getTenantId());
        employeeDTO.setCompanyId(empObject.get("companyOID").getAsString());
        employeeDTO.setCompanyOID(empObject.get("companyOID").getAsString());
        employeeDTO.setCompanyCode(empObject.get("companyOID").getAsString());
        employeeDTO.setDeptCode(departCode.get("departmentCode").getAsString());
        employeeDTO.setDeptName(empObject.get("departmentName").getAsString());
        employeeDTO.setDeptPath(empObject.get("departmentPath").getAsString());
        employeeDTO.setDeptCustomCode(departCode.get("custDeptNumber").getAsString());
        employeeDTO.setUserCardInfos(cardList);
        System.out.println(GsonUtil.objectToString(employeeDTO));
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

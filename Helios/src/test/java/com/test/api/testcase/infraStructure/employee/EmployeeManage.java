package com.test.api.testcase.infraStructure.employee;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.infrastructure.employee.EmployeeExtendComponent;
import com.hand.basicObject.infrastructure.employee.InfraEmployee;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.Infra.EmployeeMethod.EmployeeManagePage;
import com.test.api.method.InfraStructure;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
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
    private InfraEmployee infraEmployee;
    private InfraStructure infraStructure;
    //人员入参
    private String fullName = "接口新建"+ UTCTime.getBeijingTime(0,0);
    private String employeeID = String.valueOf(RandomNumber.getRandomNumber());
    private String mobile = "283666"+ RandomNumber.getRandomNumber();
    private String email = String.format("zhang%s@hui.com",RandomNumber.getRandomNumber());
    private String employeeTypeValueName = "技术";
    private String directManager = "懿测试宏";
    private String companyName = "甄滙测试宏公司（修改）测试修改";
    private String departmentName = "20200310072507测试";
    private String departmentCode = "20200310072507top";
    private String position = "测试工程师";
    private String duty = "测试";
    private String rank = "技术经理";
    private String errorEmployeeID = String.valueOf(RandomNumber.getTimeNumber(15));

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("hong.liang@xnhly.com") String phoneNumber, @Optional("hly123") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        employeeManagePage =new EmployeeManagePage();
        infraEmployee = new InfraEmployee();
        infraStructure = new InfraStructure();
    }

    @Test(description = "新增员工正常流程")
    public void addEmployee01() throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        component.setCustList("hong888");
        component.setText("1");
        component.setDefaultCostCenter("测试成本中心项987");
        JsonObject object = employeeManagePage.addEmployee(employee,fullName,mobile,employeeID,email,employeeTypeValueName,directManager,companyName,departmentName,departmentCode,position,duty,rank,component);
        String name = object.get("fullName").getAsString();
        log.info("获取到的人员姓名为：" + name);
        Assert.assertEquals(name,fullName);
    }

    @Test(description = "新增员工异常流程--工号为空")
    public void addEmployee02() throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        JsonObject object = employeeManagePage.addEmployee(employee,fullName,mobile,"",email,employeeTypeValueName,directManager,companyName,departmentName,departmentCode,position,duty,rank,component);
        String message = object.get("message").getAsString();
        String errorCode = object.get("errorCode").getAsString();
        Assert.assertEquals(message,"工号不能为空");
        Assert.assertEquals(errorCode,"6040008");
    }

    @Test(description = "新增员工异常流程--工号超20位")
    public void addEmployee03() throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        JsonObject object = employeeManagePage.addEmployee(employee,fullName,mobile,"intere"+errorEmployeeID ,email,employeeTypeValueName,directManager,companyName,departmentName,departmentCode,position,duty,rank,component);
        String message = object.get("message").getAsString();
        String errorCode = object.get("errorCode").getAsString();
        log.info(message+errorCode);
        Assert.assertEquals(message,"工号长度不允许超过20位");
        Assert.assertEquals(errorCode,"6040021");
    }

    @Test(description = "编辑员工-正常编辑-修改了邮箱,手机号以及生日")
    public void editEmployee() throws HttpStatusException {
        InfraEmployee infraEmployee =new InfraEmployee();
        infraEmployee.setMobile("130000"+RandomNumber.getRandomNumber());
        infraEmployee.setEmail(RandomNumber.getTimeNumber()+"@123.com");
        infraEmployee.setBirthday(UTCTime.getBeijingDate(-2000));
        assert employeeManagePage.editEmployee(employee,"11101",infraEmployee).toString().contains("fullName");
    }
}

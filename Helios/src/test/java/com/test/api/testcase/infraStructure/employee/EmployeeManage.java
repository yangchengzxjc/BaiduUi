package com.test.api.testcase.infraStructure.employee;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.infrastructure.employee.InfraEmployee;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.Infra.EmployeeMethod.EmployeeManagePage;
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

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978888") String phoneNumber, @Optional("hly123") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        employeeManagePage =new EmployeeManagePage();
        infraEmployee = new InfraEmployee();
    }

    @Test(description = "获取人员扩展字段详细")
    public void getEmployeeFiledDetail() throws HttpStatusException {
        JsonArray filedDetail = employeeManagePage.getEmployeeFiledDetail(employee);
        JsonObject filedDetail01 = filedDetail.get(0).getAsJsonObject();
        log.info("扩展字段1中的数据：" + filedDetail01);
        String filedOid = filedDetail01.get("fieldOID").getAsString();
        log.info("扩展字段1中的fieldOid为：" + filedOid);
    }

    @Test(description = "获取人员扩展字段自定义值列表的oid")
    public void getEmployeeFiledCustomDetail() throws HttpStatusException {
        String filedOid = employeeManagePage.getEmployeeFiledCustomEnumerationOID(employee,1);
        log.info("扩展字段自定义值列表的customEnumerationOID：" + filedOid);
    }

    @Test(description = "获取人员扩展字段自定义值列表的value")
    public void getEmployeeFieldCustomValue() throws HttpStatusException {
        String customValue = employeeManagePage.getEmployeeFiledCustomEnumerationValue(employee,0,1);
        log.info("获取到的自定义值列表value值为：" + customValue);
    }

    @Test(description = "新增员正常流程")
    public void addEmployee() throws HttpStatusException {
        JsonObject object = employeeManagePage.addEmployee(employee,"甄滙测试宏公司（修改）测试修改","20200310072507测试","20200310072507top","测试工程师","测试","技术经理");
        String fullName = object.get("fullName").getAsString();
        log.info("获取到的人员姓名为：" + fullName);
        Assert.assertEquals(fullName,infraEmployee.getFullName());
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

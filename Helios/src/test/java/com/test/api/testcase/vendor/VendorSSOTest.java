package com.test.api.testcase.vendor;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SSOBody;
import com.test.BaseTest;
import com.test.api.method.Vendor;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * @Author peng.zhang
 * @Date 2020/10/27
 * @Version 1.0
 **/
@Slf4j
public class VendorSSOTest extends BaseTest {


    private Employee employee;
    private Vendor vendor;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("yaliang.wang@cimc.com") String phoneNumber, @Optional("111111") String pwd, @Optional("stage") String env) {
        employee = getEmployee(phoneNumber, pwd, env);
        vendor = new Vendor();
    }

    @Test(description = "sso单点登录")
    public void ssoTest1() throws HttpStatusException {
        SSOBody ssoBody = SSOBody.builder()
                .tenantId(employee.getTenantId())
                .employeeId(employee.getEmployeeID())
                .deviceType("web")
                .initPage("HotelSearch")
                .orderNumber("")
                .build();
        JsonObject response = vendor.ssoLogin(employee, ssoBody, "cimccTMC", "200428140254184788", "");
        log.info("单点登录的响应:{}", response);
    }

}

package com.test.api.testcase.vendor;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SSOBody;
import com.hand.basicconstant.SupplierOID;
import com.test.BaseTest;
import com.test.api.method.InfraStructure;
import com.test.api.method.Vendor;
import com.test.api.method.VendorMethod.FlightOrder;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.*;

import static com.hand.basicconstant.SupplierOID.*;


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
    public void init(@Optional("14082978000") String phoneNumber, @Optional("hly123456") String pwd, @Optional("") String env){
        employee =getEmployee(phoneNumber,pwd,env);
        vendor =new Vendor();
    }
//    @Test(description = "sso单点登录")
//    public void ssoTest1() throws HttpStatusException {
//        SSOBody ssoBody =SSOBody.builder()
//                .tenantId(employee.getTenantId())
//                .employeeId(employee.getEmployeeID())
//                .deviceType("web")
//                .initPage("HotelSearch")
//                .orderNumber("")
//                .build();
//        JsonObject response = vendor.ssoLogin(employee,ssoBody,"cimccTMC","200428140254184788","");
//        log.info("单点登录的响应:{}",response);
//    }

    @DataProvider(name = "SSO")
    public Object[][] vendorSSO(){
        return new Object[][]{
                {employee,CTRIP_AIR,"H5","1002"},
                {employee,CTRIP_HOTEL,"H5","1002"},
                {employee,CTRIP_TRAIN,"H5","1002"}
        };
    }

    @Test(description = "sso单点登录",dataProvider = "SSO")
    public void vndSSO(Employee employee, String supplierOID,String direction,String pageType) throws HttpStatusException {
        JsonObject response = vendor.vndSSO(employee,supplierOID,direction,pageType);
        log.info("单点登录的响应:{}",response);
        int statusCode = vendor.ssoCode(employee,CTRIP_AIR,"H5","1002");
        Assert.assertEquals(statusCode,200);
    }

}

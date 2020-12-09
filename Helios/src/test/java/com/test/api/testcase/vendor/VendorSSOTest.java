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


    @DataProvider(name = "ctripSSO")
    public Object[][] ctripSSO(){
        return new Object[][]{
                {employee, CTRIPAIR.getSupplierOID(),"H5"},
                {employee, CTRIPHOTEL.getSupplierOID(),"H5"},
                {employee, CTRIPTRAIN.getSupplierOID(),"H5"},
                {employee,CTRIPAIR.getSupplierOID(),"web"},
                {employee,CTRIPHOTEL.getSupplierOID(),"web"},
                {employee,CTRIPTRAIN.getSupplierOID(),"web"},
        };
    }

    @Test(description = "携程sso首页单点登录",dataProvider = "ctripSSO")
    public void ctripSSO(Employee employee, String supplierOID,String direction) throws HttpStatusException {
        JsonObject response = vendor.vndSSO(employee,supplierOID,direction,"1002");
        log.info("单点登录的响应:{}",response);
        int statusCode = vendor.ssoCode(employee,supplierOID,direction,"1002");
        Assert.assertEquals(statusCode,200);
    }

    @DataProvider(name = "ctripSSO1")
    public Object[][] ctripSSO1(){
        return new Object[][]{
                {employee, CTRIPAIR.getSupplierOID(),"H5"},
                {employee, CTRIPHOTEL.getSupplierOID(),"H5"},
                {employee, CTRIPTRAIN.getSupplierOID(),"H5"},
        };
    }
    @Test(description = "携程sso订单单点登录",dataProvider = "ctripSSO1")
    public void ctripOrderSSO1(Employee employee, String supplierOID,String direction) throws HttpStatusException {
        JsonObject response = vendor.vndSSO(employee,supplierOID,direction,"1001");
        log.info("单点登录的响应:{}",response);
        int statusCode = vendor.ssoCode(employee,supplierOID,direction,"1001");
        Assert.assertEquals(statusCode,200);
    }

    @DataProvider(name = "meiYaSSO")
    public Object[][] meiYaSSO(){
        return new Object[][]{
                {employee, MEIYAAIR.getSupplierOID(),"H5"},
                {employee, MEIYAHOTEL.getSupplierOID(),"H5"},
                {employee, MEIYATRAIN.getSupplierOID(),"H5"},
                {employee,MEIYAAIR.getSupplierOID(),"web"},
                {employee,MEIYAHOTEL.getSupplierOID(),"web"},
                {employee,MEIYATRAIN.getSupplierOID(),"web"},
        };
    }
    @Test(description = "美亚sso首页单点登录" ,dataProvider = "meiYaSSO")
    public void meiYaSSO(Employee employee, String supplierOID,String direction) throws HttpStatusException {
        JsonObject response = vendor.vndSSO(employee,supplierOID,direction,"1002");
        log.info("单点登录的响应:{}",response);
        int statusCode = vendor.ssoCode(employee,supplierOID,direction,"1002");
        Assert.assertEquals(statusCode,200);
    }

}

package com.test.api.testcase.vendor;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import com.test.api.method.Vendor;
import lombok.extern.slf4j.Slf4j;
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
    public void init(@Optional("14082978000") String phoneNumber, @Optional("hly123456") String pwd, @Optional("") String env) {
        employee = getEmployee(phoneNumber, pwd, env);
        vendor = new Vendor();
    }


    @DataProvider(name = "ctripSSO")
    public Object[][] ctripSSO() {
        return new Object[][]{
                {"CTRIP AIR H5", CTRIPAIR.getSupplierOID(), "H5"},
                {"CTRIP HOTEL H5", CTRIPHOTEL.getSupplierOID(), "H5"},
                {"CTRIP TRAIN H5", CTRIPTRAIN.getSupplierOID(), "H5"},
                {"CTRIP AIR WEB", CTRIPAIR.getSupplierOID(), "web"},
                {"CTRIP HOTEL WEB", CTRIPHOTEL.getSupplierOID(), "web"},
                {"CTRIP TRAIN WEB", CTRIPTRAIN.getSupplierOID(), "web"},
        };
    }

    @Test(description = "携程sso首页单点登录", dataProvider = "ctripSSO")
    public void ctripSSO(String caseDesc, String supplierOID, String direction) throws HttpStatusException {
        JsonObject response = vendor.vndSSO(employee, supplierOID, direction, "1002");
        log.info("单点登录的响应:{}", response);
        int statusCode = vendor.ssoCode(employee, supplierOID, direction, "1002");
        Assert.assertEquals(statusCode, 200);
    }

    @DataProvider(name = "ctripSSO1")
    public Object[][] ctripSSO1() {
        return new Object[][]{
                {"CTRIP AIR H5", CTRIPAIR.getSupplierOID(), "H5"},
                {"CTRIP HOTEL H5", CTRIPHOTEL.getSupplierOID(), "H5"},
                {"CTRIP TRAIN H5", CTRIPTRAIN.getSupplierOID(), "H5"},
        };
    }

    @Test(description = "携程sso订单单点登录", dataProvider = "ctripSSO1")
    public void ctripOrderSSO1(String caseDesc, String supplierOID, String direction) throws HttpStatusException {
        JsonObject response = vendor.vndSSO(employee, supplierOID, direction, "1001");
        log.info("单点登录的响应:{}", response);
        int statusCode = vendor.ssoCode(employee, supplierOID, direction, "1001");
        Assert.assertEquals(statusCode, 200);
    }

    @DataProvider(name = "meiYaSSO")
    public Object[][] meiYaSSO() {
        return new Object[][]{
                {"MEIYA AIR H5", MEIYAAIR.getSupplierOID(), "H5"},
                {"MEIYA HOTEL H5", MEIYAHOTEL.getSupplierOID(), "H5"},
                {"MEIYA TRAIN H5", MEIYATRAIN.getSupplierOID(), "H5"},
                {"MEIYA AIR H5", MEIYAAIR.getSupplierOID(), "web"},
                {"MEIYA HOTEL H5", MEIYAHOTEL.getSupplierOID(), "web"},
                {"MEIYA TRAIN H5", MEIYATRAIN.getSupplierOID(), "web"},
        };
    }

    @Test(description = "美亚sso首页单点登录", dataProvider = "meiYaSSO")
    public void meiYaSSO(String caseDesc, String supplierOID, String direction) throws HttpStatusException {
        JsonObject response = vendor.vndSSO(employee, supplierOID, direction, "1002");
        log.info("单点登录的响应:{}", response);
        int statusCode = vendor.ssoCode(employee, supplierOID, direction, "1002");
        Assert.assertEquals(statusCode, 200);
    }

}

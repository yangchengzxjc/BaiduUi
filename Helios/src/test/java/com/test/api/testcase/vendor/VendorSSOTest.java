package com.test.api.testcase.vendor;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import com.test.api.method.Vendor;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.*;

import static com.hand.basicConstant.Supplier.*;


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
    public void init(@Optional("18333333333") String phoneNumber, @Optional("hly123") String pwd, @Optional("console") String env) {
        employee = getEmployee(phoneNumber, pwd, env);
        vendor = new Vendor();
    }


    @DataProvider(name = "ctripSSO")
    public Object[][] ctripSSO() {
        return new Object[][]{
                {"CTRIP AIR H5", CTRIP_AIR.getSupplierOID(), "H5"},
                {"CTRIP HOTEL H5", CTRIP_HOTEL.getSupplierOID(), "H5"},
                {"CTRIP TRAIN H5", CTRIP_TRAIN.getSupplierOID(), "H5"},
                {"CTRIP AIR WEB", CTRIP_AIR.getSupplierOID(), "web"},
                {"CTRIP HOTEL WEB", CTRIP_HOTEL.getSupplierOID(), "web"},
                {"CTRIP TRAIN WEB", CTRIP_TRAIN.getSupplierOID(), "web"},
                {"MEIYA AIR H5", MEIYA_FLIGHT.getSupplierOID(), "H5"},
                {"MEIYA HOTEL H5", MEIYA_HOTEL.getSupplierOID(), "H5"},
                {"MEIYA TRAIN H5", MEIYA_TRAIN.getSupplierOID(), "H5"},
                {"MEIYA AIR H5", MEIYA_FLIGHT.getSupplierOID(), "web"},
                {"MEIYA HOTEL H5", MEIYA_HOTEL.getSupplierOID(), "web"},
                {"MEIYA TRAIN H5", MEIYA_TRAIN.getSupplierOID(), "web"},
        };
    }

    @Test(description = "sso首页单点登录", dataProvider = "ctripSSO")
    public void SSO(String caseDesc, String supplierOID, String direction) throws HttpStatusException {
        JsonObject response = vendor.vndSSO(employee, supplierOID, direction, "1002");
        log.info("单点登录的响应:{}", response);
        int statusCode = vendor.ssoCode(employee, supplierOID, direction, "1002");
        Assert.assertEquals(statusCode, 200);
    }

}

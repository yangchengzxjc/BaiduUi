package com.test.api.testcase.vendor.sso;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import com.test.api.method.VendorMethod.VendorInfo;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class InfoSsoCommonTest extends BaseTest {

    private Employee employee;
    private VendorInfo vendorInfo;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("14082978666") String phoneNumber, @Optional("hly123456") String pwd, @Optional("stage") String env) {
        employee = getEmployee(phoneNumber, pwd, env);
        vendorInfo = new VendorInfo();
    }

    @DataProvider
    public Object[][] dataProvider(Method method){
        return new Object[][]{
//                {caseDesc,
//                supplierOID, newVendorsName, pageType, vendorType,
//                direction, orderId, businessCode, startCity, endCity, startDate, endDate, forCorp, flightSearchType
//                res_expect},
                {"ctrip",
                "50b78b72-ed9d-4392-b7a1-df3b79f4cb17", null, 1002, "2002",
                "web", null, null, null, null, null, null, null, null,
                "Backurl=http://ct.ctrip.com/flight/home\"}"},
                {"CTRIP_TRAIN",
                "213691b5-75a4-11e7-af18-00163e00373d", null, 1002, "2001",
                "H5", null, null, null, null, null, null, null, null,
                "URL=https://ct.ctrip.com/m/SingleSignOn/H5SignInfo"},
        };
    }

    @Test(description = "vendor info sso common", dataProvider = "dataProvider")
    public void ssoCommonTest(String caseDesc,
                              String supplierOID,
                              String newVendorsName,
                              Integer pageType,
                              String vendorType,
                              String direction,
                              String orderId,
                              String businessCode,
                              String startCity,
                              String endCity,
                              String startDate,
                              String endDate,
                              String forCorp,
                              String flightSearchType,
                              String res_expect) throws HttpStatusException {
        // do request
        String resStr = vendorInfo.vendorInfoSsoCommon(employee, supplierOID, newVendorsName, pageType, vendorType,
                direction, orderId, businessCode, startCity, endCity, startDate, endDate, forCorp, flightSearchType);
        // assert result
        Assert.assertTrue((resStr.contains(res_expect)));
    }
}

package com.test.api.testcase.vendor.sso;

import com.hand.api.VendorInfoApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import com.test.api.method.VendorMethod.VendorInfo;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class InfoSsoTest extends BaseTest {

    private Employee employee;
    private VendorInfoApi vendorInfo;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("14082978666") String phoneNumber, @Optional("hly123456") String pwd, @Optional("stage") String env) {
        employee = getEmployee(phoneNumber, pwd, env);
        vendorInfo = new VendorInfoApi();
    }

    @DataProvider
    public Object[][] dataProvider(Method method){
        return new Object[][]{
//                { caseDesc, supplierOID, vendorsName, businessCode, pageType, vendorType,
//                itineraryDirection, lng, lat, direction, orderId, startCity, endCity,
//                startDate, endDate, forCorp, flightSearchType,
//                res_expect },
                {"web 获取所有登录URL", "66666666-6666-11e6-9639-00ffa3fb4c67", null, null, null, null,
                null, null, null, "WEB", null, null, null,
                null, null, null, null,
                "loginUrl"},
                {"web CTRIP_TRAIN", "213691b5-75a4-11e7-af18-00163e00373d", null, null, null, "2001",
                null, null, null, "WEB", null, null, null,
                null, null, null, null,
                "ctrip"},
                {"web cimmcc_air", "8afc4c9e-a7ea-4de6-ab60-70669a5b91e8", null, null, null, "2002",
                null, null, null, "WEB", null, null, null,
                null, null, null, null,
                "cimc"},
        };
    }

    @Test(description = "vendor info sso", dataProvider = "dataProvider")
    public void ssoTest(String caseDesc,
                        @Optional("66666666-6666-11e6-9639-00ffa3fb4c67") String supplierOID,
                        String vendorsName,
                        String businessCode,
                        Integer pageType,
                        String vendorType,
                        Integer itineraryDirection,
                        Double lng,
                        Double lat,
                        @Optional("WEB") String direction,
                        String orderId,
                        String startCity,
                        String endCity,
                        String startDate,
                        String endDate,
                        String forCorp,
                        String flightSearchType,
                        String res_expect) throws HttpStatusException {
        // do request
        String resStr = vendorInfo.vendorInfoSso(employee, supplierOID, vendorsName, businessCode, pageType, vendorType,
                itineraryDirection, lng, lat, direction, orderId, startCity, endCity,
                startDate, endDate, forCorp, flightSearchType);
        // assert result
        Assert.assertTrue(resStr.contains(res_expect));
    }
}

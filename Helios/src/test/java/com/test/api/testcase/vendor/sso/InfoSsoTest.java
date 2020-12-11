package com.test.api.testcase.vendor.sso;

import com.hand.api.VendorInfoApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicConstant.Supplier;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    public Object[][] dataProvider(Method method) {
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

    @DataProvider
    public Iterator<Object[]> iteratorDataProvider() {
        List<Object[]> objList = new ArrayList<Object[]>();
        ArrayList<String> vendorList = new ArrayList<>();
        vendorList.add("cimcctmc");
        vendorList.add("大唐");
        for (String vendor:vendorList) {
            objList.add(new Object[]{vendor, Supplier.getSupplierOIDByName(vendor)});
        }
        return objList.iterator();
    }

    @Test(dataProvider = "iteratorDataProvider")
    public void iteratorDataTest(String caseDesc,
                                 String supplierOID
                                 ) {
        String testStr = caseDesc + " " + supplierOID;
        System.out.println(testStr);
    }

    @Test(description = "vendor info sso H5", dataProvider = "dataProvider")
    public void ssoTest(String caseDesc,
                        String supplierOID,
                        @Optional("H5") String direction,
                        @Optional("1002")Integer pageType,
                        String res_expect) throws HttpStatusException {
        // do request
        String resStr = vendorInfo.vendorInfoSso(employee, supplierOID, direction, pageType);
        // assert result
        Assert.assertTrue(resStr.contains(res_expect));
    }
}

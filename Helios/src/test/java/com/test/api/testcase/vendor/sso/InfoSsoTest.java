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
    public Iterator<Object[]> iteratorDataProvider() {
        List<Object[]> objList = new ArrayList<Object[]>();
        ArrayList<String> vendorList = new ArrayList<>();
        vendorList.add("cimcctmcAir");
        vendorList.add("大唐");
        vendorList.add("深圳航空");
        for (String vendor : vendorList) {
            objList.add(new Object[]{vendor, Supplier.getSupplierOIDByName(vendor),
                    "H5", 1002, Supplier.getSupplierUrlByName(vendor)});
        }
        return objList.iterator();
    }

    @Test(description = "vendor info sso H5", dataProvider = "iteratorDataProvider")
    public void ssoTest(String caseDesc,
                        String supplierOID,
                        @Optional("H5") String direction,
                        @Optional("1002") Integer pageType,
                        @Optional("url=")String res_expect) throws HttpStatusException {
        // do request
        String resStr = vendorInfo.vendorInfoSso(employee, supplierOID, direction, pageType);
        // assert result
        Assert.assertTrue(resStr.contains(res_expect));
    }
}

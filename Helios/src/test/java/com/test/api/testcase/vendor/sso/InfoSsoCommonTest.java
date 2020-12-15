package com.test.api.testcase.vendor.sso;

import com.hand.api.VendorInfoApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicConstant.Supplier;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.hand.basicConstant.Supplier.*;

@Slf4j
public class InfoSsoCommonTest extends BaseTest {

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
//                {caseDesc,
//                supplierOID,  pageType, vendorType, direction,
//                res_expect},
                {"cimcctmcAir web",
                        cimcctmcAir.getSupplierOID(), 1002, cimcctmcAir.getVendorType(), "web",
                        "url"},
        };
    }

    @DataProvider
    public Iterator<Object[]> iteratorDataProvider() {
        List<Object[]> objList = new ArrayList<Object[]>();
        ArrayList<String> vendorList = new ArrayList<>();
        vendorList.add("cimcctmcAir");
        vendorList.add("大唐");
        vendorList.add("深圳航空");
        for (String vendor : vendorList) {
            objList.add(new Object[]{vendor, Supplier.getSupplierOIDByName(vendor), 1002,
                    Supplier.getSupplierTypeByName(vendor),"web", Supplier.getSupplierUrlByName(vendor)});
        }
        return objList.iterator();
    }

    @Test(description = "vendor info sso common web", dataProvider = "iteratorDataProvider")
    public void ssoCommonTest(String caseDesc,
                              String supplierOID,
                              Integer pageType,
                              String vendorType,
                              String direction,
                              String res_expect) throws HttpStatusException {
        // do request
        String resStr = vendorInfo.vendorInfoSsoCommon(employee, supplierOID, pageType, vendorType, direction);
        log.info("actual result: {}", resStr);
        log.info("expect result: {}", res_expect);
        // assert result
        Assert.assertTrue((resStr.contains(res_expect)));
    }
}

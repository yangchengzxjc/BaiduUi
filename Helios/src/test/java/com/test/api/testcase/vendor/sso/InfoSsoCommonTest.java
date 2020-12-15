package com.test.api.testcase.vendor.sso;

import com.hand.api.VendorInfoApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static com.hand.basicConstant.Supplier.*;

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
                {"cimcctmcTrain web",
                        cimcctmcTrain.getSupplierOID(), 1002, cimcctmcTrain.getVendorType(), "web",
                        "url"},
                {"cimcctmcHotel web",
                        cimcctmcHotel.getSupplierOID(), 1002, cimcctmcHotel.getVendorType(), "web",
                        "url"},
                {"dttrip web",
                        dttrip.getSupplierOID(), 1002, dttrip.getVendorType(), "web",
                        "url"},
                {"shenzhenAir web",
                        shenzhenAir.getSupplierOID(), 1002, shenzhenAir.getVendorType(), "web",
                        "url"},
                {"onTheWayTMCAir web",
                        onTheWayTMCAir.getSupplierOID(), 1002, onTheWayTMCAir.getVendorType(), "web",
                        "url"},
                {"onTheWayTMCTrain web",
                        onTheWayTMCTrain.getSupplierOID(), 1002, onTheWayTMCTrain.getVendorType(), "web",
                        "url"},
                {"onTheWayTMCHotel web",
                        onTheWayTMCHotel.getSupplierOID(), 1002, onTheWayTMCHotel.getVendorType(), "web",
                        "url"},
                {"tehang web",
                        tehang.getSupplierOID(), 1002, tehang.getVendorType(), "web",
                        "url"},
                {"fyair web",
                        fyair.getSupplierOID(), 1002, fyair.getVendorType(), "web",
                        "url"},
        };
    }

    @Test(description = "vendor info sso common web", dataProvider = "dataProvider")
    public void ssoCommonTest(String caseDesc,
                              String supplierOID,
                              Integer pageType,
                              String vendorType,
                              String direction,
                              String res_expect) throws HttpStatusException {
        // do request
        String resStr = vendorInfo.vendorInfoSsoCommon(employee, supplierOID, pageType, vendorType, direction);
        // assert result
        Assert.assertTrue((resStr.contains(res_expect)));
    }
}

package com.test.api.testcase.vendor.sso;

import com.hand.api.VendorInfoApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import com.test.api.method.VendorMethod.VendorInfo;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static com.test.api.testcase.vendor.sso.InfoSsoEnum.*;

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
//                supplierOID, newVendorsName, pageType, vendorType,
//                direction, orderId, businessCode, startCity, endCity, startDate, endDate, forCorp, flightSearchType
//                res_expect},
                {"CTRIP_TRAIN H5",
                        CTRIP_TRAIN.getSupplierOid(), null, 1002, CTRIP_TRAIN.getCategory(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"CTRIP_AIR H5",
                        CTRIP_AIR.getSupplierOid(), null, 1002, CTRIP_AIR.getCategory(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"CTRIP_HOTEL H5",
                        CTRIP_HOTEL.getSupplierOid(), null, 1002, CTRIP_HOTEL.getCategory(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"BAOKU_AIR H5",
                        BAOKU_AIR.getSupplierOid(), null, 1002, BAOKU_AIR.getCategory(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"BAOKU_HOTEL H5",
                        BAOKU_HOTEL.getSupplierOid(), null, 1002, BAOKU_HOTEL.getCategory(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"cimcctmcAir H5",
                        cimcctmcAir.getSupplierOid(), null, 1002, cimcctmcAir.getCategory(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"cimcctmcTrain H5",
                        cimcctmcTrain.getSupplierOid(), null, 1002, cimcctmcTrain.getCategory(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"cimcctmcHotel H5",
                        cimcctmcHotel.getSupplierOid(), null, 1002, cimcctmcHotel.getCategory(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"dttrip H5",
                        dttrip.getSupplierOid(), null, 1002, dttrip.getCategory(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"shenzhenAir H5",
                        shenzhenAir.getSupplierOid(), null, 1002, shenzhenAir.getCategory(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"onTheWayTMCAir H5",
                        onTheWayTMCAir.getSupplierOid(), null, 1002, onTheWayTMCAir.getCategory(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"onTheWayTMCTrain H5",
                        onTheWayTMCTrain.getSupplierOid(), null, 1002, onTheWayTMCTrain.getCategory(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"onTheWayTMCHotel H5",
                        onTheWayTMCHotel.getSupplierOid(), null, 1002, onTheWayTMCHotel.getCategory(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"tehang H5",
                        tehang.getSupplierOid(), null, 1002, tehang.getCategory(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"fyair H5",
                        fyair.getSupplierOid(), null, 1002, fyair.getCategory(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
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

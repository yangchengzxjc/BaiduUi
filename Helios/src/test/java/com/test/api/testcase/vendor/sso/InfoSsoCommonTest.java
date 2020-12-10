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
//                supplierOID, newVendorsName, pageType, vendorType,
//                direction, orderId, businessCode, startCity, endCity, startDate, endDate, forCorp, flightSearchType
//                res_expect},
                {"CTRIP_TRAIN H5",
                        CTRIP_TRAIN.getSupplierOID(), null, 1002, CTRIP_TRAIN.getVendorType(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"CTRIP_AIR H5",
                        CTRIP_AIR.getSupplierOID(), null, 1002, CTRIP_AIR.getVendorType(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"CTRIP_HOTEL H5",
                        CTRIP_HOTEL.getSupplierOID(), null, 1002, CTRIP_HOTEL.getVendorType(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"BAOKU_AIR H5",
                        BAOKU_AIR.getSupplierOID(), null, 1002, BAOKU_AIR.getVendorType(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"BAOKU_HOTEL H5",
                        BAOKU_HOTEL.getSupplierOID(), null, 1002, BAOKU_HOTEL.getVendorType(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"cimcctmcAir H5",
                        cimcctmcAir.getSupplierOID(), null, 1002, cimcctmcAir.getVendorType(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"cimcctmcTrain H5",
                        cimcctmcTrain.getSupplierOID(), null, 1002, cimcctmcTrain.getVendorType(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"cimcctmcHotel H5",
                        cimcctmcHotel.getSupplierOID(), null, 1002, cimcctmcHotel.getVendorType(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"dttrip H5",
                        dttrip.getSupplierOID(), null, 1002, dttrip.getVendorType(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"shenzhenAir H5",
                        shenzhenAir.getSupplierOID(), null, 1002, shenzhenAir.getVendorType(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"onTheWayTMCAir H5",
                        onTheWayTMCAir.getSupplierOID(), null, 1002, onTheWayTMCAir.getVendorType(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"onTheWayTMCTrain H5",
                        onTheWayTMCTrain.getSupplierOID(), null, 1002, onTheWayTMCTrain.getVendorType(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"onTheWayTMCHotel H5",
                        onTheWayTMCHotel.getSupplierOID(), null, 1002, onTheWayTMCHotel.getVendorType(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"tehang H5",
                        tehang.getSupplierOID(), null, 1002, tehang.getVendorType(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
                {"fyair H5",
                        fyair.getSupplierOID(), null, 1002, fyair.getVendorType(),
                        "H5", null, null, null, null, null, null, null, null,
                        "url"},
        };
    }

    @Test(description = "vendor info sso common ", dataProvider = "dataProvider")
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

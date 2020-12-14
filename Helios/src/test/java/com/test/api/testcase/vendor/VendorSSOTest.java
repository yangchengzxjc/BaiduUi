package com.test.api.testcase.vendor;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import com.test.api.method.Vendor;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.*;

import static com.hand.basicConstant.SupplierOID.*;


/**
 * @Author peng.zhang
 * @Date 2020/10/27
 * @Version 1.0
 **/
@Slf4j
public class VendorSSOTest extends BaseTest {


    private Employee employee;
    private Vendor vendor;
    private String env;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("14082978000") String phoneNumber, @Optional("hly123456") String pwd, @Optional("stage") String env) {
        employee = getEmployee(phoneNumber, pwd, env);
        vendor = new Vendor();
        this.env = env;
    }

    @DataProvider( name = "vendorSSO")
    public Object[][] vendorSSO() {
        if (env.equals("prod")) {
        return new Object[][]{
            };
        }
        else {
            return new Object[][]{
                    {"甄选机票H5首页单点登录",ZHENXUANFLIGHT.getSupplierOID(),"H5","https://airlinesstage.huilianyi.com/?"},
                    {"携程机票H5首页单点登录", CTRIPAIR.getSupplierOID(), "H5","URL=https://ct.ctrip.com/m/SingleSignOn/H5SignInfo&CallBack"},
                    {"携程酒店H5首页单点登录", CTRIPHOTEL.getSupplierOID(), "H5","URL=https://ct.ctrip.com/m/SingleSignOn/H5SignInfo&CallBack"},
                    {"携程火车H5首页单点登录", CTRIPTRAIN.getSupplierOID(), "H5","URL=https://ct.ctrip.com/m/SingleSignOn/H5SignInfo&CallBack"},
                    {"携程机票WEB首页单点登录", CTRIPAIR.getSupplierOID(), "web","URL=https://www.corporatetravel.ctrip.com/corpservice/authorize/login"},
                    {"携程酒店WEB首页单点登录", CTRIPHOTEL.getSupplierOID(), "web","URL=https://www.corporatetravel.ctrip.com/corpservice/authorize/login"},
                    {"携程火车WEB首页单点登录", CTRIPTRAIN.getSupplierOID(), "web","URL=https://www.corporatetravel.ctrip.com/corpservice/authorize/login"},
                    {"携程机票H5订单单点登录", CTRIPAIR.getSupplierOID(), "H5","URL=https://ct.ctrip.com/m/SingleSignOn/H5SignInfo&CallBack"},
                    {"携程酒店H5订单单点登录", CTRIPHOTEL.getSupplierOID(), "H5","URL=https://ct.ctrip.com/m/SingleSignOn/H5SignInfo&CallBack"},
                    {"携程火车H5订单单点登录", CTRIPTRAIN.getSupplierOID(), "H5","URL=https://ct.ctrip.com/m/SingleSignOn/H5SignInfo&CallBack"},
                    {"美亚机票H5首页单点登录", MEIYAAIR.getSupplierOID(), "H5",
                            "unih5/#/pages/dAirTk/search/index?backUrl=&applyNo=&h5ApplyTripId=1&tripNum=0&menuId=DAirTk&formResult=1&data"},
                    {"美亚酒店H5首页单点登录", MEIYAHOTEL.getSupplierOID(), "H5",
                            "unih5/#/pages/dAirTk/search/index?backUrl=&applyNo=&h5ApplyTripId=1&tripNum=0&menuId=DAirTk&formResult=1&data"},
                    {"美亚火车H5首页单点登录", MEIYATRAIN.getSupplierOID(), "H5",
                            "unih5/#/pages/dAirTk/search/index?backUrl=&applyNo=&h5ApplyTripId=1&tripNum=0&menuId=DAirTk&formResult=1&data"},
                    {"美亚机票WEB首页单点登录", MEIYAAIR.getSupplierOID(), "web",
                            "Route.aspx?code=p64l0lJqZo7zvHn1Qpj"},
                    {"美亚酒店WEB首页单点登录", MEIYAHOTEL.getSupplierOID(), "web",
                            "Route.aspx?code=p64l0lJqZo7zvHn1Qpj"},
                    {"美亚火车WEB首页单点登录", MEIYATRAIN.getSupplierOID(), "web",
                            "Route.aspx?code=p64l0lJqZo7zvHn1Qpj"},
                    {"美亚机票H5订单单点登录", MEIYAAIR.getSupplierOID(), "H5",
                            "unih5/#/pages/dAirTk/search/index?backUrl=&applyNo=&h5ApplyTripId=1&tripNum=0&menuId=DAirTk&formResult=1&data"},
                    {"美亚酒店H5订单单点登录", MEIYAHOTEL.getSupplierOID(), "H5",
                            "unih5/#/pages/dAirTk/search/index?backUrl=&applyNo=&h5ApplyTripId=1&tripNum=0&menuId=DAirTk&formResult=1&data"},
                    {"美亚火车H5订单单点登录", MEIYATRAIN.getSupplierOID(), "H5",
                            "unih5/#/pages/dAirTk/search/index?backUrl=&applyNo=&h5ApplyTripId=1&tripNum=0&menuId=DAirTk&formResu1lt=1&data"}
            };
        }
    }

    @Test(priority = 1,description = "sso单点登录", dataProvider = "vendorSSO")
    public void vendorSSO(String caseDesc, String supplierOID, String direction,String res_expect) throws HttpStatusException {
        String response = vendor.vndSSO(employee, supplierOID, direction, "1002");
        log.info("单点登录的响应:{}", response);
        Assert.assertTrue(response.contains(res_expect));
    }
}

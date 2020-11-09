package com.test.api.testcase.vendor.orderData;

import com.hand.basicObject.Employee;
import com.hand.basicconstant.OrderSettlementDataPath;
import com.hand.basicconstant.TmcChannel;
import com.test.BaseTest;
import com.test.api.method.Vendor;
import com.test.api.method.VendorMethod.VendorData;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * @Author peng.zhang
 * @Date 2020/11/9
 * @Version 1.0
 **/
public class OrderDataTest extends BaseTest {


    private Employee employee;
    private Vendor vendor;
    private VendorData mVendorData;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("17767542345") String phoneNumber, @Optional("glf12345") String pwd, @Optional("stage") String env){
        employee =getEmployee(phoneNumber,pwd,env);
        vendor =new Vendor();
        mVendorData =new VendorData();
    }

    @DataProvider(name = "TMC")
    public Object[][] tmcData() {
        return new Object[][]{
                {TmcChannel.AMEX.getSupplierCode(),TmcChannel.AMEX.getCorpId(),TmcChannel.AMEX.getSigniture(), OrderSettlementDataPath.settlementUseData},
        };
    }

    public void flightOrderTest1(String supplierCode,String appName,String corpId,String signature,String path){

    }

}

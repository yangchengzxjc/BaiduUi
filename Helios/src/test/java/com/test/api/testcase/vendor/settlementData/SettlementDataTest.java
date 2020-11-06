package com.test.api.testcase.vendor.settlementData;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicconstant.TmcChannel;
import com.test.BaseTest;
import com.test.api.method.InfraStructure;
import com.test.api.method.Vendor;
import com.test.api.method.VendorMethod.VendorData;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

/**
 * @Author peng.zhang
 * @Date 2020/11/4
 * @Version 1.0
 **/
@Slf4j
public class SettlementDataTest extends BaseTest {

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
                {TmcChannel.AMEX.getAppName(),TmcChannel.CIMCC.getCorpId()},
        };
    }

    @Test(description = "用于真实的供应商的机票结算数据进行推数据落库测试",dataProvider = "TMC")
    public void settlementDataTest1(String appName,String corpId) throws HttpStatusException {
        JsonObject vendorData = vendor.getVendorData("src/test/resources/data/amexVendorData.json");
        //推送结算数据
//        vendor.pushSettlementData(employee,"flight",vendorData,appName,corpId,signature);
        //拼装数据（因为有些数据是后台逻辑查询的)
        JsonObject settlementObject = mVendorData.setSettlementData(employee,vendorData,appName,corpId);
        log.info("拼装的数据:{}",settlementObject);
    }
}

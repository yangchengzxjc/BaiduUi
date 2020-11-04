package com.test.api.testcase.vendor.settlementData;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import com.test.api.method.InfraStructure;
import com.test.api.method.Vendor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * @Author peng.zhang
 * @Date 2020/11/4
 * @Version 1.0
 **/
public class SettlementDataTest extends BaseTest {

    private Employee employee;
    private Vendor vendor;
    private InfraStructure infraStructure;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("yaliang.wang@cimc.com") String phoneNumber, @Optional("111111") String pwd, @Optional("stage") String env){
        employee =getEmployee(phoneNumber,pwd,env);
        vendor =new Vendor();
        infraStructure =new InfraStructure();
    }

    @Test(description = "用于真实的供应商的结算数据进行推数据落库测试")
    public void settlementDataTest1(String path,String appName,String corpId,String signature) throws HttpStatusException {
        JsonObject vendorData = vendor.getVendorData(path);
        //推送结算数据
        vendor.pushSettlementData(employee,"flight",vendorData,appName,corpId,signature);


    }
}

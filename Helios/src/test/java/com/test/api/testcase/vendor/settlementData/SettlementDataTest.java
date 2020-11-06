package com.test.api.testcase.vendor.settlementData;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SettlementBody;
import com.hand.basicconstant.OrderSettlementDataPath;
import com.hand.basicconstant.TmcChannel;
import com.hand.utils.GsonUtil;
import com.test.BaseTest;
import com.test.api.method.Vendor;
import com.test.api.method.VendorMethod.VendorData;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

import java.util.HashMap;

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
                {TmcChannel.AMEX.getSupplierCode(),TmcChannel.AMEX.getCorpId(),TmcChannel.AMEX.getSigniture(), OrderSettlementDataPath.settlementUseData},
        };
    }

    /**
     *  需要提供测试账号租户 和corpId  相对应起来，这样里面后台逻辑落库数据才能测试  否则会出现签名错误或者book user not found
     *
     */
    @Test(description = "用于真实的供应商的机票结算数据进行推数据落库测试",dataProvider = "TMC")
    public void settlementDataTest1(String supplierCode,String appName,String corpId,String signature,String path) throws HttpStatusException {
        JsonObject vendorData = vendor.getFlightSettlementData(employee,path,corpId,supplierCode);
        log.info("vendorData:{}",vendorData);
          //推送结算数据
        JsonObject pushData = vendor.pushSettlementData(employee,"flight",vendorData,appName,corpId,signature);
        log.info("是否成功:{}",pushData);
        //拼装数据（因为有些数据是后台逻辑查询的)
        JsonObject settlementObject = mVendorData.setFlightSettlementData(employee,vendorData,appName,corpId);
        log.info("拼装的数据:{}",settlementObject);
        //内部接口查询的数据
        SettlementBody settlementBody =SettlementBody.builder()
                .accBalanceBatchNo(vendorData.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject().get("accBalanceBatchNo").getAsString())
                .orderNo(vendorData.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject().get("orderNo").getAsString())
                .companyOid("")
                .size(10)
                .page(1)
                .build();
        JsonObject internalQuerySettlement = vendor.internalQuerySettlement(employee,"flight",settlementBody);
        log.info("查询的机票结算数据:{}",internalQuerySettlement);
        HashMap<String,String> mapping = new HashMap<>();
        mapping.put("orderType","payType");
        mapping.put("acityCode","heliosacityCode");
        mapping.put("dcityCode","heliosdcityCode");
        assert GsonUtil.compareJsonObject(settlementObject,internalQuerySettlement,mapping);
    }

    /**
     *  需要提供测试账号租户 和corpId  相对应起来，这样里面后台逻辑落库数据才能测试  否则会出现签名错误或者book user not found
     *
     */
    @Test(description = "用于真实的供应商的火车结算数据进行推数据落库测试",dataProvider = "TMC")
    public void settlementDataTest2(String supplierCode,String appName,String corpId,String signature,String path) throws HttpStatusException {
        JsonObject vendorData = vendor.getTrainSettlementData(employee,path,corpId,supplierCode);
        log.info("vendorData:{}",vendorData);
        //推送结算数据
        JsonObject pushData = vendor.pushSettlementData(employee,"flight",vendorData,appName,corpId,signature);
        log.info("是否成功:{}",pushData);
        //拼装数据（因为有些数据是后台逻辑查询的)
        JsonObject settlementObject = mVendorData.setFlightSettlementData(employee,vendorData,appName,corpId);
        log.info("拼装的数据:{}",settlementObject);
        //内部接口查询的数据
        SettlementBody settlementBody =SettlementBody.builder()
                .accBalanceBatchNo(vendorData.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject().get("accBalanceBatchNo").getAsString())
                .orderNo(vendorData.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject().get("orderNo").getAsString())
                .companyOid("")
                .size(10)
                .page(1)
                .build();
        JsonObject internalQuerySettlement = vendor.internalQuerySettlement(employee,"flight",settlementBody);
        log.info("查询的机票结算数据:{}",internalQuerySettlement);
        HashMap<String,String> mapping = new HashMap<>();
        mapping.put("orderType","payType");
        mapping.put("acityCode","heliosacityCode");
        mapping.put("dcityCode","heliosdcityCode");
        assert GsonUtil.compareJsonObject(settlementObject,internalQuerySettlement,mapping);
    }


}

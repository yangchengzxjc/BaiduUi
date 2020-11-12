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
    public void init(@Optional("15023489123") String phoneNumber, @Optional("a11111") String pwd, @Optional("stage") String env){
        employee =getEmployee(phoneNumber,pwd,env);
        vendor =new Vendor();
        mVendorData =new VendorData();
    }

    @DataProvider(name = "TMC")
    public Object[][] tmcData() {
        return new Object[][]{
                {TmcChannel.AMEX.getSupplierName(),TmcChannel.AMEX.getSupplierCode(),TmcChannel.AMEX.getAppName(),TmcChannel.AMEX.getCorpId(),TmcChannel.AMEX.getSigniture(), OrderSettlementDataPath.settlementChangeData},
        };
    }

    /**
     *  需要提供测试账号租户 和corpId  相对应起来，这样里面后台逻辑落库数据才能测试  否则会出现签名错误或者book user not found
     *  1.supplierCode  appName corpId signature 以及tmc 模板数据的路径来自数据驱动,路径使用相对路径
     *  总的逻辑是：先读取提供的模板数据 然后更改模板数据中bookClerkName,bookClerkEmployeeId 以及passengerName 和 passengerEmployeeId  目的是为了校验nebula后台逻辑
     *  订单号orderNo和批次号防止推送的数据重复  所以修改下订单号和批次号
     *  后台逻辑生成的tentantId  tentantCode 等数据校采用的是员工的的实际租户和公司 进行校验
     *  heliosacityCode 汇联易城市代码 stage 和生产如果未维护此代码的话 则落地后汇联易城市代码为null
     *
     */
    @Test(description = "用于真实的供应商的机票模板结算数据进行推数据落库测试",dataProvider = "TMC")
    public void settlementDataTest1(String supplierName,String supplierCode,String appName,String corpId,String signature,String path) throws HttpStatusException {
        //  读取模板数据 并修改订票人和乘客的name 和 code(工号)其他数据不做修改
        JsonObject vendorData = vendor.getFlightSettlementData(employee,path,corpId,supplierCode);
        log.info("vendorData:{}",vendorData);
          //推送结算数据  模板数据推送
        JsonObject pushData = vendor.pushSettlementData(employee,"flight",vendorData,appName,corpId,signature);
        //拼装数据（因为有些数据是后台逻辑查询的)添加了一下不必传的字段数据和后台逻辑生成的字段。这块拼装的数据是直接在模板数据中添加的
        JsonObject settlementObject = mVendorData.setSettlementData(employee,"flight",vendorData,supplierName,supplierCode,corpId);
        log.info("拼装的数据:{}",settlementObject);
        //内部接口查询的数据
        SettlementBody settlementBody =SettlementBody.builder()
                .accBalanceBatchNo(vendorData.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject().get("accBalanceBatchNo").getAsString())
                .orderNo(vendorData.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject().get("orderNo").getAsString())
                .companyOid("")
                .size(10)
                .page(1)
                .build();
        //内部接口查询出来的数据
        JsonObject internalQuerySettlement = vendor.internalQuerySettlement(employee,"flight",settlementBody);
        log.info("查询的机票结算数据:{}",internalQuerySettlement);
        HashMap<String,String> mapping = new HashMap<>();
        mapping.put("orderType","payType");
        mapping.put("acityCode","heliosacityCode");
        mapping.put("dcityCode","heliosdcityCode");
        //对比数据直接采用拼装的数据作为参照数据和内部接口查询的数据进行对比
        assert GsonUtil.compareJsonObject(settlementObject.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject(),internalQuerySettlement,mapping);
    }

    /**
     *  需要提供测试账号租户 和corpId  相对应起来，这样里面后台逻辑落库数据才能测试  否则会出现签名错误或者book user not found
     *
     */
    @Test(description = "用于真实的供应商的火车模板结算数据进行推数据落库测试",dataProvider = "TMC")
    public void settlementDataTest2(String supplierName,String supplierCode,String appName,String corpId,String signature,String path) throws HttpStatusException {
        JsonObject vendorData = vendor.getTrainSettlementData(employee,"src/test/resources/data/VendorTrainSettlementData.json",corpId,supplierCode);
        log.info("vendorData:{}",vendorData);
        //推送结算数据
        JsonObject pushData = vendor.pushSettlementData(employee,"train",vendorData,appName,corpId,signature);
        log.info("是否成功:{}",pushData);
        //拼装数据（因为有些数据是后台逻辑查询的)
        JsonObject settlementObject = mVendorData.setSettlementData(employee,"train",vendorData,supplierName,supplierCode,corpId);
        log.info("拼装的数据:{}",settlementObject);
        //内部接口查询的数据
        SettlementBody settlementBody =SettlementBody.builder()
                .accBalanceBatchNo(vendorData.getAsJsonArray("trainSettlementInfos").get(0).getAsJsonObject().getAsJsonObject("trainBaseSettlement").get("accBalanceBatchNo").getAsString())
                .orderNo(vendorData.getAsJsonArray("trainSettlementInfos").get(0).getAsJsonObject().getAsJsonObject("trainBaseSettlement").get("orderNo").getAsString())
                .companyOid("")
                .size(10)
                .page(1)
                .build();
        JsonObject internalQuerySettlement = vendor.internalQuerySettlement(employee,"train",settlementBody);
        log.info("查询的火车结算数据:{}",internalQuerySettlement);
        //映射表
        HashMap<String,String> mapping =new HashMap<>();
        //映射月结->M  现付-N   火车映射需要根据实际映射
//        mapping.put("月结火车票","M");
//        mapping.put("现付","2");
//        mapping.put("月结","1");
//        mapping.put("现付火车票","N");
        mapping.put("trainPassengerInfos","trainPassengerInfo");
        assert GsonUtil.compareJsonObject(settlementObject.getAsJsonArray("trainSettlementInfos").get(0).getAsJsonObject(),internalQuerySettlement,mapping);
    }

    /**
     *  需要提供测试账号租户 和corpId  相对应起来，这样里面后台逻辑落库数据才能测试  否则会出现签名错误或者book user not found
     *
     */
    @Test(description = "用于真实的供应商的酒店模板结算数据进行推数据落库测试",dataProvider = "TMC")
    public void settlementDataTest3(String supplierName,String supplierCode,String appName,String corpId,String signature,String path) throws HttpStatusException {
        JsonObject vendorData = vendor.getHotelSettlementData(employee,"src/test/resources/data/VendorTrainSettlementData.json",corpId,supplierCode);
        log.info("vendorData:{}",vendorData);
        //推送结算数据
        JsonObject pushData = vendor.pushSettlementData(employee,"hotel",vendorData,appName,corpId,signature);
        log.info("是否成功:{}",pushData);
        //拼装数据（因为有些数据是后台逻辑查询的)
        JsonObject settlementObject = mVendorData.setSettlementData(employee,"hotel",vendorData,supplierName,supplierCode,corpId);
        log.info("拼装的数据:{}",settlementObject);
        //内部接口查询的数据
        SettlementBody settlementBody =SettlementBody.builder()
                .accBalanceBatchNo(vendorData.getAsJsonArray("hotelSettlementList").get(0).getAsJsonObject().get("batchNo").getAsString())
                .orderNo(vendorData.getAsJsonArray("hotelSettlementList").get(0).getAsJsonObject().get("orderNo").getAsString())
                .companyOid("")
                .size(10)
                .page(1)
                .build();
        JsonObject internalQuerySettlement = vendor.internalQuerySettlement(employee,"hotel",settlementBody);
        log.info("查询的结算数据:{}",internalQuerySettlement);
        //映射表
        HashMap<String,String> mapping =new HashMap<>();
        assert GsonUtil.compareJsonObject(settlementObject,internalQuerySettlement,mapping);
    }
}

package com.test.api.testcase.vendor.orderData;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SettlementBody;
import com.hand.basicConstant.OrderSettlementDataPath;
import com.hand.basicConstant.TmcChannel;

import com.hand.utils.GsonUtil;
import com.test.api.method.Vendor;
import com.test.api.method.VendorMethod.VendorData;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2020/11/9import com.hand.utils.GsonUtil;
 * @Version 1.0
 **/
@Slf4j
public class OrderDataTest extends FlightOrderDataTest {


    private Employee employee;
    private Vendor vendor;
    private VendorData mVendorData;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("18123924944") String phoneNumber, @Optional("hly123") String pwd, @Optional("stage") String env){
        employee =getEmployee(phoneNumber,pwd,env);
        vendor =new Vendor();
        mVendorData =new VendorData();
    }

    @DataProvider(name = "TMC")
    public Object[][] tmcData() {
        return new Object[][]{
                {TmcChannel.TIANTAI.getSupplierName(),TmcChannel.TIANTAI.getSupplierCode(),TmcChannel.TIANTAI.getCorpId(),TmcChannel.TIANTAI.getAppName(),TmcChannel.TIANTAI.getSigniture(), OrderSettlementDataPath.orderAirData},
        };
    }

    @DataProvider(name = "TMC-CAR")
    public Object[][] tmcCarData() {
        return new Object[][]{
                {TmcChannel.EHI.getSupplierName(),TmcChannel.EHI.getSupplierCode(),TmcChannel.EHI.getCorpId(),TmcChannel.EHI.getAppName(),TmcChannel.EHI.getSigniture(), OrderSettlementDataPath.settlementUseData},
        };
    }

    @Test(description = "机票订单模板数据测试",dataProvider = "TMC")
    public void OrderTest1(String supplierName,String supplierCode,String corpId,String appName,String signature,String path) throws HttpStatusException {
        JsonObject vendorData = vendor.getFlightOrder(employee,path);
        log.info("vendorData:{}",vendorData);
        //机票订单数据推送
        vendor.pushOrderData(employee,"flight",vendorData,appName,corpId,signature);
        // 组装数据
        JsonObject orderData = mVendorData.setOrderData(employee,vendorData,"flight",supplierName,supplierCode);
        log.info("拼装的数据:{}",orderData);
        //查询数据
        SettlementBody settlementBody = SettlementBody.builder()
                .companyOid("")
                .orderNo(vendorData.getAsJsonObject("airBaseOrder").get("orderNo").getAsString())
                .page(1)
                .size(10)
                .build();
        //查询订单数据
        JsonObject  flightOrderData = vendor.queryOrderData(employee,"flight",settlementBody);
        log.info("flight order Data:{}",flightOrderData);
        //映射数据
        HashMap<String,String> mapping =new HashMap<>();
        mapping.put("S","RS");
        mapping.put("N","国内航班");
        mapping.put("yClassStandardPrice","yclassStandardPrice");
        mapping.put("flight","flightNo");
        mapping.put("employeeId","preEmployeeId");
        assert GsonUtil.compareJsonObject(orderData,flightOrderData,mapping);
    }

    @Test(description = "火车订单模板数据测试",dataProvider = "TMC")
    public void OrderTest2(String supplierName,String supplierCode,String corpId,String appName,String signature,String path) throws HttpStatusException {
        JsonObject vendorData = vendor.getTrainOrder(employee,path);
        log.info("vendorData:{}",vendorData);
//        //火车订单数据推送
        vendor.pushOrderData(employee,"train",vendorData,appName,corpId,signature);
        // 组装数据
        JsonObject orderData = mVendorData.setOrderData(employee,vendorData,"train",supplierName,supplierCode);
        log.info("拼装的数据:{}",orderData);
//        //查询数据
        SettlementBody settlementBody = SettlementBody.builder()
                .companyOid("")
                .orderNo(vendorData.getAsJsonObject("trainOrderBase").get("orderNo").getAsString())
                .page(1)
                .size(10)
                .build();
//        查询订单数据
        JsonObject  flightOrderData = vendor.queryOrderData(employee,"train",settlementBody);
        log.info("train order Data:{}",flightOrderData);
        //映射数据
        HashMap<String,String> mapping =new HashMap<>();
        mapping.put("S","RS");
        mapping.put("N","国内航班");
        mapping.put("yClassStandardPrice","yclassStandardPrice");
        mapping.put("flight","flightNo");
        mapping.put("employeeId","preEmployeeId");
        assert GsonUtil.compareJsonObject(orderData,flightOrderData,mapping);
    }

    @Test(description = "酒店订单模板数据测试",dataProvider = "TMC")
    public void OrderTest3(String supplierName,String supplierCode,String corpId,String appName,String signature,String path) throws HttpStatusException {
        JsonObject vendorData = vendor.getHotelOrder(employee,path);
        log.info("vendorData:{}",vendorData);
        //酒店订单数据推送
        vendor.pushOrderData(employee,"hotel",vendorData,appName,corpId,signature);
        // 组装数据
        JsonObject orderData = mVendorData.setOrderData(employee,vendorData,"hotel",supplierName,supplierCode);
        log.info("拼装的数据:{}",orderData);
        //查询数据
        SettlementBody settlementBody = SettlementBody.builder()
                .companyOid("")
                .orderNo(vendorData.getAsJsonObject("hotelOrderBase").get("orderNo").getAsString())
                .page(1)
                .size(10)
                .build();
        //查询订单数据
        JsonObject  flightOrderData = vendor.queryOrderData(employee,"hotel",settlementBody);
        log.info("hotel order Data:{}",flightOrderData);
        //映射数据
        HashMap<String,String> mapping =new HashMap<>();
        mapping.put("hotelOrderPassengerInfos","hotelPassengerInfo");
        mapping.put("employeeId","preEmployeeId");
        mapping.put("hotelOrderBase","hotelBaseOrder");
        mapping.put("costCenter1","costCenter");
        mapping.put("hotelOrderExceedInfos","hotelExceedInfo");
        mapping.put("originalOrderNum","originalOrderNo");
        assert GsonUtil.compareJsonObject(orderData,flightOrderData,mapping);
    }

    @Test(description = "用车订单模板数据测试", dataProvider = "TMC-CAR")
    public void OrderTest4(String supplierName,String supplierCode,String corpId,String appName,String signature,String path) throws HttpStatusException {
        JsonObject vendorData = vendor.getCarOrder(employee,"src/test/resources/data/VendorTrainSettlementData.json");
        log.info("vendorData:{}",vendorData);
        //用车订单数据推送
        JsonObject carData = vendor.pushOrderData(employee,"car",vendorData,appName,corpId,signature);
        log.info("car data response:{}",carData);
        // 组装数据
        JsonObject orderData = mVendorData.setOrderData(employee,vendorData,"car",supplierName,supplierCode);
        log.info("拼装的数据:{}",orderData);
//        //查询数据
//        SettlementBody settlementBody = SettlementBody.builder()
//                .companyOid("")
//                .orderNo(vendorData.getAsJsonObject("carBaseOrder").get("orderNo").getAsString())
//                .page(1)
//                .size(10)
//                .build();
//        //查询订单数据
//        JsonObject  flightOrderData = vendor.queryOrderData(employee,"car",settlementBody);
//        log.info("car order Data:{}",flightOrderData);
//        //用车映射数据
//        HashMap<String,String> mapping =new HashMap<>();
//        mapping.put("hotelOrderPassengerInfos","hotelPassengerInfo");
//        mapping.put("employeeId","preEmployeeId");
//        mapping.put("hotelOrderBase","hotelBaseOrder");
//        mapping.put("costCenter1","costCenter");
//        mapping.put("hotelOrderExceedInfos","hotelExceedInfo");
//        mapping.put("originalOrderNum","originalOrderNo");
//        assert GsonUtil.compareJsonObject(orderData,flightOrderData,mapping);
    }

}

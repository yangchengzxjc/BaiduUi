package com.test.api.testcase.vendor.orderData;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SettlementBody;
import com.hand.basicObject.supplierObject.airOrderInfo.*;
import com.hand.basicconstant.TmcChannel;
import com.hand.utils.GsonUtil;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.InfraStructure;
import com.test.api.method.Vendor;
import com.test.api.method.VendorMethod.FlightOrder;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2020/9/4
 * @Version 1.0
 **/
@Slf4j
public class FlightOrderDataTest extends BaseTest {

    private Employee employee;
    private Vendor vendor;
    private InfraStructure infraStructure;
    private FlightOrder flightOrder;


    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("yaliang.wang@cimc.com") String phoneNumber, @Optional("111111") String pwd, @Optional("stage") String env){
        employee =getEmployee(phoneNumber,pwd,env);
        vendor =new Vendor();
        infraStructure =new InfraStructure();
        flightOrder =new FlightOrder();
    }

    @DataProvider(name = "TMC")
    public Object[][] tmcData() {
        return new Object[][]{
                {TmcChannel.CIMCC.getSupplierName(),TmcChannel.CIMCC.getSupplierCode(),TmcChannel.CIMCC.getAppName(),TmcChannel.CIMCC.getCorpId(),TmcChannel.CIMCC.getSigniture()},
        };
    }

    @Test(description = "机票订单-单程-公司支付-月结-不改签-不退票",dataProvider = "TMC")
    public void flightOrderDataTest1(String supplierName,String supplierCode,String appName,String corpId,String signature) throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        ArrayList<String> bookerDepartments =new ArrayList<>();
        bookerDepartments.add(GsonUtil.getDepartmentFromPath(employee.getDepartmentName()));
        //机票价格
        BigDecimal ticketPrice =new BigDecimal(1000).setScale(2);
        //燃油费
        BigDecimal oilFee = new BigDecimal(50).setScale(2);
        //基建费
        BigDecimal tax =new BigDecimal(20).setScale(2);
        //服务费
        BigDecimal serverFee = new BigDecimal(20).setScale(2);
        //电子客票号
        String ticketKey = RandomNumber.getTimeNumber(10);
        bookerDepartments.add(employee.getDepartmentName());
        //票号
        String ticketNo = RandomNumber.getTimeNumber(13);
        BigDecimal amount = ticketPrice.add(oilFee).add(tax).add(serverFee);
        String depoCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        //订单基本信息
        AirBaseOrder airBaseOrder = AirBaseOrder.builder()
                .orderType("B")
                .orderNo(orderNo)
                .tripId("")
                .supplierName(supplierName)
//                .supplierCode(supplierCode)
                .approvalCode("TA"+RandomNumber.getTimeNumber(8)+"-1"+"-A")
                .orderStatus("已出票")
                .orderStatusCode("S")
//                .tenantCode(employee.getTenantId())
//                .tenantName(employee.getTenantName())
                .employeeId(employee.getEmployeeID())
                .supplierAccount("")
                .preEmployName(employee.getFullName())
//                .companyOid(employee.getCompanyOID())
//                .companyName(employee.getCompanyName())
//                .companyCode(employee.getCompanyCode())
//                .departmentName(GsonUtil.getDepartmentFromPath(employee.getDepartmentName()))
//                .departmentOid(employee.getDepartmentOID())
//                .departmentCode(depoCode)
                .bookChannel("Online-API")
                .bookType("C")
                .payType("COPAY")
                .createTime(UTCTime.getBeijingTime(-10,0,0))
                .payTime(UTCTime.getBeijingTime(-10,0,0))
                .successTime(UTCTime.getBeijingTime(-10,0,0))
                .flightClass("N")
                .flightWay("S")
                .paymentType("M")
                .accountType("C")
                .costCenter("管理综合部")
                .currency("CNY")
                .amount(amount)
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .remark("备注")
                .build();
        // 机票信息
        AirTicketInfo airTicketInfo = flightOrder.setAirTicketInfo(ticketKey,"1",ticketNo,null,"已使用",ticketPrice,oilFee,tax,serverFee);
        ArrayList<AirTicketInfo> airTicketInfos =new ArrayList<>();
        airTicketInfos.add(airTicketInfo);
        //航程信息
        AirFlightInfo airFlightInfo = flightOrder.setAirFlightInfo(orderNo);
        ArrayList<AirFlightInfo> airFlightInfos =new ArrayList<>();
        airFlightInfos.add(airFlightInfo);
        //乘机人信息
        AirPassengerInfo airPassengerInfo = flightOrder.setAirPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),bookerDepartments,GsonUtil.getDepartmentFromPath(employee.getDepartmentName()),depoCode,employee.getMobile(),employee.getEmail());
        ArrayList<AirPassengerInfo> airPassengerInfos =new ArrayList<>();
        airPassengerInfos.add((airPassengerInfo));
        //行程单打印以及配送信息
        AirTicketPrint airTicketPrint = flightOrder.setAirTicketPrint(ticketKey,ticketNo,employee.getFullName());
        ArrayList<AirTicketPrint> airTicketPrints =new ArrayList<>();
        airTicketPrints.add(airTicketPrint);
        //保险信息
        AirInsurance airInsurance = flightOrder.setAirInsurance(ticketKey,"1",1);
        ArrayList<AirInsurance> airInsurances =new ArrayList<>();
        airInsurances.add(airInsurance);
        AirOrderInfoEntity airOrderInfoEntity =AirOrderInfoEntity.builder()
                .airBaseOrder(airBaseOrder)
                .airTicketInfo(airTicketInfos)
                .airFlightInfo(airFlightInfos)
                .airPassengerInfo(airPassengerInfos)
                .airTicketPrint(airTicketPrints)
                .airInsurance(airInsurances)
                .build();
        //转成jsonobject对象
        JsonObject flightOrderDataObject =new JsonParser().parse(GsonUtil.objectToString(airOrderInfoEntity)).getAsJsonObject();
        //订单推送
       JsonObject flightOrderDataPush = vendor.pushOrderData(employee,"flight",airOrderInfoEntity,appName,corpId,signature);
       SettlementBody settlementBody = SettlementBody.builder()
                .companyOid(employee.getCompanyOID())
                .orderNo(orderNo)
                .page(1)
                .size(10)
                .build();
        //查询订单数据
        JsonObject  flightOrderData = vendor.queryOrderData(employee,"flight",settlementBody);
        log.info("flight order Data:{}",flightOrderData);
        //先对比需要删除的数据
        assert flightOrderData.getAsJsonObject("airBaseOrder").get("flightWay").getAsString().equals(flightOrderDataObject.getAsJsonObject("airBaseOrder").get("flightWay").getAsString());
        assert flightOrderData.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().get("isPolicy").getAsString().equals(flightOrderDataObject.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().get("isPolicy").getAsString());

        //先删除航程类型字段 因为单程映射会重复 删除完后单独比较   协议价  里程数
        flightOrderDataObject.getAsJsonObject("airBaseOrder").remove("flightWay");
        flightOrderDataObject.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().remove("isPolicy");
        //映射数据
        HashMap<String,String> mapping =new HashMap<>();
        mapping.put("S","BS");
        mapping.put("N","国内航班");
        mapping.put("yClassStandardPrice","yclassStandardPrice");
        mapping.put("flight","flightNo");
        mapping.put("employeeId","preEmployeeId");
        assert GsonUtil.compareJsonObject(flightOrderDataObject,flightOrderData,mapping);
        //对比预订人的oid 推送数据未推送此字段单独来比较
        if(flightOrderData.getAsJsonObject("airBaseOrder").get("preEmployeeOid").isJsonNull()){
            assert false;
        }else{
            assert flightOrderData.getAsJsonObject("airBaseOrder").get("preEmployeeOid").getAsString().equals(employee.getUserOID());
        }
        //对比第一个人的oid
        if(flightOrderData.getAsJsonArray("airPassengerInfo").get(0).getAsJsonObject().get("passengerOid").isJsonNull()){
            assert false;
        }else{
            assert flightOrderData.getAsJsonArray("airPassengerInfo").get(0).getAsJsonObject().get("passengerOid").getAsString().equals(employee.getUserOID());
        }

    }

    @Test(description = "机票订单-单程-因私-不改签-不退票",dataProvider = "TMC")
    public void flightOrderDataTest2(String supplierName,String supplierCode,String appName,String corpId,String signature) throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        ArrayList<String> bookerDepartments =new ArrayList<>();
        bookerDepartments.add(GsonUtil.getDepartmentFromPath(employee.getDepartmentName()));
        //机票价格
        BigDecimal ticketPrice =new BigDecimal(1000).setScale(2);
        //燃油费
        BigDecimal oilFee = new BigDecimal(50).setScale(2);
        //基建费
        BigDecimal tax =new BigDecimal(20).setScale(2);
        //服务费
        BigDecimal serverFee = new BigDecimal(20).setScale(2);
        //电子客票号
        String ticketKey = RandomNumber.getTimeNumber(10);
        bookerDepartments.add(employee.getDepartmentName());
        //票号
        String ticketNo = RandomNumber.getTimeNumber(13);
        BigDecimal amount = ticketPrice.add(oilFee).add(tax).add(serverFee);
        String depoCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        //订单基本信息
        AirBaseOrder airBaseOrder = AirBaseOrder.builder()
                .orderType("C")
                .orderNo(orderNo)
                .tripId("")
                .supplierName(supplierName)
                .supplierCode(supplierCode)
                .approvalCode("TA"+RandomNumber.getTimeNumber(8)+"-1"+"-A")
                .orderStatus("已出票")
                .orderStatusCode("S")
                .tenantCode(employee.getTenantId())
                .tenantName(employee.getTenantName())
                .employeeId(employee.getEmployeeID())
                .supplierAccount("")
                .preEmployName(employee.getFullName())
                .companyOid(employee.getCompanyOID())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(GsonUtil.getDepartmentFromPath(employee.getDepartmentName()))
                .departmentCode(depoCode)
                .departmentOid(employee.getDepartmentOID())
                .bookChannel("Online-API")
                .bookType("P")
                .payType("ALIPAY")
                .createTime(UTCTime.getBeijingTime(-10,0,0))
                .payTime(UTCTime.getBeijingTime(-10,0,0))
                .successTime(UTCTime.getBeijingTime(-10,0,0))
                .flightClass("N")
                .flightWay("S")
                .paymentType("N")
                .accountType("P")
                .costCenter("管理综合部")
                .currency("CNY")
                .amount(amount)
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .remark("备注")
                .build();
        // 机票信息
        AirTicketInfo airTicketInfo = flightOrder.setAirTicketInfo(ticketKey,"1",ticketNo,null,"已使用",ticketPrice,oilFee,tax,serverFee);
        ArrayList<AirTicketInfo> airTicketInfos =new ArrayList<>();
        airTicketInfos.add(airTicketInfo);
        //航程信息
        AirFlightInfo airFlightInfo = flightOrder.setAirFlightInfo(orderNo);
        ArrayList<AirFlightInfo> airFlightInfos =new ArrayList<>();
        airFlightInfos.add(airFlightInfo);
        //乘机人信息
        AirPassengerInfo airPassengerInfo = flightOrder.setAirPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),bookerDepartments,GsonUtil.getDepartmentFromPath(employee.getDepartmentName()),depoCode,employee.getPhoneNumber(),employee.getEmail());
        ArrayList<AirPassengerInfo> airPassengerInfos =new ArrayList<>();
        airPassengerInfos.add((airPassengerInfo));
        //行程单打印以及配送信息
        AirTicketPrint airTicketPrint = flightOrder.setAirTicketPrint(ticketKey,ticketNo,employee.getFullName());
        ArrayList<AirTicketPrint> airTicketPrints =new ArrayList<>();
        airTicketPrints.add(airTicketPrint);
        //保险信息
        AirInsurance airInsurance = flightOrder.setAirInsurance(employee.getFullName(),"1",1);
        ArrayList<AirInsurance> airInsurances =new ArrayList<>();
        airInsurances.add(airInsurance);
        AirOrderInfoEntity airOrderInfoEntity =AirOrderInfoEntity.builder()
                .airBaseOrder(airBaseOrder)
                .airTicketInfo(airTicketInfos)
                .airFlightInfo(airFlightInfos)
                .airPassengerInfo(airPassengerInfos)
                .airTicketPrint(airTicketPrints)
                .airInsurance(airInsurances)
                .build();
        //推送的数据封装成一个json字符串
        String hotelOrderData =GsonUtil.objectToString(airOrderInfoEntity);
        //转成jsonobject对象
        JsonObject flightOrderDataObject =new JsonParser().parse(hotelOrderData).getAsJsonObject();
        //订单推送
        vendor.pushOrderData(employee,"flight",airOrderInfoEntity,appName,corpId,signature);
        SettlementBody settlementBody = SettlementBody.builder()
                .companyOid(employee.getCompanyOID())
                .orderNo(orderNo)
                .page(1)
                .size(10)
                .build();
        //查询订单数据
        JsonObject  flightOrderData = vendor.queryOrderData(employee,"flight",settlementBody);
        log.info("flight order Data:{}",flightOrderData);
        //先对比需要删除的数据
        assert flightOrderData.getAsJsonObject("airBaseOrder").get("flightWay").getAsString().equals(flightOrderDataObject.getAsJsonObject("airBaseOrder").get("flightWay").getAsString());
        assert flightOrderData.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().get("isPolicy").getAsString().equals(flightOrderDataObject.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().get("isPolicy").getAsString());
        //先删除航程类型字段 因为单程映射会重复 删除完后单独比较   协议价  里程数
        flightOrderDataObject.getAsJsonObject("airBaseOrder").remove("flightWay");
        flightOrderDataObject.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().remove("isPolicy");
        //映射数据
        HashMap<String,String> mapping =new HashMap<>();
        mapping.put("S","BS");
        mapping.put("N","国内航班");
        mapping.put("yClassStandardPrice","yclassStandardPrice");
        mapping.put("flight","flightNo");
        mapping.put("employeeId","preEmployeeId");
        assert GsonUtil.compareJsonObject(flightOrderDataObject,flightOrderData,mapping);
        //对比预订人的oid
        if(flightOrderData.getAsJsonObject("airBaseOrder").get("preEmployeeOid").isJsonNull()){
            assert false;
        }else{
            assert flightOrderData.getAsJsonObject("airBaseOrder").get("preEmployeeOid").getAsString().equals(employee.getUserOID());
        }
        if(flightOrderData.getAsJsonArray("airPassengerInfo").get(0).getAsJsonObject().get("passengerOid").isJsonNull()){
            assert false;
        }else{
            assert flightOrderData.getAsJsonArray("airPassengerInfo").get(0).getAsJsonObject().get("passengerOid").getAsString().equals(employee.getUserOID());
        }
    }

    @Test(description = "机票订单-单程-公司支付-月结-一人退票",dataProvider = "TMC")
    public void flightOrderDataTest4(String supplierName,String supplierCode,String appName,String corpId,String signature) throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber(14);
        ArrayList<String> bookerDepartments =new ArrayList<>();
        bookerDepartments.add(GsonUtil.getDepartmentFromPath(employee.getDepartmentName()));
        //机票价格
        BigDecimal ticketPrice =new BigDecimal(1000).setScale(2);
        //燃油费
        BigDecimal oilFee = new BigDecimal(50).setScale(2);
        //基建费
        BigDecimal tax =new BigDecimal(20).setScale(2);
        //服务费
        BigDecimal serverFee = new BigDecimal(20).setScale(2);
        //电子客票号
        String ticketKey = RandomNumber.getTimeNumber(10);
        bookerDepartments.add(employee.getDepartmentName());
        //票号
        String ticketNo = RandomNumber.getTimeNumber(13);
        String depoCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        BigDecimal amount = ticketPrice.add(oilFee).add(tax).add(serverFee);
        //订单基本信息
        AirBaseOrder airBaseOrder = AirBaseOrder.builder()
                .orderType("R")
                .orderNo(orderNo)
                .tripId("")
                .originalOrderNo(RandomNumber.getTimeNumber(14))
                .supplierName(supplierName)
                .supplierCode(supplierCode)
                .approvalCode("TA"+RandomNumber.getTimeNumber(8)+"-1"+"-A")
                .orderStatus("退票成功")
                .orderStatusCode("S")
                .tenantCode(employee.getTenantId())
                .tenantName(employee.getTenantName())
                .employeeId(employee.getEmployeeID())
                .supplierAccount("")
                .preEmployName(employee.getFullName())
                .companyOid(employee.getCompanyOID())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(GsonUtil.getDepartmentFromPath(employee.getDepartmentName()))
                .departmentCode(depoCode)
                .departmentOid(employee.getDepartmentOID())
                .bookChannel("Online-API")
                .bookType("C")
                .payType("COPAY")
                .createTime(UTCTime.getBeijingTime(-10,0,0))
                .payTime(UTCTime.getBeijingTime(-10,0,0))
                .successTime(UTCTime.getBeijingTime(-10,0,0))
                .flightClass("N")
                .flightWay("S")
                .paymentType("M")
                .accountType("C")
                .costCenter("管理综合部")
                .currency("CNY")
                .amount(amount)
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .remark("备注")
                .build();
        // 机票信息
        AirTicketInfo airTicketInfo = flightOrder.setAirTicketInfo(ticketKey,"1",ticketNo,null,"已退票",ticketPrice,oilFee,tax,serverFee);
        ArrayList<AirTicketInfo> airTicketInfos =new ArrayList<>();
        airTicketInfos.add(airTicketInfo);
        //航程信息
        AirFlightInfo airFlightInfo = flightOrder.setAirFlightInfo(orderNo);
        ArrayList<AirFlightInfo> airFlightInfos =new ArrayList<>();
        airFlightInfos.add(airFlightInfo);
        //乘机人信息
        AirPassengerInfo airPassengerInfo = flightOrder.setAirPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),bookerDepartments,GsonUtil.getDepartmentFromPath(employee.getDepartmentName()),depoCode,employee.getPhoneNumber(),employee.getEmail());
        ArrayList<AirPassengerInfo> airPassengerInfos =new ArrayList<>();
        airPassengerInfos.add((airPassengerInfo));
        // 退票
        AirRefundInfo airRefundInfo =flightOrder.setAirRefundInfo(orderNo);
        ArrayList<AirRefundInfo> airRefundInfos = new ArrayList<>();
        airRefundInfos.add(airRefundInfo);
        //行程单打印以及配送信息
        AirTicketPrint airTicketPrint = flightOrder.setAirTicketPrint(ticketKey,ticketNo,employee.getFullName());
        ArrayList<AirTicketPrint> airTicketPrints =new ArrayList<>();
        airTicketPrints.add(airTicketPrint);
        //保险信息
        AirInsurance airInsurance = flightOrder.setAirInsurance(employee.getFullName(),"1",1);
        ArrayList<AirInsurance> airInsurances =new ArrayList<>();
        airInsurances.add(airInsurance);

        AirOrderInfoEntity airOrderInfoEntity =AirOrderInfoEntity.builder()
                .airBaseOrder(airBaseOrder)
                .airTicketInfo(airTicketInfos)
                .airFlightInfo(airFlightInfos)
                .airPassengerInfo(airPassengerInfos)
                .airTicketPrint(airTicketPrints)
                .airInsurance(airInsurances)
                .airRefundInfo(airRefundInfos)
                .build();
        //推送的数据封装成一个json字符串
        String hotelOrderData =GsonUtil.objectToString(airOrderInfoEntity);
        //转成jsonobject对象
        JsonObject flightOrderDataObject =new JsonParser().parse(hotelOrderData).getAsJsonObject();
        //订单推送
        vendor.pushOrderData(employee,"flight",airOrderInfoEntity,appName,corpId,signature);
        SettlementBody settlementBody = SettlementBody.builder()
                .companyOid(employee.getCompanyOID())
                .orderNo(orderNo)
                .page(1)
                .size(10)
                .build();
        //查询订单数据
        JsonObject  flightOrderData = vendor.queryOrderData(employee,"flight",settlementBody);
        log.info("flight order Data:{}",flightOrderData);
        //先对比需要删除的数据
        assert flightOrderData.getAsJsonObject("airBaseOrder").get("flightWay").getAsString().equals(flightOrderDataObject.getAsJsonObject("airBaseOrder").get("flightWay").getAsString());
        assert flightOrderData.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().get("isPolicy").getAsString().equals(flightOrderDataObject.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().get("isPolicy").getAsString());
        //先删除航程类型字段 因为单程映射会重复 删除完后单独比较   协议价  里程数
        flightOrderDataObject.getAsJsonObject("airBaseOrder").remove("flightWay");
        flightOrderDataObject.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().remove("isPolicy");
        //映射数据
        HashMap<String,String> mapping =new HashMap<>();
        mapping.put("S","RS");
        mapping.put("N","国内航班");
        mapping.put("yClassStandardPrice","yclassStandardPrice");
        mapping.put("flight","flightNo");
        mapping.put("employeeId","preEmployeeId");
        assert GsonUtil.compareJsonObject(flightOrderDataObject,flightOrderData,mapping);
        //对比预订人的oid
        if(flightOrderData.getAsJsonObject("airBaseOrder").get("preEmployeeOid").isJsonNull()){
            assert false;
        }else{
            assert flightOrderData.getAsJsonObject("airBaseOrder").get("preEmployeeOid").getAsString().equals(employee.getUserOID());
        }
        if(flightOrderData.getAsJsonArray("airPassengerInfo").get(0).getAsJsonObject().get("passengerOid").isJsonNull()){
            assert false;
        }else{
            assert flightOrderData.getAsJsonArray("airPassengerInfo").get(0).getAsJsonObject().get("passengerOid").getAsString().equals(employee.getUserOID());
        }
    }

    @Test(description = "机票订单-单程-公司支付-月结-一人改签",dataProvider = "TMC")
    public void flightOrderDataTest3(String supplierName,String supplierCode,String appName,String corpId,String signature) throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber(14);
        ArrayList<String> bookerDepartments =new ArrayList<>();
        bookerDepartments.add(GsonUtil.getDepartmentFromPath(employee.getDepartmentName()));
        //机票价格
        BigDecimal ticketPrice =new BigDecimal(1000).setScale(2);
        //燃油费
        BigDecimal oilFee = new BigDecimal(50).setScale(2);
        //基建费
        BigDecimal tax =new BigDecimal(20).setScale(2);
        //服务费
        BigDecimal serverFee = new BigDecimal(20).setScale(2);
        //电子客票号
        String ticketKey = RandomNumber.getTimeNumber(10);
        bookerDepartments.add(employee.getDepartmentName());
        //票号
        String ticketNo = RandomNumber.getTimeNumber(13);
        //改签前票号
        String preTicketNum = RandomNumber.getTimeNumber(13);
        String depoCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        BigDecimal amount = ticketPrice.add(oilFee).add(tax).add(serverFee);
        //订单基本信息
        AirBaseOrder airBaseOrder = AirBaseOrder.builder()
                .orderType("C")
                .orderNo(orderNo)
                .tripId("")
                .originalOrderNo(RandomNumber.getTimeNumber(14))
                .supplierName(supplierName)
                .supplierCode(supplierCode)
                .approvalCode("TA"+RandomNumber.getTimeNumber(8)+"-1"+"-A")
                .orderStatus("改签成功")
                .orderStatusCode("S")
                .tenantCode(employee.getTenantId())
                .tenantName(employee.getTenantName())
                .employeeId(employee.getEmployeeID())
                .supplierAccount("")
                .preEmployName(employee.getFullName())
                .companyOid(employee.getCompanyOID())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(GsonUtil.getDepartmentFromPath(employee.getDepartmentName()))
                .departmentCode(depoCode)
                .departmentOid(employee.getDepartmentOID())
                .bookChannel("Online-API")
                .bookType("C")
                .payType("COPAY")
                .createTime(UTCTime.getBeijingTime(-10,0,0))
                .payTime(UTCTime.getBeijingTime(-10,0,0))
                .successTime(UTCTime.getBeijingTime(-10,0,0))
                .flightClass("N")
                .flightWay("S")
                .paymentType("M")
                .accountType("C")
                .costCenter("管理综合部")
                .currency("CNY")
                .amount(amount)
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .remark("备注")
                .build();
        // 机票信息
        AirTicketInfo airTicketInfo = flightOrder.setAirTicketInfo(ticketKey,"1",ticketNo,preTicketNum,"已改签",ticketPrice,oilFee,tax,serverFee);
        ArrayList<AirTicketInfo> airTicketInfos =new ArrayList<>();
        airTicketInfos.add(airTicketInfo);
        //航程信息
        AirFlightInfo airFlightInfo = flightOrder.setAirFlightInfo(orderNo);
        ArrayList<AirFlightInfo> airFlightInfos =new ArrayList<>();
        airFlightInfos.add(airFlightInfo);
        //乘机人信息
        AirPassengerInfo airPassengerInfo = flightOrder.setAirPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),bookerDepartments,GsonUtil.getDepartmentFromPath(employee.getDepartmentName()),depoCode,employee.getPhoneNumber(),employee.getEmail());
        ArrayList<AirPassengerInfo> airPassengerInfos =new ArrayList<>();
        airPassengerInfos.add((airPassengerInfo));
        // 改签
        AirChangeInfo airChangeInfo =flightOrder.setAirChangeInfo(orderNo);
        ArrayList<AirChangeInfo> airChangeInfos =new ArrayList<>();
        airChangeInfos.add(airChangeInfo);
        //行程单打印以及配送信息
        AirTicketPrint airTicketPrint = flightOrder.setAirTicketPrint(ticketKey,ticketNo,employee.getFullName());
        ArrayList<AirTicketPrint> airTicketPrints =new ArrayList<>();
        airTicketPrints.add(airTicketPrint);
        //保险信息
        AirInsurance airInsurance = flightOrder.setAirInsurance(employee.getFullName(),"1",1);
        ArrayList<AirInsurance> airInsurances =new ArrayList<>();
        airInsurances.add(airInsurance);
        AirOrderInfoEntity airOrderInfoEntity =AirOrderInfoEntity.builder()
                .airBaseOrder(airBaseOrder)
                .airTicketInfo(airTicketInfos)
                .airFlightInfo(airFlightInfos)
                .airPassengerInfo(airPassengerInfos)
                .airTicketPrint(airTicketPrints)
                .airInsurance(airInsurances)
                .airChangeInfo(airChangeInfos)
                .build();
        //推送的数据封装成一个json字符串
        String hotelOrderData =GsonUtil.objectToString(airOrderInfoEntity);
        //转成jsonobject对象
        JsonObject flightOrderDataObject =new JsonParser().parse(hotelOrderData).getAsJsonObject();
        //订单推送
        vendor.pushOrderData(employee,"flight",airOrderInfoEntity,appName,corpId,signature);
        SettlementBody settlementBody = SettlementBody.builder()
                .companyOid(employee.getCompanyOID())
                .orderNo(orderNo)
                .page(1)
                .size(10)
                .build();
        //查询订单数据
        JsonObject  flightOrderData = vendor.queryOrderData(employee,"flight",settlementBody);
        log.info("flight order Data:{}",flightOrderData);
        //先对比需要删除的数据
        assert flightOrderData.getAsJsonObject("airBaseOrder").get("flightWay").getAsString().equals(flightOrderDataObject.getAsJsonObject("airBaseOrder").get("flightWay").getAsString());
        assert flightOrderData.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().get("isPolicy").getAsString().equals(flightOrderDataObject.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().get("isPolicy").getAsString());
        //先删除航程类型字段 因为单程映射会重复 删除完后单独比较   协议价  里程数
        flightOrderDataObject.getAsJsonObject("airBaseOrder").remove("flightWay");
        flightOrderDataObject.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().remove("isPolicy");
        //映射数据
        HashMap<String,String> mapping =new HashMap<>();
        mapping.put("S","CS");
        mapping.put("N","国内航班");
        mapping.put("yClassStandardPrice","yclassStandardPrice");
        mapping.put("flight","flightNo");
        mapping.put("employeeId","preEmployeeId");
        assert GsonUtil.compareJsonObject(flightOrderDataObject,flightOrderData,mapping);
        //对比预订人的oid
        if(flightOrderData.getAsJsonObject("airBaseOrder").get("preEmployeeOid").isJsonNull()){
            assert false;
        }else{
            assert flightOrderData.getAsJsonObject("airBaseOrder").get("preEmployeeOid").getAsString().equals(employee.getUserOID());
        }
        if(flightOrderData.getAsJsonArray("airPassengerInfo").get(0).getAsJsonObject().get("passengerOid").isJsonNull()){
            assert false;
        }else{
            assert flightOrderData.getAsJsonArray("airPassengerInfo").get(0).getAsJsonObject().get("passengerOid").getAsString().equals(employee.getUserOID());
        }
    }

    @Test(description = "机票订单-多人订票-公司支付-月结-不改签-不退票",dataProvider = "TMC")
    public void flightOrderDataTest5(String supplierName,String supplierCode,String appName,String corpId,String signature) throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        ArrayList<String> bookerDepartments =new ArrayList<>();
        bookerDepartments.add(GsonUtil.getDepartmentFromPath(employee.getDepartmentName()));
        //获取一个员工的信息
        JsonObject employeeInfo1 = infraStructure.getUserDetail(employee,"01399315");
        JsonObject employeeInfo2 = infraStructure.getUserDetail(employee,"01363468");
        String deptCode1 = infraStructure.getDeptCode(employee,employeeInfo1.get("departmentOID").getAsString());
        String deptCode2 = infraStructure.getDeptCode(employee,employeeInfo2.get("departmentOID").getAsString());
        //机票价格
        BigDecimal ticketPrice =new BigDecimal(1000).setScale(2);
        //燃油费
        BigDecimal oilFee = new BigDecimal(50).setScale(2);
        //基建费
        BigDecimal tax =new BigDecimal(20).setScale(2);
        //服务费
        BigDecimal serverFee = new BigDecimal(20).setScale(2);
        bookerDepartments.add(employee.getDepartmentName());
        //票号
        String ticketNo1 = RandomNumber.getTimeNumber(13);
        String ticketNo2 = RandomNumber.getTimeNumber(13);
        String ticketNo3 = RandomNumber.getTimeNumber(13);
        String depoCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        BigDecimal amount = ticketPrice.add(oilFee).add(tax).add(serverFee);
        BigDecimal toalAmount = amount.multiply(new BigDecimal(3)).setScale(2);
        //订单基本信息
        AirBaseOrder airBaseOrder = AirBaseOrder.builder()
                .orderType("B")
                .orderNo(orderNo)
                .tripId("")
                .supplierName(supplierName)
                .supplierCode(supplierCode)
                .approvalCode("TA"+RandomNumber.getTimeNumber(8)+"-1"+"-A")
                .orderStatus("已出票")
                .orderStatusCode("S")
                .tenantCode(employee.getTenantId())
                .tenantName(employee.getTenantName())
                .employeeId(employee.getEmployeeID())
                .supplierAccount("")
                .preEmployName(employee.getFullName())
                .companyOid(employee.getCompanyOID())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(GsonUtil.getDepartmentFromPath(employee.getDepartmentName()))
                .departmentCode(depoCode)
                .departmentOid(employee.getDepartmentOID())
                .bookChannel("Online-API")
                .bookType("C")
                .payType("COPAY")
                .createTime(UTCTime.getBeijingTime(-10,0,0))
                .payTime(UTCTime.getBeijingTime(-10,0,0))
                .successTime(UTCTime.getBeijingTime(-10,0,0))
                .flightClass("N")
                .flightWay("S")
                .paymentType("M")
                .accountType("C")
                .costCenter("管理综合部")
                .currency("CNY")
                .amount(toalAmount)
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .remark("备注")
                .build();
        // 机票信息
        AirTicketInfo airTicketInfo3 = flightOrder.setAirTicketInfo(ticketNo3,"3",ticketNo3,null,"已使用",ticketPrice,oilFee,tax,serverFee);
        AirTicketInfo airTicketInfo2 = flightOrder.setAirTicketInfo(ticketNo2,"2",ticketNo2,null,"已使用",ticketPrice,oilFee,tax,serverFee);
        AirTicketInfo airTicketInfo1 = flightOrder.setAirTicketInfo(ticketNo1,"1",ticketNo1,null,"已使用",ticketPrice,oilFee,tax,serverFee);

        ArrayList<AirTicketInfo> airTicketInfos =new ArrayList<>();
        airTicketInfos.add(airTicketInfo1);
        airTicketInfos.add(airTicketInfo2);
        airTicketInfos.add(airTicketInfo3);
        //航程信息
        AirFlightInfo airFlightInfo = flightOrder.setAirFlightInfo(orderNo);
        ArrayList<AirFlightInfo> airFlightInfos =new ArrayList<>();
        airFlightInfos.add(airFlightInfo);
        //乘机人信息
        AirPassengerInfo airPassengerInfo1 = flightOrder.setAirPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),bookerDepartments,GsonUtil.getDepartmentFromPath(employee.getDepartmentName()),depoCode,employee.getPhoneNumber(),employee.getEmail());
        AirPassengerInfo airPassengerInfo2 = flightOrder.setAirPassengerInfo(orderNo,"2","I",employeeInfo1.get("fullName").getAsString(),employeeInfo1.get("employeeID").getAsString(),bookerDepartments,GsonUtil.getDepartmentFromPath(employeeInfo1.get("departmentName").getAsString()),deptCode1,employeeInfo1.get("mobile").getAsString(),employeeInfo1.get("email").getAsString());
        AirPassengerInfo airPassengerInfo3 = flightOrder.setAirPassengerInfo(orderNo,"3","I",employeeInfo2.get("fullName").getAsString(),employeeInfo2.get("employeeID").getAsString(),bookerDepartments,GsonUtil.getDepartmentFromPath(employeeInfo2.get("departmentName").getAsString()),deptCode2,employeeInfo2.get("mobile").getAsString(),employeeInfo2.get("email").getAsString());
        ArrayList<AirPassengerInfo> airPassengerInfos =new ArrayList<>();
        airPassengerInfos.add((airPassengerInfo1));
        airPassengerInfos.add((airPassengerInfo2));
        airPassengerInfos.add((airPassengerInfo3));
        //行程单打印以及配送信息
        AirTicketPrint airTicketPrint1 = flightOrder.setAirTicketPrint(ticketNo1,ticketNo1,employee.getFullName());
        AirTicketPrint airTicketPrint2= flightOrder.setAirTicketPrint(ticketNo2,ticketNo2,employeeInfo1.get("fullName").getAsString());
        AirTicketPrint airTicketPrint3= flightOrder.setAirTicketPrint(ticketNo3,ticketNo3,employeeInfo2.get("fullName").getAsString());
        ArrayList<AirTicketPrint> airTicketPrints =new ArrayList<>();
        airTicketPrints.add(airTicketPrint1);
        airTicketPrints.add(airTicketPrint2);
        airTicketPrints.add(airTicketPrint3);
        //保险信息
        AirInsurance airInsurance1 = flightOrder.setAirInsurance(employee.getFullName(),"1",1);
        AirInsurance airInsurance2 = flightOrder.setAirInsurance(ticketNo2,"1",1);
        AirInsurance airInsurance3 = flightOrder.setAirInsurance(ticketNo3,"1",1);
        ArrayList<AirInsurance> airInsurances =new ArrayList<>();
        airInsurances.add(airInsurance1);
        airInsurances.add(airInsurance2);
        airInsurances.add(airInsurance3);
        AirOrderInfoEntity airOrderInfoEntity =AirOrderInfoEntity.builder()
                .airBaseOrder(airBaseOrder)
                .airTicketInfo(airTicketInfos)
                .airFlightInfo(airFlightInfos)
                .airPassengerInfo(airPassengerInfos)
                .airTicketPrint(airTicketPrints)
                .airInsurance(airInsurances)
                .build();
        //推送的数据封装成一个json字符串
        String hotelOrderData =GsonUtil.objectToString(airOrderInfoEntity);
        //转成jsonobject对象
        JsonObject flightOrderDataObject =new JsonParser().parse(hotelOrderData).getAsJsonObject();
        //订单推送
        vendor.pushOrderData(employee,"flight",airOrderInfoEntity,appName,corpId,signature);
        SettlementBody settlementBody = SettlementBody.builder()
                .companyOid(employee.getCompanyOID())
                .orderNo(orderNo)
                .page(1)
                .size(10)
                .build();
        //查询订单数据
        JsonObject  flightOrderData = vendor.queryOrderData(employee,"flight",settlementBody);
        log.info("flight order Data:{}",flightOrderData);
        //先对比需要删除的数据
        assert flightOrderData.getAsJsonObject("airBaseOrder").get("flightWay").getAsString().equals(flightOrderDataObject.getAsJsonObject("airBaseOrder").get("flightWay").getAsString());
        assert flightOrderData.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().get("isPolicy").getAsString().equals(flightOrderDataObject.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().get("isPolicy").getAsString());
        //先删除航程类型字段 因为单程映射会重复 删除完后单独比较   协议价  里程数
        flightOrderDataObject.getAsJsonObject("airBaseOrder").remove("flightWay");
        flightOrderDataObject.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().remove("isPolicy");
        //映射数据
        HashMap<String,String> mapping =new HashMap<>();
        mapping.put("S","BS");
        mapping.put("N","国内航班");
        mapping.put("yClassStandardPrice","yclassStandardPrice");
        mapping.put("flight","flightNo");
        mapping.put("employeeId","preEmployeeId");
        assert GsonUtil.compareJsonObject(flightOrderDataObject,flightOrderData,mapping);
        if(flightOrderData.getAsJsonObject("airBaseOrder").get("preEmployeeOid").isJsonNull()){
            assert false;
        }else{
            assert flightOrderData.getAsJsonObject("airBaseOrder").get("preEmployeeOid").getAsString().equals(employee.getUserOID());
        }
        //对比乘客中的一个人oid
        if(flightOrderData.getAsJsonArray("airPassengerInfo").get(0).getAsJsonObject().get("passengerOid").isJsonNull()){
            assert false;
        }else{
            assert flightOrderData.getAsJsonArray("airPassengerInfo").get(0).getAsJsonObject().get("passengerOid").getAsString().equals(employee.getUserOID());
        }
        //对比乘客中的第二个人
        if(flightOrderData.getAsJsonArray("airPassengerInfo").get(1).getAsJsonObject().get("passengerOid").isJsonNull()){
            assert false;
        }else{
            assert flightOrderData.getAsJsonArray("airPassengerInfo").get(1).getAsJsonObject().get("passengerOid").getAsString().equals(employeeInfo1.get("userOID").getAsString());
        }
        //对比乘客中的第三个人
        if(flightOrderData.getAsJsonArray("airPassengerInfo").get(1).getAsJsonObject().get("passengerOid").isJsonNull()){
            assert false;
        }else{
            assert flightOrderData.getAsJsonArray("airPassengerInfo").get(1).getAsJsonObject().get("passengerOid").getAsString().equals(employeeInfo2.get("userOID").getAsString());
        }
    }

    @Test(description = "机票订单-单程-公司支付-月结-不改签-不退票-超标",dataProvider = "TMC")
    public void flightOrderDataTest6(String supplierName,String supplierCode,String appName,String corpId,String signature) throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        ArrayList<String> bookerDepartments =new ArrayList<>();
        bookerDepartments.add(GsonUtil.getDepartmentFromPath(employee.getDepartmentName()));
        //机票价格
        BigDecimal ticketPrice =new BigDecimal(1000).setScale(2);
        //燃油费
        BigDecimal oilFee = new BigDecimal(50).setScale(2);
        //基建费
        BigDecimal tax =new BigDecimal(20).setScale(2);
        //服务费
        BigDecimal serverFee = new BigDecimal(20).setScale(2);
        //电子客票号
        String ticketKey = RandomNumber.getTimeNumber(10);
        bookerDepartments.add(employee.getDepartmentName());
        //票号
        String ticketNo = RandomNumber.getTimeNumber(13);
        BigDecimal amount = ticketPrice.add(oilFee).add(tax).add(serverFee);
        String depoCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        //订单基本信息
        AirBaseOrder airBaseOrder = AirBaseOrder.builder()
                .orderType("B")
                .orderNo(orderNo)
                .tripId("")
                .supplierName(supplierName)
                .supplierCode(supplierCode)
                .approvalCode("TA"+RandomNumber.getTimeNumber(8)+"-1"+"-A")
                .orderStatus("已出票")
                .orderStatusCode("S")
                .tenantCode(employee.getTenantId())
                .tenantName(employee.getTenantName())
                .employeeId(employee.getEmployeeID())
                .supplierAccount("")
                .preEmployName(employee.getFullName())
                .companyOid(employee.getCompanyOID())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(GsonUtil.getDepartmentFromPath(employee.getDepartmentName()))
                .departmentOid(employee.getDepartmentOID())
                .departmentCode(depoCode)
                .bookChannel("Online-API")
                .bookType("C")
                .payType("COPAY")
                .createTime(UTCTime.getBeijingTime(-10,0,0))
                .payTime(UTCTime.getBeijingTime(-10,0,0))
                .successTime(UTCTime.getBeijingTime(-10,0,0))
                .flightClass("N")
                .flightWay("S")
                .paymentType("M")
                .accountType("C")
                .costCenter("管理综合部")
                .currency("CNY")
                .amount(amount)
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .remark("备注")
                .build();
        // 机票信息
        AirTicketInfo airTicketInfo = flightOrder.setAirTicketInfo(ticketKey,"1",ticketNo,null,"已使用",ticketPrice,oilFee,tax,serverFee);
        ArrayList<AirTicketInfo> airTicketInfos =new ArrayList<>();
        airTicketInfos.add(airTicketInfo);
        //航程信息
        AirFlightInfo airFlightInfo = flightOrder.setAirFlightInfo(orderNo);
        ArrayList<AirFlightInfo> airFlightInfos =new ArrayList<>();
        airFlightInfos.add(airFlightInfo);
        //乘机人信息
        AirPassengerInfo airPassengerInfo = flightOrder.setAirPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),bookerDepartments,GsonUtil.getDepartmentFromPath(employee.getDepartmentName()),depoCode,employee.getMobile(),employee.getEmail());
        ArrayList<AirPassengerInfo> airPassengerInfos =new ArrayList<>();
        airPassengerInfos.add((airPassengerInfo));
        //行程单打印以及配送信息
        AirTicketPrint airTicketPrint = flightOrder.setAirTicketPrint(ticketKey,ticketNo,employee.getFullName());
        ArrayList<AirTicketPrint> airTicketPrints =new ArrayList<>();
        airTicketPrints.add(airTicketPrint);
        //保险信息
        AirInsurance airInsurance = flightOrder.setAirInsurance(ticketKey,"1",1);
        ArrayList<AirInsurance> airInsurances =new ArrayList<>();
        airInsurances.add(airInsurance);
        //超标信息
        AirExceedInfo airExceedInfo = flightOrder.setAirExceedInfo(ticketKey,ticketNo);
        ArrayList<AirExceedInfo> airExceedInfos =new ArrayList<>();
        airExceedInfos.add(airExceedInfo);

        AirOrderInfoEntity airOrderInfoEntity =AirOrderInfoEntity.builder()
                .airBaseOrder(airBaseOrder)
                .airTicketInfo(airTicketInfos)
                .airFlightInfo(airFlightInfos)
                .airPassengerInfo(airPassengerInfos)
                .airTicketPrint(airTicketPrints)
                .airInsurance(airInsurances)
                .airExceedInfo(airExceedInfos)
                .build();
        //转成jsonobject对象
        JsonObject flightOrderDataObject =new JsonParser().parse(GsonUtil.objectToString(airOrderInfoEntity)).getAsJsonObject();
        //订单推送
        JsonObject flightOrderDataPush = vendor.pushOrderData(employee,"flight",airOrderInfoEntity,appName,corpId,signature);
        SettlementBody settlementBody = SettlementBody.builder()
                .companyOid(employee.getCompanyOID())
                .orderNo(orderNo)
                .page(1)
                .size(10)
                .build();
        //查询订单数据
        JsonObject  flightOrderData = vendor.queryOrderData(employee,"flight",settlementBody);
        log.info("flight order Data:{}",flightOrderData);
        //先对比需要删除的数据
        assert flightOrderData.getAsJsonObject("airBaseOrder").get("flightWay").getAsString().equals(flightOrderDataObject.getAsJsonObject("airBaseOrder").get("flightWay").getAsString());
        assert flightOrderData.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().get("isPolicy").getAsString().equals(flightOrderDataObject.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().get("isPolicy").getAsString());

        //先删除航程类型字段 因为单程映射会重复 删除完后单独比较   协议价  里程数
        flightOrderDataObject.getAsJsonObject("airBaseOrder").remove("flightWay");
        flightOrderDataObject.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().remove("isPolicy");
        //映射数据
        HashMap<String,String> mapping =new HashMap<>();
        mapping.put("S","BS");
        mapping.put("N","国内航班");
        mapping.put("yClassStandardPrice","yclassStandardPrice");
        mapping.put("flight","flightNo");
        mapping.put("employeeId","preEmployeeId");
        assert GsonUtil.compareJsonObject(flightOrderDataObject,flightOrderData,mapping);
        //对比预订人的oid 推送数据未推送此字段单独来比较
        if(flightOrderData.getAsJsonObject("airBaseOrder").get("preEmployeeOid").isJsonNull()){
            assert false;
        }else{
            assert flightOrderData.getAsJsonObject("airBaseOrder").get("preEmployeeOid").getAsString().equals(employee.getUserOID());
        }
        //对比第一个人的oid
        if(flightOrderData.getAsJsonArray("airPassengerInfo").get(0).getAsJsonObject().get("passengerOid").isJsonNull()){
            assert false;
        }else{
            assert flightOrderData.getAsJsonArray("airPassengerInfo").get(0).getAsJsonObject().get("passengerOid").getAsString().equals(employee.getUserOID());
        }

    }
}

package com.test.api.testcase.vendor.settlementData;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SettlementBody;
import com.hand.basicObject.supplierObject.flightOrderSettlementInfo.FlightOrderSettlementInfo;
import com.hand.utils.GsonUtil;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.InfraStructure;
import com.test.api.method.Vendor;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2020/9/16
 * @Version 1.0
 **/
@Slf4j
public class FlightSettlementDataTest extends BaseTest {

    private Employee employee;
    private Vendor vendor;
    private InfraStructure infraStructure;

    /**
     *  目前还存在俩个bug 一个是preEmployeeOid   和后收服务费字段
     * @param phoneNumber
     * @param pwd
     * @param env
     */

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("yaliang.wang@cimc.com") String phoneNumber, @Optional("111111") String pwd, @Optional("stage") String env){
        employee =getEmployee(phoneNumber,pwd,env);
        vendor =new Vendor();
        infraStructure =new InfraStructure();
    }

    @Test(description = "机票结算费用数据对比-订票人和乘机人都是自己-1人未退票-不改签")
    public void flightSettlementDataTest1() throws HttpStatusException {
        ArrayList<FlightOrderSettlementInfo> FlightOrderSettlementInfos =new ArrayList<>();
        //初始化机票结算信息
        //结算信息的主键
        String recordId = String.valueOf(System.currentTimeMillis());
        //批次号
        String accBalanceBatchNo ="cimccTMC_200428140254184788_flight_"+ UTCTime.getBeijingDay(0);
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        //机票金额
        BigDecimal price = RandomNumber.getDoubleNumber(800,1200);
        //燃油费
        BigDecimal oilFee = new BigDecimal(50).setScale(2);
        //服务费
        BigDecimal serviceFee =new BigDecimal(20).setScale(2);
        //部门
        ArrayList<String> dept =new ArrayList<>();
        dept.add(GsonUtil.getDepartmentFromPath(employee.getDepartmentName()));
        FlightOrderSettlementInfo flightOrderSettlementInfo =FlightOrderSettlementInfo.builder()
                //结算信息的主键
                .recordId(recordId)
                .supplierName("")
                .supplierCode("cimccTMC")
                .corpId("200428140254184788")
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .companyOid(employee.getCompanyOID())
                .tenantCode(employee.getTenantCode())
                .tenantName(employee.getTenantName())
                .approvalCode("TA"+System.currentTimeMillis())
                //批次号
                .accBalanceBatchNo(accBalanceBatchNo)
                //订单号
                .orderNo(orderNo)
                .createTime(UTCTime.getBeijingTime(0,0,0))
                .orderDate(UTCTime.getBeijingTime(0,0,0))
                .deductibleFee(new BigDecimal(0.00).setScale(2))
                .nondeductibleFee(new BigDecimal(0.00).setScale(2))
                .detailType("出票")
                .orderType(1)
                .reservationSource("线上")
                .price(price)
                .variance(new BigDecimal(0.00).setScale(2))
                .tax(new BigDecimal(50).setScale(2))
                .oilFee(oilFee)
                .sendTicketFee(new BigDecimal(0).setScale(2))
                .insuranceFee(new BigDecimal(0).setScale(2))
                .serviceFee(serviceFee)
                .postServiceFee(new BigDecimal(100))
                .rebate(new BigDecimal(0).setScale(2))
                //未退票
                .refundServiceFee(new BigDecimal(0).setScale(2))
                .refundFee(new BigDecimal(0).setScale(2))
                //机票总价 机票价格+燃油费用+服务费
                .amount(price.add(oilFee).add(serviceFee).setScale(2))
                //改签费
                .rebookQueryFee(new BigDecimal(0).setScale(2))
                //改签手续费
                .dateChangeFee(new BigDecimal(0).setScale(2))
                //改签差价
                .priceDifferential(new BigDecimal(0).setScale(2))
                //改签服务费
                .reBookingServiceFee(new BigDecimal(100).setScale(2))
                .currency("CNY")
                .sequence("1")
                //当前时间5天之前起飞
                .takeOffTime(UTCTime.getBeijingTime(-5,0,0))
                //当前时间5天之前 3小时后到达
                .arrivalTime(UTCTime.getBeijingTime(-5,3,0))
                .dcityName("上海")
                .dcityCode("CHN031000000")
                .acityName("北京")
                .acityCode("CHN011000000")
                .dportName("上海虹桥")
                .aportName("首都机场")
                .airlineName("东航")
                .flightNo("MU1234")
                .priceRate("5.50")
                .className("Y")
                .travelingStandard("差旅标准")
                .bookClerkName(employee.getFullName())
                //预订人工号
                .bookClerkEmployeeId(employee.getEmployeeID())
                .bookClerkDept(dept)
                //乘客为1人
                .passengerName(employee.getFullName())
                .passengerEmployeeId(employee.getEmployeeID())
                .passengerDept(dept)
                .passengerCostCenter1("综合管理部-文秘组")
                //使用系统时间戳作为客票号
                .ticketNo(String.valueOf(System.currentTimeMillis()))
                //不改签
                .firstTicketNo("")
                .lastTicketNo("")
                //票号状态
                .ticketNoStatus("USE")
                .flightClass("N")
                .printStatus("")
                .costCenter1("综合管理部-文秘组")
                //机票行程单打印时间为航班到达时间的1小时后
                .printTime(UTCTime.getBeijingTime(-5,4,0))
                .build();
        FlightOrderSettlementInfos.add(flightOrderSettlementInfo);
        String info = GsonUtil.objectToString(FlightOrderSettlementInfos);
        //封装成JsonArray数组
        JsonArray listOrderSettlementInfo =new JsonParser().parse(info).getAsJsonArray();
        //推送的机票结算信息
        JsonObject flightSettlementJson = listOrderSettlementInfo.get(0).getAsJsonObject();
        JsonObject response = vendor.pushSettlementData(employee,"flight",FlightOrderSettlementInfos,"cimccTMC","200428140254184788","cimccTMC");
        log.info("推送的响应数据:{}",response);
        //初始化查询结算的对象
        SettlementBody settlementBody =SettlementBody.builder()
                .accBalanceBatchNo(accBalanceBatchNo)
                .orderNo(orderNo)
                .companyOid(employee.getCompanyOID())
                .recordId(recordId)
                .size(10)
                .page(1)
                .build();
        //查询结算数据
        JsonObject settlementData = vendor.internalQuerySettlement(employee,"flight",settlementBody);
        log.info("查询的结算数据:{}",settlementData);
        //查询数据中的数据在推送的结算数据中不存在对比 以及jsonarrayz中的数据对比
        //bookClerkEmployeeOid 订票人的OID 对比
        if(settlementData.get("bookClerkEmployeeOid").isJsonNull()){
            assert false;
        }else{
            assert settlementData.get("bookClerkEmployeeOid").getAsString().equals(employee.getUserOID());
        }
        if(settlementData.get("passengerEmployeeOid").isJsonNull()){
            assert false;
        }else{
            //passengerEmployeeOid  乘机人是自己
            assert settlementData.get("passengerEmployeeOid").getAsString().equals(employee.getUserOID());
        }
//        //bookClerkDept   订票人部门对比以及乘客的部门对比
        assert flightSettlementJson.get("bookClerkDept").getAsJsonArray().toString().equals(settlementData.get("bookClerkDept").getAsJsonArray().toString());
        assert flightSettlementJson.get("passengerDept").getAsJsonArray().toString().equals(settlementData.get("passengerDept").getAsJsonArray().toString());
        //进行数据对比
        //字段关系映射表 加这个是因为推数据的字段参数和查询出来的字段参数不一致,所以加上这个关系映射表
        HashMap<String,String> mapping = new HashMap<>();
        mapping.put("orderType","payType");
        mapping.put("acityCode","heliosacityCode");
        mapping.put("dcityCode","heliosdcityCode");
        assert GsonUtil.compareJsonObject(flightSettlementJson,settlementData,mapping);
    }

    @Test(description = "机票结算费用数据对比--1人改签-未退票")
    public void flightSettlementDataTest2() throws HttpStatusException {
        ArrayList<FlightOrderSettlementInfo> FlightOrderSettlementInfos =new ArrayList<>();
        //初始化机票结算信息
        //结算信息的主键
        String recordId1 = RandomNumber.getTimeNumber(8);
        //批次号
        String accBalanceBatchNo ="cimccTMC_200428140254184788_flight_"+ UTCTime.getBeijingDay(0);
        //订单号
        String orderNo = RandomNumber.getTimeNumber(14);
        //改签原票号
        String firstTicketNo = RandomNumber.getTimeNumber(13);
        //机票金额
        BigDecimal price = RandomNumber.getDoubleNumber(800,1200);
        //燃油费
        BigDecimal oilFee = new BigDecimal(50).setScale(2);
        //服务费
        BigDecimal serviceFee =new BigDecimal(20).setScale(2);
        //部门
        ArrayList<String> dept =new ArrayList<>();
        dept.add(GsonUtil.getDepartmentFromPath(employee.getDepartmentName()));
        FlightOrderSettlementInfo flightOrderSettlementInfo1 =FlightOrderSettlementInfo.builder()
                //结算信息的主键
                .recordId(recordId1)
                .supplierName("")
                .supplierCode("cimccTMC")
                .corpId("200428140254184788")
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .companyOid(employee.getCompanyOID())
                .tenantCode(employee.getTenantCode())
                .tenantName(employee.getTenantName())
                .approvalCode("TA"+System.currentTimeMillis())
                //批次号
                .accBalanceBatchNo(accBalanceBatchNo)
                //订单号
                .orderNo(orderNo)
                .createTime(UTCTime.getBeijingTime(-1,0,0))
                .orderDate(UTCTime.getBeijingTime(-5,0,0))
                .deductibleFee(new BigDecimal(0.00).setScale(2))
                .nondeductibleFee(new BigDecimal(0.00).setScale(2))
                .detailType("改签")
                .orderType(1)
                .reservationSource("线上")
                .price(price)
                .variance(new BigDecimal(0.00).setScale(2))
                .tax(new BigDecimal(50).setScale(2))
                .oilFee(oilFee)
                .sendTicketFee(new BigDecimal(0).setScale(2))
                .insuranceFee(new BigDecimal(0).setScale(2))
                .serviceFee(serviceFee)
                .postServiceFee(new BigDecimal(100))
                .rebate(new BigDecimal(0).setScale(2))
                //未退票
                .refundServiceFee(new BigDecimal(0).setScale(2))
                .refundFee(new BigDecimal(0).setScale(2))
                //机票总价 机票价格+燃油费用+服务费
                .amount(price.add(oilFee).add(serviceFee).setScale(2))
                //改签费
                .rebookQueryFee(new BigDecimal(200).setScale(2))
                //改签手续费
                .dateChangeFee(new BigDecimal(200).setScale(2))
                //改签差价
                .priceDifferential(new BigDecimal(0).setScale(2))
                //改签服务费
                .reBookingServiceFee(new BigDecimal(20).setScale(2))
                .currency("CNY")
                .sequence("1")
                //当前时间5天之前起飞
                .takeOffTime(UTCTime.getBeijingTime(-5,0,0))
                //当前时间5天之前 3小时后到达
                .arrivalTime(UTCTime.getBeijingTime(-5,3,0))
                .dcityName("上海")
                .dcityCode("CHN031000000")
                .acityName("北京")
                .acityCode("CHN011000000")
                .dportName("上海虹桥")
                .aportName("首都机场")
                .airlineName("东航")
                .flightNo("MU1234")
                .priceRate("5.50")
                .className("Y")
                .travelingStandard("差旅标准")
                .bookClerkName(employee.getFullName())
                //预订人工号
                .bookClerkEmployeeId(employee.getEmployeeID())
                .bookClerkDept(dept)
                //乘客为1人
                .passengerName(employee.getFullName())
                .passengerEmployeeId(employee.getEmployeeID())
                .passengerDept(dept)
                //使用系统时间戳作为客票号
                .ticketNo(RandomNumber.getTimeNumber(13))
                //只改签一次
                .firstTicketNo(firstTicketNo)
                .lastTicketNo(firstTicketNo)
                //票号状态
                .ticketNoStatus("EXCHANGEd")
                .flightClass("N")
                .printStatus("")
                //机票行程单打印时间为航班到达时间的1小时后
                .printTime(UTCTime.getBeijingTime(-5,4,0))
                .build();

        FlightOrderSettlementInfos.add(flightOrderSettlementInfo1);
        String info = GsonUtil.objectToString(FlightOrderSettlementInfos);
        //封装成JsonArray数组
        JsonArray listOrderSettlementInfo =new JsonParser().parse(info).getAsJsonArray();
        //推送的机票结算信息
        JsonObject flightSettlementJson = listOrderSettlementInfo.get(0).getAsJsonObject();
        vendor.pushSettlementData(employee,"flight",FlightOrderSettlementInfos,"cimccTMC","200428140254184788","cimccTMC");
        //初始化查询结算的对象
        SettlementBody settlementBody =SettlementBody.builder()
                .accBalanceBatchNo(accBalanceBatchNo)
                .orderNo(orderNo)
                .size(10)
                .page(1)
                .build();
        //查询结算数据
        JsonObject settlementData = vendor.internalQuerySettlement(employee,"flight",settlementBody);
        log.info("查询的结算数据:{}",settlementData);
        //查询数据中的数据在推送的结算数据中不存在对比 以及jsonarrayz中的数据对比
        //bookClerkEmployeeOid 订票人的OID 对比
        if(settlementData.get("bookClerkEmployeeOid").isJsonNull()){
            assert false;
        }else{
            assert settlementData.get("bookClerkEmployeeOid").getAsString().equals(employee.getUserOID());
        }
        if(settlementData.get("passengerEmployeeOid").isJsonNull()){
            assert false;
        }else{
            //passengerEmployeeOid  乘机人是自己
            assert settlementData.get("passengerEmployeeOid").getAsString().equals(employee.getUserOID());
        }
        //bookClerkDept   订票人部门对比以及乘客的部门对比
        assert flightSettlementJson.get("bookClerkDept").getAsJsonArray().toString().equals(settlementData.get("bookClerkDept").getAsJsonArray().toString());
        assert flightSettlementJson.get("passengerDept").getAsJsonArray().toString().equals(settlementData.get("passengerDept").getAsJsonArray().toString());
        //进行数据对比
        //字段关系映射表 加这个是因为推数据的字段参数和查询出来的字段参数不一致,所以加上这个关系映射表
        HashMap<String,String> mapping = new HashMap<>();
        mapping.put("orderType","payType");
        mapping.put("acityCode","heliosacityCode");
        mapping.put("dcityCode","heliosdcityCode");
        assert GsonUtil.compareJsonObject(flightSettlementJson,settlementData,mapping);
    }

    @Test(description = "机票结算费用数据对比--1退票")
    public void flightSettlementDataTest3() throws HttpStatusException {
        ArrayList<FlightOrderSettlementInfo> FlightOrderSettlementInfos =new ArrayList<>();
        //初始化机票结算信息
        //结算信息的主键
        String recordId1 = RandomNumber.getTimeNumber(8);
        //批次号
        String accBalanceBatchNo ="cimccTMC_200428140254184788_flight_"+ UTCTime.getBeijingDay(0);
        //订单号
        String orderNo = RandomNumber.getTimeNumber(14);
        //改签原票号
        String firstTicketNo = RandomNumber.getTimeNumber(13);
        //机票金额
        BigDecimal price = new BigDecimal(1000);
        //燃油费
        BigDecimal oilFee = new BigDecimal(50).setScale(2);
        //服务费
        BigDecimal serviceFee =new BigDecimal(20).setScale(2);
        //机票费
        BigDecimal amount = new BigDecimal(-850).setScale(2);
        //部门
        ArrayList<String> dept =new ArrayList<>();
        dept.add(GsonUtil.getDepartmentFromPath(employee.getDepartmentName()));
        FlightOrderSettlementInfo flightOrderSettlementInfo1 =FlightOrderSettlementInfo.builder()
                //结算信息的主键
                .recordId(recordId1)
                .supplierName("")
                .supplierCode("cimccTMC")
                .corpId("200428140254184788")
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .companyOid(employee.getCompanyOID())
                .tenantCode(employee.getTenantCode())
                .tenantName(employee.getTenantName())
                .approvalCode("TA"+System.currentTimeMillis())
                //批次号
                .accBalanceBatchNo(accBalanceBatchNo)
                //订单号
                .orderNo(orderNo)
                .createTime(UTCTime.getBeijingTime(-1,0,0))
                .orderDate(UTCTime.getBeijingTime(-5,0,0))
                .deductibleFee(new BigDecimal(0.00).setScale(2))
                .nondeductibleFee(new BigDecimal(0.00).setScale(2))
                .detailType("退票")
                .orderType(1)
                .reservationSource("线上")
                .price(price)
                .variance(new BigDecimal(0.00).setScale(2))
                .tax(new BigDecimal(50).setScale(2))
                .oilFee(oilFee)
                .sendTicketFee(new BigDecimal(0).setScale(2))
                .insuranceFee(new BigDecimal(50).setScale(2))
                .serviceFee(serviceFee)
                .postServiceFee(new BigDecimal(0).setScale(2))
                .rebate(new BigDecimal(0).setScale(2))
                //退票信息
                .refundServiceFee(new BigDecimal(20).setScale(2))
                .refundFee(new BigDecimal(300).setScale(2))
                //机票总价 机票价格+燃油费用+服务费
                .amount(amount)
                //改签费
                .rebookQueryFee(new BigDecimal(0).setScale(2))
                //改签手续费
                .dateChangeFee(new BigDecimal(0).setScale(2))
                //改签差价
                .priceDifferential(new BigDecimal(0).setScale(2))
                //改签服务费
                .reBookingServiceFee(new BigDecimal(0).setScale(2))
                .currency("CNY")
                .sequence("1")
                //当前时间5天之前起飞
                .takeOffTime(UTCTime.getBeijingTime(-5,0,0))
                //当前时间5天之前 3小时后到达
                .arrivalTime(UTCTime.getBeijingTime(-5,3,0))
                .dcityName("上海")
                .dcityCode("CHN031000000")
                .acityName("北京")
                .acityCode("CHN011000000")
                .dportName("上海虹桥")
                .aportName("首都机场")
                .airlineName("东航")
                .flightNo("MU1234")
                .priceRate("5.50")
                .className("Y")
                .travelingStandard("差旅标准")
                .bookClerkName(employee.getFullName())
                //预订人工号
                .bookClerkEmployeeId(employee.getEmployeeID())
                .bookClerkDept(dept)
                //乘客为1人
                .passengerName(employee.getFullName())
                .passengerEmployeeId(employee.getEmployeeID())
                .passengerDept(dept)
                //使用系统时间戳作为客票号
                .ticketNo(RandomNumber.getTimeNumber(13))
                //只改签一次
                .firstTicketNo(firstTicketNo)
                .lastTicketNo(firstTicketNo)
                //票号状态
                .ticketNoStatus("REFUNDED")
                .flightClass("N")
                .printStatus("")
                //机票行程单打印时间为航班到达时间的1小时后
                .printTime(UTCTime.getBeijingTime(-5,4,0))
                .build();

        FlightOrderSettlementInfos.add(flightOrderSettlementInfo1);
        String info = GsonUtil.objectToString(FlightOrderSettlementInfos);
        //封装成JsonArray数组
        JsonArray listOrderSettlementInfo =new JsonParser().parse(info).getAsJsonArray();
        //推送的机票结算信息
        JsonObject flightSettlementJson = listOrderSettlementInfo.get(0).getAsJsonObject();
        vendor.pushSettlementData(employee,"flight",FlightOrderSettlementInfos,"cimccTMC","200428140254184788","cimccTMC");
        //初始化查询结算的对象
        SettlementBody settlementBody =SettlementBody.builder()
                .accBalanceBatchNo(accBalanceBatchNo)
                .orderNo(orderNo)
                .size(10)
                .page(1)
                .build();
        //查询结算数据
        JsonObject settlementData = vendor.internalQuerySettlement(employee,"flight",settlementBody);
        log.info("查询的结算数据:{}",settlementData);
        //查询数据中的数据在推送的结算数据中不存在对比 以及jsonarrayz中的数据对比
        //bookClerkEmployeeOid 订票人的OID 对比
        if(settlementData.get("bookClerkEmployeeOid").isJsonNull()){
            assert false;
        }else{
            assert settlementData.get("bookClerkEmployeeOid").getAsString().equals(employee.getUserOID());
        }
        if(settlementData.get("passengerEmployeeOid").isJsonNull()){
            assert false;
        }else{
            //passengerEmployeeOid  乘机人是自己
            assert settlementData.get("passengerEmployeeOid").getAsString().equals(employee.getUserOID());
        }
        //bookClerkDept   订票人部门对比以及乘客的部门对比
        assert flightSettlementJson.get("bookClerkDept").getAsJsonArray().toString().equals(settlementData.get("bookClerkDept").getAsJsonArray().toString());
        assert flightSettlementJson.get("passengerDept").getAsJsonArray().toString().equals(settlementData.get("passengerDept").getAsJsonArray().toString());
        //进行数据对比
        //字段关系映射表 加这个是因为推数据的字段参数和查询出来的字段参数不一致,所以加上这个关系映射表
        HashMap<String,String> mapping = new HashMap<>();
        mapping.put("orderType","payType");
        mapping.put("acityCode","heliosacityCode");
        mapping.put("dcityCode","heliosdcityCode");
        assert GsonUtil.compareJsonObject(flightSettlementJson,settlementData,mapping);
    }


}

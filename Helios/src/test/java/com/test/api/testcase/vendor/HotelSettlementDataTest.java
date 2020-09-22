package com.test.api.testcase.vendor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SettlementBody;
import com.hand.basicObject.supplierObject.hotelOrderSettlementInfo.HotelOrderSettlementInfo;
import com.hand.basicObject.supplierObject.hotelOrderSettlementInfo.PassengerInfo;
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
public class HotelSettlementDataTest extends BaseTest {

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

    @Test(description = "酒店结算数据 - 1人1间房")
    public void hotelSettlementDataTest1() throws Exception {
        //结算主键
        String recordId =String.valueOf(System.currentTimeMillis());
        //批次号
        String accBalanceBatchNo = "cimccTMC_200428140254184788_flight_"+ UTCTime.getBeijingDay(0);
        //审批单号
        String approvalCode= "TA"+System.currentTimeMillis();
        //结算订单号
        String orderNo = RandomNumber.getTimeNumber();
        //房费
        BigDecimal roomTotalRate = new BigDecimal(1000).setScale(2);
        //服务费
        BigDecimal serviceFee =new BigDecimal(50).setScale(2);
        //部门
        ArrayList<String> dept =new ArrayList<>();
        dept.add(employee.getDepartmentName());
        PassengerInfo passengerInfo = PassengerInfo.builder()
                .passengerName(employee.getFullName())
                .passengerEmployeeId(employee.getEmployeeID())
                .passengerDepts(dept)
                .costCenter1("综合管理部-文秘组")
                .build();
        ArrayList<PassengerInfo> passengerInfos =new ArrayList<>();
        passengerInfos.add(passengerInfo);
        HotelOrderSettlementInfo hotelOrderSettlementInfo=HotelOrderSettlementInfo.builder()
                .recordId(recordId)
                .supplierName("")
                .supplierCode("cimccTMC")
                .corpId("200428140254184788")
                .companyName(employee.getCompanyName())
                .companyCode("1404")
                .companyOid(employee.getCompanyOID())
                .tenantCode(employee.getTenantCode())
                .tenantName(employee.getTenantName())
                .approvalCode(approvalCode)
                .batchNo(accBalanceBatchNo)
                .orderNo(orderNo)
                .createTime(UTCTime.getBeijingTime(0,0,0))
                .orderDate(UTCTime.getBeijingTime(0,0,0))
                .detailType("O")
                .hotelClass("N")
                .payType("1")
                //酒店支付类型
                .balanceType("前台现付")
                //房间夜间数
                .quantity("5")
                //房费总额
                .roomTotalRate(roomTotalRate)
                .price(new BigDecimal(200).setScale(2))
                .serviceFee(serviceFee)
                .serviceChargeFee(new BigDecimal(0).setScale(2))
                .currency("CNY")
                .reservationSource("线上")
                .amount(roomTotalRate.add(serviceFee))
                .variance(new BigDecimal(0).setScale(2))
                .orderType("月结")
                .hotelType("会员")
                .startTime(UTCTime.getBeijingTime(0,0,0))
                .endTime(UTCTime.getBeijingDate(5)+" 12:00:00")
                .hotelName("全季酒店")
                .hotelNameEN("")
                .roomName("商务大床房")
                .cityName("上海")
                .star("3")
                .bookClerkName(employee.getFullName())
                .bookClerkEmployeeId(employee.getEmployeeID())
                .bookClerkDepts(dept)
                .passengerList(passengerInfos)
                .costCenter1("综合管理部-文秘组")
                .build();
        ArrayList<HotelOrderSettlementInfo> hotelOrderSettlementInfos =new ArrayList<>();
        hotelOrderSettlementInfos.add(hotelOrderSettlementInfo);
        //推送的数据封装成一个json字符串
        String hotelOrderData = GsonUtil.objectToString(hotelOrderSettlementInfo);
        //转成jsonobject对象
        JsonObject hotelOrderDataObject =new JsonParser().parse(hotelOrderData).getAsJsonObject();
        vendor.pushSettlementData(employee,"hotel",hotelOrderSettlementInfos,"cimccTMC","200428140254184788","cimccTMC");
        //查询推送的结算数据
        //初始化查询结算的对象
        SettlementBody settlementBody =SettlementBody.builder()
                .accBalanceBatchNo(accBalanceBatchNo)
                .orderNo(orderNo)
                .companyOid(employee.getCompanyOID())
                .recordId(recordId)
                .size(10)
                .page(1)
                .build();
        JsonObject internalQuerySettlement = vendor.internalQuerySettlement(employee,"hotel",settlementBody);
        log.info("查询的酒店结算数据:{}",internalQuerySettlement);
        //进行入住旅客数据对比
        HashMap<String,String> mapping =new HashMap<>();
        assert GsonUtil.compareJsonArray(hotelOrderDataObject.getAsJsonArray("passengerList"),internalQuerySettlement.getAsJsonArray("passengerList"),mapping);
        //进行酒店结算信息对比
        assert GsonUtil.compareJsonObject(hotelOrderDataObject,internalQuerySettlement,mapping);
        //对比预订人部门数据
        assert GsonUtil.compareJsonArray(hotelOrderDataObject.getAsJsonArray("bookClerkDepts"),internalQuerySettlement.getAsJsonArray("bookClerkDepts"),mapping);
    }

    @Test(description = "酒店结算数据 - 俩人预定一间房")
    public void hotelSettlementDataTest2() throws Exception {
        //员工信息
        JsonObject employeeInfo1 = infraStructure.getUserDetail(employee,"01399315");
        //结算主键
        String recordId =String.valueOf(System.currentTimeMillis());
        //批次号
        String accBalanceBatchNo = "cimccTMC_200428140254184788_flight_"+ UTCTime.getBeijingDay(0);
        //审批单号
        String approvalCode= "TA"+System.currentTimeMillis();
        //结算订单号
        String orderNo = RandomNumber.getTimeNumber();
        //房费
        BigDecimal roomTotalRate = new BigDecimal(1000).setScale(2);
        //服务费
        BigDecimal serviceFee =new BigDecimal(50).setScale(2);
        BigDecimal amount =roomTotalRate.add(serviceFee);
        //部门
        ArrayList<String> dept =new ArrayList<>();
        dept.add(employee.getDepartmentName());
        PassengerInfo passengerInfo = PassengerInfo.builder()
                .passengerName(employee.getFullName()+","+employeeInfo1.get("fullName").getAsString())
                .passengerEmployeeId(employee.getEmployeeID()+","+employeeInfo1.get("employeeID").getAsString())
                .passengerDepts(dept)
                .build();
        ArrayList<PassengerInfo> passengerInfos =new ArrayList<>();
        passengerInfos.add(passengerInfo);
        HotelOrderSettlementInfo hotelOrderSettlementInfo=HotelOrderSettlementInfo.builder()
                .recordId(recordId)
                .supplierName("")
                .supplierCode("cimccTMC")
                .corpId("200428140254184788")
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .companyOid(employee.getCompanyOID())
                .tenantCode(employee.getTenantCode())
                .tenantName(employee.getTenantName())
                .approvalCode(approvalCode)
                .batchNo(accBalanceBatchNo)
                .orderNo(orderNo)
                .createTime(UTCTime.getBeijingTime(0,0,0))
                .orderDate(UTCTime.getBeijingTime(0,0,0))
                .detailType("O")
                .hotelClass("N")
                .payType("1")
                //酒店支付类型
                .balanceType("预付")
                //房间夜间数
                .quantity("5")
                //房费总额
                .roomTotalRate(roomTotalRate)
                .price(new BigDecimal(200).setScale(2))
                .serviceFee(serviceFee)
                .serviceChargeFee(new BigDecimal(0).setScale(2))
                .currency("CNY")
                .reservationSource("线上")
                .amount(amount)
                .variance(new BigDecimal(0).setScale(2))
                .orderType("月结")
                .hotelType("会员")
                .startTime(UTCTime.getBeijingTime(0,0,0))
                .endTime(UTCTime.getBeijingDate(5)+" 12:00:00")
                .hotelName("全季酒店")
                .hotelNameEN("")
                .roomName("商务大床房")
                .cityName("上海")
                .star("3")
                .bookClerkName(employee.getFullName())
                .bookClerkEmployeeId(employee.getEmployeeID())
                .bookClerkDepts(dept)
                .passengerList(passengerInfos)
                .costCenter1("综合管理部-文秘组")
                .build();
        ArrayList<HotelOrderSettlementInfo> hotelOrderSettlementInfos =new ArrayList<>();
        hotelOrderSettlementInfos.add(hotelOrderSettlementInfo);
        //推送的数据封装成一个json字符串
        String hotelOrderData = GsonUtil.objectToString(hotelOrderSettlementInfo);
        //转成jsonobject对象
        JsonObject hotelOrderDataObject =new JsonParser().parse(hotelOrderData).getAsJsonObject();
        vendor.pushSettlementData(employee,"hotel",hotelOrderSettlementInfos,"cimccTMC","200428140254184788","cimccTMC");
        //查询推送的结算数据
        //初始化查询结算的对象
        SettlementBody settlementBody =SettlementBody.builder()
                .accBalanceBatchNo(accBalanceBatchNo)
                .orderNo(orderNo)
                .companyOid(employee.getCompanyOID())
                .recordId(recordId)
                .size(10)
                .page(1)
                .build();
        JsonObject internalQuerySettlement = vendor.internalQuerySettlement(employee,"hotel",settlementBody);
        log.info("查询的酒店结算数据:{}",internalQuerySettlement);
        //进行入住旅客数据对比
        HashMap<String,String> mapping =new HashMap<>();
        assert GsonUtil.compareJsonArray(hotelOrderDataObject.getAsJsonArray("passengerList"),internalQuerySettlement.getAsJsonArray("passengerList"),mapping);
        //进行酒店结算信息对比
        assert GsonUtil.compareJsonObject(hotelOrderDataObject,internalQuerySettlement,mapping);
        //对比预订人部门数据
        assert GsonUtil.compareJsonArray(hotelOrderDataObject.getAsJsonArray("bookClerkDepts"),internalQuerySettlement.getAsJsonArray("bookClerkDepts"),mapping);
    }

    @Test(description = "酒店结算数据 -订单取消")
    public void hotelSettlementDataTest3() throws Exception {
        //结算主键
        String recordId =String.valueOf(System.currentTimeMillis());
        //批次号
        String accBalanceBatchNo = "cimccTMC_200428140254184788_flight_"+ UTCTime.getBeijingDay(0);
        //审批单号
        String approvalCode= "TA"+System.currentTimeMillis();
        //结算订单号
        String orderNo = RandomNumber.getTimeNumber();
        //房费
        BigDecimal roomTotalRate = new BigDecimal(1000).setScale(2);
        //服务费
        BigDecimal serviceFee =new BigDecimal(50).setScale(2);
        BigDecimal amount =roomTotalRate.add(serviceFee);
        //部门
        ArrayList<String> dept =new ArrayList<>();
        dept.add(employee.getDepartmentName());
        PassengerInfo passengerInfo = PassengerInfo.builder()
                .passengerName(employee.getFullName())
                .passengerEmployeeId(employee.getEmployeeID())
                .passengerDepts(dept)
                .build();
        ArrayList<PassengerInfo> passengerInfos =new ArrayList<>();
        passengerInfos.add(passengerInfo);
        HotelOrderSettlementInfo hotelOrderSettlementInfo=HotelOrderSettlementInfo.builder()
                .recordId(recordId)
                .supplierName("")
                .supplierCode("cimccTMC")
                .corpId("200428140254184788")
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .companyOid(employee.getCompanyOID())
                .tenantCode(employee.getTenantCode())
                .tenantName(employee.getTenantName())
                .approvalCode(approvalCode)
                .batchNo(accBalanceBatchNo)
                .orderNo(orderNo)
                .createTime(UTCTime.getBeijingTime(0,0,0))
                .orderDate(UTCTime.getBeijingTime(0,0,0))
                .detailType("R")
                .hotelClass("N")
                .payType("1")
                //酒店支付类型
                .balanceType("预付")
                //房间夜间数
                .quantity("5")
                //房费总额
                .roomTotalRate(roomTotalRate)
                .price(new BigDecimal(200).setScale(2))
                .serviceFee(serviceFee)
                .serviceChargeFee(new BigDecimal(0).setScale(2))
                .currency("CNY")
                .reservationSource("线上")
                .amount(amount.multiply(new BigDecimal(-1)).setScale(2))
                .variance(new BigDecimal(0).setScale(2))
                .orderType("月结")
                .hotelType("会员")
                .startTime(UTCTime.getBeijingTime(0,0,0))
                .endTime(UTCTime.getBeijingDate(5)+" 12:00:00")
                .hotelName("全季酒店")
                .hotelNameEN("")
                .roomName("商务大床房")
                .cityName("上海")
                .star("3")
                .bookClerkName(employee.getFullName())
                .bookClerkEmployeeId(employee.getEmployeeID())
                .bookClerkDepts(dept)
                .passengerList(passengerInfos)
                .costCenter1("综合管理部-文秘组")
                .build();
        ArrayList<HotelOrderSettlementInfo> hotelOrderSettlementInfos =new ArrayList<>();
        hotelOrderSettlementInfos.add(hotelOrderSettlementInfo);
        //推送的数据封装成一个json字符串
        String hotelOrderData = GsonUtil.objectToString(hotelOrderSettlementInfo);
        //转成jsonobject对象
        JsonObject hotelOrderDataObject =new JsonParser().parse(hotelOrderData).getAsJsonObject();
        vendor.pushSettlementData(employee,"hotel",hotelOrderSettlementInfos,"cimccTMC","200428140254184788","cimccTMC");
        //查询推送的结算数据
        //初始化查询结算的对象
        SettlementBody settlementBody =SettlementBody.builder()
                .accBalanceBatchNo(accBalanceBatchNo)
                .orderNo(orderNo)
                .companyOid(employee.getCompanyOID())
                .recordId(recordId)
                .size(10)
                .page(1)
                .build();
        JsonObject internalQuerySettlement = vendor.internalQuerySettlement(employee,"hotel",settlementBody);
        log.info("查询的酒店结算数据:{}",internalQuerySettlement);
        //进行入住旅客数据对比
        HashMap<String,String> mapping =new HashMap<>();
        assert GsonUtil.compareJsonArray(hotelOrderDataObject.getAsJsonArray("passengerList"),internalQuerySettlement.getAsJsonArray("passengerList"),mapping);
        //进行酒店结算信息对比
        assert GsonUtil.compareJsonObject(hotelOrderDataObject,internalQuerySettlement,mapping);
        //对比预订人部门数据
        assert GsonUtil.compareJsonArray(hotelOrderDataObject.getAsJsonArray("bookClerkDepts"),internalQuerySettlement.getAsJsonArray("bookClerkDepts"),mapping);
    }
}

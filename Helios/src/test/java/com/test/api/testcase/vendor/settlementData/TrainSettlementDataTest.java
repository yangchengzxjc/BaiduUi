package com.test.api.testcase.vendor.settlementData;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SettlementBody;
import com.hand.basicObject.supplierObject.trainSettlementInfo.*;
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
 * @Date 2020/8/26
 * @Version 1.0
 **/
@Slf4j
public class TrainSettlementDataTest extends BaseTest {
    private Employee employee;
    private Vendor vendor;
    private InfraStructure infraStructure;


    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("yaliang.wang@cimc.com") String phoneNumber, @Optional("111111") String pwd,@Optional("stage") String env){
        employee =getEmployee(phoneNumber,pwd,env);
        vendor =new Vendor();
        infraStructure =new InfraStructure();
    }

    @Test(description = "火车结算费用推送-预定-自己订票-自己乘坐-未改签-未退票")
    public void trainSettlementDataTest1() throws Exception {
        //结算主键
        String recordId =String.valueOf(System.currentTimeMillis());
        //批次号
        String accBalanceBatchNo = "cimccTMC_200428140254184788_flight_"+UTCTime.getBeijingDay(0);
        //审批单号
        String approvalCode= "TA"+System.currentTimeMillis();
        //结算订单号
        String orderNo = RandomNumber.getTimeNumber();
        //火车订单号
        String trainOrderNo =String.valueOf(System.currentTimeMillis());
        //票价
        BigDecimal ticketFee =RandomNumber.getDoubleNumber(300,700).setScale(2);
        //保险费
        BigDecimal insuranceFee = new BigDecimal(10).setScale(2);
        //服务费
        BigDecimal serviceFee =new BigDecimal(20).setScale(2);
        //部门
        ArrayList<String> dept =new ArrayList<>();
        dept.add(employee.getDepartmentName());
        //生成10位数的车票编号
        String ticketNo = RandomNumber.getUUID(10);
        TrainBaseSettlement trainBaseSettlement =TrainBaseSettlement.builder()
                .recordId(recordId)
                .supplierName("")
                .supplierCode("cimccTMC")
                .approvalCode(approvalCode)
                .accBalanceBatchNo(accBalanceBatchNo)
                .orderNo(orderNo)
                //订单类型
                .operateType("B")
                //票面价
                .ticketFee(ticketFee)
                .serviceFee(new BigDecimal(20).setScale(2))
                .refundFee(new BigDecimal(0).setScale(2))
                .insuranceFee(insuranceFee)
                .paperFare(new BigDecimal(0).setScale(2))
                .expressFee(new BigDecimal(0).setScale(2))
                //改签服务费
                .changeServiceFee(new BigDecimal(0).setScale(2))
                .postServiceFee(new BigDecimal(0).setScale(2))
                //实际应收
                .amount(ticketFee.add(insuranceFee).add(serviceFee))
                .deductibleFee(new BigDecimal(0).setScale(2))
                .nondeductibleFee(ticketFee.add(insuranceFee).add(serviceFee))
                .payType("月结")
                .lastUpdateTime(UTCTime.getBeijingTime(0,0,0))
                .orderType("月结火车票")
                .bookClerkName(employee.getFullName())
                .bookClerkEmployeeId(employee.getEmployeeID())
                .currency("CNY")
                .build();
        TrainBaseOrder trainBaseOrder = TrainBaseOrder.builder()
                .orderNo(trainOrderNo)
                .orderStatus("出票")
                .bookerSource("线上")
                .bookerName(employee.getFullName())
                .bookerRank("p3")
                .orderType("电子票")
                .payType("月结")
                //当前时间5天前预定
                .bookerTime(UTCTime.getBeijingTime(-5,0,0))
                .refundStatus("")
                .changeStatus("")
                .orderCostCenter1("综合管理部-文秘组")
                .tripPurpose("项目技术支持")
                .departments(dept)
                .build();
        //初始化一个乘客
        TrainPassengerInfo trainPassengerInfo = TrainPassengerInfo.builder()
                .passengerNo("1")
                .passengerName(employee.getFullName())
                //乘客工号
                .passengerCode(employee.getEmployeeID())
                .ticketName(employee.getFullName())
                .passengerCostCenter1("综合管理部-文秘组")
                .tripPurpose("项目技术支持")
                .departments(dept)
                .build();
        ArrayList<TrainPassengerInfo> trainPassengerInfos =new ArrayList<>();
        //添加一个乘客
        trainPassengerInfos.add(trainPassengerInfo);
        //初始化票和乘客的信息
        TrainPassengerTicketCorrelation trainPassengerTicketCorrelation = TrainPassengerTicketCorrelation.builder()
                .orderNo(trainOrderNo)
                .passengerNo("1")
                //车次编号
                .trainNo("D1234")
                //车票编号
                .ticketNo(ticketNo)
                .build();
        ArrayList<TrainPassengerTicketCorrelation> trainPassengerTicketCorrelations =new ArrayList<>();
        trainPassengerTicketCorrelations.add(trainPassengerTicketCorrelation);
        TrainTicketDetail trainTicketDetail =TrainTicketDetail.builder()
                .trainNo("D1234")
                .ticketNo(ticketNo)
                .trainType("D")
                .departureCity("西安")
                .arrivalCity("北京")
                //出发时间为当前时间的3天前
                .departureTime(UTCTime.getBeijingTime(-3,0,0))
                .departureStationName("西安北站")
                //到达时间为出发时间的4小时后
                .arriveTime(UTCTime.getBeijingTime(-3,4,0))
                .arriveStationName("北京西站")
                .electronicOrderNo(RandomNumber.getUUID(10))
                .ticketType("原车次")
                .seatNum("05车07C")
                .seatType("209")
                //出票实际价格
                .ticketActualFee(ticketFee)
                //退改预估手续费
                .refundChangeCommission(new BigDecimal(85.00).setScale(2))
                .build();
        ArrayList<TrainTicketDetail> trainTicketDetails =new ArrayList<>();
        trainTicketDetails.add(trainTicketDetail);
        TrainSettlementInfo trainSettlementInfo =TrainSettlementInfo.builder()
                .trainBaseSettlement(trainBaseSettlement)
                .trainBaseOrder(trainBaseOrder)
                .trainPassengerInfos(trainPassengerInfos)
                .trainPassengerTicketCorrelations(trainPassengerTicketCorrelations)
                .trainTicketDetails(trainTicketDetails)
                .build();
        ArrayList<TrainSettlementInfo> trainSettlementInfos =new ArrayList<>();
        trainSettlementInfos.add(trainSettlementInfo);
        //推送的数据封装成一个json字符串
        String trainOrderData =GsonUtil.objectToString(trainSettlementInfo);
        //转成jsonobject对象
        JsonObject trainOrderDataObject =new JsonParser().parse(trainOrderData).getAsJsonObject();
        //推送的结算数据
        vendor.pushSettlementData(employee,"train",trainSettlementInfos,"cimccTMC","200428140254184788","cimccTMC");
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
        JsonObject internalQuerySettlement = vendor.internalQuerySettlement(employee,"train",settlementBody);
        log.info("查询的火车结算数据:{}",internalQuerySettlement);
        //映射表
        HashMap<String,String> mapping =new HashMap<>();
        //映射月结->M  现付-N
        mapping.put("月结火车票","M");
        mapping.put("现付","2");
        mapping.put("月结","1");
        mapping.put("现付火车票","N");
        mapping.put("trainPassengerInfos","trainPassengerInfo");
        //对比bookClerkEmployeeOid和passengerOid
        if(internalQuerySettlement.getAsJsonObject("trainBaseSettlement").get("bookClerkEmployeeOid").isJsonNull()){
            assert false;
        }else{
            assert internalQuerySettlement.getAsJsonObject("trainBaseSettlement").get("bookClerkEmployeeOid").getAsString().equals(employee.getUserOID());
        }
        if(internalQuerySettlement.getAsJsonArray("trainPassengerInfos").get(0).getAsJsonObject().get("passengerOid").isJsonNull()){
            assert false;
        }else{
            assert internalQuerySettlement.getAsJsonArray("trainPassengerInfos").get(0).getAsJsonObject().get("passengerOid").getAsString().equals(employee.getUserOID());
        }
        //数据对比
        assert GsonUtil.compareJsonObject(trainOrderDataObject,internalQuerySettlement,mapping);
    }

    @Test(description = "火车结算费用推送-预定-自己订票-自己乘坐-改签")
    public void trainSettlementDataTest2() throws Exception {
        //结算主键
        String recordId =String.valueOf(System.currentTimeMillis());
        //批次号
        String accBalanceBatchNo = "cimccTMC_200428140254184788_flight_"+UTCTime.getBeijingDay(0);
        //审批单号
        String approvalCode= "TA"+System.currentTimeMillis();
        //结算订单号
        String orderNo = RandomNumber.getTimeNumber();
        //火车订单号
        String trainOrderNo =String.valueOf(System.currentTimeMillis());
        //票价
        BigDecimal ticketFee =RandomNumber.getDoubleNumber(300,700).setScale(2);
        //保险费
        BigDecimal insuranceFee = new BigDecimal(10).setScale(2);
        //服务费
        BigDecimal serviceFee =new BigDecimal(20).setScale(2);
        //部门
        ArrayList<String> dept =new ArrayList<>();
        dept.add(employee.getDepartmentName());
        //生成10位数的车票编号
        String ticketNo = RandomNumber.getUUID(10);
        TrainBaseSettlement trainBaseSettlement =TrainBaseSettlement.builder()
                .recordId(recordId)
                .supplierName("")
                .supplierCode("cimccTMC")
                .approvalCode(approvalCode)
                .accBalanceBatchNo(accBalanceBatchNo)
                .orderNo(orderNo)
                //订单类型
                .operateType("C")
                //票面价
                .ticketFee(ticketFee)
                .serviceFee(new BigDecimal(20).setScale(2))
                .refundFee(new BigDecimal(0).setScale(2))
                .insuranceFee(insuranceFee)
                .paperFare(new BigDecimal(0).setScale(2))
                .expressFee(new BigDecimal(0).setScale(2))
                //改签服务费
                .changeServiceFee(new BigDecimal(20).setScale(2))
                .postServiceFee(new BigDecimal(0).setScale(2))
                //实际应收(包含改签服务费)
                .amount(ticketFee.add(insuranceFee).add(serviceFee).add(new BigDecimal(20)))
                .deductibleFee(new BigDecimal(0).setScale(2))
                .nondeductibleFee(ticketFee.add(insuranceFee).add(serviceFee).add(new BigDecimal(20)))
                .payType("月结")
                .lastUpdateTime(UTCTime.getBeijingTime(0,0,0))
                .orderType("月结火车票")
                .bookClerkName(employee.getFullName())
                .bookClerkEmployeeId(employee.getEmployeeID())
                .currency("CNY")
                .build();
        TrainBaseOrder trainBaseOrder = TrainBaseOrder.builder()
                .orderNo(trainOrderNo)
                .orderStatus("改签成功")
                .bookerSource("线上")
                .bookerName(employee.getFullName())
                .bookerRank("p3")
                .orderType("电子票")
                .payType("月结")
                //当前时间5天前预定
                .bookerTime(UTCTime.getBeijingTime(-5,0,0))
                .refundStatus("")
                .changeStatus("S")
                .orderCostCenter1("综合管理部-文秘组")
                .tripPurpose("项目技术支持")
                .departments(dept)
                .build();
        //初始化一个乘客
        TrainPassengerInfo trainPassengerInfo = TrainPassengerInfo.builder()
                .passengerNo("1")
                .passengerName(employee.getFullName())
                //乘客工号
                .passengerCode(employee.getEmployeeID())
                .ticketName(employee.getFullName())
                .passengerCostCenter1("综合管理部-文秘组")
                .tripPurpose("项目技术支持")
                .departments(dept)
                .build();
        ArrayList<TrainPassengerInfo> trainPassengerInfos =new ArrayList<>();
        //添加一个乘客
        trainPassengerInfos.add(trainPassengerInfo);
        //初始化票和乘客的信息
        TrainPassengerTicketCorrelation trainPassengerTicketCorrelation = TrainPassengerTicketCorrelation.builder()
                .changeStatus("S")
                .orderNo(trainOrderNo)
                .passengerNo("1")
                //车次编号
                .trainNo("D1234")
                //车票编号
                .ticketNo(ticketNo)
                .build();
        ArrayList<TrainPassengerTicketCorrelation> trainPassengerTicketCorrelations =new ArrayList<>();
        trainPassengerTicketCorrelations.add(trainPassengerTicketCorrelation);
        TrainTicketDetail trainTicketDetail =TrainTicketDetail.builder()
                .trainNo("D1234")
                .ticketNo(ticketNo)
                .trainType("D")
                .departureCity("西安")
                .arrivalCity("北京")
                //出发时间为当前时间的3天前
                .departureTime(UTCTime.getBeijingTime(-3,0,0))
                .departureStationName("西安北站")
                //到达时间为出发时间的4小时后
                .arriveTime(UTCTime.getBeijingTime(-3,4,0))
                .arriveStationName("北京西站")
                .electronicOrderNo(RandomNumber.getUUID(10))
                .ticketType("改签车次")
                .seatNum("05车07C")
                .seatType("209")
                //出票实际价格
                .ticketActualFee(ticketFee)
                .changeStatus("S")
                //退改预估手续费
                .refundChangeCommission(new BigDecimal(20.00).setScale(2))
                .build();
        ArrayList<TrainTicketDetail> trainTicketDetails =new ArrayList<>();
        trainTicketDetails.add(trainTicketDetail);
        TrainSettlementInfo trainSettlementInfo =TrainSettlementInfo.builder()
                .trainBaseSettlement(trainBaseSettlement)
                .trainBaseOrder(trainBaseOrder)
                .trainPassengerInfos(trainPassengerInfos)
                .trainPassengerTicketCorrelations(trainPassengerTicketCorrelations)
                .trainTicketDetails(trainTicketDetails)
                .build();
        ArrayList<TrainSettlementInfo> trainSettlementInfos =new ArrayList<>();
        trainSettlementInfos.add(trainSettlementInfo);
        //推送的数据封装成一个json字符串
        String trainOrderData =GsonUtil.objectToString(trainSettlementInfo);
        //转成jsonobject对象
        JsonObject trainOrderDataObject =new JsonParser().parse(trainOrderData).getAsJsonObject();
        //推送的结算数据
        vendor.pushSettlementData(employee,"train",trainSettlementInfos,"cimccTMC","200428140254184788","cimccTMC");
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
        JsonObject internalQuerySettlement = vendor.internalQuerySettlement(employee,"train",settlementBody);
        log.info("查询的火车结算数据:{}",internalQuerySettlement);
        //映射表
        HashMap<String,String> mapping =new HashMap<>();
        //映射月结->M  现付-N
        mapping.put("月结火车票","M");
        mapping.put("现付","2");
        mapping.put("月结","1");
        mapping.put("现付火车票","N");
        mapping.put("trainPassengerInfos","trainPassengerInfo");
        //对比passengerInfo中的passengerOid
        //数据对比
        if(internalQuerySettlement.getAsJsonObject("trainBaseSettlement").get("bookClerkEmployeeOid").isJsonNull()){
            assert false;
        }else{
            assert internalQuerySettlement.getAsJsonObject("trainBaseSettlement").get("bookClerkEmployeeOid").getAsString().equals(employee.getUserOID());
        }
        if(internalQuerySettlement.getAsJsonArray("trainPassengerInfos").get(0).getAsJsonObject().get("passengerOid").isJsonNull()){
            assert false;
        }else{
            assert internalQuerySettlement.getAsJsonArray("trainPassengerInfos").get(0).getAsJsonObject().get("passengerOid").getAsString().equals(employee.getUserOID());
        }
        assert GsonUtil.compareJsonObject(trainOrderDataObject,internalQuerySettlement,mapping);
    }

    @Test(description = "火车结算费用推送-预定-自己订票-自己乘坐-退票")
    public void trainSettlementDataTest3() throws Exception {
        //结算主键
        String recordId =String.valueOf(System.currentTimeMillis());
        //批次号
        String accBalanceBatchNo = "cimccTMC_200428140254184788_flight_"+UTCTime.getBeijingDay(0);
        //审批单号
        String approvalCode= "TA"+System.currentTimeMillis();
        //结算订单号
        String orderNo = RandomNumber.getTimeNumber();
        //火车订单号
        String trainOrderNo =String.valueOf(System.currentTimeMillis());
        //票价
        BigDecimal ticketFee =RandomNumber.getDoubleNumber(300,700).setScale(2);
        //保险费
        BigDecimal insuranceFee = new BigDecimal(10).setScale(2);
        //服务费
        BigDecimal serviceFee =new BigDecimal(20).setScale(2);
        //部门
        ArrayList<String> dept =new ArrayList<>();
        dept.add(employee.getDepartmentName());
        //生成10位数的车票编号
        String ticketNo = RandomNumber.getUUID(10);
        TrainBaseSettlement trainBaseSettlement =TrainBaseSettlement.builder()
                .recordId(recordId)
                .supplierName("")
                .supplierCode("cimccTMC")
                .approvalCode(approvalCode)
                .accBalanceBatchNo(accBalanceBatchNo)
                .orderNo(orderNo)
                //订单类型
                .operateType("R")
                //票面价
                .ticketFee(ticketFee)
                .serviceFee(new BigDecimal(20).setScale(2))
                .refundFee(new BigDecimal(0).setScale(2))
                .insuranceFee(insuranceFee)
                .paperFare(new BigDecimal(0).setScale(2))
                .expressFee(new BigDecimal(0).setScale(2))
                //退票费
                .refundFee(new BigDecimal(100).setScale(2))
                //实际应收(包含改签服务费)
                .amount(ticketFee.add(insuranceFee).add(serviceFee).add(new BigDecimal(20)))
                .deductibleFee(new BigDecimal(0).setScale(2))
                .nondeductibleFee(ticketFee.add(insuranceFee).add(serviceFee).add(new BigDecimal(20)))
                .payType("月结")
                .lastUpdateTime(UTCTime.getBeijingTime(0,0,0))
                .orderType("月结火车票")
                .bookClerkName(employee.getFullName())
                .bookClerkEmployeeId(employee.getEmployeeID())
                .currency("CNY")
                .build();
        TrainBaseOrder trainBaseOrder = TrainBaseOrder.builder()
                .orderNo(trainOrderNo)
                .orderStatus("改签成功")
                .bookerSource("线上")
                .bookerName(employee.getFullName())
                .bookerRank("p3")
                .orderType("电子票")
                .payType("月结")
                //当前时间5天前预定
                .bookerTime(UTCTime.getBeijingTime(-5,0,0))
                .refundStatus("S")
                .changeStatus("")
                .orderCostCenter1("综合管理部-文秘组")
                .tripPurpose("项目技术支持")
                .departments(dept)
                .build();
        //初始化一个乘客
        TrainPassengerInfo trainPassengerInfo = TrainPassengerInfo.builder()
                .passengerNo("1")
                .passengerName(employee.getFullName())
                //乘客工号
                .passengerCode(employee.getEmployeeID())
                .ticketName(employee.getFullName())
                .passengerCostCenter1("综合管理部-文秘组")
                .tripPurpose("项目技术支持")
                .departments(dept)
                .build();
        ArrayList<TrainPassengerInfo> trainPassengerInfos =new ArrayList<>();
        //添加一个乘客
        trainPassengerInfos.add(trainPassengerInfo);
        //初始化票和乘客的信息
        TrainPassengerTicketCorrelation trainPassengerTicketCorrelation = TrainPassengerTicketCorrelation.builder()
                .orderNo(trainOrderNo)
                .passengerNo("1")
                //车次编号
                .trainNo("D1234")
                //车票编号
                .refundTicketStatus("S")
                .refundStatus("已退款")
                .ticketNo(ticketNo)
                .build();
        ArrayList<TrainPassengerTicketCorrelation> trainPassengerTicketCorrelations =new ArrayList<>();
        trainPassengerTicketCorrelations.add(trainPassengerTicketCorrelation);
        TrainTicketDetail trainTicketDetail =TrainTicketDetail.builder()
                .trainNo("D1234")
                .ticketNo(ticketNo)
                .trainType("D")
                .departureCity("西安")
                .arrivalCity("北京")
                //出发时间为当前时间的3天前
                .departureTime(UTCTime.getBeijingTime(-3,0,0))
                .departureStationName("西安北站")
                //到达时间为出发时间的4小时后
                .arriveTime(UTCTime.getBeijingTime(-3,4,0))
                .arriveStationName("北京西站")
                .electronicOrderNo(RandomNumber.getUUID(10))
                .ticketType("原车次")
                .seatNum("05车07C")
                .seatType("209")
                //出票实际价格
                .ticketActualFee(ticketFee)
                //退改预估手续费
                .refundChangeCommission(new BigDecimal(20.00).setScale(2))
                .build();
        ArrayList<TrainTicketDetail> trainTicketDetails =new ArrayList<>();
        trainTicketDetails.add(trainTicketDetail);
        TrainSettlementInfo trainSettlementInfo =TrainSettlementInfo.builder()
                .trainBaseSettlement(trainBaseSettlement)
                .trainBaseOrder(trainBaseOrder)
                .trainPassengerInfos(trainPassengerInfos)
                .trainPassengerTicketCorrelations(trainPassengerTicketCorrelations)
                .trainTicketDetails(trainTicketDetails)
                .build();
        ArrayList<TrainSettlementInfo> trainSettlementInfos =new ArrayList<>();
        trainSettlementInfos.add(trainSettlementInfo);
        //推送的数据封装成一个json字符串
        String trainOrderData =GsonUtil.objectToString(trainSettlementInfo);
        //转成jsonobject对象
        JsonObject trainOrderDataObject =new JsonParser().parse(trainOrderData).getAsJsonObject();
        //推送的结算数据
        vendor.pushSettlementData(employee,"train",trainSettlementInfos,"cimccTMC","200428140254184788","cimccTMC");
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
        JsonObject internalQuerySettlement = vendor.internalQuerySettlement(employee,"train",settlementBody);
        log.info("查询的火车结算数据:{}",internalQuerySettlement);
        //映射表
        HashMap<String,String> mapping =new HashMap<>();
        //映射月结->M  现付-N
        mapping.put("月结火车票","M");
        mapping.put("现付","2");
        mapping.put("月结","1");
        mapping.put("现付火车票","N");
        mapping.put("trainPassengerInfos","trainPassengerInfo");
        //对比passengerInfo中的passengerOid
        if(internalQuerySettlement.getAsJsonObject("trainBaseSettlement").get("bookClerkEmployeeOid").isJsonNull()){
            assert false;
        }else{
            assert internalQuerySettlement.getAsJsonObject("trainBaseSettlement").get("bookClerkEmployeeOid").getAsString().equals(employee.getUserOID());
        }
        if(internalQuerySettlement.getAsJsonArray("trainPassengerInfos").get(0).getAsJsonObject().get("passengerOid").isJsonNull()){
            assert false;
        }else{
            assert internalQuerySettlement.getAsJsonArray("trainPassengerInfos").get(0).getAsJsonObject().get("passengerOid").getAsString().equals(employee.getUserOID());
        }
        //数据对比
        assert GsonUtil.compareJsonObject(trainOrderDataObject,internalQuerySettlement,mapping);
    }

}
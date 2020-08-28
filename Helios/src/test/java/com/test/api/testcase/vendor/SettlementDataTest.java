package com.test.api.testcase.vendor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.flightOrderSettlementInfo.FlightOrderSettlementInfo;
import com.hand.basicObject.supplierObject.hotelOrderSettlementInfo.HotelOrderSettlementInfo;
import com.hand.basicObject.supplierObject.hotelOrderSettlementInfo.PassengerInfo;
import com.hand.basicObject.supplierObject.trainSettlementInfo.*;
import com.hand.utils.GsonUtil;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.Vendor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @Author peng.zhang
 * @Date 2020/8/26
 * @Version 1.0
 **/
public class SettlementDataTest extends BaseTest {
    private Employee employee;
    private Vendor vendor;


    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("14082978000") String phoneNumber, @Optional("hly123456") String pwd,@Optional("stage") String env){
        employee =getEmployee(phoneNumber,pwd,env);
        vendor =new Vendor();
    }

    @Test(description = "机票结算费用数据对比-1人未退票-不改签")
    public void flightSettlementDataTest1() throws HttpStatusException {
        ArrayList<FlightOrderSettlementInfo> FlightOrderSettlementInfos =new ArrayList<>();
        //初始化机票结算信息
        //机票金额
        BigDecimal price = RandomNumber.getDoubleNumber(800,1200);
        //燃油费
        BigDecimal oilFee = new BigDecimal(50).setScale(2);
        //服务费
        BigDecimal serviceFee =new BigDecimal(20).setScale(2);
        //预定机票人工号
        String bookClerkEmployeeId =String.valueOf(RandomNumber.getRandomNumber(1,1000));
        //部门
        ArrayList<String> dept =new ArrayList<>();
        dept.add("测试部门1");
        FlightOrderSettlementInfo flightOrderSettlementInfo =FlightOrderSettlementInfo.builder()
                .recordId(String.valueOf(System.currentTimeMillis()))
                .supplierName("")
                .supplierCode("")
                .corpId("200428140254184788")
                .companyName("")
                .companyCode("")
                .companyOid("c7c1fd08-e2c7-4567-858e-b4f90be39f2d")
                .tenantId("")
                .tenantCode("")
                .tenantName("")
                .approvalCode("TA"+System.currentTimeMillis())
                //批次号
                .accBalanceBatchNo("cimccTMC_200428140254184788_flight_"+UTCTime.getBeijingDay(0))
                .orderNo(RandomNumber.getTimeNumber())
                .createTime(UTCTime.getBeijingTime(0,0))
                .orderDate(UTCTime.getBeijingTime(0,0))
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
                .reBookingServiceFee(new BigDecimal(100))
                .currency("CNY")
                .sequence("1")
                //当前时间5天之前起飞
                .takeOffTime(UTCTime.getBeijingTime(-5,0))
                //当前时间5天之前 3小时后到达
                .arrivalTime(UTCTime.getBeijingTime(-5,3))
                .dcityName("上海")
                .dcityCode("SHA")
                .acityName("北京")
                .acityCode("PEK")
                .dportName("上海虹桥")
                .aportName("首都机场")
                .airlineName("东航")
                .flightNo("MU1234")
                .priceRate("5.5")
                .className("Y")
                .travelingStandard("差旅标准")
                .bookClerkName("测试人"+bookClerkEmployeeId)
                //预订人工号
                .bookClerkEmployeeId(bookClerkEmployeeId)
                .bookClerkDept(dept)
                //乘客为1人
                .passengerName("测试人"+bookClerkEmployeeId)
                .passengerEmployeeId(bookClerkEmployeeId)
                .passengerDept(dept)
                .passengerCostCenter1("成本中心1")
                .passengerCostCenter2("")
                .passengerCostCenter3("")
                .passengerCostCenter4("")
                .passengerCostCenter5("")
                .passengerCostCenter6("")
                //使用系统时间戳作为客票号
                .ticketNo(String.valueOf(System.currentTimeMillis()))
                //不改签
                .firstTicketNo("")
                .lastTicketNo("")
                //票号状态
                .ticketNoStatus("USE")
                .flightClass("N")
                .printStatus("")
                .costCenter1("")
                .printTime("")
                .costCenter2("")
                .costCenter3("")
                .costCenter4("")
                .costCenter5("")
                .costCenter6("")
                .build();
        FlightOrderSettlementInfos.add(flightOrderSettlementInfo);
        String info = GsonUtil.objectToString(FlightOrderSettlementInfos);
        JsonArray listOrderSettlementInfo =new JsonParser().parse(info).getAsJsonArray();
        System.out.println(listOrderSettlementInfo);
        JsonObject object = vendor.pushSettlementData(employee,"flight",FlightOrderSettlementInfos,"cimccTMC","200428140254184788","cimccTMC");
        System.out.println(object);
    }

    @Test(description = "火车结算费用推送-预定-未改签-未退票")
    public void trainSettlementDataTest2() throws HttpStatusException {
        //结算主键
        String recordId =String.valueOf(System.currentTimeMillis());
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
        //预定员工工号
        String bookClerkEmployeeId =String.valueOf(RandomNumber.getRandomNumber(1,1000));
        //部门
        ArrayList<String> dept =new ArrayList<>();
        dept.add("测试部门1");
        //生成10位数的车票编号
        String ticketNo = RandomNumber.getUUID(10);
        TrainBaseSettlement trainBaseSettlement =TrainBaseSettlement.builder()
                .recordId(recordId)
                .supplierName("")
                .supplierCode("")
                .approvalCode(approvalCode)
                .accBalanceBatchNo("cimccTMC_200428140254184788_flight_"+UTCTime.getBeijingDay(0))
                .orderNo(orderNo)
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
                .payType("月结")
                .lastUpdateTime(UTCTime.getBeijingTime(0,0))
                .orderType("月结火车票")
                .bookClerkName("测试人"+bookClerkEmployeeId)
                .bookClerkEmployeeId(bookClerkEmployeeId)
                .currency("CNY")
                .build();
        TrainBaseOrder trainBaseOrder = TrainBaseOrder.builder()
                .orderNo(trainOrderNo)
                .orderStatus("出票")
                .bookerSource("线上")
                .bookerName("测试人"+bookClerkEmployeeId)
                .bookerRank("p3")
                .orderType("电子")
                .payType("月结")
                //当前时间5天前预定
                .bookerTime(UTCTime.getBeijingTime(-5,0))
                .orderCostCenter1("成本中心1")
                .departments(dept)
                .build();
        //初始化一个乘客
        TrainPassengerInfo trainPassengerInfo = TrainPassengerInfo.builder()
                .passengerNo("1")
                .passengerName("测试人"+bookClerkEmployeeId)
                .passengerCode(bookClerkEmployeeId)
                .ticketName("测试人"+bookClerkEmployeeId)
                .passengerCostCenter1("成本中心1")
                .departments(dept)
                .build();
        ArrayList<TrainPassengerInfo> trainPassengerInfos =new ArrayList<>();
        //添加一个乘客
        trainPassengerInfos.add(trainPassengerInfo);
        //初始化票和乘客的信息
        TrainPassengerTicketCorrelation trainPassengerTicketCorrelation = TrainPassengerTicketCorrelation.builder()
                .orderNo(trainOrderNo)
                .passengerNo("测试人"+bookClerkEmployeeId)
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
                .departureTime(UTCTime.getBeijingTime(-3,0))
                .departureStationName("西安北站")
                //到达时间为出发时间的4小时后
                .arriveTime(UTCTime.getBeijingTime(-3,4))
                .arriveStationName("北京西站")
                .electronicOrderNo(RandomNumber.getUUID(10))
                .ticketType("原车次")
                .seatNum("05车07C")
                .seatType("209")
                .ticketActualFee(ticketFee)
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
        String info = GsonUtil.objectToString(trainSettlementInfos);
        JsonObject object = vendor.pushSettlementData(employee,"train",trainSettlementInfos,"cimccTMC","200428140254184788","cimccTMC");
        System.out.println(info);
    }


    @Test(description = "酒店结算数据 - 1人1间房")
    public void hotelSettlementDataTest3() throws HttpStatusException {
        //结算主键
        String recordId =String.valueOf(System.currentTimeMillis());
        //审批单号
        String approvalCode= "TA"+System.currentTimeMillis();
        //房费
        BigDecimal roomTotalRate = new BigDecimal(1000).setScale(2);
        //服务费
        BigDecimal serviceFee =new BigDecimal(50).setScale(2);
        //预定员工工号
        String bookClerkEmployeeId =String.valueOf(RandomNumber.getRandomNumber(1,1000));
        //部门
        ArrayList<String> dept =new ArrayList<>();
        dept.add("测试部门1");
        PassengerInfo passengerInfo = PassengerInfo.builder()
                .passengerName("测试人"+bookClerkEmployeeId)
                .passengerDepts(dept)
                .costCenter1("成本中心1")
                .build();
        ArrayList<PassengerInfo> passengerInfos =new ArrayList<>();
        passengerInfos.add(passengerInfo);
        HotelOrderSettlementInfo hotelOrderSettlementInfo=HotelOrderSettlementInfo.builder()
                .recordId(recordId)
                .supplierName("")
                .supplierCode("")
                .corpId("200428140254184788")
                .companyName("")
                .companyCode("")
                .companyOid("c7c1fd08-e2c7-4567-858e-b4f90be39f2d")
                .tenantId("")
                .tenantCode("")
                .tenantName("")
                .approvalCode(approvalCode)
                .batchNo("cimccTMC_200428140254184788_flight_"+UTCTime.getBeijingDay(0))
                .orderNo(RandomNumber.getTimeNumber())
                .createTime(UTCTime.getBeijingTime(0,0))
                .orderDate(UTCTime.getBeijingTime(0,0))
                .detailType("O")
                .hotelClass("N")
                .payType("1")
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
                .startTime(UTCTime.getBeijingTime(0,0))
                .endTime(UTCTime.getBeijingDate(5)+"12:00:00")
                .hotelName("全季酒店")
                .hotelNameEN("")
                .roomName("商务大床房")
                .cityName("上海")
                .star("3")
                .bookClerkName("测试人"+bookClerkEmployeeId)
                .bookClerkEmployeeId(bookClerkEmployeeId)
                .bookClerkDepts(dept)
                .passengerList(passengerInfos)
                .costCenter1("成本中心1")
                .build();
        ArrayList<HotelOrderSettlementInfo> hotelOrderSettlementInfos =new ArrayList<>();
        hotelOrderSettlementInfos.add(hotelOrderSettlementInfo);
        String info = GsonUtil.objectToString(hotelOrderSettlementInfos);
        System.out.println(info);
        vendor.pushSettlementData(employee,"hotel",hotelOrderSettlementInfos,"cimccTMC","200428140254184788","cimccTMC");
    }

}
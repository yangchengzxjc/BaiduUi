package com.test.api.testcase.vendor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SettlementBody;
import com.hand.basicObject.supplierObject.TrainOrderInfo.*;
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
 * @Date 2020/9/10
 * @Version 1.0
 **/
@Slf4j
public class TrainOrderDataTest extends BaseTest {

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

    @Test(description = "火车票1人预定,不改签-不退票")
    public void trainOrderDataTest2() throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        ArrayList<String> bookerDepartments =new ArrayList<>();
        //车票价
        BigDecimal ticketPrice =new BigDecimal(230).setScale(2);
        //电子客票号
        String trainElectronic = RandomNumber.getTimeNumber(10);
        bookerDepartments.add(employee.getDepartmentName());
        //订单基本信息
        TrainBaseOrder trainBaseOrder = TrainBaseOrder.builder()
                .orderType("B")
                .orderNo(orderNo)
                .originalOrderNum("")
                .supplierName("中集商旅")
                .supplierCode("cimccTMC")
                .approvalCode("TA"+System.currentTimeMillis())
                .orderStatusName("已出票")
                .orderStatusCode("TD")
                .tenantCode(employee.getTenantId())
                .tenantName(employee.getTenantName())
                .employeeNum(employee.getEmployeeID())
                .employeeName(employee.getFullName())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(employee.getDepartmentName())
                .bookChannel("Online-AP")
                .bookType("C")
                .payType("COPAY")
                .createTime(UTCTime.getBeijingTime(0,0))
                .payTime(UTCTime.getBeijingTime(0,0))
                .successTime(UTCTime.getBeijingTime(0,0))
                .paymentType("M")
                .accountType("C")
                .currency("CNY")
                .totalAmount(new BigDecimal(500).setScale(2))
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .remark("")
                .build();
        //订单车票信息
        TrainTicketInfo trainTicketInfo = TrainTicketInfo.builder()
                .orderNo(orderNo)
                .passengerNo("1")
                .sequenceNo("1")
                .trainNum("D1234")
                .trainElectronic(trainElectronic)
                .passengerType("AUD")
                .ticketPrice(ticketPrice)
                .servicePrice(new BigDecimal(20).setScale(2))
                .seatNum("05车07C")
                .seatType("209")
                .build();
        ArrayList<TrainTicketInfo> trainTicketInfos =new ArrayList<>();
        trainTicketInfos.add(trainTicketInfo);
        //订单车次
        TrainSequenceInfo trainSequenceInfo =TrainSequenceInfo.builder()
                .orderNo(orderNo)
                .sequenceNo("1")
                .trainNum("D123")
                .departureTime(UTCTime.getBeijingTime(3,0))
                .arriveTime(UTCTime.getBeijingTime(3,4))
                .dCityName("西安")
                .dCityCode("CHN061001000")
                .dStationName("西安北站")
                .aCityName("上海")
                .aCityCode("CHN031000000")
                .aStationName("虹桥火车站")
                .build();
        ArrayList<TrainSequenceInfo> trainSequenceInfos =new ArrayList<>();
        trainSequenceInfos.add(trainSequenceInfo);
        //订单乘客信息
        TrainPassengerInfo trainPassengerInfo = TrainPassengerInfo.builder()
                .orderNo(orderNo)
                .passengerNo("1")
                .passengerType("AUD")
                .passengerAttribute("I")
                .passengerName(employee.getFullName())
                .passengerNum(employee.getEmployeeID())
                .passengerDepartments(bookerDepartments)
                .departmentName(employee.getDepartmentName())
                .nationlityName("中国")
                .certificateType("IDC")
                .certificateNum("6101599468129501")
                .passengerPhone(employee.getMobile())
                .passengerEmail(employee.getEmail())
                .passengerSex("M")
                .build();
        ArrayList<TrainPassengerInfo> trainPassengerInfos =new ArrayList<>();
        trainPassengerInfos.add(trainPassengerInfo);
        //订单改签信息  有改签则传
//        TrainChangeInfo trainChangeInfo = TrainChangeInfo.builder()
//                .orderNo(orderNo)
//                .changeAmount(new BigDecimal(0).setScale(2))
//                .changeType("")
//                .changeReason("")
//                .changeMethod("")
//                .changeFee(new BigDecimal(0).setScale(2))
//                .changeRate(new BigDecimal(0).setScale(2))
//                .changeServiceFee(new BigDecimal(0).setScale(2))
//                .dCityName("西安")
//                .dCityCode("CHN061001000")
//                .dStationName("西安北站")
//                .aCityName("上海")
//                .aCityCode("CHN031000000")
//                .aStationName("虹桥火车站")
//                .build();
//        //订单退票 有退票则必传
//        TrainRefundInfo trainRefundInfo = TrainRefundInfo.builder()
//                .orderNo(orderNo)
//                .refundAmount(new BigDecimal(200).setScale(2))
//                .refundType("")
//                .refundReason("旅程修改")
//                .refundMethod("")
//                .refundFee(new BigDecimal(100).setScale(2))
//                .refundRate(new BigDecimal(20).setScale(2))
//                .refundServiceFee(new BigDecimal(100).setScale(2))
//                .build();
        TrainOrderInfoEntity trainOrderInfoEntity = TrainOrderInfoEntity.builder()
                .trainOrderBase(trainBaseOrder)
                .trainOrderTicketInfos(trainTicketInfos)
                .trainOrderSequenceInfos(trainSequenceInfos)
                .trainOrderPassengerInfos(trainPassengerInfos)
                .build();
        //推送的数据封装成一个json字符串
        String hotelOrderData =GsonUtil.objectToString(trainOrderInfoEntity);
        //转成jsonobject对象
        JsonObject hotelOrderDataObject =new JsonParser().parse(hotelOrderData).getAsJsonObject();
        //火车订单推送
        vendor.pushOrderData(employee,"train",trainOrderInfoEntity,"cimccTMC","200428140254184788","");
        SettlementBody settlementBody = SettlementBody.builder()
                .companyOid(employee.getCompanyOID())
                .orderNo(orderNo)
                .page(1)
                .size(10)
                .build();
        //查询订单数据
        JsonObject trainOrder = vendor.queryOrderData(employee,"train",settlementBody);
        log.info("train order Data:{}",trainOrder);
        //映射关系
        HashMap<String,String> mapping= new HashMap<>();
        mapping.put("trainOrderBase","trainBaseOrder");
        mapping.put("employeeNum","preEmployeeNum");
        mapping.put("employeeName","preEmployeeName");
        mapping.put("trainOrderTicketInfos","trainTicketInfo");
        mapping.put("trainOrderSequenceInfos","trainSequenceInfo");
        mapping.put("trainOrderPassengerInfos","trainPassengerInfo");
        mapping.put("dCityName","dcityName");
        mapping.put("dCityCode","dcityCode");
        mapping.put("dStationName","dstationName");
        mapping.put("aCityName","acityName");
        mapping.put("aCityCode","acityCode");
        mapping.put("aStationName","astationName");
        mapping.put("nationlityName","nationalityName");
        mapping.put(employee.getDepartmentName(),"产品三部");
        assert GsonUtil.compareJsonObject(hotelOrderDataObject,trainOrder,mapping);
        //校验预订人的
        assert trainOrder.getAsJsonObject("trainBaseOrder").get("preEmployeeOid").getAsString().equals(employee.getUserOID());
        //trainSequenceInfo 中的trainType
        String trainNum = trainOrder.getAsJsonArray("trainSequenceInfo").get(0).getAsJsonObject().get("trainNum").getAsString();
        String trainType=vendor.trainTypeMapping(trainNum);
        assert trainOrder.getAsJsonArray("trainSequenceInfo").get(0).getAsJsonObject().get("trainType").getAsString().equals(trainType);
        //trainPassengerInfo 中的乘客oid 对比
        assert trainOrder.getAsJsonArray("trainPassengerInfo").get(0).getAsJsonObject().get("passengerOid").getAsString().equals(employee.getUserOID());
    }
}

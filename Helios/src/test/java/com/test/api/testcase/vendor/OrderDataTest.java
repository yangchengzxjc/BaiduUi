package com.test.api.testcase.vendor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SettlementBody;
import com.hand.basicObject.supplierObject.airOrderInfo.*;
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
 * @Date 2020/9/4
 * @Version 1.0
 **/
@Slf4j
public class OrderDataTest extends BaseTest {

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

    @Test(description = "机票订单-单程-月结-不改签-不退票")
    public void flightOrderDataTest3() throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        ArrayList<String> bookerDepartments =new ArrayList<>();
        bookerDepartments.add(employee.getDepartmentName());
        //机票价格
        BigDecimal ticketPrice =new BigDecimal(1022).setScale(2);
        //燃油费
        BigDecimal oilFee = new BigDecimal(50).setScale(2);
        //基建费
        BigDecimal tax =new BigDecimal(20).setScale(2);
        //服务费
        BigDecimal serverFee = new BigDecimal(20).setScale(2);
        //电子客票号
        String trainElectronic = RandomNumber.getTimeNumber(10);
        bookerDepartments.add(employee.getDepartmentName());
        //票号
        String ticketNo = RandomNumber.getTimeNumber(13);
        BigDecimal amount = ticketPrice.add(oilFee).add(tax).add(serverFee);
        //订单基本信息
        AirBaseOrder airBaseOrder = AirBaseOrder.builder()
                .orderType("B")
                .orderNo(orderNo)
                .supplierName("中集商旅")
                .supplierCode("cimccTMC")
                .approvalCode("TA"+System.currentTimeMillis())
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
                .departmentName(employee.getDepartmentName())
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
                .currency("CNY")
                .amount(amount)
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .build();
        // 机票信息
        AirTicketInfo airTicketInfo = AirTicketInfo.builder()
                .ticketKey(trainElectronic)
                .passengerNo("1")
                .finalTicketTime(UTCTime.getBeijingTime(-10,1,0))
                .passengerType("AUT")
                .ticketPNR(RandomNumber.getUUID(5))
                .ticketNo(ticketNo)
                .ticketStatusName("已使用")
                .ticketStatusCode("USED")
                .isPolicy("N")
                .ticketPrice(ticketPrice)
                .variance(new BigDecimal(0).setScale(2))
                .priceRate(8.0)
                .oilFee(oilFee)
                .tax(tax)
                .serverFee(serverFee)
                .postServiceFee(new BigDecimal(0).setScale(2))
                .classType("Y")
                .subClass("A")
                .rerNotes("起飞前24小时免费")
                .refNotes("起飞前24小时免费")
                .endNotes("")
                .yClassStandardPrice("1500.00")
                .build();
        ArrayList<AirTicketInfo> airTicketInfos =new ArrayList<>();
        airTicketInfos.add(airTicketInfo);
        //航程信息
        AirFlightInfo airFlightInfo = AirFlightInfo.builder()
                .orderNo(orderNo)
                .sequence("1")
                .flight("MU2160")
                .airLineCode("MU")
                .airLineName("上海东方航空公司")
                .takeoffTime(UTCTime.getBeijingTime(-5,0,0))
                .arrivalTime(UTCTime.getBeijingTime(-5,2,0))
                .classType("Y")
                .subClass("A")
                .dcityName("西安")
                .dcityCode("CHN061001000")
                .dportName("西安咸阳国际机场")
                .dportCode("XIY")
                .dairportName("T3航站楼")
                .acityName("上海")
                .acityCode("CHN031000000")
                .aportName("上海虹桥国际机场")
                .aportCode("SHA")
                .aairportName("T2航站楼")
                .stopCity("")
                .airPort("")
                .stopTime("")
                .flightTime("3h")
                .tpm(1345)
                .craftType("空客320")
                .build();
        ArrayList<AirFlightInfo> airFlightInfos =new ArrayList<>();
        airFlightInfos.add(airFlightInfo);
        //乘机人信息
        AirPassengerInfo airPassengerInfo = AirPassengerInfo.builder()
                .orderNo(orderNo)
                .passengerNo("1")
                .passengerType("AUT")
                .passengerAttribute("I")
                .passengerName(employee.getFullName())
                .passengerNum(employee.getEmployeeID())
                .passengerDepartments(bookerDepartments)
                .departmentName(employee.getDepartmentName())
                .nationlityName("中国")
                .certificateType("IDC")
                .certificateNum("610"+System.currentTimeMillis())
                .passengerPhone(employee.getPhoneNumber())
                .passengerEmail(employee.getEmail())
                .passengerSex("M")
                .passengerDepartments(bookerDepartments)
                .build();
        ArrayList<AirPassengerInfo> airPassengerInfos =new ArrayList<>();
        airPassengerInfos.add((airPassengerInfo));
        //订单改签信息  有改签则传
//        AirChangeInfo airChangeInfo = AirChangeInfo.builder()
//                .orderNo(orderNo)
//                .changeAmount(new BigDecimal(0).setScale(2))
//                .changeType("")
//                .changeReason("")
//                .changeMethod("")
//                .changeFee(new BigDecimal(0).setScale(2))
//                .changeRate(new BigDecimal(0).setScale(2))
//                .changeServiceFee(new BigDecimal(0).setScale(2))
//                .changeDifference(new BigDecimal(200).setScale(2))
//                .changeServiceFee(new BigDecimal(50).setScale(2))
//                .build();
        //订单退票 有退票则必传
//        AirRefundInfo trainRefundInfo = AirRefundInfo.builder()
//                .orderNo(orderNo)
//                .refundAmount(new BigDecimal(200).setScale(2))
//                .refundType("")
//                .refundReason("旅程修改")
//                .refundMethod("")
//                .refundFee(new BigDecimal(100).setScale(2))
//                .refundRate(new BigDecimal(20).setScale(2))
//                .refundServiceFee(new BigDecimal(100).setScale(2))
//                .build();
        //行程单打印以及配送信息
        AirTicketPrint airTicketPrint = AirTicketPrint.builder()
                .ticketKey(trainElectronic)
                .ticketNo(ticketNo)
                .passengerName(employee.getFullName())
                .printNo(RandomNumber.getTimeNumber(7))
                .printTime(UTCTime.getBeijingTime(-5,6,0))
                .expressNo(RandomNumber.getTimeNumber(14))
                .expressCompany("中通快递")
                .expressFee(new BigDecimal(0).setScale(2))
                .expressServiceFee(new BigDecimal(0).setScale(2))
                .build();
        ArrayList<AirTicketPrint> airTicketPrints =new ArrayList<>();
        airTicketPrints.add(airTicketPrint);
        //保险信息
        AirInsurance airInsurance = AirInsurance.builder()
                .ticketKey(trainElectronic)
                .sequence("1")
                .insuranceFee(new BigDecimal(50).setScale(2))
                .insuranceStatus("P")
                .insuranceStatusDec("已出保")
                .insuaranceUnitPrice(new BigDecimal(50).setScale(2))
                .insuaranceQuantity(1)
                .insuaranceName("中国平安人身意外险")
                .insuaranceCompanyName("中国平安保险集团")
                .build();
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
        vendor.pushOrderData(employee,"flight",airOrderInfoEntity,"cimccTMC","200428140254184788","");
        SettlementBody settlementBody = SettlementBody.builder()
                .companyOid(employee.getCompanyOID())
                .orderNo(orderNo)
                .page(1)
                .size(10)
                .build();
        //查询订单数据
        JsonObject  trainOrder = vendor.queryOrderData(employee,"flight",settlementBody);
        log.info("flight order Data:{}",trainOrder);
        //先对比需要删除的数据
        assert trainOrder.getAsJsonObject("airBaseOrder").get("flightWay").getAsString().equals(flightOrderDataObject.getAsJsonObject("airBaseOrder").get("flightWay").getAsString());
        assert trainOrder.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().get("isPolicy").getAsString().equals(flightOrderDataObject.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().get("isPolicy").getAsString());
        assert trainOrder.getAsJsonArray("airFlightInfo").get(0).getAsJsonObject().get("tpm").getAsString().equals(flightOrderDataObject.getAsJsonArray("airFlightInfo").get(0).getAsJsonObject().get("rpm").getAsString());
        //先删除航程类型字段 因为单程映射会重复 删除完后单独比较   协议价  里程数
        flightOrderDataObject.getAsJsonObject("airBaseOrder").remove("flightWay");
        flightOrderDataObject.getAsJsonArray("airTicketInfo").get(0).getAsJsonObject().remove("isPolicy");
        flightOrderDataObject.getAsJsonArray("airFlightInfo").get(0).getAsJsonObject().remove("tpm");
        //不需要检查的字段为 机票保险中的ticketKey
        flightOrderDataObject.getAsJsonArray("airInsurance").get(0).getAsJsonObject().remove("ticketKey");
        //映射数据
        HashMap<String,String> mapping =new HashMap<>();
        mapping.put("S","BS");
        mapping.put("N","国内航班");
        mapping.put("yClassStandardPrice","yclassStandardPrice");
        mapping.put("flight","flightNo");
        mapping.put("employeeId","preEmployeeId");
        mapping.put(employee.getDepartmentName(),"产品三部");
        assert GsonUtil.compareJsonObject(flightOrderDataObject,trainOrder,mapping);
        //对比预订人的oid
        assert trainOrder.getAsJsonObject("airBaseOrder").get("preEmployeeOid").getAsString().equals(employee.getUserOID());
    }
}

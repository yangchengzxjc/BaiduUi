package com.test.api.method.VendorMethod;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.TrainOrderInfo.*;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.InfraStructure;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/10
 * @Version 1.0
 **/
@Slf4j
public class TrainOrder {
    private ExpenseReportComponent component;
    private InfraStructure infraStructure;

    public TrainOrder(){
        component = new ExpenseReportComponent();
        infraStructure = new InfraStructure();
    }

    /**
     * 火车订单基本信息
     * @param employee
     * @param orderStatusName
     * @param orderType
     * @param orderNo
     * @param bookChannel
     * @param bookType
     * @param payType
     * @return
     */
    public TrainBaseOrder setTrainBaseOrder(Employee employee,BigDecimal totalAmount,String orderStatusName,String orderType,String orderNo,String bookChannel,String bookType,String payType) throws HttpStatusException {
        String paymentType ="";
        String accountType ="";
        if(bookType.equals("C")){
            paymentType="M";
            accountType="C";
        }else{
            paymentType="N";
            accountType="P";
        }
        //原订单号
        String originalOrderNum = "";
        log.info("orderType:{}",orderType);
        if(!orderType.equals("B")){
            originalOrderNum = RandomNumber.getTimeNumber(14);
        }
        HashMap<String,String> orderStatusMapping =new HashMap<>();
        orderStatusMapping.put("未提交","N");
        orderStatusMapping.put("待支付","WP");
        orderStatusMapping.put("支付处理中","PP");
        orderStatusMapping.put("支付失败","PF");
        orderStatusMapping.put("购票中","TP");
        orderStatusMapping.put("已购票","TD");
        orderStatusMapping.put("出票失败","TF");
        orderStatusMapping.put("已取消","C");
        orderStatusMapping.put("退票处理中","P");
        orderStatusMapping.put("退票成功","S");
        orderStatusMapping.put("退票失败","F");
        orderStatusMapping.put("改签处理中","P");
        orderStatusMapping.put("改签成功","S");
        orderStatusMapping.put("改签取消","C");
        String deptCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        //订单基本信息
        TrainBaseOrder trainBaseOrder = TrainBaseOrder.builder()
                .orderType(orderType)
                .orderNo(orderNo)
                .originalOrderNum(originalOrderNum)
                .supplierName("中集商旅")
                .supplierCode("cimccTMC")
                .approvalCode("TA"+RandomNumber.getTimeNumber(8)+"-1"+"-T")
                .orderStatusName(orderStatusName)
                .orderStatusCode(orderStatusMapping.get(orderStatusName))
                .tenantCode(employee.getTenantId())
                .tenantName(employee.getTenantName())
                .employeeNum(employee.getEmployeeID())
                .employeeName(employee.getFullName())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(employee.getDepartmentName())
                .departmentCode(deptCode)
                .bookChannel(bookChannel)
                .bookType(bookType)
                .payType(payType)
                .costCenter("")
                .createTime(UTCTime.getBeijingTime(0,0,0))
                .payTime(UTCTime.getBeijingTime(0,0,1))
                .successTime(UTCTime.getBeijingTime(0,0,2))
                .paymentType(paymentType)
                .accountType(accountType)
                .costCenter("成本中心1")
                .currency("CNY")
                .totalAmount(totalAmount)
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .remark("")
                .build();
        return trainBaseOrder;
    }

    /**
     * 火车订单基本信息
     * @param employee
     * @param orderType
     * @param orderNo
     * @return
     */
    public TrainBaseOrder setTrainBaseOrder(Employee employee, String supplierName,String supplierCode, BigDecimal totalAmount,String orderType, String orderNo, JsonObject tmcData,JsonObject applicant) throws HttpStatusException {
        //原订单号
        String originalOrderNum = "";
        if(!orderType.equals("B")){
            originalOrderNum = RandomNumber.getTimeNumber(14);
        }
        ArrayList<String> bookerDepartments = new ArrayList<>();
        bookerDepartments.add(applicant.get("departmentName").getAsString());
        JsonObject bookClerk = infraStructure.getUserDetail(employee,tmcData.getAsJsonObject("bookClerk").get("employeeID").getAsString());
        //部门code
        String deptCode = infraStructure.getDeptCode(employee,bookClerk.get("departmentOID").getAsString());
        //订单基本信息
        TrainBaseOrder trainBaseOrder = TrainBaseOrder.builder()
                .orderType(orderType)
                .orderNo(orderNo)
                .originalOrderNum(originalOrderNum)
                .supplierName(supplierName)
                .supplierCode(supplierCode)
                .approvalCode(tmcData.get("approvalCode").getAsString())
                .orderStatusName("已购票")
                .orderStatusCode("TD")
                .tenantCode(tmcData.get("tenantId").getAsString())
                .tenantName(employee.getTenantName())
                .employeeNum(tmcData.getAsJsonObject("bookClerk").get("employeeID").getAsString())
                .employeeName(tmcData.getAsJsonObject("bookClerk").get("name").getAsString())
                .companyName(applicant.get("companyName").getAsString())
                .companyCode(employee.getCompanyCode())
                .departmentName(applicant.get("departmentName").getAsString())
                .departmentCode(deptCode)
                .bookChannel("Online-APP")
                .bookType("C")
                .payType("ALIPAY")
                .createTime(UTCTime.getBeijingTime(0,0,0))
                .payTime(UTCTime.getBeijingTime(0,0,1))
                .successTime(UTCTime.getBeijingTime(0,0,1))
                .paymentType("M")
                .accountType("C")
                .costCenter(tmcData.get("costCenter1").getAsString())
                .currency("CNY")
                .totalAmount(totalAmount)
                .contactName(tmcData.getAsJsonObject("bookClerk").get("name").getAsString())
                .contactPhone(tmcData.getAsJsonObject("bookClerk").get("mobile").getAsString())
                .contactEmail(employee.getEmail())
                .remark(tmcData.get("remark").getAsString())
                .build();
        return trainBaseOrder;
    }

    /**
     * 火车订单改签信息
     * @param orderNo 订单号
     * @param changeReason 改签信息
     * @param dCityName 出发城市名称
     * @param aCityName 到达城市名称
     * @param aStationName 到达火车站名称
     * @param dStationName 出发火车站名称
     * @return
     */
    public TrainChangeInfo setTrainChangeInfo(Employee employee,String orderNo,String changeReason,String dCityName,String aCityName,String dStationName,String aStationName) throws HttpStatusException {
        //订单改签信息  有改签则传
        String dCityCode=component.getCityCode(employee,dCityName);
        String aCityCode = component.getCityCode(employee,aCityName);
        TrainChangeInfo trainChangeInfo = TrainChangeInfo.builder()
                .orderNo(orderNo)
                .changeAmount(new BigDecimal(100).setScale(2))
                .changeType("")
                .changeReason(changeReason)
                .changeMethod("")
                .changeFee(new BigDecimal(100).setScale(2))
                .changeRate(new BigDecimal(20).setScale(2))
                .changeServiceFee(new BigDecimal(0).setScale(2))
                .dCityName(dCityName)
                .dCityCode(dCityCode)
                .dStationName(dStationName)
                .aCityName(aCityName)
                .aCityCode(aCityCode)
                .aStationName(aStationName)
                .build();
        return trainChangeInfo;
    }

    /**
     * 火车订单退票
     * @param orderNo 订单号
     * @param refundAmount  应退客户的金额
     * @param refundFee   铁路局应收退票费用
     * @return
     */
    public TrainRefundInfo setTrainRefundInfo(String orderNo,BigDecimal refundAmount,BigDecimal refundFee){

        //订单退票 有退票则必传
        TrainRefundInfo trainRefundInfo = TrainRefundInfo.builder()
                .orderNo(orderNo)
                .refundAmount(refundAmount)
                .refundType("")
                .refundReason("时间原因导致必须退款")
                .refundMethod("")
                .refundFee(refundFee)
                .refundRate(new BigDecimal(20))
                .refundServiceFee(new BigDecimal(10).setScale(2))
                .build();
        return trainRefundInfo;
    }

    /**
     * 火车订单超标信息
     * @param trainElectronic  电子客票号
     * @return
     */
    public TrainExceedInfo setExceedInfo(String trainElectronic){
        TrainExceedInfo trainExceedInfo =TrainExceedInfo.builder()
                .trainElectronic(trainElectronic)
                .violationContentCode("A1234")
                .violationContentName("基准价超标")
                .violationReasonCode("B1234")
                .violationReasonName("机场选择")
                .build();
        return trainExceedInfo;
    }

    /**订单乘客信息
     * @param orderNo  订单号
     * @param passengerNo  乘客序号
     * @param passengerName  乘客姓名
     * @param passagerNum  乘客工号
     * @param bookerDepartments  乘客部门
     * @param departmentName 乘客部门名称
     * @param certificateNum 身份证号码
     * @param passengerPhone   手机号
     * @param passengerEmail   邮箱
     * @return
     */
    public TrainPassengerInfo setTrainPassengerInfo(String orderNo, String passengerNo, String passengerAttribute,String passengerName, String passagerNum, List<String> bookerDepartments,String departmentName,String departmentCode,String certificateNum,String passengerPhone,String passengerEmail){
        //订单乘客信息
        TrainPassengerInfo trainPassengerInfo = TrainPassengerInfo.builder()
                .orderNo(orderNo)
                .passengerNo(passengerNo)
                .passengerType("AUD")
                .passengerAttribute(passengerAttribute)
                .passengerName(passengerName)
                .passengerNum(passagerNum)
                .passengerDepartments(bookerDepartments)
                .departmentName(departmentName)
                .departmentCode(departmentCode)
                .nationlityName("中国")
                .certificateType("IDC")
                .certificateNum(certificateNum)
                .passengerPhone(passengerPhone)
                .passengerEmail(passengerEmail)
                .passengerSex("M")
                //成本中心
                .passengerCostCenter("管理综合部")
                .build();
        return trainPassengerInfo;
    }

    /**订单乘客信息
     * @param orderNo  订单号
     * @param passengerNo  乘客序号
     *
     * @return
     */
    public TrainPassengerInfo setTrainPassengerInfo(Employee employee,String orderNo, String passengerNo,JsonObject tmcRequestData,JsonObject applicationParticipant) throws HttpStatusException {
        JsonObject tmcParticipant = tmcRequestData.getAsJsonArray("participantList").get(0).getAsJsonObject();
        //订单乘客信息
        //乘机人信息
        JsonObject participantInfo = infraStructure.getEmployeeDetail(employee,applicationParticipant.get("participantOID").getAsString());
        //乘机人信息
        ArrayList<String> bookerDepartments =new ArrayList<>();
        bookerDepartments.add(participantInfo.get("departmentName").getAsString());
        //身份证信息
        JsonObject cardInfo = infraStructure.queryUserCard(employee,applicationParticipant.get("participantOID").getAsString(),"身份证");
        //部门code
        String deptCode = infraStructure.getDeptCode(employee,participantInfo.get("departmentOID").getAsString());
        TrainPassengerInfo trainPassengerInfo = TrainPassengerInfo.builder()
                .orderNo(orderNo)
                .passengerNo(passengerNo)
                .passengerType("AUD")
                .passengerAttribute("I")
                .passengerName(tmcParticipant.get("name").getAsString())
                .passengerNum(tmcParticipant.get("employeeID").getAsString())
                .passengerDepartments(bookerDepartments)
                .departmentName(participantInfo.get("departmentName").getAsString())
                .departmentCode(deptCode)
                .nationlityName("中国")
                .certificateType("IDC")
                .certificateNum(cardInfo.get("originalCardNo").getAsString())
                .passengerPhone(tmcParticipant.get("mobile").getAsString())
                .passengerEmail(tmcParticipant.get("email").getAsString())
                .passengerSex(tmcParticipant.get("gender").getAsString())
                //成本中心
                .passengerCostCenter(tmcRequestData.get("costCenter1").getAsString())
                .build();
        return trainPassengerInfo;
    }

    /**
     * 火车订单信息
     * @param employee
     * @param orderNo
     * @param sequenceNo
     * @param dCityName
     * @param aCityName
     * @param dStationName
     * @return
     * @throws HttpStatusException
     */
    public TrainSequenceInfo setTrainSequenceInfo(Employee employee,String orderNo,String sequenceNo,String trainNum,String dCityName,String aCityName,String dStationName,String aStationName) throws HttpStatusException {
        String dCityCode = component.getCityCode(employee,dCityName);
        String aCityCode = component.getCityCode(employee,aCityName);
        //订单车次
        TrainSequenceInfo trainSequenceInfo =TrainSequenceInfo.builder()
                .orderNo(orderNo)
                .sequenceNo(sequenceNo)
                .trainNum(trainNum)
                .departureTime(UTCTime.getBeijingTime(3,0,0))
                .arriveTime(UTCTime.getBeijingTime(3,4,0))
                .dcityName(dCityName)
                .dcityCode(dCityCode)
                .dstationName(dStationName)
                .acityName(aCityName)
                .acityCode(aCityCode)
                .astationName(aStationName)
                .build();
        return trainSequenceInfo;
    }

    /**
     * 火车订单信息
     * @param orderNo
     * @param sequenceNo
     * @return
     * @throws HttpStatusException
     */
    public TrainSequenceInfo setTrainSequenceInfo(String orderNo,String sequenceNo,JsonObject tmcData) throws HttpStatusException {
        HashMap<String,String> station = new HashMap<>();
        station.put("西安市","西安北站");
        station.put("上海","上海虹桥火车站");
        station.put("深圳","深圳火车站");
        station.put("北京","北京西站");
        JsonObject travelTrain = tmcData.getAsJsonArray("travelTrainsList").get(0).getAsJsonObject();
        String startTime = UTCTime.BJDateMdy(travelTrain.get("fromDate").getAsString().split("\\s+")[1],travelTrain.get("floatDaysBegin").getAsInt())+" "+UTCTime.getTime(0,0);
        String endTime = UTCTime.BJDateMdy(travelTrain.get("leaveDate").getAsString().split("\\s+")[1],-(travelTrain.get("floatDaysBegin").getAsInt()))+" "+UTCTime.getTime(2,30);
        //订单车次
        TrainSequenceInfo trainSequenceInfo =TrainSequenceInfo.builder()
                .orderNo(orderNo)
                .sequenceNo(sequenceNo)
                .trainNum("D1234")
                .departureTime(startTime)
                .arriveTime(endTime)
                .dcityName(tmcData.get("fromCity").getAsString())
                .dcityCode(tmcData.get("fromCityCode").getAsString())
                .dstationName(station.get(tmcData.get("fromCity").getAsString()))
                .acityName(tmcData.get("toCity").getAsString())
                .acityCode(tmcData.get("toCityCode").getAsString())
                .astationName(station.get(tmcData.get("toCity").getAsString()))
                .build();
        return trainSequenceInfo;
    }

    /**
     * 订单车车票信息
     * @param orderNo  订单号
     * @param passengerNo  乘客序号
     * @param trainElectronic   电子客票号
     * @param ticketPrice 车票票价
     * @param seatNum 座位号
     * @return
     */
    public TrainTicketInfo setTrainTicketInfo(String orderNo,String trainNum,String passengerNo,String trainElectronic,BigDecimal ticketPrice,String seatNum){
        //订单车票信息
        TrainTicketInfo trainTicketInfo = TrainTicketInfo.builder()
                .orderNo(orderNo)
                .passengerNo(passengerNo)
                .sequenceNo("1")
                .trainNum(trainNum)
                .trainElectronic(trainElectronic)
                .passengerType("AUD")
                .ticketPrice(ticketPrice)
                .servicePrice(new BigDecimal(20).setScale(2))
                .seatNum(seatNum)
                .seatType("209")
                .build();
        return trainTicketInfo;
    }

}

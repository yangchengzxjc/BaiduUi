package com.test.api.testcase.vendor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SettlementBody;
import com.hand.basicObject.supplierObject.TrainOrderInfo.*;
import com.hand.utils.GsonUtil;
import com.hand.utils.RandomNumber;
import com.test.BaseTest;
import com.test.api.method.InfraStructure;
import com.test.api.method.Vendor;
import com.test.api.method.VendorMethod.TrainOrder;
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
    private TrainOrder trainOrder;
    private InfraStructure infraStructure;


    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("yaliang.wang@cimc.com") String phoneNumber, @Optional("111111") String pwd, @Optional("stage") String env){
        employee = getEmployee(phoneNumber,pwd,env);
        vendor = new Vendor();
        trainOrder = new TrainOrder();
        infraStructure = new InfraStructure();
    }

    @Test(description = "火车票1人预定,不改签-不退票-公司支付-月结")
    public void trainOrderDataTest1() throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        ArrayList<String> bookerDepartments =new ArrayList<>();
        //车票价
        BigDecimal ticketPrice =new BigDecimal(690).setScale(2);
        //服务费
        BigDecimal servicePrice = new BigDecimal(20).setScale(2);
        BigDecimal totalAmount =ticketPrice.add(servicePrice);
        //电子客票号
        String trainElectronic = RandomNumber.getTimeNumber(10);
        bookerDepartments.add(employee.getDepartmentName());
        //订单基本信息
        TrainBaseOrder trainBaseOrder =trainOrder.setTrainBaseOrder(employee,totalAmount,"已购票","B",orderNo,"Online-APP","C","COPAY");
        //订单车票信息
        TrainTicketInfo trainTicketInfo = trainOrder.setTrainTicketInfo(orderNo,"D1234","1",trainElectronic,"",ticketPrice,"05车07C");
        ArrayList<TrainTicketInfo> trainTicketInfos =new ArrayList<>();
        trainTicketInfos.add(trainTicketInfo);
        //订单车次
        TrainSequenceInfo trainSequenceInfo = trainOrder.setTrainSequenceInfo(employee,orderNo,"1","D1234","西安市","上海","西安北站","虹桥火车站");
        ArrayList<TrainSequenceInfo> trainSequenceInfos =new ArrayList<>();
        trainSequenceInfos.add(trainSequenceInfo);
        //订单乘客信息
        //查询部门code
        String deptCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        TrainPassengerInfo trainPassengerInfo =trainOrder.setTrainPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),bookerDepartments,employee.getDepartmentName(),deptCode,"6101599468129501",employee.getPhoneNumber(),employee.getEmail());
        ArrayList<TrainPassengerInfo> trainPassengerInfos =new ArrayList<>();
        trainPassengerInfos.add(trainPassengerInfo);
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
        mapping.put("nationlityName","nationalityName");
        mapping.put(employee.getDepartmentName(),"产品三组");
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


    @Test(description = "火车票1人预定,不改签-不退票-因私-个人支付宝支付-现付")
    public void trainOrderDataTest2() throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        ArrayList<String> bookerDepartments =new ArrayList<>();
        //车票价
        BigDecimal ticketPrice =new BigDecimal(690).setScale(2);
        //服务费
        BigDecimal servicePrice = new BigDecimal(20).setScale(2);
        BigDecimal totalAmount =ticketPrice.add(servicePrice);
        //电子客票号
        String trainElectronic = RandomNumber.getTimeNumber(10);
        bookerDepartments.add(employee.getDepartmentName());
        //订单基本信息
        TrainBaseOrder trainBaseOrder =trainOrder.setTrainBaseOrder(employee,totalAmount,"已出票","B",orderNo,"Online-APP","P","ALIPAY");
        //订单车票信息
        TrainTicketInfo trainTicketInfo = trainOrder.setTrainTicketInfo(orderNo,"D1234","1",trainElectronic,"",ticketPrice,"05车07C");
        ArrayList<TrainTicketInfo> trainTicketInfos =new ArrayList<>();
        trainTicketInfos.add(trainTicketInfo);
        //订单车次
        TrainSequenceInfo trainSequenceInfo = trainOrder.setTrainSequenceInfo(employee,orderNo,"1","D1234","西安市","上海","西安北站","虹桥火车站");
        ArrayList<TrainSequenceInfo> trainSequenceInfos =new ArrayList<>();
        trainSequenceInfos.add(trainSequenceInfo);
        //订单乘客信息
        //查询部门code
        String deptCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        TrainPassengerInfo trainPassengerInfo =trainOrder.setTrainPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),bookerDepartments,employee.getDepartmentName(),deptCode,"6101599468129501",employee.getPhoneNumber(),employee.getEmail());
        ArrayList<TrainPassengerInfo> trainPassengerInfos =new ArrayList<>();
        trainPassengerInfos.add(trainPassengerInfo);
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
        mapping.put("nationlityName","nationalityName");
        mapping.put(employee.getDepartmentName(),"产品三组");
        assert GsonUtil.compareJsonObject(hotelOrderDataObject,trainOrder,mapping);
        //校验预订人的工号
        assert trainOrder.getAsJsonObject("trainBaseOrder").get("preEmployeeOid").getAsString().equals(employee.getUserOID());
        //trainSequenceInfo 中的trainType
        String trainNum = trainOrder.getAsJsonArray("trainSequenceInfo").get(0).getAsJsonObject().get("trainNum").getAsString();
        String trainType=vendor.trainTypeMapping(trainNum);
        assert trainOrder.getAsJsonArray("trainSequenceInfo").get(0).getAsJsonObject().get("trainType").getAsString().equals(trainType);
        //trainPassengerInfo 中的乘客oid 对比
        assert trainOrder.getAsJsonArray("trainPassengerInfo").get(0).getAsJsonObject().get("passengerOid").getAsString().equals(employee.getUserOID());
    }

    @Test(description = "火车票-一人预定多人车票-不改签-不退票-因公-公司账户-月结")
    public void trainOrderDataTest3() throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        ArrayList<String> bookerDepartments =new ArrayList<>();
        //车票价
        BigDecimal ticketPrice =new BigDecimal(690).setScale(2);
        //服务费
        BigDecimal servicePrice = new BigDecimal(20).setScale(2);
        //订单总价
        BigDecimal totalAmount =ticketPrice.add(ticketPrice).add(servicePrice).setScale(2);
        //电子客票号1
        String trainElectronic1 = RandomNumber.getTimeNumber(10);
        //电子客票号2
        String trainElectronic2 = RandomNumber.getTimeNumber(10);
        //获取一个员工的信息
        JsonObject employeeInfo = infraStructure.getUserDetail(employee,"01399315");
        bookerDepartments.add(employee.getDepartmentName());
        //订单基本信息
        TrainBaseOrder trainBaseOrder =trainOrder.setTrainBaseOrder(employee,totalAmount,"已出票","B",orderNo,"Online-APP","C","COPAY");
        //订单车票信息 两张车票
        TrainTicketInfo trainTicketInfo1 = trainOrder.setTrainTicketInfo(orderNo,"D1234","1",trainElectronic1,"",ticketPrice,"05车07C");
        TrainTicketInfo trainTicketInfo2 = trainOrder.setTrainTicketInfo(orderNo,"D1234","2",trainElectronic2,"",ticketPrice,"05车07B");
        ArrayList<TrainTicketInfo> trainTicketInfos =new ArrayList<>();
        trainTicketInfos.add(trainTicketInfo1);
        trainTicketInfos.add(trainTicketInfo2);
        //订单车次
        TrainSequenceInfo trainSequenceInfo = trainOrder.setTrainSequenceInfo(employee,orderNo,"1","D1234","西安市","上海","西安北站","虹桥火车站");
        ArrayList<TrainSequenceInfo> trainSequenceInfos =new ArrayList<>();
        trainSequenceInfos.add(trainSequenceInfo);
        //订单乘客信息
        //查询部门code
        String deptCode1 = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        TrainPassengerInfo trainPassengerInfo1 =trainOrder.setTrainPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),bookerDepartments,employee.getDepartmentName(),deptCode1,"6101599468129501",employee.getPhoneNumber(),employee.getEmail());
        String deptCode2 = infraStructure.getDeptCode(employee,employeeInfo.get("departmentOID").getAsString());
        TrainPassengerInfo trainPassengerInfo2 =trainOrder.setTrainPassengerInfo(orderNo,"2","I",employeeInfo.get("fullName").getAsString(),employeeInfo.get("employeeID").getAsString(),bookerDepartments,employeeInfo.get("departmentPath").getAsString(),deptCode2,"62301599468129501",employeeInfo.get("mobile").getAsString(),employeeInfo.get("email").getAsString());
        ArrayList<TrainPassengerInfo> trainPassengerInfos =new ArrayList<>();
        trainPassengerInfos.add(trainPassengerInfo1);
        trainPassengerInfos.add(trainPassengerInfo2);
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
        JsonObject getTrainOrder = vendor.queryOrderData(employee,"train",settlementBody);
        log.info("train order Data:{}",getTrainOrder);
        //映射关系
        HashMap<String,String> mapping= new HashMap<>();
        mapping.put("trainOrderBase","trainBaseOrder");
        mapping.put("employeeNum","preEmployeeNum");
        mapping.put("employeeName","preEmployeeName");
        mapping.put("trainOrderTicketInfos","trainTicketInfo");
        mapping.put("trainOrderSequenceInfos","trainSequenceInfo");
        mapping.put("trainOrderPassengerInfos","trainPassengerInfo");
        mapping.put("nationlityName","nationalityName");
        mapping.put(employee.getDepartmentName(),"产品三组");
        assert GsonUtil.compareJsonObject(hotelOrderDataObject,getTrainOrder,mapping);
        //校验预订人的工号
        assert getTrainOrder.getAsJsonObject("trainBaseOrder").get("preEmployeeOid").getAsString().equals(employee.getUserOID());
        //trainSequenceInfo 中的trainType
        String trainNum = getTrainOrder.getAsJsonArray("trainSequenceInfo").get(0).getAsJsonObject().get("trainNum").getAsString();
        String trainType=vendor.trainTypeMapping(trainNum);
        assert getTrainOrder.getAsJsonArray("trainSequenceInfo").get(0).getAsJsonObject().get("trainType").getAsString().equals(trainType);
        //trainPassengerInfo 中的乘客oid 对比
        assert getTrainOrder.getAsJsonArray("trainPassengerInfo").get(0).getAsJsonObject().get("passengerOid").getAsString().equals(employee.getUserOID());
        //乘客2号校验
        assert getTrainOrder.getAsJsonArray("trainPassengerInfo").get(1).getAsJsonObject().get("passengerOid").getAsString().equals(employeeInfo.get("userOID").getAsString());
    }

    @Test(description = "火车票1人预定,改签-公司支付-月结")
    public void trainOrderDataTest4() throws HttpStatusException {
        //原订单号
        String orderNo = RandomNumber.getTimeNumber(14);
        ArrayList<String> bookerDepartments =new ArrayList<>();
        //车票价
        BigDecimal ticketPrice =new BigDecimal(690).setScale(2);
        //服务费
        BigDecimal servicePrice = new BigDecimal(20).setScale(2);
        BigDecimal totalAmount =ticketPrice.add(servicePrice);
        //电子客票号
        String trainElectronic = RandomNumber.getTimeNumber(10);
        String preTrainElectronic = RandomNumber.getTimeNumber(10);
        bookerDepartments.add(employee.getDepartmentName());
        //订单基本信息
        TrainBaseOrder trainBaseOrder =trainOrder.setTrainBaseOrder(employee,totalAmount,"改签成功","C",orderNo,"Online-APP","C","COPAY");
        //订单车票信息
        TrainTicketInfo trainTicketInfo = trainOrder.setTrainTicketInfo(orderNo,"G1234","1",trainElectronic,preTrainElectronic,ticketPrice,"05车07C");
        ArrayList<TrainTicketInfo> trainTicketInfos =new ArrayList<>();
        trainTicketInfos.add(trainTicketInfo);
        //订单车次
        TrainSequenceInfo trainSequenceInfo = trainOrder.setTrainSequenceInfo(employee,orderNo,"1","D1234","西安市","上海","西安北站","虹桥火车站");
        ArrayList<TrainSequenceInfo> trainSequenceInfos =new ArrayList<>();
        trainSequenceInfos.add(trainSequenceInfo);
        //订单乘客信息
        String deptCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        TrainPassengerInfo trainPassengerInfo =trainOrder.setTrainPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),bookerDepartments,employee.getDepartmentName(),deptCode,"6101599468129501",employee.getPhoneNumber(),employee.getEmail());
        ArrayList<TrainPassengerInfo> trainPassengerInfos =new ArrayList<>();
        trainPassengerInfos.add(trainPassengerInfo);
        //订单改签
        TrainChangeInfo trainChangeInfo = trainOrder.setTrainChangeInfo(employee,orderNo,"更换高铁更快","西安市","上海","西安北站","虹桥火车站");
        ArrayList <TrainChangeInfo> trainChangeInfos =new ArrayList<>();
        trainChangeInfos.add(trainChangeInfo);

        TrainOrderInfoEntity trainOrderInfoEntity = TrainOrderInfoEntity.builder()
                .trainOrderBase(trainBaseOrder)
                .trainOrderTicketInfos(trainTicketInfos)
                .trainOrderSequenceInfos(trainSequenceInfos)
                .trainOrderPassengerInfos(trainPassengerInfos)
                .trainOrderChangeInfos(trainChangeInfos)
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
        mapping.put("trainOrderChangeInfos","trainChangeInfo");
        mapping.put("nationlityName","nationalityName");
        mapping.put("dCityName","dcityName");
        mapping.put("dCityCode","dcityCode");
        mapping.put("aCityName","acityName");
        mapping.put("aCityCode","acityCode");
        mapping.put("dStationName","dstationName");
        mapping.put("aStationName","astationName");
        mapping.put(employee.getDepartmentName(),"产品三组");
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

    @Test(description = "火车票1人预定-订单退票（退票订单）-公司支付-月结")
    public void trainOrderDataTest5() throws HttpStatusException {
        //原订单号
        String orderNo = RandomNumber.getTimeNumber(14);
        ArrayList<String> bookerDepartments =new ArrayList<>();
        //车票价
        BigDecimal ticketPrice =new BigDecimal(690).setScale(2);
        //服务费
        BigDecimal servicePrice = new BigDecimal(20).setScale(2);
        BigDecimal totalAmount = ticketPrice.add(servicePrice);
        //电子客票号
        String trainElectronic = RandomNumber.getTimeNumber(10);
        bookerDepartments.add(employee.getDepartmentName());
        //订单基本信息
        TrainBaseOrder trainBaseOrder =trainOrder.setTrainBaseOrder(employee,totalAmount,"退票成功","R",orderNo,"Online-APP","C","COPAY");
        //订单车票信息
        TrainTicketInfo trainTicketInfo = trainOrder.setTrainTicketInfo(orderNo,"G1234","1",trainElectronic,"",ticketPrice,"05车07C");
        ArrayList<TrainTicketInfo> trainTicketInfos =new ArrayList<>();
        trainTicketInfos.add(trainTicketInfo);
        //订单车次
        TrainSequenceInfo trainSequenceInfo = trainOrder.setTrainSequenceInfo(employee,orderNo,"1","D1234","西安市","上海","西安北站","虹桥火车站");
        ArrayList<TrainSequenceInfo> trainSequenceInfos =new ArrayList<>();
        trainSequenceInfos.add(trainSequenceInfo);
        //订单乘客信息
        //订单乘客信息
        String deptCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        TrainPassengerInfo trainPassengerInfo =trainOrder.setTrainPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),bookerDepartments,employee.getDepartmentName(),deptCode,"6101599468129501",employee.getPhoneNumber(),employee.getEmail());
        ArrayList<TrainPassengerInfo> trainPassengerInfos =new ArrayList<>();
        trainPassengerInfos.add(trainPassengerInfo);
        //订单退票
        TrainRefundInfo trainRefundInfo = trainOrder.setTrainRefundInfo(orderNo,totalAmount,new BigDecimal(100).setScale(2));
        ArrayList <TrainRefundInfo> trainRefundInfos =new ArrayList<>();
        trainRefundInfos.add(trainRefundInfo);

        TrainOrderInfoEntity trainOrderInfoEntity = TrainOrderInfoEntity.builder()
                .trainOrderBase(trainBaseOrder)
                .trainOrderTicketInfos(trainTicketInfos)
                .trainOrderSequenceInfos(trainSequenceInfos)
                .trainOrderPassengerInfos(trainPassengerInfos)
                .trainOrderRefundInfos(trainRefundInfos)
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
        mapping.put("nationlityName","nationalityName");
        mapping.put("trainOrderRefundInfos","trainRefundInfo");
        mapping.put(employee.getDepartmentName(),"产品三组");
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

    @Test(description = "火车票1人预定-订单改签-订单退票-公司支付-月结")
    public void trainOrderDataTest6() throws HttpStatusException {
        //原订单号
        String orderNo = RandomNumber.getTimeNumber(14);
        ArrayList<String> bookerDepartments =new ArrayList<>();
        //车票价
        BigDecimal ticketPrice =new BigDecimal(690).setScale(2);
        //服务费
        BigDecimal servicePrice = new BigDecimal(20).setScale(2);
        BigDecimal totalAmount = new BigDecimal(-690).setScale(2);
        //电子客票号
        String trainElectronic = RandomNumber.getTimeNumber(10);
        String preTrainElectronic = RandomNumber.getTimeNumber(10);
        bookerDepartments.add(employee.getDepartmentName());
        //订单基本信息
        TrainBaseOrder trainBaseOrder =trainOrder.setTrainBaseOrder(employee,totalAmount,"退票成功","R",orderNo,"Online-APP","C","COPAY");
        //订单车票信息
        TrainTicketInfo trainTicketInfo = trainOrder.setTrainTicketInfo(orderNo,"G1234","1",trainElectronic,preTrainElectronic,ticketPrice,"05车07C");
        ArrayList<TrainTicketInfo> trainTicketInfos =new ArrayList<>();
        trainTicketInfos.add(trainTicketInfo);
        //订单车次
        TrainSequenceInfo trainSequenceInfo = trainOrder.setTrainSequenceInfo(employee,orderNo,"1","D1234","西安市","上海","西安北站","虹桥火车站");
        ArrayList<TrainSequenceInfo> trainSequenceInfos =new ArrayList<>();
        trainSequenceInfos.add(trainSequenceInfo);
        //订单乘客信息
        String deptCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        TrainPassengerInfo trainPassengerInfo =trainOrder.setTrainPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),bookerDepartments,employee.getDepartmentName(),deptCode,"6101599468129501",employee.getPhoneNumber(),employee.getEmail());
        ArrayList<TrainPassengerInfo> trainPassengerInfos =new ArrayList<>();
        trainPassengerInfos.add(trainPassengerInfo);
        //订单改签
        TrainChangeInfo trainChangeInfo = trainOrder.setTrainChangeInfo(employee,orderNo,"更换高铁更快","西安市","上海","西安北站","虹桥火车站");
        ArrayList <TrainChangeInfo> trainChangeInfos =new ArrayList<>();
        trainChangeInfos.add(trainChangeInfo);
        //订单退票
        TrainRefundInfo trainRefundInfo = trainOrder.setTrainRefundInfo(orderNo,totalAmount,new BigDecimal(100).setScale(2));
        ArrayList <TrainRefundInfo> trainRefundInfos =new ArrayList<>();
        trainRefundInfos.add(trainRefundInfo);

        TrainOrderInfoEntity trainOrderInfoEntity = TrainOrderInfoEntity.builder()
                .trainOrderBase(trainBaseOrder)
                .trainOrderTicketInfos(trainTicketInfos)
                .trainOrderSequenceInfos(trainSequenceInfos)
                .trainOrderPassengerInfos(trainPassengerInfos)
                .trainOrderRefundInfos(trainRefundInfos)
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
        mapping.put("nationlityName","nationalityName");
        mapping.put(employee.getDepartmentName(),"产品三组");
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

    @Test(description = "火车票-一人预定多人车票(一个人为外部人员)-不改签-不退票-因公-公司账户-月结")
    public void trainOrderDataTest7() throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        ArrayList<String> bookerDepartments =new ArrayList<>();
        //车票价
        BigDecimal ticketPrice =new BigDecimal(690).setScale(2);
        //服务费
        BigDecimal servicePrice = new BigDecimal(20).setScale(2);
        //订单总价
        BigDecimal totalAmount =ticketPrice.add(ticketPrice).add(servicePrice).setScale(2);
        //电子客票号1
        String trainElectronic1 = RandomNumber.getTimeNumber(10);
        //电子客票号2
        String trainElectronic2 = RandomNumber.getTimeNumber(10);
        bookerDepartments.add(employee.getDepartmentName());
        //订单基本信息
        TrainBaseOrder trainBaseOrder =trainOrder.setTrainBaseOrder(employee,totalAmount,"已出票","B",orderNo,"Online-APP","C","COPAY");
        //订单车票信息 两张车票
        TrainTicketInfo trainTicketInfo1 = trainOrder.setTrainTicketInfo(orderNo,"D1234","1",trainElectronic1,"",ticketPrice,"05车07C");
        TrainTicketInfo trainTicketInfo2 = trainOrder.setTrainTicketInfo(orderNo,"D1234","2",trainElectronic2,"",ticketPrice,"05车07B");
        ArrayList<TrainTicketInfo> trainTicketInfos =new ArrayList<>();
        trainTicketInfos.add(trainTicketInfo1);
        trainTicketInfos.add(trainTicketInfo2);
        //订单车次
        TrainSequenceInfo trainSequenceInfo = trainOrder.setTrainSequenceInfo(employee,orderNo,"1","D1234","西安市","上海","西安北站","虹桥火车站");
        ArrayList<TrainSequenceInfo> trainSequenceInfos =new ArrayList<>();
        trainSequenceInfos.add(trainSequenceInfo);
        //订单乘客信息
        String deptCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        TrainPassengerInfo trainPassengerInfo1 =trainOrder.setTrainPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),bookerDepartments,employee.getDepartmentName(),deptCode,"6101599468129501",employee.getPhoneNumber(),employee.getEmail());
        TrainPassengerInfo trainPassengerInfo2 =trainOrder.setTrainPassengerInfo(orderNo,"2","O","小张同学","",new ArrayList<>(),"","","62301599468129501","18292035567","");
        ArrayList<TrainPassengerInfo> trainPassengerInfos =new ArrayList<>();
        trainPassengerInfos.add(trainPassengerInfo1);
        trainPassengerInfos.add(trainPassengerInfo2);
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
        JsonObject getTrainOrder = vendor.queryOrderData(employee,"train",settlementBody);
        log.info("train order Data:{}",getTrainOrder);
        //映射关系
        HashMap<String,String> mapping= new HashMap<>();
        mapping.put("trainOrderBase","trainBaseOrder");
        mapping.put("employeeNum","preEmployeeNum");
        mapping.put("employeeName","preEmployeeName");
        mapping.put("trainOrderTicketInfos","trainTicketInfo");
        mapping.put("trainOrderSequenceInfos","trainSequenceInfo");
        mapping.put("trainOrderPassengerInfos","trainPassengerInfo");
        mapping.put("nationlityName","nationalityName");
        mapping.put(employee.getDepartmentName(),"产品三组");
        assert GsonUtil.compareJsonObject(hotelOrderDataObject,getTrainOrder,mapping);
        //校验预订人的工号
        assert getTrainOrder.getAsJsonObject("trainBaseOrder").get("preEmployeeOid").getAsString().equals(employee.getUserOID());
        //trainSequenceInfo 中的trainType
        String trainNum = getTrainOrder.getAsJsonArray("trainSequenceInfo").get(0).getAsJsonObject().get("trainNum").getAsString();
        String trainType=vendor.trainTypeMapping(trainNum);
        assert getTrainOrder.getAsJsonArray("trainSequenceInfo").get(0).getAsJsonObject().get("trainType").getAsString().equals(trainType);
        //trainPassengerInfo 中的乘客oid 对比
        assert getTrainOrder.getAsJsonArray("trainPassengerInfo").get(0).getAsJsonObject().get("passengerOid").getAsString().equals(employee.getUserOID());
    }

    @Test(description = "火车票1人预定,不改签-不退票-超标-公司支付-月结")
    public void trainOrderDataTest8() throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        ArrayList<String> bookerDepartments =new ArrayList<>();
        //车票价
        BigDecimal ticketPrice =new BigDecimal(690).setScale(2);
        //服务费
        BigDecimal servicePrice = new BigDecimal(20).setScale(2);
        BigDecimal totalAmount =ticketPrice.add(servicePrice);
        //电子客票号
        String trainElectronic = RandomNumber.getTimeNumber(10);
        bookerDepartments.add(employee.getDepartmentName());
        //订单基本信息
        TrainBaseOrder trainBaseOrder =trainOrder.setTrainBaseOrder(employee,totalAmount,"已出票","B",orderNo,"Online-APP","C","COPAY");
        //订单车票信息
        TrainTicketInfo trainTicketInfo = trainOrder.setTrainTicketInfo(orderNo,"D1234","1",trainElectronic,"",ticketPrice,"05车07C");
        ArrayList<TrainTicketInfo> trainTicketInfos =new ArrayList<>();
        trainTicketInfos.add(trainTicketInfo);
        //订单车次
        TrainSequenceInfo trainSequenceInfo = trainOrder.setTrainSequenceInfo(employee,orderNo,"1","D1234","西安市","上海","西安北站","虹桥火车站");
        ArrayList<TrainSequenceInfo> trainSequenceInfos =new ArrayList<>();
        trainSequenceInfos.add(trainSequenceInfo);
        //订单乘客信息
        String deptCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        TrainPassengerInfo trainPassengerInfo =trainOrder.setTrainPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),bookerDepartments,employee.getDepartmentName(),deptCode,"6101599468129501",employee.getPhoneNumber(),employee.getEmail());
        ArrayList<TrainPassengerInfo> trainPassengerInfos =new ArrayList<>();
        trainPassengerInfos.add(trainPassengerInfo);
        //订单超标
        TrainExceedInfo trainExceedInfo =trainOrder.setExceedInfo(trainElectronic);
        ArrayList<TrainExceedInfo> trainExceedInfos =new ArrayList<>();
        trainExceedInfos.add(trainExceedInfo);

        TrainOrderInfoEntity trainOrderInfoEntity = TrainOrderInfoEntity.builder()
                .trainOrderBase(trainBaseOrder)
                .trainOrderTicketInfos(trainTicketInfos)
                .trainOrderSequenceInfos(trainSequenceInfos)
                .trainOrderPassengerInfos(trainPassengerInfos)
                .trainExceedInfos(trainExceedInfos)
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
        mapping.put(employee.getDepartmentName(),"产品三组");
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

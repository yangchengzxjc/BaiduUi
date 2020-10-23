package com.test.api.testcase.vendor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SettlementBody;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelBaseOrder;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelExceedInfo;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelOrderInfoEntity;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelPassengerInfo;
import com.hand.utils.GsonUtil;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.InfraStructure;
import com.test.api.method.Vendor;
import com.test.api.method.VendorMethod.HotelOrder;
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
 * @Date 2020/9/14
 * @Version 1.0
 **/
@Slf4j
public class HotelOrderDataTest extends BaseTest {

    private Employee employee;
    private Vendor vendor;
    private HotelOrder hotelOrder;
    private InfraStructure infraStructure;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("yaliang.wang@cimc.com") String phoneNumber, @Optional("111111") String pwd, @Optional("stage") String env){
        employee = getEmployee(phoneNumber,pwd,env);
        vendor = new Vendor();
        hotelOrder =new HotelOrder();
        infraStructure = new InfraStructure();
    }

    @Test(description = "酒店订单-1人预定-已提交状态-公司支付-未超标")
    public void hotelOrderDataTest1() throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        ArrayList<String> bookerDepartments =new ArrayList<>();
        bookerDepartments.add(employee.getDepartmentName());
        String depoCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        HotelBaseOrder hotelBaseOrder = HotelBaseOrder.builder()
                .orderType("B")
                .orderNo(orderNo)
                .supplierName("中集商旅")
                .supplierCode("cimccTMC")
                .approvalCode("TA"+RandomNumber.getTimeNumber(8)+"-1"+"-H")
                .orderStatusName("已提交")
                .orderStatusCode("Submitted")
                .tenantCode(employee.getTenantId())
                .tenantName(employee.getTenantName())
                .employeeId(employee.getEmployeeID())
                .bookerDepartments(bookerDepartments)
                .employeeName(employee.getFullName())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(employee.getDepartmentName())
                .departmentCode(depoCode)
                .bookChannel("Online-APP")
                .bookType("C")
                .payType("COPAY")
                .createTime(UTCTime.getBeijingTime(0,0,0))
                .payTime(UTCTime.getBeijingTime(0,0,0))
                .successTime(UTCTime.getBeijingTime(0,0,0))
                .hotelClass("N")
                .paymentType("M")
                .accountType("C")
                .balanceType("P")
                .guaranteeType("N")
                .costCenter1("成本中心1")
                .currency("CNY")
                .totalAmount(new BigDecimal(1000).setScale(2))
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .remark("备注")
                .hotelType("AGR")
                .hotelName("爱丽丝酒店")
                .hotelPhone("010-123456")
                .hotelAddress("上海梅川路25弄")
                .hotelStar(3)
                .startTime(UTCTime.getBeijingTime(3,0,0))
                .endTime(UTCTime.getBeijingDate(5)+" 12:00:00")
                .lastCancelTime(UTCTime.getBeijingTime(2,0,0))
                .cityName("上海")
                .cityHeliosCode("CHN031000000")
                .passengerName(employee.getFullName())
                .roomName("商务套房")
                .roomQuantity(1)
                .roomDays(5)
                .variance(new BigDecimal(0).setScale(2))
                .build();
        HotelPassengerInfo hotelPassengerInfo =hotelOrder.setHotelPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),employee.getDepartmentName(),depoCode,bookerDepartments);
        ArrayList<HotelPassengerInfo> hotelPassengerInfos =new ArrayList<>();
        hotelPassengerInfos.add(hotelPassengerInfo);
        HotelOrderInfoEntity hotelOrderInfoEntity = HotelOrderInfoEntity.builder()
                .hotelOrderBase(hotelBaseOrder)
                .hotelOrderPassengerInfos(hotelPassengerInfos)
                .build();
        //推送的数据封装成一个json字符串
        String hotelOrderData = GsonUtil.objectToString(hotelOrderInfoEntity);
        //转成jsonobject对象
        JsonObject hotelOrderDataObject =new JsonParser().parse(hotelOrderData).getAsJsonObject();
        //订单推送
        vendor.pushOrderData(employee,"hotel",hotelOrderInfoEntity,"cimccTMC","200428140254184788","");
        SettlementBody settlementBody = SettlementBody.builder()
                .companyOid(employee.getCompanyOID())
                .orderNo(orderNo)
                .page(1)
                .size(10)
                .build();
        //查询订单数据
        JsonObject  hotelOrder = vendor.queryOrderData(employee,"hotel",settlementBody);
        log.info("hotel order Data:{}",hotelOrder);
        //进行数据对比
        HashMap<String,String> mapping =new HashMap<>();
        mapping.put("hotelOrderPassengerInfos","hotelPassengerInfo");
        mapping.put("employeeId","preEmployeeId");
        mapping.put("hotelOrderBase","hotelBaseOrder");
        mapping.put("costCenter1","costCenter");
        mapping.put(employee.getDepartmentName(),"产品三组");
        //进行数据对比
        assert GsonUtil.compareJsonObject(hotelOrderDataObject,hotelOrder,mapping);
        if(hotelOrder.getAsJsonObject("hotelBaseOrder").get("preEmployeeOId").isJsonNull()){
            assert false;
        }else{
            assert hotelOrder.getAsJsonObject("hotelBaseOrder").get("preEmployeeOId").getAsString().equals(employee.getUserOID());
        }
        if(hotelOrder.getAsJsonArray("hotelPassengerInfo").get(0).getAsJsonObject().get("passengerOid").isJsonNull()){
            assert false;
        }else{
            assert hotelOrder.getAsJsonArray("hotelPassengerInfo").get(0).getAsJsonObject().get("passengerOid").getAsString().equals(employee.getUserOID());
        }
    }


    @Test(description = "酒店订单-1统一预定多人的俩男一女两间房-已提交状态-公司支付-未超标")
    public void hotelOrderDataTest2() throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        ArrayList<String> bookerDepartments =new ArrayList<>();
        bookerDepartments.add(employee.getDepartmentName());
        //获取一个员工的信息
        JsonObject employeeInfo1 = infraStructure.getUserDetail(employee,"01399315");
        JsonObject employeeInfo2 = infraStructure.getUserDetail(employee,"01363468");
        String depoCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        String deptCode1 = infraStructure.getDeptCode(employee,employeeInfo1.get("departmentOID").getAsString());
        String deptCode2 = infraStructure.getDeptCode(employee,employeeInfo2.get("departmentOID").getAsString());
        HotelBaseOrder hotelBaseOrder = HotelBaseOrder.builder()
                .orderType("B")
                .orderNo(orderNo)
                .supplierName("中集商旅")
                .supplierCode("cimccTMC")
                .approvalCode("TA"+RandomNumber.getTimeNumber(8)+"-1"+"-H")
                .orderStatusName("已提交")
                .orderStatusCode("Submitted")
                .tenantCode(employee.getTenantId())
                .tenantName(employee.getTenantName())
                .employeeId(employee.getEmployeeID())
                .bookerDepartments(bookerDepartments)
                .employeeName(employee.getFullName())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(employee.getDepartmentName())
                .departmentCode(depoCode)
                .bookChannel("Online-APP")
                .bookType("C")
                .payType("COPAY")
                .createTime(UTCTime.getBeijingTime(0,0,0))
                .payTime(UTCTime.getBeijingTime(0,0,0))
                .successTime(UTCTime.getBeijingTime(0,0,0))
                .hotelClass("N")
                .paymentType("M")
                .accountType("C")
                .balanceType("P")
                .guaranteeType("N")
                .costCenter1("成本中心1")
                .currency("CNY")
                .totalAmount(new BigDecimal(1000).setScale(2))
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .remark("备注")
                .hotelType("AGR")
                .hotelName("爱丽丝酒店")
                .hotelPhone("010-123456")
                .hotelAddress("上海梅川路25弄")
                .hotelStar(3)
                .startTime(UTCTime.getBeijingTime(3,0,0))
                .endTime(UTCTime.getBeijingDate(5)+" 12:00:00")
                .lastCancelTime(UTCTime.getBeijingTime(2,0,0))
                .cityName("上海")
                .cityHeliosCode("CHN031000000")
                .passengerName(employee.getFullName())
                .roomName("商务套房")
                .roomQuantity(2)
                .roomDays(5)
                .variance(new BigDecimal(0).setScale(2))
                .build();
        HotelPassengerInfo hotelPassengerInfo1 =hotelOrder.setHotelPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),employee.getDepartmentName(),depoCode,bookerDepartments);
        HotelPassengerInfo hotelPassengerInfo2 = hotelOrder.setHotelPassengerInfo(orderNo,"2","I",employeeInfo1.get("fullName").getAsString(),employeeInfo1.get("employeeID").getAsString(),employeeInfo1.get("departmentName").getAsString(),deptCode1,bookerDepartments);
        HotelPassengerInfo hotelPassengerInfo3 = hotelOrder.setHotelPassengerInfo(orderNo,"3","I",employeeInfo2.get("fullName").getAsString(),employeeInfo2.get("employeeID").getAsString(),employeeInfo2.get("departmentName").getAsString(),deptCode2,bookerDepartments);
        ArrayList<HotelPassengerInfo> hotelPassengerInfos =new ArrayList<>();
        hotelPassengerInfos.add(hotelPassengerInfo1);
        hotelPassengerInfos.add(hotelPassengerInfo2);
        hotelPassengerInfos.add(hotelPassengerInfo3);
        HotelOrderInfoEntity hotelOrderInfoEntity = HotelOrderInfoEntity.builder()
                .hotelOrderBase(hotelBaseOrder)
                .hotelOrderPassengerInfos(hotelPassengerInfos)
                .build();
        //推送的数据封装成一个json字符串
        String hotelOrderData = GsonUtil.objectToString(hotelOrderInfoEntity);
        //转成jsonobject对象
        JsonObject hotelOrderDataObject =new JsonParser().parse(hotelOrderData).getAsJsonObject();
        //订单推送
        vendor.pushOrderData(employee,"hotel",hotelOrderInfoEntity,"cimccTMC","200428140254184788","");
        SettlementBody settlementBody = SettlementBody.builder()
                .companyOid(employee.getCompanyOID())
                .orderNo(orderNo)
                .page(1)
                .size(10)
                .build();
        //查询订单数据
        JsonObject  hotelOrder = vendor.queryOrderData(employee,"hotel",settlementBody);
        log.info("hotel order Data:{}",hotelOrder);
        //进行数据对比
        HashMap<String,String> mapping =new HashMap<>();
        mapping.put("hotelOrderPassengerInfos","hotelPassengerInfo");
        mapping.put("employeeId","preEmployeeId");
        mapping.put("hotelOrderBase","hotelBaseOrder");
        mapping.put("costCenter1","costCenter");
        mapping.put(employee.getDepartmentName(),"产品三组");
        //进行数据对比
        assert GsonUtil.compareJsonObject(hotelOrderDataObject,hotelOrder,mapping);
        if(hotelOrder.getAsJsonObject("hotelBaseOrder").get("preEmployeeOId").isJsonNull()){
            assert false;
        }else{
            assert hotelOrder.getAsJsonObject("hotelBaseOrder").get("preEmployeeOId").getAsString().equals(employee.getUserOID());
        }
        if(hotelOrder.getAsJsonArray("hotelPassengerInfo").get(0).getAsJsonObject().get("passengerOid").isJsonNull()){
            assert false;
        }else{
            assert hotelOrder.getAsJsonArray("hotelPassengerInfo").get(0).getAsJsonObject().get("passengerOid").getAsString().equals(employee.getUserOID());
        }
    }

    @Test(description = "酒店订单-1人预定-订单取消-公司支付-未超标")
    public void hotelOrderDataTest3() throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber(14);
        String originalOrderNum = RandomNumber.getTimeNumber(14);
        ArrayList<String> bookerDepartments =new ArrayList<>();
        bookerDepartments.add(employee.getDepartmentName());
        String depoCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        HotelBaseOrder hotelBaseOrder = HotelBaseOrder.builder()
                .orderType("R")
                .orderNo(orderNo)
                .originalOrderNum(originalOrderNum)
                .supplierName("中集商旅")
                .supplierCode("cimccTMC")
                .approvalCode("TA"+RandomNumber.getTimeNumber(8)+"-1"+"-H")
                .orderStatusName("已取消")
                .orderStatusCode("Cancelled")
                .tenantCode(employee.getTenantId())
                .tenantName(employee.getTenantName())
                .employeeId(employee.getEmployeeID())
                .bookerDepartments(bookerDepartments)
                .employeeName(employee.getFullName())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(employee.getDepartmentName())
                .departmentCode(depoCode)
                .bookChannel("Online-APP")
                .bookType("C")
                .payType("COPAY")
                .createTime(UTCTime.getBeijingTime(0,0,0))
                .payTime(UTCTime.getBeijingTime(0,0,0))
                .successTime(UTCTime.getBeijingTime(0,0,0))
                .hotelClass("N")
                .paymentType("M")
                .accountType("C")
                .balanceType("P")
                .guaranteeType("N")
                .costCenter1("成本中心1")
                .currency("CNY")
                .totalAmount(new BigDecimal(1000).setScale(2))
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .remark("备注")
                .hotelType("AGR")
                .hotelName("爱丽丝酒店")
                .hotelPhone("010-123456")
                .hotelAddress("上海梅川路25弄")
                .hotelStar(3)
                .startTime(UTCTime.getBeijingTime(3,0,0))
                .endTime(UTCTime.getBeijingDate(5)+" 12:00:00")
                .lastCancelTime(UTCTime.getBeijingTime(2,0,0))
                .cityName("上海")
                .cityHeliosCode("CHN031000000")
                .passengerName(employee.getFullName())
                .roomName("商务套房")
                .roomQuantity(1)
                .roomDays(5)
                .variance(new BigDecimal(0).setScale(2))
                .build();
        HotelPassengerInfo hotelPassengerInfo =hotelOrder.setHotelPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),employee.getDepartmentName(),depoCode,bookerDepartments);
        ArrayList<HotelPassengerInfo> hotelPassengerInfos =new ArrayList<>();
        hotelPassengerInfos.add(hotelPassengerInfo);
        HotelOrderInfoEntity hotelOrderInfoEntity = HotelOrderInfoEntity.builder()
                .hotelOrderBase(hotelBaseOrder)
                .hotelOrderPassengerInfos(hotelPassengerInfos)
                .build();
        //推送的数据封装成一个json字符串
        String hotelOrderData = GsonUtil.objectToString(hotelOrderInfoEntity);
        //转成jsonobject对象
        JsonObject hotelOrderDataObject =new JsonParser().parse(hotelOrderData).getAsJsonObject();
        //订单推送
        vendor.pushOrderData(employee,"hotel",hotelOrderInfoEntity,"cimccTMC","200428140254184788","");
        SettlementBody settlementBody = SettlementBody.builder()
                .companyOid(employee.getCompanyOID())
                .orderNo(orderNo)
                .page(1)
                .size(10)
                .build();
        //查询订单数据
        JsonObject  hotelOrder = vendor.queryOrderData(employee,"hotel",settlementBody);
        log.info("hotel order Data:{}",hotelOrder);
        //进行数据对比
        HashMap<String,String> mapping =new HashMap<>();
        mapping.put("hotelOrderPassengerInfos","hotelPassengerInfo");
        mapping.put("employeeId","preEmployeeId");
        mapping.put("costCenter1","costCenter");
        mapping.put("hotelOrderBase","hotelBaseOrder");
        mapping.put(employee.getDepartmentName(),"产品三组");
        //进行数据对比
        assert GsonUtil.compareJsonObject(hotelOrderDataObject,hotelOrder,mapping);
        if(hotelOrder.getAsJsonObject("hotelBaseOrder").get("preEmployeeOId").isJsonNull()){
            assert false;
        }else{
            assert hotelOrder.getAsJsonObject("hotelBaseOrder").get("preEmployeeOId").getAsString().equals(employee.getUserOID());
        }
        if(hotelOrder.getAsJsonArray("hotelPassengerInfo").get(0).getAsJsonObject().get("passengerOid").isJsonNull()){
            assert false;
        }else{
            assert hotelOrder.getAsJsonArray("hotelPassengerInfo").get(0).getAsJsonObject().get("passengerOid").getAsString().equals(employee.getUserOID());
        }
    }

    @Test(description = "酒店订单-员工1人预定--因私-未超标")
    public void hotelOrderDataTest4() throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber(14);
        ArrayList<String> bookerDepartments =new ArrayList<>();
        bookerDepartments.add(employee.getDepartmentName());
        String depoCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        HotelBaseOrder hotelBaseOrder = HotelBaseOrder.builder()
                .orderType("B")
                .orderNo(orderNo)
                .originalOrderNum("")
                .supplierName("中集商旅")
                .supplierCode("cimccTMC")
                .approvalCode("TA"+RandomNumber.getTimeNumber(8)+"-1"+"-H")
                .orderStatusName("已提交")
                .orderStatusCode("Submitted")
                .tenantCode(employee.getTenantId())
                .tenantName(employee.getTenantName())
                .employeeId(employee.getEmployeeID())
                .bookerDepartments(bookerDepartments)
                .employeeName(employee.getFullName())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(employee.getDepartmentName())
                .departmentCode(depoCode)
                .bookChannel("Online-APP")
                .bookType("P")
                .payType("ALIPAY")
                .createTime(UTCTime.getBeijingTime(0,0,0))
                .payTime(UTCTime.getBeijingTime(0,0,0))
                .successTime(UTCTime.getBeijingTime(0,0,0))
                .hotelClass("N")
                .paymentType("N")
                .accountType("P")
                .balanceType("P")
                .guaranteeType("N")
                .costCenter1("成本中心1")
                .currency("CNY")
                .totalAmount(new BigDecimal(1000).setScale(2))
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .remark("备注")
                .hotelType("MEM")
                .hotelName("爱丽丝酒店")
                .hotelPhone("010-123456")
                .hotelAddress("上海梅川路25弄")
                .hotelStar(3)
                .startTime(UTCTime.getBeijingTime(3,0,0))
                .endTime(UTCTime.getBeijingDate(5)+" 12:00:00")
                .lastCancelTime(UTCTime.getBeijingTime(2,0,0))
                .cityName("上海")
                .cityHeliosCode("CHN031000000")
                .passengerName(employee.getFullName())
                .roomName("商务套房")
                .roomQuantity(1)
                .roomDays(5)
                .variance(new BigDecimal(0).setScale(2))
                .build();
        HotelPassengerInfo hotelPassengerInfo =hotelOrder.setHotelPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),employee.getDepartmentName(),depoCode,bookerDepartments);
        ArrayList<HotelPassengerInfo> hotelPassengerInfos =new ArrayList<>();
        hotelPassengerInfos.add(hotelPassengerInfo);
        HotelOrderInfoEntity hotelOrderInfoEntity = HotelOrderInfoEntity.builder()
                .hotelOrderBase(hotelBaseOrder)
                .hotelOrderPassengerInfos(hotelPassengerInfos)
                .build();
        //推送的数据封装成一个json字符串
        String hotelOrderData = GsonUtil.objectToString(hotelOrderInfoEntity);
        //转成jsonobject对象
        JsonObject hotelOrderDataObject =new JsonParser().parse(hotelOrderData).getAsJsonObject();
        //订单推送
        vendor.pushOrderData(employee,"hotel",hotelOrderInfoEntity,"cimccTMC","200428140254184788","");
        SettlementBody settlementBody = SettlementBody.builder()
                .companyOid(employee.getCompanyOID())
                .orderNo(orderNo)
                .page(1)
                .size(10)
                .build();
        //查询订单数据
        JsonObject  hotelOrder = vendor.queryOrderData(employee,"hotel",settlementBody);
        log.info("hotel order Data:{}",hotelOrder);
        //进行数据对比
        HashMap<String,String> mapping =new HashMap<>();
        mapping.put("hotelOrderPassengerInfos","hotelPassengerInfo");
        mapping.put("employeeId","preEmployeeId");
        mapping.put("costCenter1","costCenter");
        mapping.put("hotelOrderBase","hotelBaseOrder");
        mapping.put(employee.getDepartmentName(),"产品三组");
        //进行数据对比
        assert GsonUtil.compareJsonObject(hotelOrderDataObject,hotelOrder,mapping);
        if(hotelOrder.getAsJsonObject("hotelBaseOrder").get("preEmployeeOId").isJsonNull()){
            assert false;
        }else{
            assert hotelOrder.getAsJsonObject("hotelBaseOrder").get("preEmployeeOId").getAsString().equals(employee.getUserOID());
        }
        if(hotelOrder.getAsJsonArray("hotelPassengerInfo").get(0).getAsJsonObject().get("passengerOid").isJsonNull()){
            assert false;
        }else{
            assert hotelOrder.getAsJsonArray("hotelPassengerInfo").get(0).getAsJsonObject().get("passengerOid").getAsString().equals(employee.getUserOID());
        }
    }


    @Test(description = "酒店订单-1人预定-超标-公司支付")
    public void hotelOrderDataTest6() throws HttpStatusException {
        //订单号
        String orderNo = RandomNumber.getTimeNumber();
        ArrayList<String> bookerDepartments =new ArrayList<>();
        bookerDepartments.add(employee.getDepartmentName());
        String depoCode = infraStructure.getDeptCode(employee,employee.getDepartmentOID());
        HotelBaseOrder hotelBaseOrder = HotelBaseOrder.builder()
                .orderType("B")
                .orderNo(orderNo)
                .supplierName("中集商旅")
                .supplierCode("cimccTMC")
                .approvalCode("TA"+RandomNumber.getTimeNumber(8)+"-1"+"-H")
                .orderStatusName("已提交")
                .orderStatusCode("Submitted")
                .tenantCode(employee.getTenantId())
                .tenantName(employee.getTenantName())
                .employeeId(employee.getEmployeeID())
                .bookerDepartments(bookerDepartments)
                .employeeName(employee.getFullName())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(employee.getDepartmentName())
                .bookChannel("Online-APP")
                .bookType("C")
                .payType("COPAY")
                .createTime(UTCTime.getBeijingTime(0,0,0))
                .payTime(UTCTime.getBeijingTime(0,0,0))
                .successTime(UTCTime.getBeijingTime(0,0,0))
                .hotelClass("N")
                .paymentType("M")
                .accountType("C")
                .balanceType("P")
                .guaranteeType("N")
                .costCenter1("成本中心1")
                .currency("CNY")
                .totalAmount(new BigDecimal(1000).setScale(2))
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .remark("备注")
                .hotelType("AGR")
                .hotelName("爱丽丝酒店")
                .hotelPhone("010-123456")
                .hotelAddress("上海梅川路25弄")
                .hotelStar(3)
                .startTime(UTCTime.getBeijingTime(3,0,0))
                .endTime(UTCTime.getBeijingDate(5)+" 12:00:00")
                .lastCancelTime(UTCTime.getBeijingTime(2,0,0))
                .cityName("上海")
                .cityHeliosCode("CHN031000000")
                .passengerName(employee.getFullName())
                .roomName("商务套房")
                .roomQuantity(1)
                .roomDays(5)
                .variance(new BigDecimal(0).setScale(2))
                .build();
        HotelPassengerInfo hotelPassengerInfo =hotelOrder.setHotelPassengerInfo(orderNo,"1","I",employee.getFullName(),employee.getEmployeeID(),employee.getDepartmentName(),depoCode,bookerDepartments);
        ArrayList<HotelPassengerInfo> hotelPassengerInfos =new ArrayList<>();
        hotelPassengerInfos.add(hotelPassengerInfo);
        //订单超标
        HotelExceedInfo hotelExceedInfo = hotelOrder.setHotelExceedInfo(orderNo);
        ArrayList<HotelExceedInfo> hotelExceedInfos =new ArrayList<>();
        hotelExceedInfos.add(hotelExceedInfo);
        HotelOrderInfoEntity hotelOrderInfoEntity = HotelOrderInfoEntity.builder()
                .hotelOrderBase(hotelBaseOrder)
                .hotelOrderPassengerInfos(hotelPassengerInfos)
                .hotelOrderExceedInfos(hotelExceedInfos)
                .build();
        //推送的数据封装成一个json字符串
        String hotelOrderData = GsonUtil.objectToString(hotelOrderInfoEntity);
        //转成jsonobject对象
        JsonObject hotelOrderDataObject =new JsonParser().parse(hotelOrderData).getAsJsonObject();
        //订单推送
        vendor.pushOrderData(employee,"hotel",hotelOrderInfoEntity,"cimccTMC","200428140254184788","");
        SettlementBody settlementBody = SettlementBody.builder()
                .companyOid(employee.getCompanyOID())
                .orderNo(orderNo)
                .page(1)
                .size(10)
                .build();
        //查询订单数据
        JsonObject  hotelOrder = vendor.queryOrderData(employee,"hotel",settlementBody);
        log.info("hotel order Data:{}",hotelOrder);
        //进行数据对比
        HashMap<String,String> mapping =new HashMap<>();
        mapping.put("hotelOrderPassengerInfos","hotelPassengerInfo");
        mapping.put("employeeId","preEmployeeId");
        mapping.put("hotelOrderBase","hotelBaseOrder");
        mapping.put("hotelOrderExceedInfos","hotelExceedInfo");
        mapping.put("costCenter1","costCenter");
        mapping.put(employee.getDepartmentName(),"产品三组");
        //进行数据对比
        assert GsonUtil.compareJsonObject(hotelOrderDataObject,hotelOrder,mapping);
    }

}

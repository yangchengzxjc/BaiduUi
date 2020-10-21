package com.test.api.method.VendorMethod;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelBaseOrder;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelExceedInfo;
import com.hand.basicObject.supplierObject.hotelOrderInfo.HotelPassengerInfo;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.test.api.method.InfraStructure;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/14
 * @Version 1.0
 **/
public class HotelOrder {
    private InfraStructure infraStructure;

    public HotelOrder(){
        infraStructure =new InfraStructure();
    }

    /**
     * 酒店订单超标
     * @param orderNo
     * @return
     */
    public HotelExceedInfo setHotelExceedInfo(String orderNo){
        //超标信息
        HotelExceedInfo hotelExceedInfo =HotelExceedInfo.builder()
                .orderNo(orderNo)
                .violationContentCode("A123")
                .violationContentName("酒店价格标准")
                .violationReasonName("距离原因")
                .violationReasonCode("B123")
                .build();
        return hotelExceedInfo;
    }

    /**
     * 订单乘客信息
     * @param orderNo
     * @param passengerNo
     * @param passengerAttribute
     * @param passengerName
     * @param passengerNum
     * @param departmentName
     * @param passengerDepartments
     * @return
     */
    public HotelPassengerInfo setHotelPassengerInfo(String orderNo, String passengerNo, String passengerAttribute, String passengerName,
                                                    String passengerNum, String departmentName, String deptCode,List<String> passengerDepartments) {
        HotelPassengerInfo hotelPassengerInfo = HotelPassengerInfo.builder()
                .orderNo(orderNo)
                .passengerNo(passengerNo)
                .passengerAttribute(passengerAttribute)
                .passengerName(passengerName)
                .passengerNum(passengerNum)
                .departmentName(departmentName)
                .departmentCode(deptCode)
                .passengerCostCenter("成本中心1")
                .passengerDepartments(passengerDepartments)
                .build();
        return hotelPassengerInfo;
    }

    /**
     * 订单乘客信息
     * @param orderNo
     * @param passengerNo
     * @return
     */
    public HotelPassengerInfo setHotelPassengerInfo(Employee employee,String orderNo, String passengerNo,JsonObject tmcRequestData,JsonObject applicationParticipant) throws HttpStatusException {
        JsonObject tmcParticipant = tmcRequestData.getAsJsonArray("participantList").get(0).getAsJsonObject();
        //查询乘机人的信息
        JsonObject participantInfo = infraStructure.getEmployeeDetail(employee,applicationParticipant.get("participantOID").getAsString());
        ArrayList<String> bookerDepartments =new ArrayList<>();
        bookerDepartments.add(participantInfo.get("departmentName").getAsString());
        //查询部门code
        String deptCode = infraStructure.getDeptCode(employee,applicationParticipant.get("departmentOID").getAsString());
        HotelPassengerInfo hotelPassengerInfo = HotelPassengerInfo.builder()
                .orderNo(orderNo)
                .passengerNo(passengerNo)
                .passengerAttribute("I")
                .passengerName(tmcParticipant.get("name").getAsString())
                .passengerNum(tmcParticipant.get("employeeID").getAsString())
                .departmentCode(deptCode)
                .departmentName(participantInfo.get("departmentName").getAsString())
                .passengerCostCenter(tmcRequestData.get("costCenter1").getAsString())
                .passengerDepartments(bookerDepartments)
                .build();
        return hotelPassengerInfo;
    }

    /**
     * 酒店订单
     * @param employee
     * @param orderType
     * @param orderNo
     * @param supplierName
     * @param supplierCode
     * @param tmcdata
     * @param applicat
     * @return
     */
    public HotelBaseOrder setHotelBaseOrder(Employee employee,String orderType, String orderNo, String supplierName, String supplierCode, JsonObject tmcdata,JsonObject applicat) throws HttpStatusException {
        JsonObject travelHotel =tmcdata.getAsJsonArray("travelHotelsList").get(0).getAsJsonObject();
        JsonArray participantList =tmcdata.getAsJsonArray("participantList");
        StringBuffer passengerName = new StringBuffer();
        for(int i=0;i<participantList.size();i++){
            passengerName.append(participantList.get(i).getAsJsonObject().get("name").getAsString()+",");
        }
        ArrayList<String> bookerDepartments = new ArrayList<>();
        bookerDepartments.add(applicat.get("departmentName").getAsString());
        String startTime = UTCTime.BJDateMdy(travelHotel.get("fromDate").getAsString().split("\\s+")[1],travelHotel.get("floatDaysBegin").getAsInt())+" "+UTCTime.getTime(0,0);
        String endTime = UTCTime.BJDateMdy(travelHotel.get("leaveDate").getAsString().split("\\s+")[1],-(travelHotel.get("floatDaysBegin").getAsInt()))+" 11:59:59";
        JsonObject bookClerk = infraStructure.getUserDetail(employee,tmcdata.getAsJsonObject("bookClerk").get("employeeID").getAsString());
        //部门code
        String deptCode = infraStructure.getDeptCode(employee,bookClerk.get("departmentOID").getAsString());
        HotelBaseOrder hotelBaseOrder = HotelBaseOrder.builder()
                .orderType(orderType)
                .orderNo(orderNo)
                .supplierName(supplierName)
                .supplierCode(supplierCode)
                .approvalCode(tmcdata.get("approvalCode").getAsString())
                .orderStatusName("已提交")
                .orderStatusCode("Submitted")
                .tenantCode(tmcdata.get("tenantId").getAsString())
                .tenantName(employee.getTenantName())
                .employeeId(tmcdata.getAsJsonObject("bookClerk").get("employeeID").getAsString())
                .bookerDepartments(bookerDepartments)
                .employeeName(tmcdata.getAsJsonObject("bookClerk").get("name").getAsString())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(applicat.get("departmentName").getAsString())
                .departmentCode(deptCode)
                .bookChannel("Online-APP")
                .bookType("C")
                .payType("COPAY")
                .createTime(UTCTime.getBeijingTime(0,0,0))
                .payTime(UTCTime.getBeijingTime(0,0,2))
                .successTime(UTCTime.getBeijingTime(0,0,2))
                .hotelClass("N")
                .paymentType("M")
                .accountType("C")
                .balanceType("P")
                .guaranteeType("N")
                .costCenter1(tmcdata.get("costCenter1").getAsString())
                .currency("CNY")
                .totalAmount(new BigDecimal(1000).setScale(2))
                .contactName(tmcdata.getAsJsonObject("bookClerk").get("name").getAsString())
                .contactPhone(tmcdata.getAsJsonObject("bookClerk").get("mobile").getAsString())
                .contactEmail(employee.getEmail())
                .remark(tmcdata.get("remark").getAsString())
                .hotelType("AGR")
                .hotelName("爱丽丝酒店")
                .hotelPhone("010-123456")
                .hotelAddress("上海梅川路25弄")
                .hotelStar(3)
                .startTime(startTime)
                .endTime(endTime)
                .lastCancelTime(UTCTime.getBeijingTime(2,0,0))
                .cityName(travelHotel.get("city").getAsString())
                .cityHeliosCode(travelHotel.get("cityCode").getAsString())
                //为字符串  多个乘客使用,隔开
                .passengerName(passengerName.deleteCharAt(passengerName.lastIndexOf(",")).toString())
                .roomName("商务套房")
                .roomQuantity(1)
                .roomDays(Days.daysBetween(new DateTime(UTCTime.BJDateMdy(travelHotel.get("fromDate").getAsString().split("\\s+")[1],travelHotel.get("floatDaysBegin").getAsInt())),new DateTime(UTCTime.BJDateMdy(travelHotel.get("leaveDate").getAsString().split("\\s+")[1],-(travelHotel.get("floatDaysBegin").getAsInt())))).getDays())
                .variance(new BigDecimal(0).setScale(2))
                .build();
        return hotelBaseOrder;
    }


}

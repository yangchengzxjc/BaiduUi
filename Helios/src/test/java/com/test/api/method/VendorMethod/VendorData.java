package com.test.api.method.VendorMethod;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.api.method.InfraStructure;
import com.test.api.method.Vendor;

/**
 * @Author peng.zhang
 * @Date 2020/11/4
 * @Version 1.0
 **/
public class VendorData {

    private Vendor vendor;
    private InfraStructure infraStructure;


    public VendorData(){
        vendor = new Vendor();
        infraStructure = new InfraStructure();
    }


    /**
     * 这块逻辑：1.一个租户使用一个tmc  这个tmc 推的数据格式是一定的
     *         2.如果要测试这个tmc  的话  需要一个租户的账号和密码    companyName 和companyCode  以及oid
     *         是根据订票人的工号查出来的 ，所以校验这块数据的话 就得需要一个租户的员工的真实信息
     *         3.tmc推送需要corpId 才能知道是那个租户的员工
     */


    /**
     * 初始化读取到的结算数据
     * @param
     * @return
     */
    public JsonObject setFlightSettlementData(Employee employee, String type,JsonObject settlement, String supplierCode, String corpId) throws HttpStatusException {

        JsonObject settlementInfo = new JsonObject();
        JsonObject employeeInfo =new JsonObject();
        String passengerEmployeeOid="";
        JsonArray trainPassengerInfos = new JsonArray();
        if(type.equals("flight")){
            settlementInfo = settlement.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject();
            //根据员工的工号 查询订票人的信息
            employeeInfo = infraStructure.getUserDetail(employee,settlementInfo.get("bookClerkEmployeeId").getAsString());
            //根据工号查询乘客的信息
            passengerEmployeeOid = infraStructure.getUserDetail(employee,settlementInfo.get("passengerEmployeeId").getAsString()).get("userOID").getAsString();
        }else if(type.equals("train")){
            settlementInfo = settlement.getAsJsonArray("trainSettlementInfos").get(0).getAsJsonObject().get("trainBaseSettlement").getAsJsonObject();
            //根据员工的工号 查询订票人的信息
            employeeInfo = infraStructure.getUserDetail(employee,settlementInfo.getAsJsonObject("trainBaseSettlement").get("bookClerkEmployeeId").getAsString());
            //根据工号查询乘客的信息
            passengerEmployeeOid = infraStructure.getUserDetail(employee,settlementInfo.getAsJsonArray("trainPassengerInfos").get(0).getAsJsonObject().get("passengerCode").getAsString()).get("userOID").getAsString();
            trainPassengerInfos = settlement.getAsJsonArray("trainSettlementInfos").get(0).getAsJsonObject().getAsJsonArray("trainPassengerInfos");
        }else if(type.equals("hotel")){
            settlementInfo = settlement.getAsJsonArray("hotelSettlementList").get(0).getAsJsonObject();
            //根据员工的工号 查询订票人的信息
            employeeInfo = infraStructure.getUserDetail(employee,settlementInfo.get("bookClerkEmployeeId").getAsString());
            //根据工号查询乘客的信息
            passengerEmployeeOid = infraStructure.getUserDetail(employee,settlementInfo.getAsJsonArray("passengerList").get(0).getAsJsonObject().get("passengerEmployeeId").getAsString()).get("userOID").getAsString();
        }
        String tenantId = employeeInfo.getAsJsonArray("userJobsDTOs").get(0).getAsJsonObject().get("tenantId").getAsString();
        String companyName = employeeInfo.get("companyName").getAsString();
        String companyOID = employeeInfo.get("companyOID").getAsString();
        String companyId = employeeInfo.getAsJsonArray("userJobsDTOs").get(0).getAsJsonObject().get("companyId").getAsString();
        //获取公司code
        String companyCode = infraStructure.getCompanyCode(employee,companyId);
        //获取该员工所在的租户信息
        JsonObject tentantInfo = infraStructure.getTenantInfo(employee,tenantId);
        String tenantName = tentantInfo.get("tenantName").getAsString();
        String tenantCode = tentantInfo.get("tenantCode").getAsString();
        settlementInfo.addProperty("supplierCode",supplierCode);
        settlementInfo.addProperty("corpId",corpId);
        settlementInfo.addProperty("companyOid",companyOID);
        settlementInfo.addProperty("tenantCode",tenantCode);
        settlementInfo.addProperty("tenantName",tenantName);
        settlementInfo.addProperty("companyName",companyName);
        settlementInfo.addProperty("companyCode",companyCode);
        settlementInfo.addProperty("bookClerkEmployeeOid",employeeInfo.get("userOID").getAsString());
        if(type.equals("flight")){
            settlementInfo.addProperty("passengerEmployeeOid",passengerEmployeeOid);
        }else if(type.equals("train")){
            trainPassengerInfos.get(0).getAsJsonObject().addProperty("passengerOid",passengerEmployeeOid);
        }else if(type.equals("hotel")){
            settlementInfo.getAsJsonArray("passengerList").get(0).getAsJsonObject().addProperty("passengerEmployeeOid",passengerEmployeeOid);
        }
        if(type.equals("flight")){
            settlement.getAsJsonArray("flightSettlementList").add(settlementInfo);
        }else if(type.equals("train")){
            settlement.getAsJsonArray("trainSettlementInfos").get(0).getAsJsonObject().add("trainBaseSettlement",settlementInfo);
            settlement.getAsJsonArray("trainSettlementInfos").get(0).getAsJsonObject().add("trainPassengerInfos",trainPassengerInfos);
        }else if(type.equals("hotel")){
            settlement.getAsJsonArray("hotelSettlementList").add(settlementInfo);
        }
        return settlement;
    }


//    /**
//     * 初始化读取到的结算数据
//     * @param
//     * @return
//     */
//    public JsonObject setTrainSettlementData(Employee employee,JsonObject settlement, String supplierName,String corpId) throws HttpStatusException {
//        JsonObject settlementInfo = settlement.getAsJsonArray("trainSettlementInfos").get(0).getAsJsonObject();
//        //根据员工的工号 查询订票人的信息
//        JsonObject employeeInfo = infraStructure.getUserDetail(employee,settlementInfo.getAsJsonObject("trainBaseSettlement").get("bookClerkEmployeeId").getAsString());
//        //根据工号查询乘客的信息
//        String passengerEmployeeOid = infraStructure.getUserDetail(employee,settlementInfo.getAsJsonArray("trainPassengerInfos").get(0).getAsJsonObject().get("passengerCode").getAsString()).get("userOID").getAsString();
//        String tenantId = employeeInfo.getAsJsonArray("userJobsDTOs").get(0).getAsJsonObject().get("tenantId").getAsString();
//        String companyName = employeeInfo.get("companyName").getAsString();
//        String companyOID = employeeInfo.get("companyOID").getAsString();
//        String companyId = employeeInfo.getAsJsonArray("userJobsDTOs").get(0).getAsJsonObject().get("companyId").getAsString();
//        //获取公司code
//        String companyCode = infraStructure.getCompanyCode(employee,companyId);
//        //获取该员工所在的租户信息
//        JsonObject tentantInfo = infraStructure.getTenantInfo(employee,tenantId);
//        String tenantName = tentantInfo.get("tenantName").getAsString();
//        String tenantCode = tentantInfo.get("tenantCode").getAsString();
//        JsonObject trainBaseSettlement = settlementInfo.get("trainBaseSettlement").getAsJsonObject();
//        trainBaseSettlement.addProperty("supplierCode",supplierName);
//        trainBaseSettlement.addProperty("corpId",corpId);
//        trainBaseSettlement.addProperty("companyOid",companyOID);
//        trainBaseSettlement.addProperty("tenantCode",tenantCode);
//        trainBaseSettlement.addProperty("tenantName",tenantName);
//        trainBaseSettlement.addProperty("companyName",companyName);
//        trainBaseSettlement.addProperty("companyCode",companyCode);
//        trainBaseSettlement.addProperty("bookClerkEmployeeOid",employeeInfo.get("userOID").getAsString());
//        settlementInfo.add("trainBaseSettlement",trainBaseSettlement);
//        //火车结算数据的乘客相关信息
//        JsonArray trainPassengerInfos = settlementInfo.getAsJsonArray("trainPassengerInfos");
//        trainPassengerInfos.get(0).getAsJsonObject().addProperty("passengerOid",passengerEmployeeOid);
//        settlementInfo.add("trainPassengerInfos",trainPassengerInfos);
//        settlement.getAsJsonArray("trainSettlementInfos").add(settlementInfo);
//        return settlement;
//    }

    /**
     * 机票订单组装数据
     * @param employee
     * @param orderData
     * @param supplierName
     * @param supplierCode
     * @return
     * @throws HttpStatusException
     */
    public JsonObject setOrderData(Employee employee,JsonObject orderData,String type,String supplierName,String supplierCode) throws HttpStatusException {
        JsonObject baseOrder =new JsonObject();
        JsonArray passengerInfo = new JsonArray();
        JsonObject employeeInfo = new JsonObject();
        JsonObject passagerInfos = new JsonObject();
        switch (type) {
            case "fight":
                baseOrder = orderData.getAsJsonObject("airBaseOrder");
                passengerInfo = orderData.getAsJsonArray("airPassengerInfo");
                //查找预定人的信息
                employeeInfo = infraStructure.getUserDetail(employee, baseOrder.get("preEmployeeId").getAsString());
                //根据工号查询乘客的信息
                passagerInfos = infraStructure.getUserDetail(employee, passengerInfo.get(0).getAsJsonObject().get("passengerNum").getAsString());
                break;
            case "train":
                baseOrder = orderData.getAsJsonObject("trainOrderBase");
                passengerInfo = orderData.getAsJsonArray("trainOrderPassengerInfos");
                //根据员工的工号 查询订票人的信息
                employeeInfo = infraStructure.getUserDetail(employee, baseOrder.get("employeeNum").getAsString());
                //根据工号查询乘客的信息
                passagerInfos = infraStructure.getUserDetail(employee, passengerInfo.get(0).getAsJsonObject().get("passengerNum").getAsString());
                break;
            case "hotel":
                baseOrder = orderData.getAsJsonObject("hotelOrderBase");
                passengerInfo = orderData.getAsJsonArray("hotelOrderPassengerInfos");
                //根据员工的工号 查询订票人的信息
                employeeInfo = infraStructure.getUserDetail(employee, baseOrder.get("employeeId").getAsString());
                //根据工号查询乘客的信息
                passagerInfos = infraStructure.getUserDetail(employee, passengerInfo.get(0).getAsJsonObject().get("passengerNum").getAsString());
                break;
        }
        String tenantId = employeeInfo.getAsJsonArray("userJobsDTOs").get(0).getAsJsonObject().get("tenantId").getAsString();
        String companyName = employeeInfo.get("companyName").getAsString();
        String companyOID = employeeInfo.get("companyOID").getAsString();
        String companyId = employeeInfo.getAsJsonArray("userJobsDTOs").get(0).getAsJsonObject().get("companyId").getAsString();
        String departmentOID = employeeInfo.get("departmentOID").getAsString();
        String departmentName = employeeInfo.get("departmentName").getAsString();
        String departmentCode = infraStructure.getDeptCode(employee,departmentOID);
        String companyCode = infraStructure.getCompanyCode(employee,companyId);
        //获取该员工所在的租户信息
        JsonObject tentantInfo = infraStructure.getTenantInfo(employee,tenantId);
        String tenantName = tentantInfo.get("tenantName").getAsString();
        String tenantCode = tentantInfo.get("tenantCode").getAsString();
        String passengerEmployeeOid = passagerInfos.get("userOID").getAsString();
        String passagerDepartmentName = passagerInfos.get("departmentName").getAsString();
        String passagerDepartmentOID = passagerInfos.get("departmentOID").getAsString();
        String passagerDepartmentCode = infraStructure.getDeptCode(employee,passagerDepartmentOID);
        //组装数据
        baseOrder.addProperty("tenantCode",tenantCode);
        baseOrder.addProperty("tenantName",tenantName);
        baseOrder.addProperty("companyName",companyName);
        baseOrder.addProperty("companyCode",companyCode);
        baseOrder.addProperty("companyOid",companyOID);
        baseOrder.addProperty("departmentName",departmentName);
        baseOrder.addProperty("departmentCode",departmentCode);
        baseOrder.addProperty("departmentOid",departmentOID);
        baseOrder.addProperty("supplierName",supplierName);
        baseOrder.addProperty("supplierCode",supplierCode);
        if(type.equals("flight")){
            baseOrder.addProperty("preEmployeeOid",employeeInfo.get("userOID").getAsString());
        }else if(type.equals("train")){
            baseOrder.addProperty("preEmployeeOid",employeeInfo.get("userOID").getAsString());
        }else if(type.equals("hotel")){
            baseOrder.addProperty("preEmployeeOId",employeeInfo.get("userOID").getAsString());
        }

        passengerInfo.get(0).getAsJsonObject().addProperty("departmentCode",passagerDepartmentCode);
        passengerInfo.get(0).getAsJsonObject().addProperty("departmentName",passagerDepartmentName);
        passengerInfo.get(0).getAsJsonObject().addProperty("passengerOid",passengerEmployeeOid);
        switch (type) {
            case "flight":
                orderData.add("airBaseOrder", baseOrder);
                orderData.add("airPassengerInfo", passengerInfo);
                break;
            case "train":
                orderData.add("trainOrderBase", baseOrder);
                orderData.add("trainOrderPassengerInfos", passengerInfo);
                break;
            case "hotel":
                orderData.add("hotelOrderBase", baseOrder);
                orderData.add("hotelOrderPassengerInfos", passengerInfo);
                break;
        }
        return orderData;
    }
}
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
    public JsonObject setFlightSettlementData(Employee employee, JsonObject settlement, String supplierCode, String corpId) throws HttpStatusException {
        JsonObject settlementInfo = settlement.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject();
        //根据员工的工号 查询订票人的信息
        JsonObject employeeInfo = infraStructure.getUserDetail(employee,settlementInfo.get("bookClerkEmployeeId").getAsString());
        //根据工号查询乘客的信息
        String passengerEmployeeOid = infraStructure.getUserDetail(employee,settlementInfo.get("passengerEmployeeId").getAsString()).get("userOID").getAsString();
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
        settlementInfo.addProperty("passengerEmployeeOid",passengerEmployeeOid);
        settlement.getAsJsonArray("flightSettlementList").add(settlementInfo);
        return settlement;
    }


    /**
     * 初始化读取到的结算数据
     * @param
     * @return
     */
    public JsonObject setTrainSettlementData(Employee employee,JsonObject settlement, String supplierName,String corpId) throws HttpStatusException {
        JsonObject settlementInfo = settlement.getAsJsonArray("trainSettlementInfos").get(0).getAsJsonObject();
        //根据员工的工号 查询订票人的信息
        JsonObject employeeInfo = infraStructure.getUserDetail(employee,settlementInfo.getAsJsonObject("trainBaseSettlement").get("bookClerkEmployeeId").getAsString());
        //根据工号查询乘客的信息
        String passengerEmployeeOid = infraStructure.getUserDetail(employee,settlementInfo.getAsJsonArray("trainPassengerInfos").get(0).getAsJsonObject().get("passengerCode").getAsString()).get("userOID").getAsString();
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

        JsonObject trainBaseSettlement = settlementInfo.get("trainBaseSettlement").getAsJsonObject();
        trainBaseSettlement.addProperty("supplierCode",supplierName);
        trainBaseSettlement.addProperty("corpId",corpId);
        trainBaseSettlement.addProperty("companyOid",companyOID);
        trainBaseSettlement.addProperty("tenantCode",tenantCode);
        trainBaseSettlement.addProperty("tenantName",tenantName);
        trainBaseSettlement.addProperty("companyName",companyName);
        trainBaseSettlement.addProperty("companyCode",companyCode);
        trainBaseSettlement.addProperty("bookClerkEmployeeOid",employeeInfo.get("userOID").getAsString());
        settlementInfo.add("trainBaseSettlement",trainBaseSettlement);
        //火车结算数据的乘客相关信息
        JsonArray trainPassengerInfos = settlementInfo.getAsJsonArray("trainPassengerInfos");
        trainPassengerInfos.get(0).getAsJsonObject().addProperty("passengerOid",passengerEmployeeOid);
        settlementInfo.add("trainPassengerInfos",trainPassengerInfos);
        settlement.getAsJsonArray("trainSettlementInfos").add(settlementInfo);
        return settlement;
    }

    /**
     * 机票订单组装数据
     * @param employee
     * @param orderData
     * @param supplierName
     * @param supplierCode
     * @return
     * @throws HttpStatusException
     */
    public JsonObject setFlightOrderData(Employee employee,JsonObject orderData,String supplierName,String supplierCode) throws HttpStatusException {
        JsonObject airBaseOrder = orderData.getAsJsonObject("airBaseOrder");
        JsonArray airPassengerInfo = orderData.getAsJsonArray("airPassengerInfo");
        //根据员工的工号 查询订票人的信息
        JsonObject employeeInfo = infraStructure.getUserDetail(employee,airBaseOrder.get("preEmployeeId").getAsString());
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
        //根据工号查询乘客的信息
        JsonObject passagerInfo = infraStructure.getUserDetail(employee,airPassengerInfo.get(0).getAsJsonObject().get("passengerNum").getAsString());
        String passengerEmployeeOid = passagerInfo.get("userOID").getAsString();
        String passagerDepartmentName = passagerInfo.get("departmentName").getAsString();
        String passagerDepartmentOID = passagerInfo.get("departmentOID").getAsString();
        String passagerDepartmentCode = infraStructure.getDeptCode(employee,passagerDepartmentOID);
        //组装数据
        airBaseOrder.addProperty("tenantCode",tenantCode);
        airBaseOrder.addProperty("tenantName",tenantName);
        airBaseOrder.addProperty("companyName",companyName);
        airBaseOrder.addProperty("companyCode",companyCode);
        airBaseOrder.addProperty("companyOid",companyOID);
        airBaseOrder.addProperty("departmentName",departmentName);
        airBaseOrder.addProperty("departmentCode",departmentCode);
        airBaseOrder.addProperty("departmentOid",departmentOID);
        airBaseOrder.addProperty("supplierName",supplierName);
        airBaseOrder.addProperty("supplierCode",supplierCode);
        airBaseOrder.addProperty("preEmployeeOid",employeeInfo.get("userOID").getAsString());
        airPassengerInfo.get(0).getAsJsonObject().addProperty("departmentCode",passagerDepartmentCode);
        airPassengerInfo.get(0).getAsJsonObject().addProperty("departmentName",passagerDepartmentName);
        airPassengerInfo.get(0).getAsJsonObject().addProperty("passengerOid",passengerEmployeeOid);
        orderData.add("airBaseOrder",airBaseOrder);
        orderData.add("airPassengerInfo",airPassengerInfo);
        return orderData;
    }

    /**
     *  火车订单数据组装
     * @param employee
     * @param orderData
     * @param supplierName
     * @param supplierCode
     * @return
     * @throws HttpStatusException
     */
    public JsonObject setTrainOrderData(Employee employee,JsonObject orderData,String supplierName,String supplierCode) throws HttpStatusException {
        JsonObject trainOrderBase = orderData.getAsJsonObject("trainOrderBase");
        JsonArray trainOrderPassengerInfos = orderData.getAsJsonArray("trainOrderPassengerInfos");
        //根据员工的工号 查询订票人的信息
        JsonObject employeeInfo = infraStructure.getUserDetail(employee,trainOrderBase.get("employeeNum").getAsString());
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
        //根据工号查询乘客的信息
        JsonObject passagerInfo = infraStructure.getUserDetail(employee,trainOrderPassengerInfos.get(0).getAsJsonObject().get("passengerNum").getAsString());
        String passengerEmployeeOid = passagerInfo.get("userOID").getAsString();
        String passagerDepartmentName = passagerInfo.get("departmentName").getAsString();
        String passagerDepartmentOID = passagerInfo.get("departmentOID").getAsString();
        String passagerDepartmentCode = infraStructure.getDeptCode(employee,passagerDepartmentOID);
        //组装数据
        trainOrderBase.addProperty("tenantCode",tenantCode);
        trainOrderBase.addProperty("tenantName",tenantName);
        trainOrderBase.addProperty("companyName",companyName);
        trainOrderBase.addProperty("companyCode",companyCode);
        trainOrderBase.addProperty("companyOid",companyOID);
        trainOrderBase.addProperty("departmentName",departmentName);
        trainOrderBase.addProperty("departmentCode",departmentCode);
        trainOrderBase.addProperty("departmentOid",departmentOID);
        trainOrderBase.addProperty("supplierName",supplierName);
        trainOrderBase.addProperty("supplierCode",supplierCode);
        trainOrderBase.addProperty("preEmployeeOid",employeeInfo.get("userOID").getAsString());
        trainOrderPassengerInfos.get(0).getAsJsonObject().addProperty("departmentCode",passagerDepartmentCode);
        trainOrderPassengerInfos.get(0).getAsJsonObject().addProperty("departmentName",passagerDepartmentName);
        trainOrderPassengerInfos.get(0).getAsJsonObject().addProperty("passengerOid",passengerEmployeeOid);
        orderData.add("airBaseOrder",trainOrderBase);
        orderData.add("airPassengerInfo",trainOrderPassengerInfos);
        return orderData;
    }
}
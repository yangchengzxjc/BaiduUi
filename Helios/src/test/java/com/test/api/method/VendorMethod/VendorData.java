package com.test.api.method.VendorMethod;

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
     *         3.tmc推送需要corpId 才能知道是那个租户的员工。
     *         4.需要一个接口是根据teantId 查询到这个租户的corpId.
     *         这样就可以实现给我一个账号  这个账号中的tmc  推数据就可以实现一个租户推送的数据是真实的 并且可以使用逻辑校验
     */


    /**
     * 初始化读取到的结算数据
     * @param
     * @return
     */
    public JsonObject setSettlementData(Employee employee,JsonObject settlement, String appName,String corpId) throws HttpStatusException {
        JsonObject settlementInfo = settlement.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject();
        //根据员工的工号 查询订票人的信息
        JsonObject employeeInfo = infraStructure.getUserDetail(employee,employee.getEmployeeID());
        //根据工号查询乘客的信息
        String passengerEmployeeOid = infraStructure.getUserDetail(employee,employee.getEmployeeID()).get("userOID").getAsString();
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
        settlementInfo.addProperty("supplierCode",appName);
        settlementInfo.addProperty("corpId",corpId);
        settlementInfo.addProperty("companyOid",companyOID);
        settlementInfo.addProperty("tenantCode",tenantCode);
        settlementInfo.addProperty("tenantName",tenantName);
        settlementInfo.addProperty("companyName",companyName);
        settlementInfo.addProperty("companyCode",companyCode);
        settlementInfo.addProperty("passengerEmployeeOid",passengerEmployeeOid);
        settlement.add("flightSettlementList",settlementInfo);
        return settlement;
    }
}

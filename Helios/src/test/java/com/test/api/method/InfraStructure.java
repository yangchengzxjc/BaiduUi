package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.api.InfraStructureApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.InfraEmployee;
import com.hand.basicObject.InfraJob;
import com.hand.utils.GsonUtil;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author peng.zhang
 * @Date 2020/7/2
 * @Version 1.0
 **/
public class InfraStructure {
    private InfraStructureApi infraStructureApi;

    public InfraStructure(){
        infraStructureApi =new InfraStructureApi();
    }

    /**
     * 根据值列表的名称获取系统值列表的systemCustomEnumerationType   可以根据这个获取值列表的详情
     * @param employee
     * @param customEnumerationNameValue  比如：职务，级别，性别，人员类型等
     * @return
     * @throws HttpStatusException
     */
    public String getSystemCustomEnumerationType(Employee employee,String customEnumerationNameValue) throws HttpStatusException {
        JsonArray array =infraStructureApi.getsystemCustomEnumerationType(employee);
        return GsonUtil.getJsonValue(array,"name",customEnumerationNameValue,"systemCustomEnumerationType");
    }

    /**
     * 获取员工的扩展字段暂未输值
     * @param employee
     * @throws HttpStatusException
     */
    private JsonArray getEmployeeExpandForm(Employee employee) throws HttpStatusException {
        String formOID = infraStructureApi.getEmployeeExpandFormOID(employee).get("formOID").getAsString();
        return infraStructureApi.getEmployeeExpandValue(employee,formOID);
    }

    /**
     * 根据值列表详情获取值列表的值
     * @param employee
     * @param customEnumerationNameValue  值列表的类型 ：性别
     * @param customEnumerationName   值列表的值得名称：男
     * @return
     * @throws HttpStatusException
     */
    public String getCustomEnumerationValue(Employee employee,String customEnumerationNameValue,String customEnumerationName) throws HttpStatusException {
        JsonArray array = infraStructureApi.getSystemEnumerationDetail(employee,getSystemCustomEnumerationType(employee,customEnumerationNameValue));
        return GsonUtil.getJsonValue(array,"messageKey",customEnumerationName,"value");
    }

    /**
     * 员工岗位添加
     * @param infraJob
     * @return
     */
    public JsonArray userJobsDTOs(ArrayList<InfraJob> infraJob){
         return new JsonParser().parse(GsonUtil.objectToString(infraJob)).getAsJsonArray();
    }

    /**
     * 新增员工
     * @param employee
     * @param infraEmployee
     * @throws HttpStatusException
     */
    public void addEmployee(Employee employee, InfraEmployee infraEmployee, ArrayList<InfraJob> infraJobs) throws HttpStatusException {
        JsonObject object = infraStructureApi.addEmployee(employee,infraEmployee,userJobsDTOs(infraJobs),getEmployeeExpandForm(employee));
    }

    /**
     * 根据员工的fullName 返回userOID
     * @param employee
     * @param fullName
     * @return
     * @throws HttpStatusException
     */
    public String  searchUser(Employee employee,String fullName) throws HttpStatusException {
        return GsonUtil.getJsonValue(infraStructureApi.getUser(employee),"fullName",fullName,"userOID");
    }

    /**
     * 根据员工的公司名称 返回companyOID
     * @param employee
     * @param companyName
     * @return
     * @throws HttpStatusException
     */
    public Map<String,String> searchCompany(Employee employee, String companyName) throws HttpStatusException {
         HashMap<String,String> map =new HashMap<>();
         JsonObject object = infraStructureApi.searchCompany(employee,companyName).get(0).getAsJsonObject();
         map.put("companyId",object.get("id").getAsString());
         map.put("companyOID",object.get("companyOID").getAsString());
         return map;
    }

    /**
     * 获取部门
     * @param employee
     * @param deptCode  部门编码
     * @param companyOID
     * @return
     * @throws HttpStatusException
     */
    public  Map<String,String> searchDepartment(Employee employee,String deptCode,String companyOID) throws HttpStatusException {
        HashMap<String,String> map =new HashMap<>();
        JsonObject object =infraStructureApi.searchDepartment(employee,deptCode,companyOID).get(0).getAsJsonObject();
        map.put("departmentId",object.get("departmentId").getAsString());
        map.put("departmentPath",object.get("path").getAsString());
        return map;
    }
}

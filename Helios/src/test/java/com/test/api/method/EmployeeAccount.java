package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.EmployeeInfoApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author peng.zhang
 * @Date 2020/6/10
 * @Version 1.0
 **/

@Slf4j
public class EmployeeAccount{

    private EmployeeInfoApi employeeInfoApi;
    public EmployeeAccount() {
        employeeInfoApi =new EmployeeInfoApi();
    }

    /**
     * set 用户的信息
     * @param employee
     */
    public Employee setEmployeeInfo(Employee employee){
        JsonObject jsonObject=null;
        try {
            jsonObject= employeeInfoApi.getEmployeeInfo(employee);
        } catch (HttpStatusException e) {
            e.printStackTrace();
        }
        employee.setUserOID(jsonObject.get("userOID").getAsString());
        employee.setSetOfBookId(getSetOfBooksId(employee));
        employee.setCompanyOID(jsonObject.get("companyOID").getAsString());
        employee.setFullName(jsonObject.get("fullName").getAsString());
        employee.setTenantId(jsonObject.get("tenantId").getAsString());
        employee.setDepartmentOID(jsonObject.get("departmentOID").getAsString());
        employee.setDepartmentName(jsonObject.get("departmentName").getAsString());
        employee.setCompanyName(jsonObject.get("companyName").getAsString());
        employee.setEmployeeID(jsonObject.get("employeeID").getAsString());
        employee.setJobId(jsonObject.get("companyWithUserJobsDTOList").getAsJsonArray().get(0).getAsJsonObject().get("userJobsDTOList").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString());
        employee.setMobile(jsonObject.get("mobile").getAsString());
        employee.setEmail(jsonObject.get("email").getAsString());
        //获取当前用户的租户信息
        try{
            JsonObject tenantInfo = employeeInfoApi.getTenantInfo(employee);
            employee.setTenantCode(tenantInfo.get("tenantCode").getAsString());
            employee.setTenantName(tenantInfo.get("tenantName").getAsString());
            //获取当前登录人的公司信息
            JsonObject companyInfo = employeeInfoApi.getCompanys(employee);
            employee.setCompanyCode(companyInfo.get("companyCode").getAsString());
        }catch (HttpStatusException e){
            e.printStackTrace();
        }
        return employee;
    }



    public void setEmployeeInfo(Employee employee,String departmentName){
        JsonObject jsonObject=null;
        String companyOID="";
        String departmentOID="";
        String jobId="";
        try {
            jsonObject= employeeInfoApi.getEmployeeInfo(employee);
        } catch (HttpStatusException e) {
            e.printStackTrace();
        }
        JsonArray jsonArray = jsonObject.get("companyWithUserJobsDTOList").getAsJsonArray();
        for(int i=0; i<jsonArray.size();i++){
            JsonArray userJobsDTOList=jsonArray.get(i).getAsJsonObject().get("userJobsDTOList").getAsJsonArray();
            for(int j=0; j<userJobsDTOList.size();j++){
                if(userJobsDTOList.get(j).getAsJsonObject().get("departmentName").equals(departmentName)){
                    companyOID=userJobsDTOList.get(j).getAsJsonObject().get("companyOID").getAsString();
                    departmentOID=userJobsDTOList.get(j).getAsJsonObject().get("departmentOID").getAsString();
                    jobId=userJobsDTOList.get(j).getAsJsonObject().get("id").getAsString();
                }
            }
        }
        employee.setUserOID(jsonObject.get("userOID").getAsString());
        employee.setSetOfBookId(getSetOfBooksId(employee));
        employee.setCompanyOID(companyOID);
        employee.setFullName(jsonObject.get("fullName").getAsString());
        employee.setTenantId(jsonObject.get("tenantId").getAsString());
        employee.setDepartmentOID(departmentOID);
        employee.setJobId(jobId);
    }

    /**
     * 获取账套的信息
     * @param employee
     * @return
     */
    public String getSetOfBooksId(Employee employee){
        JsonObject jsonObject=null;
        try {
            jsonObject = employeeInfoApi.getCompanys(employee);
        } catch (HttpStatusException e) {
            e.printStackTrace();
        }
        return jsonObject.get("setOfBooksId").getAsString();
    }

//    public String getdepartmentOID(Employee employee,String departmentName){
//        JsonObject jsonObject=null;
//        String departmentOID="";
//        try {
//            jsonObject= employeeInfoApi.getEmployeeInfo(employee);
//        } catch (HttpStatusException e) {
//            e.printStackTrace();
//        }
//        JsonArray jsonArray = jsonObject.get("companyWithUserJobsDTOList").getAsJsonArray();
//        for(int i=0; i<jsonArray.size();i++){
//            JsonArray userJobsDTOList=jsonArray.get(i).getAsJsonObject().get("userJobsDTOList").getAsJsonArray();
//            for(int j=0; j<userJobsDTOList.size();j++){
//                if(userJobsDTOList.get(j).getAsJsonObject().get("departmentName").equals(departmentName)){
//                    departmentOID=userJobsDTOList.get(j).getAsJsonObject().get("departmentOID").getAsString();
//                }
//            }
//        }
//        return departmentOID;
//    }
//
//    /**
//     * 根据部门名称获取jobId
//     * @param employee
//     * @param departmentName
//     */
//    public String getJobId(Employee employee,String departmentName){
//        JsonObject jsonObject=null;
//        String jobId="";
//        try {
//            jsonObject= employeeInfoApi.getEmployeeInfo(employee);
//        } catch (HttpStatusException e) {
//            e.printStackTrace();
//        }
//        JsonArray jsonArray = jsonObject.get("companyWithUserJobsDTOList").getAsJsonArray();
//        for(int i=0; i<jsonArray.size();i++){
//            JsonArray userJobsDTOList=jsonArray.get(i).getAsJsonObject().get("userJobsDTOList").getAsJsonArray();
//            for(int j=0; j<userJobsDTOList.size();j++){
//                if(userJobsDTOList.get(j).getAsJsonObject().get("departmentName").equals(departmentName)){
//                    jobId=userJobsDTOList.get(j).getAsJsonObject().get("id").getAsString();
//                }
//            }
//        }
//        return jobId;
//    }
}

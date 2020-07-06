package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.InfraEmployee;
import com.hand.basicconstant.ApiPath;
import com.hand.utils.RandomNumber;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Author peng.zhang
 * @Date 2020/6/30
 * @Version 1.0
 **/
@Slf4j
public class InfraStructureApi extends BaseRequest{


    /**
     * 增加员工
     * @param employee
     * @param infraEmployee   基础架构员工对象
     * @param userJobsDTOs    岗位对象  有多岗
     * @param customFormValues 个人信息扩展字段
     * @return
     * @throws HttpStatusException
     */
    public JsonObject addEmployee(Employee employee,InfraEmployee infraEmployee,JsonArray userJobsDTOs,JsonArray customFormValues) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.ADD_EMPLOYEE;
        Random random =new Random();
        random.nextInt();
        JsonObject body =new JsonObject();
        JsonArray array =new JsonArray();
        JsonObject contactI18n1 =new JsonObject();
        JsonObject contactI18n2 =new JsonObject();
        body.add("userJobsDTOs",userJobsDTOs);
        body.addProperty("departmentPath","");
        body.addProperty("directManager",infraEmployee.getDirectManager());
        body.addProperty("directManagerId",infraEmployee.getDirectManagerId());
        body.addProperty("directManagerName",infraEmployee.getDirectManagerName());
        body.addProperty("employeeType",(String) null);
        if(infraEmployee.getEmail()==null){
            body.addProperty("email", RandomNumber.getUUID()+"@hly.com");
        }else{
            body.addProperty("email", infraEmployee.getEmail());
        }
        if(infraEmployee.getMobile()==null){
            body.addProperty("mobile","");
        }else{
            body.addProperty("mobile",infraEmployee.getMobile());
        }
        body.addProperty("mobileCode","86");
        body.addProperty("countryCode","CN");
        body.addProperty("employeeTypeCode",infraEmployee.getEmployeeTypeCode());
        body.addProperty("userOID",(String) null);
        if(infraEmployee.getEmployeeID()==null){
            body.addProperty("employeeID",RandomNumber.getRandomNumber()*new Random().nextInt(20));
        }else{
            body.addProperty("employeeID",infraEmployee.getEmployeeID());
        }
        body.addProperty("fullName",infraEmployee.getFullName());
        body.addProperty("status",1001);
        body.addProperty("manager",false);
        body.addProperty("leavingDate","3018-01-31T16:00:00.000Z");
        body.addProperty("gender","");
        body.addProperty("genderCode",0);
        body.addProperty("birthday","2020-07-01");
        contactI18n1.addProperty("language","zh_cn");
        body.addProperty("entryTime","2020-07-01");
        body.addProperty("gender","");
        contactI18n1.addProperty("value",infraEmployee.getFullName());
        contactI18n2.addProperty("language","en");
        contactI18n2.addProperty("value",infraEmployee.getFullName());
        array.add(contactI18n1);
        array.add(contactI18n2);
        body.add("customFormValues",customFormValues);
        String res= doPost(url,getHeader(employee.getAccessToken()),null,body.toString(),null, employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 获取系统默认的值列表的详情
     */
    public JsonArray getSystemEnumerationDetail(Employee employee,String systemCustomEnumerationTypes) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.QUERY_ENUMERATION_DETAIL;
        Map<String,String> urlParam=new HashMap<>();
        urlParam.put("systemCustomEnumerationType",systemCustomEnumerationTypes);
        urlParam.put("page","0");
        urlParam.put("size","10");
        String res = doGet(url,getHeader(employee.getAccessToken()),urlParam,employee);
        log.info("值列表详情：{}",res);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 获取值列表详情
     * @param employee
     * @param customEnumerationOID
     */
    public JsonObject getEnumerationVDetail(Employee employee,String customEnumerationOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ String.format(ApiPath.ENUMERATION_DETAIL,customEnumerationOID);
        Map<String,String> urlParam=new HashMap<>();
        String res = doGet(url,getHeader(employee.getAccessToken()),urlParam,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 获取员工扩展字段
     * @param employee
     * @param formOID
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getEmployeeExpandValue(Employee employee,String formOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.GET_EMPLOYEE_EXPAND;
        Map<String,String> urlParam=new HashMap<>();
        urlParam.put("formOID",formOID);
        String res = doGet(url,getHeader(employee.getAccessToken()),urlParam,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 获取员工扩展字段的表单
     * @param employee
     * @return 直接get("formOID");
     * @throws HttpStatusException
     */
    public JsonObject getEmployeeExpandFormOID(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.GET_EMPLOYEE_EXPAND_form;
        Map<String,String> urlParam=new HashMap<>();
        urlParam.put("formCode","user_attach_form");
        String res = doGet(url,getHeader(employee.getAccessToken()),urlParam,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 查询系统值列表
     * @param employee
     */
    public JsonArray getsystemCustomEnumerationType(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.System_CUSTOMENUMATIONTYPE;
        Map<String,String> urlParam=new HashMap<>();
        urlParam.put("isCustom","SYSTEM");
        urlParam.put("page","0");
        urlParam.put("size","20");
        String res = doGet(url,getHeader(employee.getAccessToken()),urlParam,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 查询领导  也就是查询员工
     * @param employee
     */
    public JsonArray getUser(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.SEARCH_USER;
        Map<String,String> urlParam=new HashMap<>();
        urlParam.put("page","0");
        urlParam.put("size","10");
        String res = doGet(url,getHeader(employee.getAccessToken()),urlParam,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 查询公司
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonArray searchCompany(Employee employee,String companyName) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.SEARCH_COMPANY;
        Map<String,String> urlParam=new HashMap<>();
        urlParam.put("enabled","true");
        urlParam.put("page","0");
        urlParam.put("size","10");
        urlParam.put("keyword",companyName);
        urlParam.put("keywordLable",companyName);
        String res = doGet(url,getHeader(employee.getAccessToken()),urlParam,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 查询部门
     * @param employee
     * @param companyOID
     * @return
     * @throws HttpStatusException
     */
    public JsonArray searchDepartment(Employee employee,String deptCode,String companyOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.SEARCH_DEPARTMENT;
        Map<String,String> urlParam=new HashMap<>();
        urlParam.put("needVirtual","true");
        urlParam.put("page","0");
        urlParam.put("size","10");
        urlParam.put("name","");
        urlParam.put("deptCode",deptCode);
        urlParam.put("companyOID",companyOID);
        urlParam.put("deptCodeLable",deptCode);
        String res = doGet(url,getHeader(employee.getAccessToken()),urlParam,employee);
        log.info("查询的公司：{}",res);
        return new JsonParser().parse(res).getAsJsonArray();
    }
}

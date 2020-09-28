package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.infrastructure.employee.InfraEmployee;
import com.hand.basicObject.supplierObject.employeeInfoDto.UserCardInfoEntity;
import com.hand.basicconstant.ApiPath;
import com.hand.basicconstant.HeaderKey;
import com.hand.basicconstant.ResourceId;
import com.hand.basicconstant.TmcChannel;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.json.Json;

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
            body.addProperty("email", RandomNumber.getUUID(5)+"@hly.com");
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
        contactI18n1.addProperty("value",infraEmployee.getFullName());
        contactI18n2.addProperty("language","en");
        contactI18n2.addProperty("value",infraEmployee.getFullName());
        array.add(contactI18n1);
        array.add(contactI18n2);
        body.add("contactI18n",array);
        body.add("customFormValues",customFormValues);
        String res= doPost(url,getHeader(employee.getAccessToken()),null,body.toString(),null, employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 编辑员工
     * @param employee
     * @param userInfo   员工详情信息 防止有不修改的字段
     * @param infraEmployee
     * @param customFormValues    如果需要更新customFormValues  则从 userInfo获取到 然后修改customFormValues
     * @param userJobsDTOs  编辑信息的话 同上个人信息扩展字段 不需要修改的话直接从详情中get到
     * @return
     */
    public JsonObject editEmployeeInfo(Employee employee,JsonObject userInfo,InfraEmployee infraEmployee,JsonArray customFormValues,JsonArray userJobsDTOs) throws HttpStatusException{
        String url = employee.getEnvironment().getUrl()+ ApiPath.ADD_EMPLOYEE;
        JsonObject employeeInfo = new JsonObject();
        employeeInfo.addProperty("userOID",userInfo.get("userOID").getAsString());
        try {
            if(infraEmployee.getDirectManager() == null){
                employeeInfo.addProperty("directManager",userInfo.get("directManager").getAsString());
            }else{
                employeeInfo.addProperty("directManager",infraEmployee.getDirectManager());
            }
        }catch (NullPointerException e){
            log.info("员工信息中直属领导为空");
        }
        try{
            if(infraEmployee.getEmployeeTypeCode() == null){
                employeeInfo.addProperty("employeeTypeCode",userInfo.get("employeeTypeCode").getAsString());
            }else{
                employeeInfo.addProperty("employeeTypeCode",infraEmployee.getEmployeeTypeCode());
            }
        }catch (NullPointerException e){
            log.info("员工信息中人员类型为空");
        }
        if(infraEmployee.getEmail() == null){
            employeeInfo.addProperty("email",userInfo.get("email").getAsString());
        }else{
            employeeInfo.addProperty("email",infraEmployee.getEmail());
        }
        employeeInfo.addProperty("countryCode","CN");
        employeeInfo.addProperty("mobileCode","86");
        if(infraEmployee.getEmployeeID() == null){
            employeeInfo.addProperty("employeeID",userInfo.get("employeeID").getAsString());
        }else{
            employeeInfo.addProperty("employeeID",infraEmployee.getEmployeeID());
        }
        try{
            if(infraEmployee.getMobile() == null){
                employeeInfo.addProperty("mobile",userInfo.get("mobile").getAsString());
            }else{
                employeeInfo.addProperty("mobile",infraEmployee.getMobile());
            }
        }catch (NullPointerException e){
            log.info("员工的手机号为空");
        }
        if(infraEmployee.getFullName() ==null){
            employeeInfo.addProperty("fullName","11101");
        }else{
            employeeInfo.addProperty("fullName",infraEmployee.getFullName());
        }
        employeeInfo.addProperty("genderCode",0);
        if(infraEmployee.getBirthday() == null){
            employeeInfo.addProperty("birthday",userInfo.get("birthday").getAsString());
        }else{
            employeeInfo.addProperty("birthday",infraEmployee.getBirthday());
        }
        if(infraEmployee.getEntryTime() == null){
            employeeInfo.addProperty("entryTime",userInfo.get("entryTime").getAsString());
        }else{
            employeeInfo.addProperty("entryTime",infraEmployee.getEntryTime());
        }
        employeeInfo.add("contactI18n",new JsonArray());
        employeeInfo.add("customFormValues",customFormValues);
        employeeInfo.add("userJobsDTOs",userJobsDTOs);
        String res= doPut(url,getHeader(employee.getAccessToken()),null,employeeInfo.toString(),null, employee);
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
    public JsonObject getEnumerationDetail(Employee employee,String customEnumerationOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ String.format(ApiPath.ENUMERATION_DETAIL,customEnumerationOID);
//        Map<String,String> urlParam=new HashMap<>();
        String res = doGet(url,getHeader(employee.getAccessToken()),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

//    /**
//     * 获取员工所有扩展字段
//     * @param employee
//     * @param formOID
//     * @return
//     * @throws HttpStatusException
//     */
//    public JsonObject getEmployeeExpandValue(Employee employee,String formOID) throws HttpStatusException {
//        String url = employee.getEnvironment().getUrl() + String.format(ApiPath.GET_EMPLOYEE_CUSTOM_FORM,formOID);
//        String res = doGet(url,getHeader(employee.getAccessToken()),null,employee);
//        return new JsonParser().parse(res).getAsJsonObject();
//    }

    /**
     * 获取当前启用的人员扩展字段
     * @param employee
     * @param formOID
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getEmployeeExpandValues(Employee employee,String formOID) throws HttpStatusException {
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
     * 根据关键字搜索用户
     * @param employee
     * @param kewWords  姓名  工号  邮箱  手机号  部门
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getUser(Employee employee, String kewWords) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.SEARCH_USER;
        JsonObject body = new JsonObject();
        body.addProperty("page","0");
        body.addProperty("size","10");
        body.addProperty("keyword",kewWords);
        body.addProperty("sort","status");
        body.addProperty("status","all");
        String res = doPost(url,getHeader(employee.getAccessToken()),null,body.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 获取员工详情
     * @param employee
     * @param userOID
     * @return
     */
    public JsonObject employeeDetail(Employee employee,String userOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+String.format(ApiPath.USER_DETAIL,userOID);
        String res = doGet(url,getHeader(employee.getAccessToken()),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
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
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 新增证件信息
     * @param employee
     * @param userCardInfoEntity
     * @return
     * @throws HttpStatusException
     */
    public JsonObject addUserCardInfo(Employee employee,UserCardInfoEntity userCardInfoEntity) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.USERCARD;
        JsonObject body = new JsonObject();
        body.addProperty("cardType",userCardInfoEntity.getCardType().getCardCode());
        body.addProperty("contactCardOID",userCardInfoEntity.getContactCardOID());
        body.addProperty("firstName",userCardInfoEntity.getFirstName());
        body.addProperty("lastName",userCardInfoEntity.getLastName());
        body.addProperty("gender",userCardInfoEntity.getGender());
        body.addProperty("birthday",userCardInfoEntity.getBirthday());
        body.addProperty("nationalityCode",userCardInfoEntity.getNationalityCode());
        body.addProperty("default",userCardInfoEntity.getCardDefault());
        body.addProperty("enable",userCardInfoEntity.getEnable());
        body.addProperty("cardNo",userCardInfoEntity.getCardNo());
        body.addProperty("cardExpiredTime",userCardInfoEntity.getCardExpiredTime());
        body.addProperty("originalCardNo",userCardInfoEntity.getOriginalCardNo());
        body.addProperty("userOID",userCardInfoEntity.getUserOID());
        String res = doPost(url,getHeader(employee.getAccessToken()),null,body.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 根据人员 查询证件
     * @param employee
     * @return
     */
    public JsonArray queryUserCard(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+String.format(ApiPath.QUERY_USER_CARD,employee.getUserOID());
        String res = doGet(url,getHeader(employee.getAccessToken()),null,employee);
        JsonArray userCard =  new JsonParser().parse(res).getAsJsonObject().get("contactCardDTOS").getAsJsonArray();
        return userCard;
    }

    /**
     * 根据人员 查询证件
     * @param employee
     * @return
     */
    public JsonArray queryUserCard(Employee employee,String userOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+String.format(ApiPath.QUERY_USER_CARD,userOID);
        String res = doGet(url,getHeader(employee.getAccessToken()),null,employee);
        JsonArray userCard =  new JsonParser().parse(res).getAsJsonObject().get("contactCardDTOS").getAsJsonArray();
        return userCard;
    }

    /**
     * 员工离职
     * @param employee
     * @param userOID
     * @return
     * @throws HttpStatusException
     */
    public int leaveEmployee(Employee employee,String userOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+String.format(ApiPath.LEAVE_EMPLOYEE,userOID, UTCTime.getBeijingDate(-1));
        HashMap<String,String> map =new HashMap<>();
        map.put("roleType","TENANT");
        return getPostStatusCode(url,getHeader(employee.getAccessToken(), HeaderKey.PERSION_MANAGE, ResourceId.EMPLOYEE_MANAGE),map,new JsonObject().toString(),null,employee);
    }

    /**
     * 员工再次入职
     * @param employee
     * @param userOID  员工的userOID
     * @return
     * @throws HttpStatusException
     */
    public JsonObject rehire(Employee employee, String userOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+String.format(ApiPath.REHIRE,userOID);
        HashMap<String,String> map =new HashMap<>();
        map.put("roleType","TENANT");
        String res = doPost(url,getHeader(employee.getAccessToken(), HeaderKey.PERSION_MANAGE, ResourceId.EMPLOYEE_MANAGE),map,new JsonObject().toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 根据部门oid 查询 部门详情
     * @return
     */
    public JsonObject searchDepartmentDetail(Employee employee,String departmentOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+String.format(ApiPath.SEARCH_DEPARTMENT_DETAILS,departmentOID);
        String res = doGet(url,getHeader(employee.getAccessToken()),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 获取成本中心的值
     * @param employee
     * @param costCenterOID
     * @throws HttpStatusException
     */
    public JsonArray defautCostCenter(Employee employee,String costCenterOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+String.format(ApiPath.DEFAULTCOSTCENT,costCenterOID);
        String res = doGet(url,getHeader(employee.getAccessToken()),null,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 开放平台 查询人员同步入参
     * @param employee
     * @param tmcChannel
     * @param hlyUserMobile
     * @param hlyUserID
     * @throws HttpStatusException
     */
    public JsonArray queryUserSync(Employee employee,TmcChannel tmcChannel, String hlyUserMobile, String hlyUserID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.QUERY_USER_SYNC;
        Map<String,String> maps = new HashMap<>();
        maps.put("tmcChannel",tmcChannel.getTmcChannel());
        maps.put("hlyUserMobile",hlyUserMobile);
        maps.put("hlyUserID",hlyUserID);
        String res = doGet(url,getHeader(employee.getAccessToken()),maps,employee);
        if (res == ""){
            log.info("查询开放平台人员同步入参结果为空，请检查接口请求:tmcChannel:{},hlyUserMobile:{},hlyUserID:{}",tmcChannel,hlyUserMobile,hlyUserID);
        }
        return new JsonParser().parse(res).getAsJsonArray();
    }
}

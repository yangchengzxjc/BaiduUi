package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.api.InfraStructureApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.InfraEmployee;
import com.hand.basicObject.InfraJob;
import com.hand.basicObject.supplierObject.UserCardInfoEntity;
import com.hand.basicconstant.CardType;
import com.hand.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author peng.zhang
 * @Date 2020/7/2
 * @Version 1.0
 **/
@Slf4j
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
     * 获取员工扩展字段customFormValue 暂时为空
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
        String customEnumerationValue="";
        if(GsonUtil.isNotEmpt(array)){
            customEnumerationValue = GsonUtil.getJsonValue(array,"messageKey",customEnumerationName,"value");
        }else{
            log.info("未能获取到值列表详情，请检查入参");
        }
        return customEnumerationValue;
    }

    /**
     * 员工岗位添加  可以有多个岗位直接add 一个或者多个infraJob对象进去即可
     * @param infraJob 员工岗位对象
     * @return
     */
    public JsonArray userJobsDTOs(ArrayList<InfraJob> infraJob){
         return new JsonParser().parse(GsonUtil.objectToString(infraJob)).getAsJsonArray();
    }

    /**
     * 新增员工
     * @param employee
     * @param infraEmployee
     * @param infraJobs
     * @throws HttpStatusException
     */
    public JsonObject addEmployee(Employee employee, InfraEmployee infraEmployee, ArrayList<InfraJob> infraJobs) throws HttpStatusException {
        JsonObject object = infraStructureApi.addEmployee(employee,infraEmployee,userJobsDTOs(infraJobs),getEmployeeExpandForm(employee));
        return object;
    }

    /**
     * 编辑员工
     * @param employee
     * @param userInfo   根据userOID查询到的员工详情
     * @param infraEmployee   编辑员工 只需要初始化需要改的字段  未初始化的字段会从员工详情中获取到
     * @param customFormValues    从详情中可以获取到customFormValues  然后根据修改内容可以重新修改
     * @param userJobsDTOs    从详情中获取到的岗位信息userJobsDTOs 根据需要修改的岗位信息新型修改编辑
     * @throws HttpStatusException
     */
    public JsonObject editEmploye(Employee employee,JsonObject userInfo,InfraEmployee infraEmployee,JsonArray customFormValues,JsonArray userJobsDTOs) throws HttpStatusException {
        return infraStructureApi.editEmployeeInfo(employee,userInfo,infraEmployee,customFormValues,userJobsDTOs);
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
         JsonArray companyList = infraStructureApi.searchCompany(employee,companyName);
         if(GsonUtil.isNotEmpt(companyList)){
             JsonObject object = companyList.get(0).getAsJsonObject();
             map.put("companyId",object.get("id").getAsString());
             map.put("companyOID",object.get("companyOID").getAsString());
         }else{
             log.info("搜索公司列表为空,请检查参数");
         }
         return map;
    }

    /**
     * 获取部门
     * @param employee
     * @param deptCode  部门编码
     * @param companyOID  公司的OID
     * @return
     * @throws HttpStatusException
     */
    public  Map<String,String> searchDepartment(Employee employee,String deptCode,String companyOID) throws HttpStatusException {
        HashMap<String,String> map =new HashMap<>();
        JsonArray departmentList = infraStructureApi.searchDepartment(employee,deptCode,companyOID);
        //判断搜索的部门为空的情况
        if(GsonUtil.isNotEmpt(departmentList)){
            JsonObject object =departmentList.get(0).getAsJsonObject();
            //从搜索部门的响应中获取到部门id以及部门path
            map.put("departmentId",object.get("departmentId").getAsString());
            map.put("departmentPath",object.get("path").getAsString());
        }else{
            log.info("搜索的部门为空,请检查入参");
        }
        return map;
    }

    /**
     * 在员工列表获取员工的userOID
     * @param employee
     * @param keyWords
     * @return
     * @throws HttpStatusException
     */
    private String getUserUserOID(Employee employee, String keyWords) throws HttpStatusException {
        JsonArray userList = infraStructureApi.getUser(employee,keyWords);
        String userOID ="";
        if(GsonUtil.isNotEmpt(userList)){
            userOID = userList.get(0).getAsJsonObject().get("userOID").getAsString();
        }else{
            log.info("用户列表查询结果为空,请检查入参");
        }
        return userOID;
    }

    /**
     * 获取员工详情
     * @param employee
     * @param keyWords
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getUserDetail(Employee employee, String keyWords) throws HttpStatusException {
        return infraStructureApi.employeeDetail(employee,getUserUserOID(employee,keyWords));
    }

    /**
     * 新增证件信息
     * @param employee
     */
    public JsonObject addUserCardInfo(Employee employee,String userOID,CardType cardType,String lastName,Boolean enable) throws HttpStatusException {
        UserCardInfoEntity userCardInfoEntity = new UserCardInfoEntity();
        userCardInfoEntity.setCardType(cardType);
        userCardInfoEntity.setContactCardOID(null);
        userCardInfoEntity.setLastName(lastName);
        userCardInfoEntity.setGender("0");
        userCardInfoEntity.setBirthday("2020-07-29T10:25:11+08:00");
        userCardInfoEntity.setNationalityCode("CN");
        userCardInfoEntity.setCardDefault(false);
        userCardInfoEntity.setEnable(enable);
        userCardInfoEntity.setCardNo("11223344");
        userCardInfoEntity.setOriginalCardNo("");
        userCardInfoEntity.setCardExpiredTime("2022-07-29T10:25:11+08:00");
        userCardInfoEntity.setUserOID(userOID);
        JsonObject cardInfo = infraStructureApi.addUserCardInfo(employee,userCardInfoEntity);
        return cardInfo;
    }

    /**
     * 员工离职
     * @param employee
     * @param fullName
     * @return
     * @throws HttpStatusException
     */
    public int leaveEmployee(Employee employee,String fullName) throws HttpStatusException {
       return infraStructureApi.leaveEmployee(employee,searchUser(employee,fullName));
    }
}

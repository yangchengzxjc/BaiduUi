package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.api.InfraStructureApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.infrastructure.employee.EmployeeExtendComponent;
import com.hand.basicObject.infrastructure.employee.InfraEmployee;
import com.hand.basicObject.infrastructure.employee.InfraJob;
import com.hand.basicObject.supplierObject.UserCardInfoEntity;
import com.hand.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;

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
     * @return 返回这个值列表的type  例如：1001 是人员类型 1002 是职务 等
     * @throws HttpStatusException
     */
    public String getSystemCustomEnumerationType(Employee employee,String customEnumerationNameValue) throws HttpStatusException {
        JsonArray array =infraStructureApi.getsystemCustomEnumerationType(employee);
        return GsonUtil.getJsonValue(array,"name",customEnumerationNameValue,"systemCustomEnumerationType");
    }

    /**
     * 获取员工扩展字段customFormValue   此方法可以为扩展字段没有配置并且有扩展字段但是没有值
     * @param employee
     * @throws HttpStatusException
     */
    public JsonArray getEmployeeExpand(Employee employee) throws HttpStatusException {
        String formOID = infraStructureApi.getEmployeeExpandFormOID(employee).get("formOID").getAsString();
        return infraStructureApi.getEmployeeExpandValues(employee,formOID);
    }

    /**
     * 员工扩展字段有值必填
     * @param employee
     * @param employeeExtendComponent
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getEmployeeExpand(Employee employee,EmployeeExtendComponent employeeExtendComponent) throws HttpStatusException {
        String formOID = infraStructureApi.getEmployeeExpandFormOID(employee).get("formOID").getAsString();
        JsonArray customFormValue = infraStructureApi.getEmployeeExpandValues(employee,formOID);
        for(int i=0;i<customFormValue.size();i++){
            String name = customFormValue.get(i).getAsJsonObject().get("name").getAsString();
            switch (name){
                case "自定义列表":
                    customFormValue.get(i).getAsJsonObject().addProperty("value",employeeExtendComponent.getCustList());
                    break;
                case "数字":
                    customFormValue.get(i).getAsJsonObject().addProperty("value",employeeExtendComponent.getNumber());
                    break;
                case "单行输入框":
                    customFormValue.get(i).getAsJsonObject().addProperty("value",employeeExtendComponent.getText());
                    break;
                case "多行输入框":
                    customFormValue.get(i).getAsJsonObject().addProperty("value",employeeExtendComponent.getTextArea());
                    break;
                case "时间":
                    customFormValue.get(i).getAsJsonObject().addProperty("value",employeeExtendComponent.getTime());
                    break;
                case "日期":
                    customFormValue.get(i).getAsJsonObject().addProperty("value",employeeExtendComponent.getDate());
                    break;
                case "用车制度ID":
                    customFormValue.get(i).getAsJsonObject().addProperty("value",employeeExtendComponent.getCarRuleId());
                    break;
                case "用车备注必填控制":
                    customFormValue.get(i).getAsJsonObject().addProperty("value",employeeExtendComponent.getCarRemark());
                    break;
                case "员工驻地":
                    customFormValue.get(i).getAsJsonObject().addProperty("value",employeeExtendComponent.getEmployeeResident());
                    break;
                case "默认成本中心项":
                    customFormValue.get(i).getAsJsonObject().addProperty("value",employeeExtendComponent.getDefaultCostCenter());
                    break;
                case "员工户籍所在地":
                    customFormValue.get(i).getAsJsonObject().addProperty("value",employeeExtendComponent.getEmployeeDomicile());
                    break;
            }
        }
        return customFormValue;
    }

    /**
     * 根据customEnumerationOID获取自定义值列表的values数据
     * @param employee
     * @param customEnumerationOID
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getEmployeeFiledCustomEnumerationValueDetail(Employee employee,String customEnumerationOID) throws HttpStatusException {
        JsonObject customEnumerationDetail = infraStructureApi.getEnumerationDetail(employee,customEnumerationOID);
        JsonArray customEnumerationValues = customEnumerationDetail.get("values").getAsJsonArray();
        log.info("获取到的员工扩展字段自定义值列表的values数据：" + customEnumerationValues);
        return customEnumerationValues;
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
     * @param customFormValue 员工扩展字段  员工扩展字段配置如果关闭配置的话则传一个空的JsonArray
     * @throws HttpStatusException
     */
    public JsonObject addEmployee(Employee employee,InfraEmployee infraEmployee, ArrayList<InfraJob> infraJobs, JsonArray customFormValue) throws HttpStatusException {
        JsonObject employeeInfo = infraStructureApi.addEmployee(employee,infraEmployee,userJobsDTOs(infraJobs),customFormValue);
        return employeeInfo;
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
     * 根据员工的根据员工的邮箱 返回userOID
     * @param employee
     * @param keyWord   可以使用工号 姓名 邮箱进行搜索  但是工号可能匹配到别的地方 姓名可能重复  尽量使用邮箱进行搜索
     * @return
     * @throws HttpStatusException
     */
    public String searchUser(Employee employee,String keyWord) throws HttpStatusException {
        JsonArray userList = infraStructureApi.getUser(employee, keyWord);
        String userOID ="";
        if(GsonUtil.isNotEmpt(userList)){
            userOID = userList.get(0).getAsJsonObject().get("userOID").getAsString();
        }else{
            log.info("未搜索到用户");
        }
        return userOID;
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
    private String getUserOID(Employee employee, String keyWords) throws HttpStatusException {
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
        return infraStructureApi.employeeDetail(employee,getUserOID(employee,keyWords));
    }

    /**
     * 新增证件信息
     * @param employee
     * @param userCardInfoEntity
     */
    public JsonObject addUserCardInfo(Employee employee,UserCardInfoEntity userCardInfoEntity) throws HttpStatusException {
        return infraStructureApi.addUserCardInfo(employee,userCardInfoEntity);
    }

    /**
     * 员工离职    (员工离职后 员工详情中的status字段是1003 待离职是1002  未离职是1001)
     * @param employee
     * @param keyWord   可以使用工号 姓名 邮箱进行搜索  但是工号可能匹配到别的地方 姓名可能重复  尽量使用邮箱进行搜索
     * @return
     * @throws HttpStatusException
     */
    public int leaveEmployee(Employee employee,String keyWord) throws HttpStatusException {
       return infraStructureApi.leaveEmployee(employee,searchUser(employee,keyWord));
    }

    /**
     * 员工再次入职
     * @param employee
     * @param keyWord   姓名   工号  邮箱等关键字
     * @return
     * @throws HttpStatusException
     */
    public String rehire(Employee employee, String keyWord) throws HttpStatusException {
        JsonObject checkResult = infraStructureApi.rehire(employee,searchUser(employee,keyWord));
        return checkResult.get("checkResult").getAsString();
    }


}

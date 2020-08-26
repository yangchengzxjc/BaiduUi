package com.test.api.method.Infra;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.EmployeeExtendedFields;
import com.hand.basicObject.InfraEmployee;
import com.hand.basicObject.InfraJob;
import com.hand.basicObject.supplierObject.UserCardInfoEntity;
import com.hand.basicconstant.CardType;
import com.hand.utils.GsonUtil;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.test.api.method.InfraStructure;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author peng.zhang
 * @Date 2020/7/3
 * @Version 1.0
 **/
@Slf4j
public class EmployeeManagePage {
    private InfraStructure infraStructure;

    public EmployeeManagePage(){
        infraStructure =new InfraStructure();
    }

    /**
     * 添加员工
     * @param employee
     * @param companyName
     * @param departmentName
     * @param departmentCode
     * @param position
     * @throws HttpStatusException
     */
    public JsonObject addEmployee(Employee employee,String companyName,String departmentName,String departmentCode,String position,String duty,String rank) throws HttpStatusException {
        InfraEmployee infraEmployee =new InfraEmployee();
        InfraJob infraJob  = new InfraJob();
        EmployeeExtendedFields employeeExtendedFields = new EmployeeExtendedFields();
        //邮箱不set的话会有默认值输入
        infraEmployee.setFullName("接口新建"+ UTCTime.getBeijingDate(0));
        infraEmployee.setEmployeeID(String.valueOf(RandomNumber.getRandomNumber()));
        infraEmployee.setMobile("283666"+RandomNumber.getRandomNumber());
        infraEmployee.setEmail(String.format("zhang%s@hui.com",RandomNumber.getRandomNumber()));
        infraEmployee.setEmployeeTypeCode(infraStructure.getCustomEnumerationValue(employee,"人员类型","技术"));
        infraEmployee.setDirectManager(infraStructure.searchUser(employee,"懿测试宏"));
        infraEmployee.setGenderCode(0);
        log.info("新增的员工信息：{}",infraEmployee);
        ArrayList<InfraJob> infraJobArrayList = new ArrayList<>();
        Map<String,String> company =infraStructure.searchCompany(employee,companyName);
        Map<String,String> department = infraStructure.searchDepartment(employee,departmentCode,company.get("companyOID"));
        //初始化岗位信息
        infraJob.setCompanyName(companyName);
        infraJob.setCompanyId(company.get("companyId"));
        infraJob.setDepartmentName(departmentName);
        infraJob.setDepartmentId(department.get("departmentId"));
        infraJob.setDepartmentPath(department.get("departmentPath"));
        infraJob.setDuty(duty);
        infraJob.setDutyCode(infraStructure.getCustomEnumerationValue(employee,"职务",duty));
        infraJob.setRank(rank);
        infraJob.setRankCode(infraStructure.getCustomEnumerationValue(employee,"级别",rank));
        infraJob.setPosition(position);
        infraJob.setCompanyMainPosition(true);
        infraJob.setUni_id(company.get("companyId")+department.get("departmentId")+position);
        infraJob.set_index(0);
        infraJobArrayList.add(infraJob);
        ArrayList<EmployeeExtendedFields> employeeExtendedFieldsArrayList = new ArrayList<>();
        JsonObject employeeFiledDetails = getEmployeeFiledDetails(employee,1).getAsJsonObject();
        employeeExtendedFields.setFieldOID(employeeFiledDetails.get("fieldOID").getAsString());
        employeeExtendedFields.setFieldConstraint(employeeFiledDetails.get("fieldConstraint").getAsString());
        employeeExtendedFields.setFieldContent(employeeFiledDetails.get("fieldContent").getAsString());
        employeeExtendedFields.setFieldName(employeeFiledDetails.get("fieldName").getAsString());
        employeeExtendedFields.setFieldType(employeeFiledDetails.get("fieldType").getAsString());
        employeeExtendedFields.setFormOID(employeeFiledDetails.get("formOID").getAsString());
        employeeExtendedFields.setMessageKey(employeeFiledDetails.get("messageKey").getAsString());
        employeeExtendedFields.setReadOnly(employeeFiledDetails.get("isReadOnly").getAsBoolean());
        employeeExtendedFields.setRequired(employeeFiledDetails.get("required").getAsBoolean());
        employeeExtendedFields.setSequence(employeeFiledDetails.get("sequence").getAsInt());
        employeeExtendedFields.setSystemSequence(employeeFiledDetails.get("systemSequence").getAsInt());
        employeeExtendedFields.setValue("15");
        employeeExtendedFieldsArrayList.add(employeeExtendedFields);
        JsonObject employeeInfo = infraStructure.addEmployee(employee,infraEmployee,infraJobArrayList,employeeExtendedFieldsArrayList);
        return employeeInfo;
    }

    /**
     * 编辑员工 只编辑员工个个人信息  岗位以及其他字段不可编辑
     * @param employee
     * @param keyWords 表示你需要编辑的员工的姓名 手机号 邮箱 以及工号 部门等
     * @param infraEmployee
     * @throws HttpStatusException
     */
    public JsonObject editEmployee(Employee employee, String keyWords, InfraEmployee infraEmployee) throws HttpStatusException {
        //获取员工详情
       JsonObject userInfo = infraStructure.getUserDetail(employee,keyWords);
       JsonArray custformValue = userInfo.get("customFormValues").getAsJsonArray();
       JsonArray userJobsDTOs = userInfo.get("userJobsDTOs").getAsJsonArray();
       return infraStructure.editEmploye(employee,userInfo,infraEmployee,custformValue,userJobsDTOs);
    }

    /**
     *获取员工扩展字段oid
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public String getEmployeeFiledOid(Employee employee) throws HttpStatusException {
        return infraStructure.getEmployeeExpandFormOid(employee);
    }

    /**
     * 根据扩展字段oid获取详情
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getEmployeeFiledDetail(Employee employee) throws HttpStatusException{
        return infraStructure.getEmployeeExpandFormDetail(employee);
    }

    /**
     * 根据systemSequence参数获取第n个扩展字段的详细字段信息
     * @param employee
     * @param systemSequence  第n个扩展字段的详细字段信息
     * @return
     * @throws HttpStatusException
     */
    private JsonObject getEmployeeFiledDetails(Employee employee,int systemSequence) throws HttpStatusException {
        JsonObject employeeFiledDetail = getEmployeeFiledDetail(employee).get(systemSequence).getAsJsonObject();
        log.info("获取到的第 " + systemSequence + " 个扩展字段的段值字段为：" + employeeFiledDetail);
        return employeeFiledDetail;
    }

    /**
     * 获取人员扩展字段里自定义值列表dataSource里的oid，用于查询该值列表里的值列表项数据
     * @param employee
     * @param customNumber      扩展字段序号，数组0开始
     * @return
     * @throws HttpStatusException
     */
    public String getEmployeeFiledCustomEnumerationOID(Employee employee,int customNumber) throws HttpStatusException {
        JsonArray employeeFiledCustomDetail = infraStructure.getEmployeeExpandFormDetail(employee);
        JsonObject filedDetailDetail = employeeFiledCustomDetail.get(customNumber).getAsJsonObject();
        log.info("扩展字段自定义值列表的数据为：" + filedDetailDetail);
        String filedDataSource = filedDetailDetail.get("dataSource").getAsString();
        log.info("扩展字段自定义值列表的dataSource：" + filedDataSource);
        //截取dataSource里的customEnumerationOID
        String filedOid = filedDataSource.substring(25,61);
        log.info("扩展字段自定义值列表的customEnumerationOID：" + filedOid);
        return filedOid;
    }

    /**
     * 根据人员扩展字段中自定义值列表的oid获取值列表value
     * @param employee
     * @param customValueNumber  值列表项序号，数组0开始
     * @param customNumber        扩展字段序号，数组0开始
     * @return
     * @throws HttpStatusException
     */
    public String getEmployeeFiledCustomEnumerationValue(Employee employee, int customValueNumber,int customNumber) throws  HttpStatusException {
        JsonArray employeeFiledCustomEnumerationValues = infraStructure.getEmployeeFiledCustomEnumerationValueDetail(employee,getEmployeeFiledCustomEnumerationOID(employee,customNumber));
        JsonObject customEnumerationValues = employeeFiledCustomEnumerationValues.get(customValueNumber).getAsJsonObject();
        log.info("获取到的自定义值列表第1个jsonobject为：" + customEnumerationValues);
        String customEnumerationValue = customEnumerationValues.get("value").getAsString();
        log.info("获取到的自定义值列表值为：" + customEnumerationValue);
        return customEnumerationValue;

    }

    /**
     * 新增证件
     * @param employee
     * @param userOID
     * @param cardType
     * @param lastName
     * @param enable
     * @return
     * @throws HttpStatusException
     */
    public JsonObject addUserCard(Employee employee,String userOID, CardType cardType,String lastName,Boolean enable) throws HttpStatusException {
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
        JsonObject jsonObject = infraStructure.addUserCardInfo(employee,userCardInfoEntity);
        return jsonObject;
    }

}
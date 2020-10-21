package com.test.api.method.Infra.EmployeeMethod;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.infrastructure.employee.EmployeeExtendComponent;
import com.hand.basicObject.infrastructure.employee.InfraEmployee;
import com.hand.basicObject.infrastructure.employee.InfraJob;
import com.hand.basicObject.supplierObject.employeeInfoDto.UserCardInfoEntity;
import com.hand.basicconstant.CardType;
import com.test.api.method.InfraStructure;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
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
     * 添加员工  包含扩展字段
     * @param employee
     * @param companyName
     * @param departmentName
     * @param departmentCode
     * @param position
     * @throws HttpStatusException
     */
    public JsonObject addEmployee(Employee employee,String fullName,String mobile,String employeeId,
                                  String email,String employeeTypeValueName,String directManager, String companyName,String departmentName,String departmentCode,String position,
                                  String duty,String rank,EmployeeExtendComponent component) throws HttpStatusException {
        InfraEmployee infraEmployee =new InfraEmployee();
        //邮箱不set的话会有默认值输入
        infraEmployee.setFullName(fullName);
        infraEmployee.setEmployeeID(employeeId);
        infraEmployee.setMobile(mobile);
        infraEmployee.setEmail(email);
        //获取人员类型
        infraEmployee.setEmployeeTypeCode(infraStructure.getCustomEnumerationValue(employee,"人员类型",employeeTypeValueName));
        infraEmployee.setDirectManager(infraStructure.searchUser(employee,directManager));
        infraEmployee.setGenderCode(0);
        //获取人员类型
//        String employeeType = infraStructure.getCustomEnumerationValue(employee,"人员类型","业务");
//        infraEmployee.setEmployeeTypeCode(employeeType);
        log.info("新增的员工信息：{}",infraEmployee);
        ArrayList<InfraJob> infraJobArrayList = new ArrayList<>();
        Map<String,String> company =infraStructure.searchCompany(employee,companyName);
        Map<String,String> department = infraStructure.searchDepartment(employee,departmentCode,company.get("companyOID"));
        //初始化岗位信息
        InfraJob infraJob  = new InfraJob();
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
        //获取员工扩展字段
        JsonObject employeeInfo = infraStructure.addEmployee(employee,infraEmployee,infraJobArrayList,infraStructure.getEmployeeExpand(employee,component));
        return employeeInfo;
    }


    /**
     * 添加员工  未配置扩展字段 或员工扩展字段无值
     * @param employee
     * @param companyName
     * @param departmentName
     * @param departmentCode
     * @param position
     * @throws HttpStatusException
     */
    public JsonObject addEmployee(Employee employee,String fullName,String mobile,String employeeId,
                                  String email,String employeeTypeValueName,String directManager, String companyName,String departmentName,String departmentCode,String position,
                                  String duty,String rank) throws HttpStatusException {
        InfraEmployee infraEmployee =new InfraEmployee();
        //邮箱不set的话会有默认值输入
        infraEmployee.setFullName(fullName);
        infraEmployee.setEmployeeID(employeeId);
        infraEmployee.setMobile(mobile);
        infraEmployee.setEmail(email);
        infraEmployee.setEmployeeTypeCode(infraStructure.getCustomEnumerationValue(employee,"人员类型",employeeTypeValueName));
        infraEmployee.setDirectManager(infraStructure.searchUser(employee,directManager));
        infraEmployee.setGenderCode(0);
        //获取人员类型
        String employeeType = infraStructure.getCustomEnumerationValue(employee,"人员类型","业务");
        infraEmployee.setEmployeeTypeCode(employeeType);
        log.info("新增的员工信息：{}",infraEmployee);
        ArrayList<InfraJob> infraJobArrayList = new ArrayList<>();
        Map<String,String> company =infraStructure.searchCompany(employee,companyName);
        Map<String,String> department = infraStructure.searchDepartment(employee,departmentCode,company.get("companyOID"));
        //初始化岗位信息
        InfraJob infraJob  = new InfraJob();
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
        //获取员工扩展字段
        JsonObject employeeInfo = infraStructure.addEmployee(employee,infraEmployee,infraJobArrayList,infraStructure.getEmployeeExpand(employee));
        return employeeInfo;
    }

    /***
     * 新增人员，创建多岗
     * @param employee
     * @param fullName
     * @param mobile
     * @param employeeId
     * @param email
     * @param employeeTypeValueName
     * @param directManager
     * @param companyName
     * @param companyNameMultiplePosts
     * @param departmentName
     * @param departmentNameMultiplePosts
     * @param departmentCode
     * @param departmentCodeMultiplePosts
     * @param position
     * @param positionMultiplePosts
     * @param duty
     * @param rank
     * @param companyMainPosition
     * @param companyMainPositions
     * @param component
     * @return
     * @throws HttpStatusException
     */
    public JsonObject addEmployeeMultiplePosts(Employee employee,String fullName,String mobile,String employeeId,
                                  String email,String employeeTypeValueName,String directManager, String companyName, String companyNameMultiplePosts,
                                  String departmentName,String departmentNameMultiplePosts,String departmentCode,String departmentCodeMultiplePosts,
                                  String position,String positionMultiplePosts,String duty,String rank,boolean companyMainPosition,boolean companyMainPositions,
                                  EmployeeExtendComponent component) throws HttpStatusException {
        InfraEmployee infraEmployee =new InfraEmployee();
        //邮箱不set的话会有默认值输入
        infraEmployee.setFullName(fullName);
        infraEmployee.setEmployeeID(employeeId);
        infraEmployee.setMobile(mobile);
        infraEmployee.setEmail(email);
        //获取人员类型
        infraEmployee.setEmployeeTypeCode(infraStructure.getCustomEnumerationValue(employee,"人员类型",employeeTypeValueName));
        infraEmployee.setDirectManager(infraStructure.searchUser(employee,directManager));
        infraEmployee.setGenderCode(0);
        log.info("新增的员工信息：{}",infraEmployee);
        ArrayList<InfraJob> infraJobArrayList = new ArrayList<>();
        Map<String,String> company =infraStructure.searchCompany(employee,companyName);
        Map<String,String> department = infraStructure.searchDepartment(employee,departmentCode,company.get("companyOID"));
        Map<String,String> companyMultiplePosts =infraStructure.searchCompany(employee,companyNameMultiplePosts);
        Map<String,String> departmentMultiplePosts = infraStructure.searchDepartment(employee,departmentCodeMultiplePosts,"",departmentNameMultiplePosts);
        //初始化岗位信息
        InfraJob infraJob  = new InfraJob();
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
        infraJob.setCompanyMainPosition(companyMainPosition);
        infraJob.setUni_id(company.get("companyId")+department.get("departmentId")+position);
        infraJob.set_index(0);
        InfraJob infraJobs  = new InfraJob();
        infraJobs.setCompanyName(companyNameMultiplePosts);
        infraJobs.setCompanyId(companyMultiplePosts.get("companyId"));
        infraJobs.setDepartmentName(departmentNameMultiplePosts);
        infraJobs.setDepartmentId(departmentMultiplePosts.get("departmentId"));
        infraJobs.setDepartmentPath(departmentMultiplePosts.get("departmentPath"));
        infraJobs.setDuty(duty);
        infraJobs.setDutyCode(infraStructure.getCustomEnumerationValue(employee,"职务",duty));
        infraJobs.setRank(rank);
        infraJobs.setRankCode(infraStructure.getCustomEnumerationValue(employee,"级别",rank));
        infraJobs.setPosition(positionMultiplePosts);
        infraJobs.setCompanyMainPosition(companyMainPositions);
        infraJobs.setUni_id(companyMultiplePosts.get("companyId")+departmentMultiplePosts.get("departmentId")+positionMultiplePosts);
        infraJobs.set_index(1);
        infraJobArrayList.add(infraJob);
        infraJobArrayList.add(infraJobs);
        //获取员工扩展字段
        JsonObject employeeInfo = infraStructure.addEmployee(employee,infraEmployee,infraJobArrayList,infraStructure.getEmployeeExpand(employee,component));
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

//    /**
//     * 根据systemSequence参数获取第n个扩展字段的详细字段信息
//     * @param employee
//     * @param systemSequence  第n个扩展字段的详细字段信息
//     * @return
//     * @throws HttpStatusException
//     */
//    private JsonObject getEmployeeFiledDetails(Employee employee,int systemSequence) throws HttpStatusException {
//        JsonObject employeeFiledDetail = getEmployeeFiledDetail(employee).get(systemSequence).getAsJsonObject();
//        log.info("获取到的第 " + systemSequence + " 个扩展字段的段值字段为：" + employeeFiledDetail);
//        return employeeFiledDetail;
//    }

//    /**
//     * 获取人员扩展字段里自定义值列表dataSource里的oid，用于查询该值列表里的值列表项数据
//     * @param employee
//     * @param customNumber      扩展字段序号，数组0开始
//     * @return
//     * @throws HttpStatusException
//     */
//    public String getEmployeeFiledCustomEnumerationOID(Employee employee,int customNumber) throws HttpStatusException {
//        JsonArray employeeFiledCustomDetail = infraStructure.getEmployeeExpandFormDetails(employee);
//        JsonObject filedDetailDetail = employeeFiledCustomDetail.get(customNumber).getAsJsonObject();
//        String filedDataSource = filedDetailDetail.get("dataSource").getAsString();
//        //截取dataSource里的customEnumerationOID
//        String filedOid = filedDataSource.substring(25,61);
//        log.info("扩展字段自定义值列表的customEnumerationOID：" + filedOid);
//        return filedOid;
//    }

//    /**
//     * 根据人员扩展字段中自定义值列表的oid获取值列表value
//     * @param employee
//     * @param customValueNumber  值列表项序号，数组0开始
//     * @param customNumber        扩展字段序号，数组0开始
//     * @return
//     * @throws HttpStatusException
//     */
//    public String getEmployeeFiledCustomEnumerationValue(Employee employee, int customValueNumber,int customNumber) throws  HttpStatusException {
//        JsonArray employeeFiledCustomEnumerationValues = infraStructure.getEmployeeFiledCustomEnumerationValueDetail(employee,getEmployeeFiledCustomEnumerationOID(employee,customNumber));
//        JsonObject customEnumerationValues = employeeFiledCustomEnumerationValues.get(customValueNumber).getAsJsonObject();
//        String customEnumerationValue = customEnumerationValues.get("value").getAsString();
//        log.info("获取到的自定义值列表值为：" + customEnumerationValue);
//        return customEnumerationValue;
//    }

    /**
     * 新增证件  写死性别 男  生日 2020-07-29 失效日期 2020-07-29 证件号11223344 国籍 中国
     * @param employee
     * @param userOID
     * @param cardType
     * @param lastName
     * @param enable
     * @return
     * @throws HttpStatusException
     */
    public JsonObject addUserCard(Employee employee,String userOID, CardType cardType,String firstName,String lastName,Boolean enable) throws HttpStatusException {
        UserCardInfoEntity userCardInfoEntity = new UserCardInfoEntity();
        userCardInfoEntity.setCardType(cardType);
        userCardInfoEntity.setContactCardOID(null);
        userCardInfoEntity.setFirstName(firstName);
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
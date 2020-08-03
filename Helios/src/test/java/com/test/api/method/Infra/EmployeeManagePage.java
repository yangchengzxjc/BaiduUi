package com.test.api.method.Infra;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.InfraEmployee;
import com.hand.basicObject.InfraJob;
import com.hand.basicObject.supplierObject.UserCardInfoEntity;
import com.hand.basicconstant.CardType;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.test.api.method.InfraStructure;
import lombok.extern.slf4j.Slf4j;

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
        //邮箱不set的话会有默认值输入
        infraEmployee.setFullName("接口新建"+ UTCTime.getBeijingDate(0));
        infraEmployee.setEmployeeID(String.valueOf(RandomNumber.getRandomNumber()));
        infraEmployee.setMobile("283666"+RandomNumber.getRandomNumber());
        infraEmployee.setEmail(String.format("zhang%s@hui.com",RandomNumber.getRandomNumber()));
        infraEmployee.setEmployeeTypeCode(infraStructure.getCustomEnumerationValue(employee,"人员类型","技术"));
        infraEmployee.setDirectManager(infraStructure.searchUser(employee,"懿消费商(xiao/feishang)"));
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
        JsonObject jsonObject = infraStructure.addEmployee(employee,infraEmployee,infraJobArrayList);
        return jsonObject;
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
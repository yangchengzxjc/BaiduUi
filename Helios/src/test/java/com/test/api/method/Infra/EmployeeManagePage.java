package com.test.api.method.Infra;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.InfraEmployee;
import com.hand.basicObject.InfraJob;
import com.hand.utils.RandomNumber;
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
    public void addEmployee(Employee employee,String companyName,String departmentName,String departmentCode,String position) throws HttpStatusException {
        InfraEmployee infraEmployee =new InfraEmployee();
        InfraJob infraJob  = new InfraJob();
        //邮箱不set的话会有默认值输入
        infraEmployee.setFullName("小蓝"+String.valueOf(RandomNumber.getRandomNumber()));
        infraEmployee.setEmployeeID(String.valueOf(RandomNumber.getRandomNumber()));
        infraEmployee.setEmail(String.format("zhang%s@hui.com",RandomNumber.getRandomNumber()));
        infraEmployee.setEmployeeTypeCode(infraStructure.getCustomEnumerationValue(employee,"人员类型","技术"));
        infraEmployee.setDirectManager(infraStructure.searchUser(employee,"懿佳欢_stage"));
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
        infraJob.setDuty("CEO");
        infraJob.setDutyCode(infraStructure.getCustomEnumerationValue(employee,"职务","CEO"));
        infraJob.setRank("P");
        infraJob.setRankCode(infraStructure.getCustomEnumerationValue(employee,"级别","P"));
        infraJob.setPosition(position);
        infraJob.setCompanyMainPosition(true);
        infraJob.setUni_id(company.get("companyId")+department.get("departmentId")+position);
        infraJob.set_index(0);
        infraJobArrayList.add(infraJob);
        infraStructure.addEmployee(employee,infraEmployee,infraJobArrayList);
    }

    /**
     * 编辑员工 只编辑员工个个人信息  岗位以及其他字段不可编辑
     * @param employee
     * @param keyWords 表示你需要编辑的员工的姓名 手机号 邮箱 以及工号 部门等
     * @param infraEmployee
     * @throws HttpStatusException
     */
    public JsonObject editEmploye(Employee employee,String keyWords,InfraEmployee infraEmployee) throws HttpStatusException {
        //获取员工详情
       JsonObject userInfo = infraStructure.getUserDetail(employee,keyWords);
       JsonArray custformValue = userInfo.get("customFormValues").getAsJsonArray();
       JsonArray userJobsDTOs = userInfo.get("userJobsDTOs").getAsJsonArray();
       return infraStructure.editEmploye(employee,userInfo,infraEmployee,custformValue,userJobsDTOs);
    }
}
package com.test.api.method.Infra;

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
}
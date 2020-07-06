package com.hand.basicObject;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author peng.zhang
 * @Date 2020/7/1
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
public class InfraJob {
    //公司名称
    String companyName;
    //公司id
    String companyId;
    String companyOID;
    //部门名称
    String departmentName;
    //部门ID
    String departmentId;
    //职务
    String duty;
    //职务code
    String dutyCode;
    //级别
    String rank;
    String userId;
    //级别代码
    String rankCode;
    //工号
    String employeeId;
    //职位
    String position;
    //是否主岗
    boolean companyMainPosition;
    //部门路径
    String departmentPath;
    boolean deleted  =false;
    //岗位标识
    String uni_id;
    int _index =0;

    public InfraJob(){
    }
}

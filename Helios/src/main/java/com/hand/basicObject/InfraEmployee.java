package com.hand.basicObject;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @Author peng.zhang
 * @Date 2020/6/30
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
public class InfraEmployee {

    //姓名
    private String fullName;
    //工号
    private String employeeID;
    //手机号
    private String mobile;
    //邮箱
    private String email;
    //人员类型
    private String employeeTypeCode;
    //直属领导    为员工的OID
    private String directManager;
    //领导的工号
    private String directManagerId;
    //领导的名字
    private String directManagerName;
}

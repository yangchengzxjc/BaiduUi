package com.hand.basicObject.infrastructure.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeExtendComponent {

    //自定义列表
    private String custList;
    //数字
    private String number;
    //单行输入框
    private String text;
    //多行输入框
    private String textArea;
    //时间
    private String time;
    // 日期
    private String date;
    //常用扩展字段
    private String carRuleId;
    //用车备注
    private String carRemark;
    //员工驻地
    private String employeeResident;
    //员工户籍
    private String employeeDomicile;
    //默认的成本中心项
    private String defaultCostCenter;

}

package com.hand.basicObject.Rule;

import com.google.gson.JsonArray;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author peng.zhang
 * @Date 2020/11/26
 * @Version 1.0
 **/
@Data
public class StandardRules {

    private boolean status = true;
    //人员组默认为所有
    private JsonArray userGroups= new JsonArray();
    /**
     * 控制方式类型： PERIOD (周期管控) SUMMARY(单据内汇总管控) SINGLE(单条管控)
     */
    private String controlModeType = "PERIOD";
    //规则的名称
    private String name;
    // 设置方式   默认为 "EXPENSE_TYPE"  可选："EXPENSE_TYPE_CATEGORY"
    private String setType = "EXPENSE_TYPE";
    //适用的费用类型  如果是费用费用大类的话，则传一个费用大类的code 对象
    private JsonArray expenseTypes;
    //表单
    private JsonArray forms;
    /**
     * 控制方式:每天(DAY),每月(MONTH),每季度(QUARTER),每年(YEAR),汇总校验（SUMMARY),单条管控(SINGLE)
     */
    private String controlType;
    private String crossCompanyStandard = "OWNER";
    /**
     * 控制力度:警告(WARN),禁止(FORBID)
     */
    private String controlLevel = "WARN";
    private boolean complianceCheck = false;
    private String message = "费用超标了";
    private String type = "AMOUNT";
    //空的话则默认 全部参与同住
    private String nonAmountCtrlItem = null;
    /**
     * 费用参与人标准,就高(HIGH)、和值(SUM)
     */
    private String participantsMode;
    /**
     * 费用参与人系数：正整数 1-200 默认100
     */
    private Integer participantsRatio = 100;
    /**
     * 费用参与人标准开关 周期管控 费用参与人默认关闭，无法打开
     */
    private boolean participantsEnable = false;
    private Integer businessType = 1001;
    private String levelCode = "SET_OF_BOOK";
    private String levelOrgId;
    private JsonArray companys;

}

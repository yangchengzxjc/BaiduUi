package com.hand.basicconstant.submitControl;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author peng.zhang
 * @Date 2020/11/25
 * @Version 1.0
 **/
@Getter
@AllArgsConstructor
public enum ControlCond {

    /**
     * 费用类型
     */
    EXPENSE_TYPE(1001, "费用类型"),
    /**
     * 费用消费日期
     */
    EXPENSE_COST_DATE(1002, "费用消费日期"),
    /**
     * 发票开票日期
     */
    INVOICE_BILLING_DATE(1003, "发票开票日期"),
    /**
     * 关联申请的最晚结束日期
     */
    ASSOCIATED_APPLICATION_LATEST_END_DATE(1004, "关联申请单的结束日期"),
    /**
     * 费用消费月
     */
    EXPENSE_COST_MONTH(1005, "费用消费月"),
    /**
     * 费用消费年
     */
    EXPENSE_COST_YEAR(1006, "费用消费年"),
    /**
     * 自定义数
     */
    CUSTOM_NUMBER(1007, null),
    /**
     * 关联申请单的行程城市
     */
    ASSOCIATED_APPLICATION_TRAVEl_CITY(1008, "关联申请单的行程城市"),
    /**
     * 报销单的行程城市
     */
    REPORT_TRAVEl_CITY(1009, "报销单的行程城市"),
    /**
     * 关联申请单的行程日期
     */
    ASSOCIATED_APPLICATION_TRAVEl_DATE(1010, "关联申请单的行程日期"),
    /**
     * 报销单起止日期
     */
    REIMBURSEMENT_START_END_DATE(1011, "报销单起止日期"),
    /**
     * 关联申请单的起止日期
     */
    ASSOCIATED_APPLICATION_START_END_DATE(1012, "关联申请单的起止日期"),
    /**
     * 报销单的行程日期
     */
    REPORT_TRAVEl_DATE(1013, "报销单的行程日期"),
    ;
    private Integer controlCond;

    private String typeName;
}

package com.hand.basicconstant.submitControl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @Author peng.zhang
 * @Date 2020/11/25
 * @Version 1.0
 **/
@Getter
@AllArgsConstructor
public enum ControlItem {


    /**
     * 费用类型
     */
    EXPENSE_TYPE(1001, "费用类型"),
    /**
     * 费用重复校验
     */
    SAME_COST_DATE(1002, "费用重复校验"),

    REIMBURSEMENT_TIMES(1005, "报销次数"),
    /**
     * 报销单提交日期
     */
    REIMBURSEMENT_SUBMIT_DATE(1006, "报销单提交日期"),
    /**
     * 报销单提交月
     */
    REIMBURSEMENT_SUBMIT_MONTH(1007, "报销单提交月"),
    /**
     * 报销单提交年
     */
    REIMBURSEMENT_SUBMIT_YEAR(1008, "报销单提交年"),
    /**
     * 报销单内费用的城市
     */
    REIMBURSEMENT_INVOICE_CITY(1009, "报销单内费用的城市"),
    /**
     * 报销单内费用的消费日期
     */
    REIMBURSEMENT_COST_DATE(1010, "报销单内费用的消费日期"),
    /**
     * 报销单内发票的开票日期
     */
    REIMBURSEMENT_BILLING_DATE(1011, "报销单内发票的开票日期"),
    /**
     * 申请人信用分
     */
    CREDIT_SCORE(1012, "申请人信用分");

    private Integer ControlItem;
    private String typeName;
}

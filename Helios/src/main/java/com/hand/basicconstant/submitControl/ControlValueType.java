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
public enum ControlValueType {


    /**
     * 等于
     */
    EQ(1001, "="),

    /**
     * 大于
     */
    GT(1002, ">"),
    /**
     * 大于等于
     */
    GE(1003, ">="),
    /**
     * 小于
     */
    LT(1004, "<"),
    /**
     * 小于等于
     */
    LE(1005, "<="),

    INCLUDE(1006, "包含"),
    UNINCLUDE(1007, "不包含"),

    /**
     * 不等于
     */
    NE(1008, "≠"),

    IS_NULL(1009, "为空"),

    IS_NOT_NULL(1010, "不为空"),

    BELONG_TO(1011, "归属于"),

    UN_BELONG_TO(1012, "不归属于"),
    ;

    private Integer valueType;
    private String desc;
}

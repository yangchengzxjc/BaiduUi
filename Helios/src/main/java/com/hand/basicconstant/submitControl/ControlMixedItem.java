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
public enum ControlMixedItem {

    PLUS(1001, "+"),
    MINUS(1002, "-"),
    DAY(2001, "天"),
    MONTH(2002, "月"),
    QUARTER(2003, "季度"),
    YEAR(2004, "年"),
    ;

    private Integer mixItem;
    private String desc;
}

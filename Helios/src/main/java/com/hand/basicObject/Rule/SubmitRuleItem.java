package com.hand.basicObject.Rule;

import lombok.Data;

/**
 * @Author peng.zhang
 * @Date 2020/11/19
 * @Version 1.0
 **/
@Data
public class SubmitRuleItem {

    // 1001->费用类型  1002->费用重复校验  1005->报销次数
    private Integer controlItem;
    // 取值方式 1006->包含 1007->不包含
    private Integer valueType;
    //条件
    private Integer controlCond;

    private Object fieldValue;

    private Object extendValue;

    private Long setOfBooksId;
 }

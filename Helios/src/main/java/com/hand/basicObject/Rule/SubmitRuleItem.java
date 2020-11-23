package com.hand.basicObject.Rule;

import lombok.Data;

/**
 * @Author peng.zhang
 * @Date 2020/11/19
 * @Version 1.0
 **/
@Data
public class SubmitRuleItem {

    // 1001->费用类型  1002->费用重复校验  1005->报销次数 1006-> 报销单提交日期
    private Integer controlItem;
    // 取值方式 1006->包含 1007->不包含  1002->为 ">"
    private Integer valueType;
    //条件
    private Integer controlCond;

    private Object fieldValue;

    private Object extendValue;

    private Long setOfBooksId;

    // 是按照天管控还是月 季度 还是年 2001 为按天  2002 为按月
    private Integer mixedItem;
 }

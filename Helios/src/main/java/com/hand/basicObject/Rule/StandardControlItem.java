package com.hand.basicObject.Rule;

import lombok.Data;

import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/12/2
 * @Version 1.0
 **/
@Data
public class StandardControlItem {

    private String id;

    private String controlItem = "INVOICE_AMOUNT";

    private Integer valueType =1002;

    private String controlCond = "BASIC_STANDARD";
    //如果是人员组的话，直接传人员组的名称  后面公共方法会直接修改
    private Object fieldValue;

    private Object extendsValue;

    private String dataType = "DOUBLE";

    private Long setOfBooksId;

    private List<StandardCondition> conditions;

}

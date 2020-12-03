package com.hand.basicObject.Rule;

import com.google.gson.JsonArray;
import lombok.Data;

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

    private Object fieldValue;

    private Object extendsValue;

    private String dataType = "DOUBLE";

    private Long setOfBooksId;

    private JsonArray conditions;

}

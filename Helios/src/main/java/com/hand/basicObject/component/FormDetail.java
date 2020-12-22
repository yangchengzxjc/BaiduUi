package com.hand.basicObject.component;

import lombok.Data;

/**
 * 这个类用来封装报销单详情中的某些信息 比如 expenseReportOID businessOID等
 * @Author peng.zhang
 * @Date 2020/12/22
 * @Version 1.0
 **/
@Data
public class FormDetail {

    private String expenseReportOID;
    //单号
    private String businessCode;
    //表单id
    private String id;
}

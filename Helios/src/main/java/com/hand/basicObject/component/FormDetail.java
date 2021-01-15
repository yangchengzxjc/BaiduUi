package com.hand.basicObject.component;

import com.google.gson.JsonObject;
import lombok.Data;

/**
 * 这个类用来封装报销单详情中的某些信息 比如 expenseReportOID businessOID等
 * @Author peng.zhang
 * @Date 2020/12/22
 * @Version 1.0
 **/
@Data
public class FormDetail {

    //表单oid  包括借款单  报销单  申请单
    private String ReportOID;
    //单号
    private String businessCode;
    //表单id
    private String id;
    //费用的oid
    private String invoiceOID;
    // 接口的响应
    private JsonObject response;
}

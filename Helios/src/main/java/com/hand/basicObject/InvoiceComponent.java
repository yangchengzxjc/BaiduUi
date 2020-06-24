package com.hand.basicObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Author peng.zhang
 * @Date 2020/6/23
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@Builder
public class InvoiceComponent {

    //申请单
    String application;
    //币种 会默认人民币
    String currencyCode;
    //是否公司支付  false=1001   true=1002可选
    boolean isCompanyPay;
    //地点控件  一般名称为出发地和目的地
    String departure ;
    //汇率
    String rate;
    //目的地
    String destination;
    //城市
    String city;
    // 时间
    String dateTime;
    //浮点数
    double floatNumber;
    //正整数
    int number;
    //月份   "2020-06-23T15:45:00Z"
    String month;
    //值列表
    String cust;
    //日期   "2020-06-23T15:44:30Z"
    String date;
    //文本
    String text;
    //参与人 user的个人信息
    String participants;
    //同行人   "{"userOID":"d387ce07-e4f3-487e-a330-c76e56cf4c2e"}"
    String participant;
    //开始结束日期    "{"startDate":"2020-06-22T16:00:00.000Z","endDate":"2020-06-24T16:00:00.000Z","duration":2}"
    String startAndEndData ;
    //业务用途  值列表
    public InvoiceComponent(){

    }

}

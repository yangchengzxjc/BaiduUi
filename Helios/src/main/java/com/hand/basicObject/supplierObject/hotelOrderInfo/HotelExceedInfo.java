package com.hand.basicObject.supplierObject.hotelOrderInfo;

import lombok.Builder;

/**
 * @Author peng.zhang
 * @Date 2020/9/3
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class HotelExceedInfo {

    //关联的订单号
    private String orderNo;
    //违反的差旅政策编码
    private String violationContentCode;
    //违反的差旅政策名称
    private String violationContentName;
    //差旅政策违规原因编码   用户选择的违规理由
    private String violationReasonCode;
    //违规原因  用户选择的违规理由/或用户手填的理由
    private String violationReasonName;
}

package com.hand.basicObject.supplierObject.airOrderInfo;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * @Author peng.zhang
 * @Date 2020/9/4
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class AirExceedInfo {

    //消费商机票唯一标识    该超标对应的机票
    private String ticketKey;
    //机票票号
    private String ticketNo;
    //违反的差旅政策编码
    private String violationContentCode;
    //违反的差旅政策名称 低价RC，舱等RC，提前天数RC等名称
    private String violationContentName;
    //差旅政策违规原因编码   用户选择的违规理由
    private String violationReasonCode;
    //违规原因  用户选择的违规理由/或用户手填的理由
    private String violationReasonName;
    //最低价航班号
    private String lowFlight;
    //最低价航班舱位
    private String lowClass;
    //最低价航班价格
    private BigDecimal lowPrice;
    //最低价航班扣率
    private BigDecimal lowRate;
    //最低价航班起飞时间（当地时间）
    private String lowDTime;
}

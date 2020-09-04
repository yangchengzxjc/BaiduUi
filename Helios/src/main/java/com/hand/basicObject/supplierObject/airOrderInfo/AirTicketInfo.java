package com.hand.basicObject.supplierObject.airOrderInfo;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * @Author peng.zhang
 * @Date 2020/9/4
 * @Version 1.0
 **/
@Builder(toBuilder = true)
public class AirTicketInfo {
    //机票唯一标识
    private String ticketKey;
    //乘客序号
    private String passengerNo;
    //约定的机票最晚出票时间
    private String finalTicketTime;
    //乘客类型   AUT：成人票
    //CHD：儿童票
    //INF：婴儿票
    private String passengerType;
    //PNR
    private String ticketPNR;
    //机票票号
    private String ticketNo;
    //改签前票号
    private String preTicketNum;
    //改签原票号
    private String originalNum;
    //机票票号状态
    private String ticketStatusName;
    //机票票号状态代码
    private String ticketStatusCode;
    //是否协议价    Y：协议价
    //N：非协议价
    private String isPolicy;
    //机票票面价格
    private BigDecimal ticketPrice;
    //差额
    private BigDecimal variance;
    //机票票价折扣率（仅针对国内机票）
    private Double priceRate;
    //燃油费
    private BigDecimal oilFee;
    //税/机建费
    private BigDecimal tax;
    //服务费
    private BigDecimal serverFee;
    //后收服务费
    private BigDecimal postServiceFee;
    //舱等  F：头等舱
    //C：公务舱
    //Y：经济舱
    //SY：超级经济舱
    private String classType;
    //子舱位
    private String subClass;
    //改签规则
    private String rerNotes;
    //退票规则
    private String refNotes;
    //签转规则
    private String endNotes;
    //经济舱标准价
    private String yClassStandardPrice;
    //用户选择机票时价格
    private BigDecimal bufferRecordPrice;


}

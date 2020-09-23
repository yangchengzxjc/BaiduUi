package com.hand.basicObject.supplierObject.syncApproval.syncPlatformEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/18
 * @Version 1.0
 **/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelFlightItinerary {
    /*
        默认:0。
        未知（0）
        头等舱（1）
        公务舱（2）
        超级经济舱（4）
        经济舱（3）
        管控时,审批单舱等级别不能低于订单舱等级别
     */
    private Integer seatClass;
    private Integer discount;
    //机票最大金额
    private BigDecimal ticketPrice;
    //1001 单程  1002  返程
    private Integer itineraryType;
    private Integer itineraryDirection;
    //1001国内机票   1002 国际机票
    private Integer productType;
    private String fromCity;
    private String toCity;
    private String fromCityCode;
    private String toCityCode;
    private List<String> fromCities;
    private List<String> toCities;
    private List<String> fromCityCodes;
    private List<String> toCityCodes;
    private String takeOffBeginTime;
    private String takeOffEndTime;
    private String arrivalBeginTime;
    private String arrivalEndTime;
    private String takeOffBegin;
    private String takeOffEnd;
    private String arrivalBegin;
    private String arrivalEnd;
    //浮动天数
    private Integer floatDaysBegin;
    private Integer floatDaysEnd;

}

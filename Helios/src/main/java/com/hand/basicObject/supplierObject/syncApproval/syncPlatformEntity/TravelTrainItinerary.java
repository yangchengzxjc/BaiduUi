package com.hand.basicObject.supplierObject.syncApproval.syncPlatformEntity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/18
 * @Version 1.0
 **/
@Builder
@Data
public class TravelTrainItinerary {

    private Integer itineraryType;
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
    //浮动天数
    private Integer floatDaysBegin;
    private Integer floatDaysEnd;
    private BigDecimal ticketPrice;
    private List<String> seatType;
}

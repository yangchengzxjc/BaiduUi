package com.hand.basicObject.supplierObject.syncApproval.syncPlatformEntity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/18
 * @Version 1.0
 **/
@Data
public class TravelHotelItinerary {

    private String city;
    private String cityCode;
    private List<String> cities;
    private List<String> cityCodes;
    private String fromDate;
    private String leaveDate;
    private String maxPrice;
    private String minPrice;
    private Integer roomNumber;
    private Integer floatDaysBegin;
    private Integer floatDaysEnd;
}

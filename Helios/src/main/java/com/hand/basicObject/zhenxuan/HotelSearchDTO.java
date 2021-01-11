/**
 * Copyright (c) 2020.year. Shanghai Zhenhui Information Technology Co,. ltd. All rights are reserved.
 *
 * @project Helios
 * @author pan.jiang@huilianyi.com
 * @Date 2021-01-05 - 21:41
 * @description:
 */

package com.hand.basicObject.zhenxuan;

import java.util.List;

public class HotelSearchDTO {

    /**
     * 甄选酒店搜索 RequestDTO
     * <p>
     * cityCode : 310000
     * cityName : 上海
     * checkInDate : 2021-01-08
     * checkOutDate : 2021-01-09
     * pageNo : 1
     * pageSize : 20
     * keywords :
     * distance : 20
     * direct : false
     * expensesFilter : false
     * breakfast : false
     * sortCriteria : 0
     * realTime : false
     * agreement : false
     * hotelStar : ["5"]
     * chainBrandId : ["751","14"]
     * locationV2 : {"type":"business","name":"吉买盛购物中心(8壹广场店)","location":"121.396736,31.620806","address":"八一路206号8壹广场F2层37","adcode":"310151"}
     */

    private String cityCode;
    private String cityName;
    private String checkInDate;
    private String checkOutDate;
    private int pageNo;
    private int pageSize;
    private String keywords;
    private int distance;
    private boolean direct;
    private boolean expensesFilter;
    private boolean breakfast;
    private int sortCriteria;
    private boolean realTime;
    private boolean agreement;
    private LocationV2Bean locationV2;
    private List<String> hotelStar;
    private List<String> chainBrandId;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    public boolean isExpensesFilter() {
        return expensesFilter;
    }

    public void setExpensesFilter(boolean expensesFilter) {
        this.expensesFilter = expensesFilter;
    }

    public boolean isBreakfast() {
        return breakfast;
    }

    public void setBreakfast(boolean breakfast) {
        this.breakfast = breakfast;
    }

    public int getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(int sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public boolean isRealTime() {
        return realTime;
    }

    public void setRealTime(boolean realTime) {
        this.realTime = realTime;
    }

    public boolean isAgreement() {
        return agreement;
    }

    public void setAgreement(boolean agreement) {
        this.agreement = agreement;
    }

    public LocationV2Bean getLocationV2() {
        return locationV2;
    }

    public void setLocationV2(LocationV2Bean locationV2) {
        this.locationV2 = locationV2;
    }

    public List<String> getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(List<String> hotelStar) {
        this.hotelStar = hotelStar;
    }

    public List<String> getChainBrandId() {
        return chainBrandId;
    }

    public void setChainBrandId(List<String> chainBrandId) {
        this.chainBrandId = chainBrandId;
    }

    public static class LocationV2Bean {
        /**
         * type : business
         * name : 吉买盛购物中心(8壹广场店)
         * location : 121.396736,31.620806
         * address : 八一路206号8壹广场F2层37
         * adcode : 310151
         */

        private String type;
        private String name;
        private String location;
        private String address;
        private String adcode;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public LocationV2Bean() {
        }

        public LocationV2Bean(String type, String name, String location, String address, String adcode) {
            this.type = type;
            this.name = name;
            this.location = location;
            this.address = address;
            this.adcode = adcode;
        }
    }

    public HotelSearchDTO() {}

    // 默认请求参数的构造方法
    public HotelSearchDTO(String cityCode, String cityName, String checkInDate, String checkOutDate, int pageNo, int pageSize) {
        this.cityCode = cityCode;
        this.cityName = cityName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    // 全参数构造方法
    public HotelSearchDTO(String cityCode, String cityName, String checkInDate, String checkOutDate,
                          int pageNo, int pageSize, String keywords, int distance, boolean direct,
                          boolean expensesFilter, boolean breakfast, int sortCriteria, boolean realTime,
                          boolean agreement, LocationV2Bean locationV2, List<String> hotelStar, List<String> chainBrandId) {
        this.cityCode = cityCode;
        this.cityName = cityName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.keywords = keywords;
        this.distance = distance;
        this.direct = direct;
        this.expensesFilter = expensesFilter;
        this.breakfast = breakfast;
        this.sortCriteria = sortCriteria;
        this.realTime = realTime;
        this.agreement = agreement;
        this.locationV2 = locationV2;
        this.hotelStar = hotelStar;
        this.chainBrandId = chainBrandId;
    }
}

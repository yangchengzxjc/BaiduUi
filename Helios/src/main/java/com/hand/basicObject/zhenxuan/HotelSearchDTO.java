/**
 * Copyright (c) 2020.year. Shanghai Zhenhui Information Technology Co,. ltd. All rights are reserved.
 *
 * @project Helios
 * @author pan.jiang@huilianyi.com
 * @Date 2021-01-05 - 21:41
 * @description:
 */

package com.hand.basicObject.zhenxuan;

public class HotelSearchDTO {

    /** example👇
     * cityCode : 310000
     * cityName : 上海
     * checkInDate : 2021-01-05
     * checkOutDate : 2021-01-06
     * pageNo : 1
     * pageSize : 20
     * keywords :
     * distance : 20
     * direct : false
     * expensesFilter : false
     * breakfast : false
     * sortCriteria : 0
     * realTime : false
     * location : null
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
    private Object location;

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

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }
}

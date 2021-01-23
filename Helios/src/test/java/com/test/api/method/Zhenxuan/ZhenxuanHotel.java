/**
 * Copyright (c) 2020.year. Shanghai Zhenhui Information Technology Co,. ltd. All rights are reserved.
 *
 * @project Helios
 * @author pan.jiang@huilianyi.com
 * @Date 2021-01-11 - 21:02
 * @description: 甄选酒店 业务方法封装
 */

package com.test.api.method.Zhenxuan;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.api.ZhenxuanApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.zhenxuan.HotelSearchDTO;
import com.hand.utils.UTCTime;

public class ZhenxuanHotel {

    private ZhenxuanApi zhenxuanApi;

    public ZhenxuanHotel() {
        zhenxuanApi = new ZhenxuanApi();
    }

    public JsonObject searchHotelDefault(Employee employee, String cityCode, String cityName) throws HttpStatusException {
        HotelSearchDTO hotelSearchDTO = new HotelSearchDTO();
        // 获取当前日期 yyyy-mm-dd
        hotelSearchDTO.setCheckInDate(UTCTime.getBeijingDate(0));
        hotelSearchDTO.setCheckOutDate(UTCTime.getBeijingDate(1));
        hotelSearchDTO.setCityCode(cityCode);
        hotelSearchDTO.setCityName(cityName);
        hotelSearchDTO.setPageNo(1);
        hotelSearchDTO.setPageSize(20);

        String res = zhenxuanApi.searchHotel(employee, hotelSearchDTO);
        JsonObject resJsonObj = new JsonParser().parse(res).getAsJsonObject();
        return resJsonObj;
    }

    public String searchHotelByLocation(Employee employee, String adcode, HotelSearchDTO hotelSearchDTO) throws  HttpStatusException{
        String hotelMapString = zhenxuanApi.getHotelMap(employee, adcode);
        // todo 组装 locationBean

        HotelSearchDTO.LocationV2Bean locationV2Bean = new HotelSearchDTO.LocationV2Bean();
        locationV2Bean.setAdcode(adcode);
        hotelSearchDTO.setLocationV2(locationV2Bean);
        return zhenxuanApi.searchHotel(employee, hotelSearchDTO);
    }
}

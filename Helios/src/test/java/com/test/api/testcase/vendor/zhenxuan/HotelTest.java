/**
 * Copyright (c) 2020.year. Shanghai Zhenhui Information Technology Co,. ltd. All rights are reserved.
 *
 * @project Helios
 * @author pan.jiang@huilianyi.com
 * @Date 2021-01-05 - 21:07
 * @description:
 */

package com.test.api.testcase.vendor.zhenxuan;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicConstant.City;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import com.test.api.method.Zhenxuan.ZhenxuanHotel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class HotelTest extends BaseTest {

    private Employee employee;
    private ZhenxuanHotel zhenxuanHotel;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("18333333333") String phoneNumber, @Optional("hly123") String pwd, @Optional("console") String env) {
        employee = getEmployee(phoneNumber, pwd, env);
        zhenxuanHotel = new ZhenxuanHotel();
    }

    @DataProvider(name = "city")
    public Object[][] cityDataProvider() {
        return new Object[][]{
                {"上海", "310000", "上海"},
                {"北京", "110000", "北京"}
        };
    }

    @DataProvider(name = "cityV2")
    public Iterator<Object[]> iteratorDataProvider(){
        List<Object[]> resultList = new ArrayList<>();
        // 定义城市 查询数据
        String[] cityList = new String[] {"上海","北京","广州","杭州","厦门","南京","成都","青岛"};
        for (int i = 0; i < cityList.length; i++) {
            resultList.add(
                    new Object[]{cityList[i],
                    City.getCity(cityList[i]).getCityCode(),
                    City.getCity(cityList[i]).getCityName()}
                    );
        }
        return resultList.iterator();
    }

    @Test(description = "甄选酒店默认搜索", dataProvider = "cityV2")
    public void testHotelSearchDefault(String caseDesc, String cityCode, String cityName) throws HttpStatusException {
        JsonObject resJsonObj = zhenxuanHotel.searchHotelDefault(employee, cityCode, cityName);
        log.info("search hotel default: {}-->{}", cityName, resJsonObj);

        // assert
        Integer resCode = resJsonObj.get("code").getAsInt();
        log.debug("rescode: {}", resCode);
        assert 200 == resCode;
        Boolean isSuccess = resJsonObj.get("success").getAsBoolean();
        log.debug("res success: {}", isSuccess);
        assert isSuccess;
        JsonArray resultBody = resJsonObj.getAsJsonArray("body");
        assert resultBody.size() > 0;

        // 搜索结果匹配 cityName
        List<JsonObject> hotelDTOs = new Gson().fromJson(resultBody, new TypeToken<List<JsonObject>>() {}.getType());
        List<JsonObject> hotelFilter = hotelDTOs.stream().filter(
                hotelDTO -> hotelDTO.get("cityName").getAsString().contains(cityName)
        ).collect(Collectors.toList());
        log.debug("hotelResult: {}", hotelFilter);
        assert !hotelFilter.isEmpty();
    }

    @Test
    public void testHotelSearchByLocation(String caseDesc, String cityCode, String cityName) throws HttpStatusException {
        // todo
    }
}

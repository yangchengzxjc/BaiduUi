/**
 * Copyright (c) 2020.year. Shanghai Zhenhui Information Technology Co,. ltd. All rights are reserved.
 *
 * @project Helios
 * @author pan.jiang@huilianyi.com
 * @Date 2021-01-05 - 21:07
 * @description:
 */

package com.test.api.testcase.vendor.zhenxuan;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import com.test.api.method.Zhenxuan.ZhenxuanHotel;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

@Slf4j
public class HotelTest extends BaseTest {

    private Employee employee;
    private ZhenxuanHotel zhenxuanHotel;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("14082978000") String phoneNumber, @Optional("hly123456") String pwd, @Optional("stage") String env) {
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

    @Test(dataProvider = "city")
    public void testHotelSearchDefault(String caseDesc, String cityCode, String cityName) throws HttpStatusException {
        String res = zhenxuanHotel.searchHotelDefault(employee, cityCode, cityName);
        log.info("search hotel default: {}-->{}", cityName, res);
        // todo: assert
    }

    @Test
    public void testHotelSearchByLocation(String caseDesc, String cityCode, String cityName) throws HttpStatusException {
        // todo
    }
}

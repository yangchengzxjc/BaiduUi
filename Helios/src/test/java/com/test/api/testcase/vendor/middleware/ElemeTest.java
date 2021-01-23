/**
 * Copyright (c) 2020.year. Shanghai Zhenhui Information Technology Co,. ltd. All rights are reserved.
 *
 * @project Helios
 * @author pan.jiang@huilianyi.com
 * @Date 2021-01-07 - 17:14
 * @description: eleme Middleware
 */

package com.test.api.testcase.vendor.middleware;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.api.VendorApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.eleme.ElemeOrderReportRequest;
import com.test.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;


@Slf4j
public class ElemeTest extends BaseTest {

    private Employee employee;
    private VendorApi vendorApi;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("14082978000") String phoneNumber, @Optional("hly123456") String pwd, @Optional("stage") String env) {
        employee = getEmployee(phoneNumber, pwd, env);
        vendorApi = new VendorApi();
    }

    @DataProvider(name = "eleme")
    public Object[][] dataProvider1() {
        return new Object[][]{
//                {orderNo, orderNo98, bno, uno, mealNumber, mealReason, mealReceipt, mealDistance, orderType,
//                                     invoice, photograph}
                // uat
//                {"1202034339765235799", "781653488299932848135546", "123", "232731066730761835",
//                        "1", "123123123", "http://uat.huilianyi.com/heliosimg1/a/a9da99b9526eca9f4581d8393034bc86BG.jpg", "123", "1"},
                // stage
                {"2137807627110181105888444", "9999999", "38f463ae-753b-4be5-9884-5b8cdac6fa6c", "13661982832",
                        "1", "mealReason", "http://uat.huilianyi.com/heliosimg1/a/a9da99b9526eca9f4581d8393034bc86BG.jpg",
                        "1000", "1", "https://inv-veri.chinatax.gov.cn/fpcs/zw-img/image001.png", "https://inv-veri.chinatax.gov.cn/fpcs/zw-img/image022.png"}
        };
    }

    // 测试报备接口全参数
    @Test(dataProvider = "eleme", description = "饿了么报备接口")
    public void testElemeOrderReport(String orderNo, String orderNo98, String bno,
                                     String uno, String mealNumber, String mealReason,
                                     String mealReceipt, String mealDistance, String orderType,
                                     String invoice, String photograph) throws HttpStatusException {
        // 测试数据 转 DTO
        ElemeOrderReportRequest elemeOrderReportRequest = new ElemeOrderReportRequest(
                orderNo, orderNo98,
                new ElemeOrderReportRequest.ReportInfo(Integer.parseInt(mealNumber),
                mealReason, mealReceipt, Long.parseLong(mealDistance), invoice, photograph),
                bno, uno, Integer.parseInt(orderType)
        );

        // 发送请求
        String res = vendorApi.elemeOrderReport(employee, elemeOrderReportRequest);
        log.info("res: {}", res);

        JsonObject resJson = new JsonParser().parse(res).getAsJsonObject();
        int res_code = resJson.get("code").getAsInt();
        // 断言
        assert res_code == 200;
    }

    @DataProvider(name = "eleme_invoice_update")
    public Object[][] dataProvider2() {
        return new Object[][]{
//                {String caseDesc, String invoice, String photograph}
//                {"单张图片",
//                        "http://uat.huilianyi.com/heliosimg1/a/a9da99b9526eca9f4581d8393034bc86BG.jpg",
//                        "https://appmail.mail.10086.cn/m6/images/temp/139mail_weixin.png"},
//                {
//                    "多张图片",
//                        "https://inv-veri.chinatax.gov.cn/fpcs/zw-img/image001.png," +
//                                "https://inv-veri.chinatax.gov.cn/fpcs/zw-img/image022.png," +
//                                "https://inv-veri.chinatax.gov.cn/fpcs/zw-img/image002.png," +
//                                "https://inv-veri.chinatax.gov.cn/fpcs/zw-img/image20.jpg",
//                        "https://inv-veri.chinatax.gov.cn/fpcs/zw-img/image004.png," +
//                                "https://inv-veri.chinatax.gov.cn/fpcs/zw-img/image005.png," +
//                                "https://inv-veri.chinatax.gov.cn/fpcs/zw-img/image19.jpg," +
//                                "https://inv-veri.chinatax.gov.cn/fpcs/zw-img/image21.png"
//                },
//                {
//                    "单张图片",
//                        "https://enterprise.elemecdn.com/e/bc/610a395042e27398ee2c93298de4apng.png",
//                        "https://enterprise.elemecdn.com/a/4f/5fe8fb3eecf899132fdef9fc3f757jpeg.jpeg"
//                },
                {
                    "多张图片",
                        "https://enterprise.elemecdn.com/e/bc/610a395042e27398ee2c93298de4apng.png,https://enterprise.elemecdn.com/e/bc/610a395042e27398ee2c93298de4apng.png,https://enterprise.elemecdn.com/e/bc/610a395042e27398ee2c93298de4apng.png,https://enterprise.elemecdn.com/e/bc/610a395042e27398ee2c93298de4apng.png,https://enterprise.elemecdn.com/e/bc/610a395042e27398ee2c93298de4apng.png,https://enterprise.elemecdn.com/e/bc/610a395042e27398ee2c93298de4apng.png,https://enterprise.elemecdn.com/e/bc/610a395042e27398ee2c93298de4apng.png,https://enterprise.elemecdn.com/e/bc/610a395042e27398ee2c93298de4apng.png",
                        "https://enterprise.elemecdn.com/a/4f/5fe8fb3eecf899132fdef9fc3f757jpeg.jpeg,https://enterprise.elemecdn.com/8/db/38cbc6081facf58cd227577edd583jpeg.jpeg"
                }
        };
    }

    // 多线程并发测试-仅更新图片
    // threadPoolSize 线程数量
    // invocationCount 执行次数(每条测试数据执行次数)
    // timeOut 超时时间
    @Test(description = "饿了么报备接口并发测试", dataProvider = "eleme_invoice_update", threadPoolSize = 50, invocationCount =500 )
    public void testElemeOrderReportUpdate(String caseDesc, String invoice, String photograph) throws HttpStatusException {

        // uat
//        String orderNo = "1202034339765235799";
//        String orderNo98 = "88888888";
//        String bno = "b7572844-5216-40d6-addd-bc9d1f5a2412";
//        String uno = "18323454321";
        // stage
        String orderNo = "2137807627110181105888444";
        String orderNo98 = "9999999";
        String bno = "38f463ae-753b-4be5-9884-5b8cdac6fa6c";
        String uno = "13661982832";

        String mealNumber = "1";
        String mealReason = "eleme_invoice_update_test";
        String mealReceipt = "https://g.alicdn.com/aliyun/dms-front/0.0.49/js/share/images/dms_phone.png";
        String mealDistance = "88888888";
        String orderType = "1";

        // 请求DTO
        ElemeOrderReportRequest elemeOrderReportRequest = new ElemeOrderReportRequest(
                orderNo, orderNo98,
                new ElemeOrderReportRequest.ReportInfo(Integer.parseInt(mealNumber),
                        mealReason, mealReceipt, Long.parseLong(mealDistance), invoice, photograph),
                bno, uno, Integer.parseInt(orderType)
        );

        // 发送请求
        String res = vendorApi.elemeOrderReport(employee, elemeOrderReportRequest);
        log.info("res: {}", res);

        JsonObject resJson = new JsonParser().parse(res).getAsJsonObject();
        int res_code = resJson.get("code").getAsInt();
        // 断言
        assert res_code == 200;
    }
}

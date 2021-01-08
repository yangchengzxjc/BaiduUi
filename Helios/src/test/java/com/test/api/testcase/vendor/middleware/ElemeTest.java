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
    public void init(@Optional("14082978000") String phoneNumber, @Optional("hly123456") String pwd, @Optional("uat") String env) {
        employee = getEmployee(phoneNumber, pwd, env);
        vendorApi = new VendorApi();
    }

    @DataProvider(name = "eleme")
    public Object[][] dataProvider1() {
        return new Object[][]{
//                {orderNo, orderNo98, bno, uno, mealNumber, mealReason, mealReceipt, mealDistance}
                {"1202034339765235799", "781653488299932848135546", "123", "232731066730761835",
                        "1", "123123123", "http://uat.huilianyi.com/heliosimg1/a/a9da99b9526eca9f4581d8393034bc86BG.jpg", "123", "1"}
        };
    }

    @Test(dataProvider = "eleme")
    public void testElemeOrderReport(String orderNo, String orderNo98, String bno,
                                     String uno, String mealNumber, String mealReason,
                                     String mealReceipt, String mealDistance, String orderType) throws HttpStatusException {
        // 测试数据 转 DTO
        ElemeOrderReportRequest elemeOrderReportRequest = new ElemeOrderReportRequest();
        elemeOrderReportRequest.setOrderNo(orderNo);
        elemeOrderReportRequest.setOrderNo98(orderNo98);
        elemeOrderReportRequest.setUno(uno);
        elemeOrderReportRequest.setBno(bno);
        elemeOrderReportRequest.setOrderType(Integer.valueOf(orderType));
        ElemeOrderReportRequest.ReportInfo reportInfo = new ElemeOrderReportRequest.ReportInfo();
        reportInfo.setMealDistance(Long.valueOf(mealDistance));
        reportInfo.setMealNumber(Integer.parseInt(mealNumber));
        reportInfo.setMealReason(mealReason);
        reportInfo.setMealReceipt(mealReceipt);
        elemeOrderReportRequest.setReportInfo(reportInfo);

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
//                {invoice, photograph}
                {"http://uat.huilianyi.com/heliosimg1/a/a9da99b9526eca9f4581d8393034bc86BG.jpg",
                        "https://appmail.mail.10086.cn/m6/images/temp/139mail_weixin.png"}
        };
    }

    // threadPoolSize 线程数量
    // invocationCount 执行总次数
    // timeOut 超时时间
    @Test(dataProvider = "eleme_invoice_update", threadPoolSize = 70, invocationCount = 700)
    public void testElemeOrderReportUpdate(String invoice, String photograph) throws HttpStatusException {

        String orderNo = "1202034339765235799";
        String orderNo98 = "88888888";
        String bno = "b7572844-5216-40d6-addd-bc9d1f5a2412";
        String uno = "18323454321";
        String mealNumber = "1";
        String mealReason = "eleme_invoice_update_test";
        String mealReceipt = "https://g.alicdn.com/aliyun/dms-front/0.0.49/js/share/images/dms_phone.png";
        String mealDistance = "88888888";
        String orderType = "1";

        // 测试数据 转 DTO
        ElemeOrderReportRequest elemeOrderReportRequest = new ElemeOrderReportRequest();
        elemeOrderReportRequest.setOrderNo(orderNo);
        elemeOrderReportRequest.setOrderNo98(orderNo98);
        elemeOrderReportRequest.setUno(uno);
        elemeOrderReportRequest.setBno(bno);
        elemeOrderReportRequest.setOrderType(Integer.valueOf(orderType));
        ElemeOrderReportRequest.ReportInfo reportInfo = new ElemeOrderReportRequest.ReportInfo();
        reportInfo.setMealDistance(Long.valueOf(mealDistance));
        reportInfo.setMealNumber(Integer.parseInt(mealNumber));
        reportInfo.setMealReason(mealReason);
        reportInfo.setMealReceipt(mealReceipt);
        reportInfo.setInvoice(invoice);
        reportInfo.setPhotograph(photograph);
        elemeOrderReportRequest.setReportInfo(reportInfo);

        // 发送请求
        String res = vendorApi.elemeOrderReport(employee, elemeOrderReportRequest);
        log.info("res: {}", res);

        JsonObject resJson = new JsonParser().parse(res).getAsJsonObject();
        int res_code = resJson.get("code").getAsInt();
        // 断言
        assert res_code == 200;
    }
}

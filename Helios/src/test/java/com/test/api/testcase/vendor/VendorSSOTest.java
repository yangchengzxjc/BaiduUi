package com.test.api.testcase.vendor;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SSOBody;
import com.test.BaseTest;
import com.test.api.method.InfraStructure;
import com.test.api.method.Vendor;
import com.test.api.method.VendorMethod.FlightOrder;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * @Author peng.zhang
 * @Date 2020/10/27
 * @Version 1.0
 **/
@Slf4j
public class VendorSSOTest extends BaseTest {


    private Employee employee;
    private Vendor vendor;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("yaliang.wang@cimc.com") String phoneNumber, @Optional("111111") String pwd, @Optional("stage") String env){
        employee =getEmployee(phoneNumber,pwd,env);
        vendor =new Vendor();
    }
    @Test(description = "sso单点登录")
    public void ssoTest1() throws HttpStatusException {
        SSOBody ssoBody =SSOBody.builder()
                .tenantId(employee.getTenantId())
                .employeeId(employee.getEmployeeID())
                .deviceType("web")
                .initPage("HotelSearch")
                .orderNumber("")
                .build();
        JsonObject response = vendor.ssoLogin(employee,ssoBody,"cimccTMC","200428140254184788","");
        log.info("单点登录的响应:{}",response);
    }

    @Test(description = "web sso 预订入口")
    public void ssoTest2(String res) throws HttpStatusException {
        // request param
        String roleType = "TENANT";
        String supplierOID = "66666666-6666-11e6-9639-00ffa3fb4c67";
        String realmId = "639514855899537001";
        String companyOID = "11234";
        String emnum = "ca6af54d-f28d-48fe-9b07-5a75bde983c0";
        String direction = "WEB";
        String pageType = "";
        // expect response
        String res_expect = "[{\"venName\":\"机票\",\"venIco\":\"http://hly-static.oss-cn-shanghai.aliyuncs.com/%E8%88%AA%E7%A9%BA.svg\",\"venItems\":[{\"vendorTitle\":\"air\",\"vendorCode\":\"BAOKU_AIR\",\"vendorIco\":\"https://cloudhelios-static.oss-cn-shanghai.aliyuncs.com/vendor-logo/Baoku_Air_New.png\",\"vendorName\":\"中青旅机票\",\"loginUrl\":\"/vendor-info-service/api/sso/common?supplierOID=91502799-22db-4f31-bae6-b7813db5d159&direction=web&pageType=1002&vendorType=2001\"},{\"vendorTitle\":\"air\",\"vendorCode\":\"ZHENXUAN_AIR\",\"vendorIco\":\"https://helioscloud-uat-static.oss-cn-shanghai.aliyuncs.com/other/01e5aba9-b59d-46b2-af97-367b950cb355-1543903881715.png\",\"vendorName\":\"甄选机票\",\"loginUrl\":\"/vendor-info-service/api/sso/common?supplierOID=90fc8496-1c60-4674-88d7-8c2153eeebc8&direction=web&pageType=1002&vendorType=2001\"},{\"vendorTitle\":\"air\",\"vendorCode\":\"CTRIP_AIR\",\"vendorIco\":\"http://cloudhelios-static.oss-cn-shanghai.aliyuncs.com/vendor-logo/Ctrip_Air_New.png\",\"vendorName\":\"携程机票\",\"loginUrl\":\"/vendor-info-service/api/sso/common?supplierOID=50b78b72-ed9d-4392-b7a1-df3b79f4cb17&direction=web&pageType=1002&vendorType=2001\"},{\"vendorTitle\":\"air\",\"vendorCode\":\"MEIYA_FLIGHT\",\"vendorIco\":\"https://cloudhelios-static.oss-cn-shanghai.aliyuncs.com/vendor-logo/NEW_MEYA_FLIGHT.png\",\"vendorName\":\"美亚机票\",\"loginUrl\":\"/vendor-info-service/api/sso/common?supplierOID=0d6d1a69-62f5-11e7-a0e0-00163e000c55&direction=web&pageType=1002&vendorType=2001\"},{\"vendorTitle\":null,\"vendorCode\":\"cimccTMC\",\"vendorIco\":\"https://helioscloud-uat-static.oss-cn-shanghai.aliyuncs.com/other/78ed95b3-09ed-4298-9968-d538c85016d0-1590716534942.png\",\"vendorName\":\"中集商旅机票\",\"loginUrl\":\"/vendor-info-service/api/sso/common?supplierOID=8afc4c9e-a7ea-4de6-ab60-70669a5b91e8&direction=web&pageType=1002&vendorType=2001\"}]},{\"venName\":\"酒店\",\"venIco\":\"http://hly-static.oss-cn-shanghai.aliyuncs.com/%E9%85%92%E5%BA%97.svg\",\"venItems\":[{\"vendorTitle\":\"hotel\",\"vendorCode\":\"BAOKU_HOTEL\",\"vendorIco\":\"https://cloudhelios-static.oss-cn-shanghai.aliyuncs.com/vendor-logo/Baoku_Hotel_New.png.png\",\"vendorName\":\"蓝标优选酒店\",\"loginUrl\":\"/vendor-info-service/api/sso/common?supplierOID=87ea0b85-5988-4007-b443-a334d7aacb2f&direction=web&pageType=1002&vendorType=2003\"},{\"vendorTitle\":\"hotel\",\"vendorCode\":null,\"vendorIco\":\"http://cloudhelios-static.oss-cn-shanghai.aliyuncs.com/vendor-logo/HRS_Open_Logo.png\",\"vendorName\":\"HRS\",\"loginUrl\":\"/vendor-info-service/api/sso/common?supplierOID=9a70376b-94bd-42a2-882e-3bb34138f2d8&direction=web&pageType=1002&vendorType=2003\"},{\"vendorTitle\":\"hotel\",\"vendorCode\":\"ZHENXUAN\",\"vendorIco\":\"https://helioscloud-uat-static.oss-cn-shanghai.aliyuncs.com/other/b7cf28c2-cbf7-4765-a50a-a2b938df1c6e-1543903843626.png\",\"vendorName\":\"甄选酒店\",\"loginUrl\":\"/vendor-info-service/api/sso/common?supplierOID=f4cd142f-2057-11e8-b507-00163e1c8bb1&direction=web&pageType=1002&vendorType=2003\"},{\"vendorTitle\":\"hotel\",\"vendorCode\":\"CTRIP_HOTEL\",\"vendorIco\":\"https://cloudhelios-static.oss-cn-shanghai.aliyuncs.com/vendor-logo/Ctrip_Hotel_New.png\",\"vendorName\":\"携程酒店\",\"loginUrl\":\"/vendor-info-service/api/sso/common?supplierOID=74da7eb8-e5d5-4c2f-b84f-6cead8436fa3&direction=web&pageType=1002&vendorType=2003\"},{\"vendorTitle\":\"hotel\",\"vendorCode\":\"MEIYA_HOTEL\",\"vendorIco\":\"https://cloudhelios-static.oss-cn-shanghai.aliyuncs.com/vendor-logo/Meiya_Hotel_New.png\",\"vendorName\":\"美亚酒店\",\"loginUrl\":\"/vendor-info-service/api/sso/common?supplierOID=d73880c8-172f-4379-8682-4f93f279e5ed&direction=web&pageType=1002&vendorType=2003\"},{\"vendorTitle\":null,\"vendorCode\":\"cimccTMC\",\"vendorIco\":\"https://helioscloud-uat-static.oss-cn-shanghai.aliyuncs.com/other/78ed95b3-09ed-4298-9968-d538c85016d0-1590716534942.png\",\"vendorName\":\"中集商旅酒店\",\"loginUrl\":\"/vendor-info-service/api/sso/common?supplierOID=8afc4c9e-a7ea-4de6-ab60-70669a5b91e8&direction=web&pageType=1002&vendorType=2003\"}]},{\"venName\":\"火车\",\"venIco\":\"http://hly-static.oss-cn-shanghai.aliyuncs.com/%E7%81%AB%E8%BD%A6.svg\",\"venItems\":[{\"vendorTitle\":\"train\",\"vendorCode\":\"CTRIP_TRAIN\",\"vendorIco\":\"https://cloudhelios-static.oss-cn-shanghai.aliyuncs.com/vendor-logo/Ctrip_Train_New.png\",\"vendorName\":\"携程火车\",\"loginUrl\":\"/vendor-info-service/api/sso/common?supplierOID=c9677c97-cc48-4c2e-b988-4c0986faf1b5&direction=web&pageType=1002&vendorType=2002\"},{\"vendorTitle\":null,\"vendorCode\":\"cimccTMC\",\"vendorIco\":\"https://helioscloud-uat-static.oss-cn-shanghai.aliyuncs.com/other/78ed95b3-09ed-4298-9968-d538c85016d0-1590716534942.png\",\"vendorName\":\"中集商旅火车\",\"loginUrl\":\"/vendor-info-service/api/sso/common?supplierOID=8afc4c9e-a7ea-4de6-ab60-70669a5b91e8&direction=web&pageType=1002&vendorType=2002\"}]},{\"venName\":\" 用车\",\"venIco\":\"https://helioscloud-uat-static.oss-cn-shanghai.aliyuncs.com/b7572844-5216-40d6-addd-bc9d1f5a2412/other/ed064b9a-ab7f-4d00-b658-e6b3d04dfb7a-1564553072222.svg\",\"venItems\":[]}]";
        // do request
        String res = vendor.vendorInfoSso(employee, roleType, supplierOID, realmId, companyOID, emnum, pageType, direction);
        // assert result
        Assert.assertEquals(res, res_expect);
    }
}

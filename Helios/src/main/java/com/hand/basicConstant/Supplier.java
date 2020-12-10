package com.hand.basicConstant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author peng.zhang
 * @Date 2020/8/5
 * @Version 1.0
 **/

@AllArgsConstructor
@Getter
public enum Supplier {

//    CTRIPAIR("CTRIP_AIR","ctrip_flight","50b78b72-ed9d-4392-b7a1-df3b79f4cb17",""),
//    CTRIPHOTEL("CTRIP_HOTEL","ctrip_hotel","74da7eb8-e5d5-4c2f-b84f-6cead8436fa3",""),
//    CTRIPTRAIN("CTRIP_TRAIN","ctrip_train","c9677c97-cc48-4c2e-b988-4c0986faf1b5",""),
//    MEIYAAIR("MEIYA_AIR","meiya","0d6d1a69-62f5-11e7-a0e0-00163e000c55",""),
//    MEIYAHOTEL("MEIYA_HOTEL","meiya_hotel","d73880c8-172f-4379-8682-4f93f279e5ed",""),
//    MEIYATRAIN("MEIYA_TRAIN","meiya_train","7864964d-f57b-4528-a371-babfe6f64d86",""),
//    MEIYAINT("MEIYA_INT","meiya_inter_air","564c9461-cbbc-4bb7-93e1-c5d561575ec6",""),
//    DTTRIP("","","9c7207b5-7807-42b0-86f9-3db6ccb723f6","2001");


    ALL("获取所有登录URL", "all", "", "66666666-6666-11e6-9639-00ffa3fb4c67", "", ""),
    // 非 openApi
//    CTRIP_TRAIN("携程", "ctrip", "CTRIP_TRAIN", "213691b5-75a4-11e7-af18-00163e00373d", "2001",""),
//    CTRIP_AIR("携程", "ctrip", "CTRIP_AIR", "fbf77233-766f-11e6-9639-00ffa3fb4c67", "2002"),
    CTRIP_AIR("携程", "ctrip_flight", "CTRIP_AIR", "50b78b72-ed9d-4392-b7a1-df3b79f4cb17", "2002", ""),
    //    CTRIP_HOTEL("携程", "ctrip", "CTRIP_HOTEL", "d9771362-5359-11e7-ace0-7cd30ad300be", "2003"),
    CTRIP_HOTEL("携程", "ctrip_hotel", "CTRIP_HOTEL", "74da7eb8-e5d5-4c2f-b84f-6cead8436fa3", "2001", ""),
    CTRIP_TRAIN("携程", "ctrip_train", "CTRIP_TRAIN", "c9677c97-cc48-4c2e-b988-4c0986faf1b5", "2003", ""),
    MEIYA_FLIGHT("美亚", "meiya", "MEIYA_FLIGHT", "0d6d1a69-62f5-11e7-a0e0-00163e000c55", "2002", ""),
    MEIYA_HOTEL("美亚酒店", "meiya_hotel", "MEIYA_HOTEL", "d73880c8-172f-4379-8682-4f93f279e5ed", "2001", ""),
    MEIYA_TRAIN("美亚火车", "meiya_train", "MEIYA_TRAIN", "7864964d-f57b-4528-a371-babfe6f64d86", "2003", ""),
    MEIYA_INTER_AIR("美亚国际", "meiya_inter_air", "", "564c9461-cbbc-4bb7-93e1-c5d561575ec6", "2002", ""),
    BAOKU_AIR("宝库", "baoku", "BAOKU_AIR", "fbfc7b74-766f-11e6-9639-00ffa3fb4c67", "2001", ""),
    BAOKU_HOTEL("宝库", "baoku", "BAOKU_HOTEL", "2eb865bf-981a-43c7-b2e5-e1426c769573", "2001", ""),
    CTSHO_AIR("中旅", "ctsho", "CTSHO_AIR", "f41c3bcc-0869-11e7-ad4a-00163e000c55", "", ""),
    FASCO("供应商Fasco", "fasco", "FASCO", "fd5e7b53-a86e-439f-af25-6881907fd8d0", "", ""),
    HRS("供应商HRS", "hrs", "HRS", "ddb76c1e-b2f3-4388-b7c3-e0b3464a16d7", "2001", ""),
    YGET("供应商油卡话费", "yget", "YGET", "a971408e-0ebf-4a56-adbd-1e5627ee2f52", "2000", ""),
    HUAZHU("供应商华住", "huazhu", "", "67fa4c98-c377-4a5d-8b57-55698f8f3d39", "", ""),
//            ("供应商百动","bestdo,BAIDONG,cbddced1-3064-40ad-94e2-b290d5097b10,2000"),
//            ("小秘书,xms,null,fcaf022e-fc0e-4c33-a74c-5ff1e258f45b,0"),
//            ("供应商订单","order,null,951a2ccf-0535-4446-ac82-f58298ca5035,0"),
//            ("供应商滴滴","didi,null,d3c3d06f-9d10-4d82-9b4a-d5cc8a46e47f,0"),
//            ("玫琳凯,marykay,null,3bfc86ec-adc4-46b5-a9a4-bd4eeed44501,0"),
//            ("京东","jd,JINGDONG,fa44e1b4-3d4e-47c0-9456-428f513ca42a,2000"),
//            ("宝库","baoku_flight,"BAOKU_AIR,91502799-22db-4f31-bae6-b7813db5d159,2002"),
//            ("宝库","baoku_hotel,BAOKU_HOTEL,87ea0b85-5988-4007-b443-a334d7aacb2f,2001"),
//            ("汇联易优选机票","helios_flight,null,658fd305-c295-11e7-a926-00163e1c8bb1,2002"),
//            ("汇联易优选酒店","helios_hotel,null,93ce0cd8-3e6a-450e-ab7d-272ebf2f5322,2001"),
//            ("饿了么,eleme,ELEME,d82e4152-a3ce-4dfd-8265-1573f46d3d30,2000"),
//            ("神舟专车","zuche,null,93d2a555-3ee5-4351-b6e1-ceba82821ee9,0"),
//            ("甄选酒店","zhenhui_hotel,ZHENXUAN,f4cd142f-2057-11e8-b507-00163e1c8bb1,2003"),
//            ("泛嘉机票","fanjia_flight,FANJIA_FLIGHT,fd1aa055-ca9d-492d-806a-114f3629d185,2002"),
//            ("泛嘉酒店","fanjia_hotel,FANJIA_HOTEL,04ebb13d-3ddd-489b-8e7f-3669e233db60,2001"),
//            ("泛嘉火车","fanjia_train,FANJIA_TRAIN,594ea4fe-21e0-4888-b673-1e1cf1661ec8,2003"),
//            ("美团","meituan,null,c804287e-4101-40d5-9706-6130d47e50ce,2000"),
//            ("点评","dianping,null,7231515c-f991-4a1f-a320-c0f186c17cbf,2000"),
//            ("7space,space,null,367a6911-599c-48a4-bc54-9c03c25593f6,2000"),
//            ("神舟专车","zuche,null,93d2a555-3ee5-4351-b6e1-ceba82821ee9,0"),
//            ("甄选机票","zhenxuan_flight,ZHENXUAN_AIR,90fc8496-1c60-4674-88d7-8c2153eeebc8,0"),
//            ("饿了么企业版,ele_com,null,70548a2e-b7bc-4514-9d67-a67a18a94c9e,0"),
//            ("泛嘉审批","fanjia_approval,null,bc870bf9-27da-4594-b2a1-a459bd13c48b,0"),
//            ("优行机票","ubtrip_flight,UBTRIP_AIR,e486b476-6cfc-48e6-848c-f7ba70f655ae,2002"),
//            ("差旅一号机票","travelnoOne_flight,TRAVEL_FLIGHT,e80122ef-4df6-4b7d-b606-bf724e50da1b,2002"),
//            ("差旅一号酒店","travelnoOne_hotel,TRAVEL_HOTEL,e425a187-d6a4-459c-9ca3-ab40568b04e4,2001"),
//            ("FCM机票,fcm_flight,FCM_AIR,778d25c6-ca51-4986-ba58-70c8ed3429b7,2002"),
//            ("空港嘉华机票,kgjh,KGJH_AIR,22b7ed44-f31b-436e-af84-360db6d544c8,2001"),
//            ("Fasco,fasco_inter,FASCO_INTER,bc0f6d9a-7f27-402e-8bca-f294f6706ddf,2002"),
//            ("携程用车","ctrip_car,CTRIP_CAR,ad965d46-51dc-46b0-9882-89d614c56d2f,2002"),
//            ("国旅机票","cits_air,CITS_AIR,c4541fa5-18d2-4eac-9746-1459bfad5cc8,2002"),
//            ("八商山酒店,travelpay8_hotel,null,9a70376b-94bd-42a2-882e-3bb34138f2d8,2001"),
//            ("BCD,bcd_air,BCD_AIR,35ebfce8-9e52-466d-ac74-4dbe5c692335,2002"),
//            ("TicketingPlatform,ticketing_platform,TICKETINGPLATFORM,179a170d-72d9-472b-b38c-efa3d01b5b42,2002"),
//            ("携程授权","ctrip_accredit,null,689b5a8a-cfb0-4a61-a4c6-f7969f016aaa,0"),
//            ("美团打车","meituan_car,MEITUAN_CAR,e506bb66-d0cb-49d3-8c05-1c8477ebf4e8,2000"),
//            ("程腾机票","chttravel_flight,CHTTRAVEL_AIR,c7e261c6-045c-40e0-a811-63e9e1531318,2001"),
//            ("程腾酒店","chttravel_hotel,CHTTRAVEL_HOTEL,e3e36018-eec7-456f-aece-46e278f0f911,2003"),
//            ("事后审批","afterwardsApproval,null,80e419e8-7441-44d4-9a11-e4aec9a90fab,2000"),
//            ("同程机票","tongcheng_air,TY_AIR,ce68040e-d4bc-4e1a-8cde-79b2ef672c26,2002"),
//            ("同程酒店","tongcheng_hotel,TY_HOTEL,a2630b84-175b-4cab-9305-cbdb89ddc443,2001"),
//            ("在途首页","z_trip_hotel,ZTRIP_HOTEL,3702c226-6963-4bfc-8101-2264ee535318,null"),
//            ("在途首页","z_trip_flight,ZTRIP_FLIGHT,ade5d27b-109c-4320-919f-840f98e2cbef,null"),
//            ("美亚首页","meiya_home,null,06153164-a86d-4450-bfc3-c0a81d867d10,null"),
//            ("钉钉","dingding,null,50d33ad6-2082-4635-b0a7-8ca9e848d384,0"),
//            ("中集","cimcc,null,8afc4c9e-a7ea-4de6-ab60-70669a5b91e8,0"),
//            ("行旅国际","auvgo,auvgo,da78cc68-c02c-11ea-b507-00163e1c8bb1,2001"),
//            ("中航易购","yeeGo,yeeGoAir,f424f03d-a0b0-11ea-b507-00163e1c8bb1,2001"),
//            ("滴滴企业版H5,didiH5,didiH5,780c0637-c4bd-11ea-b507-00163e1c8bb1,2001"),
//            ("美团商企通首页,meituan_home,MEITUAN_HOME,2d5ea68f-0eeb-4a2d-8e71-532303ffd1a8,2000"),
//            ("优行酒店,ubtrip_hotel,UBTRIP_HOTEL,53cf97cc-b049-4607-8ef6-3bfb7bf0f728,2003"),
//            ("一嗨,ehi_car,EHI_CAR,26d7abc0-82c9-49ed-8408-1e9924165d9c,2004"),
//            ("同程火车,tongcheng_train,TY_TRAIN,ed552797-7ac7-48fb-8e5f-9bbd3dc02924,2002"),
//            ("国旅运通新,amex,vendor.Amex,2537d9a8-7100-42a4-9d76-7bea686114b2,2001"),
//            ("差旅一号国际机票,travelnoOne_flight,TRAVEL_FLIGHT,b40dfb55-ac1b-4cdf-86b4-96cdf86aee55,2001"),
//            ("srm,srm,SRM,9dfbb2ac-c04d-498a-b652-a2314a9ba9a2,2001"),
//            ("差旅壹号火车","travelnoOne_train,TRAVEL_TRAIN,177fb6ea-0aa1-4637-89b6-37fe41d83dda,2002"),
//            ("差旅壹号用车","travelnoOne_car,TRAVEL_CAR,cbbe0f7a-a51d-49e5-bd56-681af56aa84e,2004"),
//            ("FCM酒店,fcm_hotel,FCM_HOTEL,c13a1d10-a1ba-4fb3-ada3-51cb0219a49e,2003"),

    // openApi
    cimcctmcAir("cimcctmc", "cimccTMC", "supplyCimccTMCService", "8afc4c9e-a7ea-4de6-ab60-70669a5b91e8", "2001", ""),
    cimcctmcTrain("cimcctmc", "cimccTMC", "supplyCimccTMCService", "8afc4c9e-a7ea-4de6-ab60-70669a5b91e8", "2003", ""),
    cimcctmcHotel("cimcctmc", "cimccTMC", "supplyCimccTMCService", "8afc4c9e-a7ea-4de6-ab60-70669a5b91e8", "2002", ""),
    dttrip("大唐", "dttrip", "supplyDTTripTmcService", "9c7207b5-7807-42b0-86f9-3db6ccb723f6", "2001", ""),
    shenzhenAir("深圳航空", "shenzhenAir", "supplyShenZhenAirTmcService", "810f87c4-f59b-4718-948e-381460715390", "2001", ""),
    onTheWayTMCAir("在路上商旅", "onTheWayTMC", "supplyOnTheWayTMCService", "31e68258-ffb1-4fde-afc2-b88cf9533632", "2001", ""),
    onTheWayTMCTrain("在路上商旅", "onTheWayTMC", "supplyOnTheWayTMCService", "31e68258-ffb1-4fde-afc2-b88cf9533632", "2003", ""),
    onTheWayTMCHotel("在路上商旅", "onTheWayTMC", "supplyOnTheWayTMCService", "31e68258-ffb1-4fde-afc2-b88cf9533632", "2002", ""),
    tehang("特航", "tehang", "supplyTeHangTMCService", "3385750c-733a-47ec-be12-49419e7e3f19", "2001", ""),
    fyair("丰谊商旅", "fyair", "supplyFyAirTMCService", "8d3b7d9e-d0ad-4fed-89c6-2563bd67ff36", "2002", ""),
    ehi("一嗨", "ehi", "supplyEHiTMCService", "26d7abc0-82c9-49ed-8408-1e9924165d9c", "2004", ""),
    amex("国旅运通", "amex", "supplyCitsAirService", "f91390b5-a467-48d2-910d-0f9fa7354e43", "2001", ""),
    auvgo("行旅", "auvgo", "supplyAuvgoTMCService", "f71b9532-ea8a-46ba-83ab-39919fe854a1", "2002", ""),
    neitAir("光大商旅", "neit", "supplyNEITService", "b62b10c8-b92b-4f8a-8d42-ed1620fe556c", "2001", ""),
    neitHotel("光大商旅", "neit", "supplyNEITService", "b62b10c8-b92b-4f8a-8d42-ed1620fe556c", "2003", ""),
    xrtmc("金翔达商旅", "xrtmc", "supplyXRTMCService", "1e9e137a-d5ce-4925-a882-43dde36269cd", "2001", ""),
    zhenxuanHotel("甄选机票", "zhenxuanHotel", "cloudheliosHotelService", "67f89eae-13ad-4485-a71a-d8a37137fe3c", "2003", ""),
    ctripAir("携程商旅", "ctrip", "supplyCtripService", "067fe549-b075-43a6-8dc4-b2a8851cb7a3", "2001", ""),
    ctripTrain("携程商旅", "ctrip", "supplyCtripService", "067fe549-b075-43a6-8dc4-b2a8851cb7a3", "2003", ""),
    ctripHotel("携程商旅", "ctrip", "supplyCtripService", "067fe549-b075-43a6-8dc4-b2a8851cb7a3", "2002", ""),
    zhenxuanAirlines("甄选机票", "zhenxuanAirlines", "cloudheliosFlightService", "9dbb6bed-e3c7-4353-bee2-f0dfc69d6317", "2001", "");


    private String name;
    private String vendorsName;
    private String appCode;
    private String supplierOID;
    private String vendorType;
    //    private String category;
    private String expectUrl;

    /**
     * 匹配supplierOID
     * @param venName
     * @return
     */
    public static String getSupplier(String venName){
        Supplier[] suppliers = values();
        for (Supplier supplier: suppliers){
            if(supplier.getName().equals(venName)){
                return supplier.getSupplierOID();
            }
        }
        return null;
    }

    public static Supplier getInfoSso(String vendorName) {
        for (Supplier info : Supplier.values()) {
            if (info.vendorsName.equalsIgnoreCase(vendorName)) {
                return info;
            }
        }
        return CTRIP_AIR;
    }

}

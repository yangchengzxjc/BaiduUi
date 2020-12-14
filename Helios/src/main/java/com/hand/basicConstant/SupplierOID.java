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
public enum  SupplierOID {

    CTRIPAIR("CTRIP_AIR","ctrip_flight","50b78b72-ed9d-4392-b7a1-df3b79f4cb17",""),
    CTRIPHOTEL("CTRIP_HOTEL","ctrip_hotel","74da7eb8-e5d5-4c2f-b84f-6cead8436fa3",""),
    CTRIPTRAIN("CTRIP_TRAIN","ctrip_train","c9677c97-cc48-4c2e-b988-4c0986faf1b5",""),
    MEIYAAIR("MEIYA_AIR","meiya","0d6d1a69-62f5-11e7-a0e0-00163e000c55",""),
    MEIYAHOTEL("MEIYA_HOTEL","meiya_hotel","d73880c8-172f-4379-8682-4f93f279e5ed",""),
    MEIYATRAIN("MEIYA_TRAIN","meiya_train","7864964d-f57b-4528-a371-babfe6f64d86",""),
    MEIYAINT("MEIYA_INT","meiya_inter_air","564c9461-cbbc-4bb7-93e1-c5d561575ec6",""),
    DTTRIP("","","9c7207b5-7807-42b0-86f9-3db6ccb723f6","2001"),
    ZHENXUANHOTEL("ZHENXUAN_HOTEL","zhenhui_hotel","f4cd142f-2057-11e8-b507-00163e1c8bb1",""),
    ZHENXUANFLIGHT("ZHENXUAN_FLIGHT","zhenxuan_flight","90fc8496-1c60-4674-88d7-8c2153eeebc8","");

    private String name;
    private String vendorsName;
    private String supplierOID;
    private String vendorType;

}

package com.hand.basicconstant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TmcChannel {

    DT("DT","supplyDTTripTmcService","9c7207b5-7807-42b0-86f9-3db6ccb723f6","","","","",""),
    CIMCC("CIMCC","supplyCimccTMCService","8afc4c9e-a7ea-4de6-ab60-70669a5b91e8","cimccTMC","中集商旅","cimccTMC","200428140254184788","0ab0ca2870a369b96132ebf0b11c290b"),
    FY("FY","supplyFyAirTMCService","","","","","",""),
    CTRIP("Ctrip","supplyCtripService","","","","","",""),
    ZLS("ZLS","supplyOnTheWayTMCService","","","","","",""),
    AMEX("amex","","","amex","","amex","200428140254184795","3055d656f9feed61f69b368787762d05");


    private String name;
    private String tmcChannel;
    private String supplierOID;
    private String supplierCode;
    private String supplierName;
    private String appName;
    private String corpId;
    private String signiture;


    public static TmcChannel getTmcInfo(String tmc) {
        for (TmcChannel tmcInfo : TmcChannel.values()) {
            if (tmcInfo.getName().equalsIgnoreCase(tmc)) {
                return tmcInfo;
            }
        }
        return DT;
    }

}

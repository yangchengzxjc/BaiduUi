package com.hand.basicconstant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TmcChannel {
    DT("DT","supplyDTTripTmcService","9c7207b5-7807-42b0-86f9-3db6ccb723f6"),
    CIMCC("CIMCC","supplyCimccTMCService","8afc4c9e-a7ea-4de6-ab60-70669a5b91e8"),
    FY("FY","supplyFyAirTMCService",""),
    CTRIP("Ctrip","supplyCtripService",""),
    ZLS("ZLS","supplyOnTheWayTMCService","");



    private String name;
    private String tmcChannel;
    private String supplierOID;



    public static TmcChannel getTmcInfo(String tmc) {
        for (TmcChannel tmcInfo : TmcChannel.values()) {
            if (tmcInfo.getName().equalsIgnoreCase(tmc)) {
                return tmcInfo;
            }
        }
        return DT;
    }

}

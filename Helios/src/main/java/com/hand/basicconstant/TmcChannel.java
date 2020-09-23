package com.hand.basicconstant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TmcChannel {
    DT("DT","supplyDTTripTmcService"),
    ZJ("ZJ","supplyCimccTMCService"),
    FY("FY","supplyFyAirTMCService"),
    ZLS("ZLS","supplyOnTheWayTMCService");


    private String name;
    private String value;

}

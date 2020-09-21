package com.hand.basicObject.supplierObject.syncApproval.syncPlatformEntity;

import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/18
 * @Version 1.0
 **/
public class SyncEntity {
    //默认为1  审批状态 0为无效
    private Integer status;
    //租户Id
    private String tenantId;
    private String companyId;
    //失效日期
    private String expiredDate;
    //预定人
    private BookClerk bookClerk;
    private List<Participant> participantList;
    private List<TravelFlightItinerary> travelFlightsList;
    private List<TravelHotelItinerary> travelHotelsList;
    private List<TravelTrainItinerary> travelTrainsList;
    private List<TravelCarItinerary> travelCarsList;
    private String businessCode;
    private String approvalCode;
    private String costCenter1;
    private String costCenter2;
    private String costCenter3;
    private String costCenter4;
    private String costCenter5;
    private String costCenter6;

}

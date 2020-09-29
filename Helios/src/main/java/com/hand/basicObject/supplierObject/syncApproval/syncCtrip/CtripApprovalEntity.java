package com.hand.basicObject.supplierObject.syncApproval.syncCtrip;

import lombok.Data;

import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/17
 * @Version 1.0
 **/
@Data
public class CtripApprovalEntity {
    //status
    private Integer Status;
    //审批后的行程单号
    private String ApprovalNumber;
    //员工工号
    private String EmployeeID;
    //飞机行程
    private List<FlightEndorsementDetail> FlightEndorsementDetails;
    //火车行程
    private List<TrainEndorsementDetail> TrainEndorsementDetails;
    //酒店行程
    private List<HotelEndorsementDetail> HotelEndorsementDetails;
    //扩展字段列表
    private List<com.hand.basicObject.supplierObject.syncApproval.syncCtrip.ExtendFieldList> ExtendFieldList;
}

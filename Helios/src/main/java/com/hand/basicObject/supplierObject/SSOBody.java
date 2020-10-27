package com.hand.basicObject.supplierObject;

import lombok.Builder;

/**
 * @Author peng.zhang
 * @Date 2020/10/27
 * @Version 1.0
 **/
@Builder
public class SSOBody {
    private String tenantId;

    private String employeeId;

    private String deviceType;

    private String initPage;

    private String orderNumber;

    private String language;

    private String businessCode;

    private String approvalCode;

    private String startCity;

    private String endCity;

    private String startDate;

    private String endDate;

    private String forCorp;


}

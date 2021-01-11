package com.hand.basicObject.Rule.receiptConfig;

import com.hand.utils.UTCTime;
import lombok.Data;

/**
 * @Author peng.zhang
 * @Date 2021/1/11
 * @Version 1.0
 **/

@Data
public class ReceiptOverTime {
    //逾期提醒设置 逾期天数提醒
    private Integer expiryRemindDay = 1;
    //静态逻辑校验
    private boolean staticCalibrationFlag = true;

    private Integer expiryDay = 5;
    //动态 校验规则
    private String dynamicRuleFailureTime = UTCTime.beijingDay(-1);

    private boolean enabled = true;
    // 是否开启强管控 默认为false
    private String forceEnabled = "false";
    // 规则生效日期
    private String effectiveTime = UTCTime.beijingDay(0);

    private String companyOid;

    private String description= "逾期规则";

    private String effectiveMonth = effectiveTime.split("-")[0];

    private String effectiveDay = effectiveTime.split("-")[1];

    private String dynamicRuleFailureMonth = dynamicRuleFailureTime.split("-")[0];

    private String dynamicRuleFailureDay = dynamicRuleFailureTime.split("-")[1];
}

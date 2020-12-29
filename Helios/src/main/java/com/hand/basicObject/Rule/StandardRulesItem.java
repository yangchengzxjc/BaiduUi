package com.hand.basicObject.Rule;

import com.google.gson.JsonArray;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author peng.zhang
 * @Date 2020/12/1
 * @Version 1.0
 **/
@Data
public class StandardRulesItem {

    /**
     * 城市关联类型：0不包含，1包含，默认1
     */
    private Integer cityAssociateType = 1;

    /**
     * 人员关联类型：0不包含，1包含，默认1
     */
    private Integer userAssociateType = 1;

    /**
     * 币种默认为人民币
     */
    private String currencyCode = "CNY";

    /**
     * 规则oid
     */
    private String ruleOID;

    /**
     * 标准oid
     */
    private String standardOID;

    /**
     * 返回空给前端时表示通用
     */
    private JsonArray userGroups;

    /**
     * 返回空给前端时表示通用
     */
    private JsonArray citys;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 
     */
    private JsonArray customEnumerationItems;
}

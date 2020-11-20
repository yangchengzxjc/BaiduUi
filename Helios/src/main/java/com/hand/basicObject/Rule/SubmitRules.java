package com.hand.basicObject.Rule;

import com.google.gson.JsonArray;
import lombok.Data;

/**
 * @Author peng.zhang
 * @Date 2020/11/17
 * @Version 1.0
 **/
@Data
public class SubmitRules {

    private boolean status = true;
    private String name;
    private JsonArray forms;
    // "FORBID" 表示禁止
    private String controlLevel = "WARN";
    private String message = "该笔报销不符合报销单提交规则";
    //默认1002为报销单
    private Integer businessType =1002;
    // SET_OF_BOOK 或者 公司级 COMPANY
    private String levelCode = "SET_OF_BOOK";
    //账套id/或者公司的id
    private String levelOrgId;
    //账套名称或者公司的名称
    private String levelOrgName = "默认账套";
    private String companyOID;
    //如果是账套级的配置在配置指定公司有值
    // 例如：{
    //            "name":"甄滙_上海通用公司",
    //            "id":"10701"
    //        }
    private JsonArray companys = new JsonArray();
}

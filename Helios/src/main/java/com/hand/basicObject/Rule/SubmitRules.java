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
    private String controlLevel;
    private String message;
    private Integer businessType =1002;
    private String levelCode;
    //账套id
    private String levelOrgId;
    //账套名称
    private String levelOrgName;
    private JsonArray companys = new JsonArray();
}

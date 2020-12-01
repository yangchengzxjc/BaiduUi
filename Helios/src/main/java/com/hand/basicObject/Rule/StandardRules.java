package com.hand.basicObject.Rule;

import com.google.gson.JsonArray;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author peng.zhang
 * @Date 2020/11/26
 * @Version 1.0
 **/
@Data
public class StandardRules {

    private boolean status = true;
    //人员组默认为所有
    private JsonArray userGroups= new JsonArray();
    //管控模式
    private String controlModeType = "PERIOD";
    //
    private String name;
    //适用的费用类型
    private JsonArray expenseTypes;
    //表单
    private JsonArray forms;
    //控制方式
    private String controlType;
    private String crossCompanyStandard = "OWNER";
    private String controlLevel = "WARN";
    private String complianceCheck;
    private String message = "费用超标了";
    private String type = "AMOUNT";
    private String nonAmountCtrlItem =null;
    private boolean participantsEnable = false;
    private Integer businessType = 1001;
    private String levelCode = "SET_OF_BOOK";
    private String levelOrgId;
    private JsonArray companys;

}

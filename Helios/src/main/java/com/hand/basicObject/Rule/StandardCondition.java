package com.hand.basicObject.Rule;

import lombok.Data;

/**
 * @Author peng.zhang
 * @Date 2020/12/2
 * @Version 1.0
 **/
@Data
public class StandardCondition {

    private String rightFieldCode;
    // 1001 ->"+"   1004->"*"
    private Integer operatorId;
    //private OperatorEnum operator;
    //自定义正数
    //单条合并后，用户可以手输，允许手输非数字，但是后端给友好提示
    private String positiveNumber;
}

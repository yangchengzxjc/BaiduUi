package com.hand.basicObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author peng.zhang
 * @Date 2020/7/16
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelSubsidyConfig {

    //申请单差补管控  警告->1001   不可提交->1002
    int requestControl;
    //报销单管控   不可提交->1003  取最后一个目的地的补贴金额->1001  取城市就高->1002  取第一个目的地当天的补贴金额->1004
    int reportControl;
    // 员工修改差补金额  不可修改->1001 可修改，修改后的金额不可大于原金额->1002 可修改，修改后的金额可大于原金额->1003
    int amountModify;
    //报销单自动获取补贴方式  申请单带入"true"  报销单头起止日期生成补贴->"default"   不自动获取 "false"
    String reportAttachDisable;
    //行程是否自动计算  是->true 否 ->false
    boolean travelAutoCalculate;
}

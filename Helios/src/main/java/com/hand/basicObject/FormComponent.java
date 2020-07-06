package com.hand.basicObject;

import com.google.gson.JsonArray;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Author peng.zhang
 * @Date 2020/6/23
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class FormComponent {

    //公司
    String company;
    //选择框
    String selectBox;
    //币种
    String currencyCode;
    //部门
    String department;
    //自定义列表
    String custList;
    //多选值列表
    String moreCustList;
    //成本中心
    String costCenter;
    //级联成本中心
    String associatCostCenter;
    //下级成本中心
    String subordinateCostCenter;
    //数字控件
    String number;
    //employee_expand   个人信息扩展字段
    String employeExpand;
    //时间        格式："2020-06-23T03:17:52.958Z"
    String time;
    //附件
    JsonArray attachment;
    //图片
    JsonArray image;
    //银行卡号
    String bankAccount;
     //普通开关
    boolean ordinarySwitch;
    //日期
    String data;
     //开始结束日期
    String startDate;
    //开始结束日期
    String endDate;
    //日期时间
    String dateTime;
    //选人    传的是userOID
    String selectUser;
    //城市       CHN061001000 城市代码
    String city;
    //参与人  "[{"userOID":"10098d8b-f346-4f14-a12d-3b4c2526e0fd","fullName":"小红35","highOff":null,"participantOID":"10098d8b-f346-4f14-a12d-3b4c2526e0fd","avatar":null},{"userOID":"d387ce07-e4f3-487e-a330-c76e56cf4c2e","fullName":"yuuki","highOff":null,"participantOID":"d387ce07-e4f3-487e-a330-c76e56cf4c2e","avatar":null}]"
    String participant;
    //联动开关    字符串的 "ture" "false"
    String linkageSwitch;
    //备注
    String remark;
    //多行输入框
    String textArea;
    //单行输入框
    String input;
    //事由
    String cause;
    //关联的申请单号
    String applicationOID;
    //关联多申请的dto      例：[{"applicationOID":"073b77b0-d7af-44cb-bd0e-bff9c8bc4d98","tenantId":"1089991852293296130"}]  如果有多个的话 就添加多个object就好
    JsonArray expenseReportApplicationDTOS;

    //因为事由是必输项 所以初始化的时候就输入
    public FormComponent(String cause){
        this.cause =cause;
    }
    public FormComponent(){
    }
}

package com.hand.basicconstant;


/**
 * @author Administrator
 */
public class BaseConstant {
    // 字符编码
    public static final String CHARSET_NAME = "UTF-8";
    // webdriver相关变量
    public static final String CHROME_DRIVER_NAME = "webdriver.chrome.driver";
    public static final String FIREFOX_DRIVER_NAME = "webdriver.gecko.driver";
    public static final String EDGE_DRIVER_NAME = "webdriver.edge.driver";
    public static final String IE_DRIVER_NAME = "webdriver.ie.driver";
    public static final String[]  REQUEST_SPEED_TOO_FAST =new  String[]{"baseRequest speed is too fast", "ボタンを押すのが早すぎます！", "baseRequest speed is too fast"};
    // 时间变量
    public static final int ONE_THOUSAND = 1000;
    public static final int TWO_THOUSANG = 2000;
    public static final int THREE_THOUSANG = 3000;
    public static final int FOUR_THOUSANG = 4000;
    public static final int FIVE_THOUSANG = 5000;
    public static final int SIX_THOUSANG = 6000;
    public static final int SEVEN_THOUSANG = 7000;
    public static final int EIGHT_THOUSANG = 8000;
    public static final int NINE_THOUSANG = 9000;
    public static final int TEN_THOUSANG = 10000;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int ESEVEN = 7;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final int TEN = 10;
    public static final int TWENTY = 20;
    public static final int THIRTY = 30;


    public static final String AUTHORIZATION ="Basic QXJ0ZW1pc0FwcDpuTENud2RJaGl6V2J5a0h5dVpNNlRwUURkN0t3SzlJWERLOExHc2E3U09X";
    public static final String CONTENT_TYPE ="application/json";
    public static final String CURRENCY ="JPY";
    public static final  String[] EXPENSE_REPORT_STATUS_EDIT =new  String[]{"编辑中", "編集中", ""};
    public static final  String[] EXPENSE_REPORT_STATUS_CANCEL =new   String[]{"已撤回", "取消済", ""};
    public static final  String[] EXPENSE_REPORT_STATUS_PENDING_APPROVAL =new   String[]{"审批中", "承認待ち", ""};
    public static final  String[] EXPENSE_REPORT_STATUS_PENDING_DISMISSED =new   String[]{"已驳回", "差戻済", ""};
    public static final  String[] EXPENSE_REPORT_STATUS_AUDIT_REJECTED =new   String[]{"审核驳回", "検印差戻し", ""};


//    已付款申请单
    public static final  int  APPLICATIONS_STATUS_PAYMENT =1004;

    public static final String DEFAULT_PASSWORD ="hand1234";
    public static final  String[] FORM_TYPE = new String[]{"101", "102"};
    public static final  int[] ENTITY_TYPE = new int[]{1001, 1002};
    public static final  String[] ADMIN_MODE = new String[]{"管理员模式", "管理者モードへ","Admin mode"};
//    性别值列表
    public static final  String[] GENDER = new String[]{"性别", "性別","Gender"};
    public static final  String[] USER_MODE = new String[]{"员工模式", "従業員モードへ","Staff Mode"};
    public static final  String[] FORM_MANAGE = new String[]{"表单管理", "フォーム管理","フォーム管理"};


    public static final  String[] EXPENSE_REPORT_CAUSE = new String[]{"报销单测试", "償還テスト", "ReimbursementApi test"};
    public static final  String[] APPLICATION_CAUSE = new String[]{"申请单测试", "申請書テスト", "Application form test"};
    public static final  String[] EXPENSE_REPORT_AUDITPASS_TXT = new String[]{"报销单测试", "償還テスト", "ReimbursementApi test"};
    public static final  String[] EXPENSE_REPORT_AUDITREJECT_TXT = new String[]{"报销单测试", "償還テスト", "ReimbursementApi test"};
    public static final  String[] EXPENSE_REPORT_APPROVALASS_TXT = new String[]{"审批通过", "承認済み", "Approved"};
    public static  final  String INVOICE_FILE_JPG ="src/test/resources/attachment/invoice1.jpg";


}
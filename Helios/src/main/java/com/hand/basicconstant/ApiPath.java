package com.hand.basicconstant;

/**
 * @Author peng.zhang
 * @Date 2020/6/5
 * @Version 1.0
 **/
public class ApiPath {
       //登录
        public static final String GET_TOKEN ="/oauth/token";
        //    获取用户信息
        public static final String GET_ACCOUNT ="/api/account";
        //    获取账套
        public static final String GETSET_OF_BOOKS ="/api/setOfBooks/query/dto";
        //    获取租户信息
        public static final String GETCOMPANIES ="/api/my/companies";
        //        获取可用表单列表
        public static final String GETAVAILABLE_BXFORMS ="/api/custom/forms/my/available";
        //获得报销单表单详情
        public static final String GETBX_FORMS ="/api/custom/forms/%s";
        //报销单保存
        public static final String NEW_EXPENSE_REPORT ="/api/expense/reports/custom/form/draft";
//        报销单选择部门
        public static final String EXPENSE_REPORT_SELECT_DEPARTMENT ="/api/DepartmentGroup/selectDept/enabled";
        //获取报销单中的审批历史
        public static final String APPROVE_HISTORY = "/api/v2/expense/reports/approval/history";
//      查询可导入的费用
        public static final String saerchAvailableImport="/api/expense/report/invoices/import/search";
        //        查询成本中心项
        public static final String GETCOSTCENTEROIDITEMS ="/api/my/cost/center/items/%s";
//        费用分摊查询成本中心项
        public static final String GET_EXPENSE_COST_CENTER_ITEMS ="/api/my/cost/center/items";
//        报销单新建费用选择
        public static final String GET_EXPENSE_REPORT_EXPENSE_TYPES ="/api/expense/type/byUser";
//        管理员模式查看费用类型
        public static final String GET_EXPENSE_TYPES ="/invoice/api/expense/types/groupby/category";
//        选择费用并查看控件信息
        public static final String GET_EXPENSE_TYPE ="/api/expense/types/select/%s";
//        创建费用
        public static final String CREATEINVOICE ="/invoice/api/v5/invoices";
//        获取报销单详情
        public static final String GETEXPENSE_REPORT_DETAL ="/api/v3/expense/reports/%s";
//        获取申请单详情
        public static final String APPLICATION_DETAL ="/api/application/%s";
//        获取银行卡
        public static final String BANK_ACCOUNT ="/api/contact/bank/account/enable";
//        报销单货币控件
        public static final String GETCURRENCY ="/api/company/standard/currency/getAll";
//        表单默认值
        public static final String DEFAULT_VALUES ="/api/custom/form/user/default/values";

        public static final String QueryExpenseLocation= "/amap/v3/assistant/inputtips";
//        查询源泉税类别
        public static final String SOURCE_TAX ="/api/source/tax/category";

//        报销单查询公司控件
        public static final String GET_COMPANIES ="/api/refactor/companies/user/setOfBooks";

//        参与人员查询
        public static final String SELECT_PARTICIPANT ="/api/application/participantsList";

        public static final String queryInvoiceDetail ="/api/expense/report/invoices";
//        查询我的借款
        public static final String APPLICATION_LAN_MY ="/api/loan/application/statusIn/my";

        public static final String IMPORT_INVOICES ="/api/expense/report/invoices/import";
//        报销单提交
        public static final String EXPENSE_REPORT_SUBMIT ="/api/v3/expense/report/submit";
//        申请单提交
        //报销单账本导入费用
        public static final String APPLICATION_SUBMIT ="/api/loan/application/submit";

        public static final String validate ="/api/travel/standards/validate";
//        报销单撤回
        public static final String EXPENSE_REPORT_WITHDRAW ="/api/approvals/withdraw";
//        报销单删除
        public static final String EXPENSE_REPORT_DELETE ="/api/expense/reports/%s";
//        账本中删除费用
        public static final String INVOICEOID_DELETE ="/api/invoice/batch";

//        申请单删除
        public static final String APPLICATION_DELETE ="/api/applications/all/%s";

//        切换语言
        public static final String SWITCH_LANGUAGE ="/api/users/language/%s";

//        获得用户配置
        public static final String PERSONAL_SETTINGS ="/main/personal-settings";
//        获得值列表信息
        public static final String GET_CUSTOMENUMERATIONOID ="/api/custom/enumerations/%s/items/v2";

//        点击差补，获取差补时间
        public static final String GET_TRAVEL_EXPENSE_TIME ="/api/expense/report/subsidy/view";

//        上传附件
        public static final String UPLOAD_ATTACHMENT="/api/upload/attachment";
//        获取差补规则标准
        public static final String GET_TRAVEL_STANDARD ="/api/expense/report/subsidy/add";
//        修改差补时间
        public static final String SUBSIDY_UPDATE ="/api/subsidy/extend/update";
        //报销单撤回费用
        public static final String removeExpense ="/api/expense/reports/remove/invoice/%s/%s";
//        差旅补贴计算
        public static final String CALCULATION_TRAVEL_EXPENSE ="/api/expense/report/subsidy/save/%s";

//        创建费用查询消费税
        public static final String MANAGER_GET_SALE_TAX="/api/consumption/tax";
//        查询员工
        public static final String QUERY_EMPLOYEES="/api/users/v3/search";
//        报销单审批
        public static final String EXPENSEREPORT_APPROVAL="/api/approvals/pass";
//        报销单审批列表查询
        public static final String APPROVAL_LIST_SEARCH ="/api/approvals/batchfilters/v4";
//        财务审核报销单查询
        public static final String FINANCIAL_EXPENSE_REORTS_AUDIT_LIST_SEARCH ="/api/v2/expense/reports/finance/admin/search";
        public static final String FINANCIAL_APPLICATION_AUDIT_LIST_SEARCH="/api/loan/application/finance/admin/search";
//        获得用户资源id
        public  static  final String USER_PERMISSION ="/api/authorization/policy/user/permission";
//        报销单财务审核通过
        public  static  final String EXPENSEREPORT_AUDITPASS ="/api/audit/pass";
//        报销单财务审核拒绝
        public  static  final String EXPENSEREPORT_AUDITREJECT ="/api/audit/reject";

//        借款申请单保存
        public  static  final  String  APPLICATION_DRAFT="/api/loan/application/draft";
//        我的报销单列表
        public  static  final  String  MY_EXPENSEREPORT_LIST="/api/expense/reports/search/my";
//        我的申请单列表
        public  static  final  String  MY_APPLICATION_LIST="/api/applications/v3/search";

//        确认付款操作
        public  static  final  String  CONFIRM_PAYMENT="/api/reimbursement/batch/pay/finished/confirm";
//        获得费用控件明细
        public  static  final  String  EXPENSE_TYPES_WIDGETS="/api/expense/types/select/%s";
//        更新费用类型信息
        public  static  final  String  UPDATE_EXPENSE_TYPES_WIDGETS="/invoice/api/expense/type/%s/fields";
//        获得系统控件列表
        public  static  final  String  EXPENSE_WIDGETS_LIST="/invoice/api/expense/widgets";
//       账本费用列表
        public  static  final  String  INVOICES_LIST="/api/invoices/my";
//        搜索城市
        public  static  final  String  LOCATION_SEARCH="/location-service/api/custom/location/search/keyword";
//        新建费用查询会计科目
        public  static  final  String  ACCOUNTS_QUERY="/api/accounts/query";
//        控件值列表查询
        public  static  final  String  CUSTOM_ENUMERATIONS="/api/custom/enumerations/by/custom/form";

        // 集团模式下-查询公司信息
        public static final String QUERY_COMPANY_CLIENTINFO="/api/company/my/clientInfo";

        //账本-查询所有的费用类型信息
        public static final String QUERY_All_EXPENSE_INFO="/api/expense/types";
        //查询交通IC卡消费数据明细页面
        public static final String QUERY_ICEXPENSE_RECORDSINFO="/pittouch/api/iccard/records/list";
        //导入交通IC卡费用接口
        public static final String IMPORT_IC_EXPENSE="/invoice/api/iccard/records/import";
        //设置-币种页面
        //查询币种列表
        public static final String QUERY_CURRENCY_RATE="/api/currency/rate/list";
        //更新币种的状态
        public static final String UPDATE_CURRENCY_RATE="/api/currency/status/enable";
        //搜索用户
        public static final String SEARCHTRANSFERUser = "/api/users/v3/search";
        //费用转交
        public static final String TRANSFER_TO = "/invoice/api/invoices/transfer/to";
        // 搜索可以关联的申请单
        public static final String SEARCH_APPLICATION = "/api/applications/passed/search";
        //申请单带出的value
        public static final String APPLICATION_VALUE = "/api/expense/report/application/value";
        //员工新增
        public static final String ADD_EMPLOYEE = "/api/refactor/users/v2?roleType=TENANT";
        //获取系统值列表
        public static final String GET_SYSTEM_ENUMERATION ="/api/custom/enumeration/by/system/type";
        //获取值列表的详情
        public static final String ENUMERATION_DETAIL = "/api/custom/enumerations/%s";
        //根据值列表的类型查询系统值列表的详情
        public static final String QUERY_ENUMERATION_DETAIL="/api/custom/enumeration/system/by/type/condition";
        //获取员工扩展字段
        public static final String GET_EMPLOYEE_EXPAND = "/api/custom/values/form";
        //获取员工扩展字段的的表单名称
        public static final String GET_EMPLOYEE_EXPAND_form = "/api/custom/forms/by/form/code";
        //系统值列表
        public static final String System_CUSTOMENUMATIONTYPE = "/api/v2/custom/enumerations";
        //搜索用户
        public static final String SEARCH_USER = "/api/users/v3/search";
        //搜索公司
        public static final String SEARCH_COMPANY = "/api/widget/simpleCompany/all";
        //搜索部门
        public static final String SEARCH_DEPARTMENT ="/api/DepartmentGroup/selectDept/deptCode/name";
    }



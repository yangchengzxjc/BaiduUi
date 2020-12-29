package com.hand.basicConstant;

/**
 * @Author peng.zhang
 * @Date 2020/6/5
 * @Version 1.0
 **/
public class ApiPath {
       //登录
        public static final String GET_TOKEN ="/proxy/oauth/token";
        //    获取用户信息
        public static final String GET_ACCOUNT ="/api/account";
        //    获取账套
        //获取租户信息
        public static final String GETTENANTINFO = "/api/tenant/getById";
        public static final String GET_SET_OF_BOOKS ="/api/setOfBooks/query/dto";
        //    获取租户信息
        public static final String GETCOMPANIES ="/api/my/companies";
        //        获取可用表单列表
        public static final String GETAVAILABLE_BXFORMS ="/api/custom/forms/my/available";
        //获得报销单表单详情
        public static final String GETBX_FORMS ="/api/custom/forms/%s";
        //报销单保存
        public static final String NEW_EXPENSE_REPORT ="/api/expense/reports/custom/form/draft";
        // 借款单 提交
        public static final String SUBMIT_LOAN_BILL = "/api/loanBill/submit/v2";

        //借款单 保存
        public static final String DRAFT_LOAN = "/api/loanBill/draft";

        //新建借款行
        public static final String LOAN_LINE = "/api/loan/line";
        //删除借款单
        public static final String DELETE_LOAN = "/api/loanBill/%s";
        //查询借款类型
        public static final String LOAN_TYPE = "/api/custom/forms/%S/reference/loan/types";
        //查询银行账户
        public static final String GET_BANK = "/api/contact/bank/account/enable";
        //获取借款单详情
        public static final String LOAN_BILL_DETAIL = "/api/loanBill/%s";
        //差旅申请单保存
        public static final String TRAVEL_APPLICATION_SAVE = "/api/travel/applications/draft";
        //费用申请单保存
        public static final String EXPENSE_APPLICATION_SAVE = "/api/expense/applications/draft";
//        报销单选择部门
        public static final String EXPENSE_REPORT_SELECT_DEPARTMENT ="/api/DepartmentGroup/selectDept/enabled";
        //获取报销单中的审批历史
        public static final String APPROVE_HISTORY = "/api/v2/expense/reports/approval/history";
//      查询可导入的费用
        public static final String saerchAvailableImport="/api/expense/report/invoices/import/search";
        //        查询成本中心项
        public static final String GETCOSTCENTEROIDITEMS ="/api/my/cost/center/items/%s";
//        报销单新建费用选择
        public static final String GET_EXPENSE_REPORT_EXPENSE_TYPES ="/api/expense/type/byUser";
//        管理员模式查看费用类型
        public static final String GET_EXPENSE_TYPES ="/invoice/api/expense/types/groupby/category";
//        选择费用并查看控件信息
        public static final String GET_EXPENSE_TYPE ="/api/expense/types/select/%s";
//        创建费用
        public static final String CREATEINVOICE ="/invoice/api/v5/invoices?isDateCombinedUTC=false";
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
        //查询城市组
        public static final String QUERYCITYGROUP = "/api/levels/my";
        //新增员工获取默认的成本中心项
        public static final String DEFAULTCOSTCENT ="/api/cost/center/items/%s/all";

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
        //新建城市组
        public static final String CREATE_CITY_GROUP = "/api/levels?roleType=TENANT";

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

        //ocr
        public static final String OCR = "/receipt/api/receipt/ocr";
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
        //获取费用的参与人信息
        public static final String EXPENSE_PARTICIPANT = "/api/expense/participantsList";
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
        //费用详情
        public static  final String INVOICE_DETAIL = "/api/invoices/%s";
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
        //获取默认的分摊行
        public static final String DEFAULT_APPORTIONEMNT = "/api/v3/expense/default/apportionment";
        // 搜索可以关联的申请单
        public static final String SEARCH_APPLICATION = "/api/applications/passed/search";
        //申请单带出的value
        public static final String APPLICATION_VALUE = "/api/expense/report/application/value";
        //员工新增
        public static final String ADD_EMPLOYEE = "/api/refactor/users/v2?roleType=TENANT";
        //证件新增 修改
        public static final String USERCARD="/api/contact/cards?roleType=TENANT";
        //证件查询
        public static final String QUERY_USER_CARD="/api/user/extra/all/%s";
        //员工离职
        public static final String LEAVE_EMPLOYEE = "/api/users/%s/set/leaving/date/%s";
        //员工重新入职
        public static final String REHIRE = "/api/refactor/users/rehire/%s";
        //获取系统值列表
        public static final String GET_SYSTEM_ENUMERATION ="/api/custom/enumeration/by/system/type";
        //获取值列表的详情
        public static final String ENUMERATION_DETAIL = "/api/custom/enumerations/%s";
        //根据值列表的类型查询系统值列表的详情
        public static final String QUERY_ENUMERATION_DETAIL="/api/custom/enumeration/system/by/type/condition";
        //根据部门OID 查询部门详情
        public static final String SEARCH_DEPARTMENT_DETAILS="/api/departments/%s";
        //获取员工扩展字段
        public static final String GET_EMPLOYEE_EXPAND = "/api/custom/values/form";
        //获取员工扩展字段的的表单名称
        public static final String GET_EMPLOYEE_EXPAND_form = "/api/custom/forms/by/form/code";
        //获取员工所有扩展字段（禁用&启用）
        public static final String GET_EMPLOYEE_CUSTOM_FORM = "/api/custom/forms/%s/simple";
        //系统值列表
        public static final String System_CUSTOMENUMATIONTYPE = "/api/v2/custom/enumerations";
        //搜索用户
        public static final String SEARCH_USER = "/api/users/v3/search";
        //员工详情
        public static final String USER_DETAIL = "/api/users/v2/%s";
        //搜索公司
        public static final String SEARCH_COMPANY = "/api/widget/simpleCompany/all";
        //搜索部门
        public static final String SEARCH_DEPARTMENT ="/api/DepartmentGroup/selectDept/deptCode/name";
        //添加飞机行程
        public static final String ADD_FLIGHT_ITINERARY ="/api/travel/flight/itinerary";
        //添加酒店
        public static final String ADD_HOTEL_ITINERARY = "/api/travel/hotel/itinerary";
        //添加火车
        public static final String ADD_TRAIN_ININERARY = "/api/travel/train/itinerary";
        // 添加用餐行程
        public static final String ADD_DINING_ITINERARY = "/api/travel/dining/itinerary";
        //差旅申请单提交
        public static final String SUBMIT_APPLICATION = "/api/travel/applications/submit/v2";
        //费用申请单提交
        public static final String SUBMIT_EXPENSE_APPLICATION = "/api/expense/applications/submit/v2";
        //差旅申请单提交检查
        public static final String SUBMIT_CHECK = "/api/application/submit/check";
        //获取申请单中的差旅详情
        public static final String APPLICATION_ITINERARY = "/api/travel/applications/itinerarys";
        //自动获取预算费用
        public static final String BUDGET_EXPENSE = "/api/application/form/expense/budget";
        //差补管控
        public static final String TRAVEL_SUBSIDY_CONFIG = "/api/travel/subsidy/control";
        //获取差补表单列表
        public static final String TRAVEL_SUBSIDY_FORM = "/api/travel/subsidy/control/form/list";
        //获取会计期
        public static final String GET_ACCOUNTING_PERIOD = "/api/periodset";
        //获取科目表
        public static final String GET_LEDGER_ACCOUNT = "/ledger/api/account/set/query";
        //获取本位币
        public static final String GET_CURRENCY = "/api/currencyI18n";
        //新增/编辑账套
        public static final String ADD_SET_OF_BOOKS = "/api/setOfBooks";
        //搜索账套
        public static final String SEARCH_SET_OF_BOOKS = "/api/setOfBooks/query/dto";
        //获取账套详情
        public static final String GET_SET_OF_BOOKS_DETAIL ="/api/setOfBooks/%s";

        //获取表单差补基础配置
        public static final String TRAVEL_RULE_BASE_CONFIG = "/api/travel/subsidies/rule/dimension";
        //获取表单差补规则list
        public static final String SUBSIDY_RULE_List = "/api/travel/subsidies/rule/list/v2";
        //获取表单配置的差补规则的基础设置
        public static final String SUBSIDY_RULE_CONFIG = "/api/travel/subsidies/rule/form";
        //获取差补规则详情/差补规则保存
        public static final String SUBSIDY_RULE_DETAIL = "/api/travel/subsidies/rule";
        //差补表单详情
        public static final String FORM_DETAIL = "/api/custom/forms/%s/detail";
        // @api {GET} /api/dining/scene/form/enable  查询表单可用的用餐场景
        public static final String FORM_DINING_SCRNE = "/api/dining/scene/form/enable";
        //新建报销标准规则
        public static final String ADD_REIMB_STANDARD = "/api/expense/standard/rule";
        //获取费用类型
        public static final String EXPENSE_TYPE = "/api/expense/type/by/setOfBooks";
        //获取启用公司
        public static final String ENABLED_COMPANY = "/api/widget/company/all";
        //获取人员组
        public static final String USER_GROUPS = "/api/user/groups/search";
        //获取单据类型
        public static final String FORM_TYPE = "/api/custom/forms/loan/reference?roleType=TENANT&page=0&size=10&formTypeList=3001&formTypeList=3002&formTypeList=3003&setOfBooksId=%s&keyword=%s&keywordLable=%s";
        //删除报销标准
        public static final String DELETE_REIMB_STANDARD = "/api/expense/standard/rule/delete/%s";
        //消费商接口
        //机票,酒店,火车结算数据获取(从供应商)
        public static final String TMCDATA = "/data/api/openapi/%s/settlement";
        //机票,酒店,火车 结算数据推送
        public static final String PUSHTMCSEETLEMRNTDATA = "/data/api/openapi/%s/settlement/push";
        // 落库查询数据 机票  酒店 火车 结算数据
        public static final String QUERYVENDORDATA = "/vendor-data-service/api/open/internal/%s/settlement";
        //新增管控信息
        public static final String ADD_CONTROLITEM = "/api/expense/standard/controlitem/%s";
        //获取报销标准规
        public static final String GET_CONTROLITEM = "/api/expense/standard/master/controlitems/%s";
        //获取报销标准规则基本标准
        public static final String GET_ITEM = "/api/expense/standard/master/items/%s";
        //修改报销标准基本标准
        public static final String ADD_ITEM = "/api/expense/standard/item";
        // 火车,机票,酒店 订单数据推送
        public static final String  PUSHTMCORDERDATA = "/data/api/openapi/%s/order/push";
        // 火车 机票 酒店 订单数据内部查询
        public static final String QUERYORDERDATA = "/vendor-data-service/api/open/internal/%s/order";
        //查询国内 国际两舱设置
        public static final String QUERYBOOKCLASS = "/vendor-info-service/api/company/bookClass";
        //报销标准规则查询
        public static final String QUERY_REIM_RULES = "/api/expense/standard/rule/list";
        //tmc  人员信息查询
        public static final String TMCUSER = "/vendor-sync-service/api/tmcRequest/user";
        // tmc  申请单行程查询
        public static final String TMCAPPLICATION = "/vendor-sync-service/api/tmcRequest/plan";
        //获取报销单提交管控规则
        public static final String GET_SUBMISSION_RULES = "/api/expense/standard/master/rule/%s";
        //开放平台查询 人员同步
        public static final String QUERY_USER_SYNC="/vendor-sync-service/api/tmcRequest/user";
        //新建提交管控规则管控项
        public static final String ADD_SUBMISSION_ITEM = "/api/expense/submit/control/item/%s";
        //获取提交管控规则管控项信息
        public static final String GET_SUBMISSION_ITEM = "/api/expense/submit/control/items/%s";
        //获取表单消费商管控
        public static final String GETFORMVENDORCONTROL = "/api/custom/forms/property/travel/configuration/%s";
        //部门管理查询部门详情的接口
        public static final String DepartmentDetail = "/api/departments/%s";
        //公司定义   公司详情
        public static final String COMPANYDetail = "/api/company/%s";
        //平台sso登录
        public static final String SSOLOGIN = "/sso";
        //消费平台H5 SSO单点
        public static final String SUPPLIER_SSO="/vendor-info-service/api/sso/common";
        // 消费平台 web sso H5
        public static final String SSO = "/vendor-info-service/api/sso";
        // 消费平台 web sso 统一单点登录 web
        public static final String SSO_COMMON = "/vendor-info-service/api/sso/common";
        //发票查验
        public static final String  RECEIPT_VERIFY = "/receipt/api/receipt/verify";
        //批量查验发票
        public static final String BATCH_VERIFY = "/receipt/api/receipt/verify/batch";
        //报销标准查询城市组
        public static final String GET_CITY_GROUP = "/api/levels/org";
        //删除报销标准中的标准项
        public static final String SANDARD_ITEM_DELETE = "/api/expense/standard/item/delete/%s/%s";
        //报销单提交管控查询单据
        public static final String QUERY_FORM = "/api/custom/forms/loan/reference/v2?roleType=TENANT&page=0&size=10&keyword=%s&keywordLable=%s&formTypeList=3001&formTypeList=3002&formTypeList=3003&setOfBooksId=%s&companyOID=%s";
        //财务卷票机ocr识别
        public static final String SCAN_OCR = "/api/expense/reports/bag/receipt/scan";
        //删除发票
        public static final String DELETE_RECEIPT = "/receipt/api/receipts/%s";
        //财务查询票据详情
        public static final String RECEIPT_QUERY = "/api/expense/reports/receipt/finance/query";
        //借款单查询供应商
        public static final String GET_SUPPLIER = "/supplier/api/ven/info/by/company";
        //非金额管控 查询值列表
        public static final String GET_enumerations = "/api/custom/enumerations/items/by/type";
}



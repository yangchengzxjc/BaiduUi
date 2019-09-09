# 获得登录信息
account='/api/account'
# 获取公司信息
company = '/api/my/companies'
# 获取表单id
get_form_url = '/api/custom/forms/my/available'
# 获取控件ID
get_ControlId = '/api/custom/forms/'
# 查看单据审核的页面
get_check = '/api/v2/Reimbursement/reports/finance/admin/search?page=0&size=10&sort='
# 财务管理-报销单查看
Finance_reimbursement_Find = '/api/approvals/filters/get'
# 财务管理-报销单查看-打印
Finance_reimbursement_print = '/api/Reimbursement/reports/generate/pdf/'
# 查询员工信息
# 获取参与人的信息
participant_info = '/api/applications/passed/search?page=0&size=10&formOID=%s&userOID=%s'
# 关闭差补城市的控件的url
close_city = '/api/travel/subsidies/rule/dimension?roleType=TENANT'
# 滴滴费用
yangzhao_url = '/invoice/api/v4/invoices'
# profile配置
profile_url = '/main/setting/function-profile'
# 查询借款单的url
query_loan_url='/api/loan/line/query/pending/confirm'
# 确认付款的url
confirm = '/api/reimbursement/batch/pending/pay/confirm'
confirm_paid = '/api/reimbursement/batch/paid/confirm'
# 单据审核页面的url
invoice_verify = '/main/financial-management/finance-audit/:tab'
# 报销单查看的页面的url
# 差补规则的URL
expense_standard = '/main/expense-reimbursement-control/expense-standard'
reimbursement_look = '/main/expense-parent-report/expense-report'
# 借款单确认付款
confirm_paid_url = '/main/financial-management/confirm-payment'
# 审批页面的url
approve_url = '/main/approve/:entityType'
expenseTypeOID = '/invoice/api/expense/types/groupby/category?setOfBooksId=%s&createdManually=&enabled=&roleType=TENANT'
# 第三方费用类型
third_expense = '/api/invoices/third/party'
# 修改差补规则的url
travel_setting = '/api/travel/subsidies/rule/base/setting/?setOfBooksId='
# 申请单界面的url
apply_url = '/main/request'
# 报销单查看界面的url
reimbursement_find = '/main/financial-management/finance-view-expense'
# 获取表单的所有类型
formType = '/api/custom/forms/my/available?formType=101&userOID=%s'
# 修改差补规则的url
subsidy_rule = '/api/travel/subsidies/rule/dimension'
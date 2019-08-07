# 表单类型：差旅报销单-新建他人费用
form_type = 'xpath=>//li[@class="ant-dropdown-menu-item"and text()="差旅报销单-新建他人费用"]'
# 控件选择
form1 = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/form/div[2]/div[2]/div/div/div[1]/div/div/div/..'
department_form = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/form/div[2]/div/div[1]/div[2]/div/div/div[1]/div/div/div'
# 选择一个部门
my_department='//*[@class="ant-radio-inner"]/..'
# 选择关联的申请单（第一个）
apply_number = '//td[@class="ant-table-selection-column"]'
# 选择了申请单后的确定按钮
confirm = '//*[@class="ant-btn ant-btn-primary"]'
# 新建报销单
submit = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/form/div[5]/div/div/button[1]'
# 审批页面的汇率输入框
rate_input = 'xpath=>//*[@id="actualCurrencyRate"]'
# 审批页面进行保存的button
rate_save = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/span/div/div/div[1]/div/div[2]/div/form/div[1]/div/div/div/div[1]/div[2]/div/div[6]/button[1]'
# 审批界面的汇率
approve_rate = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[2]/div/div[1]/span/div/span/div/div/div[1]/div[2]/div/div/div/div[2]/div/table/tbody/tr/td[8]'

# 审核界面的汇率
Finance_rate = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[2]/div/div[1]/span/div/span/div/div/div[1]/div[2]/div/div/div/div[2]/div/table/tbody/tr/td[7]'
# 汇率警告提醒
warning_remind = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/span/div/div/div[1]/div/div[2]/div/form/div[1]/div/div/div/div[1]/div[2]/div/div[3]/div[2]/div'
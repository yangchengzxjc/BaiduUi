#日常报销单-UI自动化
form_type = 'xpath=>//li[@class="ant-dropdown-menu-item"and text()="日常报销单-UI自动化"]'
#财务管理
financial_management ='xpath=>//span[text()="财务管理"]'
#单据审核
check = 'xpath=>//*[@id="financial-management$Menu"]/li[1]'
#审核通过
passed = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[4]/div/div/div/div/div/div[2]/div/span/button'
#审核通过后的报销单
checked_expense = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[3]/div/div/div/div/div/table/tbody/tr[1]/td[5]/span'
#打印按钮
Printing_button = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[4]/div/button[1]/span'
Printing_button1 = '//*[@id="app"]/div/div[2]/div[2]/div/div[4]/div/button[1]/span'
CNY ='xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[2]/div/div[1]/span/div/span/div/div/div[1]/div[2]/div/div/div/div[2]/div/table/tbody/tr/td[5]'
input_expenses = '//*[@id="amount"]'
travel_input_expenses = 'xpath=>//*[@id="amount"]'
destinations ='//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/span/div/div/div[1]/div/div[2]/div/form/div[1]/div/div/div/div[9]/div[2]/div/div/div/div/div/ul/li/div/input'
# 报销单详情页获取单号

get_businessCode = '//*[@id="app"]/div/div[2]/div[2]/div/div[1]/div/div[2]/div/div/div/div[2]/span[1]'
# 撤回报销单
cancel = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[4]/div/button[1]'
confirm = 'xpath=>/html/body/div[7]/div/div[2]/div/div[1]/div/div/div[2]/button[2]'
# 第一条单据的状态
bill_status = '//*[@id="app"]/div/div[2]/div[2]/div/div[3]/div/div/div/div/div/div/table/tbody/tr[1]/td[12]/div/span'
# 报销单的再次提交
continue_submit = '//*[@class="ant-btn ant-btn-primary"]'
# 继续提交的text
continue_submit_text = 'xpath=>//*[text()="继续提交"]'
# 选择申请单的搜索框
apply_search = '//*[@id="keyword"]'
# 点击搜索
search_reason = '//*[text()="搜 索"]/..'
participant= 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/form/div[2]/div/div[3]/div[2]/div/div/div[1]/div/div/ul'
# 添加一个参与人
add_participant='//*[@class="ant-checkbox-input"]'
# 报销单页面的大巴费用
big_bus_expense='//*[text()="大巴"]'
# 第三方消费
third_consumption = '//img[@class="ven-item-icon"]'
# 携程在线客服
Customer_service = 'xpath=>//*[text()="在线客服"]'
# 账本导入
book_enter = 'xpath=>//*[text()="账本导入"]/..'
# 扬招费用类型
yangzhao_expensetype = '//*[text()="扬招费用"]'
select_button_confirm='//*[@class="ant-btn ant-btn-primary"]'
# 单号
# loan_number1 ='xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/form/div/div[1]/div[4]/div/div[2]/div/div/div/div/div[1]/..'
loan_number = '//*[@class="ant-select-selection__rendered"]'
# 确认付款的搜索框
payment_search = 'xpath=>//*[text()="搜 索"]/..'
# 确认付款选择框
payment_select = '//input[@class="ant-checkbox-input"]'
# 确认付款
payment ='xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[4]/div[2]/button[2]'
# 单据审核页面查看第二笔费用
second_expense ='xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[2]/div/div[1]/span/div/div[1]/div[2]/div/div/div/div[2]/div/table/tbody/tr[2]/td[13]/a'
# 输入新的金额
new_amount = 'xpath=>//*[@id="amount"]'
save_amount = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/span/div/div/div[1]/div/div[2]/div/form/div[1]/div/div/div/div[1]/div[2]/div/div[5]/button[1]'
# 审批搜索报销单
search_approve = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[3]/div[2]/span/input'
# 财务审核录入专用发票后的分摊金额
financial_apportion1 = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/span/div/div/div[1]/div/div[2]/div/form/div[1]/div/div[2]/div/div[6]/div[2]/div/div/div/div/div/div/div/table/tbody/tr[1]/td[2]'
financial_apportion2 = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/span/div/div/div[1]/div/div[2]/div/form/div[1]/div/div[2]/div/div[6]/div[2]/div/div/div/div/div/div/div/table/tbody/tr[2]/td[2]'
financial_apportion3 = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/span/div/div/div[1]/div/div[2]/div/form/div[1]/div/div/div/div[6]/div[2]/div/div/div/div/div/div/div/table/tbody/tr[1]/td[2]'
financial_apportion4 = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/span/div/div/div[1]/div/div[2]/div/form/div[1]/div/div/div/div[6]/div[2]/div/div/div/div/div/div/div/table/tbody/tr[2]/td[2]'
# 原币金额
origin_amout = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/span/div/div/div[1]/div/div[2]/div/form/div[1]/div/div[2]/div/div[1]/div[2]/div/div[3]/span/span[1]'
# 删除发票
delete_invoice = 'xpath=>//*[@class="ant-btn button-delete ant-btn-primary ant-btn-sm"]'
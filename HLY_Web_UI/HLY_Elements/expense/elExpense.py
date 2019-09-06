# # 新建费用里面的大巴
Expense_bus = 'xpath=>//div[@class="expense-name" and text()="大巴"]'

# 收款单位及个人添加
Receipt_unit='xpath=>//*[text()="添加"]'

# 收款单位及个人查找
Receipt_unit_search='xpath=>//button[@type="submit"]/span[text()="搜 索"]'
# 收款人信息列表
Receipt_info='xpath=>/html/body/div[8]/div/div[2]/div/div[1]/div[2]/div[2]/div/div/div/div/div/table/tbody/tr/td[2]'
# 请选择收款账号确定按钮
Receipt_account_ok='xpath=>//button[@type="button"]/span[text()="确 定"]'

# 报销单详情编辑按钮
Expense_Edit='xpant=>//*[@class="edit"]'
# 选择币种下拉框
Currency = 'xpath=>//div[@title="CNY, 人民币"]'

# 港币币种
HKD = 'xpath=>//li[text()="HKD"]'
# 美元汇率
USD = 'xpath=>//LI[text()="USD"]'
# 新建费用金额输入框
Expense_Amount = 'xpath=>//input[@class="ant-input-number-input" and @placeholder="请输入"]'

# 汇率输入框
Expense_Amount_input='xpath=>//input[@id="actualCurrencyRate"]'
# 新建费用保存按钮
# Save_Expense='xpath=>//span[text()="保 存"]'
Save_Expense = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/div/div[2]/button[1]'

# 核销按钮
SalesAccount = 'xpath=>//a[text()="冲借款"]'
SalesAccount_prod = 'xpath=>//a[text()="核销"]'
# 提交报销单
submit_expense = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[4]/div/button[1]'

# 费用全选框
# Choose_All_expense='xpath=>//input[@type="checkbox"]'
Choose_All_expense='//input[@type="checkbox"]'
# 选择费用添加付款行按钮
Multi_payment_lineReimbursementFormHasBeenOpened='//button[@type="button" and  @class="ant-btn ant-btn-primary"]/span'
# 选择关联的申请单
linked_apply = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/form/div[1]/div[2]/div/div/div[1]/div/div/div'
# 确定button
# enter_button = '//div[@class="ant-modal-footer"]//button[2]'
# enter_button = '//div[@class="ant-modal-footer"]/div/button[2]'
enter_button = '//div[@class="ant-modal-footer"]/div/button/span[text()="确 定"]/..'
# 选择关联的申请单（选择第一个申请单）
select_apply = "//td[text()='TA00090716']"
# 差旅报销单-多人参与
travel_expense_participants ='xpath=>//li[@class="ant-dropdown-menu-item"and text()="差旅报销单-多人参与"]'

# 报销单界面的新建按钮
New = 'xpath=>//button[@type="submit"]'

# 报销单单据金额
DocumentAmount='//*[@id="app"]/div/div[2]/div[2]/div/div[1]/div/div[2]/div/div/div/div[4]/span/span[1]'
PaymentAmount='//*[@id="app"]/div/div[2]/div[2]/div/div[1]/div/div[2]/div/div/div/div[4]/span/span[2]'
# 报销单页面的查看标签
check_lable = '//span[@class="ant-table-row-expand-icon ant-table-row-collapsed"]'
# 费用信息
expense_info = 'xpath=>//div[text()="费用信息"]'

# 报销单详情页面的lable
detal_page_lable = '//span[@class="ant-table-row-expand-icon ant-table-row-collapsed"]'
# 报销单页面借款申请单的链接
loan_link = 'xpath=>//a[@class="link-loan"]'
loan_links = '//a[@class="link-loan"]'
# 单据审核页面的+号标签
check_lable2 = 'xpath=>//span[@class="ant-table-row-expand-icon ant-table-row-expanded"]'
# 财务审核页面的汇率描述
rate_description = 'xpath=>//*[@class="expense-rate-description"]'
# 财务审核界面的搜索
Financial_search = 'xpath=>//*[text()="搜 索"]/..'
# 报销单详情页中的费用信息币种
coin_varity = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[2]/div/div[1]/span/div/span/div/div/div[1]/div[2]/div/div/div/div[2]/div/table/thead/tr/th[5]'

# 报销单界面的第一笔费用的金额
first_amout = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[2]/div/div[1]/span/div/span/div/div/div[1]/div[2]/div/div/div/div[2]/div/table/tbody/tr[2]/td[9]/span'
# 录入发票点击请选择
invoice_type = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/span/div/div/div/div/div[1]/div/div/div/div/form/div[1]/div[2]/div/div/div/div/div'
# 发票代码
invoice_code = 'xpath=>//*[@id="invoiceCode"]'
# 发票号码
invoice_number = 'xpath=>//*[@id="invoiceNumber"]'
# 开票日期
invoice_date = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/span/div/div/div/div/div[1]/div/div/div/div/form/div[3]/div[3]/div[2]/div/span/div/input'
# 发票金额（不含税）
invoice_money = 'xpath=>//*[@id="nonVATinclusiveAmount"]'
# 1月5日
date = 'xpath=>/html/body/div[11]/div/div/div/div/div[2]/div[2]/table/tbody/tr[1]/td[6]'
# 日历翻页
page_turning = 'xpath=>/html/body/div[11]/div/div/div/div/div[2]/div[1]/div/a[2]'
# 日历顶上的日期
month = 'xpath=>/html/body/div[7]/div/div/div/div/div[2]/div[1]/div/span/a[2]'
# 一月
juanary = 'xpath=>/html/body/div[7]/div/div/div/div/div[2]/div[1]/div[2]/div/div[2]/table/tbody/tr[1]/td[1]/a'
select_expense_frame = '//*[@class="ant-checkbox-input"]'
select_expense_frames = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[2]/div/div[1]/span/div/span/div/div/div[1]/div[2]/div/div/div/div[2]/div/table/tbody/tr[2]/td[2]/span/label/span/span'
# 目的地
Destination = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/span/div/div/div[1]/div/div[2]/div/form/div[1]/div/div/div/div[9]/div[2]/div/div/div/div/div/ul/li/div/input'
Destination_prod = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/div/div[1]/div/div[2]/div/form/div[1]/div/div/div/div[9]/div[2]/div/div/div/div/div/ul/li/div/input'
departure = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/span/div/div/div[1]/div/div[2]/div/form/div[1]/div/div/div/div[8]/div[2]/div/div/div/div/div/ul/li/div/input'
departure_prod ='xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/div/div[1]/div/div[2]/div/form/div[1]/div/div/div/div[8]/div[2]/div/div/div/div/div/ul/li/div/input'
# scroll_locate
scroll_locate = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/div/div[1]/div/div[2]/div/form/div[1]/div/div/div/div[7]/div[1]/label'
# 大巴费用
bus_expense = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/span/div/div/div/div/div[1]/div/div/div/div/div[2]/div[2]/div[7]/div/div/div/div/div'
# input_expense
input_expense = 'xpath=>//*[@id="amount"]'
# 保存按钮
save = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/span/div/div/div[2]/button[1]'
destinations_prod='//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/div/div[1]/div/div[2]/div/form/div[1]/div/div/div/div[9]/div[2]/div/div/div/div/div/ul/li/div/input'
# 出发地
departures ='//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/span/div/div/div[1]/div/div[2]/div/form/div[1]/div/div/div/div[8]/div[2]/div/div/div/div/div/ul/li/div/input'
departures_prod ='//*[@id="app"]/div/div[2]/div[2]/div/div[5]/div[2]/div[2]/div/div[1]/div/div[2]/div/form/div[1]/div/div/div/div[8]/div[2]/div/div/div/div/div/ul/li/div/input'
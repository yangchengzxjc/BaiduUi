# 单据金额
invoice_amount = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[1]/div/div[2]/div/div/div/div[4]/span/span[1]'
# 付款金额
pay_amount = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[1]/div/div[2]/div/div/div/div[4]/span/span[2]'
# 付款行开户人名称
card_name = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[2]/div/div[2]' \
            '/div/div[3]/div/div/div/div/div[2]/div/table/tbody/tr[1]/td[8]/div/span[1]'
# 报销单
expense = 'xpath=>//*[@id="app"]/div/div[1]/div/div[2]/ul/li[3]/div'
# 我的报销单
my_expense = 'xpath=>//*[@id="Reimbursement-parent-report$Menu"]/li/div'
# 新建报销单
new_expense = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[2]/button'
# 差旅报销单
travel_expense = 'xpath=>//li[@class="ant-dropdown-menu-item"and text()="差旅报销单-全控件"]'
travel_expense2 = 'xpath=>//li[@class="ant-dropdown-menu-item"and text()="%s"]'

# 部门选择框
select_department = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/form/div[2]/div/div[1]/div[2]/div/div/div[1]/div/div/div'

# 报销单里新建费用按钮

# NewExpenseBut="xpath=>//span[text()='新建费用']"
NewExpenseBut = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[2]/div/div[1]/span/div/div[1]/button[1]'
# 输入事由
cause = '//*[@class="ant-input"]'
# 时间控件
data_time = 'xpath=>//*[@class="ant-calendar-picker-input ant-input"]'
# 差补行程时间
stroke_time = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[6]/div/div[2]/div[2]/div/section/div/div/div/div/div/span/div/div[2]/div[1]/div/div/div[2]'
# 选择日期
select_day = '//*[@class="ant-calendar-cell"]'
#新建费用
new_expense = 'xpath=>//*[text()="新建费用"]/..'
# 新建他人费用
new_other_expense = 'xpath=>//*[text()="新建他人费用"]/..'
# 新建button
submit ='xpath=>//*[@id="app"]/div/div[2]/div[2]/div/form/div[4]/div/div/button[1]'
# 我没有银行卡用户
no_card_user = 'xpath=>//*[contains(text(),"我没有银行卡")]'
# 收款方
Receipt = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[2]/div/div[2]/div/div[4]/div[2]/div[2]/div/form/div[2]/div[2]/div/div/span/div/div/div/div'
# 费用归属人
expense_owner ="xpath=>//span[text()='费用归属人']"
expense_owners ='xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[2]/div/div[1]/span/div/span/div/div/div[1]/div[2]/div/div/div/div[2]/div/table/tbody/tr/td[13]/span'
# 用户名
user_name = 'xpath=>//*[@id="app"]/div/div[2]/div[1]/div[2]/div[7]/div[2]'
# 付款用途
PaymentUse = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[2]/div/div[2]/div/div[4]/div[2]/div[2]/div/form/div[6]/div[2]/div/div/div/div/div'
# 未输入收款银行卡的账号保存提示后显示红色的提示请选择
card_select = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[2]/div/div[2]/div/div[4]/div[2]/div[2]/div/form/div[3]/div[2]/div/div[2]'
# 部门信息
department = '//*[text()="部门"]'
#提交报销单
submit_expense ='xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[4]/div/button[1]'
# 报销单撤回确定按钮
withdraw_confirm = 'xpath=>/html/body/div[7]/div/div[2]/div/div[1]/div/div/div[2]/button[2]'
# 分摊驳回后的第二笔费用
second_apportion = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[2]/div/div[1]/span/div/span/div/div/div[1]/div[2]/div/div/div/div[2]/div/table/tbody/tr[2]/td[3]'
# 报销单界面编辑日历
edit_date ='xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[1]/div/div[3]/div[2]/div[2]/div/form/div[2]/div/div[3]/div[2]/div/span/span'
# startDate
start_Date ='xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[1]/div/div[1]/div[3]/div[2]'
# 报销单查看确认单号
confirm_businessCode = 'xpath=>//li[text()="%s"]'

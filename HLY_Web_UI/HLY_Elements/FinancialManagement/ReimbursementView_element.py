# 搜索按钮
Search = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[1]/div[2]/div[1]/div[1]/form/div/div[2]/div/button[1]'
# 报销单单据ID输入框
ReimbursementID = '//div[text()="请输入父单/子单ID/报销单ID"]'
ReimbursementID2 = '//*[@id="businessCode"]'
# 报销单查看行的报销单单号
Line_ReimbursementID = '//*[@id="app"]/div/div[2]/div[2]/div/div[1]/div[2]/div[1]/div[4]/div/div/div/div/div/div/table/tbody/tr[aaaaaaa]/td[7]/span'
# 报销单查看行的报销单打印按钮
Line_ReimbursementPrint = '//*[@id="app"]/div/div[2]/div[2]/div/div[1]/div[2]/div[1]/div[4]/div/div/div/div/div/div/table/tbody/tr[aaaaaaa]/td[13]'
# 报销单详情打印按钮
Print_But = '//button[@type="button" and  @class="ant-btn back-btn ant-btn-primary"]'
# 报销单界面申请人frame
reimbursement_applicant_frame = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[1]/form/div[2]/div[1]/div[3]/div/div[2]/div/div/span/div/div/div/div'

# 申请单界面的申请人frame
apply_applicant_frame = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[1]/form/div[2]/div[1]/div[2]/div/div[2]/div/div/div[1]/div/div/div'
# 搜索界面的展示条数
info_number = '//tr[@class="ant-table-row chooser-table-row ant-table-row-level-0"]'
# 审批界面的申请人的frame
approve_applicant_frame = 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/form/div[2]/div[1]/div[2]/div/div[2]/div/div/div[1]/div/div/div'
#共搜索到数据条数显示
Search_result='xpath=>//div[contains(text(),"共搜索到")]'
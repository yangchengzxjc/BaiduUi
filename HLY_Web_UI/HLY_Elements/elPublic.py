# coding=utf-8
# !/usr/bin/env python
#===============================================================================
# Created on 2019/1/7 14:10
# author: Administrator
# @Software: PyCharm
#脚本功能描述：关于测试环境的配置
#===============================================================================
homepage = 'xpath=>//*[@id="mainFrame"]/md-toolbar/div/div[2]/span[1]'    # 跳至新中控
account = 'xpath=>//*[@id="app"]/div/div[2]/div[1]/div[2]/div[8]/div[2]'    # 用户中心，个人设置
mode_al = 'xpath=>//*[@id="app"]/div/div[1]/div[1]/div[1]/div'  #集团模式
mode_tc = 'xpath=>//*[@id="app"]/div/div[1]/div[1]/div[1]/div[2]'
SwitchingCompany='xpath=>//li[@class="ant-dropdown-menu-item"and text()="切换公司"]' #切换公司选项
com1="xpath=>/html/body/div[7]/div/div[2]/div/div[1]/div[2]/div[4]/div/div/div/div/div/table/tbody/tr[1]/td[1]/span/label/span/input"
com2="xpath=>/html/body/div[7]/div/div[2]/div/div[1]/div[2]/div[4]/div/div/div/div/div/table/tbody/tr[2]/td[1]/span/label/span/input"
SwitchingCompanyOK="xpath=>/html/body/div[7]/div/div[2]/div/div[1]/div[3]/div/button[2]"
# 公司模式下首页的报销单
company_page = 'xpath=>//*[text()="报销单"]'
# 切换公司
select_company = 'xpath=>/html/body/div[6]/div/div/div/div[2]/div/div/div/div/div[3]/div/div/div/div/div[1]/div/div[1]'



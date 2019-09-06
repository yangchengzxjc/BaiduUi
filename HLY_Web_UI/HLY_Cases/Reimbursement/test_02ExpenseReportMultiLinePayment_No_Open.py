# coding=utf-8
# !/usr/bin/env python
#===============================================================================
# Created on 2019/1/25 11:09
# author: Administrator
# @Software: PyCharm
#脚本功能描述：关于测试环境的配置
#===============================================================================
from selenium.webdriver.common.keys import Keys

from HLY_PageObject.UI.Process import Process
from HLY_Elements.expense import  elExpense
from HLY_PageObject.UI.my_expense.my_expense import  My_Expense
import  time
def test_ExpenseReportMultiLinePayment_No_Open(enter):
    """

    :param enteraward: 提前进入新中控
    :param SwitchingCompany: 提前切换公司，进去公司模式下
    :return:
    """

    driver = enter.driver
    process = Process(driver)
    my_expense = My_Expense(driver)
    my_expense.Newexpense("多付款行报销单未开启") #打开固定的新建报销单页面
    my_expense.InputCause("多付款行报销单未开启","多付款行报销单未开启测试")
    my_expense.ClickNew()
    my_expense.ClickNewExpenseButton()
    my_expense.SelectExpense("大巴")
    my_expense.InputExpense_Amount(100)
    my_expense.ClickExpenseSave(3)
    my_expense.Pagescroll(elExpense.SalesAccount, 2)
    el=driver.find_elements_by_xpath(elExpense.Multi_payment_lineReimbursementFormHasBeenOpened)
    isTrue = False
    for x in el:
        if str(x.text).find("选择费用添加付款行") !=-1:
            isTrue = True
    assert not isTrue,"按钮消失"
    my_expense.summit_expense(2)
    process.continue_submit()


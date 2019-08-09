# coding=utf-8
# !/usr/bin/env python
#===============================================================================
# Created on 2019/1/25 11:09
# author: Administrator
# @Software: PyCharm
#脚本功能描述：Bug 20589日常报销单测试外币金额
#===============================================================================
import pytest
from HLY_PageObject.UI.Process import Process
from common.log import logger
from HLY_PageObject.UI.my_expense.my_expense import  My_Expense
import  time

@pytest.mark.run
def test_DailyExpenseReport(enter):
    """

    :param enteraward: 提前进入新中控
    :param SwitchingCompany: 提前切换公司，进去公司模式下
    :return:
    """
    driver = enter.driver
    process = Process(driver)
    my_expense = My_Expense(driver)
    my_expense.Newexpense("日常报销单-UI自动化") #打开固定的新建报销单页面
    my_expense.InputCause("日常报销单-UI自动化","日常报销单-UI自动化测试外币金额")
    my_expense.ClickNew()
    my_expense.ClickNewExpenseButton()
    my_expense.SelectExpense("大巴")
    my_expense.SelectExpense_Currency("HKD")
    my_expense.InputRate("3.9")
    my_expense.InputExpense_Amount("100.12")
    my_expense.ClickExpenseSave(5)
    amount1 = round(3.9 * 100.12, 2)
    time.sleep(2)
    logger.info("第二次创建费用")
    my_expense.ClickNewExpenseButton()
    my_expense.SelectExpense("大巴")
    my_expense.SelectExpense_Currency("HKD")
    my_expense.InputRate("0.8")
    my_expense.InputExpense_Amount("200.34")
    my_expense.ClickExpenseSave(3)
    amount2 = round(3.9 * 200.34, 2)
    amount3 = amount2+amount1
    time.sleep(5)
    assert str("%.2f"% amount3) == my_expense.getDocumentAmount().replace(',','')
    # assert str(amount2 + amount1) == my_expense.getDocumentAmount().replace(',', '')
    my_expense.summit_expense(2)
    process.continue_submit()


# coding=utf-8
# !/usr/bin/env python
#===============================================================================
# Created on 2019/1/25 11:09
# author: Administrator
# @Software: PyCharm
#脚本功能描述：财务管理-报销单查看-查看明细
#===============================================================================
from HLY_PageObject.UI.Process import Process
from common.log import logger
from HLY_PageObject.UI.my_expense.my_expense import  My_Expense
from HLY_PageObject.UI.FinancialManagement.ReimbursementView  import   ReimbursementView
from HLY_PageObject.UI.Reimbursement import Reimbursement
import  time
from common.parameter import  GetConfigp as pa
from common.globalMap import GlobalMap


def test_FinanceViewExpenseReport(enter):
    """
    :param enteraward: 提前进入新中控
    :param SwitchingCompany: 提前切换公司，进去公司模式下
    :return:
    """
    glo = GlobalMap()
    pp = pa('./config/hly.config')
    driver = enter.driver
    process = Process(driver)
    my_expense = My_Expense(driver)
    reimbursementView = ReimbursementView(driver)
    my_expense.Newexpense("日常报销单-UI自动化") #打开固定的新建报销单页面
    my_expense.InputCause("日常报销单-UI自动化","日常报销单-UI自动化测试外币金额")
    my_expense.ClickNew()
    time.sleep(2)
    my_expense.ClickNewExpenseButton()
    my_expense.SelectExpense("大巴")
    my_expense.SelectExpense_Currency("HKD")
    my_expense.InputRate("3.9")
    my_expense.InputExpense_Amount("100.12")
    my_expense.ClickExpenseSave(3)
    time.sleep(5)
    business_Code = process.get_businessCode()
    logger.info("单号:"+business_Code)
    my_expense.summit_expense()
    time.sleep(3)
    process.continue_submit()
    time.sleep(6)
    driver.get('%s://%s%s' % (glo.get("Webprotocol"),glo.get('Webhost'), pp.getoption("BASIC", "ReimbursementView")))
    reimbursementView.Search_Reimbursement(business_Code)
    ReimbursementID = reimbursementView.get_ReimbursementID(1)
    assert ReimbursementID == business_Code
    original_window = driver.current_window_handle
    reimbursementView.Click_View_ReimbursementDetal(1)
    all_handles = driver.window_handles
    for handle in all_handles:
        if handle != original_window:
            driver._switch_to.window(handle)

    all_handles_old = driver.window_handles
    reimbursementView.ReimbursementDetal_click_Print()
    all_handles = driver.window_handles
    logger.info("判断打印PDF页面是否已经打开")
    assert len(all_handles) > len(all_handles_old)
    assert process.get_businessCode() == ReimbursementID
    driver.switch_to_window(all_handles[2])
    driver.closes()
    driver.switch_to_window(all_handles[1])
    time.sleep(5)
    driver.closes()
    all_handle = driver.window_handles
    time.sleep(5)
    driver.switch_to_window(all_handle[0])







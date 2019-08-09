# coding=utf-8
# !/usr/bin/env python
#===============================================================================
# Created on 2019/1/25 11:09
# author: Administrator
# @Software: PyCharm
#脚本功能描述：财务管理-报销单查看-打印
#===============================================================================
from HLY_PageObject.UI.Process import Process
from common.log import logger
from HLY_PageObject.UI.my_expense.my_expense import  My_Expense
from HLY_PageObject.UI.FinancialManagement.ReimbursementView  import   ReimbursementView
from HLY_PageObject.UI.Reimbursement import Reimbursement
import  time
from common.parameter import  GetConfigp as pa
from common.globalMap import GlobalMap
def test_FinancePrintExpenseReport(enter):
    """
    财务管理-报销单查看-打印
    :return:
    """
    glo = GlobalMap()
    pp = pa('./config/hly.config')
    driver = enter.driver
    process = Process(driver)
    my_expense = My_Expense(driver)
    reimbursementView = ReimbursementView(driver)
    my_expense.Newexpense("日常报销单-UI自动化")  # 打开固定的新建报销单页面
    time.sleep(2)
    my_expense.InputCause("日常报销单-UI自动化","日常报销单-UI自动化测试外币金额")
    my_expense.ClickNew()
    my_expense.ClickNewExpenseButton()
    my_expense.SelectExpense("大巴")
    my_expense.SelectExpense_Currency("HKD")
    # 生产的汇率是0.8,这块先改成3.9   影响的case有：test01 和test02
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
    time.sleep(2)
    ReimbursementID = reimbursementView.get_ReimbursementID(1)
    logger.info("页面的单号：%s"%ReimbursementID)
    assert  ReimbursementID==business_Code
    original_window = driver.current_window_handle
    all_handles_old = driver.window_handles
    reimbursementView.ClickPrintBut(1)
    time.sleep(2)
    all_handles = driver.window_handles
    assert len(all_handles) > len(all_handles_old)
    driver.switch_to_window(all_handles[1])
    driver.closes()
    all_handle = driver.window_handles
    driver.switch_to_window(all_handle[0])






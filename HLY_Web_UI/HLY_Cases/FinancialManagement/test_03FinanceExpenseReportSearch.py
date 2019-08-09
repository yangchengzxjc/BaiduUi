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
from HLY_Elements.FinancialManagement  import  ReimbursementView_element
import  time,re
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
    driver.get('%s://%s%s' % (glo.get("Webprotocol"),glo.get('Webhost'), pp.getoption("BASIC", "ReimbursementView")))
    logger.info("查看默认界面查找出的数据条数")
    time.sleep(5)
    co = driver.get_element(ReimbursementView_element.Search_result, s=True, timeout=2)
    num0 = re.findall('\d+', co[0].text)[0]
    logger.info(str(num0))
    time.sleep(3)
    driver.click(ReimbursementView_element.Search)
    c1 = driver.get_element(ReimbursementView_element.Search_result, s=True, timeout=3)
    num1 = re.findall('\d+', c1[0].text)[0]
    logger.info(str(num1))
    assert str(num1) == str(num0)








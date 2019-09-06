# coding=utf-8
# !/usr/bin/env python
#===============================================================================
# Created on 2019/1/25 11:09
# author: Administrator
# @Software: PyCharm
#脚本功能描述：财务管理-报销单查看-打印
#===============================================================================
from HLY_Elements.BudgetElements import BudgetElement
from HLY_PageObject.UI.BudgetObject.Budget import  Budget

from HLY_PageObject.UI.Reimbursement import Reimbursement
from common.parameter import GetConfigp as pa
from common.globalMap import GlobalMap
def test_BudgetBalanceInquiry(enter):
    """
    财务管理-报销单查看-打印
    :return:
    """
    glo = GlobalMap()
    pp = pa('./config/hly.config')
    driver=enter.driver
    reimbursement = Reimbursement(driver)
    budget = Budget(driver)
    budget.Click_Budget()

    budget.Click_BudgetBalanceInquiry()
    assert reimbursement.get_elements_text(0, reimbursement.get_origin_xpath(glo.get("version"))) == glo.get("version")

    assert reimbursement.get_elements_text(0, reimbursement.get_origin_xpath(glo.get("struct"))) == glo.get("struct")

    assert reimbursement.get_elements_text(0, reimbursement.get_origin_xpath(glo.get("scene"))) == glo.get("scene")

    assert reimbursement.get_elements_text(0, reimbursement.get_origin_xpath(glo.get("year"))) == glo.get("year")
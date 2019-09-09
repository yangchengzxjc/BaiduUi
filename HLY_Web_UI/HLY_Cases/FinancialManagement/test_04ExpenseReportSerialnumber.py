# coding=utf-8
# !/usr/bin/env python
#===============================================================================
# Created on 2019/1/25 11:09
# author: Administrator
# @Software: PyCharm
#脚本功能描述：财务管理-报销单查看-查看明细
#===============================================================================
from HLY_Elements.FinancialManagement.ReimbursementView_element import delete_time, more, second_page, order_number, \
    display_number
from HLY_PageObject.UI.Reimbursement import Reimbursement
from common.log import logger
from config.api_urls import reimbursement_find


def test_FinanceViewExpenseReport(enter):
    """
    :param:enteraward: 提前进入新中控
    :param:SwitchingCompany: 提前切换公司，进去公司模式下
    :return:财务管理-报销单查看-提交日期选空-状态选已付款-点击搜索-选择200条/页-点击第二页
    """
    driver = enter.driver
    reimbursement = Reimbursement(driver)
    reimbursement.get_url(reimbursement_find)
    driver.click(delete_time)
    logger.info("删除提交日期")
    # reimbursement.get_elements_click(1, more)
    driver.click(reimbursement.get_parent_xpath("已付款"))
    driver.click(reimbursement.get_parent_xpath("搜 索"))
    logger.info("点击搜索")
    driver.click(second_page, timeout=1)
    logger.info("点击第二页")
    assert driver.get_text(order_number) == "21"
    logger.info("10条数据的编号：%s" % driver.get_text(order_number))
    driver.click(display_number, timeout=2)
    driver.click(reimbursement.get_xpath("200 条/页"))
    logger.info("修改每页展示的数量")
    driver.click(second_page)
    assert driver.get_text(order_number) == "201"
    logger.info("200条数据的编号：%s" % driver.get_text(order_number))
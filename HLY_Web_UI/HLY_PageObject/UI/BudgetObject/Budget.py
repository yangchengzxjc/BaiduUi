# coding=utf-8
# !/usr/bin/env python
#===============================================================================
# Created on 2019/1/8 10:54
# author: Administrator
# @Software: PyCharm
#脚本功能描述：关于测试环境的配置
#===============================================================================
from HLY_Elements.BudgetElements    import  BudgetElement
from common.parameter import  GetConfigp as pa
from common.globalMap import GlobalMap
from common.log import logger
from main import glo
import  time
class Budget(object):
    def __init__(self,driver):
        """
        初始化类
        :param driver 实例化时候需要将driver传进:
        """
        self.driver=driver
        self.glo = GlobalMap()
        pass
    def Click_Budget(self):
        """
        点击预算菜单
        :param :
        :return:
        """
        logger.info("点击菜单预算余额查询")
        self.driver.click(BudgetElement.Budget,timeout=3)
        time.sleep(3)
        self.driver.get('%s://%s/main/budget/budget-balance' % (glo.get("Webprotocol"), glo.get("Webhost")))
        time.sleep(3)

    def Click_BudgetBalanceInquiry(self):
        """
        点击余额查询
        :param :
        :return:
        """
        pp = pa('./config/hly.config')
        logger.info("点击菜单预算余额查询")

        time.sleep(1)
        self.driver.click(BudgetElement.Budget_version_select, timeout=3)
        time.sleep(1)

        self.driver.click(BudgetElement.Budget_version.replace("aaaa",glo.get("version")), timeout=3)


        time.sleep(1)
        self.driver.click(BudgetElement.Budget_vstruct_select, timeout=3)
        time.sleep(1)
        # self.driver.click(BudgetElement.Budget_vstruct, timeout=3)
        self.driver.click(BudgetElement.Budget_vstruct.replace("aaaa", glo.get("struct")),timeout=3)


        time.sleep(1)
        self.driver.click(BudgetElement.Budget_scene_select, timeout=3)
        time.sleep(3)
        self.driver.click(BudgetElement.Budget_scene.replace("aaaa", glo.get("scene")), timeout=3)



        time.sleep(2)
        self.driver.click(BudgetElement.Budget_year_select, timeout=3)
        time.sleep(2)
        self.driver.click(BudgetElement.Budget_year.replace("aaaa", glo.get("year")), timeout=3)


        time.sleep(1)
        self.driver.click(BudgetElement.Budget_Period_start_select, timeout=3)
        time.sleep(5)
        self.driver.click(BudgetElement.Budget_Period_start.replace("aaaa", glo.get("Period")), timeout=3)



        time.sleep(2)
        self.driver.click(BudgetElement.Budget_amount_select, timeout=3)
        self.driver.click(BudgetElement.Budget_amount, timeout=3)
        time.sleep(3)
        self.driver.click(BudgetElement.Budget_Search, timeout=3)
        time.sleep(3)
        self.driver.click(BudgetElement.Budget_Amount_back, timeout=3)

        time.sleep(3)







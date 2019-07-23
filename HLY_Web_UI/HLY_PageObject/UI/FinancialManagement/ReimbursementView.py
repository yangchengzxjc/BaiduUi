# coding=utf-8
# !/usr/bin/env python
#===============================================================================
# Created on 2019/1/8 10:54
# author: Administrator
# @Software: PyCharm
#脚本功能描述：关于测试环境的配置
#===============================================================================
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from HLY_Elements.FinancialManagement   import ReimbursementView_element
from HLY_Elements.FinancialManagement.ReimbursementView_element import Search
from HLY_PageObject.API  import   apis
from selenium.webdriver import ActionChains
from common.globalMap import GlobalMap
from common.parameter import  GetConfigp
from common.log import logger
import  time

class ReimbursementView(object):
    def __init__(self,driver):
        """
        初始化类
        :param driver 实例化时候需要将driver传进:
        """
        self.driver = driver
        self.glo = GlobalMap()
        self.pa = GetConfigp('./config/hly.config')
        pass
    def Search_Reimbursement(self,business_Code=None):
        """
        报销单查看页面、搜索报销单
        :return:
        """
        logger.info("报销单查看页面、搜索报销单")
        if business_Code is not None:
            time.sleep(5)
            self.driver.find_element_by_xpath(ReimbursementView_element.ReimbursementID).click()
            time.sleep(2)
            self.driver.find_element_by_xpath(ReimbursementView_element.ReimbursementID2).send_keys(business_Code)
        time.sleep(3)
        self.driver.click(Search)
        logger.info("点击搜索按钮")
        time.sleep(2)
        self.driver.click(Search)
        logger.info("再次点击搜索按钮")


    def get_ReimbursementID(self,line,timeout=2):
        """
        获取某一行的报销单ID
        :param line 输入行号:
        :return:
        """
        logger.info("获取财务管理-报销单查看第%s行的报销单单号。" % str(line))
        time.sleep(timeout)
        xpath = ReimbursementView_element.Line_ReimbursementID.replace('aaaaaaa', str(line))
        time.sleep(timeout)
        return self.driver.find_element_by_xpath(xpath).text
        pass
    def ClickPrintBut(self,Line,timeout=2):
        """
        点击某一行的报销单打印按钮
        :param Line 行号:
        :return:
        """
        logger.info("获取财务管理-报销单点击第%s行的报销单打印按钮。" % str(Line))
        time.sleep(timeout)
        xpath = ReimbursementView_element.Line_ReimbursementPrint.replace('aaaaaaa',str(Line))
        self.driver.find_element_by_xpath(xpath).click()
    def Click_View_ReimbursementDetal(self,line,timeout=2):
        """
        点击某一行，进入报销单详情页面
        :param line:
        :return:
        """
        logger.info("财务管理-报销单点击第%s行进入报销单详情页面。" % str(line))
        time.sleep(timeout)
        xpath=ReimbursementView_element.Line_ReimbursementID.replace('aaaaaaa',str(line))
        self.driver.find_element_by_xpath(xpath).click()
        time.sleep(2)
        pass
    def ReimbursementDetal_click_Print(self):
        """
        财务管理-报销单查看-报销单详情打印按钮点击
        :return:
        """
        logger.info("点击报销单详情页面的打印按钮")
        # self.driver.click(ReimbursementView_element.Print_But,timeout=3)
        WebDriverWait(self.driver, 20, 0.5).until(EC.presence_of_element_located((By.XPATH, ReimbursementView_element.Print_But))).click()
        time.sleep(2)




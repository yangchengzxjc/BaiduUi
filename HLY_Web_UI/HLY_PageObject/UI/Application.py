# coding=utf-8
# !/usr/bin/env python
#===============================================================================
# Created on 2019/1/8 10:54
# author: Administrator
# @Software: PyCharm
#脚本功能描述：关于测试环境的配置
#===============================================================================
from HLY_Elements.travel    import  Application_Full_Control
from HLY_PageObject.API  import     apis
from selenium.webdriver import ActionChains
from common.globalMap import GlobalMap
from common.log import logger
from selenium.webdriver.common.keys import Keys
from HLY_PageObject.UI import display_element
class Application(object):
    def __init__(self,driver):
        """
        初始化类
        :param driver 实例化时候需要将driver传进:
        """
        self.driver=driver
        self.glo = GlobalMap()
        pass
    def NewApplication(self,ApplicationName):
        """
        新建申请单
        :param ApplicationName 申请单名称:
        :return:
        """
        logger.info("点击进入差旅申请单-全控件")
        NewApplication = self.driver.get_element(Application_Full_Control.NewApplication)
        ActionChains(self.driver).move_to_element(NewApplication).perform()
        import  time
        time.sleep(3)
        self.driver.click(Application_Full_Control.Travel_Application_Full_Control_But)
        time.sleep(2)
        logger.info("选择差旅时间")
        self.driver.click(Application_Full_Control.Travel_StartData)
        time.sleep(1)
        self.driver.click(Application_Full_Control.Travel_Toady)
        time.sleep(1)
        self.driver.click(Application_Full_Control.Travel_Toady)
        time.sleep(1)
        Travel=apis.get_form(101)
        logger.info(Travel)
        for x in  Travel:
            if str(x["formName"]).find(ApplicationName) !=-1:
                TravelformID=x["formOID"]
                break


        TravelformIDs=apis.get_ControlId(TravelformID)
        logger.info(TravelformIDs)

        for x in  TravelformIDs["customFormFields"]:
            if str(x["fieldName"]).find("事由") !=-1:
                TravelfieldOID_Cause=x["fieldOID"]
            if str(x["fieldName"]).find("数字") !=-1:
                TravelfieldOID_numeral=x["fieldOID"]
            if str(x["fieldName"]).find("单行输入框") !=-1:
                TravelfieldOID_Single_line_input=x["fieldOID"]
            if str(x["fieldName"]).find("多行输入框") !=-1:
                TravelfieldOID_Multi_line_input=x["fieldOID"]

        logger.info("输入事由")
        self.driver.sendkeys('xpath=>//*[@id="%s"]' % TravelfieldOID_Cause,"去北京出差")
        logger.info("选择选择框")
        self.driver.click(Application_Full_Control.choice,1)
        self.driver.click(Application_Full_Control.choice_values,1)
        logger.info("选择自定义列表")
        self.driver.click(Application_Full_Control.Customlist,1)
        self.driver.click(Application_Full_Control.Customlist_values,1)
        logger.info("输入数字控件")
        self.driver.sendkeys(Application_Full_Control.Customlist,Keys.TAB ,1)
        self.driver.sendkeys('xpath=>//*[@id="%s"]' % TravelfieldOID_numeral, "612323198505177913")
        self.driver.sendkeys('xpath=>//*[@id="%s"]' % TravelfieldOID_numeral, Keys.TAB,1)
        logger.info("选择币种")
        time.sleep(2)
        self.driver.click(Application_Full_Control.Currency)
        time.sleep(2)
        self.driver.click(Application_Full_Control.Currency_values)

        logger.info("单行输入框")
        self.driver.sendkeys('xpath=>//*[@id="%s"]' % TravelfieldOID_Single_line_input, "单行输入框")
        logger.info("多行输入框")
        self.driver.sendkeys('xpath=>//*[@id="%s"]' % TravelfieldOID_Multi_line_input, "多行输入框"+Keys.TAB)
        logger.info("请选择时间")
        self.driver.click(Application_Full_Control.Please_select_time)

        logger.info(time.strftime('%Y.%m.%d',time.localtime(time.time())))
        H=time.strftime('%H',time.localtime(time.time()))
        M=time.strftime('%M',time.localtime(time.time()))
        time.sleep(2)
        self.driver.sendkeys(Application_Full_Control.time,"11:00")
        logger.info("点击一下多行输入框")
        self.driver.click('xpath=>//*[@id="%s"]' % TravelfieldOID_Multi_line_input)
        self.driver.click(Application_Full_Control.CostCenter,1);
        time.sleep(3)
        self.driver.click(Application_Full_Control.CostCenter_choice_ok);







        time.sleep(5)



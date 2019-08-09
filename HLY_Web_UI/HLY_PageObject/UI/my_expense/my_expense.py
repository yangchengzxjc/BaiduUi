# coding=utf-8
# !/usr/bin/env python
#===============================================================================
# Created on 2019/1/8 10:54
# author: Administrator
# @Software: PyCharm
#脚本功能描述：关于测试环境的配置
#===============================================================================
from HLY_Elements.expense import elExpense
from HLY_Elements.expense.elExpense import New, Expense_bus, Currency, HKD
from HLY_Elements.expense.elReimbursement import new_expense, NewExpenseBut
from HLY_PageObject.API import apis
from selenium.webdriver import ActionChains

from HLY_PageObject.API.apis import get_jobID
from common.globalMap import GlobalMap
from common.parameter import GetConfigp
from common.log import logger
import time

class My_Expense(object):
    def __init__(self,driver):
        """
        初始化类
        :param driver 实例化时候需要将driver传进:
        """
        self.driver=driver
        self.glo = GlobalMap()
        self.pa = GetConfigp('./config/hly.config')
        pass

    def MouseMoveToNew_expense(self):
        """
        鼠标滑动至固定的报销单列表上面
        :return:
        """
        logger.info("鼠标滑动至:%s" %("新建报销单"))
        ExpenseForm = self.driver.get_element(new_expense,timeout=1)
        ActionChains(self.driver).move_to_element(ExpenseForm).perform()

    def Newexpense(self,expenseName):
        """
        点击对应的报销单新建按钮
        :param expenseName 报销单表单名称:
        :return:
        """
        logger.info("打开报销单:%s页面" % expenseName)
        fromList=apis.get_form(self.pa.getoption("BASIC","ExpenseForm"))
        for x in fromList:
            logger.info(x["formName"])
            if str(x["formName"]).find(expenseName) != -1:
                fromId=x["formOID"]
                logger.info(fromId)
                break
        jobID=get_jobID()
        logger.info("jobID是：%s" % jobID)
        url = "%s://%s/main/expense-parent-report/expense-report/new-expense-report/%s/:userOID/:applicationOID/%s" % (self.glo.get("Webprotocol"), self.glo.get("Webhost"), fromId, jobID)
        self.driver.get(url)

    def InputCause(self,expenseName,Cause):
        """
        :param Cause 报销单的事由:
        :return:
        """
        logger.info("输入报销单事由")
        fromList=apis.get_form(self.pa.getoption("BASIC","ExpenseForm"))
        for x in fromList:
            # logger.info(x["formName"])
            if str(x["formName"]).find(expenseName) != -1:
                fromId=x["formOID"]
                break
        CauseControL=apis.get_ControlId(fromId)
        for x in CauseControL["customFormFields"]:
            logger.info(x["fieldName"])
            if str(x["fieldName"]).find("事由") != -1:
                fieldOID=x["fieldOID"]
                break
        path='xpath=>//*[@id="%s"]' %(fieldOID)
        # logger.info(path)
        self.driver.sendkeys(path,Cause)
    def ClickNew(self):
        """
        点击新建报销单按钮
        :return:
        """
        logger.info("点击新建按钮")
        self.driver.click(New,timeout=1)
        pass
    def ClickNewExpenseButton(self):
        """
        报销单里点击新建费用按钮
        :return:
        """
        logger.info("报销单详情页面点击新建费用按钮")
        self.driver.click(NewExpenseBut,timeout=3)
        pass

    def SelectExpense(self,expense_name):
        """
        报销单里新建大巴类型的费用
        :return:
        """
        logger.info("报销单里新建类型的费用")
        ExpenseName = Expense_bus.replace("大巴",expense_name)
        if not self.driver.is_exist(ExpenseName):
            self.Pagescroll(ExpenseName,timeout=2)
        else:
            pass
        self.driver.click(ExpenseName,timeout=2)
        pass

    def SelectExpense_Currency(self, kind):
        """
        选择费用币种
        :return:
        """
        logger.info("选择对应的币种")
        self.driver.click(Currency,2)
        if kind =="HKD":
            self.driver.click(HKD,2)
        if kind =="USD":
            self.driver.click(elExpense.USD,2)
        pass

    def InputRate(self, rate):
        """
        输入外币汇率
        :param rate :
        :return:外币汇率
        """
        time.sleep(2)
        logger.info("输入汇率:%s" % ( rate ))
        self.driver.sendkeys(elExpense.Expense_Amount_input,text=rate,clear=True, timeout=2)
        pass
    def InputExpense_Amount(self,amount):
        """
        报销单里新建费用，并输入大巴类型的金额
        :return:
        """
        logger.info("报销单里新建费用，并输入大巴类型的金额")
        self.driver.sendkeys(css=elExpense.Expense_Amount,text=amount,clear=True, timeout=2)
        pass
    def ClickExpenseSave(self,timeout):
        """
        报销单里，新建费用保存按钮
        :return:
        """
        logger.info("报销单里，新建费用保存按钮")
        self.driver.click(elExpense.Save_Expense, timeout=timeout)
        pass

    def Pagescroll(self,xpath,timeout):
        """
        报销单详情页面，滚动页面
        :return:
        """
        logger.info("报销单详情页面，滚动页面")
        # 第一种：
        #
        # # 滑到底部
        #
        # js = "var q=document.documentElement.scrollTop=100000"
        #
        # driver.execut_script(js)
        #
        # 目前在firefox，chrome上验证都是可以跑通的
        #
        # # 滑动到顶部
        #
        # js = "var q=document.documentElement.scrollTop=0"
        #
        # driver.execut_script(js)
        #
        # 第二种
        #
        # # 滑到底部
        #
        # js = "window.scrollTo(0,document.body.scrollHeight)"
        #
        # driver.execute_script(js)
        #
        # 目前在firefox，chrome上验证都是可以跑通的
        #
        # # 滑动到顶部
        #
        # js = "window.scrollTo(0,0)"
        #
        # driver.execute_script(js)
        #
        # scrollHeight
        # 获取对象的滚动高度。 
        # scrollLeft
        # 设置或获取位于对象左边界和窗口中目前可见内容的最左端之间的距离。 
        # scrollTop
        # 设置或获取位于对象最顶端和窗口中可见内容的最顶端之间的距离。 
        # scrollWidth
        import  time
        time.sleep(timeout)
        # js = "var q=document.documentElement.scrollTop=10000"
        # el = self.driver.get_element( 'xpath=>//*[@id="app"]/div/div[2]/div[2]/div/div[2]/div[2]/div/div[2]/div/div[3]/div/div/div/div/div[2]/div/table/tbody/tr/td[9]/a');
        el = self.driver.get_element(xpath)
        self.driver.execute_script("arguments[0].scrollIntoView();", el)
        pass

    # def Choose_All_expense(self,timeout):
    #     """
    #     选择所有的费用
    #     :return:
    #     """
    #     import  time
    #     time.sleep(timeout)
    #     logger.info("报销单里选择所有的费用")
    #     els = self.driver.find_elements_by_xpath('//input[@type="checkbox"]')
    #     els[0].click()
    #
    #
    #     # els=self.driver.get_element(elExpense.Choose_All_expense,s=True)
    #     # els[0].click()
    #     pass

    def Choose_All_expense(self, timeout):
        """
        选择所有的费用
        :return:
        """
        import time
        time.sleep(timeout)
        logger.info("报销单里选择所有的费用")
        els = self.driver.find_elements_by_xpath(elExpense.Choose_All_expense)
        els[0].click()
        pass
    def summit_expense(self,timeout=3):
        """
        提交报销单
        :return:
        """
        self.driver.click(elExpense.submit_expense,timeout=timeout)
    def getDocumentAmount(self):
        """
        获取单据金额
        :return:
        """
        return self.driver.find_element_by_xpath(elExpense.DocumentAmount).text
        pass
    def getPaymentAmount(self):
        """
        获取单据金额
        :return:
        """
        return  self.driver.find_element_by_xpath(elExpense.PaymentAmount).text
        pass











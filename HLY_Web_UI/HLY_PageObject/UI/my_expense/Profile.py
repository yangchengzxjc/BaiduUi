from time import sleep
from HLY_Elements.profile.elprofile import desplay_button, save_button, not_desplay, close, Iknow, profile_search
from HLY_PageObject.UI.Reimbursement import Reimbursement
from common.globalMap import GlobalMap
from common.log import logger
from common.parameter import  GetConfigp
from config.api_urls import profile_url


class Profile:
    """
       这个是一个profile的类，用来设置各种profile
       """
    def __init__(self,driver):
        """
        初始化类
        :param driver 实例化时候需要将driver传进:
        """
        self.driver=driver
        self.glo = GlobalMap()
        self.pa = GetConfigp('./config/hly.config')
        pass


    def enter_profile(self):
        """
        进入profile设置
        :return:
        """
        url = "%s://%s%s" % (self.glo.get("Webprotocol"), self.glo.get("Webhost"), profile_url)
        logger.info("profile的url:%s" % url)
        self.driver.get(url)
        sleep(2)
        if self.driver.is_exist(close):
            self.driver.click(close)
        else:
            pass
        sleep(2)
        if self.driver.is_exist(Iknow):
            self.driver.click(Iknow)
        else:
            pass

    def get_xpath(self,name):
        return '//*[text()="%s"]'%name

    def print_open(self):
        """
        打开审核通过后显示打印按纽
        :return:
        """
        self.enter_profile()
        reimbursement = Reimbursement(self.driver)
        reimbursement.get_elements_click(0,self.get_xpath("打印"))
        #输入profile
        self.driver.sendkeys(profile_search, "web.report.expense.print.audit.disabled")
        if self.driver.is_exist(reimbursement.get_xpath("添加此项配置")):
            self.driver.click(reimbursement.get_xpath("添加此项配置"),timeout=2)
        else:
            pass
        self.driver.click(desplay_button,timeout=2)
        self.driver.click(save_button,timeout=2)
        sleep(2)
        self.driver.refresh()


    def print_close(self):
        """
        审批过后不显示打印按钮
        :return:
        """
        self.enter_profile()
        reimbursement = Reimbursement(self.driver)
        reimbursement.get_elements_click(0, self.get_xpath("打印"))
        # 输入profile
        self.driver.sendkeys(profile_search,"web.report.expense.print.audit.disabled")
        if self.driver.is_exist(reimbursement.get_xpath("添加此项配置")):
            self.driver.click(reimbursement.get_xpath("添加此项配置"),timeout=2)
        else:
            pass
        self.driver.click(not_desplay,timeout=2)
        self.driver.click(save_button,timeout=2)
        sleep(2)
        self.driver.refresh()

    def card_open(self):
        """
        开启报销单提交时校验付款行的「收款方银行账号」、「收款方户名」必填!的porfile
        :return:
        """
        self.enter_profile()
        reimbursement = Reimbursement(self.driver)
        reimbursement.get_elements_click(1, self.get_xpath("报销单"))
        self.driver.sendkeys(profile_search, "report.property.card.required")
        if self.driver.is_exist(reimbursement.get_xpath("添加此项配置")):
            self.driver.click(reimbursement.get_xpath("添加此项配置"),timeout=2)
        else:
            pass
        self.driver.click(reimbursement.get_xpath("启用"), timeout=2)
        self.driver.click(save_button, timeout=2)
        sleep(2)
        self.driver.refresh()

    def card_close(self):
        """
        开启报销单提交时校验付款行的「收款方银行账号」、「收款方户名」必填!的porfile
        :return:
        """
        self.enter_profile()
        reimbursement = Reimbursement(self.driver)
        reimbursement.get_elements_click(1, self.get_xpath("报销单"))
        self.driver.sendkeys(profile_search, "report.property.card.required")
        if self.driver.is_exist(reimbursement.get_xpath("添加此项配置")):
            self.driver.click(reimbursement.get_xpath("添加此项配置"),timeout=2)
        else:
            pass
        self.driver.click(reimbursement.get_xpath("不启用"), timeout=2)
        self.driver.click(save_button, timeout=2)
        sleep(2)
        self.driver.refresh()





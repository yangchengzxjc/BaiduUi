from time import sleep

from selenium.webdriver.support.wait import WebDriverWait

from HLY_Elements.expense.elFinanciaCheck import bill_status
from HLY_PageObject.API.apis import get_account
from common.globalMap import GlobalMap
from common.log import logger


class Reimbursement():
    def __init__(self,driver):
        """
        初始化类
        :param driver 实例化时候需要将driver传进:
        """
        self.driver = driver
        wait = WebDriverWait(driver,10)
        self.glo = GlobalMap()
        pass

    def get_elements_click(self, number, element, timeout=4):
        sleep(timeout)
        els = self.driver.find_elements_by_xpath(element)
        logger.info("元素已找到")
        logger.info("%s，找到个数为:%s" % (element, len(els)))
        els[number].click()

    def get_elements_attribute(self,number,element,value,timeout=2):
        sleep(timeout)
        els=self.driver.find_elements_by_xpath(element)
        logger.info("元素个数：%s" % len(els))
        return els[number].get_attribute(value)

    def get_elements_text(self,number,element,timeout=2):
        sleep(timeout)
        els = self.driver.find_elements_by_xpath(element)
        return els[number].text

    def get_elements_sendKey(self, number, element, text, timeout=2):

        sleep(timeout)
        els = self.driver.find_elements_by_xpath(element)
        self.get_element_clear(number,element)
        logger.info("%s的元素的个数是：%s"% (text, len(els)))
        els[number].send_keys(text)

    def get_element_clear(self, number, element, timeout=3):
        sleep(timeout)
        els = self.driver.find_elements_by_xpath(element)
        els[number].clear()

    def Pagescroll(self, xpath, timeout=1):
        sleep(timeout)
        el = self.driver.get_element(xpath)
        self.driver.execute_script("arguments[0].scrollIntoView();", el)
        pass

    def get_xpath(self, name):
        return 'xpath=>//*[text()="%s"]'%name

    def get_parent_xpath(self,name):
        '''
        获得元素的父节点
        :param name:
        :return:
        '''
        return 'xpath=>//*[text()="%s"]/..'%name

    def get_origin_parent_xpath(self,name):
        '''
        获得原生的自己父节点元素
        :param name:
        :return:
        '''
        return '//*[text()="%s"]/..'%name

    def get_origin_xpath(self,name):
        '''
        获得原生的自己
        :param name:
        :return:
        '''
        return '//*[text()="%s"]'%name

    def get_bill_status(self):
        sleep(3)
        return self.driver.find_element_by_xpath(bill_status).text

    def get_url(self, acturl):
        """
        请求的url
        :param acturl:
        :return:
        """
        sleep(4)
        url = '%s://%s%s' % (self.glo.get("Webprotocol"), self.glo.get("Webhost"), acturl)
        self.driver.get(url)

    def get_department(self):
        return get_account()["departmentName"]

    def replace_element(self, character, old_word, new_word):
        return character.replace(old_word, new_word)

    def enter_consumption(self):
        url = '%s://%s/main/third-consumption'%(self.glo.get("Webprotocol"),self.glo.get("Webhost"))
        self.driver.get(url)



from time import sleep
from selenium.webdriver.common.keys import Keys

from HLY_Elements.expense.elApprove import know_prod, business_code, business_codes, approve, reason, refuse, \
    approve_process
from HLY_Elements.expense.elFinanciaCheck import search_approve, passed, new_amount, save_amount, \
    get_businessCode, cancel, confirm, continue_submit_text, continue_submit, input_expenses
from HLY_Elements.expense.elExpense import Financial_search, save, input_expense, scroll_locate, departure_prod, \
    Destination_prod, destinations_prod
from HLY_Elements.expense.elReimbursement import new_expense, new_other_expense, no_card_user, withdraw_confirm
from HLY_Elements.expense.expense_type import amount_input, new_apportion, apportion_department2, \
    apportion_department1
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense
from common.globalMap import GlobalMap
from common.log import logger
from config.api_urls import approve_url, invoice_verify, reimbursement_look


class Process(Reimbursement):
    """
    这是一个操作类对报销单的操作
    """
    def __init__(self, driver):
        """
        初始化类
        :param driver:
        """
        self.glo = GlobalMap()
        self.driver = driver
        Reimbursement.__init__(self, driver)
        self.my_expense = My_Expense(self.driver)
        pass

    def create_expense(self, money, expense_name, currency="HKD", flag=False):
        """
        新建一笔费用:if flag =false 则不需要外币 等于true则创建外币费用
        :return:
        """
        my_expense = My_Expense(self.driver)
        self.driver.click(new_expense, timeout=2)
        logger.info("新建费用")
        sleep(2)
        my_expense.SelectExpense(expense_name)
        sleep(3)
        if flag == False:
            logger.info("money:%s" % money)
            self.driver.find_element_by_xpath(input_expenses).clear()
            sleep(2)
            self.driver.find_element_by_xpath(input_expenses).send_keys(money)
            logger.info("输入金额")
            pass
        else:
            my_expense.SelectExpense_Currency(currency)
            my_expense.InputRate("0.8")
            self.driver.find_element_by_xpath(input_expenses).clear()
            sleep(2)
            self.driver.find_element_by_xpath(input_expenses).send_keys(money)
            logger.info("输入金额")
            pass
        # 保存新建的费用
        sleep(2)
        self.driver.click(save)
        sleep(10)
        logger.info("新建费用完成")

    def create_other_expense(self, money, expense_name):
        """
        新建他人费用
        :return:
        """
        my_expense = My_Expense(self.driver)
        sleep(2)
        self.driver.click(new_other_expense)
        # 选择费用归属人
        logger.info("点击新建他人费用")
        self.get_elements_click(7, self.get_origin_xpath("请选择"))
        logger.info("选择员工")
        self.driver.click(no_card_user, timeout=3)
        # 确定
        self.driver.click(self.get_parent_xpath("确 定"), timeout=2)
        my_expense.SelectExpense(expense_name)
        sleep(3)
        logger.info("money:%s" % money)
        self.driver.find_element_by_xpath(input_expenses).clear()
        sleep(2)
        self.driver.find_element_by_xpath(input_expenses).send_keys(money)
        # 保存新建的费用
        sleep(2)
        self.driver.click(save)
        sleep(5)
        logger.info("新建他人费用完成")

    def create_all_expense(self, money, expense_name, departures, Destinations):
        """
        新建有控件的一笔费用
        :return:
        """
        my_expense = My_Expense(self.driver)
        sleep(4)
        self.driver.click(new_expense)
        logger.info("点击新建费用")
        my_expense.SelectExpense(expense_name)
        self.driver.clear(input_expense)
        self.driver.sendkeys(input_expense, money)
        logger.info("输入金额")
        sleep(2)
        my_expense.Pagescroll(scroll_locate, timeout=1)
        self.driver.sendkeys(departure_prod, departures, timeout=2)
        logger.info("输入出发地")
        self.driver.sendkeys(Destination_prod, Destinations, timeout=2)
        logger.info("输入目的地")
        sleep(1)
        self.driver.find_element_by_xpath(destinations_prod).send_keys(Keys.TAB)
        # 保存新建的费用
        self.driver.click(save, timeout=3)
        logger.info("新建费用完成")

    def approve(self, businessCode):
        """
        审批通过
        :return:
        """
        my_expense = My_Expense(self.driver)
        sleep(3)
        self.get_url(approve_url)
        if self.driver.is_exist(know_prod):
            self.driver.click(know_prod)
        else:
            pass
        sleep(5)
        businesscode = business_code.replace("ER", businessCode)
        businesscodes = business_codes.replace("ER", businessCode)
        self.driver.sendkeys(search_approve, businessCode)
        logger.info("审批搜索单号")
        sleep(1)
        my_expense.Pagescroll(businesscodes, timeout=2)
        sleep(1)
        self.get_elements_click(0, businesscode)
        self.driver.click(approve, timeout=2)
        logger.info("审批通过")

    def enter_approve(self,businessCode):
        """
        进入审批的页面并且滑动到查看出现
        :return:
        """
        sleep(3)
        self.get_url(approve_url)
        if self.driver.is_exist(know_prod):
            self.driver.click(know_prod)
        else:
            pass
        logger.info("进入审批页面")
        sleep(3)
        businesscode = business_code.replace("ER",businessCode)
        businesscodes = business_codes.replace("ER",businessCode)
        self.driver.sendkeys(search_approve,businessCode)
        logger.info("输入搜索的单号")
        sleep(1)
        self.my_expense.Pagescroll(businesscodes, timeout=2)
        sleep(1)
        self.get_elements_click(0, businesscode)
        sleep(3)
        self.my_expense.Pagescroll(self.get_xpath("查看"),timeout=1)

    def find_approve_bill(self,businessCode):
        """
        查看审批的单据是否存在，并且返回单号
        :return:
        """
        my_expense = My_Expense(self.driver)
        self.get_url(approve_url)
        if self.driver.is_exist(know_prod):
            self.driver.click(know_prod)
        else:
            pass
        sleep(5)
        # businesscode = business_code.replace("ER", businessCode)
        businesscodes = business_codes.replace("ER", businessCode)
        self.driver.sendkeys(search_approve, businessCode)
        sleep(1)
        my_expense.Pagescroll(businesscodes, timeout=2)
        sleep(1)
        return business_codes.replace("ER",businessCode)

    def Financial_audit(self, businessCode):
        """
        财务管理-单据审核-审核通过
        :return:
        """
        sleep(4)
        self.get_url(invoice_verify)
        sleep(3)
        logger.info("进入单据审核")
        businesscode = business_code.replace("ER", businessCode)
        self.my_expense.Pagescroll(Financial_search, timeout=2)
        sleep(1)
        self.driver.find_element_by_xpath(businesscode).click()
        self.driver.click(passed,timeout=5)
        logger.info("单据审核通过")

    def enter_Financial_audit(self, businessCode):
        """
        进入单据审核的页面
        :return:
        """
        sleep(4)
        self.get_url(invoice_verify)
        businesscode = business_code.replace("ER", businessCode)
        # businesscodes = business_codes.replace("ER", businessCode)
        sleep(4)
        self.my_expense.Pagescroll(Financial_search, timeout=1)
        sleep(2)
        self.driver.find_element_by_xpath(businesscode).click()
        self.my_expense.Pagescroll(self.get_xpath("查看"), timeout=4)

    def change_amount(self, businessCode):
        """
        财务修改费用
        :param businessCode:
        :return:
        """
        sleep(4)
        self.get_url(invoice_verify)
        businesscode = business_code.replace("ER", businessCode)
        sleep(4)
        self.my_expense.Pagescroll(Financial_search, timeout=2)
        self.driver.find_element_by_xpath(businesscode).click()
        self.my_expense.Pagescroll(self.get_xpath("总金额"), timeout=1)
        self.driver.click(self.get_parent_xpath("大巴"), timeout=3)
        sleep(3)
        self.driver.click(self.get_xpath("核定金额"))
        logger.info("点击核定金额")
        sleep(2)
        self.driver.clear(new_amount)
        self.driver.sendkeys(new_amount,10,timeout=2)
        self.driver.click(save_amount)
        logger.info("财务修改费用完成")

    def enter_invoice_verify(self,businessCode):
        sleep(4)
        self.get_url(invoice_verify)
        businesscode = business_code.replace("ER", businessCode)
        # businesscodes = business_codes.replace("ER", businessCode)
        sleep(4)
        self.my_expense.Pagescroll(Financial_search, timeout=2)
        self.driver.find_element_by_xpath(businesscode).click()

    def Financial_audit_refuse(self,businessCode):
        """
        财务驳回
        :param businessCode:
        :return:
        """
        self.get_url(invoice_verify)
        sleep(4)
        businesscode = business_code.replace("ER", businessCode)
        businesscodes = business_codes.replace("ER", businessCode)
        self.my_expense.Pagescroll(Financial_search, timeout=2)
        sleep(1)
        # self.driver.click(businesscode)
        self.driver.find_element_by_xpath(businesscode).click()
        self.driver.sendkeys(reason,"金额不符")
        logger.info("输入驳回原因")
        self.driver.click(refuse, timeout=3)
        logger.info("财务驳回报销单")

    def approve_refuse(self, businessCode, flag =True):
        """
        审批驳回
        :param businessCode:
        :return:
        """
        sleep(3)
        self.get_url(approve_url)
        if self.driver.is_exist(know_prod):
            self.driver.click(know_prod)
        else:
            pass
        sleep(5)
        businesscode = business_code.replace("ER", businessCode)
        businesscodes = business_codes.replace("ER", businessCode)
        self.driver.sendkeys(search_approve, businessCode)
        logger.info("搜索驳回的单号")
        sleep(1)
        self.my_expense.Pagescroll(businesscodes, timeout=2)
        sleep(1)
        if flag == True:
            self.get_elements_click(0, businesscode)
            self.driver.sendkeys(reason, "金额不符")
            self.driver.click(refuse, timeout=3)
            sleep(4)
            logger.info("审批驳回")
        else:
            self.get_elements_click(0, businesscode)


    def open_reimbursement(self, businessCode):
        """
        进入报销单
        :param businessCode:
        :return:
        """
        sleep(4)
        self.get_url(reimbursement_look)
        businesscode = self.replace_element(business_code, "ER", businessCode)
        sleep(2)
        self.driver.find_element_by_xpath(businesscode).click()
        sleep(3)

    def get_businessCode(self):
        """
        获取报销单号
        :return:
        """
        text = self.driver.find_element_by_xpath(get_businessCode).text
        logger.info(text)
        return text.split("：")[1]

    def enter_approve_process(self):
        """
        进入审批进度
        :return:
        """
        self.driver.click(approve_process)

    def find_approve_process(self, element):
        return self.driver.find_element_by_xpath(element).text

    def cancel_bill(self, timeout=3):
        self.driver.click(cancel, timeout=timeout)
        self.driver.click(confirm)

    def continue_submit(self, timeout=5):
        """
        判断是否有借款，继续提交
        :return:
        """
        sleep(timeout)
        if self.driver.is_exist(continue_submit_text):
            els = self.driver.find_elements_by_xpath(continue_submit)
            logger.info(len(els))
            print(len(els))
            els[len(els)-1].click()
        else:
            pass

    def withdraw(self, bussinessCode, timeout=5):
        """
        报销单撤回
        :return:
        """
        sleep(timeout)
        self.open_reimbursement(bussinessCode)
        self.driver.click(self.get_parent_xpath("撤 回"))
        self.driver.click(self.get_parent_xpath("确 定"))
        logger.info("点击撤回，并确定")

    def new_apporation_expense(self, amount, approation_amount, department, currency="HKD", rate ="0.8", expense_name="分摊费用类型", flag =False):
        """
        新建分摊费用类型,按照部门分摊自己的部门和测试部门分摊
        :param apportion_department:分摊部门
        :param amount:金额
        :param approation_amount:第一个公司的分摊金额
        :param expense_name:费用类型
        :param apportion_line: 分滩行
        :return:
        """
        # 创建本位币分摊费用50.23元
        my_expense = My_Expense(self.driver)
        self.driver.click(new_expense, timeout=2)
        logger.info("点击新建")
        sleep(2)
        my_expense.SelectExpense(expense_name)
        logger.info("选择分摊费用类型")
        if not flag:
            sleep(3)
            self.driver.find_element_by_xpath(input_expenses).clear()
            sleep(2)
            self.driver.find_element_by_xpath(input_expenses).send_keys(amount)
            logger.info("输入金额")
            sleep(2)
            self.Pagescroll(self.get_xpath("费用分摊"), timeout=1)
            logger.info("滑动到分摊行")
            self.get_element_clear(1, amount_input)
            self.get_elements_sendKey(1, amount_input, approation_amount)
            logger.info("修改第一个分摊行金额")
            # 点击新增分摊
            self.get_elements_click(0, new_apportion)
            logger.info("新增分摊")
            # 选择公司
            self.driver.click(apportion_department1, timeout=2)
            logger.info("点击选择部门")
            self.driver.click(self.get_parent_xpath(department), timeout=2)
            logger.info("选择部门-%s" % department)
            # 点击确定
            sleep(2)
            self.get_elements_click(0, self.get_origin_parent_xpath("确 定"))
            sleep(3)
            logger.info("点击确定")
            logger.info("分摊金额是：%s %s" % (self.get_elements_attribute(1, amount_input, "value"),
                                         self.get_elements_attribute(3, amount_input, "value")))
            assert (round(float(self.get_elements_attribute(1, amount_input, "value")), 2) + round(float(self.get_elements_attribute(3, amount_input, "value")), 2)) == round(float(amount), 2)
        else:
            my_expense.SelectExpense_Currency(currency)
            logger.info("选择币种，默认港币")
            my_expense.InputRate(rate)
            sleep(3)
            self.driver.find_element_by_xpath(input_expenses).clear()
            sleep(2)
            self.driver.find_element_by_xpath(input_expenses).send_keys(amount)
            logger.info("输入金额")
            sleep(2)
            self.Pagescroll(self.get_xpath("费用分摊"), timeout=1)
            logger.info("滑动到分摊行")
            self.get_element_clear(2, amount_input)
            self.get_elements_sendKey(2, amount_input, approation_amount)
            logger.info("修改第一个分摊行金额")
            # 点击新增分摊
            self.get_elements_click(0, new_apportion)
            logger.info("新增分摊")
            # 选择公司
            self.driver.click(apportion_department2, timeout=2)
            logger.info("点击选择公司")
            self.driver.click(self.get_parent_xpath(department), timeout=2)
            logger.info("选择部门-%s" % department)
            # 点击确定
            sleep(2)
            self.get_elements_click(0, self.get_origin_parent_xpath("确 定"))
            logger.info("点击确定")
            logger.info("分摊金额是：%s %s" % (self.get_elements_attribute(2, amount_input, "value"),
                                         self.get_elements_attribute(4, amount_input, "value")))
            assert round((float(self.get_elements_attribute(2, amount_input, "value")) + float(
                self.get_elements_attribute(4, amount_input, "value"))), 2) == round(float(amount), 2)
        self.driver.click(save)
        logger.info("保存费用")
        sleep(4)
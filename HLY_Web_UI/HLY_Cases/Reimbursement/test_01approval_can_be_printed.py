from time import sleep

from HLY_Elements.expense.elApprove import business_code, approved_Print
from HLY_Elements.expense.elFinanciaCheck import Printing_button, \
    Printing_button1
from HLY_Elements.expense.elReimbursement import cause, submit_expense
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.Profile import Profile
from HLY_PageObject.UI.my_expense.my_expense import My_Expense
from common.log import logger


def test_approval_can_be_printed(enter):
    """
    报销单审核通过后可进行打印
    需求：4828审核后是否长打印按钮受profile控制：显示按钮打开
    :return:
    """
    driver = enter.driver
    reimbursement = Reimbursement(driver)
    process = Process(driver)
    # 选择报销单类型(日常报销单)
    my_expense = My_Expense(driver)
    profile = Profile(driver)
    sleep(2)
    profile.print_open()
    sleep(3)
    my_expense.Newexpense("日常报销单-UI自动化")
    sleep(2)
    reimbursement.get_elements_sendKey(1, cause, "test报销单审核后可打印")
    # 新建报销单
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    # 新建费用
    sleep(5)
    process.create_expense(200, "大巴")
    business_Code = process.get_businessCode()
    # 提交报销单
    sleep(3)
    driver.click(submit_expense)
    logger.info("提交报销单")
    sleep(3)
    process.continue_submit()
    sleep(5)
    businesscode = business_code.replace("ER", business_Code)
    driver.find_element_by_xpath(businesscode).click()
    assert not driver.is_exist(approved_Print)
    sleep(2)
    # 审批报销单
    process.approve(business_Code)
    sleep(5)
    # 审核通过
    process.Financial_audit(business_Code)
    process.open_reimbursement(business_Code)
    sleep(5)
    a=driver.find_element_by_xpath(Printing_button1).text
    assert driver.is_exist(Printing_button)
    assert a == "打 印"
















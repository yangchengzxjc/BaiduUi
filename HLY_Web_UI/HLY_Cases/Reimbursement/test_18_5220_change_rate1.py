from time import sleep
from selenium.common.exceptions import WebDriverException
from HLY_Elements.expense.elFinanciaCheck import passed
from HLY_Elements.expense.elApprove import approve_reason, approve
from HLY_Elements.expense.elExpense import submit_expense
from HLY_Elements.expense.elTravel_approval_can_be_printed import rate_input, rate_save, approve_rate, warning_remind, \
    Finance_rate
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense
from common.log import logger


def test_change_rate(enter):
    """
    需求：财务核定金额修改汇率允许改大 审批修改小于警告汇率
    :param enter:
    :return:
    """
    driver = enter.driver
    my_expense = My_Expense(driver)
    process = Process(driver)
    reimbursement = Reimbursement(driver)
    my_expense.Newexpense("日常报销单-UI自动化")  # 打开固定的新建报销单页面
    my_expense.InputCause("日常报销单-UI自动化", "测试财务核定金额修改汇率允许改动")
    my_expense.ClickNew()
    my_expense.ClickNewExpenseButton()
    my_expense.SelectExpense("大巴")
    my_expense.SelectExpense_Currency("USD")
    my_expense.InputExpense_Amount("100")
    my_expense.ClickExpenseSave(2)
    business_Code = process.get_businessCode()
    sleep(5)
    driver.click(submit_expense)
    process.continue_submit()
    # 进入到审批页面
    process.enter_approve(business_Code)
    driver.sendkeys(approve_reason, "审批通过", timeout=4)
    # 点击查看按钮
    # case1(输入小于警告的)
    driver.click(reimbursement.get_xpath("查看"))
    # 点击核定金额
    logger.info("点击查看")
    driver.click(reimbursement.get_xpath("核定金额"), timeout=2)
    # 输入变小的汇率
    logger.info("点击核定金额")
    driver.sendkeys(rate_input, "6.1000", timeout=2)
    logger.info("输入汇率")
    driver.click(rate_save)
    logger.info("保存汇率")
    assert driver.get_text(approve_rate, timeout=5) == "6.1000"
    assert driver.get_text(approve_reason) == "审批通过"
    # case3（报销单审批时更改汇率超过警告容差，小于禁止容差：警告提示，允许保存修改）
    driver.click(reimbursement.get_xpath("查看"))
    # 点击核定金额
    driver.click(reimbursement.get_xpath("核定金额"), timeout=2)
    # 输入变小的汇率
    driver.sendkeys(rate_input, "7.5000", timeout=3)
    assert 'warning' in driver.getAttribute(warning_remind, "class")
    driver.click(rate_save)
    assert driver.get_text(approve_rate, timeout=5) == "7.5000"
    # 报销单审批通过

    # case5 报销单审批更改汇率超过禁止容差：禁止提示,【保存】按钮置灰;
    driver.click(reimbursement.get_xpath("查看"))
    # 点击核定金额
    driver.click(reimbursement.get_xpath("核定金额"), timeout=2)
    # 输入的汇率
    driver.sendkeys(rate_input, "8.5000", timeout=2)
    assert 'error' in driver.getAttribute(warning_remind, "class")
    try:
        driver.click(rate_save)
    except (WebDriverException, TimeoutError) as e:
        assert True
        pass
    finally:
        sleep(3)
        driver.sendkeys(rate_input, "7.5000")
        driver.click(rate_save)
    sleep(3)
    driver.click(approve, timeout=2)
    # 进入财务审核
    process.enter_Financial_audit(business_Code)
    driver.sendkeys(approve_reason, "审核通过", timeout=4)
    # 点击费用查看
    # case2（输入小于警告的）
    driver.click(reimbursement.get_xpath("查看"), timeout=2)
    driver.click((reimbursement.get_xpath("核定金额")), timeout=2)
    driver.sendkeys(rate_input, "6.1001", timeout=2)
    driver.click(rate_save)
    sleep(5)
    assert driver.get_text(Finance_rate) == "6.1001"
    assert driver.get_text(approve_reason, timeout=1)
    # case4(财务审核更改汇率超过警告容差，小于禁止容差：警告提示，允许保存修改)
    driver.click(reimbursement.get_xpath("查看"), timeout=2)
    sleep(2)
    driver.click((reimbursement.get_xpath("核定金额")))
    sleep(3)
    driver.sendkeys(rate_input, "7.5000")
    assert 'warning' in driver.getAttribute(warning_remind, "class")
    driver.click(rate_save)
    sleep(2)
    assert driver.get_text(Finance_rate, timeout=5) == "7.5000"

    # case 6 财务审核更改汇率超过禁止容差：禁止提示，【保存】按钮置灰；
    driver.click(reimbursement.get_xpath("查看"), timeout=2)
    # 点击核定金额
    sleep(2)
    driver.click((reimbursement.get_xpath("核定金额")))
    # 输入的汇率
    driver.sendkeys(rate_input, "8.5000", timeout=2)
    assert 'error' in driver.getAttribute(warning_remind, "class")
    try:
        driver.click(rate_save)
    except (WebDriverException, TimeoutError) as e:
        assert True
        pass
    finally:
        sleep(2)
        driver.sendkeys(rate_input, "7.5000")
        driver.click(rate_save)
    sleep(3)
    driver.click(passed)




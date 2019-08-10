from time import sleep

from HLY_Elements.expense import elTravel_approval_can_be_printed
from HLY_Elements.expense.elExpense import enter_button, rate_description, submit_expense
from HLY_Elements.expense.elReimbursement import select_department, cause
from HLY_Elements.expense.elTravel_approval_can_be_printed import my_department, rate_input, rate_save
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense
from common.log import logger


def test_bug_32036(enter):
    """
    bug描述：报销单财务核定了汇率，费用分摊金额还是以原汇率换算得到本位币金额
    :param enter:
    :return:
    """
    driver = enter.driver
    process = Process(driver)
    reimbursement = Reimbursement(driver)
    # 选择报销单类型(日常报销单)
    my_expense = My_Expense(driver)
    my_expense.Newexpense("日常报销单-UI自动化")
    driver.click(elTravel_approval_can_be_printed.department_form)
    reimbursement.get_elements_click(0, my_department)
    # 选择部门后点击确定
    reimbursement.get_elements_click(0, enter_button)
    sleep(2)
    reimbursement.get_elements_sendKey(1, cause, "test费用分摊金额还是以原汇率换算得到本位币金额")
    # 新建报销单
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    my_expense.ClickNewExpenseButton()
    my_expense.SelectExpense("分摊费用类型")
    my_expense.SelectExpense_Currency("USD")
    my_expense.InputExpense_Amount("100")
    my_expense.ClickExpenseSave(2)
    logger.info("保存费用")
    business_Code = process.get_businessCode()
    sleep(5)
    driver.click(submit_expense)
    process.continue_submit()
    # 审批通过
    logger.info("报销单提交成功")
    process.approve(business_Code)
    # 进入审核的页面
    process.enter_Financial_audit(business_Code)
    driver.click(reimbursement.get_xpath("查看"), timeout=3)
    # 点击核定金额
    logger.info("点击查看")
    driver.click(reimbursement.get_xpath("核定金额"), timeout=2)
    # 输入变小的汇率
    logger.info("点击核定金额")
    driver.sendkeys(rate_input, "6.6025", timeout=2)
    # 进行保存
    logger.info("输入汇率")
    driver.click(rate_save)
    logger.info("保存汇率")
    driver.click(reimbursement.get_xpath("查看"), timeout=2)
    sleep(3)
    text = driver.get_text(rate_description)
    rate = text.split("：")[1].split("企")[0].split("\n")[0]
    logger.info("rate的类型：%s" % type(rate))
    assert (float(rate)*100) == 660.25
    logger.info("汇率：%s" % rate)



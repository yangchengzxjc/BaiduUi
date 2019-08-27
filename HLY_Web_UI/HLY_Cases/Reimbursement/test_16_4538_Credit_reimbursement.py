from time import sleep
from HLY_Elements.expense.elFinanciaCheck import passed
from HLY_Elements.expense.elExpense import check_lable, detal_page_lable, loan_link, expense_info, loan_links, \
    submit_expense
from HLY_Elements.expense.elReimbursement import cause
from HLY_PageObject.API.apis import query_loan, confirm, confirm_paid
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense
from common.log import logger


def test_credit_reimbursement(enter):
    """
    需求4538：报销单信用付款后，详情页面的展示
    case1和case3
    :param enter:
    :return:
    """
    driver = enter.driver
    process = Process(driver)
    reimbursement = Reimbursement(driver)
    my_expense = My_Expense(driver)
    my_expense.Newexpense("日常报销单-信用报销")
    sleep(2)
    reimbursement.get_elements_sendKey(1, cause, "test信用报销")
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    sleep(5)
    process.create_expense(10, "大巴")
    sleep(3)
    business_Code = process.get_businessCode()
    driver.click(submit_expense, timeout=3)
    sleep(5)
    process.continue_submit()
    sleep(5)
    # 点击标签的+号
    reimbursement.get_elements_click(0, check_lable)
    assert "信用报销" in driver.get_text(reimbursement.get_xpath("信用报销"))
    # 进入报销单详情页查看
    process.open_reimbursement(business_Code)
    sleep(4)
    assert driver.get_text(reimbursement.get_xpath("信用报销")) == "信用报销"
    reimbursement.Pagescroll(reimbursement.get_xpath("大巴"), timeout=1)
    reimbursement.get_elements_click(1, detal_page_lable)
    driver.click(loan_link)
    sleep(2)
    allhandle = driver.window_handles
    driver.switch_to_window(allhandle[1])
    lable1=driver.get_text(reimbursement.get_xpath("信用借款单"), timeout=5)
    assert lable1 == "信用借款单:%s"%lable1.split(":")[1]
    loan_Code = lable1.split(":")[1]
    logger.info("loan_code:%s"%loan_Code)
    driver.closes()
    allhandles = driver.window_handles
    driver.switch_to_window(allhandles[0])
    # #审批报销单
    process.approve(business_Code)
    # 虚拟借款单进行还款
    id = query_loan(loan_Code)
    confirm(id, loan_Code)
    # 确认已付款
    confirm_paid(id)
    sleep(3)
    # case3单据审核界面查看信用标签
    process.enter_invoice_verify(business_Code)
    assert driver.get_text(reimbursement.get_xpath("信用报销")) == "信用报销"
    driver.click(passed, timeout=4)
    # 查看审核通过的报销单
    process.open_reimbursement(business_Code)
    assert driver.get_text(reimbursement.get_xpath("信用报销")) == "信用报销"
    reimbursement.Pagescroll(reimbursement.get_xpath("大巴"), timeout=1)
    reimbursement.get_elements_click(1, detal_page_lable)
    assert loan_Code in reimbursement.get_elements_text(0, loan_links)
    assert loan_Code in reimbursement.get_elements_text(1, loan_links)













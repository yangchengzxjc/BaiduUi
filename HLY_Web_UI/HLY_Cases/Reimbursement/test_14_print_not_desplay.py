from time import sleep
from HLY_Elements.expense import elExpense
from HLY_Elements.expense.elReimbursement import cause, submit_expense, expense
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.Profile import Profile
from HLY_PageObject.UI.my_expense.my_expense import My_Expense


def test_ptintbtton_not_desplay(enter):
    """
    需求：4828审核后是否长打印按钮受profile控制：显示按钮关闭
    :return:
    """
    driver = enter.driver
    process = Process(driver)
    profile = Profile(driver)
    my_expense = My_Expense(driver)
    reimbursement = Reimbursement(driver)
    sleep(1)
    profile.print_close()
    my_expense.Newexpense("日常报销单-UI自动化")
    sleep(2)
    reimbursement.get_elements_sendKey(1, cause, "test打印按钮关掉不显示")
    # 新建报销单
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    # 新建费用
    sleep(3)
    process.create_expense(200, "大巴")
    business_Code = process.get_businessCode()
    # 提交报销单
    sleep(3)
    driver.click(submit_expense)
    sleep(5)
    process.continue_submit()
    sleep(5)
    # 审批通过报销单
    process.approve(business_Code)
    process.Financial_audit(business_Code)
    driver.click(expense, timeout=2)
    # 进入到我的报销单
    process.open_reimbursement(business_Code)
    sleep(5)
    # a=driver.get_text(Printing_button1)
    assert not driver.is_exist(reimbursement.get_xpath("打 印"))
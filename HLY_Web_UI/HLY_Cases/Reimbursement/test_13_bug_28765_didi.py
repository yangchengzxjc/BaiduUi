from time import sleep
from HLY_Elements.expense.elFinanciaCheck import book_enter, yangzhao_expensetype, \
    select_button_confirm
from HLY_Elements.expense.elReimbursement import cause, submit_expense
from HLY_PageObject.API.apis import expense_yangzhao
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense


def test_bug_28765_didi(enter):
    """
    bug描述：在报销单内保存费用（滴滴，扬招）提示必填字段为空，但是必填字段都已填写
    :param enter:
    :return:
    """
    driver = enter.driver
    process = Process(driver)
    reimbursement = Reimbursement(driver)
    # 选择报销单类型(日常报销单)
    my_expense = My_Expense(driver)
    my_expense.Newexpense("日常报销单-UI自动化")
    sleep(3)
    reimbursement.get_elements_sendKey(1, cause, "test必填字段已填保存报销单")
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    # 产生扬招费用
    yangzhao_code = expense_yangzhao()
    # 判断杨招费用是否新建成功
    assert yangzhao_code == 200
    sleep(2)
    driver.click(book_enter)
    reimbursement.get_elements_click(0, yangzhao_expensetype)
    reimbursement.get_elements_click(4, select_button_confirm)
    sleep(3)
    assert not driver.is_exist(reimbursement.get_xpath("必输未填：费用必填字段为空"))
    driver.click(submit_expense, timeout=3)
    sleep(5)
    process.continue_submit()


from time import sleep

from HLY_Elements.expense.elExpense import scroll_locate, departures_prod, destinations_prod
from HLY_Elements.expense.elFinanciaCheck import CNY, input_expenses
from HLY_Elements.expense.elReimbursement import cause
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense


def test_bug_16265_edit_expense(enter):
    """
    bug描述:用户在PC端编辑报销单时，填好的费用项保存后再编辑，点击进去，里面是空白无记录。
    :return:
    """
    driver = enter.driver
    process = Process(driver)
    reimbursement = Reimbursement(driver)
    # 选择报销单类型(日常报销单-UI自动化)
    # reimbursement.new_reimbursement(form_type)
    my_expense = My_Expense(driver)
    my_expense.Newexpense("日常报销单-UI自动化")
    sleep(2)
    reimbursement.get_elements_sendKey(1, cause, "test报销单审核后可打印")
    # 新建报销单
    driver.click(reimbursement.get_parent_xpath("新 建"))
    # 新建费用并且保存
    process.create_all_expense(200, "大巴", "西安钟楼", "西安大雁塔")
    sleep(5)
    # 再次编辑
    reimbursement.Pagescroll(CNY, timeout=2)
    driver.click(CNY)
    sleep(4)
    value1 = driver.find_element_by_xpath(input_expenses).get_attribute("value")
    assert value1 == "200.00"
    my_expense.Pagescroll(scroll_locate, timeout=5)
    value2 = driver.find_element_by_xpath(departures_prod).get_attribute("value")
    assert value2 == "西安钟楼"
    sleep(2)
    valus3 = driver.find_element_by_xpath(destinations_prod).get_attribute("value")
    assert valus3 == "西安大雁塔"

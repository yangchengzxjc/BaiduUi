from time import sleep
from HLY_Elements.expense import elTravel_approval_can_be_printed
from HLY_Elements.expense.elReimbursement import cause, new_expense
from HLY_Elements.expense.elFinanciaCheck import input_expenses
from HLY_Elements.expense.expense_type import amount_input, new_apportion
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense
from common.log import logger


def test_apportion_line(enter):
    """
    需求：4451：分摊控件交互优化

    :param enter:
    :return:
    """
    driver = enter.driver
    process = Process(driver)
    reimbursement = Reimbursement(driver)
    my_expense = My_Expense(driver)
    sleep(3)
    my_expense.Newexpense("日常报销单-UI自动化")
    # 选择一个部门
    driver.click(elTravel_approval_can_be_printed.department_form)
    reimbursement.get_elements_click(0, elTravel_approval_can_be_printed.my_department)
    reimbursement.get_elements_click(1, elTravel_approval_can_be_printed.confirm)
    # 添加事由
    reimbursement.get_elements_sendKey(1, cause, "test分摊行的优化")
    # 新建报销单
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    sleep(5)
    # reimbursement.create_expense(100, "分摊费用类型")
    driver.click(new_expense, timeout=2)
    sleep(2)
    my_expense.SelectExpense("分摊费用类型")
    sleep(3)
    driver.find_element_by_xpath(input_expenses).clear()
    sleep(2)
    driver.find_element_by_xpath(input_expenses).send_keys("100")
    sleep(5)
    reimbursement.Pagescroll(reimbursement.get_xpath("费用分摊"), timeout=1)
    assert reimbursement.get_elements_attribute(1, amount_input, "value") == "100.00"
    # 输入新的分摊金额
    sleep(5)
    reimbursement.get_element_clear(1, amount_input)
    reimbursement.get_elements_sendKey(1, amount_input, "30.33")
    value1 = reimbursement.get_elements_attribute(2, amount_input, "value", timeout=5)
    logger.info("value1: %s" % value1)
    assert value1 == ('%.2f' % (30.33/100*100))
    # 点击新增分摊
    reimbursement.get_elements_click(0, new_apportion)
    sleep(2)
    value2 = reimbursement.get_elements_attribute(3, amount_input, "value")
    logger.info("value2: %s" % value2)
    assert value2 == "69.67"
    # 修改第一个分摊的金额为10元
    reimbursement.get_element_clear(1, amount_input)
    sleep(2)
    reimbursement.get_elements_sendKey(1, amount_input, "10.45")
    # 点击新增分摊
    reimbursement.get_elements_click(0, new_apportion)
    # reimbursement.get_element_clear(1, amount_input)
    # 判断第三个分滩行金额
    value3 = reimbursement.get_elements_attribute(5, amount_input, "value")
    assert value3 == "19.88"
    logger.info("第三个分滩行的金额：%s" % value3)
    value4 = reimbursement.get_elements_attribute(6, amount_input, "value")
    assert value4 == "19.88"
    assert round(float(value2)+float(value3)+float("10.45"), 2) == 100.00

















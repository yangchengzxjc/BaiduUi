from time import sleep

from HLY_Elements.expense.elReimbursement import cause
from HLY_Elements.expense.elTravel_allowance import Travel_allowance, add_Travel_allowance, city_travel_input, \
    select_place, save_travel_allowance, delete_button, delete_frame, confirm_button1, \
    success_delete
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense


def test_4473_Travel_allowance(enter):
    """
    需求：差补费用删除文案优化 差补费用类型，[操作]列的按钮文案改为：删除
          弹框文案改为：确认删除该费用？
          费用删除后的toast文案：费用删除成功！
    :param enter:
    :return:
    """
    driver = enter.driver
    reimbursement = Reimbursement(driver)
    process = Process(driver)
    # 选择报销单类型(日常报销单)
    my_expense = My_Expense(driver)
    my_expense.Newexpense("日常报销单-UI自动化")
    sleep(2)
    reimbursement.get_elements_sendKey(1, cause, "test差补费用删除文案优化")
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    # 进入差补计算
    sleep(5)
    reimbursement.get_elements_click(0, Travel_allowance)
    # 点击添加的按钮
    driver.click(add_Travel_allowance)
    driver.sendkeys(city_travel_input, "西安")
    driver.click(select_place)
    # 保存差补
    sleep(5)
    driver.click(save_travel_allowance, timeout=1)
    sleep(5)
    reimbursement.Pagescroll(reimbursement.get_xpath("删除"), timeout=1)
    delete_text = reimbursement.get_elements_text(0, delete_button)
    assert delete_text == '删除'
    driver.click(reimbursement.get_xpath("删除"))
    sleep(2)
    assert driver.get_text(delete_frame) == "确定要删除吗？"
    driver.click(confirm_button1)
    assert driver.get_text(success_delete) == "删除成功"


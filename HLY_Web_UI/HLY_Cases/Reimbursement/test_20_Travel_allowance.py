from time import sleep

from HLY_Elements.expense.elReimbursement import cause
from HLY_Elements.expense.elTravel_allowance import Travel_allowance, add_Travel_allowance, city_travel, \
      city_travel_input, select_place, save_travel_allowance, setoff_input, dalian_place
from HLY_PageObject.API.apis import open_auto_route_Calculation, close_auto_route_Calculation
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense


def test_4460_Travel_allowance01(enter):
    """
    差补规则基于“城市”维度.
    当差补中的自动行程开关打开时，选择城市出发城市和目的城市，默认全选
    :param enter:
    :return:
    """
    driver = enter.driver
    reimbursement = Reimbursement(driver)
    # 选择报销单类型(日常报销单)
    my_expense = My_Expense(driver)
    my_expense.Newexpense("日常报销单-UI自动化")
    sleep(2)
    # 打开自动行程计算
    open_auto_route_Calculation()
    reimbursement.get_elements_sendKey(1, cause, "自动行程计算打开对差补的影响")
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    sleep(5)
    # 进入差补计算
    reimbursement.get_elements_click(0, Travel_allowance)
    # 点击添加按钮
    driver.click(add_Travel_allowance)
    # #判断出差城市的元素是否存在
    # assert driver.is_exist(city_travel)
    driver.sendkeys(setoff_input, "西安")
    sleep(1)
    driver.click(select_place)
    driver.sendkeys(city_travel_input, "大连", timeout=1)
    driver.click(dalian_place, timeout=1)
    # 默认带出全部差补
    assert driver.get_text(reimbursement.get_xpath("全选"), timeout=5) == "全选"
    # 保存差补
    close_auto_route_Calculation()
    driver.click(save_travel_allowance)




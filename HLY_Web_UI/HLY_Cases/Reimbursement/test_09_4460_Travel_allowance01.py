from time import sleep

from HLY_Elements.expense.elFinanciaCheck import \
    travel_input_expenses
from HLY_Elements.expense.elExpense import coin_varity, first_amout, save
from HLY_Elements.expense.elReimbursement import cause
from HLY_Elements.expense.elTravel_allowance import Travel_allowance, add_Travel_allowance, city_travel, \
    city_travel_input, select_place, save_travel_allowance, city_travel2, \
    amount, defult_type_status, allowance_city2
from HLY_PageObject.API.apis import close_auto_route_Calculation
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense
from common.log import logger
from config.api_urls import expense_standard


def test_4460_Travel_allowance01(enter):
    """
    需求描述：报销单差补填写时，城市不必填
    差补规则基于“城市”维度.
    需求：4439申请单匹配到的差补规则默认全选:case :默认选择开关打开状态
    :param enter:
    :return:
    """
    driver = enter.driver
    reimbursement = Reimbursement(driver)
    # 选择报销单类型(日常报销单)
    my_expense = My_Expense(driver)
    process = Process(driver)
    close_auto_route_Calculation()
    reimbursement.get_url(expense_standard)
    close_auto_route_Calculation()
    logger.info("允许修改费用配置成功")
    sleep(5)
    my_expense.Newexpense("日常报销单-UI自动化")
    sleep(2)
    reimbursement.get_elements_sendKey(1, cause, "差补规则基于“城市”维度")
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    sleep(5)
    # 进入差补计算
    reimbursement.get_elements_click(0, Travel_allowance)
    # 点击添加按钮
    # driver.click(add_Travel_allowance)
    # 判断出差城市的元素是否存在
    assert driver.is_exist(city_travel)
    driver.sendkeys(city_travel_input, "西安")
    sleep(1)
    driver.click(select_place)
    # 选择全部类型
    sleep(5)
    assert driver.is_exist(defult_type_status)
    # 保存差补
    driver.click(save_travel_allowance)
    sleep(5)
    reimbursement.Pagescroll(amount, timeout=1)
    business_Code = process.get_businessCode()
    # 报销单页面的城市控件信息
    place1 = driver.get_text(allowance_city2)
    logger.info("差补城市：%s" % place1)
    assert "西安" in place1
    reimbursement.Pagescroll(coin_varity, timeout=1)
    sleep(2)
    reimbursement.get_elements_click(4, reimbursement.get_origin_xpath("CNY"))
    logger.info("查看第二个差补费用")
    sleep(3)
    driver.sendkeys(travel_input_expenses, "19", timeout=3)
    logger.info("修改差补费用19元")
    driver.click(save_travel_allowance, timeout=2)
    sleep(5)
    assert driver.get_text(first_amout) == "19.00"
    reimbursement.get_elements_click(0, Travel_allowance)
    assert reimbursement.get_elements_text(1, city_travel2) == "西安"

    # driver.click(submit_expense)
    # reimbursement.continue_submit()
    # reimbursement.open_reimbursement(business_Code)
    # sleep(7)
    # reimbursement.Pagescroll(check, timeout=1)
    # reimbursement.get_elements_click(0, check2)
    # place3 = driver.get_text(city_travel3)
    # assert place3 == "城市"






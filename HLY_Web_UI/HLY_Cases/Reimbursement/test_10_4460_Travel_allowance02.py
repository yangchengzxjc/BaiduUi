from time import sleep
from HLY_Elements.expense import elTravel_approval_can_be_printed
from HLY_Elements.expense.elTravel_allowance import Travel_allowance, add_Travel_allowance, city_travel, \
    travel_type, save_travel_allowance,\
    amount, city_travel4, defult_type_status, allowance_city2
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense
from common.log import logger


def test_4460_Travel_allowance01(enter):
    """
    需求描述：报销单差补填写时，城市不必填
    差补规则不基于“城市”维度.
    需求：4439申请单匹配到的差补规则默认全选:case:默认选择开关为关闭状态
    :param enter:
    :return:
    """
    driver = enter.driver
    process = Process(driver)
    reimbursement = Reimbursement(driver)
    # 选择差旅报销单-新建他人费用的表单
    my_expense = My_Expense(driver)
    my_expense.Newexpense("差旅报销单-UI自动化")
    # 点击关联申请单
    driver.click(elTravel_approval_can_be_printed.form1)
    sleep(2)
    # 点击搜索框输入条件
    # reimbursement.get_elements_sendKey(0, apply_search, "test部门信息")
    # 点击搜索
    # reimbursement.get_elements_click(1, elTravel_approval_can_be_printed.confirm)
    reimbursement.get_elements_click(0, reimbursement.get_origin_xpath("test部门信息"))
    # 点击确定，选择申请单完毕
    reimbursement.get_elements_click(1, elTravel_approval_can_be_printed.confirm)
    sleep(2)
    driver.click(reimbursement.get_parent_xpath("新 建"))
    logger.info("新建报销单")
    sleep(5)
    # 进入差补计算
    reimbursement.get_elements_click(0, Travel_allowance)
    # 点击添加按钮
    driver.click(add_Travel_allowance)
    # 判断出差城市的元素是否存在
    assert not driver.is_exist(city_travel)
    # 选择全部类型
    sleep(5)
    assert not driver.is_exist(defult_type_status)
    reimbursement.get_elements_click(0,travel_type)
    # 保存差补
    driver.click(save_travel_allowance)
    sleep(6)
    reimbursement.Pagescroll(amount, timeout=1)
    business_Code = process.get_businessCode()
    # 报销单页面的城市控件信息
    place1 = driver.get_text(allowance_city2)
    logger.info("差补城市：%s" % place1)
    assert "补贴城市:   西安" not in place1
    sleep(2)
    reimbursement.get_elements_click(0, Travel_allowance)
    assert driver.get_text(city_travel4) == "通用城市"
    # driver.click(submit_expense)
    # reimbursement.continue_submit()
    # reimbursement.open_reimbursement(business_Code)
    # sleep(5)
    # reimbursement.Pagescroll(check, timeout=1)
    # reimbursement.get_elements_click(0, check2)
    # sleep(5)
    # place3 = reimbursement.get_elements_text(0,city_display)
    # assert place3 == "-"






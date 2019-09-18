from time import sleep

import pytest

from HLY_Elements.expense import elTravel_approval_can_be_printed
from HLY_Elements.expense.elReimbursement import expense_owner, expense_owners, user_name
from HLY_Elements.expense.elTravel_allowance import Travel_allowance, city_travel_input,\
    save_travel_allowance, select_city
from HLY_PageObject.API.apis import close_auto_route_Calculation, change_subsidy_rule
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense
from common.log import logger


@pytest.fixture(scope='function')
def config_env(request, enter):
    """
    配置环境
    :param enter:
    :return:
    """
    # 报销单自动获取补贴方式 : 不自动获取
    close_auto_route_Calculation(allowanceAttachExpenseReportDisable="false")
    change_subsidy_rule("差旅申请单-差补专用", ALLOWANCE_CITY=True)
    def clear_env():
        """
        清理环境：修改补贴方式为申请单带入  并且打开拆不规则中的城市
        :return:
        """
        close_auto_route_Calculation()
        change_subsidy_rule("差旅申请单-差补专用", ALLOWANCE_CITY=True)
    request.addfinalizer(clear_env)
    return enter


def test_6404_Travel_allowance(config_env):
    """
    需求：差补界面优化
    1.费用归属人 = 行程自动计算的费用的费用归属人；
    2.单据头出差往返日期有值时， 后台大行程日期=单据头开始结束日期；
    3.最后一个小形程没有删除按钮
    :param enter:
    :return:
    """
    driver = config_env.driver
    reimbursement = Reimbursement(driver)
    process = Process(driver)
    # 选择报销单类型(差旅报销单-差补专用)
    my_expense = My_Expense(driver)
    my_expense.Newexpense("差旅报销单-差补专用")
    sleep(2)
    # 选择申请单
    driver.click(elTravel_approval_can_be_printed.form1)
    reimbursement.get_elements_click(0, reimbursement.get_origin_xpath("差补专用"))
    # 点击确定，选择申请单完毕
    reimbursement.get_elements_click(1, elTravel_approval_can_be_printed.confirm)
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    sleep(3)
    reimbursement.get_elements_click(0, Travel_allowance)
    assert not driver.is_exist(reimbursement.get_xpath("添加差旅行程"), timeout=2)
    driver.sendkeys(city_travel_input, "西安")
    logger.info("输入出差城市")
    sleep(1)
    driver.click(select_city)
    driver.click(save_travel_allowance, timeout=8)
    logger.info("保存差补")
    reimbursement.get_elements_click(0, Travel_allowance)
    logger.info("再次打开补贴计算查看删除按钮")
    assert driver.get_element(reimbursement.get_xpath("删除"), timeout=2)
    # 删除按钮
    process.Pagescroll(expense_owner, timeout=3)
    assert driver.get_text(expense_owners) == driver.get_text(user_name)







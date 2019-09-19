import datetime
from time import sleep

import pytest

from HLY_Elements.expense import elTravel_approval_can_be_printed
from HLY_Elements.expense.elTravel_allowance import Travel_allowance, \
    save_travel_allowance, select_city, city_travel_input, stoke_time_noStoke, edit_expense_owen, select_persion, \
    owen_expense
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
    close_auto_route_Calculation(allowanceAttachExpenseReportDisable="true")
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
    点击[更改]，判断：[费用归属人] 、 [出差往返日期]
    a) 均没有发生变更，则等同于点击[取消]，页面不做变更。
    b) 任一一项发生变更，则页面做变更。
    c) 提示文案：更改费用归属人或者出差往返日期，可能更改相应行程日期，请重新选择生成的补贴。
    :param enter:
    :return:
    """
    driver = config_env.driver
    reimbursement = Reimbursement(driver)
    process = Process(driver)
    my_expense = My_Expense(driver)
    my_expense.Newexpense("差旅报销单-代理制单人")
    sleep(2)
    driver.click(elTravel_approval_can_be_printed.form1)
    driver.click(reimbursement.get_xpath("TA00888669"))
    reimbursement.get_elements_click(1, elTravel_approval_can_be_printed.confirm)
    # 新建报销单
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    reimbursement.get_elements_click(0, Travel_allowance)
    reimbursement.get_elements_click(1, reimbursement.get_origin_xpath("编辑"), timeout=2)
    driver.click(edit_expense_owen)
    driver.click(select_persion, timeout=2)
    driver.click(reimbursement.get_xpath("更改"))
    # 点击更改发生了变化
    assert driver.is_exist(reimbursement.get_xpath("更改费用归属人或者出差往返日期，可能更改相应行程日期，请重新选择生成的补贴。"))
    assert "员工1号" in driver.get_text(owen_expense)
    driver.sendkeys(city_travel_input, "西安")
    logger.info("输入出差城市")
    sleep(1)
    driver.click(select_city)
    driver.click(save_travel_allowance, timeout=3)
    logger.info("保存差补")

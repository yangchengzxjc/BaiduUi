import datetime
from time import sleep

import pytest

from HLY_Elements.expense.elReimbursement import cause
from HLY_Elements.expense.elTravel_allowance import Travel_allowance, \
    save_travel_allowance, select_city, city_travel_input,stoke_time_noStoke
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
    单据头日期没有值或无该控件，则默认当天。
    :param enter:
    :return:
    """
    driver = config_env.driver
    reimbursement = Reimbursement(driver)
    process = Process(driver)
    # 选择报销单类型(差旅报销单-差补专用)
    my_expense = My_Expense(driver)
    my_expense.Newexpense("日常报销单-日期控件")
    sleep(2)
    date = datetime.datetime.now().date()
    reimbursement.get_elements_sendKey(1, cause, "报销单头信息自动生成补贴")
    # 新建报销单
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    reimbursement.get_elements_click(0, Travel_allowance)
    driver.sendkeys(city_travel_input, "西安")
    logger.info("输入出差城市")
    sleep(1)
    driver.click(select_city)
    driver.click(save_travel_allowance, timeout=3)
    logger.info("保存差补")
    reimbursement.get_elements_click(0, Travel_allowance, timeout=5)
    assert date in str(driver.get_text(stoke_time_noStoke))











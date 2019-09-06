from datetime import datetime
from time import sleep

import pytest

from HLY_Elements.expense.elReimbursement import cause, data_time, select_day, stroke_time, edit_date, user_name, \
    start_Date
from HLY_Elements.expense.elTravel_allowance import Travel_allowance, add_Travel_allowance, city_travel_input, \
    select_place, save_travel_allowance, delete_button, delete_frame, confirm_button1, \
    success_delete, edit, owen_expense, goBackDate
from HLY_PageObject.API.apis import get_formType, close_auto_route_Calculation, change_subsidy_rule
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
    # 报销单自动获取补贴方式 : 报销单头起止日期生成补贴
    close_auto_route_Calculation(allowanceAttachExpenseReportDisable=True)
    # 删除差补中的城市
    change_subsidy_rule("差旅申请单-UI自动化", ALLOWANCE_CITY=True)
    def clear_env():
        """
        清理环境：修改补贴方式为申请单带入  并且打开拆不规则中的城市
        :return:
        """
        close_auto_route_Calculation()
        change_subsidy_rule("差旅申请单-UI自动化", ALLOWANCE_CITY=True)
    request.addfinalizer(clear_env)
    return enter


def test_6404_Travel_allowance(config_env):
    """
    6157：显示大行程：显示1条大行程+1条小行程
          1、大行程：费用归属人 = 单据申请人
    :param enter:
    :return:
    """
    driver = config_env.driver
    reimbursement = Reimbursement(driver)
    process = Process(driver)
    # 选择报销单类型(日常报销单)
    my_expense = My_Expense(driver)
    my_expense.Newexpense("日常报销单-日期控件")
    sleep(2)
    reimbursement.get_elements_sendKey(1, cause, "报销单头信息自动生成补贴")
    # 选择时间
    driver.click(data_time)
    logger.info("点击时间控件选择时间")
    day = datetime.datetime.now().day
    logger.info("day:%s" % day)
    sleep(2)
    reimbursement.get_elements_click(day-1, select_day)
    logger.info("输入第一个日期")
    reimbursement.get_elements_click(day, select_day)
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    # 获取报销单的出发日期（开始日期）
    startDate = driver.get_text(start_Date,timeout=2)
    # 进入差补计算
    reimbursement.get_elements_click(0, Travel_allowance)
    # 判断是否有大行程
    assert driver.is_exist(reimbursement.get_xpath("出差往返日期"))
    # 判断费用归熟人= 单据申请人
    assert driver.get_text(owen_expense) == driver.get_text(user_name)
    # 获取大行程的时间
    time = driver.getAttribute(goBackDate, "value")
    GMT_FORMAT = '%a %b %d %Y %H:%M:%S GMT+0800'
    assert startDate in datetime.strptime(time[0], GMT_FORMAT)
    assert "%s" % (day+1) in driver.get_text(stroke_time)
    # 检查事由的值带入到行程里的备注
    assert driver.is_exist(reimbursement.get_xpath("报销单头信息自动生成补贴"))
    driver.click(save_travel_allowance)
    logger.info("保存差补")
    driver.click(reimbursement.get_xpath("编辑"),  timeout=5)
    driver.click(edit_date, timeout=1)
    logger.info("编辑报销单头，点击日期")
    reimbursement.get_elements_click(day - 1, select_day)
    reimbursement.get_elements_click(day + 1, select_day)
    reimbursement.get_elements_click(0, reimbursement.get_origin_parent_xpath("保 存"))
    assert driver.is_exist(reimbursement.get_xpath("修改后，补贴费用将重新生成，是否继续"))
    reimbursement.get_elements_click(1, reimbursement.get_origin_parent_xpath("确 定"))
    sleep(7)
    driver.click(reimbursement.get_parent_xpath("删 除"), timeout=4)
    reimbursement.get_elements_click(1, reimbursement.get_origin_parent_xpath("确 定"))




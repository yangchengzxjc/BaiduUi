from time import sleep
import datetime as date
import pytest


from HLY_Elements.expense.elReimbursement import cause, data_time, select_day, user_name, \
    start_Date
from HLY_Elements.expense.elTravel_allowance import Travel_allowance, save_travel_allowance, \
    owen_expense, goBackDate, stoke_time
from HLY_PageObject.API.apis import  close_auto_route_Calculation, change_subsidy_rule
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
    close_auto_route_Calculation(allowanceAttachExpenseReportDisable="true")
    # 删除差补中的城市
    change_subsidy_rule("差旅申请单-UI自动化", ALLOWANCE_CITY=False)
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
          2.大行程：出差往返日期（时间） = 单据头日期（时间）
          3.行程日期 = 大行程的出差往返日期。
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
    day = date.datetime.now().day
    logger.info("day:%s" % day)
    sleep(2)
    reimbursement.get_elements_click(day-1, select_day)
    logger.info("输入第一个日期")
    reimbursement.get_elements_click(day, select_day)
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    # 获取报销单的出发日期（开始日期）
    userName = driver.get_text(user_name)
    startDate = driver.get_text(start_Date, timeout=2)
    # 进入差补计算
    reimbursement.get_elements_click(0, Travel_allowance)
    # 判断是否有大行程
    sleep(5)
    assert driver.is_exist(reimbursement.get_xpath("出差往返日期"))
    # 判断费用归熟人= 单据申请人
    assert driver.get_text(owen_expense) == userName
    # 获取大行程的时间
    time = driver.getAttribute(goBackDate, "value")
    logger.info("日期：%s" % time.split(",")[0])
    GMT_FORMAT = '%a %b %d %Y %H:%M:%S GMT+0800'
    from datetime import datetime
    logger.info("格式化后的时间类型：%s" % (type(datetime.strptime(time.split(",")[0], GMT_FORMAT))))
    assert startDate in str(datetime.strptime(time.split(",")[0], GMT_FORMAT))
    driver.click(save_travel_allowance, timeout=2)
    logger.info("保存差补")
    reimbursement.get_elements_click(0, Travel_allowance)
    logger.info("再次进入差补")
    stoke_date =driver.get_text(stoke_time).split(" ~ ")[0]
    logger.info("stoke_date:%s"% stoke_date)
    assert stoke_date in str(datetime.strptime(time.split(",")[0], GMT_FORMAT))



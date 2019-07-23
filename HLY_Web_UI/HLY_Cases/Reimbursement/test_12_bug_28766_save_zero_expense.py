from time import sleep
from HLY_Elements.expense.elFinanciaCheck import CNY
from HLY_Elements.expense.elReimbursement import cause
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense
from common.log import logger


def test_bug_28766_save_zero_expense(enter):
    """
    bug描述：报销单内新建任意费用金额为0可保存，再次编辑费用报错显示费用分摊项金额比例不能为0
    配置：功能配置文件中配置费用的总金额可为零。
    :param enter:
    :return:
    """
    driver = enter.driver
    process = Process(driver)
    reimbursement = Reimbursement(driver)
    # 选择报销单类型(日常报销单)
    my_expense = My_Expense(driver)
    my_expense.Newexpense("日常报销单-UI自动化")
    sleep(2)
    reimbursement.get_elements_sendKey(1, cause, "test费用为零保存后再次编辑")
    # 新建报销单
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    logger.info("新建报销单")
    sleep(5)
    process.create_expense(0, "大巴")
    sleep(6)
    reimbursement.Pagescroll(CNY, timeout=1)
    driver.click(CNY)
    assert not driver.is_exist(reimbursement.get_xpath("费用分摊项金额与比例不能为0"))









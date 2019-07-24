from time import sleep
from HLY_Elements.expense.elFinanciaCheck import book_enter, select_button_confirm
from HLY_Elements.expense.elReimbursement import cause, submit_expense
from HLY_Elements.expense.elTravel_allowance import amount
from HLY_PageObject.API.apis import Consumer_expense
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense
from common.log import logger


def test_26_Abnormal_operation(enter):
    """
    bug:两个标签页同时操作，费用导入，报销单提交，报销单撤回
    :param enter:
    :return:
    """
    driver = enter.driver
    reimbursement = Reimbursement(driver)
    process = Process(driver)
    # 选择报销单类型(日常报销单)
    my_expense = My_Expense(driver)
    Consumer_expense(100, "空港嘉华机票")
    logger.info("推送空港嘉华机票")
    my_expense.Newexpense("日常报销单-UI自动化")
    reimbursement.get_elements_sendKey(1, cause, "标签页同时操作")
    # 新建报销单
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    logger.info("新建完成第一个报销单")
    sleep(5)
    PageUrl1 = driver.current_url
    # 开始新建第二个报销单
    my_expense.Newexpense("日常报销单-UI自动化")
    reimbursement.get_elements_sendKey(1, cause, "标签页同时操作", timeout=5)
    # 新建报销单
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    logger.info("新建第二个报销单完成")
    sleep(5)
    PageUrl2 = driver.current_url
    driver.get(PageUrl1)
    # 进入第一个报销单导入账本
    driver.click(book_enter, timeout=5)
    reimbursement.get_elements_click(0, reimbursement.get_origin_xpath("空港嘉华机票"), timeout=7)
    logger.info("第一个页面选择费用")
    driver.js('window.open("%s")' % PageUrl2, timeout=1)
    # 当前页面打开账本
    sleep(5)
    handlers = driver.window_handles
    logger.info("当前页面的标签数：%s" % len(handlers))
    driver.switch_to_window(handlers[1])
    logger.info("切换到第二个标签页")
    driver.click(book_enter, timeout=2)
    logger.info("第二个页面点击账本导入")
    reimbursement.get_elements_click(0, reimbursement.get_origin_xpath("空港嘉华机票"), timeout=6)
    logger.info("第二个页面选择费用")
    reimbursement.get_elements_click(3, select_button_confirm)
    logger.info("第二页面费用选择确定")
    sleep(3)
    driver.switch_to_window(handlers[0])
    logger.info("切换到第一个标签页")
    reimbursement.get_elements_click(3, select_button_confirm)
    logger.info("第一个页面费用选择确定")
    reimbursement.Pagescroll(amount, timeout=1)
    logger.info("滑到费用行")
    assert not driver.is_exist(reimbursement.get_xpath("空港嘉华机票"), timeout=1)
    # case 3和case 4 同一个报销单导入同一笔费用
    Consumer_expense(110, "空港嘉华机票")
    driver.click(book_enter, timeout=6)
    reimbursement.get_elements_click(0, reimbursement.get_origin_xpath("空港嘉华机票"), timeout=6)
    # 切换到第二个标签页
    driver.switch_to_window(handlers[1])
    sleep(2)
    driver.get(PageUrl1)
    logger.info("第二个页面打开第一个报销单")
    driver.click(book_enter, timeout=6)
    logger.info("第二个页面点击账本导入")
    reimbursement.get_elements_click(0, reimbursement.get_origin_xpath("空港嘉华机票"), timeout=6)
    logger.info("第二个页面选择费用")
    reimbursement.get_elements_click(3, select_button_confirm)
    logger.info("第二页面费用选择确定")
    driver.switch_to_window(handlers[0])
    reimbursement.get_elements_click(3, select_button_confirm)
    logger.info("第一个页面费用选择确定")
    driver.click(submit_expense, timeout=3)
    process.continue_submit()
    logger.info("第一个页面提交成功")
    sleep(5)
    driver.switch_to_window(handlers[1])
    driver.click(submit_expense, timeout=3)
    assert not driver.is_exist(reimbursement.get_xpath("存在借款未还清！"), timeout=3)
    driver.closes()
    driver.switch_to_window(handlers[0])

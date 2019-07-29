from time import sleep

import pytest

from HLY_Elements.expense import elTravel_approval_can_be_printed
from HLY_Elements.expense.elApprove import apportion_line3, refuse, reason
from HLY_Elements.expense.elExpense import save, submit_expense
from HLY_Elements.expense.elFinanciaCheck import travel_input_expenses
from HLY_Elements.expense.elReimbursement import cause, new_expense
from HLY_Elements.expense.expense_type import amount_input, new_apportion, apportion_department1, apportion_department3
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense
from common.log import logger

@pytest.fixture(scope="function")
def creat_expense(enter):
    driver = enter.driver
    process = Process(driver)
    reimbursement = Reimbursement(driver)
    my_expense = My_Expense(driver)
    sleep(3)
    my_expense.Newexpense("日常报销单-UI自动化")
    # 选择一个部门
    driver.click(elTravel_approval_can_be_printed.department_form)
    reimbursement.Pagescroll(reimbursement.get_xpath("部门编码"), timeout=1)
    driver.click(reimbursement.get_parent_xpath("001"))
    reimbursement.get_elements_click(1, elTravel_approval_can_be_printed.confirm)
    # 添加事由
    reimbursement.get_elements_sendKey(1, cause, "test分摊金额和分摊比例尾差的处理")
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    # logger.info("新建报销单完成")
    return driver, reimbursement, my_expense, process


def test_apportion_line(creat_expense):
    """
    费用场景描述：1.分摊比例尾差处理
                  2.分摊金额尾插的处理
                  3.编辑分摊
    :param enter:
    :return:
    """
    driver, reimbursement, my_expense, process = creat_expense
    # 新建报销单
    driver.click(new_expense, timeout=4)
    my_expense.SelectExpense("分摊费用类型")
    logger.info("新建分摊费用")
    # 输入分摊的金额
    driver.sendkeys(travel_input_expenses, "30")
    reimbursement.Pagescroll(reimbursement.get_xpath("费用分摊"), timeout=1)
    logger.info("滑动到分摊行")
    reimbursement.get_element_clear(1, amount_input)
    reimbursement.get_elements_sendKey(1, amount_input, "10")
    logger.info("修改第一个分摊行金额")
    # 点击新增分摊
    process.get_elements_click(0, new_apportion)
    logger.info("新增分摊")
    # 选择公司
    driver.click(apportion_department1, timeout=2)
    logger.info("点击选择部门")
    driver.click(reimbursement.get_parent_xpath("测试部门"), timeout=2)
    logger.info("选择部门测试部门")
    # 点击确定
    reimbursement.get_elements_click(0, reimbursement.get_origin_parent_xpath("确 定"))
    sleep(3)
    logger.info("点击确定")
    # 修改分滩行
    reimbursement.get_element_clear(3, amount_input)
    reimbursement.get_elements_sendKey(3, amount_input, "10")
    logger.info("修改完成第二个分摊金额")
    # 点击新增分摊
    process.get_elements_click(0, new_apportion)
    driver.click(apportion_department3, timeout=2)
    logger.info("点击选择部门")
    reimbursement.get_elements_click(1, reimbursement.get_origin_parent_xpath("YS部门1"), timeout=2)
    logger.info("选择YS部门1")
    reimbursement.get_elements_click(1, reimbursement.get_origin_parent_xpath("确 定"))
    # 修改分滩行
    reimbursement.get_element_clear(5, amount_input)
    reimbursement.get_elements_sendKey(5, amount_input, "10")
    logger.info("修改第三个分滩行的金额")
    assert process.get_elements_attribute(6, amount_input, "value") == "33.33"
    # 点击保存分摊费用
    driver.click(save)
    sleep(5)
    business_Code = process.get_businessCode()
    reimbursement.Pagescroll(reimbursement.get_xpath("复制"), timeout=2)
    assert driver.get_text(reimbursement.get_xpath("分摊费用类型")) == "分摊费用类型"
    driver.click(reimbursement.get_xpath("分摊费用类型"))
    reimbursement.Pagescroll(reimbursement.get_xpath("费用分摊"), timeout=1)
    assert process.get_elements_attribute(6, amount_input, "value") == "33.34"
    driver.click(save)
    driver.click(submit_expense, timeout=2)
    process.continue_submit()
    logger.info("报销单提交")
    # 报销单撤回
    process.withdraw(business_Code)
    driver.click(reimbursement.get_xpath(business_Code), timeout=1)
    reimbursement.Pagescroll(reimbursement.get_xpath("复制"), timeout=3)
    driver.click(reimbursement.get_xpath("分摊费用类型"))
    reimbursement.Pagescroll(reimbursement.get_xpath("费用分摊"), timeout=1)
    assert process.get_elements_attribute(6, amount_input, "value") == "33.34"
    driver.click(save)
    driver.click(submit_expense, timeout=3)
    process.continue_submit()
    # 审批界面查看分摊比例以及金额
    # 报销单驳回
    process.enter_approve("ER02208326")
    driver.click(reimbursement.get_xpath("查看"))
    sleep(3)
    reimbursement.Pagescroll(reimbursement.get_xpath("费用分摊"), timeout=1)
    assert driver.get_text(apportion_line3) == "33.34%"
    # 点击返回
    reimbursement.get_elements_click(1, reimbursement.get_origin_parent_xpath("返 回"))
    driver.sendkeys(reason, "金额不符")
    driver.click(refuse, timeout=2)
    process.open_reimbursement("ER02208326")
    logger.info("审批驳回后，进入报销单查看分摊")
    reimbursement.Pagescroll(reimbursement.get_xpath("复制"), timeout=2)
    driver.click(reimbursement.get_xpath("分摊费用类型"))
    reimbursement.Pagescroll(reimbursement.get_xpath("费用分摊"), timeout=1)
    assert process.get_elements_attribute(6, amount_input, "value") == "33.34"
    driver.click(save)
    driver.click(submit_expense, timeout=2)
    process.continue_submit()
    # 报销单审批通过
    process.approve("ER02208326")
    process.enter_Financial_audit("ER02208326")
    driver.click(reimbursement.get_xpath("查看"))
    sleep(3)
    reimbursement.Pagescroll(reimbursement.get_xpath("费用分摊"), timeout=1)
    assert driver.get_text(apportion_line3) == "33.34%"



























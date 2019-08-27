from time import sleep
import pytest
from HLY_Elements.expense import elTravel_approval_can_be_printed
from HLY_Elements.expense.elExpense import submit_expense
from HLY_Elements.expense.elFinanciaCheck import Printing_button1, Printing_button
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense
from config.api_urls import reimbursement_look


@pytest.mark.run
def test_bug_24399_bill_refuse(enter):
    """
    bug描述：报销单驳回时一直转圈
    :return:
    """
    driver = enter.driver
    process = Process(driver)
    reimbursement = Reimbursement(driver)
    # 选择报销单类型(日常报销单-UI自动化)
    my_expense = My_Expense(driver)
    my_expense.Newexpense("差旅报销单-bug23062")
    driver.click(elTravel_approval_can_be_printed.form1, timeout=3)
    reimbursement.get_elements_click(2, elTravel_approval_can_be_printed.apply_number)
    reimbursement.get_elements_click(1, elTravel_approval_can_be_printed.confirm)
    # 新建报销单
    driver.click(reimbursement.get_parent_xpath("新 建"))
    # 新建一笔费用
    sleep(3)
    process.create_expense("100", "大巴")
    sleep(2)
    # 提交报销单,并且拿到报销单号
    business_Code = process.get_businessCode()
    driver.click(submit_expense, timeout=2)
    process.continue_submit()
    sleep(5)
    process.open_reimbursement(business_Code)
    # 撤回报销单
    sleep(4)
    process.cancel_bill()
    # 再次提交报销单
    sleep(3)
    bill_status = reimbursement.get_bill_status()
    assert bill_status == "已撤回"
    process.open_reimbursement(business_Code)
    # 撤回报销单后再次进入可见提交按钮
    assert driver.is_exist(submit_expense)
    driver.click(submit_expense, timeout=2)
    process.continue_submit()
    # 审批驳回
    process.approve_refuse(business_Code)
    # 进入到报销单的页面
    sleep(2)
    reimbursement.get_url(reimbursement_look)
    bill_status2 = reimbursement.get_bill_status()
    assert bill_status2 == "已驳回"
    # 再次提交报销单
    process.open_reimbursement(business_Code)
    sleep(3)
    assert driver.is_exist(submit_expense)
    driver.click(submit_expense)
    process.continue_submit()
    # 审批通过
    sleep(3)
    process.approve(business_Code)
    # 再次审批通过
    sleep(4)
    process.approve(business_Code)
    # 查看打印按钮
    process.open_reimbursement(business_Code)
    sleep(3)
    Printing = driver.find_element_by_xpath(Printing_button1).text
    assert driver.is_exist(Printing_button)
    assert Printing == "打 印"
    # 财务审核通过
    process.Financial_audit(business_Code)

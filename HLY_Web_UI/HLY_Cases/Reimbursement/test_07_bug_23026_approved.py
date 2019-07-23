from time import sleep
import pytest
from HLY_Elements.expense import elTravel_approval_can_be_printed
from HLY_Elements.expense.elApprove import wait_approve
from HLY_Elements.expense.elExpense import submit_expense
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense


@pytest.mark.run
def test_bug_23026_approved(enter):
    """
    bug描述：单据修改金额后没有重新审批，直接审核通过了
    :param enter:
    :return:
    """
    driver = enter.driver
    process = Process(driver)
    reimbursement = Reimbursement(driver)
    my_expense = My_Expense(driver)
    my_expense.Newexpense("差旅报销单-bug23062")
    driver.click(elTravel_approval_can_be_printed.form1)
    reimbursement.get_elements_click(1, elTravel_approval_can_be_printed.apply_number)
    reimbursement.get_elements_click(1, elTravel_approval_can_be_printed.confirm)
    # 新建报销单
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=2)
    # 新建一笔费用
    sleep(2)
    process.create_expense("100", "大巴")
    sleep(5)
    # 提交报销单
    business_Code = process.get_businessCode()
    driver.click(submit_expense)
    process.continue_submit()
    # 本部门经理审批
    sleep(5)
    process.approve(business_Code)
    sleep(5)
    # 副经理审批
    process.approve(business_Code)
    # 财务驳回
    sleep(2)
    process.Financial_audit_refuse(business_Code)
    sleep(5)
    process.open_reimbursement(business_Code)
    sleep(5)
    # 下滑到费用
    reimbursement.Pagescroll(reimbursement.get_xpath("大巴"), timeout=2)
    driver.click(reimbursement.get_xpath("大巴"))
    my_expense.InputExpense_Amount(500)
    # 修改费用
    sleep(3)
    my_expense.ClickExpenseSave(timeout=1)
    # 提交报销单
    sleep(3)
    my_expense.summit_expense()
    process.continue_submit()
    process.open_reimbursement(business_Code)
    # 进入审批历史
    process.enter_approve_process()
    sleep(3)
    text = process.find_approve_process(wait_approve)
    assert text == "等待审批"
    businessCode = process.find_approve_bill(business_Code)
    assert driver.is_exist(businessCode)

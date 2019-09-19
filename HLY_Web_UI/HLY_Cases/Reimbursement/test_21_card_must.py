from time import sleep

from HLY_Elements.FinancialManagement.ReimbursementView_element import select_applys
from HLY_Elements.expense import elTravel_approval_can_be_printed
from HLY_Elements.expense.elExpense import select_expense_frame, select_apply
from HLY_Elements.expense.elFinanciaCheck import apply_search, search_reason
from HLY_Elements.expense.elReimbursement import Receipt, PaymentUse, card_select
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.Profile import Profile
from HLY_PageObject.UI.my_expense.my_expense import My_Expense


def test_5483_card_must(enter):
    """
    需求5483：报销单付款行的付款方银行卡必填逻辑
    case06:report.property.card. required=开启，修改付款行信息时，提示
    :param enter:
    :return:
    """
    driver = enter.driver
    process = Process(driver)
    reimbursement = Reimbursement(driver)
    my_expense = My_Expense(driver)
    profile = Profile(driver)
    sleep(3)
    profile.card_open()
    my_expense.Newexpense("差旅报销单-新建他人费用")
    # 选择一个部门
    driver.click(elTravel_approval_can_be_printed.form1, timeout=2)
    reimbursement.get_elements_sendKey(0, apply_search, "test修改付款行信息时，提示")
    reimbursement.get_elements_click(0, search_reason)
    # 选择关联的申请单
    reimbursement.get_elements_click(0, reimbursement.get_origin_xpath("TA00789841"))
    reimbursement.get_elements_click(1, elTravel_approval_can_be_printed.confirm)
    # 创建报销单
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    process.create_other_expense("100", "大巴")
    reimbursement.Pagescroll(reimbursement.get_xpath("大巴"), timeout=1)
    # 选中付款的费用
    sleep(3)
    els = driver.find_elements_by_xpath(select_expense_frame)
    els[1].click()
    driver.click(reimbursement.get_parent_xpath("选择费用添加付款行"))
    # 点击收款方并选择一个收款方
    driver.click(Receipt, timeout=2)
    sleep(2)
    reimbursement.get_elements_click(3, reimbursement.get_origin_xpath("我没有银行卡"))
    reimbursement.get_elements_click(2, reimbursement.get_origin_parent_xpath("确 定"))
    # 选择员工报销
    driver.click(PaymentUse)
    reimbursement.get_elements_click(1, reimbursement.get_origin_xpath("员工报销"), timeout=2)
    # 点击保存
    sleep(2)
    reimbursement.get_elements_click(1, reimbursement.get_origin_parent_xpath("保 存"))
    assert driver.get_text(card_select, timeout=2) == "请选择"
    profile.card_close()


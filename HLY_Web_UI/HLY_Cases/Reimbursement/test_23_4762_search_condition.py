from time import sleep
from HLY_Elements.FinancialManagement.ReimbursementView_element import reimbursement_applicant_frame, \
    apply_applicant_frame, info_number, approve_applicant_frame
from HLY_Elements.expense.elFinanciaCheck import apply_search, search_reason
from HLY_PageObject.UI.Reimbursement import Reimbursement
from config.api_urls import reimbursement_look, apply_url, approve_url


def test_search_condition(enter):
    """
    需求：4762 报销单增加搜索条件“申请人”
    :param enter:前置条件
    :return:
    """
    driver = enter.driver
    reimbursement = Reimbursement(driver)
    reimbursement.get_url(reimbursement_look)
    driver.click(reimbursement_applicant_frame)
    assert len(driver.find_elements_by_xpath(info_number)) == 10

    # case1 报销单的界面
    # 搜索工号
    reimbursement.get_elements_sendKey(0, apply_search, "10086")
    reimbursement.get_elements_click(1, search_reason)
    assert driver.get_text(reimbursement.get_xpath("我没有银行卡")) == "我没有银行卡"
    # 搜索电话号
    reimbursement.get_elements_sendKey(0, apply_search, "13700000010")
    sleep(1)
    reimbursement.get_elements_click(1, search_reason)
    assert driver.get_text(reimbursement.get_xpath("我没有银行卡"), timeout=1) == "我没有银行卡"
    # 搜索姓名
    reimbursement.get_elements_sendKey(0, apply_search, "我没有银行卡")
    reimbursement.get_elements_click(1, search_reason)
    assert driver.get_text(reimbursement.get_xpath("10086")) == "10086"
    # case2申请单的界面
    reimbursement.get_url(apply_url)
    driver.click(apply_applicant_frame)
    assert len(driver.find_elements_by_xpath(info_number)) == 10
    # 搜索工号
    reimbursement.get_elements_sendKey(0, apply_search, "10086")
    reimbursement.get_elements_click(1, search_reason)
    assert driver.get_text(reimbursement.get_xpath("我没有银行卡")) == "我没有银行卡"
    # 搜索电话号
    reimbursement.get_elements_sendKey(0, apply_search, "13700000010")
    reimbursement.get_elements_click(1, search_reason)
    assert driver.get_text(reimbursement.get_xpath("我没有银行卡")) == "我没有银行卡"
    # 搜索姓名
    reimbursement.get_elements_sendKey(0, apply_search, "我没有银行卡")
    reimbursement.get_elements_click(1, search_reason)
    assert driver.get_text(reimbursement.get_xpath("10086")) == "10086"
    # case3报销单审批列表
    reimbursement.get_url(approve_url)
    driver.click(approve_applicant_frame)
    # 搜索工号
    reimbursement.get_elements_sendKey(0, apply_search, "10086")
    reimbursement.get_elements_click(1, search_reason)
    assert driver.get_text(reimbursement.get_xpath("我没有银行卡")) == "我没有银行卡"
    # 搜索电话号
    reimbursement.get_elements_sendKey(0, apply_search, "13700000010")
    reimbursement.get_elements_click(1, search_reason)
    assert driver.get_text(reimbursement.get_xpath("我没有银行卡")) == "我没有银行卡"
    # 搜索姓名
    reimbursement.get_elements_sendKey(0, apply_search, "我没有银行卡")
    reimbursement.get_elements_click(1, search_reason)
    assert driver.get_text(reimbursement.get_xpath("10086")) == "10086"









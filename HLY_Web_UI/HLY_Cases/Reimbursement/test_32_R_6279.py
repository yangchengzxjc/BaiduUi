from time import sleep
from HLY_Elements.expense.elFinanciaCheck import book_enter, yangzhao_expensetype, \
    select_button_confirm
from HLY_Elements.expense.elReimbursement import cause, submit_expense
from HLY_PageObject.API.apis import expense_yangzhao
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense
from common.log import logger


def test_32_R_6279_1(enter):
    """
    bug描述：在报销单内保存费用（滴滴，扬招）提示必填字段为空，但是必填字段都已填写
    :param enter:
    :return:
    """
    driver = enter.driver
    process = Process(driver)
    reimbursement = Reimbursement(driver)
    # 选择报销单类型(日常报销单)
    my_expense = My_Expense(driver)
    my_expense.Newexpense("日常报销单01")
    sleep(3)
    reimbursement.get_elements_sendKey(1, cause, "日常报销单01，报销单单据上成本中心、部门需展示code和名称测试")
    logger.info("选择部门")
    reimbursement.get_elements_click(0,"//*[@title='部门']/parent::div/following-sibling::div/div/div/span/div/div/div",timeout=2)
    reimbursement.get_elements_click(0, "//*[@class='ant-table-tbody']/tr[1]/td[1]",timeout=2)
    OrgDepartmentCode=reimbursement.get_elements_text(0, "//*[@class='ant-table-tbody']/tr[1]/td[2]/span",timeout=2)
    OrgDepartmentName=reimbursement.get_elements_text(0, "//*[@class='ant-table-tbody']/tr[1]/td[3]/span",timeout=2)
    reimbursement.get_elements_click(0, "//*[text()='确 定']/..", timeout=2)


    logger.info("成本中心")
    reimbursement.get_elements_click(0,"//*[@title='成本中心']/parent::div/following-sibling::div/div/div/span/div/div/div",timeout=2)
    reimbursement.get_elements_click(0, "//*[text()='成本中心名称']/parent::th/parent::tr/parent::thead/following-sibling::tbody/tr[1]/td[1]",timeout=2)
    OrgCostCenterCode=reimbursement.get_elements_text(0, "//*[text()='成本中心名称']/parent::th/parent::tr/parent::thead/following-sibling::tbody/tr[1]/td[2]/span",timeout=2)
    OrgCostCenterName=reimbursement.get_elements_text(0, "//*[text()='成本中心名称']/parent::th/parent::tr/parent::thead/following-sibling::tbody/tr[1]/td[3]/span",timeout=2)
    reimbursement.get_elements_click(1, "//*[text()='确 定']/..", timeout=2)

    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)

    DepartmentName=reimbursement.get_elements_text(0,"//*[text()='编辑']/ancestor::h3/following-sibling::div/div[1]/div[2]/span",3)
    DepartmentCode = reimbursement.get_elements_text(0, "//*[text()='编辑']/ancestor::h3/following-sibling::div/div[1]/div[2]/span/span",3)

    CostCenterName = reimbursement.get_elements_text(0, "//*[text()='编辑']/ancestor::h3/following-sibling::div/div[2]/div[2]/span", 3)
    CostCenterCode = reimbursement.get_elements_text(0, "//*[text()='编辑']/ancestor::h3/following-sibling::div/div[2]/div[2]/span/span",3)

    # logger.info(DepartmentName)
    # logger.info(DepartmentCode)
    # logger.info(CostCenterName)
    # logger.info(CostCenterCode)

    assert  (CostCenterName.find(OrgCostCenterCode)!=-1)
    assert (CostCenterName.find(OrgCostCenterName) != 1)
    assert  (DepartmentName.find(OrgDepartmentName) !=1)
    assert  (DepartmentName.find(OrgDepartmentCode) != 1)


def test_32_R_6279_2(enter):
    """
    bug描述：在报销单内保存费用（滴滴，扬招）提示必填字段为空，但是必填字段都已填写
    :param enter:
    :return:
    """
    driver = enter.driver
    process = Process(driver)
    reimbursement = Reimbursement(driver)
    # 选择报销单类型(日常报销单)
    my_expense = My_Expense(driver)
    my_expense.Newexpense("日常报销单02")
    sleep(3)
    reimbursement.get_elements_sendKey(1, cause, "日常报销单02，报销单单据上成本中心、部门需展示code和名称测试")
    logger.info("选择部门")
    reimbursement.get_elements_click(0,"//*[@title='部门']/parent::div/following-sibling::div/div/div/span/div/div/div",timeout=2)
    reimbursement.get_elements_click(0, "//*[@class='ant-table-tbody']/tr[1]/td[1]",timeout=2)
    OrgDepartmentCode=reimbursement.get_elements_text(0, "//*[@class='ant-table-tbody']/tr[1]/td[2]/span",timeout=2)
    OrgDepartmentName=reimbursement.get_elements_text(0, "//*[@class='ant-table-tbody']/tr[1]/td[3]/span",timeout=2)
    reimbursement.get_elements_click(0, "//*[text()='确 定']/..", timeout=2)


    logger.info("成本中心")
    reimbursement.get_elements_click(0,"//*[@title='成本中心']/parent::div/following-sibling::div/div/div/span/div/div/div",timeout=2)
    reimbursement.get_elements_click(0, "//*[text()='成本中心名称']/parent::th/parent::tr/parent::thead/following-sibling::tbody/tr[1]/td[1]",timeout=2)
    OrgCostCenterCode=reimbursement.get_elements_text(0, "//*[text()='成本中心名称']/parent::th/parent::tr/parent::thead/following-sibling::tbody/tr[1]/td[2]/span",timeout=2)
    OrgCostCenterName=reimbursement.get_elements_text(0, "//*[text()='成本中心名称']/parent::th/parent::tr/parent::thead/following-sibling::tbody/tr[1]/td[3]/span",timeout=2)
    reimbursement.get_elements_click(1, "//*[text()='确 定']/..", timeout=2)

    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)

    DepartmentName=reimbursement.get_elements_text(0,"//*[text()='编辑']/ancestor::h3/following-sibling::div/div[1]/div[2]/span",3)
    CostCenterName = reimbursement.get_elements_text(0,"//*[text()='编辑']/ancestor::h3/following-sibling::div/div[2]/div[2]/span", 3)
    logger.info(DepartmentName)
    logger.info(CostCenterName)

    # assert (CostCenterName.find(OrgCostCenterName) != 0)
    # logger.info(DepartmentName.find(OrgCostCenterCode))
    # assert  (CostCenterName.find(OrgCostCenterCode)==0)
    #
    # assert  (DepartmentName.find(OrgDepartmentName) !=0)
    # assert  (DepartmentName.find(OrgDepartmentCode) == 0)
    # logger.info (DepartmentName.find(OrgDepartmentCode))




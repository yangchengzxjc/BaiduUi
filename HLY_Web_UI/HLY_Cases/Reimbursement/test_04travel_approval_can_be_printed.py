from time import sleep

from HLY_Elements.expense import elTravel_approval_can_be_printed
from HLY_Elements.expense.elFinanciaCheck import Printing_button1, Printing_button
from HLY_Elements.expense.elReimbursement import department, submit_expense
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.Profile import Profile
from HLY_PageObject.UI.my_expense.my_expense import My_Expense
from common.log import logger


def test_travel_approval_can_be_peinted(enter):
    """
    差旅报销单审核通过后可打印，使用差旅报销单-新建他人费用的表单
    bug编号19326，用户差旅报销单默认部门信息卡屏了，默认的部门信息没有被带过来
    :param enter:
    :return:
    """
    driver = enter.driver
    profile = Profile(driver)
    process = Process(driver)
    reimbursement = Reimbursement(driver)
    # 选择差旅报销单-新建他人费用的表单
    my_expense = My_Expense(driver)
    sleep(3)
    profile.print_open()
    my_expense.Newexpense("差旅报销单-UI自动化")
    # 点击关联申请单
    driver.click(elTravel_approval_can_be_printed.form1)
    reimbursement.get_elements_click(0, reimbursement.get_origin_xpath("test部门信息"))
    reimbursement.get_elements_click(1, elTravel_approval_can_be_printed.confirm)
    # reimbursement.get_elements_click(0,elTravel_approval_can_be_printed.apply_number)
    # 点击确定，选择申请单完毕
    sleep(3)
    # 拿到部门信息
    department_info=reimbursement.get_elements_text(0,reimbursement.replace_element(department,"部门",reimbursement.get_department()))
    logger.info(department_info)
    assert department_info == reimbursement.get_department()
    # 创建报销单
    driver.click(reimbursement.get_parent_xpath("新 建"))
    logger.info("新建报销单")
    # 创建一笔费用
    sleep(2)
    process.create_expense("200","大巴")
    business_Code = process.get_businessCode()
    sleep(3)
    # 提交报销单
    driver.click(submit_expense)
    logger.info("提交报销单")
    process.continue_submit()
    sleep(5)
    # 审批通过
    process.approve(business_Code)
    # 财务审核通过
    process.Financial_audit(business_Code)
    # 打开报销单
    process.open_reimbursement(business_Code)
    sleep(5)
    a = driver.find_element_by_xpath(Printing_button1).text
    assert driver.is_exist(Printing_button)
    assert a == "打 印"



from time import sleep
from HLY_Elements.expense import elTravel_approval_can_be_printed
from HLY_Elements.expense.elExpense import invoice_type, invoice_code, invoice_number, invoice_date, page_turning, date, \
    invoice_money, month, submit_expense
from HLY_Elements.expense.elFinanciaCheck import financial_apportion1, financial_apportion2, origin_amout, \
    delete_invoice, financial_apportion3, financial_apportion4, new_amount
from HLY_Elements.expense.elReimbursement import invoice_amount, pay_amount, cause
from HLY_Elements.expense.elTravel_approval_can_be_printed import rate_save
from HLY_Elements.expense.expense_type import approation_amount1, approation_amount2
from HLY_PageObject.UI.Process import Process
from HLY_PageObject.UI.Reimbursement import Reimbursement
from HLY_PageObject.UI.my_expense.my_expense import My_Expense
from common.log import logger


def test_apportion_line(enter):
    """
    bug:31066:报销单费用分摊金额相加的总额与费用总金额不对等，相差金额很大
    流程:针对外币的分摊，普通费用，普通分摊，外币费用进行测试
    :param enter:
    :return:
    """
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
    reimbursement.get_elements_sendKey(1, cause, "test分摊金额和报销单总金额是否一致")
    # 新建报销单
    driver.click(reimbursement.get_parent_xpath("新 建"), timeout=1)
    sleep(5)
    process.new_apporation_expense("43.45", "22.13", "测试部门", currency="HKD", flag=True)
    # 创建本位币分摊费用43.45元
    process.new_apporation_expense("43.45", "22.13", "测试部门")
    # 创建大巴的普通费用类型
    process.create_expense("35.23", "大巴")
    # 创建港币的费用
    process.create_expense("33.87", "大巴", currency="HKD", flag=True)
    # 单据金额和付款金额是否一致
    assert driver.get_text(pay_amount) == "140.54"
    logger.info("单据的付款金额：%s" % driver.get_text(pay_amount))
    businessCode = process.get_businessCode()
    assert driver.get_text(invoice_amount) == driver.get_text(pay_amount)
    # 报销单提交
    driver.click(submit_expense)
    process.continue_submit()
    # 报销单驳回
    process.approve_refuse(businessCode)
    # 进入报销单
    process.open_reimbursement(businessCode)
    sleep(4)
    # 提交报销单
    driver.click(submit_expense)
    process.continue_submit()
    # 再次进入报销单查看费用
    process.open_reimbursement(businessCode)
    # 查看费用滑动到费用信息
    process.Pagescroll(reimbursement.get_xpath("全部费用"), timeout=1)
    logger.info("滑动到费用信息")
    process.get_elements_click(1, process.get_origin_xpath("查看"))
    logger.info("进入费用并查看")
    sleep(1)
    process.Pagescroll(reimbursement.get_xpath("费用分摊"), timeout=1)
    assert round(float(driver.get_text(approation_amount1))+float(driver.get_text(approation_amount2)), 2) == 43.45
    logger.info("驳回后的分摊行的费用:%s %s" % (driver.get_text(approation_amount1), driver.get_text(approation_amount2)))
    # 审批通过
    process.approve("ER02208786")
    # 进入单据审核
    process.enter_Financial_audit(businessCode)
    # 选择第二笔费用进行录入发票
    process.get_elements_click(1, process.get_origin_xpath("查看"))
    logger.info("点击查看")
    # 录入发票
    driver.click(process.get_xpath("添加发票"), timeout=2)
    driver.click(reimbursement.get_xpath("手动录入"), timeout=2)
    logger.info("录入发票,手工录入")
    driver.click(invoice_type)
    logger.info("选择发票类型")
    driver.click(process.get_xpath("增值税专用发票"))
    driver.sendkeys(invoice_code, "6100183130")
    logger.info("输入发票代码")
    driver.sendkeys(invoice_number, "04853654")
    logger.info("输入发票号码")
    driver.click(invoice_date)
    logger.info("点击开票日期")
    driver.click(month, timeout=2)
    logger.info("点击选择月份")
    sleep(2)
    driver.click(process.get_xpath("一月"))
    sleep(2)
    driver.click(process.get_xpath("5"))
    logger.info("点击1月5号")
    driver.sendkeys(invoice_money, "146.03")
    driver.click(process.get_parent_xpath("确 定"))
    # assert driver.get_text(origin_amout, timeout=4) == "37.46"
    reimbursement.Pagescroll(reimbursement.get_xpath("费用分摊"), timeout=4)
    assert driver.get_text(financial_apportion1, timeout=1) == "19.08"
    assert driver.get_text(financial_apportion2, timeout=1) == "18.38"
    reimbursement.Pagescroll(delete_invoice, timeout=1)
    driver.click(delete_invoice, timeout=2)
    driver.click(reimbursement.get_parent_xpath("确 定"), timeout=2)
    # 删除发票后分摊费用更新
    reimbursement.Pagescroll(reimbursement.get_xpath("费用分摊"), timeout=4)
    assert driver.get_text(financial_apportion3, timeout=1) == "22.13"
    assert driver.get_text(financial_apportion4, timeout=1) == "21.32"
    reimbursement.Pagescroll(reimbursement.get_xpath("核定金额"), timeout=2)
    driver.click(reimbursement.get_xpath("核定金额"))
    driver.sendkeys(new_amount, "43.33")
    process.get_elements_click(1, reimbursement.get_origin_parent_xpath("保 存"))
    process.get_elements_click(1, process.get_origin_xpath("查看"))
    reimbursement.Pagescroll(reimbursement.get_xpath("费用分摊"), timeout=4)
    assert driver.get_text(financial_apportion3, timeout=1) == "22.07"
    assert driver.get_text(financial_apportion4, timeout=1) == "21.26"


















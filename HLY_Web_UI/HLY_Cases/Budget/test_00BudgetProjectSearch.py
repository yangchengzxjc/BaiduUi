from time import sleep
from HLY_Elements.BudgetElements.BudgetElement import First_Project_Mapping, project_name, source_type, search_result, \
    search_frame
from HLY_PageObject.UI.Reimbursement import Reimbursement


def test00_BudgetProjectSearch(enter_group):
    """
    需求5484：预算项目映射时，选择预算项目时，支持按照预算项目名称搜索
    :param enter_Budget:
    :return:
    """
    driver = enter_group.driver
    reimbursement = Reimbursement(driver)
    # 点击预算设置
    driver.click(reimbursement.get_xpath("预算设置"), timeout=2)
    # 点击进入预算组织定义
    driver.click(reimbursement.get_xpath("预算组织定义"), timeout=2)
    # 点击任意一个组织
    driver.click(reimbursement.get_xpath("启用"))
    # 点击进入预算项目映射
    driver.click(reimbursement.get_xpath("预算项目映射"))
    # 选择第一个的预算项目
    driver.click(First_Project_Mapping)
    # case 2
    driver.sendkeys(search_frame, "p0001", timeout=2)
    assert driver.get_text(search_result, timeout=1) == "交通费"
    driver.sendkeys(search_frame, "交通", timeout=1)
    assert driver.get_text(search_result) == "交通费"
    # 下面是case1
    # 点击添加
    driver.click(reimbursement.get_parent_xpath("添 加"), timeout=1)
    # 选择来源类别
    sleep(2)
    driver.click(source_type)
    # 选择来源类别
    reimbursement.get_elements_click(2, reimbursement.get_origin_xpath("费用类型"))
    # 预算项目搜索
    reimbursement.get_elements_click(5, reimbursement.get_origin_xpath("请选择"))
    assert driver.get_text(reimbursement.get_xpath("名称")) == "名称"
    driver.sendkeys(project_name, "交通费")
    reimbursement.get_elements_click(1, reimbursement.get_origin_parent_xpath("搜 索"))
    assert driver.get_text(reimbursement.get_xpath("共搜索到 1 条数据")) == "共搜索到 1 条数据 / 已选 0 条"
    assert reimbursement.get_elements_text(1,reimbursement.get_origin_xpath("交通费"))
    reimbursement.get_elements_click(1,reimbursement.get_origin_parent_xpath("清 空"))
    assert driver.getAttribute(project_name,"value") == ""




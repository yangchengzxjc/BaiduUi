from time import sleep

import pytest
from selenium.webdriver import ActionChains
from HLY_Elements import elPublic
from HLY_Elements.BudgetElements.BudgetElement import hly_guild
from HLY_Elements.elPublic import company_page, select_company
from HLY_PageObject.UI.Reimbursement import Reimbursement
from common.log import logger
from main import glo


@pytest.fixture(scope='module')
def enter(request, prepare):
    """
    进入新中控，并且选择公司为第一个默认公司
    """
    driver, hly = prepare.driver, prepare.hly
    logger.info("进入新中控")
    driver.get('%s://%s/main/dashboard'%(glo.get("Webprotocol"), glo.get("Webhost")))
    sleep(5)
    if driver.is_exist(company_page):
        pass
    else:
        a = driver.get_element(elPublic.mode)
        ActionChains(driver).move_to_element(a).perform()
        # driver.click(elPublic.SwitchingCompany, timeout=3)
        # driver.click(elPublic.SwitchingCompanyOK, 2)
        driver.click(select_company)


    def awardbacktohome():
        """
        用例执行完返回首页
        """
        import  time
        time.sleep(2)
        hly.go_main()
    request.addfinalizer(awardbacktohome)
    return prepare

@pytest.fixture(scope='module')
def enter_group(request, prepare):
    """
    进入新中控,不进入公司模式
    """
    driver, hly = prepare.driver, prepare.hly
    reimbursement = Reimbursement(driver)
    logger.info("进入新中控")
    driver.get('%s://%s/main/dashboard'%(glo.get("Webprotocol"),glo.get("Webhost")))
    sleep(2)
    # 进入管理员模式
    driver.click(reimbursement.get_xpath("进入管理后台"), timeout=2)
    if driver.is_exist(hly_guild):
        driver.click(hly_guild,timeout=2)
    else:
        pass

    def awardbacktohome():
        """
        用例执行完返回首页
        """
        import  time
        time.sleep(2)
        driver.get('%s://%s/main/dashboard' % (glo.get("Webprotocol"), glo.get("Webhost")))
        sleep(1)
        driver.click(reimbursement.get_xpath("回到员工模式"))
        hly.go_main()
    request.addfinalizer(awardbacktohome)
    return prepare

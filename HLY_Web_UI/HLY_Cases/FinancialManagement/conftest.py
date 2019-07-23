from time import sleep

import pytest
from selenium.webdriver import ActionChains
from HLY_Elements import elPublic
from HLY_Elements.elPublic import company_page
from common.log import logger
from main import glo


@pytest.fixture(scope='module')
def enter(request, prepare):
    """
    进入新中控，并且选择公司为第一个默认公司
    """
    driver, hly = prepare.driver, prepare.hly
    logger.info("进入新中控")
    driver.get('%s://%s/main/dashboard'%(glo.get("Webprotocol"),glo.get("Webhost")))
    sleep(5)
    if driver.is_exist(company_page):
        pass
    else:
        a = driver.get_element(elPublic.mode)
        ActionChains(driver).move_to_element(a).perform()
        driver.click(elPublic.SwitchingCompany, timeout=3)
        driver.click(elPublic.SwitchingCompanyOK, 2)
    def awardbacktohome():
        """
        用例执行完返回首页
        """
        import  time
        time.sleep(2)
        hly.go_main()
    request.addfinalizer(awardbacktohome)
    return prepare

import pytest
from py.xml import html
from common.baseObject import HLY
from common.log import logger
from common.pyselenium_local import Pyapp_Local
from config import baseinfo as base
from common.loggingUtil import Logging
from common.globalMap import GlobalMap
from common.pyselenium_remote import Pyapp_Remote

__author__ = 'yuezy'

glo = GlobalMap()
# flag = glo.get('ip')


# 依次实例driver sqlserver mysql
Flag =glo.get("flag")
logger.info("运行方式：%s" % Flag)
if Flag == "remote":
    driver = Pyapp_Remote(base.url, base.default_desired_caps)
else:
    driver = Pyapp_Local()
driver.implicitly_wait(30)
driver.maximize_window()
url = "%s://%s" % (glo.get("Webprotocol"), glo.get('Webhost'))
driver.get(url)
# driver.minimize_window()
# driver.refresh()

# 依次将driver sqlserver mysql三个实例塞进全局变量,方便需要时调用
glo.set("driver", driver)
hly = HLY()


# def pytest_configure(config):
#     config._metadata = {
#         'Os Version': base.desired_capabilities['platformName'] + ' ' + base.desired_capabilities['platformVersion'],
#         'Phone Model': base.desired_capabilities['deviceName'],
#     }
#     try:
#         package = glo.get('package')
#         # if flag == 'ios':
#         #     config._metadata['Package name'] = package
#         config._metadata['Package name'] = package
#     except KeyError:
#         pass
@pytest.mark.optionalhook
def pytest_html_results_table_header(cells):
    """
    定制测试报告表头
    """
    cells.insert(1, html.th('Test'))
    cells.pop(2)
    cells.pop()


@pytest.mark.optionalhook
def pytest_html_results_table_row(report, cells):
    """
    定制测试报告行内容
    """
    if report.outcome == 'rerun':
        del cells[:]
    else:
        try:
            cells.insert(1, html.td(report.description))
        except AttributeError:
            # print('测试用例已执行完成，或测试用例无description')
            pass
        cells.pop(2)
        cells.pop()


@pytest.mark.hookwrapper
def pytest_runtest_makereport(item):
    """
    Extends the PyTest Plugin to take and embed screenshot in html report, whenever test fails.
    :param item:
    """
    pytest_html = item.config.pluginmanager.getplugin('html')
    outcome = yield
    report = outcome.get_result()
    extra = getattr(report, 'extra', [])
    if report.when == 'call' or report.when == "setup":
        report.description = str(item.function.__doc__) + "  -- -  " + report.nodeid.split("::")[0].split("/")[-1]
        xfail = hasattr(report, 'wasxfail')
        if (report.skipped and xfail) or (report.failed and not xfail):
            screenshot_base64 = driver.get_windows_img_base64()
            if screenshot_base64:
                html = '<div><img src ="Data: image / png;base64,%s" alt="screenshot" style="width:250px;height:250px;" ' \
                       'onclick="window.open(this.src)" align="right" /></div>' % screenshot_base64
                extra.append(pytest_html.extras.html(html))
        report.extra = extra


@pytest.fixture(scope='module', autouse=True)
def add_log(request):
    Logging.set_logger_name(request.module.__name__)
    logger = Logging.get_logger()
    logger.info('\n>>>>>>>>>>%s start>>>>>>>>>>\n' % request.module.__name__)

    def fin():
        logger.info('\n<<<<<<<<<<%s end<<<<<<<<<<\n' % request.module.__name__)

    request.addfinalizer(fin)
    return logger


@pytest.fixture(scope='session', autouse=True)
def prepare(request):
    """
    全局UI登录及API登录
    """
    hly.login()
    hly.apilogin()
    def fin():
        """
        所有用例测试完成退出浏览器
        """
        driver.quit()
    request.addfinalizer(fin)
    class Tmp:
        driver, hly, glo = driver, hly, glo
    return Tmp()
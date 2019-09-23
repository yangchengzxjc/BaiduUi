import os
import sys
import pytest
from common.parameter import  GetConfigp as pa
from common.globalMap import GlobalMap
from common.log import logger
__author__ = 'yuezy'

glo = GlobalMap()
if not os.path.exists("logs"):
    os.mkdir('logs')


def parse_command_params():
    """
    解析命令行参数
    """
    command_argv = sys.argv
    logger.info('执行脚本的参数： %s' % command_argv)
    glo.set("value", command_argv[1])
    glo.set('ip',command_argv[2])
    glo.set("port",command_argv[3])
    glo.set("flag",command_argv[4])

# def set_env(ip='127.0.0.1', port=4444, env='stage'):
#
#     glo.set('port', port)  # appium服务端口
#     glo.set('ip', ip)  # appium服务所在机器的ip地址
#     glo.set('env', env)  # 指定调用接口使用test环境

def set_env2():
    parse_command_params()
    env = glo.get("value")
    logger.info("env=="+env)
    pp = pa('./config/hly.config')
    glo.set("env",env)
    if env == "stage":
        glo.set("Webprotocol","http")
        glo.set("Webhost","stage.huilianyi.com")
        glo.set("WebUsername","17960000000")
        glo.set("version","预算版本")
        glo.set("struct", "预算表")
        glo.set("scene", "预算场景1")
        glo.set("year", "2019")
        glo.set("Period","2019-5")
    if env == pp.getoption("ALPROD","ENV"):
        glo.set("Webprotocol",pp.getoption("ALPROD","protocol"))
        glo.set("Webhost", pp.getoption("ALPROD", "host"))
        glo.set("WebUsername", pp.getoption("ALPROD", "username"))
        glo.set("version", "预算当前版本")
        glo.set("struct", "预算表")
        glo.set("scene", "预算场景")
        glo.set("year", "2019")
        glo.set("Period", "2019-YS_5")
    if env == pp.getoption("TCPROD","ENV"):
        glo.set("Webprotocol", pp.getoption("TCPROD", "protocol"))
        glo.set("Webhost", pp.getoption("TCPROD", "host"))
        glo.set("WebUsername", pp.getoption("TCPROD", "username"))
        glo.set("version", "预算当前版本")
        glo.set("struct", "预算表")
        glo.set("scene", "预算场景")
        glo.set("year", "2019")
        glo.set("Period", "2019-YS_5")
    if env == pp.getoption("MLPROD","ENV"):
        glo.set("Webprotocol", pp.getoption("MLPROD", "protocol"))
        glo.set("Webhost", pp.getoption("MLPROD", "host"))
        glo.set("WebUsername", pp.getoption("MLPROD", "username"))


def get_report_name(prefix='汇联易国内版本自动化'):
    device_info = glo.get('Webhost')
    report_name='report/%s-%s.html' % (prefix, device_info)
    logger.info("本次出报告:%s" % report_name)
    return report_name


if __name__ == '__main__':
    # set_env(ip='192.168.12.35', env='stage')
    set_env2()
    # init_global_param(udid="3af53e7")
    # 运行指定mark用例

    pytest.main(['--capture=no', '-v',
                 '--html=' + get_report_name(), '--self-contained-html'])
    # print(glo.get('desired_capabilities'))
    # 运行指定mark用例
    # pytest.main(['--capture=sys', '-v', '--html=' + get_report_name(), '--self-contained-html'])
    #
    # 多mark关键字过滤
    # pytest.main(['-s', '-q', '--capture=sys', '-v', '-m', 'not normal and not low',
    #              '--html=report.html', '--self-contained-html'])
    # 指定文件夹+mark
    # pytest.main(["HLY_Cases/Reimbursement/test_30-3_6157_Travel_allowance_08.py", '-s', '-q', '--capture=sys', '-v','--html=' + get_report_name(), '--self-contained-html'])
    # E:/git-zhishinet/APPUI/zhishinet-stuAPPUI/HLY_cases/userinfoModify/test_modify_gender.py
    # E:/git-zhishinet/APPUI/zhishinet-stuAPPUI/HLY_cases/register/test_register_05_invalidphonenum.py
    # E:/git-zhishinet/APPUI/zhishinet-stuAPPUI/HLY_cases/register/test_register_01_codeshort.py
    # E:/git-zhishinet/APPUI/zhishinet-stuAPPUI/HLY_cases/register/test_register_00_codelong.py
    # E:/git-zhishinet/APPUI/zhishinet-stuAPPUI/HLY_cases/register/test_register_07_classwrong.py
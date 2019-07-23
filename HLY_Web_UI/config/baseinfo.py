import json
from common.parameter import  GetConfigp as pa
from common.globalMap import GlobalMap
from main import parse_command_params
parse_command_params()
glo = GlobalMap()

ip = glo.get('ip')
port = glo.get('port')
url = 'http://%s:%s/wd/hub' % (ip, port)
# url = 'http://192.168.12.35:4444/wd/hub'

default_desired_caps = {
        "platformName": "Android",
        "appPackage": "com.hand.kp.StudentClient",
        "appActivity": ".MainActivity",
        "autoWebview": True,  # 自动进入webview
        "automationName": "uiautomator2",
        "newCommandTimeout": 180
    }
env = glo.get("value")
pp = pa('./config/hly.config')
if env == 'stage':
    # 接口信息
    stuProtocol = '%s://'%glo.get("Webprotocol")
    stuHost = '%s'%glo.get("Webhost")
    stuLogin = '/oauth/token'
    stuHeader = {'Content-Type': 'application/x-www-form-urlencoded','Authorization':"Basic QXJ0ZW1pc0FwcDpuTENud2RJaGl6V2J5a0h5dVpNNlRwUURkN0t3SzlJWERLOExHc2E3U09X"}

if env == pp.getoption("ALPROD","ENV"):
    stuProtocol = '%s://' % glo.get("Webprotocol")
    stuHost = '%s' % glo.get("Webhost")
    stuLogin = '/oauth/token'
    stuHeader = {'Content-Type': 'application/x-www-form-urlencoded',
                 'Authorization': "Basic QXJ0ZW1pc0FwcDpuTENud2RJaGl6V2J5a0h5dVpNNlRwUURkN0t3SzlJWERLOExHc2E3U09X"}
if env == pp.getoption("TCPROD","ENV"):
    stuProtocol = '%s://' % glo.get("Webprotocol")
    stuHost = '%s' % glo.get("Webhost")
    stuLogin = '/oauth/token'
    stuHeader = {'Content-Type': 'application/x-www-form-urlencoded',
                 'Authorization': "Basic QXJ0ZW1pc0FwcDpuTENud2RJaGl6V2J5a0h5dVpNNlRwUURkN0t3SzlJWERLOExHc2E3U09X"}
if env ==pp.getoption("MLPROD","ENV"):
    stuProtocol = '%s://' % glo.get("Webprotocol")
    stuHost = '%s' % glo.get("Webhost")
    stuLogin = '/oauth/token'
    stuHeader = {'Content-Type': 'application/x-www-form-urlencoded',
                 'Authorization': "Basic QXJ0ZW1pc0FwcDpuTENud2RJaGl6V2J5a0h5dVpNNlRwUURkN0t3SzlJWERLOExHc2E3U09X"}

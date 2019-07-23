from copy import deepcopy
from time import sleep
from HLY_Elements import elPublic
from config import baseinfo as base
from HLY_Data import dtLogin
from HLY_Elements import elLogin
from common.apiRequest import ApiRequest
from common.log import logger
from common.parameter import  GetConfigp as pa
from common.globalMap import GlobalMap
class HLY:

    def __init__(self):
        self.glo = GlobalMap()
        self.driver = self.glo.get('driver')
        self.pa=pa('./config/hly.config')
    def login(self, password=dtLogin.password, **kwargs):
        logger.info("点击账号登录选项！")
        if self.driver.is_exist(elLogin.AccountLogin):
            self.driver.click(elLogin.AccountLogin)
        else :
            pass
        logger.info("输入用户名")
        sleep(2)
        self.driver.sendkeys(elLogin.username_prod, self.glo.get("WebUsername"), **kwargs)
        logger.info("输入密码")
        self.driver.sendkeys(elLogin.password_prod, password, **kwargs)
        logger.info("点击登录按钮")
        self.driver.click(elLogin.loginbtn_prod, **kwargs)
        import  time
        time.sleep(10)

    def apilogin(self):
    #     """
    #     API登录，为用例调用接口做准备
    #     """
        user = {'username':self.glo.get("WebUsername"), 'password': dtLogin.password,'grant_type': dtLogin.grant_type, 'scope': dtLogin.scope,'token_type':dtLogin.token_type}
        response = ApiRequest().api_request(base.stuLogin, 'post', header=base.stuHeader, rdata=user)
        authheader = deepcopy(base.stuHeader)  # 深度拷贝登录后的header,不影响base.stuheader
        authheader['Authorization'] = "Bearer %s" % response.json()['access_token']

    # application / json
        self.glo.set('header', {"Content-Type": "application/json", "Authorization": "Bearer %s" % response.json()['access_token']})


    def apilogin_agin(self):
    #     """
    #     API登录，为用例调用接口做准备
    #     """
        user = {'username':self.glo.get("WebUsername"), 'password': dtLogin.password,'grant_type': dtLogin.grant_type, 'scope': dtLogin.scope,'token_type':dtLogin.token_type}
        response = ApiRequest().api_request(base.stuLogin, 'post', header=base.stuHeader, rdata=user)
        authheader = deepcopy(base.stuHeader)  # 深度拷贝登录后的header,不影响base.stuheader
        authheader['Authorization'] = "Bearer %s" % response.json()['access_token']
        return {"Content-Type": "application/json", "Authorization": "Bearer %s" % response.json()['access_token']}
    def  go_main(self, **kwargs):
        """
        返回到主界面
        :param kwargs:
        :return:
        """
        logger.info("用例执行结束后，进入主界面")
        self.driver.get('%s://%s%s' %(self.glo.get("Webprotocol"),self.glo.get('Webhost'),self.pa.getoption("BASIC","homeurl")))
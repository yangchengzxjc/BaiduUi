import requests
from requests.exceptions import *
from config import baseinfo as base
from common.loggingUtil import Logging
from common.globalMap import GlobalMap
import json


class ApiRequest:

    def api_request(self, url, method, header=None, rdata=None, rjson=None, timeout=20):
        """
        请求接口
        :param url: 只传路径，如：/test.html
        :param method: post or get
        :param header: 请求头部信息
        :param rdata:  传参对应jmeter中传参parameters
        :param rjson:　传参对应jmeter中传参body HLY_Data
        :param timeout:  请求超时时间
        :return: response
        """
        logger = Logging.get_logger()
        glo = GlobalMap()
        url = base.stuProtocol + base.stuHost + url
        if "http://" not in url and "https://" not in url:
            e = MissingSchema("请检查Url中是否包含http或https")
            logger.exception(e)
            raise e
        if method.lower() not in ['get', 'post']:
            e = InvalidSchema("暂时只支持get/post两种方法")
            logger.exception(e)
            raise e
        if not header:
            header = json.loads(glo.get('header'))
        logger.info("http请求为: %s %s" % (method.upper(), url))
        logger.info("http请求header: %s" % header)
        if method.lower() == 'get':
            logger.info("请求参数: %s" % rdata)
            response = requests.get(url, headers=header, params=rdata, timeout=timeout)
        elif method.lower() == 'post' and rjson:
            logger.info("请求参数: %s" % rjson)
            response = requests.post(url, headers=header, json=rjson, timeout=timeout)
        elif method.lower() == 'post':
            logger.info("请求参数: %s" % rdata)
            response = requests.post(url, headers=header, data=rdata, timeout=timeout)
        else:
            response = None
            logger.error("请检查是否正确传入请求参数")
        logger.info("httpcode:%s, responsetext: %s" % (response.status_code, response.text))
        return response

    def response_json(self, url, method, header=None, rdata=None, rjson=None, timeout=10):
        """
        传入各参数对应api_request
        :return:
        status_code和json格式信息
        """
        response = self.api_request(url, method, header, rdata, rjson, timeout)
        return response.status_code, response.json()

    def response_text(self, url, method, header=None, rdata=None, rjson=None, timeout=10):
        """
        传入各参数对应api_request
        :return:
        返回status_code和text格式信息
        """
        response = self.api_request(url, method, header, rdata, rjson, timeout)
        return response.status_code, response.text()

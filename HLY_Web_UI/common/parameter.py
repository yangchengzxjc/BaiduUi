# coding=utf-8
# !/usr/bin/env python
#====
# Created on 2017年5月27日
# author: chenxinghong
#脚本功能描述：获得参数
#====
import configparser
import sys 
class GetConfigp(object):
    def __init__(self,mail_config_file):
        self.config=configparser.ConfigParser()
        self.mail_config_file=mail_config_file
        self.config.read(mail_config_file,encoding="UTF-8")
        sections = self.config.sections()  
#        self.from_host = self.config.get('TEST', 'host')
#        self.from_port = self.config.get('TEST', 'port')
#        self.login_user = self.config.get('TEST', 'user')
#        self.login_pwd = self.config.get('TEST', 'passwd')
    def getsections(self):
        return self.config.sections()  
    def getoptions(self,section):
        return self.config.options(section)
    def addsections(self,section):
        self.config.add_section(section) 
        self.config.write(open(self.mail_config_file, "w")) 
    def getoption(self,section="self.go.get('Environment')",key=None):
        return self.config.get(section, key)
    def addoption(self,section="self.go.get('Environment')",key="",values=""):
        self.config.set(section, key,values)   
        self.config.write(open(self.mail_config_file, "w"))
    def removeoption(self,section="self.go.get('Environment')",option=None,):
        self.config.remove_option(section, option)
        self.config.write(open(self.mail_config_file, "w"))
    def has_option(self,section="self.go.get('Environment')",option=None,):
        return self.config.has_option(section, option)


        
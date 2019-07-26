from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.by import By
from selenium.common.exceptions import *
from selenium.webdriver.support.select import Select
from time import sleep
from common.log import logger
from common.loggingUtil import Logging

__author__ = 'yuezy'

"""
对APPIUM底层方法进行简单封装，继承APPIUM，可直接使用原生方法。
"""
#coding=utf-8
from selenium import webdriver
#指定运行主机与端口号

class Pyapp_Local(webdriver.Chrome):
    """
    封装selenium常用方法，方便调用
    """
    def __init__(self):
        """
        返回浏览器对象
        """
        super(Pyapp_Local, self).__init__(executable_path="chromedriver")
    def get_element(self, css, s=False, secs=7, poll_frequency=1, timeout=0, display=True, method=None, **kwargs):
        """
        获取并返回元素
        :param css: “id=>username”
        :param s: 为True时代表获取一组元素
        :param secs: 等待元素时间
        :param poll_frequency: 等待元素间隔时间
        :param timeout: 获取元素之前等待n秒
        :param display: 获取的元素是否需要显示
        :param method: 执行的动作,click  clear 等
        :return: element 返回selenium对象或一组元素的列表
        """
        # 由于移动端页面渲染比较慢，对比上个页面源码与当前源码，如果不一致，等待timeout秒
        sleep(timeout)
        patterns = {'id': By.ID,
                    'name': By.NAME,
                    'class': By.CLASS_NAME,
                    'xpath': By.XPATH,
                    'css': By.CSS_SELECTOR,
                    'tagname': By.TAG_NAME,
                    'linktext': By.LINK_TEXT,
                    'partiallinktext': By.PARTIAL_LINK_TEXT}

        if "=>" not in css:
            e = NameError("请传入正确格式, 比如 'id=>username',如查找一组元素，则传入'id=>username', s=True")
            Logging.get_logger().exception(e)
            raise e
        by = css.split("=>")[0]
        value = css.split("=>")[1]
        try:
            if not s:
                if display:
                    ele = WebDriverWait(self, secs, poll_frequency).until(EC.element_to_be_clickable((patterns[by], value)))
                else:
                    ele = WebDriverWait(self, secs, poll_frequency).until(EC.presence_of_element_located((patterns[by], value)))
                if method:  # 尝试操作3次,如果失败,抛出异常
                    times = 0
                    while times < 4:
                        try:
                            return eval("ele.%s" % method)
                        except Exception as e:
                            sleep(1)
                            if times == 3:
                                raise e
                            times += 1
                else:
                    return ele
            elif s and display:
                return WebDriverWait(self, secs, poll_frequency).until(EC.visibility_of_any_elements_located((patterns[by], value)))
            elif s and not display:
                return WebDriverWait(self, secs, poll_frequency).until(EC.presence_of_all_elements_located((patterns[by], value)))
            else:
                return False
        except KeyError:
            e = NameError("""Please input correct element info,'id','name','class','linktext','partiallinktext','xpath','tagname','css'.""")
            Logging.get_logger().exception(e)
            raise e
        except TimeoutException:
            e = TimeoutError("Cannot find element by : %s, please check element's locator" % css)
            if not kwargs and kwargs.get('log', True):
                Logging.get_logger().exception(e, exc_info=True)
            raise e
        except WebDriverException as e:
            Logging.get_logger().error('Locate element error by locator: %s' % css)
            Logging.get_logger().exception(e)
            raise e

    def is_exist(self, css, secs=5, poll_frequency=1, timeout=2, display=True, **kwargs):
        try:
            return self.get_element(css, secs=secs, poll_frequency=poll_frequency, timeout=timeout, display=display, **kwargs)
        except TimeoutError:
            return False

    def max_window(self):
        """
        最大化浏览器.
        """
        self.maximize_window()

    def set_window(self, wide, high):
        """
        设置浏览器大小.
        """
        self.set_window_size(wide, high)

    def sendkeys(self, css, text, timeout=0, clear=True, **kwargs):
        """
        向输入框输入内容.
        """
        if clear:
            self.clear(css, timeout=timeout)
            self.get_element(css, timeout=timeout, method="send_keys('%s')" % text, **kwargs)
            # el = self.get_element(css, timeout=timeout, method="clear()", **kwargs)
            # el.send_keys(text)
        else:
            self.get_element(css, timeout=timeout, method="send_keys('%s')" % text, **kwargs)
    def clear(self, css, timeout=0):
        """
        清空输入框内容.
        """
        logger.info(type(css))
        if 'selenium' not in str(type(css)):
            self.get_element(css, timeout=timeout, method='clear()')
        else:
            css.clear()

    def click(self, css, timeout=0, **kwargs):
        """
        点击任何可点击元素，如：输入框、图片、链接等
        """
        if 'selenium' not in str(type(css)):
            logger.info("元素的类型：%s"%str(type(css)))
            self.get_element(css, timeout=timeout, method='click()', **kwargs)
        else:
            css.click()


    def closes(self):
        """
        关闭浏览器或标签页.
        """
        super().close()

    def quit(self):
        """
        销毁driver并关闭浏览器.
        """
        super().quit()

    def submit(self, css, timeout=0):
        """
        表格提交.
        """
        if 'appium' not in str(type(css)):
            self.get_element(css, timeout=timeout, method='submit()')
        else:
            css.submit()

    def js(self, script,timeout, *args):
        """
        执行 JavaScript scripts.
        """
        import  time
        time.sleep(timeout)
        return self.execute_script(script, *args)

    def getAttribute(self, css, attribute, timeout=0):
        """
        获取元素属性.
        """
        if 'appium' not in str(type(css)):
            return self.get_element(css, timeout=timeout, method="get_attribute('%s')" % attribute)
        else:
            return css.get_attribute(attribute)

    def get_location(self, css, timeout=0):
        """
        获取元素坐标
        """
        if 'appium' not in str(type(css)):
            return self.get_element(css, timeout=timeout, method='location')
        else:
            return css.location

    def get_text(self, css, timeout=0, display=True):
        """
        获取元素的文字信息.
        """

        if 'selenium' not in str(type(css)):
            return self.get_element(css, timeout=timeout, display=display, method='text')
        else:
            return css.text

    def get_display(self, css, timeout=0):
        """
        获取元素是否显示，返回 true or false.
        """
        if 'appium' not in str(type(css)):
            return self.get_element(css, timeout=timeout, method='is_displayed()')
        else:
            return css.is_displayed()

    def get_title(self):
        """
        获取浏览器标题.
        """
        return self.title

    def get_url(self):
        """
        获取当前Url.
        """
        return self.current_url

    def get_windows_img(self, file_path):
        """
        截图.
        """
        self.save_screenshot(file_path)

    def get_windows_img_base64(self):
        """
        截图返回图片base64
        """
        return self.get_screenshot_as_base64()

    def wait(self, secs):
        """
        隐式等待页面加载完成
        """
        self.implicitly_wait(secs)

    def switch_to_frame(self, css, timeout=0):
        """
        切换至frame.
        """
        if 'appium' not in str(type(css)):
            iframe_el = self.get_element(css, timeout=timeout)
            self._switch_to.frame(iframe_el)
        else:
            self._switch_to.frame(css)

    def switch_to_frame_out(self):
        """
        切出frame.
        """
        self._switch_to.default_content()

    def get_cookies(self):
        """
        返回cookies
        """
        cookies = {item['name']: item['value'] for item in self.get_cookies()}
        return cookies

    def timeout(self, sec):
        self.set_page_load_timeout(sec)

    def open_new_window(self, css, timeout=0):
        """
        跳转至新的标签页.
        """
        original_window = self.current_window_handle
        el = self.get_element(css, timeout=timeout)
        el.click()
        all_handles = self.window_handles
        for handle in all_handles:
            if handle != original_window:
                self._switch_to.window(handle)

    def select(self, css, value, timeout=0):
        """
        下拉框内容选择

        举例:
            <select name="NR" id="nr">
                <option value="10" selected="">每页显示10条</option>
                <option value="20">每页显示20条</option>
                <option value="50">每页显示50条</option>
            </select>

            driver.select("#nr", '20')
            driver.select("xpath=>//[@name='NR']", '20')
        """
        if 'appium' not in str(type(css)):
            el = self.get_element(css, timeout=timeout)
            Select(el).select_by_value(value)
        else:
            Select(css).select_by_value(value)
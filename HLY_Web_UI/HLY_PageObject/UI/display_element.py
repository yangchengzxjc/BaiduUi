

def highLightElement(driver,element):
    #封装好的高亮显示页面元素的方法
    #使用JavaScript代码将传入的页面元素对象的背景颜色和边框颜色分别
    #设置为绿色和红色
    driver.execute_script("arguments[0].setAttribute('style',arguments[1]);",
                         element,"background:green ;border:2px solid red;")
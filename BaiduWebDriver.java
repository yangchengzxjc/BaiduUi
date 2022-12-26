package com.test.api.testcase.UiAndintreF;

import org.openqa.selenium.WebDriver;

/**
 * @Author yc
 * @Date 2022/12/12
 * @Version 1.0
 **/

public class BaiduWebDriver {
    public WebDriver driver;
    public String url;


    public BaiduWebDriver(WebDriver driver,String url){
        this.driver = driver;
        this.url = url;
    }


    //public void BrowserWebDriver(String url){
    //    System.setProperty("webdriver.chrome.driver", "D:\\soft\\webdriver\\108\\chromedriver.exe");
    //    driver = new ChromeDriver();
    //    driver.get(url);
    //}
}

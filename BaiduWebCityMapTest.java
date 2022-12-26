package com.test.api.testcase.UiAndintreF;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @Author yc
 * @Date 2022/12/21
 * @Version 1.0
 **/
public class BaiduWebCityMapTest {

    public String url = "https://www.baidu.com";
    private WebDriver driver;
    private BaiduWebUiTestPage baiduWebUiTestPage;

    @BeforeTest(alwaysRun = true)
    public void setUp(){
        System.out.println("打开浏览器");
        System.setProperty("webdriver.chrome.driver", "D:\\soft\\webdriver\\108\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get(url);
        baiduWebUiTestPage = new BaiduWebUiTestPage(driver);
        baiduWebUiTestPage.maximizePage();
    }

    @AfterTest(alwaysRun = true)
    public void TearDown(){
        driver.quit();
    }

    String cityMapButEle1 = "地图";
    @Test(groups = {"Group1"},description = "地图",priority = 10)
    public void test_cityMap(){
        if(baiduWebUiTestPage.isElementExist("linkText",cityMapButEle1)){
            WebElement webElement = baiduWebUiTestPage.FindElementMethod("LText",cityMapButEle1);
            //System.out.println("test_setOptions01");
            webElement.click();
        }
    }
}

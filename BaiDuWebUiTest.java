/*
package com.test.api.testcase.UiAndintreF;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

*/
/**
 * @Author yc
 * @Date 2022/12/15
 * @Version 1.0
 **//*

public class BaiDuWebUiTest {
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

*/
/*    @Test(groups = {"Group"},description = "登录",priority = 1)
    public void LoginBrowser(){
        System.out.println("登录");
        System.setProperty("webdriver.chrome.driver", "D:\\soft\\webdriver\\108\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get(url);
        baiduWebUiTestPage = new BaiduWebUiTestPage(driver);
    }*//*


*/
/*        @AfterTest(alwaysRun = true)*//*

        public void TearDown(){
            driver.quit();
        }

    String newsButEle1 = "新闻";
    String newsButEle2 = ".div[id='headline-tabs'][class='mod-headline-tab']";
    @Test(groups = {"Group1"},description = "新闻",priority = 10)
    public void test_news01(){
        if(baiduWebUiTestPage.isElementExist("linkText",newsButEle1)){
            WebElement webElement = baiduWebUiTestPage.FindElementMethod("LText",newsButEle1);
            //System.out.println("test_setOptions01");
            webElement.click();
            baiduWebUiTestPage.switchToWindow("百度一下，你就知道");
            baiduWebUiTestPage.screenShot(driver);
            if(baiduWebUiTestPage.isElementExist("Css",newsButEle2)){
                WebElement webElement1 = baiduWebUiTestPage.FindElementMethod("Css",newsButEle2);
                String text = webElement1.getText();
                System.out.println(text);
                Assert.assertEquals(text,"热点要闻");
            }else{
                System.out.println(newsButEle2+driver+"元素未找到");
            }
        }
    }

    String hao123ButEle1 = "hao123";
    String hao123ButEle2 = "#govsite-top > a.g-gc.title";
    @Test(groups = {"Group1"},description = "新闻",priority = 10)
    public void test_hao123(){
        if(baiduWebUiTestPage.isElementExist("linkText",hao123ButEle1)){
            WebElement webElement = baiduWebUiTestPage.FindElementMethod("LText",hao123ButEle1);
            //System.out.println("test_setOptions01");
            webElement.click();

            baiduWebUiTestPage.switchToWindow("hao123_上网从这里开始");
            baiduWebUiTestPage.screenShot(driver);
            WebElement webElement1 = baiduWebUiTestPage.FindElementMethod("Css",hao123ButEle2);
            String text = webElement1.getText();
            System.out.println(text);
            Assert.assertEquals(text,"hao123推荐");
        }
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
*/

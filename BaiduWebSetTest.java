package com.test.api.testcase.UiAndintreF;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @Author yc
 * @Date 2022/12/20
 * @Version 1.0
 **/
public class BaiduWebSetTest {
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

    String genSearchSetEle1 = "span[id='s-usersetting-top'][name='tj_settingicon']";
    String genSearchSetEle2 = "搜索设置";
    String genSearchSetEle3 = "i[class='c-icon s-skin-close']";
    String genSearchSetEle4 = "";
    @Test(groups = {"Group1"},description = "设置-搜索设置",priority = 40)
    public void test_setOptions03(){
        //点击设置
        if(baiduWebUiTestPage.isElementExist("Css",genSearchSetEle1)){
            WebElement webElement =baiduWebUiTestPage.FindElementMethod("Css",genSearchSetEle1);
            webElement.click();
        }
        //点击搜索设置
        if(baiduWebUiTestPage.isElementExist("LText",genSearchSetEle2)){
            WebElement webElement1 = baiduWebUiTestPage.FindElementMethod("LText",genSearchSetEle2);
            System.out.println("test_setOptions03");
            webElement1.click();
        }
    }

/*    String searchSetEle1 = "span[id='s-usersetting-top'][name='tj_settingicon']";
    String searchSetEle2 = "高级搜索";
    @Test(groups = {"Group1"},description = "设置-高级搜索",priority = 40)
    public void test_setOptions04(){
        //点击设置
        if(baiduWebUiTestPage.isElementExist("Css",searchSetEle1)){
            WebElement webElement =baiduWebUiTestPage.FindElementMethod("Css",searchSetEle1);
            System.out.println("test_setOptions04");
            webElement.click();
        }

        //点击高级设置
        if(baiduWebUiTestPage.isElementExist("Css",searchSetEle2)){
            WebElement webElement1 = baiduWebUiTestPage.FindElementMethod("LText",searchSetEle2);
            System.out.println("test_setOptions04");
            webElement1.click();
        }
    }*/
}

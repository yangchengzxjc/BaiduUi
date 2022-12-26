package com.test.api.testcase.UiAndintreF;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @Author yc
 * @Date 2022/12/21
 * @Version 1.0
 **/
public class BaiduWebSearchTest {

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

        String InSearchEle1 = "span[id='s-usersetting-top'][name='tj_settingicon']";
    String InSearchEle2 = "input[id='su'][value='百度一下']";

    @Test(groups = {"Group1"},description = "输入搜索",priority = 30)
    public void test_input02() throws InterruptedException {
        try{
            new WebDriverWait(driver,10,1).
                    until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(InSearchEle1)));
            System.out.println("元素已找到");
            WebElement webElement = baiduWebUiTestPage.FindElementMethod("Css",InSearchEle1);
            System.out.println(webElement.getAttribute("class"));
            webElement.clear();
            webElement.sendKeys("我是谁");
            WebElement webElement2 = baiduWebUiTestPage.FindElementMethod("Css",InSearchEle2);
            System.out.println("test_setOptions02");
            webElement2.click();
        }
        catch(NoSuchElementException e){
            System.out.println(InSearchEle1+InSearchEle2+"test_input02"+"元素未找到");
        }
    }
}

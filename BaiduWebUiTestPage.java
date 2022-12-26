package com.test.api.testcase.UiAndintreF;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * @Author yc
 * @Date 2022/12/12
 * @Version 1.0
 **/
public class BaiduWebUiTestPage {

    private BaiduWebDriver baiduWebDriver;
    private  WebDriver driver;
    //private WebElement driver;
    public BaiduWebUiTestPage(WebDriver driver){
        this.driver = driver;
    }

    public WebElement FindElementMethod(String method, String key){
        if("Id".equals(method)){
            WebElement webElement = driver.findElement(By.id(key));
            return webElement;
        }
        if("Name".equals(method)){
            WebElement webElement = driver.findElement(By.name(key));
            return webElement;
        }
        if("Xpath".equals(method)){
            WebElement webElement = driver.findElement(By.xpath(key));
            return webElement;
        }
        if("Css".equals(method)){
            WebElement webElement = driver.findElement(By.cssSelector(key));
            return webElement;
        }
        if("ClassN".equals(method)){
            WebElement webElement = driver.findElement(By.className(key));
            return webElement;
        }
        if("LText".equals(method)){
            WebElement webElement = driver.findElement(By.linkText(key));
            return webElement;
        }
        if("PLText".equals(method)){
            WebElement webElement = driver.findElement(By.partialLinkText(key));
            return webElement;
        }
        if("TName".equals(method)){
            WebElement webElement = driver.findElement(By.tagName(key));
            return webElement;
        }
        return null;
    }
    /**判断一个元素是否存在**/
    public boolean isElementExist(String m, String key){
        try {
            this.FindElementMethod(m, key);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**2、页面放大，等待10s*/
    public void maximizePage(){
        driver.manage().window().maximize();
    }

    /**3、操作元素前进，操作元素后退**/
    public void elementMoveForward(){
        driver.navigate().forward();
        //return (driver.findElements(by).size()>0) ? true : false;
    }
    public void elementMoveBack(){
        driver.navigate().back();
        //return (driver.findElements(by).size()>0) ? true : false;
    }

    /**3、判断元素列表是否存在**/
    public boolean elementsExists(By by){
        return driver.findElements(by).size() > 0;
        //return (driver.findElements(by).size()>0) ? true : false;
    }

    /**4、获取元素列表中指定的元素**/
    public WebElement FindByElements(By by,int index){
        WebElement element = null;
        if(this.elementsExists(by)){
            element = driver.findElements(by).get(index);
        }
        return element;
    }

    /**5、判断弹出框**/
    public Alert alert;
    public boolean isAlertPresent(){

        try {
            alert = driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException Ex) {
            return false;
        }
    }


    /**6、接受弹出框**/
    public void acceptAlert(){
        if(this.isAlertPresent()){
            alert.accept();
        }
    }

    /**7、取消弹出框**/
    public void dimissAlert(){
        if(this.isAlertPresent()){
            alert.dismiss();
        }
    }

    /**8、获取弹出内容**/
    public String getAlertText(){
        String text = null ;
        if(this.isAlertPresent()){
            text = alert.getText();
        }else{
            //todo:log;
        }
        return text;
    }

    /**切换窗口**/
    public void switchToWindow(String windowTtitle){
        Set<String> windowHandles = driver.getWindowHandles();
        for(String handler:windowHandles){
            driver.switchTo().window(handler);
            String title = driver.getTitle();
            if(windowTtitle.equals(title)){
                break;
            }
        }
    }

    /**
     切换窗口-根据frameid
     */
    public void switchToFrame(String frameId){
        driver.switchTo().frame(frameId);
    }
    public void switchToFrame(int index){
        driver.switchTo().frame(index);
    }

    /**根据元素切换窗口*/
    public void switchToframe(WebElement element){
        driver.switchTo().frame(element);
    }

    /**判断是否加载有JQuery**/
    public Boolean JQueryLoaded(){
        Boolean loaded;
        JavascriptExecutor js =(JavascriptExecutor)driver;
        try{
            loaded  =(Boolean)js.executeScript("return"+"JQuery()!=null");
        }catch(WebDriverException e){
            loaded = false;
        }
        return loaded;

    }

    /**截屏**/
    public void screenShot(WebDriver driver){
        String dir_name ="screenshot";
        if(!(new File(dir_name).exists())){
            new File(dir_name).mkdir();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String time = sdf.format(new Date());
        try{
            File source_file=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);//执行截屏
            FileUtils.copyFile(source_file,new File(dir_name+ File.separator+time+".png"));
        }catch(IOException e){
            e.printStackTrace();
        }

    }
    /**获取键盘**/
    public Keyboard getKeyboard(){
        return ((HasInputDevices)driver).getKeyboard();
    }

    /**模拟crtrl+F5**/
    public void refreshWithCtrlF5(){
        getKeyboard().sendKeys(Keys.CONTROL, Keys.F5);
    }


}

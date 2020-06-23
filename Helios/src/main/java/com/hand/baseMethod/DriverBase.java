package com.hand.baseMethod;
import java.io.*;
import com.hand.basicconstant.BaseConstant;

import com.hand.utils.PropertyReader;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.net.URL;
import java.util.*;

/**
 * @Author ： xinghong.chen
 * @Email: xinghong.chen@huilianyi.com
 * @DATE : 2019/1/24 15:42
 **/


@Slf4j
public class DriverBase {
    // webdriver相关变量
    private WebDriver driver;
    private String browseName;
    private String chromeDriverLinuxPath;
    private String chromeDriverPath;
    private String firefoxDriverPath;
    private String ieDriverPath;
    private String edgeDriverPath;
    private String chromeDriverLinuxKey = "driver.chromeDriverLinux";
    private String chromeDriverKey = "driver.chromeDriver";
    private String firefoxDriverKey = "driver.firefoxDriver";
    private String ieDriverKey = "driver.ieDriver";
    private String edgeDriverKey = "driver.edgeDriver";
    private static ThreadLocalUtil<WebDriver> driverThreadLocalUtil = new ThreadLocalUtil<WebDriver>();
    private static ThreadLocalUtil<String> browseNameThreadLocalUtil = new ThreadLocalUtil<String>();
    private static ThreadLocal<WebDriver> threadDriver = new ThreadLocal<WebDriver>();
    private static ThreadLocal<String> threadBrowseName = new ThreadLocal<String>();

    // 根据读取的配置文件，给变量赋值
    public void setValue() throws IOException{
        this.chromeDriverLinuxPath = PropertyReader.getValue( chromeDriverLinuxKey );
        this.chromeDriverPath = PropertyReader.getValue( chromeDriverKey );
        this.firefoxDriverPath = PropertyReader.getValue( firefoxDriverKey );
        this.ieDriverPath = PropertyReader.getValue( ieDriverKey );
        this.edgeDriverPath = PropertyReader.getValue( edgeDriverKey );
    }

    //随机启动四种浏览器
    public void randomOpenBrowse(int browseNumber, String enableListener, String remoteIP, String browserVersion) throws IOException {
        setValue();
        // 驱动路径前缀
        log.info("测试开始，拉起浏览器");
        String driverBasePath = this.getClass().getResource("/").getPath();
        ArrayList<Integer> number = new ArrayList<Integer>( Arrays.asList(1, 2, 3, 4, 5));
        if (!number.contains( browseNumber )){
            Random random = new Random();
            browseNumber = random.nextInt(4)+1;
        }
        log.info("当前是否启用监听:"+enableListener);
        switch (browseNumber){
            case 1:   //谷歌
                try {
                    if(remoteIP == null || "".equals(remoteIP)) {
                        // 如果是 Windows 系统
                        if(System.getProperty("os.name").contains("Windows")) {
                            System.setProperty(BaseConstant.CHROME_DRIVER_NAME, driverBasePath + chromeDriverPath);
                            ChromeOptions chromeOptions = new ChromeOptions();
                            chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
                            chromeOptions.addArguments("--no-sandbox");
                            chromeOptions.addArguments("--disable-dev-shm-usage");
//                            this.driver=new ChromeDriver(chromeOptions);
//                            EnableListener 为true启用监听
                            if (!"true".equals(enableListener)){
                                this.driver=new ChromeDriver(chromeOptions);
                            }
                            else {
                                this.driver = new EventFiringWebDriver(new ChromeDriver(chromeOptions)).register(new DriverEventListener());
                            }
                            driverThreadLocalUtil.setThreadValue(threadDriver, this.driver);
                            browseNameThreadLocalUtil.setThreadValue(threadBrowseName, "谷歌");
                            log.info("成功启动谷歌浏览器");
                        }
                        // 如果是 Linux 系统
                        else if(System.getProperty("os.name").contains("Linux")){
                            System.setProperty(BaseConstant.CHROME_DRIVER_NAME, driverBasePath + chromeDriverLinuxPath);
                            ChromeOptions chromeOptions = new ChromeOptions();
                            chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
                            chromeOptions.addArguments("--no-sandbox");
                            chromeOptions.addArguments("--disable-dev-shm-usage");
                            this.driver=new ChromeDriver(chromeOptions);
                            driverThreadLocalUtil.setThreadValue(threadDriver, this.driver);
                            browseNameThreadLocalUtil.setThreadValue(threadBrowseName, "谷歌");
                            log.info("成功启动谷歌浏览器");
                        }
                    }
                    else{
                        /*DesiredCapabilities desiredCaps = new DesiredCapabilities("chrome", browserversion, Platform.LINUX);
                        driver = new RemoteWebDriver(new URL("http://" + remoteIP + ":4444/wd/hub/"), desiredCaps);
                        driverThreadLocalUtil.setThreadValue(threadDriver, driver);
                        browseNameThreadLocalUtil.setThreadValue(threadBrowseName, "谷歌");
                        driver.manage().window().maximize();*/

                        // 浏览器驱动
//                        DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
//                        ChromeOptions chromeOptions = new ChromeOptions();

                        DesiredCapabilities desiredCapabilities = new DesiredCapabilities("chrome", browserVersion, Platform.LINUX);
                        ChromeOptions chromeOptions = new ChromeOptions().merge(desiredCapabilities);

                        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--disable-dev-shm-usage");
//                        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                        //chromeOptions.merge(desiredCapabilities);

                        // 驱动使用 RemoteWebDriver
//                            this.driver = new RemoteWebDriver(new URL("http://" + remoteIP + ":4444/wd/hub/"), chromeOptions);
//                        EnableListener 为true启用监听
                        if (!"true".equals(enableListener)) {
                            this.driver = new MyRemoteWebDriver(new URL("http://" + remoteIP + ":4444/wd/hub/"), chromeOptions);
                        }
                        else {
                            this.driver=new EventFiringWebDriver(new MyRemoteWebDriver(new URL("http://" + remoteIP + ":4444/wd/hub/"), chromeOptions)).register(new DriverEventListener());
                        }

                        driverThreadLocalUtil.setThreadValue(threadDriver, driver);
                        browseNameThreadLocalUtil.setThreadValue(threadBrowseName, "谷歌");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    log.info("谷歌浏览器启动失败");
                }finally {
                    break;
                }
            case 2: //火狐
                try {
                    System.setProperty( BaseConstant.FIREFOX_DRIVER_NAME, driverBasePath + firefoxDriverPath);
                    driverThreadLocalUtil.setThreadValue( threadDriver, new FirefoxDriver() );
                    browseNameThreadLocalUtil.setThreadValue( threadBrowseName, "火狐" );
                    log.info( "成功启动火狐浏览器" );
                }catch (Exception e){
                    e.printStackTrace();
                    log.info("火狐浏览器启动失败");
                }finally {
                    break;
                }
            case 3:  //Edge
                try {
                    System.setProperty( BaseConstant.EDGE_DRIVER_NAME, driverBasePath + edgeDriverPath);
                    driverThreadLocalUtil.setThreadValue( threadDriver, new EdgeDriver() );
                    browseNameThreadLocalUtil.setThreadValue( threadBrowseName, "Edge" );
                    log.info( "成功启动Edge浏览器" );
                }
                catch (Exception e){
                    e.printStackTrace();
                    log.info("Edge浏览器启动失败");
                }finally {
                    break;
                }
            case 4: //IE
                try {
                    System.setProperty( BaseConstant.IE_DRIVER_NAME, driverBasePath + ieDriverPath);
                    // ie浏览器安全设置
                    DesiredCapabilities ieCapabilities  = DesiredCapabilities.internetExplorer ();
                    ieCapabilities.setCapability ( InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true );
                    InternetExplorerOptions option = new InternetExplorerOptions ( ieCapabilities );
                    driverThreadLocalUtil.setThreadValue( threadDriver, new InternetExplorerDriver(option) );
                    browseNameThreadLocalUtil.setThreadValue( threadBrowseName, "IE" );
                    log.info( "成功启动IE浏览器" );
                }catch (Exception e){
                    e.printStackTrace();
                    log.info("IE浏览器启动失败");
                }finally {
                    break;
                }
            case 5: //谷歌手机模拟器
                try {
                    if(remoteIP == null || "".equals(remoteIP)) {
                        // 若是 windows 系统
                        if(System.getProperty("os.name").contains("Windows")) {
                            System.setProperty(BaseConstant.CHROME_DRIVER_NAME, driverBasePath + chromeDriverPath);
                            Map<String, String> mobileEmulation = new HashMap<>();
                            mobileEmulation.put("deviceName", "Nexus 5");
                            ChromeOptions chromeOptions = new ChromeOptions();
                            chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
                            chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
                            chromeOptions.addArguments("--no-sandbox");
                            chromeOptions.addArguments("--disable-dev-shm-usage");
                            if (!"true".equals(enableListener)) {
                                this.driver=new ChromeDriver(chromeOptions);
                            }
                            else {
                                this.driver=new EventFiringWebDriver(new ChromeDriver(chromeOptions)).register(new DriverEventListener());}



                            driverThreadLocalUtil.setThreadValue(threadDriver, this.driver);
                            browseNameThreadLocalUtil.setThreadValue(threadBrowseName, "谷歌");
                            log.info("成功启动谷歌手机模拟浏览器");
                        }
                        // 如果是 Linux 系统
                        else if(System.getProperty("os.name").contains("Linux")){
//                            System.setProperty(BaseConstant.CHROME_DRIVER_NAME, driverBasePath + chromeDriverLinuxPath);
                            Map<String, String> mobileEmulation = new HashMap<>();
                            mobileEmulation.put("deviceName", "Nexus 5");
                            ChromeOptions chromeOptions = new ChromeOptions();
                            chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
                            chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
                            chromeOptions.addArguments("--no-sandbox");
                            chromeOptions.addArguments("--disable-dev-shm-usage");
                            this.driver=new ChromeDriver(chromeOptions);
                            driverThreadLocalUtil.setThreadValue(threadDriver, this.driver);
                            browseNameThreadLocalUtil.setThreadValue(threadBrowseName, "谷歌");
                            log.info("成功启动谷歌手机模拟浏览器");
                        }
                    }
                    else{
                        // 设置属性
                        Map<String, String> mobileEmulation = new HashMap<>();
                        mobileEmulation.put("deviceName", "Nexus 5");

                        DesiredCapabilities desiredCapabilities = new DesiredCapabilities("chrome", browserVersion, Platform.LINUX);
                        ChromeOptions chromeOptions = new ChromeOptions().merge(desiredCapabilities);

//                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
                        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--disable-dev-shm-usage");
                        // 驱动使用 RemoteWebDriver


                        if (!"true".equals(enableListener)) {
                            this.driver = new MyRemoteWebDriver(new URL("http://" + remoteIP + ":4444/wd/hub/"), chromeOptions);
                        }
                        else {
                            this.driver=new EventFiringWebDriver(new MyRemoteWebDriver(new URL("http://" + remoteIP + ":4444/wd/hub/"), chromeOptions)).register(new DriverEventListener());
                        }

                        driverThreadLocalUtil.setThreadValue(threadDriver, driver);
                        browseNameThreadLocalUtil.setThreadValue(threadBrowseName, "谷歌");
                        log.info("成功启动谷歌手机模拟浏览器");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    log.info("谷歌手机模拟浏览器启动失败");
                }finally {
                    break;
                }
            default:
                log.info("无匹配项");

        }

    }

    // 获得driver
    public WebDriver getDriver(){
        return driverThreadLocalUtil.getThreadValue( threadDriver );
    }

    // 设置driver
    public void setDriver(WebDriver driver){
        this.driver = driver;
    }

    // 获得BrowseName
    public String getBrowseName(){
        return browseNameThreadLocalUtil.getThreadValue( threadBrowseName );
    }

    // 设置BrowseName
    public void setBrowseName(String browseName){
        this.browseName = browseName;
    }

    // 关闭driver
    public void stopDriver(){
        setDriver( getDriver() );
        setBrowseName( getBrowseName() );
        if(driver != null){
            driver.quit();
            log.info("测试结束成功关闭"  + browseName + "浏览器");
            threadDriver.remove();
            threadBrowseName.remove();

        }
    }
}

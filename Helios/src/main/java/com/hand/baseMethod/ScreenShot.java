package com.hand.baseMethod;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author ： xinghong.chen
 * @Email: xinghong.chen@huilianyi.com
 * @DATE : 2019/3/18 17:18
 **/

@Slf4j
public class ScreenShot{
    private static String screenShotDirPath = System.getProperty("user.dir") + File.separator +"target" + File.separator + "test-output" + File.separator + "errorScreenShot";

    public static void takeScreenShot(WebDriver driver,ITestResult iTestResult) {
        Object currentClass = iTestResult.getInstance();// 获取当前 Object 类型的测试类
        //获取包名
        String packageName = currentClass.getClass().getPackage().getName();
        System.out.println("packageName : " + packageName);
 /*       //根据获得的类名，去掉包名
        String currentClassName = currentClass.toString().replaceAll(packageName,"");
        System.out.println("currentClassName : " + currentClassName);*/
        String currentClassName = currentClass.toString();

        //正则匹配
        Pattern pattern = Pattern.compile(packageName+".(.*?)@");//此处为正则表达式
        Matcher matcher = pattern.matcher(currentClassName);//去匹配正则表达式
        while (matcher.find()) {
            currentClassName = matcher.group(1);
            System.out.println("currentClassName:" + currentClassName);
        }
    //  System.out.println("出错的类为：" + currentClassStr);

        File screenShotDir = new File(screenShotDirPath);
        if (!screenShotDir.exists() && !screenShotDir.isDirectory()) {
            screenShotDir.mkdirs();
        }
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年-MM月-dd日-HH时-mm分-ss秒SSS");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH点mm分ss秒");
        String time = simpleDateFormat.format(new Date());
        try {
            File source_file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);                              // 屏幕截图
            log.info("截图开始，出错的截图命名为：" + currentClass.toString() + ".png");
            FileUtils.copyFile(source_file, new File(screenShotDirPath + File.separator + currentClassName + time + ".png"));  // 另存截图
            log.info("截图成功，出错的截图命名为：" + currentClass.toString() + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

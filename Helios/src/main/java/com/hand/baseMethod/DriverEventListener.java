package com.hand.baseMethod;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Thread.sleep;

@Slf4j
public class DriverEventListener implements WebDriverEventListener{

    public int timeoutSec;
    public int tryMaxCount;
    public DriverEventListener() {
		this.timeoutSec = 1;
		this.tryMaxCount =20;
	}
    public DriverEventListener(int timeout, int tryCount) {
		this.timeoutSec = timeout;
		this.tryMaxCount = tryCount;
	}
	@Override
	public void beforeAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void afterAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void afterAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void afterNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void afterNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeNavigateRefresh(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void afterNavigateRefresh(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver){
		// TODO Auto-generated method stub
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		Calendar beforeTime = Calendar.getInstance();
		beforeTime.add(Calendar.SECOND,0);// 2分钟之前的时间
		Date LimitTime = beforeTime.getTime();
		LimitTime.setTime(LimitTime.getTime() +30*1000);
		String time = formatter.format(LimitTime);
//		log.info("准备找元素截止时间是:"+time);
		int count=1;
		while (true) {
			Calendar OneTime = Calendar.getInstance();
			OneTime.add(Calendar.SECOND,0);// 2分钟之前的时间
			Date FindDate = OneTime.getTime();
//			log.info("第"+count+"次准备找元素时间是:"+formatter.format(FindDate));
			if (FindDate.after(LimitTime)) {
				log.info("第"+count+"次准备找元素超时,准备跳出循环。");
				break;
			}
			if (count >= tryMaxCount) {
				break;
//				throw new Exception(String.format("通过%s，间隔%s秒,重试%s 无法找到", by.toString(), timeoutSec, tryMaxCount));
			}

			try{
				count++;
				driver.findElement(by);
//				log.info("监听，发现元素成功！");
				return;
			}catch(Exception e){
				try {
//					sleep(timeoutSec*1000);
//					sleep(timeoutSec*1000);
					log.info(String.format("通过%s，间隔%s秒,已重试%s次", by.toString(), timeoutSec, count));
				} catch (Exception e1) {
				}
			}

		}
	}


	public void beforeFindBy2(By by, WebElement element, WebDriver driver){
		// TODO Auto-generated method stub
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		Calendar beforeTime = Calendar.getInstance();
		beforeTime.add(Calendar.SECOND,0);// 2分钟之前的时间
		Date LimitTime = beforeTime.getTime();
		LimitTime.setTime(LimitTime.getTime() +30*1000);
		String time = formatter.format(LimitTime);
//		log.info("准备找元素截止时间是:"+time);
		int count=1;
		while (true) {
			Calendar OneTime = Calendar.getInstance();
			OneTime.add(Calendar.SECOND,0);// 2分钟之前的时间
			Date FindDate = OneTime.getTime();
//			log.info("第"+count+"次准备找元素时间是:"+formatter.format(FindDate));
			if (FindDate.after(LimitTime)) {
				log.info("第"+count+"次准备找元素超时,准备跳出循环。");
				break;
			}
			if (count >= tryMaxCount) {
				break;
//				throw new Exception(String.format("通过%s，间隔%s秒,重试%s 无法找到", by.toString(), timeoutSec, tryMaxCount));
			}

			try{
				count++;
				driver.findElement(by);
				return;
			}catch(Exception e){
				try {
					sleep(timeoutSec*1000);
					log.info(String.format("通过%s，间隔%s秒,已重试%s次", by.toString(), timeoutSec, count));
				} catch (Exception e1) {
				}
			}

		}
	}
	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeScript(String script, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void afterScript(String script, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSwitchToWindow(String s, WebDriver webDriver) {

	}

	@Override
	public void afterSwitchToWindow(String s, WebDriver webDriver) {

	}
	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <X> void beforeGetScreenshotAs(OutputType<X> outputType) {

	}

	@Override
	public <X> void afterGetScreenshotAs(OutputType<X> outputType, X x) {

	}

	@Override
	public void beforeGetText(WebElement webElement, WebDriver webDriver) {

	}

	@Override
	public void afterGetText(WebElement webElement, WebDriver webDriver, String s) {

	}

}

package com.test.listener;

import com.hand.baseMethod.Base;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;


/**
 * @Author ： xinghong.chen
 * @Email: xinghong.chen@huilianyi.com
 * @DATE : 2019/3/19 11:27
 **/

@Slf4j
public class TestLogListener extends TestListenerAdapter {
    @Override
    public void onStart(ITestContext iTestContext) {
        super.onStart(iTestContext);
        Base base = new Base();
        ThreadContext.put("logid", base.GetUUid());
        ThreadContext.put("ROUTINGKEY", base.GetUUid());
        ThreadContext.put("ThreadName", iTestContext.getName());
        log.info(String.format("====================测试线程：%s测试开始====================", iTestContext.getName()));
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        super.onTestStart(iTestResult);
        ThreadContext.put("CaseName", "case:" + iTestResult.getName());
        log.info(String.format("========用例 %s.%s测试开始========", iTestResult.getInstanceName(), iTestResult.getName()));
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        super.onTestSuccess(iTestResult);
        log.info(String.format("========用例 %s.%s测试通过========", iTestResult.getInstanceName(), iTestResult.getName()));
        ThreadContext.remove("CaseName");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        log.info("测试失败啦，暂时不需要截图");
//        try {
//                log.info("失败截图开始");
////                WebDriver driver = BaseTest.driverBase.getDriver();
//                log.info( "report_driver_fail:" + driver );
//                super.onTestFailure( iTestResult );
//                // 失败截图
//                ScreenShot.takeScreenShot( driver,iTestResult);
//                log.info("失败截图结束");
//        } catch (Exception e) {
//            log.info("截图失败啦："+e.getMessage());
//        }
//        log.error( String.format( "========用例 %s.%s测试失败,失败原因如下：\n%s========", iTestResult.getInstanceName(), iTestResult.getName(), iTestResult.getThrowable() ));
//        ThreadContext.remove("CaseName");
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        super.onTestSkipped(iTestResult);
        log.info(String.format("========用例 %s.%s跳过测试========", iTestResult.getInstanceName(), iTestResult.getName()));
        ThreadContext.remove("CaseName");
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        super.onFinish(iTestContext);
//        ThreadContext.remove("logid");
        ThreadContext.remove("ROUTINGKEY");
        ThreadContext.remove("logid");
        log.info(String.format("====================测试线程：%s测试结束====================", iTestContext.getName()));
        ThreadContext.remove("ThreadName");
    }
}

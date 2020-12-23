package com.test.listener;

import com.google.gson.*;
import com.hand.utils.DingDingUtil;
import lombok.extern.slf4j.Slf4j;
import org.testng.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author ： xinghong.chen
 * @Email: xinghong.chen@huilianyi.com
 * @DATE : 2019/4/23 15:25
 **/
@Slf4j
public class TestReportListener implements IReporter {
    // 日期格式化
    private static Date date = new Date();
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
    private static String reportdate = simpleDateFormat.format(date);
    private static String getReportName = "汇联易自动化测试报告" + reportdate;

    // 定义html模板所在路径
    private String templatePath = this.getClass().getResource("/").getPath() + "report/template.html";
    // 定义报告生成的路径
    private String reportDirPath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-output" + File.separator + "report";
    private String reportPath = reportDirPath + File.separator + getReportName + ".html";

    private String name = "DemoTest";
    private int testsPass;
    private int testsFail;
    private int testsSkip;
    private String beginTime;
    private long totalTime;
    private String project = "Helios";
    private String suitName = "";
    private String environment = "";
    private String module = "[]";


    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {

        List<ITestResult> list = new ArrayList<ITestResult>();
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> suiteResults = suite.getResults();
            if(suite.getParameter("module")!= null){
                module = suite.getParameter("module");
            }
            suitName = suite.getName();
            if (suite.getParameter("environment") == null) {
                throw new NullPointerException("环境信息未配置");
            }
            if (suite.getParameter("environment").equalsIgnoreCase("uat")) {
                this.environment = "UAT";
            } else if (suite.getParameter("environment").equalsIgnoreCase("stage")) {
                this.environment = "STAGE";
            } else if (suite.getParameter("environment").equalsIgnoreCase("console")) {
                this.environment = "CONSOLE";
            } else if (suite.getParameter("environment").equalsIgnoreCase("console-tc")) {
                this.environment = "CONSOLE-TC";
            }

            for (ISuiteResult suiteResult : suiteResults.values()) {
                ITestContext testContext = suiteResult.getTestContext();
                IResultMap passedTests = testContext.getPassedTests();
                testsPass = testsPass + passedTests.size();
                IResultMap failedTests = testContext.getFailedTests();
                testsFail = testsFail + failedTests.size();
                IResultMap skippedTests = testContext.getSkippedTests();
                testsSkip = testsSkip + skippedTests.size();
                IResultMap failedConfig = testContext.getFailedConfigurations();
                list.addAll(this.listTestResult(passedTests));
                list.addAll(this.listTestResult(failedTests));
                list.addAll(this.listTestResult(skippedTests));
                list.addAll(this.listTestResult(failedConfig));
            }
        }

        this.project = this.project + "-" + this.environment;
        this.sort(list);
        this.outputResult(list);
        //新加钉钉机器人测试报告  如果@人不是空的话就进行发送钉钉消息
        if (!module.equals("[]")) {
            String url = "https://oapi.dingtalk.com/robot/send?access_token=592a7abc3b71fa4570aa9b48115511f50f803b4405614620fa44b2e6bdd7cfc2";
            int testAll = testsPass + testsFail + testsSkip;
            String pass = DingDingUtil.folatToPer((float) testsPass / testAll);
            StringBuilder context = new StringBuilder();
            JsonElement moduleelement = new JsonParser().parse(module);
            if(testsFail==0){
//                String contxtString =String.format("\"### **%s**\n> - <font color=\"##006600\">环境：%s</font><br/>\n> - <font color=\"#006600\">总用例数：%s</font><br/>\n> - <font color=\"#006600\">通过：%s</font><br/>\n> - <font color=\"#006600\">失败：%s</font><br/>\n> - <font color=\"#006600\">跳过：%s</font><br/>\n> - <font color=\"#006600\">通过率为：%s</font><br/>\"",suitName,environment,testAll,testsPass,testsFail,testsSkip,pass);
                String contxtString = String.format("\"### **%s**\\n> - <font color=\\\"#34A853\\\">环境：%s</font><br/>\\n> - <font color=\\\"#34A853\\\">总用例数：%s</font><br/>\\n> - <font color=\\\"#34A853\\\">通过：%s</font><br/>\\n> - <font color=\\\"#34A853\\\">失败：%s</font><br/>\\n> - <font color=\\\"#34A853\\\">跳过：%s</font><br/>\\n> - <font color=\\\"#34A853\\\">通过率为：%s</font><br/>\"",suitName,environment,testAll,testsPass,testsFail,testsSkip,pass);
                context.append(contxtString);
            }
            if(testsFail>0){
                context.append(String.format("\"### **%s**\\n> - <font color=\\\"#EA4335\\\">环境：%s</font><br/>\\n> - <font color=\\\"#EA4335\\\">总用例数：%s</font><br/>\\n> - <font color=\\\"#EA4335\\\">通过：%s</font><br/>\\n> - <font color=\\\"#EA4335\\\">失败：%s</font><br/>\\n> - <font color=\\\"#EA4335\\\">跳过：%s</font><br/>\\n> - <font color=\\\"#EA4335\\\">通过率为：%s</font><br/>\"",suitName,environment,testAll,testsPass,testsFail,testsSkip,pass));
                if(moduleelement.getAsJsonArray().isJsonArray()){
                    for(int i=0;i<moduleelement.getAsJsonArray().size();i++){
                        context.append("\\n").append("@").append(Long.valueOf(moduleelement.getAsJsonArray().get(i).getAsString()));
                    }
                }
            }
            if(testsFail==0){
                module="[]";
            }
            try {
                log.info("发送的消息为：{}",context.toString());
                DingDingUtil.sendVal(url,context.toString(),suitName,environment,testAll,testsPass,testsFail,testsSkip,pass,module);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<ITestResult> listTestResult(IResultMap resultMap) {
        Set<ITestResult> results = resultMap.getAllResults();
        return new ArrayList<ITestResult>(results);
    }

    private void sort(List<ITestResult> list) {
        Collections.sort(list, new Comparator<ITestResult>() {
            @Override
            public int compare(ITestResult r1, ITestResult r2) {
                return r1.getStartMillis() < r2.getStartMillis() ? -1 : 1;
            }
        });
    }

    private void outputResult(List<ITestResult> list) {
        try {
            List<ReportInfo> listInfo = new ArrayList<ReportInfo>();
            int index = 0;
            for (ITestResult result : list) {
                String testName = result.getMethod().getMethodName();
                if (index == 0) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                    beginTime = formatter.format(new Date(result.getStartMillis()));
                    index++;
                }
                long spendTime = result.getEndMillis() - result.getStartMillis();
                totalTime += spendTime;
                String status = this.getStatus(result.getStatus());
                List<String> log = Reporter.getOutput(result);
                for (int i = 0; i < log.size(); i++) {
                    log.set(i, log.get(i).replaceAll("\"", "\\\\\""));
                }
                Throwable throwable = result.getThrowable();
                if (throwable != null) {
                    log.add(throwable.toString().replaceAll("\"", "\\\\\""));
                    StackTraceElement[] st = throwable.getStackTrace();
                    for (StackTraceElement stackTraceElement : st) {
                        log.add(("    " + stackTraceElement).replaceAll("\"", "\\\\\""));
                    }
                }
                ReportInfo info = new ReportInfo();
                info.setName(testName);
                info.setSpendTime(spendTime + "ms");
                info.setStatus(status);
                info.setClassName(result.getInstanceName());
                info.setMethodName(result.getName());
                try {
                    info.setDescription(result.getMethod().getDescription() + "-" + result.getParameters()[0].toString());
                } catch (ArrayIndexOutOfBoundsException e) {
                    info.setDescription(result.getMethod().getDescription());
                }
                info.setLog(log);
                listInfo.add(info);
//                reportName = result.getTestContext().getSuite().getName();
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("environmentName", this.project);
//            result.put("reportName",reportName);
            result.put("testPass", testsPass);
            result.put("testFail", testsFail);
            result.put("testSkip", testsSkip);
            result.put("testAll", testsPass + testsFail + testsSkip);
            result.put("beginTime", beginTime);
            result.put("totalTime", totalTime + "ms");
            result.put("testResult", listInfo);
            Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
            String template = this.read(reportDirPath, templatePath);
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(reportPath)), "UTF-8"));
            template = template.replace("${resultData}", gson.toJson(result));
            output.write(template);
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getStatus(int status) {
        String statusString = null;
        switch (status) {
            case 1:
                statusString = "成功";
                break;
            case 2:
                statusString = "失败";
                break;
            case 3:
                statusString = "跳过";
                break;
            default:
                break;
        }
        return statusString;
    }

    public static class ReportInfo {

        private String name;

        private String className;

        private String methodName;

        private String description;

        private String spendTime;

        private String status;

//        private Object[] parameters;

        private List<String> log;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public String getSpendTime() {
            return spendTime;
        }

        public void setSpendTime(String spendTime) {
            this.spendTime = spendTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<String> getLog() {
            return log;
        }

        public void setLog(List<String> log) {
            this.log = log;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

//        public Object[] getParameters() {
//            return parameters;
//        }
//
//        public void setParameters(Object[] parameters) {
//            this.parameters = parameters;
//        }
    }

    private String read(String reportDirPath, String templatePath) {
        //文件夹不存在时级联创建目录
        File reportDir = new File(reportDirPath);
        if (!reportDir.exists() && !reportDir.isDirectory()) {
            reportDir.mkdirs();
        }
        File templateFile = new File(templatePath);
        InputStream inputStream = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            inputStream = new FileInputStream(templateFile);
            int index = 0;
            byte[] b = new byte[1024];
            while ((index = inputStream.read(b)) != -1) {
                stringBuffer.append(new String(b, 0, index));
            }
            return stringBuffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}


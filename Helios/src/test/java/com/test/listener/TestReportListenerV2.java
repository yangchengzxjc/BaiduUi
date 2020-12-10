package com.test.listener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author ： xinghong.chen
 * @Email: xinghong.chen@huilianyi.com
 * @DATE : 2019/4/23 15:25
 **/

public class TestReportListenerV2 implements IReporter {

    // 定义html模板所在路径
    private String templatePath = this.getClass().getResource("/").getPath() + "report/template.html";
    // 定义报告生成的路径
    private String reportDirPath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-output" + File.separator;
    private String reportPath = reportDirPath + File.separator + "TestReport" + ".html";

    private String name = "DemoTest";
    private int testsPass;
    private int testsFail;
    private int testsSkip;
    private String beginTime;
    private long totalTime;
    private String project = "Helios";
    private String browseNumber = "";
    private String environment = "";
//    private String language = "";


    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
//        System.out.println("browseNumber："+ browseNumber);
//        System.out.println("environment："+ environment);
//        System.out.println("language："+ language);

        List<ITestResult> list = new ArrayList<ITestResult>();
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> suiteResults = suite.getResults();

            if (suite.getParameter("environment") == null){
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
                try{
                    info.setDescription(result.getMethod().getDescription() + "-" + result.getParameters()[0].toString());
                }catch (ArrayIndexOutOfBoundsException e){
                    info.setDescription(result.getMethod().getDescription());
                }
                info.setLog(log);
                listInfo.add(info);
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("testName", this.project);
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


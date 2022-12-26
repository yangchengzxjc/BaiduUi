package com.test.api.testcase.UiAndintreF;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @Author yc
 * @Date 2022/12/12
 * @Version 1.0
 **/
public class ReadFile {
    public void ReadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(""));//需要添加路径
        String browserType = properties.getProperty("auto browserType");
    }
}

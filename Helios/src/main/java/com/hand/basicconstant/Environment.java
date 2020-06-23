package com.hand.basicconstant;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author peng.zhang
 * @Date 2020/6/5
 * @Version 1.0
 **/
@AllArgsConstructor
@Getter
public enum Environment {
    UAT("uat", "https://uat.huilianyi.com"),
    STAGE("stage", "https://stage.huilianyi.com"),
    CONSOLE("console", "https://console.huilianyi.com"),
    CONSOLE_TC("console-tc", "https://console-tc.huilianyi.com");

    private String environment;
    private String url;

    public static Environment getEnv(String environment) {
        for (Environment env : Environment.values()) {
            if (env.getEnvironment().equalsIgnoreCase(environment)) {
                return env;
            }
        }
        return STAGE;
    }
}

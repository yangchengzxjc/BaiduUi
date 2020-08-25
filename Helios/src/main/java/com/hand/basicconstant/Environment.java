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
    UAT("uat", "https://uat.huilianyi.com","https://zhenxuanuat.huilianyi.com/open/"),
    STAGE("stage", "https://stage.huilianyi.com","https://zhenxuanstage.huilianyi.com/open/"),
    CONSOLE("console", "https://console.huilianyi.com","https://zhenxuan.huilianyi.com/open/"),
    mcd("mcd","https://tes.mcd.com.cn/",""),
    CONSOLE_TC("console-tc", "https://console-tc.huilianyi.com","");




    private String environment;
    private String url;
    private String zhenxuanURL;


    public static Environment getEnv(String environment) {
        for (Environment env : Environment.values()) {
            if (env.getEnvironment().equalsIgnoreCase(environment)) {
                return env;
            }
        }
        return STAGE;
    }
}

package com.hand.utils;

import com.hand.baseMethod.OkHttpUtils;
import com.hand.basicObject.MyResponse;
import lombok.extern.slf4j.Slf4j;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DingDingUtil {

    //通过钉钉机器人发送消息到钉钉群中
    public static void sendVal(String url, String context) throws Exception {
        Map<String, String> headermap = new HashMap<>();
        headermap.put("Content-Type", "application/json");
        String entityString = "{'msgtype': 'text', 'text': {'content': '" + context + "'}}";

        MyResponse responseJson = OkHttpUtils.post(url, headermap, null, entityString, null);
        log.info("dingding res: {}", responseJson);
    }

    //folat数字转换为百分号格式
    public static String folatToPer(float folatNum) {

        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        String per = null;
        try {
            numberFormat.setMaximumFractionDigits(2); //精确到2位
            per = numberFormat.format(folatNum);
//            Reporter.log("小数点数字转百分数字符串:" + folatNum + " 转为 " + per);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return per;
    }

    public static void main(String[] args) {
        String c = DingDingUtil.folatToPer((float) 5 / 7);
        System.out.println(c);

    }
}

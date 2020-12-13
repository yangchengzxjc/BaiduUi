package com.hand.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.http.entity.ContentType;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DingDingUtil {

    //通过钉钉机器人发送消息到钉钉群中
    public static void sendVal(String url, String context) throws Exception {
        Map<String, String> headermap = new HashMap<>();
//        String entityString = "{'msgtype': 'text', 'text': {'content': '" + context + "'}}";
        String entityString = "{'msgtype': 'markdown', 'markdown': {'title': 'Helios接口测试','text': '" + context + "'}}";
        MediaType JSON = MediaType.parse(String.valueOf(ContentType.APPLICATION_JSON));
        RequestBody requestBody = RequestBody.create(JSON, entityString);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request req = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", String.valueOf(ContentType.APPLICATION_JSON))
                .post(requestBody)
                .build();
        try (Response response = okHttpClient.newCall(req).execute()) {
            ResponseBody body = response.body();
            if (response.isSuccessful()) {
                log.info("push to DingDing success. ", body == null ? "" : body.toString());
            } else {
                log.error("push DingDing error, statusCode={},body={}", response.code(), body == null ? "" : body.toString());
            }
        }
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

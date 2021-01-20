package com.hand.baseMethod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.basicObject.APIResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;

import java.io.IOException;
import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;

import static java.lang.Thread.sleep;

/**
 * HTTP通讯结构处理器
 *
 * @author Administrator
 */
@Slf4j
public class OkHttpUtils {

    private static OkHttpClient mOkHttpClient;
    private static final int CONNECT_TIMEOUT = 20;
    private static final int READ_TIMEOUT = 20;
    private static final int WRITE_TIMEOUT = 20;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    private static final String PUT = "PUT";

    private OkHttpUtils() {
    }

    public static OkHttpClient getInstance() {
        if (mOkHttpClient == null) {
            synchronized (OkHttpClient.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return mOkHttpClient;
    }

    private static void addRequestLog(String method, String url, String urlParam, String body, String formParam) {
        log.info("===========================request begin================================================");
        log.info("URI: {}", url);
        if (StringUtils.isNotBlank(body)) {
            log.info("Request body : {}", body);
        }
        if (StringUtils.isNotBlank(formParam)) {
            log.info("Request param: {}", formParam);
        }
//        log.info("---------------------------request end--------------------------------------------------");
    }

    private static void addResponseLog(String method, String callMethod, String url, String jsonbody, String formParam, Response response, int httpCode, String result, String SpanID, long startTime) {

        log.info("Status: {}", httpCode);
        log.info("SpanID: {}", SpanID);

        long endTime = System.currentTimeMillis();
        try {
            log.info("APIResponse: {}", new JsonParser().parse(result).getAsJsonObject().toString());
        } catch (Exception e) {

            try {
                log.info("APIResponse: {}", new JsonParser().parse(result).getAsJsonArray().toString());

            } catch (Exception ignored) {
            }
        }
        log.info("Time: {} ms", endTime - startTime);
        if ((endTime - startTime) >= 5000 && (endTime - startTime) < 7000) {
            log.info("请求大于5秒，有待观察:");
        } else if ((endTime - startTime) >= 7000) {
            log.info("请求大于7秒，有待观察:");
        }
//        // 报告打印日志 debug
//        Reporter.log("httpCode: " + httpCode);
//        Reporter.log("spanID: " + response.header("SpanID"));
//        Reporter.log("res: " + result);

        log.info("===========================response end================================================");
    }

    /**
     * 返回相应结果
     *
     * @param httpCode
     * @param result
     * @param response
     * @return
     * @throws HttpStatusException
     */
    private static APIResponse handleHttpResponse(int httpCode, String result, Response response) {
        APIResponse APIResponse = new APIResponse();
        APIResponse.setBody(result);
        APIResponse.setSpanId(response.header("SpanID"));
        APIResponse.setStatusCode(httpCode);
        APIResponse.setTime(response.receivedResponseAtMillis()-response.sentRequestAtMillis());
        return APIResponse;
    }

    private static void handleHttpThrowable(Exception ex, String url) throws HttpStatusException {
        if (ex instanceof HttpStatusException) {
            throw (HttpStatusException) ex;
        }
        log.error("OkHttp error url: " + url, ex);
        if (ex instanceof SocketTimeoutException) {
            throw new RuntimeException("request time out of OkHttp when do get:" + url);
        }
        throw new RuntimeException(ex);
    }

    /**
     * get方法连接拼加参数
     *
     * @param mapParams
     * @return
     */
    private static String setGetUrlParams(Map<String, String> mapParams) {
        String strParams = "";
        if (mapParams != null) {
            Iterator<String> iterator = mapParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next();
                strParams += key + "=" + mapParams.get(key) + "&";
            }
        }
        if ("&".equals(strParams.substring(strParams.length() - 1))) {
            return "?" + strParams.substring(0, strParams.length() - 1);
        } else {
            return "?" + strParams;
        }

    }


    /**
     * 设置请求头
     *
     * @param headersParams
     * @return
     */
    private static Headers setHeaders(Map<String, String> headersParams) {
        Headers headers = null;
        okhttp3.Headers.Builder headersbuilder = new okhttp3.Headers.Builder();

        if (headersParams != null) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                headersbuilder.add(key, headersParams.get(key));
            }
        }
        headers = headersbuilder.build();
        return headers;
    }

    /**
     * post请求参数
     *
     * @param bodyParams
     * @return
     */
    private static RequestBody SetRequestFormBody(Map<String, String> bodyParams) {
        RequestBody body = null;
        okhttp3.FormBody.Builder formEncodingBuilder = new okhttp3.FormBody.Builder();
        if (bodyParams != null) {
            Iterator<String> iterator = bodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, bodyParams.get(key));
            }
        }
        body = formEncodingBuilder.build();
        return body;

    }

    /**
     * Post上传图片的参数
     *
     * @param bodyParams
     * @param fileParams
     * @return
     */
    public static RequestBody SetFileRequestBody(Map<String, String> bodyParams, Map<String, String> fileParams) {
        //带文件的Post参数
        RequestBody body = null;
        okhttp3.MultipartBody.Builder MultipartBodyBuilder = new okhttp3.MultipartBody.Builder();
        MultipartBodyBuilder.setType(MultipartBody.FORM);
        RequestBody fileBody = null;

        if (bodyParams != null) {
            Iterator<String> iterator = bodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                MultipartBodyBuilder.addFormDataPart(key, bodyParams.get(key));
            }
        }

        if (fileParams != null) {
            Iterator<String> iterator = fileParams.keySet().iterator();
            String key = "";
            int i = 0;
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                i++;
                MultipartBodyBuilder.addFormDataPart(key, fileParams.get(key));
                log.info("post http", "post_Params===" + key + "====" + fileParams.get(key));
                MediaType MEDIATYPE = MediaType.parse("text/plain; charset=utf-8");
                fileBody = RequestBody.create(MEDIATYPE, new File(fileParams.get(key)));
                MultipartBodyBuilder.addFormDataPart(key, i + ".png", fileBody);
            }
        }
        body = MultipartBodyBuilder.build();
        return body;
    }

    /**
     * 带有URL的get请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static APIResponse get(String url, Map<String, String> headersParams, Map<String, String> formBody) throws HttpStatusException {
        Headers headers = null;
        String strParams = "";
        long startTime = System.currentTimeMillis();
        OkHttpClient client = getInstance();
        if (headersParams != null) {
            headers = setHeaders(headersParams);
        }
        if (formBody != null) {
            strParams = setGetUrlParams(formBody);
        }
        url += strParams;
        addRequestLog(GET, url, strParams, null, null);
        Request req = null;
        Response response = null;
        String res = "";
        int httpCode = 0;
        try {
            req = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .build();
            sleep(500);
            response = client.newCall(req).execute();
            res = response.body().string();
            httpCode = response.code();
        } catch (SocketTimeoutException e) {
            log.error("SocketTimeoutException,try again:" + e.getMessage());
            req = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .build();
            try {
                sleep(500);
                response = client.newCall(req).execute();
                res = response.body().string();
            } catch (Exception ex) {
                log.error("Retry still fails:" + ex.getMessage());
                ex.printStackTrace();
            }
            httpCode = response.code();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (httpCode != 200 && httpCode != 201) {
            addResponseLog(GET, url, url, null, null, response, httpCode, res, response.header("SpanID"), startTime);
        }
        return handleHttpResponse(httpCode, res, response);
    }

    /**
     * JSON数据修改
     *
     * @param url
     * @param requestBody
     * @return
     * @throws IOException
     */
    public static APIResponse put(String url, Map<String, String> headersParams, Map<String, String> urlMapParams, String requestBody, Map<String, String> formBody){
        Headers headers = null;
        RequestBody body = null;
        String strParams = "";
        OkHttpClient client = getInstance();
        Request req = null;
        long startTime = System.currentTimeMillis();
        String res = "";
        String jsonlog = "";
        String FormLog = "";
        if (headersParams != null) {
            headers = setHeaders(headersParams);
        }
        if (urlMapParams != null) {
            strParams = setGetUrlParams(urlMapParams);
            url += strParams;
        }
//        form请求
        if (formBody != null) {
            body = SetRequestFormBody(formBody);
            FormLog = formBody.toString();
        }
//        json 请求
        if (requestBody != null) {
            MediaType JSON = MediaType.parse(String.valueOf(ContentType.APPLICATION_JSON));
            body = RequestBody.create(JSON, requestBody);
        }
        if (formBody != null) {
            req = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .addHeader("Content-Type", String.valueOf(ContentType.APPLICATION_FORM_URLENCODED))
                    .put(body)
                    .build();
        } else if (requestBody != null) {
            req = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .addHeader("Content-Type", String.valueOf(ContentType.APPLICATION_JSON))
                    .put(body)
                    .build();
        }
        addRequestLog(req.method(), url, strParams, requestBody, FormLog);
        Response response = null;
        int httpCode = 0;
        try {
            sleep(500);
            Call call = client.newCall(req);
            response = call.execute();
            res = response.body().string();
            httpCode = response.code();
        } catch (SocketTimeoutException e) {
            log.error("SocketTimeoutException,try again:" + e.getMessage());
            Call call = client.newCall(req);
            try {
                response = call.execute();
                res = response.body().string();
            } catch (Exception ex) {
                log.error("Retry still fails:" + ex.getMessage());
                ex.printStackTrace();
            }
            httpCode = response.code();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (httpCode != 200 && httpCode != 201) {
            addResponseLog(PUT, url, url, jsonlog, FormLog, response, httpCode, res, response.header("SpanID"), startTime);
        }
        return handleHttpResponse(httpCode, res, response);
    }

    /**
     * JSON数据提交
     *
     * @param url
     * @param requestBody
     * @return
     * @throws IOException
     */
    public static APIResponse post(String url, Map<String, String> headersParams, Map<String, String> urlMapParams, String requestBody, Map<String, String> formData){
        Headers headers = null;
        RequestBody body = null;
        String strParams = "";
        OkHttpClient client = getInstance();
        Request req = null;
        long startTime = System.currentTimeMillis();
        String res = "";
        String jsonlog = "";
        String FormLog = "";
        if (headersParams != null) {
            headers = setHeaders(headersParams);
        }
        if (urlMapParams != null) {
            strParams = setGetUrlParams(urlMapParams);
            url += strParams;

        }
////        form请求
        if (formData != null) {
            body = SetRequestFormBody(formData);
            FormLog = formData.toString();
        }
//        json 请求
        if (requestBody != null) {
            MediaType JSON = MediaType.parse(String.valueOf(ContentType.APPLICATION_JSON));
            body = RequestBody.create(JSON, requestBody);
        }
        if (formData!= null) {
            req = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .addHeader("Content-Type", String.valueOf(ContentType.APPLICATION_FORM_URLENCODED))
                    .post(body)
                    .build();
        } else if (requestBody != null) {
            req = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .addHeader("Content-Type", String.valueOf(ContentType.APPLICATION_JSON))
                    .post(body)
                    .build();
        }
        addRequestLog(req.method(), url, strParams, requestBody, FormLog);
        Response response = null;
        int httpCode = 0;
        try {
            sleep(500);
            Call call = client.newCall(req);
            response = call.execute();
            res = response.body().string();
            httpCode = response.code();
        } catch (SocketTimeoutException e) {
            log.error("SocketTimeoutException,try again:" + e.getMessage());
            Call call = client.newCall(req);
            try {
                response = call.execute();
                res = response.body().string();
            } catch (Exception ex) {
                log.error("Retry still fails:" + ex.getMessage());
                ex.printStackTrace();
            }
            httpCode = response.code();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (httpCode != 200 && httpCode != 201) {
            addResponseLog(POST, url, url, jsonlog, FormLog, response, httpCode, res, response.header("SpanID"), startTime);
        }
        return handleHttpResponse(httpCode, res, response);
    }

    /**
     * 上传单个文件
     *
     * @param Url
     * @param headersParams
     * @param formBody
     * @param name
     * @param filePath
     * @param fileMediaType
     * @return
     * @throws IOException
     */
    public static APIResponse upLoadFile(String Url, Map<String, String> headersParams, Map<String, String> formBody, String name, String filePath, String
            fileMediaType){
        Headers headers = null;
        long startTime = System.currentTimeMillis();
        String res = "";
        RequestBody body = null;
        okhttp3.MultipartBody.Builder MultipartBodyBuilder = new okhttp3.MultipartBody.Builder();
        MultipartBodyBuilder.setType(MultipartBody.FORM);

        OkHttpClient client = getInstance();
        if (headersParams != null) {
            headers = setHeaders(headersParams);
        }

        if (formBody != null) {
            Iterator<String> iterator = formBody.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                MultipartBodyBuilder.addFormDataPart(key, formBody.get(key));
            }
        }
        File file = new File(filePath);
        MultipartBodyBuilder.addFormDataPart(name, file.getName(), RequestBody.create(MediaType.parse(fileMediaType), file));
        body = MultipartBodyBuilder.build();
        Response response = null;
        int httpCode = 0;
        Request req = null;
        addRequestLog(POST, Url, null, null, null);
        try {
            req = new Request.Builder()
                    .url(Url)
                    .headers(headers)
                    .post(body)
                    .build();
            Call call = client.newCall(req);
            response = call.execute();
            res = response.body().string();
            httpCode = response.code();
        } catch (SocketTimeoutException e) {
            log.error("SocketTimeoutException,try again:" + e.getMessage());
            req = new Request.Builder()
                    .url(Url)
                    .headers(headers)
                    .post(body)
                    .build();
            Call call = client.newCall(req);
            try {
                response = call.execute();
                res = response.body().string();
            } catch (Exception ex) {
                log.error("Retry still fails:" + ex.getMessage());
                ex.printStackTrace();
            }

            httpCode = response.code();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addResponseLog(POST, Url, Url, null, null, response, httpCode, res, response.header("SpanID"), startTime);
        return handleHttpResponse(httpCode, res, response);
    }

    /**
     * 删除操作
     *
     * @param Url           请求URL
     * @param headersParams 请求头参数
     * @param UrlMapParams  URL参数
     * @param Jsonbody      请求Json
     * @return
     * @throws IOException
     */
    public static APIResponse delete(String Url, Map<String, String> headersParams, Map<String, String> UrlMapParams, JsonObject Jsonbody){
        Headers headers = null;
        String strParams = "";
        String res = "";
        long startTime = System.currentTimeMillis();
        if (headersParams != null) {
            headers = setHeaders(headersParams);
        }
        if (UrlMapParams != null) {
            strParams = setGetUrlParams(UrlMapParams);
            Url += strParams;
        }
        MediaType JSON = MediaType.parse(String.valueOf(ContentType.APPLICATION_JSON));
        RequestBody body = RequestBody.create(JSON, Jsonbody.toString());
        addRequestLog(DELETE, Url, Url, null, null);
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder()
                .url(Url)
                .headers(headers)
                .delete(body)
                .build();
        Response response = null;
        try {
            sleep(500);
            response = client.newCall(req).execute();
            res = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int httpCode = response.code();

        if (httpCode != 200 && httpCode != 201) {
            addResponseLog(DELETE, Url, Url, null, null, response, httpCode, res, response.header("SpanID"), startTime);
        }
        return handleHttpResponse(httpCode, res, response);
    }

}
package com.hand.baseMethod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.basicConstant.FileMediaType;
import com.hand.basicObject.MyResponse;
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
import org.testng.Reporter;

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
            log.info("MyResponse: {}", new JsonParser().parse(result).getAsJsonObject().toString());
        } catch (Exception e) {

            try {
                log.info("MyResponse: {}", new JsonParser().parse(result).getAsJsonArray().toString());

            } catch (Exception ignored) {
            }
        }
        log.info("Time: {} ms", endTime - startTime);
        if ((endTime - startTime) >= 5000 && (endTime - startTime) < 7000) {
            log.info("请求大于5秒，有待观察:");
        } else if ((endTime - startTime) >= 7000) {
            log.info("请求大于7秒，有待观察:");
        }
        // 报告打印日志
        Reporter.log("httpCode: " + httpCode);
        Reporter.log("spanID: " + response.header("SpanID"));
        Reporter.log("res: " + result);

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
    private static MyResponse handleHttpResponse(int httpCode, String result, Response response) {
        MyResponse myResponse = new MyResponse();
        myResponse.setBody(result);
        myResponse.setStatusCode(httpCode);
        return myResponse;
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
     * @param BodyParams
     * @return
     */
    private static RequestBody SetRequestFormBody(Map<String, String> BodyParams) {
        RequestBody body = null;
        okhttp3.FormBody.Builder formEncodingBuilder = new okhttp3.FormBody.Builder();
        if (BodyParams != null) {
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, BodyParams.get(key));
            }
        }
        body = formEncodingBuilder.build();
        return body;

    }

    /**
     * Post上传图片的参数
     *
     * @param BodyParams
     * @param fileParams
     * @return
     */
    public static RequestBody SetFileRequestBody(Map<String, String> BodyParams, Map<String, String> fileParams) {
        //带文件的Post参数
        RequestBody body = null;
        okhttp3.MultipartBody.Builder MultipartBodyBuilder = new okhttp3.MultipartBody.Builder();
        MultipartBodyBuilder.setType(MultipartBody.FORM);
        RequestBody fileBody = null;

        if (BodyParams != null) {
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                MultipartBodyBuilder.addFormDataPart(key, BodyParams.get(key));
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
     * @param Url
     * @return
     * @throws IOException
     */
    public static MyResponse get(String Url, Map<String, String> headersParams, Map<String, String> UrlMapParams) throws HttpStatusException {
        Headers headers = null;
        String strParams = "";
        long startTime = System.currentTimeMillis();
        OkHttpClient client = getInstance();
        if (headersParams != null) {
            headers = setHeaders(headersParams);
        }
        if (UrlMapParams != null) {
            strParams = setGetUrlParams(UrlMapParams);
        }
        Url += strParams;
        addRequestLog(GET, Url, strParams, null, null);
        Request req = null;
        Response response = null;
        String res = "";
        int httpCode = 0;
        try {
            req = new Request.Builder()
                    .url(Url)
                    .headers(headers)
                    .build();
            response = client.newCall(req).execute();
            res = response.body().string();
            httpCode = response.code();


        } catch (SocketTimeoutException e) {
            log.error("SocketTimeoutException,try again:" + e.getMessage());
            req = new Request.Builder()
                    .url(Url)
                    .headers(headers)
                    .build();
            try {
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
            addResponseLog(GET, Url, Url, null, null, response, httpCode, res, response.header("SpanID"), startTime);
        }
        return handleHttpResponse(httpCode, res, response);
    }

    /**
     * JSON数据修改
     *
     * @param Url
     * @param Jsonbody
     * @return
     * @throws IOException
     */
    public static MyResponse put(String Url, Map<String, String> headersParams, Map<String, String> UrlMapParams, String Jsonbody, Map<String, String> BodyParams) throws HttpStatusException {
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
        if (UrlMapParams != null) {
            strParams = setGetUrlParams(UrlMapParams);
            Url += strParams;

        }
//        form请求
        if (BodyParams != null) {
            body = SetRequestFormBody(BodyParams);
            FormLog = BodyParams.toString();
        }

//        json 请求
        if (Jsonbody != null) {
            MediaType JSON = MediaType.parse(String.valueOf(ContentType.APPLICATION_JSON));
            body = RequestBody.create(JSON, Jsonbody);
        }


        if (BodyParams != null) {
            req = new Request.Builder()
                    .url(Url)
                    .headers(headers)
                    .addHeader("Content-Type", String.valueOf(ContentType.APPLICATION_FORM_URLENCODED))
                    .put(body)
                    .build();
        } else if (Jsonbody != null) {
            req = new Request.Builder()
                    .url(Url)
                    .headers(headers)
                    .addHeader("Content-Type", String.valueOf(ContentType.APPLICATION_JSON))
                    .put(body)
                    .build();
        }
        addRequestLog(req.method(), Url, strParams, Jsonbody, FormLog);
        Response response = null;
        int httpCode = 0;
        try {
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
            addResponseLog(PUT, Url, Url, jsonlog, FormLog, response, httpCode, res, response.header("SpanID"), startTime);
        }
        return handleHttpResponse(httpCode, res, response);
    }

    /**
     * JSON数据提交
     *
     * @param Url
     * @param Jsonbody
     * @return
     * @throws IOException
     */
    public static MyResponse post(String Url, Map<String, String> headersParams, Map<String, String> UrlMapParams, String Jsonbody, Map<String, String> BodyParams) throws HttpStatusException {
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
        if (UrlMapParams != null) {
            strParams = setGetUrlParams(UrlMapParams);
            Url += strParams;

        }
//        form请求
        if (BodyParams != null) {
            body = SetRequestFormBody(BodyParams);
            FormLog = BodyParams.toString();
        }

//        json 请求
        if (Jsonbody != null) {
            MediaType JSON = MediaType.parse(String.valueOf(ContentType.APPLICATION_JSON));
            body = RequestBody.create(JSON, Jsonbody);
        }
        if (BodyParams != null) {
            req = new Request.Builder()
                    .url(Url)
                    .headers(headers)
                    .addHeader("Content-Type", String.valueOf(ContentType.APPLICATION_FORM_URLENCODED))
                    .post(body)
                    .build();
        } else if (Jsonbody != null) {
            req = new Request.Builder()
                    .url(Url)
                    .headers(headers)
                    .addHeader("Content-Type", String.valueOf(ContentType.APPLICATION_JSON))
                    .post(body)
                    .build();
        }
        addRequestLog(req.method(), Url, strParams, Jsonbody, FormLog);
        Response response = null;
        int httpCode = 0;
        try {
            Call call = client.newCall(req);
            response = call.execute();
            res = response.body().string();
            httpCode = response.code();
        } catch (SocketTimeoutException e) {
            log.error("SocketTimeoutException,try again:" + e.getMessage());
            Call call = client.newCall(req);
            try {
                response = call.execute();
                log.info("执行了吗");
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
            addResponseLog(POST, Url, Url, jsonlog, FormLog, response, httpCode, res, response.header("SpanID"), startTime);
        }
        return handleHttpResponse(httpCode, res, response);
    }

    /**
     * 上传单个文件
     *
     * @param Url
     * @param headersParams
     * @param BodyParams
     * @param Name
     * @param FilePath
     * @param FileMediaType
     * @return
     * @throws IOException
     */
    public static MyResponse UpLoadFile(String Url, Map<String, String> headersParams, Map<String, String> BodyParams, String Name, String FilePath, String
            FileMediaType) throws HttpStatusException {
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

        if (BodyParams != null) {
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                MultipartBodyBuilder.addFormDataPart(key, BodyParams.get(key));
            }
        }

        File file = new File(FilePath);
        MultipartBodyBuilder.addFormDataPart(Name, file.getName(), RequestBody.create(MediaType.parse(FileMediaType), file));
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
    public static MyResponse delete(String Url, Map<String, String> headersParams, Map<String, String> UrlMapParams, JsonObject Jsonbody) throws HttpStatusException {
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
            response = client.newCall(req).execute();
            res = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int httpCode = response.code();

        if (httpCode != 200 && httpCode != 201) {
            addResponseLog(DELETE, Url, Url, null, null, response, httpCode, res, response.header("SpanID"), startTime);
        }
        return handleHttpResponse(httpCode, res, response);
    }
}
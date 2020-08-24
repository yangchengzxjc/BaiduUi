package com.hand.api;

import com.hand.basicconstant.BaseConstant;
import com.hand.utils.Md5Util;

import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2020/6/18
 * @Version 1.0
 **/
public class ConsumptionApi extends BaseRequest{


    /**
     *  商旅TMC 用户调用请求头 （包含签名）
     * @param token
     * @return
     */
    public HashMap<String,String> getHeaderSignature(String token,String appName,String corpId,String passWord){
        HashMap<String, String> headersdatas = new HashMap<>();
        headersdatas.put("Authorization", "Bearer "+token);
        headersdatas.put("Content-Type", BaseConstant.CONTENT_TYPE);
        //签名
        headersdatas.put("appName",appName);
        headersdatas.put("corpId",corpId);
        headersdatas.put("format","JSON");
        headersdatas.put("signType","Md5");
        // 加密后的数据为：Md5[appName+Md5[password]+corpId]
        headersdatas.put("signature",getMD5Secret(appName,corpId,passWord));
        headersdatas.put("version","1.0");
        return  headersdatas;
    }

    /**
     * 获取经过MD5加密后的算法  算法如下：Md5[appName+Md5[password]+corpId]
     * @param appName  汇联易消费商注册的应用名称
     * @param corpId   消费商开通的公司Id
     * @return
     */
    public String getMD5Secret(String appName,String corpId,String passWord){
        String md5Password = Md5Util.getMd5(passWord);
        String signature = Md5Util.getMd5(appName+md5Password+corpId);
        return signature;
    }
}

package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.ApplicationApi;
import com.hand.api.ReimbursementApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2020/7/6
 * @Version 1.0
 **/
@Slf4j
public class TravelApplication {

    private ApplicationApi applicationApi;
    private ReimbursementApi reimbursementApi;

    public TravelApplication(){
        applicationApi = new ApplicationApi();
        reimbursementApi =new ReimbursementApi();
    }

    /**
     * 返回差旅申请单的表单的formOID
     * @param employee
     * @param formName
     * @return
     * @throws HttpStatusException
     */
    public String applicationFormOID(Employee employee,String formName) throws HttpStatusException {
        JsonArray array = applicationApi.getAvailableform(employee,employee.getJobId());
        return GsonUtil.getJsonValue(array,"formName",formName,"formOID");
    }

    /**
     *创建（保存）差旅申请单
     * @param employee
     * @param formName
     * @param component
     * @throws HttpStatusException
     */
    public HashMap<String,String> createTravelApplication(Employee employee, String formName, FormComponent component) throws HttpStatusException {
        JsonObject formDetail = reimbursementApi.getFormDetail(employee,applicationFormOID(employee,formName));
        log.info("差旅表单的详情：{}",formDetail);
        JsonObject jsonObject = applicationApi.createApplication(employee,formDetail,component,employee.getJobId(),employee.getUserOID());
        HashMap<String,String> info = new HashMap<>();
        info.put("applicationOID",jsonObject.get("applicationOID").getAsString());
        info.put("businessCode",jsonObject.get("businessCode").getAsString());
        log.info("businessCode:{}",info.get("businessCode"));
        return info;
    }
}

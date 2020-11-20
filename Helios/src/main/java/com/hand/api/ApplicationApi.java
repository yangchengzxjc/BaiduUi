package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.basicconstant.ApiPath;
import com.hand.basicconstant.HeaderKey;
import com.hand.basicconstant.ResourceId;
import com.hand.utils.GsonUtil;
import com.hand.utils.UTCTime;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author peng.zhang
 * @Date 2020/7/6
 * @Version 1.0
 **/
@Slf4j
public class ApplicationApi extends BaseRequest{


    /**
     *搜索申请单
     * @param employee
     * @param businessCode
     * @return
     * @throws HttpStatusException
     */
    public JsonArray searchApplication(Employee employee, String businessCode) throws HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.MY_APPLICATION_LIST;
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("withBudget", "false");
        datas.put("withCustomForm", "false");
        datas.put("withCustomFormProperty", "false");
        datas.put("withCustomFormValue", "false");
        datas.put("withParticipant", "false");
        datas.put("withUserInfo", "false");
        datas.put("status", "1001&status=1002&status=1003&status=1004&status=1005&status=1006&status=1007&status=1008");
        datas.put("method", "0");
        datas.put("size", "10");
        datas.put("businessCode", businessCode);
        String res= doGet(url,getHeader(employee.getAccessToken()),datas,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 获得可用的申请的表单
     * @param employee
     * @param jobId
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getAvailableform(Employee employee,String jobId) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.GETAVAILABLE_BXFORMS;
        Map<String, String> datas = new HashMap<>();
        datas.put("formType","101");
        datas.put("jobId", jobId);
        String res = doGet(url, getHeader(employee.getAccessToken()), datas, employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 新建差旅申请单
     * @param employee
     * @param formdetal
     * @param component  component如果是空的对象的话那就是进行造数据
     * @throws HttpStatusException
     */
    public JsonObject createApplication(Employee employee,JsonObject formdetal, FormComponent component) throws HttpStatusException {
        JsonObject responseEntity=null;
        JsonArray  custFormValues;
        ReimbursementApi reimbursementApi =new ReimbursementApi();
        JsonArray customFormFields = formdetal.get("customFormFields").getAsJsonArray();
        String url = employee.getEnvironment().getUrl()+ ApiPath.TRAVEL_APPLICATION_SAVE;
        if(component==null){
            custFormValues = reimbursementApi.processCustFormValues(employee,customFormFields);
        }else{
            custFormValues = reimbursementApi.processCustFormValues(employee,customFormFields,component);
        }
        formdetal.remove("custFormValues");
        formdetal.remove("customFormFields");
        formdetal.add("custFormValues",custFormValues);
        formdetal.add("customFormFields",customFormFields);
        formdetal.addProperty("visibleUserScope",1001);
        formdetal.addProperty("applicant",(String) null);
        formdetal.addProperty("takeQuota",false);
        formdetal.addProperty("quotaCurrencyCode","");
        formdetal.addProperty("quotaAmount",0);
        formdetal.addProperty("quotaBankCardOID","");
        formdetal.addProperty("referenceApplicationOID","");
        formdetal.add("countersignApproverOIDs",new JsonArray());
        formdetal.addProperty("applicantOID",employee.getUserOID());
        formdetal.addProperty("jobId",employee.getJobId());
        formdetal.add("travelApplication",new JsonObject());
        String res= doPost(url,getHeader(employee.getAccessToken()),null,formdetal.toString(),null,employee);
        responseEntity=new JsonParser().parse(res).getAsJsonObject();
        return  responseEntity;
    }

    /**
     * 费用申请单新建
     * @param employee
     * @param formdetal
     * @param component   造数据的话componnet 为空的话  则认为使用默认的控件
     * @param budget  预算明细   可以先拼成jsonArray然后toString()
     * @throws HttpStatusException
     */
    public JsonObject expenseApplication(Employee employee,JsonObject formdetal,FormComponent component,String budget) throws HttpStatusException {
        JsonObject responseEntity=null;
        JsonArray  custFormValues;
        ReimbursementApi reimbursementApi =new ReimbursementApi();
        JsonArray customFormFields = formdetal.get("customFormFields").getAsJsonArray();
        String url = employee.getEnvironment().getUrl()+ ApiPath.EXPENSE_APPLICATION_SAVE;
        if(component==null){
            custFormValues = reimbursementApi.processCustFormValues(employee,customFormFields);
        }else{
            custFormValues = reimbursementApi.processCustFormValues(employee,customFormFields,component);
        }
        //给费用申请单添加预算  费用申请单预算明细  一般是在表头必填
        for(int i=0; i<custFormValues.size();i++){
            if(custFormValues.get(i).getAsJsonObject().get("fieldName").getAsString().equals("预算明细")){
                custFormValues.get(i).getAsJsonObject().addProperty("value",budget);
            }
        }
        formdetal.remove("custFormValues");
        formdetal.remove("customFormFields");
        formdetal.add("custFormValues",custFormValues);
        formdetal.add("customFormFields",customFormFields);
        formdetal.addProperty("applicant",(String) null);
        formdetal.addProperty("takeQuota",false);
        formdetal.addProperty("quotaCurrencyCode","");
        formdetal.addProperty("quotaAmount",0);
        formdetal.addProperty("quotaBankCardOID","");
        formdetal.addProperty("referenceApplicationOID","");
        formdetal.add("countersignApproverOIDs",new JsonArray());
        formdetal.addProperty("applicantOID",employee.getUserOID());
        formdetal.addProperty("jobId",employee.getJobId());
        String res= doPost(url,getHeader(employee.getAccessToken()),null,formdetal.toString(),null,employee);
        responseEntity=new JsonParser().parse(res).getAsJsonObject();
        return  responseEntity;
    }

    /**
     * 添加一个飞机的行程
     * @param employee
     * @param applicationOID  申请单的OID
     * @param flightItineraries  飞机行程对象
     */
    public <E> JsonArray addFlightItinerary(Employee employee, String applicationOID, ArrayList<E> flightItineraries) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.ADD_FLIGHT_ITINERARY;
        Map<String, String> data = new HashMap<>();
        data.put("applicationOID",applicationOID);
        JsonArray array = new JsonParser().parse(GsonUtil.objectToString(flightItineraries)).getAsJsonArray();
        String res= doPost(url,getHeader(employee.getAccessToken()),data,array.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 添加一个酒店的行程
     * @param employee
     * @param applicationOID
     * @param hotelItineraries
     */
    public <E> JsonArray addHotelItinerary(Employee employee, String applicationOID, ArrayList<E> hotelItineraries) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.ADD_HOTEL_ITINERARY;
        Map<String, String> data = new HashMap<>();
        data.put("applicationOID",applicationOID);
        JsonArray array = new JsonParser().parse(GsonUtil.objectToString(hotelItineraries)).getAsJsonArray();
        String res= doPost(url,getHeader(employee.getAccessToken()),data,array.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 添加一个火车的行程
     * @param employee
     * @param applicationOID
     * @param trainItineraries
     */
    public <E> JsonArray addTrainItinerary(Employee employee, String applicationOID, ArrayList<E> trainItineraries) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.ADD_TRAIN_ININERARY;
        Map<String, String> data = new HashMap<>();
        data.put("applicationOID",applicationOID);
        JsonArray array = new JsonParser().parse(GsonUtil.objectToString(trainItineraries)).getAsJsonArray();
        String res= doPost(url,getHeader(employee.getAccessToken()),data,array.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }


    /**
     * 获取预算费用类型
     * @param employee
     * @param formOID
     */
    public JsonArray getBudgetExpenseType(Employee employee,String formOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.GET_EXPENSE_REPORT_EXPENSE_TYPES;
        JsonObject object =new JsonObject();
        object.addProperty("companyOID",employee.getCompanyOID());
        object.addProperty("formOID",formOID);
        object.addProperty("subsidyType",99);
        object.addProperty("userOID",employee.getUserOID());
        object.addProperty("setOfBooksId",employee.getSetOfBookId());
        String res= doPost(url,getHeader(employee.getAccessToken()),null,object.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject().get("rows").getAsJsonArray();
    }

    /**
     * 获取申请单详情
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getApplicationDetail(Employee employee,String applicationOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ String.format(ApiPath.APPLICATION_DETAL,applicationOID);
        String res = doGet(url, getHeader(employee.getAccessToken()), null, employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 差旅申请单提交
     * @param employee
     * @param applicationDetail  申请单详情
     * @return  返回申请单的相关信息
     * @throws HttpStatusException
     */
    public JsonObject submitApplication(Employee employee, JsonObject applicationDetail) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.SUBMIT_APPLICATION;
        String res= doPost(url,getHeader(employee.getAccessToken()),null,applicationDetail.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject().get("applicationDTO").getAsJsonObject();
    }

    /**
     *差旅申请单提交检查
     * @param employee
     */
    public JsonObject submitCheck(Employee employee,String applicationOID,JsonObject applicationDetail) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.SUBMIT_CHECK;
        HashMap<String,String> map = new HashMap<>();
        map.put("applicationOID",applicationOID);
        map.put("startDate", UTCTime.getNowUtcTime());
        map.put("endDate",UTCTime.getNowUtcTime());
        map.put("userOIDs",employee.getUserOID());
        String res= doPost(url,getHeader(employee.getAccessToken()),map,applicationDetail.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     *费用申请单提交
     * @param employee
     * @param expenseApplicationDetail
     * @return
     * @throws HttpStatusException
     */
    public JsonObject submitExpenseApplication(Employee employee, JsonObject expenseApplicationDetail) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.SUBMIT_EXPENSE_APPLICATION;
        String res= doPost(url,getHeader(employee.getAccessToken()),null,expenseApplicationDetail.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject().get("applicationDTO").getAsJsonObject();
    }

    /**
     * 获取差旅申请单中的行程
     * @param employee
     * @param applicationOID
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getItinerarys(Employee employee,String applicationOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.APPLICATION_ITINERARY;
        HashMap<String,String> map = new HashMap<>();
        map.put("applicationOID",applicationOID);
        map.put("itineraryShowDetails","true");
        map.put("withItemDetail","true");
        map.put("withRequestDetail","true");
        String res= doGet(url,getHeader(employee.getAccessToken()),map,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 获取差旅申请单中的预算费用信息   如果是自动获取的预算费用可以调这个方法
     * @param employee
     * @param applicationOID
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getBudgetExpense(Employee employee,String applicationOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.BUDGET_EXPENSE;
        HashMap<String,String> map = new HashMap<>();
        map.put("applicationOID",applicationOID);
        String res= doGet(url,getHeader(employee.getAccessToken()),map,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 获取表单中的消费商配置
     * @param employee
     * @param formOID
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getFormVendorControl(Employee employee,String formOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+String.format(ApiPath.GETFORMVENDORCONTROL,formOID);
        Map<String,String> maps = new HashMap<>();
        maps.put("roleType","TENANT");
        String res = doGet(url,getHeader(employee.getAccessToken(), HeaderKey.FORM_CONFIG, ResourceId.FORM_CONFIG),maps,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

}

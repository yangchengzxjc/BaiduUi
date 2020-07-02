package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.InvoiceComponent;
import com.hand.basicconstant.ApiPath;
import com.hand.basicObject.Employee;
import com.hand.utils.UTCTime;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author peng.zhang
 * @Date 2020/6/9
 * @Version 1.0
 **/

@Slf4j
public class ExpenseApi extends BaseRequest{


    private ComponentQuery componentQuery;

    private EmployeeInfoApi employeeInfoApi;

    public ExpenseApi(){
        componentQuery =new ComponentQuery();
        employeeInfoApi =new EmployeeInfoApi();
    }



    /**
     * 报销单新建费用获取费用类型
     * @param
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getExpenseReportExpenseTypes(Employee employee,String expenseReportOID) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.GET_EXPENSE_REPORT_EXPENSE_TYPES;
        JsonObject datas = new JsonObject();
        datas.addProperty("setOfBooksId", employee.getSetOfBookId());
        datas.addProperty("expenseReportOID",expenseReportOID);
        datas.addProperty("userOID", employee.getUserOID());
        datas.addProperty("createManually", "true");
        String res=doPost(url,getHeader(employee.getAccessToken()),null,datas.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 管理员查看费用类型
     * @param employee
     * @param setOfBooksId
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getexpenseTypes(Employee employee, String setOfBooksId) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.GET_EXPENSE_TYPES;
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("setOfBooksId", setOfBooksId);
        datas.put("createdManually", "");
        datas.put("enabled","");
        datas.put("roleType","TENANT");
        String res= doGet(url,getHeader(employee.getAccessToken()),datas,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 集团管理员查询基础数据
     * @param employee
     * @return
     * @throws HttpStatusException

     */
    public JsonArray managerGetSaleTax(Employee employee) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.MANAGER_GET_SALE_TAX;
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("size","99");
        datas.put("method","0");
        datas.put("roleType", "TENANT");
        String res= doGet(url,getHeader(employee.getAccessToken()),datas,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }


    /**
     * 获得报销单的费用控件信息
     * @param expenseTypeid
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getExpenseTypeInfo(Employee employee, String expenseTypeid) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ String.format(ApiPath.GET_EXPENSE_TYPE,expenseTypeid);
        String res= doGet(url,getHeader(employee.getAccessToken()),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }


    /**
     * 我的账本中删除费用
     * @param employee
     * @param invoiceOIDs
     * @return
     * @throws HttpStatusException
     */
    public  void invoiceDelete(Employee employee, String  invoiceOIDs) throws HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.INVOICEOID_DELETE;
        Map<String, String> formbody = new HashMap<>();
        formbody.put("invoiceOIDs", invoiceOIDs);
        JsonObject body=new JsonObject();
        doDlete(url,getHeader(employee.getAccessToken()),formbody,body, employee);
    }

    /**
     * @param employee
     * @param currencyCode  币种
     * @param expenseTypeinfo
     * @param expenseReportOID   报销单的oid
     * @param participants    参与人信息
     * @param participantOid     同行人的userOid
     * @param applicationBusinessCode    申请单的businessCode
     * @param amount    费用金额
     * @param attachments   附件  没有则为空
     * @param startDate    开始结束日期   例如   {"startDate":"2020-06-09T16:00:00.000Z","endDate":"2020-06-11T16:00:00.000Z","duration":3}
     * @param COMPANY_PAID    是否公司支付
     * @param expenseApportion    分摊  没有的化就传个空的json array
     * @param departureLocation   出发地信息
     * @param destinationLocation   目的地信息  例如：{"latitude":"34.277618","longitude":"108.961831","address":"西安站"}
     * @return
     * @throws HttpStatusException
     */
    public  JsonObject expenseReportCreateinvoice(Employee employee, String currencyCode,JsonObject  expenseTypeinfo, String expenseReportOID,
                                                  String participantOid,String applicationBusinessCode, double amount,
                                                  JsonArray attachments, JsonObject startDate, JsonObject participants, JsonArray receiptList,
                                                  boolean COMPANY_PAID,JsonArray expenseApportion,JsonObject departureLocation,JsonObject destinationLocation) throws HttpStatusException {
        JsonObject responseEntity=null;
        String url=employee.getEnvironment().getUrl()+ ApiPath.CREATEINVOICE;
        JsonObject body=new JsonObject();
        String accountSetId=expenseTypeinfo.get("setOfBooks").getAsJsonObject().get("accountSetId").getAsString();
        String messageKey=null;
        if (!expenseTypeinfo.get("messageKey").isJsonNull()) {
            messageKey=expenseTypeinfo.get("messageKey").getAsString();
        }
        JsonArray fieldsdata=expenseTypeinfo.get("fields").getAsJsonArray();
        //开始给控件塞值
        for (int i=0;i<fieldsdata.size();i++) {
            JsonObject data = fieldsdata.get(i).getAsJsonObject();
            String fieldType=data.get("fieldType").getAsString();
            switch (fieldType)
            {
                case "ASSOCIATE_APPLICATION":                //关联申请单
                    data.addProperty("value",applicationBusinessCode);
                    break;
                case "CUSTOM_ENUMERATION":                          //值列表
                    JsonArray customenumerationlist =componentQuery.getCustomEumerationOid(employee,data.get("customEnumerationOID").getAsString());
                    data.addProperty("value", customenumerationlist.get(0).getAsJsonObject().get("value").getAsString());
                    break;
                case  "MONTH":
                    data.addProperty("value", UTCTime.getNowUtcTime());
                    break;
                case  "TEXT":
                    data.addProperty("value", "test");
                    break;
                case  "LONG":
                    data.addProperty("value", 125L);
                    break;
                case  "POSITIVE_INTEGER":
                    data.addProperty("value", 125L);
                    break;
                case  "DATETIME":
                    data.addProperty("value", UTCTime.getNowUtcTime());
                    break;
                case  "DOUBLE":
                    data.addProperty("value", 1.25f);
                    break;
                case  "DATE":
                    data.addProperty("value", UTCTime.getNowUtcTime());
                    break;
                case  "LOCATION":              //城市控件
                    String  code=componentQuery.locationSearch(employee,"大").get(0).getAsJsonObject().get("code").getAsString();
                    data.addProperty("value", code);
                    break;
                case  "PARTICIPANTS":                 //参与人
                    data.addProperty("value",participants.toString());
                    break;
                case  "PARTICIPANT":                 //同行人
                    data.addProperty("value",participantOid);
                    break;
                case  "START_DATE_AND_END_DATE":               //开始结束日期
                    data.addProperty("value",startDate.toString());
                    break;
                case  "TEXT_AREA":
                    data.addProperty("value", "text");
                    break;
                case  "COMPANY_PAID":             //公司支付   1002是公司已付    1001是公司未付费用
                    if (COMPANY_PAID){
                        data.addProperty("value", "1002");
                    }
                    else {
                        data.addProperty("value", "1001");
                    }
                    break;
                case  "GPS":                //出发地或者目的地
                    log.info("location:{}"+departureLocation.toString());
                    if(data.get("messageKey").getAsString().equalsIgnoreCase("departure.location")){
                        data.addProperty("value",departureLocation.toString());
                    }else if(data.get("messageKey").getAsString().equalsIgnoreCase("destination.location")){
                        data.addProperty("value",destinationLocation.toString());
                    }
                    break;
            }
        }
//        结束给控件塞值  附件
        if (attachments !=null) {
            body.add("attachments",attachments);
        }
        else {
            body.add("attachments",new JsonArray());
        }
        body.addProperty("invoiceStatus","INIT");
        body.addProperty("readonly",expenseTypeinfo.get("readonly").getAsBoolean());
        body.addProperty("recognized",false);
        if(!currencyCode.equals("")){
            //币种默认为本位币 人民币可不传
            body.addProperty("currencyCode",currencyCode);
        }else{
            body.addProperty("currencyCode","CNY");
        }
        body.addProperty("expenseTypeName",expenseTypeinfo.get("name").getAsString());
        body.addProperty("expenseTypeOID",expenseTypeinfo.get("expenseTypeOID").getAsString());
        body.addProperty("expenseTypeId",expenseTypeinfo.get("id").getAsString());
        body.addProperty("expenseTypeIconName",expenseTypeinfo.get("iconName").getAsString());
        body.addProperty("expenseTypeKey",messageKey);
        body.addProperty("pasteInvoiceNeeded",expenseTypeinfo.get("pasteInvoiceNeeded").getAsBoolean());
        body.addProperty("invoiceRequired",expenseTypeinfo.get("invoiceRequired").getAsBoolean());
        body.addProperty("amount",new DecimalFormat("#0.00").format(amount));
        body.addProperty("actualCurrencyRate","1");
        body.add("receiptList",receiptList);
        body.addProperty("createdDate",UTCTime.getNowUtcTime());
        if(!currencyCode.equals("")){
            //币种默认为本位币 人民币可不传
            body.addProperty("invoiceCurrencyCode",currencyCode);
        }else{
            body.addProperty("invoiceCurrencyCode","CNY");
        }
        body.addProperty("comment","事由");
        body.addProperty("invoiceInsteadReason","");
        body.add("data",fieldsdata);
        body.addProperty("baseCurrency","CNY");
        body.addProperty("updateRate",true);
//        分摊项
        body.add("expenseApportion", expenseApportion);
        if (!expenseReportOID.equals("")) {
            body.addProperty("expenseReportOID",expenseReportOID);
        }
        body.addProperty("timeZoneOffset",480);
        body.addProperty("ownerOID",employee.getUserOID());
        body.addProperty("paymentType",1001);
        getHeader(employee.getAccessToken()).put("Authorization", "Bearer "+employee.getAccessToken()+"");
        String res= doPost(url,getHeader(employee.getAccessToken()),null,body.toString(),null, employee);
        responseEntity=new JsonParser().parse(res).getAsJsonObject();
        return  responseEntity;
    }

    /**
     *
     * @param employee
     * @param expenseTypeinfo
     * @param expenseReportOID   报销单的oid
     * @param amount    费用金额
     * @param attachments   附件  没有则为空
     * @return
     * @throws HttpStatusException
     */
    public  JsonObject expenseReportCreateinvoice(Employee employee, JsonObject expenseTypeinfo, InvoiceComponent component,String expenseReportOID,
                                                  double amount, JsonArray attachments, JsonArray receiptList,JsonArray expenseApportion) throws HttpStatusException {
        JsonObject responseEntity=null;
        String url=employee.getEnvironment().getUrl()+ ApiPath.CREATEINVOICE;
        JsonObject body=new JsonObject();
        String accountSetId=expenseTypeinfo.get("setOfBooks").getAsJsonObject().get("accountSetId").getAsString();
        String messageKey=null;
        if (!expenseTypeinfo.get("messageKey").isJsonNull()) {
            messageKey=expenseTypeinfo.get("messageKey").getAsString();
        }
        JsonArray fieldsdata=expenseTypeinfo.get("fields").getAsJsonArray();
        //开始给控件塞值
        for (int i=0;i<fieldsdata.size();i++) {
            JsonObject data = fieldsdata.get(i).getAsJsonObject();
            String fieldType=data.get("fieldType").getAsString();
            switch (fieldType)
            {
                case "ASSOCIATE_APPLICATION":                //关联申请单
                    data.addProperty("value",component.getApplication());
                    break;
                case "CUSTOM_ENUMERATION":                          //值列表
                    JsonArray customenumerationlist =componentQuery.getCustomEumerationOid(employee,data.get("customEnumerationOID").getAsString());
                    data.addProperty("value", customenumerationlist.get(0).getAsJsonObject().get("value").getAsString());
                    break;
                case  "MONTH":
                    data.addProperty("value", UTCTime.getNowUtcTime());
                    break;
                case  "TEXT":
                    data.addProperty("value", "test");
                    break;
                case  "LONG":
                    data.addProperty("value", 125L);
                    break;
                case  "POSITIVE_INTEGER":
                    data.addProperty("value", 125L);
                    break;
                case  "DATETIME":
                    data.addProperty("value",component.getDateTime());
                    break;
                case  "DOUBLE":
                    data.addProperty("value", 1.25f);
                    break;
                case  "DATE":
                    data.addProperty("value", UTCTime.getNowUtcTime());
                    break;
                case  "LOCATION":              //城市控件
                    String  code=componentQuery.locationSearch(employee,"大").get(0).getAsJsonObject().get("code").getAsString();
                    data.addProperty("value", code);
                    break;
                case  "PARTICIPANTS":                 //参与人
                    data.addProperty("value",component.getParticipants());
                    break;
                case  "PARTICIPANT":                 //同行人
                    data.addProperty("value",component.getParticipant());
                    break;
                case  "START_DATE_AND_END_DATE":               //开始结束日期
                    data.addProperty("value",component.getStartAndEndData());
                    break;
                case  "TEXT_AREA":
                    data.addProperty("value", "text");
                    break;
                case  "COMPANY_PAID":             //公司支付   1002是公司已付    默认1001是公司未付费用
                    if (component.isCompanyPay()){
                        data.addProperty("value", "1002");
                    }
                    else {
                        data.addProperty("value", "1001");
                    }
                    break;
                case  "GPS":                //出发地或者目的地
                    if(data.get("messageKey").getAsString().equalsIgnoreCase("departure.location")){
                        data.addProperty("value",component.getDeparture());
                    }else if(data.get("messageKey").getAsString().equalsIgnoreCase("destination.location")){
                        data.addProperty("value",component.getDestination());
                    }
                    break;
            }
        }
//        结束给控件塞值  附件
        if (attachments !=null) {
            body.add("attachments",attachments);
        }
        else {
            body.add("attachments",new JsonArray());
        }
        body.addProperty("invoiceStatus","INIT");
        body.addProperty("readonly",expenseTypeinfo.get("readonly").getAsBoolean());
        body.addProperty("recognized",false);
        if(component.getCurrencyCode()==null){
            body.addProperty("currencyCode","CNY");
        }else{
            body.addProperty("currencyCode",component.getCurrencyCode());
        }
        body.addProperty("expenseTypeName",expenseTypeinfo.get("name").getAsString());
        body.addProperty("expenseTypeOID",expenseTypeinfo.get("expenseTypeOID").getAsString());
        body.addProperty("expenseTypeId",expenseTypeinfo.get("id").getAsString());
        body.addProperty("expenseTypeIconName",expenseTypeinfo.get("iconName").getAsString());
        body.addProperty("expenseTypeKey",messageKey);
        body.addProperty("pasteInvoiceNeeded",expenseTypeinfo.get("pasteInvoiceNeeded").getAsBoolean());
        body.addProperty("invoiceRequired",expenseTypeinfo.get("invoiceRequired").getAsBoolean());
        body.addProperty("amount",amount);
        //如果非人民币的汇率要加上汇率 使用equals会出现空指针,因为初始化的时候没有赋值。
        if(component.getCurrencyCode()!=("CNY")){
            body.addProperty("actualCurrencyRate",component.getRate());
        }
        body.add("receiptList",receiptList);
        body.addProperty("createdDate",UTCTime.getNowUtcTime());
        if(component.getCurrencyCode()==null){
            body.addProperty("invoiceCurrencyCode","CNY");
        }else {
            body.addProperty("invoiceCurrencyCode",component.getCurrencyCode());
        }
        body.addProperty("comment","事由");
        body.addProperty("invoiceInsteadReason","");
        body.add("data",fieldsdata);
        body.addProperty("baseCurrency","CNY");
        body.addProperty("updateRate",true);
//        分摊项
        body.add("expenseApportion", expenseApportion);
        if (!expenseReportOID.equals("")) {
            body.addProperty("expenseReportOID",expenseReportOID);
        }
        body.addProperty("timeZoneOffset",480);
        body.addProperty("ownerOID",employee.getUserOID());
        body.addProperty("paymentType",1001);
        String res= doPost(url,getHeader(employee.getAccessToken()),null,body.toString(),null, employee);
        responseEntity=new JsonParser().parse(res).getAsJsonObject();
        return  responseEntity;
    }

    /**
     * 获得我的账本列表基础
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public  JsonArray getInvoiceLlist(Employee employee) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.INVOICES_LIST;
        JsonObject object=new JsonObject();
        object.addProperty("page",0);
        object.addProperty("size",20);
        object.addProperty("expenseTypeIds", (String) null);
        String Res= doPost(url,getHeader(employee.getAccessToken()),null,object.toString(),null,employee);
        return new JsonParser().parse(Res).getAsJsonObject().get("rows").getAsJsonArray();
    }

    /**
     * 获得系统当前费用类型可以选择的控件信息列表
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getExpenseWidgetsList(Employee employee) throws HttpStatusException {
        String Url=employee.getEnvironment().getUrl()+ ApiPath.EXPENSE_WIDGETS_LIST;
        Map<String,String> UrlParam=new HashMap<>();
        UrlParam.put("roleType","TENANT");
        String Res= doGet(Url,getHeader(employee.getAccessToken()),UrlParam,employee);
        return new JsonParser().parse(Res).getAsJsonObject();
    }

    /**
     * 更新费用的控件
     * @param employee
     * @param expenseTypesId
     * @param fields
     * @return
     * @throws HttpStatusException
     */
    public JsonObject updateExpenseTypesDetail(Employee employee,String expenseTypesId,JsonArray fields) throws HttpStatusException {
        String Url=employee.getEnvironment().getUrl()+ String.format(ApiPath.UPDATE_EXPENSE_TYPES_WIDGETS,expenseTypesId);
        Map<String,String> UrlParam=new HashMap<>();
        UrlParam.put("roleType","TENANT");
        JsonObject Jsonbody=new JsonObject();
        Jsonbody.addProperty("timeSupported",false);
        Jsonbody.add("fields",fields);
        String Res= doPost(Url,getHeader(employee.getAccessToken()),UrlParam,Jsonbody.toString(),null,employee);
        return new JsonParser().parse(Res).getAsJsonObject();
    }

    public JsonArray queryExpenseTypeId(Employee employee)throws HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ String.format(ApiPath.QUERY_All_EXPENSE_INFO);
        String res= doGet(url,getHeader(employee.getAccessToken()),null,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 查询报销单内的费用详情
     * @param employee
     * @param expenseReportOID
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getInvoiceDetail(Employee employee,String expenseReportOID) throws HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ApiPath.queryInvoiceDetail;
        Map<String,String> UrlParam=new HashMap<>();
        UrlParam.put("expenseReportOID",expenseReportOID);
        String res= doGet(url,getHeader(employee.getAccessToken()),UrlParam,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     *费用转交搜索用户
     * @param employee
     */
    public JsonArray searchTransferUser(Employee employee, String setOfBooksId) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.SEARCHTRANSFERUser;
        Map<String,String> urlParam=new HashMap<>();
        urlParam.put("setOfBooksId",setOfBooksId);
        urlParam.put("page","0");
        urlParam.put("size","10");
        urlParam.put("status","1001");
        String res= doGet(url,getHeader(employee.getAccessToken()),urlParam,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 费用转交
     * @param employee
     * @param reimbursementUserId
     * @param array
     */
    public JsonObject transferTo(Employee employee,String reimbursementUserId,JsonArray array) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.TRANSFER_TO;
        JsonObject object =new JsonObject();
        object.addProperty("reimbursementUserId",reimbursementUserId);
        object.add("invoiceOIDs",array);
        String Res= doPost(url,getHeader(employee.getAccessToken()),null,object.toString(),null,employee);
        return new JsonParser().parse(Res).getAsJsonObject();
    }
}
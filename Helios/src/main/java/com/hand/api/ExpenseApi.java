package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.component.InvoiceComponent;
import com.hand.basicConstant.ApiPath;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.DiDi;
import com.hand.utils.GsonUtil;
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


    private ComponentQueryApi componentQueryApi;

    public ExpenseApi(){
        componentQueryApi =new ComponentQueryApi();
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
     * @param invoiceOID
     * @return
     * @throws HttpStatusException
     */
    public  void invoiceDelete(Employee employee, String  invoiceOID) throws HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ String.format(ApiPath.INVOICEOID_DELETE,invoiceOID);
        JsonObject body=new JsonObject();
        doDlete(url,getHeader(employee.getAccessToken()),null,body, employee);
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
                    JsonArray customenumerationlist = componentQueryApi.getCustomEumerationOid(employee,data.get("customEnumerationOID").getAsString());
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
                    String  code= componentQueryApi.locationSearch(employee,"大").get(0).getAsJsonObject().get("code").getAsString();
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
        body.add("expenseAmortise",new JsonArray());
        body.addProperty("timeZoneOffset",480);
        body.addProperty("ownerOID",employee.getUserOID());
        body.addProperty("paymentType",1001);
        String res= doPost(url,getHeader(employee.getAccessToken()),null,body.toString(),null, employee);
        responseEntity=new JsonParser().parse(res).getAsJsonObject();
        return  responseEntity;
    }

    /**
     *
     * @param employee
     * @param expenseTypeInfo
     * @param expenseReportOID   报销单的oid
     * @param amount    费用金额
     * @param attachments   附件  没有则为空
     * @return
     * @throws HttpStatusException
     */
    public  JsonObject expenseReportCreateinvoice(Employee employee, JsonObject expenseTypeInfo, InvoiceComponent component,String expenseReportOID,
                                                  double amount, JsonArray attachments, JsonArray receiptList,JsonArray expenseApportion) throws HttpStatusException {
        JsonObject responseEntity=null;
        String url=employee.getEnvironment().getUrl()+ ApiPath.CREATEINVOICE;
        JsonObject body=new JsonObject();
        //暂时没人用 先注释掉
//        String accountSetId=expenseTypeInfo.get("setOfBooks").getAsJsonObject().get("accountSetId").getAsString();
        String messageKey=null;
        if (!expenseTypeInfo.get("messageKey").isJsonNull()) {
            messageKey=expenseTypeInfo.get("messageKey").getAsString();
        }
        JsonArray fieldsdata=expenseTypeInfo.get("fields").getAsJsonArray();
        //开始给控件塞值
        for (int i=0;i<fieldsdata.size();i++) {
            JsonObject data = fieldsdata.get(i).getAsJsonObject();
            String name=data.get("name").getAsString();
            switch (name)
            {
                case "关联申请单":                //关联申请单
                    data.addProperty("value",component.getApplication());
                    break;
                case "值列表":                          //值列表
                    JsonArray customenumerationlist = componentQueryApi.getCustomEumerationOid(employee,data.get("customEnumerationOID").getAsString());
                    data.addProperty("value", customenumerationlist.get(0).getAsJsonObject().get("value").getAsString());
                    break;
                case  "月份":
                    data.addProperty("value", UTCTime.getNowUtcTime());
                    break;
                case  "文本":
                    data.addProperty("value", "test");
                    break;
                case  "正整数":
                    data.addProperty("value", 125L);
                    break;
                case  "POSITIVE_INTEGER":
                    data.addProperty("value", 125L);
                    break;
                case  "时间":
                    data.addProperty("value",component.getDateTime());
                    break;
                case  "浮点数":
                    data.addProperty("value", 1.25f);
                    break;
                case  "日期":
                    data.addProperty("value", UTCTime.getNowUtcTime());
                    break;
                case  "城市":              //城市控件
                    data.addProperty("value", component.getCity());
                    break;
                case  "参与人"://参与人
                    if(component.getParticipants()!=null){
                        JsonArray array = new JsonArray();
                        //参与人员
                        if(component.getParticipants().getClass().equals(String [].class)){
                            String [] participant = (String [])component.getParticipants();
                            for(int j=0;j<participant.length;j++){
                                array.add(setParticipant(employee,expenseReportOID,participant[j]));
                            }
                            data.addProperty("value",array.toString());
                        }
                        if(component.getParticipants().getClass().equals(String.class)){
                            String participant = (String) component.getParticipants();
                            data.addProperty("value",participant);
                        }
                    }
                    break;
                case  "同行人":                 //同行人
                    data.addProperty("value",component.getParticipant());
                    break;
                case  "开始结束日期":               //开始结束日期
                    data.addProperty("value",component.getStartAndEndData());
                    break;
                case  "TEXT_AREA":
                    data.addProperty("value", "text");
                    break;
                case  "公司已付":             //公司支付   1002是公司已付    默认1001是公司未付费用
                    if (component.isCompanyPay()){
                        data.addProperty("value", "1002");
                    }
                    else {
                        data.addProperty("value", "1001");
                    }
                    break;
                case "出发地":                //出发地或者目的地
                        data.addProperty("value",component.getDeparture());
                    break;
                case "目的地" :
                    data.addProperty("value",component.getDestination());
                    break;
                case "飞机舱等":
                    data.addProperty("value",component.getCabin());
                    break;
                case "火车座等":
                    data.addProperty("value",component.getCabin());
                    break;
                case "轮船座次":
                    data.addProperty("value",component.getCabin());
                    break;
                case "出发城市":
                    data.addProperty("value",component.getSetoffCity());
                    break;
                case "到达城市":
                    data.addProperty("value",component.getArriveCity());
                    break;
                case "陪同人数":
                    data.addProperty("value",component.getAccompanying());
                    break;
                case "招待人数":
                    data.addProperty("value",component.getHospitalized());
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
        body.addProperty("readonly",expenseTypeInfo.get("readonly").getAsBoolean());
        body.addProperty("recognized",false);
        if(component.getCurrencyCode()==null){
            body.addProperty("currencyCode","CNY");
        }else{
            body.addProperty("currencyCode",component.getCurrencyCode());
        }
        body.addProperty("expenseTypeName",expenseTypeInfo.get("name").getAsString());
        body.addProperty("expenseTypeOID",expenseTypeInfo.get("expenseTypeOID").getAsString());
        body.addProperty("expenseTypeId",expenseTypeInfo.get("id").getAsString());
        body.addProperty("expenseTypeIconName",expenseTypeInfo.get("iconName").getAsString());
        body.addProperty("expenseTypeKey",messageKey);
        body.addProperty("pasteInvoiceNeeded",expenseTypeInfo.get("pasteInvoiceNeeded").getAsBoolean());
        body.addProperty("invoiceRequired",expenseTypeInfo.get("invoiceRequired").getAsBoolean());
        body.addProperty("amount",amount);
        //如果非人民币的汇率要加上汇率 使用equals会出现空指针,因为初始化的时候没有赋值。
        if(!body.get("currencyCode").getAsString().equals("CNY")){
            body.addProperty("actualCurrencyRate",component.getRate());
        }
        body.add("receiptList",receiptList);
        body.addProperty("createdDate",component.getCreatedDate());
        if(component.getCurrencyCode()==null){
            body.addProperty("invoiceCurrencyCode","CNY");
        }else {
            body.addProperty("invoiceCurrencyCode",component.getCurrencyCode());
        }
        body.addProperty("invoiceInstead",component.isInvoiceInstead());
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
        body.add("expenseAmortise",new JsonArray());
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
     * 根据inviceOID获取到费用的详情
     * @param employee
     * @param invoiceOID
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getInvocieDetail(Employee employee,String invoiceOID) throws HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ String.format(ApiPath.INVOICE_DETAIL,invoiceOID);
        String response = doGet(url,getHeader(employee.getAccessToken()),null,employee);
        return new JsonParser().parse(response).getAsJsonObject();
    }

    /**
     * 获得系统当前费用类型可以选择的控件信息列表
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getExpenseWidgetsList(Employee employee) throws HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.EXPENSE_WIDGETS_LIST;
        Map<String,String> UrlParam=new HashMap<>();
        UrlParam.put("roleType","TENANT");
        String res= doGet(url,getHeader(employee.getAccessToken()),UrlParam,employee);
        return new JsonParser().parse(res).getAsJsonObject();
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
        String res = doPost(url,getHeader(employee.getAccessToken()),null,object.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     *分摊费用类型带出的默认分摊行  新建分摊行的话必须修改字段defaultApportion：false
     * 一般第一个分摊行为默认的分摊行并且只有一个  后续新建的分摊行如果不改的话 会导分摊
     * 行出现俩个默认的分摊行。
     * @param employee
     * @param currency
     * @param expenseReportOID
     * @param expenseTypeId
     * @return
     * @throws HttpStatusException
     */
    public JsonObject defaultApportionment(Employee employee,String currency,String expenseReportOID,String expenseTypeId) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.DEFAULT_APPORTIONEMNT;
        Map<String,String> urlParam=new HashMap<>();
        urlParam.put("expenseReportOID",expenseReportOID);
        urlParam.put("expenseTypeId",expenseTypeId);
        urlParam.put("amount","100");
        urlParam.put("currency",currency);
        urlParam.put("ownerOID",employee.getUserOID());
        urlParam.put("merge","true");
        String res= doGet(url,getHeader(employee.getAccessToken()),urlParam,employee);
        return new JsonParser().parse(res).getAsJsonArray().get(0).getAsJsonObject();
    }

    /**
     * 获取费用上的参与人信息
     * @param employee
     * @param expenseReportOID
     * @param keyWord
     */
    public JsonObject getExpenseParticipant(Employee employee,String expenseReportOID,String keyWord) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.EXPENSE_PARTICIPANT;
        Map<String,String> urlParam=new HashMap<>();
        urlParam.put("expenseReportOId",expenseReportOID);
        urlParam.put("page","0");
        urlParam.put("size","10");
        urlParam.put("keyword",keyWord);
        urlParam.put("keywordLable",keyWord);
        urlParam.put("ownerOId",employee.getUserOID());
        urlParam.put("selectRange","3");
        String res = doGet(url,getHeader(employee.getAccessToken()),urlParam,employee);
        return new JsonParser().parse(res).getAsJsonArray().get(0).getAsJsonObject();
    }

    /**
     * 组装
     * @param employee
     * @param expenseReportOID
     * @param keyWords
     * @return
     * @throws HttpStatusException
     */
    public JsonObject setParticipant(Employee employee,String expenseReportOID,String keyWords) throws HttpStatusException {
        JsonObject participants = getExpenseParticipant(employee,expenseReportOID,keyWords);
        JsonObject participant = new JsonObject();
        participant.addProperty("userOID",participants.get("userOID").getAsString());
        participant.addProperty("fullName",participants.get("fullName").getAsString());
        participant.addProperty("participantOID",participants.get("userOID").getAsString());
        participant.addProperty("highOff", (String) null);
        participant.addProperty("avatar",(String) null);
        return participant;
    }


    /**
     * 发票查验
     * @param employee
     * @param receptionInfo  发票信息的json字符
     */
    public JsonObject receiptVerify(Employee employee,String receptionInfo) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.RECEIPT_VERIFY;
        JsonObject receptInfo = new JsonParser().parse(receptionInfo).getAsJsonObject();
        JsonObject body = new JsonObject();
        body.add("invoiceInfo",receptInfo);
        body.addProperty("userOID",employee.getUserOID());
        body.addProperty("entityType",(String)null);
        body.addProperty("finance",false);
        String res = doPost(url,getHeader(employee.getAccessToken()),null,body.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 发票附件上传上传附件
     * @param employee
     * @param path
     * @return
     * @throws HttpStatusException
     */
    public JsonObject uploadAttachment(Employee employee,String path) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.UPLOAD_ATTACHMENT;
        HashMap<String,String> parm = new HashMap<>();
        parm.put("attachmentType","PDF");
        String res = doupload(url,getHeader(employee.getAccessToken()),parm,"file",path,"application/pdf",employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 发票识别ocr
     * @param employee
     * @param attachment
     * @return
     * @throws HttpStatusException
     */
    public JsonObject ocr(Employee employee,JsonArray attachment) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.OCR;
        String res = doPost(url,getHeader(employee.getAccessToken()),null,attachment.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 上传文件批量识别
     * @param employee
     * @param receiptInfo 发票信息
     * @return
     * @throws HttpStatusException
     */
    public JsonArray batchVerify(Employee employee,JsonObject receiptInfo) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.BATCH_VERIFY;
        JsonArray array = new JsonArray();
        JsonObject body = new JsonObject();
        body.add("invoiceInfo",receiptInfo);
        body.addProperty("finance",false);
        body.addProperty("userOID",employee.getUserOID());
        array.add(body);
        String res = doPost(url,getHeader(employee.getAccessToken()),null,array.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * ofd 发票识别
     * @param employee
     * @param attachment
     * @return
     */
    public JsonObject ofdReceiptOCR(Employee employee, JsonArray attachment) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.OFD_RECEIPT_OCR;
        String res = doPost(url,getHeader(employee.getAccessToken()),null,attachment.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 推送滴滴费用
     * @param employee
     * @param didi
     * @throws HttpStatusException
     */
    public void pushDiDi(Employee employee, DiDi didi) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.PUSH_DIDI;
        JsonArray array = new JsonArray();
        array.add(new JsonParser().parse(GsonUtil.objectToString(didi)).getAsJsonObject());
        doPost(url,getHeader(employee.getAccessToken()),null,array.toString(),null,employee);
    }
}
package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.ExpenseApi;
import com.hand.api.FinanceApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.component.FormDetail;
import com.hand.basicObject.component.InvoiceComponent;
import com.hand.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @Author peng.zhang
 * @Date 2020/6/15
 * @Version 1.0
 **/
@Slf4j
public class ExpenseReportInvoice {


    private ExpenseApi expenseApi;
    private ExpenseReportComponent expenseReportComponent;

    public ExpenseReportInvoice(){
        expenseApi =new ExpenseApi();
        expenseReportComponent =new ExpenseReportComponent();
    }

    /**
     * 根据费用名称获取费用的expenseID和expenseTypeOID
     * @param employee
     * @param expenseTypenName
     * @param expenseReportOID
     * @throws HttpStatusException
     */
    public HashMap<String, String> getExpenseReportExpenseTypes(Employee employee, String expenseTypenName, String expenseReportOID) throws HttpStatusException {
       JsonArray jsonArray=expenseApi.getExpenseReportExpenseTypes(employee,expenseReportOID).get("rows").getAsJsonArray();
       HashMap<String,String> map =new HashMap<>();
       for(int i =0;i<jsonArray.size();i++){
           if(jsonArray.get(i).getAsJsonObject().get("name").getAsString().equalsIgnoreCase(expenseTypenName)){
               map.put("expenseTypeId",jsonArray.get(i).getAsJsonObject().get("id").getAsString());
               map.put("expenseTypeOID",jsonArray.get(i).getAsJsonObject().get("expenseTypeOID").getAsString());
           }
       }
       return map;
    }

    /**
     * 获取费用控件信息
     * @param employee
     * @param expenseTypenName
     * @param expenseReportOID
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getExpenseTypeInfo(Employee employee,String expenseTypenName,String expenseReportOID) throws HttpStatusException {
        return expenseApi.getExpenseTypeInfo(employee,getExpenseReportExpenseTypes(employee,expenseTypenName,expenseReportOID).get("expenseTypeId"));
    }

    /**
     * 创建一笔费用，
     * @param employee
     * @param expenseTypeName  费用类型的名称
     * @param expenseReportOID   报销单的OID
     * @param amount   金额
     * @return
     */
    public HashMap<String,String> createExpenseInvoice(Employee employee,String expenseTypeName,String expenseReportOID,double amount){
        JsonObject jsonObject=null;
        try {
           jsonObject= expenseApi.expenseReportCreateinvoice(employee,"",getExpenseTypeInfo(employee,expenseTypeName,expenseReportOID),
                    expenseReportOID,"","",amount,new JsonArray(),new JsonObject(),new JsonObject(),new JsonArray(),
                    false,new JsonArray(),expenseReportComponent.getExpenseLocation(employee,"西安环普科技产业园E座"),new JsonObject());
        } catch (HttpStatusException e) {
            e.printStackTrace();
        }
        HashMap<String,String> info =new HashMap<>();
        info.put("invoiceOID",jsonObject.get("rows").getAsJsonObject().get("invoiceOID").getAsString());
        return info;
    }

    /**
     * 创建一笔费用，
     * @param employee
     * @param expenseTypenName  费用类型的名称
     * @param expenseReportOID   报销单的OID
     * @param amount   金额
     * @return
     */
    public HashMap<String,String> createExpenseInvoice(Employee employee, InvoiceComponent component, String expenseTypenName, String expenseReportOID, double amount,JsonArray expenseApportion){
        JsonObject jsonObject=null;
        try {
            jsonObject= expenseApi.expenseReportCreateinvoice(employee,getExpenseTypeInfo(employee,expenseTypenName,expenseReportOID),component,
                    expenseReportOID,amount,new JsonArray(),new JsonArray(),expenseApportion);
        } catch (HttpStatusException e) {
            e.printStackTrace();
        }
        HashMap<String,String> info =new HashMap<>();
        info.put("invoiceOID",jsonObject.get("rows").getAsJsonObject().get("invoiceOID").getAsString());
        return info;
    }

    /**
     * 创建一笔费用，费用中添加发票的费用
     * @param employee
     * @param expenseTypenName  费用类型的名称
     * @param expenseReportOID   报销单的OID
     * @param amount   金额
     * @return
     */
    public FormDetail createExpenseInvoice(Employee employee, InvoiceComponent component, String expenseTypenName, String expenseReportOID, double amount,JsonObject receipt){
        JsonObject jsonObject=null;
        JsonArray receiptList = new JsonArray();
        receiptList.add(receipt);
        try {
            jsonObject= expenseApi.expenseReportCreateinvoice(employee,getExpenseTypeInfo(employee,expenseTypenName,expenseReportOID),component,
                    expenseReportOID,amount,new JsonArray(),receiptList,new JsonArray());
        } catch (HttpStatusException e) {
            e.printStackTrace();
        }
        FormDetail formDetail = new FormDetail();
        log.info("费用的响应为：");
        formDetail.setInvoiceOID(jsonObject.get("rows").getAsJsonObject().get("invoiceOID").getAsString());
        return formDetail;
    }

    /**
     * 获取报销单内的费用详情的list
     * @param employee
     * @param expenseReportOID
     * @return
     */
    public JsonArray getInvoiceDetail(Employee employee,String expenseReportOID) throws HttpStatusException {
        return expenseApi.getInvoiceDetail(employee,expenseReportOID).getAsJsonObject("rows").getAsJsonArray("expenseReportInvoices");
    }

    /**
     * 获取账本中所有费用的invoiceOID
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public ArrayList getExpenseItem(Employee employee) throws HttpStatusException {
        ArrayList<String> expenseItem =new ArrayList<>();
        JsonArray expenseList=expenseApi.getInvoiceLlist(employee);
        //检查账本是否为空
        if(GsonUtil.isNotEmpt(expenseList)){
            for (int i=0;i<expenseList.size();i++){
                expenseItem.add(expenseList.get(i).getAsJsonObject().get("invoiceOID").getAsString());
            }
        }else{
            log.info("账本暂无费用");
        }
        return expenseItem;
    }

    /**
     * 根据费用的invoiceOID 获取费用详情
     * @param employee
     * @param invoiceOID
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getInvoice(Employee employee,String invoiceOID) throws HttpStatusException {
        return expenseApi.getInvocieDetail(employee,invoiceOID);
    }


    /**
     * 删除费用
     * @param employee
     * @param invoiceOID
     * @throws HttpStatusException
     */
    public void deleteInvoice(Employee employee, String invoiceOID) throws HttpStatusException {
        expenseApi.invoiceDelete(employee,invoiceOID);
    }

    /**
     * 搜索被转交的用户
     * @param employee
     * @param fullName
     * @param setOfBooksId
     * @return
     * @throws HttpStatusException
     */
    public String searchTransferUser(Employee employee,String fullName, String setOfBooksId) throws HttpStatusException {
       JsonArray jsonArray= expenseApi.searchTransferUser(employee,setOfBooksId);

       String userId =GsonUtil.getJsonValue(jsonArray,"fullName",fullName,"id");
       return userId;
    }

    /**
     * 费用转交
     * @param employee
     * @param fullName
     * @param invoiceOIDs
     * @throws HttpStatusException
     */
    public String  transferTo(Employee employee, String fullName,JsonArray invoiceOIDs) throws HttpStatusException {
        JsonObject object=expenseApi.transferTo(employee,searchTransferUser(employee,fullName,employee.getSetOfBookId()),invoiceOIDs);
        return object.get("success").getAsString();
    }

    /**
     * 分摊行数据  支持按照部门或者按照成本中心分摊
     * @param employee
     * @param isDefaultApportion 是否是默认的分摊行
     * @param expenseTypeName   费用名称
     * @param proportion   分摊比例   为小数比如0.12
     * @param amount   分摊金额
     * @param currency  币种
     * @param expenseReportOID   报销单的OID
     * @param costCenterItemOID   部门OID 或者成本中心的OID
     * @param costCenterItemName  部门名称  或者成本中心的名称
     * @param itemCode   部门编码或者成本中心的编码
     * @return
     * @throws HttpStatusException
     */
    public JsonObject apportionLine(Employee employee,boolean isDefaultApportion, String expenseTypeName,double proportion, double amount,
                                    String currency,String expenseReportOID,String costCenterItemOID, String costCenterItemName,
                                    String itemCode) throws HttpStatusException {
        JsonObject apportionment = expenseApi.defaultApportionment(employee,currency,expenseReportOID,getExpenseReportExpenseTypes(employee,expenseTypeName,expenseReportOID).get("expenseTypeId"));
        log.info("分摊项:{}",apportionment);
        JsonArray costCenterItems = apportionment .get("costCenterItems").getAsJsonArray();
        apportionment.addProperty("amount",amount);
        apportionment.addProperty("proportion",proportion);
        apportionment.addProperty("defaultApportion",isDefaultApportion);
        //这块逻辑是：表头有部门信息或者成本中心 并且是默认分摊行的话 则不需要进行分摊项的数据输入
        if(!isDefaultApportion && costCenterItems.get(0).getAsJsonObject().get("costCenterItemOID").isJsonNull()){
            costCenterItems.get(0).getAsJsonObject().addProperty("costCenterItemOID",costCenterItemOID);
            costCenterItems.get(0).getAsJsonObject().addProperty("costCenterItemName",costCenterItemName);
            costCenterItems.get(0).getAsJsonObject().addProperty("itemCode",itemCode);
            costCenterItems.get(0).getAsJsonObject().addProperty("departmentOid",costCenterItemOID);
            costCenterItems.get(0).getAsJsonObject().addProperty("name",costCenterItemName);
        }
        apportionment.add("costCenterItems",costCenterItems);
        return apportionment;
    }

    /**
     * 设置总的分摊行
     * @param expenseApportion  一个可变参数 需要传一个分摊行的JsonObject
     * @return
     */
    public JsonArray createrExpenseApporation(JsonObject ...expenseApportion){
        JsonArray expenseApportions =new JsonArray();
        for(int i =0 ;i<expenseApportion.length;i++){
            expenseApportions.add(expenseApportion[i]);
        }
        return expenseApportions;
    }

    /**
     * 费用内的标签信息
     * @param employee
     * @param invoiceOID
     * @param type 标签类型 超费用标准：EXPENSE_STANDARD_EXCEEDED_WARN；校验警告：REPORT_SUBMIT_WARN
     * @param expectValue
     * @return
     */
    public boolean checkInvoiceLabel(Employee employee,String invoiceOID,String type,String expectValue) throws HttpStatusException {
        JsonObject result = getInvoice(employee,invoiceOID);
        JsonArray invoiceLabel = result.get("invoiceLabels").getAsJsonArray();
        if(GsonUtil.isNotEmpt(invoiceLabel)){
            try{
                String toast = GsonUtil.getJsonValue(invoiceLabel,"type",type).get("toast").getAsString();
                log.info("费用内的标签:{}",toast);
                if(toast.equals(expectValue)){
                    return true;
                }else{
                    return false;
                }
            }catch (NullPointerException e){
                throw new RuntimeException(String.format("费用中无此%s类型的标签",type));
            }
        }else{
            return false;
        }
    }

    /**
     * 费用内的标签信息
     * @param employee
     * @param invoiceOID
     * @param type 标签类型 超费用标准：EXPENSE_STANDARD_EXCEEDED_WARN；校验警告：REPORT_SUBMIT_WARN
     * @return
     */
    public String checkInvoiceLabel(Employee employee,String invoiceOID,String type) throws HttpStatusException {
        JsonObject result = getInvoice(employee,invoiceOID);
        JsonArray invoiceLabel = result.get("invoiceLabels").getAsJsonArray();
        if(GsonUtil.isNotEmpt(invoiceLabel)) {
            try {
                String toast = GsonUtil.getJsonValue(invoiceLabel, "type", type).get("toast").getAsString();
                log.info("费用内的标签:{}", toast);
                return toast;
            }catch (NullPointerException e){
                throw new RuntimeException(String.format("费用中无此%s类型的标签",type));
            }
        }else{
            throw new RuntimeException("未存在标签");
        }
    }

    /**
     * 费用内的标签信息
     * @param employee
     * @param invoiceOID
     * @param type 标签类型 超费用标准：EXPENSE_STANDARD_EXCEEDED_WARN；校验警告：REPORT_SUBMIT_WARN
     * @return
     */
    public String checkInvoiceLabelName(Employee employee,String invoiceOID,String type) throws HttpStatusException {
        JsonObject result = getInvoice(employee,invoiceOID);
        JsonArray invoiceLabel = result.get("invoiceLabels").getAsJsonArray();
        log.info("标签内容为:{}",invoiceLabel);
        if(GsonUtil.isNotEmpt(invoiceLabel)) {
            try {
                String toast = GsonUtil.getJsonValue(invoiceLabel, "type", type).get("name").getAsString();
                log.info("费用内的标签名称:{}", toast);
                return toast;
            }catch (NullPointerException e){
                throw new RuntimeException(String.format("费用中无此%s类型的标签",type));
            }
        }else{
            throw new RuntimeException("未存在标签");
        }
    }

    /**
     * 发票查验发票标签断言  可以选择断言标题还是断言descrition
     * @param employee
     * @param filePath
     * @param code
     * @return
     * @throws HttpStatusException
     */
    public String checkVerifyReceipt(Employee employee,String filePath, String code,boolean isDescription) throws HttpStatusException {
        JsonObject verifyInfo = getOCRReceiptVerifyInfo(employee,filePath).getAsJsonObject("errorList");
        String label ="";
        try{
            JsonArray errorList = verifyInfo.get("errorList").getAsJsonArray();
            if(isDescription){
                if(GsonUtil.isNotEmpt(errorList)){
                    label = GsonUtil.getJsonValue(errorList,"code",code,"message");
                }
            }else {
                if (GsonUtil.isNotEmpt(errorList)){
                    label =  GsonUtil.getJsonValue(errorList, "code", code, "title");
                }
            }
        }catch (NullPointerException e){
            throw new RuntimeException("发票查验错误,标签errorList不存在");
        }
        return label;
    }

    /**
     * 检查发票发票查验是否成功
     * @param employee
     * @param receptInfo
     * @return
     * @throws HttpStatusException
     */
    public String receptVerify(Employee employee,String receptInfo) throws HttpStatusException {
        return expenseApi.receiptVerify(employee,receptInfo).get("msg").getAsString();
    }

    /**
     * 获取发票查验
     * @param employee
     * @param receptInfo
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getReceptVerify(Employee employee,String receptInfo) throws HttpStatusException {
        return expenseApi.receiptVerify(employee,receptInfo).getAsJsonObject("invoiceInfo");
    }

    /**
     * 获取发票查验 所有
     * @param employee
     * @param receptInfo
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getReceptVerifyInfo(Employee employee,String receptInfo) throws HttpStatusException {
        return expenseApi.receiptVerify(employee,receptInfo);
    }


    /**
     * ocr识别发票-上传pdf 方式 并查验
     * @param employee
     * @param filePath
     * @return
     * @throws HttpStatusException
     */
    public String ocrReceptVerify(Employee employee,String filePath) throws HttpStatusException {
        JsonObject attachment = expenseApi.uploadAttachment(employee,filePath);
        JsonArray ocrArray = new JsonArray();
        ocrArray.add(attachment);
        JsonObject receiptInfo = expenseApi.ocr(employee,ocrArray).getAsJsonObject("rows").getAsJsonArray("receiptList").get(0).getAsJsonObject();
        //发票查验
        return expenseApi.batchVerify(employee,receiptInfo).get(0).getAsJsonObject().get("msg").getAsString();
    }

    /**
     * ocr识别发票-上传pdf 方式 并查验
     * @param employee
     * @param filePath
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getOCRReceiptVerifyInfo(Employee employee, String filePath) throws HttpStatusException {
        JsonObject attachment = expenseApi.uploadAttachment(employee,filePath);
        JsonArray ocrArray = new JsonArray();
        ocrArray.add(attachment);
        JsonObject receiptInfo = expenseApi.ocr(employee,ocrArray).getAsJsonObject("rows").getAsJsonArray("receiptList").get(0).getAsJsonObject();
        //发票查验
        return expenseApi.batchVerify(employee,receiptInfo).get(0).getAsJsonObject();
    }

    /**
     * ocr识别发票-上传pdf 方式
     * @param employee
     * @param filePath
     * @return
     * @throws HttpStatusException
     */
    public boolean ocr(Employee employee,String filePath) throws HttpStatusException {
        JsonObject attachment = expenseApi.uploadAttachment(employee,filePath);
        JsonArray ocrArray = new JsonArray();
        ocrArray.add(attachment);
        return expenseApi.ocr(employee,ocrArray).get("success").getAsBoolean();
    }

    /**
     * 财务卷票机 scan  发票识别
     * @param employee
     * @param filePath
     * @param reportId
     * @return
     * @throws HttpStatusException
     */
    public String scanOcr(Employee employee,String filePath,String reportId) throws HttpStatusException {
        FinanceApi financeApi = new FinanceApi();
        JsonObject attachment = expenseApi.uploadAttachment(employee,filePath);
        JsonObject ocr = financeApi.scanOcr(employee,reportId,attachment);
        return ocr.get("message").getAsString();
    }

    /**
     * ofd 格式的发票识别并查验
     * @param employee
     * @return
     */
    public String ofd(Employee employee,String filePath) throws HttpStatusException {
        JsonObject attachment = expenseApi.uploadAttachment(employee,filePath);
        JsonArray ocrArray = new JsonArray();
        ocrArray.add(attachment);
        JsonObject ofdInfo = expenseApi.ofdReceiptOCR(employee,ocrArray).getAsJsonObject("rows").getAsJsonArray("receiptList").get(0).getAsJsonObject();
        return expenseApi.batchVerify(employee,ofdInfo).get(0).getAsJsonObject().get("msg").getAsString();
    }

}

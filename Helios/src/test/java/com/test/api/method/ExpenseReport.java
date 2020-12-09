package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.ReimbursementApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.utils.GsonUtil;
import com.hand.utils.UTCTime;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2020/6/11
 * @Version 1.0
 **/
@Slf4j
public class ExpenseReport {

    private ReimbursementApi reimbursementApi;
    private ExpenseReportComponent expenseReportComponent;
    private ExpenseReportInvoice expenseReportInvoice;

    public ExpenseReport(){
        reimbursementApi =new ReimbursementApi();
        expenseReportComponent =new ExpenseReportComponent();
        expenseReportInvoice =new ExpenseReportInvoice();
    }

    /**
     * 根据表单名称返回表单的formOID
     * @param employee
     * @param formName
     * @return
     */
    public String getFormOID(Employee employee, String formName) throws HttpStatusException {
        JsonArray jsonArray=null;
        String formOID="";
        jsonArray=reimbursementApi.getAvailableforms(employee,"102",employee.getJobId());
        if(GsonUtil.isNotEmpt(jsonArray)){
            formOID=GsonUtil.getJsonValue(jsonArray,"formName",formName,"formOID");
        }
        //如果formOID 为空的话 则去申请单查找下是否存在该表单
        if(formOID.equals("")){
            jsonArray=reimbursementApi.getAvailableforms(employee,"101",employee.getJobId());
            formOID = GsonUtil.getJsonValue(jsonArray,"formName",formName,"formOID");
        }
        return formOID;
    }

    /**
     * 获取表单控件的默认值 ：例如 部门   公司  参与人  成本中心等
     * @param employee
     * @param formOID   表单的formOID
     * @param fieldName  控件的name
     * @return
     * @throws HttpStatusException
     */
    public String getdefaultValue(Employee employee, String formOID,String fieldName) throws HttpStatusException {
       JsonArray jsonArray = reimbursementApi.getFormDefaultValues(employee,formOID,employee.getJobId());
       String value="";
       for(int i =0; i<jsonArray.size(); i++){
           if(jsonArray.get(i).getAsJsonObject().get("fieldName").getAsString().equalsIgnoreCase(fieldName)){
               value =jsonArray.get(i).getAsJsonObject().get("value").getAsString();
           }
       }
       return value;
    }


    /**
     * 参与人是自己
     * @param employee
     * @return
     */
    public String participant(Employee employee){
        JsonArray jsonArray =new JsonArray();
        JsonObject jsonObject =new JsonObject();
        jsonObject.addProperty("userOID",employee.getPhoneNumber());
        jsonObject.addProperty("fullName",employee.getFullName());
        jsonObject.addProperty("participantOID",employee.getUserOID());
        jsonObject.addProperty("avatar", (String) null);
        jsonArray.add(jsonObject);
        return jsonArray.toString();
    }


//    /**
//     * 获取表单控件的详情  返回customFormFields
//     * @param employee
//     */
//    public JsonArray getFormDetail(Employee employee, String formName){
//        JsonObject jsonObject =null;
//        try {
//           jsonObject = reimbursementApi.getFormDetail(employee,getFormOID(employee,formName));
//        } catch (HttpStatusException e) {
//            e.printStackTrace();
//        }
//        return jsonObject.get("customFormFields").getAsJsonArray();
//    }


    /**
     * 创建报销单，根据自己表单的控件传参数，如果表单没有的参数的话  就随便传一个获取根据自己的需要重载方法就行了。
     * @param employee
     * @param formName   表单名称
     * @param city   如果存在城市控件则传一个城市名称，没有城市控件的话就传一个空的字符串。
     * @throws HttpStatusException
     */
    public HashMap<String,String> createExpenseReport(Employee employee, String formName, String departmentOID,String city) throws HttpStatusException {
        JsonObject jsonObject =reimbursementApi.createExpenseReport(employee,reimbursementApi.getFormDetail(employee,getFormOID(employee,formName)),departmentOID,
                0, UTCTime.getNowUtcTime(),UTCTime.getUtcTime(2,0),employee.getCompanyOID(),expenseReportComponent.getCityCode(employee,city),participant(employee),new JsonArray(),new JsonArray(),
                employee.getJobId(),employee.getUserOID());
        HashMap<String,String> info =new HashMap<>();
        info.put("expenseReportOID",jsonObject.get("expenseReportOID").getAsString());
        info.put("businessCode",jsonObject.get("businessCode").getAsString());
        log.info("businessCode:{}",info.get("businessCode"));
        return info;
    }

    /**
     * 使用控件对象新建报销单
     * @param employee
     * @param formName
     * @param component
     * @return
     * @throws HttpStatusException
     */
    public HashMap<String,String> createExpenseReport (Employee employee, String formName, FormComponent component) throws HttpStatusException {
        JsonObject formDetail = reimbursementApi.getFormDetail(employee,getFormOID(employee,formName));
        JsonObject jsonObject =reimbursementApi.createExpenseReport(employee,formDetail,component,employee.getJobId(),employee.getUserOID());
        HashMap<String,String> info =new HashMap<>();
        info.put("expenseReportOID",jsonObject.get("expenseReportOID").getAsString());
        info.put("businessCode",jsonObject.get("businessCode").getAsString());
        log.info("businessCode:{}",info.get("businessCode"));
        return info;
    }

    /**
     * 造数据---新建报销单
     * @param employee
     * @param formName
     * @return
     * @throws HttpStatusException
     */
    public HashMap<String,String> createExpenseReport (Employee employee, String formName) throws HttpStatusException {
        JsonObject jsonObject =reimbursementApi.createExpenseReport(employee,reimbursementApi.getFormDetail(employee,getFormOID(employee,formName)),employee.getJobId(),employee.getUserOID());
        HashMap<String,String> info =new HashMap<>();
        info.put("expenseReportOID",jsonObject.get("expenseReportOID").getAsString());
        info.put("businessCode",jsonObject.get("businessCode").getAsString());
        log.info("businessCode:{}",info.get("businessCode"));
        return info;
    }

    /**
     * 获取报销单的详情
     * @param employee
     * @param expenseReportOID
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getExpenseReportDetail(Employee employee,String expenseReportOID) throws HttpStatusException {
        return reimbursementApi.getExpenseReportDetail(employee,expenseReportOID).get("rows").getAsJsonObject();
    }

    /**
     * 用于报销单提交检查：费用标准，报销标,各种标签校验
     * @param employee
     * @param expenseReportOID
     * @throws HttpStatusException
     */
    public JsonObject expenseReportSubmitCheck(Employee employee,String expenseReportOID) throws HttpStatusException {
        reimbursementApi.validate(employee,expenseReportOID);
        return reimbursementApi.expenseReportSubmit(employee,getExpenseReportDetail(employee,expenseReportOID),false,"",expenseReportInvoice.
                getInvoiceDetail(employee,expenseReportOID)).getAsJsonObject();
    }

    /**
     * 报销单提交检查如果有提交检查后续会强制提交
     * @param employee
     * @param expenseReportOID
     * @throws HttpStatusException
     */
    public String expenseReportSubmit(Employee employee,String expenseReportOID) throws HttpStatusException {
        String submitSuccess="";
        reimbursementApi.validate(employee,expenseReportOID);
        if(reimbursementApi.expenseReportSubmit(employee,getExpenseReportDetail(employee,expenseReportOID),false,"",expenseReportInvoice.
                getInvoiceDetail(employee,expenseReportOID)).get("submitSuccess").getAsString().equalsIgnoreCase("false")){
           submitSuccess = reimbursementApi.enforceExpenseReportSubmit(employee,getExpenseReportDetail(employee,expenseReportOID),false,"",expenseReportInvoice.
                    getInvoiceDetail(employee,expenseReportOID)).get("submitSuccess").getAsString();
        }
        return submitSuccess;
    }

//    public JsonObject expenseReportValidate(Employee employee,String expenseReportOID) throws HttpStatusException {
//        reimbursementApi.validate(employee,expenseReportOID);
//    }

    /**
     *  根据费用类型和费用的归属人以及导入几笔费用
     * @param employee
     * @param expenseReportOID
     * @param expenseTypeName   费用名称
     * @param reimbursementUserName    报销单的归属人
     * @param number   需要导入的费用的数量
     * @throws HttpStatusException
     */
    public ArrayList<String> getImportInvoice(Employee employee, String expenseReportOID, String expenseTypeName, String reimbursementUserName, int number,boolean isbook) throws HttpStatusException {
        JsonArray jsonArray=reimbursementApi.searchAvailableImport(employee,expenseReportOID,isbook);
        ArrayList<String> invoices =new ArrayList<>();
        ArrayList<String> invoicesOID =new ArrayList<>();
        //先根据费用名称和费用归属人拿到这些全部的费用
        for(int i=0;i<jsonArray.size();i++){
            if(jsonArray.get(i).getAsJsonObject().get("expenseTypeName").getAsString().equals(expenseTypeName) && jsonArray.get(i).getAsJsonObject().get("reimbursementUserName").getAsString().equals(reimbursementUserName)){
                invoices.add(jsonArray.get(i).getAsJsonObject().get("invoiceOID").getAsString());
            }
        }
        //然后根据你想导入的费用的数量进行导入
        for(int j =0;j<number;j++){
            invoicesOID.add(invoices.get(j));
        }
        return  invoicesOID;
    }

    /**
     * 不区分费用类型和费用的归属人导入费用
     * @param employee
     * @param expenseReportOID
     * @param number   需要导入的费用的数量
     * @return
     * @throws HttpStatusException
     */
    public ArrayList<String> getImportInvoice(Employee employee, String expenseReportOID,int number,boolean isbook) throws HttpStatusException {
        JsonArray jsonArray=reimbursementApi.searchAvailableImport(employee,expenseReportOID,isbook);
        ArrayList<String> invoicesOID =new ArrayList<>();
        log.info("账本的费用：{}",jsonArray);
        //然后根据你想导入的费用的数量进行导入
        for(int j =0;j<number;j++){
            invoicesOID.add(jsonArray.get(j).getAsJsonObject().get("invoiceOID").getAsString());
        }
        return  invoicesOID;
    }

    /**
     * 报销单导入费用  根据费用类型和费用的归属人以及导入几笔费用
     * @param employee
     * @param expenseReportOID
     * @param expenseTypeName   费用名称
     * @param reimbursementUserName    报销单的归属人
     * @param number   需要导入的费用的数量
     * @param isbook   是否为账本费用   false 为结算费用
     * @throws HttpStatusException
     */
    public String importInvoice(Employee employee, String expenseReportOID,String expenseTypeName, String reimbursementUserName, int number, boolean isbook) throws HttpStatusException {
        return reimbursementApi.importInvoices(employee,expenseReportOID,getImportInvoice(employee,expenseReportOID,expenseTypeName,reimbursementUserName,number,isbook),isbook);
    }

    /**
     * 报销单导入费用  根据费用类型和费用的归属人以及导入几笔费用
     * @param employee
     * @param expenseReportOID
     * @param number   需要导入的费用的数量
     * @throws HttpStatusException
     */
    public String importInvoice(Employee employee, String expenseReportOID,int number,boolean isbook) throws HttpStatusException {
        return reimbursementApi.importInvoices(employee,expenseReportOID,getImportInvoice(employee,expenseReportOID,number,isbook),isbook);
    }

    /**
     * 查看报销单中费用的标签
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public  String  invoiceLabel(Employee employee, String expenseReportOID) throws HttpStatusException {
        //查看费用详情  (报销单中可能有多个费用，并且每个费用可能有多个标签)
        ArrayList<String> labelList = new ArrayList<>();
        for(int i=0;i<expenseReportInvoice.getInvoiceDetail(employee,expenseReportOID).size();i++){
            JsonArray labelArray = expenseReportInvoice.getInvoiceDetail(employee,expenseReportOID).get(i).getAsJsonObject().get("invoiceView").getAsJsonObject().get("invoiceLabels").getAsJsonArray();
            //处理没有标签的情况。如果label里面是空的就跳出循环。
            if(labelArray.size()==0){
                labelList.add("无标签");
                break;
            }else{
                for(int j=0; j<labelArray.size();j++){
                    labelList.add(labelArray.get(j).getAsJsonObject().get("toast").getAsString());
                }
            }
        }
        log.info("expenseLabel:{}",labelList);
        return  labelList.toString();
    }

    /**
     * 报销单删除
     * @param employee
     * @param expenseReportOID
     * @throws HttpStatusException
     */
    public void deleteExpenseReport(Employee employee,String expenseReportOID) throws HttpStatusException {
        reimbursementApi.expenseReportDelete(employee,expenseReportOID);
    }

    /**
     * 报销单撤回
     * @param employee
     * @param expenseReportOID
     * @throws HttpStatusException
     */
    public void withdraw(Employee employee,String expenseReportOID) throws HttpStatusException {
        reimbursementApi.expenseReportWithdraw(employee,expenseReportOID);
    }

    /**
     * 报销单内删除费用
     * @param employee
     * @param expenseReportOID
     * @param invoiceOID
     * @throws HttpStatusException
     */
    public void removeInvoice(Employee employee,String expenseReportOID,String invoiceOID) throws HttpStatusException {
        reimbursementApi.removeExpense(employee,expenseReportOID,invoiceOID);
    }

    /**
     * 差旅报销单获取关联的差旅申请单
     * @param employee
     * @param formName
     * @return
     * @throws HttpStatusException
     */
    public ArrayList<String> getApplication(Employee employee,String formName) throws HttpStatusException {
        JsonArray array = reimbursementApi.getApplication(employee,getFormOID(employee,formName));
        ArrayList<String> list =new ArrayList<>();
        for(int i =0;i<array.size();i++){
            list.add(array.get(i).getAsJsonObject().get("applicationOID").getAsString());
        }
        return list;
    }

    /**
     * 新建差旅报销单
     * @param employee
     * @param isMoreApplication
     * @param formName
     * @param component    如果控件数据是从申请单带出的  则初始化的时候使用带出的数据
     * @return
     * @throws HttpStatusException
     */
    public HashMap<String, String> createTravelExpenseReport(Employee employee, boolean isMoreApplication, String formName, FormComponent component) throws HttpStatusException {
        JsonObject jsonObject = reimbursementApi.createTravelExpenseReport(employee,isMoreApplication,reimbursementApi.getFormDetail(employee,getFormOID(employee,formName)),component);
        HashMap<String,String> info =new HashMap<>();
        info.put("expenseReportOID",jsonObject.get("expenseReportOID").getAsString());
        info.put("businessCode",jsonObject.get("businessCode").getAsString());
        log.info("[businessCode]:{}",info.get("businessCode"));
        return info;
    }

    /**
     * 新建差旅报销单 ----- 控件数据全部由申请单带出的
     * @param employee
     * @param customFormValues 申请单带出的控件信息
     * @param formName
     * @return
     * @throws HttpStatusException
     */
    public HashMap<String, String> createTravelExpenseReport(Employee employee, String formName, String applicationOID,JsonArray customFormValues) throws HttpStatusException {
        JsonObject reportdetail = reimbursementApi.createTravelExpenseReport(employee,applicationOID,reimbursementApi.getFormDetail(employee,getFormOID(employee,formName)),customFormValues);
        HashMap<String,String> info =new HashMap<>();
        info.put("expenseReportOID",reportdetail.get("expenseReportOID").getAsString());
        info.put("businessCode",reportdetail.get("businessCode").getAsString());
        log.info("[businessCode]:{}",info.get("businessCode"));
        return info;
    }

    /**
     * 根据控件名称获取申请单中带出的字段
     * @param employee
     * @param applicationOID  可能会存在关联多个申请单所以需要传俩个applicationOID
     * @param fieldName
     * @return
     * @throws HttpStatusException
     */
    public String getValueFromApplication(Employee employee, ArrayList applicationOID,String fieldName) throws HttpStatusException {
        JsonArray array =reimbursementApi.getValueFromApplication(employee,applicationOID);
        return GsonUtil.getJsonValue(array,"fieldName",fieldName,"value");
    }

    /**
     * 获取申请单自动默认带出的控件信息 all   如果报销单的数据是从申请单带入的 则可以掉这个接口 初始化component
     * @param employee
     * @param applicationOID
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getValueFromApplication(Employee employee,String applicationOID) throws HttpStatusException {
        ArrayList<String> list =new ArrayList<>();
        list.add(applicationOID);
        return reimbursementApi.getValueFromApplication(employee,list);
    }


    /**
     * 报销单费控标签检查
     * @param employee
     * @param expenseReportOID
     * @param externalPropertyName 标签类型：5001 超费用标准；REPORT_SUBMIT_WARN 校验警告
     * @param expectValue
     * @throws HttpStatusException
     */
    public boolean checkSubmitLabel(Employee employee,String expenseReportOID,String externalPropertyName,String expectValue) throws HttpStatusException {
        JsonObject result = expenseReportSubmitCheck(employee,expenseReportOID);
        log.info("校验的结果:{}",result);
        JsonArray checkResultList = result.get("checkResultList").getAsJsonArray();
        if(GsonUtil.isNotEmpt(checkResultList)){
            String message = GsonUtil.getJsonValue(checkResultList,"externalPropertyName",externalPropertyName).get("message").getAsString();
            if(message.contains(expectValue)){
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }

    /**
     * 查看报销单 的状态 1001 编辑中  1002 审批中 1003 是审批通过
     * @param employee
     * @param expenseReportOID
     * @return
     * @throws HttpStatusException
     */
    public Integer getReportStatus(Employee employee,String expenseReportOID) throws HttpStatusException {
       return getExpenseReportDetail(employee,expenseReportOID).get("status").getAsInt();
    }
}

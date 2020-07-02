package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.ExpenseApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.InvoiceComponent;
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
     * @param expenseTypenName  费用类型的名称
     * @param expenseReportOID   报销单的OID
     * @param amount   金额
     * @return
     */
    public HashMap<String,String> createExpenseInvoice(Employee employee,String expenseTypenName,String expenseReportOID,double amount){
        JsonObject jsonObject=null;
        try {
           jsonObject= expenseApi.expenseReportCreateinvoice(employee,"",getExpenseTypeInfo(employee,expenseTypenName,expenseReportOID),
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
    public HashMap<String,String> createExpenseInvoice(Employee employee, InvoiceComponent component, String expenseTypenName, String expenseReportOID, double amount){
        JsonObject jsonObject=null;
        try {
            jsonObject= expenseApi.expenseReportCreateinvoice(employee,getExpenseTypeInfo(employee,expenseTypenName,expenseReportOID),component,
                    expenseReportOID,amount,new JsonArray(),new JsonArray(),new JsonArray());
        } catch (HttpStatusException e) {
            e.printStackTrace();
        }
        HashMap<String,String> info =new HashMap<>();
        info.put("invoiceOID",jsonObject.get("rows").getAsJsonObject().get("invoiceOID").getAsString());
        return info;
    }

    /**
     * 获取报销单内的费用详情的list
     * @param employee
     * @param expenseReportOID
     * @return
     */
    public JsonArray getInvoiceDetail(Employee employee,String expenseReportOID) throws HttpStatusException {
        return expenseApi.getInvoiceDetail(employee,expenseReportOID).get("rows").getAsJsonObject().get("expenseReportInvoices").getAsJsonArray();
    }

    /**
     * 获取账本中所有费用的invoiceOID
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public ArrayList getExpenseItem(Employee employee) throws HttpStatusException {
        ArrayList<String> expenseItem =new ArrayList<>();
        JsonArray jsonArray=expenseApi.getInvoiceLlist(employee);
        for (int i=0;i<jsonArray.size();i++){
            expenseItem.add(jsonArray.get(i).getAsJsonObject().get("invoiceOID").getAsString());
        }
        return expenseItem;
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

}

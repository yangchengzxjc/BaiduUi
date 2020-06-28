package com.test.api.method.ExpenseMethod;

import com.google.gson.JsonArray;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.api.method.ExpenseReportInvoice;

import java.util.ArrayList;

/**
 * @Author peng.zhang
 * @Date 2020/6/28
 * @Version 1.0
 **/
public class BookInvoicePage {

    private ExpenseReportInvoice expenseReportInvoice;

    public BookInvoicePage(){
        expenseReportInvoice =new ExpenseReportInvoice();
    }

    public String transferInvoice(Employee employee) throws HttpStatusException {
        if(expenseReportInvoice.getExpenseItem(employee).size()==0){
            expenseReportInvoice.createExpenseInvoice(employee,"交通","",101.11);
        }
        ArrayList invoicdOID = expenseReportInvoice.getExpenseItem(employee);
        JsonArray array =new JsonArray();
        array.add(invoicdOID.get(0).toString());
        return expenseReportInvoice.transferTo(employee,"yuuki",array);
    }
}

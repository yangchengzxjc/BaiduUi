package com.test.api.method.BusinessMethod;

import com.google.gson.JsonArray;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * @Author peng.zhang
 * @Date 2020/6/17
 * @Version 1.0
 **/
@Slf4j
public class ExpenseReportPage {

    private ExpenseReport expenseReport;
    private ExpenseReportInvoice expenseReportInvoice;
    public ExpenseReportPage(){
        expenseReport =new ExpenseReport();
        expenseReportInvoice =new ExpenseReportInvoice();
    }


}
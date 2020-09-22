package com.test.api.testcase.ReimbStand;

import com.google.gson.JsonArray;
import com.hand.api.ReimbSubmissionControlApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.sun.xml.internal.ws.handler.HandlerException;
import com.test.BaseTest;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.Infra.SetOfBooksMethod.SetOfBooksDefine;
import com.test.api.method.ReimbSubmissionControl;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


@Slf4j
public class ReimbSubmissionControlTest extends BaseTest {
    private Employee employee;
    private ExpenseReport expenseReport;
    private ExpenseReportInvoice expenseReportInvoice;
    private ReimbSubmissionControl reimbSubmissionControl;
    private SetOfBooksDefine setOfBooksDefine;
    private ReimbSubmissionControlApi reimbSubmissionControlApi;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        expenseReport =new ExpenseReport();
        expenseReportInvoice =new ExpenseReportInvoice();
        employee=getEmployee(phoneNumber,pwd,env);
        reimbSubmissionControl =new ReimbSubmissionControl();
        reimbSubmissionControlApi =new ReimbSubmissionControlApi();

    }

    @Test(priority = 1,description = "创建报销单提交管控规则")
    public void creatRules() throws HttpStatusException {
        String rulesOid =reimbSubmissionControl.addReimbSubmissionControl(employee,"报销单提交日期","WARN",
                new JsonArray(),"费用消费日期不得超过报销单提交日期","SET_OF_BOOK",
                "1089991868999208961",
                "默认账套",new JsonArray());
    }




}

package com.test.api.testcase.ReimbStand;

import com.google.gson.JsonArray;
import com.hand.api.ReimbSubmissionControlApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
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
        setOfBooksDefine=new SetOfBooksDefine();
    }

    @Test(priority = 1,description = "创建报销单提交管控规则")
    public void creatRules() throws HttpStatusException {
        String rulesOid =reimbSubmissionControl.addReimbSubmissionControl(employee,"报销单提交日期1","WARN",
                new JsonArray(),"费用消费日期不得超过报销单提交日期","SET_OF_BOOK",
                setOfBooksDefine.getSetOfBooksId(employee,"DEFAULT_SOB","默认账套","reimb-submission-control"),
                "默认账套",new JsonArray());
        log.info("规则oid：{}",rulesOid);
        //获取默认规则详情
        String  rulesDetail=reimbSubmissionControl.getRules(employee,rulesOid);
        log.info("规则默认详情：{}",rulesDetail);
        //新建管控项
        reimbSubmissionControl.addRulesItem(employee,rulesOid,1006,1004,1002,1001,20);
        //查看管控项
        JsonArray itemsDetails =new JsonArray();
        itemsDetails=reimbSubmissionControl.getItems(employee,rulesOid);
        log.info("管控项详情：{}",itemsDetails);
    }
}
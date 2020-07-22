package com.test.api.testcase.setOfBooks;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import com.test.api.method.SetOfBooksDefine;
import com.test.api.method.SetOfBooksMethod.SetOfBooksPage;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@Slf4j
public class SetOfBooksTest extends BaseTest {
    private Employee employee;
    private SetOfBooksDefine setOfBooksDefine;
    private SetOfBooksPage setOfBooksPage;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978888") String phoneNumber, @Optional("hly123") String pwd, @Optional("stage") String env){
        employee = getEmployee(phoneNumber,pwd,env);
        setOfBooksDefine = new SetOfBooksDefine();
        setOfBooksPage = new SetOfBooksPage();
    }
    @Test(description = "测试获取会计期数据接口")
    public void getAccountingPeriod() throws HttpStatusException {
        setOfBooksDefine.getAccountingPeriod(employee,"默认会计期");
    }

    @Test(description = "测试获取科目表数据接口")
    public void getAccountSetId() throws HttpStatusException{
        setOfBooksDefine.getAccountSetId(employee,"DEFAULT_ACC");
    }

    @Test(description = "测试获取币种数据接口")
    public void getCurrencyCode() throws HttpStatusException {
        setOfBooksDefine.getfunctionalCurrencyCode(employee,"人民币");
    }

    @Test(description = "正常新建账套")
    public void addSetOfBooks() throws HttpStatusException {
        setOfBooksPage.addSetOfBooks(employee,true,"新增","add");
    }


}

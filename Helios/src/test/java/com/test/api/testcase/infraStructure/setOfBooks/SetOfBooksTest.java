package com.test.api.testcase.infraStructure.setOfBooks;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.infrastructure.setOfBooks.SetOfBooks;
import com.hand.utils.RandomNumber;
import com.test.BaseTest;
import com.test.api.method.Infra.SetOfBooksMethod.SetOfBooksDefine;
import com.test.api.method.Infra.SetOfBooksMethod.SetOfBooksPage;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@Slf4j
public class SetOfBooksTest extends BaseTest {
    private Employee employee;
    private SetOfBooksDefine setOfBooksDefine;
    private SetOfBooksPage setOfBooksPage;
    private SetOfBooks setOfBooks;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978888") String phoneNumber, @Optional("hly123") String pwd, @Optional("stage") String env) {
        employee = getEmployee(phoneNumber, pwd, env);
        setOfBooksDefine = new SetOfBooksDefine();
        setOfBooksPage = new SetOfBooksPage();
        setOfBooks = new SetOfBooks();
    }

    @Test(description = "测试获取会计期数据接口")
    public void getAccountingPeriod() throws HttpStatusException {
        String periodSetCode = setOfBooksDefine.getAccountingPeriod(employee, "默认会计期");
        Assert.assertEquals(periodSetCode, "DEFAULT_CAL");
    }

    @Test(description = "测试获取科目表数据接口")
    public void getAccountSetId() throws HttpStatusException {
        setOfBooksDefine.getAccountSetId(employee, "DEFAULT_ACC");
    }

    @Test(description = "测试获取币种数据接口")
    public void getCurrencyCode() throws HttpStatusException {
        String currencyCode = setOfBooksDefine.getFunctionalCurrencyCode(employee, "人民币");
        Assert.assertEquals(currencyCode, "CNY");
    }

    @Test(description = "正常新建账套")
    public void addSetOfBooksTest01() throws HttpStatusException {
        JsonObject object = setOfBooksPage.addSetOfBooks(employee, true, "新增", "add");
        String setOfBooksName = object.get("setOfBooksName").getAsString();
        String setOfBooksCode = object.get("setOfBooksCode").getAsString();
        log.info("新增的账套名称为：" + setOfBooksName);
        log.info("新增的账套编码为：" + setOfBooksCode);
        Assert.assertEquals(setOfBooksName, setOfBooks.getSetOfBooksName());
        Assert.assertEquals(setOfBooksCode, setOfBooks.getSetOfBooksCode());
    }

    @Test(description = "异常新建账套，账套code不符合编码规则，包含中文")
    public void addSetOfBooksTest02() throws HttpStatusException {
        JsonObject object = setOfBooksPage.addSetOfBooks(employee, true, "新增", "中文");
        String message = object.get("message").getAsString();
        log.info("获取到的message信息：" + message);
        String errorCode = object.get("errorCode").getAsString();
        log.info("获取到的错误代码：" + errorCode);
        Assert.assertEquals(message, "字符长度大于0&小于36,且只能输入数字,字母,下划线,点符号");
        Assert.assertEquals(errorCode, "6029001");
    }

    @Test(description = "正常编辑账套")
    public void editSetOfBooksTest01() throws HttpStatusException {
        //不传账套code和name，默认取到第一条账套数据的code和name
        JsonObject object = setOfBooksDefine.getSetOfBooksDetail(employee, "", "","set-of-books");
        String setOfBooksName = object.get("setOfBooksName").getAsString();
        String setOfBooksCode = object.get("setOfBooksCode").getAsString();
        String updateSetOfBooksName = "测试修改" + RandomNumber.getTimeNumber();
        String updateSetOfBooksCode = object.get("setOfBooksCode").getAsString();
        String updatePeriodSetCode = object.get("periodSetCode").getAsString();
        String updateAccountSetId = object.get("accountSetId").getAsString();
        String updateCurrencyCode = object.get("functionalCurrencyCode").getAsString();
        //将取到的账套code和name传递给编辑账套方法
        JsonObject editObject = setOfBooksPage.editSetOfBooks(employee, setOfBooks, setOfBooksCode, setOfBooksName, updateSetOfBooksCode, updateSetOfBooksName, updatePeriodSetCode, updateAccountSetId, updateCurrencyCode,"set-of-books");
        String editSetOfBooksName = editObject.get("setOfBooksName").getAsString();
        Assert.assertEquals(editSetOfBooksName, setOfBooks.getSetOfBooksName());
    }

    @Test(description = "异常编辑账套-修改账套code")
    public void editSetOfBooksTest02() throws HttpStatusException {
        JsonObject object = setOfBooksDefine.getSetOfBooksDetail(employee, "", "","set-of-books");
        String setOfBooksName = object.get("setOfBooksName").getAsString();
        String setOfBooksCode = object.get("setOfBooksCode").getAsString();
        String updateSetOfBooksName = object.get("setOfBooksName").getAsString();
        String updateSetOfBooksCode = RandomNumber.getTimeNumber();
        String updatePeriodSetCode = object.get("periodSetCode").getAsString();
        String updateAccountSetId = object.get("accountSetId").getAsString();
        String updateCurrencyCode = object.get("functionalCurrencyCode").getAsString();
        //将取到的账套code和name传递给编辑账套方法
        JsonObject editObject = setOfBooksPage.editSetOfBooks(employee, setOfBooks, setOfBooksCode, setOfBooksName, updateSetOfBooksCode, updateSetOfBooksName, updatePeriodSetCode, updateAccountSetId, updateCurrencyCode,"set-of-books");
        String message = editObject.get("message").getAsString();
        log.info("获取到的message信息：" + message);
        String errorCode = editObject.get("errorCode").getAsString();
        log.info("获取到的错误代码：" + errorCode);
        Assert.assertEquals(message,"帐套code不允许修改");
        Assert.assertEquals(errorCode,"6018016");
    }

}
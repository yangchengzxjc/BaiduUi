package com.test.api.method;

import com.google.gson.JsonObject;
import com.hand.api.SetOfBooksApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.SetOfBooks;
import com.hand.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SetOfBooksDefine {
    private SetOfBooksApi SetOfBooksApi;

    public SetOfBooksDefine(){
        SetOfBooksApi = new SetOfBooksApi();
    }

    /*
    * 新增账套
    * */
    public void addSetOfBooks(Employee employee, SetOfBooks setOfBooks,boolean enabled, String SetOfBooksName, String SetOfBooksCode) throws HttpStatusException {
        JsonObject object = SetOfBooksApi.addSetOfBooks(employee,setOfBooks,enabled,SetOfBooksName,SetOfBooksCode);

    }

    /*
    * 根据会计期名称 返回默认会计期code
    * */
    public String getAccountingPeriod(Employee employee, String periodSetName) throws HttpStatusException {
        String periodSetCode =GsonUtil.getJsonValue(SetOfBooksApi.getAccountingPeriod(employee),"periodSetName",periodSetName,"periodSetCode");
        log.info("获取到的periodSetCode：" + periodSetCode);
        return periodSetCode;
    }

    /*
    * 根据科目表code，返回默认科目表id
    * */
    public String getAccountSetId(Employee employee,String accountSetCode) throws HttpStatusException{
        String accountSetId = GsonUtil.getJsonValue(SetOfBooksApi.getAccountSetId(employee).getAsJsonArray("rows"),"accountSetCode",accountSetCode,"id");
        log.info("获取到的科目表id为：" + accountSetId);
        return accountSetId;
    }

    /*
    * 根据币种名称，返回币种code
    * */
    public String getfunctionalCurrencyCode(Employee employee,String CurrencyName) throws HttpStatusException {
        String CurrencyCode = GsonUtil.getJsonValue(SetOfBooksApi.getfunctionalCurrencyCode(employee).getAsJsonArray("rows"),"currencyName",CurrencyName,"currencyCode");
        log.info("获取到的币种code为：" + CurrencyCode);
        return CurrencyCode;
    }
}

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

    /**
     * 新增账套所需字段
     * @param employee
     * @param setOfBooks
     * @param enabled
     * @param setOfBooksName
     * @param setOfBooksCode
     * @throws HttpStatusException
     */
    public void addSetOfBooks(Employee employee, SetOfBooks setOfBooks,boolean enabled, String setOfBooksName, String setOfBooksCode) throws HttpStatusException {
        JsonObject object = SetOfBooksApi.addSetOfBooks(employee,setOfBooks,enabled,setOfBooksName,setOfBooksCode);

    }

    /**
     * 根据会计期名称 获取默认会计期code
     * @param employee
     * @param periodSetName
     * @return
     * @throws HttpStatusException
     */
    public String getAccountingPeriod(Employee employee, String periodSetName) throws HttpStatusException {
        String periodSetCode =GsonUtil.getJsonValue(SetOfBooksApi.getAccountingPeriod(employee),"periodSetName",periodSetName,"periodSetCode");
        log.info("获取到的periodSetCode：" + periodSetCode);
        return periodSetCode;
    }

    /**
     * 根据科目表code，获取默认科目表id
     * @param employee
     * @param accountSetCode
     * @return
     * @throws HttpStatusException
     */
    public String getAccountSetId(Employee employee,String accountSetCode) throws HttpStatusException{
        String accountSetId = GsonUtil.getJsonValue(SetOfBooksApi.getAccountSetId(employee).getAsJsonArray("rows"),"accountSetCode",accountSetCode,"id");
        log.info("获取到的科目表id为：" + accountSetId);
        return accountSetId;
    }

    /**
     * 根据币种名称，获取币种code
     * @param employee
     * @param currencyName
     * @return
     * @throws HttpStatusException
     */
    public String getFunctionalCurrencyCode(Employee employee,String currencyName) throws HttpStatusException {
        String currencyCode = GsonUtil.getJsonValue(SetOfBooksApi.getFunctionalCurrencyCode(employee).getAsJsonArray("rows"),"currencyName",currencyName,"currencyCode");
        log.info("获取到的币种code为：" + currencyCode);
        return currencyCode;
    }
}

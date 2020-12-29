package com.test.api.method.Infra.SetOfBooksMethod;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.infrastructure.setOfBooks.SetOfBooks;
import com.hand.utils.RandomNumber;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SetOfBooksPage {
    private SetOfBooksDefine setOfBooksDefine;

    public SetOfBooksPage(){
        setOfBooksDefine = new SetOfBooksDefine();
    }

    /**
     * 新增账套数据
     * @param employee
     * @param enabled
     * @param setOfBooksName
     * @param setOfBooksCode
     * @throws HttpStatusException
     */
    public JsonObject addSetOfBooks(Employee employee,SetOfBooks setOfBooks, boolean enabled, String setOfBooksName, String setOfBooksCode,
                                    String accountSetCode,String periodSetName,String currencyName) throws HttpStatusException{
        setOfBooks.setEnabled(enabled);
        setOfBooks.setSetOfBooksName(setOfBooksName + RandomNumber.getTimeNumber());
        setOfBooks.setSetOfBooksCode(setOfBooksCode + RandomNumber.getTimeNumber());
        setOfBooks.setAccountSetId(setOfBooksDefine.getAccountSetId(employee,accountSetCode));
        setOfBooks.setPeriodSetCode(setOfBooksDefine.getAccountingPeriod(employee,periodSetName));
        setOfBooks.setFunctionalCurrencyCode(setOfBooksDefine.getFunctionalCurrencyCode(employee,currencyName));
        return setOfBooksDefine.addSetOfBooks(employee,setOfBooks,enabled);
    }

    public JsonObject addSetOfBooks(Employee employee,SetOfBooks setOfBooks, boolean enabled, String setOfBooksName, String setOfBooksCode) throws HttpStatusException{
        setOfBooks.setEnabled(enabled);
        setOfBooks.setSetOfBooksName(setOfBooksName);
        setOfBooks.setSetOfBooksCode(setOfBooksCode);
        setOfBooks.setAccountSetId(setOfBooksDefine.getAccountSetId(employee,"DEFAULT_ACC"));
        setOfBooks.setPeriodSetCode(setOfBooksDefine.getAccountingPeriod(employee,"默认会计期"));
        setOfBooks.setFunctionalCurrencyCode(setOfBooksDefine.getFunctionalCurrencyCode(employee,"人民币"));
        return setOfBooksDefine.addSetOfBooks(employee,setOfBooks,enabled);
    }

    /**
     * 编辑账套数据
     * @param employee
     * @param setOfBooks
     * @param setOfBooksCode
     * @param setOfBooksName
     * @return
     * @throws HttpStatusException
     */
    public JsonObject editSetOfBooks(Employee employee,SetOfBooks setOfBooks,String setOfBooksCode,String setOfBooksName,String updateSetOfBooksCode,String updateSetOfBooksName,String updatePeriodSetCode,String updateAccountSetId,String updateCurrencyCode,String key) throws HttpStatusException {
//        SetOfBooks setOfBooks = new SetOfBooks();
        JsonObject setOfBooksInfo = setOfBooksDefine.getSetOfBooksDetail(employee,setOfBooksCode,setOfBooksName,key);
        setOfBooks.setSetOfBooksName(updateSetOfBooksName);
        setOfBooks.setSetOfBooksCode(updateSetOfBooksCode);
        setOfBooks.setPeriodSetCode(updatePeriodSetCode);
        setOfBooks.setAccountSetId(updateAccountSetId);
        setOfBooks.setFunctionalCurrencyCode(updateCurrencyCode);
        //账套名称多语言数组
        JsonArray arrayLanguage = new JsonArray();
        //账套名称多语言对象
        JsonObject objectSetOfBooksName = new JsonObject();
        JsonObject setOfBooksNameI18n1 =  new JsonObject();
        JsonObject setOfBooksNameI18n2 =  new JsonObject();
        setOfBooksNameI18n1.addProperty("language","zh_cn");
        setOfBooksNameI18n1.addProperty("value",updateSetOfBooksName);
        setOfBooksNameI18n2.addProperty("language","en");
        setOfBooksNameI18n2.addProperty("value",updateSetOfBooksName);
        arrayLanguage.add(setOfBooksNameI18n1);
        arrayLanguage.add(setOfBooksNameI18n2);
        objectSetOfBooksName.add("setOfBooksName",arrayLanguage);
        setOfBooks.setI18n(objectSetOfBooksName);
        return setOfBooksDefine.editSetOfBooks(employee,setOfBooks,setOfBooksInfo);
    }
}

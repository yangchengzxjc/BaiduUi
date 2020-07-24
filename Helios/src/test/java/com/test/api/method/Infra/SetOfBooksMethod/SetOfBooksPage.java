package com.test.api.method.SetOfBooksMethod;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.SetOfBooks;
import com.hand.utils.RandomNumber;
import com.test.api.method.SetOfBooksDefine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SetOfBooksPage {
    private SetOfBooksDefine setOfBooksDefine;

    public SetOfBooksPage(){
        setOfBooksDefine = new SetOfBooksDefine();
    }

    /*
     * 新增账套
     * */
    public void addSetOfBooks(Employee employee,boolean enabled, String SetOfBooksName, String SetOfBooksCode) throws HttpStatusException{
        SetOfBooks setOfBooks = new SetOfBooks();
        setOfBooks.setEnabled(enabled);
        setOfBooks.setSetOfBooksName(SetOfBooksName + String.valueOf(RandomNumber.getTimeNumber()));
        setOfBooks.setSetOfBooksCode(SetOfBooksCode + String.valueOf(RandomNumber.getTimeNumber()));
        setOfBooks.setAccountSetId(setOfBooksDefine.getAccountSetId(employee,"DEFAULT_ACC"));
        setOfBooks.setPeriodSetCode(setOfBooksDefine.getAccountingPeriod(employee,"默认会计期"));
        setOfBooks.setFunctionalCurrencyCode(setOfBooksDefine.getfunctionalCurrencyCode(employee,"人民币"));
        log.info("新增的账套信息为：" + setOfBooks);
        setOfBooksDefine.addSetOfBooks(employee,setOfBooks,enabled,SetOfBooksName,SetOfBooksCode);
    }
}

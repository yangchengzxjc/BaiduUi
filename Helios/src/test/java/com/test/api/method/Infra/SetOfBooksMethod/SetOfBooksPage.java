package com.test.api.method.Infra.SetOfBooksMethod;

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

    /**
     * 新增账套数据
     * @param employee
     * @param enabled
     * @param setOfBooksName
     * @param setOfBooksCode
     * @throws HttpStatusException
     */
    public void addSetOfBooks(Employee employee,boolean enabled, String setOfBooksName, String setOfBooksCode) throws HttpStatusException{
        SetOfBooks setOfBooks = new SetOfBooks();
        setOfBooks.setEnabled(enabled);
        setOfBooks.setSetOfBooksName(setOfBooksName + RandomNumber.getTimeNumber());
        setOfBooks.setSetOfBooksCode(setOfBooksCode + RandomNumber.getTimeNumber());
        setOfBooks.setAccountSetId(setOfBooksDefine.getAccountSetId(employee,"DEFAULT_ACC"));
        setOfBooks.setPeriodSetCode(setOfBooksDefine.getAccountingPeriod(employee,"默认会计期"));
        setOfBooks.setFunctionalCurrencyCode(setOfBooksDefine.getFunctionalCurrencyCode(employee,"人民币"));
        log.info("新增的数据：" + setOfBooks);
        setOfBooksDefine.addSetOfBooks(employee,setOfBooks,enabled);
    }
}

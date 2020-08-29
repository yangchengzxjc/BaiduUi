package com.test.api.method.Infra.SetOfBooksMethod;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.SetOfBooksApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.infrastructure.setOfBooks.SetOfBooks;
import com.hand.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SetOfBooksDefine {
    private SetOfBooksApi SetOfBooksApi;

    public SetOfBooksDefine(){
        SetOfBooksApi = new SetOfBooksApi();
    }

    /**
     * 获取新增账套响应数据
     * @param employee
     * @param setOfBooks
     * @throws HttpStatusException
     */
    public JsonObject addSetOfBooks(Employee employee,SetOfBooks setOfBooks,boolean enabled) throws HttpStatusException {
        JsonObject object = SetOfBooksApi.addSetOfBooks(employee,setOfBooks,enabled);
        return object;
    }

    /**
     * 获取编辑账套响应数据
     * @param employee
     * @param setOfBooksInfo
     * @return
     * @throws HttpStatusException
     */
    public JsonObject editSetOfBooks(Employee employee,SetOfBooks setOfBooks,JsonObject setOfBooksInfo) throws HttpStatusException {
        JsonObject object = SetOfBooksApi.editSetOfBooks(employee,setOfBooks,setOfBooksInfo);
        return object;
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

    /**
     * 根据账套code和账套name查询获取账套id
     * @param employee
     * @param setOfBooksCode
     * @param setOfBooksName
     * @return
     * @throws HttpStatusException
     */
    public String getSetOfBooksId(Employee employee,String setOfBooksCode,String setOfBooksName,String key) throws HttpStatusException {
        JsonArray setOfBooksList = SetOfBooksApi.getSetOfBooks(employee,setOfBooksCode,setOfBooksName,key);
        String id = "";
        if(GsonUtil.isNotEmpt(setOfBooksList)){
            id = setOfBooksList.get(0).getAsJsonObject().get("id").getAsString();
        }else{
            log.info("账套列表查询结果为空,请检查入参");
        }
        return id;
    }

    /**
     * 根据账套id获取账套详情
     * @param employee
     * @param setOfBooksCode
     * @param setOfBooksName
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getSetOfBooksDetail(Employee employee,String setOfBooksCode,String setOfBooksName,String key) throws HttpStatusException {
        return SetOfBooksApi.getSetOfBooksDetail(employee,getSetOfBooksId(employee,setOfBooksCode,setOfBooksName,key));
    }
}

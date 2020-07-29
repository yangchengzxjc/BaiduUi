package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.ReimbStandardApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;



@Slf4j
public class RuleDetails {
    private ReimbStandardApi reimbStandardRules;

    //    private TravelSubsidyConfigAPi reimbStandardRules;
    public RuleDetails() {
        reimbStandardRules = new ReimbStandardApi();
    }

    /*
     * 根据账套名称查账套id
     * @ setOfBooksName 账套名称
     * @ return
     */
    public String getSetOfBookId (Employee employee,String BooksName) throws HttpStatusException{
        JsonArray BookList = reimbStandardRules.getSetOfBooks(employee);
        return GsonUtil.getJsonValue(BookList,"setOfBooksName",BooksName,"id");
    }
    /*
     * 根据人员组名称获取人员组
     * @ userGroupsName 人员组名称
     * @ return
     */

    public JsonObject getUserGroups(Employee employee,String userGroupsName) throws HttpStatusException {
        JsonArray userGroupsList = reimbStandardRules.getUserGroups(employee, getSetOfBookId(employee, "默认账套"));
//        log.info("userList,{}",userGroupsList);
        JsonObject userGroups;
        userGroups = GsonUtil.getJsonValue(userGroupsList,"name",userGroupsName);
       log.info("userList,{}",userGroupsList);
        return userGroups;
    }
    /*
     * 根据费用类型名称获取费用
     * @ expenseTypeName 费用类型名称
     * @ return
     */
    public JsonObject getExpenseType(Employee employee,String expenseTypeName)throws HttpStatusException{
        JsonArray expenseTypeList = reimbStandardRules.getExpenseType(employee,getSetOfBookId(employee,"默认账套"));
        JsonObject expenseType;
        expenseType = GsonUtil.getJsonValue(expenseTypeList,"name",expenseTypeName);
//        for (int i =0;i<expenseTypeList.size();i++){
//            if(expenseTypeList.get(i).getAsJsonObject().get("name").getAsString().equals(expenseTypeName)){
//                expenseType=expenseTypeList.get(i).getAsJsonObject();
//            }
//        }
        return expenseType;
    }

}

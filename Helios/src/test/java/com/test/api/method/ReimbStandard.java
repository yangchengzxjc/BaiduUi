package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.ReimbStandardApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;


@Slf4j
public class ReimbStandard {
    private ReimbStandardApi reimbStandardRules;
    private ReimbStandard reimbStandard;

    //    private TravelSubsidyConfigAPi reimbStandardRules;
    public ReimbStandard() {
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
        JsonObject userGroups;
        userGroups = GsonUtil.getJsonValue(userGroupsList,"name",userGroupsName);
        log.info("userList,{}",userGroupsList);
        return userGroups;
    }

    /**
     * 根据费用类型名称获取费用类型
     * @param employee
     * @param expenseTypeName
     * @return
     * @throws HttpStatusException
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

    /**
     * 新增报销标准规则
     * @param employee
     * @param name
     * @param controlLevel
     * @param levelCode
     * @param levelOrgId
     * @param controlType
     * @param controlModeType
     * @param message
     * @param userGroups
     * @param expenseTypes
     * @param forms
     * @param companys
     * @return
     */

    public String addReimbstandard(Employee employee, String name, String controlLevel, String levelCode, String levelOrgId,
                                   String controlType, String controlModeType, String message,
                                   JsonArray userGroups, JsonArray expenseTypes, JsonArray forms,
                                   JsonArray companys)throws HttpStatusException{

      String rulesOid= reimbStandardRules.creatReimbStandardRules(employee,name,controlLevel,levelCode,
                levelOrgId,controlType,controlModeType,message,userGroups,expenseTypes,forms,companys);
        return rulesOid;
    }

    /**
     * 启用公司
     * @param companyGroup
     * @return
     */
    public JsonArray companyGroups(JsonObject ... companyGroup){
        JsonArray array = new JsonArray();
        for (int i=0;i<companyGroup.length;i++){
            array.add(companyGroup[i]);
        }
        return  array;
    }

    /**
     * 人员组
     * @param userGroup
     * @return
     */
    public JsonArray userGroups(JsonObject ... userGroup){
        JsonArray array =new JsonArray();
        for(int i=0;i<userGroup.length;i++){
            array.add(userGroup[i]);
        }
        return array;
    }

    /**
     * 费用类型
     * @param expenseType
     * @return
     */
    public JsonArray expenseType(JsonObject ... expenseType){
        JsonArray array = new JsonArray();
        for (int i=0;i<expenseType.length;i++){
            array.add(expenseType[i]);
        }
        return array;
    }

    /**
     * 删除报销标准规则
     * @param employee
     * @param rulesOid
     * @throws HttpStatusException
     */
    public void deleteReimbStandardRules (Employee employee,String rulesOid)throws HttpStatusException{
        reimbStandardRules.deleteReimbStandardRules(employee,rulesOid);

    }

    /**
     * 获取报销标准默认的管控信息
     * @param employee
     * @param rulesOid
     * @throws HttpStatusException
     */
    public JsonArray getControlItems(Employee employee,String rulesOid)throws HttpStatusException{
        JsonArray controlItems = new JsonArray();
        controlItems=reimbStandardRules.getControlItem(employee,rulesOid);
        return controlItems;
    }

    /**
     * 获取报销标准默认的基本标准
     * @param employee
     * @param rulesOid
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getItem(Employee employee,String rulesOid)throws HttpStatusException{
        JsonArray item = new JsonArray();
        item = reimbStandardRules.getItem(employee,rulesOid);
        return  item;
    }

    /**
     * 添加基本标准
     * @param employee
     * @param standardOid
     * @param amount
     * @param userGroups
     * @param citys
     * @return
     * @throws HttpStatusException
     */
    public String addItems(Employee employee,String standardOid,String rulesOid,String amount,JsonArray userGroups,JsonArray citys)throws HttpStatusException{
         String items = reimbStandardRules.addItems(employee,standardOid,rulesOid,amount,userGroups,citys);
        return items;
    }

}

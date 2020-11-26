package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.InfraStructureApi;
import com.hand.api.ReimbStandardApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.StandardRules;
import com.hand.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ReimbStandard {
    private ReimbStandardApi reimbStandardRules;

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
    public JsonObject getUserGroups(Employee employee,String userGroupsName,String setOfBooksId) throws HttpStatusException {
        JsonArray userGroupsList = reimbStandardRules.getUserGroups(employee, setOfBooksId);
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
    public JsonObject getExpenseType(Employee employee,String expenseTypeName,String setOfBooksId)throws HttpStatusException{
        JsonArray expenseTypeList = reimbStandardRules.getExpenseType(employee,setOfBooksId);
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
     * 获取单据类型
     * @param employee
     * @param formName
     * @param setOfBooksId
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getFormType(Employee employee,String formName,String setOfBooksId)throws HttpStatusException{
        JsonArray formTypeList = reimbStandardRules.getFormTpye(employee,setOfBooksId,formName);
        log.info("formList:{}",formTypeList);
        JsonObject formType = new JsonObject();
        if(GsonUtil.isNotEmpt(formTypeList)){
            formType = GsonUtil.getJsonValue(formTypeList,"formName",formName);
        }
        log.info("获取表单：{}",formType);
        return formType;
    }

    /**
     *
     * @param employee
     * @param companyName  公司的名称
     * @param setOfBooksId 账套的id
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getCompany(Employee employee,String companyName,String setOfBooksId)throws HttpStatusException{
        JsonArray companyList = reimbStandardRules.getEnabledCompany(employee,setOfBooksId);
        JsonObject company;
        company = GsonUtil.getJsonValue(companyList,"name",companyName);
        return company;
    }
    /**
     * 新增报销标准规则
     * @return
     */

    public String addReimbstandard(Employee employee, StandardRules rules,String formName,String ... expenseTypeName)throws HttpStatusException{
        InfraStructureApi infraStructureApi =new InfraStructureApi();
        if(rules.getLevelCode().equals("SET_OF_BOOK")){
            rules.setLevelOrgId(employee.getSetOfBookId());
        }else{
            rules.setLevelOrgId(employee.getCompanyId());
        }
        //处理费用类型
        JsonArray expenseType = new JsonArray();
        for (String aExpenseTypeName : expenseTypeName){
           expenseType.add(GsonUtil.getJsonValue(infraStructureApi.getExpenseType(employee,rules.getLevelOrgId(),aExpenseTypeName),"name",aExpenseTypeName));
        }
        rules.setExpenseTypes(expenseType);
        //处理表单
        JsonArray form = new JsonArray();

        return reimbStandardRules.addReimbStandardRules(employee,rules);
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
    public JsonArray expenseTypes(JsonObject ... expenseType){
        JsonArray array = new JsonArray();
        for (int i=0;i<expenseType.length;i++){
            array.add(expenseType[i]);
        }
        return array;
    }

    /**
     * 单据类型
     * @param formType
     * @return
     */
    public JsonArray formTypes(JsonObject ... formType){
        JsonArray array = new JsonArray();
        for (JsonObject aFormType : formType) {
            array.add(aFormType);
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
        return reimbStandardRules.getControlItem(employee,rulesOid);
    }

    /**
     * 获取报销标准默认的基本标准
     * @param employee
     * @param rulesOid
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getItem(Employee employee,String rulesOid)throws HttpStatusException{

        return reimbStandardRules.getItem(employee,rulesOid);

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
    public String addItems(Employee employee,String standardOid,String rulesOid,Integer amount,JsonArray userGroups,JsonArray citys)throws HttpStatusException{
         String items = reimbStandardRules.addItems(employee,standardOid,rulesOid,amount,userGroups,citys);
        return items;
    }

    /**
     * 查询规则
     * @param employee
     * @param ruleName
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getRules(Employee employee,String ruleName)throws HttpStatusException{
        JsonArray rulesList = new JsonArray();
        rulesList =reimbStandardRules.getRules(employee,ruleName);
        return  rulesList;
    }

//    public String creatReimbSubmissionControlRuls(Employee employee)throws HttpStatusException{
//        String rulesOid=reimbStandardRules
//    }
}

package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.InfraStructureApi;
import com.hand.api.ReimbStandardApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.StandardRules;
import com.hand.basicObject.Rule.StandardRulesItem;
import com.hand.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;


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


    /**
     * 查询人员组
     * @param employee
     * @param userGroupsName 人员组的名称
     * @param rules
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getUserGroups(Employee employee,String userGroupsName,StandardRules rules) throws HttpStatusException {
        JsonArray userGroupsList = reimbStandardRules.getUserGroups(employee,rules.getLevelCode(),rules.getLevelOrgId(),userGroupsName);
        JsonObject userGroups;
        userGroups = GsonUtil.getJsonValue(userGroupsList,"name",userGroupsName);
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
     * 报销标准-新建规则
     * @param employee
     * @param rules
     * @param formName 适用的表单名称
     * @param expenseTypeName  费用类型
     * @return
     * @throws HttpStatusException
     */
    public HashMap<String,String> addReimbstandard(Employee employee, StandardRules rules, String [] userGroupsName, String[] formName, String ... expenseTypeName)throws HttpStatusException{
        InfraStructureApi infraStructureApi =new InfraStructureApi();
        if(rules.getLevelCode().equals("SET_OF_BOOK")){
            rules.setLevelOrgId(employee.getSetOfBookId());
            //账套级的 默认为通用
            rules.setCompanys(new JsonArray());
        }else{
            //如果是公司模式  默认了当前账号的公司
            rules.setLevelOrgId(employee.getCompanyId());
        }
        //设置人员组
        if(userGroupsName.length!=0){
            JsonArray userGroup = new JsonArray();
            for (String mUserGroupsName : userGroupsName){
                userGroup.add(getUserGroups(employee,mUserGroupsName,rules));
            }
        }else{
            rules.setUserGroups(new JsonArray());
        }
        //处理费用类型
        JsonArray expenseType = new JsonArray();
        for (String aExpenseTypeName : expenseTypeName){
           expenseType.add(GsonUtil.getJsonValue(infraStructureApi.getExpenseType(employee,rules.getLevelOrgId(),aExpenseTypeName),"name",aExpenseTypeName));
        }
        rules.setExpenseTypes(expenseType);
        //处理表单 form
        if(formName.length!=0){
            //适配单据
            JsonArray form = new JsonArray();
            for (String mFormName : formName){
                form.add(GsonUtil.getJsonValue(infraStructureApi.controlGetForm(employee,rules.getLevelOrgId(),mFormName),"formName",mFormName));
            }
            rules.setForms(form);
        }
        // expense participants standard
        if(rules.isParticipantsEnable()){
            //default participantsMode="HIGH"
            if(rules.getParticipantsMode()==null){
                rules.setParticipantsMode("HIGH");
            }
        }
        String ruleOID = reimbStandardRules.addReimbStandardRules(employee,rules).replace("\"","");
        //获取默认的标准的oid
        String standardOid = reimbStandardRules.getItem(employee,ruleOID).get(0).getAsJsonObject().get("standardOID").getAsString();
        HashMap<String,String> map = new HashMap<>();
        map.put("ruleOID",ruleOID);
        map.put("dafaultStandardOID",standardOid);
        return map;
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
     * @param item 标准管控项
     * @param defaultStandardOID 默认的基本标准的oid,如果基本标准存在，则必须删除掉
     * @return
     * @throws HttpStatusException
     */
    public String addItems(Employee employee, StandardRules rules,StandardRulesItem item,String defaultStandardOID,String [] userGroupsName,String []cityGroupsName)throws HttpStatusException{
        // config userGroups
        if(userGroupsName.length!=0){
            JsonArray userGroups =new JsonArray();
            for (String userGroupName: userGroupsName){
                 JsonObject userGroupObject = getUserGroups(employee,userGroupName,rules);
                 userGroups.add(userGroupObject);
            }
            item.setUserGroups(userGroups);
        }
        //config cityGroups
        if(cityGroupsName.length!=0){
            JsonArray userGroupArray = reimbStandardRules.getCityGroup(employee,rules.getLevelCode(),rules.getLevelOrgId());
            JsonArray cityGroups = new JsonArray();
            for(String cityGroupName:cityGroupsName){
                if(GsonUtil.isNotEmpt(cityGroups)){
                cityGroups.add(GsonUtil.getJsonValue(userGroupArray,"levelName",cityGroupName));
                }
            }
            item.setCitys(cityGroups);
        }
        String items = reimbStandardRules.addItems(employee,item);
        //删除默认的管控标准
        if(!defaultStandardOID.equals("")){
            reimbStandardRules.deleteStandardItem(employee,item.getRuleOID(),defaultStandardOID);
        }
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
}

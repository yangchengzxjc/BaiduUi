package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.InfraStructureApi;
import com.hand.api.ReimbSubmissionControlApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.SubmitRuleItem;
import com.hand.basicObject.Rule.SubmitRules;
import com.hand.basicConstant.HeaderKey;
import com.hand.utils.GsonUtil;
import com.test.api.method.Infra.SetOfBooksMethod.SetOfBooksDefine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReimbSubmissionControl {
    private ReimbSubmissionControlApi reimbSubmissionControl;

    public ReimbSubmissionControl(){
        reimbSubmissionControl = new ReimbSubmissionControlApi();
    }

    /**
     * 报销单提交管控规则创建
     * @param employee
     * @param rules
     * @param formName  适用单据为空则默认为 通用
     * @param companyName 账套模式下的公司配置  如果为空则认为是通用
     * @return
     * @throws HttpStatusException
     */
    public String addReimbSubmissionControl(Employee employee, SubmitRules rules,String formName,String companyName)throws HttpStatusException {
        //获取控制规则表单
        SetOfBooksDefine setOfBooksDefine =new SetOfBooksDefine();
        ReimbStandard reimbStandard = new ReimbStandard();
        InfraStructureApi infraStructureApi =new InfraStructureApi();
        String levelOrgId="";
        JsonArray form;
        //账套级配置
        if(rules.getLevelCode().equals("SET_OF_BOOK")){
            levelOrgId = setOfBooksDefine.getSetOfBooksId(employee,"",rules.getLevelOrgName(), HeaderKey.REIMB_SUBMIT_CONTROL);
            rules.setLevelOrgId(levelOrgId);
            if(!formName.equals("")) {
                form = infraStructureApi.controlGetForm(employee, rules.getLevelOrgId(), formName);
                form.get(0).getAsJsonObject().addProperty("key",form.get(0).getAsJsonObject().get("formOID").getAsString());
                rules.setForms(form);
            }
            if(!companyName.equals("")){
                JsonObject company = new JsonObject();
                company.addProperty("name",companyName);
                company.addProperty("id",reimbStandard.getCompany(employee,companyName,rules.getLevelOrgId()).get("id").getAsString());
                JsonArray array = new JsonArray();
                array.add(company);
                rules.setCompanys(array);
            }
        }else {
            //公司级配置
            rules.setLevelOrgName(companyName);
            JsonObject companyObject = reimbStandard.getCompany(employee,rules.getLevelOrgName(),"");
            levelOrgId = companyObject.get("id").getAsString();
            rules.setLevelOrgId(levelOrgId);
            rules.setCompanyOID(companyObject.get("companyOID").getAsString());
            if(!formName.equals("")){
                form = infraStructureApi.controlGetForm(employee,rules.getCompanyOID(),formName);
                form.get(0).getAsJsonObject().addProperty("key",form.get(0).getAsJsonObject().get("formOID").getAsString());
                rules.setForms(form);
            }
        }
        return reimbSubmissionControl.creatSubmitRules(employee,rules).replace("\"","");
    }


    /**
     * 获取规则默认详情
     * @param employee
     * @param rulesOid
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getRules(Employee employee, String rulesOid)throws HttpStatusException{
        JsonObject rulesDetails=reimbSubmissionControl.getRules(employee,rulesOid);
        return rulesDetails;
    }

    /**
     * 新增管控项
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public String addRulesItem(Employee employee,String rulesOid,SubmitRuleItem item,String expenseTypeName)throws HttpStatusException{
        JsonObject ruleDetail = getRules(employee,rulesOid);
        String expenseTypeOid = getExpense(employee,ruleDetail.get("levelOrgId").getAsString(),expenseTypeName).get("expenseTypeOID").getAsString();
        if(item.getControlItem().equals(1002) || item.getControlItem().equals(1001) || item.getControlItem().equals(1009)){
            JsonArray expense = new JsonArray();
            expense.add(expenseTypeOid);
            item.setFieldValue(expense);
        }else if(item.getControlItem().equals(1005)){
            JsonArray expense = new JsonArray();
            expense.add(expenseTypeOid);
            item.setExtendValue(expense);
        }
        return reimbSubmissionControl.addRulesItems(employee,rulesOid,item);
    }

    /**
     * 获取规则管控项
     * @param employee
     * @param rulesOid
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getItems(Employee employee,String rulesOid)throws HttpStatusException{
        return reimbSubmissionControl.getItems(employee,rulesOid);
    }

    /**
     * 规则删除
     * @param employee
     * @param rulesOid
     * @throws HttpStatusException
     */
    public void deleteReimbSubmissionRules(Employee employee,String rulesOid) throws HttpStatusException{
        reimbSubmissionControl.deleteReimbSubmissionControlRules(employee,rulesOid);
    }

    /**
     *
     * @param employee
     * @param setBooksId
     * @param expenseName
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getExpense(Employee employee,String setBooksId,String expenseName) throws HttpStatusException {
        InfraStructureApi infraStructure =new InfraStructureApi();
        JsonArray expense = infraStructure.getExpenseType(employee,setBooksId,expenseName);
        if(GsonUtil.isNotEmpt(expense)){
            return GsonUtil.getJsonValue(expense,"name",expenseName);
        }else{
            throw new RuntimeException("未查找到相关费用类型");
        }
    }
}
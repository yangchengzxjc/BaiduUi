package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.ReimbSubmissionControlApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.SubmitRules;
import com.hand.basicconstant.HeaderKey;
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
        String levelOrgId="";
        JsonArray form;
        //账套级配置
        if(rules.getLevelCode().equals("SET_OF_BOOK")){
            levelOrgId = setOfBooksDefine.getSetOfBooksId(employee,"",rules.getLevelOrgName(), HeaderKey.REIMB_SUBMIT_CONTROL);
            rules.setLevelOrgId(levelOrgId);
            if(!formName.equals("")) {
                form = reimbSubmissionControl.controlGetForm(employee, rules.getLevelOrgId(), "", formName);
                log.info("适用单据:{}",form);
                rules.setForms(form);
            }
            if(!companyName.equals("")){
                JsonObject company = new JsonObject();
                company.addProperty("name",companyName);
                company.addProperty("id",reimbStandard.getCompany(employee,companyName,"").get("id").getAsString());
                JsonArray array = new JsonArray();
                array.add(company);
                rules.setCompanys(array);
            }
        }else {
            rules.setLevelOrgName(companyName);
            JsonObject companyObject = reimbStandard.getCompany(employee,rules.getLevelOrgName(),"");
            levelOrgId = companyObject.get("id").getAsString();
            rules.setLevelOrgId(levelOrgId);
            rules.setCompanyOID(companyObject.get("companyOID").getAsString());
            if(!formName.equals("")){
                form = reimbSubmissionControl.controlGetForm(employee,"",rules.getCompanyOID(),formName);
                rules.setForms(form);
            }
        }
        return reimbSubmissionControl.creatSubmitRules(employee,rules);
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
     * @param rulesOid
     * @param controlItem
     * @param valueType
     * @param controlCond
     * @param mixedItem
     * @param extendValue
     * @return
     * @throws HttpStatusException
     */
    public String addRulesItem(Employee employee,String rulesOid,int controlItem,int valueType,
                               int controlCond,int mixedItem,int extendValue)throws HttpStatusException{
        String body=reimbSubmissionControl.addRulesItems(employee,rulesOid,controlItem,valueType,controlCond,mixedItem,extendValue);
        return body;
    }

    /**
     * 获取规则管控项
     * @param employee
     * @param rulesOid
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getItems(Employee employee,String rulesOid)throws HttpStatusException{
        JsonArray items =new JsonArray();
        items = reimbSubmissionControl.getItems(employee,rulesOid);
        return items;
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
}
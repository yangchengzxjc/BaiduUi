package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicConstant.ApiPath;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author peng.zhang
 * @Date 2020/6/9
 * @Version 1.0
 **/
@Slf4j
public class ComponentQueryApi extends BaseRequest{



    /**
     * 获取值列表信息
     * @param employee
     * @return
     */
    public JsonArray getCustomEumerationOid(Employee employee, String customEnumerationOID) throws HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ String.format(ApiPath.GET_CUSTOMENUMERATIONOID,customEnumerationOID);
        String res= doGet(url,getHeader(employee.getAccessToken()),null,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 报销单查询货币
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getCurrency(Employee employee) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.GETCURRENCY;
        Map<String, String> urlform = new HashMap<String, String>();
        urlform.put("language", "chineseName");
        urlform.put("userOID",employee.getUserOID());
        String res= doGet(url,getHeader(employee.getAccessToken()),urlform,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 报销单查询成本中心项
     * @param employee
     * @param costCenterOID
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getCostCenterOIDItems(Employee employee,String costCenterOID) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ String.format(ApiPath.GETCOSTCENTEROIDITEMS,costCenterOID);
        Map<String, String> datas = new HashMap<>();
        datas.put("method", "0");
        datas.put("size", "10");
        datas.put("applicantOID", employee.getUserOID());
        String res= doGet(url,getHeader(employee.getAccessToken()),datas,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 获得可用的报销单部门查询
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getformDepartment(Employee employee,String deptCode) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.EXPENSE_REPORT_SELECT_DEPARTMENT;
        Map<String, String> datas = new HashMap<>();
        datas.put("leafEnable", "false");
        datas.put("method", "0");
        datas.put("size", "10");
        datas.put("deptCode",deptCode);
        datas.put("name", "");
        String res=doGet(url,getHeader(employee.getAccessToken()),datas,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 报销单公司控件查询结果列表
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getCompanies(Employee employee) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.GET_COMPANIES;
        Map<String, String> urlform = new HashMap<>();
        urlform.put("method", "0");
        urlform.put("size", "10");
        urlform.put("enable","true");
        urlform.put("userOID",employee.getUserOID());
        String res= doGet(url,getHeader(employee.getAccessToken()),urlform,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 查询员工
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonArray queryEmployees(Employee employee) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.QUERY_EMPLOYEES;
        Map<String, String> datas = new HashMap<>();
        datas.put("sort","status,employeeId");
        datas.put("method","0");
        datas.put("size","10");
        datas.put("status", "all");
        datas.put("roleType", "TENANT");
        String res= doGet(url,getHeader(employee.getAccessToken()),datas,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 新建费用搜索城市
     * @param employee
     * @param keyWord
     * @return
     * @throws HttpStatusException
     */
    public JsonArray locationSearch( Employee employee, String keyWord) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.LOCATION_SEARCH;
        Map<String, String> urlbody = new HashMap<>();
        urlbody.put("vendorType", "standard");
        urlbody.put("language","zh_cn");
        urlbody.put("keyword",keyWord);
        urlbody.put("country","china");
        urlbody.put("size","10");
        String res=doGet(url,getHeader(employee.getAccessToken()),urlbody,employee);
        log.info("获取城市信息:{}",res);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 查询参与人员
     * @param employee
     * @param formOID
     * @return
     * @throws HttpStatusException
     */
    public  JsonArray getSelectParticipant(Employee employee,String formOID,String keyword) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.SELECT_PARTICIPANT;
        Map<String, String> urlform = new HashMap<>();
        urlform.put("proposerOID",employee.getUserOID());
        urlform.put("keyword",keyword);
        urlform.put("departmentOID",employee.getDepartmentOID());
        urlform.put("method", "0");
        urlform.put("size", "10");
        urlform.put("formOID", formOID);
        urlform.put("keywordLable",keyword);
        urlform.put("jobId",employee.getJobId());
        String res= doGet(url,getHeader(employee.getAccessToken()),urlform,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 查询员工银行卡
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getBankAccount(Employee employee) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.BANK_ACCOUNT;
        Map<String, String> urlform = new HashMap<>();
        urlform.put("method", "0");
        urlform.put("size", "10");
        urlform.put("userOID",employee.getUserOID());
        String res=doGet(url,getHeader(employee.getAccessToken()),urlform,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 查询表单默认值
     * @param employee
     * @param formOID
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getFormDefaultValues(Employee employee,String formOID) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.DEFAULT_VALUES;
        Map<String, String> urlform = new HashMap<>();
        urlform.put("formOID",formOID);
        urlform.put("userOID",employee.getUserOID());
        String res= doGet(url,getHeader(employee.getAccessToken()),urlform,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    public JsonArray queryExpenseLocation(Employee employee, String keywords) throws HttpStatusException {
        String url =employee.getEnvironment().getUrl()+ApiPath.QueryExpenseLocation;
        Map<String,String> parameters =new HashMap<>();
        parameters.put("key","174c8e2d376b5789cebbf78cd59f9e2f");
        parameters.put("keywords",keywords);
        parameters.put("type","poi");
        parameters.put("loacation","");
        parameters.put("city","");
        parameters.put("datatype","all");
        String res =doGet(url,getHeader(employee.getAccessToken()),parameters,employee);
        return new JsonParser().parse(res).getAsJsonObject().get("tips").getAsJsonArray();
    }


    /**
     * 查询城市组
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getCityGroup(Employee employee) throws HttpStatusException {
        String url =employee.getEnvironment().getUrl()+ApiPath.QueryExpenseLocation;
        Map<String,String> parameters =new HashMap<>();
        parameters.put("roleType","TENANT");
        parameters.put("page","0");
        parameters.put("size","20");
        String res =doGet(url,getHeader(employee.getAccessToken()),parameters,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 从控件上查询参与人根据fullName 获取参与人 （是否是自己）
     * @param employee
     * @param formOID
     * @param keyWord 可以是工号 姓名
     * @throws HttpStatusException
     */
    public JsonObject getParticipant(Employee employee, String formOID, String keyWord) throws HttpStatusException {
        JsonArray array = getSelectParticipant(employee,formOID,keyWord);
        log.info("查找参与人:{}",array);
        JsonObject participant = new JsonObject();
        for(int i=0; i<array.size();i++){
            participant.addProperty("userOID",array.get(i).getAsJsonObject().get("userOID").getAsString());
            participant.addProperty("fullName",array.get(i).getAsJsonObject().get("fullName").getAsString());
            participant.addProperty("participantOID",array.get(i).getAsJsonObject().get("userOID").getAsString());
            participant.addProperty("highOff", (String) null);
            participant.addProperty("avatar",(String) null);
        }
        return participant;
    }
}

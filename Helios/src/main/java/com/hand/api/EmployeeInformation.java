package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.baseMethod.Base;
import com.hand.basicconstant.ApiPath;
import com.hand.basicconstant.BaseConstant;
import com.hand.basicObject.Employee;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
public class EmployeeInformation extends Base{
    /**
     * 构造方法
     * @param Username
     * @param Password
     */
    private BaseRequest baseRequest =new BaseRequest();
    /**
     * 获得用户的信息
     * @param employee
     * @return

     * @throws HttpStatusException

     */
    public  JsonObject getAccountInfo(Employee employee) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.GET_ACCOUNT;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        String res= baseRequest.doGet(url,headersdatas,null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 获取当前账号的公司信息
     * @return
//
     * @throws HttpStatusException

     */
    public JsonArray getsetOfBooks(Employee employee,String setOfBooksCode) throws HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.GETSET_OF_BOOKS;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("setOfBooksCode", setOfBooksCode);
        datas.put("setOfBooksName", "");
        datas.put("method", "0");
        datas.put("size", "10");
        datas.put("roleType", "TENANT");
        String res= baseRequest.doGet(url,headersdatas,datas,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }


    /**
     * 获得可用的报销单部门查询
     * @return

     * @throws HttpStatusException

     */
    public JsonArray getBxformDepartment(Employee employee) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.EXPENSE_REPORT_SELECT_DEPARTMENT;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("leafEnable", "false");
        datas.put("method", "0");
        datas.put("size", "10");
        datas.put("deptCode", "");
        datas.put("name", "");
        String res= baseRequest.doGet(url,headersdatas,datas,employee);
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
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String, String> datas = new HashMap<String, String>();

        datas.put("method", "0");
        datas.put("size", "10");
        datas.put("applicantOID", employee.getUserOID());
        String res= baseRequest.doGet(url,headersdatas,datas,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }


    /**
     * 报销单创建费用，查询成本中心
     * @param employee
     * @param costCenterOID
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getExpenseCostCenterItems(Employee employee, String costCenterOID, String expenseTypeId) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.GET_EXPENSE_COST_CENTER_ITEMS;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String, String> datas = new HashMap<String, String>();

        datas.put("method", "0");
        datas.put("size", "10");
        datas.put("costCenterOID", costCenterOID);
        datas.put("expenseTypeId", expenseTypeId);
        datas.put("userOID", employee.getUserOID());
        String res= baseRequest.doGet(url,headersdatas,datas,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 获得可用的报销单列表信息
     * @param
     * @return

     * @throws HttpStatusException

     */
    public JsonArray getavailableBxforms(Employee employee, String formType) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.GETAVAILABLE_BXFORMS;
        Map<String, String> headersdatas = new HashMap<>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String, String> datas = new HashMap<>();
        datas.put("formType",formType);
        String res= baseRequest.doGet(url,headersdatas,datas,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 获得表单详情，控件信息
     * @param bxFormOID
     * @return

     * @throws HttpStatusException

     */
    public JsonObject getFormDetal(Employee employee, String bxFormOID) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ String.format(ApiPath.GETBX_FORMS,bxFormOID);
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        String res= baseRequest.doGet(url,headersdatas,null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }





    /**
     * 按照关键字查询员工
     * @param employee
     * @param keyword
     * @return
     * @throws HttpStatusException

     */
    public JsonArray queryEmployees(Employee employee,String keyword) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.QUERY_EMPLOYEES;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("sort","status,employeeId");
        datas.put("method","0");
        datas.put("size","10");
        datas.put("keyword", keyword);
        datas.put("status", "all");
        datas.put("roleType", "TENANT");
        String res= baseRequest.doGet(url,headersdatas,datas,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }


    /**
     * 报销单查询员工控件
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonArray queryEmployees(Employee employee) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.QUERY_EMPLOYEES;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String, String> datas = new HashMap<String, String>();

        datas.put("method","0");
        datas.put("size","10");
        datas.put("roleType", "TENANT");
        String res= baseRequest.doGet(url,headersdatas,datas,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }




    /**
     * 核销查询我的借款列表
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getLoanApplication(Employee employee) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.APPLICATION_LAN_MY;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");


        Map<String, String> formbody = new HashMap<String, String>();
        formbody.put("applicantOID", employee.getUserOID());
        formbody.put("currencyCode", BaseConstant.CURRENCY);
        formbody.put("companyOID", employee.getCompanyOID());
        formbody.put("method", "0");
        formbody.put("size", "100");
        formbody.put("status", "1005&status=1006");

        String res= baseRequest.doGet(url,headersdatas,formbody,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }


    /**
     * 查询表单默认值
     * @param employee
     * @param formOID
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getFormDefault_values(Employee employee,String formOID) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.DEFAULT_VALUES;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String, String> urlform = new HashMap<String, String>();
        urlform.put("formOID",formOID);
        urlform.put("userOID",employee.getUserOID());
        String res= baseRequest.doGet(url,headersdatas,urlform,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 报销单处理控件值
     * @param employee
     * @param formdetal
     * @param Cause
     * @param attachment
     * @param image
     * @return
     * @throws HttpStatusException
     */
    public  JsonArray processCustFormValues(Employee employee, JsonObject  formdetal, String Cause, JsonArray attachment, JsonArray image) throws HttpStatusException {
        JsonArray custFormValues=formdetal.get("customFormFields").getAsJsonArray();
        String formOID=formdetal.get("formOID").getAsString();
        for (int i=0;i<custFormValues.size();i++)
        {
            JsonObject data= custFormValues.get(i).getAsJsonObject();
            String messageKey=data.get("messageKey").getAsString();
            switch (messageKey)
            {
                case "select_box":
                    JsonArray fieldContentlist=new JsonParser().parse(data.get("fieldContent").getAsString()).getAsJsonArray();
                    JsonObject fieldContent=fieldContentlist.get(0).getAsJsonObject();
                    fieldContent.remove("promoptInfo");
                    data.addProperty("value",fieldContent.toString());
                    break;
                case "cust_list":
                    JsonArray customenumerationlist = getCustomenumerationoid(employee,data.get("customEnumerationOID").getAsString());
                    data.addProperty("value", customenumerationlist.get(0).getAsJsonObject().get("value").getAsString());
                    break;
                case "number":
                    data.addProperty("value",1);
                    break;
                case "currency_code":
                    data.addProperty("value",getCurrency(employee).get(0).getAsJsonObject().get("baseCurrency").getAsString());
                    break;
                case "input":
                    data.addProperty("value","text");
                    break;
                case "text_area":
                    data.addProperty("value","text");
                    break;
                case "time":
                    data.addProperty("value",getUTCDate(0));
                    break;
                case "attachment":
                    if (attachment !=null) {
                        data.addProperty("value", attachment.toString());
                    } else{
                        data.addProperty("value", new JsonArray().toString());
                        break;
                    }
                case "select_cost_center":
                    data.addProperty("value", getCostCenterOIDItems(employee,new JsonParser().parse(data.get("dataSource").getAsString()).getAsJsonObject().get("costCenterOID").getAsString()).get(0).getAsJsonObject().get("").getAsString());
                    break;
                case "select_department":
                    JsonArray  DepartmentList= getBxformDepartment(employee);
                    data.addProperty("value",DepartmentList.get(0).getAsJsonObject().get("departmentOid").getAsString());
                    break;
                case "switch":
                    data.addProperty("value",true);
                    break;
                case "common.date":
                    data.addProperty("value",getUTCDate(0));
                    break;
                case "dateTime":
                    data.addProperty("value",getUTCDate(0));
                    break;
                case "employee_expand":
                    String fieldOID=data.get("fieldOID").getAsString();
                    String Default_values=getFormDefault_values(employee,formOID).getAsString();
                    String value=JsonExtractor(Default_values,String.format("$..[?(@.fieldOID == '%s')].value",fieldOID)).get(0);
                    data.addProperty("value",value);
                    break;
                case "select_user":
                    data.addProperty("value",queryEmployees(employee).get(0).getAsJsonObject().get("userOID").getAsString());
                    break;
                case "select_company":
                    data.addProperty("value",getCompanies(employee).get(0).getAsJsonObject().get("companyOID").getAsString());
                    break;
                case "title":
                    data.addProperty("value",Cause);
                    break;
                case "city":
                    String  city=locationSearch(employee,"大").get(0).getAsJsonObject().get("city").getAsString();
                    data.addProperty("value", city);
                    break;
                case "select_participant":
                    JsonObject Participant=getSelectParticipant(employee,formOID).get(0).getAsJsonObject();
                    JsonArray ja0 = new JsonArray();
                    JsonObject myobj = new JsonObject();
                    myobj.addProperty("userOID",Participant.get("userOID").getAsString());
                    myobj.addProperty("fullName",Participant.get("fullName").getAsString());
                    myobj.addProperty("participantOID",Participant.get("userOID").getAsString());
                    ja0.add(myobj);
                    data.addProperty("value",ja0.toString());
                    break;
                case "image":
                    if (image !=null) {
                        data.addProperty("value", image.toString());
                    } else {
                        data.addProperty("value", new JsonArray().toString());
                        break;
                    }
                case "linkage_switch":
                    data.addProperty("value","true");
                    String Content=data.get("fieldContent").getAsString().replace("null","\"22222\"");
                    data.remove("fieldContent");
                    data.addProperty("fieldContent",Content);
                    break;
                case "contact_bank_account":
                    String contactBankAccountOID=getBankAccount(employee).get(0).getAsJsonObject().get("contactBankAccountOID").getAsString();
                    data.addProperty("value",contactBankAccountOID);
                    break;
                case "remark":
                    data.addProperty("value","Cause");
                    break;

            }

        }

        return  custFormValues;
    }




    /**
     * 查询员工银行卡
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getBankAccount(Employee employee) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.BANK_ACCOUNT;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String, String> urlform = new HashMap<String, String>();
        urlform.put("method", "0");
        urlform.put("size", "10");
        urlform.put("userOID",employee.getUserOID());
        String res= baseRequest.doGet(url,headersdatas,urlform,employee);
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
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String, String> urlform = new HashMap<String, String>();
        urlform.put("language", "chineseName");
        urlform.put("userOID",employee.getUserOID());
        String res= baseRequest.doGet(url,headersdatas,urlform,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }




    /**
     * 查询源泉税类别
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getSourceTax(Employee employee) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.SOURCE_TAX;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");

        Map<String, String> urlform = new HashMap<String, String>();
        urlform.put("method", "0");
        urlform.put("size", "10");
        urlform.put("enable","true");
        String res= baseRequest.doGet(url,headersdatas,urlform,employee);
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
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String, String> urlform = new HashMap<String, String>();
        urlform.put("method", "0");
        urlform.put("size", "10");
        urlform.put("enable","true");
        urlform.put("userOID",employee.getUserOID());
        String res= baseRequest.doGet(url,headersdatas,urlform,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }





    /**
     * 查询参与人员
     * @param employee
     * @param formOID
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getSelectParticipant(Employee employee,String formOID) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.SELECT_PARTICIPANT;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String, String> urlform = new HashMap<String, String>();

        urlform.put("proposerOID",employee.getUserOID());
        urlform.put("departmentOID",employee.getDepartmentOID());
        urlform.put("method", "0");
        urlform.put("size", "10");
        urlform.put("formOID", formOID);
        String res= baseRequest.doGet(url,headersdatas,urlform,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }





    /**
     * 获取值列表信息
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public  JsonArray getCustomenumerationoid( Employee employee,String customEnumerationOID) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ String.format(ApiPath.GET_CUSTOMENUMERATIONOID,customEnumerationOID);
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        String res= baseRequest.doGet(url,headersdatas,null,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }


    /**
     * 新建费用搜索城市
     * @param employee
     * @param keyWord
     * @return
     * @throws HttpStatusException
     */
    public  JsonArray locationSearch( Employee employee,String keyWord) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.LOCATION_SEARCH;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String, String> urlbody = new HashMap<String, String>();
        urlbody.put("vendorType", "Bearer");
        urlbody.put("language", employee.getLanguage());
        urlbody.put("keyWord",keyWord);

        String res= baseRequest.doGet(url,headersdatas,urlbody,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**新建费用查询会计科目
     *
     * @param employee
     * @param accountSetId
     * @return
     * @throws HttpStatusException
     */
    public  JsonArray accountsQuery( Employee employee,String accountSetId) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.ACCOUNTS_QUERY;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String, String> urlbody = new HashMap<String, String>();
        urlbody.put("method", "0");
        urlbody.put("size", "10");
        urlbody.put("accountSetId",accountSetId);
        urlbody.put("enabled","true");
        String res= baseRequest.doGet(url,headersdatas,urlbody,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 上传附件信息
     * @param employee
     * @param fileName
     * @param filepath
     * @param FileMediaType
     * @return

     * @throws HttpStatusException

     */
    public  JsonObject uploadattachment( Employee employee,Map<String, String> formbody,String fileName,String filepath,String FileMediaType) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.UPLOAD_ATTACHMENT;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        String res= baseRequest.doupload(url,headersdatas,formbody,fileName,filepath,FileMediaType,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }



    /**
     * 获取当前账号的账套信息
     * @return

     * @throws HttpStatusException

     */
    public  JsonObject Getcompanies( Employee employee) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.GETCOMPANIES;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        String res= baseRequest.doGet(url,headersdatas,null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }


    /**
     * 获得用户配置
     * @param employee
     * @return

     * @throws HttpStatusException

     */
    public  String getPersonalSettings(Employee employee, String  AccessToken) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.PERSONAL_SETTINGS;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+AccessToken+"");
        String res= baseRequest.doGet(url,headersdatas,null,employee);
        return res;
    }


    /**
     * 获得用户自己的菜单资源id
     * @param employee
     * @return

     * @throws HttpStatusException

     */
    public JsonObject userPermission(Employee employee) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.USER_PERMISSION;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");

//        Map<String, String> params = new HashMap<String, String>();
//        headersdatas.put("roleType", "TENANT ");

        String res= baseRequest.doGet(url,headersdatas,null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 用户切换语言
     * @param employee
     * @param AccessToken
     * @param Language
     * @return

     * @throws HttpStatusException

     */
    public  JsonObject switchLanguage(Employee employee, String  AccessToken, String Language) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ String.format(ApiPath.SWITCH_LANGUAGE,Language);
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+AccessToken+"");
        JsonObject body=new JsonObject();
        String res= baseRequest.doPost(url,headersdatas,null,body.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 点击差补按钮获取差补-差补开始结束日期
     * @param employee
     * @return

     * @throws HttpStatusException

     */
    public JsonObject subsidy_View(Employee employee, String expenseReportOID) throws HttpStatusException {
        String Url=employee.getEnvironment().getUrl()+ ApiPath.GET_TRAVEL_EXPENSE_TIME;
        Map<String,String> HeaderParam=new HashMap<>();
        HeaderParam.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String,String> UrlParam=new HashMap<>();
        UrlParam.put("expenseReportOID",expenseReportOID);
        String Res= baseRequest.doGet(Url,HeaderParam,UrlParam,employee);
        return new JsonParser().parse(Res).getAsJsonObject();
    }





    /**
     * 控件值列表，查询值
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getCustomEnumerations(Employee employee) throws HttpStatusException {
        String Url=employee.getEnvironment().getUrl()+ ApiPath.CUSTOM_ENUMERATIONS;
        Map<String,String> HeaderParam=new HashMap<>();
        HeaderParam.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String,String> UrlParam=new HashMap<>();
        UrlParam.put("method","0");
        UrlParam.put("size","100");
        UrlParam.put("roleType","TENANT");
        String Res= baseRequest.doGet(Url,HeaderParam,UrlParam,employee);
        return new JsonParser().parse(Res).getAsJsonArray();
    }




    /**
     * 更换差补时间
     * @param employee
     * @param expenseReportOID
     * @param startDate
     * @param endDate
     * @return
     */
    public void subsidy_update(Employee employee, String expenseReportOID,String startDate,String endDate) throws HttpStatusException {
        String Url=employee.getEnvironment().getUrl()+ApiPath.SUBSIDY_UPDATE;
        Map<String,String> HeaderParam =new HashMap<>();
        HeaderParam.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        JsonObject Jsonbody=new JsonObject();
        Jsonbody.addProperty("expenseReportOID",expenseReportOID);
        Jsonbody.addProperty("startDate",startDate);
        Jsonbody.addProperty("endDate",endDate);
        Jsonbody.addProperty("tenantId",employee.getTenantId());
        String Res= baseRequest.doPost(Url,HeaderParam,null,Jsonbody.toString(),null,employee);
    }

    /**
     *
     * @param employee
     * @param cityCode  城市代码
     * @param expenseReportOID 报销单OID
     * @param cityName  城市
     * @return
     */
    public JsonObject subsidy_Add(Employee employee,String cityCode,String expenseReportOID,String cityName) throws HttpStatusException {
        String Url=employee.getEnvironment().getUrl()+ApiPath.GET_TRAVEL_STANDARD;
        Map<String,String> HeaderParam =new HashMap<>();
        HeaderParam.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String,String> UrlParam=new HashMap<>();
        UrlParam.put("cityCode",cityCode);
        UrlParam.put("expenseReportOID",expenseReportOID);
        try {
            UrlParam.put("cityName",java.net.URLEncoder.encode(cityName,"utf-8"));
//            UrlParam.put("cityName",URLEncoder.encode(cityName, "UTF-8" ));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObject Jsonbody=new JsonObject();
        String Res= baseRequest.doPost(Url,HeaderParam,UrlParam,Jsonbody.toString(),null,employee);
        return  new JsonParser().parse(Res).getAsJsonObject();
    }

    /**
     * 保存保存获取差补金额
     * @param employee
     * @param expenseReportOID
     * @param JsonBody
     * @throws HttpStatusException
     */
    public void subsidy_Save(Employee employee, String expenseReportOID,JsonArray JsonBody) throws HttpStatusException {
        String Url=employee.getEnvironment().getUrl()+String.format(ApiPath.CALCULATION_TRAVEL_EXPENSE,expenseReportOID);
        Map<String,String> HeaderParam =new HashMap<>();
        HeaderParam.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        baseRequest.doPost(Url,HeaderParam,null,JsonBody.toString(),null,employee);
    }
    /**
     * 用于编辑费用
     * @param employee
     * @param reqBody
     * @return
     * @throws HttpStatusException
     */
    public JsonObject invoice_edit(Employee employee,JsonObject reqBody) throws HttpStatusException {
        JsonObject responseEntity=null;
        log.info("reqBody。。。。。。。。。。"+reqBody);
        String url=employee.getEnvironment().getUrl()+ ApiPath.CREATEINVOICE;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        String res= baseRequest.doPost(url,headersdatas,null,reqBody.toString(),null, employee);
        responseEntity=new JsonParser().parse(res).getAsJsonObject();
        return  responseEntity;
    }

    /**
     * 租户管理员获得公司clientSecret跟clientId
     * @param employee
     * @return
     * @throws HttpStatusException

     */
    public JsonObject queryCompanies(Employee employee) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.QUERY_COMPANY_CLIENTINFO;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("roleType", "TENANT");
        String res= baseRequest.doGet(url,headersdatas,datas,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }
}

package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.component.FormComponent;
import com.hand.basicConstant.ApiPath;
import com.hand.basicConstant.BaseConstant;
import com.hand.basicObject.component.LoanBillComponent;
import com.hand.utils.GsonUtil;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author peng.zhang
 * @Date 2020/6/8
 * @Version 1.0
 **/
@Slf4j
public class ReimbursementApi extends BaseRequest {

    private ComponentQueryApi componentQueryApi;
    public ReimbursementApi(){
        componentQueryApi =new ComponentQueryApi();
    }


    /**
     * 获取员工可用单据类型
     * @param formType 单据类型   可选102为报销单  101 申请单  104 借款单
     */
    public JsonArray getAvailableforms(Employee employee, String formType, String jobId) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.GETAVAILABLE_BXFORMS;
        Map<String, String> datas = new HashMap<>();
        datas.put("formType", formType);
        datas.put("jobId", jobId);
        String res = doGet(url, getHeader(employee.getAccessToken()), datas, employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 获取报销单的控件的详情信息
     */
    public JsonObject getFormDetail(Employee employee, String FormOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + String.format(ApiPath.GETBX_FORMS, FormOID);
        String res = doGet(url, getHeader(employee.getAccessToken()), null, employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 获取报销单详情接口
     * @param expenseReportOID
     * @return
     */
    public JsonObject getExpenseReportDetail(Employee employee, String expenseReportOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + String.format(ApiPath.GETEXPENSE_REPORT_DETAL, expenseReportOID);
        String res = doGet(url, getHeader(employee.getAccessToken()), null, employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 报销单提交请求
     * @param employee
     * @param ExpenseReportdetal 报销单详情
     * @param repayment          是否核销借款， 核销的话为true
     * @param loanApplicationOID 如果进行核销的话，则传一个loanApplicationOID  当repayment为false时可为空
     * @return
     * @throws HttpStatusException
     */
    public JsonObject expenseReportSubmit(Employee employee, JsonObject ExpenseReportdetal, boolean repayment, String loanApplicationOID,JsonArray invoiceDetail) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.EXPENSE_REPORT_SUBMIT;
        if (repayment) {
            ExpenseReportdetal.addProperty("loanApplicationOID", loanApplicationOID);
        }
        ExpenseReportdetal.add("expenseReportInvoices",new JsonParser().parse(invoiceDetail.toString()));
        String res = doPost(url, getHeader(employee.getAccessToken()), null, ExpenseReportdetal.toString(), null, employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 报销单提交等管控检查，在提交的时候必须加这个接口
     * @param employee
     * @throws HttpStatusException
     */
    public void validate(Employee employee,String expenseReportOID) throws HttpStatusException {
        String url =employee.getEnvironment().getUrl()+ApiPath.validate;
        JsonObject object =new JsonObject();
        object.addProperty("applicantOID",employee.getUserOID());
        object.addProperty("expenseReportOID",expenseReportOID);
        doPost(url, getHeader(employee.getAccessToken()), null, object.toString().toString(), null, employee);
    }

    /**
     * 弱管控提交报销单     修改：ignoreBudgetWarningFlag 值为true 提交报销单
     * @param employee
     * @param expenseReportdetal 报销单详情
     * @param repayment          是否核销借款，核销的话为true
     * @param loanApplicationOID 如果进行核销的话，则传一个loanApplicationOID  当repayment为false时可为空
     * @return
     * @throws HttpStatusException
     */
    public JsonObject enforceExpenseReportSubmit(Employee employee, JsonObject expenseReportdetal, boolean repayment, String loanApplicationOID, JsonArray invoiceDetail) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.EXPENSE_REPORT_SUBMIT;
        if (repayment) {
            expenseReportdetal.addProperty("loanApplicationOID", loanApplicationOID);
        }
        expenseReportdetal.add("expenseReportInvoices",new JsonParser().parse(invoiceDetail.toString()));
        expenseReportdetal.addProperty("ignoreBudgetWarningFlag",true);
        String res = doPost(url, getHeader(employee.getAccessToken()), null, expenseReportdetal.toString(), null, employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     *   报销单撤回请求
     * @param employee
     * @param expenseReportOID
     * @return
     * @throws HttpStatusException
     */
    public  JsonObject expenseReportWithdraw(Employee employee, String  expenseReportOID) throws HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.EXPENSE_REPORT_WITHDRAW;
        JsonObject body=new JsonObject();
        JsonObject expenseReport=new JsonObject();
        JsonArray entities=new JsonArray();
        expenseReport.addProperty("entityOID",expenseReportOID);
        expenseReport.addProperty("entityType", BaseConstant.ENTITY_TYPE[1]);
        entities.add(expenseReport);
        body.add("entities", entities);
        String res= doPost(url,getHeader(employee.getAccessToken()),null,body.toString(),null,  employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 报销单删除
     * @param employee
     * @param expenseReportOID
     * @throws HttpStatusException
     */
    public  void expenseReportDelete(Employee employee, String  expenseReportOID) throws HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ String.format(ApiPath.EXPENSE_REPORT_DELETE,expenseReportOID);
        JsonObject body=new JsonObject();
        doDlete(url,getHeader(employee.getAccessToken()),null,body, employee);
    }

    /**
     *
     * @param employee
     * @param formdetal   控件详情
     * @param costCenterItem   成本中心选择第几个从0开始
     * @param startDate   日期控件  开始日期
     * @param endDate     日期控件  结束日期
     * @param companyOID   公司oid
     * @param cityCode     城市控件code
     * @param participant   参与人
     * @param attachment  附件
     * @param image    图片
     * @param jobId
     * @param userOid
     * @return
     * @throws HttpStatusException
     */
    public  JsonObject createExpenseReport(Employee employee, JsonObject formdetal,String departmentOID,int costCenterItem,
                                           String startDate, String endDate, String companyOID,String cityCode,String participant,
                                           JsonArray attachment, JsonArray image,String jobId, String userOid) throws HttpStatusException {
        JsonObject responseEntity=null;
        JsonArray customFormFields = formdetal.get("customFormFields").getAsJsonArray();
        String url=employee.getEnvironment().getUrl()+ ApiPath.NEW_EXPENSE_REPORT;
        JsonArray  custFormValues=processCustFormValues(employee,formdetal,departmentOID,costCenterItem,startDate,
                endDate,companyOID,cityCode,participant,attachment,image);
        formdetal.remove("custFormValues");
        formdetal.remove("customFormFields");
        formdetal.add("custFormValues",custFormValues);
        formdetal.add("customFormFields",customFormFields);
        formdetal.addProperty("visibleUserScope",1001);
        formdetal.addProperty("timeZoneOffset",480);
        formdetal.addProperty("currencySame",false);
        formdetal.addProperty("applicantJobId",jobId);
        formdetal.add("countersignApproverOIDs",new JsonArray());
        formdetal.addProperty("applicantOID",userOid);
        formdetal.add("expenseReportInvoices", new JsonArray());
        formdetal.addProperty("recalculateSubsidy",true);
        String res= doPost(url,getHeader(employee.getAccessToken()),null,formdetal.toString(),null,employee);
        responseEntity=new JsonParser().parse(res).getAsJsonObject();
        return  responseEntity;
    }

    /**
     * 日常报销单创建
     * @param employee
     * @param formdetal   控件详情
     * @param jobId    岗位id
     * @param userOID
     * @return
     * @throws HttpStatusException
     */
    public  JsonObject createExpenseReport(Employee employee, JsonObject formdetal,FormComponent component, String jobId, String userOID) throws HttpStatusException {
        JsonObject responseEntity=null;
        String url = employee.getEnvironment().getUrl()+ ApiPath.NEW_EXPENSE_REPORT;
        JsonArray  custFormValues = processCustFormValues(employee,formdetal,component);
        formdetal.add("custFormValues",custFormValues);
        formdetal.addProperty("visibleUserScope",1001);
        formdetal.addProperty("timeZoneOffset",480);
        formdetal.addProperty("currencySame",false);
        formdetal.addProperty("applicantJobId",jobId);
        formdetal.add("countersignApproverOIDs",new JsonArray());
        formdetal.addProperty("applicantOID",userOID);
        formdetal.add("expenseReportInvoices", new JsonArray());
        formdetal.addProperty("recalculateSubsidy",true);
        formdetal.addProperty("isDateCombinedUTC",false);
        String res= doPost(url,getHeader(employee.getAccessToken()),null,formdetal.toString(),null,employee);
        responseEntity=new JsonParser().parse(res).getAsJsonObject();
        return  responseEntity;
    }

    /**
     * 差旅报销单创建
     * @param employee
     * @param isMoreApplication 是否多关联多申请
     * @param formdetal   控件详情
     * @return
     * @throws HttpStatusException
     */
    public  JsonObject createTravelExpenseReport(Employee employee, boolean isMoreApplication, JsonObject formdetal, FormComponent component) throws HttpStatusException {
        JsonObject responseEntity=null;
        JsonArray  custFormValues;
        JsonArray customFormFields = formdetal.get("customFormFields").getAsJsonArray();
        String url = employee.getEnvironment().getUrl()+ ApiPath.NEW_EXPENSE_REPORT;
        if(component==null){
            custFormValues = processCustFormValues(employee,customFormFields);
        }else{
            custFormValues = processCustFormValues(employee,formdetal,component);
        }
        formdetal.remove("custFormValues");
        formdetal.remove("customFormFields");
        formdetal.add("custFormValues",custFormValues);
        formdetal.add("customFormFields",customFormFields);
        formdetal.addProperty("visibleUserScope",1001);
        formdetal.addProperty("timeZoneOffset",480);
        formdetal.addProperty("currencySame",false);
        formdetal.addProperty("applicantJobId",employee.getJobId());
        formdetal.add("countersignApproverOIDs",new JsonArray());
        formdetal.addProperty("applicantOID",employee.getUserOID());
        formdetal.addProperty("associateExpenseReport",true);
        formdetal.addProperty("applicationOID",component.getApplicationOID());
        if(isMoreApplication){
            formdetal.add("expenseReportApplicationDTOS",component.getExpenseReportApplicationDTOS());
        }
        formdetal.add("expenseReportInvoices", new JsonArray());
        formdetal.addProperty("recalculateSubsidy",true);
        String res= doPost(url,getHeader(employee.getAccessToken()),null,formdetal.toString(),null,employee);
        responseEntity=new JsonParser().parse(res).getAsJsonObject();
        return  responseEntity;
    }

    /**
     * 差旅报销单创建  ---控件信息由申请单带出
     * @param employee
     * @param applicationOID  需要关联的申请单的OID
     * @param formdetal   控件详情
     * @param customFormValues  申请单带出的控件信息
     * @return
     * @throws HttpStatusException
     */
    public  JsonObject createTravelExpenseReport(Employee employee, String applicationOID,JsonObject formdetal,JsonArray customFormValues) throws HttpStatusException {
        JsonObject responseEntity=null;
        JsonArray customFormFields = formdetal.get("customFormFields").getAsJsonArray();
        String url = employee.getEnvironment().getUrl()+ ApiPath.NEW_EXPENSE_REPORT;
        formdetal.remove("custFormValues");
        formdetal.remove("customFormFields");
        formdetal.add("custFormValues",customFormValues);
        formdetal.add("customFormFields",customFormFields);
        formdetal.addProperty("visibleUserScope",1001);
        formdetal.addProperty("timeZoneOffset",480);
        formdetal.addProperty("currencySame",false);
        formdetal.addProperty("applicantJobId",employee.getJobId());
        formdetal.add("countersignApproverOIDs",new JsonArray());
        formdetal.addProperty("applicantOID",employee.getUserOID());
        formdetal.addProperty("associateExpenseReport",true);
        formdetal.addProperty("applicationOID",applicationOID);
        formdetal.add("expenseReportInvoices", new JsonArray());
        formdetal.addProperty("recalculateSubsidy",true);
        String res= doPost(url,getHeader(employee.getAccessToken()),null,formdetal.toString(),null,employee);
        responseEntity=new JsonParser().parse(res).getAsJsonObject();
        return  responseEntity;
    }

    /**
     * 获取表单默认带出的值
     * @param employee
     * @param formOID
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getFormDefaultValues(Employee employee,String formOID,String jobID) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.DEFAULT_VALUES;
        Map<String, String> urlform = new HashMap<>();
        urlform.put("formOID",formOID);
        urlform.put("userOID",employee.getUserOID());
        urlform.put("jobId",jobID);
        String res= doGet(url,getHeader(employee.getAccessToken()),urlform,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 报销单处理控件值
     * @param employee
     * @param formdetal
     * @param attachment  附件
     * @param startDate  日期控件 开始日期
     * @param endDate 结束日期
     * @param companyOID  公司的OID
     * @param cityCode  城市code
     * @param costCenterItemOID 成本中心的oid
     * @param participant 参与人信息  例如字符串：[{"userOID":"52142c99-df10-4de4-adbf-19cd39f8b279","fullName":"懿佳欢_stage","participantOID":"52142c99-df10-4de4-adbf-19cd39f8b279","avatar":null}”
     * @param image
     * @return
     * @throws HttpStatusException
     */
    private JsonArray processCustFormValues(Employee employee, JsonObject  formdetal, String departmentOID,int costCenterItemOID,String startDate,
                                            String endDate, String companyOID,String cityCode,String participant,JsonArray attachment, JsonArray image) throws HttpStatusException {
        JsonArray custFormValues=formdetal.get("customFormFields").getAsJsonArray();
        String formOID=formdetal.get("formOID").getAsString();
        for (int i=0;i<custFormValues.size();i++)
        {
            JsonObject data= custFormValues.get(i).getAsJsonObject();
            String fieldName=data.get("fieldName").getAsString();
            switch (fieldName)
            {
                case "选择框":            //选择框
                    JsonArray fieldContentlist=new JsonParser().parse(data.get("fieldContent").getAsString()).getAsJsonArray();
                    JsonObject fieldContent=fieldContentlist.get(0).getAsJsonObject();
                    fieldContent.remove("promoptInfo");
                    data.addProperty("value",fieldContent.toString());
                    break;
                case "自定义列表":          //自定义列表
                    JsonArray customenumerationlist = componentQueryApi.getCustomEumerationOid(employee,data.get("customEnumerationOID").getAsString());
                    data.addProperty("value", customenumerationlist.get(0).getAsJsonObject().get("value").getAsString());
                    break;
                case "数字":      //数字
                    data.addProperty("value",1);
                    break;
                case "币种":
                    data.addProperty("value", componentQueryApi.getCurrency(employee).get(0).getAsJsonObject().get("baseCurrency").getAsString());
                    break;
                case "单行输入框":     //输入框
                    data.addProperty("value","text");
                    break;
                case "多行输入框":      //多行输入框
                    data.addProperty("value","text");
                    break;
                case "时间":           // 时间
                    data.addProperty("value",UTCTime.getNowUtcTime());
                    break;
                case "附件":         //  附件
                    if (attachment !=null) {
                        data.addProperty("value", attachment.toString());
                    } else{
                        data.addProperty("value", new JsonArray().toString());
                        break;
                    }
                case "成本中心":        //选择成本中心
                    data.addProperty("value", componentQueryApi.getCostCenterOIDItems(employee,new JsonParser().parse(data.get("dataSource").getAsString()).getAsJsonObject().
                            get("costCenterOID").getAsString()).get(costCenterItemOID).getAsJsonObject().get("costCenterItemOID").getAsString());
//                    data.addProperty("value",costCenterItemOID);
                    break;
                case "部门":             //选择部门
//                    JsonArray  DepartmentList= componentQueryApi.getBxformDepartment(employee);
//                    data.addProperty("value",DepartmentList.get(0).getAsJsonObject().get("departmentOid").getAsString());
                    data.addProperty("value",departmentOID);
                    break;
                case "普通开关":       //普通开关
                    data.addProperty("value",true);
                    break;
                case "结束日期":     // 结束日期

                    data.addProperty("value",endDate);
                case "开始日期":      //开始日期
                    data.addProperty("value",startDate);
                case "日期":             //日期
                    data.addProperty("value", UTCTime.getNowUtcTime());
                    break;
                case "日期时间":            //日期时间
                    data.addProperty("value",UTCTime.getNowUtcTime());
                    break;
                case "个人信息扩展字段":       //个人信息扩展字段
                    String fieldOID=data.get("fieldOID").getAsString();
                    String Default_values=getFormDefaultValues(employee,formOID,employee.getJobId()).getAsString();
                    String value= GsonUtil.JsonExtractor(Default_values,String.format("$..[?(@.fieldOID == '%s')].value",fieldOID)).get(0);
                    data.addProperty("value",value);
                    break;
                case "选人":               //选人
                    data.addProperty("value", componentQueryApi.queryEmployees(employee).get(0).getAsJsonObject().get("userOID").getAsString());
                    break;
                case "公司":                //选择公司
//                    data.addProperty("value",componentQueryApi.getCompanies(employee).get(0).getAsJsonObject().get("companyOID").getAsString());
                    data.addProperty("value",companyOID);
                    break;
                case "事由":                      //事由
                    data.addProperty("value","自动化测试");
                    break;
                case "城市":               //城市
//                    String  city=componentQueryApi.locationSearch(employee,"西安").get(0).getAsJsonObject().get("city").getAsString();
                    data.addProperty("value", cityCode);
                    break;
                case "参与人":                   //参与人
//                    JsonObject Participant=componentQueryApi.getSelectParticipant(employee,formOID).get(0).getAsJsonObject();
//                    JsonArray ja0 = new JsonArray();
//                    JsonObject myobj = new JsonObject();
//                    myobj.addProperty("userOID",Participant.get("userOID").getAsString());
//                    myobj.addProperty("fullName",Participant.get("fullName").getAsString());
//                    myobj.addProperty("participantOID",Participant.get("userOID").getAsString());
//                    ja0.add(myobj);
                    data.addProperty("value",participant);
                    break;
                case "图片":      //图片
                    if (image !=null) {
                        data.addProperty("value", image.toString());
                    } else {
                        data.addProperty("value", new JsonArray().toString());
                        break;
                    }
                case "联动开关":       //联动开关
                    data.addProperty("value","true");
                    String Content=data.get("fieldContent").getAsString().replace("null","\"22222\"");
                    data.remove("fieldContent");
                    data.addProperty("fieldContent",Content);
                    break;
                case "银行卡号":          //银行卡号
                    String contactBankAccountOID= componentQueryApi.getBankAccount(employee).get(0).getAsJsonObject().get("contactBankAccountOID").getAsString();
                    data.addProperty("value",contactBankAccountOID);
                    break;
                case "备注":              //备注
                    data.addProperty("value","Cause");
                    break;
            }
        }
        return  custFormValues;
    }

    /**
     * 报销单处理控件值
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonArray processCustFormValues(Employee employee, JsonObject formdetal, FormComponent component) throws HttpStatusException {
        JsonArray custFormValues=formdetal.get("customFormFields").getAsJsonArray();
        String formOID=formdetal.get("formOID").getAsString();
        for (int i=0;i<custFormValues.size();i++)
        {
            JsonObject data= custFormValues.get(i).getAsJsonObject();
            String fieldName = data.get("fieldName").getAsString();
            switch (fieldName)
            {
                case "选择框":            //选择框
                    JsonArray fieldContentlist=new JsonParser().parse(data.get("fieldContent").getAsString()).getAsJsonArray();
                    JsonObject fieldContent=fieldContentlist.get(0).getAsJsonObject();
                    fieldContent.remove("promoptInfo");
                    data.addProperty("value",fieldContent.toString());
                    break;
                case "自定义列表":          //自定义列表
//                    JsonArray customenumerationlist = componentQueryApi.getCustomEumerationOid(employee,data.get("customEnumerationOID").getAsString());
//                    data.addProperty("value", customenumerationlist.get(0).getAsJsonObject().get("value").getAsString());
                    data.addProperty("value",component.getCustList());
                    break;
                case "数字":      //数字
                    data.addProperty("value",1);
                    break;
                case "币种":      //币种
                    if(component.getCurrencyCode()==null){
                        data.addProperty("value","CNY");
                    }else{
                        data.addProperty("value",component.getCurrencyCode());
                    }
                    break;
                case "单行输入框":     //输入框
                    data.addProperty("value","text");
                    break;
                case "多行输入框":      //多行输入框
                    data.addProperty("value","text");
                    break;
                case "时间":           // 时间
                    data.addProperty("value",UTCTime.getNowUtcTime());
                    break;
                case "附件":         //  附件
                        data.addProperty("value", component.getAttachment().toString());
                        break;
                case "成本中心":        //选择成本中心
                    String costCenterOID = new JsonParser().parse(data.get("dataSource").getAsString()).getAsJsonObject().get("costCenterOID").getAsString();
                    JsonArray costCenter = componentQueryApi.getCostCenterOIDItems(employee,costCenterOID);
                    String costCenterItemOID = "";
                    if(GsonUtil.isNotEmpt(costCenter)){
                        costCenterItemOID =GsonUtil.getJsonValue(costCenter,"name",component.getCostCenter(),"costCenterItemOID");
                    }
                    data.addProperty("value",costCenterItemOID);
                    break;
                case "级联成本中心":
                    data.addProperty("value",component.getAssociatCostCenter());
                    break;
                case "下级成本中心":
                    data.addProperty("value",component.getSubordinateCostCenter());
                    break;
                case "部门":             //选择部门
//                    JsonArray  DepartmentList= componentQueryApi.getBxformDepartment(employee);
//                    data.addProperty("value",DepartmentList.get(0).getAsJsonObject().get("departmentOid").getAsString());
                    data.addProperty("value",component.getDepartment());
                    break;
                case "普通开关":       //普通开关
                    data.addProperty("value",true);
                    break;
                case "结束日期":     // 结束日期
                    data.addProperty("value",component.getEndDate());
                    data.addProperty("startDate",component.getStartDate());
                    break;
                case "开始日期":      //开始日期
                    data.addProperty("value",component.getStartDate());
                    data.addProperty("endDate",component.getEndDate());
                    break;
                case "日期":             //日期
                    data.addProperty("value", UTCTime.getNowUtcTime());
                    break;
                case "日期时间":            //时间
                    data.addProperty("value",UTCTime.getNowUtcTime());
                    break;
//                case "employee_expand":       //个人信息扩展字段
//                    String fieldOID=data.get("fieldOID").getAsString();
//                    String Default_values=getFormDefaultValues(employee,formOID,employee.getJobId()).getAsString();
//                    String value= GsonUtil.JsonExtractor(Default_values,String.format("$..[?(@.fieldOID == '%s')].value",fieldOID)).get(0);
//                    data.addProperty("value",value);
//                    break;
                case "选人":               //选人
                    data.addProperty("value", componentQueryApi.queryEmployees(employee).get(0).getAsJsonObject().get("userOID").getAsString());
                    break;
                case "公司":                //选择公司
//                    data.addProperty("value",componentQueryApi.getCompanies(employee).get(0).getAsJsonObject().get("companyOID").getAsString());
                    data.addProperty("value",component.getCompany());
                    break;
                case "事由":                      //事由
                    data.addProperty("value",component.getCause());
                    break;
                case "城市":               //城市
//                    String  city=componentQueryApi.locationSearch(employee,"西安").get(0).getAsJsonObject().get("city").getAsString();
                    data.addProperty("value", component.getCity());
                    break;
                case "参与人员":
                    if(component.getParticipant()!=null){
                        JsonArray array = new JsonArray();
                        if(component.getParticipant().getClass().equals(String [].class)){
                            String [] participant = (String [])component.getParticipant();
                            for(int j=0;j<participant.length;j++){
                                array.add(componentQueryApi.getParticipant(employee,formOID,participant[j]));
                            }
                            data.addProperty("value",array.toString());
                        }
                        if(component.getParticipant().getClass().equals(String.class)){
                            String participant = (String) component.getParticipant();
                            data.addProperty("value",participant);
                        }
                    }
                    break;
                case "图片":      //图片
                        data.addProperty("value", component.getImage().toString());
                        break;
                case "联动开关":       //联动开关
                    data.addProperty("value","true");
                    String Content=data.get("fieldContent").getAsString().replace("null","\"22222\"");
                    data.remove("fieldContent");
                    data.addProperty("fieldContent",Content);
                    break;
                case "银行卡号":          //银行卡号
//                    String contactBankAccountOID=componentQueryApi.getBankAccount(employee).get(0).getAsJsonObject().get("contactBankAccountOID").getAsString();
                    data.addProperty("value",component.getBankAccount());
                    break;
                case "备注":              //备注
                    data.addProperty("value","Cause");
                    break;
                case "借款金额" :
                    data.addProperty("value",component.getTotalBudget());
                    break;
                case "预计还款日期":
                    data.addProperty("value",component.getRebackDate());
            }
        }
        return  custFormValues;
    }



    /**
     * 获取报销单的审批历史
     * @param employee
     * @param expenseReportOID
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getApproveHistory(Employee employee, String expenseReportOID) throws HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.APPROVE_HISTORY;
        Map<String, String> urlform = new HashMap<>();
        urlform.put("expenseReportOID",expenseReportOID);
        String res= doGet(url,getHeader(employee.getAccessToken()),urlform,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 报销单费用导入查询费用
     * @param employee
     * @param expenseReportOID
     * @param isbook  这个参数用于区分是账本费用还是 结算费用   账本费用传""  结算费用为："publicBook"
     * @return
     * @throws HttpStatusException
     */
    public JsonArray searchAvailableImport(Employee employee,String expenseReportOID,boolean isbook) throws HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ApiPath.saerchAvailableImport;
        JsonObject jsonObject =new JsonObject();
        jsonObject.addProperty("expenseReportOID",expenseReportOID);
        if(isbook){
            jsonObject.addProperty("importFrom","");
        }else{
            jsonObject.addProperty("importFrom","publicBook");
        }
        String Res= doPost(url,getHeader(employee.getAccessToken()),null,jsonObject.toString(),null,employee);
        return new JsonParser().parse(Res).getAsJsonObject().get("rows").getAsJsonArray();
    }

    /**
     * 报销单从账本导入费用
     * @param employee
     * @param expenseReportOID   报销单的oid
     * @param invoiceOIDs   invoiceOID  可以批量导入  new ArrayList就行
     * @return
     * @throws HttpStatusException
     */
    public String importInvoices(Employee employee, String expenseReportOID, ArrayList<String> invoiceOIDs, boolean isbook) throws HttpStatusException {
        String url =employee.getEnvironment().getUrl()+ApiPath.IMPORT_INVOICES;
        JsonObject object  =new JsonObject();
        JsonArray jsonArray =new JsonArray();
        object.addProperty("expenseReportOID",expenseReportOID);
        if(!isbook){
            object.addProperty("importFrom","publicBook");
        }
        //如果存在多个费用的话  就遍历导入
        for (String invoiceOID: invoiceOIDs
        ) {
            jsonArray.add(invoiceOID);
        }
        object.add("invoiceOIDs",jsonArray);
        String res= doPost(url,getHeader(employee.getAccessToken()),null,object.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject().get("success").getAsString();
    }

    /**
     * 报销单内费用删除退回账本
     * @param employee
     * @param expenseReportOID
     * @param invoiceOID
     * @throws HttpStatusException
     */
    public void removeExpense(Employee employee, String expenseReportOID,String invoiceOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+String.format(ApiPath.removeExpense,expenseReportOID,invoiceOID);
        JsonObject  object =new JsonObject();
        doDlete(url,getHeader(employee.getAccessToken()),null,object,employee);
    }

    /**
     * 报销单关联申请单-查询申请单
     * @param employee
     * @param formOID
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getApplication(Employee employee,String formOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.SEARCH_APPLICATION;
        Map<String, String> urlform = new HashMap<>();
        urlform.put("page","0");
        urlform.put("size","10");
        urlform.put("formOID",formOID);
        urlform.put("userOID",employee.getUserOID());
        String res= doGet(url,getHeader(employee.getAccessToken()),urlform,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 报销单默认带出申请单带入的默认value
     * @param employee
     * @param applicationOID
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getValueFromApplication(Employee employee,ArrayList applicationOID) throws HttpStatusException {
        String url =employee.getEnvironment().getUrl()+ApiPath.APPLICATION_VALUE;
        Map<String, String> urlform = new HashMap<>();
        urlform.put("jobId",employee.getJobId());
        JsonArray array =new JsonArray();
        for (Object anApplicationOID : applicationOID) {
            array.add(anApplicationOID.toString());
        }
        String res= doPost(url,getHeader(employee.getAccessToken()),urlform,array.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 制造数据   根据需求添加控件
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    JsonArray processCustFormValues(Employee employee,JsonArray custFormValues) throws HttpStatusException {
//        JsonArray custFormValues = formdetal.get("customFormFields").getAsJsonArray();
        for (int i = 0; i < custFormValues.size(); i++) {
            JsonObject data = custFormValues.get(i).getAsJsonObject();
            String fieldName = data.get("fieldName").getAsString();
            switch (fieldName){
                case "事由":
                    String [] casue ={"事由1","事由2","事由3"};
                    int number =RandomNumber.getRandomNumber(0,2);
                    data.addProperty("value",casue[number]);
                    break;
                case "部门":
                    data.addProperty("value",employee.getDepartmentOID());
                    break;
                case "参与人员":
                    //添加参与人员  参与人员的value 是一段json数组。 默认选择自己
                    JsonArray array = new JsonArray();
                    JsonObject participant =new JsonObject();
                    participant.addProperty("userOID",employee.getUserOID());
                    participant.addProperty("fullName",employee.getFullName());
                    participant.addProperty("participantOID",employee.getUserOID());
                    participant.addProperty("highOff", (String) null);
                    participant.addProperty("avatar",(String) null);
                    array.add(participant);
                    data.addProperty("value",array.toString());
                    break;
                case "结束日期":     // 结束日期
                    data.addProperty("value",UTCTime.getUtcTime(5,0));
                    data.addProperty("startDate",UTCTime.getNowUtcTime());
                    break;
                case "开始日期":      //开始日期
                    data.addProperty("value",UTCTime.getNowUtcTime());
                    data.addProperty("endDate",UTCTime.getUtcTime(5,0));
                    break;
            }
        }
        return custFormValues;
    }

    /**
     * 造数据    日常报销单创建
     * @param employee
     * @param formdetal   控件详情
     * @param jobId    岗位id
     * @param userOID
     * @return
     * @throws HttpStatusException
     */
    public  JsonObject createExpenseReport(Employee employee, JsonObject formdetal, String jobId, String userOID) throws HttpStatusException {
        JsonObject responseEntity=null;
        JsonArray customFormFields = formdetal.get("customFormFields").getAsJsonArray();
        String url = employee.getEnvironment().getUrl()+ ApiPath.NEW_EXPENSE_REPORT;
        JsonArray  custFormValues=processCustFormValues(employee,customFormFields);
        formdetal.remove("custFormValues");
        formdetal.add("custFormValues",custFormValues);
        formdetal.addProperty("visibleUserScope",1001);
        formdetal.addProperty("timeZoneOffset",480);
        formdetal.addProperty("currencySame",false);
        formdetal.addProperty("applicantJobId",jobId);
        formdetal.add("countersignApproverOIDs",new JsonArray());
        formdetal.addProperty("applicantOID",userOID);
        formdetal.add("expenseReportInvoices", new JsonArray());
        formdetal.addProperty("recalculateSubsidy",true);
        String res= doPost(url,getHeader(employee.getAccessToken()),null,formdetal.toString(),null,employee);
        responseEntity=new JsonParser().parse(res).getAsJsonObject();
        return  responseEntity;
    }

    /**
     * 借款单提交
     * @param employee
     */
    public JsonObject submitLoanBill(Employee employee,JsonObject loanReportDetail,JsonArray customFormValue) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.SUBMIT_LOAN_BILL;
        loanReportDetail.add("customFormValues",customFormValue);
        loanReportDetail.remove("customFormProperties");
        loanReportDetail.addProperty("referenceApplicationOID","");
        loanReportDetail.addProperty("jobId",employee.getJobId());
        loanReportDetail.add("countersignApproverOIDs",new JsonArray());
        loanReportDetail.addProperty("applicantOID",employee.getUserOID());
        loanReportDetail.addProperty("type",2005);
        String res= doPost(url,getHeader(employee.getAccessToken()),null,loanReportDetail.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 创建借款单
     * @param employee
     * @param loanReportDetail
     * @param component
     * @return
     * @throws HttpStatusException
     */
    public JsonObject createLoanBill(Employee employee,JsonObject loanReportDetail,FormComponent component) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ApiPath.DRAFT_LOAN;
        JsonArray customFormValues = processCustFormValues(employee,loanReportDetail,component);
        loanReportDetail.add("customFormValues",customFormValues);
        loanReportDetail.remove("customFormProperties");
        loanReportDetail.addProperty("referenceApplicationOID","");
        loanReportDetail.addProperty("jobId",employee.getJobId());
        loanReportDetail.add("countersignApproverOIDs",new JsonArray());
        loanReportDetail.addProperty("applicantOID",employee.getUserOID());
        loanReportDetail.addProperty("type",2005);
        String res = doPost(url,getHeader(employee.getAccessToken()),null,loanReportDetail.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 查询借款类型
     * @param employee
     * @param formOID
     */
    public JsonObject getLoanType(Employee employee,String formOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+String.format(ApiPath.LOAN_TYPE,formOID);
        HashMap<String,String> map = new HashMap<>();
        map.put("setOfBooksId",employee.getSetOfBookId());
        map.put("applicantOID",employee.getUserOID());
        String res = doGet(url,getHeader(employee.getAccessToken()),map,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 借款获取银行信息
     * @param employee
     * @return
     */
    public JsonArray getBankInfo(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.GET_BANK;
        HashMap<String,String> map = new HashMap<>();
        map.put("userOID",employee.getUserOID());
        String res = doGet(url,getHeader(employee.getAccessToken()),map,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 获取借款单的详情
     * @param employee
     * @param loanBillOID
     * @return
     * @throws HttpStatusException
     */
    public JsonObject loanBillDetail(Employee employee,String loanBillOID) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + String.format(ApiPath.LOAN_BILL_DETAIL,loanBillOID);
        String res = doGet(url,getHeader(employee.getAccessToken()),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 新建借款行
     * @param employee
     * @param component
     * @return
     * @throws HttpStatusException
     */
    public JsonObject createLanLine(Employee employee, LoanBillComponent component) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.LOAN_LINE;
        JsonObject body = new JsonParser().parse(GsonUtil.objectToString(component)).getAsJsonObject();
        String res = doPost(url,getHeader(employee.getAccessToken()),null,body.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }
}

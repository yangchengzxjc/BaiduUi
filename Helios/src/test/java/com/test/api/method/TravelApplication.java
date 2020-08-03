package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.ApplicationApi;
import com.hand.api.ExpenseApi;
import com.hand.api.ReimbursementApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.itinerary.FlightItinerary;
import com.hand.basicObject.FormComponent;
import com.hand.basicObject.itinerary.HotelItinerary;
import com.hand.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2020/7/6
 * @Version 1.0
 **/
@Slf4j
public class TravelApplication {

    private ApplicationApi applicationApi;
    private ReimbursementApi reimbursementApi;
    private ExpenseApi expenseApi;

    public TravelApplication(){
        applicationApi = new ApplicationApi();
        reimbursementApi =new ReimbursementApi();
        expenseApi =new ExpenseApi();
    }

    /**
     * 返回差旅申请单的表单的formOID
     * @param employee
     * @param formName
     * @return
     * @throws HttpStatusException
     */
    public String applicationFormOID(Employee employee,String formName) throws HttpStatusException {
        JsonArray array = applicationApi.getAvailableform(employee,employee.getJobId());
        return GsonUtil.getJsonValue(array,"formName",formName,"formOID");
    }

    /**
     *创建（保存）差旅申请单
     * @param employee
     * @param formName
     * @param component
     * @throws HttpStatusException
     */
    public HashMap<String,String> createTravelApplication(Employee employee, String formName, FormComponent component) throws HttpStatusException {
        JsonObject formDetail = reimbursementApi.getFormDetail(employee,applicationFormOID(employee,formName));
        log.info("差旅表单的详情：{}",formDetail);
        JsonObject jsonObject = applicationApi.createApplication(employee,formDetail,component,employee.getJobId(),employee.getUserOID());
        HashMap<String,String> info = new HashMap<>();
        info.put("applicationOID",jsonObject.get("applicationOID").getAsString());
        info.put("businessCode",jsonObject.get("businessCode").getAsString());
        log.info("businessCode:{}",info.get("businessCode"));
        return info;
    }

    /**
     *预算费用类型详情  此方法是单个差旅预算费用 生成之后要添加到一个JsonArray 中去
     * 需要俩个的话 就添加俩个
     * @param employee
     * @param amount
     * @param isCompanyPay
     * @param expenseName
     * @param formName
     * @return
     * @throws HttpStatusException
     */
    public JsonObject addBudgetExpenseType(Employee employee,double amount, boolean isCompanyPay,String expenseName,String formName) throws HttpStatusException {
        JsonArray array = applicationApi.getBudgetExpenseType(employee,applicationFormOID(employee,formName));
        JsonObject object =new JsonObject();
        JsonObject expenseType =new JsonObject();
        JsonObject budget = new JsonObject();
        for(int i=0;i<array.size();i++){
            if(array.get(i).getAsJsonObject().get("name").getAsString().equals(expenseName)){
                object=array.get(i).getAsJsonObject();
            }
        }
        budget.addProperty("iconName",object.get("iconName").getAsString());
        budget.addProperty("name",expenseName);
        budget.addProperty("iconURL",object.get("iconURL").getAsString());
        budget.addProperty("expenseTypeOID",object.get("expenseTypeOID").getAsString());
        budget.addProperty("apportionEnabled",false);
        if(isCompanyPay){
            budget.addProperty("paymentType",1002);
        }else{
            budget.addProperty("paymentType",1001);
        }
        budget.addProperty("actualCurrencyRate",1);
        budget.addProperty("actualCurrencyRate",1);
        budget.addProperty("currencyCode","CNY");
        budget.addProperty("amount",amount);
        budget.addProperty("baseCurrencyAmount",amount);
        budget.addProperty("rateProhibit",false);
        budget.addProperty("rateWarning",false);
        expenseType.addProperty("iconName",object.get("iconName").getAsString());
        expenseType.addProperty("name",expenseName);
        expenseType.addProperty("iconURL",object.get("iconURL").getAsString());
        expenseType.addProperty("expenseTypeOID",object.get("expenseTypeOID").getAsString());
        expenseType.addProperty("apportionEnabled",false);
        budget.add("expenseType",expenseType);
        return budget;
    }

    /**
     * 此方法是获取到申请单中所有的预算费用
     * @param employee
     * @param budgetDetail
     * @param allAmount  所有的预算费用的的金额
     */
    public String getBudgetDetail(Employee employee,JsonArray budgetDetail,double allAmount){
        JsonObject object = new JsonObject();
        object.add("budgetDetail",budgetDetail);
        object.addProperty("amount",allAmount);
        return object.toString();
    }

    /**
     * 申请单提交需要添加预算费用。有俩种方式1.在创建的时时直接给customformValue 的预算控件的value 把预算费用信息塞进去
     * 2.在提交前：获取到申请单详情 然后给详情中的customformValue的预算控件的value假如预算费用。
     * 这个方法是在获取详情后在添加预算费用
     * @param employee
     * @param applicationOID
     */
    public JsonObject submitApplication(Employee employee,String applicationOID,String budgetDetail) throws HttpStatusException {
        JsonObject applicationDetail = applicationApi.getApplicationDetail(employee,applicationOID);
        JsonArray custFormValues = applicationDetail.get("custFormValues").getAsJsonArray();
        //找到那个预算明细的控件 给他塞入value;
        for(int i=0; i<custFormValues.size();i++){
            if(custFormValues.get(i).getAsJsonObject().get("fieldName").getAsString().equals("预算明细")){
                custFormValues.get(i).getAsJsonObject().addProperty("value",budgetDetail);
            }
        }
        //把新的custFormValues加到详情中去
        applicationDetail.add("custFormValues",custFormValues);
        log.info("申请单的详情:{}",applicationDetail);
        return applicationApi.submitApplication(employee,applicationDetail);
    }

    /**
     * 申请单添加行程
     * @param employee
     * @param applicationOID
     * @param t  使用泛型  根据实际类型来判断
     * @throws HttpStatusException
     */
    public <E> void addItinerary(Employee employee, String applicationOID, ArrayList<E> t) throws HttpStatusException {
        if(t.size()==0){
            log.info("请添加至少一个行程");
        }else if(t.get(0).getClass().equals(FlightItinerary.class)){
            applicationApi.addFlightItinerary(employee,applicationOID,t);
        }else if(t.get(0).getClass().equals(HotelItinerary.class)){
            applicationApi.addHotelItinerary(employee,applicationOID,t);
        }
    }


}

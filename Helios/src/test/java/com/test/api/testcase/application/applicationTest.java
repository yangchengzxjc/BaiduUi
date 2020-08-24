package com.test.api.testcase.application;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.itinerary.FlightItinerary;
import com.hand.basicObject.FormComponent;
import com.hand.basicconstant.SupplierOID;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.ApplicationMethod.TravelApplicationPage;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.TravelApplication;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * @Author peng.zhang
 * @Date 2020/7/7
 * @Version 1.0
 **/
@Slf4j
public class applicationTest extends BaseTest {

    private Employee employee;
    private TravelApplication travelApplication;
    private ExpenseReportComponent expenseReportComponent;
    private ExpenseReport expenseReport;
    private TravelApplicationPage travelApplicationPage;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        travelApplication =new TravelApplication();
        expenseReportComponent = new ExpenseReportComponent();
        expenseReport =new ExpenseReport();
        travelApplicationPage =new TravelApplicationPage();
    }

    @Test(description = "新建差旅申请单")
    public void createApplicationTest01() throws HttpStatusException {
        FormComponent component = new FormComponent("自动化测试新建差旅申请单");
        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(UTCTime.getNowUtcTime());
        component.setEndDate(UTCTime.getUtcTime(3,0));
        //添加参与人员  参与人员的value 是一段json数组。
        JsonArray array = new JsonArray();
        array.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"差旅申请单-节假日"),"懿佳欢_stage"));
        component.setParticipant(array.toString());
        //创建申请单
        String applicationOID = travelApplication.createTravelApplication(employee,"差旅申请单-节假日",component).get("applicationOID");
        //添加差旅行程(目前支持飞机行程和酒店行程)
        ArrayList<FlightItinerary> flightItineraries =new ArrayList<>();
        FlightItinerary flightItinerary=travelApplicationPage.addFlightItinerary(employee,1001,SupplierOID.CTRIP_AIR,"西安市","北京",null,UTCTime.getNowStartUtcDate());
        flightItineraries.add(flightItinerary);
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        //申请单添加预算
        JsonObject expenseObject = travelApplication.addBudgetExpense(employee,1000.00,false,"机票","差旅申请单-节假日");
        JsonArray expenseArray =new JsonArray();
        expenseArray.add(expenseObject);
        String budgetDetail = travelApplication.addBudgetDetail(expenseArray,1000.00);
        travelApplication.submitApplication(employee,applicationOID,budgetDetail);

    }

    @Test(description = "新建费用申请单")
    public void createApplicationTest02() throws HttpStatusException {
        FormComponent component = new FormComponent("自动化测试新建费用申请单");
        component.setDepartment(employee.getDepartmentOID());
        //申请单生成预算
        JsonObject expenseBudget = travelApplication.addBudgetExpense(employee,23.0,false,"大巴","费用申请单-测试");
        JsonArray expenseBudgets = new JsonArray();
        expenseBudgets.add(expenseBudget);
        String budget = travelApplication.addBudgetDetail(expenseBudgets,23.0);
        JsonObject application = travelApplication.createExpenseApplication(employee,"费用申请单-测试",component,budget);
        String applicationOID =application.get("applicationOID").getAsString();
        //申请单提交
        travelApplication.submitExpenseApplication(employee,applicationOID);
        //新建费用报销单
        //获取申请单默认的控件信息
        JsonArray dafaultCustomFormValue = expenseReport.getValueFromApplication(employee,applicationOID);
        expenseReport.createTravelExpenseReport(employee,"费用报销单-测试",applicationOID,dafaultCustomFormValue);
    }
}

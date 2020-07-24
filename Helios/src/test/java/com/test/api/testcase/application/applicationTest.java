package com.test.api.testcase.application;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FlightItinerary;
import com.hand.basicObject.FormComponent;
import com.hand.basicObject.InvoiceComponent;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.ExpenseReportInvoice;
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

    private FormComponent component;
    private Employee employee;
    private TravelApplication travelApplication;
    private ExpenseReportComponent expenseReportComponent;
    private ExpenseReport expenseReport;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        component = FormComponent.builder().cause("自动化测试").build();
        employee=getEmployee(phoneNumber,pwd,env);
        travelApplication =new TravelApplication();
        expenseReportComponent = new ExpenseReportComponent();
        expenseReport =new ExpenseReport();
    }

    @Test(description = "新建差旅申请单")
    public void createApplication() throws HttpStatusException {
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
        FlightItinerary flightItinerary =new FlightItinerary();
        flightItinerary.setItineraryType(1002);
        flightItinerary.setDiscount("");
        flightItinerary.setFromCity("西安");
        flightItinerary.setToCity("北京");
        flightItinerary.setStartDate(UTCTime.getNowStartUtcDate());
        //如果这块是往返的话，这块时间必须跟控件上的结束时间一致  单程的话就是今天时间
        flightItinerary.setEndDate(UTCTime.getUtcStartDate(3));
        flightItinerary.setFromCityCode(expenseReportComponent.getCityCode(employee,"西安"));
        flightItinerary.setToCityCode(expenseReportComponent.getCityCode(employee,"北京"));
        flightItineraries.add(flightItinerary);
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        //申请单添加预算
        JsonObject expenseObject = travelApplication.addBudgetExpenseType(employee,1000.00,false,"机票","差旅申请单-节假日");
        JsonArray expenseArray =new JsonArray();
        expenseArray.add(expenseObject);
        String budgetDetail = travelApplication.getBudgetDetail(employee,expenseArray,1000.00);
        travelApplication.submitApplication(employee,applicationOID,budgetDetail);
    }
}
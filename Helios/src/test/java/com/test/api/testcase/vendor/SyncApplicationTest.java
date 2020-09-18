package com.test.api.testcase.vendor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.basicObject.itinerary.FlightItinerary;
import com.hand.basicObject.itinerary.HotelItinerary;
import com.hand.basicObject.itinerary.TrainItinerary;
import com.hand.basicconstant.SupplierOID;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.ApplicationMethod.TravelApplicationPage;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.TravelApplication;
import com.test.api.method.Vendor;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;


/**
 * @Author peng.zhang
 * @Date 2020/8/4
 * @Version 1.0
 **/
@Slf4j
public class SyncApplicationTest extends BaseTest {

    private TravelApplication travelApplication;
    private FormComponent component;
    private Employee employee;
    private ExpenseReportComponent expenseReportComponent;
    private ExpenseReport expenseReport;
    private TravelApplicationPage travelApplicationPage;
    private Vendor vendor;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("14082978000") String phoneNumber, @Optional("hly123456") String pwd, @Optional("stage") String env){
        component = FormComponent.builder().cause("消费商平台自动化测试").build();
        employee=getEmployee(phoneNumber,pwd,env);
        travelApplication =new TravelApplication();
        expenseReportComponent = new ExpenseReportComponent();
        expenseReport =new ExpenseReport();
        travelApplicationPage =new TravelApplicationPage();
        vendor =new Vendor();
    }

    @Test(description = "消费商-中集供应商",groups = "D")
    public void vendorTest1() throws HttpStatusException {
        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(UTCTime.getNowUtcTime());
        component.setEndDate(UTCTime.getUtcTime(3,0));
        //添加参与人员  参与人员的value 是一段json数组。
        JsonArray array = new JsonArray();
        array.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"差旅申请单-消费平台"),"懿消费商(xiao/feishang)"));
        component.setParticipant(array.toString());
//        创建申请单
        String applicationOID = travelApplication.createTravelApplication(employee,"差旅申请单-消费平台",component).get("applicationOID");
//        //初始化飞机行程 供应商为中集
        ArrayList<FlightItinerary> flightItineraries =new ArrayList<>();
        FlightItinerary flightItinerary=travelApplicationPage.addFlightItinerary(employee,1001, SupplierOID.cimccTMC,"西安市","北京",null,UTCTime.getNowStartUtcDate());
        //初始化酒店行程  中集
        ArrayList<HotelItinerary> hotelItineraries =new ArrayList<>();
        //酒店行程  开始日期要跟申请单的表头一样
        HotelItinerary hotelItinerary =new HotelItinerary("北京",1,UTCTime.getNowStartUtcDate(),UTCTime.getUtcStartDate(3),SupplierOID.cimccTMC,expenseReportComponent.getCityCode(employee,"北京"));
        //初始化火车行程   中集
        ArrayList<TrainItinerary> trainItineraries =new ArrayList<>();
        TrainItinerary trainItinerary =new TrainItinerary("北京","深圳",UTCTime.getNowStartUtcDate(),SupplierOID.cimccTMC,expenseReportComponent.getCityCode(employee,"北京"),expenseReportComponent.getCityCode(employee,"深圳"));
        flightItineraries.add(flightItinerary);
        hotelItineraries.add(hotelItinerary);
        trainItineraries.add(trainItinerary);
        // 添加行程
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        travelApplication.addItinerary(employee,applicationOID,hotelItineraries);
        travelApplication.addItinerary(employee,applicationOID,trainItineraries);
        //申请单提交
        travelApplication.submitApplication(employee,applicationOID,"");
        // 获取申请单详情
        JsonObject applicationDetail = travelApplication.getApplicationDetail(employee,applicationOID);
        //获取到申请单中的行程信息
        JsonArray flightItineraryInfo = travelApplication.getItinerary(employee,applicationOID,"FLIGHT");
    }

    @Test(description = "消费商-大唐消费商",groups = "C")
    public void vendorTest2() throws HttpStatusException {

        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(UTCTime.getNowUtcTime());
        component.setEndDate(UTCTime.getUtcTime(3,0));
        //添加参与人员  参与人员的value 是一段json数组。
        JsonArray array = new JsonArray();
        array.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"差旅申请单-消费平台"),"懿消费商(xiao/feishang)"));
        component.setParticipant(array.toString());
        //创建申请单
        String applicationOID = travelApplication.createTravelApplication(employee,"差旅申请单-消费平台",component).get("applicationOID");
        //添加飞机行程 供应商为中集
        ArrayList<FlightItinerary> flightItineraries =new ArrayList<>();
        //单程机票无返回时间
        FlightItinerary flightItinerary=travelApplicationPage.addFlightItinerary(employee,1001, SupplierOID.dttrip,"西安市","北京",null,UTCTime.getNowStartUtcDate());
        flightItineraries.add(flightItinerary);
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        travelApplication.submitApplication(employee,applicationOID,"");
    }

}

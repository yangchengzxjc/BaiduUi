package com.test.api.method.ApplicationMethod;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.component.FormComponent;
import com.hand.basicObject.itinerary.DiningItinerary;
import com.hand.basicObject.itinerary.FlightItinerary;
import com.hand.basicObject.itinerary.TrainItinerary;
import com.hand.basicConstant.Supplier;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.TravelApplication;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2020/8/4
 * @Version 1.0
 **/
public class TravelApplicationPage {

    private TravelApplication travelApplication;
    private ExpenseReportComponent expenseReportComponent;

    public TravelApplicationPage(){
        travelApplication =new TravelApplication();
        expenseReportComponent =new ExpenseReportComponent();
    }

    /**
     * 初始化飞机行程
     * @param employee
     * @param itineraryType 行程类型 单程1001或往返1002
     * @param supplierOID  供应商的OID
     * @param fromCity  出发城市
     * @param toCity   到达城市
     * @param endDate   当是单程的时候 结束日期为null  为表单的结束日期
     * @param startDate  出发日期  为表单上的控件的开始日期
     * @return
     * @throws HttpStatusException
     */
    public FlightItinerary addFlightItinerary(Employee employee, Integer itineraryType, String supplierOID, String fromCity, String toCity,String endDate,String startDate) throws HttpStatusException {
        //添加差旅行程(目前支持飞机行程和酒店行程)
        FlightItinerary flightItinerary =new FlightItinerary();
        //如果是单程的话就不需要返回时间
        if(itineraryType==1001){
            flightItinerary.setEndDate(null);
        }else{
            //如果是往返时间的话是跟申请单的控件的结束日期一致
            flightItinerary.setEndDate(endDate);
        }
        flightItinerary.setItineraryType(itineraryType);
        flightItinerary.setDiscount(new BigDecimal("7"));
        flightItinerary.setFromCity(fromCity);
        flightItinerary.setToCity(toCity);
        flightItinerary.setStartDate(startDate);
        flightItinerary.setTicketPrice(Double.valueOf(RandomNumber.getRandomNumber(900,1200)));
        flightItinerary.setFromCityCode(expenseReportComponent.getCityCode(employee,fromCity));
        flightItinerary.setToCityCode(expenseReportComponent.getCityCode(employee,toCity));
        flightItinerary.setSupplierOID(supplierOID);
        flightItinerary.setSeatClass("经济舱");
        return flightItinerary;
    }

    public TrainItinerary setTrainItinerary(Employee employee,String fromCity,String toCity,String startDate,String supplierOID,String seatClass) throws HttpStatusException {
        HashMap<String,String> setclassCode = new HashMap<>();
        setclassCode.put("一等座","YDZ");
        setclassCode.put("二等座","EDZ");
        setclassCode.put("商务座","SWZ");
        setclassCode.put("硬座","YZ");
        setclassCode.put("软卧","RW");
        TrainItinerary trainItinerary =new TrainItinerary();
        trainItinerary.setFromCity(fromCity);
        trainItinerary.setFromCityCode(expenseReportComponent.getCityCode(employee,fromCity));
        trainItinerary.setStartDate(startDate);
        trainItinerary.setToCity(toCity);
        trainItinerary.setToCityCode(expenseReportComponent.getCityCode(employee,toCity));
        trainItinerary.setSupplierOID(supplierOID);
        trainItinerary.setTicketPrice(new BigDecimal(RandomNumber.getRandomNumber(400,700)).setScale(1));
        trainItinerary.setSeatClass(seatClass);
        trainItinerary.setSeatClassCode(setclassCode.get(seatClass));
        return trainItinerary;
    }

    public DiningItinerary setDiningItinerary(Employee employee, Long diningSceneId, String cityName, String startDate, String endDate, Double standardAmount, String remark) throws HttpStatusException {
        DiningItinerary diningItinerary = new DiningItinerary();
        diningItinerary.setDiningSceneId(diningSceneId);
        diningItinerary.setCityName(cityName);
        diningItinerary.setCityCode(expenseReportComponent.getCityCode(employee, cityName));
        diningItinerary.setStartDate(startDate);
        diningItinerary.setEndDate(endDate);
        diningItinerary.setStandardAmount(standardAmount);
        diningItinerary.setRemark(remark);
        return diningItinerary;
    }



    /**
     * 新建差旅申请单   控件 开始结束日期  和参与人
     * @param employee
     * @param formName
     * @return
     * @throws HttpStatusException
     */
    public String setTravelApplication(Employee employee,String formName,String endDate) throws HttpStatusException {
        FormComponent component = new FormComponent("自动化测试差旅申请单");
        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(UTCTime.getUtcStartDate(-5));
        component.setEndDate(endDate);
        //添加参与人员  参与人员的value 是一段json数组。
        component.setParticipant(new String[]{employee.getFullName()});
        //创建申请单
        String applicationOID = travelApplication.createTravelApplication(employee,formName,component).get("applicationOID");
        //添加差旅行程(目前支持飞机行程和酒店行程)
        ArrayList<FlightItinerary> flightItineraries =new ArrayList<>();
        FlightItinerary flightItinerary = addFlightItinerary(employee,1001, Supplier.CTRIP_AIR.getSupplierOID(),"西安","北京",null,UTCTime.getUtcStartDate(-4));
        flightItineraries.add(flightItinerary);
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        travelApplication.submitApplication(employee,applicationOID,"");
        return applicationOID;
    }
}

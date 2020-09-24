package com.test.api.method.ApplicationMethod;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.itinerary.FlightItinerary;
import com.hand.basicObject.itinerary.HotelItinerary;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.TravelApplication;

import java.math.BigDecimal;

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



}

package com.test.api.method.VendorMethod;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.basicObject.supplierObject.syncApproval.syncPlatformEntity.*;
import com.hand.utils.GsonUtil;
import com.hand.utils.UTCTime;
import com.test.api.method.InfraStructure;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/18
 * @Version 1.0
 **/
public class SyncApproval {

    private InfraStructure infraStructure;

    public SyncApproval(){
        infraStructure = new InfraStructure();
    }


    // 大唐tmcChannel->supplyDttripTmcService

    /**
     * 审批单同步 飞机行程初始化
     * @param itinerary 行程信息
     * @param component
     * @return
     */
    public TravelFlightItinerary setTravelFlight(JsonObject itinerary, FormComponent component){
        //舱等映射
        String seatClass ="";
        if(itinerary.get(seatClass).isJsonNull()){
            seatClass="未知";
        }else{
            seatClass =itinerary.get("seatClass").getAsString();
        }
        HashMap<String,Integer> seatClassMapping = new HashMap<>();
        seatClassMapping.put("未知",0);
        seatClassMapping.put("头等舱",1);
        seatClassMapping.put("公务舱",2);
        seatClassMapping.put("经济舱",3);
        seatClassMapping.put("超级经济舱",4);
        // 折扣
        TravelFlightItinerary travelFlightItinerary = new TravelFlightItinerary();
        travelFlightItinerary.setSeatClass(seatClassMapping.get(seatClass));
        if(!itinerary.get("discount").isJsonNull()){
            travelFlightItinerary.setDiscount(new BigDecimal(itinerary.get("discount").getAsInt()).setScale(1));
        }
        if(!itinerary.get("ticketPrice").isJsonNull()){
            travelFlightItinerary.setTicketPrice(new BigDecimal(itinerary.get("ticketPrice").getAsInt()).setScale(2));
        }
        travelFlightItinerary.setItineraryType(itinerary.get("itineraryType").getAsInt());
        travelFlightItinerary.setProductType(itinerary.get("productType").getAsInt());
        travelFlightItinerary.setFromCity(itinerary.get("fromCity").getAsString());
        travelFlightItinerary.setToCity(itinerary.get("toCity").getAsString());
        travelFlightItinerary.setFromCityCode(itinerary.get("fromCityCode").getAsString());
        travelFlightItinerary.setToCityCode(itinerary.get("toCityCode").getAsString());
        ArrayList<String> fromCities =new ArrayList<>();
        fromCities.add(itinerary.get("fromCity").getAsString());
        travelFlightItinerary.setFromCities(fromCities);
        ArrayList<String> toCities =new ArrayList<>();
        toCities.add(itinerary.get("toCity").getAsString());
        travelFlightItinerary.setToCities(toCities);
        ArrayList<String> fromCitiesCode =new ArrayList<>();
        fromCitiesCode.add(itinerary.get("fromCityCode").getAsString());
        travelFlightItinerary.setFromCityCodes(fromCitiesCode);
        ArrayList<String> toCityCodes =new ArrayList<>();
        toCityCodes.add(itinerary.get("toCityCode").getAsString());
        travelFlightItinerary.setToCities(toCityCodes);
        //判断下起飞开始结束时间是否存在
        if(itinerary.get("takeOffBeginTime").isJsonNull()){
            travelFlightItinerary.setTakeOffBeginTime(UTCTime.utcToBJtime(itinerary.get("startDate").getAsString())+" 00:00:00");
        }else{
            travelFlightItinerary.setTakeOffBeginTime(UTCTime.utcToBJtime(itinerary.get("takeOffBeginTime").getAsString()));
        }
        if(itinerary.get("takeOffEndTime").isJsonNull()){
            travelFlightItinerary.setTakeOffBeginTime(UTCTime.utcToBJtime(component.getEndDate())+" 00:00:00");
        }else{
            travelFlightItinerary.setTakeOffBeginTime(UTCTime.utcToBJtime(itinerary.get("takeOffEndTime").getAsString()));
        }
        //初始化到达时间
        travelFlightItinerary.setArrivalBeginTime(UTCTime.utcToBJtime(itinerary.get("startDate").getAsString())+" 00:00:00");
        travelFlightItinerary.setArrivalEndTime(UTCTime.utcToBJtime(component.getEndDate())+" 00:00:00");
        travelFlightItinerary.setTakeOffBegin(UTCTime.utcToBJtime(itinerary.get("startDate").getAsString()));
        travelFlightItinerary.setTakeOffEnd(UTCTime.utcToBJtime(component.getEndDate()));
        travelFlightItinerary.setArrivalBegin(UTCTime.utcToBJtime(itinerary.get("startDate").getAsString()));
        travelFlightItinerary.setArrivalEnd(UTCTime.utcToBJtime(component.getEndDate()));
        travelFlightItinerary.setFloatDaysBegin(4);
        travelFlightItinerary.setFloatDaysEnd(4);
        return travelFlightItinerary;
    }

    /**
     * 初始化 订票人信息
     * @param employee
     * @param travelApplication   为审批单中的 travelApplication 字段
     * @throws HttpStatusException
     */
    public BookClerk setBookClerk(Employee employee,JsonObject travelApplication) throws HttpStatusException {
        BookClerk bookClerk = new BookClerk();
        JsonObject card =new JsonObject();
        //获取身份证信息
        JsonArray cardList = infraStructure.queryUserCard(employee);
        //获取用户基本信息
        JsonObject bookInfo = infraStructure.getUserDetail(employee,travelApplication.get("bookingClerkName").getAsString());
        if(GsonUtil.isNotEmpt(cardList)){
            card = GsonUtil.getJsonValue(cardList,"cardTypeName","身份证");
        }
        bookClerk.setName(card.get("lastName").getAsString());
        bookClerk.setEmployeeID(bookInfo.get("employeeID").getAsString());
        bookClerk.setGender(bookInfo.get("gender").getAsString().equals("女") ? "F" : "M");
        bookClerk.setMobile(bookInfo.get("mobile").getAsString());
        return bookClerk;
    }

    /**
     * 初始化参与人
     * @param employee
     * @param applicationParticipant  申请单详情中的字段 applicationParticipant
     * @throws HttpStatusException
     */
    public Participant setParticipant(Employee employee,JsonObject applicationParticipant) throws HttpStatusException {
        Participant participant =new Participant();
        //获取参与人详情
        JsonObject employeeInfo = infraStructure.getEmployeeDetail(employee,applicationParticipant.get("participantOID").getAsString());
        //身份证信息
        JsonObject cardInfo = infraStructure.queryUserCard(employee,applicationParticipant.get("participantOID").getAsString(),"身份证");
        //护照
        JsonObject passportInfo = infraStructure.queryUserCard(employee,applicationParticipant.get("participantOID").getAsString(),"护照");
        //name 优先身份证中获取
        participant.setName(cardInfo.get("lastName").getAsString());
        participant.setEmployeeID(employeeInfo.get("employeeID").getAsString());
        participant.setGender(employeeInfo.get("gender").getAsString().equals("女") ? "F" : "M");
        participant.setMobile(employeeInfo.get("mobile").getAsString());
        //enName 是总护照里面获取
        participant.setEnFirstName(passportInfo.get("firstName").getAsString());
        participant.setEnLastName(passportInfo.get("lastName").getAsString());
        return participant;
    }

    /**
     * 初始化syncEntity 
     * @param employee
     * @param component
     * @param bookClerk
     * @param applicationDetail
     * @param itinerary
     * @param participants
     * @param travelItineraries
     * @param travelHotelItineraries
     * @param travelTrainItineraries
     * @return
     */
    public SyncEntity setSyncEntity(Employee employee,FormComponent component, BookClerk bookClerk, JsonObject applicationDetail, JsonObject itinerary, List<Participant> participants, List<TravelFlightItinerary> travelItineraries,List<TravelHotelItinerary> travelHotelItineraries,List<TravelTrainItinerary> travelTrainItineraries){
        SyncEntity syncEntity =new SyncEntity();
        syncEntity.setTenantId(employee.getTenantId());
        syncEntity.setCompanyId(employee.getCompanyOID());
        syncEntity.setStatus(1);
        syncEntity.setBookClerk(bookClerk);
        syncEntity.setBusinessCode(applicationDetail.get("businessCode").getAsString());
        syncEntity.setApprovalCode(itinerary.get("approvalNum").getAsString());
        syncEntity.setCostCenter1(component.getCostCenter());
        syncEntity.setParticipantList(participants);
        syncEntity.setTravelFlightsList(travelItineraries);
        syncEntity.setTravelHotelsList(travelHotelItineraries);
        syncEntity.setTravelTrainsList(travelTrainItineraries);
        return syncEntity;
    }



}

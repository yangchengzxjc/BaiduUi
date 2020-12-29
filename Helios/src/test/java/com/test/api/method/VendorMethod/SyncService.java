package com.test.api.method.VendorMethod;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.employeeInfoDto.EmployeeDTO;
import com.hand.basicObject.supplierObject.employeeInfoDto.UserCardInfoDTO;
import com.hand.basicObject.supplierObject.syncApproval.syncCtrip.*;
import com.hand.basicObject.supplierObject.syncApproval.syncEleme.SyncElemePlanRequest;
import com.hand.basicObject.supplierObject.syncApproval.syncPlatformEntity.*;
import com.hand.utils.GsonUtil;
import com.hand.utils.UTCTime;
import com.test.api.method.InfraStructure;
import com.test.api.method.TravelApplication;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/9/18
 * @Version 1.0
 **/
@Slf4j
public class SyncService {

    private InfraStructure infraStructure;

    public SyncService(){
        infraStructure = new InfraStructure();
    }


    // 大唐tmcChannel->supplyDttripTmcService

    /**
     * 审批单同步 飞机行程初始化
     * @param itinerary 申请单接口中提供的行程信息
     * @param floatDays 申请单同步过来的数据中floatDays字段
     * @return
     */
    public TravelFlightItinerary setTravelFlight(JsonObject itinerary,JsonObject floatDays){
        //舱等映射
        String seatClass ="";
        if(itinerary.get("seatClass").isJsonNull()){
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
            travelFlightItinerary.setDiscount(itinerary.get("discount").getAsInt());
        }
        if(!itinerary.get("ticketPrice").isJsonNull()){
            travelFlightItinerary.setTicketPrice(new BigDecimal(itinerary.get("ticketPrice").getAsInt()).setScale(1));
        }
        travelFlightItinerary.setItineraryType(itinerary.get("itineraryType").getAsInt());
        travelFlightItinerary.setProductType(itinerary.get("productType").getAsInt());
        String fromCity = itinerary.get("fromCity").getAsString();
        String toCity = itinerary.get("toCity").getAsString();
        String fromCityCode = itinerary.get("fromCityCode").getAsString();
        String toCityCode = itinerary.get("toCityCode").getAsString();
        travelFlightItinerary.setFromCity(fromCity);
        travelFlightItinerary.setToCity(toCity);
        travelFlightItinerary.setFromCityCode(fromCityCode);
        travelFlightItinerary.setToCityCode(toCityCode);
        ArrayList<String> fromCities =new ArrayList<>();
        fromCities.add(fromCity);
        travelFlightItinerary.setFromCities(fromCities);
        ArrayList<String> toCities =new ArrayList<>();
        toCities.add(toCity);
        travelFlightItinerary.setToCities(toCities);

        ArrayList<String> fromCityCodes =new ArrayList<>();
        fromCityCodes.add(fromCityCode);
        travelFlightItinerary.setFromCityCodes(fromCityCodes);
        ArrayList<String> toCityCodes =new ArrayList<>();
        toCityCodes.add(toCityCode);
        travelFlightItinerary.setToCityCodes(toCityCodes);

        //判断下起飞开始结束时间是否存在
        travelFlightItinerary.setTakeOffBeginTime(UTCTime.utcToBJDate(itinerary.get("startDate").getAsString(),-(floatDays.get("start").getAsInt()))+" 00:00:00");
        travelFlightItinerary.setArrivalBeginTime(UTCTime.utcToBJDate(itinerary.get("startDate").getAsString(),-(floatDays.get("start").getAsInt()))+" 00:00:00");
        travelFlightItinerary.setTakeOffBegin(UTCTime.utcToBJDate(itinerary.get("startDate").getAsString(),-(floatDays.get("start").getAsInt())));
        travelFlightItinerary.setArrivalBegin(UTCTime.utcToBJDate(itinerary.get("startDate").getAsString(),-(floatDays.get("start").getAsInt())));
        //单程的话 enddate为null   取值为startDate+浮动天数  如果为往返的话 endDate 有值  为endDate+浮动天数
        if(itinerary.get("itineraryType").getAsInt()==1001){
            travelFlightItinerary.setTakeOffEndTime(UTCTime.utcToBJDate(itinerary.get("startDate").getAsString(),floatDays.get("end").getAsInt())+" 00:00:00");
            travelFlightItinerary.setArrivalEndTime(UTCTime.utcToBJDate(itinerary.get("startDate").getAsString(),floatDays.get("end").getAsInt())+" 00:00:00");
            travelFlightItinerary.setArrivalEnd(UTCTime.utcToBJDate(itinerary.get("startDate").getAsString(),floatDays.get("end").getAsInt()));
            travelFlightItinerary.setTakeOffEnd(UTCTime.utcToBJDate(itinerary.get("startDate").getAsString(),floatDays.get("end").getAsInt()));
        }else{
            travelFlightItinerary.setTakeOffEndTime(UTCTime.utcToBJDate(itinerary.get("endDate").getAsString(),floatDays.get("end").getAsInt())+" 00:00:00");
            travelFlightItinerary.setArrivalEndTime(UTCTime.utcToBJDate(itinerary.get("endDate").getAsString(),floatDays.get("end").getAsInt())+" 00:00:00");
            travelFlightItinerary.setArrivalEnd(UTCTime.utcToBJDate(itinerary.get("endDate").getAsString(),floatDays.get("end").getAsInt()));
            travelFlightItinerary.setTakeOffEnd(UTCTime.utcToBJDate(itinerary.get("endDate").getAsString(),floatDays.get("end").getAsInt()));
        }
        travelFlightItinerary.setFloatDaysBegin(floatDays.get("start").getAsInt());
        travelFlightItinerary.setFloatDaysEnd(floatDays.get("end").getAsInt());
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
        JsonArray cardList = infraStructure.queryUserCard(employee,"");
        //获取用户基本信
        String bookUserOID ="";
        try {
            bookUserOID = travelApplication.get("bookingClerkOID").getAsString();
        }catch (NullPointerException e) {
            try {
                bookUserOID = travelApplication.get("trainBookingClerkOID").getAsString();
            } catch (NullPointerException e1) {
                bookUserOID = travelApplication.get("hotelBookingClerkOID").getAsString();
            }
        }
        JsonObject bookInfo = infraStructure.getEmployeeDetail(employee,bookUserOID);
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
     * @param bookClerk
     * @param applicationDetail
     * @param itinerary
     * @param participants
     * @param travelItineraries
     * @param travelHotelItineraries
     * @param travelTrainItineraries
     * @return
     */
    public SyncEntity setSyncEntity(Employee employee,String itineraryType,TravelApplication travelApplication,BookClerk bookClerk, JsonObject applicationDetail, JsonObject itinerary, List<Participant> participants, List<TravelFlightItinerary> travelItineraries, List<TravelHotelItinerary> travelHotelItineraries, List<TravelTrainItinerary> travelTrainItineraries) throws HttpStatusException {
        JsonArray customFormValues = applicationDetail.get("custFormValues").getAsJsonArray();
        JsonObject costCenter = new JsonObject();
        if(GsonUtil.isNotEmpt(customFormValues)){
            costCenter = GsonUtil.getJsonValue(customFormValues,"fieldName","成本中心");
        }
        SyncEntity syncEntity =new SyncEntity();
        syncEntity.setTenantId(applicationDetail.get("tenantId").getAsString());
        syncEntity.setCompanyId(applicationDetail.get("companyOID").getAsString());
        syncEntity.setStatus(1);
        syncEntity.setBookClerk(bookClerk);
        syncEntity.setBusinessCode(applicationDetail.get("businessCode").getAsString());
        try{
            if(itinerary.get("approvalNum").isJsonNull()){
                //判断行程单号是否为空 为空则为审批单未审批通过 重新获取审批单行程详情
                JsonObject newItinerary = travelApplication.getItinerary(employee,applicationDetail.get("applicationOID").getAsString(),itineraryType).get(0).getAsJsonObject();
                log.info("最新的行程信息：{}",newItinerary);
                syncEntity.setApprovalCode(newItinerary.get("approvalNum").getAsString());
            }else{
                syncEntity.setApprovalCode(itinerary.get("approvalNum").getAsString());
            }
        }catch (NullPointerException e){
            if(itinerary.get("approvalNumber").isJsonNull()){
                //判断行程单号是否为空 为空则为审批单未审批通过 重新获取审批单行程详情
                JsonObject filght = travelApplication.getItinerary(employee,applicationDetail.get("applicationOID").getAsString(),itineraryType).get(0).getAsJsonObject();
                syncEntity.setApprovalCode(filght.get("approvalNumber").getAsString());
            }else{
                syncEntity.setApprovalCode(itinerary.get("approvalNumber").getAsString());
            }
        }
        syncEntity.setCostCenter1(costCenter.get("value").getAsString());
        syncEntity.setParticipantList(participants);
        syncEntity.setTravelFlightsList(travelItineraries);
        syncEntity.setTravelHotelsList(travelHotelItineraries);
        syncEntity.setTravelTrainsList(travelTrainItineraries);
        return syncEntity;
    }

    /**
     * 初始化酒店行程
     * @param itinerary 酒店行程中的
     * @return
     */
    public TravelHotelItinerary setTravelHotelItinerary(JsonObject itinerary,JsonObject floatDays){
        TravelHotelItinerary travelHotelItinerary =new TravelHotelItinerary();
        travelHotelItinerary.setCity(itinerary.get("cityName").getAsString());
        travelHotelItinerary.setCityCode(itinerary.get("cityCode").getAsString());
        travelHotelItinerary.setFromDate(UTCTime.utcToBJDate(itinerary.get("fromDate").getAsString(),-(floatDays.get("start").getAsInt()))+" 00:00:00");
        travelHotelItinerary.setLeaveDate(UTCTime.utcToBJDate(itinerary.get("leaveDate").getAsString(),floatDays.get("end").getAsInt())+" 00:00:00");
        travelHotelItinerary.setMaxPrice(itinerary.get("maxPrice").getAsString());
        if(!itinerary.get("minPrice").isJsonNull()){
            travelHotelItinerary.setMinPrice(itinerary.get("minPrice").getAsString());
        }
        travelHotelItinerary.setRoomNumber(itinerary.get("roomNumber").getAsInt());
        travelHotelItinerary.setCities(new ArrayList<String>());
        travelHotelItinerary.setCityCodes(new ArrayList<String>());
        travelHotelItinerary.setFloatDaysBegin(floatDays.get("start").getAsInt());
        travelHotelItinerary.setFloatDaysEnd(floatDays.get("end").getAsInt());
        return travelHotelItinerary;
    }

    /**
     * 初始化火车行程
     * @param itinerary
     * @return
     */
    public TravelTrainItinerary setTravelTrainItinerary(JsonObject itinerary,JsonObject floatDays){
        String seatType="";
        if(itinerary.get("seatClass").isJsonNull()){
            seatType="未知";
        }else{
            seatType =itinerary.get("seatClass").getAsString();
        }
        HashMap<String,String> seatClassMapping = new HashMap<>();
        seatClassMapping.put("硬座","201");
        seatClassMapping.put("特等座","205");
        seatClassMapping.put("一等座","207");
        seatClassMapping.put("二等座","209");
        seatClassMapping.put("商务座","221");
        seatClassMapping.put("一等软座","301");
        seatClassMapping.put("二等软座","302");
        seatClassMapping.put("未知",null);
        ArrayList<String> seatClass =new ArrayList<>();
        seatClass.add(seatClassMapping.get(seatType));
        TravelTrainItinerary travelTrainItinerary =new TravelTrainItinerary();
        travelTrainItinerary.setFromCity(itinerary.get("fromCity").getAsString());
        travelTrainItinerary.setToCity(itinerary.get("toCity").getAsString());
        travelTrainItinerary.setFromCityCode(itinerary.get("fromCityCode").getAsString());
        travelTrainItinerary.setToCityCode(itinerary.get("toCityCode").getAsString());
        ArrayList<String> fromCities =new ArrayList<>();
        fromCities.add(itinerary.get("fromCity").getAsString());
        travelTrainItinerary.setFromCities(fromCities);
        ArrayList<String> toCities =new ArrayList<>();
        toCities.add(itinerary.get("toCity").getAsString());
        travelTrainItinerary.setToCities(toCities);
        ArrayList<String> fromCitiesCode =new ArrayList<>();
        fromCitiesCode.add(itinerary.get("fromCityCode").getAsString());
        travelTrainItinerary.setFromCityCodes(fromCitiesCode);
        ArrayList<String> toCityCodes =new ArrayList<>();
        toCityCodes.add(itinerary.get("toCityCode").getAsString());
        travelTrainItinerary.setToCities(toCityCodes);
        travelTrainItinerary.setTicketPrice(itinerary.get("ticketPrice").getAsBigDecimal());
        //判断下起飞开始结束时间是否存在
        travelTrainItinerary.setTakeOffBeginTime(UTCTime.utcToBJDate(itinerary.get("startDate").getAsString(),-(floatDays.get("start").getAsInt()))+" 00:00:00");
        travelTrainItinerary.setTakeOffEndTime(UTCTime.utcToBJDate(itinerary.get("startDate").getAsString(),floatDays.get("end").getAsInt())+" 00:00:00");
        //初始化到达时间
        travelTrainItinerary.setArrivalBeginTime(UTCTime.utcToBJDate(itinerary.get("startDate").getAsString(),-(floatDays.get("start").getAsInt()))+" 00:00:00");
        travelTrainItinerary.setArrivalEndTime(UTCTime.utcToBJDate(itinerary.get("startDate").getAsString(),floatDays.get("end").getAsInt())+" 00:00:00");
        travelTrainItinerary.setSeatType(seatClass);
        travelTrainItinerary.setFloatDaysBegin(floatDays.get("start").getAsInt());
        travelTrainItinerary.setFloatDaysEnd(floatDays.get("end").getAsInt());
        return travelTrainItinerary;
    }

    /**
     * 根据查询出的证件List 封装成openApi中的证件对象
     * @param userCardInfo
     * @returnuserCardInfos
     */
    public ArrayList addUserCardInfoDTO(JsonArray userCardInfo){

        ArrayList<UserCardInfoDTO> userCardInfos =new ArrayList<>();
        for (int i = 0; userCardInfo.size()>i ; i++) {
            JsonObject jsb = userCardInfo.get(i).getAsJsonObject();
            UserCardInfoDTO userCardInfoDTO = new UserCardInfoDTO();
            userCardInfoDTO.setCardNo(jsb.get("originalCardNo").getAsString());
            userCardInfoDTO.setCardType(jsb.get("cardType").getAsString());
            if(jsb.get("cardExpiredTime").isJsonNull()){

            }else{
                userCardInfoDTO.setIDCardTimelimit(jsb.get("cardExpiredTime").getAsString());
            }
            userCardInfoDTO.setFirstName(jsb.get("firstName").getAsString());
            userCardInfoDTO.setLastName(jsb.get("lastName").getAsString());
            userCardInfos.add(userCardInfoDTO);
        }
        return userCardInfos;
    }

    /**
     * 封装开发平台openApi 人员同步对象
     * @param empObject
     * @param departCode
     * @param bookClass
     * @param employee
     * @param cardList
     * @return employeeDTO
     */
    public EmployeeDTO addEmployeeDTO(JsonObject empObject, JsonObject departCode, JsonObject bookClass, Employee employee, ArrayList cardList){
        JsonArray cardLists = new JsonParser().parse(GsonUtil.objectToString(cardList)).getAsJsonArray();
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setStatus((empObject.get("status").getAsString().equals("1001"))?1:0);
        employeeDTO.setFullName((GsonUtil.getJsonValue(cardLists,"cardType","101","lastName") != "")?
                GsonUtil.getJsonValue(cardLists,"cardType","102","lastName"):
                empObject.get("fullName").getAsString());
        employeeDTO.setEmployeeID(empObject.get("employeeID").getAsString());
        employeeDTO.setMobile(empObject.get("mobile").getAsString());
        employeeDTO.setEmail(empObject.get("email").getAsString());
        employeeDTO.setName((GsonUtil.getJsonValue(cardLists,"cardType","101","lastName") != "")?
                GsonUtil.getJsonValue(cardLists,"cardType","102","lastName"):
                empObject.get("fullName").getAsString());
        if (GsonUtil.getJsonValue(cardLists,"cardType","102","lastName") != ""){
            employeeDTO.setEnFirstName(GsonUtil.getJsonValue(cardLists,"cardType","102","firstName"));
            employeeDTO.setEnLastName(GsonUtil.getJsonValue(cardLists,"cardType","102","lastName"));
        }
        else {
            employeeDTO.setEnFirstName(null);
            employeeDTO.setEnLastName(null);
        }
        employeeDTO.setNationality(null);
        employeeDTO.setGender(empObject.get("genderCode").getAsString() == "1"?"F":"M");
        employeeDTO.setRankName(empObject.get("rank").getAsString());
        employeeDTO.setIsBookClass(bookClass.get("isBookClass").getAsString());
        employeeDTO.setIntlBookClassBlock(bookClass.get("intlBookClassBlock").getAsString());
        employeeDTO.setTenantId(employee.getTenantId());
        employeeDTO.setCompanyId(empObject.get("companyOID").getAsString());
        employeeDTO.setCompanyOID(empObject.get("companyOID").getAsString());
        employeeDTO.setCompanyCode(empObject.get("companyOID").getAsString());
        employeeDTO.setDeptCode(departCode.get("departmentCode").getAsString());
        employeeDTO.setDeptName(empObject.get("departmentName").getAsString());
        employeeDTO.setDeptPath(empObject.get("departmentPath").getAsString());
        employeeDTO.setDeptCustomCode(departCode.get("custDeptNumber").getAsString());
        employeeDTO.setUserCardInfos(cardList);
        return employeeDTO;
    }

    /**
     * 初始化携程同步实体
     * @return
     */
    public CtripApprovalEntity setCtripApprovalEntity(JsonObject applicationDetail, JsonObject itinerary, ArrayList<ExtendFieldList> extendFieldLists, ArrayList<FlightEndorsementDetail> flightEndorsementDetails, ArrayList<HotelEndorsementDetail> hotelEndorsementDetails, ArrayList<TrainEndorsementDetail> trainEndorsementDetails){
        CtripApprovalEntity ctripApprovalEntity =new CtripApprovalEntity();
        ctripApprovalEntity.setEmployeeID(applicationDetail.get("applicant").getAsJsonObject().get("employeeID").getAsString());
        ctripApprovalEntity.setStatus(1);
        try{
            if(!itinerary.get("approvalNum").isJsonNull()){
                ctripApprovalEntity.setApprovalNumber(itinerary.get("approvalNum").getAsString());
            }
        }catch (NullPointerException e){
            if(!itinerary.get("approvalNumber").isJsonNull()){
                ctripApprovalEntity.setApprovalNumber(itinerary.get("approvalNumber").getAsString());
            }
        }
        ctripApprovalEntity.setExtendFieldList(extendFieldLists);
        ctripApprovalEntity.setFlightEndorsementDetails(flightEndorsementDetails);
        ctripApprovalEntity.setHotelEndorsementDetails(hotelEndorsementDetails);
        ctripApprovalEntity.setTrainEndorsementDetails(trainEndorsementDetails);
        return ctripApprovalEntity;
    }

    /**
     *  初始化机票 行程
     * @return
     */
    public FlightEndorsementDetail setFlightEndorsementDetail(JsonObject itinerary,JsonObject floatDays,ArrayList<Passenger> passagesList){
        //舱等映射
        String seatClass ="";
        if(itinerary.get("seatClass").isJsonNull()){
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
        FlightEndorsementDetail flightEndorsementDetail = new FlightEndorsementDetail();
        String fromCity = itinerary.get("fromCity").getAsString();
        String toCity = itinerary.get("toCity").getAsString();
        String fromCityCode = itinerary.get("fromCityCode").getAsString();
        String toCityCode = itinerary.get("toCityCode").getAsString();
        ArrayList<String> fromCities =new ArrayList<>();
        fromCities.add(fromCity);

        ArrayList<String> toCities =new ArrayList<>();
        toCities.add(toCity);
        flightEndorsementDetail.setToCities(toCities);

        ArrayList<String> toCityCodes =new ArrayList<>();
        toCityCodes.add(toCityCode);

        ArrayList<String> fromCityCodes =new ArrayList<>();
        fromCityCodes.add(fromCityCode);
        flightEndorsementDetail.setFromCities(fromCities);
        flightEndorsementDetail.setToCities(toCities);
        flightEndorsementDetail.setFromCitiesCode(fromCityCodes);
        flightEndorsementDetail.setToCitiesCode(toCityCodes);
        flightEndorsementDetail.setCurrency(2);
        flightEndorsementDetail.setFlightWay(1);
        flightEndorsementDetail.setDepartDate(UTCTime.utcToBJDate(itinerary.get("startDate").getAsString(),0)+" 00:00:00");
        flightEndorsementDetail.setDepartBeginDate(UTCTime.utcToBJDate(itinerary.get("startDate").getAsString(),-(floatDays.get("start").getAsInt()))+" 00:00:00");
        flightEndorsementDetail.setDepartEndDate(UTCTime.utcToBJDate(itinerary.get("startDate").getAsString(),floatDays.get("start").getAsInt())+" 00:00:00");
        flightEndorsementDetail.setDepartFloatDays(floatDays.get("start").getAsInt());
        flightEndorsementDetail.setPassengerList(passagesList);
        flightEndorsementDetail.setProductType(1);
        flightEndorsementDetail.setPrice(itinerary.get("ticketPrice").getAsBigDecimal());
        flightEndorsementDetail.setSeatClass(seatClassMapping.get(seatClass));
        flightEndorsementDetail.setReturnBeginDate(UTCTime.utcToBJDate(itinerary.get("startDate").getAsString(),-(floatDays.get("start").getAsInt()))+" 00:00:00");
        flightEndorsementDetail.setReturnEndDate(UTCTime.utcToBJDate(itinerary.get("startDate").getAsString(),floatDays.get("start").getAsInt())+" 00:00:00");
        flightEndorsementDetail.setReturnFloatDays(floatDays.get("end").getAsInt());
        flightEndorsementDetail.setTravelerCount(passagesList.size());
        return flightEndorsementDetail;
    }

    /**
     * 扩展字段
     * @return
     */
    public ExtendFieldList setExtendFieldList(String fieldName,String value){
        ExtendFieldList extendFieldList =new ExtendFieldList();
        extendFieldList.setFieldName(fieldName);
        extendFieldList.setFieldType("String");
        extendFieldList.setFieldType(value);
        return extendFieldList;
    }

    public HotelEndorsementDetail setHotelEndorsementDetail(){
        HotelEndorsementDetail hotelEndorsementDetail = new HotelEndorsementDetail();
        return hotelEndorsementDetail;
    }

    /**
     * 携程初始化乘客信息
     * @param employee
     * @param applicationParticipant
     * @return
     * @throws HttpStatusException
     */
    public Passenger setPassenger(Employee employee,JsonObject applicationParticipant) throws HttpStatusException {
        Passenger passenger =new Passenger();
        JsonObject bookInfo = infraStructure.getEmployeeDetail(employee,applicationParticipant.get("participantOID").getAsString());
        passenger.setName(bookInfo.get("fullName").getAsString());
        return passenger;
    }

    // todo-构造饿了么审批单同步dto
    public SyncElemePlanRequest setElemePlanDTO(Object applicationDTO){
        SyncElemePlanRequest syncElemePlanRequest = new SyncElemePlanRequest();
//        syncElemePlanRequest.setBusinessCode(applicationDTO.getBusinessCode());
        return syncElemePlanRequest;
    }
}

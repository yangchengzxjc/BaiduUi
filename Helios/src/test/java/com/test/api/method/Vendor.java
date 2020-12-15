package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.api.VendorApi;
import com.hand.api.VendorInfoApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.SSOBody;
import com.hand.basicObject.supplierObject.SettlementBody;
import com.hand.basicObject.supplierObject.employeeInfoDto.UserCardInfoDTO;
import com.hand.utils.DocumnetUtil;
import com.hand.utils.GsonUtil;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.openqa.selenium.json.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/8/26
 * @Version 1.0
 **/
@Slf4j
public class Vendor {

    private VendorApi vendorApi;

    public Vendor() {
        vendorApi = new VendorApi();
    }

    /**
     * 消费商结算数据推送
     *
     * @param employee
     * @param type     结算类型  可选 filght train hotel
     * @param object   机票  酒店  火车等结算对象
     * @param appName  汇联易消费商注册的应用名称
     * @param corpId   消费商开通的公司Id
     * @return
     * @throws HttpStatusException
     */
    public <T> JsonObject pushSettlementData(Employee employee, String type, List<T> object, String appName, String corpId, String signature) throws HttpStatusException {
        String info = GsonUtil.objectToString(object);
        JsonArray listOrderSettlementInfo = new JsonParser().parse(info).getAsJsonArray();
        return vendorApi.pushSettlementData(employee, type, listOrderSettlementInfo, appName, corpId, signature);
    }

    /**
     * tmc 直接使用Json数据推送
     *
     * @param employee
     * @param type
     * @param listOrderSettlementInfo
     * @param appName
     * @param corpId
     * @param signature
     * @return
     * @throws HttpStatusException
     */
    public JsonObject pushSettlementData(Employee employee, String type, JsonObject listOrderSettlementInfo, String appName, String corpId, String signature) throws HttpStatusException {
        return vendorApi.pushSettlementData(employee, type, listOrderSettlementInfo, appName, corpId, signature);
    }

    /**
     * tmc订单推送
     *
     * @param employee
     * @param orderType
     * @param object    订单对象
     * @param appName
     * @param corpId
     * @param <T>
     * @return
     * @throws HttpStatusException
     */
    public <T> JsonObject pushOrderData(Employee employee, String orderType, T object, String appName, String corpId, String signature) throws HttpStatusException {
        //将数据序列化为JSON 字符串
        String orderString = GsonUtil.objectToString(object);
        //转化为 JsonObject
        JsonObject orderData = new JsonParser().parse(orderString).getAsJsonObject();
        return vendorApi.pushOrderData(employee, orderType, orderData, appName, corpId, signature);
    }

    /**
     * tmc订单推送  -  直接使用提供的json 数据进行推送数据
     *
     * @param employee
     * @param orderType
     * @param orderData 订单对象
     * @param appName
     * @param corpId
     * @param signature 签名
     * @return
     * @throws HttpStatusException
     */
    public JsonObject pushOrderData(Employee employee, String orderType, JsonObject orderData, String appName, String corpId, String signature) throws HttpStatusException {
        return vendorApi.pushOrderData(employee, orderType, orderData, appName, corpId, signature);
    }

    /**
     * sso 单点登录
     *
     * @param employee
     * @param object
     * @param appName
     * @param corpId
     * @return
     * @throws HttpStatusException
     */
    public JsonObject ssoLogin(Employee employee, SSOBody object, String appName, String corpId, String signature) throws HttpStatusException {
        String orderString = GsonUtil.objectToString(object);
        //转化为 JsonObject
        JsonObject requestBody = new JsonParser().parse(orderString).getAsJsonObject();
        return vendorApi.vendorSSO(employee, requestBody, appName, corpId, signature);
    }

    /**
     * 消费平台 sso单点登录
     * @param employee
     * @param supplierOID
     * @param direction
     * @param pageType
     * @return
     * @throws HttpStatusException
     */
    public String vndSSO(Employee employee,String supplierOID,String direction,String pageType) throws HttpStatusException {
        return vendorApi.vndSSO(employee,supplierOID,direction,pageType);
    }
    public int ssoCode(Employee employee,String supplierOID,String direction,String pageType) throws HttpStatusException {
        return vendorApi.ssoCode(employee,supplierOID,direction,pageType);
    }

    /**
     * 内部使用的查询结算数据的接口
     *
     * @return
     */
    public JsonObject internalQuerySettlement(Employee employee, String type, SettlementBody settlementBody) throws HttpStatusException {
        JsonObject respons = vendorApi.internalGetSettlementData(employee, type, settlementBody);
        JsonObject orderData = new JsonObject();
        if (respons.get("success").getAsBoolean()) {
            orderData = respons.getAsJsonObject("body").get("list").getAsJsonArray().get(0).getAsJsonObject();
        }
        return orderData;
    }

    /**
     * 内部使用查询订单数据接口
     *
     * @param employee
     * @param orderType
     * @param settlementBody
     * @return
     * @throws HttpStatusException
     */
    public JsonObject queryOrderData(Employee employee, String orderType, SettlementBody settlementBody) throws HttpStatusException {
        JsonObject respons = vendorApi.queryInternalOrderData(employee, orderType, settlementBody);
        JsonObject orderData = new JsonObject();
        log.info("内部接口的响应数据:{}", respons);
        if (respons.get("success").getAsBoolean()) {
            orderData = respons.getAsJsonObject("body").get("list").getAsJsonArray().get(0).getAsJsonObject();
        }
        return orderData;
    }

    public String trainTypeMapping(String trainNum) {
        StringBuffer newTrainNum = new StringBuffer(trainNum);
        char keyword = newTrainNum.charAt(0);
        log.info("keyword:{}", keyword);
        String trainType = "";
        switch (keyword) {
            case 'G':
                trainType = "高铁";
                break;
            case 'D':
                trainType = "动车";
                break;
            case 'T':
                trainType = "特快";
                break;
            case 'K':
                trainType = "快车";
                break;
            case 'C':
                trainType = "城际铁路";
                break;
            case ' ':
                trainType = "未知车型";
                break;
        }
        return trainType;
    }

    /**
     * 查询国际国内 两舱设置
     *
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonObject queryBookClass(Employee employee) throws HttpStatusException {
        return vendorApi.queryBookClass(employee);
    }


    /**
     * 查询TMC 人员同步信息
     *
     * @param employee
     * @param tmcChannel
     * @param hlyUserMobile
     * @throws HttpStatusException
     */
    public JsonObject getTMCUser(Employee employee, String tmcChannel, String hlyUserMobile) throws HttpStatusException {
        return vendorApi.tmcUserInfo(employee, tmcChannel, hlyUserMobile);
    }

    /**
     * 查询TMC 申请单同步信息
     *
     * @param employee
     * @param tmcChannel 例：supplyUbtripService优行   supplyCimccTMCService中集  supplyCtripService
     * @param approvalNo 行程单号
     * @throws HttpStatusException
     */
    public JsonObject getTMCPlan(Employee employee, String tmcChannel, String approvalNo) throws HttpStatusException {
        return vendorApi.tmcPlanInfo(employee, tmcChannel, approvalNo);
    }

    /**
     * 读取模板数据
     *
     * @param path 读取数据的路径 建议相对路径   读取到的数据进行 订单号和批次号的重新输入 以及更换订票人的fullname和工号。
     * @return
     */
    public JsonObject getFlightSettlementData(Employee employee, String path, String corpId, String supplierCode) {
        String vendorData = DocumnetUtil.fileReader(path);
        JsonObject vendorObject = new JsonParser().parse(vendorData).getAsJsonObject();
        String orderNo = RandomNumber.getTimeNumber();
        String originalOrderNo = RandomNumber.getTimeNumber();
        vendorObject.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject().addProperty("accBalanceBatchNo", supplierCode + "_" + corpId + "_flight_" + UTCTime.getBeijingDay(0));
        vendorObject.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject().addProperty("bookClerkName", employee.getFullName());
        vendorObject.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject().addProperty("bookClerkEmployeeId", employee.getEmployeeID());
        vendorObject.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject().addProperty("passengerName", employee.getFullName());
        vendorObject.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject().addProperty("passengerEmployeeId", employee.getEmployeeID());
        vendorObject.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject().addProperty("orderNo", orderNo);
        if (!vendorObject.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject().get("detailType").getAsString().equals("出票")) {
            vendorObject.getAsJsonArray("flightSettlementList").get(0).getAsJsonObject().addProperty("originalOrderNo", originalOrderNo);
        }
        return vendorObject;
    }

    /**
     * 读取火车模板结算数据 并修改预订人的工号信息
     *
     * @param employee
     * @param path
     * @param corpId
     * @param supplierCode
     * @return
     */
    public JsonObject getTrainSettlementData(Employee employee, String path, String corpId, String supplierCode) {
        String vendorData = DocumnetUtil.fileReader(path);
        JsonObject vendorObject = new JsonParser().parse(vendorData).getAsJsonObject();
        String orderNo = RandomNumber.getTimeNumber();
        JsonObject trainBaseSettlement = vendorObject.getAsJsonArray("trainSettlementInfos").get(0).getAsJsonObject().getAsJsonObject("trainBaseSettlement");
        trainBaseSettlement.addProperty("accBalanceBatchNo", supplierCode + "_" + corpId + "_train_" + UTCTime.getBeijingDay(0));
        trainBaseSettlement.addProperty("orderNo", orderNo);
        trainBaseSettlement.addProperty("bookClerkName", employee.getFullName());
        trainBaseSettlement.addProperty("bookClerkEmployeeId", employee.getEmployeeID());
        vendorObject.getAsJsonArray("trainSettlementInfos").get(0).getAsJsonObject().getAsJsonArray("trainPassengerInfos").get(0).getAsJsonObject().addProperty("passengerName", employee.getFullName());
        vendorObject.getAsJsonArray("trainSettlementInfos").get(0).getAsJsonObject().getAsJsonArray("trainPassengerInfos").get(0).getAsJsonObject().addProperty("passengerCode", employee.getEmployeeID());
        vendorObject.getAsJsonArray("trainSettlementInfos").get(0).getAsJsonObject().getAsJsonObject("trainBaseSettlement").addProperty("orderNo", orderNo);
        vendorObject.getAsJsonArray("trainSettlementInfos").get(0).getAsJsonObject().getAsJsonObject("trainBaseOrder").addProperty("orderNo", orderNo);
        vendorObject.getAsJsonArray("trainSettlementInfos").get(0).getAsJsonObject().getAsJsonArray("trainPassengerTicketCorrelations").get(0).getAsJsonObject().addProperty("orderNo", orderNo);
        return vendorObject;
    }


    /**
     * 读取酒店模板结算数据
     *
     * @param path 读取数据的路径 建议相对路径   读取到的数据进行 订单号和批次号的重新输入 以及更换订票人的fullname和工号。
     * @return
     */
    public JsonObject getHotelSettlementData(Employee employee, String path, String corpId, String supplierCode) {
        String vendorData = DocumnetUtil.fileReader(path);
        JsonObject vendorObject = new JsonParser().parse(vendorData).getAsJsonObject();
        String orderNo = RandomNumber.getTimeNumber();
        vendorObject.getAsJsonArray("hotelSettlementList").get(0).getAsJsonObject().addProperty("batchNo", supplierCode + "_" + corpId + "_hotel_" + UTCTime.getBeijingDay(0));
        vendorObject.getAsJsonArray("hotelSettlementList").get(0).getAsJsonObject().addProperty("bookClerkName", employee.getFullName());
        vendorObject.getAsJsonArray("hotelSettlementList").get(0).getAsJsonObject().addProperty("bookClerkEmployeeId", employee.getEmployeeID());
        vendorObject.getAsJsonArray("hotelSettlementList").get(0).getAsJsonObject().getAsJsonArray("passengerList").get(0).getAsJsonObject().addProperty("passengerName", employee.getFullName());
        vendorObject.getAsJsonArray("hotelSettlementList").get(0).getAsJsonObject().getAsJsonArray("passengerList").get(0).getAsJsonObject().addProperty("passengerEmployeeId", employee.getFullName());
        vendorObject.getAsJsonArray("hotelSettlementList").get(0).getAsJsonObject().addProperty("orderNo", orderNo);
        return vendorObject;
    }

    /**
     * 读取飞机订单模板数据
     *
     * @param employee
     * @param path
     * @return
     */
    public JsonObject getFlightOrder(Employee employee, String path) {
        String vendorOrderData = DocumnetUtil.fileReader(path);
        JsonObject vendorObject = new JsonParser().parse(vendorOrderData).getAsJsonObject();
        String orderNo = RandomNumber.getTimeNumber();
        String originalOrderNo = RandomNumber.getTimeNumber();
        if (GsonUtil.isExistProperty(vendorObject, "airBaseOrder")) {
            vendorObject.getAsJsonObject("airBaseOrder").addProperty("orderNo", orderNo);
            //如果是改签或者退票的话需要更换下原单号
            if (!vendorObject.getAsJsonObject("airBaseOrder").get("orderType").getAsString().equals("B")) {
                vendorObject.getAsJsonObject("airBaseOrder").addProperty("originalOrderNo", originalOrderNo);
            }
            vendorObject.getAsJsonObject("airBaseOrder").addProperty("employeeId", employee.getEmployeeID());
            vendorObject.getAsJsonObject("airBaseOrder").addProperty("preEmployName", employee.getFullName());
        }
        vendorObject.getAsJsonArray("airFlightInfo").get(0).getAsJsonObject().addProperty("orderNo", orderNo);
        vendorObject.getAsJsonArray("airPassengerInfo").get(0).getAsJsonObject().addProperty("orderNo", orderNo);
        vendorObject.getAsJsonArray("airPassengerInfo").get(0).getAsJsonObject().addProperty("passengerName", employee.getFullName());
        vendorObject.getAsJsonArray("airPassengerInfo").get(0).getAsJsonObject().addProperty("passengeNum", employee.getEmployeeID());
        //检查是否存在改签
        if (GsonUtil.isExistProperty(vendorObject, "airChangeInfo")) {
            vendorObject.getAsJsonArray("airChangeInfo").get(0).getAsJsonObject().addProperty("orderNo", orderNo);
        }
        //检查是否存在退票
        if (GsonUtil.isExistProperty(vendorObject, "airRefundInfo")) {
            vendorObject.getAsJsonArray("airRefundInfo").get(0).getAsJsonObject().addProperty("orderNo", orderNo);
        }
        //检查是否有保险 更换employeeName
        if (GsonUtil.isExistProperty(vendorObject, "airInsurance")) {
            vendorObject.getAsJsonArray("airRefundInfo").get(0).getAsJsonObject().addProperty("employeeName", employee.getFullName());
        }
        return vendorObject;
    }

    /**
     * 读取火车订单模板数据
     *
     * @param employee
     * @param path
     * @return
     */
    public JsonObject getTrainOrder(Employee employee, String path) {
        String vendorOrderData = DocumnetUtil.fileReader(path);
        JsonObject vendorObject = new JsonParser().parse(vendorOrderData).getAsJsonObject();
        String orderNo = RandomNumber.getTimeNumber();
        String originalOrderNo = RandomNumber.getTimeNumber();
        vendorObject.getAsJsonObject("trainOrderBase").addProperty("orderNo", orderNo);
        //如果是改签或者退票的话需要更换下原单号
        if (!vendorObject.getAsJsonObject("trainOrderBase").get("orderType").getAsString().equals("B")) {
            vendorObject.getAsJsonObject("trainOrderBase").addProperty("originalOrderNum", originalOrderNo);
        }
        vendorObject.getAsJsonObject("trainOrderBase").addProperty("employeeNum", employee.getEmployeeID());
        vendorObject.getAsJsonObject("trainOrderBase").addProperty("employeeName", employee.getFullName());
        vendorObject.getAsJsonArray("trainOrderTicketInfos").get(0).getAsJsonObject().addProperty("orderNo", orderNo);
        vendorObject.getAsJsonArray("trainOrderPassengerInfos").get(0).getAsJsonObject().addProperty("orderNo", orderNo);
        vendorObject.getAsJsonArray("trainOrderPassengerInfos").get(0).getAsJsonObject().addProperty("passengerName", employee.getFullName());
        vendorObject.getAsJsonArray("trainOrderPassengerInfos").get(0).getAsJsonObject().addProperty("passengeNum", employee.getEmployeeID());
        vendorObject.getAsJsonArray("trainOrderSequenceInfos").get(0).getAsJsonObject().addProperty("orderNo", orderNo);
        //检查是否存在改签
        if (GsonUtil.isExistProperty(vendorObject, "trainOrderChangeInfos")) {
            vendorObject.getAsJsonArray("trainOrderChangeInfos").get(0).getAsJsonObject().addProperty("orderNo", orderNo);
        }
        //检查是否存在退票
        if (GsonUtil.isExistProperty(vendorObject, "trainOrderRefundInfos")) {
            vendorObject.getAsJsonArray("trainOrderRefundInfos").get(0).getAsJsonObject().addProperty("orderNo", orderNo);
        }
        return vendorObject;
    }

    /**
     * 读取酒店订单模板数据
     *
     * @param employee
     * @param path
     * @return
     */
    public JsonObject getHotelOrder(Employee employee, String path) {
        String vendorOrderData = DocumnetUtil.fileReader(path);
        JsonObject vendorObject = new JsonParser().parse(vendorOrderData).getAsJsonObject();
        String orderNo = RandomNumber.getTimeNumber();
        String originalOrderNo = RandomNumber.getTimeNumber();
        vendorObject.getAsJsonObject("hotelOrderBase").addProperty("orderNo", orderNo);
        //如果是改签或者退票的话需要更换下原单号
        if (!vendorObject.getAsJsonObject("hotelOrderBase").get("orderType").getAsString().equals("B")) {
            vendorObject.getAsJsonObject("hotelOrderBase").addProperty("originalOrderNum", originalOrderNo);
        }
        vendorObject.getAsJsonObject("hotelOrderBase").addProperty("employeeId", employee.getEmployeeID());
        vendorObject.getAsJsonObject("hotelOrderBase").addProperty("employeeName", employee.getFullName());
        vendorObject.getAsJsonArray("hotelOrderPassengerInfos").get(0).getAsJsonObject().addProperty("orderNo", orderNo);
        vendorObject.getAsJsonArray("hotelOrderPassengerInfos").get(0).getAsJsonObject().addProperty("passengerName", employee.getFullName());
        vendorObject.getAsJsonArray("hotelOrderPassengerInfos").get(0).getAsJsonObject().addProperty("passengerNum", employee.getEmployeeID());
        //检查是否存在超标信息
        if (GsonUtil.isExistProperty(vendorObject, "hotelOrderExceedInfos")) {
            vendorObject.getAsJsonArray("hotelOrderExceedInfos").get(0).getAsJsonObject().addProperty("orderNo", orderNo);
        }
        return vendorObject;
    }


    /**
     * 读取用车订单模板数据
     *
     * @param employee
     * @param path
     * @return
     */
    public JsonObject getCarOrder(Employee employee, String path) {
        String vendorOrderData = DocumnetUtil.fileReader(path);
        JsonObject vendorObject = new JsonParser().parse(vendorOrderData).getAsJsonObject();
        String orderNo = RandomNumber.getTimeNumber();
        vendorObject.getAsJsonObject("carBaseOrder").addProperty("orderNo", orderNo);
        vendorObject.getAsJsonObject("carBaseOrder").addProperty("bookClerkEmployeeId", employee.getEmployeeID());
        vendorObject.getAsJsonObject("carBaseOrder").addProperty("bookClerkName", employee.getFullName());
        vendorObject.getAsJsonArray("carOrderPassengerInfos").get(0).getAsJsonObject().addProperty("passengerName", employee.getFullName());
        vendorObject.getAsJsonArray("carOrderPassengerInfos").get(0).getAsJsonObject().addProperty("passengerCode", employee.getEmployeeID());
        return vendorObject;
    }

    /**
     * 读取用车模板结算数据 并修改预订人的工号信息
     *
     * @param employee
     * @param path
     * @param corpId
     * @param supplierCode
     * @return
     */
    public JsonObject getCarSettlementData(Employee employee, String path, String corpId, String supplierCode) {
        String vendorData = DocumnetUtil.fileReader(path);
        JsonObject vendorObject = new JsonParser().parse(vendorData).getAsJsonObject();
        String orderNo = RandomNumber.getTimeNumber();
        JsonObject carBaseSettlement = vendorObject.getAsJsonArray("carSettlementInfos").get(0).getAsJsonObject().getAsJsonObject("carBaseSettlement");
        //用车删除了批次号
//        carBaseSettlement.addProperty("accBalanceBatchNo", supplierCode + "_" + corpId + "_car_" + UTCTime.getBeijingDay(0));
        carBaseSettlement.addProperty("orderNo", orderNo);
        carBaseSettlement.addProperty("bookClerkName", employee.getFullName());
        carBaseSettlement.addProperty("bookClerkEmployeeId", employee.getEmployeeID());
        vendorObject.getAsJsonArray("carSettlementInfos").get(0).getAsJsonObject().getAsJsonArray("carPassengerInfos").get(0).getAsJsonObject().addProperty("passengerName", employee.getFullName());
        vendorObject.getAsJsonArray("carSettlementInfos").get(0).getAsJsonObject().getAsJsonArray("carPassengerInfos").get(0).getAsJsonObject().addProperty("passengerCode", employee.getEmployeeID());
        return vendorObject;
    }
}

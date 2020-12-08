package com.hand.api;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicconstant.ApiPath;

import java.util.HashMap;
import java.util.Map;

public class VendorInfoApi extends BaseRequest {

    /**
     * 通用 sso 接口
     * get /sso
     *
     * @param employee
     * @param supplierOID        查询出属于哪个供应商
     * @param vendorsName        供应商EN
     * @param businessCode       单据号
     * @param pageType           指定登录页面   (机票，酒店，火车，机票订单，酒店订单，火车订单 )
     * @param vendorType       消费上类型，机酒火
     * @param itineraryDirection 往返有效 1001：去程 1002：返程
     * @param direction          H5/WEB登录
     * @param lng                经度 需支持(百动、小秘书)
     * @param lat                维度 需支持(百动、小秘书)
     * @param orderId            订单id
     * @param startCity          出发城市
     * @param endCity            到达城市
     * @param startDate          出发时间 yyyy-MM-dd
     * @param endDate            到达时间
     * @param forCorp            0：因公出行，1 ：因私出行
     * @param flightSearchType   S：单程 D：往返
     * @return
     * @throws HttpStatusException
     */
    public String vendorInfoSso(Employee employee,
                                String supplierOID,
                                String vendorsName,
                                String businessCode,
                                Integer pageType,
                                String vendorType,
                                Integer itineraryDirection,
                                Double lng,
                                Double lat,
                                String direction,
                                String orderId,
                                String startCity,
                                String endCity,
                                String startDate,
                                String endDate,
                                String forCorp,
                                String flightSearchType) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.SSO;
        Map<String, String> datas = new HashMap<>();
        if (supplierOID != null) {
            datas.put("supplierOID", supplierOID);
        }
        if (vendorsName != null) {
            datas.put("vendorsName", vendorsName);
        }
        if (businessCode != null) {
            datas.put("businessCode", businessCode);
        }
        if (pageType != null) {
            datas.put("pageType", String.valueOf(pageType));
        }
        if (vendorType != null) {
            datas.put("vendorType", vendorType);
        }
        if (null != itineraryDirection) {
            datas.put("itineraryDirection", String.valueOf(itineraryDirection));
        }
        if (lng != null) {
            datas.put("lng", String.valueOf(lng));
        }
        if (lat != null) {
            datas.put("lat", String.valueOf(lat));
        }
        if (direction != null) {
            datas.put("direction", direction);
        }
        if (orderId != null) {
            datas.put("orderId", orderId);
        }
        if (startCity != null) {
            datas.put("startCity", startCity);
        }
        if (endCity != null) {
            datas.put("endCity", endCity);
        }
        if (startDate != null) {
            datas.put("startDate", startDate);
        }
        if (endDate != null) {
            datas.put("endDate", endDate);
        }
        if (forCorp != null) {
            datas.put("forCorp", forCorp);
        }
        if (flightSearchType != null) {
            datas.put("flightSearchType", flightSearchType);
        }
        String res = doGet(url, getHeader(employee.getAccessToken()), datas, employee);
        return res;
    }

    /**
     * 统一单点登录接口
     * get /sso/common
     *
     * @param supplierOID      消费商oid
     * @param newVendorsName   消费商名称
     * @param pageType         跳转页面，首页 订单 审批单
     * @param vendorType       消费上类型，机酒火
     * @param direction        web、h5
     * @param orderId          订单id
     * @param businessCode     审批单号
     * @param startCity        出发城市
     * @param endCity          到达城市
     * @param startDate        出发日期
     * @param endDate          到达日期
     * @param forCorp          因公0 因私1
     * @param flightSearchType 单程S 往返D
     * @return
     */
    public String vendorInfoSsoCommon(Employee employee,
                                      String supplierOID,
                                      String newVendorsName,
                                      Integer pageType,
                                      String vendorType,
                                      String direction,
                                      String orderId,
                                      String businessCode,
                                      String startCity,
                                      String endCity,
                                      String startDate,
                                      String endDate,
                                      String forCorp,
                                      String flightSearchType) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.SSO_COMMON;
        Map<String, String> datas = new HashMap<>();
        if (supplierOID != null) {
            datas.put("supplierOID", supplierOID);
        }
        if (newVendorsName != null) {
            datas.put("newVendorsName", newVendorsName);
        }
        if (pageType != null) {
            datas.put("pageType", String.valueOf(pageType));
        }
        if (vendorType != null) {
            datas.put("vendorType", vendorType);
        }
        if (direction != null) {
            datas.put("direction", direction);
        }
        if (orderId != null) {
            datas.put("orderId", orderId);
        }
        if (businessCode != null) {
            datas.put("businessCode", businessCode);
        }
        if (startCity != null) {
            datas.put("startCity", startCity);
        }
        if (endCity != null) {
            datas.put("endCity", endCity);
        }
        if (startDate != null) {
            datas.put("startDate", startDate);
        }
        if (endDate != null) {
            datas.put("endDate", endDate);
        }
        if (forCorp != null) {
            datas.put("forCorp", forCorp);
        }
        if (flightSearchType != null) {
            datas.put("flightSearchType", flightSearchType);
        }
        String res = doGet(url, getHeader(employee.getAccessToken()), datas, employee);
        return res;
    }


}

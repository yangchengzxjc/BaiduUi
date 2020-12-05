package com.hand.api;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicconstant.ApiPath;

import java.util.HashMap;
import java.util.Map;

public class VendorInfoApi extends BaseRequest {

    /**
     * info 通用sso入口
     * get /sso
     *
     * @param employee
     * @param roleType
     * @param supplierOID
     * @param realmId
     * @param companyOID
     * @param emnum
     * @param pageType
     * @param direction
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
     * @param supplierOID
     * @param newVendorsName
     * @param pageType
     * @param vendorType
     * @param direction
     * @param orderId
     * @param businessCode
     * @param startCity
     * @param endCity
     * @param startDate
     * @param endDate
     * @param forCorp
     * @param flightSearchType
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

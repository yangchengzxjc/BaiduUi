package com.test.api.method.VendorMethod;

import com.hand.api.VendorInfoApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;

public class VendorInfo {
    private VendorInfoApi vendorInfoApi;

    public VendorInfo() {
        // 构造方法 初始化实例对象
        vendorInfoApi = new VendorInfoApi();
    }

    /**
     * 封装 sso 接口请求
     * @param employee
     * @param supplierOID
     * @param vendorsName
     * @param businessCode
     * @param pageType
     * @param vendorType
     * @param itineraryDirection
     * @param lng
     * @param lat
     * @param direction
     * @param orderId
     * @param startCity
     * @param endCity
     * @param startDate
     * @param endDate
     * @param forCorp
     * @param flightSearchType
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
        return vendorInfoApi.vendorInfoSso(employee, supplierOID, vendorsName, businessCode, pageType, vendorType,
                itineraryDirection, lng, lat, direction, orderId, startCity, endCity,
                startDate, endDate, forCorp, flightSearchType);
    }

    /**
     *
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
                                      String flightSearchType) throws HttpStatusException{
        return vendorInfoApi.vendorInfoSsoCommon(employee, supplierOID, newVendorsName, pageType, vendorType,
                direction, orderId, businessCode, startCity, endCity, startDate, endDate, forCorp, flightSearchType);
    }
}

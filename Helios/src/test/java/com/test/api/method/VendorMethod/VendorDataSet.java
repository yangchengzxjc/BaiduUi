package com.test.api.method.VendorMethod;

import com.google.gson.JsonObject;
import com.hand.basicObject.Employee;

/**
 * @Author peng.zhang
 * @Date 2020/11/10
 * @Version 1.0
 **/
public interface VendorDataSet<T> {

    /**
     * 写了一个接口 可以实现它来写自己的数据拼装
     * @param employee
     * @param orderData
     * @param supplierName
     * @param supplierCode
     * @return
     */
    public T setOrderData(Employee employee,JsonObject orderData,String supplierName,String supplierCode);

    public T setSettlementData(Employee employee, JsonObject settlement, String supplierCode, String corpId);

}

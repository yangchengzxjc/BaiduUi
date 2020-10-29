package com.test.api.method.VendorMethod;

import com.test.api.method.InfraStructure;
import com.hand.basicObject.supplierObject.carOrderInfo.*;

/**
 * @Author Dazzle
 * @Date 2020/10/23
 */
public class CarOrder {
    private InfraStructure infraStructure;

    public CarOrder(){
        infraStructure = new InfraStructure();
    }

    /**
     * 订单基本信息
     * @return
     */
    public CarBaseInfo setCarBaseInfo(){
        CarBaseInfo carBaseInfo = CarBaseInfo.builder()
                .build();
        return carBaseInfo;
    }

}

package com.hand.basicObject.supplierObject.carOrderInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CarOrderInfoEntity {
    //订单基本信息
    private CarBaseInfo carBaseInfo;
    //用车乘客信息
    private CarPassengerInfo carPassengerInfo;
}

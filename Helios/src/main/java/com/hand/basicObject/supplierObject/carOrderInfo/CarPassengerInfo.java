package com.hand.basicObject.supplierObject.carOrderInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CarPassengerInfo {
    //出行人编号
    private String passengerNo;
    //出行人姓名
    private String passengerName;
    //员工编号
    private String passengerCode;
    //员工成本中心1
    private String passengerCostCenter1;
    //员工成本中心2
    private String passengerCostCenter2;
    //员工成本中心3
    private String passengerCostCenter3;
    //员工成本中心4
    private String passengerCostCenter4;
    //员工成本中心5
    private String passengerCostCenter5;
    //员工成本中心6
    private String passengerCostCenter6;
    //项目号
    private String itemNumber;
    //出行目的
    private String tripPurpose;
    //员工部门集合，最大10
    private List<String> departments;
}

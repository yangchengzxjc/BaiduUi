package com.hand.basicObject.supplierObject.hotelOrderSettlementInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2020/8/26
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerInfo {
    //入住人信息
    private String passengerName;
    //入住人工号
    private String passengerEmployeeId;
    //入住人部门集合   有顺序时为空的请传空字符串，保证顺序正确。
    //
    //如：部门1 = xx公司、部门3 = xx项目
    //
    //传值时需要传 ["xx公司","","xx项目"
    private List<String> passengerDepts;
    //入住人成本中心
    private String costCenter1;
    private String costCenter2;
    private String costCenter3;
    private String costCenter4;
    private String costCenter5;
    private String costCenter6;
}

package com.test.api.method.VendorMethod;

import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.TrainOrderInfo.TrainBaseOrder;
import com.hand.utils.UTCTime;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2020/9/10
 * @Version 1.0
 **/
public class OrderData {

    /**
     * 火车订单基本信息
     * @param employee
     * @param orderStatusName
     * @param orderType
     * @param orderNo
     * @param bookChannel
     * @param bookType
     * @param payType
     * @return
     */
    public TrainBaseOrder getTrainBaseOrder(Employee employee,String orderStatusName,String orderType,String orderNo,String bookChannel,String bookType,String payType){
        String paymentType ="";
        String accountType ="";
        if(bookType.equals("C")){
            paymentType="M";
            accountType="C";
        }else{
            paymentType="N";
            accountType="P";
        }
        HashMap<String,String> orderStatusMapping =new HashMap<>();
        orderStatusMapping.put("未提交","N");
        orderStatusMapping.put("待支付","WP");
        orderStatusMapping.put("支付处理中","PP");
        orderStatusMapping.put("支付失败","PF");
        orderStatusMapping.put("购票中","TP");
        orderStatusMapping.put("已购票","TD");
        orderStatusMapping.put("出票失败","TF");
        orderStatusMapping.put("已取消","C");
        //订单基本信息
        TrainBaseOrder trainBaseOrder = TrainBaseOrder.builder()
                .orderType(orderType)
                .orderNo(orderNo)
                .originalOrderNum("")
                .supplierName("中集商旅")
                .supplierCode("cimccTMC")
                .approvalCode("TA"+System.currentTimeMillis())
                .orderStatusName(orderStatusName)
                .orderStatusCode(orderStatusMapping.get(orderStatusName))
                .tenantCode(employee.getTenantId())
                .tenantName(employee.getTenantName())
                .employeeNum(employee.getEmployeeID())
                .employeeName(employee.getFullName())
                .companyName(employee.getCompanyName())
                .companyCode(employee.getCompanyCode())
                .departmentName(employee.getDepartmentName())
                .bookChannel(bookChannel)
                .bookType(bookType)
                .payType(payType)
                .createTime(UTCTime.getBeijingTime(0,0))
                .payTime(UTCTime.getBeijingTime(0,0))
                .successTime(UTCTime.getBeijingTime(0,0))
                .paymentType(paymentType)
                .accountType(accountType)
                .currency("CNY")
                .totalAmount(new BigDecimal(500).setScale(2))
                .contactName(employee.getFullName())
                .contactPhone(employee.getMobile())
                .contactEmail(employee.getEmail())
                .remark("")
                .build();
        return trainBaseOrder;
    }
}

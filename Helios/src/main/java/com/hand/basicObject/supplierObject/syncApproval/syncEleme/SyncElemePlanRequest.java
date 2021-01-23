/**
 * Copyright (c) 2020.year. Shanghai Zhenhui Information Technology Co,. ltd. All rights are reserved.
 *
 * @project Helios
 * @author pan.jiang@huilianyi.com
 * @Date 2020-12-29 - 21:13
 * @description:
 */

package com.hand.basicObject.supplierObject.syncApproval.syncEleme;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class SyncElemePlanRequest {

    /** example:
     * {
     * 	"businessCode": "TA00416767-1-E",
     * 	"status": 1,
     * 	"travelDiningRequestList": [{
     * 			"companyOID": "9d6ba226-27f3-4c9f-a572-d01500a29c75",
     * 			"isAllEmploy": 2,
     * 			"employeeIdAmountsDtos": [{
     * 					"sendAmount": 100,
     * 					"phoneNum": "14082978000"
     *                                }
     * 			],
     * 			"effectiveStartTime": 1609171200000,
     * 			"effectiveEndTime": 1609689599000,
     * 			"dishTypes": [1, 2],
     * 			"alivableType": 2,
     * 			"isLimitTime": 1,
     * 			"limitStartTime": 1609203600000,
     * 			"limitEndTime": 1609250400000,
     * 			"useType": "1218722645100748802",
     * 			"mealCity": ["1"],
     * 			"mealCityCode": ["1"],
     * 			"applyAmount": 1,
     * 			"mealPeopleCount": 1,
     * 			"applicantUser": {
     * 				"staffCode": "S0001",
     * 				"mobile": "14082978000",
     * 				"email": "17791432320@163.com"
     * 				 			},
     * 			"bookUser": [{
     * 					"staffCode": "S0001",
     * 					"mobile": "14082978000",
     * 					"email": "17791432320@163.com"
     * 				}
     * 			],
     * 			"title": "测试饿了么",
     * 			"remark": "eleme-test",
     * 			"pushTimeStamp": "1609246290063"
     * 		}
     * 	]
     * }
     */

    private String businessCode;

    //状态 (1 有效 0 失效)
    private Integer status = 1;

    private List<TravelDiningRequest> travelDiningRequestList;

    @Getter
    @Setter
    public static class TravelDiningRequest {

        private String companyOID;
        /**
         * 企业ID
         */
        private Long enterpriseId;
        /**
         * 是否是企业全部员工 1是  2否  默认传2
         */
        private Integer isAllEmploy;
        /**
         * 部分员工情况 n  统一订票时，此字段传入为所有参与人，需要消费商根据外层的bookerClerk选出订餐人，累加所有金额，最终只传订餐人
         */
        private List<EmployeeIdAmountsDto> employeeIdAmountsDtos;
        /**
         * 生效开始时间 y 用餐行程开始时间 时间点为0点0分0秒
         */
        private Long effectiveStartTime;
        /**
         * 生效结束时间 y 用餐行程结束时间 时间点为23点59分59秒
         */
        private Long effectiveEndTime;
        /**
         * 餐补金额(单位分) n isAllEmploy 为1时传入  为2时不传
         */
        private Long sendAmount;
        /**
         * 用餐场景 1；外卖 2；到点 3；团餐（多个逗号隔开） y
         */
        private List<Integer> dishTypes;
        /**
         * 0;工作日 1；非工作日 2；全部日期 n
         */
        private Integer alivableType;

        /**
         * 外卖/到店消费时间 否是限制 1；是 0；否  y
         */
        private Integer isLimitTime;
        /**
         * 外卖/到店消费限制时间(时分秒)1970-01-01 n
         */
        private Long limitStartTime;
        /**
         * 外卖/到店消费限制时间(时分秒)1970-01-01 n
         */
        private Long limitEndTime;

        /**
         * 最多可使用次数(为空是不限使用次数，如果使用次数不限制，不能共享，shareSwitch设置无效)
         */
        private Integer maxUseTimes;
        /**
         * 外卖送餐地址是否可以自定义 1；是 0；否 n
         */
        private Integer isAddressLimit;
        /**
         * 预留字段(50字符以内) n
         */
        private String addition;
        /**
         * 共享开关默认值是0
         */
        private Integer shareSwitch;
        /**
         * 设置餐补名称默认值是餐补
         */
        private String allowanceName;

        /**
         * 用餐类型
         * 与申请单组提供的用餐类型查询接口一致
         * 2020.12迭代RDC-59146
         */
        private String useType;

        /**
         * 可用餐城市
         * 2020.12迭代RDC-59146
         */
        private List<String> mealCity;

        /**
         * 可用餐城市code
         * 2020.12迭代RDC-59146
         */
        private List<String> mealCityCode;

        /**
         * 申请金额 11.2
         * 2020.12迭代RDC-59146
         */
        private BigDecimal applyAmount;

        /**
         * 可用餐人数
         * 2020.12迭代RDC-59146
         */
        private int mealPeopleCount;

        /**
         * 申请人
         */
        private ElemeConUsers applicantUser;

        /**
         * 预定人( 对应申请单:参与人列表-participantList )
         */
        private List<ElemeConUsers> bookUser;

        /**
         * 事由 从申请单主表获取
         */
        private String title;

        /**
         * 备注 从申请单主表获取
         */
        private String remark;

        /**
         * 申请单推送时间 rdc task 54081
         */
        private String pushTimeStamp;

    }

    @Data
    public static class ElemeConUsers {
        /**
         * 工号
         */
        private String staffCode;

        /**
         * 手机号
         */
        private String mobile;

        /**
         * 邮箱
         */
        private String email;

    }

    @Data
    public static class EmployeeIdAmountsDto {
        /**
         * 该员工发放金额
         */
        private Long sendAmount;
        /**
         * 员工手机号
         */
        private String phoneNum;
    }
}

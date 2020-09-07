package com.hand.basicObject.supplierObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author peng.zhang
 * @Date 2020/8/27
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementBody {
    //批次 消费商约定的应结账期的标示。用于客户统计最终的应付明细。批次生成逻辑：supplierCode_corpId_产品_结算开始日期
    //
    //举例：CCTRIP_testcomapny_flight_20200501
    private String accBalanceBatchNo;

    //开始时间  yyyy-MM-dd HH:mm:ss
    private String dateFrom;
    //结束时间   结算生成时间
    private String dateTo;
    //订单号  为空是查询所有数据
    private String orderNo;

    //汇联易公司oid
    private String companyOid;
    //供应商code
    private String supplierCode;
    //结算主键
    private String recordId;
    //页码
    private Integer page;
    //每页数量
    private Integer size;
}

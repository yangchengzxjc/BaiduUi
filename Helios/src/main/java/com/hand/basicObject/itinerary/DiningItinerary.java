package com.hand.basicObject.itinerary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiningItinerary {


    /**
     * 行程oid
     */
    private UUID itineraryOID;

    /**
     * 供应商oid
     */
    private UUID supplierOID;

    /**
     * 用餐类型id（用餐场景）
     */
    private Long diningSceneId;

    /**
     * 用餐城市名称
     */
    private String cityName;

    /**
     * 用餐城市code 汇联易标准城市code
     */
    private String cityCode;

    /**
     * 用餐日期开始时间
     */
    private String startDate;

    /**
     * 用餐日期结束时间
     */
    private String endDate;

    /**
     * 用餐标准金额
     */
    private Double standardAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否来自上一版本申请单
     */
    private Boolean isExtend;

    /**
     * 是否被停用
     */
    private Boolean disabled;

    /**
     * 订票状态（1.隐藏按钮2.显示灰色按钮3.显示可订票按钮)
     */
    private Integer bookedStatus;

    /**
     * 失效日期
     */
    private DateTime expiredDate;

}

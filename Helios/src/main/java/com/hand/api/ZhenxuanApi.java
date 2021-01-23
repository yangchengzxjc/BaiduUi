/**
 * Copyright (c) 2020.year. Shanghai Zhenhui Information Technology Co,. ltd. All rights are reserved.
 *
 * @project Helios
 * @author pan.jiang@huilianyi.com
 * @Date 2021-01-05 - 21:24
 * @description:
 */

package com.hand.api;

import com.google.gson.Gson;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicConstant.ApiPath;
import com.hand.basicObject.Employee;
import com.hand.basicObject.zhenxuan.HotelSearchDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ZhenxuanApi extends BaseRequest {

    /**
     *
     * @param employee
     * @param adcode 310000
     * @return
     * @throws HttpStatusException
     * res:
     * {
     * 	"success": true,
     * 	"code": 200,
     * 	"message": "成功",
     * 	"body": {
     * 		"districtList": [{
     * 				"type": "region",
     * 				"adcode": "310151",
     * 				"name": "崇明区",
     * 				"location": "121.397516,31.626946",
     * 				"subList": [{
     * 						"type": "business",
     * 						"name": "吉买盛购物中心(8壹广场店)",
     * 						"location": "121.396736,31.620806",
     * 						"address": "八一路206号8壹广场F2层37"
     *                                        }
     * 				]
     * 			}
     * 		],
     * 		"trafficList": [{
     * 				"type": "car",
     * 				"adcode": "310107",
     * 				"name": "交通大众长途汽车站",
     * 				"location": "121.399315,31.261634",
     * 				"address": "桃浦路168号"
     * 			}
     * 		],
     * 		"subwayList": [{
     * 				"type": null,
     * 				"name": "1号线",
     * 				"subList": [{
     * 						"type": "subway",
     * 						"adcode": "310101",
     * 						"name": "新闸路",
     * 						"location": "121.468151,31.238373"
     *                    }
     * 				]
     * 			}
     * 		]
     * 	}
     * }
     */
    public String getHotelMap(Employee employee, String adcode) throws HttpStatusException {
        String url = employee.getEnvironment().getZhenxuanOpenURL() + ApiPath.HOTEL_MAP_PLACE;
        HashMap<String, String> param = new HashMap<>();
        param.put("adcode",adcode);
        return doGet(url, getHeader(employee.getAccessToken()), param, employee);
    }

    /**
     *
     * @param employee
     * @param hotelSearchDTO
     * @return
     * @throws HttpStatusException
     */
    public String searchHotel(Employee employee, HotelSearchDTO hotelSearchDTO) throws HttpStatusException{
        String url = employee.getEnvironment().getZhenxuanOpenURL() + ApiPath.HOTEL_SEARCH;
        return doPost(url, getHeader(employee.getAccessToken()), null, new Gson().toJson(hotelSearchDTO),null, employee);
    }
}

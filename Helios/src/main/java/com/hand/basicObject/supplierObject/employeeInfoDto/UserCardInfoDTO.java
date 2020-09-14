package com.hand.basicObject.supplierObject.employeeInfoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class UserCardInfoDTO {
    //证件号码
    private String cardNo;
    //证件类型
    private String cardType;
    //证件名称
    private String cardTypeName;
    //证件有效期
    private String IDCardTimelimit;
    //firstName
    private String firstName;
    //listName
    private String lastName;
}
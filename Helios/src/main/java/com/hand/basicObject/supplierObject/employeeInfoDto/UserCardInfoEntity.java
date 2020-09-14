package com.hand.basicObject.supplierObject.employeeInfoDto;

import com.hand.basicconstant.CardType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCardInfoEntity {
    //证件类型
    private CardType cardType;
    //证件OID
    private String contactCardOID;
    //性别  男：0  女：1
    private String gender;
    //生日
    private String birthday;
    //国籍
    private String nationalityCode;
    //是否默认证件
    private Boolean cardDefault;
    //是否启用
    private Boolean enable;
    //firstName   除身份证军人证外需要set firstName
    private String firstName;
    //listName;
    private String lastName;
    //卡号
    private String cardNo;
    //旧卡号
    private String originalCardNo;
    //用户oid
    private String userOID;
    //失效日期
    private String cardExpiredTime;

}

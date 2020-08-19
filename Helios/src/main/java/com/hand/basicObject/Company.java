package com.hand.basicObject;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Company {
    //公司code
    String companyCode;
    //所属法人id
    String legalEntityId;
    //上级公司id
    String parentCompanyId;
    //所属国家
    String countryCode;
    //顺序号
    long sequence;
    //公司名称
    String companyName;
    //公司语言
    String companyLanguage;
    //公司地址
    String companyAddress;
    //公司名称多语言
    JsonObject companyI18n;
    //有效日期从
    String startDateActive;
    //有效日期至
    String endDateActive;
    //公司状态
    boolean enabled;

    public Company() {

    }
}

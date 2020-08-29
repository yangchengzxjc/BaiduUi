package com.hand.basicObject.infrastructure.setOfBooks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SetOfBooks {
    //账套id
    String id;
    //账套名称
    String setOfBooksName;
    //账套代码
    String setOfBooksCode;
    //账套id
    String setOfBooksId;
    //会计期code
    String periodSetCode;
    //会计期id
    String periodSetId;
    //科目表id
    String accountSetId;
    //科目表code
    String accountSetCode;
    //本位币code
    String functionalCurrencyCode;
    //状态
    boolean enabled;
    //账套名称多语言
    JsonObject i18n;

    public SetOfBooks(){

    }

}

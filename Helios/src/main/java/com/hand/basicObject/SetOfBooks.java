package com.hand.basicObject;

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
    //会计期code
    String periodSetCode;
    //科目表id
    String accountSetId;
    //本位币code
    String functionalCurrencyCode;
    //状态
    boolean enabled;

    public SetOfBooks(){

    }

}

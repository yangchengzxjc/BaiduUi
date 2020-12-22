package com.hand.basicObject.component;

import lombok.Data;

/**
 * @Author peng.zhang
 * @Date 2020/12/22
 * @Version 1.0
 **/
@Data
public class LoanBillComponent {

        private String loanTypeId;
        private Integer payeeType;
        private String payeeId;
        private String payeeAccountNumber;
        private String payeeAccountName;
        private double amount;
        private String planedRepaymentDate;
        private String paymentType;
        private String remark;
        private String loanBillId;
        private String ownerOid;
        private String loanTypeName;
        private String payeeName;
        private String payeeCode;
        private String payeeBankCode;
        private String payeeAccountType;
        private String currencyCode;
}

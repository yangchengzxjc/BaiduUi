package com.hand.basicObject.infrastructure.employee;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeExtendedFields {
    String bizType;

    String bizTypeId;

    String formValueOID;

    String bizOID;

    String fieldOID;

    String fieldName;

    String fieldType;

    String fieldTypeId;

    String name;

    String value;

    String showValue;

    String valueCode;

    String createdDate;

    String lastModifiedDate;

    String fieldContent;

    String formOID;

    String referenceOID;

    String messageKey;

    String dataSource;

    int sequence;

    int systemSequence;

    boolean required;

    String hide;

    boolean isReadOnly;

    String fieldConstraint;

    String fieldCode;

    public EmployeeExtendedFields() {

    }
}

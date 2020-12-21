package com.hand.basicConstant;

import lombok.Data;

/**
 * @Author peng.zhang
 * @Date 2020/12/18
 * @Version 1.0
 **/
@Data
public class Receript {


    //手录发票
    public static final String handRecept = "{\"invoiceTypeNo\":\"10\", \"isHandNew\":true, \"cardsignType\":\"HAND\", \"billingNo\":\"21092338\", \"billingCode\":\"061002001111\", \"checkCode\":\"303353\", \"slicingAttachmentOID\":null, \"invoiceCode\":\"061002001111\", \"invoiceNumber\":\"21092338\", \"invoiceDate\":\"2020-11-09T03:48:14.348Z\", \"owner\":\"张鹏\", \"billingTime\":\"1604893694\", \"receiptTypeNo\":\"10\", \"slicingAttachment\":null, \"receiptOwner\":\"张鹏\", \"moreInfo\":\"{}\", \"showUserInfo\":\"\", \"internalStaff\":\"\", \"agentTag\":\"\",\"domesticPassengers\":\"\"}";
    //备用发票信息
    public static final String handReceipt2 = "{\"invoiceTypeNo\":\"10\",\"isHandNew\":true,\"cardsignType\":\"HAND\",\"billingNo\":\"21093068\",\"billingCode\":\"061002001111\",\"checkCode\":\"413706\",\"invoiceCode\":\"061002001111\",\"invoiceNumber\":\"21093068\",\"invoiceDate\":\"2020-11-10T06:06:49.803Z\",\"billingTime\":\"1604988410\",\"receiptTypeNo\":\"10\",\"moreInfo\":\"{}\",\"showUserInfo\":\"\",\"internalStaff\":\"\",\"agentTag\":\"\",\"domesticPassengers\":\"\"}";

    //员工ocr  查验发票
    public static final String ocrReceipt = "src/main/resources/receiptInfo/receipt.pdf";

    //财务ocr  查验发票
    public static final String scanOcrReceipt = "src/main/resources/receiptInfo/scanReceipt.pdf";

}

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
    //发票四
    public static final String receipt4 = "src/main/resources/receiptInfo/receipt4.pdf";
    public static final String receipt5 = "src/main/resources/receiptInfo/receipt5.pdf";
    public static final String receipt6 = "src/main/resources/receiptInfo/receipt6.jpg";
    //电信发票
    public static final String dianxinReceipt = "src/main/resources/receiptInfo/dianxin.pdf";
    //手录假发票信息,测试发票连号
    //多税率发票
    public static final String moreTaxRateReceipt = "src/main/resources/receiptInfo/duoshuilv.png";
    public static final String HANDRECEIPT1 = "{\"invoiceTypeNo\":\"10\",\"isHandNew\":true,\"tmpFee\":0,\"cardsignType\":\"HAND\",\"billingNo\":\"21092020\",\"billingCode\":\"061002001111\",\"checkCode\":\"413706\",\"invoiceCode\":\"061002001111\",\"invoiceNumber\":\"21092020\",\"invoiceDate\":\"2020-12-07T08:45:06.515Z\",\"priceTaxAmount\":10,\"billingTime\":\"1607330707\",\"receiptTypeNo\":\"10\",\"fee\":1000,\"moreInfo\":\"{}\",\"showUserInfo\":\"\",\"internalStaff\":\"\",\"agentTag\":\"\",\"domesticPassengers\":\"\"}";
    public static final String HANDRECEIPT2 = "{\"invoiceTypeNo\":\"10\",\"isHandNew\":true,\"tmpFee\":0,\"cardsignType\":\"HAND\",\"billingNo\":\"21092021\",\"billingCode\":\"061002001111\",\"checkCode\":\"413704\",\"invoiceCode\":\"061002001111\",\"invoiceNumber\":\"21092021\",\"invoiceDate\":\"2020-12-07T08:45:06.515Z\",\"priceTaxAmount\":10,\"billingTime\":\"1607330707\",\"receiptTypeNo\":\"10\",\"fee\":1000,\"moreInfo\":\"{}\",\"showUserInfo\":\"\",\"internalStaff\":\"\",\"agentTag\":\"\",\"domesticPassengers\":\"\"}";
    public static final String HANDRECEIPT4 = "{\"invoiceTypeNo\":\"10\",\"isHandNew\":true,\"tmpFee\":0,\"cardsignType\":\"HAND\",\"billingNo\":\"%s\",\"billingCode\":\"061002001111\",\"checkCode\":\"%s\",\"invoiceCode\":\"061002001111\",\"invoiceNumber\":\"%s\",\"invoiceDate\":\"%s\",\"priceTaxAmount\":100,\"billingTime\":\"%s\",\"receiptTypeNo\":\"10\",\"fee\":100,\"moreInfo\":\"{}\",\"showUserInfo\":\"\",\"internalStaff\":\"\",\"agentTag\":\"\",\"domesticPassengers\":\"\"}";
    //逾期的假发票
    public static final String HANDRECEIPT3 = "{\"invoiceTypeNo\":\"10\",\"isHandNew\":true,\"tmpFee\":0,\"cardsignType\":\"HAND\",\"billingNo\":\"21092021\",\"billingCode\":\"061002001111\",\"checkCode\":\"413705\",\"invoiceCode\":\"061002001111\",\"invoiceNumber\":\"21092021\",\"invoiceDate\":\"%s\",\"priceTaxAmount\":10,\"billingTime\":\"1607330707\",\"receiptTypeNo\":\"10\",\"fee\":1000,\"moreInfo\":\"{}\",\"showUserInfo\":\"\",\"internalStaff\":\"\",\"agentTag\":\"\",\"domesticPassengers\":\"\"}";
    // 火车票
    public static final String trainReceipt = "src/main/resources/receiptInfo/train.jpg";
    // 区块链发票
    public static final String qukuailian = "src/main/resources/receiptInfo/qukuailian.png";
    // ofd 格式的发票
    public static final String ofdReceipt = "src/main/resources/receiptInfo/ofdReceipt.ofd";
}

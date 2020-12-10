package com.hand.basicConstant;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CardType {

    CHINA_ID("CHINA_ID","101"),
    PASSPORT("PASSPORT","102"),
    MAINLAND("MAINLAND","103"),
    HOME_RETURN_PERMIT("HOME_RETURN_PERMIT","104"),
    HONG_KONG("HONG_KONG","105"),
    PERMANENT_RESIDENCE("PERMANENT_RESIDENCE","106"),
    MILITARY("MILITARY","107"),
    TRAVEL("TRAVEL","108"),
    TAIWAN("TAIWAN","109");

    private String cardName;
    private String cardCode;

    public static CardType getCardType(String cardType){
        for (CardType cardType1:CardType.values()){
            if (cardType1.getCardName().equalsIgnoreCase(cardType)){
                return cardType1;
            }
        }
        return CHINA_ID;
    }


}

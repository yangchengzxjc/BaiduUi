package com.hand.baseMethod;


public class HttpStatusException extends Exception {

    private int httpStatus;

    private String userMessage;

    public HttpStatusException(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatusException(int httpStatus, String userMessage) {
        this.httpStatus = httpStatus;
        this.userMessage = userMessage;
    }

    public int getHttpStatus() {
        return httpStatus;
    }


    public String getUserMessage() {
        return userMessage;
    }


}
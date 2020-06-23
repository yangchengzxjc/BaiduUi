package com.hand.exception;


public class ScreenshotSkipException extends BaseException {

    public ScreenshotSkipException(String msg) {
        super(msg);
    }

    public ScreenshotSkipException(Throwable cause) {
        super(cause);
    }

    public ScreenshotSkipException() {
        super("ScreenshotSkipException");
    }


}

package com.hand.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class NestedExceptionUtils {
    public NestedExceptionUtils() {
    }

    public static String buildMessage(String message, Throwable cause) {
        if (cause != null) {
            StringBuilder sb = new StringBuilder();
            if (message != null) {
                sb.append(message).append("; ");
            }

            sb.append("nested exception is ").append(cause);
            return sb.toString();
        } else {
            return message;
        }
    }

    public static String buildStackTrace(Throwable e) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        printWriter.flush();
        return writer.toString();
    }
}

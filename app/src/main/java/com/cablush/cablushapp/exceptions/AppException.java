package com.cablush.cablushapp.exceptions;

/**
 * Created by oscar on 13/12/15.
 */
public class AppException extends Exception {

    public AppException(String message) {
        super(message);
    }

    public AppException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}

package com.cloudtravel.common.exception;

/**
 * Created by Administrator on 2021/7/18 0018.
 */
public class CloudTravelException extends RuntimeException {

    private String code;

    private String message;

    public CloudTravelException() {
    }

    public CloudTravelException(String message) {
        super(message);
    }

    public CloudTravelException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloudTravelException(Throwable cause) {
        super(cause);
    }

    public CloudTravelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getCode() {
        return code;
    }

    public CloudTravelException setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public CloudTravelException setMessage(String message) {
        this.message = message;
        return this;
    }
}

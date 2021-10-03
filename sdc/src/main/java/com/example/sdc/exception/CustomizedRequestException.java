package com.example.sdc.exception;

public class CustomizedRequestException extends RuntimeException {
    private int code;
    private int httpResponseCode;

    public CustomizedRequestException(String message, int code, int httpResponseCode) {
        super(message);
        this.code = code;
        this.httpResponseCode = httpResponseCode;
    }
    public CustomizedRequestException(String message, int code) {
        super(message);
        this.code = code;

    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getHttpResponseCode() {
        return httpResponseCode;
    }

    public void setHttpResponseCode(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }
}

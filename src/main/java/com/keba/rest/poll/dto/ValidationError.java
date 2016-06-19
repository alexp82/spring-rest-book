package com.keba.rest.poll.dto;

/**
 * Created by alexp on 18/06/16.
 */
public class ValidationError {
    public String code;
    public String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

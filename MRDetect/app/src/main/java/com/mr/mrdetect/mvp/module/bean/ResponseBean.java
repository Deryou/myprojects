package com.mr.mrdetect.mvp.module.bean;

/**
 * Created by MR on 2017/8/31.
 */

public class ResponseBean {
    private int code;
    private String message;
    private String body;

    public int getCode() {
        return code;
    }

    public ResponseBean setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseBean setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getBody() {
        return body;
    }

    public ResponseBean setBody(String body) {
        this.body = body;
        return this;
    }
}

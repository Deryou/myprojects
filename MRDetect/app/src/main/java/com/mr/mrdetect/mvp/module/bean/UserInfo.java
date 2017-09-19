package com.mr.mrdetect.mvp.module.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MR on 2017/8/30.
 */

public class UserInfo {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private UserData mData;

    public int getCode() {
        return code;
    }

    public UserInfo setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public UserInfo setMessage(String message) {
        this.message = message;
        return this;
    }

    public UserData getData() {
        return mData;
    }

    public UserInfo setData(UserData data) {
        mData = data;
        return this;
    }

    public UserInfo() {

    }

    public static class UserData{
        @SerializedName("username")
        private String username;
        @SerializedName("email")
        private String email;
        @SerializedName("mobile")
        private String mobile;
        @SerializedName("realname")
        private String realname;
        @SerializedName("status")
        private int status;

        public String getUsername() {
            return username;
        }

        public UserData setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public UserData setEmail(String email) {
            this.email = email;
            return this;
        }

        public String getMobile() {
            return mobile;
        }

        public UserData setMobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public String getRealname() {
            return realname;
        }

        public UserData setRealname(String realname) {
            this.realname = realname;
            return this;
        }

        public int getStatus() {
            return status;
        }

        public UserData setStatus(int status) {
            this.status = status;
            return this;
        }
    }
}

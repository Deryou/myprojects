package com.mr.mrdetect.mvp.module.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MR on 2017/9/14.
 */

public class CompanyInfoBean {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private CompanyData mCompanyData;

    public int getCode() {
        return code;
    }

    public CompanyInfoBean setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CompanyInfoBean setMessage(String message) {
        this.message = message;
        return this;
    }

    public CompanyData getCompanyDatas() {
        return mCompanyData;
    }

    public CompanyInfoBean setCompanyDatas(CompanyData companyData) {
        mCompanyData = companyData;
        return this;
    }

    public static class CompanyData{
        @SerializedName("name")
        public String name;
        @SerializedName("description")
        public String desc;
        @SerializedName("address")
        public String address;
        @SerializedName("email")
        public String email;
        @SerializedName("zipcode")
        public String zipcode;
        @SerializedName("phone")
        public String phone;
        @SerializedName("updatetime")
        public String updateTime;
    }
}

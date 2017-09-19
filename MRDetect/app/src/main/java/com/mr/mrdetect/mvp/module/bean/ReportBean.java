package com.mr.mrdetect.mvp.module.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MR on 2017/9/7.
 */

public class ReportBean {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<ReportData> mReportDatas;

    public int getCode() {
        return code;
    }

    public ReportBean setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ReportBean setMessage(String message) {
        this.message = message;
        return this;
    }

    public List<ReportData> getReportDatas() {
        return mReportDatas;
    }

    public ReportBean setReportDatas(List<ReportData> reportDatas) {
        mReportDatas = reportDatas;
        return this;
    }

    public static class ReportData {
        @SerializedName("id")
        public String id;
        @SerializedName("userid")
        public String userId;
        @SerializedName("companyid")
        public String companyId;
        @SerializedName("fileid")
        public String fileId;
        @SerializedName("title")
        public String title;
        @SerializedName("client")
        public String client;
        @SerializedName("address")
        public String address;
        @SerializedName("name")
        public String name;
        @SerializedName("identification")
        public String identification;
        @SerializedName("manufacturer")
        public String manufacturer;
        @SerializedName("manufacturerNo")
        public String manufacturerNo;
        @SerializedName("model")
        public String model;
        @SerializedName("class")
        public String degree;
        @SerializedName("version")
        public String version;
        @SerializedName("updatetime")
        public String updateTime;
        @SerializedName("filename")
        public String fileName;
        @SerializedName("path")
        public String path;
    }
}

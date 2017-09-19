package com.mr.mrdetect.mvp.module.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MR on 2017/9/6.
 */

public class ControlerBean {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<ControlorData> mControlorDatas;

    public int getCode() {
        return code;
    }

    public ControlerBean setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ControlerBean setMessage(String message) {
        this.message = message;
        return this;
    }

    public List<ControlorData> getControlorDatas() {
        return mControlorDatas;
    }

    public ControlerBean setControlorDatas(List<ControlorData> controlorDatas) {
        mControlorDatas = controlorDatas;
        return this;
    }

    public static class ControlorData {
        @SerializedName("id")
        public String id;
        @SerializedName("category_id")
        public String categoryId;
        @SerializedName("name")
        public String name;
        @SerializedName("description")
        public String desc;
        @SerializedName("identification")
        public String identification;
        @SerializedName("series")
        public String series;
    }
}


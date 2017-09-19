package com.mr.mrdetect.mvp.module.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MR on 2017/9/11.
 */

public class BannerBean {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<BannerData> mBannerDatas;

    public int getCode() {
        return code;
    }

    public BannerBean setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public BannerBean setMessage(String message) {
        this.message = message;
        return this;
    }

    public List<BannerData> getBannerDatas() {
        return mBannerDatas;
    }

    public BannerBean setBannerDatas(List<BannerData> bannerDatas) {
        mBannerDatas = bannerDatas;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(code + "\n")
                .append(message + "\n");
        for (int i = 0; i < mBannerDatas.size(); i++) {
            builder.append(mBannerDatas.get(i).imgPath + "\n");
        }
        return super.toString();
    }

    public static class BannerData {
        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;
        @SerializedName("url")
        public String url;
        @SerializedName("path")
        public String imgPath;
        @SerializedName("type")
        public String type;
        @SerializedName("updatetime")
        public String updateTime;
        @SerializedName("status")
        public String status;
    }
}

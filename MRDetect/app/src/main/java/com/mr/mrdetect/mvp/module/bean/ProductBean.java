package com.mr.mrdetect.mvp.module.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MR on 2017/9/6.
 */

public class ProductBean {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<ProductData> mDatas;

    public int getCode() {
        return code;
    }

    public ProductBean setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ProductBean setMessage(String message) {
        this.message = message;
        return this;
    }

    public List<ProductData> getDatas() {
        return mDatas;
    }

    public ProductBean setDatas(List<ProductData> datas) {
        mDatas = datas;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(code);
        builder.append(message+"\n");
        for (int i = 0; i < mDatas.size(); i++) {
            builder.append("数据" + i + "：{");
            builder.append(mDatas.get(i).id+"    \n").append(mDatas.get(i).name+"    \n")
                    .append(mDatas.get(i).description+"    \n")
                    .append(mDatas.get(i).series);
            builder.append("}\n");
        }
        return builder.toString();
    }

    public static class ProductData {
        @SerializedName("id")
        public int id;
        @SerializedName("series")
        public String series;
        @SerializedName("name")
        public String name;
        @SerializedName("description")
        public String description;
    }
}

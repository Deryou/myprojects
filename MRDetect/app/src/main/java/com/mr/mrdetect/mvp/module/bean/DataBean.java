package com.mr.mrdetect.mvp.module.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MR on 2017/9/8.
 */

public class DataBean {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<DataBean.Data> mDatas;

    public int getCode() {
        return code;
    }

    public DataBean setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public DataBean setMessage(String message) {
        this.message = message;
        return this;
    }

    public List<Data> getDatas() {
        return mDatas;
    }

    public DataBean setDatas(List<Data> datas) {
        mDatas = datas;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(code + "\n");
        builder.append(message + "\n");
        if (mDatas != null) {
            for (int i = 0; i < mDatas.size(); i++) {
                builder.append("数据" + i + "：{");
                builder.append(mDatas.get(i).id + "\n")
                        .append(mDatas.get(i).userName + "\n")
                        .append(mDatas.get(i).deviceIdentify + "\n")
                        .append(mDatas.get(i).sensorIdentify + "\n")
                        .append(mDatas.get(i).temp + "\n")
                        .append(mDatas.get(i).updateTime + "\n");
                builder.append("}\n");
            }
        } else {
            builder.append("{数据：null}\n");
        }
        return builder.toString();
    }

    public static class Data {
        @SerializedName("id")
        public String id;
        @SerializedName("username")
        public String userName;
        @SerializedName("device_identification")
        public String deviceIdentify;
        @SerializedName("sense_identification")
        public String sensorIdentify;
        @SerializedName("temp")
        public String temp;
        @SerializedName("updatetime")
        public String updateTime;
    }
}


package com.mr.mrdetect.mvp.module.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MR on 2017/9/7.
 */

public class SensorBean {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<SensorData> mSensorDatas;

    public int getCode() {
        return code;
    }

    public SensorBean setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public SensorBean setMessage(String message) {
        this.message = message;
        return this;
    }

    public List<SensorData> getSensorDatas() {
        return mSensorDatas;
    }

    public SensorBean setSensorDatas(List<SensorData> sensorDatas) {
        mSensorDatas = sensorDatas;
        return this;
    }

    public static class SensorData {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("description")
        public String desc;
        @SerializedName("identification")
        public String identification;
        @SerializedName("number")
        public String number;
        @SerializedName("device_identification")
        public String deviceIdentify;
        @SerializedName("temp")
        public String temp;
    }
}

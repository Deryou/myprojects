package com.mr.mrdetect.mvp.module.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MR on 2017/9/16.
 */

public class AddProductBean {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ProductData mProductData;

    public int getCode() {
        return code;
    }

    public AddProductBean setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public AddProductBean setMessage(String message) {
        this.message = message;
        return this;
    }

    public ProductData getProductData() {
        return mProductData;
    }

    public AddProductBean setProductData(ProductData productData) {
        mProductData = productData;
        return this;
    }

    public static class ProductData {
        @SerializedName("id")
        public int id;
    }
}

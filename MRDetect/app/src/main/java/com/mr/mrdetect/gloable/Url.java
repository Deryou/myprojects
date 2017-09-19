package com.mr.mrdetect.gloable;

/**
 * Created by MR on 2017/8/30.
 */

public class Url {

    public static final int SUCCESS_CODE = 200;

    //BaseUul
    public static final String BASE_URL = "http://i-freddy.ticp.io";

    //接口api
    //登录
    public static final String LOGIN = "/app/user/login";
    //用户信息
    public static final String PERSON_INFO = BASE_URL + "/app/user/info";
    //公司信息
    public static final String COMPANY_INFO = BASE_URL + "/app/company/info";
    //产品分类列表
    public static final String PRODUCT_TYPE_LIST = BASE_URL + "/app/category/index";
    //控制器列表
    public static final String CONTROLOR_LIST = BASE_URL + "/app/device/index";
    //传感器列表
    public static final String SENSOR_LIST = BASE_URL + "/app/sense/index";
    //报表列表
    public static final String REPORT_LIST = BASE_URL + "/app/report/index";
    //数据列表
    public static final String DATA_LIST = BASE_URL + "/app/data/index";
    //首页轮播图
    public static final String HOME_BANNER = BASE_URL + "/app/banner/index";
    //产品种类下拉列表
    public static final String PRODUCT_SELECT_LIST = BASE_URL + "/app/category/info";
    //添加控制器
    public static final String ADD_CONTROLER = BASE_URL + "/app/device/add";

    //响应码
    public static final int RESPONSE_OK = 200;
    public static final int RESPONSE_ERROR = 203;

    //标识
    public static final String USER_NAME = "username";
    public static final String USER_PASSWORD = "password";
}

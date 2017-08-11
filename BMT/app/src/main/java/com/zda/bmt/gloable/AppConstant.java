package com.zda.bmt.gloable;

/**
 * Created by MR on 2017/7/17.
 */

public class AppConstant {

    //远程端口与ip地址
    public static final int REMOTE_PORT = 8899;
    public static final String REMOTE_IP = "10.10.100.254";

    //tab对应的索引值
    public static final int EQUIPMENTFRAGMENT = 0;
    public static final int TEMPERATUREFRAGMENT = 1;
    public static final int HUMIDITYFRAGMENT = 2;
    public static final int OXYGENFRAGMENT = 3;

    //手机本地数据
    public static final String LOCAL_DATA = "LOCAL_DATA";
    //初始化热点值
    public static final String HOTSPOT_NAME = "HOTSPOT_NAME";
    public static final String HOTSPOT_PWD = "HOTSPOT_PWD";
    public static final String EQUIPMENT_NUMBER = "EQUIPMENT_NUMBER";

    //检测温度类型
    public static final String TYPE_32 = "32_";
    public static final String TYPE_36 = "36_";

    //本地作为服务器的port端口
    public static final int HOTSPOT_PORT = 8282;
    public static final String HOTSPOT_IP = "192.168.43.1";

    //报告文档保存路径
    public static final String REPORT_FOLDER = "/reportBMT/";

    //保存到文档里的数据
    public static final String DOC_DATA = "doc_data";
}

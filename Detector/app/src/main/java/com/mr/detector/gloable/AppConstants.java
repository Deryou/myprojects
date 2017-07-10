package com.mr.detector.gloable;

/**
 * Created by MR on 2017/4/28.
 */

public class AppConstants {
    //远程端口与ip地址
    public static final int REMOTE_PORT = 8899;
    public static final String REMOTE_IP = "10.10.100.254";
    //本地作为服务器的port端口
    public static final int HOTSPOT_PORT = 12355;

    //手机本地数据
    public static final String LOCAL_DATA = "LOCAL_DATA";
    //初始化热点值
    public static final String HOTSPOT_NAME = "HOTSPOT_NAME";
    public static final String HOTSPOT_PWD = "HOTSPOT_PWD";

    //手机开启热点后的IP
    public static final String HOTSPOT_IP = "192.168.43.1";

    //tab对应的索引值
    public static final int EQUIPMENTFRAGMENT = 0;
    public static final int TEMPERATUREFRAGMENT = 1;
    public static final int HUMIDITYFRAGMENT = 2;
    public static final int OXYGENFRAGMENT = 3;

    //设备值索引
    public static final int EQUIP_TA = 0;
    public static final int EQUIP_TB = 1;
    public static final int EQUIP_TC = 2;
    public static final int EQUIP_TD = 3;
    public static final int EQUIP_TE = 4;
    public static final int EQUIP_TH = 5;
    public static final int EQUIP_TO = 6;

    //报告文档保存路径
    public static final String REPORT_FOLDER = "/reportDIR/";

    //检测温度类型
    public static final String TYPE_32 = "32_";
    public static final String TYPE_36 = "36_";

    //湿度采集组数
    public static final int TEMP_NUM = 15;
    public static final int HUMIDITY_NUM = 3;
    public static final int OXYGEN_NUM = 3;
}

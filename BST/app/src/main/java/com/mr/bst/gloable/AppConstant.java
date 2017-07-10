package com.mr.bst.gloable;

/**
 * Created by MR on 2017/6/9.
 */

public class AppConstant {
    //远程端口与ip地址
    public static final int REMOTE_PORT = 8899;
    public static final String REMOTE_IP = "10.10.100.254";

    //手机本地数据
    public static final String LOCAL_DATA = "LOCAL_DATA";
    //初始化热点值
    public static final String HOTSPOT_NAME = "HOTSPOT_NAME";
    public static final String HOTSPOT_PWD = "HOTSPOT_PWD";
    public static final String EQUIPMENT_NUMBER = "EQUIPMENT_NUMBER";

    //本地作为服务器的port端口
    public static final int HOTSPOT_PORT = 8282;
    public static final String HOTSPOT_IP = "192.168.43.1";

    //流速测试
    public static final int FLOW_A = 0;
    public static final int FLOW_B = 1;
    public static final int FLOW_C = 2;

    //照度测试
    public static final int LIGHT_A = 0;
    public static final int LIGHT_B = 1;

    //温压测试
    public static final int TP_PRESSURE = 0;
    public static final int TP_TEMP = 1;

    //报告文档保存路径
    public static final String REPORT_FOLDER = "/reportDIR/";

    //保存到文档里的数据
    public static final String TP_DATA = "tp_data";
    public static final String LIGHT_DATA = "light_data";
    public static final String FLOW_DATA = "flow_data";
    public static final String DOC_DATA = "doc_data";
//    public static final String PRESS_DIFF = "press_diff";
//    public static final String PRESS_AVE = "press_ave";
//    public static final String BOOT_BEFORE_TEMP = "boot_before_temp";
//    public static final String BOOT_AFTER_TEMP = "boot_after_temp";
//    public static final String TEMP_RAISED = "temp_raised";

//    public static final String LAMP_OPEN_DATA = "lamp_open_data";
//    public static final String LAMP_CLOSE_DATA = "lamp_close_data";
//    public static final String FLOW_UP_DATA = "flow_up_data";
//    public static final String FLOW_DOWN_DATA = "flow_down_data";
}

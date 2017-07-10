package com.mr.detector.callback;

import com.mr.detector.bean.ReportData;
import com.mr.detector.util.TcpClient;

import java.util.List;
import java.util.Map;

/**
 * Created by MR on 2017/5/4.
 * 设备页面生成表单回调
 */

public interface EquipmentCallback {
    void createForm(String fileName,String dataTime);

    Map<String,String> getEqumentReport();
}

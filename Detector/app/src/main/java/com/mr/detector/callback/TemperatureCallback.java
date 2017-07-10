package com.mr.detector.callback;

import java.util.List;
import java.util.Map;

/**
 * Created by MR on 2017/5/2.
 * 温度测试数据回调，
 */

public interface TemperatureCallback extends DataShowCallback {
    /**
     * 返回得到的数据集合
     * @return
     */
    Map<String,List<Map<String, String>>> getTempReport();

    /**
     * 进行数据清理
     */
    void dataClear();

    void tempStateChanged(String data);
}

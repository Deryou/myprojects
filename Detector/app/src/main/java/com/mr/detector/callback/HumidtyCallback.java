package com.mr.detector.callback;

import java.util.Map;

/**
 * Created by MR on 2017/5/2.
 */

public interface HumidtyCallback extends DataShowCallback {
    /**
     * 获取湿度
     * @return
     */
    Map<String, String> getHumData();
}

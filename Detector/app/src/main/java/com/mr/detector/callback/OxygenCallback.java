package com.mr.detector.callback;

import java.util.Map;

/**
 * Created by MR on 2017/5/2.
 */

public interface OxygenCallback extends DataShowCallback {
    /**
     * 获取氧浓度数据
     * @return
     */
    Map<String, String> getOxygendata();
}

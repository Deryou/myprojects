package com.zda.bmt.callback;

import java.util.List;
import java.util.Map;

/**
 * Created by MR on 2017/5/2.
 */

public interface DataShowCallback {
    /**
     * 当图标上数据过多时进行清理
     */
    void clearData();

    /**
     * 获取显示的值-待校准
     * @return
     */
    String getInputData();
}

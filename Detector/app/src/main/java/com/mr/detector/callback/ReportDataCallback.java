package com.mr.detector.callback;

import com.mr.detector.bean.ReportData;

/**
 * Created by MR on 2017/5/27.
 */

public interface ReportDataCallback {
    /**
     *添加表单数据回调
     */
    void addToReport(ReportData data);
}

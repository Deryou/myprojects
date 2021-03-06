package com.zda.bmt.callback;

/**
 * Created by MR on 2017/5/2.
 * 温度测试数据回调，
 */

public interface OxygenCallback extends DataShowCallback {
    /**
     * 多个fragment图表数据设置
     *
     * @param sendData 从仪器实时解析的数据
     */
    void setData(float sendData);
}

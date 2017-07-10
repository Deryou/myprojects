package com.mr.detector.callback;

/**
 * Created by MR on 2017/5/2.
 */

public interface DataShowCallback {
    /**
     * 多个fragment图表数据设置
     * @param sendData 从仪器实时解析的数据
     */
    void setData(float [] sendData,String type);
    /**
     * 当图标上数据过多时进行清理
     */
    void clearData();
}

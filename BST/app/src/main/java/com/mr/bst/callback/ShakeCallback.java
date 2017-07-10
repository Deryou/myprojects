package com.mr.bst.callback;

/**
 * Created by MR on 2017/6/21.
 */

public interface ShakeCallback{

    /**
     * 多个fragment图表数据设置
     * @param sendData 从仪器实时解析的数据
     */
    void setData(float sendData);
    /**
     * 当图标上数据过多时进行清理
     */
    void clearData();
}

package com.zda.bmt.callback;

import java.util.Map;

/**
 * Created by MR on 2017/5/2.
 * 温度测试数据回调，
 */

public interface EquimentCallback{
    /**
     * 获取所填的信息
     * @return 返回一个map集合
     */
    Map<String, String> getEquipData();
}

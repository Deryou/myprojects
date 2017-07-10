package com.mr.detector.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by MR on 2017/5/27.
 */

public class ReportData {
//    private String company;
//    private String equipName;
//    private String manufaturer;
//    private String serialNum;
//    private String equipType;
//    private String equipNum;
//    private String reportTime;

    private Map<String,String> equipmentInfo;
    private List<Map<String,String>> tempData;

    public Map<String, String> getEquipmentInfo() {
        return equipmentInfo;
    }

    public ReportData setEquipmentInfo(Map<String, String> equipmentInfo) {
        this.equipmentInfo = equipmentInfo;
        return this;
    }

    public ReportData setTempData(List<Map<String, String>> tempData) {
        this.tempData = tempData;
        return this;
    }

    public List<Map<String, String>> getTempData() {
        return tempData;
    }
}

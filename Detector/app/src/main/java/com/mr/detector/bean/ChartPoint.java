package com.mr.detector.bean;

/**
 * Created by MR on 2017/5/5.
 * 折线图的上的坐标点
 */

public class ChartPoint {
    private float pointX;
    private float pointY;

    public ChartPoint() {

    }

    public ChartPoint(float pointX, float pointY) {
        this.pointX = pointX;
        this.pointY = pointY;
    }

    public float getPointX() {
        return pointX;
    }

    public void setPointX(float pointX) {
        this.pointX = pointX;
    }

    public float getPointY() {
        return pointY;
    }

    public void setPointY(float pointY) {
        this.pointY = pointY;
    }
}

package com.qiniu.pili.droid.livedemo.common.model;

import com.qiniu.pili.droid.streaming.WatermarkSetting;

import java.io.Serializable;

/**
 * Created by kevin on 2017/8/18.
 */

public class WaterMarkSettingModel  implements Serializable {
    public static final String WATER_MARK_SETTING_MODEL = "WaterMarkSettingModel";


    private boolean isWaterOpen;
    private WatermarkSetting.WATERMARK_SIZE size;
    private WatermarkSetting.WATERMARK_LOCATION location;
    private int alpha;
    private float postionX;
    private float postionY;
    private boolean isCustomLocation;

    public boolean isCustomLocation() {
        return isCustomLocation;
    }

    public void setCustomLocation(boolean customLocation) {
        isCustomLocation = customLocation;
    }


    public boolean isWaterOpen() {
        return isWaterOpen;
    }

    public void setWaterOpen(boolean waterOpen) {
        isWaterOpen = waterOpen;
    }

    public WatermarkSetting.WATERMARK_SIZE getSize() {
        return size;
    }

    public void setSize(WatermarkSetting.WATERMARK_SIZE size) {
        this.size = size;
    }

    public WatermarkSetting.WATERMARK_LOCATION getLocation() {
        return location;
    }

    public void setLocation(WatermarkSetting.WATERMARK_LOCATION location) {
        this.location = location;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public float getPostionX() {
        return postionX;
    }

    public void setPostionX(float postionX) {
        this.postionX = postionX;
    }

    public float getPostionY() {
        return postionY;
    }

    public void setPostionY(float postionY) {
        this.postionY = postionY;
    }
}

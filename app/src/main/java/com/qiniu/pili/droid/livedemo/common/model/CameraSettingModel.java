package com.qiniu.pili.droid.livedemo.common.model;

import com.qiniu.pili.droid.streaming.CameraStreamingSetting;

import java.io.Serializable;

/**
 * Created by kevin on 2017/8/16.
 */

public class CameraSettingModel implements Serializable {
    public static final String CAMERA_SETTING_MODEL = "CameraSettingModel";

    private int cameraID ;
    private boolean continuousFocusModeEnabled;
    private String focusMode;
    private CameraStreamingSetting.PREVIEW_SIZE_LEVEL cameraPrvSizeLevel;
    private CameraStreamingSetting.PREVIEW_SIZE_RATIO cameraPrvSizeRatio;
    private boolean isBeauty;

    public boolean isBeauty() {
        return isBeauty;
    }

    public void setBeauty(boolean beauty) {
        isBeauty = beauty;
    }

    public int getCameraID() {
        return cameraID;
    }

    public void setCameraID(int cameraID) {
        this.cameraID = cameraID;
    }

    public boolean isContinuousFocusModeEnabled() {
        return continuousFocusModeEnabled;
    }

    public void setContinuousFocusModeEnabled(boolean continuousFocusModeEnabled) {
        this.continuousFocusModeEnabled = continuousFocusModeEnabled;
    }

    public String getFocusMode() {
        return focusMode;
    }

    public void setFocusMode(String focusMode) {
        this.focusMode = focusMode;
    }

    public CameraStreamingSetting.PREVIEW_SIZE_LEVEL getCameraPrvSizeLevel() {
        return cameraPrvSizeLevel;
    }

    public void setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL cameraPrvSizeLevel) {
        this.cameraPrvSizeLevel = cameraPrvSizeLevel;
    }

    public CameraStreamingSetting.PREVIEW_SIZE_RATIO getCameraPrvSizeRatio() {
        return cameraPrvSizeRatio;
    }

    public void setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO cameraPrvSizeRatio) {
        this.cameraPrvSizeRatio = cameraPrvSizeRatio;
    }
}

package com.qiniu.pili.droid.livedemo.common.model;

import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.StreamingProfile;

import java.io.Serializable;

/**
 * Created by kevin on 2017/8/17.
 */

public class EncodingSettingModel implements Serializable {
    public static final String ENCODING_SETTING_MODEL = "EncodingSettingModel";

    private AVCodecType codecType;
    private StreamingProfile.EncoderRCModes encodingRCMode;
    private boolean adaptiveBitrateEnable;
    private int videoQuality;
    private int videoQualityCustomFPS;
    private int videoQualityCustomBitrate;
    private int videoQualityCustomMaxKeyFrameInterval;
    private boolean isQualityCustomOpen;

    private boolean isEncodingSizeCustomOpen;
    private int encodingSize;
    private int encodingSizeCustomH;
    private int encodingSizeCustomW;
    private boolean isDebugMode;

    public boolean isEncodingSizeCustomOpen() {
        return isEncodingSizeCustomOpen;
    }

    public void setEncodingSizeCustomOpen(boolean encodingSizeCustomOpen) {
        isEncodingSizeCustomOpen = encodingSizeCustomOpen;
    }

    public int getEncodingSize() {
        return encodingSize;
    }

    public void setEncodingSize(int encodingSize) {
        this.encodingSize = encodingSize;
    }

    public int getEncodingSizeCustomH() {
        return encodingSizeCustomH;
    }

    public void setEncodingSizeCustomH(int encodingSizeCustomH) {
        this.encodingSizeCustomH = encodingSizeCustomH;
    }

    public int getEncodingSizeCustomW() {
        return encodingSizeCustomW;
    }

    public void setEncodingSizeCustomW(int encodingSizeCustomW) {
        this.encodingSizeCustomW = encodingSizeCustomW;
    }

    public boolean isQualityCustomOpen() {
        return isQualityCustomOpen;
    }

    public void setQualityCustomOpen(boolean qualityCustomOpen) {
        isQualityCustomOpen = qualityCustomOpen;
    }

    public int getVideoQualityCustomFPS() {
        return videoQualityCustomFPS;
    }

    public void setVideoQualityCustomFPS(int videoQualityCustomFPS) {
        this.videoQualityCustomFPS = videoQualityCustomFPS;
    }

    public int getVideoQualityCustomBitrate() {
        return videoQualityCustomBitrate;
    }

    public void setVideoQualityCustomBitrate(int videoQualityCustomBitrate) {
        this.videoQualityCustomBitrate = videoQualityCustomBitrate;
    }

    public int getVideoQualityCustomMaxKeyFrameInterval() {
        return videoQualityCustomMaxKeyFrameInterval;
    }

    public void setVideoQualityCustomMaxKeyFrameInterval(int videoQualityCustomMaxKeyFrameInterval) {
        this.videoQualityCustomMaxKeyFrameInterval = videoQualityCustomMaxKeyFrameInterval;
    }

    public int getVideoQuality() {
        return videoQuality;
    }

    public void setVideoQuality(int videoQuality) {
        this.videoQuality = videoQuality;
    }


    public AVCodecType getCodecType() {
        return codecType;
    }

    public void setCodecType(AVCodecType codecType) {
        this.codecType = codecType;
    }

    public StreamingProfile.EncoderRCModes getEncodingRCMode() {
        return encodingRCMode;
    }

    public void setEncodingRCMode(StreamingProfile.EncoderRCModes encodingRCMode) {
        this.encodingRCMode = encodingRCMode;
    }

    public boolean isAdaptiveBitrateEnable() {
        return adaptiveBitrateEnable;
    }

    public void setAdaptiveBitrateEnable(boolean adaptiveBitrateEnabl) {
        this.adaptiveBitrateEnable = adaptiveBitrateEnabl;
    }

    public boolean isDebugMode() {
        return isDebugMode;
    }

    public void setDebugMode(boolean debugMode) {
        isDebugMode = debugMode;
    }
}

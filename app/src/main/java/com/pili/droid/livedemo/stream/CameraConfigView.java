package com.pili.droid.livedemo.stream;

import android.content.Context;
import android.hardware.Camera;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.pili.droid.livedemo.R;
import com.pili.droid.livedemo.common.model.CameraSettingModel;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;

/**
 * Created by kevin on 2017/8/17.
 */

public class CameraConfigView extends FrameLayout {
    private CameraSettingModel mCameraSettingModel;
    private CheckBox mBeautyBox;

    public CameraConfigView(Context context) {
        super(context);
        mCameraSettingModel = new CameraSettingModel();
        LayoutInflater.from(context).inflate(R.layout.view_camera_config, this);
        mBeautyBox = (CheckBox) findViewById(R.id.FaceBeautyCheckBox);
    }

    public CameraSettingModel buildCameraModel() {
        if (mBeautyBox.isChecked()) {
            mCameraSettingModel.setBeauty(true);
        } else {
            mCameraSettingModel.setBeauty(false);
        }

        RadioGroup ratioRadioGroup = (RadioGroup) findViewById(R.id.CameraRatioRadioGroup);
        onRadioBtnChecked(ratioRadioGroup.getCheckedRadioButtonId());

        RadioGroup sizeRadioGroup = (RadioGroup) findViewById(R.id.CameraSizeRadioGroup);
        onRadioBtnChecked(sizeRadioGroup.getCheckedRadioButtonId());

        RadioGroup faceRadioGroup = (RadioGroup) findViewById(R.id.CameraFaceRadioGroup);
        onRadioBtnChecked(faceRadioGroup.getCheckedRadioButtonId());

        RadioGroup focusModeRadioGroup = (RadioGroup) findViewById(R.id.FocusModeRadioGroup);
        onRadioBtnChecked(focusModeRadioGroup.getCheckedRadioButtonId());

        return mCameraSettingModel;
    }

    public void onRadioBtnChecked(@IdRes int checkedId) {
        switch (checkedId) {
            case R.id.RadioCamera43:
                mCameraSettingModel.setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_4_3);
                break;
            case R.id.RadioCamera169:
                mCameraSettingModel.setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9);
                break;
            case R.id.RadioCameraSmall:
                mCameraSettingModel.setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.SMALL);
                break;
            case R.id.RadioCameraMedium:
                mCameraSettingModel.setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.MEDIUM);
                break;
            case R.id.RadioCameraLarge:
                mCameraSettingModel.setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.LARGE);
                break;
            case R.id.RadioCameraFront:
                mCameraSettingModel.setCameraID(Camera.CameraInfo.CAMERA_FACING_FRONT);
                break;
            case R.id.RadioCameraBack:
                mCameraSettingModel.setCameraID(Camera.CameraInfo.CAMERA_FACING_BACK);
                break;
            case R.id.RadioFocusContinuousPicture:
                mCameraSettingModel.setFocusMode(CameraStreamingSetting.FOCUS_MODE_CONTINUOUS_PICTURE);
                break;
            case R.id.RadioFocusContinuousVideo:
                mCameraSettingModel.setFocusMode(CameraStreamingSetting.FOCUS_MODE_CONTINUOUS_VIDEO);
                break;
            case R.id.RadioFocusAuto:
                mCameraSettingModel.setFocusMode(CameraStreamingSetting.FOCUS_MODE_AUTO);
                break;
        }
    }
}

package com.pili.droid.livedemo.stream;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.pili.droid.livedemo.R;
import com.pili.droid.livedemo.common.model.EncodingSettingModel;
import com.qiniu.pili.droid.streaming.StreamingProfile;

import static com.qiniu.pili.droid.streaming.AVCodecType.HW_VIDEO_SURFACE_AS_INPUT_WITH_HW_AUDIO_CODEC;
import static com.qiniu.pili.droid.streaming.AVCodecType.SW_VIDEO_H265_WITH_SW_AUDIO_CODEC;
import static com.qiniu.pili.droid.streaming.AVCodecType.SW_VIDEO_WITH_SW_AUDIO_CODEC;

/**
 * Created by kevin on 2017/8/17.
 */

public class EncodingConfigView extends FrameLayout implements RadioGroup.OnCheckedChangeListener {

    public EncodingSettingModel mEncodingSettingModel;

    private Spinner mVideoQualitySpinner;
    private Spinner mEncodingSizeSpinner;
    private LinearLayout mVideoQualityCustomLayout;
    private LinearLayout mEncodingSizeCustomLayout;
    private RadioGroup mVideoQualityRadioGroup;
    private RadioGroup mEncodingSizeRadioGroup;

    public EncodingConfigView(Context context) {
        super(context);
        mEncodingSettingModel = new EncodingSettingModel();

        LayoutInflater.from(context).inflate(R.layout.view_encoding_config, this);
        mVideoQualityCustomLayout = (LinearLayout) findViewById(R.id.videoQualityCustomLayout);
        mEncodingSizeCustomLayout = (LinearLayout) findViewById(R.id.encodingSizeCustomPanel);

        mVideoQualitySpinner = (Spinner) findViewById(R.id.videoQualitySpinner);
        initVideoSizePanel();

        mEncodingSizeSpinner = (Spinner) findViewById(R.id.encodingSizeSpinner);
        initEncodingSizePanel();

        mVideoQualityRadioGroup = (RadioGroup) findViewById(R.id.VideoQualityRadioGroup);
        mVideoQualityRadioGroup.setOnCheckedChangeListener(this);

        mEncodingSizeRadioGroup = (RadioGroup) findViewById(R.id.EncodingSizeRadioGroup);
        mEncodingSizeRadioGroup.setOnCheckedChangeListener(this);
    }

    private void initVideoSizePanel() {
        ArrayAdapter<String> data = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, StreamConstant.VIDEO_QUALITY_PRESETS);
        mVideoQualitySpinner.setAdapter(data);
        mVideoQualitySpinner.setSelection(4);
    }

    private void initEncodingSizePanel() {
        ArrayAdapter<String> data = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, StreamConstant.ENCODING_SIZE_PRESETS);
        mEncodingSizeSpinner.setAdapter(data);
        mEncodingSizeSpinner.setSelection(1);
    }

    public EncodingSettingModel buildEncodingModel() {
        int quality = StreamConstant.VIDEO_QUALITY_PRESETS_MAPPING[mVideoQualitySpinner.getSelectedItemPosition()];
        mEncodingSettingModel.setVideoQuality(quality);

        mEncodingSettingModel.setVideoQualityCustomBitrate(getTextIntValueByResID(R.id.videoQualityCustomBitrate));
        mEncodingSettingModel.setVideoQualityCustomFPS(getTextIntValueByResID(R.id.videoQualityCustomFps));
        mEncodingSettingModel.setVideoQualityCustomMaxKeyFrameInterval(getTextIntValueByResID(R.id.videoQualityCustomMaxKeyFrameInterval));

        int size = StreamConstant.ENCODING_SIZE_PRESETS_MAP[mEncodingSizeSpinner.getSelectedItemPosition()];
        mEncodingSettingModel.setEncodingSize(size);

        mEncodingSettingModel.setEncodingSizeCustomH(getTextIntValueByResID(R.id.encodingSizeCustomHeight));
        mEncodingSettingModel.setEncodingSizeCustomW(getTextIntValueByResID(R.id.encodingSizeCustomWidth));

        RadioGroup autoBitrateRadioGroup = (RadioGroup) findViewById(R.id.AutoBitrateRadioGroup);
        onRadioBtnChecked(autoBitrateRadioGroup.getCheckedRadioButtonId());

        RadioGroup RCRadioGroup = (RadioGroup) findViewById(R.id.RCRadioGroup);
        onRadioBtnChecked(RCRadioGroup.getCheckedRadioButtonId());

        RadioGroup encodingRadioGroup = (RadioGroup) findViewById(R.id.EncodingRadioGroup);
        onRadioBtnChecked(encodingRadioGroup.getCheckedRadioButtonId());

        onRadioBtnChecked(mVideoQualityRadioGroup.getCheckedRadioButtonId());
        onRadioBtnChecked(mEncodingSizeRadioGroup.getCheckedRadioButtonId());

        return mEncodingSettingModel;
    }

    public void onRadioBtnChecked(@IdRes int checkedId) {
        switch (checkedId) {
            case R.id.RadioEncodingSW:
                mEncodingSettingModel.setCodecType(SW_VIDEO_WITH_SW_AUDIO_CODEC);
                break;
            case R.id.RadioEncodingHW:
                mEncodingSettingModel.setCodecType(HW_VIDEO_SURFACE_AS_INPUT_WITH_HW_AUDIO_CODEC);
                break;
            case R.id.RadioEncoding265SW:
                mEncodingSettingModel.setCodecType(SW_VIDEO_H265_WITH_SW_AUDIO_CODEC);
                break;
            case R.id.RadioVideoQualityPreset:
                mEncodingSettingModel.setQualityCustomOpen(false);
                break;
            case R.id.RadioVideoQualityCustom:
                mEncodingSettingModel.setQualityCustomOpen(true);
                break;
            case R.id.RadioEncodingPreset:
                mEncodingSettingModel.setEncodingSizeCustomOpen(false);
                break;
            case R.id.RadioEncodingCustom:
                mEncodingSettingModel.setEncodingSizeCustomOpen(true);
                break;
            case R.id.RadioAutoOpen:
                mEncodingSettingModel.setAdaptiveBitrateEnable(true);
                break;
            case R.id.RadioAutoClose:
                mEncodingSettingModel.setAdaptiveBitrateEnable(false);
                break;
            case R.id.RadioRCQuality:
                mEncodingSettingModel.setEncodingRCMode(StreamingProfile.EncoderRCModes.QUALITY_PRIORITY);
                break;
            case R.id.RadioRCBitrate:
                mEncodingSettingModel.setEncodingRCMode(StreamingProfile.EncoderRCModes.BITRATE_PRIORITY);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.RadioVideoQualityPreset:
                mVideoQualitySpinner.setVisibility(View.VISIBLE);
                mVideoQualityCustomLayout.setVisibility(View.GONE);
                break;
            case R.id.RadioVideoQualityCustom:
                mVideoQualitySpinner.setVisibility(View.GONE);
                mVideoQualityCustomLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.RadioEncodingPreset:
                mEncodingSizeSpinner.setVisibility(View.VISIBLE);
                mEncodingSizeCustomLayout.setVisibility(View.GONE);
                break;
            case R.id.RadioEncodingCustom:
                mEncodingSizeSpinner.setVisibility(View.GONE);
                mEncodingSizeCustomLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    public int getTextIntValueByResID(@IdRes int resID) {
        TextView textView = (TextView) findViewById(resID);
        String textStr = textView.getText().toString();
        int value = Integer.parseInt(textStr);
        return value;
    }
}


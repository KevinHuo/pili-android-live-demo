package com.qiniu.pili.droid.livedemo.stream;

import android.content.Context;
import android.support.annotation.IdRes;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.qiniu.pili.droid.livedemo.R;
import com.qiniu.pili.droid.livedemo.common.model.WaterMarkSettingModel;
import com.qiniu.pili.droid.livedemo.common.utils.Util;
import com.qiniu.pili.droid.streaming.WatermarkSetting;


/**
 * Created by kevin on 2017/8/18.
 */

public class WaterMarkConfigView extends FrameLayout implements RadioGroup.OnCheckedChangeListener {
    private WaterMarkSettingModel mWaterMarkSettingModel;

    private LinearLayout mLocationCustomPanel;
    private RadioGroup mLocationPresetRadioGroup;
    private RadioGroup mLocationRadioGroup;

    public WaterMarkConfigView(Context context) {
        super(context);
        mWaterMarkSettingModel = new WaterMarkSettingModel();

        LayoutInflater.from(context).inflate(R.layout.view_watermark_config, this);
        mLocationCustomPanel = (LinearLayout) findViewById(R.id.WaterLocationCustomPanel);
        mLocationPresetRadioGroup = (RadioGroup) findViewById(R.id.WaterLocationPresetRadioGroup);
        mLocationRadioGroup = (RadioGroup) findViewById(R.id.WaterLocationRadioGroup);
        mLocationRadioGroup.setOnCheckedChangeListener(this);

        initAlphaEditText();
    }

    public void initAlphaEditText() {
        final EditText alphaEditText = (EditText) findViewById(R.id.WaterAlphaEditText);
        alphaEditText.setFilters(new InputFilter[]{new Util.InputFilterMinMax("0", "255")});
    }

    public WaterMarkSettingModel buildWaterMarkConfig() {
        EditText alphaEditText = (EditText) findViewById(R.id.WaterAlphaEditText);
        String alStr = alphaEditText.getText().toString();
        if (alStr.isEmpty()) {
            alStr = "0";
        }
        mWaterMarkSettingModel.setAlpha(Integer.parseInt(alStr));

        EditText xEditText = (EditText) findViewById(R.id.WaterCustomX);
        String xStr = xEditText.getText().toString();
        mWaterMarkSettingModel.setPostionX(Float.parseFloat(xStr));

        EditText yEditText = (EditText) findViewById(R.id.WaterCustomY);
        String yStr = yEditText.getText().toString();
        mWaterMarkSettingModel.setPostionY(Float.parseFloat(yStr));

        RadioGroup waterOpenRadioGroup = (RadioGroup) findViewById(R.id.WaterOpenRadioGroup);
        onRadioBtnChecked(waterOpenRadioGroup.getCheckedRadioButtonId());

        RadioGroup waterSizeRadioGroup = (RadioGroup) findViewById(R.id.WaterSizeRadioGroup);
        onRadioBtnChecked(waterSizeRadioGroup.getCheckedRadioButtonId());

        onRadioBtnChecked(mLocationRadioGroup.getCheckedRadioButtonId());
        onRadioBtnChecked(mLocationPresetRadioGroup.getCheckedRadioButtonId());

        return mWaterMarkSettingModel;
    }

    public void onRadioBtnChecked(@IdRes int checkedId) {
        switch (checkedId) {
            case R.id.RadioWaterOpen:
                mWaterMarkSettingModel.setWaterOpen(true);
                break;
            case R.id.RadioWaterClose:
                mWaterMarkSettingModel.setWaterOpen(false);
                break;
            case R.id.RadioWaterSmall:
                mWaterMarkSettingModel.setSize(WatermarkSetting.WATERMARK_SIZE.SMALL);
                break;
            case R.id.RadioWaterMedium:
                mWaterMarkSettingModel.setSize(WatermarkSetting.WATERMARK_SIZE.MEDIUM);
                break;
            case R.id.RadioWaterLarge:
                mWaterMarkSettingModel.setSize(WatermarkSetting.WATERMARK_SIZE.LARGE);
                break;
            case R.id.RadioWaterLocationPreset:
                mWaterMarkSettingModel.setCustomLocation(false);
                break;
            case R.id.RadioWaterLocationCustom:
                mWaterMarkSettingModel.setCustomLocation(true);
                break;
            case R.id.RadioWaterNW:
                mWaterMarkSettingModel.setLocation(WatermarkSetting.WATERMARK_LOCATION.NORTH_WEST);
                break;
            case R.id.RadioWaterNE:
                mWaterMarkSettingModel.setLocation(WatermarkSetting.WATERMARK_LOCATION.NORTH_EAST);
                break;
            case R.id.RadioWaterSE:
                mWaterMarkSettingModel.setLocation(WatermarkSetting.WATERMARK_LOCATION.SOUTH_EAST);
                break;
            case R.id.RadioWaterSW:
                mWaterMarkSettingModel.setLocation(WatermarkSetting.WATERMARK_LOCATION.SOUTH_WEST);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.RadioWaterLocationPreset:
                mLocationCustomPanel.setVisibility(View.GONE);
                mLocationPresetRadioGroup.setVisibility(View.VISIBLE);
                break;
            case R.id.RadioWaterLocationCustom:
                mLocationCustomPanel.setVisibility(View.VISIBLE);
                mLocationPresetRadioGroup.setVisibility(View.GONE);
                break;
        }
    }
}

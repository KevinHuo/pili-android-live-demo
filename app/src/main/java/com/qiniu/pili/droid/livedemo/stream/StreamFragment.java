package com.qiniu.pili.droid.livedemo.stream;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.qiniu.pili.droid.livedemo.R;
import com.qiniu.pili.droid.livedemo.common.model.CameraSettingModel;
import com.qiniu.pili.droid.livedemo.common.model.EncodingSettingModel;
import com.qiniu.pili.droid.livedemo.common.model.WaterMarkSettingModel;
import com.qiniu.pili.droid.livedemo.common.utils.Cache;
import com.qiniu.pili.droid.livedemo.common.utils.StreamUtils;
import com.qiniu.pili.droid.livedemo.common.utils.Util;

import java.util.ArrayList;

import static com.qiniu.pili.droid.livedemo.common.model.EncodingSettingModel.ENCODING_SETTING_MODEL;
import static com.qiniu.pili.droid.livedemo.common.model.WaterMarkSettingModel.WATER_MARK_SETTING_MODEL;
import static com.qiniu.pili.droid.livedemo.common.utils.Util.showToast;

/**
 * Created by kevin on 2017/8/16.
 */

public class StreamFragment extends Fragment implements View.OnClickListener {
    private static final int UPDATE_URL_MSG = 100;

    private ArrayList<View> mViewList;
    private ArrayList<String> mTitleList;

    private EncodingConfigView mEncodingConfigView;
    private CameraConfigView mCameraConfigView;
    private WaterMarkConfigView mWaterMarkConfigView;

    private EditText mURLEdit;
    private Button mButton;
    private String mPushURL = "";
    private ProgressBar mURLProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stream, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.StreamViewpager);
        FloatingActionButton fb = (FloatingActionButton) view.findViewById(R.id.StreamFloatingActionButton);
        fb.setOnClickListener(this);

        mURLProgressBar = (ProgressBar) view.findViewById(R.id.StreamURLProgressBar);
        mURLEdit = (EditText) view.findViewById(R.id.StreamPushURLEditView);
        mURLEdit.setText(Cache.retrievePublishURL(getActivity()));
        mButton = (Button) view.findViewById(R.id.StreamURLGenBtn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String roomID = mURLEdit.getText().toString();
                if (!Util.isRoomIDValuable(roomID)) {
                    showToast(getActivity(), getString(R.string.stream_input_roomid_tip));
                    return;
                }

                mURLProgressBar.setVisibility(View.VISIBLE);
                updateURLByRoomID(roomID);
            }
        });

        mEncodingConfigView = new EncodingConfigView(getActivity());
        mCameraConfigView = new CameraConfigView(getActivity());
        mWaterMarkConfigView = new WaterMarkConfigView(getActivity());

        mViewList = new ArrayList<>();
        mViewList.add(mCameraConfigView);
        mViewList.add(mWaterMarkConfigView);
        mViewList.add(mEncodingConfigView);

        mTitleList = new ArrayList<>();
        mTitleList.add(getString(R.string.stream_camera_config));
        mTitleList.add(getString(R.string.stream_water_mark_config));
        mTitleList.add(getString(R.string.stream_push_config));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.StreamTabs);
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(2)));
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setAdapter(new MyViewPageAdapter());
        return view;
    }

    public void updateURLByRoomID(final String roomID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPushURL = StreamUtils.requestPublishAddress(roomID);
                mHandler.sendEmptyMessage(UPDATE_URL_MSG);
            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        String url = mURLEdit.getText().toString().trim();

        if (url.isEmpty() || url.length() <= 5) {
            showToast(getActivity(), getString(R.string.stream_input_url_tip));
            return;
        }

        Intent intent = new Intent(getActivity(), AVStreamingActivity.class);

        EncodingSettingModel encodingSettingModel = mEncodingConfigView.buildEncodingModel();
        CameraSettingModel cameraSettingModel = mCameraConfigView.buildCameraModel();
        WaterMarkSettingModel waterMarkSettingModel = mWaterMarkConfigView.buildWaterMarkConfig();

        intent.putExtra(ENCODING_SETTING_MODEL, encodingSettingModel);
        intent.putExtra(CameraSettingModel.CAMERA_SETTING_MODEL, cameraSettingModel);
        intent.putExtra(WATER_MARK_SETTING_MODEL, waterMarkSettingModel);

        intent.putExtra(AVStreamingActivity.INPUT_TEXT, url);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        mHandler.removeMessages(UPDATE_URL_MSG);
        super.onDestroy();
    }

    class MyViewPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            // TODO Auto-generated method stub
            container.removeView(mViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_URL_MSG) {
                mURLEdit.setText(mPushURL);
                mURLProgressBar.setVisibility(View.GONE);
                Cache.savePublishURL(getActivity(), mPushURL);
            }
        }
    };
}

package com.pili.droid.livedemo.player;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.google.zxing.integration.android.IntentIntegrator;

import com.pili.droid.livedemo.R;
import com.pili.droid.livedemo.common.utils.Cache;
import com.pili.droid.livedemo.common.utils.StreamUtils;
import com.pili.droid.livedemo.common.utils.Util;
import com.pili.pldroid.player.AVOptions;

/**
 * Created by kevin on 2017/8/16.
 */

public class PlayerFragment extends Fragment implements View.OnClickListener {
    private static final int UPDATE_URL_MSG = 100;

    private RadioGroup mDecodeTypeRadioGroup;
    private RadioGroup mPlayerTypeRadioGroup;
    private CheckBox mCacheCheckBox;

    private String mVideopath;
    private EditText mURLEdit;
    private Button mButton;
    private String mPushURL;
    private ProgressBar mURLProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        mCacheCheckBox = (CheckBox) view.findViewById(R.id.CacheCheckBox);
        mURLProgressBar = (ProgressBar) view.findViewById(R.id.PlayURLProgressBar);
        mURLEdit = (EditText) view.findViewById(R.id.PlayURLEditView);
        mURLEdit.setText(Cache.retrievePlayURL(getActivity()));

        mButton = (Button) view.findViewById(R.id.PlayURLGenBtn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String roomID = mURLEdit.getText().toString();
                if (!Util.isRoomIDValuable(roomID)) {
                    Util.showToast(getActivity(), getString(R.string.tip_correct_roomid));
                    return;
                }

                mURLProgressBar.setVisibility(View.VISIBLE);
                updateURLByRoomID(roomID);
            }
        });

        mDecodeTypeRadioGroup = (RadioGroup) view.findViewById(R.id.DecodeTypeRadioGroup);
        mPlayerTypeRadioGroup = (RadioGroup) view.findViewById(R.id.PlayerTypeRadioGroup);

        FloatingActionButton fb = (FloatingActionButton) view.findViewById(R.id.PlayFloatingActionButton);
        fb.setOnClickListener(this);

        Button playLocalBtn = (Button) view.findViewById(R.id.PlayLocalBtn);
        playLocalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocalFile();
            }
        });

        Button playScanBtn = (Button) view.findViewById(R.id.PlayScanBtn);
        playScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQrcode();
            }
        });
        return view;
    }

    public void updateURLByRoomID(final String roomID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPushURL = StreamUtils.requestPlayURL(roomID);
                mHandler.sendEmptyMessage(UPDATE_URL_MSG);
            }
        }).start();
    }

    public void scanQrcode() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setOrientationLocked(true);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    @Override
    public void onClick(View v) {
        mVideopath = mURLEdit.getText().toString().trim();

        if (mVideopath.isEmpty() || mVideopath.length() <= 5) {
            Util.showToast(getActivity(), getString(R.string.play_input_url_tip));
            return;
        }

        Cache.savePlayURL(getActivity(), mVideopath);

        Intent intent = new Intent(getActivity(), PLVideoViewActivity.class);
        intent.putExtra("videoPath", mVideopath);

        if (mDecodeTypeRadioGroup.getCheckedRadioButtonId() == R.id.RadioHWDecode) {
            intent.putExtra("mediaCodec", AVOptions.MEDIA_CODEC_HW_DECODE);
        } else if (mDecodeTypeRadioGroup.getCheckedRadioButtonId() == R.id.RadioSWDecode) {
            intent.putExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE);
        } else {
            intent.putExtra("mediaCodec", AVOptions.MEDIA_CODEC_AUTO);
        }

        if (mPlayerTypeRadioGroup.getCheckedRadioButtonId() == R.id.RadioPlayLive) {
            intent.putExtra("liveStreaming", 1);
        } else {
            intent.putExtra("liveStreaming", 0);
        }

        intent.putExtra("cache", mCacheCheckBox.isChecked());
        intent.putExtra("video-data-callback", false);
        intent.putExtra("audio-data-callback", false);
        startActivity(intent);
    }

    public void getLocalFile() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("video/*");
        }

        getActivity().startActivityForResult(Intent.createChooser(intent, getString(R.string.play_choose_video_tip)), 0);
    }

    public void setPathURL(String url) {
        mURLEdit.setText(url);
    }

    @Override
    public void onDestroy() {
        mHandler.removeMessages(UPDATE_URL_MSG);
        super.onDestroy();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_URL_MSG) {
                mURLEdit.setText(mPushURL);
                mURLProgressBar.setVisibility(View.GONE);
                Cache.savePlayURL(getActivity(), mPushURL);
            }
        }
    };
}

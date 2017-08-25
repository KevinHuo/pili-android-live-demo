package com.pili.droid.livedemo.stream;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.pili.droid.livedemo.R;
import com.pili.droid.livedemo.common.model.CameraSettingModel;
import com.pili.droid.livedemo.common.model.EncodingSettingModel;
import com.pili.droid.livedemo.common.model.WaterMarkSettingModel;
import com.pili.droid.livedemo.common.utils.Util;
import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.http.DnspodFree;
import com.qiniu.android.dns.local.AndroidDnsServer;
import com.qiniu.android.dns.local.Resolver;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.FrameCapturedCallback;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.StreamStatusCallback;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingSessionListener;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;
import com.qiniu.pili.droid.streaming.WatermarkSetting;

import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by kevin on 2017/8/17.
 */

public class AVStreamingActivity extends Activity implements StreamStatusCallback, StreamingSessionListener, StreamingStateChangedListener, View.OnClickListener {
    private static final String TAG = "AVStreamingActivity";

    public static final String INPUT_TEXT = "INPUT_TEXT";
    private CameraSettingModel mCameraSettingModel;
    private EncodingSettingModel mEncodingSettingModel;
    private WaterMarkSettingModel mWaterMarkSettingModel;

    private StreamingProfile mStreamingProfile;
    private WatermarkSetting mWatermarkSetting;
    private CameraStreamingSetting mCameraStreamingSetting;
    private MediaStreamingManager mMediaStreamingManager;

    private CameraPreviewFrameView mCameraPreviewFrameView;
    private FloatingActionButton mBeautyFB;
    private FloatingActionButton mOrientationFB;
    private CheckBox mMuteCheckBox;
    private TextView mStatView;
    private boolean mIsPublishStreamStarted = false;
    private String mStatusMsgContent;


    private int mCurrentCamFacingIndex;

    private TextView mDebugText;
    protected Button mShutterButton;
    private ScreenShooter mScreenShooter = new ScreenShooter();


    private boolean mIsNeedFB = false;
    private boolean mIsPreviewMirror = false;
    private boolean mIsEncodingMirror = false;
    private boolean mOrientationChanged = false;
    private boolean mIsEncOrientationPort = true;

    protected boolean mShutterButtonPressed = false;
    protected boolean mIsReady;

    private String mURL;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avstreaming);
        mCameraPreviewFrameView = (CameraPreviewFrameView) findViewById(R.id.cameraPreviewFrameView);

        mBeautyFB = (FloatingActionButton) findViewById(R.id.FBBeautyOnOff);
        mOrientationFB = (FloatingActionButton) findViewById(R.id.FBOrientation);
        mDebugText = (TextView) findViewById(R.id.debugTextView);
        mStatView = (TextView)findViewById(R.id.infoTextView);
        mMuteCheckBox = (CheckBox) findViewById(R.id.MuteCheckBox);
        mMuteCheckBox.setOnClickListener(mMuteButtonClickListener);
        mShutterButton = (Button) findViewById(R.id.toggleRecording_button);
        mShutterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mShutterButtonPressed) {
                    stopStreamingInternal();
                } else {
                    startStreamingInternal();
                }
            }
        });

        mCameraSettingModel = (CameraSettingModel) getIntent().getSerializableExtra(CameraSettingModel.CAMERA_SETTING_MODEL);
        mEncodingSettingModel = (EncodingSettingModel) getIntent().getSerializableExtra(EncodingSettingModel.ENCODING_SETTING_MODEL);
        mWaterMarkSettingModel = (WaterMarkSettingModel) getIntent().getSerializableExtra(WaterMarkSettingModel.WATER_MARK_SETTING_MODEL);

        mURL = getIntent().getStringExtra(INPUT_TEXT);
        initCameraStreamingSetting();
        initStreamingProfile();
        initMediaStreamingManager();
        initWaterMarkSetting();

        boolean isWaterMarkOpen = mWaterMarkSettingModel.isWaterOpen();
        if (isWaterMarkOpen) {
            mMediaStreamingManager.prepare(mCameraStreamingSetting, null, mWatermarkSetting, mStreamingProfile);
        } else {
            mMediaStreamingManager.prepare(mCameraStreamingSetting, mStreamingProfile);
        }

        initMenu();
    }

    public void initMenu() {
        initFloatBtn(R.id.FBBeautyOnOff);
        initFloatBtn(R.id.FBCaptureFrame);
        initFloatBtn(R.id.FBEncodingMirror);
        initFloatBtn(R.id.FBOrientation);
        initFloatBtn(R.id.FBPreviewMirror);
    }


    private void initFloatBtn(@IdRes int checkedId) {
        FloatingActionButton fb = (FloatingActionButton) findViewById(checkedId);
        if (fb != null) {
            fb.setOnClickListener(this);
        }
    }

    public void initCameraStreamingSetting() {
        mCameraStreamingSetting = new CameraStreamingSetting();
        mCameraStreamingSetting.setCameraId(mCameraSettingModel.getCameraID());
        mCameraStreamingSetting.setCameraPrvSizeLevel(mCameraSettingModel.getCameraPrvSizeLevel());
        mCameraStreamingSetting.setCameraPrvSizeRatio(mCameraSettingModel.getCameraPrvSizeRatio());
        mCameraStreamingSetting.setFocusMode(mCameraSettingModel.getFocusMode());
        mCameraStreamingSetting.setBuiltInFaceBeautyEnabled(true);
        mIsNeedFB = mCameraSettingModel.isBeauty();
        mBeautyFB.setTitle(mIsNeedFB ? "美颜关" : "美颜开");
        // FaceBeautySetting 中的参数依次为：beautyLevel，whiten，redden，即磨皮程度、美白程度以及红润程度，取值范围为[0.0f, 1.0f]
        mCameraStreamingSetting.setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(0.8f, 0.7f, 0.6f))
                .setVideoFilter(mIsNeedFB ? CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY : CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_NONE);
        mCurrentCamFacingIndex = mCameraSettingModel.getCameraID();
    }

    public void initStreamingProfile() {
        StreamingProfile.VideoProfile vProfile = null;
        StreamingProfile.AudioProfile aProfile = null;

        mStreamingProfile = new StreamingProfile();
        mStreamingProfile.setEncoderRCMode(mEncodingSettingModel.getEncodingRCMode());

        if (mEncodingSettingModel.isQualityCustomOpen()) {
            vProfile = new StreamingProfile.VideoProfile(mEncodingSettingModel.getVideoQualityCustomFPS(),
                    mEncodingSettingModel.getVideoQualityCustomBitrate() * 1024,
                    mEncodingSettingModel.getVideoQualityCustomMaxKeyFrameInterval());
        }else {
            mStreamingProfile.setVideoQuality(mEncodingSettingModel.getVideoQuality());
        }

        mStreamingProfile.setAdaptiveBitrateEnable(mEncodingSettingModel.isAdaptiveBitrateEnable());
        try {
            mStreamingProfile.setPublishUrl(mURL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (mEncodingSettingModel.isEncodingSizeCustomOpen()) {
            mStreamingProfile.setPreferredVideoEncodingSize(mEncodingSettingModel.getEncodingSizeCustomW(), mEncodingSettingModel.getEncodingSizeCustomH());
        } else {
            mStreamingProfile.setEncodingSizeLevel(mEncodingSettingModel.getEncodingSize());
        }

        if (vProfile != null) {
            StreamingProfile.AVProfile avProfile = new StreamingProfile.AVProfile(vProfile, aProfile);
            mStreamingProfile.setAVProfile(avProfile);
        }

        mStreamingProfile.setDnsManager(getMyDnsManager())
                .setStreamStatusConfig(new StreamingProfile.StreamStatusConfig(3))
                .setSendingBufferProfile(new StreamingProfile.SendingBufferProfile(0.2f, 0.8f, 3.0f, 20 * 1000));
    }

    public void initWaterMarkSetting() {
        mWatermarkSetting = new WatermarkSetting(this);
        mWatermarkSetting.setResourceId(R.drawable.qiniu_logo);
        mWatermarkSetting.setAlpha(mWaterMarkSettingModel.getAlpha());
        mWatermarkSetting.setSize(mWaterMarkSettingModel.getSize());
        if (mWaterMarkSettingModel.isCustomLocation()) {
            mWatermarkSetting.setCustomPosition(mWaterMarkSettingModel.getPostionX(), mWaterMarkSettingModel.getPostionY());
        } else {
            mWatermarkSetting.setLocation(mWaterMarkSettingModel.getLocation());
        }
    }

    public void initMediaStreamingManager() {
        mMediaStreamingManager = new MediaStreamingManager(this, mCameraPreviewFrameView, mEncodingSettingModel.getCodecType());
        mMediaStreamingManager.setStreamingSessionListener(this);
        mMediaStreamingManager.setStreamingStateListener(this);
        mMediaStreamingManager.setStreamStatusCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMediaStreamingManager != null) {
            mMediaStreamingManager.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mShutterButtonPressed = false;

        if (mMediaStreamingManager != null) {
            mMediaStreamingManager.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaStreamingManager != null) {
            mMediaStreamingManager.destroy();
        }
    }

    @Override
    public void notifyStreamStatusChanged(final StreamingProfile.StreamStatus streamStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatView.setText("bitrate:" + streamStatus.totalAVBitrate / 1024 + " kbps"
                        + "\naudio:" + streamStatus.audioFps + " fps"
                        + "\nvideo:" + streamStatus.videoFps + " fps");
            }
        });
    }

    @Override
    public boolean onRecordAudioFailedHandled(int i) {
        return false;
    }

    @Override
    public boolean onRestartStreamingHandled(int i) {
        return false;
    }

    @Override
    public Camera.Size onPreviewSizeSelected(List<Camera.Size> list) {
        return null;
    }

    @Override
    public void onStateChanged(StreamingState streamingState, Object o) {
        switch (streamingState) {
            case PREPARING:
                mStatusMsgContent = getString(R.string.string_state_preparing);
                break;
            case READY:
                mIsReady = true;
                mStatusMsgContent = getString(R.string.string_state_ready);
                startStreamingInternal();
                break;
            case SHUTDOWN:
                mStatusMsgContent = getString(R.string.string_state_ready);
                setShutterButtonEnabled(true);
                setShutterButtonPressed(false);

                if (mOrientationChanged) {
                    mOrientationChanged = false;
                    startStreamingInternal();
                }
                break;
            case STREAMING:
                mStatusMsgContent = getString(R.string.string_state_streaming);
                setShutterButtonEnabled(true);
                setShutterButtonPressed(true);
                break;
            case IOERROR:
                mStatusMsgContent = getString(R.string.string_state_ioerror);
                setShutterButtonEnabled(true);
                break;
        }

        final String info = streamingState.toString();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDebugText.setText(mStatusMsgContent);
            }
        });
    }

    public void onClickOrientation(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mOrientationChanged = true;
                mIsEncOrientationPort = !mIsEncOrientationPort;
                mStreamingProfile.setEncodingOrientation(mIsEncOrientationPort ? StreamingProfile.ENCODING_ORIENTATION.PORT : StreamingProfile.ENCODING_ORIENTATION.LAND);
                mMediaStreamingManager.setStreamingProfile(mStreamingProfile);
                stopStreamingInternal();
                setRequestedOrientation(mIsEncOrientationPort ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mMediaStreamingManager.notifyActivityOrientationChanged();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mOrientationFB.setTitle(mIsEncOrientationPort? "横屏":"竖屏");
                    }
                });
            }
        }).start();
    }

    public void onClickBeauty(View view) {
        mIsNeedFB = !mIsNeedFB;
        mMediaStreamingManager.setVideoFilterType(mIsNeedFB ?
                CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY
                : CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_NONE);
        mBeautyFB.setTitle(mIsNeedFB ? "美颜关" : "美颜开");
    }

    public void onClickCaptureFrame(View view) {
        new Thread(mScreenShooter).start();
    }

    public void onClickPreviewMirror(View view) {
        mIsPreviewMirror = !mIsPreviewMirror;
        mMediaStreamingManager.setPreviewMirror(mIsPreviewMirror);
        Util.showToast(this, "镜像成功！");
    }

    public void onClickEncodingMirror(View view) {
        mIsEncodingMirror = !mIsEncodingMirror;
        mMediaStreamingManager.setEncodingMirror(mIsEncodingMirror);
        Util.showToast(this, "镜像成功！");
    }

    public void onClickSwitchCamera(View v) {
        mCurrentCamFacingIndex = (mCurrentCamFacingIndex + 1) % CameraStreamingSetting.getNumberOfCameras();
        CameraStreamingSetting.CAMERA_FACING_ID facingId;
        if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK.ordinal()) {
            facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK;
        } else if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT.ordinal()) {
            facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT;
        } else {
            facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD;
        }
        Log.i(TAG, "switchCamera:" + facingId);
        mMediaStreamingManager.switchCamera(facingId);
    }

    public void onClickExit(View v) {
        finish();
    }


    private View.OnClickListener mMuteButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMediaStreamingManager.mute(mMuteCheckBox.isChecked());
        }
    };

    /**
     * If you want to use a custom DNS server, config it
     * Not required.
     */
    private static DnsManager getMyDnsManager() {
        IResolver r0 = null;
        IResolver r1 = new DnspodFree();
        IResolver r2 = AndroidDnsServer.defaultResolver();
        try {
            r0 = new Resolver(InetAddress.getByName("119.29.29.29"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new DnsManager(NetworkInfo.normal, new IResolver[]{r0, r1, r2});
    }

    @Override
    public void onClick(View v) {
        int resID = v.getId();
        switch (resID) {
            case R.id.FBBeautyOnOff:
                onClickBeauty(v);
                break;
            case R.id.FBCaptureFrame:
                onClickCaptureFrame(v);
                break;
            case R.id.FBEncodingMirror:
                onClickEncodingMirror(v);
                break;
            case R.id.FBOrientation:
                onClickOrientation(v);
                break;
            case R.id.FBPreviewMirror:
                onClickPreviewMirror(v);
                break;
        }
    }

    protected boolean startStreaming() {
        if (mMediaStreamingManager != null) {
            return mMediaStreamingManager.startStreaming();
        }
        return false;
    }

    protected boolean stopStreaming() {
        if (mMediaStreamingManager != null) {
            return mMediaStreamingManager.stopStreaming();
        }
        return false;
    }

    protected void setShutterButtonEnabled(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mShutterButton.setFocusable(enable);
                mShutterButton.setClickable(enable);
                mShutterButton.setEnabled(enable);
            }
        });
    }

    protected void setShutterButtonPressed(final boolean pressed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mShutterButtonPressed = pressed;
                mShutterButton.setPressed(pressed);
            }
        });
    }

    protected void startStreamingInternal() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setShutterButtonEnabled(false);
                boolean res = startStreaming();
                mShutterButtonPressed = true;
                if (!res) {
                    mShutterButtonPressed = false;
                    setShutterButtonEnabled(true);
                }
                setShutterButtonPressed(mShutterButtonPressed);
            }
        }).start();
    }

    protected void stopStreamingInternal() {
        if (mShutterButtonPressed) {
            // disable the shutter button before stopStreaming
            setShutterButtonEnabled(false);
            boolean res = stopStreaming();
            if (!res) {
                mShutterButtonPressed = true;
                setShutterButtonEnabled(true);
            }
            setShutterButtonPressed(mShutterButtonPressed);
        }
    }

    private class ScreenShooter implements Runnable {
        @Override
        public void run() {
            final String fileName = "PLStreaming_" + System.currentTimeMillis() + ".jpg";
            mMediaStreamingManager.captureFrame(100, 100, new FrameCapturedCallback() {
                private Bitmap bitmap;

                @Override
                public void onFrameCaptured(Bitmap bmp) {
                    if (bmp == null) {
                        return;
                    }
                    bitmap = bmp;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                saveToSDCard(fileName, bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (bitmap != null) {
                                    bitmap.recycle();
                                    bitmap = null;
                                }
                            }
                        }
                    }).start();
                }
            });
        }
    }

    private void saveToSDCard(String filename, Bitmap bmp) throws IOException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
                bmp.recycle();
                bmp = null;
            } finally {
                if (bos != null) bos.close();
            }

            final String info = "Save frame to:" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AVStreamingActivity.this, info, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}

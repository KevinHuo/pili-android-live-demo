package com.pili.droid.livedemo.about;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pili.droid.livedemo.BuildConfig;
import com.pili.droid.livedemo.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by kevin on 2017/8/16.
 */

public class AboutFragment extends Fragment {
    TextView mVersionText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tip,container,false);

        mVersionText = (TextView)view.findViewById(R.id.versionInfoTextView);
        String info = "DEMO版本号：" + getVersionDescription() + "\n\n编译时间：" + getBuildTimeDescription();
        mVersionText.setText(info);

        return view;
    }

    private String getVersionDescription() {
        PackageManager packageManager = getActivity().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "未知";
    }

    protected String getBuildTimeDescription() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(BuildConfig.BUILD_TIMESTAMP);
    }

}

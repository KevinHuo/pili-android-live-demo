package com.pili.droid.livedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pili.droid.livedemo.common.utils.*;
import com.pili.droid.livedemo.common.utils.PermissionChecker;
import com.pili.droid.livedemo.player.PlayerFragment;
import com.pili.droid.livedemo.stream.StreamFragment;
import com.pili.droid.livedemo.about.AboutFragment;

import static com.pili.droid.livedemo.common.utils.Util.showToast;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private PermissionChecker mPermissionChecker = new PermissionChecker(this);

    private PlayerFragment mPlayerFragment;
    private StreamFragment mStreamFragment;
    private AboutFragment mAboutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayerFragment = new PlayerFragment();
        mStreamFragment = new StreamFragment();
        mAboutFragment = new AboutFragment();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.NavigationPlay);

        boolean isPermissionOK = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || mPermissionChecker.checkPermission();
        if (!isPermissionOK) {
//            Util.showToast(this, "您需要开启权限，才能够继续，否则会产生未知错误～");
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mPermissionChecker.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.NavigationPlay:
                    switchFragment(mPlayerFragment);
                    return true;
                case R.id.NavigationStream:
                    switchFragment(mStreamFragment);
                    return true;
                case R.id.NavigationAbout:
                    switchFragment(mAboutFragment);
                    return true;
            }
            return false;
        }

    };

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(mPlayerFragment);
        fragmentTransaction.hide(mStreamFragment);
        fragmentTransaction.hide(mAboutFragment);
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.fragmentContainer, fragment);
        }
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == 0) {
            String selectedFilepath = GetPathFromUri.getPath(this, data.getData());
            if (selectedFilepath != null && !"".equals(selectedFilepath)) {
                mPlayerFragment.setPathURL(selectedFilepath);
            }
        } else if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    showToast(this, getString(R.string.play_scan_cancel));
                } else {
                    mPlayerFragment.setPathURL(result.getContents());
                }
            }
        }
    }
}

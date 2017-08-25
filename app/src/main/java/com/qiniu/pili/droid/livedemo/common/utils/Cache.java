package com.qiniu.pili.droid.livedemo.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Cache {
    public static void savePublishURL(Context context, String url) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(context).edit();
        e.putString("PUBLISHURL", url);
        e.commit();
    }

    public static String retrievePublishURL(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("PUBLISHURL", "");
    }

    public static void savePlayURL(Context context, String url) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(context).edit();
        e.putString("PLAYURL", url);
        e.commit();
    }

    public static String retrievePlayURL(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("PLAYURL", "");
    }
}

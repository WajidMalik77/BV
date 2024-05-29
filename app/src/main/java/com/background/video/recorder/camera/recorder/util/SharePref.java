package com.background.video.recorder.camera.recorder.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.background.video.recorder.camera.recorder.application.MyApp;

public class SharePref {

    private static SharedPreferences pref;
    public static  String Locale_KeyValue = "SavedLocale";
    public static  String countryCodeKey = "countryCode";

    private static void initPref() {
        pref = MyApp.Companion.getAppContext()
                .getSharedPreferences("Background_Video_Maker", Context.MODE_PRIVATE);
    }

    public static void putString(String key, String value) {
        if (pref == null)
            initPref();
        pref.edit().putString(key, value).apply();
    }

    public static void putBoolean(String key, Boolean value) {
        if (pref == null)
            initPref();
        pref.edit().putBoolean(key, value).apply();
    }

    public static void putInt(String key, Integer value) {
        if (pref == null)
            initPref();
        pref.edit().putInt(key, value).apply();
    }

    public static void putLong(String key, Long value) {
        if (pref == null)
            initPref();
        pref.edit().putLong(key, value).apply();
    }

    public static String getString(String key, String mDefault) {
        if (pref == null)
            initPref();
        return pref.getString(key, mDefault);
    }

    public static Boolean getBoolean(String key, Boolean mDefault) {
        if (pref == null)
            initPref();
        return pref.getBoolean(key, mDefault);
    }

    public static Integer getInt(String key, Integer mDefault) {
        if (pref == null)
            initPref();
        return pref.getInt(key, mDefault);
    }

    public static Long getLong(String key, Long mDefault) {
        if (pref == null)
            initPref();
        return pref.getLong(key, mDefault);
    }

    public static void remove(String key) {
        if (pref == null)
            initPref();
        pref.edit().remove(key).apply();
        Log.d("HuHu", "remove:");
    }

    public static String getSelectedLanguage() {
        if (pref == null)
            initPref();
        return pref.getString(Locale_KeyValue, "en");
    }

    public static String getSelectedCountryCode() {
        if (pref == null)
            initPref();
        return pref.getString(countryCodeKey, "en");
    }

}

package com.background.video.recorder.camera.recorder.PhotoEditor.text;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingsSharedPref {
    private Editor editor;
    //Context _context;
    private int PRIVATE_MODE = 0;
    private SharedPreferences pref;
    private static final String PREF_NAME = "my_app_shared_prefs";
    public static final String PURCHASED = "inAppPurchase";
    public static final String PROVERSION = "ProVersion";
    public static final String REMOVEWATERMARK = "RemoveWatermark";
    public static final String iap_panel_counter = "iap_panel_counter";
    public static final String rate_us_counter = "rate_us_counter";
    public static final String dialogRateUS = "dialog_rateus";
    public static final String scores = "scores";

    public static final String singleFramesUnlocked = "singleFramesUnlocked";
    public static final String doubleFramesUnlocked = "doubleFramesUnlocked";
    public static final String portraitFramesUnlocked = "portraitFramesUnlocked";
    public static final String landscapeFramesUnlocked = "landscapeFramesUnlocked";
    public static final String stickersUnlocked="stickersUnlocked";
    public static final String gallerypick_blend_lscape="tut";

    public static String isFirstTimeForToolTip = "isFirstTime";

    public SettingsSharedPref(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

    }

    public void setToolTipFirstTime(boolean code) {
        editor = pref.edit();
        editor.putBoolean(isFirstTimeForToolTip, code);
        editor.apply();
    }

    public boolean getToolTipFirstTime() {
        return pref.getBoolean(isFirstTimeForToolTip, false);
    }

    public void setSingleFramesUnlocked(boolean code) {
        editor = pref.edit();
        editor.putBoolean(singleFramesUnlocked, code);
        editor.apply();
    }

    public boolean getSingleFramesUnlocked() {
        return pref.getBoolean(singleFramesUnlocked, false);
    }
    //Get Booleans if not found return a predefined default value
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getPrefs(context).getBoolean(key, defaultValue);
    }
    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    //Save Booleans
    public static void savePref(Context context, String key, boolean value) {
        getPrefs(context).edit().putBoolean(key, value).commit();
    }

    public void setDoubleFramesUnlocked(boolean code) {
        editor = pref.edit();
        editor.putBoolean(doubleFramesUnlocked, code);
        editor.apply();
    }

    public boolean getDoubleFramesUnlocked() {
        return pref.getBoolean(doubleFramesUnlocked, false);
    }

    public void setPortraitFramesUnlocked(boolean code) {
        editor = pref.edit();
        editor.putBoolean(portraitFramesUnlocked, code);
        editor.apply();
    }

    public boolean getPortraitFramesUnlocked() {
        return pref.getBoolean(portraitFramesUnlocked, false);
    }

    public void setLandscapeFramesUnlocked(boolean code) {
        editor = pref.edit();
        editor.putBoolean(landscapeFramesUnlocked, code);
        editor.apply();
    }

    public boolean getLandscapeFramesUnlocked() {
        return pref.getBoolean(landscapeFramesUnlocked, false);
    }
    public void setStickersUnlocked(boolean code) {
        editor = pref.edit();
        editor.putBoolean(stickersUnlocked, code);
        editor.apply();
    }

    public boolean getStickersUnlocked() {
        return pref.getBoolean(stickersUnlocked, false);
    }

    //Remove Ads Dialog
    public void setPrefForInAPPPurchase(boolean code) {
        editor = pref.edit();
        editor.putBoolean(PURCHASED, code);
        editor.apply();
    }

    public boolean getPrefForInAPPPurchase() {
        return pref.getBoolean(PURCHASED, false);
    }

    public void setPrefForProVersion(boolean code) {
        editor = pref.edit();
        editor.putBoolean(PROVERSION, code);
        editor.apply();
    }

    public boolean getPrefForProVersion() {
        return pref.getBoolean(PROVERSION, false);
    }

    public void setPrefForWatermark(boolean code) {
        editor = pref.edit();
        editor.putBoolean(REMOVEWATERMARK, code);
        editor.apply();
    }

    public boolean getPrefForWatermark() {
        return pref.getBoolean(REMOVEWATERMARK, false);
    }

    public void setProPanelCounter(int propanelcounter) {
        editor = pref.edit();
        editor.putInt(iap_panel_counter, propanelcounter);
        editor.apply();
    }

    public int getProPanelCounter() {
        return pref.getInt(iap_panel_counter, 0);
    }

    public void setRateUSCounter(int ratepanelcounter) {
        editor = pref.edit();
        editor.putInt(rate_us_counter, ratepanelcounter);
        editor.apply();
    }

    public int getRateUSCounter() {
        return pref.getInt(rate_us_counter, 0);
    }



    public void setdialogRateUS(int ratepanelcounter) {
        editor = pref.edit();
        editor.putInt(dialogRateUS, ratepanelcounter);
        editor.apply();
    }

    public int getdialogRateUS() {
        return pref.getInt(dialogRateUS, 0);
    }

    public void saveHighScoreList(String scoreString) {
        editor = pref.edit();
        editor.putString(scores, scoreString);
        editor.apply();
    }

    public String getHighScoreList() {
        return pref.getString(scores, "");
    }

    public void clearScoresfromPref(){
        editor.remove(scores);
        editor.commit();
    }
    public void setImageGalleryCounter(int galleryCounter) {
        editor = pref.edit();
        editor.putInt(gallerypick_blend_lscape, galleryCounter);
        editor.apply();
    }

    public int getImageGalleryCounter() {
        return pref.getInt(gallerypick_blend_lscape, 0);
    }
}


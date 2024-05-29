package com.background.video.recorder.camera.recorder.util.constant;


import android.util.Size;

import com.background.video.recorder.camera.recorder.BuildConfig;

public class Constants {

    public static final String NULL_STRING = "null";
    public static final String FirstOpen = "FirstOpen";
    public static final String isOnBoarding = "isOnBoarding";
    public static final int THUMBNAIL_SIZE = 1000;
    public static final String FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS";
    public static final String VIDEO_EXTENSION = ".mp4";
    public static final String IMAGE_EXTENSION = ".jpg";
    public static final String MEDIA_TYPE_IMAGE = "image";
    public static final String MEDIA_TYPE_VIDEO = "video";
    public static final String RATE_US_BASE_URL = "https://play.google.com/store/apps/details?id=";
    public static final int MEDIA_TYPE_FAVOURITE = 1;
    public static final int MEDIA_TYPE_NON_FAVOURITE = 2;
    public static final boolean FILE_TYPE_LOCKED = true;
    public static final boolean FILE_TYPE_UNLOCKED = false;
    public static final long MAX_SIZE_VIDEO = (long) ((1000L * 60) * 5); // 2hour
    public static boolean ON_PRIVACY_FRAGMENT = false;



    public static final String MEDIA_FILE_TRANSFER_KEY = "MEDIA_FILE_TRANSFER_KEY";
    public static boolean FAV_FILE_CLICK = false;
    public static boolean LONG_CLICKED_ENABLED = false;
    public static boolean SELECT_ALL = false;
    public static boolean ISLOCKEDALREADY = false;
    public static boolean LOCKFROMSETTINGS = false;
    public static boolean LONG_CLICKED_ENABLED_FAV_FILE = false;
    public static boolean SELECT_ALL_FAV_FILE = false;
    public static boolean APP_LOCKED = false;

    public static boolean FROM_HOME_ACTIVITY = false;

    public static boolean INTERSTITIAL_AD_SHOWED = false;

    public static boolean IS_RECORDING_ON_BACKGROUND = false;

    public static boolean STOPPED_FROM_NOTIFICATION = false;

    public static String isPermissionGranted = "isPermissionGranted";
    public static String isLock = "isLock";



    public static Size getVideoThumbnailSize() {
        return new Size(THUMBNAIL_SIZE, THUMBNAIL_SIZE);
    }

}
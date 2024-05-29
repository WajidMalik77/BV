//package com.background.video.recorder.camera.recorder.ads;
//
//
//import android.app.Activity;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
//import com.background.video.recorder.camera.recorder.application.MyApp;
//import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
//import com.google.android.gms.ads.AdError;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.FullScreenContentCallback;
//import com.google.android.gms.ads.LoadAdError;
//import com.google.android.gms.ads.appopen.AppOpenAd;
//
//import java.util.Date;
//
//
//public class AppOpenManager {
//    private static final String LOG_TAG = "AppOpenManager";
//    private AppOpenAd appOpenAd = null;
//    private Activity currentActivity;
//    private static boolean isShowingAd = false;
//    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
//    private long loadTime = 0;
//    //    MyApp context;
//    MyApp context;
//
//    String appopenidFromFirebase = "";
//
//    //    public AppOpenManager(MyApp context) {
////        this.context = context;
////    }
//    public AppOpenManager(MyApp context) {
//        this.context = context;
//    }
//
//    public void showAdIfAvailable(Activity currentActivity) {
//        this.currentActivity = currentActivity;
//        if (isAdAvailable()) {
//            Log.d(LOG_TAG, "Will show ad.");
//
//            FullScreenContentCallback fullScreenContentCallback =
//                    new FullScreenContentCallback() {
//                        @Override
//                        public void onAdDismissedFullScreenContent() {
//                            // Set the reference to null so isAdAvailable() returns false.
//                            AppOpenManager.this.appOpenAd = null;
//                            isShowingAd = false;
//                            fetchAd();
//                        }
//
//                        @Override
//                        public void onAdFailedToShowFullScreenContent(AdError adError) {
//                        }
//
//                        @Override
//                        public void onAdShowedFullScreenContent() {
//                            isShowingAd = true;
//                        }
//                    };
//            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
//            appOpenAd.show(this.currentActivity);
//
//        } else {
//            Log.d(LOG_TAG, "Can not show ad.");
//
//            fetchAd();
//
//
//        }
//    }
//
//    public void fetchAd() {
//        // Check if the app open ad switch is on
//        SharedPrefsHelper prefs = SharedPrefsHelper.getInstance(context);
//        boolean isAppOpenAdEnabled = prefs.getAppopenSwitch();
//
//        if (isAppOpenAdEnabled && !isAdAvailable()) {
////            Toast.makeText(currentActivity, "switch is open", Toast.LENGTH_SHORT).show();
//            loadCallback =
//                    new AppOpenAd.AppOpenAdLoadCallback() {
//                        @Override
//                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                            super.onAdFailedToLoad(loadAdError);
//                        }
//
//                        @Override
//                        public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
//                            super.onAdLoaded(appOpenAd);
//                            AppOpenManager.this.appOpenAd = appOpenAd;
//                            AppOpenManager.this.loadTime = (new Date().getTime());
//                        }
//                    };
//            AdRequest request = getAdRequest();
//            AppOpenAd.load(
//                    context,
////                    prefs.getAppopenAdId(), // Load the ad ID from shared preferences
//                    "ca-app-pub-3940256099942544/5575463023", // Load the ad ID from shared preferences
//                    request,
//                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
//                    loadCallback
//            );
//        } else {
////            Toast.makeText(currentActivity, "switch is closed", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private AdRequest getAdRequest() {
//        return new AdRequest.Builder().build();
//    }
//
//    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
//        long dateDifference = (new Date()).getTime() - this.loadTime;
//        long numMilliSecondsPerHour = 3600000;
//        return (dateDifference < (numMilliSecondsPerHour * numHours));
//    }
//
//    /**
//     * Utility method that checks if ad exists and can be shown.
//     */
//    public boolean isAdAvailable() {
//        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
//    }
//
//}
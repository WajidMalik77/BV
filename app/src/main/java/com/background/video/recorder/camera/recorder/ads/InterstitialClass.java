package com.background.video.recorder.camera.recorder.ads;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import roozi.app.ads.AdsManager;

public class InterstitialClass {
    public static boolean isInterstitialShowing = false;
    static InterstitialAd mInterstitialAd;
    static String mInterstitialID;
    static String logTag = "Ads_";
    static ActionOnAdClosedListener mActionOnAdClosedListener;
    static boolean isAdDecided = false;
    static int DELAY_TIME = 0;
    static boolean stopInterstitial = false;
    static boolean timerCalled = false;
    //    static ProgressDialog progressDialog;
    static Dialog progressDialog;
    static AlertDialog alertDialog;
    private static Handler handler;
    private static Runnable runnable;

    public static void requestInterstitial(Activity activity, String interstitial_id, ActionOnAdClosedListener actionOnAdClosedListenersm) {
        mInterstitialID = interstitial_id;
        mActionOnAdClosedListener = actionOnAdClosedListenersm;
        isAdDecided = false;

        if (AdTimerClass.isEligibleForAd()) {
            loadInterstitial(activity);
        } else {
            performAction();
        }

    }

    public static void loadInterstitial(Activity activity) {
        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            return;
        }

        if (mInterstitialAd == null) {
            isInterstitialShowing = true;
            showAdDialog(activity);
            stopInterstitial = false;
            timerCalled = false;
            AdRequest adRequestInterstitial = new AdRequest.Builder().build();
            InterstitialAd.load(activity, mInterstitialID, adRequestInterstitial,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;
                            isAdDecided = true;
                            Log.d(logTag, "Insterstitial Loaded.");
                            handler.removeCallbacks(runnable);

                            if (!timerCalled) {
                                closeAdDialog();
                                showInterstitial(activity);
                            }


                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.d(logTag, "Interstitial Failed to Load." + loadAdError.getMessage());
                            mInterstitialAd = null;
                            isAdDecided = true;
                            handler.removeCallbacks(runnable);
                            if (!timerCalled) {
                                closeAdDialog();
                                performAction();
                            }

                        }
                    });
            timerAdDecided(activity);
        } else {


            Log.d(logTag, "Ad was already loaded.: ");

            stopInterstitial = false;
            showAdDialog(activity);
            new Handler().postDelayed(() -> {
                closeAdDialog();
                showInterstitial(activity);
            }, 5000);
        }
    }

//    static void showAdDialog(Activity activity) {
//        progressDialog = new ProgressDialog(activity , R.layout.activity_my_album_new);
//        progressDialog.setTitle("Please Wait.");
//        progressDialog.setMessage("Please wait Full Screen Ad is expected to show.");
//        progressDialog.setCancelable(false);
//        progressDialog.create();
//
//        // Optional: Adjust the window attributes as needed
//        Window window = progressDialog.getWindow();
//        if (window != null) {
//            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//            layoutParams.copyFrom(window.getAttributes());
//            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//            window.setAttributes(layoutParams);
//        }
//
//
//        progressDialog.show();
//
//            /* Window window = progressDialog.getWindow();
//            WindowManager.LayoutParams wlp = window.getAttributes();
//            wlp.gravity = Gravity.BOTTOM;
//            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//            window.setAttributes(wlp);*/
//
//
//    }

    static void showAdDialog(Activity activity) {
        // Create a new Dialog with a full-screen theme
        progressDialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        progressDialog.setContentView(R.layout.custom_ad_dialog); // Set your custom layout here
        progressDialog.setCancelable(false);

        // Get the window of the dialog and set layout to match parent
        Window window = progressDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }

        progressDialog.show();
    }


    static void closeAdDialog() {
        if (progressDialog.isShowing() || progressDialog != null) {
            progressDialog.dismiss();
        }


    }

    static void timerAdDecided(Activity activity) {
        runnable = () -> {
            if (!isAdDecided) {
                stopInterstitial = true;
                timerCalled = true;
                Log.d(logTag, "Handler Cancel.");
                AdTimerClass.cancelTimer();
                closeAdDialog();
                showInterstitial(activity);
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 5000);
    }

    static boolean check;

    static void showInterstitial(Activity activity) {
        if (mInterstitialAd != null && !stopInterstitial) {
            mInterstitialAd.show(activity);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    Log.d(logTag, "Insterstitial Failed to Show.");
                    mInterstitialAd = null;
                    closeAdDialog();
                    isInterstitialShowing = false;
                    performAction();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();

                    isInterstitialShowing = true;
                    AdsManager.Companion.setInterShowing(true);

                    mInterstitialAd = null;
                    closeAdDialog();
                    performAction();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    Log.d(logTag, "Insterstitial Shown.");
                    isInterstitialShowing = false;
                    AdsManager.Companion.setInterShowing(false);
//                    mInterstitialAd = null;
//                    closeAdDialog();
//                    performAction();
                }
            });
        } else {
            performAction();
        }
    }

    static void performAction() {
        mActionOnAdClosedListener.ActionAfterAd();
    }
}

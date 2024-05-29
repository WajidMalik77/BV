package com.background.video.recorder.camera.recorder.facebookopractice;

import android.content.Context;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

public class FacebookInterstitialAdManager {

    private InterstitialAd interstitialAd;
    private Context context;
    private boolean isAdLoaded = false;

    public FacebookInterstitialAdManager(Context context, String adUnitId) {
        this.context = context;
        this.interstitialAd = new InterstitialAd(context, adUnitId);
    }

    public void loadInterstitialAd() {
        InterstitialAdListener adListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Code to be executed when the interstitial ad is displayed.
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Code to be executed when the interstitial ad is dismissed.
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Code to be executed when an error occurs.
                Log.e("facebookis", " Fcebook Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Code to be executed when the ad finishes loading.
                isAdLoaded = true;
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Code to be executed when the ad is clicked.
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Code to be executed when an impression is logged.
            }
        };

        this.interstitialAd.loadAd(
                this.interstitialAd.buildLoadAdConfig()
                        .withAdListener(adListener)
                        .build());
    }

    public void showInterstitialAd() {
        if (isAdLoaded && interstitialAd.isAdLoaded()) {
            interstitialAd.show();
        } else {
            Log.d("FacebookAds", "Interstitial ad is not loaded yet");
        }
    }

    public void destroy() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
    }
}

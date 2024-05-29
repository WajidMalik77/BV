package com.background.video.recorder.camera.recorder.facebookopractice;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.background.video.recorder.camera.recorder.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class Ad_Native {

    NativeAd nativeAd;
    NativeAdLayout nativeAdLayout;
    Context context;
    String adUnitId;
    private boolean isAdLoaded = false; // Flag to indicate if the ad is loaded


    public Ad_Native(Context context, NativeAdLayout nativeAdLayout, String adUnitId) {
        this.context = context;
        this.nativeAdLayout = nativeAdLayout;
        this.adUnitId = adUnitId;
        if (!isAdLoaded) {  // Check if the ad is not already loaded
            loadNativeAd(adUnitId);
        }
    }


    private void loadNativeAd(String adUnitId) {
        nativeAd = new NativeAd(context, adUnitId);
        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Toast.makeText(context, "Facebook Error: " + adError.getErrorMessage(), Toast.LENGTH_SHORT).show();
                Log.d("facebookis", "onError: "+adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                isAdLoaded = true;  // Set the flag to true as the ad is now loaded

//                inflateAd(nativeAd);
                inflateAd(nativeAd, nativeAdLayout); // Use the stored NativeAdLayout

            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
        nativeAd.loadAd(nativeAd.buildLoadAdConfig()
                .withAdListener(nativeAdListener)
                .build());

    }

    private void inflateAd(NativeAd nativeAd, NativeAdLayout nativeAdLayout) {
        nativeAd.unregisterView();
        LayoutInflater inflater = LayoutInflater.from(context);
        View adView = inflater.inflate(R.layout.item_native_ad, nativeAdLayout, false);

        nativeAdLayout.addView(adView);

        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        MaterialButton nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        clickableViews.add(nativeAdIcon);

        nativeAd.registerViewForInteraction(
                adView, nativeAdMedia, nativeAdIcon, clickableViews);
    }
}
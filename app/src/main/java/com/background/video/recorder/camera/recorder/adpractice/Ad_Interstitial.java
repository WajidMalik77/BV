package com.background.video.recorder.camera.recorder.adpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.background.video.recorder.camera.recorder.R;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class Ad_Interstitial extends AppCompatActivity {

    TextView txStatus;
    ProgressBar progress;
    Button btnShowAd;
    AdRequest adRequest;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_interstitial);

        txStatus = findViewById(R.id.interstitial_status);
        progress = findViewById(R.id.interstitial_progress);

        loadAdmob();
    }

    private void loadAdmob() {

        adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Toast.makeText(getApplicationContext(), "AD NOT LOADED", Toast.LENGTH_SHORT).show();
                        Log.d("admob_interstitial", "failed Even");
                        mInterstitialAd = null;

                        txStatus.setText("Ad Failed");
                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd minterstitialAd) {
                        super.onAdLoaded(minterstitialAd);
                        mInterstitialAd = minterstitialAd;
                        txStatus.setText("Ad Loaded");
                        showadmob();
                        Toast.makeText(getApplicationContext(), "AD LOADED", Toast.LENGTH_SHORT).show();
                        Log.d("admob_interstitial", "adLoaded Even");
                    }
                });
    }

    private void showadmob() {
        if (mInterstitialAd == null) {
//            if (intent!=null) {
//                startActivity(intent);
//            }
//            loadInterstitialAd();
        } else {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
//                    Constants.adOpened=false;
                    mInterstitialAd = null;
                    txStatus.setText("Ad Showed");

                    progress.setVisibility(View.GONE);
                    Log.d("admob_interstitial", "dismiss");
//                    if (intent!=null){
//                        startActivity(intent);
//                    }
//                    loadInterstitialAd();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();

                }
            });
            mInterstitialAd.show(this);
        }
    }


}


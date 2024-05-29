package com.background.video.recorder.camera.recorder.ui.activitiy;

import static com.background.video.recorder.camera.recorder.util.constant.AdsKeys.InApp;
import static com.background.video.recorder.camera.recorder.util.constant.AdsKeys.isASplash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.databinding.ActivityPasswordBinding;
import com.background.video.recorder.camera.recorder.util.FirebaseEvents;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.google.firebase.ktx.Firebase;

import roozi.app.ads.AdsManager;
import roozi.app.billing.BillingManager;
import roozi.app.interfaces.EventListener;
import roozi.app.interfaces.IPurchaseListener;

public class PasswordActivity extends AppCompatActivity implements IPurchaseListener, EventListener {
    private static final String TAG = "PasswordActivity";
    private ActivityPasswordBinding binding;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setLanguage(SharePref.getSelectedLanguage(), SharePref.countryCodeKey);
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordBinding.inflate(getLayoutInflater());
        BillingManager.Companion.setPurchaseListener(this);
        view = binding.getRoot();
        AdsManager.Companion.setEventListener(this);
        setContentView(view);
        initComponents();
    }

    private void initComponents() {
        setUpNavGraph();
    }

    private void setUpNavGraph() {
        // Initializing Host for navigating using Android Navigation Components
        try {
            NavHostFragment mNavHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerViewPass);
            if (mNavHostFragment != null) {

            } else {
                Log.d(TAG, "setUpNavGraph: " + "null");
            }
        } catch (NullPointerException nullPointerException) {
            Log.e(TAG, "setUpNavGraph: e", nullPointerException.getCause()
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setLanguage(SharePref.getSelectedLanguage(), SharePref.countryCodeKey);
    }

    @Override
    public void activatePremiumVersion() {
        SharePref.putBoolean(InApp, true);
        if (PasswordActivity.this.getWindow().getDecorView().getRootView().isShown()) {
            BillingManager.Companion.showProgressDialog(
                    PasswordActivity.this,
            new Intent(PasswordActivity.this, SplashActivity.class)
            );
        }
    }


    @Override
    public void onDismiss(@NonNull String key) {
        FirebaseEvents.Companion.logAnalytic("${key}_Dismiss");
        Log.d("EventLogsss", "${key}_Dismiss");
    }

    @Override
    public void onAdShow(@NonNull String key) {
        FirebaseEvents.Companion.logAnalytic("${key}_Show");
        Log.d("EventLogsss", "${key}_Show");
    }

    @Override
    public void onAdClick(@NonNull String key) {
        FirebaseEvents.Companion.logAnalytic("${key}_Click");
        Log.d("EventLogsss", "${key}_Click");
    }
}
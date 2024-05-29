package com.background.video.recorder.camera.recorder.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.background.video.recorder.camera.recorder.BuildConfig;
import com.background.video.recorder.camera.recorder.databinding.LayoutFeedbackBinding;
import com.background.video.recorder.camera.recorder.util.constant.Constants;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;


public class FeedbackFragment extends Fragment {
    private static final String TAG = "FeedbackFragment";
    private LayoutFeedbackBinding feedbackBinding;
    public static boolean feedbackFragment = false;

    public FeedbackFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedbackFragment = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        feedbackBinding =LayoutFeedbackBinding.inflate(inflater,container,false);
        return feedbackBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents();

    }

    private void initComponents() {
        setListeners();
    }

    private void setListeners() {
        feedbackBinding.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(Constants.RATE_US_BASE_URL + BuildConfig.APPLICATION_ID));
                    startActivity(viewIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        feedbackBinding.btnMaybe.setOnClickListener(view -> {
            Log.e(TAG, "setListeners: " + "on feedback fragment" );
            NavigationUtils.navigateBack(feedbackBinding.getRoot());
        });
    }
}

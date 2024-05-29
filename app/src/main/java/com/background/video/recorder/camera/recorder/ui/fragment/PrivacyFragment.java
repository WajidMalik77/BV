package com.background.video.recorder.camera.recorder.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.databinding.LayoutPrivacyGuidesBinding;
import com.background.video.recorder.camera.recorder.ui.activitiy.HomeActivity;
import com.background.video.recorder.camera.recorder.ui.dialog.AdLoadingDialog;
import com.background.video.recorder.camera.recorder.util.constant.Constants;
import com.background.video.recorder.camera.recorder.util.file.FileUtils;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;

public class PrivacyFragment extends Fragment {
    private LayoutPrivacyGuidesBinding privacyBinding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AdLoadingDialog dialog;
    private FileUtils fileUtils;


    public PrivacyFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isAdded())
            sharedPreferences = requireActivity().getSharedPreferences("stateSavePreference", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        fileUtils = new FileUtils(getContext());


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        privacyBinding = LayoutPrivacyGuidesBinding.inflate(inflater, container, false);
        return privacyBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        privacyBinding.btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("fromPrivacy", true);

                if (sharedPreferences.getBoolean("appLocked", false) && isAdded()) {
                    Intent intent = new Intent(requireActivity(), HomeActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                } else {
                    if (isAdded())
                        NavigationUtils.navigate(requireActivity(), R.id.action_privacyFragment_to_askingSetPassword);
                }
            }
        });


        privacyBinding.tvPrivacyLink.setOnClickListener(view1 -> {
            try {
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://sites.google.com/view/barakatappssoleprivacypolicy/home"));
                startActivity(viewIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Constants.ON_PRIVACY_FRAGMENT = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        Constants.ON_PRIVACY_FRAGMENT = false;
    }
}

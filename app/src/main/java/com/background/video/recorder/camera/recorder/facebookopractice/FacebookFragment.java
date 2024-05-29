package com.background.video.recorder.camera.recorder.facebookopractice;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.background.video.recorder.camera.recorder.R;
import com.facebook.ads.NativeAdLayout;

public class FacebookFragment extends Fragment {
    private Ad_Native adNative;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_facebook, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NativeAdLayout nativeAdLayout = view.findViewById(R.id.native_ad_container);
        adNative = new Ad_Native(getActivity(), nativeAdLayout, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");

    }
}
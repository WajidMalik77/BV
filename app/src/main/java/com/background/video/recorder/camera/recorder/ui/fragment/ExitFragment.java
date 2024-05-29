package com.background.video.recorder.camera.recorder.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.ActionOnAdClosedListener;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.ads.InterstitialClass;
import com.background.video.recorder.camera.recorder.databinding.FragmentExitBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.util.FirebaseEvents;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;

public class ExitFragment extends Fragment {
    boolean native_bg;
    FrameLayout nativeadlayout;
    SharedPreferences sharedPrefs;

    private boolean exit_admob_native = true;
    private boolean exit_facebook_native = true;
    private boolean exit_interstitial_admob = true;
    private boolean exit_interstitial_facebook = true;
    private String facebook_native_ad_id = "";
    private String admob_native_id = "";
    private SharedPrefsHelper prefs = null;
    private String TAG = "ExitFragment123";

    FragmentExitBinding binding;
    private String admob_interstitial_home_id = "";
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_exit, container, false);

        binding = FragmentExitBinding.inflate(inflater, container, false);

        FirebaseEvents.Companion.logAnalytic("Exit_panel");

        navController = Navigation.findNavController(getActivity(), R.id.fragmentContainerViewHome);

        sharedPrefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        native_bg = sharedPrefs.getBoolean("native_bg", false);

        prefs = SharedPrefsHelper.getInstance(getActivity());
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {
            exit_admob_native = localPrefs.getexit_admob_nativeSwitch();
//            exit_admob_native = true;
            exit_facebook_native = localPrefs.getexit_facebook_nativeSwitch();
            exit_interstitial_admob = localPrefs.getexit_interstitial_admobSwitch();
            admob_native_id = localPrefs.getadmob_native_idId();
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId();
            admob_interstitial_home_id = localPrefs.getadmob_interstitial_home_idId();


            Log.d(TAG, "onCreate:  admob_interstitial_splash_id  " + exit_admob_native);
            Log.d(TAG, "onCreate:  facebook_interstitial_splash_id  " + exit_facebook_native);
        }


        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.nativeAd.setVisibility(View.GONE);
        }  else if (exit_admob_native) {
            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadMediationNative(
                        activity,
                        binding.nativeAd,
                        admob_native_id,
                        binding.nativeAdContainer,
                        R.layout.ad_native_layout_modified
                );
            }
        } else if (exit_facebook_native) {
            // passwordNewBinding.nativeAd.setVisibility(View.GONE);

            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadNativeFacebookAd(
                        facebook_native_ad_id,
                        activity,
                        binding.nativeAdContainer
                );
            }
        } else {
            Log.d("checksswitcehs", "onViewCreated: both are off");
            binding.nativeAd.setVisibility(View.INVISIBLE);
        }


        // Reference the "Yes" button
//        binding.btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Close the app
//
//
//                FirebaseEvents.Companion.logAnalytic("Exit_OK_btn");
//                requireActivity().finishAffinity();
//            }
//        });

        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log exit event
                FirebaseEvents.Companion.logAnalytic("Exit_OK_btn");

                // Close the app and remove from recent tasks
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    requireActivity().finishAndRemoveTask();
                } else {
                    requireActivity().finishAffinity();
                }

            }
        });


        // Reference the "No" button
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharePref.getBoolean(AdsKeys.InApp, false)) {
                    NavigationUtils.navigate(getActivity(), R.id.homeFragment);
                } else {
                    if (exit_interstitial_admob) {
                        InterstitialClass.requestInterstitial(getActivity(), admob_interstitial_home_id, new ActionOnAdClosedListener() {
                            @Override
                            public void ActionAfterAd() {

                                NavigationUtils.navigate(getActivity(), R.id.homeFragment);
                            }
                        });

                    } else {
                        NavigationUtils.navigate(getActivity(), R.id.homeFragment);

                    }

                }


                FirebaseEvents.Companion.logAnalytic("Exit_cancel_btn");
                          }
        });

        return binding.getRoot();
    }
}
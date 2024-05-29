package com.background.video.recorder.camera.recorder.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.ActionOnAdClosedListener;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.ads.InterstitialClass;
import com.background.video.recorder.camera.recorder.databinding.LayoutSetPasswordNewBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.util.FirebaseEvents;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import roozi.app.ads.AdsManager;


public class AskingSetPassword extends Fragment {
    private static final String TAG = "AskingSetPassword123";
    private LayoutSetPasswordNewBinding passwordNewBinding;
    private boolean fromPrivacy;

    public AskingSetPassword() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fromPrivacy = getArguments().getBoolean("fromPrivacy");
        } else {
            Log.d(TAG, "onCreate: " + "null arguments");
        }

    }

    boolean native_bg;
    SharedPreferences sharedPrefs;

    private boolean asking_password_admob_native = true;
    private boolean asking_password_facebook_native = true;
    private boolean asking_password_setpassword_admob_interstitial = true;
    private boolean asking_password_notnow_admob_interstitial = true;
    private String facebook_native_ad_id = "";
    private String admob_native_id = "";
    private SharedPrefsHelper prefs = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        passwordNewBinding = LayoutSetPasswordNewBinding.inflate(inflater, container, false);

        sharedPrefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        native_bg = sharedPrefs.getBoolean("native_bg", false);

        prefs = SharedPrefsHelper.getInstance(getActivity());
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {
            asking_password_admob_native = localPrefs.getasking_password_admob_nativeSwitch();
            asking_password_facebook_native = localPrefs.getasking_password_facebook_nativeSwitch();
            asking_password_setpassword_admob_interstitial = localPrefs.getasking_password_setpassword_admob_interstitialSwitch();
            asking_password_notnow_admob_interstitial = localPrefs.getasking_password_notnow_admob_interstitialSwitch();
            admob_native_id = localPrefs.getadmob_native_idId();
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId();
            home_admob_home_inter = localPrefs.gethome_admob_home_interSwitch();
            admob_interstitial_home_id = localPrefs.getadmob_interstitial_home_idId();

            Log.d(TAG, "onCreate:  admob_interstitial_splash_id  " + asking_password_admob_native);
            Log.d(TAG, "onCreate:  facebook_interstitial_splash_id  " + asking_password_facebook_native);
        }


        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            passwordNewBinding.nativeAd.setVisibility(View.GONE);
        }  else if (asking_password_admob_native) {
            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadMediationNative(
                        activity,
                        passwordNewBinding.nativeAd,
                        admob_native_id,
                        passwordNewBinding.nativeAdContainer,
                        R.layout.ad_native_layout_modified
                );
            }
        } else if (asking_password_facebook_native) {
            // passwordNewBinding.nativeAd.setVisibility(View.GONE);

            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadNativeFacebookAd(
                        facebook_native_ad_id,
                        activity,
                        passwordNewBinding.nativeAdContainer
                );
            }
        } else {
            Log.d("checksswitcehs", "onViewCreated: both are off");
            passwordNewBinding.nativeAd.setVisibility(View.GONE);
        }
//        InterstitialAdUtils.INSTANCE.loadMediationNative(getActivity(), passwordNewBinding.nativeAd);

        return passwordNewBinding.getRoot();
    }

    Activity activity;
    private boolean home_admob_home_inter = true;
    private String admob_interstitial_home_id = "";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showAd();

        activity = getActivity();
        passwordNewBinding.btnSetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseEvents.Companion.logAnalytic("btn_setPassword_btn");
                Bundle bundle = new Bundle();
                bundle.putBoolean("fromAskPassword", false);
                if (isAdded())
//                    NavigationUtils.navigate(requireActivity(),R.id.enterPasswordFragment, bundle);
//                NavigationUtils.navigate(passwordNewBinding.getRoot(), R.id.action_askingSetPassword_to_setUserPasswordFragmentOne, bundle);

                    if (activity != null) {
//                    InterstitialAdUtils.INSTANCE.showInterstitialAdHome(activity, "interHomeID",
//                            R.id.action_askingSetPassword_to_setUserPasswordFragmentOne);

                        if (asking_password_setpassword_admob_interstitial) {
                            InterstitialClass.requestInterstitial(getActivity(), admob_interstitial_home_id, new ActionOnAdClosedListener() {
                                @Override
                                public void ActionAfterAd() {

                                    NavigationUtils.navigate(getActivity(), R.id.enterPasswordFragment);
                                }
                            });

                        } else {
                            NavigationUtils.navigate(getActivity(), R.id.enterPasswordFragment);

                        }
                    }


            }
        });
        passwordNewBinding.btnNotNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseEvents.Companion.logAnalytic("btn_not_now_setPassword");



                if (asking_password_notnow_admob_interstitial) {
                    InterstitialClass.requestInterstitial(getActivity(), admob_interstitial_home_id, new ActionOnAdClosedListener() {
                        @Override
                        public void ActionAfterAd() {

                            requireActivity().onBackPressed();
                        }
                    });

                } else {
                    requireActivity().onBackPressed();

                }

            }
        });
    }

    private void showAd() {
//        AdsManager.Companion.nativee(
//                SplashTwoFragment.getBoolean(AdsKeys.Admob_SetPassword_Screen_Native),
//                SplashTwoFragment.getBoolean(AdsKeys.Facebook_SetPassword_Screen_Native),
//                requireActivity(), passwordNewBinding.nativeAd, new Function1<Boolean, Unit>() {
//                    @Override
//                    public Unit invoke(Boolean aBoolean) {
//                        return null;
//                    }
//                }
//        );
    }
}

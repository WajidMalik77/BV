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
import androidx.lifecycle.ViewModelProvider;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.databinding.LayoutSavedSuccessBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.model.Authentication;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.background.video.recorder.camera.recorder.util.constant.Constants;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;
import com.background.video.recorder.camera.recorder.viewmodel.MediaFilesViewModel;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import roozi.app.ads.AdsManager;


public class SavedSuccessFragment extends Fragment {
    private static final String TAG = "SavedSuccessFragment";
    String[] pass;
    String answer;
    String question;
    boolean fromSettings;
    private LayoutSavedSuccessBinding successBinding;
    private MediaFilesViewModel viewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    boolean native_bg;
    SharedPreferences sharedPrefs;
    public SavedSuccessFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isAdded())
            viewModel = new ViewModelProvider(this,
                    (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                            .getInstance(requireActivity().getApplication()))
                    .get(MediaFilesViewModel.class); // viewModel init
        if (getArguments() != null) {
            pass = getArguments().getStringArray("pass");
            question = (String) getArguments().getString("question");
            answer = (String) getArguments().getString("answer");
            fromSettings = getArguments().getBoolean("fromSettings");
        } else {
            Log.e(TAG, "onCreate: Security fragment " + " pass is null");
        }
        if (isAdded()){
            sharedPreferences = requireActivity().getSharedPreferences("stateSavePreference", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }

    private boolean saved_successfull_admob_native = true;
    private boolean saved_successfull_facebook_native = true;
    private String facebook_native_ad_id = "";
    private String admob_native_id = "";
    private SharedPrefsHelper prefs = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        successBinding = LayoutSavedSuccessBinding.inflate(inflater, container, false);
        sharedPrefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        native_bg   = sharedPrefs.getBoolean("native_bg", false);


        prefs = SharedPrefsHelper.getInstance(getActivity());
        SharedPrefsHelper localPrefs = prefs;

        if (localPrefs != null) {
            saved_successfull_admob_native = localPrefs.getsaved_successfull_admob_nativeSwitch();
            saved_successfull_facebook_native = localPrefs.getsaved_successfull_facebook_nativeSwitch();
            admob_native_id = localPrefs.getadmob_native_idId();
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId();


            Log.d(TAG, "onCreate:  admob_interstitial_splash_id  " + saved_successfull_admob_native);
            Log.d(TAG, "onCreate:  facebook_interstitial_splash_id  " + saved_successfull_facebook_native);
        }



        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            successBinding.nativeAd.setVisibility(View.INVISIBLE);
        }  else if (saved_successfull_admob_native) {
            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadMediationNative(
                        activity,
                        successBinding.nativeAd,
                        admob_native_id,
                        successBinding.nativeAdContainer,
                        R.layout.ad_native_layout_modified
                );
            }
        }
        else if (saved_successfull_facebook_native) {
            // successBinding.nativeAd.setVisibility(View.GONE);

            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadNativeFacebookAd(
                        facebook_native_ad_id,
                        activity,
                        successBinding.nativeAdContainer
                );
            }
        }
        else {
            Log.d("checksswitcehs", "onViewCreated: both are off");
            successBinding.nativeAd.setVisibility(View.GONE);
        }

        Log.d("hoem_frag", "onCreateView: Recordings fargment");

        return successBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ShowAd();
        //                    Intent intent = new Intent(getActivity(), HomeActivity.class);
        //                    intent.putExtras(bundle);
        //                    startActivity(intent);
        //                    getActivity().finish();
        if (fromSettings) {
            Log.e(TAG, "onViewCreated: " + "forgot pass is here if");
            Constants.LOCKFROMSETTINGS = true;
            Bundle bundle = new Bundle();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < pass.length; i++) {
                builder.append(pass[i]);
            }
            viewModel.insertUser(new Authentication(Integer.parseInt(builder.toString()), answer));
        } else {
            Log.e(TAG, "onViewCreated: " + "forgot pass is here else");
            Bundle bundle = new Bundle();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < pass.length; i++) {
                builder.append(pass[i]);
            }
            Constants.LOCKFROMSETTINGS = false;
            viewModel.deleteAllTable();
            viewModel.insertUser(new Authentication(Integer.parseInt(builder.toString()), answer));
            // successBinding.ivSavedPassword.playAnimation();
        }
        editor.putBoolean("appLocked", true);
        editor.apply();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAdded())
                    NavigationUtils.navigate(requireActivity(), R.id.homeFragment);
//                    Intent intent = new Intent(getActivity(), HomeActivity.class);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                    getActivity().finish();

//                    NavigationUtils.navigate(successBinding.getRoot(), R.id.action_savedSuccessFragment2_to_settingsFragment,bundle);
            }
        }, 2000);
        if(sharedPreferences!= null){
            if (sharedPreferences.getBoolean("fromForgot", false)) {
                Log.e(TAG, "onViewCreated: " + "forgot pass is here else");
                Bundle bundle = new Bundle();
                StringBuilder builder = new StringBuilder();
                for (String s : pass) {
                    builder.append(s);
                }
                Constants.LOCKFROMSETTINGS = false;
                viewModel.deleteAllTable();
                viewModel.insertUser(new Authentication(Integer.parseInt(builder.toString()), answer));
                editor.putBoolean("appLocked", true);
                editor.apply();
                // successBinding.ivSavedPassword.playAnimation();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isAdded())
                            NavigationUtils.navigate(requireActivity(), R.id.homeFragment);
//                    Intent intent = new Intent(getActivity(), HomeActivity.class);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
                        editor.remove("fromForgot");
                        editor.apply();
//                    getActivity().finish();
                    }
                }, 2000);
            }
        }
        super.onViewCreated(view, savedInstanceState);
    }

    private void ShowAd() {
        AdsManager.Companion.nativee(
                SplashTwoFragment.getBoolean(AdsKeys.Admob_Save_Password_Screen_Native),
                SplashTwoFragment.getBoolean(AdsKeys.Facebook_Save_Password_Screen_Native),
                requireActivity(), successBinding.nativeAd, new Function1<Boolean, Unit>() {
                    @Override
                    public Unit invoke(Boolean aBoolean) {
                        return null;
                    }
                }
        );
//        AdsManager.Companion.interstitial(
//                SplashTwoFragment.getBoolean(AdsKeys.Admob_save_question_inter),
//                SplashTwoFragment.getBoolean(AdsKeys.Facebook_save_question_inter),
//                AdsKeys.Admob_save_question_inter,
//                AdsKeys.Facebook_save_question_inter,
//                requireActivity(), b -> null
//        );
    }
}

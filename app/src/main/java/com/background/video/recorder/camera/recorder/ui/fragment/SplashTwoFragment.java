package com.background.video.recorder.camera.recorder.ui.fragment;

import static com.background.video.recorder.camera.recorder.util.constant.AdsKeys.isASplash;
import static com.background.video.recorder.camera.recorder.util.constant.Constants.isPermissionGranted;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.ActionOnAdClosedListener;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.ads.InterstitialClass;
import com.background.video.recorder.camera.recorder.ads.InterstitialClassActivity;
import com.background.video.recorder.camera.recorder.application.MyApp;
import com.background.video.recorder.camera.recorder.databinding.LayoutSplashOneFragmentBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.RemoteConfig;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.ui.activitiy.HomeActivity;
import com.background.video.recorder.camera.recorder.ui.activitiy.LanguageActivity;
import com.background.video.recorder.camera.recorder.ui.dialog.CorrectPasswordDialog;
import com.background.video.recorder.camera.recorder.ui.dialog.WrongPasswordDialog;
import com.background.video.recorder.camera.recorder.util.FirebaseEvents;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.UserAuthenticationUtil;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;
import com.background.video.recorder.camera.recorder.viewmodel.MediaFilesViewModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.background.video.recorder.camera.recorder.ads.AdsHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import roozi.app.ads.AdsManager;
import roozi.app.billing.BillingManager;
import roozi.app.interfaces.IPurchaseListener;

public class SplashTwoFragment extends Fragment {
    private static final String TAG = "SplashTwoFragment123";
    public static int userEnteredPassword;
    private final String[] pass = new String[4];
    private final int size = 1;
    String password = "";
    UserAuthenticationUtil.UserVerification verification;
    private LayoutSplashOneFragmentBinding binding;
    private boolean locked;
    private int counter = 0;
    private MediaFilesViewModel viewModel;
    private UserAuthenticationUtil userVerification;
    private CorrectPasswordDialog correctDialog;
    private WrongPasswordDialog wrongDialog;

    public SplashTwoFragment() {
    }

    boolean homeInter, splashInter;
    boolean native_bg;
    SharedPreferences sharedPrefs;
    Intent intentToHome;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("checkyy", "onCreate: ");

        MyApp.Companion.disableshouldshowappopenad();


        if (isAdded())
            viewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(MediaFilesViewModel.class);
    }

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

//    private void startProgress() {
//        new Thread(new Runnable() {
//            public void run() {
//                int totalSleepTime = 0; // Total milliseconds the progress has slept
//                int progressMax = 5000; // Total milliseconds for the splash (5 seconds)
//                int sleepInterval = 50; // Sleep interval in milliseconds
//
//                while (totalSleepTime < progressMax) {
//                    progressStatus = (int) (((double) totalSleepTime / progressMax) * 100);
//                    handler.post(new Runnable() {
//                        public void run() {
//                            binding.progressBar.setProgress(progressStatus);
//                        }
//                    });
//
//                    try {
//                        Thread.sleep(sleepInterval);
//                        totalSleepTime += sleepInterval;
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                // When progress completes
//                handler.post(() -> {
//                    if (isAdded() && getActivity() != null) {
//                        startActivity(intentToHome);
//                        getActivity().finish();
//                    }
//                });
//            }
//        }).start();
//    }


    private void startProgress() {
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    handler.post(new Runnable() {
                        public void run() {
                            binding.progressBar.setProgress(progressStatus);
//                            if (!splash_admob && !splash_facebook) {
//                                getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));
//                            }
//                            progressBar.setProgress(progressStatus);
                        }
                    });
                    try {
                        // Sleep for 120 milliseconds.
                        Thread.sleep(120);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private boolean splash_admob = true;
    private boolean splash_facebook = true;
    private String admob_interstitial_splash_id = "";
    private String facebook_interstitial_splash_id = "";
    private SharedPrefsHelper prefs = null;


    private static final int DELAY_SECONDS = 10;
    //    private Handler handler = new Handler();
    private boolean splash_admob_native = true;
    private boolean splash_facebook_native = true;
    private String facebook_native_ad_id = "";
    private String admob_native_id = "";
    Intent intent;

    SharedPreferences.Editor editor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = LayoutSplashOneFragmentBinding.inflate(inflater, container, false);

        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isFirstTime = sharedPrefs.getBoolean("is_first_time", true);


        editor = sharedPrefs.edit();
        splashInter = sharedPrefs.getBoolean("splash_inter", true);
        homeInter = sharedPrefs.getBoolean("home_inter", false);
        native_bg = sharedPrefs.getBoolean("native_bg", false);
//        if (isFirstTime) {
//            intent = new Intent(getActivity(), LanguageActivity.class);
//
//            SharedPreferences.Editor editor = sharedPrefs.edit();
//            editor.putBoolean("is_first_time", false);
//            editor.apply();
//
//
//        } else {
//            intent = new Intent(getActivity(), HomeActivity.class);
//        }

        prefs = SharedPrefsHelper.getInstance(getActivity());
        sharedPrefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {
            splash_admob = localPrefs.getSplashAdmobSwitch();
            splash_facebook = localPrefs.getSplashFacebookSwitch();
            splash_admob_native = localPrefs.getsplash_admob_nativeSwitch();
//            splash_admob_native = true;

            splash_facebook_native = localPrefs.getsplash_facebook_nativeSwitch();
            admob_interstitial_splash_id = localPrefs.getadmob_interstitial_splash_idId();
            facebook_interstitial_splash_id = localPrefs.getfacebook_interstitial_splash_idId();
            admob_native_id = localPrefs.getadmob_native_idId();
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId();


            Log.d(TAG, "onCreate:  admob_interstitial_splash_id  " + admob_interstitial_splash_id);
            Log.d(TAG, "onCreate:  facebook_interstitial_splash_id  " + facebook_interstitial_splash_id);
        }

        if (splash_admob) {

//            InterstitialAdUtils.INSTANCE.loadSplashInterstitial(getActivity(), admob_interstitial_splash_id);
        }

        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            Log.d("tagaddzy", "InApp splash true: ");
            binding.nativeAd.setVisibility(View.INVISIBLE);
        } else if (splash_admob_native) {
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
        } else if (splash_facebook_native) {
            // binding.nativeAd.setVisibility(View.GONE);

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


        if (isFirstTime) {
            intent = new Intent(getActivity(), LanguageActivity.class);

            editor.putBoolean("is_first_time", false);
            editor.apply();


        } else {
            intent = new Intent(getActivity(), HomeActivity.class);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.startBtn.setVisibility(View.VISIBLE);
                binding.progressnewslider.setVisibility(View.GONE);
            }
        }, 3000); // 5000 milliseconds = 5 seconds



        binding.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity != null) {


//                    InterstitialAdUtils.INSTANCE.showSplashInterstitial(getActivity(), intent);

                    if (splash_admob) {

                        InterstitialClassActivity.requestInterstitial(getActivity(), admob_interstitial_splash_id, new ActionOnAdClosedListener() {
                            @Override
                            public void ActionAfterAd() {

                                intent.putExtra("isSplashFirstTime", true);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });

                    } else {
                        startActivity(intent);
                        getActivity().finish();

                    }
                }
                else {

                    Log.d("helloies", "null metho d");
                    startActivity(intent);
//                    activity.finish(); // Close the current activity to prevent it from showing up again.
                    Log.d(TAG, "Activity is null, cannot show interstitial ad.");
                }
            }
        });

        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            Log.d("tagaddzy", "InApp splash true: ");

            binding.startBtn.setVisibility(View.VISIBLE);
            binding.progressnewslider.setVisibility(View.GONE);
        } else if (!splash_admob && !splash_facebook) {

            binding.startBtn.setVisibility(View.VISIBLE);
            binding.progressnewslider.setVisibility(View.GONE);

        }


        if (splash_facebook) {
            Log.d(TAG, "onCreate: splash Facebook is true so ad will be loaded");
            // InterstitialAdUtils.loadInterstitialFacebookAd(this, "interSplashAdId", this);
            if (isFirstTime) {
                intent = new Intent(getActivity(), LanguageActivity.class);

                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean("is_first_time", false);
                editor.apply();


            } else {
                intent = new Intent(getActivity(), HomeActivity.class);

            }
            InterstitialAdUtils.INSTANCE.loadInterstitialFacebookAd(getActivity(), facebook_interstitial_splash_id, getActivity(), intent);
        } else {
            Log.d(TAG, "onCreate: splash Facebook is false so ad will not be loaded");
        }


        Log.d("checkyy", "onCreateView: ");
        setListeners();
        customTextView(binding.termText);
        binding.checkbox.setChecked(SharePref.getBoolean("isPrivacy", false));
        if (SharePref.getBoolean("isPrivacy", false)) {
            binding.checkbox.setVisibility(View.INVISIBLE);
            binding.termText.setVisibility(View.INVISIBLE);
        }
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remove any pending callbacks from the handler
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("checkyy", "onViewCreated: ");
        FirebaseEvents.Companion.logAnalytic("Splash_Screen_Show");

        startProgress();

    }


    private void setListeners() {


        binding.subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() != null)
                    BillingManager.Companion.showPremium(getContext(), aBoolean -> null);
            }
        });

    }

    private void customTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "I Agree to the ");
        spanTxt.append(Html.fromHtml(" <b>Terms of Condition</b>"));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (isAdded())
                    BillingManager.Companion.showPrivacyDialog(requireContext(), "https://barakatappssole.wordpress.com/2022/11/03/terms-conditions/", "Terms of Condition");
            }
        }, spanTxt.length() - "Terms of Condition".length(), spanTxt.length(), 0);
        spanTxt.append(" & ");
        spanTxt.setSpan(new ForegroundColorSpan(Color.BLACK), spanTxt.length(), spanTxt.length(), 0);
        spanTxt.append(Html.fromHtml("<b>Privacy Policy.</b>"));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (isAdded())
                    BillingManager.Companion.showPrivacyDialog(requireContext(), "https://barakatappssole.wordpress.com/blog/", "Privacy Policy");
            }
        }, spanTxt.length() - " Privacy Policy.".length(), spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

    public static Boolean getBoolean(String key) {
        return SharePref.getBoolean(key, false);
    }

}

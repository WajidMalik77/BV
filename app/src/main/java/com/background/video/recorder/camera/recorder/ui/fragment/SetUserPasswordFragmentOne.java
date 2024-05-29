package com.background.video.recorder.camera.recorder.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.databinding.LayoutSetPasswordOneBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import roozi.app.ads.AdsManager;

public class SetUserPasswordFragmentOne extends Fragment {
    private static final String TAG = "SetUserPasswordFragment";
    private final String[] pass = new String[4];
    int size = 1;
    private LayoutSetPasswordOneBinding binding;
    private StringBuilder stringBuilder;
    private boolean fromAskPassword = false;
    boolean native_bg;
    SharedPreferences sharedPrefs;
    public SetUserPasswordFragmentOne() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isAdded())
            if (requireActivity().getIntent().getExtras() != null) {
                fromAskPassword = requireActivity().getIntent().getExtras().getBoolean("fromSettings");
            }
        if (getArguments() != null) {
            fromAskPassword = getArguments().getBoolean("fromAskPassword");
        }
    }

    private boolean setuserpassword_admob_native = true;
    private boolean setuserpassword_facebook_native = true;
    private String facebook_native_ad_id = "";
    private String admob_native_id = "";
    private SharedPrefsHelper prefs = null;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LayoutSetPasswordOneBinding.inflate(inflater, container, false);

        sharedPrefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        native_bg   = sharedPrefs.getBoolean("native_bg", false);
        prefs = SharedPrefsHelper.getInstance(getActivity());
        SharedPrefsHelper localPrefs = prefs;

        if (localPrefs != null) {
            setuserpassword_admob_native = localPrefs.getsetuserpassword_admob_nativeSwitch();
            setuserpassword_facebook_native = localPrefs.getsetuserpassword_facebook_nativeSwitch();
            admob_native_id = localPrefs.getadmob_native_idId();
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId();


            Log.d(TAG, "onCreate:  admob_interstitial_splash_id  " + setuserpassword_admob_native);
            Log.d(TAG, "onCreate:  facebook_interstitial_splash_id  " + setuserpassword_facebook_native);
        }


        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.nativeAd.setVisibility(View.INVISIBLE);
        }  else if (setuserpassword_admob_native) {
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
        }
        else if (setuserpassword_facebook_native) {
            // binding.nativeAd.setVisibility(View.GONE);

            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadNativeFacebookAd(
                        facebook_native_ad_id,
                        activity,
                        binding.nativeAdContainer
                );
            }
        }
        else {
            Log.d("checksswitcehs", "onViewCreated: both are off");
            binding.nativeAd.setVisibility(View.GONE);
        }



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showAd();
        stringBuilder = new StringBuilder();
        setListeners();
        setFocus();
    }

    private void showAd() {
//        AdsManager.Companion.nativee(
//                SplashTwoFragment.getBoolean(AdsKeys.Admob_SetPassword_Screen_One_Native),
//                SplashTwoFragment.getBoolean(AdsKeys.Facebook_SetPassword_Screen_One_Native),
//                requireActivity(), binding.nativeAd, new Function1<Boolean, Unit>() {
//                    @Override
//                    public Unit invoke(Boolean aBoolean) {
//                        return null;
//                    }
//                }
//        );
    }


    private void setListeners() {

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().finish();
                NavigationUtils.navigateBack(binding.getRoot());
            }
        });
    }

    private void setFocus() {
        binding.edtDigitOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == size) {
                    pass[0] = s.toString();
                    binding.edtDigitTwo.requestFocus();
                    Log.d(TAG, "onTextChanged: " + stringBuilder.toString());
                }
            }
        });
        binding.edtDigitTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == size) {
                    binding.edtDigitThree.requestFocus();
                    pass[1] = s.toString();
                    Log.d(TAG, "onTextChanged: " + stringBuilder.toString());
                }
            }
        });
        binding.edtDigitThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == size) {
                    binding.edtDigitFour.requestFocus();
                    pass[2] = s.toString();

                    Log.d(TAG, "onTextChanged: " + stringBuilder.toString());
                }
            }
        });
        binding.edtDigitFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (fromAskPassword) {
                    if (s.toString().length() == size) {
                        pass[3] = s.toString();
                        if (!binding.edtDigitOne.getText().toString().isEmpty() && !binding.edtDigitTwo.getText().toString().isEmpty()
                                && !binding.edtDigitThree.getText().toString().isEmpty() && !binding.edtDigitFour.getText().toString().isEmpty()) {
                            Log.d(TAG, "onTextChanged: " + pass[0] + pass[1] + pass[2] + pass[3]);
                            if (isAdded()) {
                                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(binding.edtDigitFour.getWindowToken(), 0);
                                Bundle bundle = new Bundle();
                                bundle.putStringArray("pass", pass);
                                bundle.putBoolean("fromSettings", true);
                                if (pass.length == 4) {
                                    if (isAdded())
                                        NavigationUtils.navigate(requireActivity(), R.id.reEnterPasswordFragment, bundle);
//                                NavigationUtils.navigate(binding.getRoot(), R.id.action_setUserPasswordFragmentOne_to_setUserFragment, bundle);
                                    binding.edtDigitOne.getText().clear();
                                    binding.edtDigitTwo.getText().clear();
                                    binding.edtDigitThree.getText().clear();
                                    binding.edtDigitFour.getText().clear();
                                    binding.edtDigitOne.requestFocus();

                                }
                            }
                        } else {
                            binding.edtDigitOne.getText().clear();
                            binding.edtDigitTwo.getText().clear();
                            binding.edtDigitThree.getText().clear();
                            binding.edtDigitFour.getText().clear();
                            binding.edtDigitOne.requestFocus();
                            if (isAdded())
                                Toast.makeText(requireContext(), "Enter your 4digit password", Toast.LENGTH_SHORT).show();
                        }

                        // userDetailsProvider.getUserDetails(stringBuilder);
                    }

                } else {
                    if (s.toString().length() == size) {
                        pass[3] = s.toString();
                        Log.d(TAG, "onTextChanged: " + pass.toString());
                        if (!binding.edtDigitOne.getText().toString().isEmpty() && !binding.edtDigitTwo.getText().toString().isEmpty()
                                && !binding.edtDigitThree.getText().toString().isEmpty() && !binding.edtDigitFour.getText().toString().isEmpty()) {
                            if (isAdded()) {
                                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(binding.edtDigitFour.getWindowToken(), 0);
                                Bundle bundle = new Bundle();
                                bundle.putStringArray("pass", pass);
                                bundle.putBoolean("fromSettings", false);
                                if (pass.length == 4) {
                                    if (isAdded())
                                        NavigationUtils.navigate(requireActivity(), R.id.reEnterPasswordFragment, bundle);

//                                NavigationUtils.navigate(binding.getRoot(), R.id.action_setUserPasswordFragmentOne_to_setUserFragment, bundle);
                                    binding.edtDigitOne.getText().clear();
                                    binding.edtDigitTwo.getText().clear();
                                    binding.edtDigitThree.getText().clear();
                                    binding.edtDigitFour.getText().clear();
                                    binding.edtDigitOne.requestFocus();
                                }
                            }
                        } else {
                            binding.edtDigitOne.getText().clear();
                            binding.edtDigitTwo.getText().clear();
                            binding.edtDigitThree.getText().clear();
                            binding.edtDigitFour.getText().clear();
                            binding.edtDigitOne.requestFocus();
                            if (isAdded())
                                Toast.makeText(requireContext(), "Enter your 4digit password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    // userDetailsProvider.getUserDetails(stringBuilder);

                }
            }
        });
    }


}

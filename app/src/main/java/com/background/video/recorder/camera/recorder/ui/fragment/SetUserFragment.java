package com.background.video.recorder.camera.recorder.ui.fragment;

import android.content.Context;
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
import com.background.video.recorder.camera.recorder.databinding.LayoutEnterPasswordBinding;
import com.background.video.recorder.camera.recorder.listener.UserDetailsProvider;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;
import java.util.Arrays;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import roozi.app.ads.AdsManager;

public class SetUserFragment extends Fragment {
    private static final String TAG = "SetUserFragment";
    public static boolean fromAskPassword;
    private final int size = 1;
    private final String[] reEnterPass = new String[4];
    private String answer;
    private int password;
    private LayoutEnterPasswordBinding passwordBinding;
    private UserDetailsProvider userDetailsProvider;
    private StringBuilder stringBuilder;
    private String userEnterPass;
    private String[] oldPass;
    private boolean notFromSettings;

    public SetUserFragment() {
    }

    public void setPasswordProvider(UserDetailsProvider userDetailsProvider) {
        this.userDetailsProvider = userDetailsProvider;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fromAskPassword = getArguments().getBoolean("fromAskPassword");
        } else {
        }
        if (getArguments() != null) {
            notFromSettings = getArguments().getBoolean("fromSettings");
            if (!notFromSettings) {
                oldPass = getArguments().getStringArray("pass");
                Log.e(TAG, "onCreate: " + oldPass[0] + oldPass[1] + oldPass[2] + oldPass[3]);
            } else {
                oldPass = getArguments().getStringArray("pass");
                Log.e(TAG, "onCreate: " + oldPass[0] + oldPass[1] + oldPass[2] + oldPass[3]);
            }


        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        passwordBinding = LayoutEnterPasswordBinding.inflate(inflater, container, false);

        view = passwordBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



        showAds();
        stringBuilder = new StringBuilder();
//        passwordBinding.btnSaveUser.setOnClickListener(view1 -> {
//            answer = passwordBinding.edtSetQuestionAnsWer.getText().toString();
//            Log.d(TAG, "onViewCreated: "+answer);
//            try {
//                password = Integer.parseInt(passwordBinding.edtSetPassword.getText().toString());
//            }catch (NumberFormatException formatException){
//                formatException.getCause();
//            }
//            String question = passwordBinding.edtSetQuestion.getText().toString();
//            userDetailsProvider.getUserDetails(password, answer,question);
//        });


        passwordBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!fromAskPassword){
//                    NavigationUtils.navigate(passwordBinding.getRoot(),R.id.);
//                }else {
//                    NavigationUtils.navigateBack(passwordBinding.getRoot());
//                }
                NavigationUtils.navigateBack(passwordBinding.getRoot());
            }

        });
        setFocus();
    }

    private void showAds() {
        AdsManager.Companion.nativee(
                SplashTwoFragment.getBoolean(AdsKeys.Admob_SetPassword_Screen_Two_Native),
                SplashTwoFragment.getBoolean(AdsKeys.Facebook_SetPassword_Screen_Two_Native),
                requireActivity(), passwordBinding.nativeAd, new Function1<Boolean, Unit>() {
                    @Override
                    public Unit invoke(Boolean aBoolean) {
                        return null;
                    }
                }
        );
    }

    private void setFocus() {
        passwordBinding.edtDigitOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == size) {

                    passwordBinding.edtDigitTwo.requestFocus();
                    reEnterPass[0] = s.toString();
                    Log.d(TAG, "onTextChanged: " + stringBuilder.toString());
                }
            }
        });
        passwordBinding.edtDigitTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == size) {
                    passwordBinding.edtDigitThree.requestFocus();
                    reEnterPass[1] = s.toString();
                    Log.d(TAG, "onTextChanged: " + stringBuilder.toString());
                }
            }
        });
        passwordBinding.edtDigitThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == size) {
                    passwordBinding.edtDigitFour.requestFocus();
                    reEnterPass[2] = s.toString();
                    Log.d(TAG, "onTextChanged: " + stringBuilder.toString());
                }
            }
        });
        passwordBinding.edtDigitFour.addTextChangedListener(new TextWatcher() {
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
                        reEnterPass[3] = s.toString();
                        if (!passwordBinding.edtDigitOne.getText().toString().isEmpty() && !passwordBinding.edtDigitTwo.getText().toString().isEmpty()
                                && !passwordBinding.edtDigitThree.getText().toString().isEmpty() && !passwordBinding.edtDigitFour.getText().toString().isEmpty() && isAdded()) {
                            Log.d(TAG, "onTextChanged: " + stringBuilder.toString());
                            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(passwordBinding.edtDigitFour.getWindowToken(), 0);
                            Bundle bundle = new Bundle();
                            bundle.putStringArray("pass", reEnterPass);
                            bundle.putBoolean("fromSettings", true);
                            if (Arrays.equals(oldPass, reEnterPass) && isAdded()) {
                                NavigationUtils.navigate(requireActivity(), R.id.mySecurityFragment, bundle);
//                                NavigationUtils.navigate(passwordBinding.getRoot(), R.id.action_setUserFragment_to_securityFragment, bundle);
                                passwordBinding.edtDigitOne.getText().clear();
                                passwordBinding.edtDigitTwo.getText().clear();
                                passwordBinding.edtDigitThree.getText().clear();
                                passwordBinding.edtDigitFour.getText().clear();
                                passwordBinding.edtDigitOne.requestFocus();
                            } else {
                                passwordBinding.edtDigitOne.getText().clear();
                                passwordBinding.edtDigitTwo.getText().clear();
                                passwordBinding.edtDigitThree.getText().clear();
                                passwordBinding.edtDigitFour.getText().clear();
                                passwordBinding.edtDigitOne.requestFocus();
                                if (isAdded())
                                    Toast.makeText(requireContext(), "Please enter same passcode as you entered before", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            passwordBinding.edtDigitOne.getText().clear();
                            passwordBinding.edtDigitTwo.getText().clear();
                            passwordBinding.edtDigitThree.getText().clear();
                            passwordBinding.edtDigitFour.getText().clear();
                            passwordBinding.edtDigitOne.requestFocus();
                            if (isAdded())
                                Toast.makeText(requireContext(), "Re Enter your 4digit password", Toast.LENGTH_SHORT).show();
                        }

                        // userDetailsProvider.getUserDetails(stringBuilder);
                    }

                } else {
                    if (s.toString().length() == size) {
                        reEnterPass[3] = s.toString();
                        Log.d(TAG, "onTextChanged: " + reEnterPass[0] + reEnterPass[1] + reEnterPass[2] + reEnterPass[3]);
                        if (!passwordBinding.edtDigitOne.getText().toString().isEmpty() && !passwordBinding.edtDigitTwo.getText().toString().isEmpty()
                                && !passwordBinding.edtDigitThree.getText().toString().isEmpty() && !passwordBinding.edtDigitFour.getText().toString().isEmpty() && isAdded()) {
                            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(passwordBinding.edtDigitFour.getWindowToken(), 0);
                            Bundle bundle = new Bundle();
                            bundle.putStringArray("pass", reEnterPass);
                            bundle.putBoolean("fromSettings", false);
                            if (Arrays.equals(oldPass, reEnterPass)) {
                                if (isAdded())
                                    NavigationUtils.navigate(requireActivity(), R.id.mySecurityFragment, bundle);
//                                NavigationUtils.navigate(passwordBinding.getRoot(), R.id.action_setUserFragment_to_securityFragment, bundle);
                                passwordBinding.edtDigitOne.getText().clear();
                                passwordBinding.edtDigitTwo.getText().clear();
                                passwordBinding.edtDigitThree.getText().clear();
                                passwordBinding.edtDigitFour.getText().clear();
                                passwordBinding.edtDigitOne.requestFocus();
                            } else {
                                passwordBinding.edtDigitOne.getText().clear();
                                passwordBinding.edtDigitTwo.getText().clear();
                                passwordBinding.edtDigitThree.getText().clear();
                                passwordBinding.edtDigitFour.getText().clear();
                                passwordBinding.edtDigitOne.requestFocus();
                                if (isAdded())
                                    Toast.makeText(requireContext(), "Please enter same passcode as you entered before", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            passwordBinding.edtDigitOne.getText().clear();
                            passwordBinding.edtDigitTwo.getText().clear();
                            passwordBinding.edtDigitThree.getText().clear();
                            passwordBinding.edtDigitFour.getText().clear();
                            passwordBinding.edtDigitOne.requestFocus();
                            if (isAdded())
                                Toast.makeText(requireContext(), "Re Enter your 4digit password", Toast.LENGTH_SHORT).show();
                        }


                        // userDetailsProvider.getUserDetails(stringBuilder);

                    }
                }
            }
        });
    }

}

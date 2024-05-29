package com.background.video.recorder.camera.recorder.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.databinding.LayoutSetQuestionBinding;
import com.background.video.recorder.camera.recorder.listener.SecurityQuestion;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;

import roozi.app.ads.AdsManager;

public class SecurityFragment extends Fragment {
    private static final String TAG = "SecurityFragment";
    String[] pass;
    boolean fromSettings;
    private LayoutSetQuestionBinding questionBinding;
    private SecurityQuestion sequrityQuestion;


    public SecurityFragment() {
    }

    public void setSecurityQuestionProvider(SecurityQuestion securityQuestion) {
        this.sequrityQuestion = securityQuestion;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pass = getArguments().getStringArray("pass");
            fromSettings = getArguments().getBoolean("fromSettings");
        } else {
            Log.e(TAG, "onCreate: Security fragment " + " pass is null");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        questionBinding = LayoutSetQuestionBinding.inflate(inflater, container, false);
        return questionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showAd();
        questionBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String question = questionBinding.edtQuestion.getText().toString();
                String answer = questionBinding.edtAnswer.getText().toString();
                if (answer.isEmpty() && isAdded()) {
                    Toast.makeText(requireContext(), "Please provide following information first!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        if (fromSettings) {
                            Bundle bundle = new Bundle();
                            bundle.putStringArray("pass", pass);
                            bundle.putString("answer", answer);
                            bundle.putBoolean("fromSettings", true);
                            if (isAdded())
                                NavigationUtils.navigate(requireActivity(), R.id.mySuccessFragment, bundle);
//                            NavigationUtils.navigate(questionBinding.getRoot(), R.id.action_securityFragment_to_savedSuccessFragment, bundle);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putStringArray("pass", pass);
                            bundle.putString("answer", answer);
                            bundle.putBoolean("fromSettings", false);
                            if (isAdded())
                                NavigationUtils.navigate(requireActivity(), R.id.mySuccessFragment, bundle);

//                            NavigationUtils.navigate(questionBinding.getRoot(), R.id.action_securityFragment_to_savedSuccessFragment, bundle);
                        }
                        //  sequrityQuestion.securityQuestionListener(question, answer);

                    } catch (NullPointerException nullPointerException) {
                        Log.e(TAG, "onClick: " + nullPointerException.getLocalizedMessage());
                    }

                }

            }
        });
    }

    private void showAd() {
//        AdsManager.Companion.interstitial(
//                SplashTwoFragment.getBoolean(AdsKeys.Admob_Save_Password_Screen_Inter),
//                SplashTwoFragment.getBoolean(AdsKeys.Facebook_Save_Password_Screen_Inter),
//                AdsKeys.Admob_Save_Password_Screen_Inter,
//                AdsKeys.Facebook_Save_Password_Screen_Inter,
//                requireActivity() , b->null
//        );
    }
}

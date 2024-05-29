package com.background.video.recorder.camera.recorder.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.databinding.LayoutForgotBinding;
import com.background.video.recorder.camera.recorder.model.Authentication;
import com.background.video.recorder.camera.recorder.ui.dialog.WrongPasswordDialog;
import com.background.video.recorder.camera.recorder.util.UserAuthenticationUtil;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;
import com.background.video.recorder.camera.recorder.viewmodel.MediaFilesViewModel;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import roozi.app.ads.AdsManager;


public class ForgotFragment extends Fragment implements UserAuthenticationUtil.UserVerification {
    private static final String TAG = "ForgotFragment";
    public static String userEnterAsnwer;
    private MediaFilesViewModel viewModel;
    private LayoutForgotBinding binding;
    private UserAuthenticationUtil authentication;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private WrongPasswordDialog wrongPasswordDialog;


    public ForgotFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isAdded()) {
            viewModel = new ViewModelProvider(this,
                    (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                            .getInstance(requireActivity().getApplication()))
                    .get(MediaFilesViewModel.class);
            authentication = new UserAuthenticationUtil(requireContext(), this, false);
            sharedPreferences = requireActivity().getSharedPreferences("stateSavePreference", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LayoutForgotBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showAd();
        initComponents();
    }

    private void showAd() {
        AdsManager.Companion.nativee(
                SplashTwoFragment.getBoolean(AdsKeys.Admob_Forget_Password_Screen_Native),
                SplashTwoFragment.getBoolean(AdsKeys.Facebook_Forget_Password_Screen_Native),
                requireActivity(), binding.nativeAd, new Function1<Boolean, Unit>() {
                    @Override
                    public Unit invoke(Boolean aBoolean) {
                        return null;
                    }
                }
        );
    }

    private void initComponents() {


        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.edtAnswer.getText().toString().isEmpty() && isAdded()) {
                    Toast.makeText(requireContext(), "Enter valid answer first!", Toast.LENGTH_SHORT).show();
                } else {
                    userEnterAsnwer = binding.edtAnswer.getText().toString();
                    Log.e(TAG, "onClick: " + userEnterAsnwer);

                    if (isAdded())
                        viewModel.getGetAllUser().observe(requireActivity(), new Observer<List<Authentication>>() {
                            @Override
                            public void onChanged(List<Authentication> authentications) {
                                authentication.userAuthentication(authentications);
                                binding.edtAnswer.getText().clear();
                            }
                        });
                }
            }
        });
    }

    @Override
    public void userVerified(boolean password) {
        if (isAdded())
            requireActivity().runOnUiThread(() -> {
                if (password) {
                    binding.animation.setVisibility(View.VISIBLE);
                    binding.main.setVisibility(View.GONE);
                    binding.animation.addAnimatorListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            editor.putBoolean("fromForgot", true);
                            editor.apply();
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("fromAskPassword", false);
                            if (isAdded())
                                NavigationUtils.navigate(requireActivity(), R.id.enterPasswordFragment, bundle);
                        }
                    });
                } else {
                    Log.e(TAG, "run: " + "im in else");
                    binding.animation.setAnimation(R.raw.anim_wrong_password);
                    binding.animation.setVisibility(View.VISIBLE);
                    binding.main.setVisibility(View.GONE);
                    binding.animation.addAnimatorListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            binding.animation.setVisibility(View.GONE);
                            binding.main.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
    }
}

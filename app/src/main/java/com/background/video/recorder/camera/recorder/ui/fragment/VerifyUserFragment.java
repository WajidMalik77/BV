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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.background.video.recorder.camera.recorder.databinding.LayoutAuthUserBinding;
import com.background.video.recorder.camera.recorder.model.Authentication;
import com.background.video.recorder.camera.recorder.util.UserAuthenticationUtil;
import com.background.video.recorder.camera.recorder.viewmodel.MediaFilesViewModel;

import java.util.List;

public class VerifyUserFragment extends Fragment implements UserAuthenticationUtil.UserVerification {
    private static final String TAG = "VerifyUserFragment";
    private LayoutAuthUserBinding userBinding;
    private MediaFilesViewModel viewModel;
    public static int userEnteredPassword;
    private UserAuthenticationUtil userAuthenticationUtil;
    int size = 1;
    private StringBuilder stringBuilder;
    int count = 0;


    public VerifyUserFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (isAdded())
            viewModel = new ViewModelProvider(
                    this,
                    (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())
            ).get(MediaFilesViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userBinding = LayoutAuthUserBinding.inflate(inflater, container, false);
        return userBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initComponents();

    }

    private void initComponents() {
        setListeners();
        stringBuilder = new StringBuilder();
    }

    private void setListeners() {

//        verifyPasswordBinding.btnVerifyPassword.setOnClickListener(view1 -> {
//            userEnteredPassword = Integer.parseInt(verifyPasswordBinding.edtInputPass.getText().toString());
//            viewModel.getGetAllUser().observe(requireActivity(), new Observer<List<Authentication>>() {
//                @Override
//                public void onChanged(List<Authentication> authentications) {
//                    Log.d(TAG, "onChanged: " + authentications.get(0).getUserId());
//                    userAuthenticationUtil.userAuthentication(authentications);
//                }
//            });
//
//        });
        setFocus();
    }

    @Override
    public void userVerified(boolean password) {
        if (isAdded())
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (password) {

                    } else {
                        Log.d(TAG, "userVerified: " + password);
                        if (isAdded())
                            Toast.makeText(requireContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void setFocus() {
        userBinding.tvDigitOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() == size) {
                    count++;
                    userBinding.tvDigitTwo.requestFocus();
                    stringBuilder.append(s);
                    Log.d(TAG, "onTextChanged: " + stringBuilder.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        userBinding.tvDigitTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == size) {
                    count++;
                    userBinding.tvDigitThree.requestFocus();
                    stringBuilder.append(s);
                    Log.d(TAG, "onTextChanged: " + stringBuilder.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        userBinding.tvDigitThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == size) {
                    userBinding.tvDigitFour.requestFocus();
                    stringBuilder.append(s);
                    count++;
                    Log.d(TAG, "onTextChanged: " + stringBuilder.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        userBinding.tvDigitFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == size) {
                    count++;
                    if (count == 4) {
                        stringBuilder.append(s);
                        Log.d(TAG, "onTextChanged: " + stringBuilder.toString());
                        if (isAdded()) {
                            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(userBinding.tvDigitFour.getWindowToken(), 0);
                            userEnteredPassword = Integer.parseInt(stringBuilder.toString());
                            Log.e(TAG, "onTextChanged: " + "password entered by user === " + userEnteredPassword);
                            if (isAdded())
                                viewModel.getGetAllUser().observe(requireActivity(), new Observer<List<Authentication>>() {
                                    @Override
                                    public void onChanged(List<Authentication> authentications) {
                                        userAuthenticationUtil.userAuthentication(authentications);
                                    }
                                });
                        }
                    }

                    // userDetailsProvider.getUserDetails(stringBuilder);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}

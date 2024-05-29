package com.background.video.recorder.camera.recorder.ui.fragment;

import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static com.background.video.recorder.camera.recorder.util.constant.AdsKeys.isASplash;
import static com.background.video.recorder.camera.recorder.util.constant.Constants.isPermissionGranted;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import com.background.video.recorder.camera.recorder.application.MyApp;
import com.background.video.recorder.camera.recorder.util.SharePref;


public class Permissions extends Fragment {

    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.FOREGROUND_SERVICE,
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); // Retain instance to handle permission requests across configuration changes
//        mCheckPermission();
    }

    private void mCheckPermission() {
        if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            MyApp.Companion.disableshouldshowappopenad();
            requestPermissions(PERMISSIONS, 1011);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1011 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                SharePref.putBoolean(isPermissionGranted, true);
                // Check if storage and camera permissions are granted
                if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // Set shouldNotShowAd to 2

                }
            } else {
                // Permission denied
                SharePref.putBoolean(isPermissionGranted, false);
            }
        }
        MyApp.Companion.enableshouldshowappopenad();
    }
}


//Bewst AD Logic
//public class Permissions extends Fragment {
//
//    String[] PERMISSIONS = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.FOREGROUND_SERVICE,
//    };
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setRetainInstance(true); // Retain instance to handle permission requests across configuration changes
//        mCheckPermission();
//
//    }
//
//    private void mCheckPermission() {
//        if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
//            requestPermissions(PERMISSIONS, 1011);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == 1011 && grantResults.length > 0) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted
//                SharePref.putBoolean(isPermissionGranted, true);
//            } else {
//                // Permission denied
//                SharePref.putBoolean(isPermissionGranted, false);
//            }
//        }
//    }
//}
//


//public class Permissions extends Fragment {
//    FragmentPermissionsBinding binding;
//
//    String[] PERMISSIONS = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.FOREGROUND_SERVICE,
//    };
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = FragmentPermissionsBinding.inflate(getLayoutInflater(), container, false);
//
//        listeners();
//
//        mCheckPermission();
//        return binding.getRoot();
//    }
//
//    private void listeners() {
//        binding.allow.setOnClickListener(view -> {
//            mCheckPermission();
//        });
//
//        binding.deny.setOnClickListener(view -> {
//            if (isAdded()) {
//                SharePref.putBoolean(isASplash , true);
//                SharePref.putBoolean(isPermissionGranted, true);
//                startActivity(new Intent(requireContext(), HomeActivity.class));
//                requireActivity().finish();
//            }
//        });
//    }
//
//    private void mCheckPermission() {
//        if (isAdded())
//            if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
//                requestPermissions(PERMISSIONS, 1011);
//            }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == 1011 && grantResults.length > 0) {
//            if (isAdded())
//                Toast.makeText(requireContext(), "accepted", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(requireContext(), HomeActivity.class));
//            SharePref.putBoolean(isPermissionGranted, true);
//            if (isAdded()) {
//                SharePref.putBoolean(isASplash , true);
//                startActivity(new Intent(requireContext(), HomeActivity.class));
//                requireActivity().finish();
//            }
//
//        } else {
//            if (isAdded())
//                Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT).show();
//        }
//    }
//
////    @SuppressLint("NewApi")
////    public static void premiumDialog(Context context, Boolean s) {
////        Activity activity = (Activity) context;
////        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////        PremiumBinding bind = PremiumBinding.inflate(layoutInflater);
////        Dialog dialog = new Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
////        dialog.setContentView(bind.getRoot());
////        bind.iLikeAd.setOnClickListener(view -> {
////            dialog.dismiss();
////        });
////        bind.continueBtn.setOnClickListener(view -> {
////            dialog.dismiss();
////        });
////        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
////            @Override
////            public void onDismiss(DialogInterface dialogInterface) {
////                if (s) {
////                    activity.startActivity(new Intent(context, HomeActivity.class));
////                    activity.finish();
////                } else
////                    dialogInterface.dismiss();
//////                if (!SharePref.getBoolean(appLocked , false)){
//////                    Navigation.findNavController(vieww).navigate(R.id.askingSetPassword);
////////                    NavigationUtils.navigate(privacyBinding.getRoot(), R.id.action_privacyFragment_to_askingSetPassword);
//////                }else{
//////                    context.startActivity(new Intent(context , HomeActivity.class));
//////                }
////            }
////        });
////        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
////        dialog.show();
////    }
//}


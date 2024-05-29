package com.background.video.recorder.camera.recorder.ui.fragment;

import static android.content.Context.MEDIA_PROJECTION_SERVICE;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.ActionOnAdClosedListener;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.ads.InterstitialClass;
import com.background.video.recorder.camera.recorder.application.MyApp;
import com.background.video.recorder.camera.recorder.databinding.FragmentScreenRecording2Binding;
import com.background.video.recorder.camera.recorder.databinding.LayoutHomeFragmentNewBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.screenRecordingListener.MyHBRecorderListener;
import com.background.video.recorder.camera.recorder.screenRecordingListener.ScreenRecordingListener;
import com.background.video.recorder.camera.recorder.util.FirebaseEvents;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;
import com.hbisoft.hbrecorder.HBRecorder;
import com.hbisoft.hbrecorder.HBRecorderListener;

import java.io.File;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;


public class ScreenRecordingFragment extends Fragment implements HBRecorderListener {

    public FragmentScreenRecording2Binding binding;
    private SurfaceHolder surfaceHolder;
    private SharedPreferences sharedPreferences;
    private String admob_interstitial_home_id = "";
    private static final String PREFS_NAME = "ScreenRecordingPrefs";
    private static final String BUTTON_TEXT_KEY = "ButtonText";
    private SharedPrefsHelper prefs = null;
    SharedPreferences sharedPrefs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScreenRecording2Binding.inflate(inflater, container, false);
        surfaceHolder = binding.surfaceView.getHolder();
        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        sharedPrefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        prefs = SharedPrefsHelper.getInstance(getActivity());
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {

            admob_interstitial_home_id = localPrefs.getadmob_interstitial_home_idId();

        }


        initHbRecorder();

        return binding.getRoot();
    }

    private void restoreButtonText() {
        String buttonText = sharedPreferences.getString(BUTTON_TEXT_KEY, "Start");
        startrecordingbtnfragemtn.setText(buttonText);
    }

    private void saveButtonText(String text) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BUTTON_TEXT_KEY, text);
        editor.apply();
    }

    private void initHbRecorder() {
        if (getContext() != null) {
            surfaceHolder = binding.surfaceView.getHolder();
            surfaceHolder.addCallback(new ScreenRecordingListener());
            hbRecorder = new HBRecorder(requireContext(), new MyHBRecorderListener());
            hbRecorder.enableCustomSettings();
            // Set the output path to the new directory
            String appName = "BackgroundVideoMaker"; // Replace with your actual app name
//            hbRecorder.setOutputPath(getRecordingDir(requireContext(), appName));

            hbRecorder = new HBRecorder(getActivity(), new HBRecorderListener() {
                @Override
                public void HBRecorderOnStart() {
                    startrecordingbtnfragemtn.setText("Pause");
                    saveButtonText("Pause");
                    Log.d("hbies", "HBRecorderOnStart: ");
                }

                @Override
                public void HBRecorderOnComplete() {
                    startrecordingbtnfragemtn.setText("Start");
                    saveButtonText("Start");
                    Log.d("hbies", "HBRecorderOnComplete: ");

                }

                @Override
                public void HBRecorderOnError(int errorCode, String reason) {
                    Log.d("hbies", "HBRecorderOnError: ");

                }

                @Override
                public void HBRecorderOnPause() {
                    Log.d("hbies", "HBRecorderOnPause: ");

                }

                @Override
                public void HBRecorderOnResume() {
                    Log.d("hbies", "HBRecorderOnResume: ");

                }
            });


            hbRecorder.setOutputPath(getAudioDir(requireContext()));
            hbRecorder.setNotificationSmallIconVector(R.mipmap.ic_app_icon_foreground);

        }

    }

    public static String Screen_Recording = "Screen_Recording";

    private String getAudioDir(Context context) {
        File file = context.getExternalFilesDir(Screen_Recording);
//        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        if (file != null) {
            if (!file.exists()) {
                file.mkdirs(); // Create the directory if it doesn't exist
            }
            return file.getPath();
        } else {
            // Handle the case where getExternalFilesDir() returns null
            Log.e("TAG", "External storage is not available.");
            return null; // or return a default path or handle it as needed
        }
    }

    HBRecorder hbRecorder;
    TextView startrecordingbtnfragemtn;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        startrecordingbtnfragemtn = view.findViewById(R.id.startrecordingbtnfragemtn);
        restoreButtonText(); // Restore button text when fragment is created
        view.findViewById(R.id.startrecordingbtnfragemtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseEvents.Companion.logAnalytic("DashBoard_Screen_recordings_Btn_Click");

                //////////////////////////

                MyApp.Companion.disableshouldshowappopenad();
                if (hbRecorder.isBusyRecording()) {
                    FirebaseEvents.Companion.logAnalytic("HOME_SCREEN_REC_STOP_CLICKED");
                    stopWarningDialog();
                }
                //else start recording
                else {
                    FirebaseEvents.Companion.logAnalytic("HOME_SCREEN_REC_CLICKED");
                    startScreenRecording();
                }
            }
        });

        binding.countDown.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                binding.countDown.pauseAnimation();
                binding.countDown.setVisibility(View.GONE);
                startRecordingScreen();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


    }

    private void startRecordingScreen() {
        if (getContext() != null) {
            MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) requireContext().getSystemService(MEDIA_PROJECTION_SERVICE);
            Intent permissionIntent = mediaProjectionManager.createScreenCaptureIntent();
            someActivityResultLauncher.launch(permissionIntent);
        }
    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        hbRecorder.startScreenRecording(result.getData(), result.getResultCode());
//                        MyApp.Companion.disableshouldshowappopenad();
                    }
                }
            });

    private void stopWarningDialog() {
        if (getContext() != null)
            new AlertDialog.Builder(requireContext())
                    .setTitle("Warning Alert")
                    .setCancelable(true)
                    .setIcon(R.mipmap.ic_app_icon_foreground)
                    .setMessage("Are You Sure You Want to Stop Screen Recording.")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        hbRecorder.stopScreenRecording();
                        dialogInterface.dismiss();
                        startrecordingbtnfragemtn.setText("Start");
                        saveButtonText("Start"); // Save the button text

//                        GalleryFragment galleryFragment = new GalleryFragment();
//                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                        fragmentManager.beginTransaction()
//                                .replace(R.id.fragmentContainerViewHome, galleryFragment)
//                                .addToBackStack(null)
//                                .commit();


//                        if (photoeditorcollagemakerHomeAdmobInter) {


                            InterstitialClass.requestInterstitial(getActivity(), admob_interstitial_home_id, new ActionOnAdClosedListener() {
                                @Override
                                public void ActionAfterAd() {

                                    MyApp.Companion.enableshouldshowappopenad();
                                    NavigationUtils.navigate(getActivity(), R.id.galleryFragment);
                                }
                            });

//                        } else {
//                            NavigationUtils.navigate(getActivity(), R.id.imagePickerFragment,args);
//
//                        }
//                        InterstitialAdUtils.INSTANCE.showInterstitialRecovery(getActivity(), "", new Function0<Unit>() {
//                            @Override
//                            public Unit invoke() {
//                                NavigationUtils.navigate(requireActivity(), R.id.action_screenRecordingFragment2_to_galleryFragment);
//
//                                return null;
//                            }
//                        });


                    })
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
    }

    private void startScreenRecording() {
        MyApp.Companion.disableshouldshowappopenad();

        if (getContext() != null)
            new AlertDialog.Builder(requireContext())
                    .setTitle("Screen Recording Alert")
                    .setCancelable(true)
                    .setIcon(R.mipmap.ic_app_icon_foreground)
                    .setMessage("This Function Will Record Your Mobile Screen Until You Will Stop it.\nAre You Sure You Want to Start Screen Recording.")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        checkPermission();
                        dialogInterface.dismiss();

                        startrecordingbtnfragemtn.setText("Pause");
                        saveButtonText("Pause"); // Save the button text
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
    }

    private void checkPermission() {
        if (getActivity() != null)
            if (requireActivity().checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                binding.countDown.setVisibility(View.VISIBLE);
                binding.countDown.playAnimation();
//            startRecordingScreen();
            } else {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
            }
    }

    ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        binding.countDown.setVisibility(View.VISIBLE);
                        binding.countDown.playAnimation();
//                startRecordingScreen();
                    } else if (getContext() != null)
                        Toast.makeText(requireContext(), "permission denied", Toast.LENGTH_SHORT).show();
                }
            });


    @Override
    public void HBRecorderOnStart() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startrecordingbtnfragemtn.setText("Pause");
                saveButtonText("Pause");
            }
        });
    }

    @Override
    public void HBRecorderOnPause() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startrecordingbtnfragemtn.setText("Resume");
                saveButtonText("Resume");

                MyApp.Companion.enableshouldshowappopenad();
            }
        });
    }

    @Override
    public void HBRecorderOnComplete() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startrecordingbtnfragemtn.setText("Start");
                saveButtonText("Start");


            }
        });
    }


    @Override
    public void HBRecorderOnError(int errorCode, String reason) {
        // Handle error if needed
    }

    @Override
    public void HBRecorderOnResume() {
        // Handle resume if needed
    }

}
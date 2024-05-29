package com.background.video.recorder.camera.recorder.ui.fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MEDIA_PROJECTION_SERVICE;
import static com.background.video.recorder.camera.recorder.service.BackgroundVideoRecording.addWindowManager;
import static com.background.video.recorder.camera.recorder.util.constant.AdsKeys.isASplash;
import static com.background.video.recorder.camera.recorder.util.constant.Constants.isPermissionGranted;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.background.video.recorder.camera.recorder.ImagePicker.Config;
import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.ActionOnAdClosedListener;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.ads.InterstitialClass;
import com.background.video.recorder.camera.recorder.application.MyApp;
import com.background.video.recorder.camera.recorder.databinding.LayoutHomeFragmentBinding;
import com.background.video.recorder.camera.recorder.databinding.LayoutHomeFragmentNewBinding;
import com.background.video.recorder.camera.recorder.databinding.PermissionOverlayDialogBinding;
import com.background.video.recorder.camera.recorder.databinding.SaveDialogBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.listener.OnDragTouchListener;
import com.background.video.recorder.camera.recorder.model.MediaFiles;
import com.background.video.recorder.camera.recorder.screenRecordingListener.MyHBRecorderListener;
import com.background.video.recorder.camera.recorder.screenRecordingListener.ScreenRecordingListener;
import com.background.video.recorder.camera.recorder.service.BackgroundVideoRecording;
import com.background.video.recorder.camera.recorder.util.FirebaseEvents;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.background.video.recorder.camera.recorder.util.constant.Constants;
import com.background.video.recorder.camera.recorder.util.file.FileUtils;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;
import com.background.video.recorder.camera.recorder.viewmodel.MediaFilesViewModel;
import com.background.video.recorder.camera.recorder.viewmodel.SharedViewModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.google.common.util.concurrent.ListenableFuture;
import com.hbisoft.hbrecorder.HBRecorder;
import com.hbisoft.hbrecorder.HBRecorderListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.request.ExplainScope;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import roozi.app.ads.AdsManager;

public class HomeFragment extends Fragment implements HBRecorderListener {
    private static final String TAG = "HomeFragment123";
    private static final int SCREEN_RECORD_REQUEST_CODE = 77;
    private static final int PERMISSION_REQUEST_CODE = 99;
    private final String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    //    public LayoutHomeFragmentBinding binding;
    public LayoutHomeFragmentNewBinding binding;
    public boolean recordingStatus = false;
    public SharedPreferences servicePreferences;
    public SharedPreferences.Editor serviceEditor;
    private MediaFilesViewModel mediaFilesViewModel;
    private BroadcastReceiver videoStoppedReceiver;
    private SharedPreferences settingPreferences;
    private FileUtils fileUtils;
    private SharedPreferences dialogPreference;
    private Bundle bundle;
    private Boolean isSplash = false;
    ProgressDialog progressDialog;
    private boolean isProgressDialogShownBecauseOfAd = false;
    SharedPreferences sharedPrefs;
    boolean homeInter, splashInter;
    boolean native_bg;
    HBRecorder hbRecorder;
    private NavController navController;

    public HomeFragment() {
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        navController = Navigation.findNavController(getActivity(), R.id.fragmentContainerViewHome);
//    }

    private void addMediaFileToDatabase() {
        BroadcastReceiver mediaFileReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (intent != null) {
                        MediaFiles mediaFiles = (MediaFiles) intent.getExtras().getSerializable(Constants.MEDIA_FILE_TRANSFER_KEY);
                        if (mediaFiles != null) {
                            Log.e(TAG, "onReceive: " + mediaFiles.getName());
                            mediaFilesViewModel.insertMediaFiles(mediaFiles);


                        } else {
                            Log.e(TAG, "onReceive: mediaFile is null.");
                        }
                    } else {
                        if (isAdded())
                            Toast.makeText(requireActivity().getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onReceive: " + e.getLocalizedMessage());
                }

            }
        };
        IntentFilter intentFilter = new IntentFilter(BackgroundVideoRecording.BROADCAST_ACTION);
        if (isAdded())
            LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(mediaFileReceiver, intentFilter);

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
                    }
                }
            });

//    private void requestPermissions() {
//        Dexter.withContext(getContext())
//                .withPermissions(
//                        Manifest.permission.RECORD_AUDIO,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.CAMERA// For Android 10 or higher, this is not needed for most use-cases
//                )
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        if (report.areAllPermissionsGranted()) {
//                            // Permissions are granted. Start the screen recording.
//                        }
//
//                        if (report.isAnyPermissionPermanentlyDenied()) {
//                            // Handle permanent denial of permissions
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                })
//                .withErrorListener(error -> Log.e("Dexter", "There was an error: " + error.toString()))
//                .onSameThread()
//                .check();
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, start recording
            } else {
                // Permissions denied
            }
        }
    }


    private String getFilePath() {
        String fileName = "HD" + System.currentTimeMillis() + ".mp4";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use MediaStore to create a file in the Movies directory
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MOVIES);
            Uri uri = getActivity().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            return getRealPathFromURI(uri);
        } else {
            // Use the External Storage directory for older versions
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            return new File(directory, fileName).getAbsolutePath();
        }
    }


    private String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private static final String PREFS_NAME = "MyPrefs";
    private static final String PREF_CHECK_PERMISSION = "checkPermission";
    boolean checkPermission = false;
    private SharedPreferences sharedPreferences;
    private static final int CAMERA_REQUEST_CODE = 101; // You can define any request code you want
    private String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 155;
    private SurfaceHolder surfaceHolder;
    public static String Screen_Recording = "Screen_Recording";

    private boolean dashboard_native = true;
    private boolean user_consent_form = true;
    private boolean dashboard_native_facebook = true;
    private boolean home_admob_home_inter = true;
    private boolean overlay_permission_admob_native = true;
    private boolean overlay_permission_facebook_native = true;
    Activity activity;
    private SharedPrefsHelper prefs = null;
    private String admob_native_id = "";
    private String facebook_native_ad_id = "";
    private String admob_interstitial_home_id = "";

    private boolean photoeditorcollagemakerHomeAdmobNative = false;
    private boolean photoeditorcollagemakerAdmobNative = true;
    private boolean photoeditorcollagemakerFacebookNative = true;
    private boolean photoeditorcollagemakerHomeAdmobInter = true;
    private String photoeditorcollagemakerHomeAdmobInterId = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApp.Companion.enableshouldshowappopenad();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        binding = LayoutHomeFragmentNewBinding.inflate(inflater, container, false);

        navController = Navigation.findNavController(getActivity(), R.id.fragmentContainerViewHome);


        sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        checkPermission = sharedPreferences.getBoolean(PREF_CHECK_PERMISSION, false);
//        chkPermission();

        activity = getActivity();
        sharedPrefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        homeInter = sharedPrefs.getBoolean("home_inter", false);
        prefs = SharedPrefsHelper.getInstance(getActivity());
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {


            dashboard_native = localPrefs.getdashboard_nativeSwitch();
//            dashboard_native = true;
            user_consent_form = localPrefs.getuser_consent_formSwitch();
            dashboard_native_facebook = localPrefs.getdashboard_native_facebookSwitch();
            overlay_permission_admob_native = localPrefs.getoverlay_permission_admob_nativeSwitch();
            overlay_permission_facebook_native = localPrefs.getoverlay_permission_facebook_nativeSwitch();
            admob_native_id = localPrefs.getadmob_native_idId();
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId();
            home_admob_home_inter = localPrefs.gethome_admob_home_interSwitch();
//            home_admob_home_inter = true;
            admob_interstitial_home_id = localPrefs.getadmob_interstitial_home_idId();

            photoeditorcollagemakerAdmobNative = localPrefs.getphotoeditorcollagemaker_admob_nativeSwitch();
            photoeditorcollagemakerFacebookNative = localPrefs.getphotoeditorcollagemaker_facebook_nativeSwitch();
            photoeditorcollagemakerHomeAdmobInter = localPrefs.getphotoeditorcollagemaker_home_admob_nativeSwitch();
//            photoeditorcollagemakerHomeAdmobInter = true;
            photoeditorcollagemakerHomeAdmobInterId = localPrefs.getphotoeditorcollagemaker_home_admob_inter_idId();

            Log.d(TAG, "onCreate:  admob_interstitial_home_id  " + dashboard_native);
            Log.d(TAG, "onCreate:  admob_interstitial_home_id  " + dashboard_native_facebook);
        }


        bundle = new Bundle();
        initHbRecorder();
        surfaceHolder = binding.surfaceView.getHolder();


        if (user_consent_form) {

            Log.d("tagaddzy", "onCreateView: in consent everytime");
        }

        sharedPrefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        native_bg = sharedPrefs.getBoolean("native_bg", false);


        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.nativeAd.setVisibility(View.INVISIBLE);
        } else if (dashboard_native) {
            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadMediationNative(
                        activity,
                        binding.nativeAd,
                        admob_native_id,
                        binding.nativeAdContainer,
                        R.layout.ad_native_layout_home
                );
            }
        } else if (dashboard_native_facebook) {
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
            binding.nativeAd.setVisibility(View.GONE);
        }

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

        binding.superCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseEvents.Companion.logAnalytic("DashBoard_Super_Camera_Btn_Click");
                if (checkPermission) {

                    MyApp.Companion.enableshouldshowappopenad();
                    dispatchTakePictureIntent();

                } else {

                    MyApp.Companion.disableshouldshowappopenad();
                    chkPermission();
                }
            }
        });

        binding.screenrecorderBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseEvents.Companion.logAnalytic("DashBoard_Screen_recordings_Btn_Click");


                //////////////////////////
                if (SharePref.getBoolean(AdsKeys.InApp, false)) {
                    NavigationUtils.navigate(requireActivity(), R.id.action_homeFragment_to_screenRecordingFragment2);
                } else if (activity != null) {


                    if (home_admob_home_inter) {
                        InterstitialClass.requestInterstitial(getActivity(), admob_interstitial_home_id, new ActionOnAdClosedListener() {
                            @Override
                            public void ActionAfterAd() {

                                NavigationUtils.navigate(getActivity(), R.id.screenRecordingFragment2);
                            }
                        });

                    } else {
                        NavigationUtils.navigate(getActivity(), R.id.screenRecordingFragment2);

                    }
                }
            }
        });

        binding.photoEditorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseEvents.Companion.logAnalytic("photoEditor_Screen_editor_btn");

                if (checkPermission) {

                    MyApp.Companion.enableshouldshowappopenad();
                    if (SharePref.getBoolean(AdsKeys.InApp, false)) {

                        Config config = new Config();
                        ImagePickerFragment.setConfig(config);

                        Bundle args = new Bundle();
                        args.putString("FromActivity", "EditClick");

                        NavController navController = Navigation.findNavController(activity, R.id.fragmentContainerViewHome);
                        navController.navigate(R.id.action_homeFragment_to_imagePickerFragment, args);
                    } else {
                        Config config = new Config();
                        config.setSelectionMin(1);
                        config.setSelectionLimit(1);

                        ImagePickerFragment.setConfig(config);
                        Bundle args = new Bundle();
                        args.putString("FromActivity", "EditClick");

                        Activity activity = getActivity();
                        if (activity != null) {

                            if (photoeditorcollagemakerHomeAdmobInter) {
                                InterstitialClass.requestInterstitial(getActivity(), photoeditorcollagemakerHomeAdmobInterId, new ActionOnAdClosedListener() {
                                    @Override
                                    public void ActionAfterAd() {

                                        NavigationUtils.navigate(getActivity(), R.id.imagePickerFragment, args);
                                    }
                                });

                            } else {
                                NavigationUtils.navigate(getActivity(), R.id.imagePickerFragment, args);

                            }
                        }
                    }
                } else {

                    MyApp.Companion.disableshouldshowappopenad();
                    chkPermission();
                }
                //////////////////////////


            }
        });

        binding.collageMakerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseEvents.Companion.logAnalytic("photoEditor_Screen_collage_btn");

                if (checkPermission) {

                    MyApp.Companion.enableshouldshowappopenad();
                    if (SharePref.getBoolean(AdsKeys.InApp, false)) {
                        Config config = new Config();
                        config.setSelectionMin(2);
                        config.setSelectionLimit(9);
                        ImagePickerFragment.setConfig(config);
                        Bundle args = new Bundle();
                        args.putString("FromActivity", "CollageClick");

                        NavController navController = Navigation.findNavController(activity, R.id.fragmentContainerViewHome);
                        navController.navigate(R.id.action_homeFragment_to_imagePickerFragment, args);
                    } else {
                        Config config = new Config();
                        config.setSelectionMin(2);
                        config.setSelectionLimit(9);

                        ImagePickerFragment.setConfig(config);
                        Bundle args = new Bundle();
                        args.putString("FromActivity", "CollageClick");

                        Activity activity = getActivity();
                        if (activity != null) {

                            if (photoeditorcollagemakerHomeAdmobInter) {
                                InterstitialClass.requestInterstitial(getActivity(), photoeditorcollagemakerHomeAdmobInterId, new ActionOnAdClosedListener() {
                                    @Override
                                    public void ActionAfterAd() {

                                        NavigationUtils.navigate(getActivity(), R.id.imagePickerFragment, args);
                                    }
                                });

                            } else {
                                NavigationUtils.navigate(getActivity(), R.id.imagePickerFragment, args);

                            }

//                            InterstitialAdUtils.INSTANCE.showadMobphotoCollagmakerInterstitialAd(
//                                    activity,
//                                    "",
//                                    R.id.action_homeFragment_to_imagePickerFragment,
//                                    config,
//                                    args,
//                                    navController
//                            );
                        }
                    }
                } else {

                    MyApp.Companion.disableshouldshowappopenad();
                    chkPermission();
                }
                //////////////////////////


            }
        });

        binding.myAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseEvents.Companion.logAnalytic("photoEditor_Screen_album_btn");

                if (checkPermission) {

                    MyApp.Companion.enableshouldshowappopenad();
                    if (SharePref.getBoolean(AdsKeys.InApp, false)) {
                        NavigationUtils.navigate(
                                requireActivity(),
                                R.id.action_homeFragment_to_albumFragment
                        );
                    } else {
                        Bundle bundle = new Bundle();
                        Activity activity = getActivity();
                        if (activity != null) {

                            if (photoeditorcollagemakerHomeAdmobInter) {
                                InterstitialClass.requestInterstitial(getActivity(), photoeditorcollagemakerHomeAdmobInterId, new ActionOnAdClosedListener() {
                                    @Override
                                    public void ActionAfterAd() {

                                        NavigationUtils.navigate(getActivity(), R.id.albumFragment);
                                    }
                                });

                            } else {
                                NavigationUtils.navigate(getActivity(), R.id.albumFragment);

                            }
                        }
                    }
                } else {

                    MyApp.Companion.disableshouldshowappopenad();
                    chkPermission();
                }

            }
        });


        if (isAdded()) {
            servicePreferences = requireActivity().getSharedPreferences("backgroundRecordingState", Context.MODE_PRIVATE);
            serviceEditor = servicePreferences.edit();
            settingPreferences = requireActivity().getSharedPreferences("stateSavePreference", Context.MODE_PRIVATE);
            dialogPreference = requireActivity().getSharedPreferences("DialogSavePreference", Context.MODE_PRIVATE);
            fileUtils = new FileUtils(requireContext());

            mediaFilesViewModel = new ViewModelProvider(
                    this,
                    (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())
            ).get(MediaFilesViewModel.class);
        }

        try {
            if (settingPreferences != null) {
                if (settingPreferences.getBoolean("appLocked", false)) {
//                    binding.favouriteLock.setImageResource(R.drawable.lock);
//                    binding.trimLock.setImageResource(R.drawable.lock);
//                    binding.recordingLock.setImageResource(R.drawable.lock);
                } else {
//                    binding.favouriteLock.setImageResource(R.drawable.unlock);
//                    binding.trimLock.setImageResource(R.drawable.unlock);
//                    binding.recordingLock.setImageResource(R.drawable.unlock);
                }
            }
        } catch (NullPointerException e) {
        }
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseEvents.Companion.logAnalytic("DashBoard_Screen_Show");

        sharedPrefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        homeInter = sharedPrefs.getBoolean("home_inter", false);
        binding.startRecording.setImageResource(R.drawable.home_spy_recorder_icon);


        notificationTapped();
        checkingUIUpdate();
        addMediaFileToDatabase();
        videoStoppedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (intent != null) {
                        boolean videoStopped = intent.getBooleanExtra("videoStopped", false);
                        if (videoStopped) {
                            binding.startRecording.setImageResource(R.drawable.home_spy_recorder_icon);
//                            binding.startRecording.setImageResource(R.drawable.spy_camera_icon);
                            setUiVisible();
                            recordingStatus = false;
                            serviceEditor.clear();
                            serviceEditor.commit();
                            serviceEditor.apply();
                            if (isAdded())
                                LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(videoStoppedReceiver);
                        } else {
                            Log.e(TAG, "onReceive: " + "sorry");
                        }
                    }
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        };
        IntentFilter videoStopped = new IntentFilter(BackgroundVideoRecording.BROADCAST_STOP_VIDEO);
        if (isAdded())
            LocalBroadcastManager.getInstance(requireActivity().getApplicationContext()).registerReceiver(videoStoppedReceiver, videoStopped);


        binding.startRecording.setOnClickListener(view1 -> {
//                getPermissionForServices()
//                    Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
//                    binding.startRecording.setImageResource(R.drawable.stop);
//                    FirebaseEvents.Companion.logAnalytic("DashBoard_Start_Recording_Btn_Click");
//                    permissionForService();
                }
        );

//        binding.playContainer.setOnClickListener(view123 -> {
////            showProgressDialog();
////                    binding.startRecording.setImageResource(R.drawable.stop);
//                    FirebaseEvents.Companion.logAnalytic("DashBoard_Start_Recording_Btn_Click");
//                    permissionForService();
//
//                }
//        );

        binding.playContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                binding.startRecording.setImageResource(R.drawable.stop);

                FirebaseEvents.Companion.logAnalytic("DashBoard_Start_secret_Recording_Btn_Click");
                handleRecordingAction();
            }
        });

//        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
//
//        sharedViewModel.getStartRecording().observe(getViewLifecycleOwner(), start -> {
//            if (start) {
//                // Start the recording
//                handleRecordingAction();
//                // Reset the trigger
//                sharedViewModel.resetTrigger();
//            }
//        });


        binding.videoRecorderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseEvents.Companion.logAnalytic("DashBoard_Start_Video_Recording_Btn_Click");
                handleVideoRecordingAction();
//                playBackgroundVideoRecorder();
            }
        });
        binding.startRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRecordingAction();
            }
        });


        binding.recordings.setOnClickListener(view12 -> {
                    showProgressDialog();
                    FirebaseEvents.Companion.logAnalytic("DashBoard_Gallery_Btn_Click");
                    if (isAdded()) {
                        if (settingPreferences.getBoolean("appLocked", false)) {
                            bundle.putString("action", "gallery");
                            if (isAdded())
                                NavigationUtils.navigate(requireActivity(), R.id.verifyPassword, bundle);
                        } else {
                            if (SharePref.getBoolean(AdsKeys.InApp, false)) {
                                NavigationUtils.navigate(requireActivity(), R.id.action_homeFragment_to_galleryFragment);
                            } else if (isAdded())

                                if (home_admob_home_inter) {
                                    InterstitialClass.requestInterstitial(getActivity(), admob_interstitial_home_id, new ActionOnAdClosedListener() {
                                        @Override
                                        public void ActionAfterAd() {

                                            NavigationUtils.navigate(getActivity(), R.id.galleryFragment);
                                        }
                                    });

                                } else {
                                    NavigationUtils.navigate(getActivity(), R.id.galleryFragment);

                                }

                        }
                    }
                }
        );
//        binding.setting.setOnClickListener(view13 ->
//                {
//                    showProgressDialog();
//                    FirebaseEvents.Companion.logAnalytic("DashBoard_Settings_Btn_Click");
////                    Navigation.findNavController(view13).navigate(R.id.mySetting);
//                    startActivityNext(Navigation.findNavController(getView()), R.id.action_homeFragment_to_settingsFragment);
//                }
//        );
        binding.favourite.setOnClickListener(view1 -> {
            showProgressDialog();
            FirebaseEvents.Companion.logAnalytic("DashBoard_Favourite_Btn_Click");

            if (isAdded()) {
                if (settingPreferences.getBoolean("appLocked", false)) {
                    bundle.putString("action", "favourite");
                    Navigation.findNavController(getView()).navigate(R.id.verifyPassword, bundle);
//                    Navigation.findNavController(view1)
                } else {
                    if (SharePref.getBoolean(AdsKeys.InApp, false)) {
                        NavigationUtils.navigate(requireActivity(), R.id.action_homeFragment_to_favFilesFragment);
                    } else if (isAdded()) {

                        if (home_admob_home_inter) {
                            InterstitialClass.requestInterstitial(getActivity(), admob_interstitial_home_id, new ActionOnAdClosedListener() {
                                @Override
                                public void ActionAfterAd() {

                                    NavigationUtils.navigate(getActivity(), R.id.favFilesFragment);
                                }
                            });

                        } else {
                            NavigationUtils.navigate(getActivity(), R.id.favFilesFragment);

                        }
                    }
                }
            }


        });
        binding.trimVideo.setOnClickListener(view1 -> {
            showProgressDialog();
            FirebaseEvents.Companion.logAnalytic("DashBoard_Trim_Btn_Click");
            if (isAdded()) {
                if (settingPreferences.getBoolean("appLocked", false)) {
                    bundle.putString("action", "trim");
                    Navigation.findNavController(getView()).navigate(R.id.verifyPassword, bundle);
                } else {
                    if (SharePref.getBoolean(AdsKeys.InApp, false)) {
                        NavigationUtils.navigate(requireActivity(), R.id.action_homeFragment_to_trimFragment);
                    } else if (isAdded()) {

                        if (home_admob_home_inter) {
                            InterstitialClass.requestInterstitial(getActivity(), admob_interstitial_home_id, new ActionOnAdClosedListener() {
                                @Override
                                public void ActionAfterAd() {

                                    NavigationUtils.navigate(getActivity(), R.id.trimFragment);
                                }
                            });

                        } else {
                            NavigationUtils.navigate(getActivity(), R.id.trimFragment);

                        }
                    }
                }
            }
        });


//        binding.videoToGIFBtn.setOnClickListener(view1 -> {
//            showProgressDialog();
//            FirebaseEvents.Companion.logAnalytic("DashBoard_Trim_Btn_Click");
//            if (isAdded()) {
//                if (settingPreferences.getBoolean("appLocked", false)) {
//                    bundle.putString("action", "trim");
//                    Navigation.findNavController(getView()).navigate(R.id.verifyPassword, bundle);
//                } else {
//                    if (isAdded())
//                        startActivityNext(Navigation.findNavController(getView()), R.id.action_homeFragment_to_videoToGifFragment);
//
//                }
//            }
//        });

        binding.preview.setOnClickListener(view1 -> {

            FirebaseEvents.Companion.logAnalytic("preview_btn_Click");
            Log.d("backyyy", "addWindowManager: clicked1");
            try {
                addWindowManager();
            } catch (IllegalArgumentException e) {
            }

        });
//        homeBinding.btnShowCameraPreview.setOnClickListener(view14 -> getPermissions());
//
//        homeBinding.ibFeedBack.setOnClickListener(view15 -> NavigationUtils.navigate(homeBinding.getRoot(), R.id.action_homeFragment_to_feedbackFragment));
//        homeBinding.ibShare.setOnClickListener(view16 -> {
//            Intent sendIntent = new Intent();
//            sendIntent.setAction(Intent.ACTION_SEND);
//
//            String stringBuilder = "Hi, " +
//                    "\n" +
//                    "I have found this interesting app that records the video in background." +
//                    "\n" +
//                    "Download yours: " +
//                    "\n" +
//                    "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
//
//            sendIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder);
//            sendIntent.setType("text/plain");
//            Intent shareIntent = Intent.createChooser(sendIntent, null);
//            startActivity(shareIntent);
//        });

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
            hbRecorder.setOutputPath(getAudioDir(requireContext()));
            hbRecorder.setNotificationSmallIconVector(R.mipmap.ic_app_icon_foreground);



        }
    }


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
            Log.e(TAG, "External storage is not available.");
            return null; // or return a default path or handle it as needed
        }
    }

    private String getRecordingDir(Context context, String appName) {
        // Get the Movies directory in internal storage
        File moviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);

        // Create a new directory inside Movies for your app
        File appDir = new File(moviesDir, appName);
        if (!appDir.exists()) {
            if (!appDir.mkdirs()) {
                // Handle the case where the app directory couldn't be created
                Log.e(TAG, "Could not create directory for app in Movies folder.");
                return null;
            }
        }

        return appDir.getPath();
    }

//    private void initHbRecorder() {
//        if (getContext() != null) {
//            surfaceHolder = binding.surfaceView.getHolder();
//            surfaceHolder.addCallback(new ScreenRecordingListener());
//            hbRecorder = new HBRecorder(requireContext(), new MyHBRecorderListener());
//            hbRecorder.enableCustomSettings();
//
//            // Set the output path to the new directory
//            String appName = "BackgroundVideoMaker"; // Replace with your actual app name
//            hbRecorder.setOutputPath(getRecordingDir(requireContext(), appName));
//
//            hbRecorder.setNotificationSmallIconVector(R.mipmap.ic_app_icon_foreground);
//        }
//    }


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
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
    }


    private void startScreenRecording() {
        if (getContext() != null)
            new AlertDialog.Builder(requireContext())
                    .setTitle("Screen Recording Alert")
                    .setCancelable(true)
                    .setIcon(R.mipmap.ic_app_icon_foreground)
                    .setMessage("This Function Will Record Your Mobile Screen Until You Will Stop it.\nAre You Sure You Want to Start Screen Recording.")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        checkPermission();
                        dialogInterface.dismiss();
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

    ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
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

    private void dispatchTakePictureIntent() {
//
        Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
        File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), ".temp.jpg");
        mCurrentPhotoPath = file.getAbsolutePath();
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        Log.d("aaaddd", "onClick: " + file);
        intent2.putExtra("output", FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName(), file));
        startActivityForResult(intent2, REQUEST_IMAGE_CAPTURE);

    }

    private void chkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
//            Toast.makeText(getApplicationContext(), "greater than 12", Toast.LENGTH_SHORT).show();
            Dexter.withContext(getActivity())
                    .withPermissions(
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.CAMERA
                    )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                checkPermission = true;
                                sharedPreferences.edit()
                                        .putBoolean(PREF_CHECK_PERMISSION, checkPermission)
                                        .apply();
                            } else {
                                checkPermission = false;
                                sharedPreferences.edit()
                                        .putBoolean(PREF_CHECK_PERMISSION, checkPermission)
                                        .apply();
                                showSettingDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(
                                List<PermissionRequest> list,
                                PermissionToken token
                        ) {
                            token.continuePermissionRequest();
                        }
                    })
                    .withErrorListener(new PermissionRequestErrorListener() {
                        @Override
                        public void onError(DexterError error) {
                            Toast.makeText(
                                    getActivity(),
                                    "Error Occurred while granting permissions",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    })
                    .onSameThread()
                    .check();
        } else {
//            Toast.makeText(getApplicationContext(), "lesser than 12", Toast.LENGTH_SHORT).show();
            Dexter.withContext(getActivity())
                    .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                checkPermission = true;
                                sharedPreferences.edit()
                                        .putBoolean(PREF_CHECK_PERMISSION, checkPermission)
                                        .apply();
                            } else {
                                checkPermission = false;
                                sharedPreferences.edit()
                                        .putBoolean(PREF_CHECK_PERMISSION, checkPermission)
                                        .apply();
                                showSettingDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(
                                List<PermissionRequest> list,
                                PermissionToken token
                        ) {
                            token.continuePermissionRequest();
                        }
                    })
                    .withErrorListener(new PermissionRequestErrorListener() {
                        @Override
                        public void onError(DexterError error) {
                            // Handle error if needed
                        }
                    })
                    .onSameThread()
                    .check();
        }
    }

    private void showSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Need Permissions");
        builder.setMessage("This App Needs permission to use this feature. You can grant this fom app settings");
        builder.setPositiveButton("Goto Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

//    private void requestUserConsent() {
//        ConsentInformation consentInformation = UserMessagingPlatform.getConsentInformation(getActivity());
//        ConsentRequestParameters params = new ConsentRequestParameters.Builder()
//                .setTagForUnderAgeOfConsent(false)
//                .build();
//
//        consentInformation.requestConsentInfoUpdate(
//                getActivity(),
//                params,
//                () -> {
//                    if (consentInformation.isConsentFormAvailable()) {
//                        loadForm();
//                    }
//                },
//                formError -> Log.e("MainActivity", "Consent information update error: " + formError.getMessage())
//        );
//    }

//    private void loadForm() {
//        ConsentInformation consentInformation = UserMessagingPlatform.getConsentInformation(requireActivity());
//        UserMessagingPlatform.loadConsentForm(
//                getActivity(),
//                consentForm -> {
//                    if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {
//                        consentForm.show(getActivity(), formError -> {
//                            if (formError != null) {
//                                // Handle the error.
//                                Log.e("MainActivity", "Consent form dismissed with error: " + formError.getMessage());
//                            } else {
//                                Log.e("MainActivity", "Consent form dismissed with error: " +consentInformation.getConsentStatus());
//
//                                // Handle the dismissal without error.
//                                // You may want to check the user's consent choices here.
//                            }
//                        });
//                    }
//                },
//                formError -> Log.e("MainActivity", "Consent form load error: " + formError.getMessage())
//        );
//
//        consentInformation.reset();
//    }


    private void showAds() {
        Log.d(TAG, "showAds: call");
    }


    private void showProgressDialog() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(requireContext());
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(true);
            progressDialog.show();

            // Set the flag
            isProgressDialogShownBecauseOfAd = true;
        }
    }

    private void hideProgressDialog() {
        if (isProgressDialogShownBecauseOfAd && progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();

            // Reset the flag
            isProgressDialogShownBecauseOfAd = false;
        }
    }


    private void startFragment() {

        NavigationUtils.navigate(requireActivity(), R.id.trimFragment);
    }


    private void checkingUIUpdate() {
        if (BackgroundVideoRecording.serviceIsRunning) {
            recordingStatus = true;
//            binding.startRecording.setImageResource(R.drawable.stop);
//            binding.chronometerHome.setVisibility(View.INVISIBLE);
//            setUiInVisible();
            clickViewHide();
        } else {
            Log.e(TAG, "onViewCreated: " + "service was not running");
        }
    }

    private void notificationTapped() {
        if (isAdded()) {
            Intent intent = requireActivity().getIntent();
            String ac = intent.getAction();
            if (ac != null)
                if (ac.equals("VID_STARTED")) {
                    if (intent.getExtras().getBoolean("notificationTapped")) {
                        if (BackgroundVideoRecording.serviceIsRunning) {
                            recordingStatus = true;
                            clickViewHide();
                        } else {
                            Log.e(TAG, "onViewCreated: " + "service was not running");
                        }
                    }
                } else {
                    Log.e(TAG, "onCreate: " + "notification not tapped ");
                }
        }
    }

    private void infoDialog() {

        MyApp.Companion.enableshouldshowappopenad();
        new AlertDialog.Builder(requireContext())
                .setTitle("Warning!")
                .setMessage("Camera and MicroPhone will be used privately in background.Are you Sure you want to Record Video Privately in background. Until you will Stop it.")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    SharePref.putBoolean("isWarningShow", true);
                    playBackgroundVideoRecorder();
                    dialogInterface.dismiss();
                })
                .setNegativeButton("No", (d, i) -> {
                    d.dismiss();
                })
                .show();
    }

    private void handleRecordingAction() {
        FirebaseEvents.Companion.logAnalytic("DashBoard_Start_Recording_Btn_Click");
        permissionForService();
        // Add any additional actions here
    }

    private void handleVideoRecordingAction() {
        FirebaseEvents.Companion.logAnalytic("DashBoard_Start_Recording_Btn_Click");
        permissionForVideoService();
        // Add any additional actions here
    }

    private void playBackgroundVideoRecorder() {
//        onlyPreView();

        MyApp.Companion.enableshouldshowappopenad();
        long availableSpace = fileUtils.getExternalStorageSpaceAvailable();
        try {
            if (!recordingStatus) {
                if (availableSpace > 600) {
                    int limit = settingPreferences.getInt("videoDurationLimit", 0);
                    setCurrentTime(SystemClock.elapsedRealtime(), true);
                    if (limit != 0) {
                        setTimer(limit);
                    } else {
                        Log.d(TAG, "playCameraPreview: " + "timer started no limit is set");
                    }
                    recordingStatus = true;
                    Constants.IS_RECORDING_ON_BACKGROUND = true;
                    serviceEditor.putBoolean("serviceStatus", true);
                    serviceEditor.apply();
                    binding.startRecording.setImageResource(R.drawable.stop);
                    Intent intent = new Intent(getActivity(), BackgroundVideoRecording.class);
                    intent.putExtra("ACTION_TYPE", "video_recorder");

                    intent.setAction(BackgroundVideoRecording.BROADCAST_START_VIDEO);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (isAdded())
                            requireActivity().startForegroundService(intent);
                        clickViewHide();

//                        setUiInVisible();
                    } else {
                        if (isAdded())
                            requireActivity().startService(intent);
                        clickViewHide();
//                        setUiInVisible();
                        Log.d(TAG, "playBackgroundVideo: " + "service started");
                    }
                } else {
                    if (isAdded())
                        Toast.makeText(requireActivity().getApplicationContext(), "Not enough Space", Toast.LENGTH_SHORT).show();
                }
            } else {
                setCurrentTime(SystemClock.elapsedRealtime(), false);
                setUiVisible();
                recordingStatus = false;
                Constants.IS_RECORDING_ON_BACKGROUND = false;
                BackgroundVideoRecording.StopVideoAndService.stopVideo();
            }

        } catch (NullPointerException e) {
            e.getLocalizedMessage();
        }


    }

    private void playBackgroundVideo() {
//        onlyPreView();
        long availableSpace = fileUtils.getExternalStorageSpaceAvailable();
        try {
            if (!recordingStatus) {
                if (availableSpace > 600) {
                    int limit = settingPreferences.getInt("videoDurationLimit", 0);
                    setCurrentTime(SystemClock.elapsedRealtime(), true);
                    if (limit != 0) {
                        setTimer(limit);
                    } else {
                        Log.d(TAG, "playCameraPreview: " + "timer started no limit is set");
                    }
                    recordingStatus = true;
                    Constants.IS_RECORDING_ON_BACKGROUND = true;
                    serviceEditor.putBoolean("serviceStatus", true);
                    serviceEditor.apply();
                    binding.startRecording.setImageResource(R.drawable.stop);
                    Intent intent = new Intent(getActivity(), BackgroundVideoRecording.class);
                    intent.putExtra("ACTION_TYPE", "bg_recorder");

                    intent.setAction(BackgroundVideoRecording.BROADCAST_START_VIDEO);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (isAdded())
                            requireActivity().startForegroundService(intent);
                        clickViewHide();

//                        setUiInVisible();
                    } else {
                        if (isAdded())
                            requireActivity().startService(intent);
                        clickViewHide();
//                        setUiInVisible();
                        Log.d(TAG, "playBackgroundVideo: " + "service started");
                    }
                } else {
                    if (isAdded())
                        Toast.makeText(requireActivity().getApplicationContext(), "Not enough Space", Toast.LENGTH_SHORT).show();
                }
            } else {
                setCurrentTime(SystemClock.elapsedRealtime(), false);
                setUiVisible();
                recordingStatus = false;
                Constants.IS_RECORDING_ON_BACKGROUND = false;
                BackgroundVideoRecording.StopVideoAndService.stopVideo();
                Toast.makeText(activity, "stopped", Toast.LENGTH_SHORT).show();
            }

        } catch (NullPointerException e) {
            e.getLocalizedMessage();
        }


    }

    private void clickViewHide() {
        if (BackgroundVideoRecording.serviceIsRunning) {
            Long pauseOffset = SharePref.getLong("pauseOffset", 0L);
            Long clock = SharePref.getLong("clock", 0L);
            Long minus = SystemClock.elapsedRealtime() - clock;
            Long sum = minus + pauseOffset;
            binding.chronometerHome.setBase(SystemClock.elapsedRealtime() - sum);
            binding.chronometerHome.start();
            Log.d(TAG, "clickViewHide: Hellooooooooooooo :::: " + SharePref.getLong("pauseOffset", 0L));
        }

        binding.chronometerHome.setVisibility(View.VISIBLE);
        binding.recordTxt.setVisibility(View.INVISIBLE);
        binding.startRecording.setImageResource(R.drawable.stop);
        binding.bottomView.setVisibility(View.INVISIBLE);
//        binding.movaAble.setVisibility(View.VISIBLE);
        binding.bottom2.setVisibility(View.VISIBLE);
//        binding.pressTxt.setText("Press the button to Stop recording");
        binding.pressTxt.setText("Stop recording");
    }


    private void getPermissions() {
        if (!dialogPreference.getBoolean("permissionGranted", false)) {

        } else {
            if (isAdded())
                NavigationUtils.navigate(requireActivity(), R.id.action_homeFragment_to_cameraPreviewFragment);
        }
    }

    private void permission() {
        PermissionX.init(this)
                .permissions(permissions[0], permissions[1])
                .onExplainRequestReason(new ExplainReasonCallback() {
                    @Override
                    public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList) {
                        scope.showRequestReasonDialog(deniedList, "Core Functionality depends on these permissions", "OK", "Cancel");
                    }
                }).request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        onlyPreView();
//                        NavigationUtils.navigate(binding.getRoot(), R.id.action_homeFragment_to_cameraPreviewFragment);
                        //    Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "onResult: " + "not Granted");
                    }
                });
    }

    private void getPermissionForServices() {
        if (!dialogPreference.getBoolean("permissionGranted", false)) {
        } else {
            playBackgroundVideo();
        }
    }


    private void permissionForVideoService() {

        MyApp.Companion.disableshouldshowappopenad();

        PermissionX.init(this)
                .permissions(permissions[0], permissions[1])
                .onExplainRequestReason(new ExplainReasonCallback() {
                    @Override
                    public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList) {
                        scope.showRequestReasonDialog(deniedList, "Core Functionality depends on these permissions", "OK", "Cancel");
                    }
                }).request((allGranted, grantedList, deniedList) -> {
                    Log.d(TAG, "permissionForService:" + allGranted + grantedList.size() + deniedList.size());
                    if (allGranted) {
                        SharePref.putBoolean(isPermissionGranted, true);
                        if (Settings.canDrawOverlays(getContext())) {
                            if (!recordingStatus)
                                if (!SharePref.getBoolean("isWarningShow", false))
                                    infoDialog();
                                else
                                    playBackgroundVideoRecorder();
                            else
                                playBackgroundVideoRecorder();
                        } else {
                            permissionOverLayDialog();
                        }

                        //Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "onResult: " + "not Granted");
                    }
                });
    }


    private void permissionForService() {
        PermissionX.init(this)
                .permissions(permissions[0], permissions[1])
                .onExplainRequestReason(new ExplainReasonCallback() {
                    @Override
                    public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList) {
                        scope.showRequestReasonDialog(deniedList, "Core Functionality depends on these permissions", "OK", "Cancel");
                    }
                }).request((allGranted, grantedList, deniedList) -> {
                    Log.d(TAG, "permissionForService:" + allGranted + grantedList.size() + deniedList.size());
                    if (allGranted) {
                        SharePref.putBoolean(isPermissionGranted, true);
                        if (Settings.canDrawOverlays(getContext())) {
                            if (!recordingStatus)
                                if (!SharePref.getBoolean("isWarningShow", false))
                                    infoDialog();
                                else
                                    playBackgroundVideo();
                            else
                                playBackgroundVideo();
                        } else {
                            permissionOverLayDialog();
                        }

                        //Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "onResult: " + "not Granted");
                    }
                });
    }

    private void permissionOverLayDialog() {

        MyApp.Companion.disableshouldshowappopenad();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        if (inflater == null) {
            return; // Context is not available
        }

        PermissionOverlayDialogBinding bind = PermissionOverlayDialogBinding.inflate(inflater);
        Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(bind.getRoot());

        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.nativeAd.setVisibility(View.INVISIBLE);
        } else if (overlay_permission_admob_native) {
            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadMediationNative(
                        activity,
                        bind.nativeAd,
                        admob_native_id,
                        bind.nativeAdContainer,
                        R.layout.ad_native_layout_home
                );
            }
        } else if (overlay_permission_facebook_native) {
            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadNativeFacebookAd(
                        facebook_native_ad_id,
                        activity,
                        bind.nativeAdContainer
                );
            }
        } else {
            Log.d("checksswitcehs", "onViewCreated: both are off");
            bind.nativeAd.setVisibility(View.GONE);
        }

        bind.deny.setOnClickListener(view -> dialog.dismiss());
        bind.back.setOnClickListener(view -> dialog.dismiss());
        bind.pro.setOnClickListener(view -> {
        });
        bind.allow.setOnClickListener(view -> {
            requestPermission();
            dialog.dismiss();
        });
        bind.permis.setOnClickListener(view -> {
            requestPermission();
            dialog.dismiss();
        });
        dialog.show();
    }

    private Boolean requestPermission() {
        if (!Settings.canDrawOverlays(getContext())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getActivity().getPackageName()));
            startActivityForResult(intent, 999);

            MyApp.Companion.enableshouldshowappopenad();
            return true;

        }
        MyApp.Companion.enableshouldshowappopenad();
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SCREEN_RECORD_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                // Start screen recording
                hbRecorder.startScreenRecording(data, resultCode);
            }
        }

//        if (requestCode == REQUEST_IMAGE_CAPTURE) {
//            if (mCurrentPhotoPath != null) {
//                try {
//                    // Load the image as a Bitmap
//                    File imageFile = new File(mCurrentPhotoPath);
//                    Bitmap originalBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
//
//                    // Rotate the Bitmap if needed
//                    Bitmap rotatedBitmap = rotateBitmap(originalBitmap, mCurrentPhotoPath);
//
//                    // Create a new file to save the rotated image
//                    File rotatedImageFile = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), ".temp_rotated.jpg");
//
//                    // Save the rotated Bitmap to the new file
//                    FileOutputStream outputStream = new FileOutputStream(rotatedImageFile);
//                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                    outputStream.close();
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString("imagePath", rotatedImageFile.getAbsolutePath());
//
//                    // Navigate to the destination fragment and pass the Bundle as an argument
//                    NavigationUtils.navigate(
//                            requireActivity(),
//                            R.id.imageFragment, // Destination fragment ID
//                            bundle
//                    );
//
//                    // Pass the path of the rotated image to the next activity
////                    Intent intentToEditor = new Intent(this, EditImageActivity.class);
////                    intentToEditor.putExtra("cameraBitmapPhoto", rotatedImageFile.getAbsolutePath());
////                    intentToEditor.putExtra("conditionFromActivity", "cameraBitmap");
////                    startActivity(intentToEditor);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Log.e("camera_intentactivity", "onActivityResult: mCurrentPhotoPath is null");
//                Toast.makeText(getActivity(), "Failed to capture image", Toast.LENGTH_SHORT).show();
//            }
//        }


//        if (requestCode == REQUEST_IMAGE_CAPTURE) {
//            if (mCurrentPhotoPath != null) {
//                try {
//                    // Load the image as a Bitmap
//                    File imageFile = new File(mCurrentPhotoPath);
//                    Bitmap originalBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
//
//                    // Rotate the Bitmap if needed
//                    Bitmap rotatedBitmap = rotateBitmap(originalBitmap, mCurrentPhotoPath);
//
//                    // Get the Pictures directory for your app's private use
//                    File picturesDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//
//                    // Create a new file in the Pictures directory to save the rotated image
//                    // The file name could include a timestamp to ensure uniqueness
//                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//                    File rotatedImageFile = new File(picturesDir, "ROTATED_" + timeStamp + ".jpg");
//
//                    // Save the rotated Bitmap to the new file
//                    try (FileOutputStream outputStream = new FileOutputStream(rotatedImageFile)) {
//                        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                    }
//
//                    // Bundle for navigation
//                    Bundle bundle = new Bundle();
//                    bundle.putString("imagePath", rotatedImageFile.getAbsolutePath());
//
//                    // Navigate to the destination fragment and pass the Bundle as an argument
//                    NavigationUtils.navigate(
//                            requireActivity(),
//                            R.id.imageFragment, // Destination fragment ID
//                            bundle
//                    );
//
//                    // Additional code for starting another activity can be added here
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getActivity(), "Failed to process image", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Log.e("camera_intentactivity", "onActivityResult: mCurrentPhotoPath is null");
//                Toast.makeText(getActivity(), "Failed to capture image", Toast.LENGTH_SHORT).show();
//            }
//        }


        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (mCurrentPhotoPath != null) {
                try {
                    // Load the image as a Bitmap
                    File imageFile = new File(mCurrentPhotoPath);
                    Bitmap originalBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);

                    if (originalBitmap != null) { // Check if Bitmap is not null

                        // Rotate the Bitmap if needed
                        Bitmap rotatedBitmap = rotateBitmap(originalBitmap, mCurrentPhotoPath);

                        // Get the Pictures directory for your app's private use
                        File picturesDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                        // Create a new file in the Pictures directory to save the rotated image
                        // The file name could include a timestamp to ensure uniqueness
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                        File rotatedImageFile = new File(picturesDir, "ROTATED_" + timeStamp + ".jpg");

                        // Save the rotated Bitmap to the new file
                        try (FileOutputStream outputStream = new FileOutputStream(rotatedImageFile)) {
                            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        }

                        // Bundle for navigation
                        Bundle bundle = new Bundle();
                        bundle.putString("imagePath", rotatedImageFile.getAbsolutePath());

                        // Navigate to the destination fragment and pass the Bundle as an argument
                        NavigationUtils.navigate(
                                requireActivity(),
                                R.id.imageFragment, // Destination fragment ID
                                bundle
                        );

                        // Additional code for starting another activity can be added here
                    } else {
                        // Handle the case where the originalBitmap is null
                        Log.e("camera_intentactivity", "onActivityResult: originalBitmap is null");
                        Toast.makeText(getActivity(), "Failed to process image", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed to process image", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("camera_intentactivity", "onActivityResult: mCurrentPhotoPath is null");
                Toast.makeText(getActivity(), "Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }


        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                // Now, you have the captured image as a Bitmap (imageBitmap)

                // Pass the image to the next fragment
                Bundle bundle = new Bundle();
                bundle.putParcelable("image", imageBitmap);

                NavigationUtils.navigate(requireActivity(), R.id.imageFragment, bundle);

                ImageFragment nextFragment = new ImageFragment();
                nextFragment.setArguments(bundle);

                // Replace or add the fragment to your layout
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainerViewHome, nextFragment)
                        .commit();
            }
        }

        if (requestCode == 999) {

            MyApp.Companion.disableshouldshowappopenad();
            if (Settings.canDrawOverlays(getContext())) {
                if (!recordingStatus) {
                    if (!SharePref.getBoolean("isWarningShow", false))
                        infoDialog();
                    else
                        playBackgroundVideo();
                } else {
                    playBackgroundVideo();
                }
            } else {
                Toast.makeText(getContext(), "Please Allow Permission To Use Functionality", Toast.LENGTH_SHORT).show();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap rotateBitmap(Bitmap bitmap, String photoPath) {
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(270);
                    break;
                default:
                    return bitmap;
            }

            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
            return bitmap;
        }
    }


    private long[] setCurrentTime(long baseTime, boolean start) {
        if (start) {
            final long[] current = new long[1];
            binding.chronometerHome.setBase(baseTime);
            binding.chronometerHome.setOnChronometerTickListener(chronometer -> {

                current[0] = SystemClock.elapsedRealtime() - chronometer.getBase();
                Log.e(TAG, "onChronometerTick: " + current[0]);
                serviceEditor.putLong("time", current[0]);
            });
            binding.chronometerHome.start();
            return current;
        } else {
            final long[] stop = new long[1];
            binding.chronometerHome.stop();
            binding.chronometerHome.setBase(SystemClock.elapsedRealtime());
            return stop;
        }
    }


    private void setUiVisible() {
        binding.chronometerHome.setText("00:00");
        binding.startRecording.setImageResource(R.drawable.home_spy_recorder_icon);
        binding.bottom2.setVisibility(View.INVISIBLE);
        binding.bottomView.setVisibility(View.VISIBLE);
//        binding.movaAble.setVisibility(View.GONE);
//        binding.pressTxt.setText("Press the button to start recording");
        binding.pressTxt.setText("Start recording");
        binding.chronometerHome.setVisibility(View.INVISIBLE);
        binding.recordTxt.setVisibility(View.INVISIBLE);
        navigateToFiles();
    }


    private void setTimer(int durationMin) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            binding.startRecording.setImageResource(R.drawable.home_spy_recorder_icon);
            setUiVisible();
            recordingStatus = false;
            BackgroundVideoRecording.StopVideoAndService.stopVideo();
            Toast.makeText(activity, "stopped", Toast.LENGTH_SHORT).show();
            serviceEditor.clear();
            serviceEditor.apply();
        }, (long) durationMin * 60 * 1000);

    }


    private void navigateToFiles() {
        if (isAdded()) {

            if (home_admob_home_inter) {
                InterstitialClass.requestInterstitial(getActivity(), admob_interstitial_home_id, new ActionOnAdClosedListener() {
                    @Override
                    public void ActionAfterAd() {

                        NavigationUtils.navigate(getActivity(), R.id.filesFragment);
                    }
                });

            } else {
                NavigationUtils.navigate(getActivity(), R.id.filesFragment);

            }
        }

    }

    private void saveDialog(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SaveDialogBinding bind = SaveDialogBinding.inflate(inflater);
        Dialog dialog = new Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(bind.getRoot());
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 2000);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Navigation.findNavController((Activity) context, R.id.fragmentContainerViewHome).navigate(R.id.filesFragment);

            }
        });
        dialog.show();
    }

    private void onlyPreView() {
        if (isAdded()) {
            ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture = ProcessCameraProvider.getInstance(requireContext());// getting camera x instance
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cameraProviderListenableFuture.addListener(() -> {
                    try {
                        ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                        cameraProvider.unbindAll();
                        Preview preview = new Preview.Builder().build(); // binding our view with camera
                        preview.setSurfaceProvider(binding.movaAble.getSurfaceProvider());
                        cameraProvider.bindToLifecycle(getViewLifecycleOwner(), toggleCamera(), preview);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, requireContext().getMainExecutor());
            } else {

                cameraProviderListenableFuture.addListener(() -> {
                    try {
                        ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                        cameraProvider.unbindAll();
                        Preview preview = new Preview.Builder().build(); // binding our view with camera
                        preview.setSurfaceProvider(binding.movaAble.getSurfaceProvider());
                        cameraProvider.bindToLifecycle(getViewLifecycleOwner(), toggleCamera(), preview);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, ContextCompat.getMainExecutor(requireContext()));

                Log.e(TAG, "initCamera: " + "idr issue h");
            }
        }
    }

    public CameraSelector toggleCamera() {
        if (settingPreferences.getBoolean("frontCamera", false)) {
            return CameraSelector.DEFAULT_FRONT_CAMERA;
        } else {
            return CameraSelector.DEFAULT_BACK_CAMERA;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        if (BackgroundVideoRecording.serviceIsRunning && binding != null) {
            SharePref.putLong("pauseOffset", SystemClock.elapsedRealtime() - binding.chronometerHome.getBase());
            SharePref.putLong("clock", SystemClock.elapsedRealtime());

        }
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void HBRecorderOnPause() {

    }

    @Override
    public void HBRecorderOnResume() {

    }

    @Override
    public void HBRecorderOnStart() {
        Log.d("HBRecorderewrewrewr", "Recording started");
    }

    @Override
    public void HBRecorderOnComplete() {
        Log.d("HBRecorderewrewrewr", "Recording complete");
    }

    @Override
    public void HBRecorderOnError(int errorCode, String reason) {
        Log.e("HBRecorderewrewrewr", "Recording error " + errorCode + ": " + reason);
    }

}

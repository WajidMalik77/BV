package com.background.video.recorder.camera.recorder.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.background.video.recorder.camera.recorder.BuildConfig;
import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.databinding.LayoutSettingsBinding;
import com.background.video.recorder.camera.recorder.ui.activitiy.PasswordActivity;
import com.background.video.recorder.camera.recorder.util.constant.Constants;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;
import com.background.video.recorder.camera.recorder.viewmodel.MediaFilesViewModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;

public class SettingsFragment extends Fragment {
    public static final int[] progress = new int[1];
    private static final String TAG = "SettingsFragment";
    public static boolean cameraToggle = false;
    public static boolean front;
    public static int videoQuality;
    private LayoutSettingsBinding settingBinding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private MediaFilesViewModel mediaFilesViewModel;
    private int quality = 0; // 1 for 2160 p 2 for 1080 p 3 for 720p 4 for 480 p 0 for defualt

    public SettingsFragment() {

    }
    boolean native_bg;
    SharedPreferences sharedPrefs;
    boolean homeInter, splashInter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isAdded()) {
            mediaFilesViewModel = new ViewModelProvider(
                    this,
                    (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())
            ).get(MediaFilesViewModel.class);
            sharedPreferences = requireActivity().getSharedPreferences("stateSavePreference", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
//        if (Constants.APP_LOCKED) {
//            if (getArguments() != null) {
//                pass = Integer.parseInt(getArguments().getString("pass"));
//                question = getArguments().getString("question");
//                answer = getArguments().getString("answer");
//            } else {
//                Log.d(TAG, "onCreate: " + "null arguments");
//            }
//            mediaFilesViewModel.insertUser(new Authentication(pass, answer, question));
//            Constants.APP_LOCKED = false;
//        } else {
//            Log.d(TAG, "onCreate: already locked enabled ");
//        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        settingBinding = LayoutSettingsBinding.inflate(inflater, container, false);

        sharedPrefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        native_bg   = sharedPrefs.getBoolean("native_bg", false);
        homeInter = sharedPrefs.getBoolean("home_inter", false);

        if (native_bg) {
//            InterstitialAdUtils.INSTANCE.loadMediationNative(getActivity(), settingBinding.nativeAd);//bs rehny dook hy

        }else {
            settingBinding.nativeAd.setVisibility(View.INVISIBLE);
        }


        return settingBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents();
    }

    private void initComponents() {
//        settingBinding.llAdViewPlaceholder.setVisibility(View.VISIBLE);

        loadSettingsFromPreferences();
        setListeners();
        //  loadSettingsFromPreferences();

    }


    private void setListeners() {
        settingBinding.ibBack.setOnClickListener(view -> NavigationUtils.navigateBack(settingBinding.getRoot()));

        settingBinding.ivHome.setOnClickListener(view -> {
            startActivityNext(Navigation.findNavController(getView()), R.id.action_settingsFragment_to_homeFragment);
//            NavigationUtils.navigateBack(settingBinding.getRoot());
        });


        settingBinding.lytVideoCamera.setOnClickListener(view -> {

            settingBinding.lytFeedback.setClickable(false);
            settingBinding.lytVideoQuality.setClickable(false);
            settingBinding.lytRecordingDuration.setClickable(false);
            setLytForCameraVisible();

        });

        ColorStateList colorStateList = new ColorStateList(
                new int[][]
                        {
                                new int[]{-android.R.attr.state_checked}, // Disabled
                                new int[]{android.R.attr.state_checked}   // Enabled
                        },
                new int[]
                        {
                                Color.WHITE, // disabled
                                Color.GREEN   // enabled
                        }
        );

        settingBinding.rbFrontCamera.setButtonTintList(colorStateList);
        settingBinding.rbBackCamera.setButtonTintList(colorStateList);
        settingBinding.rb480.setButtonTintList(colorStateList);
        settingBinding.rb720.setButtonTintList(colorStateList);
        settingBinding.rb1080.setButtonTintList(colorStateList);
        settingBinding.rb2160.setButtonTintList(colorStateList);

        settingBinding.rbFrontCamera.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                cameraToggle = b;
                editor.clear();
                setLytForCameraInVisible();
                settingBinding.tvVideoCameraValue.setText("Front Camera");
                settingBinding.tvVideoCameraValue2.setText("Front Camera");
                editor.putBoolean("frontCamera", b);
                settingBinding.lytFeedback.setClickable(true);
                settingBinding.lytVideoQuality.setClickable(true);
                settingBinding.lytRecordingDuration.setClickable(true);
                editor.apply();
            } else {
                Log.e(TAG, "setListeners: " + " front cam is false");
            }

        });
        settingBinding.rbBackCamera.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                cameraToggle = false;
                editor.clear();
                setLytForCameraInVisible();
                settingBinding.tvVideoCameraValue.setText("Back Camera");
                editor.putBoolean("backCamera", b);
                settingBinding.lytFeedback.setClickable(true);
                settingBinding.lytVideoQuality.setClickable(true);
                settingBinding.lytRecordingDuration.setClickable(true);
                editor.apply();
            } else {
                Log.e(TAG, "setListeners: " + "rbbackcamera is false");
            }

        });
        settingBinding.lytRecordingDuration.setOnClickListener(view -> {
            setBgPrimary(settingBinding.lytRecordingDuration);
            settingBinding.lytFeedback.setClickable(false);
            settingBinding.lytVideoQuality.setClickable(false);
            settingBinding.lytVideoCamera.setClickable(false);
            settingBinding.lytRecordingDurationInnerLyt.setVisibility(View.VISIBLE);
            settingBinding.tvRecodingDurationInvisible.setVisibility(View.VISIBLE);
            settingBinding.tvTimeDurationInvisible.setVisibility(View.VISIBLE);
            settingBinding.tvTimeDuration.setVisibility(View.INVISIBLE);
            settingBinding.tvRecodingDuration.setVisibility(View.INVISIBLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                settingBinding.sbRecordingInMinutes.setMin(0);
            }
            settingBinding.sbRecordingInMinutes.setMax(10);


            settingBinding.sbRecordingInMinutes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                    int durationInMin = seekBar.getProgress();

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    int durationInMin = seekBar.getProgress();

                    Log.e(TAG, "onStopTrackingTouch: " + durationInMin);

                    editor.putInt("videoDurationLimit", durationInMin);
                    editor.apply();

                    progress[0] = seekBar.getProgress();

                    if (durationInMin == 0) {
                        settingBinding.tvRecordingLimitValue.setText("Unlimited");
                    } else {
                        settingBinding.tvRecordingLimitValue.setText(durationInMin + " min");
                    }
                }
            });
            settingBinding.btnSelectLimit.setOnClickListener(view1 -> {
                settingBinding.lytRecordingDurationInnerLyt.setVisibility(View.INVISIBLE);
                String duration = progress[0] == 0 ? "Unlimited" : progress[0] + " Min";
                settingBinding.tvRecodingDurationInvisible.setVisibility(View.INVISIBLE);
                settingBinding.tvTimeDurationInvisible.setVisibility(View.INVISIBLE);
                settingBinding.tvTimeDuration.setVisibility(View.VISIBLE);
                settingBinding.tvRecodingDuration.setVisibility(View.VISIBLE);
                settingBinding.tvTimeDuration.setText(duration);
                settingBinding.lytFeedback.setClickable(true);
                settingBinding.lytVideoQuality.setClickable(true);
                settingBinding.lytVideoCamera.setClickable(true);
                setBgWhite(settingBinding.lytRecordingDuration);
            });

        });
        settingBinding.lytVideoQuality.setOnClickListener(view -> {
            settingBinding.lytFeedback.setClickable(false);
            settingBinding.lytRecordingDuration.setClickable(false);
            settingBinding.lytVideoCamera.setClickable(false);
            setLayoutForVideoQuality();
        });
        settingBinding.lytFeedback.setOnClickListener(view -> {
            try {
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(Constants.RATE_US_BASE_URL + BuildConfig.APPLICATION_ID));
                startActivity(viewIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        settingBinding.switchAppLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                Log.e(TAG, "onCheckedChanged: " + mediaFilesViewModel.getRowCount());

                if (!sharedPreferences.getBoolean("appLocked", false) && isAdded()) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("fromSettings", true);
                    Intent intent = new Intent(requireActivity(), PasswordActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {

                    editor.remove("appLocked");
                    editor.apply();
                    mediaFilesViewModel.deleteAllTable();
//                    Bundle bundle = new Bundle();
//                    bundle.putBoolean("fromSettings", true);
//                    NavigationUtils.navigate(settingBinding.getRoot(), R.id.action_settingsFragment_to_verifyUserFragment2);
                }
            }
        });
        settingBinding.cbSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!sharedPreferences.getBoolean("noSound", false)) {
                    editor.putBoolean("noSound", true);
                    editor.apply();
                } else {
                    editor.remove("noSound");
                    editor.apply();
                }
            }
        });
//        settingBinding.touchOutsideView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (settingBinding.lytRecordingDurationInnerLyt.getVisibility() == View.VISIBLE) {
//                    settingBinding.lytFeedback.setClickable(true);
//                    settingBinding.lytRecordingDuration.setClickable(true);
//                    settingBinding.lytVideoCamera.setClickable(true);
//                    settingBinding.lytVideoQuality.setClickable(true);
//                    settingBinding.lytRecordingDurationInnerLyt.setVisibility(View.GONE);
//                    settingBinding.touchOutsideView.setVisibility(View.GONE);
//                    settingBinding.tvRecodingDurationInvisible.setVisibility(View.INVISIBLE);
//                    settingBinding.tvTimeDurationInvisible.setVisibility(View.INVISIBLE);
//                    settingBinding.tvTimeDuration.setVisibility(View.VISIBLE);
//                    settingBinding.tvRecodingDuration.setVisibility(View.VISIBLE);
//
//                } else if (settingBinding.lytQuality.getVisibility() == View.VISIBLE) {
//                    settingBinding.lytFeedback.setClickable(true);
//                    settingBinding.lytRecordingDuration.setClickable(true);
//                    settingBinding.lytVideoCamera.setClickable(true);
//                    settingBinding.lytVideoQuality.setClickable(true);
//                    settingBinding.lytQuality.setVisibility(View.GONE);
//                    setBgWhite(settingBinding.lytVideoQuality);
//                    settingBinding.touchOutsideView.setVisibility(View.GONE);
//
//                } else if (settingBinding.lytCameraToggle.getVisibility() == View.VISIBLE) {
//                    settingBinding.lytFeedback.setClickable(true);
//                    settingBinding.lytRecordingDuration.setClickable(true);
//                    settingBinding.lytVideoCamera.setClickable(true);
//                    settingBinding.lytVideoQuality.setClickable(true);
//                    settingBinding.lytCameraToggle.setVisibility(View.GONE);
//                    settingBinding.touchOutsideView.setVisibility(View.GONE);
//                    setLytForCameraVisible();
//
//                }
//            }
//        });
//        settingBinding.switchAppLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                editor.putBoolean("lock", b);
//                editor.apply();
//
//                if (b) {
//
//                } else {
//
//                    Log.e(TAG, "onCheckedChanged: " + "switch not enabled ");
//                }
//            }
//        });

    }

    public void startActivityNext(NavController navController,
                                  int navDirections) {
        if (InterstitialAdUtils.INSTANCE.getAdMobHomeInterstitialAd() != null) {
            InterstitialAdUtils.INSTANCE.getAdMobHomeInterstitialAd().setFullScreenContentCallback(new FullScreenContentCallback() {
//                @Override
//                public void onAdDismissedFullScreenContent() {
//                    super.onAdDismissedFullScreenContent();
//
////                    AdsHelper_new.isShowingInterstitial = false;
////                    AdsHelper_new.isInterstitialShowing = false;
//                    // Toast.makeText(activity, "Admob home dismiss", Toast.LENGTH_SHORT).show();
//                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    InterstitialAdUtils.INSTANCE.setAdMobHomeInterstitialAd(null);
//                    AdsHelper_new.isInterstitialShowing = false;
//                    startFragment();
                    navController.navigate(navDirections);
                    // AdsHelper.loadInterstitialAd(requireActivity());
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
//                    startFragment();
                    navController.navigate(navDirections);
                    InterstitialAdUtils.INSTANCE.setAdMobHomeInterstitialAd(null);
                    if (homeInter) {
//                    InterstitialAdUtils.INSTANCE.loadHomeInterstitial(getActivity());
                    } else {
//                            Toast.makeText(getActivity(), "hoem is false", Toast.LENGTH_SHORT).show();
                    }
//                    AdsHelper_new.isInterstitialShowing = true;
                    // AdsHelper.loadInterstitialAd(requireActivity());
                }
            });

            InterstitialAdUtils.INSTANCE.getAdMobHomeInterstitialAd().show(requireActivity());
//            AdsHelper_new.isShowingInterstitial = true;
        } else {
            navController.navigate(navDirections);
//            startFragment();
        }
    }


    private void setLytForCameraVisible() {
        settingBinding.tvVideoCamera.setVisibility(View.INVISIBLE);
        settingBinding.tvVideoCameraValue.setVisibility(View.INVISIBLE);
        settingBinding.tvVideoCamera2.setVisibility(View.VISIBLE);
        settingBinding.tvVideoCameraValue2.setVisibility(View.VISIBLE);
        settingBinding.lytCameraToggle.setVisibility(View.VISIBLE);
//        settingBinding.lytVideoCamera.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_primary));
        setBgPrimary(settingBinding.lytVideoCamera);
    }

    private void setLytForCameraInVisible() {
        settingBinding.tvVideoCamera.setVisibility(View.VISIBLE);
        settingBinding.tvVideoCameraValue.setVisibility(View.VISIBLE);
        settingBinding.tvVideoCamera2.setVisibility(View.INVISIBLE);
        settingBinding.tvVideoCameraValue2.setVisibility(View.INVISIBLE);
        settingBinding.lytCameraToggle.setVisibility(View.INVISIBLE);
//        settingBinding.lytVideoCamera.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        setBgWhite(settingBinding.lytVideoCamera);
    }

    private void setBgPrimary(View view) {
        if (isAdded())
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_primary));
    }

    private void setBgWhite(View view) {
        if (isAdded())
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white));
    }

    private void setLayoutForVideoQuality() {
        settingBinding.lytQuality.setVisibility(View.VISIBLE);
        setBgPrimary(settingBinding.lytVideoQuality);
        settingBinding.rb2160.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                quality = 1;
                settingBinding.lytFeedback.setClickable(true);
                settingBinding.lytRecordingDuration.setClickable(true);
                settingBinding.lytVideoCamera.setClickable(true);
                settingBinding.lytQuality.setVisibility(View.INVISIBLE);
                settingBinding.tvAppLockDescription.setText("2160p");
                Log.e(TAG, "setLayoutForVideoQuality: quality =" + quality);
                editor.putInt("quality", quality);
                editor.apply();
                setBgWhite(settingBinding.lytVideoQuality);
            } else {
                Log.e(TAG, "setLayoutForVideoQuality: " + "2160 not checked");
            }
        });
        settingBinding.rb1080.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                quality = 2;
                settingBinding.lytFeedback.setClickable(true);
                settingBinding.lytRecordingDuration.setClickable(true);
                settingBinding.lytVideoCamera.setClickable(true);
                settingBinding.lytQuality.setVisibility(View.INVISIBLE);
                settingBinding.tvAppLockDescription.setText("1080p");
                Log.e(TAG, "setLayoutForVideoQuality: quality =" + quality);
                editor.putInt("quality", quality);
                editor.apply();
                setBgWhite(settingBinding.lytVideoQuality);
            } else {
                Log.e(TAG, "setLayoutForVideoQuality: " + "1080 not checked");
            }
        });
        settingBinding.rb720.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                quality = 3;
                settingBinding.lytFeedback.setClickable(true);
                settingBinding.lytRecordingDuration.setClickable(true);
                settingBinding.lytVideoCamera.setClickable(true);
                settingBinding.lytQuality.setVisibility(View.INVISIBLE);
                settingBinding.tvAppLockDescription.setText("720p");
                Log.e(TAG, "setLayoutForVideoQuality: quality =" + quality);
                editor.putInt("quality", quality);
                editor.apply();
                setBgWhite(settingBinding.lytVideoQuality);
            } else {
                Log.e(TAG, "setLayoutForVideoQuality: " + "720 not checked");
            }
        });
        settingBinding.rb480.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                quality = 4;
                settingBinding.lytFeedback.setClickable(true);
                settingBinding.lytRecordingDuration.setClickable(true);
                settingBinding.lytVideoCamera.setClickable(true);
                settingBinding.lytQuality.setVisibility(View.INVISIBLE);
                settingBinding.tvAppLockDescription.setText("480p");
                Log.e(TAG, "setLayoutForVideoQuality: quality =" + quality);
                editor.putInt("quality", quality);
                editor.apply();
                setBgWhite(settingBinding.lytVideoQuality);
            } else {
                Log.e(TAG, "setLayoutForVideoQuality: " + "480 not checked");
            }
        });


    }

    private void loadSettingsFromPreferences() {
//        boolean locked = sharedPreferences.getBoolean("lock", true);
//        Log.e(TAG, "onResume: locked" + locked);
//        if (locked) {
//            settingBinding.switchAppLock.setChecked(true);
//        } else {
//            settingBinding.switchAppLock.setChecked(false);
//        }
        // camera rotation setting
        front = sharedPreferences.getBoolean("frontCamera", false);
        boolean back = sharedPreferences.getBoolean("backCamera", false);
        Log.e(TAG, "onResume: " + front + " ");
        if (front) {
            settingBinding.tvVideoCameraValue.setText("Front Camera");
        } else if (back) {
            settingBinding.tvVideoCameraValue.setText("Back Camera");
        }
        // video limit setting
        int seekProgress = sharedPreferences.getInt("videoDurationLimit", 0);
        if (seekProgress == 0) {
            settingBinding.tvTimeDuration.setText("Unlimited");
            settingBinding.tvRecordingLimitValue.setText("Unlimited");
        } else {
            settingBinding.tvTimeDuration.setText("" + seekProgress + "Min");
            settingBinding.tvRecordingLimitValue.setText("" + seekProgress + "Min");
        }

        settingBinding.sbRecordingInMinutes.setProgress(seekProgress);
        //video quality setting
        videoQuality = sharedPreferences.getInt("quality", 0);
        if (videoQuality == 0) {
            settingBinding.tvAppLockDescription.setText("Select video quality");
        } else if (videoQuality == 1) {
            settingBinding.tvAppLockDescription.setText("2160p");
        } else if (videoQuality == 2) {
            settingBinding.tvAppLockDescription.setText("1080p");
        } else if (videoQuality == 3) {
            settingBinding.tvAppLockDescription.setText("720p");
        } else if (videoQuality == 4) {
            settingBinding.tvAppLockDescription.setText("480p");
        }
        boolean appLocked = sharedPreferences.getBoolean("appLocked", false);
        if (appLocked) {
            settingBinding.switchAppLock.setChecked(true);
        }
        boolean noSound = sharedPreferences.getBoolean("noSound", false);
        if (noSound) {
            settingBinding.cbSound.setChecked(true);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        //loadSettingsFromPreferences();
    }

}


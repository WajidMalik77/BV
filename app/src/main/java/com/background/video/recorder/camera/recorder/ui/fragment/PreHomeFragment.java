package com.background.video.recorder.camera.recorder.ui.fragment;

import static com.background.video.recorder.camera.recorder.util.constant.Constants.isPermissionGranted;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.databinding.FragmentPreHomeBinding;
import com.background.video.recorder.camera.recorder.databinding.LayoutHomeFragmentNewBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.util.FirebaseEvents;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.background.video.recorder.camera.recorder.util.file.FileUtils;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;
import com.background.video.recorder.camera.recorder.viewmodel.MediaFilesViewModel;
import com.background.video.recorder.camera.recorder.viewmodel.SharedViewModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.hbisoft.hbrecorder.HBRecorder;


public class PreHomeFragment extends Fragment {
    private NavController navController;
    private static final String TAG = "preHomeFragment123";
    private static final int SCREEN_RECORD_REQUEST_CODE = 77;
    private static final int PERMISSION_REQUEST_CODE = 99;
    private final String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    //    public LayoutHomeFragmentBinding binding;
    public FragmentPreHomeBinding binding;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        binding = FragmentPreHomeBinding.inflate(inflater, container, false);
        activity = getActivity();
        sharedPrefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        homeInter = sharedPrefs.getBoolean("home_inter", false);
        prefs = SharedPrefsHelper.getInstance(getActivity());

        navController = Navigation.findNavController(getActivity(), R.id.fragmentContainerViewHome);
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {
            pre_home_admob_home_inter = localPrefs.getpre_home_admob_home_interSwitch();
//            pre_home_admob_home_inter = true;
            pre_home_facebook_home_inter = localPrefs.getpre_home_facebook_home_interSwitch();
            pre_home_facebook_SR_inter = localPrefs.getpre_home_facebook_SR_interSwitch();
            pre_home_admob_SR_inter = localPrefs.getpre_home_admob_SR_interSwitch();
            pre_home_facebook_collage_inter = localPrefs.getpre_home_facebook_collage_interSwitch();
            pre_home_admob_collage_inter = localPrefs.getpre_home_admob_collage_interSwitch();
            pre_home_admob_native = localPrefs.getpre_home_admob_nativeSwitch();
//            pre_home_admob_native = true;
            pre_home_facebook_native = localPrefs.getpre_home_facebook_nativeSwitch();
            admob_native_id = localPrefs.getadmob_native_idId();
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId();
            admob_interstitial_home_id = localPrefs.getadmob_interstitial_home_idId();


            Log.d(TAG, "onCreate:  admob_interstitial_home_id  " + admob_interstitial_home_id);
        }



        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_pre_home, container, false);
    }

    private SharedPrefsHelper prefs = null;
    private boolean pre_home_admob_home_inter = true;
    private boolean pre_home_facebook_home_inter = true;
    private boolean pre_home_facebook_SR_inter = true;
    private boolean pre_home_admob_SR_inter = true;
    private boolean pre_home_facebook_collage_inter = true;
    private boolean pre_home_admob_collage_inter = true;
    private boolean pre_home_admob_native = false;
    private boolean pre_home_facebook_native = true;
    private String admob_native_id = "";
    private String facebook_native_ad_id = "";
    private String admob_interstitial_home_id = "";
    Activity activity;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

//        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
//            Log.d(TAG, "onViewCreated: inapp is true so fdont laod add ");
//            return;
//        } else
            if (pre_home_admob_home_inter) {
            Log.d(TAG, "in home intermethod ");
//            InterstitialAdUtils.INSTANCE.loadHomeInterstitial(getActivity(), admob_interstitial_home_id);
        }


        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            Log.d("tagaddzy", "InApp splash true: ");
            binding.nativeAdPreHome.setVisibility(View.INVISIBLE);
        }
        else if (pre_home_admob_native) {
            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadMediationNative(
                        activity,
                        binding.nativeAdPreHome,
                        admob_native_id,
                        binding.nativeAdContainer,
                        R.layout.ad_native_layout_modified
                );
            }
        } else if (pre_home_facebook_native) {
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
            binding.nativeAdPreHome.setVisibility(View.INVISIBLE);
        }


//        native_bg = sharedPrefs.getBoolean("native_bg", false);
//        if (native_bg) {
//            InterstitialAdUtils.INSTANCE.loadMediationNative(getActivity(), binding.nativeAd);
//
//        }else {
//            binding.nativeAd.setVisibility(View.INVISIBLE);
//        }

        binding.collagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, "onViewCreated: button collage clicked");
//                FirebaseEvents.Companion.logAnalytic("pre_home_collage_btn");
//



                if (!SharePref.getBoolean(isPermissionGranted, false)) {
                    Log.d(TAG, "onClick: permission screne ");
                    // Permission not granted, navigate to Permissions fragment
                    Permissions permissionsFragment = new Permissions();
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(permissionsFragment, "permissionsFragment");
                    transaction.commit();
                } else {
                    if (SharePref.getBoolean(AdsKeys.InApp, false)) {
                        Log.d(TAG, "onViewCreated:INAPP CLICKEd");
//                        NavigationUtils.navigate(requireActivity(), R.id.action_preHomeFragment_to_photoEditorCollageMakerFragment);
                    }  else if (activity != null) {
//                        InterstitialAdUtils.INSTANCE.showInterstitialAdHome(activity, "interHomeID", R.id.action_preHomeFragment_to_photoEditorCollageMakerFragment,navController);
                    }

                }

            }
        });

        binding.homeDashboardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: homedasdhboardbtn");
                FirebaseEvents.Companion.logAnalytic("pre_home_dashboard");
                // Check if permission is granted
                if (!SharePref.getBoolean(isPermissionGranted, false)) {
                    Log.d(TAG, "onClick: permission screne ");
                    // Permission not granted, navigate to Permissions fragment
                    Permissions permissionsFragment = new Permissions();
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(permissionsFragment, "permissionsFragment");
                    transaction.commit();
                } else {
                    if (SharePref.getBoolean(AdsKeys.InApp, false)) {

//                        NavigationUtils.navigate(requireActivity(), R.id.action_preHomeFragment_to_homeFragment);
                    }  else
                    if (activity != null) {

                        Log.d(TAG, "onClick: will show ad");
//                        InterstitialAdUtils.INSTANCE.showInterstitialAdHome(activity,
//                                "interHomeID", R.id.action_preHomeFragment_to_homeFragment,navController);
                    }

                }
            }
        });


        binding.screenRecorderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseEvents.Companion.logAnalytic("pre_home_Screen_recording");
                sharedViewModel.triggerRecording();


                if (!SharePref.getBoolean(isPermissionGranted, false)) {
                    Log.d(TAG, "onClick: permission screne ");
                    // Permission not granted, navigate to Permissions fragment
                    Permissions permissionsFragment = new Permissions();
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(permissionsFragment, "permissionsFragment");
                    transaction.commit();
                } else {
                    if (SharePref.getBoolean(AdsKeys.InApp, false)) {

//                        NavigationUtils.navigate(requireActivity(), R.id.action_preHomeFragment_to_screenRecordingFragment2);
                    }  else

                    if (activity != null) {
//                        InterstitialAdUtils.INSTANCE.showInterstitialAdHome(activity, "interHomeID", R.id.action_preHomeFragment_to_screenRecordingFragment2,navController);
                    }

                }

//                startActivityNext(Navigation.findNavController(getView()), R.id.action_preHomeFragment_to_homeFragment);



            }
        });
    }

    public void handleBackPress() {

//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else {

//        Navigation.findNavController(getActivity(), R.id.action_preHomeFragment_to_exitFragment);


//            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, new ExitFragment());
//            transaction.addToBackStack(null);
//            transaction.commit();
    }



}
package com.background.video.recorder.camera.recorder.ui.practice;

import static com.background.video.recorder.camera.recorder.ui.practice.VideoAdapter.playDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.databinding.FragmentScreenRecordingBinding;
import com.background.video.recorder.camera.recorder.databinding.VideoPreviewDialogBinding;
import com.background.video.recorder.camera.recorder.databinding.VideoPreviewDialogScreenrecordingBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ScreenRecordingFragment extends Fragment {
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private List<File> videoFiles = new ArrayList<>();

    FragmentScreenRecordingBinding binding;
    private String admob_native_id = "";
    private String facebook_native_ad_id = "";
    private boolean screenrecordinggalleryadmob_native = true;
    private boolean screenrecordinggalleryfacebook_native = true;
    private SharedPrefsHelper prefs = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentScreenRecordingBinding.inflate(inflater, container, false);


        prefs = SharedPrefsHelper.getInstance(getActivity());
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {
            screenrecordinggalleryadmob_native = localPrefs.getscreenrecordinggalleryadmob_nativeSwitch();
//           screenrecordinggalleryadmob_native = true;
            screenrecordinggalleryfacebook_native = localPrefs.getscreenrecordinggalleryfacebook_nativeSwitch();
            admob_native_id = localPrefs.getadmob_native_idId();
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId();


        }


        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.nativeAd.setVisibility(View.GONE);
        }  else if (screenrecordinggalleryadmob_native) {
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
        } else if (screenrecordinggalleryfacebook_native) {
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
        Log.d("hoem_frag", "onCreateView: Recordings fargment");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        loadVideos();
    }
//    private void loadVideos() {
//        File directory = new File(getActivity().getExternalFilesDir(null), "Screen_Recording");
//        File[] files = directory.listFiles();
//        if (files != null) {
//            videoFiles = Arrays.asList(files);
//            videoAdapter = new VideoAdapter(getActivity(),videoFiles);
//            recyclerView.setAdapter(videoAdapter);
//        }
//
//        videoAdapter = new VideoAdapter(getActivity(), videoFiles);
//        videoAdapter.setOnVideoClickListener(videoUri -> {
//            Bundle bundle = new Bundle();
//            bundle.putString("videoUri", videoUri.toString());
//            Navigation.findNavController(getView()).navigate(R.id.action_screenRecordingFragment_to_videoViewFragment, bundle);
//        });
//
//        recyclerView.setAdapter(videoAdapter);
//
//    }


    private void loadVideos() {
        File directory = new File(getActivity().getExternalFilesDir(null), "Screen_Recording");
        File[] files = directory.listFiles();

        Log.d("screeni", "loadVideos: " + files.length);
        if (files != null) {
            videoFiles = Arrays.asList(files);
            videoAdapter = new VideoAdapter(getActivity(), videoFiles);


            videoAdapter.setOnVideoClickListener(videoUri -> {
                Log.d("screeni", "loadVideos: Clicked");

                Log.d("screeni", "loadVideos: Clicked");
                Bundle bundle = new Bundle();
                bundle.putString("videoUri", videoUri.toString());


                playDialogNew(getActivity(), videoUri.getPath());
//                VideoViewFragment videoViewFragment = new VideoViewFragment();
//                videoViewFragment.setArguments(bundle);
//
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragmentContainerViewHome, videoViewFragment)
//                        .addToBackStack(null) // if you want to add this transaction to the back stack
//                        .commit();


            });


            recyclerView.setAdapter(videoAdapter);
        }
    }
    public void playDialogNew(Context context, String path) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        VideoPreviewDialogScreenrecordingBinding bind = VideoPreviewDialogScreenrecordingBinding.inflate(inflater);

        Dialog dialog = new Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(bind.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.setOnShowListener(dialogInterface -> {
            // Replace with your ad logic if needed
            Log.d("home_frag", "onCreateView: Trim fragment");
            // Example: InterstitialAdUtils.loadMediationNative((Activity) context, bind.nativeAd);

            if (SharePref.getBoolean(AdsKeys.InApp, false)) {
                bind.nativeAd.setVisibility(View.GONE);
            }  else if (screenrecordinggalleryadmob_native) {
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
            } else if (screenrecordinggalleryfacebook_native) {
                // binding.nativeAd.setVisibility(View.GONE);

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
        });

        ExoPlayer player = new ExoPlayer.Builder(context).build();
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(Uri.parse(path))
                .build();
        player.prepare();
        player.play();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            bind.player.setVisibility(View.VISIBLE);
            bind.progress.setVisibility(View.GONE);
            player.setMediaItem(mediaItem);
            bind.player.setPlayer(player);
            bind.player.setUseController(true);
        }, 2000);

        bind.back.setOnClickListener(view -> dialog.dismiss());
        dialog.setOnDismissListener(dialogInterface -> {
            player.stop();
            dialogInterface.dismiss();
        });

        dialog.show();
    }




}
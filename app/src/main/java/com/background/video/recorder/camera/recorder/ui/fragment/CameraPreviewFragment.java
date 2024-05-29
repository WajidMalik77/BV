package com.background.video.recorder.camera.recorder.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.databinding.LayoutPreviewVideoBinding;
import com.background.video.recorder.camera.recorder.listener.MediaFileProvider;
import com.background.video.recorder.camera.recorder.model.MediaFiles;
import com.background.video.recorder.camera.recorder.util.camera.PreviewCamera;
import com.background.video.recorder.camera.recorder.util.file.FileUtils;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;
import com.background.video.recorder.camera.recorder.viewmodel.MediaFilesViewModel;

public class CameraPreviewFragment extends Fragment {
    private static final String TAG = "CameraPreviewFragment";
    PreviewCamera camera;
    boolean timerStarted = false;
    private LayoutPreviewVideoBinding videoBinding;
    private boolean recordingStatus = false;
    private MediaFilesViewModel viewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FileUtils fileUtils;
    private long availableSpace;
    public CameraPreviewFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireActivity().getSharedPreferences("stateSavePreference", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        camera = new PreviewCamera();
        viewModel = new ViewModelProvider(
                this,
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(MediaFilesViewModel.class);
        addMediaFileToDatabase();
        // videoBinding.ivWaves.setVisibility(View.INVISIBLE);
        fileUtils = new FileUtils(getContext());


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        videoBinding = LayoutPreviewVideoBinding.inflate(inflater, container, false);
        return videoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        availableSpace = fileUtils.getExternalStorageSpaceAvailable();


        videoBinding.llAdViewPlaceholder.setVisibility(View.VISIBLE);
//        banner.createAd(videoBinding.bannerAdContainer);


        videoBinding.ibPlayPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!recordingStatus) {
                    if (availableSpace > 600) {
                        startVideoAndRefreshUI();
                    } else {
                        Toast.makeText(getContext(), "Not enough space", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    stopVideoAndRefreshUI();
                    playCameraPreview(true);
                }
            }
        });

        videoBinding.btnHidePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationUtils.navigateBack(videoBinding.getRoot());
            }
        });

        playCameraPreview(true);

    }


    private void startVideoAndRefreshUI() {
        try {
            timerStarted = true;
            videoBinding.previewCameraSurface.setVisibility(View.VISIBLE);
            playCameraPreview(false);
            videoBinding.ibPlayPreview.setImageResource(R.drawable.ic_stop);
            videoBinding.ivWaves.setVisibility(View.VISIBLE);
            videoBinding.ivWaves.playAnimation();
            videoBinding.llRecording.setVisibility(View.VISIBLE);
            videoBinding.btnHidePreview.setVisibility(View.INVISIBLE);
        } catch (NullPointerException e) {
            e.getLocalizedMessage();
        }

    }

    private void stopVideoAndRefreshUI() {
        try {
            videoBinding.chronoTimer.stop();
            videoBinding.chronoTimer.setBase(SystemClock.elapsedRealtime());
            videoBinding.chronoTimer.setText("00:00");
            videoBinding.chronoTimer.setVisibility(View.INVISIBLE);
            videoBinding.llRecording.setVisibility(View.INVISIBLE);
            videoBinding.previewCameraSurface.setVisibility(View.INVISIBLE);
            videoBinding.ibPlayPreview.setImageResource(R.drawable.ic_play);
            videoBinding.ivWaves.pauseAnimation();
            videoBinding.ivWaves.setVisibility(View.INVISIBLE);
            PreviewCamera.recording.stop();
            videoBinding.animSavingVideo.setVisibility(View.VISIBLE);
            videoBinding.tvSavingVideo.setVisibility(View.VISIBLE);
            showSavingAnim();
            recordingStatus = false;
            videoBinding.btnHidePreview.setVisibility(View.VISIBLE);
            timerStarted = false;
        } catch (NullPointerException e) {
            e.getLocalizedMessage();
        }

    }

    public void playCameraPreview(boolean isOnlyPreview) {
        int limit = sharedPreferences.getInt("videoDurationLimit", 0);

        camera.initCamera(requireContext(), videoBinding, isOnlyPreview);
        if (!isOnlyPreview) {
            videoBinding.chronoTimer.setBase(SystemClock.elapsedRealtime());
            videoBinding.chronoTimer.setVisibility(View.VISIBLE);
            recordingStatus = true;
            videoBinding.chronoTimer.start();
            // Get duration from setting preferences
            // if duration > 0 call this
            if (limit != 0) {
                setTimer(limit);
            } else {
                Log.d(TAG, "playCameraPreview: " + "timer started no limit is set");
                // else do nothing
            }
        }
    }

    private void showSavingAnim() {
        videoBinding.animSavingVideo.setVisibility(View.VISIBLE);
        videoBinding.tvSavingVideo.setVisibility(View.VISIBLE);
        if (videoBinding.animSavingVideo.getVisibility() == View.VISIBLE) {
            videoBinding.animSavingVideo.addAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    try {
                        NavigationUtils.navigate(getActivity(), R.id.action_cameraPreviewFragment_to_filesFragment);
                    } catch (Exception e) {
                        Log.e(TAG, "onAnimationEnd: " + e.getLocalizedMessage());
                    }

                }
            });
        }
    }

    private void setTimer(int durationMin) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                stopVideoAndRefreshUI();
            }
        }, (long) durationMin * 60 * 1000);

    }


    // this method add user media file information to database when using camera preview
    private void addMediaFileToDatabase() {
        camera.setMediaFileProvider(new MediaFileProvider() {
            @Override
            public void mediaFileInformation(MediaFiles mediaFiles) {
                if (mediaFiles == null) {
//                    Toast.makeText(requireActivity().getApplicationContext(), "Media File is Empty", Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.insertMediaFiles(mediaFiles);
//                    Toast.makeText(requireActivity().getApplicationContext(), "Media File is added to database", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
        if (recordingStatus) {
            stopVideoAndRefreshUI();
            recordingStatus = false;
            Log.e(TAG, "onPause: " + " on pause called and video has been saved ");
        } else {
            Log.e(TAG, "onPause: " + "on pause called video was not being recorded!");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
        if (recordingStatus) {
        } else {
            Log.e(TAG, "onStop: " + "recording was open");
        }
    }
}
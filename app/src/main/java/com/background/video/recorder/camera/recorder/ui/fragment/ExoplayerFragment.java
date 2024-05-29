package com.background.video.recorder.camera.recorder.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.background.video.recorder.camera.recorder.databinding.LayoutExoplayerBinding;
import com.background.video.recorder.camera.recorder.util.constant.Constants;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

public class ExoplayerFragment extends Fragment {
    private static final String TAG = "ExoplayerFragment";
    private LayoutExoplayerBinding exoplayerBinding;
    private ExoPlayer player;
    private MediaItem mediaItem;
    private String videoPath;


    public ExoplayerFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            videoPath = getArguments().getString("videoPath");
            Log.d(TAG, "onCreate: " + videoPath);
        } else {
            Log.d(TAG, "onCreate: " + " Empty Video Path  ");
        }
        if (!videoPath.isEmpty()) {
            initExoPlayer(getContext());
        }
        if (Constants.FAV_FILE_CLICK) {
            if (getArguments() != null) {
                videoPath = getArguments().getString("videoPath");
                Log.d(TAG, "onCreate: " + videoPath);
            } else {
                Log.d(TAG, "onCreate: " + " Empty Video Path  ");
            }
            if (!videoPath.isEmpty()) {
                if (isAdded())
                    initExoPlayer(requireContext());
            }
        } else {
            Log.d(TAG, "onCreate: " + "did not get path of video file");
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        exoplayerBinding = LayoutExoplayerBinding.inflate(inflater, container, false);
        return exoplayerBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        initComponents();
    }


    private void initComponents() {
        exoplayerBinding.llAdViewPlaceholder.setVisibility(View.VISIBLE);
        exoplayerBinding.exoPlayerView.setPlayer(player);
        exoplayerBinding.exoPlayerView.setUseController(true);
        player.setMediaItem(mediaItem);
        setListener();
    }

    private void setListener() {
        exoplayerBinding.ibBack.setOnClickListener(view -> {
            player.stop();
            NavigationUtils.navigateBack(exoplayerBinding.getRoot());

        });
    }

    public void initExoPlayer(Context context) {
        player = new ExoPlayer.Builder(context).build();
        mediaItem = new MediaItem.Builder()
                .setUri(videoPath)
                .setMimeType(Constants.VIDEO_EXTENSION)
                .build();
        player.prepare();
        player.play();
    }

    @Override
    public void onPause() {
        super.onPause();
        player.stop();
    }
}

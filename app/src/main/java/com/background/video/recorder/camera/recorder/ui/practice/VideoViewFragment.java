package com.background.video.recorder.camera.recorder.ui.practice;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.background.video.recorder.camera.recorder.R;

public class VideoViewFragment extends Fragment {

    private static final String ARG_VIDEO_URI = "videoUri";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_view, container, false);
        VideoView videoView = view.findViewById(R.id.video_view);

        if (getArguments() != null && getArguments().containsKey(ARG_VIDEO_URI)) {
            Uri videoUri = Uri.parse(getArguments().getString(ARG_VIDEO_URI));
            videoView.setVideoURI(videoUri);

            // Create media controller
            MediaController mediaController = new MediaController(requireContext());
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // Start playing the video
                    videoView.start();

                    // Show the media controller
                    mediaController.show();
                }
            });
        }

        return view;
    }


}

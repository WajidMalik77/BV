package com.background.video.recorder.camera.recorder.ui.practice;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.media.browse.MediaBrowser;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.background.video.recorder.camera.recorder.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.databinding.VideoPreviewDialogBinding;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

import java.io.File;
import java.util.Locale;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<File> videoFiles;
    private Context context;

    public interface OnVideoClickListener {
        void onVideoClick(Uri videoUri);
    }

    private OnVideoClickListener onVideoClickListener;

    public void setOnVideoClickListener(OnVideoClickListener listener) {
        this.onVideoClickListener = listener;
    }
    static boolean native_bg;
    static SharedPreferences sharedPrefs;

    public VideoAdapter(Context context, List<File> videoFiles) {
        this.context = context;
        this.videoFiles = videoFiles;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
//        File videoFile = videoFiles.get(position);
//
//        holder.videoName.setText(videoFile.getName());
//        Glide.with(context).load(Uri.fromFile(videoFile)).centerCrop().into(holder.thumbnail);
//
//        // Set video size
//        long fileSizeInBytes = videoFile.length();
//        long fileSizeInKB = fileSizeInBytes / 1024;
//        long fileSizeInMB = fileSizeInKB / 1024;
//        holder.size.setText(fileSizeInMB + " MB");
//
//        // Set video duration
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(videoFile.getAbsolutePath());
//        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//        long timeInMillisec = Long.parseLong(time);
//        holder.duration.setText(formatDuration(timeInMillisec));
//        try {
//            retriever.release();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        holder.itemView.setOnClickListener(v -> {
//            // Use the playDialog method to play the video
//            playDialog(context, Uri.fromFile(videoFile).toString(), MimeTypeMap.getFileExtensionFromUrl(videoFile.getAbsolutePath()));
//        });
//
//    }


    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        File videoFile = videoFiles.get(position);

        holder.videoName.setText(videoFile.getName());
        Glide.with(context).load(Uri.fromFile(videoFile)).centerCrop().into(holder.thumbnail);

        // You can add more details such as video duration and size here
        // For now, setting dummy data



        // Set video size
        long fileSizeInBytes = videoFile.length();
        long fileSizeInKB = fileSizeInBytes / 1024;
        long fileSizeInMB = fileSizeInKB / 1024;
        holder.size.setText(fileSizeInMB + " MB");

        // Set video duration
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoFile.getAbsolutePath());
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        if (time != null) {
            try {
                long timeInMillisec = Long.parseLong(time);
                holder.duration.setText(formatDuration(timeInMillisec));
            } catch (NumberFormatException e) {
                // Handle the case where parsing fails
                holder.duration.setText("N/A");
            }
        } else {
            holder.duration.setText("N/A");
        }

        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        holder.itemView.setOnClickListener(v -> {
            if (onVideoClickListener != null) {
                Uri videoUri = Uri.fromFile(videoFile);
                onVideoClickListener.onVideoClick(videoUri);
            }
        });

    }


    public static void playDialog(Context context, String path, String extension) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        VideoPreviewDialogBinding bind = VideoPreviewDialogBinding.inflate(inflater);

        Dialog dialog = new Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(bind.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        sharedPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        native_bg   = sharedPrefs.getBoolean("native_bg", false);
        if (native_bg) {
//            InterstitialAdUtils.INSTANCE.loadMediationNative((Activity) context, bind.nativeAd);
        }else {
            bind.nativeAd.setVisibility(View.INVISIBLE);
        }

        dialog.setOnShowListener(dialogInterface -> {
            // Replace with your ad logic if needed
            Log.d("home_frag", "onCreateView: Trim fragment");
            // Example: InterstitialAdUtils.loadMediationNative((Activity) context, bind.nativeAd);
        });

        ExoPlayer player = new ExoPlayer.Builder(context).build();
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(Uri.parse(path))
                .setMimeType(extension)
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


    private void updateFavoriteIcon(VideoViewHolder holder, boolean isFavorite) {
        int drawableId = isFavorite ? R.drawable.fav : R.drawable.favorites;
        holder.favourite.setImageResource(drawableId);
    }

    private String formatDuration(long durationInMillis) {
        long seconds = (durationInMillis / 1000) % 60;
        long minutes = (durationInMillis / (1000 * 60)) % 60;
        long hours = durationInMillis / (1000 * 60 * 60);

        if (hours > 0) {
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
    }

    @Override
    public int getItemCount() {
        return videoFiles.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail, more, favourite;
        TextView videoName, duration, size;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            more = itemView.findViewById(R.id.more);
            favourite = itemView.findViewById(R.id.favourite);
            videoName = itemView.findViewById(R.id.videoName);
            duration = itemView.findViewById(R.id.duration);
            size = itemView.findViewById(R.id.size);
        }
    }
}

package com.background.video.recorder.camera.recorder.util.camera;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.FileOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoRecordEvent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.lifecycle.LifecycleOwner;

import com.background.video.recorder.camera.recorder.databinding.LayoutPreviewVideoBinding;
import com.background.video.recorder.camera.recorder.listener.MediaFileProvider;
import com.background.video.recorder.camera.recorder.model.MediaFiles;
import com.background.video.recorder.camera.recorder.util.constant.Constants;
import com.background.video.recorder.camera.recorder.util.file.FileUtils;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PreviewCamera {
    private static final String TAG = "PreviewCamera";
    public static Recording recording;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private Preview preview;
    private MediaFiles mediaFile;
    private MediaFileProvider mediaFileProvider;
    private FileOutputOptions outputOptions;
    private Camera camera;
    private Recorder recorder;
    private FileUtils fileUtils;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProcessCameraProvider cameraProvider;


    public void setMediaFileProvider(MediaFileProvider mediaFileProvider) {
        this.mediaFileProvider = mediaFileProvider;
    }


    public void initCamera(Context context, LayoutPreviewVideoBinding binding, boolean isOnlyPreview) {
        sharedPreferences = context.getSharedPreferences("stateSavePreference", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(context);// getting camera x instance
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            cameraProviderListenableFuture.addListener(() -> {
                try {
                    cameraProvider = cameraProviderListenableFuture.get();
                    startCamera(cameraProvider, binding, context, isOnlyPreview);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, context.getMainExecutor());
        } else {

            cameraProviderListenableFuture.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                    startCamera(cameraProvider, binding, context, isOnlyPreview);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, ContextCompat.getMainExecutor(context));

            Log.e(TAG, "initCamera: " + "idhr issue h");
        }
    }


    @SuppressLint("RestrictedApi")
    public void startCamera(ProcessCameraProvider cameraProvider, LayoutPreviewVideoBinding binding, Context c, boolean isOnlyPreview) {
        cameraProvider.unbindAll(); // unbind all the previous components with camera
        // camera selector is for using front or back lens
        preview = new Preview.Builder().build(); // binding our view with camera
        preview.setSurfaceProvider(binding.previewCameraSurface.getSurfaceProvider());

        if (isOnlyPreview) {
            cameraProvider.bindToLifecycle((LifecycleOwner) c, toggleCamera(), preview);
            return;
        }


        int videoQuality = sharedPreferences.getInt("quality", 0);
        Log.e(TAG, "startCamera: " + videoQuality);

        if (videoQuality == 1) {
            Log.d(TAG, "startCamera quality checking : ==  " + videoQuality);
            recorder = new Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.FHD)).build();
            androidx.camera.video.VideoCapture videoCapture = androidx.camera.video.VideoCapture.withOutput(recorder);
            camera = cameraProvider.bindToLifecycle((LifecycleOwner) c, toggleCamera(), preview, videoCapture);
        } else if (videoQuality == 2) {
            Log.d(TAG, "startCamera quality checking : ==  " + videoQuality);
            recorder = new Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.HD)).build();
            androidx.camera.video.VideoCapture videoCapture = androidx.camera.video.VideoCapture.withOutput(recorder);
            camera = cameraProvider.bindToLifecycle((LifecycleOwner) c, toggleCamera(), preview, videoCapture);
        } else if (videoQuality == 3) {
            Log.d(TAG, "startCamera quality checking : ==  " + videoQuality);
            recorder = new Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.SD)).build();
            androidx.camera.video.VideoCapture videoCapture = androidx.camera.video.VideoCapture.withOutput(recorder);
            camera = cameraProvider.bindToLifecycle((LifecycleOwner) c, toggleCamera(), preview, videoCapture);
        } else if (videoQuality == 4) {
            Log.d(TAG, "startCamera quality checking : ==  " + videoQuality);
            recorder = new Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.LOWEST)).build();
            androidx.camera.video.VideoCapture videoCapture = androidx.camera.video.VideoCapture.withOutput(recorder);
            camera = cameraProvider.bindToLifecycle((LifecycleOwner) c, toggleCamera(), preview, videoCapture);
        } else {
            Log.d(TAG, "startCamera quality checking : ==  " + videoQuality);
            recorder = new Recorder.Builder().build();
            androidx.camera.video.VideoCapture videoCapture = androidx.camera.video.VideoCapture.withOutput(recorder);
            camera = cameraProvider.bindToLifecycle((LifecycleOwner) c, toggleCamera(), preview, videoCapture);
        }


        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // outputDirectory = getOutputDirectory(c);
        fileUtils = new FileUtils(c);
        File videoFile = createFile(fileUtils.getAppDir(), Constants.FILENAME, Constants.VIDEO_EXTENSION);
        outputOptions = new FileOutputOptions.Builder(videoFile).build();


        mediaFile = new MediaFiles(
                videoFile.getName(),
                Constants.VIDEO_EXTENSION,
                Constants.MEDIA_TYPE_NON_FAVOURITE,
                videoFile.getAbsolutePath(),
                Constants.MEDIA_TYPE_VIDEO,
                Constants.FILE_TYPE_UNLOCKED
        );
        Log.d(TAG, "startCamera: videoPath" + videoFile.getAbsolutePath());

        if (mediaFileProvider != null) {
            mediaFileProvider.mediaFileInformation(mediaFile);
        }

        Log.d(TAG, "startCamera: path" + outputOptions.toString());

        boolean noSound = sharedPreferences.getBoolean("noSound", false);
        if (noSound) {
            recording = recorder.prepareRecording(c, outputOptions)
                    .start(ContextCompat.getMainExecutor(c), new Consumer<VideoRecordEvent>() {
                        @Override
                        public void accept(VideoRecordEvent videoRecordEvent) {

                        }
                    });
        } else {
            recording = recorder.prepareRecording(c, outputOptions)
                    .withAudioEnabled()
                    .start(ContextCompat.getMainExecutor(c), new Consumer<VideoRecordEvent>() {
                        @Override
                        public void accept(VideoRecordEvent videoRecordEvent) {
                        }
                    });
        }


    }


    private File createFile(File baseFolder, String format, String extension) {
        return new File(baseFolder, new SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + Constants.VIDEO_EXTENSION);
    }


    public CameraSelector toggleCamera() {
        if (sharedPreferences.getBoolean("frontCamera", false)) {
            return CameraSelector.DEFAULT_FRONT_CAMERA;
        } else {
            return CameraSelector.DEFAULT_BACK_CAMERA;
        }
    }
}

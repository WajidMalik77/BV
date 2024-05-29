package com.background.video.recorder.camera.recorder.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.lifecycle.LifecycleService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.model.MediaFiles;
import com.background.video.recorder.camera.recorder.ui.activitiy.HomeActivity;
import com.background.video.recorder.camera.recorder.util.constant.Constants;
import com.background.video.recorder.camera.recorder.util.file.FileUtils;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

public class BackgroundVideoRecording extends LifecycleService {

    private static WindowManager windowManager;
    private static RelativeLayout relativeLayout;
    private static PreviewView cameraView;
    private static ImageView cross;
    public static CardView cameraCard;
    public static ProcessCameraProvider backgroundCamera;
    private static WindowManager.LayoutParams params;
    static int flag;
    float dX;
    float dY;
    int lastAction;
    public static final String BROADCAST_ACTION = "br.com.example.backgroundvideorecorder.BROADCAST";
    public static final String BROADCAST_STOP_VIDEO = "br.com.example.backgroundvideorecorder.STOP_VIDEO";
    public static final String BROADCAST_START_VIDEO = "br.com.example.backgroundvideorecorder.START_VIDEO";


    private static final String TAG = "BackgroundVideoRecorder";
    public static boolean serviceIsRunning = false;
    final int WHAT = 102;
    private final Binder myBinder = new MyBackgroundIBinder();
    TimerTask timerTask;
    private ListenableFuture<ProcessCameraProvider> backgroundCameraProvider;
    private FileUtils fileUtils;
    private Camera camera;
    public Preview preview;
    private Recording recording;
    private FileOutputOptions serviceOutputOptions;
    private MediaFiles mediaFiles;
    private Receiver receiver;
    private File videoFile;
    private Recorder recorder;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Timer timer;
    private Handler handler;

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate() {
        backgroundCameraProvider = ProcessCameraProvider.getInstance(this);
        //outputDirectory = getOutputDirectory(this);
        fileUtils = new FileUtils(this);
        receiver = new Receiver();
        sharedPreferences = getSharedPreferences("stateSavePreference", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        super.onCreate();

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @SuppressLint("RestrictedApi")
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
//            Log.e(TAG, "onStartCommand: " + intent.getAction());

        if (intent != null) {
            String actionType = intent.getStringExtra("ACTION_TYPE");


            Log.d(TAG, "onStartCommand: ");
            setListener();
//        showWindowManager();
//        startRecordingWithCameraX();

            if ("video_recorder".equals(actionType)) {
                showWindowManagerForVideoRecorder();
            } else if ("bg_recorder".equals(actionType)) {
                showWindowManager();
            }

            showNotification();
            super.onStartCommand(intent, flags, startId);

        }

        return START_STICKY;
    }



    @SuppressLint("ClickableViewAccessibility")
    private void showWindowManager() {



        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        relativeLayout = (RelativeLayout) inflater.inflate(R.layout.overlay_view_for_camera, null);
        cross = relativeLayout.findViewById(R.id.hide_camera);
        cameraView = relativeLayout.findViewById(R.id.cameraViewPreview);
        cameraCard = relativeLayout.findViewById(R.id.camera_card);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            flag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            flag = WindowManager.LayoutParams.TYPE_PHONE;
        }
//        params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                flag,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
//                PixelFormat.TRANSLUCENT
//        );


        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,  // For full width
                WindowManager.LayoutParams.MATCH_PARENT,  // For full height
                flag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
        );



//        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        relativeLayout.setOnTouchListener((v, event) ->
                {
                    WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) relativeLayout.getLayoutParams();

                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            dX = layoutParams.x - event.getRawX();
                            dY = layoutParams.y - event.getRawY();
                            lastAction = MotionEvent.ACTION_DOWN;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            layoutParams.y = (int) (event.getRawY() + dY);
                            layoutParams.x = (int) (event.getRawX() + dX);
                            lastAction = MotionEvent.ACTION_MOVE;
                            windowManager.updateViewLayout(v, layoutParams);
                        case MotionEvent.ACTION_UP:
                            if (lastAction == MotionEvent.ACTION_DOWN) {
                                if (cross.getVisibility() == View.GONE) {
                                    cross.setVisibility(View.VISIBLE);
                                } else {
                                    cross.setVisibility(View.GONE);
                                }
                                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                            return false;
                    }
                    return true;
                }

        );
        cross.setOnClickListener(view -> {
            removeWindowManager();
        });
        windowManager.addView(relativeLayout, params);
    }

//    @SuppressLint("ClickableViewAccessibility")
//    private void showWindowManagerForVideoRecorder() {
//
//
//        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        relativeLayout = (RelativeLayout) inflater.inflate(R.layout.overlay_view_for_video_recorder, null);
//        cross = relativeLayout.findViewById(R.id.hide_camera);
//        cameraView = relativeLayout.findViewById(R.id.cameraViewPreview);
//        cameraCard = relativeLayout.findViewById(R.id.camera_card);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            flag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        } else {
//            flag = WindowManager.LayoutParams.TYPE_PHONE;
//        }
//        params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                flag,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
//                PixelFormat.TRANSLUCENT
//        );
//        params.x = 0;
//        params.y = 0;
//        relativeLayout.setOnTouchListener((v, event) ->
//                {
//                    WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) relativeLayout.getLayoutParams();
//
//                    switch (event.getActionMasked()) {
//                        case MotionEvent.ACTION_DOWN:
//                            dX = layoutParams.x - event.getRawX();
//                            dY = layoutParams.y - event.getRawY();
//                            lastAction = MotionEvent.ACTION_DOWN;
//                            break;
//                        case MotionEvent.ACTION_MOVE:
//                            layoutParams.y = (int) (event.getRawY() + dY);
//                            layoutParams.x = (int) (event.getRawX() + dX);
//                            lastAction = MotionEvent.ACTION_MOVE;
//                            windowManager.updateViewLayout(v, layoutParams);
//                        case MotionEvent.ACTION_UP:
//                            if (lastAction == MotionEvent.ACTION_DOWN) {
//                                if (cross.getVisibility() == View.GONE) {
//                                    cross.setVisibility(View.VISIBLE);
//                                } else {
//                                    cross.setVisibility(View.GONE);
//                                }
//                                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
//                            }
//                            break;
//                        default:
//                            return false;
//                    }
//                    return true;
//                }
//
//        );
//        cross.setOnClickListener(view -> {
//            removeWindowManager();
//        });
//        windowManager.addView(relativeLayout, params);
//    }


    @SuppressLint("ClickableViewAccessibility")
    private void showWindowManagerForVideoRecorder() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        relativeLayout = (RelativeLayout) inflater.inflate(R.layout.overlay_view_for_video_recorder, null);
        cross = relativeLayout.findViewById(R.id.hide_camera);
        cameraView = relativeLayout.findViewById(R.id.cameraViewPreview);
        cameraCard = relativeLayout.findViewById(R.id.camera_card);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            flag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            flag = WindowManager.LayoutParams.TYPE_PHONE;
        }

        // Specify the width and height as 200x300 pixels
        int widthInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                200,
                getResources().getDisplayMetrics()
        );
        int heightInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                300,
                getResources().getDisplayMetrics()
        );

        params = new WindowManager.LayoutParams(
                widthInPixels,  // Width
                heightInPixels, // Height
                flag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
        );

        params.x = 0;
        params.y = 0;
        relativeLayout.setOnTouchListener((v, event) ->
        {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) relativeLayout.getLayoutParams();

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    dX = layoutParams.x - event.getRawX();
                    dY = layoutParams.y - event.getRawY();
                    lastAction = MotionEvent.ACTION_DOWN;
                    break;
                case MotionEvent.ACTION_MOVE:
                    layoutParams.y = (int) (event.getRawY() + dY);
                    layoutParams.x = (int) (event.getRawX() + dX);
                    lastAction = MotionEvent.ACTION_MOVE;
                    windowManager.updateViewLayout(v, layoutParams);
                case MotionEvent.ACTION_UP:
                    if (lastAction == MotionEvent.ACTION_DOWN) {
                        if (cross.getVisibility() == View.GONE) {
                            cross.setVisibility(View.VISIBLE);
                        } else {
                            cross.setVisibility(View.GONE);
                        }
                        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    return false;
            }
            return true;
        });

        cross.setOnClickListener(view -> {
            removeWindowManager();
        });

        windowManager.addView(relativeLayout, params);
    }


    public static void removeWindowManager() {
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) relativeLayout.getLayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.x = -10000;
        layoutParams.y = -10000;
        windowManager.updateViewLayout(relativeLayout, layoutParams);
    }

    public static void addWindowManager() {
        Log.d("backyyy", "addWindowManager: clicked");
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) relativeLayout.getLayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.x = 0;
        layoutParams.y = 0;
        windowManager.updateViewLayout(relativeLayout, layoutParams);
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private void showNotification() {
        notificationChannel();
        //for stopping video from notification
        Intent stopVideo = new Intent(this, BackgroundVideoRecording.Receiver.class);
        stopVideo.setAction(BROADCAST_STOP_VIDEO);
        Receiver.backgroundVideoRecording = this;
        PendingIntent stopVideoPendingIntent = PendingIntent.getBroadcast(this, 100, stopVideo, PendingIntent.FLAG_IMMUTABLE);
        //for clicking on notification
        Intent intent1 = new Intent(this, HomeActivity.class);
        intent1.setAction("VID_STARTED");
        //    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.putExtra("notificationTapped", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 123456, intent1, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this, "2411")
                .setContentTitle("Screen Rec & Video Rec")
                .setContentText("Video is running in Background")
                .setContentIntent(pendingIntent)
                .setUsesChronometer(true)
                .setSmallIcon(R.drawable.app_icon)
                .setAutoCancel(false)
                .addAction(new NotificationCompat.Action(R.drawable.ic_stop_notification, "STOP", stopVideoPendingIntent))
                .build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.e(TAG, "showNotification: " + " if called ");
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA);
        } else {
            Log.e(TAG, "showNotification: " + "else called ");
            startForeground(1, notification);
        }
        //    videoStarted(this);

    }


    @Nullable
    @Override
    public IBinder onBind(@NonNull Intent intent) {
        Log.e(TAG, "onBind: ");
        super.onBind(intent);
        return myBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.e(TAG, "onRebind: ");
        super.onRebind(intent);
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        Log.e(TAG, "unbindService: ");
        super.unbindService(conn);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind: ");
        return true;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: ");
        super.onDestroy();
        try {
            //stopTimer();
            try {
                windowManager.removeView(relativeLayout);
            } catch (IllegalArgumentException e) {
            }
            recording.stop();
            stopForeground(true);
            stopSelf();
            serviceIsRunning = false;
        } catch (Exception e) {
        }
    }

    private void notificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("2411", "myVideoReocrderChannel"
                    , NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void setListener() {
        Log.d(TAG, "setListener: ");
        backgroundCameraProvider.addListener(() -> {
            backgroundCamera = null;
            try {
                backgroundCamera = backgroundCameraProvider.get();
                startBackgroundCamera(backgroundCamera);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, getServiceExecutor());
    }

    private Executor getServiceExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    @SuppressLint("RestrictedApi")
    private void startBackgroundCamera(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll(); // unbind all the previous components with camera
        // camera selector is for using front or back lens
        CameraSelector cameraSelector = new CameraSelector
                .Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(cameraView.getSurfaceProvider());
        // video capture lifecycle
        //binding our use cases with camera x life

        cameraProvider.bindToLifecycle(this, toggleCamera(), preview);
        createRecording(cameraProvider);
    }


    private File createFile(File baseFolder, String format, String extension) {
        return new File(baseFolder, new SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + extension);
    }


    private void createRecording(ProcessCameraProvider cameraProvider) {
        try {
//            cameraProvider.unbindAll();
            int videoQuality = sharedPreferences.getInt("quality", 0);
            Log.e(TAG, "startCamera: " + videoQuality);
            if (videoQuality == 1) {
                Log.d(TAG, "startCamera quality checking : ==  " + videoQuality);
                recorder = new Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.FHD)).build();
                androidx.camera.video.VideoCapture videoCapture = androidx.camera.video.VideoCapture.withOutput(recorder);
                camera = cameraProvider.bindToLifecycle(this, toggleCamera(), videoCapture);
            } else if (videoQuality == 2) {
                Log.d(TAG, "startCamera quality checking : ==  " + videoQuality);
                recorder = new Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.HD)).build();
                androidx.camera.video.VideoCapture videoCapture = androidx.camera.video.VideoCapture.withOutput(recorder);
                camera = cameraProvider.bindToLifecycle(this, toggleCamera(), videoCapture);
            } else if (videoQuality == 3) {
                Log.d(TAG, "startCamera quality checking : ==  " + videoQuality);
                recorder = new Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.SD)).build();
                androidx.camera.video.VideoCapture videoCapture = androidx.camera.video.VideoCapture.withOutput(recorder);
                camera = cameraProvider.bindToLifecycle(this, toggleCamera(), videoCapture);
            } else if (videoQuality == 4) {
                Log.d(TAG, "startCamera quality checking : ==  " + videoQuality);
                recorder = new Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.LOWEST)).build();
                androidx.camera.video.VideoCapture videoCapture = androidx.camera.video.VideoCapture.withOutput(recorder);
                camera = cameraProvider.bindToLifecycle(this, toggleCamera(), videoCapture);
            } else if (videoQuality == 0) {
                Log.d(TAG, "startCamera quality checking : ==  " + videoQuality);
                recorder = new Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.HD)).build();
                androidx.camera.video.VideoCapture videoCapture = androidx.camera.video.VideoCapture.withOutput(recorder);
                camera = cameraProvider.bindToLifecycle(this, toggleCamera(), videoCapture);
            }


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }


            videoFile = createFile(fileUtils.getAppDir(), Constants.FILENAME, Constants.VIDEO_EXTENSION);
            serviceOutputOptions = new FileOutputOptions.Builder(videoFile).build();
            File file = new File(videoFile.getAbsolutePath());

            mediaFiles = new MediaFiles(
                    videoFile.getName(),
                    Constants.VIDEO_EXTENSION,
                    Constants.MEDIA_TYPE_NON_FAVOURITE,
                    videoFile.getAbsolutePath(),
                    Constants.MEDIA_TYPE_VIDEO,
                    Constants.FILE_TYPE_UNLOCKED
            );
            if (mediaFiles != null) {
                Log.d(TAG, "createRecording: mediaFiles==" + mediaFiles.getName());
                sendMediaFileToActivity(mediaFiles, this);
            }


            StopVideoAndService.backgroundVideoRecording = this;


            boolean noSound = sharedPreferences.getBoolean("noSound", false);
            if (noSound) {
                recording = recorder.prepareRecording(this, serviceOutputOptions)
                        .start(ContextCompat.getMainExecutor(this), new Consumer<VideoRecordEvent>() {
                            @Override
                            public void accept(VideoRecordEvent videoRecordEvent) {

                            }
                        });
            } else {
                recording = recorder.prepareRecording(this, serviceOutputOptions)
                        .withAudioEnabled()
                        .start(ContextCompat.getMainExecutor(this), new Consumer<VideoRecordEvent>() {
                            @Override
                            public void accept(VideoRecordEvent videoRecordEvent) {
                            }
                        });
            }


            serviceIsRunning = true;

            Log.e(TAG, "createRecording: " + serviceIsRunning);
        } catch (NullPointerException e) {
            e.getLocalizedMessage();
        }


    }

    @Override
    public boolean stopService(Intent name) {
        Log.e(TAG, "stopService: ");

        recording.stop();
        serviceIsRunning = false;
        //stopTimer();
        return super.stopService(name);
    }

    private void sendMediaFileToActivity(MediaFiles mediaFiles, Context context) {
        try {
            Log.e(TAG, "sendMediaFileToActivity: ");

            LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(
                    new Intent(BROADCAST_ACTION).putExtra(Constants.MEDIA_FILE_TRANSFER_KEY, mediaFiles)
            );
            // Toast.makeText(getApplicationContext(), "added to database", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "sendMediaFileToActivity: ", e.getCause());
        }

    }

    private void videoStopped(Context context) {
        try {
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(BROADCAST_STOP_VIDEO).putExtra("videoStopped", true));
            Log.e(TAG, "videoStopped: " + "video stopped with notification");
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }


    public CameraSelector toggleCamera() {

        if (sharedPreferences.getBoolean("frontCamera", false)) {
            return new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();
        } else {
            return new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        }
    }
//
//    public void startTimer(TextView textView) {
//        handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message message) {
//                switch (message.what) {
//                    case WHAT:
//
//                        textView.setText(message.obj + "");
//                        break;
//                }
//
//                return true;
//            }
//        });
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                Message message = new Message();
//                message.what = WHAT;
//                message.obj = (System.currentTimeMillis()) / 1000;
//                Log.e(TAG, "run: " + SystemClock.elapsedRealtime() + "");
//                handler.sendMessage(message);
//            }
//        };
//        timer = new Timer();
//        timer.schedule(timerTask, 0, 1000);
//
//    }

//    public void stopTimer() {
//        timer.cancel();
//        timerTask.cancel();
//    }


    public interface VideoStoppedListener {
        void isVideoStopped(boolean stopped);
    }

    public static class Receiver extends BroadcastReceiver {
        private static final String TAG = "Receiver";
        public static BackgroundVideoRecording backgroundVideoRecording;


        public Receiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent != null) {
                    if (backgroundVideoRecording != null)
                        if (intent.getAction() != null)
                            if (BROADCAST_STOP_VIDEO.equals(intent.getAction())) {
                                Constants.STOPPED_FROM_NOTIFICATION = true;
                                backgroundVideoRecording.recording.stop();
                                backgroundVideoRecording.stopForeground(true);
                                backgroundVideoRecording.stopSelf();
                                backgroundVideoRecording.videoStopped(backgroundVideoRecording.getBaseContext());
                            }
                }
            }catch (NullPointerException e){}
        }
    }

    public static class StopVideoAndService {
        private static final String TAG = "StopVideoAndService";
        public static BackgroundVideoRecording backgroundVideoRecording;


        public static void stopVideo() {
            backgroundVideoRecording.recording.stop();
            backgroundVideoRecording.stopForeground(true);
            backgroundVideoRecording.stopSelf();
        }
    }

    public class MyBackgroundIBinder extends Binder {
        public BackgroundVideoRecording getService() {
            return BackgroundVideoRecording.this;
        }
    }
}




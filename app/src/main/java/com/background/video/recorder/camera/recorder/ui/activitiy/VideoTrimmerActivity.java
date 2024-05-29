package com.background.video.recorder.camera.recorder.ui.activitiy;

//  The MIT License (MIT)

//  Copyright (c) 2018 Intuz Solutions Pvt Ltd.

//  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files
//  (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify,
//  merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:

//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
//  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
//  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

import static com.background.video.recorder.camera.recorder.util.constant.AdsKeys.InApp;
import static com.background.video.recorder.camera.recorder.util.constant.AdsKeys.isASplash;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.model.MediaFiles;
import com.background.video.recorder.camera.recorder.trimmer.customVideoViews.BackgroundTask;
import com.background.video.recorder.camera.recorder.trimmer.customVideoViews.BarThumb;
import com.background.video.recorder.camera.recorder.trimmer.customVideoViews.CustomRangeSeekBar;
import com.background.video.recorder.camera.recorder.trimmer.customVideoViews.OnRangeSeekBarChangeListener;
import com.background.video.recorder.camera.recorder.trimmer.customVideoViews.OnVideoTrimListener;
import com.background.video.recorder.camera.recorder.trimmer.customVideoViews.TileView;
import com.background.video.recorder.camera.recorder.ui.fragment.SplashTwoFragment;
import com.background.video.recorder.camera.recorder.util.FirebaseEvents;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.background.video.recorder.camera.recorder.util.constant.Constants;
import com.background.video.recorder.camera.recorder.util.file.FileUtils;
import com.background.video.recorder.camera.recorder.util.trimmerutill.Utility;
import com.background.video.recorder.camera.recorder.viewmodel.MediaFilesViewModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import roozi.app.ads.AdsManager;
import roozi.app.billing.BillingManager;
import roozi.app.interfaces.EventListener;
import roozi.app.interfaces.IPurchaseListener;


public class VideoTrimmerActivity extends AppCompatActivity implements View.OnClickListener, IPurchaseListener, EventListener {

    private static final String TAG = "VideoTrimmerActivity";
    private final Handler mHandler = new Handler();
    String srcFile;
    String dstFile;
    long durationInSec;
    MediaFilesViewModel viewModel;
    MediaFiles mediaFiles;
    private ConstraintLayout mainLayout;
    boolean homeInter,splashInter;
    OnVideoTrimListener mOnVideoTrimListener = new OnVideoTrimListener() {
        @Override
        public void onTrimStarted() {

        }

        @Override
        public void getResult(Uri uri) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(uri.getPath());
                    mediaFiles = new MediaFiles(file.getName(), Constants.VIDEO_EXTENSION, Constants.MEDIA_TYPE_NON_FAVOURITE, file.getAbsolutePath()
                            , Constants.MEDIA_TYPE_VIDEO, Constants.FILE_TYPE_UNLOCKED);
                    viewModel.insertMediaFiles(mediaFiles);
                    mainLayout.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "run: ");

                    startActivityNext(null,0);
//                    InterstitialAdUtils.INSTANCE.showInterstitialAd(VideoTrimmerActivity.this);
                    finish();


//                    AdsManager.Companion.interstitial(
//                            SplashTwoFragment.getBoolean(AdsKeys.Admob_TrimBtnClick_Inter),
//                            SplashTwoFragment.getBoolean(AdsKeys.Facebook_TrimBtnClick_Inter),
//                            AdsKeys.Admob_TrimBtnClick_Inter,
//                            AdsKeys.Facebook_TrimBtnClick_Inter,
//                            VideoTrimmerActivity.this, aBoolean -> {
//                                Log.d(TAG, "run: finish");
//                                finish();
//                                return null;
//                            });

                    FirebaseEvents.Companion.logAnalytic("Video_Trim_Success");
                }
            });


        }

        @Override
        public void cancelAction() {
        }

        @Override
        public void onError(String message) {
            Log.e(TAG, "onError: " + message);
        }
    };

    public void startActivityNext(NavController navController,
                                  int navDirections) {
        if (navController != null) {
            if (InterstitialAdUtils.INSTANCE.getAdMobHomeInterstitialAd() != null) {
                InterstitialAdUtils.INSTANCE.getAdMobHomeInterstitialAd().setFullScreenContentCallback(new FullScreenContentCallback() {
//                @Override
//                public void onAdDismissedFullScreenContent() {
//                    super.onAdDismissedFullScreenContent();
//                    InterstitialAdUtils.INSTANCE.setAdMobHomeInterstitialAd(null);
//
////                    InterstitialAdUtils.INSTANCE.loadHomeInterstitial(VideoTrimmerActivity.this);
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
//                        if (homeInter) {
//                            InterstitialAdUtils.INSTANCE.loadHomeInterstitial(VideoTrimmerActivity.this);
//                        } else {
////                            Toast.makeText(getActivity(), "hoem is false", Toast.LENGTH_SHORT).show();
//                        }
                        navController.navigate(navDirections);

//                    AdsHelper_new.isInterstitialShowing = true;
                        // AdsHelper.loadInterstitialAd(requireActivity());
                    }
                });

                InterstitialAdUtils.INSTANCE.getAdMobHomeInterstitialAd().show(VideoTrimmerActivity.this);
//            AdsHelper_new.isShowingInterstitial = true;
            } else {
                navController.navigate(navDirections);
//            startFragment();
            }
        } else {

        }
    }

    private ImageView txtVideoCancel;
    private TextView txtVideoUpload;
    private TextView txtVideoEditTitle;
    private TextView txtVideoTrimSeconds;
    private TileView tileView;
    private CustomRangeSeekBar mCustomRangeSeekBarNew;
    private VideoView mVideoView;
    private ImageView imgPlay;
    private SeekBar seekBarVideo;
    private TextView txtVideoLength;
    private FileUtils fileUtils;
    private int mDuration = 0;
    private int mTimeVideo = 0;
    private int mStartPosition = 1;
    SharedPreferences sharedPrefs;
    private final Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (seekBarVideo.getProgress() >= seekBarVideo.getMax()) {
                seekBarVideo.setProgress((mVideoView.getCurrentPosition() - mStartPosition * 1000));
                txtVideoLength.setText(milliSecondsToTimer(seekBarVideo.getProgress()) + "");
                mVideoView.seekTo(mStartPosition * 1000);
                mVideoView.pause();
                seekBarVideo.setProgress(0);
                txtVideoLength.setText("00:00");
                imgPlay.setBackgroundResource(android.R.drawable.ic_media_play);
            } else {
                seekBarVideo.setProgress((mVideoView.getCurrentPosition() - mStartPosition * 1000));
                txtVideoLength.setText(milliSecondsToTimer(seekBarVideo.getProgress()) + "");
                mHandler.postDelayed(this, 100);
            }
        }
    };
    private int mEndPosition = 0;
    // set your max video trim seconds
    private int mMaxDuration;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        setLanguage(SharePref.getSelectedLanguage(), SharePref.countryCodeKey);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_trimmer);
        sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        homeInter   = sharedPrefs.getBoolean("home_inter", false);

        AdsManager.Companion.setEventListener(this);
        BillingManager.Companion.setPurchaseListener(this);
        FirebaseEvents.Companion.logAnalytic("Trimmer_Screen_Show");
        txtVideoCancel = findViewById(R.id.ivBack);
        txtVideoUpload = findViewById(R.id.ivSave);
        // txtVideoEditTitle = (TextView) findViewById(R.id.txtVideoEditTitle);
        txtVideoTrimSeconds = findViewById(R.id.txtVideoTrimSeconds);
        tileView = findViewById(R.id.timeLineView);
        mCustomRangeSeekBarNew = findViewById(R.id.timeLineBar);
        mVideoView = findViewById(R.id.videoView);
        imgPlay = findViewById(R.id.imgPlay);
        seekBarVideo = findViewById(R.id.seekBarVideo);
        txtVideoLength = findViewById(R.id.txtVideoLength);
//        frameLayout = findViewById(R.id.adViewContainerTrimmer);
//        constraintLayout = findViewById(R.id.llAdViewPlaceholderTrimmer);
        mainLayout = findViewById(R.id.lytMainTrim);
//        animationView = findViewById(R.id.savingAnimation);

        fileUtils = new FileUtils(this);

        if (getIntent().getExtras() != null) {
            srcFile = getIntent().getExtras().getString("EXTRA_PATH");
        }

        File videoFile = new File(srcFile);
        if (!videoFile.exists()) {
            Toast.makeText(this, "File does not exist!", Toast.LENGTH_SHORT).show();
            return;  // Exit the function to prevent further processing.
        }

        viewModel = new ViewModelProvider(
                this,
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
        ).get(MediaFilesViewModel.class);


        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this, Uri.fromFile(new File(srcFile)));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            durationInSec = Long.parseLong(time);
            retriever.release();
            mMaxDuration = (int) durationInSec;
            Log.e(TAG, "onCreate: " + mMaxDuration);
        } catch (NumberFormatException | IOException e) {
            Log.e(TAG, "onCreate: " + e.getLocalizedMessage());
        }


        tileView.post(new Runnable() {
            @Override
            public void run() {
                setBitmap(Uri.parse(srcFile));
                mVideoView.setVideoURI(Uri.parse(srcFile));
            }
        });

        txtVideoCancel.setOnClickListener(this);
        txtVideoUpload.setOnClickListener(this);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                onVideoPrepared(mp);
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onVideoCompleted();
            }
        });

        // handle your range seekbar changes
        mCustomRangeSeekBarNew.addOnRangeSeekBarListener(new OnRangeSeekBarChangeListener() {
            @Override
            public void onCreate(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                // Do nothing
            }

            @Override
            public void onSeek(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                try {
                    onSeekThumbs(index, value);
                }catch (NumberFormatException e){}
            }

            @Override
            public void onSeekStart(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                if (mVideoView != null) {
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    seekBarVideo.setProgress(0);
                    mVideoView.seekTo(mStartPosition * 1000);
                    mVideoView.pause();
                    imgPlay.setBackgroundResource(android.R.drawable.ic_media_play);
                }
            }

            @Override
            public void onSeekStop(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                onStopSeekThumbs();
            }
        });

        imgPlay.setOnClickListener(this);
        imgPlay.setBackgroundResource(android.R.drawable.ic_media_play);

        // handle changes on seekbar for video play
        seekBarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mVideoView != null) {
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    seekBarVideo.setMax(mTimeVideo * 1000);
                    seekBarVideo.setProgress(0);
                    mVideoView.seekTo(mStartPosition * 1000);
                    mVideoView.pause();
                    imgPlay.setBackgroundResource(android.R.drawable.ic_media_play);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                mVideoView.seekTo((mStartPosition * 1000) - seekBarVideo.getProgress());
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == txtVideoCancel) {
            finish();
        }
        else if (view == txtVideoUpload) {
            int diff = mEndPosition - mStartPosition;
            if (diff < 3) {
                Toast.makeText(this, "Video length should be minimum of 3 sec", Toast.LENGTH_SHORT).show();
            } else {

                MediaMetadataRetriever
                        mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(VideoTrimmerActivity.this, Uri.parse(srcFile));
                final File file = new File(srcFile);

                //notify that video trimming started
                if (mOnVideoTrimListener != null)
                    mOnVideoTrimListener.onTrimStarted();

                BackgroundTask.execute(
                        new BackgroundTask.Task("", 0L, "") {
                            @Override
                            public void execute() {
                                try {
                                    File videoDestination = fileUtils.createFile(fileUtils.getAppDir(), FileUtils.FILENAME, Constants.VIDEO_EXTENSION);
                                    File dst = new File(srcFile);
                                    Log.d(TAG, "execute: " + "location" + videoDestination);
                                    Utility.startTrim(file, videoDestination.getAbsolutePath(), mStartPosition * 1000, mEndPosition * 1000, mOnVideoTrimListener);
                                } catch (final Throwable e) {
                                    Log.d(TAG, "execute: " + e.getLocalizedMessage());
                                }
                            }
                        }
                );
            }

        } else if (view == imgPlay) {
            if (mVideoView.isPlaying()) {
                if (mVideoView != null) {
                    mVideoView.pause();
                    imgPlay.setBackgroundResource(android.R.drawable.ic_media_play);
                }
            } else {
                if (mVideoView != null) {
                    mVideoView.start();
                    imgPlay.setBackgroundResource(android.R.drawable.ic_media_pause);
                    if (seekBarVideo.getProgress() == 0) {
                        txtVideoLength.setText("00:00");
                        updateProgressBar();
                    }
                }
            }
        }
    }

    private void setBitmap(Uri mVideoUri) {
        tileView.setVideo(mVideoUri);
    }

    private void onVideoPrepared(@NonNull MediaPlayer mp) {
        // Adjust the size of the video
        // so it fits on the screen
        //TODO manage proportion for video
        /*int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;
        int screenWidth = rlVideoView.getWidth();
        int screenHeight = rlVideoView.getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;
        ViewGroup.LayoutParams lp = mVideoView.getLayoutParams();

        if (videoProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }
        mVideoView.setLayoutParams(lp);*/

        mDuration = mVideoView.getDuration() / 1000;
        try {
            setSeekBarPosition();
        }catch(ArithmeticException e){}
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private void setSeekBarPosition() {

        if (mDuration >= mMaxDuration) {
            mStartPosition = 1;
            mEndPosition = mMaxDuration;

            mCustomRangeSeekBarNew.setThumbValue(0, (mStartPosition * 100) / mDuration);
            mCustomRangeSeekBarNew.setThumbValue(1, (mEndPosition * 100) / mDuration);

        } else {
            mStartPosition = 1;
            mEndPosition = mDuration;
        }


        mTimeVideo = mDuration;
        mCustomRangeSeekBarNew.initMaxWidth();
        seekBarVideo.setMax(mMaxDuration * 1000);
        mVideoView.seekTo(mStartPosition * 1000);

        String mStart = mStartPosition + "";
        if (mStartPosition < 10)
            mStart = "0" + mStartPosition;

        int startMin = Integer.parseInt(mStart) / 60;
        int startSec = Integer.parseInt(mStart) % 60;

        String mEnd = mEndPosition + "";
        if (mEndPosition < 10)
            mEnd = "0" + mEndPosition;

        int endMin = Integer.parseInt(mEnd) / 60;
        int endSec = Integer.parseInt(mEnd) % 60;

        txtVideoTrimSeconds.setText(String.format(Locale.US, "%02d:%02d - %02d:%02d", startMin, startSec, endMin, endSec));
    }

    /**
     * called when playing video completes
     */
    private void onVideoCompleted() {
        mHandler.removeCallbacks(mUpdateTimeTask);
        seekBarVideo.setProgress(0);
        mVideoView.seekTo(mStartPosition * 1000);
        mVideoView.pause();
        imgPlay.setBackgroundResource(android.R.drawable.ic_media_play);
    }

    /**
     * Handle changes of left and right thumb movements
     *
     * @param index index of thumb
     * @param value value
     */
    private void onSeekThumbs(int index, float value) {
        switch (index) {
            case BarThumb.LEFT: {
                mStartPosition = (int) ((mDuration * value) / 100L);
                mVideoView.seekTo(mStartPosition * 1000);
                break;
            }
            case BarThumb.RIGHT: {
                mEndPosition = (int) ((mDuration * value) / 100L);
                break;
            }
        }
        mTimeVideo = (mEndPosition - mStartPosition);
        seekBarVideo.setMax(mTimeVideo * 1000);
        seekBarVideo.setProgress(0);
        mVideoView.seekTo(mStartPosition * 1000);

        String mStart = mStartPosition + "";
        if (mStartPosition < 10)
            mStart = "0" + mStartPosition;

        int startMin = Integer.parseInt(mStart) / 60;
        int startSec = Integer.parseInt(mStart) % 60;

        String mEnd = mEndPosition + "";
        if (mEndPosition < 10)
            mEnd = "0" + mEndPosition;
        int endMin = Integer.parseInt(mEnd) / 60;
        int endSec = Integer.parseInt(mEnd) % 60;

        txtVideoTrimSeconds.setText(String.format(Locale.US, "%02d:%02d - %02d:%02d", startMin, startSec, endMin, endSec));

    }

    private void onStopSeekThumbs() {
//        mMessageHandler.removeMessages(SHOW_PROGRESS);
//        mVideoView.pause();
//        mPlayView.setVisibility(View.VISIBLE);
    }


    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString;
        String minutesString;


        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = "" + minutes;
        }

        finalTimerString = finalTimerString + minutesString + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setLanguage(SharePref.getSelectedLanguage(), SharePref.countryCodeKey);
    }

    @Override
    public void activatePremiumVersion() {
        SharePref.putBoolean(InApp, true);
        if (VideoTrimmerActivity.this.getWindow().getDecorView().getRootView().isShown()) {
            BillingManager.Companion.showProgressDialog(
                    VideoTrimmerActivity.this,
                    new Intent(VideoTrimmerActivity.this, SplashActivity.class)
            );
        }
    }

    @Override
    public void onBackPressed() {
//        AdsManager.Companion.interstitial(
//                SplashTwoFragment.getBoolean(AdsKeys.Admob_TrimmerBack_Inter),
//                SplashTwoFragment.getBoolean(AdsKeys.Facebook_TrimmerBack_Inter),
//                AdsKeys.Admob_TrimmerBack_Inter,
//                AdsKeys.Facebook_TrimmerBack_Inter,
//                VideoTrimmerActivity.this, aBoolean -> {
//                    super.onBackPressed();
//                    return null;
//                });
    }

    @Override
    public void onDismiss(@NonNull String key) {
        FirebaseEvents.Companion.logAnalytic("${key}_Dismiss");
        Log.d("EventLogsss", "${key}_Dismiss");
    }

    @Override
    public void onAdShow(@NonNull String key) {
        FirebaseEvents.Companion.logAnalytic("${key}_Show");
        Log.d("EventLogsss", "${key}_Show");
    }

    @Override
    public void onAdClick(@NonNull String key) {
        FirebaseEvents.Companion.logAnalytic("${key}_Click");
        Log.d("EventLogsss", "${key}_Click");
    }
}


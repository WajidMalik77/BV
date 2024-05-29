package com.background.video.recorder.camera.recorder.util.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.os.StatFs;
import android.util.Log;
import android.util.Size;


import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.util.trimmerutill.Utility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FileUtils {

    private static final String TAG = "FileUtils";
    public static final String FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS";

    // Properties
    private Context context;
    public Context getContext() { return context; }
    public void setContext(Context context) { this.context = context; }

    // Constructors
    public FileUtils() { }
    public FileUtils(Context context) {
        this.context = context;
    }

    /** Gives a list of files in the internal media directory of the app **/
    public ArrayList<File> getAppMediaFiles(File appMediaDir) {
        ArrayList<File> mediaFiles = new ArrayList<>();
        File[] appMediaDirFiles = appMediaDir.listFiles();
        if (appMediaDirFiles != null) {
            for (File mediaFile : appMediaDirFiles) {
                mediaFiles.add(mediaFile);
            }
        }
        return mediaFiles;
    }

    /**
     * Gives Internal Media Folder of the app
     **/
    public File getAppDir() {
        File[] mediaDirs = context.getExternalFilesDirs("BackgroundVideos");
        if (mediaDirs != null) {
            if (mediaDirs.length > 0) {
                File myMediaDir = new File(mediaDirs[0], context.getApplicationContext().getResources().getString(R.string.app_name));
                myMediaDir.mkdirs();
                if (myMediaDir != null) {
                    if (myMediaDir.exists()) {
                        return myMediaDir;
                    } else {
                        return context.getApplicationContext().getFilesDir();
                    }
                } else {
                    Log.e(TAG, "getOutputDirectory: myMediaDir is null.");
                }
            } else {
                Log.e(TAG, "getOutputDirectory: mediaDirs size is 0");
            }
        } else {
            Log.e(TAG, "getOutputDirectory: mediaDirs is null");
        }
        return null;
    }
    public long getExternalStorageSpaceAvailable() {

        StatFs memorySpace = new StatFs(context.getExternalFilesDir("BackgroundVideos").getPath());
        long bytesAvailable;
        bytesAvailable = memorySpace.getBlockSizeLong() * memorySpace.getAvailableBlocksLong();
        long mbAvailable = bytesAvailable / (1024 * 1024 );
        Log.e("", "Available MB : " + mbAvailable);
       return mbAvailable;
    }
    public File createFile(File baseFolder, String format, String extension) {
        return new File(baseFolder, new SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + Utility.VIDEO_FORMAT);
    }
    public boolean isDeviceConnectedToInterNet() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    /**
     * Generates a thumbnail for a video file
     **/


}

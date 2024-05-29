package com.background.video.recorder.camera.recorder.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class VideoFileUtil {
    private static final String TAG = "VideoFileUtil";
    public static String Screen_Recording = "Screen_Recording";

    private Context context;
    private long MB_VALUE = 1048576;

    public VideoFileUtil(Context context) {
        this.context = context;
    }

    public File getAppFolder() {
        File[] path = context.getExternalFilesDirs("SpyVideos");
        if (path != null) {
            File folder = new File(path[0], "Video Recorder");
            if (!folder.exists()) {
                folder.mkdir();
            }
            return folder;
        } else {
            Log.e(TAG, "saveVideoToAppFolder: " + "given path is invalid");
        }
        return null;
    }



    public String getTrimDir(Context context) {
        File file = context.getExternalFilesDir("Trim");
        if (file != null)
            if (!file.exists())
                file.mkdir();

        return file.getPath();
    }
    public String getVideoDir(Context context) {
        File file = context.getExternalFilesDir("SpyVideos");
        if (file != null)
            if (!file.exists())
                file.mkdir();

        return file.getPath();
    }

    public ArrayList<File> getVideosFromAppFolder(File path) {
        ArrayList<File> files = new ArrayList<>();
        File[] videoFiles = path.listFiles();
        if (videoFiles != null) {
            files.addAll(Arrays.asList(videoFiles));
        }
        return files;
    }

    public long getAvailableStorage() {

        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
//        long bytesAvailable = (long) stat.getBlockSizeLong() * (long) stat.getBlockCountLong();
        long bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        long mbAvailable = bytesAvailable / MB_VALUE;
        System.out.println("MBs :" + mbAvailable);
        return mbAvailable;
    }

}

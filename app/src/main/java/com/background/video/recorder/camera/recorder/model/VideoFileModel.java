package com.background.video.recorder.camera.recorder.model;

import android.graphics.Bitmap;

import com.background.video.recorder.camera.recorder.util.constant.Constants;

public class VideoFileModel {

    private String name;
    private String path;
    private Bitmap thumbnail;

    public VideoFileModel() {
        setVideoFile(
                Constants.NULL_STRING,
                Constants.NULL_STRING,
                null
        );
    }
    public VideoFileModel(Bitmap thumbnail) {
        setVideoFile(Constants.NULL_STRING, Constants.NULL_STRING, thumbnail);
    }

    public VideoFileModel(String name, String path) {
        setVideoFile(name, path, null);
    }

    public VideoFileModel(String name, String path, Bitmap thumbnail) {
        setVideoFile(name, path, thumbnail);
    }

    private void setVideoFile(String name, String path, Bitmap thumbnail) {
        setName(name);
        setPath(path);
        setThumbnail(thumbnail);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

}

package com.background.video.recorder.camera.recorder.ui.practice;

public class VideoFiles {

    private String name;
    private String size;
    private long duration;
    private String path;

    public VideoFiles(String name, String size, long duration, String path) {
        this.name = name;
        this.size = size;
        this.duration = duration;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}

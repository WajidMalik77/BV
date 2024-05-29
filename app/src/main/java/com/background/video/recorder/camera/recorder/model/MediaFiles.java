package com.background.video.recorder.camera.recorder.model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "MediaFiles",indices = @Index(value = {"name"},unique = true))
public class MediaFiles implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String extension;
    private int type;        //type means favourite or not 1 means fav and 2 means non fav
    private String path;
    private String mediaType;
    private boolean locked;

    public MediaFiles(String name, String extension, int type, String path, String mediaType, boolean locked) {
        this.name = name;
        this.extension = extension;
        this.type = type;
        this.path = path;
        this.mediaType = mediaType;
        this.locked = locked;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public int getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public String getMediaType() {
        return mediaType;
    }

    public boolean isLocked() {
        return locked;
    }
}


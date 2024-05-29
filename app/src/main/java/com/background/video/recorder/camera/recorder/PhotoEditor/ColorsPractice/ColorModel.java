package com.background.video.recorder.camera.recorder.PhotoEditor.ColorsPractice;

import android.net.Uri;

public class ColorModel {
    private int colorValue;
    private String colorName;
    private Uri imageUri;

    public ColorModel(int colorValue, String colorName, Uri imageUri) {
        this.colorValue = colorValue;
        this.colorName = colorName;
        this.imageUri = imageUri;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public int getColorValue() {
        return colorValue;
    }

    public void setColorValue(int colorValue) {
        this.colorValue = colorValue;
    }

    public String getColorName() {
        return colorName;
    }
}
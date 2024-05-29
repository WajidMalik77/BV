package com.background.video.recorder.camera.recorder.ImagePicker;


import android.annotation.SuppressLint;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.background.video.recorder.camera.recorder.R;


public class Config {

    private int toolbarTitleRes =  R.string.toolbar_title;

    private int tabBackgroundColor;
    private int tabSelectionIndicatorColor;

    private int selectedBottomColor;

    private int selectionLimit = Integer.MAX_VALUE;
    private int selectionMin = 0;

    private int cameraHeight =  250;

    private int cameraBtnImage =  R.drawable.ic_camera;
    private int cameraBtnBackground =  R.drawable.btn_bg;

    private int selectedCloseImage =  R.drawable.ic_clear;
    private int selectedBottomHeight =  200;

    private int savedDirectoryName =  R.string.default_directory;


    private boolean flashOn = false;

    public int getCameraHeight() {
        return cameraHeight;
    }

    @SuppressLint("ResourceType")
    public void setCameraHeight(@DimenRes int dimenRes) {
        if (dimenRes <= 0)
            throw new IllegalArgumentException("Invalid value for cameraHeight");

        this.cameraHeight = dimenRes;
    }

    public int getSavedDirectoryName() {
        return savedDirectoryName;
    }

    @SuppressLint("ResourceType")
    public void setSavedDirectoryName(@StringRes int stringRes) {
        if (stringRes <= 0)
            throw new IllegalArgumentException("Invalid value for savedDirectoryName");

        this.savedDirectoryName = stringRes;
    }

    public int getSelectedBottomHeight() {
        return selectedBottomHeight;
    }

    @SuppressLint("ResourceType")
    public void setSelectedBottomHeight(@DimenRes int dimenRes) {
        if (dimenRes <= 0)
            throw new IllegalArgumentException("Invalid value for selectedBottomHeight");

        this.selectedBottomHeight = dimenRes;
    }

    public int getSelectedBottomColor() {
        return selectedBottomColor;
    }

    @SuppressLint("ResourceType")
    public void setSelectedBottomColor(@ColorRes int colorRes) {
        if (colorRes <= 0)
            throw new IllegalArgumentException("Invalid value for selectedBottomColor");

        this.selectedBottomColor = colorRes;
    }

    public int getToolbarTitleRes() {
        return toolbarTitleRes;
    }

    @SuppressLint("ResourceType")
    public void setToolbarTitleRes(@StringRes int toolbarTitleRes) {
        if (toolbarTitleRes <= 0)
            throw new IllegalArgumentException("Invalid value for toolbarTitleRes");

        this.toolbarTitleRes = toolbarTitleRes;
    }


    public int getTabBackgroundColor() {
        return tabBackgroundColor;
    }

    @SuppressLint("ResourceType")
    public void setTabBackgroundColor(@ColorRes int colorRes) {
        if (colorRes <= 0)
            throw new IllegalArgumentException("Invalid value for tabBackgroundColor");


        this.tabBackgroundColor = colorRes;

    }


    public int getTabSelectionIndicatorColor() {
        return tabSelectionIndicatorColor;
    }

    /**
     * Sets selected tab indicator color.
     */
    @SuppressLint("ResourceType")
    public void setTabSelectionIndicatorColor(@ColorRes int colorRes) {
        if (colorRes <= 0) throw new IllegalArgumentException("Invalid value for tabSelectionIndicatorColor");


        this.tabSelectionIndicatorColor = colorRes;

    }

    public int getSelectionLimit() {
        return selectionLimit;
    }

    /**
     * Limit the number of images that can be selected. By default the user can
     * select infinite number of images.
     *
     * @param selectionLimit
     */
    public void setSelectionLimit(int selectionLimit) {
        this.selectionLimit = selectionLimit;

    }

    public int getSelectionMin() {
        return selectionMin;
    }

    public void setSelectionMin(int selectionmin) {
        selectionMin = selectionmin;

    }


    public int getCameraBtnImage() {
        return cameraBtnImage;
    }

    public void setCameraBtnImage(@DrawableRes int drawableRes) {
        if (drawableRes <= 0) throw new IllegalArgumentException("Invalid value for cameraBtnImage");
        this.cameraBtnImage = drawableRes;
    }

    public int getCameraBtnBackground() {
        return cameraBtnBackground;
    }

    @SuppressLint("ResourceType")
    public void setCameraBtnBackground(@DrawableRes int drawableRes) {
        if (drawableRes <= 0) throw new IllegalArgumentException("Invalid value for cameraBtnBackground");
        this.cameraBtnBackground = drawableRes;
    }


    public int getSelectedCloseImage() {
        return selectedCloseImage;
    }

    @SuppressLint("ResourceType")
    public void setSelectedCloseImage(@DrawableRes int drawableRes) {
        if (drawableRes <= 0) throw new IllegalArgumentException("Invalid value for selectedCloseImage");

        this.selectedCloseImage = drawableRes;
    }


    public boolean isFlashOn(){
        return flashOn;
    }

    public void setFlashOn(boolean flashOn){
        this.flashOn = flashOn;
    }
}

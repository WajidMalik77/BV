package com.background.video.recorder.camera.recorder.PhotoEditor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ColorOverlayViewModel extends ViewModel {
    private MutableLiveData<Integer> colorValue = new MutableLiveData<>();

    public LiveData<Integer> getColorValue() {
        return colorValue;
    }

    public void setColorValue(int color) {
        colorValue.setValue(color);
    }
}

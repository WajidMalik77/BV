package com.background.video.recorder.camera.recorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> startRecording = new MutableLiveData<>();

    public void triggerRecording() {
        startRecording.setValue(true);
    }

    public LiveData<Boolean> getStartRecording() {
        return startRecording;
    }

    public void resetTrigger() {
        startRecording.setValue(false);
    }
}

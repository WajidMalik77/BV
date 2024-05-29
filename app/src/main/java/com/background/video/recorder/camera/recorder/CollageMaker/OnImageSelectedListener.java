package com.background.video.recorder.camera.recorder.CollageMaker;

import android.net.Uri;

public interface OnImageSelectedListener {
    void onImageSelected(Uri imageUri);
    void onImageUnselected(Uri imageUri);
}

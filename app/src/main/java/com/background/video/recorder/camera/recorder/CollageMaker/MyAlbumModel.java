package com.background.video.recorder.camera.recorder.CollageMaker;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

public class MyAlbumModel extends ViewModel {
    Uri Uri;
    //MutableLiveData<Uri> mutableLiveData=new MutableLiveData<>();
    public MyAlbumModel() {
    }

    public Uri getUri() {
        return Uri;
    }
    public void setUri(Uri uri) {
        Uri = uri;
    }

}

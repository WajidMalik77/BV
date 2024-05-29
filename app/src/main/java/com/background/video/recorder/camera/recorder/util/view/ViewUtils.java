package com.background.video.recorder.camera.recorder.util.view;


import android.view.View;

public class ViewUtils {

    public static void visible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static void invisible(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    public static void gone(View view) {
        view.setVisibility(View.GONE);
    }
}

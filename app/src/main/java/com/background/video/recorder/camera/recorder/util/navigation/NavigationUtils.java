package com.background.video.recorder.camera.recorder.util.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.navigation.Navigation;

import com.background.video.recorder.camera.recorder.R;

public class NavigationUtils {

    public static void navigate(Activity activity, @IdRes int actionId, Bundle bundle) {
        Navigation.findNavController(activity, R.id.fragmentContainerViewHome).navigate(actionId, bundle);
    }

    public static void navigate(Activity activity, @IdRes int actionId) {
        Navigation.findNavController(activity, R.id.fragmentContainerViewHome).navigate(actionId);
    }

    public static void navigateBack(View view) {
        Navigation.findNavController(view).popBackStack();
    }


}


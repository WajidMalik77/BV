package com.background.video.recorder.camera.recorder.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.application.MyApp;


public class PreSplashFragment extends Fragment {

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable navigateRunnable = new Runnable() {
        @Override
        public void run() {
            navigateToSplashFragment();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApp.Companion.disableshouldshowappopenad();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        MyApp.Companion.enableshouldshowappopenad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pre_splash, container, false);

        handler.postDelayed(navigateRunnable, 3000);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(navigateRunnable); // Cancel the delayed navigation
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        handler.postDelayed(navigateRunnable, 3000);
//    }

    private void navigateToSplashFragment() {

        if (isAdded()) {
            // Navigate to the splash fragment
            Navigation.findNavController(requireActivity(), R.id.navContainerSplash).navigate(R.id.action_preSplashFragment_to_splashTwoFragment);
        }

    }


}
package com.background.video.recorder.camera.recorder.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.background.video.recorder.camera.recorder.R;

public class SwitcherActivity extends AppCompatActivity {

    private ImageView imageView1, imageView2;
    private Handler handler = new Handler();
    private int currentImageIndex = 0;
    private int[] images = {R.drawable.image1, R.drawable.image2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switcher);

        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);

        imageView1.setImageResource(images[0]);
        imageView2.setImageResource(images[1]);

        startAnimation();
    }

    private void startAnimation() {
        Runnable animationRunnable = new Runnable() {
            @Override
            public void run() {
                ImageView currentImageView = (currentImageIndex % 2 == 0) ? imageView1 : imageView2;
                ImageView nextImageView = (currentImageIndex % 2 == 0) ? imageView2 : imageView1;

                float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(currentImageView, "alpha", 1.0f, 0.0f);
                ObjectAnimator slideOut = ObjectAnimator.ofFloat(currentImageView, "translationX", 0, screenWidth);
                fadeOut.setDuration(1000);
                slideOut.setDuration(1000);

                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(nextImageView, "alpha", 0.0f, 1.0f);
                ObjectAnimator slideIn = ObjectAnimator.ofFloat(nextImageView, "translationX", -screenWidth, 0);
                fadeIn.setDuration(1000);
                slideIn.setDuration(1000);

                AnimatorSet setOut = new AnimatorSet();
                setOut.playTogether(fadeOut, slideOut);
                AnimatorSet setIn = new AnimatorSet();
                setIn.playTogether(fadeIn, slideIn);

                setOut.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        currentImageIndex++;
                        setIn.start();
                    }
                });

                setOut.start();
            }
        };

        handler.post(animationRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}

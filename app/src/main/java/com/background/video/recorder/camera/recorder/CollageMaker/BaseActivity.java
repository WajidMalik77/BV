package com.background.video.recorder.camera.recorder.CollageMaker;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.background.video.recorder.camera.recorder.R;
import com.google.android.gms.ads.interstitial.InterstitialAd;

public class BaseActivity extends AppCompatActivity {
    //Ads
    protected InterstitialAd mInterstitialAd;
    protected String TAG = "interstitialAd";
    protected Context context;
    protected AppCompatActivity activity;
    protected ImageView removeAd;
    String ctgryname;

    protected static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof AppCompatActivity) {
            final AppCompatActivity activity = (AppCompatActivity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }


    protected void setCustomActionBar(String activityName, String removeAdseventname) {
        final ActionBar abar = getSupportActionBar();
        ctgryname = activityName;
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        TextView title = viewActionBar.
                findViewById(R.id.tvTitle);
        title.setText(activityName);
        try {
            Typeface aerialFont = Typeface.createFromAsset(getAssets(),
                    "fonts/arial.ttf");
            title.setTypeface(aerialFont);
        } catch (Exception e) {

        }
        abar.setDisplayShowTitleEnabled(false);
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayHomeAsUpEnabled(true);


    }

    //endregion
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            try {
                onBackPressed();
            } catch (IllegalStateException e) {
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion
    protected void show(View view) {
        if (view != null && !view.isShown())
            view.setVisibility(View.VISIBLE);
    }

    protected void hide(View view) {
        if (view != null && view.isShown())
            view.setVisibility(View.GONE);
    }

}

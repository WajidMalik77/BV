package com.background.video.recorder.camera.recorder.CollageMaker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.AdMobBanner;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.databinding.ActivityMyAlbumNewBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.ui.activitiy.HomeActivity;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyAlbumNewActivity extends AppCompatActivity implements recyclerViewInterfaces {

    private File[] files;
    private ArrayList<String> list;
    private ConstraintLayout iv_back;
    private RecyclerView recyclerView;
    private LinearLayout tvNothing;
    private LinearLayout bannerLayout;
    private TextView tv;
    private MyAlbumAdapter adapter;
    private boolean empty = false;
    private MyAlbumModel getCupy;
    private List<MyAlbumModel> arrayListData = new ArrayList<>();
    ActivityMyAlbumNewBinding binding;
    private boolean album_native_admob = true;
    private boolean album_native_facebook = true;
    private boolean home_admob_home_inter = true;
    private boolean overlay_permission_admob_native = true;
    private boolean overlay_permission_facebook_native = true;
    Activity activity;
    private SharedPrefsHelper prefs = null;
    private String admob_native_id = "";
    private String facebook_native_ad_id = "";
    private String admob_interstitial_home_id = "";
    private boolean facebook_banner_enable = true;
    private boolean admob_banner_enable = true;
    private String admob_banner_id = "";
    private String facebook_banner_ad_id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyAlbumNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = SharedPrefsHelper.getInstance(this);
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {
            album_native_admob = localPrefs.getalbum_native_admobSwitch();
            album_native_facebook = localPrefs.getdashboard_native_facebookSwitch();
            overlay_permission_admob_native = localPrefs.getoverlay_permission_admob_nativeSwitch();
            overlay_permission_facebook_native = localPrefs.getoverlay_permission_facebook_nativeSwitch();
            admob_native_id = localPrefs.getadmob_native_idId();
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId();
            home_admob_home_inter = localPrefs.gethome_admob_home_interSwitch();
            admob_interstitial_home_id = localPrefs.getadmob_interstitial_home_idId();


            facebook_banner_enable = localPrefs.getfacebook_banner_enableSwitch();
            admob_banner_enable = localPrefs.getadmob_banner_enableSwitch();
            admob_banner_id = localPrefs.getadmob_banner_idId();
            facebook_banner_ad_id = localPrefs.getfacebook_banner_ad_idId();


        }

        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.nativeAd.setVisibility(View.GONE);
        }  else if (album_native_admob) {
                InterstitialAdUtils.INSTANCE.loadMediationNative(
                        this,
                        binding.nativeAd,
                        admob_native_id,
                        binding.nativeAdContainer,
                        R.layout.ad_native_layout_modified
                );

        } else if (album_native_facebook) {
                InterstitialAdUtils.INSTANCE.loadNativeFacebookAd(
                        facebook_native_ad_id,
                        this,
                        binding.nativeAdContainer
                );

            } else {
                Log.d("checksswitcehs", "onViewCreated: both are off");
                binding.nativeAd.setVisibility(View.GONE);

        }

        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.bannerAd.setVisibility(View.GONE);
        }  else  if (admob_banner_enable) {
            Log.e("bannercheckit", "Banner: admob_banner_enable loaded ");
            AdMobBanner.INSTANCE.loadAd(this, binding.bannerAd, admob_banner_id, facebook_banner_ad_id);
        } else if (facebook_banner_enable) {
            Log.e("bannercheckit", "Banner: facebook_banner_enable loaded ");
            AdMobBanner.INSTANCE.loadAdFifty(binding.bannerAd, this, facebook_banner_ad_id);
        } else {
            binding.bannerAd.setVisibility(View.GONE);
        }
        list = new ArrayList<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            // Background work here
            handler.post(() -> {
                for (int i = 0; i < getData().size(); i++) {
                    list.add(getData().get(i).getUri().toString());
                }
                isEmpty();
            });
        });

        init();
        getData();

        iv_back.setOnClickListener(view -> {
//            Intent intentToDrawerActivity = new Intent(MyAlbumNewActivity.this, HomeActivity.class);
//            intentToDrawerActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intentToDrawerActivity.putExtra("loadFragment1", true); // A boolean extra
//            startActivity(intentToDrawerActivity);

            onBackPressed();
//            finish();
        });
    }


    private void init() {
        iv_back = findViewById(R.id.iv_back_share_activity);
        recyclerView = findViewById(R.id.recyclerViewFont);
        tvNothing = findViewById(R.id.tvNothing);
    }

    private void isEmpty() {
        if (list.size() == 0) {
            tvNothing.setVisibility(View.VISIBLE);
        } else {
            tvNothing.setVisibility(View.GONE);
        }

        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                File file1 = new File(Uri.parse(lhs).getPath());
                File file2 = new File(Uri.parse(rhs).getPath());
                return Long.compare(file2.lastModified(), file1.lastModified());
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MyAlbumAdapter(MyAlbumNewActivity.this, list, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private List<MyAlbumModel> getData() {
        List<MyAlbumModel> arrayList = new ArrayList<>();

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + getResources().getString(R.string.app_name));

        try {
            files = file.listFiles();
            if (files != null) {
                // Sort the files based on last modified date in descending order
                Arrays.sort(files, (file1, file2) -> Long.compare(file2.lastModified(), file1.lastModified()));
                for (File file1 : files) {
                    MyAlbumModel getCupy = new MyAlbumModel();
                    getCupy.setUri(Uri.fromFile(file1));
                    arrayList.add(getCupy);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return arrayList;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intentToDrawerActivity = new Intent(MyAlbumNewActivity.this, ShareActivity.class);
        intentToDrawerActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentToDrawerActivity.putExtra("loadFragment1", true); // A boolean extra
        startActivity(intentToDrawerActivity);
        finish();
    }

    @Override
    public void onClick(int position) {
        if (position < list.size() && position >= 0) {
            String getCupy = list.get(position);
            if (getCupy != null) {
                Intent intentToImageViewActivity = new Intent(MyAlbumNewActivity.this, imageViewActivity.class);
                intentToImageViewActivity.putExtra("imageViewZoom",getCupy);
                Log.d("imageViewZoom", "onClick: " + getCupy);
                intentToImageViewActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentToImageViewActivity);
            }
        } else {
            Toast.makeText(this, " please wait", Toast.LENGTH_SHORT).show();
        }
    }

}


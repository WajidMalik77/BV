package com.background.video.recorder.camera.recorder.PhotoEditor.sticker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.AdMobBanner;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.databinding.ActivityStickerBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

public class StickerActivity extends AppCompatActivity {
    int stickerPosition = 0;
    int[] stickerList;

    ImageView closeEmojiBtn, emojiDoneBtn;
    AdView mAdView;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView mainHeading;

    private String banner_id;

    private boolean banner_bg;
    private SharedPreferences sharedPrefs;
    private boolean remove_password_admob_native = true;
    private boolean remove_password_facebook_native = true;
    private boolean facebook_banner_enable = true;
    private boolean admob_banner_enable = true;
    private String facebook_native_ad_id = "";
    private String admob_native_id = "";
    private SharedPrefsHelper prefs;
    private String admob_banner_id = "";
    private String facebook_banner_ad_id = "";

    ActivityStickerBinding binding;
    private boolean sticker_admob_native = true;
    private boolean sticker_facebook_native = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStickerBinding.inflate(getLayoutInflater());


        setContentView(binding.getRoot());
        RelativeLayout rvbannerlayout = findViewById(R.id.bannerAd);

        closeEmojiBtn = findViewById(R.id.closeEmojiBtn);
        emojiDoneBtn = findViewById(R.id.emojiDoneBtn);
        prefs = SharedPrefsHelper.getInstance(this);
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {
            facebook_banner_enable = localPrefs.getfacebook_banner_enableSwitch();
            admob_banner_enable = localPrefs.getadmob_banner_enableSwitch();
            sticker_admob_native = localPrefs.getsticker_admob_nativeSwitch();
            sticker_facebook_native = localPrefs.getsticker_facebook_nativeSwitch();
            admob_banner_id = localPrefs.getadmob_banner_idId();
            facebook_banner_ad_id = localPrefs.getfacebook_banner_ad_idId();
            admob_native_id = localPrefs.getadmob_native_idId();
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId();
            Log.d("TAG", "onCreate:  admob_interstitial_splash_id  " + remove_password_facebook_native);
            Log.d("", "onCreate:  facebook_interstitial_splash_id  " + remove_password_facebook_native);
        }

        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.bannerAd.setVisibility(View.GONE);
        }  else
        if (admob_banner_enable) {
            Log.e("bannercheckit", "Banner: admob_banner_enable loaded ");
            AdMobBanner.INSTANCE.loadAd(this, rvbannerlayout, admob_banner_id, facebook_banner_ad_id);
        } else if (facebook_banner_enable) {
            Log.e("bannercheckit", "Banner: facebook_banner_enable loaded ");
            AdMobBanner.INSTANCE.loadAdFifty(rvbannerlayout, this, facebook_banner_ad_id);
        } else {
            rvbannerlayout.setVisibility(View.GONE);
        }


        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.nativeAd.setVisibility(View.GONE);
        }  else if (sticker_admob_native) {
            InterstitialAdUtils.INSTANCE.loadMediationNative(
                    this,
                    binding.nativeAd,
                    admob_native_id,
                    binding.nativeAdContainer,
                    R.layout.ad_native_layout_modified
            );

        } else if (sticker_facebook_native) {
            InterstitialAdUtils.INSTANCE.loadNativeFacebookAd(
                    facebook_native_ad_id,
                    this,
                    binding.nativeAdContainer
            );

        } else {
            Log.d("checksswitcehs", "onViewCreated: both are off");
            binding.nativeAd.setVisibility(View.GONE);
        }

        RecyclerView rvEmoji = findViewById(R.id.rvEmoji);

        stickerList = new int[]{
                R.drawable.st_1, R.drawable.st_2, R.drawable.st_3, R.drawable.st_4, R.drawable.st_5,
                R.drawable.st_6, R.drawable.st_7, R.drawable.st_8, R.drawable.st_9, R.drawable.st_10,
                R.drawable.st_11, R.drawable.st_21, R.drawable.st_31,
                R.drawable.st_12, R.drawable.st_22, R.drawable.st_32,
                R.drawable.st_13, R.drawable.st_23, R.drawable.st_33,
                R.drawable.st_14, R.drawable.st_24, R.drawable.st_34,
                R.drawable.st_15, R.drawable.st_25, R.drawable.st_35,
                R.drawable.st_16, R.drawable.st_26, R.drawable.st_36,
                R.drawable.st_17, R.drawable.st_27, R.drawable.st_37,
                R.drawable.st_18, R.drawable.st_28, R.drawable.st_38,
                R.drawable.st_19, R.drawable.st_29, R.drawable.st_39,
                R.drawable.st_20, R.drawable.st_30, R.drawable.st_40,

        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvEmoji.setLayoutManager(gridLayoutManager);
        StickerAdapter stickerAdapter = new StickerAdapter();
        rvEmoji.setAdapter(stickerAdapter);


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Add Sticker");


        emojiDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("stikcerSticker", stickerList[stickerPosition]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        closeEmojiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
        int row_index = 0;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sticker_activity, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Picasso.get().load((stickerList[position])).into(holder.imgSticker);
//            holder.imgSticker.setImageResource(stickerList[position]);
            holder.itemView.setOnClickListener(v -> {
                stickerPosition = position;
                row_index = position;
//                item.setVisible(true);
                notifyDataSetChanged();
            });
            if (row_index == position) {
                holder.imgSticker.setBackgroundColor(Color.parseColor("#567845"));
            } else {
                holder.imgSticker.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }

        @Override
        public int getItemCount() {
            return stickerList.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgSticker;

            ViewHolder(View itemView) {
                super(itemView);
                imgSticker = itemView.findViewById(R.id.imgSticker);

                itemView.setOnClickListener(v -> {


                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
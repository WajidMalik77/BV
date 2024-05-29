package com.background.video.recorder.camera.recorder.PhotoEditor.emoji;


import android.app.Activity;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.AdMobBanner;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.databinding.ActivityEmojiBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

public class EmojiActivity extends AppCompatActivity {
    //    MenuItem item;
    int stickerPosition = 0;
    int[] stickerList;
//    AdsActivity adsActivity;

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

    ActivityEmojiBinding binding;


    private boolean emoji_admob_native = true;
    private boolean emoji_facebook_native = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEmojiBinding.inflate(getLayoutInflater());


        setContentView(binding.getRoot());

        closeEmojiBtn = findViewById(R.id.closeEmojiBtn);
        emojiDoneBtn = findViewById(R.id.emojiDoneBtn);

        RelativeLayout rvbannerlayout = findViewById(R.id.bannerAd);
        prefs = SharedPrefsHelper.getInstance(this);
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {
            facebook_banner_enable = localPrefs.getfacebook_banner_enableSwitch();
            admob_banner_enable = localPrefs.getadmob_banner_enableSwitch();
            emoji_admob_native = localPrefs.getemoji_admob_nativeSwitch();
            emoji_facebook_native = localPrefs.getemoji_facebook_nativeSwitch();
            admob_banner_id = localPrefs.getadmob_banner_idId();
            admob_native_id = localPrefs.getadmob_native_idId();
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId();
            facebook_banner_ad_id = localPrefs.getfacebook_banner_ad_idId();
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
        }  else  if (emoji_admob_native) {
                InterstitialAdUtils.INSTANCE.loadMediationNative(
                        this,
                        binding.nativeAd,
                        admob_native_id,
                        binding.nativeAdContainer,
                        R.layout.ad_native_layout_modified
                );

        } else if (emoji_facebook_native) {
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

        stickerList = new int[]{R.drawable.sticker1, R.drawable.sticker2, R.drawable.sticker3, R.drawable.sticker4, R.drawable.sticker5, R.drawable.sticker6,
                R.drawable.sticker7, R.drawable.sticker8, R.drawable.sticker9, R.drawable.sticker10, R.drawable.sticker11, R.drawable.sticker12, R.drawable.sticker13,
                R.drawable.sticker14, R.drawable.sticker15, R.drawable.sticker16, R.drawable.sticker17, R.drawable.sticker18, R.drawable.sticker19, R.drawable.sticker20,
                R.drawable.sticker21, R.drawable.sticker22, R.drawable.sticker23, R.drawable.sticker24, R.drawable.sticker25, R.drawable.sticker26, R.drawable.sticker27,
                R.drawable.sticker28, R.drawable.sticker29, R.drawable.sticker30, R.drawable.sticker31, R.drawable.sticker32, R.drawable.sticker33, R.drawable.sticker34,
                R.drawable.sticker35, R.drawable.sticker36, R.drawable.sticker37, R.drawable.sticker38, R.drawable.sticker39, R.drawable.sticker40, R.drawable.sticker41,
                R.drawable.sticker42, R.drawable.sticker43, R.drawable.sticker44, R.drawable.sticker45, R.drawable.sticker46, R.drawable.sticker47, R.drawable.sticker48,
                R.drawable.sticker49, R.drawable.sticker50, R.drawable.sticker51, R.drawable.sticker52, R.drawable.sticker53, R.drawable.sticker54, R.drawable.sticker55,
                R.drawable.sticker56, R.drawable.sticker57, R.drawable.sticker58, R.drawable.sticker59, R.drawable.sticker60, R.drawable.sticker61, R.drawable.sticker62,
                R.drawable.sticker63, R.drawable.sticker64, R.drawable.sticker65, R.drawable.sticker66, R.drawable.sticker67, R.drawable.sticker68, R.drawable.sticker69,
                R.drawable.sticker70, R.drawable.sticker71, R.drawable.sticker72, R.drawable.sticker73, R.drawable.sticker74, R.drawable.sticker75, R.drawable.sticker76,
                R.drawable.sticker77, R.drawable.sticker78, R.drawable.sticker79, R.drawable.sticker80, R.drawable.sticker81, R.drawable.sticker82,
                R.drawable.sticker83, R.drawable.sticker84, R.drawable.sticker85, R.drawable.sticker86, R.drawable.sticker87,
        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rvEmoji.setLayoutManager(gridLayoutManager);
        StickerAdapter stickerAdapter = new StickerAdapter();
        rvEmoji.setAdapter(stickerAdapter);

        emojiDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("emojiSticker", stickerList[stickerPosition]);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_emoji_activity, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

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
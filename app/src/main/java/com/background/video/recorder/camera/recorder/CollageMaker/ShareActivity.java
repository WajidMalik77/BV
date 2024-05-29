package com.background.video.recorder.camera.recorder.CollageMaker;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.AdMobBanner;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.databinding.ActivityShareNewBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.ui.activitiy.HomeActivity;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;

import java.io.File;

public class ShareActivity extends BaseActivity implements View.OnClickListener {


    ConstraintLayout shareIntentBtn, Albumbtn;
    boolean stop = false;
    private LinearLayout facebook, whatsapp, twitter, more;
    private ImageView imageView, homeBtnImg;
    private String imagePathShare = "";
    private String imagePathSharetwo = "";
    private String imagePathSharegreaterthan10 = "";
    private long mLastClickTime = 0;

    Intent intentToAlbum;
    ProgressDialog progressDialog;
    String a;
    Bitmap bmp;
    Uri imageUri;
    boolean native_bg;
    SharedPreferences sharedPrefs;

    ActivityShareNewBinding binding;
    private boolean sahre_activity_admob_native = true;
    private boolean sahre_activity_facebook_native = true;
    private boolean sahre_activity_album_admob_interstitial = true;
    private String facebook_native_ad_id = "";
    private String admob_native_id = "";
    private SharedPrefsHelper prefs = null;

    boolean homeInter;
    private String admob_interstitial_home_id = "";
    private boolean facebook_banner_enable = true;
    private boolean admob_banner_enable = true;
    private String admob_banner_id = "";
    private String facebook_banner_ad_id = "";
    private boolean sahre_activity_admob_inter = true;
    private boolean sahre_activity_admob_home = true;
    private boolean sahre_activity_admob_album = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityShareNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefs = SharedPrefsHelper.getInstance(this);
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {
            sahre_activity_admob_native = localPrefs.getsahre_activity_admob_nativeSwitch();
            sahre_activity_admob_inter = localPrefs.getsahre_activity_admob_interSwitch();
            sahre_activity_admob_album = localPrefs.getsahre_activity_admob_albumSwitch();
            sahre_activity_admob_home = localPrefs.getsahre_activity_admob_homeSwitch();
            sahre_activity_facebook_native = localPrefs.getsahre_activity_facebook_nativeSwitch();
            sahre_activity_album_admob_interstitial = localPrefs.getsahre_activity_album_admob_interstitialSwitch();
            admob_native_id = localPrefs.getadmob_native_idId();
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId();
            admob_interstitial_home_id = localPrefs.getadmob_interstitial_home_idId();
            facebook_banner_enable = localPrefs.getfacebook_banner_enableSwitch();
            admob_banner_enable = localPrefs.getadmob_banner_enableSwitch();
            admob_banner_id = localPrefs.getadmob_banner_idId();
            facebook_banner_ad_id = localPrefs.getfacebook_banner_ad_idId();
            Log.d(TAG, "onCreate:  admob_interstitial_splash_id  " + sahre_activity_admob_native);
            Log.d(TAG, "onCreate:  facebook_interstitial_splash_id  " + sahre_activity_admob_native);
        }

        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.bannerAd.setVisibility(View.GONE);
        }  else
        if (admob_banner_enable) {
            Log.e("bannercheckit", "Banner: admob_banner_enable loaded ");
            AdMobBanner.INSTANCE.loadAd(this, binding.bannerAd, admob_banner_id, facebook_banner_ad_id);
        } else if (facebook_banner_enable) {
            Log.e("bannercheckit", "Banner: facebook_banner_enable loaded ");
            AdMobBanner.INSTANCE.loadAdFifty(binding.bannerAd, this, facebook_banner_ad_id);
        } else {
            binding.bannerAd.setVisibility(View.GONE);
        }



        sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        native_bg = sharedPrefs.getBoolean("native_bg", false);
        homeInter = sharedPrefs.getBoolean("home_inter", false);


        FrameLayout nativeAd = findViewById(R.id.nativeAdshareActivity);




        Log.d("ShareActivityTAG", "onCreate:  sahre_activity_album_admob_interstitial  " + sahre_activity_album_admob_interstitial);
        if (sahre_activity_album_admob_interstitial) {
            Log.d("ShareActivityTAG", "onCreate:  sahre_activity_album_admob_interstitial  " + sahre_activity_album_admob_interstitial);
//            InterstitialAdUtils.INSTANCE.loadHomeInterstitial(this, admob_interstitial_home_id);

        }
        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.nativeAdshareActivity.setVisibility(View.INVISIBLE);
        }  else if (sahre_activity_admob_native) {
            InterstitialAdUtils.INSTANCE.loadMediationNative(
                    this,
                    binding.nativeAdshareActivity,
                    admob_native_id,
                    binding.nativeAdContainer,
                    R.layout.ad_native_layout_saveshare
            );

        } else if (sahre_activity_facebook_native) {
            // binding.nativeAd.setVisibility(View.GONE);

            InterstitialAdUtils.INSTANCE.loadNativeFacebookAd(
                    facebook_native_ad_id,
                    this,
                    binding.nativeAdContainer
            );

        } else {
            Log.d("checksswitcehs", "onViewCreated: both are off");
            binding.nativeAdshareActivity.setVisibility(View.GONE);
        }


        if (homeInter) {
//            Toast.makeText(getActivity(), "home is on", Toast.LENGTH_SHORT).show();
//            InterstitialAdUtils.INSTANCE.loadHomeInterstitial(this);
        } else {
//                            Toast.makeText(getActivity(), "hoem is false", Toast.LENGTH_SHORT).show();
        }

        intentToAlbum = new Intent(ShareActivity.this, MyAlbumNewActivity.class);
        Albumbtn = findViewById(R.id.albumButton_new);
        shareIntentBtn = findViewById(R.id.shareIntentBtn);
        imageView = findViewById(R.id.savedImg);
        homeBtnImg = findViewById(R.id.home_btn_main);
        homeBtnImg.setOnClickListener(this);
        Albumbtn.setOnClickListener(this);
        shareIntentBtn.setOnClickListener(this);

        a = getIntent().getStringExtra("conditionFromActivity");
        if (a != null && a.equals("MirrorActivity")) {
            String imageUriString = getIntent().getStringExtra("imageUri");
            imageUri = Uri.parse(imageUriString);
            if (isValidContextForGlide(this)) {
                Glide.with(this)
                        .load(imageUri)
                        .into(imageView);
            }
        } else if (a != null && a.equals("ProcessActivity")) {
            imagePathShare = getIntent().getStringExtra("uriToShareActivity");
            Log.d("imagePathShareUri", "onCreate: " + imagePathShare);
            if (isValidContextForGlide(this)) {
                Glide.with(this)
                        .load(imagePathShare)
                        .into(imageView);
            }
        } else if (a != null && a.equals("EditImageActivitySecond")) {
            byte[] byteArray = getIntent().getByteArrayExtra("bitmapCamera");
            Log.d("intent_image_new_SHARE", "onCreate: " + byteArray);
            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            if (isValidContextForGlide(this)) {
                Glide.with(this)
                        .load(bmp)
                        .into(imageView);
            }
        } else if (a != null && a.equals("EditImageActivity")) {
            imagePathSharegreaterthan10 = getIntent().getStringExtra("uriToShareActivity");
            Log.d("IntentToShareURI", "onCreate: " + imagePathSharegreaterthan10);
            if (isValidContextForGlide(this)) {
                Glide.with(this)
                        .load(imagePathSharegreaterthan10)
                        .into(imageView);
            }
        } else if (a != null && a.equals("SaveImageWorker")) {
            imagePathSharegreaterthan10 = getIntent().getStringExtra("SaveImageWorker");
            Log.d("IntentToShareURI", "onCreate: " + imagePathSharegreaterthan10);
            if (isValidContextForGlide(this)) {
                Glide.with(this)
                        .load(imagePathSharegreaterthan10)
                        .into(imageView);
            }
        }

        Log.e("imagePath", "" + imagePathShare);


        savePrefForRateus_Dialog(getPrefForRateus_Dialog("dialog_rateus") + 1);

        Albumbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(ShareActivity.this);
                        progressDialog.setMessage("Loading..."); // Setting Message
                        progressDialog.setTitle("Please wait while loading"); // Setting Title
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                        progressDialog.show(); // Display Progress Dialog
                        progressDialog.setCancelable(false);
                    }
                });



                intentToAlbum.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                if (sahre_activity_admob_inter) {
                    startActivityNext(intentToAlbum);
                } else {
                    startActivity(new Intent(intentToAlbum));
                }
                finish();
            }
        });
    }

    public void startActivityNext(Intent intent) {
        if (InterstitialAdUtils.INSTANCE.getAdMobHomeInterstitialAd() != null) {
            InterstitialAdUtils.INSTANCE.getAdMobHomeInterstitialAd().setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
//
//                    if (homeInter) {
////                        Toast.makeText(getActivity(), "home is on", Toast.LENGTH_SHORT).show();
//                        InterstitialAdUtils.INSTANCE.loadHomeInterstitial(ShareActivity.this);
//                    } else {
////                            Toast.makeText(getActivity(), "hoem is false", Toast.LENGTH_SHORT).show();
//                    }

                    startActivity(intent);
//                    AdsHelper_new.isInterstitialShowing = true;
                    // AdsHelper.loadInterstitialAd(requireActivity());

                    InterstitialAdUtils.INSTANCE.setAdMobHomeInterstitialAd(null);

                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    InterstitialAdUtils.INSTANCE.setAdMobHomeInterstitialAd(null);
//                    AdsHelper_new.isInterstitialShowing = false;
//                    startFragment();
                    Toast.makeText(ShareActivity.this, ""+adError.getMessage() , Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    // AdsHelper.loadInterstitialAd(requireActivity());
                }

            });

            InterstitialAdUtils.INSTANCE.getAdMobHomeInterstitialAd().show(ShareActivity.this);
//            AdsHelper_new.isShowingInterstitial = true;
        } else {
            startActivity(intent);
//            startFragment();
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {


            case R.id.shareIntentBtn:
                if (a.equals("ProcessActivity")) {
                    shareSingle(Uri.parse(imagePathShare));
                } else if (a.equals("MirrorActivity")) {
                    shareSinglenew(imageUri);
                } else if (a.equals("EditImageActivitySecond")) {
                    shareSingle(Uri.parse(String.valueOf(bmp)));
                } else if (a.equals("EditImageActivity")) {
                    shareSingle(Uri.parse(imagePathSharegreaterthan10));
                }
                break;


            case R.id.home_btn_main:


                Intent intentToPCollageMaker = new Intent(ShareActivity.this, HomeActivity.class);
//                startActivityNext(intentToPCollageMaker);

                if (sahre_activity_admob_home) {

                    startActivityNext(intentToPCollageMaker);
                } else {
                    startActivity(new Intent(intentToPCollageMaker));
                }


//                startActivity(intentToPCollageMaker);
//                finish();
                break;
            default:
        }
    }

    public void shareSingle(Uri file) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        Uri uriForFile = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(file.getPath()));
        intent.putExtra(Intent.EXTRA_STREAM, uriForFile);
        startActivity(Intent.createChooser(intent, "Sending single attachment"));

    }

    public void shareSinglenew(Uri fileUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, "");

        // Use the Uri directly without converting it to a File
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        startActivity(Intent.createChooser(intent, "Sending single attachment"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentToMain = new Intent(ShareActivity.this, HomeActivity.class);
        startActivity(intentToMain);
        finish();
    }

    private void callBack(String title) {
        if (title.equals("onBackPressed"))
            finish();
        else {
            Intent intent = new Intent(ShareActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    ///////////////////////Shared Preferences
    private void savePrefForRateus_Dialog(int value) {
        SharedPreferences preferences = getSharedPreferences("rateus_dialogPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("dialog_rateus", value);
        editor.commit();
    }

    private int getPrefForRateus_Dialog(String key) {
        SharedPreferences preferences = getSharedPreferences("rateus_dialogPref", MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }

    public void onClose(View view) {
        finish();
    }

    @Override
    protected void onPause() {

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        Log.d("pausechk", "onPause: ");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
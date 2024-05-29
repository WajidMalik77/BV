package com.background.video.recorder.camera.recorder.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.ActionOnAdClosedListener;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.ads.InterstitialClass;
import com.background.video.recorder.camera.recorder.databinding.FragmentShareBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;
import com.bumptech.glide.Glide;

import java.io.File;


public class ShareFragment extends Fragment implements View.OnClickListener {

    ConstraintLayout shareIntentBtn, Albumbtn;
    boolean stop = false;
    private LinearLayout facebook, whatsapp, twitter, more;
    private ImageView imageView, homeBtnImg;
    private String imagePathShare = "";
    private String imagePathSharetwo = "";
    private String imagePathSharegreaterthan10 = "";
    private long mLastClickTime = 0;
    ProgressDialog progressDialog;
    String a;
    Bitmap bmp;
    Uri imageUri;
    private AppCompatButton homeBtn;
    private ImageView back;
    private boolean sahre_activity_admob_native = true;
    private boolean sahre_activity_admob_inter = true;
    private boolean sahre_activity_facebook_native = true;
    private boolean sahre_activity_album_admob_interstitial = true;
    private String facebook_native_ad_id = "";
    private String admob_native_id = "";
    private SharedPrefsHelper prefs = null;
    NavController navController;
    boolean homeInter;
    private String admob_interstitial_home_id = "";

    @Override
    public void onResume() {
        super.onResume();
        if (homeBtn == null) {
            homeBtn = requireActivity().findViewById(R.id.homeBtn);
            homeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    NavController navController = NavHostFragment.findNavController(ShareFragment.this);
//                    navController.navigate(R.id.action_shareFragment_to_photoEditorCollageMakerFragment);

                    if (sahre_activity_admob_inter) {
                        InterstitialClass.requestInterstitial(getActivity(), photoeditorcollagemaker_home_admob_inter_id, new ActionOnAdClosedListener() {
                            @Override
                            public void ActionAfterAd() {

                                NavigationUtils.navigate(getActivity(), R.id.homeFragment);
                            }
                        });

                    } else {
                        NavigationUtils.navigate(getActivity(), R.id.homeFragment);

                    }
                }
            });
        }
    }

    private String photoeditorcollagemaker_home_admob_inter_id = "";
    private boolean photoeditorcollagemaker_home_admob_inter = true;
    FragmentShareBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentShareBinding.inflate(inflater, container, false);
        navController = NavHostFragment.findNavController(ShareFragment.this);
        prefs = SharedPrefsHelper.getInstance(getActivity());
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {
            sahre_activity_admob_native = localPrefs.getsahre_activity_admob_nativeSwitch();
//            sahre_activity_admob_native = true;
            sahre_activity_admob_inter = localPrefs.getsahre_activity_admob_interSwitch();
//            sahre_activity_admob_inter = true;
            sahre_activity_facebook_native = localPrefs.getsahre_activity_facebook_nativeSwitch();
            sahre_activity_album_admob_interstitial = localPrefs.getsahre_activity_album_admob_interstitialSwitch();
            admob_native_id = localPrefs.getadmob_native_idId();
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId();
            admob_interstitial_home_id = localPrefs.getadmob_interstitial_home_idId();
            photoeditorcollagemaker_home_admob_inter = localPrefs.getphotoeditorcollagemaker_home_admob_interSwitch();
            photoeditorcollagemaker_home_admob_inter_id = localPrefs.getphotoeditorcollagemaker_home_admob_inter_idId();
        }


        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.nativeAd.setVisibility(View.GONE);
        } else if (sahre_activity_admob_native) {
            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadMediationNative(
                        activity,
                        binding.nativeAd,
                        admob_native_id,
                        binding.nativeAdContainer,
                        R.layout.ad_native_layout_saveshare
                );
            }
        } else if (sahre_activity_facebook_native) {
            // binding.nativeAd.setVisibility(View.GONE);

            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadNativeFacebookAd(
                        facebook_native_ad_id,
                        activity,
                        binding.nativeAdContainer
                );
            }
        } else {
            Log.d("checksswitcehs", "onViewCreated: both are off");
            binding.nativeAd.setVisibility(View.INVISIBLE);
        }
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Albumbtn = view.findViewById(R.id.albumButton_new);
        shareIntentBtn = view.findViewById(R.id.shareIntentBtn);
        imageView = view.findViewById(R.id.savedImg);
        homeBtnImg = view.findViewById(R.id.home_btn_main);
        homeBtnImg.setOnClickListener(this);
        Albumbtn.setOnClickListener(this);
        shareIntentBtn.setOnClickListener(this);
        Bundle args = getArguments();

        if (args != null) {
            a = args.getString("conditionFromActivity");
            if (a != null && a.equals("MirrorActivity")) {
                String imageUriString = args.getString("imageUri");
                if (imageUriString != null) {
                    imageUri = Uri.parse(imageUriString);
                    Glide.with(getActivity())
                            .load(imageUri)
                            .into(imageView);
                }
            }
        } else {
            Log.e("ShareFragment", "Arguments are null");
        }




        Log.e("imagePath", "" + imagePathShare);


//        savePrefForRateus_Dialog(getPrefForRateus_Dialog("dialog_rateus") + 1);

        Albumbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setMessage("Loading..."); // Setting Message
                        progressDialog.setTitle("Please wait while loading"); // Setting Title
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                        progressDialog.show(); // Display Progress Dialog
                        progressDialog.setCancelable(false);
                    }
                });

                Bundle bundle = new Bundle();

                if (sahre_activity_admob_inter) {
                    InterstitialClass.requestInterstitial(getActivity(), photoeditorcollagemaker_home_admob_inter_id, new ActionOnAdClosedListener() {
                        @Override
                        public void ActionAfterAd() {

                            NavigationUtils.navigate(getActivity(), R.id.albumFragment, bundle);
                        }
                    });

                } else {
                    NavigationUtils.navigate(getActivity(), R.id.albumFragment, bundle);

                }

            }
        });
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

                Bundle bundle = new Bundle();

                if (sahre_activity_admob_inter) {
                    InterstitialClass.requestInterstitial(getActivity(), photoeditorcollagemaker_home_admob_inter_id, new ActionOnAdClosedListener() {
                        @Override
                        public void ActionAfterAd() {

                            NavigationUtils.navigate(getActivity(), R.id.homeFragment, bundle);
                        }
                    });

                } else {
                    NavigationUtils.navigate(getActivity(), R.id.homeFragment, bundle);

                }

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
        Uri uriForFile = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", new File(file.getPath()));
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


    ///////////////////////Shared Preferences
//    private void savePrefForRateus_Dialog(int value) {
//        SharedPreferences preferences = getSharedPreferences("rateus_dialogPref", MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putInt("dialog_rateus", value);
//        editor.commit();
//    }
//
//    private int getPrefForRateus_Dialog(String key) {
//        SharedPreferences preferences = getSharedPreferences("rateus_dialogPref", MODE_PRIVATE);
//        return preferences.getInt(key, 0);
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
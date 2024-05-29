package com.background.video.recorder.camera.recorder.ui.fragment;

import android.app.Activity;
import android.content.Intent;
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

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.databinding.FragmentImageViewBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.bumptech.glide.Glide;

import java.io.File;


public class ImageViewFragment extends Fragment {

    ImageView imgViewZoom;
//    ConstraintLayout iv_back_share_activity;
    Uri path;
    //    TextView details;
    AppCompatButton shareImageBtn, detailsImageBtn;
    FragmentImageViewBinding binding;
    private SharedPrefsHelper prefs = null;
    private boolean imageview_native_admob = true;
    private boolean imageview_native_facebook = true;
    private String admob_native_id = "";
    private String facebook_native_ad_id = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentImageViewBinding.inflate(inflater, container, false);

        prefs = SharedPrefsHelper.getInstance(getActivity());
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {
            imageview_native_admob = localPrefs.getimageview_native_admobSwitch();
//           imageview_native_admob = true;
            imageview_native_facebook = localPrefs.getimageview_native_facebookSwitch();
            admob_native_id = localPrefs.getadmob_native_idId();
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId();
        }

        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.nativeAd.setVisibility(View.GONE);
        }  else if (imageview_native_admob) {
            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadMediationNative(
                        activity,
                        binding.nativeAd,
                        admob_native_id,
                        binding.nativeAdContainer,
                        R.layout.ad_native_layout_collage
                );
            }
        } else if (imageview_native_facebook) {
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
            binding.nativeAd.setVisibility(View.GONE);
        }


        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);





        shareImageBtn = view.findViewById(R.id.shareImageBtnIntent);
        detailsImageBtn = view.findViewById(R.id.detailsImageBtn);
//        iv_back_share_activity = view.findViewById(R.id.iv_back_share_activity);
//        details = findViewById(R.id.details);

        imgViewZoom = view.findViewById(R.id.imgViewZoom);

        String imageViewZoom = getArguments().getString("imageViewZoom", "default_value");

        path = Uri.parse(imageViewZoom);
//        imgViewZoom.setImageResource(Integer.parseInt(String.valueOf(path)));
//        Picasso.get().load(path).fit().centerCrop().into(imgViewZoom);

        Glide.with(this)
                .load(path)
                .fitCenter()
                .into(imgViewZoom);


        shareImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(imageViewActivity.this, "btn clicked", Toast.LENGTH_SHORT).show();
//                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                Uri screenshotUri = Uri.parse(String.valueOf(path));
//                sharingIntent.setType("image/png");
//                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
//                startActivity(Intent.createChooser(sharingIntent, "Share image using"));

            }
        });

        detailsImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareSingle(Uri.parse(String.valueOf(path)));
            }
        });


//        iv_back_share_activity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                NavController navController = NavHostFragment.findNavController(ImageViewFragment.this);
//                navController.navigate(R.id.action_imageViewFragment_to_albumFragment);
//            }
//        });

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

}
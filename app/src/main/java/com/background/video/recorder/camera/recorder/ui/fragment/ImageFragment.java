package com.background.video.recorder.camera.recorder.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.util.FirebaseEvents;
import com.bumptech.glide.Glide;

import java.io.File;


public class ImageFragment extends Fragment {
    ConstraintLayout shareIntentBtn;
    String imageBitmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shareIntentBtn = view.findViewById(R.id.shareIntentBtn);
        Bundle bundle = getArguments();
        if (bundle != null) {
            imageBitmap = bundle.getString("imagePath");

            if (imageBitmap != null) {
                ImageView imageView = view.findViewById(R.id.imageViewimage);
                Glide.with(this)
                        .load(imageBitmap)
                        .encodeQuality(100) // Set image quality to 100 (highest)
                        .into(imageView);
//                imageView.setImageBitmap(imageBitmap);
            }
        }

        shareIntentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseEvents.Companion.logAnalytic("super_camera_share_btn");
                if (imageBitmap != null) {
                    File imageFile = new File(imageBitmap);
                    if (imageFile.exists()) {
                        Uri contentUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() , imageFile);
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        shareIntent.setType("image/jpeg");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                        startActivity(Intent.createChooser(shareIntent, "Share Image"));
                    }
                }
            }
        });
    }
}
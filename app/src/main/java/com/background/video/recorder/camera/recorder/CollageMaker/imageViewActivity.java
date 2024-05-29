package com.background.video.recorder.camera.recorder.CollageMaker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.background.video.recorder.camera.recorder.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;

import java.io.File;

public class imageViewActivity extends AppCompatActivity {

    //    AdsActivity adsActivity;
    AdView mAdView;
    ImageView imgViewZoom;
    ConstraintLayout iv_back_share_activity;
    Uri path;
    //    TextView details;
    AppCompatButton shareImageBtn, detailsImageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        shareImageBtn = findViewById(R.id.shareImageBtnIntent);
        detailsImageBtn = findViewById(R.id.detailsImageBtn);
        iv_back_share_activity = findViewById(R.id.iv_back_share_activity);
//        details = findViewById(R.id.details);

        imgViewZoom = findViewById(R.id.imgViewZoom);


        path = Uri.parse(getIntent().getStringExtra("imageViewZoom"));
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


        iv_back_share_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        Log.i("AddTextSpinner", "textSpinner : " + hasFocus);
//
//        if (hasFocus) {
//            findViewById(R.id.relativeLayout3).setVisibility(View.VISIBLE);
//        } else {
//            findViewById(R.id.relativeLayout3).setVisibility(View.GONE);
//        }
//    }

}
package com.background.video.recorder.camera.recorder.PhotoEditor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import com.background.video.recorder.camera.recorder.CollageMaker.ShareActivity;
import com.background.video.recorder.camera.recorder.PhotoEditor.ColorsPractice.ColorAdapter;
import com.background.video.recorder.camera.recorder.PhotoEditor.ColorsPractice.ColorModel;
import com.background.video.recorder.camera.recorder.PhotoEditor.ColorsPractice.ImageModelInterface;
import com.background.video.recorder.camera.recorder.PhotoEditor.base.BaseActivity;
import com.background.video.recorder.camera.recorder.PhotoEditor.emoji.EmojiActivity;
import com.background.video.recorder.camera.recorder.PhotoEditor.filters.FilterListener;
import com.background.video.recorder.camera.recorder.PhotoEditor.filters.FilterViewAdapter;
import com.background.video.recorder.camera.recorder.PhotoEditor.sticker.StickerActivity;
import com.background.video.recorder.camera.recorder.PhotoEditor.text.FontColorTextActivity;
import com.background.video.recorder.camera.recorder.PhotoEditor.tools.EditingToolsAdapter;
import com.background.video.recorder.camera.recorder.PhotoEditor.tools.ToolType;
import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.ActionOnAdClosedListener;
import com.background.video.recorder.camera.recorder.ads.AdMobBanner;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.ads.InterstitialClass;
import com.background.video.recorder.camera.recorder.databinding.ActivityEditImageOldBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.ui.activitiy.HomeActivity;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.SingleMediaScanner;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xw.repo.BubbleSeekBar;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.ViewType;
import ja.burhanrashid52.photoeditor.shape.ShapeBuilder;
import ja.burhanrashid52.photoeditor.shape.ShapeType;


public class EditImageActivity extends BaseActivity implements OnPhotoEditorListener, ImageModelInterface,
        View.OnClickListener,
        PropertiesBSFragment.Properties,
        ShapeBSFragment.Properties,
        EditingToolsAdapter.OnItemSelected, FilterListener, GestureDetector.OnDoubleTapListener {

    private static final String TAG = EditImageActivity.class.getSimpleName();
    public static final String FILE_PROVIDER_AUTHORITY = "com.app.photo.collage.maker.picture.editor.provider";
    private static final int CAMERA_REQUEST = 52;
    StickerView stickerView;
    Bitmap bitmapWithColorOverlay;
    private static final int PICK_REQUEST = 53;
    public static final String ACTION_NEXTGEN_EDIT = "action_nextgen_edit";
    public static final String PINCH_TEXT_SCALABLE_INTENT_KEY = "PINCH_TEXT_SCALABLE";
    Bitmap bitmapFromView;
    ArrayList<Parcelable> pathList = new ArrayList<>();
    ConstraintLayout conslayouteditor;
    String filePath;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    Uri imageUri;
    File image;
    PhotoEditor mPhotoEditor;
    SeekBar seekbarEraser;
    BubbleSeekBar bubbleSeekBar;
    private PhotoEditorView mPhotoEditorView;
    ProgressDialog progressDialog;
    private PropertiesBSFragment mPropertiesBSFragment;
    private ShapeBSFragment mShapeBSFragment;
    private ShapeBuilder mShapeBuilder;
    private TextView mTxtCurrentTool;
    ConstraintLayout seekbarLayoutHideShow, filterLayoutHideShow;
    private Typeface mWonderFont;
    private RecyclerView mRvTools, mRvFilters;
    private final EditingToolsAdapter mEditingToolsAdapter = new EditingToolsAdapter(this);
    private final FilterViewAdapter mFilterViewAdapter = new FilterViewAdapter(this);
    private ConstraintLayout mRootView;
    private final ConstraintSet mConstraintSet = new ConstraintSet();
    private boolean mIsFilterVisible;
    ImageView eraserCloseBtn, eraserDoneBtn, filterCloseBtn, filterDoneBtn;
    AdView mAdView;
    int textReturn = 3;
    public static Bitmap textBitmap;
    public static Bitmap stickerBitmap;
    public static Bitmap emojiBitmap;

    Bitmap mBitmapSampled;
    Bitmap bitmapFromCameraRef;

    @Nullable
    @VisibleForTesting
    Uri mSaveImageUri;
    private Uri path;
    private Uri path2;

    private FileSaveHelper mSaveFileHelper;
    private int REQUEST_CODE_EMOJI = 555;
    private int REQUEST_CODE_STICKER = 666;

    public static int RC_CROP_IMAGE = 1050;
    Bitmap bitmapFromCamera;
    Intent intentToShareActivity;
    String a;

    ColorAdapter colorAdapter;
    List<ColorModel> colors;
    Uri imageUriNewWay;
    ExecutorService executor;
    RelativeLayout rvbannerlayout;
    ColorOverlayViewModel colorViewModel;


    //////////////////////////////////////////

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
    ActivityEditImageOldBinding binding;

Intent intent;
    /////////////////////////////////////////
    private String photoeditorcollagemaker_home_admob_inter_id = "";
    private boolean photoeditorcollagemaker_home_admob_inter = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();

        binding = ActivityEditImageOldBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        executor = Executors.newSingleThreadExecutor();
        rvbannerlayout = findViewById(R.id.bannerAd);
        colorViewModel = new ViewModelProvider(this).get(ColorOverlayViewModel.class);

        initViews();

        prefs = SharedPrefsHelper.getInstance(this);
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {
            facebook_banner_enable = localPrefs.getfacebook_banner_enableSwitch();
            admob_banner_enable = localPrefs.getadmob_banner_enableSwitch();
            admob_banner_id = localPrefs.getadmob_banner_idId();
            facebook_banner_ad_id = localPrefs.getfacebook_banner_ad_idId();
            photoeditorcollagemaker_home_admob_inter = localPrefs.getphotoeditorcollagemaker_home_admob_interSwitch();
            photoeditorcollagemaker_home_admob_inter_id = localPrefs.getphotoeditorcollagemaker_home_admob_inter_idId();


            Log.d("TAG", "onCreate:  admob_interstitial_splash_id  " + remove_password_facebook_native);
            Log.d("", "onCreate:  facebook_interstitial_splash_id  " + remove_password_facebook_native);
        }


        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.bannerAd.setVisibility(View.GONE);
        }  else if (admob_banner_enable) {
            Log.e("bannercheckit", "Banner: admob_banner_enable loaded ");
            AdMobBanner.INSTANCE.loadAd(this, rvbannerlayout, admob_banner_id, facebook_banner_ad_id);
        } else if (facebook_banner_enable) {
            Log.e("bannercheckit", "Banner: facebook_banner_enable loaded ");
            AdMobBanner.INSTANCE.loadAdFifty(rvbannerlayout, this, facebook_banner_ad_id);
        } else {
            rvbannerlayout.setVisibility(View.GONE);
        }


//        intentToShareActivity = new Intent(EditImageActivity.this, ShareActivity.class);

        a = getIntent().getStringExtra("conditionFromActivity");
        if (a.equals("cameraBitmap")) {

            if (mPhotoEditor == null) {
                path = Uri.parse(getIntent().getStringExtra("cameraBitmapPhoto"));
                BitmapFactory.Options options2 = new BitmapFactory.Options();
                mBitmapSampled = BitmapFactory.decodeFile(String.valueOf(path), options2);
                bitmapWithColorOverlay = mBitmapSampled;
                mPhotoEditorView.getSource().setImageURI(path);
            } else {
                Toast.makeText(this, "Please Try again", Toast.LENGTH_SHORT).show();
                // Handle the case when path is null
            }

        }

//        if (a.equals("MirrorActivity")) {
//            if (path == null) {
//                path = getIntent().getParcelableExtra("photo_path");
//                Log.d("priceoye", "updatePicture: " + path);
//                BitmapFactory.Options options2 = new BitmapFactory.Options();
//                mBitmapSampled = BitmapFactory.decodeFile(String.valueOf(path), options2);
//                bitmapWithColorOverlay = mBitmapSampled;
//                mPhotoEditorView.getSource().setImageBitmap(mBitmapSampled);
//            } else {
//                Toast.makeText(this, "Please Try again", Toast.LENGTH_SHORT).show();
//            }
//        }


        if (a.equals("MirrorActivity")) {
            if (path == null) {
                path = getIntent().getParcelableExtra("photo_path");
                Log.d("priceoye", "updatePicture: " + path);

                // Decode the bitmap from the file
                BitmapFactory.Options options = new BitmapFactory.Options();
                mBitmapSampled = BitmapFactory.decodeFile(String.valueOf(path), options);

                // Check the orientation of the image and rotate the bitmap if necessary
                try {
                    ExifInterface exif = new ExifInterface(String.valueOf(path));
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                    mBitmapSampled = rotateBitmap(mBitmapSampled, orientation);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Set the bitmap to your ImageView or wherever you want to display it
                mPhotoEditorView.getSource().setImageBitmap(mBitmapSampled);
            } else {
                Toast.makeText(this, "Please Try again", Toast.LENGTH_SHORT).show();
            }
        }


        handleIntentImage(mPhotoEditorView.getSource());

        mWonderFont = Typeface.createFromAsset(getAssets(), "beyond_wonderland.ttf");

        mPropertiesBSFragment = new PropertiesBSFragment();

        mShapeBSFragment = new ShapeBSFragment();

        mPropertiesBSFragment.setPropertiesChangeListener(this);
        mShapeBSFragment.setPropertiesChangeListener(this);

        LinearLayoutManager llmTools = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvTools.setLayoutManager(llmTools);
        mRvTools.setAdapter(mEditingToolsAdapter);

        LinearLayoutManager llmFilters = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvFilters.setLayoutManager(llmFilters);
        mRvFilters.setAdapter(mFilterViewAdapter);
        boolean pinchTextScalable = getIntent().getBooleanExtra(PINCH_TEXT_SCALABLE_INTENT_KEY, true);

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(pinchTextScalable) // set flag to make text scalable when pinch
                //.setDefaultTextTypeface(mTextRobotoTf)
                //.setDefaultEmojiTypeface(mEmojiTypeFace)
                .build(); // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this);

        eraserDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekbarLayoutHideShow.setVisibility(View.GONE);
            }
        });


        eraserCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                seekbarLayoutHideShow.setVisibility(View.GONE);
            }
        });

        filterCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilter(false);
                filterLayoutHideShow.setVisibility(View.GONE);
                binding.rvColors.setVisibility(View.GONE);
            }
        });

        filterDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFilter(false);
                filterLayoutHideShow.setVisibility(View.GONE);
                binding.rvColors.setVisibility(View.GONE);
            }
        });


        mSaveFileHelper = new FileSaveHelper(this);
    }


    private Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return rotatedBitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    //    public void Colo0rFilters(Bitmap mBitmapSampled) {
    public void Colo0rFilters(Uri imageUri) {

        colors = new ArrayList<>();
        colors.add(new ColorModel(getResources().getColor(R.color.transparent1), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent2), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent3), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent4), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent5), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent6), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent7), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent8), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent9), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent10), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent11), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent12), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent13), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent14), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent15), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent16), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent17), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent18), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent19), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent20), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent21), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent22), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent23), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent24), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent25), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent26), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent27), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent28), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent29), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent30), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent31), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent32), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent33), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent34), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent35), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent36), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent37), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent38), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent39), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent40), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent41), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent42), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent43), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent44), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent45), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent46), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent47), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent48), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent49), "#00FF00", imageUri));
        colors.add(new ColorModel(getResources().getColor(R.color.transparent50), "#00FF00", imageUri));


        binding.rvColors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        colorAdapter = new ColorAdapter(colors, this, this, mBitmapSampled, binding.rvColors);
        colorAdapter = new ColorAdapter(colors, this, this, binding.rvColors);
//        colorAdapter = new ColorAdapter(colors, this, this, mBitmapSampled);
        binding.rvColors.setAdapter(colorAdapter);
//        colorAdapter.notifyDataSetChanged();
    }

    private void handleIntentImage(ImageView source) {
        Intent intent = getIntent();
        if (intent != null) {
            // NOTE(lucianocheng): Using "yoda conditions" here to guard against
            //                     a null Action in the Intent.
            if (Intent.ACTION_EDIT.equals(intent.getAction()) ||
                    ACTION_NEXTGEN_EDIT.equals(intent.getAction())) {
                try {
                    Uri uri = intent.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    source.setImageBitmap(bitmap);
                    Log.d("BitmapImageFromGal", "handleIntentImage: " + bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                String intentType = intent.getType();
                if (intentType != null && intentType.startsWith("image/")) {
                    Uri imageUri = intent.getData();
                    if (imageUri != null) {
                        source.setImageURI(imageUri);
                        Log.d("BitmapImageFromGal", "handleIntentImage: " + imageUri);
                    }
                }
            }
        }
    }

    private void initViews() {
        ImageView imgUndo;
        ImageView imgRedo;
        ImageView imgCamera;
        ImageView imgGallery;
        ImageView imgSave;
        ConstraintLayout imgClose;
        ImageView imgShare;
        stickerView = findViewById(R.id.sticker_view);
        mPhotoEditorView = findViewById(R.id.photoEditorView);
        mTxtCurrentTool = findViewById(R.id.txtCurrentTool);
        conslayouteditor = findViewById(R.id.conslayouteditor);
        mRvTools = findViewById(R.id.rvConstraintTools);
        mRvFilters = findViewById(R.id.rvFilterView);
        mRootView = findViewById(R.id.rootView);
        bubbleSeekBar = findViewById(R.id.seekbarEraser);
        seekbarLayoutHideShow = findViewById(R.id.seekbarLayoutHideShow);
        filterLayoutHideShow = findViewById(R.id.filterLayoutHideShow);

        eraserDoneBtn = findViewById(R.id.eraserDoneBtn);
        eraserCloseBtn = findViewById(R.id.eraserCloseBtn);

        filterCloseBtn = findViewById(R.id.filterCloseBtn);
        filterDoneBtn = findViewById(R.id.filterDoneBtn);


        imgUndo = findViewById(R.id.imgUndo);
        imgUndo.setOnClickListener(this);

        imgRedo = findViewById(R.id.imgRedo);
        imgRedo.setOnClickListener(this);

        imgCamera = findViewById(R.id.imgCamera);
        imgCamera.setOnClickListener(this);

        imgGallery = findViewById(R.id.imgGallery);
        imgGallery.setOnClickListener(this);

        imgSave = findViewById(R.id.imgSave);
        imgSave.setOnClickListener(this);

        imgClose = findViewById(R.id.iv_back_share_activity);
        imgClose.setOnClickListener(this);

        imgShare = findViewById(R.id.imgShare);
        imgShare.setOnClickListener(this);

    }

    @Override
    public void onEditTextChangeListener(final View rootView, String text, int colorCode) {
//        TextEditorDialogFragment textEditorDialogFragment =
//                TextEditorDialogFragment.show(this, text, colorCode);
//        textEditorDialogFragment.setOnTextEditorListener((inputText, newColorCode) -> {
//            final TextStyleBuilder styleBuilder = new TextStyleBuilder();
//            styleBuilder.withTextColor(newColorCode);
//
//            mPhotoEditor.editText(rootView, inputText, styleBuilder);
//            mTxtCurrentTool.setText(R.string.label_text);
//        });
    }


    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onTouchSourceImage(MotionEvent event) {
        Log.d(TAG, "onTouchView() called with: event = [" + event + "]");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imgUndo:
                mPhotoEditor.undo();
                break;

            case R.id.imgRedo:
                mPhotoEditor.redo();
                break;

            case R.id.imgSave:
                mPhotoEditor.clearHelperBox();
                stickerView.clearBorders();
                saveDialog();
                break;

            case R.id.iv_back_share_activity:
                onBackPressed();
                break;
            case R.id.imgShare:
                shareImage();
                break;

            case R.id.imgCamera:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;

            case R.id.imgGallery:
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST);

                break;
        }
    }

    private void shareImage() {
        if (mSaveImageUri == null) {
            showSnackbar(getString(R.string.msg_save_image_to_share));
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, buildFileProviderUri(mSaveImageUri));
        startActivity(Intent.createChooser(intent, getString(R.string.msg_share_image)));
    }

    private Uri buildFileProviderUri(@NonNull Uri uri) {
        return FileProvider.getUriForFile(this,
                FILE_PROVIDER_AUTHORITY,
                new File(uri.getPath()));
    }

    @SuppressLint("ResourceAsColor")
    public static Bitmap getBitmapFromView(View view, int quality) {
        try {
            Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            view.buildDrawingCache();
            view.setDrawingCacheEnabled(true);
            Canvas canvas = new Canvas(returnedBitmap);
            Drawable bgDrawable = view.getBackground();
            if (bgDrawable != null)
                bgDrawable.draw(canvas);
            else
                canvas.drawColor(android.R.color.transparent);
            view.draw(canvas);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            returnedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            byte[] byteArray = baos.toByteArray();
            returnedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            return returnedBitmap;
        } catch (Exception e) {
            Log.e("getBitmapFromView", e.getMessage());
            return null;
        }
    }

    private void saveImage(Bitmap bitmap, String name, ExecutorService executor) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                OutputStream fos;
                try {
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + getResources().getString(R.string.app_name));
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File image = new File(file, name + ".png");
                    fos = new FileOutputStream(image);
                    boolean saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    new SingleMediaScanner(EditImageActivity.this, file);
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // Shutdown the executor after the task is executed
        executor.shutdown();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case CAMERA_REQUEST:
                    mPhotoEditor.clearAllViews();
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    mPhotoEditorView.getSource().setImageBitmap(photo);
                    break;
                case PICK_REQUEST:
                    try {
                        mPhotoEditor.clearAllViews();
                        Uri uri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        mPhotoEditorView.getSource().setImageBitmap(bitmap);

//
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }

            if (requestCode == INTENT_REQUEST_GET_IMAGES) {

//                pathList = data.getExtras().getParcelableArrayList(ImagePickerActivity.EXTRA_IMAGE_URIS);
//                Log.d("Selected_image_back", "onActivityResult: Selected_image_back " + pathList);
//                Intent i = new Intent(EditImageActivity.this, EditImageActivity.class);
//                i.putExtra("photo_path", pathList.get(0));
//                Log.d("Selected_image_back", "onActivityResult: Selected_image_back " + pathList);
//                startActivity(i);

            }

            if (requestCode == textReturn) {
                if (data != null) {

                    String originalText = data.getStringExtra("textOriginal");
                    byte[] textBitmapBytes = data.getByteArrayExtra("textBitmap");

                    if (originalText != null && textBitmapBytes != null) {
                        Bitmap textBitmap = BitmapFactory.decodeByteArray(textBitmapBytes, 0, textBitmapBytes.length);
                        Drawable textDrawable = new BitmapDrawable(getResources(), textBitmap);

                        // Assuming stickerView is your sticker container
                        stickerView.addSticker(new DrawableSticker(textDrawable),
                                Sticker.Position.CENTER | Sticker.Position.CENTER);
                    }
                }
            }


            if (requestCode == REQUEST_CODE_EMOJI) {

                if (data != null) {
                    int emojiResource = data.getIntExtra("emojiSticker", 0);
                    if (emojiResource != 0) {
                        Glide.with(this).load(emojiResource)
                                .apply(new RequestOptions().encodeQuality(50).encodeFormat(Bitmap.CompressFormat.PNG))
                                .into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                        stickerView.addSticker(new DrawableSticker(resource),
                                                Sticker.Position.CENTER | Sticker.Position.CENTER);
                                    }
                                });

                    }
                }
            }

            if (requestCode == REQUEST_CODE_STICKER) {

                if (data != null) {

                    int emojiResource = data.getIntExtra("stikcerSticker", 0);
                    if (emojiResource != 0) {
                        Glide.with(this).load(emojiResource)
                                .apply(new RequestOptions().encodeQuality(50).encodeFormat(Bitmap.CompressFormat.PNG))
                                .into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                        stickerView.addSticker(new DrawableSticker(resource),
                                                Sticker.Position.CENTER | Sticker.Position.CENTER);
                                    }
                                });
                        Log.d("TextBitmapForEdit", "onActivityResult: " + textBitmap);


                    }
                }
            }

            if (requestCode == RC_CROP_IMAGE && data != null) {
                Uri cropResult = data.getData();

                try {
                    // Assuming 'uri' is the URI of the image you want to convert
                    InputStream inputStream = getContentResolver().openInputStream(cropResult);
                    mBitmapSampled = BitmapFactory.decodeStream(inputStream);
                    // Now 'bitmap' contains the image data as a Bitmap
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Glide.with(this).load((cropResult)).fitCenter().into(mPhotoEditorView.getSource());
            }
        }
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        Log.i("AddTextSpinner", "textSpinner : " + hasFocus);
//
//        if (hasFocus) {
//
//
////            rvbannerlayout.setVisibility(View.VISIBLE);
//        } else {
//            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.START);
//            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
//                    ConstraintSet.PARENT_ID, ConstraintSet.START);
//            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.END,
//                    ConstraintSet.PARENT_ID, ConstraintSet.END);
////            rvbannerlayout.setVisibility(View.GONE);
//
//        }
//    }

    @Override
    public void onColorChanged(int colorCode) {
        mPhotoEditor.setShape(mShapeBuilder.withShapeColor(colorCode));
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onOpacityChanged(int opacity) {
        mPhotoEditor.setShape(mShapeBuilder.withShapeOpacity(opacity));
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onShapeSizeChanged(int shapeSize) {
        mPhotoEditor.setShape(mShapeBuilder.withShapeSize(shapeSize));
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onShapePicked(ShapeType shapeType) {

    }

    private Uri saveImageScopedStorage(Bitmap bitmap, String fileName, Context context) throws IOException {
        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + getResources().getString(R.string.app_name));

        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (uri != null) {
            try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri)) {
                boolean saved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                if (!saved) {
                    throw new IOException("Failed to save bitmap.");
                }
            }
        } else {
            throw new IOException("Failed to create new MediaStore record.");
        }
        return uri;
    }

    public void saveDialog() {
        bitmapFromView = getBitmapFromView(conslayouteditor, 70);
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmsddMMyyyy", Locale.getDefault());
        String name = sdf.format(new Date());
        Uri savedImageUri = null; // Assuming the method returns Uri
        try {
            savedImageUri = saveImageScopedStorage(bitmapFromView, name, this);

//        saveImage(bitmapFromView, name, executor);
            ProgressDialog saveProgress = new ProgressDialog(EditImageActivity.this);
            saveProgress.setMessage("Saving Image. Please Wait...");
            saveProgress.setCancelable(false);
            saveProgress.show();
            Uri finalSavedImageUri = savedImageUri;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (saveProgress.isShowing()) {
                        saveProgress.dismiss();
                    }
//                    Intent intent = new Intent(EditImageActivity.this, ShareActivity.class);
//                    intent.putExtra("imageUri", finalSavedImageUri.toString());
//                    intent.putExtra("conditionFromActivity", "MirrorActivity");
//                    startActivity(intent);

//                    InterstitialAdUtils.INSTANCE.showadMobphotoCollagmakerInternalInterstitialAdActivity(
//                            EditImageActivity.this, new Intent(EditImageActivity.this, ShareActivity.class)
//                                    .putExtra("imageUri", finalSavedImageUri.toString()).putExtra("conditionFromActivity", "MirrorActivity"));

//                    finish();
                    if (photoeditorcollagemaker_home_admob_inter) {
                        InterstitialClass.requestInterstitial(EditImageActivity.this, photoeditorcollagemaker_home_admob_inter_id, new ActionOnAdClosedListener() {
                            @Override
                            public void ActionAfterAd() {

                                NavigationUtils.navigate(EditImageActivity.this, R.id.albumFragment);
                            }
                        });

                    } else {
                        NavigationUtils.navigate(EditImageActivity.this, R.id.albumFragment);

                    }

//                InterstitialAdUtils.INSTANCE.showInterstitialAd(EditImageActivity.this, intent);
                }
            }, 1500);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void onFilterSelected(PhotoFilter photoFilter) {
        progressDialog = new ProgressDialog(EditImageActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Please wait while loading"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mPhotoEditor.setFilterEffect(photoFilter);

                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        }, 1000);


    }

    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
            case SHAPE:
                binding.imgRedo.setVisibility(View.VISIBLE);
                binding.imgUndo.setVisibility(View.VISIBLE);
                seekbarLayoutHideShow.setVisibility(View.GONE);
                binding.txtCurrentTool.setVisibility(View.GONE);
                mPhotoEditor.setBrushDrawingMode(true);
                mShapeBuilder = new ShapeBuilder();
                mPhotoEditor.setShape(mShapeBuilder);
//                mTxtCurrentTool.setText(R.string.label_shape);
                showBottomSheetDialogFragment(mShapeBSFragment);
                break;
            case TEXT:
                binding.imgRedo.setVisibility(View.GONE);
                binding.imgUndo.setVisibility(View.GONE);
                seekbarLayoutHideShow.setVisibility(View.GONE);
                Intent intentToActivity = new Intent(EditImageActivity.this, FontColorTextActivity.class);
                startActivityForResult(intentToActivity, textReturn);


                break;

            case CROP:
                binding.imgRedo.setVisibility(View.GONE);
                binding.imgUndo.setVisibility(View.GONE);
                seekbarLayoutHideShow.setVisibility(View.GONE);
                CropImageGallery();
                break;
            case ERASER:

                binding.imgRedo.setVisibility(View.GONE);
                binding.imgUndo.setVisibility(View.GONE);
                seekbarLayoutHideShow.setVisibility(View.VISIBLE);
                mPhotoEditor.setBrushEraserSize(0f);
                mPhotoEditor.setBrushSize(0f);
                binding.txtCurrentTool.setText("Eraser");
                mPhotoEditor.brushEraser();

                bubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
                    @Override
                    public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                        super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser);

                        mPhotoEditor.setOpacity(progress);
                        mPhotoEditor.setBrushEraserSize(progress);
                        mPhotoEditor.brushEraser();
                        mPhotoEditor.setBrushSize(progress);

                    }

                    @Override
                    public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                        super.getProgressOnActionUp(bubbleSeekBar, progress, progressFloat);
                    }

                    @Override
                    public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                        super.getProgressOnFinally(bubbleSeekBar, progress, progressFloat, fromUser);
                    }
                });


                mTxtCurrentTool.setText(R.string.label_eraser_mode);
                break;
            case FILTER:
                binding.imgRedo.setVisibility(View.GONE);
                binding.imgUndo.setVisibility(View.GONE);
                filterLayoutHideShow.setVisibility(View.VISIBLE);
                Colo0rFilters(path);
                binding.rvColors.setVisibility(View.VISIBLE);
                binding.txtCurrentTool.setText("Filters");
                break;
            case EMOJI:
                binding.imgRedo.setVisibility(View.GONE);
                binding.imgUndo.setVisibility(View.GONE);
                seekbarLayoutHideShow.setVisibility(View.GONE);
                Intent intentToEmojiActivity = new Intent(EditImageActivity.this, EmojiActivity.class);
                startActivityForResult(intentToEmojiActivity, REQUEST_CODE_EMOJI);
                break;
            case STICKER:
                binding.imgRedo.setVisibility(View.GONE);
                binding.imgUndo.setVisibility(View.GONE);
                seekbarLayoutHideShow.setVisibility(View.GONE);
                Intent intentToStickerActivity = new Intent(EditImageActivity.this, StickerActivity.class);
                startActivityForResult(intentToStickerActivity, REQUEST_CODE_STICKER);

                break;
        }
    }

    private void showBottomSheetDialogFragment(BottomSheetDialogFragment fragment) {
        if (fragment == null || fragment.isAdded()) {
            return;
        }
        fragment.show(getSupportFragmentManager(), fragment.getTag());
    }

    void showFilter(boolean isVisible) {
        mIsFilterVisible = isVisible;
        mConstraintSet.clone(mRootView);

        if (isVisible) {
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.END);
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(mRootView, changeBounds);

        mConstraintSet.applyTo(mRootView);
    }

    @Override
    public void onBackPressed() {

        dialogExit();
    }

    public void dialogExit() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.do_you_really_want_to_signout));
        builder.setTitle(getResources().getString(R.string.sign_out));
        builder.setCancelable(false);
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intentToMain = new Intent(EditImageActivity.this, HomeActivity.class);
                intentToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentToMain.putExtra("loadFragment", true); // A boolean extra

                startActivity(intentToMain);
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onClick(int position) {
        int colorValue = colors.get(position).getColorValue();
        colorViewModel.setColorValue(colorValue); // Save color overlay value

        bitmapWithColorOverlay = applyColorOverlay(mBitmapSampled, colorValue);
        mPhotoEditorView.getSource().setImageBitmap(bitmapWithColorOverlay);
        Colo0rFilters(path);
    }

    private void CropImageGallery() {
//        if (path != null) {
//            imageUri = bitmapToUri(bitmapWithColorOverlay);
//            CropRequest themeCropRequest = new CropRequest.Auto(imageUri, RC_CROP_IMAGE, StorageType.CACHE, new ArrayList<>(), new CroppyTheme(R.color.bottomBarColor));
//            // Start croppy with your custom request.
//            Croppy.INSTANCE.start(this, themeCropRequest);
//        } else {
//            Toast.makeText(this, "Please Try Again", Toast.LENGTH_SHORT).show();
//        }
    }


    private Uri bitmapToUri(Bitmap bitmap) {
        File imageFile = saveBitmapToFile(bitmap);
        return FileProvider.getUriForFile(this, "com.app.photo.collage.maker.picture.editor.provider", imageFile);
    }


    private File saveBitmapToFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp_image.jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    public Bitmap applyColorOverlay(Bitmap bitmap, int color) {
        Bitmap resultBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));

        canvas.drawBitmap(resultBitmap, 0f, 0f, paint);

        return resultBitmap;
    }

    @Override
    public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public void onAddViewListener(@Nullable ViewType viewType, int numberOfAddedViews) {

    }
}

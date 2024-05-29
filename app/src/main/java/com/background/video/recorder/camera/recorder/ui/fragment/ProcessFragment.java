package com.background.video.recorder.camera.recorder.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.background.video.recorder.camera.recorder.CollageMaker.PuzzleUtils;
import com.background.video.recorder.camera.recorder.CollageMaker.practice.ProcessPuzzleAdapter;
import com.background.video.recorder.camera.recorder.ImagePicker.Config;
import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.ActionOnAdClosedListener;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.ads.InterstitialClass;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.PuzzlePiece;
import com.xiaopo.flying.puzzle.PuzzleView;
import com.xiaopo.flying.puzzle.slant.SlantPuzzleLayout;
import com.xw.repo.BubbleSeekBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ProcessFragment extends Fragment implements View.OnClickListener {

    private static final int FLAG_CONTROL_LINE_SIZE = 1;
    String filePath;
    private long lastTimeClicked = 0;
    private final long defaultInterval = 1000; // 1 second

    private static final int FLAG_CONTROL_CORNER = 1 << 1;
    boolean saved;
    private static final int INTENT_REQUEST_GET_REPLACE_IMAGES = 13;

    private PuzzleLayout puzzleLayout;
    private List<String> bitmapPaint = new ArrayList<>();
    private PuzzleView puzzleView;
    private SeekBar degreeSeekBarBorder, degreeSeekBarRadius;
    //    private SeekBar degreeSeekBar;
    private BubbleSeekBar degreeSeekBar, corner_seek_bar;
    LinearLayout snackLayout;
    HorizontalScrollView horizontalScrollView;

    ImageView backBtnPhotoPicker, borderDoneBtn, borderCloseBtn, cornerCloseBtn, cornerDoneBtn;
    private List<Target> targets = new ArrayList<>();
    private int deviceWidth = 0;
    ConstraintLayout rvlayout;
    AdView mAdView;
    ImageView doneAfterRv, cancelAfterRv;
    int type = 0;
    int pieceSize = 0;
    int themeId = 0;
    int peiceNumberItemPosition;
    String path;
    RecyclerView layoutRecyclerView;
    ConstraintLayout conslaypoutseekbar, cornerseekbarlayout;
    //PuzzleAdapter puzzleAdapter;
    Intent intentFromCollageEditortoShare;
    ProcessPuzzleAdapter processPuzzleAdapter;
    private int controlFlag;

    private ArrayList<Uri> image_uris = new ArrayList<>();

    Uri urisReplace;
    private Bitmap bitmapToReplace;
    private boolean pieceSelected = false;

    ArrayList<Uri> uris;
    private PuzzlePiece selectedPiece = null;
    SharedPreferences sharedPrefs;
    boolean homeInter, splashInter;
    private String admob_interstitial_home_id = "";


    private AppCompatButton saveBtn;
    private ImageView back;

    @Override
    public void onResume() {
        super.onResume();
        if (saveBtn == null) {

            saveBtn = requireActivity().findViewById(R.id.saveBtn);


            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    saveDialog();
                }
            });
        }
    }

    private String photoeditorcollagemaker_home_admob_inter_id = "";
    private SharedPrefsHelper prefs = null;
    private boolean photoeditorcollagemaker_home_admob_inter = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_process, container, false);
        navController = NavHostFragment.findNavController(ProcessFragment.this);
        prefs = SharedPrefsHelper.getInstance(getActivity());
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {
            photoeditorcollagemaker_home_admob_inter = localPrefs.getphotoeditorcollagemaker_home_admob_interSwitch();
            photoeditorcollagemaker_home_admob_inter_id = localPrefs.getphotoeditorcollagemaker_home_admob_inter_idId();

        }



        return view.getRootView();
    }

    // Somewhere in your code where you need to start the activity for result
    public void openActivityForResult() {
        Intent intent = new Intent(); // Replace with your intent
        getResultLauncher.launch(intent);
    }


    private void updateUIWithBitmaps(List<Bitmap> listBitmap) {
        // Update your adapter or UI components with the list of Bitmaps


        processPuzzleAdapter.refreshData(PuzzleUtils.getPuzzleLayouts(uris.size()), null);
        layoutRecyclerView.setAdapter(processPuzzleAdapter);
    }


    private void loadBitmaps(List<Uri> uris) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Handler handler = new Handler(Looper.getMainLooper());

        List<Bitmap> listBitmap = Collections.synchronizedList(new ArrayList<>());

        for (Uri uri : uris) {
            executor.execute(() -> {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                    listBitmap.add(bitmap);
                    if (listBitmap.size() == uris.size()) {
                        handler.post(() -> {
                            // Update UI with listBitmap
                            updateUIWithBitmaps(listBitmap);
                        });
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown(); // Shutdown the executor once all tasks are submitted
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedPrefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        homeInter = sharedPrefs.getBoolean("home_inter", false);

        layoutRecyclerView = view.findViewById(R.id.layoutRecyclerView);

        SharedPrefsHelper prefs = SharedPrefsHelper.getInstance(getActivity());
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {

            admob_interstitial_home_id = localPrefs.getadmob_interstitial_home_idId();


            Log.d("dashboard_native", "dashboard_native: " + admob_interstitial_home_id);
        }

        deviceWidth = getResources().getDisplayMetrics().widthPixels;
        uris = new ArrayList<>();

        uris = getArguments().getParcelableArrayList("photo_path");

        loadBitmaps(uris);
//
        type = getArguments().getInt("type", uris.size());
        pieceSize = getArguments().getInt("piece_size", uris.size());
        Log.d("piece_size", "onCreate: " + uris.size());
        themeId = getArguments().getInt("theme_id", 3);

//        List<Bitmap> listBitmap = new ArrayList<>();

//        for (int i = 0; i < uris.size(); i++) {
//
//            BitmapFactory.Options options2 = new BitmapFactory.Options();
//            Bitmap myBitmap = BitmapFactory.decodeFile(String.valueOf(uris.get(i)), options2);
//
//            listBitmap.add(myBitmap);
//
//        }

        puzzleLayout = PuzzleUtils.getPuzzleLayout(type, pieceSize, themeId);
        processPuzzleAdapter = new ProcessPuzzleAdapter(getActivity());
        layoutRecyclerView = view.findViewById(R.id.layoutRecyclerView);
        layoutRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutRecyclerView.setLayoutManager(layoutManager);
        layoutRecyclerView.setAdapter(processPuzzleAdapter);

        processPuzzleAdapter.refreshData(PuzzleUtils.getPuzzleLayouts(uris.size()), null);
        Log.d("BITMAP_SAIZE", "onCreate: " + uris.size());


        processPuzzleAdapter.setOnItemClickListener(new ProcessPuzzleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PuzzleLayout puzzleLayout, int themeId) {

                if (puzzleLayout instanceof SlantPuzzleLayout) {
                    type = 0;
                } else {
                    type = 1;
                }
                pieceSize = puzzleLayout.getAreaCount();
                themeId = themeId;
                puzzleLayout = PuzzleUtils.getPuzzleLayout(type, pieceSize, themeId);

                puzzleView.setPuzzleLayout(puzzleLayout);
                puzzleView.post(new Runnable() {
                    @Override
                    public void run() {
                        loadPhoto();
                    }
                });
            }
        });

        initView(view);

        layoutRecyclerView = view.findViewById(R.id.layoutRecyclerView);
        puzzleView.post(new Runnable() {
            @Override
            public void run() {
                loadPhoto();
            }
        });


        doneAfterRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.VISIBLE);
                rvlayout.setVisibility(View.GONE);
            }
        });
        cancelAfterRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.VISIBLE);
                rvlayout.setVisibility(View.GONE);
            }
        });

    }

    private void loadPhoto() {
        if (uris == null) {

//            Toast.makeText(this, "Load photo method start but failed", Toast.LENGTH_SHORT).show();
            loadPhotoFromRes();
            return;
        }

        final List<Bitmap> pieces = new ArrayList<>();

//        Toast.makeText(this, "Load photo method start", Toast.LENGTH_SHORT).show();

        final int count = uris.size() > puzzleLayout.getAreaCount() ? puzzleLayout.getAreaCount()
                : uris.size();

        for (int i = 0; i < count; i++) {
            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    pieces.add(bitmap);
                    if (pieces.size() == count) {
                        if (uris.size() < puzzleLayout.getAreaCount()) {
                            for (int i = 0; i < puzzleLayout.getAreaCount(); i++) {
                                puzzleView.addPiece(pieces.get(i % count));
                            }
                        } else {
                            puzzleView.addPieces(pieces);
                        }
                    }
                    targets.remove(this);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }


                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            Picasso.get()
                    .load("file:///" + uris.get(i))
                    .resize(deviceWidth, deviceWidth)
                    .centerInside()
                    .config(Bitmap.Config.RGB_565)
                    .into(target);

            targets.add(target);
        }
    }

    private void loadPhotoFromRes() {
        final List<Bitmap> pieces = new ArrayList<>();

        final int[] resIds = new int[]{
                R.drawable.album_icon, R.drawable.album_icon, R.drawable.album_icon, R.drawable.album_icon
        };

        final int count =
                resIds.length > puzzleLayout.getAreaCount() ? puzzleLayout.getAreaCount() : resIds.length;

        for (int i = 0; i < count; i++) {
            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    pieces.add(bitmap);
                    if (pieces.size() == count) {
                        if (resIds.length < puzzleLayout.getAreaCount()) {
                            for (int i = 0; i < puzzleLayout.getAreaCount(); i++) {
                                puzzleView.addPiece(pieces.get(i % count));
                            }
                        } else {
                            puzzleView.addPieces(pieces);
                        }
                    }
                    targets.remove(this);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }


                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            Picasso.get().load(resIds[i]).config(Bitmap.Config.RGB_565).into(target);

            targets.add(target);
        }
    }

    private void initView(View view) {
        horizontalScrollView = view.findViewById(R.id.hidehorizontal);
        rvlayout = view.findViewById(R.id.rvlayout);
        doneAfterRv = view.findViewById(R.id.doneAfterRv);
        cancelAfterRv = view.findViewById(R.id.cancelAfterRv);
        borderDoneBtn = view.findViewById(R.id.borderDoneBtn);
        borderCloseBtn = view.findViewById(R.id.borderCloseBtn);
        cornerCloseBtn = view.findViewById(R.id.cornerCloseBtn);
        cornerDoneBtn = view.findViewById(R.id.cornerDoneBtn);
        conslaypoutseekbar = view.findViewById(R.id.conslaypoutseekbar);
        cornerseekbarlayout = view.findViewById(R.id.cornerseekbarlayout);
        puzzleView = (PuzzleView) view.findViewById(R.id.puzzle_view);
        puzzleView.setPieceRadian(0f);

        horizontalScrollView.setVisibility(View.VISIBLE);
        rvlayout.setVisibility(View.VISIBLE);
//        degreeSeekBar.setVisibility(View.INVISIBLE);
//        corner_seek_bar.setVisibility(View.INVISIBLE);
        pieceSelected = false;

        ImageView btnBack = (ImageView) view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show an AlertDialog
                new AlertDialog.Builder(requireContext())
                        .setTitle("Exit")
                        .setMessage("Do you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Navigate back to photoEditorCollageMakerFragment
                                Navigation.findNavController(view).navigate(R.id.action_processFragment_to_homeFragment);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Dismiss the dialog if "No" is clicked
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

        borderDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conslaypoutseekbar.setVisibility(View.GONE);
            }
        });

        borderCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conslaypoutseekbar.setVisibility(View.GONE);
            }
        });

        cornerDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cornerseekbarlayout.setVisibility(View.GONE);
            }
        });

        cornerCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cornerseekbarlayout.setVisibility(View.GONE);
            }
        });


        degreeSeekBar = view.findViewById(R.id.degree_seek_bar);
        corner_seek_bar = view.findViewById(R.id.corner_seek_bar);
        puzzleView.setPuzzleLayout(puzzleLayout);
        puzzleView.setTouchEnable(true);
        puzzleView.setNeedDrawLine(false);
        puzzleView.setNeedDrawOuterLine(false);
        puzzleView.setLineSize(4);
        puzzleView.setLineColor(Color.WHITE);
        puzzleView.setSelectedLineColor(Color.WHITE);
        puzzleView.setHandleBarColor(Color.WHITE);
        puzzleView.setAnimateDuration(300);
        puzzleView.setOnPieceSelectedListener(new PuzzleView.OnPieceSelectedListener() {
            @Override
            public void onPieceSelected(PuzzlePiece piece, int position) {

//                Toast.makeText(ProcessActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                peiceNumberItemPosition = position;
                pieceSelected = true;

            }
        });

        ImageView btnReplace = (ImageView) view.findViewById(R.id.btn_replace);
        ImageView btnRotate = (ImageView) view.findViewById(R.id.btn_rotate);
        ImageView btnFlipHorizontal = (ImageView) view.findViewById(R.id.btn_flip_horizontal);
        ImageView btnFlipVertical = (ImageView) view.findViewById(R.id.btn_flip_vertical);
        ImageView btnBorder = (ImageView) view.findViewById(R.id.btn_border);
        ImageView btnCorner = (ImageView) view.findViewById(R.id.btn_corner);
        ImageView btnlayout = (ImageView) view.findViewById(R.id.btn_layout);

        btnReplace.setOnClickListener(this);
        btnlayout.setOnClickListener(this);
        btnRotate.setOnClickListener(this);
        btnFlipHorizontal.setOnClickListener(this);
        btnFlipVertical.setOnClickListener(this);
        btnBorder.setOnClickListener(this);
        btnCorner.setOnClickListener(this);

        TextView btnSave = (TextView) view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

//                File file = FileUtils.getNewFile(ProcessActivity.this, "Photo Editor Pic Collage maker");
//                FileUtils.savePuzzle(puzzleView, file, 100, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        Snackbar.make(view, "Succesfully saved", Snackbar.LENGTH_SHORT).show();
//                        intentFromCollageEditortoShare.putExtra("uriToShareActivity", file.toString());
//                        intentFromCollageEditortoShare.putExtra("conditionFromActivity", "ProcessActivity");
//                        intentFromCollageEditortoShare.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        InterstitialAdUtils.INSTANCE.showInterstitialAd(ProcessActivity.this, intentFromCollageEditortoShare);
//                        startActivity(intentFromCollageEditortoShare);
//                        new SingleMediaScanner(ProcessActivity.this, file);
//                    }
//
//                    @Override
//                    public void onFailed() {
//                        Snackbar.make(view, "Cannot save tryagain", Snackbar.LENGTH_SHORT).show();
//                    }
//                });
                saveDialog();

            }
        });
//        degreeSeekBar.setProgress(100);
//        degreeSeekBar.setProgress(100);


        degreeSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser);

                if (progress == 0) {
                    puzzleView.setNeedDrawLine(false);
                } else {

                    puzzleView.setNeedDrawLine(true);
                    puzzleView.setLineSize(progress);
                }
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


        corner_seek_bar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser);
//                switch (controlFlag) {
//                    case FLAG_CONTROL_LINE_SIZE:
//                        puzzleView.setLineSize(progress);
//                        break;
//                    case FLAG_CONTROL_CORNER:
                puzzleView.setPieceRadian(progress);
//                        break;
//                }

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

    }

    NavController navController;

    public void saveDialog() {

        try {
            Bitmap bitmap = getBitmapFromView(puzzleView);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String name = sdf.format(new Date());

            Uri savedImageUri = saveImageScopedStorage(bitmap, name, getActivity()); // Assuming the method returns Uri

//            saveImageScopedStorage(bitmap, name, getApplicationContext());
//            saveImage(bitmap);
            ProgressDialog saveProgress = new ProgressDialog(getActivity());
            saveProgress.setMessage("Saving Image. Please Wait...");
            saveProgress.setCancelable(false);
            saveProgress.show();
            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
////                    saveProgress.dismiss();
////
////                    Intent intent = new Intent(getApplicationContext(), ShareActivity.class);
////                    intent.putExtra("fileName", name + ".jpg");
////                    Log.d("ImageUri_intent", "run: " + filePath);
////                    intent.putExtra("conditionFromActivity", "MirrorActivity");
////                    InterstitialAdUtils.INSTANCE.showInterstitialAd(ProcessActivity.this);
////                    startActivity(intent);
//                    saveProgress.dismiss();
////                    Intent intent = new Intent(getApplicationContext(), ShareActivity.class);
////                    intent.putExtra("fileName", name);
////                    intent.putExtra("fileName", name + ".jpg");
////                    intent.putExtra("conditionFromActivity", "MirrorActivity");
////                    startActivity(intent);
//
//
//                    Intent intent = new Intent(getActivity(), ShareActivity.class);
//                    intent.putExtra("imageUri", savedImageUri.toString()); // Pass Uri as string
//                    intent.putExtra("conditionFromActivity", "MirrorActivity");
//                    startActivityNext(intent);
////                    finish();
//                }

                @Override
                public void run() {
                    saveProgress.dismiss();

                    // Prepare the bundle with data
                    Bundle bundle = new Bundle();
                    bundle.putString("imageUri", savedImageUri.toString());
                    bundle.putString("conditionFromActivity", "MirrorActivity");


//                    InterstitialAdUtils.INSTANCE.showadMobphotoCollagmakerInternalInterstitialAd(getActivity(), "",
//                            R.id.shareFragment, bundle, navController);
                    if (photoeditorcollagemaker_home_admob_inter) {
                        InterstitialClass.requestInterstitial(getActivity(), photoeditorcollagemaker_home_admob_inter_id, new ActionOnAdClosedListener() {
                            @Override
                            public void ActionAfterAd() {

                                NavigationUtils.navigate(getActivity(), R.id.shareFragment,bundle);
                            }
                        });

                    } else {
                        NavigationUtils.navigate(getActivity(), R.id.shareFragment,bundle);

                    }
                }


            }, 1500);
        } catch (IOException e) {
            Toast.makeText(getActivity(), "Something went wrong" + e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
            Log.d("photoeditories", "saveDialog: " + e.getLocalizedMessage().toString());
        }

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


//    private void saveImage(Bitmap bitmap, String name) throws IOException {
//
//        OutputStream fos;
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + getResources().getString(R.string.app_name));
//        if (!file.exists()) {
//            file.mkdir();
//        }
//        File image = new File(file, name + ".jpg");
//        fos = new FileOutputStream(image);
//        saved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//        fos.flush();
//        fos.close();
//        new SingleMediaScanner(this, file);
//
//    }

    private void saveImage(Bitmap bitmap, String name) throws IOException {
        OutputStream fos;
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File appDir = new File(downloadsDir, getResources().getString(R.string.app_name));
        if (!appDir.exists()) {
            appDir.mkdirs(); // Use mkdirs() instead of mkdir()
        }
        File imageFile = new File(appDir, name + ".jpg");
        fos = new FileOutputStream(imageFile);
        boolean saved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();

        // Notify the media scanner
        MediaScannerConnection.scanFile(getActivity(), new String[]{imageFile.getAbsolutePath()}, null, null);
    }


    @SuppressLint("ResourceAsColor")
    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(android.R.color.transparent);
        view.draw(canvas);
        return returnedBitmap;
    }

//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//        dialogExit();
//    }

    public void dialogExit() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
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
//                Toast.makeText(MirrorActivity.this, R.string.log_out_success,
//                        Toast.LENGTH_LONG).show();
//                builder.finish(); // Yaha frgaemtn ko backpres krwana hy
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_layout:
//                Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
                horizontalScrollView.setVisibility(View.VISIBLE);
                rvlayout.setVisibility(View.VISIBLE);
                degreeSeekBar.setVisibility(View.INVISIBLE);
                corner_seek_bar.setVisibility(View.INVISIBLE);
                pieceSelected = false;
                break;
            case R.id.btn_replace:
//                showSelectedPhotoDialog();

//                if (puzzleView.hasPieceSelected()) {
//                    Config config = new Config();
//                    config.setSelectionMin(1);
//                    config.setSelectionLimit(1);
//                    ImagePickerActivity.setConfig(config);
//                    Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
//                    intent.putExtra("FromActivity", "ReplaceImage");
////                    startActivityForResult(intent);
//                    startActivityForResult(intent, INTENT_REQUEST_GET_REPLACE_IMAGES);
////                    pieceSelected = true;
//                    degreeSeekBar.setVisibility(View.INVISIBLE);
//                    corner_seek_bar.setVisibility(View.INVISIBLE);
//                } else {
//                    Toast.makeText(getActivity(), "Please Select image to Replace", Toast.LENGTH_SHORT).show();
////                    pieceSelected = false;
//                }

                break;
            case R.id.btn_rotate:
                if (puzzleView.hasPieceSelected()) {
//                    pieceSelected = false;

                    pieceSelected = true;
                    puzzleView.rotate(90f);
                } else {
                    Toast.makeText(getActivity(), "Please Select image to Rotate", Toast.LENGTH_SHORT).show();
                    pieceSelected = false;
                }


                conslaypoutseekbar.setVisibility(View.INVISIBLE);
                cornerseekbarlayout.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_flip_horizontal:
                if (puzzleView.hasPieceSelected()) {

                    pieceSelected = true;
//                    pieceSelected = false;
                    puzzleView.flipHorizontally();
                } else {
                    Toast.makeText(getActivity(), "Please Select image to Flip", Toast.LENGTH_SHORT).show();
                    pieceSelected = false;
                }


                conslaypoutseekbar.setVisibility(View.INVISIBLE);
                cornerseekbarlayout.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_flip_vertical:
                if (puzzleView.hasPieceSelected()) {

                    pieceSelected = true;
//                    pieceSelected = false;
                    puzzleView.flipVertically();

                } else {
                    Toast.makeText(getActivity(), "Please Select image to Flip", Toast.LENGTH_SHORT).show();
                    pieceSelected = false;
                }

                conslaypoutseekbar.setVisibility(View.INVISIBLE);
                cornerseekbarlayout.setVisibility(View.INVISIBLE);
                break;
//            case R.id.btn_border:
//
//                pieceSelected = false;
//                controlFlag = FLAG_CONTROL_LINE_SIZE;
//                puzzleView.setNeedDrawLine(!puzzleView.isNeedDrawLine());
//                if (puzzleView.isNeedDrawLine()) {
//                    conslaypoutseekbar.setVisibility(View.VISIBLE);
//                    degreeSeekBar.setVisibility(View.VISIBLE);
//                } else {
//                    conslaypoutseekbar.setVisibility(View.INVISIBLE);
//                }
//                break;

//            case R.id.btn_border:
//                if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
//                    return;
//                }
//                lastTimeClicked = SystemClock.elapsedRealtime();
//                cornerseekbarlayout.setVisibility(View.INVISIBLE);
//                pieceSelected = false;
//                controlFlag = FLAG_CONTROL_LINE_SIZE;
//                if (puzzleView != null) {
//                    puzzleView.setNeedDrawLine(!puzzleView.isNeedDrawLine());
//                    if (puzzleView.isNeedDrawLine()) {
////                        puzzleView.setNeedDrawLine(!puzzleView.isNeedDrawLine());
//                        conslaypoutseekbar.setVisibility(View.VISIBLE);
//                        degreeSeekBar.setVisibility(View.VISIBLE);
//                    } else {
//                        conslaypoutseekbar.setVisibility(View.INVISIBLE);
//                    }
//                }
//                break;

            case R.id.btn_border:
                if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
                    return;
                }
                lastTimeClicked = SystemClock.elapsedRealtime();
                cornerseekbarlayout.setVisibility(View.INVISIBLE);
                pieceSelected = false;
                controlFlag = FLAG_CONTROL_LINE_SIZE;
                if (puzzleView != null) {
                    puzzleView.setNeedDrawLine(true); // Always set the border to be visible here
                    conslaypoutseekbar.setVisibility(View.VISIBLE);
                    degreeSeekBar.setVisibility(View.VISIBLE);
                }
                break;


            case R.id.btn_corner:
                if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
                    return;
                }
                lastTimeClicked = SystemClock.elapsedRealtime();

                conslaypoutseekbar.setVisibility(View.INVISIBLE);

                if (controlFlag == FLAG_CONTROL_CORNER && corner_seek_bar.getVisibility() == View.VISIBLE) {
                    cornerseekbarlayout.setVisibility(View.INVISIBLE);
                    return;
                }
//                controlFlag = FLAG_CONTROL_CORNER;
                cornerseekbarlayout.setVisibility(View.VISIBLE);
                corner_seek_bar.setVisibility(View.VISIBLE);
                pieceSelected = false;
                break;

        }
    }

    private ActivityResultLauncher<Intent> getResultLauncher;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_GET_REPLACE_IMAGES) {
            String uri = data.getStringExtra("photo_path_replace");
            Log.d("image_repcace", "onActivityResult: " + image_uris);
            uris.set(peiceNumberItemPosition, Uri.parse(uri));

            if (puzzleView.getHandlingPiece() != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap mBitmapSampled = BitmapFactory.decodeFile(uri, options);
                puzzleView.replace(mBitmapSampled, "");
            }
        }

    }
}

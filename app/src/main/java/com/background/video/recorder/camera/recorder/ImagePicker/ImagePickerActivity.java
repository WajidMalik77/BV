//package com.background.video.recorder.camera.recorder.ImagePicker;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.viewpager.widget.ViewPager;
//
//import com.background.video.recorder.camera.recorder.CollageMaker.ProcessActivity;
//import com.background.video.recorder.camera.recorder.ImagePicker.Config;
//import com.background.video.recorder.camera.recorder.R;
//import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
//import com.google.android.gms.ads.AdView;
//import com.google.android.material.tabs.TabLayout;
//
//import java.util.ArrayList;
//
//
//public class ImagePickerActivity extends AppCompatActivity {
//
//    public static final String EXTRA_IMAGE_URIS = "image_uris";
//    // initialize with default config.
//    private static Config mConfig = new Config();
//
//    public ArrayList<Uri> mSelectedImages;
//    protected Toolbar toolbar;
//    int counting = 0;
//    View view_root;
//    ImageView dltItems;
//    TextView mSelectedImageEmptyMessage;
//    View view_selected_photos_container;
//    RecyclerView rc_selected_photos;
//    TextView tv_selected_title;
//    ViewPager mViewPager;
//
//    Intent intentToMirror;
//    AdView mAdView;
//    TabLayout tabLayout;
//    PagerAdapter_Picker adapter;
//    Adapter_SelectedPhoto adapter_selectedPhoto;
//
//    public static Config getConfig() {
//        return mConfig;
//    }
//
//    public static void setConfig(Config config) {
//
//        if (config == null) {
//            throw new NullPointerException("Config cannot be passed null. Not setting config will use default values.");
//        }
//
//        mConfig = config;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setupFromSavedInstanceState(savedInstanceState);
//        setContentView( R.layout.picker_activity_main_pp_new);
//        initView();
//
//        setTitle(mConfig.getToolbarTitleRes());
//
//
//
//        setupTabs();
//        setSelectedPhotoRecyclerView();
//
//
//        dltItems.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clear();
//            }
//        });
//
//    }
//    private void initView() {
//
//        toolbar = findViewById( R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//
//
//        view_root = findViewById( R.id.view_root);
//        dltItems = findViewById( R.id.dltItems);
//        mViewPager = findViewById( R.id.pager);
//        tabLayout = findViewById( R.id.tab_layout);
//
//        tv_selected_title = findViewById( R.id.tv_selected_title);
//
//        rc_selected_photos = findViewById( R.id.rc_selected_photos);
//        mSelectedImageEmptyMessage = findViewById( R.id.selected_photos_empty);
//
//        view_selected_photos_container = findViewById( R.id.view_selected_photos_container);
//       /* view_selected_photos_container.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                view_selected_photos_container.getViewTreeObserver().removeOnPreDrawListener(this);
//
//                int selected_bottom_size = mConfig.getSelectedBottomHeight();
//
//                ViewGroup.LayoutParams params = view_selected_photos_container.getLayoutParams();
//                params.height = selected_bottom_size;
//                view_selected_photos_container.setLayoutParams(params);
//
//
//                return true;
//            }
//        });*/
//
//
//        if (mConfig.getSelectedBottomColor() > 0) {
//            tv_selected_title.setBackgroundColor(ContextCompat.getColor(this, mConfig.getSelectedBottomColor()));
//            mSelectedImageEmptyMessage.setTextColor(ContextCompat.getColor(this, mConfig.getSelectedBottomColor()));
//        }
//
//
//    }
//
//    private void setupFromSavedInstanceState(Bundle savedInstanceState) {
//
//
//        if (savedInstanceState != null) {
//            mSelectedImages = savedInstanceState.getParcelableArrayList(EXTRA_IMAGE_URIS);
//        } else {
//            mSelectedImages = getIntent().getParcelableArrayListExtra(EXTRA_IMAGE_URIS);
//        }
//
//
//        if (mSelectedImages == null) {
//            mSelectedImages = new ArrayList<>();
//        }
//
//
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        if (mSelectedImages != null) {
//            outState.putParcelableArrayList(EXTRA_IMAGE_URIS, mSelectedImages);
//        }
//
//    }
//
//    private void setupTabs() {
//        adapter = new PagerAdapter_Picker(this, getSupportFragmentManager());
//        mViewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(mViewPager);
//
//        if (mConfig.getTabSelectionIndicatorColor() > 0)
//            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, mConfig.getTabSelectionIndicatorColor()));
//
//    }
//
//    private void setSelectedPhotoRecyclerView() {
//
//
//        LinearLayoutManager mLayoutManager_Linear = new LinearLayoutManager(this);
//        mLayoutManager_Linear.setOrientation(LinearLayoutManager.HORIZONTAL);
//
//        rc_selected_photos.setLayoutManager(mLayoutManager_Linear);
////        rc_selected_photos.addItemDecoration(new SpacesItemDecoration(Util.dpToPx(this, 5), SpacesItemDecoration.TYPE_VERTICAL));
//        rc_selected_photos.setHasFixedSize(true);
//
//
//
//        int closeImageRes = mConfig.getSelectedCloseImage();
//
//        adapter_selectedPhoto = new Adapter_SelectedPhoto(this, closeImageRes);
//        adapter_selectedPhoto.updateItems(mSelectedImages);
//        rc_selected_photos.setAdapter(adapter_selectedPhoto);
//
//
//        if (mSelectedImages.size() >= 1) {
//            mSelectedImageEmptyMessage.setVisibility(View.GONE);
//        }
//    }
//
//
//    public GalleryFragment getGalleryFragment() {
//
//        if (adapter == null || adapter.getCount() < 2)
//            return null;
//
//        return (GalleryFragment) adapter.getItem(1);
//
//    }
//
//    public void addImage(final Uri uri) {
//
//
//        if (mSelectedImages.size() == mConfig.getSelectionLimit()) {
//            String text = String.format(getResources().getString( R.string.max_count_msg), mConfig.getSelectionLimit());
//            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
//
//
//            return;
//        }
//
//
//        mSelectedImages.add(uri);
//        adapter_selectedPhoto.updateItems(mSelectedImages);
//
//
//        if (mSelectedImages.size() >= 1) {
//            if(counting<9) {
//                counting++;
////                Toast.makeText(this, counting+"", Toast.LENGTH_SHORT).show();
//            }
//
//            tv_selected_title.setText("Select up to ("+counting+"/"+mConfig.getSelectionLimit()+") items");
//
//            mSelectedImageEmptyMessage.setVisibility(View.GONE);
//        }
//
//
//        rc_selected_photos.smoothScrollToPosition(adapter_selectedPhoto.getItemCount() - 1);
//        Log.d("item_count", "addImage: "+adapter_selectedPhoto.getItemCount());
//
//
//
//    }
//
//    public void removeImage(Uri uri) {
//
//
//        mSelectedImages.remove(uri);
//
//        adapter_selectedPhoto.updateItems(mSelectedImages);
//
//        if (mSelectedImages.size() == 0) {
//            mSelectedImageEmptyMessage.setVisibility(View.VISIBLE);
//        }
//        GalleryFragment.mGalleryAdapter.notifyDataSetChanged();
//
//        if(counting >= 0) {
//
//            counting--;
////            Toast.makeText(this, counting+"", Toast.LENGTH_SHORT).show();
//        }
//
//        tv_selected_title.setText("Select up to ("+counting+"/"+mConfig.getSelectionLimit()+") items");
//
//    }
//
//    public void clear() {
//        counting = 0;
//        int size = mSelectedImages.size();
//        if (size > 0) {
//            for (int i = 0; i < size; i++) {
//                mSelectedImages.remove(0);
//
//            }
//
//            GalleryFragment.mGalleryAdapter.notifyDataSetChanged();
//            adapter_selectedPhoto.updateItems(mSelectedImages);
//            tv_selected_title.setText("Select up to ("+counting+"/"+mConfig.getSelectionLimit()+") items");
//            if(counting >= 0) {
//                counting=0;
////            Toast.makeText(this, counting+"", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    public boolean containsImage(Uri uri) {
//        return mSelectedImages.contains(uri);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate( R.menu.menu_confirm, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            finish();
//            return true;
//        } else if (id ==  R.id.action_done) {
//            updatePicture();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void updatePicture() {
//
//        if (mSelectedImages.size() < mConfig.getSelectionMin()) {
//            String text = String.format(getResources().getString( R.string.min_count_msg), mConfig.getSelectionMin());
//            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (mSelectedImages.isEmpty()) {
//            Toast.makeText(this, "No images selected.", Toast.LENGTH_SHORT).show();
//            return;
//        }
////
////        if (mSelectedImages.size() < mConfig.getSelectionMin()) {
////            String text = String.format(getResources().getString(R.string.min_count_msg), mConfig.getSelectionMin());
////            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
////            return;
////        }
//
//        String a = getIntent().getStringExtra("FromActivity");
//        if(a.equals("CollageClick")) {
//            intentToMirror = new Intent(this, ProcessActivity.class);
//            intentToMirror.putExtra("conditionFromActivity", "CollageImage");
//            intentToMirror.putExtra("photo_path", mSelectedImages);
////            InterstitialAdUtils.INSTANCE.showInterstitialAd(ImagePickerActivity.this);//new implement
//            startActivity(intentToMirror);
//        } else if(a.equals("MirrorClick")) {
////            intentToMirror = new Intent(this, MirrorActivity.class);
////            intentToMirror.putExtra("photo_path", mSelectedImages.get(0));
////
////            InterstitialAdUtils.INSTANCE.showInterstitialAd(ImagePickerActivity.this, intentToMirror);
//
//        }else if(a.equals("EditClick")){
////             intentToMirror = new Intent(this, EditImageActivity.class);
////             intentToMirror.putExtra("conditionFromActivity", "MirrorActivity");
////            intentToMirror.putExtra("photo_path", mSelectedImages.get(0));
////            Log.d("priceoye", "updatePicture: "+mSelectedImages.get(0));
////
////            InterstitialAdUtils.INSTANCE.showInterstitialAd(ImagePickerActivity.this, intentToMirror);
//        } else if(a.equals("ReplaceImage")) {
//            Intent intentToCollageReplace = new Intent();
//            intentToCollageReplace.putExtra("photo_path_replace", String.valueOf(mSelectedImages.get(0)));
//            Log.d("replace_image", "updatePicture: "+mSelectedImages.get(0));
//              setResult(RESULT_OK, intentToCollageReplace);
//              finish();
//        }
//        Log.d("Selected_image", "updatePicture: "+EXTRA_IMAGE_URIS);
//        Log.d("Selected_image", "updatePicture: "+mSelectedImages);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(mAdView!=null) {
//            mAdView.destroy();
//        }
//    }
//}

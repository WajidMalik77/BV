package com.background.video.recorder.camera.recorder.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.background.video.recorder.camera.recorder.CollageMaker.OnImageSelectedListener;
import com.background.video.recorder.camera.recorder.ImagePicker.Adapter_SelectedPhoto;
import com.background.video.recorder.camera.recorder.ImagePicker.Config;
import com.background.video.recorder.camera.recorder.ImagePicker.OnCloseButtonClickListener;
import com.background.video.recorder.camera.recorder.ImagePicker.view.CustomSquareFrameLayout;
import com.background.video.recorder.camera.recorder.PhotoEditor.EditImageActivity;
import com.background.video.recorder.camera.recorder.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ImagePickerFragment extends Fragment implements OnImageSelectedListener, Adapter_SelectedPhoto.OnCloseButtonClickListener {

    // Part of ImagePickerFragment
    private GridView galleryGridView;
    private ImageGalleryAdapter mGalleryAdapter;
    private ArrayList<Uri> mSelectedImages;

    @Override
    public void onImageSelected(Uri imageUri) {
        // Add image to the selected images list
        addImage(imageUri);
    }

    @Override
    public void onImageUnselected(Uri imageUri) {
        // Remove image from the selected images list
        removeImage(imageUri);
    }

    public static final String EXTRA_IMAGE_URIS = "image_uris";
    // initialize with default config.
    private static Config mConfig = new Config();

    protected Toolbar toolbar;
    int counting = 0;
    View view_root;
    ImageView dltItems;
    TextView mSelectedImageEmptyMessage;
    View view_selected_photos_container;
    RecyclerView rc_selected_photos;
    TextView tv_selected_title;
    ViewPager mViewPager;

    Intent intentToMirror;
    AdView mAdView;
    TabLayout tabLayout;

    Adapter_SelectedPhoto adapter_selectedPhoto;

    public static Config getConfig() {
        return mConfig;
    }

    public static void setConfig(Config config) {

        if (config == null) {
            throw new NullPointerException("Config cannot be passed null. Not setting config will use default values.");
        }

        mConfig = config;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_confirm, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCloseButtonClick(Uri uri) {
        removeImage(uri);
    }

    int closeImageRes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment
        View view = inflater.inflate(R.layout.fragment_image_picker, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && toolbar != null) {
            activity.setSupportActionBar(toolbar);
        }
        initView(view);

//        setTitle(mConfig.getToolbarTitleRes());

        // Initialize mSelectedImages if it's null
        if (mSelectedImages == null) {
            mSelectedImages = new ArrayList<>();
        }
//        setupTabs();
        setSelectedPhotoRecyclerView();

        // Initialize the gallery grid view and adapter
        galleryGridView = view.findViewById(R.id.gallery_grid);
        mSelectedImages = new ArrayList<>();
//        mGalleryAdapter = new ImageGalleryAdapter(getActivity(), getImagesFromGallery(getActivity()));// old way
        mGalleryAdapter = new ImageGalleryAdapter(getActivity(), new ArrayList<>());

        // Fetch images
        fetchImages();

        closeImageRes = mConfig.getSelectedCloseImage();
//        adapter_selectedPhoto = new Adapter_SelectedPhoto(getActivity(), closeImageRes, this);
        galleryGridView.setAdapter(mGalleryAdapter);

        // Set the item click listener for the gallery
        galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Uri mUri = mGalleryAdapter.getItem(i);
                if (!containsImage(mUri)) {
                    addImage(mUri);
                } else {
                    removeImage(mUri);
                }
                mGalleryAdapter.notifyDataSetChanged();
            }
        });

        dltItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });

        return view;
    }

    private void fetchImages() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            // Background work here
            List<Uri> images = getImagesFromGallery(getActivity());
            handler.post(() -> {
                // UI Thread work here
                mGalleryAdapter.clear();
                mGalleryAdapter.addAll(images);
                mGalleryAdapter.notifyDataSetChanged();
            });
        });
    }

    public class ViewHolder {
        CustomSquareFrameLayout root;

        ImageView mThumbnail;

        // This is like storing too much data in memory.
        // find a better way to handle this
        Uri uri;

        public ViewHolder(View view) {
            root = (CustomSquareFrameLayout) view.findViewById(R.id.root);
            mThumbnail = (ImageView) view.findViewById(R.id.thumbnail_image);
        }

    }

    public class ImageGalleryAdapter extends ArrayAdapter<Uri> {

        Context context;


        public ImageGalleryAdapter(Context context, List<Uri> images) {
            super(context, 0, images);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.picker_grid_item_gallery_thumbnail, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            final Uri mUri = getItem(position);
            boolean isSelected = containsImage(mUri);


            if (holder.root instanceof FrameLayout) {
                ((FrameLayout) holder.root).setForeground(isSelected ? ResourcesCompat.getDrawable(getResources(), R.drawable.ic_cnew, null) : null);
            }


            if (holder.uri == null || !holder.uri.equals(mUri)) {


                Glide.with(context)
                        .load(mUri.toString())
                        .thumbnail(0.1f)
                        //.fit()
                        .dontAnimate()
                        //   .override(holder.mThumbnail.getWidth(), holder.mThumbnail.getWidth())
                        //  .override(holder.root.getWidth(), holder.root.getWidth())
                        .centerCrop()
                        .placeholder(R.drawable.place_holder_gallery)
                        .error(R.drawable.no_image)

                        .into(holder.mThumbnail);
                holder.uri = mUri;


            }


            return convertView;
        }
    }

    public List<Uri> getImagesFromGallery(Context context) {

        List<Uri> images = new ArrayList<Uri>();
        Log.e("GalleryImages", "getImagesFromGallary: " + images);
        Cursor imageCursor = null;
        try {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
            final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";


            imageCursor = context.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            while (imageCursor.moveToNext()) {
                Uri uri = Uri.parse(imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                images.add(uri);


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageCursor != null && !imageCursor.isClosed()) {
                imageCursor.close();
            }
        }


        return images;

    }

    private void initView(View view) {

        toolbar = view.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);


        view_root = view.findViewById(R.id.view_root);
        dltItems = view.findViewById(R.id.dltItems);
        mViewPager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tab_layout);

        tv_selected_title = view.findViewById(R.id.tv_selected_title);

        rc_selected_photos = view.findViewById(R.id.rc_selected_photos);
        mSelectedImageEmptyMessage = view.findViewById(R.id.selected_photos_empty);

        view_selected_photos_container = view.findViewById(R.id.view_selected_photos_container);
       /* view_selected_photos_container.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view_selected_photos_container.getViewTreeObserver().removeOnPreDrawListener(this);

                int selected_bottom_size = mConfig.getSelectedBottomHeight();

                ViewGroup.LayoutParams params = view_selected_photos_container.getLayoutParams();
                params.height = selected_bottom_size;
                view_selected_photos_container.setLayoutParams(params);


                return true;
            }
        });*/


        if (mConfig.getSelectedBottomColor() > 0) {
            tv_selected_title.setBackgroundColor(ContextCompat.getColor(getActivity(), mConfig.getSelectedBottomColor()));
            mSelectedImageEmptyMessage.setTextColor(ContextCompat.getColor(getActivity(), mConfig.getSelectedBottomColor()));
        }


    }

    private void setupFromSavedInstanceState(Bundle savedInstanceState) {


        if (savedInstanceState != null) {
            mSelectedImages = savedInstanceState.getParcelableArrayList(EXTRA_IMAGE_URIS);
        } else {
            mSelectedImages = getArguments().getParcelableArrayList(EXTRA_IMAGE_URIS);
        }


        if (mSelectedImages == null) {
            mSelectedImages = new ArrayList<>();
        }


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mSelectedImages != null) {
            outState.putParcelableArrayList(EXTRA_IMAGE_URIS, mSelectedImages);
        }
    }


//    private void setupTabs() {
//        adapter = new PagerAdapter_Picker(getActivity(), getActivity().getSupportFragmentManager());
//        mViewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(mViewPager);
//
//        if (mConfig.getTabSelectionIndicatorColor() > 0)
//            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), mConfig.getTabSelectionIndicatorColor()));
//
//    }

    private void setSelectedPhotoRecyclerView() {


        LinearLayoutManager mLayoutManager_Linear = new LinearLayoutManager(getActivity());
        mLayoutManager_Linear.setOrientation(LinearLayoutManager.HORIZONTAL);

        rc_selected_photos.setLayoutManager(mLayoutManager_Linear);
//        rc_selected_photos.addItemDecoration(new SpacesItemDecoration(Util.dpToPx(getActivity(), 5), SpacesItemDecoration.TYPE_VERTICAL));
        rc_selected_photos.setHasFixedSize(true);


//        Adapter_SelectedPhoto adapter = new Adapter_SelectedPhoto(getActivity(), closeImageRes, this);
        adapter_selectedPhoto = new Adapter_SelectedPhoto(getActivity(), closeImageRes, this);
        adapter_selectedPhoto.updateItems(mSelectedImages);
        rc_selected_photos.setAdapter(adapter_selectedPhoto);


        if (mSelectedImages.size() >= 1) {
            mSelectedImageEmptyMessage.setVisibility(View.GONE);
        }
    }


//    public com.background.video.recorder.camera.recorder.ImagePicker.GalleryFragment getGalleryFragment() {
//
//        if (adapter == null || adapter.getCount() < 2)
//            return null;
//
//        return adapter.getItem(1);
//
//    }

    public void addImage(final Uri uri) {


        if (mSelectedImages.size() == mConfig.getSelectionLimit()) {
            String text = String.format(getResources().getString(R.string.max_count_msg), mConfig.getSelectionLimit());
            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();


            return;
        }


        mSelectedImages.add(uri);
        adapter_selectedPhoto.updateItems(mSelectedImages);


        if (mSelectedImages.size() >= 1) {
            if (counting < 9) {
                counting++;
//                Toast.makeText(getActivity(), counting+"", Toast.LENGTH_SHORT).show();
            }

            tv_selected_title.setText("Select up to (" + counting + "/" + mConfig.getSelectionLimit() + ") items");

            mSelectedImageEmptyMessage.setVisibility(View.GONE);
        }


        rc_selected_photos.smoothScrollToPosition(adapter_selectedPhoto.getItemCount() - 1);
        Log.d("item_count", "addImage: " + adapter_selectedPhoto.getItemCount());


    }

    public void removeImage(Uri uri) {


        mSelectedImages.remove(uri);

        adapter_selectedPhoto.updateItems(mSelectedImages);

        if (mSelectedImages.size() == 0) {
            mSelectedImageEmptyMessage.setVisibility(View.VISIBLE);
        }
        mGalleryAdapter.notifyDataSetChanged();

        if (counting >= 0) {

            counting--;
//            Toast.makeText(getActivity(), counting+"", Toast.LENGTH_SHORT).show();
        }

        tv_selected_title.setText("Select up to (" + counting + "/" + mConfig.getSelectionLimit() + ") items");

    }

    public void clear() {
        counting = 0;
        int size = mSelectedImages.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                mSelectedImages.remove(0);

            }

            mGalleryAdapter.notifyDataSetChanged();
            adapter_selectedPhoto.updateItems(mSelectedImages);
            tv_selected_title.setText("Select up to (" + counting + "/" + mConfig.getSelectionLimit() + ") items");
            if (counting >= 0) {
                counting = 0;
//            Toast.makeText(getActivity(), counting+"", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean containsImage(Uri uri) {
        return mSelectedImages.contains(uri);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.menu_confirm, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
//            finish();
            return true;
        } else if (id == R.id.action_done) {
            updatePicture();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updatePicture() {

        if (mSelectedImages.size() < mConfig.getSelectionMin()) {
            String text = String.format(getResources().getString(R.string.min_count_msg), mConfig.getSelectionMin());
            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            return;
        }

        if (mSelectedImages.isEmpty()) {
            Toast.makeText(getActivity(), "No images selected.", Toast.LENGTH_SHORT).show();
            return;
        }
//
//        if (mSelectedImages.size() < mConfig.getSelectionMin()) {
//            String text = String.format(getResources().getString(R.string.min_count_msg), mConfig.getSelectionMin());
//            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
//            return;
//        }

        String a = getArguments().getString("FromActivity");
//        if (a.equals("CollageClick")) {
//            intentToMirror = new Intent(getActivity(), ProcessActivity.class);
//            intentToMirror.putExtra("conditionFromActivity", "CollageImage");
//            intentToMirror.putExtra("photo_path", mSelectedImages);
////            InterstitialAdUtils.INSTANCE.showInterstitialAd(ImagePickerActivity.getActivity());//new implement
//            startActivity(intentToMirror);
//        }
        if (a.equals("CollageClick")) {
            // Prepare the bundle with data
            Bundle bundle = new Bundle();
            bundle.putString("conditionFromActivity", "CollageImage");
            bundle.putParcelableArrayList("photo_path", mSelectedImages);

            // Use NavController to navigate
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_imagePickerFragment_to_processFragment, bundle);

            // Optionally, show interstitial ad if needed
            // InterstitialAdUtils.INSTANCE.showInterstitialAd(getActivity());
        } else if (a.equals("MirrorClick")) {
//            intentToMirror = new Intent(getActivity(), MirrorActivity.class);
//            intentToMirror.putExtra("photo_path", mSelectedImages.get(0));
//
//            InterstitialAdUtils.INSTANCE.showInterstitialAd(ImagePickerActivity.getActivity(), intentToMirror);

        } else if (a.equals("EditClick")) {

            Bundle bundle = new Bundle();
            bundle.putString("conditionFromActivity", "MirrorActivity");
            bundle.putParcelableArrayList("photo_path", mSelectedImages);

            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_imagePickerFragment_to_editImageFragment, bundle);

//            intentToMirror = new Intent(getActivity(), EditImageActivity.class);
//            intentToMirror.putExtra("conditionFromActivity", "MirrorActivity");
//            intentToMirror.putExtra("photo_path", mSelectedImages.get(0));
//            Log.d("priceoye", "updatePicture: " + mSelectedImages.get(0));
//            getActivity().startActivity(intentToMirror);

//            InterstitialAdUtils.INSTANCE.showInterstitialAd(ImagePickerActivity.getActivity(), intentToMirror);
        }
//        else if (a.equals("ReplaceImage")) {
//            Intent intentToCollageReplace = new Intent();
//            intentToCollageReplace.putExtra("photo_path_replace", String.valueOf(mSelectedImages.get(0)));
//            Log.d("replace_image", "updatePicture: " + mSelectedImages.get(0));
//            getsetResult(RESULT_OK, intentToCollageReplace);
//            finish();
//        }
        Log.d("Selected_image", "updatePicture: " + EXTRA_IMAGE_URIS);
        Log.d("Selected_image", "updatePicture: " + mSelectedImages);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mAdView != null) {
//            mAdView.destroy();
//        }
//    }


    private AppCompatButton nextBtn;
    private ImageView back;

    @Override
    public void onResume() {
        super.onResume();
        if (nextBtn == null) {
            nextBtn = requireActivity().findViewById(R.id.nextBtn);
            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updatePicture();
                }
            });
        }
    }

}
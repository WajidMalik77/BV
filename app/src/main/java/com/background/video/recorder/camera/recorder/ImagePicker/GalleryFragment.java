//package com.background.video.recorder.camera.recorder.ImagePicker;
//
//import android.content.ContentUris;
//import android.content.Context;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.FrameLayout;
//import android.widget.GridView;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.core.content.res.ResourcesCompat;
//import androidx.fragment.app.Fragment;
//
//import com.background.video.recorder.camera.recorder.CollageMaker.OnImageSelectedListener;
//import com.background.video.recorder.camera.recorder.ImagePicker.view.CustomSquareFrameLayout;
//import com.background.video.recorder.camera.recorder.R;
//import com.background.video.recorder.camera.recorder.ui.fragment.ImagePickerFragment;
//import com.bumptech.glide.Glide;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class GalleryFragment extends Fragment {
//    public OnImageSelectedListener mListener;
//
//    public static ImageGalleryAdapter mGalleryAdapter;
//    public static ImagePickerFragment mActivity;
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        try {
//            mListener = (OnImageSelectedListener) getParentFragment();
//        } catch (ClassCastException e) {
//            throw new ClassCastException(getParentFragment().toString() + " must implement OnImageSelectedListener");
//        }
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View rootView = inflater.inflate(R.layout.picker_fragment_gallery, container, false);
//        GridView galleryGridView = (GridView) rootView.findViewById(R.id.gallery_grid);
//
//
////        mActivity = ((ImagePickerFragment) getActivity());
//
//        Fragment parentFragment = getParentFragment();
//        if (parentFragment instanceof ImagePickerFragment) {
//            mActivity = (ImagePickerFragment) parentFragment;
//        } else {
//            throw new IllegalStateException("Parent fragment must be ImagePickerFragment");
//        }
//
//
//        List<Uri> images = getImagesFromGallary(getActivity());
//        mGalleryAdapter = new ImageGalleryAdapter(getActivity(), images);
//
//        galleryGridView.setAdapter(mGalleryAdapter);
//        galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//
//                Uri mUri = mGalleryAdapter.getItem(i);
//
//
//                if (!mActivity.containsImage(mUri)) {
//                    mActivity.addImage(mUri);
//                } else {
//                    mActivity.removeImage(mUri);
//
//                }
//
//                mGalleryAdapter.notifyDataSetChanged();
//            }
//
////            @Override
////            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                Uri mUri = mGalleryAdapter.getItem(i);
////                if (mListener != null) {
////                    if (!mActivity.containsImage(mUri)) {
////                        mListener.onImageSelected(mUri);
////                    } else {
////                        mListener.onImageUnselected(mUri);
////                    }
////                }
////            }
//
////            @Override
////            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                Uri mUri = mGalleryAdapter.getItem(i);
////                if (mListener != null) {
////                    if (!containsImage(mUri)) { // Update this method to check image selection
////                        mListener.onImageSelected(mUri);
////                    } else {
////                        mListener.onImageUnselected(mUri);
////                    }
////                }
////            }
//
//        });
//
//        return rootView;
//    }
//
//
//    public void refreshGallery(Context context) {
//
//        List<Uri> images = getImagesFromGallary(context);
//
//        if (mGalleryAdapter == null) {
//
//            mGalleryAdapter = new ImageGalleryAdapter(context, images);
//        } else {
//
//            mGalleryAdapter.clear();
//            mGalleryAdapter.addAll(images);
//            mGalleryAdapter.notifyDataSetChanged();
//
//        }
//
//
//    }
//
//
//    // Perfectly running but not for andorid 10
//    public List<Uri> getImagesFromGallary(Context context) {
//
//        List<Uri> images = new ArrayList<Uri>();
//        Log.e("GalleryImages", "getImagesFromGallary: " + images);
//        Cursor imageCursor = null;
//        try {
//            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
//            final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
//
//
//            imageCursor = context.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
//            while (imageCursor.moveToNext()) {
//                Uri uri = Uri.parse(imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
//                images.add(uri);
//
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (imageCursor != null && !imageCursor.isClosed()) {
//                imageCursor.close();
//            }
//        }
//
//
//        return images;
//
//    }
//
////    public List<Uri> getImagesFromGallary(Context context) {
//
////        List<Uri> images = new ArrayList<>();
////        Cursor imageCursor = null;
////        try {
////            final String[] columns = {MediaStore.Images.Media._ID}; // Change here
////            final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
////
////            imageCursor = context.getContentResolver().query(
////                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
////                    columns,
////                    null,
////                    null,
////                    orderBy
////            );
////
////            while (imageCursor.moveToNext()) {
////                long id = imageCursor.getLong(imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
////                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id); // Construct URI
////                images.add(imageUri);
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        } finally {
////            if (imageCursor != null) {
////                imageCursor.close();
////            }
////        }
////
////        return images;
////    }
//
//
//    public class ViewHolder {
//        CustomSquareFrameLayout root;
//
//        ImageView mThumbnail;
//
//        // This is like storing too much data in memory.
//        // find a better way to handle this
//        Uri uri;
//
//        public ViewHolder(View view) {
//            root = (CustomSquareFrameLayout) view.findViewById(R.id.root);
//            mThumbnail = (ImageView) view.findViewById(R.id.thumbnail_image);
//        }
//
//    }
//
//    public class ImageGalleryAdapter extends ArrayAdapter<Uri> {
//
//        Context context;
//
//
//        public ImageGalleryAdapter(Context context, List<Uri> images) {
//            super(context, 0, images);
//            this.context = context;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            final ViewHolder holder;
//            if (convertView == null) {
//                convertView = LayoutInflater.from(getContext()).inflate(R.layout.picker_grid_item_gallery_thumbnail, null);
//                holder = new ViewHolder(convertView);
//
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//
//            final Uri mUri = getItem(position);
//            boolean isSelected = mActivity.containsImage(mUri);
//
//
//            if (holder.root instanceof FrameLayout) {
//                ((FrameLayout) holder.root).setForeground(isSelected ? ResourcesCompat.getDrawable(getResources(), R.drawable.ic_cnew, null) : null);
//            }
//
//
//            if (holder.uri == null || !holder.uri.equals(mUri)) {
//
//
//                Glide.with(context)
//                        .load(mUri.toString())
//                        .thumbnail(0.1f)
//                        //.fit()
//                        .dontAnimate()
//                        //   .override(holder.mThumbnail.getWidth(), holder.mThumbnail.getWidth())
//                        //  .override(holder.root.getWidth(), holder.root.getWidth())
//                        .centerCrop()
//                        .placeholder(R.drawable.place_holder_gallery)
//                        .error(R.drawable.no_image)
//
//                        .into(holder.mThumbnail);
//                holder.uri = mUri;
//
//
//            }
//
//
//            return convertView;
//        }
//    }
//}
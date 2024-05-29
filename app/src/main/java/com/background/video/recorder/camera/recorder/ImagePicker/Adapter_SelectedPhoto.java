package com.background.video.recorder.camera.recorder.ImagePicker;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.background.video.recorder.camera.recorder.ImagePicker.adapter.BaseRecyclerViewAdapter;
import com.background.video.recorder.camera.recorder.R;
import com.bumptech.glide.Glide;

//public class Adapter_SelectedPhoto extends BaseRecyclerViewAdapter<Uri, Adapter_SelectedPhoto.SelectedPhotoHolder> {
//
//
//
//    int closeImageRes;
//
//    ImagePickerActivity imagePickerActivity;
//
//    public Adapter_SelectedPhoto(ImagePickerActivity imagePickerActivity, int closeImageRes) {
//
//        super(imagePickerActivity);
//        this.imagePickerActivity = imagePickerActivity;
//        this.closeImageRes = closeImageRes;
//
//    }
//
//    @Override
//    public void onBindView(SelectedPhotoHolder holder, int position) {
//
//        Uri uri = getItem(position);
//
//
//
//        Glide.with(imagePickerActivity)
//                .load(uri.toString())
//             //   .override(selected_bottom_size, selected_bottom_size)
//                .dontAnimate()
//                .error( R.drawable.no_image)
//                .into(holder.selected_photo);
//
//
//
//
//        holder.iv_close.setTag(uri);
//
//
//
//    }
//
//    @Override
//    public SelectedPhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        LayoutInflater mInflater = LayoutInflater.from(getContext());
//        View view = mInflater.inflate( R.layout.picker_list_item_selected_thumbnail, parent, false);
//        return new SelectedPhotoHolder(view);
//    }
//
//
//
//
//
//    class SelectedPhotoHolder extends RecyclerView.ViewHolder {
//
//
//        ImageView selected_photo;
//        ImageView iv_close;
//
//
//        public SelectedPhotoHolder(View itemView) {
//            super(itemView);
//            selected_photo = (ImageView) itemView.findViewById( R.id.selected_photo);
//            iv_close = (ImageView) itemView.findViewById( R.id.iv_close);
//            iv_close.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Uri uri = (Uri) view.getTag();
//                    imagePickerActivity.removeImage(uri);
//
//
//                }
//            });
//
//        }
//    }
//}

public class Adapter_SelectedPhoto extends BaseRecyclerViewAdapter<Uri, Adapter_SelectedPhoto.SelectedPhotoHolder> {

    private int closeImageRes;
    private OnCloseButtonClickListener closeButtonClickListener;
    private FragmentActivity fragmentActivity;

    public Adapter_SelectedPhoto(FragmentActivity fragmentActivity, int closeImageRes, OnCloseButtonClickListener listener) {
        super(fragmentActivity);
        this.fragmentActivity = fragmentActivity;
        this.closeImageRes = closeImageRes;
        this.closeButtonClickListener = listener; // Set the click listener
    }

    // Define an interface for the close button click listener
    public interface OnCloseButtonClickListener {
        void onCloseButtonClick(Uri uri);
    }

    @Override
    public void onBindView(SelectedPhotoHolder holder, int position) {
        Uri uri = getItem(position);

        Glide.with(fragmentActivity)
                .load(uri.toString())
                .dontAnimate()
                .error(R.drawable.no_image)
                .into(holder.selected_photo);

        holder.iv_close.setTag(uri);
    }

    @Override
    public SelectedPhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.picker_list_item_selected_thumbnail, parent, false);
        return new SelectedPhotoHolder(view);
    }

    class SelectedPhotoHolder extends RecyclerView.ViewHolder {
        ImageView selected_photo;
        ImageView iv_close;

        public SelectedPhotoHolder(View itemView) {
            super(itemView);
            selected_photo = itemView.findViewById(R.id.selected_photo);
            iv_close = itemView.findViewById(R.id.iv_close);

            // Set the click listener for the close button
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = (Uri) view.getTag();
                    if (closeButtonClickListener != null) {
                        closeButtonClickListener.onCloseButtonClick(uri);
                    }
                }
            });
        }
    }
}

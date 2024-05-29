package com.background.video.recorder.camera.recorder.adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.IconCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.databinding.LayoutFilesBinding;
import com.background.video.recorder.camera.recorder.model.MediaFiles;
import com.background.video.recorder.camera.recorder.util.constant.Constants;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    private static final String TAG = "FileAdapter";
    private final Context c;
    private final List<MediaFiles> videoInformationList;
    private final LayoutFilesBinding binding;
    private final OnLongClickedListener longClickedListener;
    private final List<MediaFiles> listOfSelectedItem = new ArrayList<>();
    private final ListForOperationListener operationListener;
    private final ArrayList<ViewHolder> holdersForSelectedItem = new ArrayList<>();
    private final ArrayList<ViewHolder> holders = new ArrayList<>();
    private final int[] arr = new int[150];
    private final Activity activity;
    public List<MediaFiles> allSelectedFile = new ArrayList<>();
    int index = 0;
    private VideoClickListener videoClickListener;
    private ViewHolder holder;

    public FileAdapter(Context c, List<MediaFiles> mediaFile, LayoutFilesBinding binding, VideoClickListener videoClickListener,
                       OnLongClickedListener longClickedListener, ListForOperationListener list, Activity activity) {
        this.c = c;
        this.videoInformationList = mediaFile;
        this.binding = binding;
        this.videoClickListener = videoClickListener;
        this.longClickedListener = longClickedListener;
        this.operationListener = list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e(TAG, "onCreateViewHolder: " + viewType);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_files_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        this.holder = holder;
        holders.add(holder);
        listOfSelectedItem.clear();

        if (videoInformationList.get(position).getType() == Constants.MEDIA_TYPE_FAVOURITE) {
            holder.ibSelectFav.setImageResource(R.drawable.ic_heart_file);
        } else {
            holder.ibSelectFav.setImageResource(R.drawable.ic_non_fav);
        }


        String currMediaFilePath = videoInformationList.get(position).getPath();
        MediaFiles currentmediaFiles = videoInformationList.get(position);


        // holder.thumbnail.setImageBitmap(videoInformationList.get(position));
        try {
            Glide.with(c)
                    .load(videoInformationList.get(position).getPath())
                    .centerCrop()
                    .into(holder.thumbnail);
        } catch (Exception e) {
            e.fillInStackTrace();
        }

        // Item Click listener
        holder.thumbnail.setOnClickListener(view -> {

            if (Constants.LONG_CLICKED_ENABLED) {
                if (listOfSelectedItem.contains(videoInformationList.get(position))) {
                    holder.ibSelectItem.setVisibility(View.GONE);
                    listOfSelectedItem.remove(videoInformationList.get(position));
                    holder.thumbnail.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else {
                    index++;
                    holder.ibSelectItem.setVisibility(View.VISIBLE);
                    listOfSelectedItem.add(videoInformationList.get(position));
                    holder.thumbnail.setBackgroundColor(Color.parseColor(c.getString(R.string.selected_item_color)));
                    operationListener.selectedItemList(listOfSelectedItem);
                    holdersForSelectedItem.add(this.holder);
                    arr[index] = holder.getAbsoluteAdapterPosition();
                    Log.e(TAG, "onBindViewHolder: " + listOfSelectedItem);
                }

            } else {


                holder.ibSelectItem.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putString("videoPath", currMediaFilePath);
                NavigationUtils.navigate((Activity) activity, R.id.action_filesFragment_to_exoplayerFragment, bundle);
            }

        });
        holder.thumbnail.setOnLongClickListener(view -> {

            Constants.LONG_CLICKED_ENABLED = true;
            longClickedListener.onLongClicked(true);
            listOfSelectedItem.add(videoInformationList.get(position)); // adding files to the list for deleting or updating or sharing
            holder.ibSelectItem.setVisibility(View.VISIBLE);
            holder.thumbnail.setBackgroundColor(Color.parseColor(c.getString(R.string.selected_item_color)));
            operationListener.selectedItemList(listOfSelectedItem);
            holdersForSelectedItem.add(this.holder);
            arr[index] = holder.getAbsoluteAdapterPosition();
            allSelectedFile.addAll(videoInformationList);

            return true;
        });
        holder.ibSelectFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoClickListener.onVideoClicked(currentmediaFiles);
                videoInformationList.remove(currentmediaFiles);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoInformationList.size();
    }

    /**
     * GETTERS & SETTERS
     **/

    public void selectAll() {
        Log.e(TAG, "selectAll: " + videoInformationList.size());
        try {
            if (Constants.SELECT_ALL) {
                for (int i = 0; i < getItemCount(); i++) {
                    holders.get(i).ibSelectItem.setVisibility(View.VISIBLE);
                }
            }
        } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            arrayIndexOutOfBoundsException.getLocalizedMessage();
        }

    }

    public void cancelAll() {
        try {
            if (Constants.SELECT_ALL) {
                Log.e(TAG, "cancelAll: " + " yess");
                for (int i = 0; i < arr.length; i++) {
                    holders.get(i).ibSelectItem.setVisibility(View.GONE);
                }
                Constants.SELECT_ALL = false;
                Constants.LONG_CLICKED_ENABLED = false;
            }
        } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            arrayIndexOutOfBoundsException.getLocalizedMessage();
        }

    }

    public void unSelect() {
        try {
            Log.e(TAG, "unSelect: arr size " + arr.length);
            Log.e(TAG, "unSelect: " + holdersForSelectedItem.size() + Constants.LONG_CLICKED_ENABLED);
            if (Constants.LONG_CLICKED_ENABLED) {
                for (int i = 0; i < videoInformationList.size(); i++) {
                    holders.get(i).ibSelectItem.setVisibility(View.GONE);
                }
                Constants.LONG_CLICKED_ENABLED = false;
                listOfSelectedItem.clear();
                Log.e(TAG, "unSelect: selected item list size is ==" + listOfSelectedItem.size());
            } else {
                Log.e(TAG, "unSelect: " + "Long clicked is not enabled ");
            }
        } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            arrayIndexOutOfBoundsException.getLocalizedMessage();
        }

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public interface VideoClickListener {
        void onVideoClicked(MediaFiles mediaFiles);
    }

    public interface OnLongClickedListener {
        void onLongClicked(boolean longClicked);
    }

    public interface ListForOperationListener {
        void selectedItemList(List<MediaFiles> list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        ImageButton ibSelectItem, ibSelectFav;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.ivThumbnail);
            ibSelectFav = itemView.findViewById(R.id.ibSelectFav);
            ibSelectItem = itemView.findViewById(R.id.ibSelectedItem);
        }
    }

}

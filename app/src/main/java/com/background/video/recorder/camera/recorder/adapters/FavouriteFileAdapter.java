package com.background.video.recorder.camera.recorder.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.databinding.LayoutFavFilesBinding;
import com.background.video.recorder.camera.recorder.model.MediaFiles;
import com.background.video.recorder.camera.recorder.util.constant.Constants;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFileAdapter extends RecyclerView.Adapter<FavouriteFileAdapter.ViewHolder> {
    private static final String TAG = "FavouriteFileAdapter";
    private final Context context;
    private final List<MediaFiles> favFiles;
    private final UpdateFavFile updateFavFile;
    private final LayoutFavFilesBinding binding;
    private final List<MediaFiles> listOfSelectedItemFav = new ArrayList<>();
    private final int[] arrFavFileHolderPos = new int[100];
    private final ArrayList<ViewHolder> holders = new ArrayList<>();
    private final Activity activity;
    public List<MediaFiles> allSelectedFavFile = new ArrayList<>();
    int indexFav = 0;
    private OnLongClickedListenerFavFile longClickedListenerFavFile;
    private ListForOperationListenerFavFile operationListenerFavFile;


    public FavouriteFileAdapter(Context context, List<MediaFiles> favFiles, LayoutFavFilesBinding binding
            , UpdateFavFile updateFavFile, Activity activity) {
        this.context = context;
        this.favFiles = favFiles;
        this.updateFavFile = updateFavFile;
        this.binding = binding;
        this.activity = activity;


//        this.longClickedListenerFavFile = longClickedListenerFavFile;
//        this.operationListenerFavFile = operationListenerFavFile;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_fav_files_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holders.add(holder);
        listOfSelectedItemFav.clear();


        MediaFiles currentFile = favFiles.get(position);
        String currentFilePath = favFiles.get(position).getPath();


//        holder.ivThumbnail.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//
//                Constants.LONG_CLICKED_ENABLED_FAV_FILE = true;
//                longClickedListenerFavFile.onLongClicked(true);
//                listOfSelectedItemFav.add(currentFile);
//                operationListenerFavFile.selectedItemList(listOfSelectedItemFav);
//                holder.ibFavouriteSelected.setVisibility(View.VISIBLE);
//                allSelectedFavFile.addAll(favFiles);
//                arrFavFileHolderPos[indexFav] = holder.getAbsoluteAdapterPosition();
//                return true;
//            }
//        });
        holder.ibFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFavFile.fileUpdatePosition(currentFile);
            }
        });

        holder.ivThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constants.FAV_FILE_CLICK = true;
                Bundle bundle = new Bundle();
                bundle.putString("videoPath", currentFilePath);
                NavigationUtils.navigate((Activity) context, R.id.action_favFilesFragment_to_exoplayerFragment, bundle);


//                if (Constants.LONG_CLICKED_ENABLED_FAV_FILE) {
//                    if (listOfSelectedItemFav.contains(currentFile)) {
//                        holder.ibFavouriteSelected.setVisibility(View.INVISIBLE);
//                        listOfSelectedItemFav.remove(currentFile);
//                    } else {
//                        indexFav++;
//                        holder.ibFavouriteSelected.setVisibility(View.VISIBLE);
//                        listOfSelectedItemFav.add(currentFile);
//                        operationListenerFavFile.selectedItemList(listOfSelectedItemFav);
//                        arrFavFileHolderPos[indexFav] = holder.getAbsoluteAdapterPosition();
//                        Log.e(TAG, "onBindViewHolder: " + listOfSelectedItemFav.toString());
//                    }
//
//                } else {
//                    Constants.FAV_FILE_CLICK = true;
//                    Bundle bundle = new Bundle();
//                    bundle.putString("videoPath",currentFilePath);
//                    NavigationUtils.navigate(binding.getRoot(),R.id.action_favFilesFragment_to_exoplayerFragment,bundle);
//                }


            }
        });
        Glide.with(context)
                .load(favFiles.get(position).getPath())
                .centerCrop()
                .into(holder.ivThumbnail);
    }

    @Override
    public int getItemCount() {
        return favFiles.size();
    }

    public void selectAllFavFile() {
        Log.e(TAG, "selectAllFavFile: " + favFiles.size());
        if (Constants.SELECT_ALL) {
            for (int i = 0; i < getItemCount(); i++) {
                holders.get(i).ibFavouriteSelected.setVisibility(View.VISIBLE);
            }
        }
    }

    public void cancelAllFavFile() {
        if (Constants.SELECT_ALL) {
            for (int i = 0; i < favFiles.size(); i++) {
                holders.get(i).ibFavouriteSelected.setVisibility(View.INVISIBLE);
            }
            Constants.SELECT_ALL_FAV_FILE = false;
        }
    }

    public void unSelect() {
        if (Constants.LONG_CLICKED_ENABLED) {
            for (int i = 0; i < arrFavFileHolderPos.length; i++) {
                holders.get(arrFavFileHolderPos[i]).ibFavouriteSelected.setVisibility(View.INVISIBLE);
            }
            Constants.LONG_CLICKED_ENABLED = false;
            listOfSelectedItemFav.clear();
            Log.e(TAG, "unSelect: selected item list size is ==" + listOfSelectedItemFav.size());
        } else {
            Log.e(TAG, "unSelect: " + "Long clicked is not enabled ");
        }
    }

    public interface OnLongClickedListenerFavFile {
        void onLongClicked(boolean longClicked);
    }

    public interface ListForOperationListenerFavFile {
        void selectedItemList(List<MediaFiles> list);
    }

    public interface UpdateFavFile {
        void fileUpdatePosition(MediaFiles mediaFiles);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        ImageButton ibFavouriteSelected, ibFav;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            ibFav = itemView.findViewById(R.id.ibFav);
            ibFavouriteSelected = itemView.findViewById(R.id.ibFavSelect);
        }
    }

}

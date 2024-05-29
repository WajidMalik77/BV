package com.background.video.recorder.camera.recorder.CollageMaker;


import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.background.video.recorder.camera.recorder.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAlbumAdapter extends RecyclerView.Adapter<MyAlbumAdapter.ViewHolder> {

    // initialize variables
    Activity activity;
    public static ArrayList<String> arrayList;
    //TextView tvEmpty;
//    MainViewModel mainViewModel;

    recyclerViewInterfaces recyclerViewInterfaces;
    boolean isEnable = false;
    boolean isSelectAll = false;
    ArrayList<String> selectList = new ArrayList<>();
    public static int pos = 0;
    public static Uri uri = null;
    Dialog dialogDelete;
    ArrayList<Uri> deletedUri = new ArrayList<>();
    ArrayList<String> deletedString = new ArrayList<>();
    private boolean zoomOut =  false;

    // create constructor
    public MyAlbumAdapter(Activity activity, ArrayList<String> arrayList, recyclerViewInterfaces recyclerViewInterfaces) {
        this.recyclerViewInterfaces = recyclerViewInterfaces;
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // initialize variables
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_item, parent, false);

        // initialize view Model
//        mainViewModel = ViewModelProviders.of((FragmentActivity) activity)
//                .get(MainViewModel.class);

        // return view
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Glide.with(activity)
                .load(arrayList.get(position))
                .fitCenter()
                .into(holder.textView);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // initialize variables
        ImageView textView;
        ImageView checkbox;
        ImageView tick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // assign variables
            textView = itemView.findViewById(R.id.image);
            checkbox = itemView.findViewById(R.id.check_box);
            tick = itemView.findViewById(R.id.tick);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewInterfaces.onClick(getAdapterPosition());
                }
            });

        }
    }

}


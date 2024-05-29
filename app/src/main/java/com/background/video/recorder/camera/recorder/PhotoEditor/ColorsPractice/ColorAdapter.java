package com.background.video.recorder.camera.recorder.PhotoEditor.ColorsPractice;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.background.video.recorder.camera.recorder.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {
    private List<ColorModel> colorList;
    private Context context;
    private ImageModelInterface imageModelInterface;
    private RecyclerView recyclerView;

    public ColorAdapter(List<ColorModel> colorList, Context context, ImageModelInterface imageModelInterface, RecyclerView recyclerView) {
        this.colorList = colorList;
        this.context = context;
        this.imageModelInterface = imageModelInterface;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        ColorModel color = colorList.get(position);
//        holder.colorView.setBackgroundColor(color.getColorValue());
//        holder.colorName.setText(color.getColorName());

        // Load image from URI using Glide
        // Load and display image from URI using Glide
        Glide.with(context)
                .load(color.getImageUri())
                .into(holder.iv_bg);

        holder.colorView.setBackgroundColor(color.getColorValue());
        holder.colorName.setText(color.getColorName());    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {
        ImageView colorView, iv_bg;
        TextView colorName;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            colorView = itemView.findViewById(R.id.color_view);
            iv_bg = itemView.findViewById(R.id.iv_bg);
            colorName = itemView.findViewById(R.id.color_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageModelInterface.onClick(getAdapterPosition());
                }
            });
        }
    }
}

package com.background.video.recorder.camera.recorder.PhotoEditor.tools;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.background.video.recorder.camera.recorder.R;

import java.util.ArrayList;
import java.util.List;


public class EditingToolsAdapter extends RecyclerView.Adapter<EditingToolsAdapter.ViewHolder> {

    private List<ToolModel> mToolList = new ArrayList<>();
    private OnItemSelected mOnItemSelected;

    private int lastPosition = -1;
    int row_index = -1;



    public EditingToolsAdapter(OnItemSelected onItemSelected) {
        mOnItemSelected = onItemSelected;

        mToolList.add(new ToolModel("Light Effect", R.drawable.filters_icon, ToolType.FILTER));
//        mToolList.add(new ToolModel("Crop", R.drawable.ic_croppy, ToolType.CROP));
        mToolList.add(new ToolModel("Text", R.drawable.text_icon, ToolType.TEXT));
        mToolList.add(new ToolModel("Shape", R.drawable.shape_icon, ToolType.SHAPE));
        mToolList.add(new ToolModel("Eraser", R.drawable.eraser_icon, ToolType.ERASER));
        mToolList.add(new ToolModel("Emoji", R.drawable.emoji_icon, ToolType.EMOJI));
        mToolList.add(new ToolModel("Sticker", R.drawable.sticker_icon, ToolType.STICKER));
    }

    public interface OnItemSelected {
        void onToolSelected(ToolType toolType);
    }

    class ToolModel {
        private String mToolName;
        private int mToolIcon;
        private ToolType mToolType;

        ToolModel(String toolName, int toolIcon, ToolType toolType) {
            mToolName = toolName;
            mToolIcon = toolIcon;
            mToolType = toolType;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_editing_tools, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ToolModel item = mToolList.get(position);
        holder.txtTool.setText(item.mToolName);
        holder.imgToolIcon.setImageResource(item.mToolIcon);
    }



    @Override
    public int getItemCount() {
        return mToolList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgToolIcon;
        TextView txtTool;
        ConstraintLayout clrChangeLayout;

        ViewHolder(View itemView) {
            super(itemView);
            imgToolIcon = itemView.findViewById(R.id.imgToolIcon);
            txtTool = itemView.findViewById(R.id.txtTool);
            clrChangeLayout = itemView.findViewById(R.id.clrChangeLayout);
            itemView.setOnClickListener(v ->


                    mOnItemSelected.onToolSelected (
                            mToolList.get(getLayoutPosition()).mToolType)
            );
        }
    }
}

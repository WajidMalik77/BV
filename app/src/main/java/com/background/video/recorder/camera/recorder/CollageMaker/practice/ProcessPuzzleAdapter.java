package com.background.video.recorder.camera.recorder.CollageMaker.practice;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.background.video.recorder.camera.recorder.CollageMaker.layout.slant.NumberSlantLayout;
import com.background.video.recorder.camera.recorder.CollageMaker.layout.straight.NumberStraightLayout;
import com.background.video.recorder.camera.recorder.R;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.PuzzleView;

import java.util.ArrayList;
import java.util.List;

public class ProcessPuzzleAdapter extends RecyclerView.Adapter<ProcessPuzzleAdapter.PuzzleViewHolder> {

    private List<PuzzleLayout> layoutData = new ArrayList<>();
    private List<Bitmap> bitmapData = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    Activity activity;
    int index = -1;

    public ProcessPuzzleAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public PuzzleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_puzzle_process, parent, false);
        return new PuzzleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PuzzleViewHolder holder, int position) {
        final PuzzleLayout puzzleLayout = layoutData.get(position);

        holder.puzzleView.setNeedDrawLine(true);
        holder.puzzleView.setNeedDrawOuterLine(true);
        holder.puzzleView.setTouchEnable(false);

        holder.puzzleView.setPuzzleLayout(puzzleLayout);

        if (index == position) {
            holder.puzzleView.setLineColor(activity.getResources().getColor(R.color.pink));
            holder.puzzleView.setLineSize(8);

        } else {
            holder.puzzleView.setLineColor(activity.getResources().getColor(android.R.color.darker_gray));
            holder.puzzleView.setLineSize(3);
        }
        //holder.item.setBackgroundColor(activity.getResources().getColor(android.R.color.darker_gray));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                notifyDataSetChanged();
                if (onItemClickListener != null) {
                    int theme = 0;
                    if (puzzleLayout instanceof NumberSlantLayout) {
                        theme = ((NumberSlantLayout) puzzleLayout).getTheme();
                    } else if (puzzleLayout instanceof NumberStraightLayout) {
                        theme = ((NumberStraightLayout) puzzleLayout).getTheme();
                    }
                    onItemClickListener.onItemClick(puzzleLayout, theme);
                }

            }
        });

        if (bitmapData == null) return;

        final int bitmapSize = bitmapData.size();

    /*if (puzzleLayout.getAreaCount() > bitmapSize) {
      for (int i = 0; i < puzzleLayout.getAreaCount(); i++) {
        holder.puzzleView.addPiece(bitmapData.get(i % bitmapSize));
      }
    } else {
      holder.puzzleView.addPieces(bitmapData);
    }
  }*/
        //Log.e("","kkk: "+puzzleLayout.getAreaCount());
        if (puzzleLayout.getAreaCount() == bitmapSize) {
            for (int i = 0; i < puzzleLayout.getAreaCount(); i++) {
                holder.puzzleView.addPiece(bitmapData.get(i % bitmapSize));
            }
        } else {
            holder.puzzleView.addPieces(bitmapData);
        }
    }

    @Override
    public int getItemCount() {
        return layoutData == null ? 0 : layoutData.size();
    }

    public void refreshData(List<PuzzleLayout> layoutData, List<Bitmap> bitmapData) {
        this.layoutData = layoutData;
        this.bitmapData = bitmapData;

        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class PuzzleViewHolder extends RecyclerView.ViewHolder {

        PuzzleView puzzleView;
        RelativeLayout item;

        public PuzzleViewHolder(View itemView) {
            super(itemView);
            puzzleView = (PuzzleView) itemView.findViewById(R.id.puzzle);
            item = itemView.findViewById(R.id.item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(PuzzleLayout puzzleLayout, int themeId);
    }
}
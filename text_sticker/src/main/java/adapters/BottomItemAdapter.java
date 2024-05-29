package adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.xiaopo.flying.sticker.R;

import java.util.ArrayList;

import models.CategoryItem;


public class BottomItemAdapter extends RecyclerView.Adapter<BottomItemAdapter.ViewHolder> {

    private ArrayList<CategoryItem> CategoryItemList;
    private Context context;
    //    private int lastPosition = -1;
    private RecyclerItemClickListener listener;
    private boolean isTextlayout = false;
    private int mSelectedItem = -1;
    private boolean firsTime = true;


    public BottomItemAdapter(Context context, ArrayList<CategoryItem> CategoryItemList) {
        this.context = context;
        this.CategoryItemList = CategoryItemList;
    }

    public BottomItemAdapter(RecyclerItemClickListener listener, Context context,
                             ArrayList<CategoryItem> CategoryItemList) {
        this.listener = listener;
        this.context = context;
        this.isTextlayout = isTextlayout;
        this.CategoryItemList = CategoryItemList;
    }


    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bottom_icon, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        /////// recycler button selector
        if (position == mSelectedItem) {
            holder.ivCategoryIcon.setColorFilter(Color.parseColor("#48D9FE"));
            holder.tvCategoryName.setTextColor(Color.parseColor("#48D9FE"));
        } else {
            holder.ivCategoryIcon.setColorFilter(Color.WHITE);
            holder.tvCategoryName.setTextColor(Color.WHITE);
        }
        //////////////////////
        CategoryItem item = CategoryItemList.get(position);
        holder.ivCategoryIcon.setImageDrawable(item.getCategoryIconDrawable());
        holder.tvCategoryName.setText(item.getCategoryName());
        setAnimation(holder.itemView, position);

    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > mSelectedItem && firsTime == true) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            mSelectedItem = position;
        }
    }

    @Override
    public int getItemCount() {
        return CategoryItemList == null ? 0 : CategoryItemList.size();
//        return CategoryItemList.size();
    }

    // Static inner class to initialize the views of rows
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvCategoryName;
        private ImageView ivCategoryIcon;
        private LinearLayout generalLayout;

        public ViewHolder(View CategoryItemView) {
            super(CategoryItemView);

            CategoryItemView.setOnClickListener(this);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            ivCategoryIcon = itemView.findViewById(R.id.ivCategoryIcon);
            generalLayout = itemView.findViewById(R.id.generalLayout);
        }

        @Override
        public void onClick(View view) {
            if (listener != null)
                listener.RecyclerCropperItemClick(getAdapterPosition());
            mSelectedItem = getAdapterPosition();
            firsTime = false;
            notifyDataSetChanged();
        }
    }

    public interface RecyclerItemClickListener {
        void RecyclerCropperItemClick(int position);

    }
}
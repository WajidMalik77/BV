package adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaopo.flying.sticker.R;

import java.util.ArrayList;

import interfaces.TextColorClickListener;
import models.TextColorModel;

public class TextColorAdpater extends RecyclerView.Adapter<TextColorAdpater.viewHolder> {

    TextColorClickListener textColorClickListener;
    Context context;
    ArrayList<TextColorModel> arrayList;
    private int mSelectedItem = -1;
    private boolean isGradient = false;
    private GradientDrawable drawable;


    public TextColorAdpater(Context context, ArrayList<TextColorModel> arrayList, TextColorClickListener textColorClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.textColorClickListener = textColorClickListener;
    }

    public void setGradient(boolean isGradient) {
        this.isGradient = isGradient;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_text_color, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, int position) {
        TextColorModel model = arrayList.get(position);

        if (isGradient == true) {
            drawable = new GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    new int[]{Color.parseColor(model.getColor1()), Color.parseColor(model.getColor2())});
        } else {
            drawable = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{Color.parseColor(model.getColor1()), Color.parseColor(model.getColor1())});
        }
        drawable.setCornerRadius(10);
        viewHolder.textColor.setBackground(drawable);
        viewHolder.itemView.setOnClickListener(view -> {
            mSelectedItem = position;
            notifyDataSetChanged();

            if (textColorClickListener != null) {
                textColorClickListener.onItemTextColorClickListener(position, arrayList.get(position));
            }
        });

        if (position == mSelectedItem) {
            viewHolder.constraintLayoutTextColorPicker.setBackgroundResource(R.drawable.item_text_color_bg_selected_text);
        } else {
            viewHolder.constraintLayoutTextColorPicker.setBackground(null);
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        View textColor;
        ConstraintLayout constraintLayoutTextColorPicker;

        public viewHolder(View itemView) {
            super(itemView);
            constraintLayoutTextColorPicker = (ConstraintLayout) itemView.findViewById(R.id.constraintLayoutTextColorPicker);
            textColor = (View) itemView.findViewById(R.id.textColor);

        }
    }
}

package fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaopo.flying.sticker.R;

import java.util.ArrayList;

import adapters.TextColorAdpater;
import interfaces.StickerOperation;
import interfaces.TextColorClickListener;
import models.TextColorModel;

public class GradientFragment extends Fragment implements TextColorClickListener {

    private StickerOperation stickerOperation;

//    String colors[] = {"#FFFFFF", "#D4D4D4", "#4E4E4E", "#3A3A3A", "#000000", "#FFCCD5",
//            "#FFB3C1", "#FF4D6D", "#C9184A", "#A4133C", "#800F2F", "#590D22", "#FFEA00",
//            "#FFDD00", "#FFDD00", "#FFD000", "#FFAA00", "#FF9500", "#FF8800", "#FFB703",
//            "#4CC9F0", "#4895EF", "#4361EE", "#3F37C9", "#3A0CA3", "#480CA8", "#7209B7",
//            "#B5179E", "#F72585", "#8338EC", "#EF476F", "#06D6A0", "#EF476F", "#073B4C",
//            "#5F0F40", "#CCFF33", "#9EF01A", "#38B000", "#008000", "#006400", "#004B23",
//            "#F7D1CD", "#A47148", "#892B64", "#3D2645", "#0d3b66", "#faf0ca", "#f4d35e",
//            "#ee964b", "#f95738", "#6fffe9", "#5bc0be", "#41ead4", "#011627", "#f7af9d",
//            "#c08497", "#b0d0d3", "#ffcad4", "#456990"};

    String mSelectedColor = "#000000";
    int mSelectedColorOpacity = 255;
    ArrayList<TextColorModel> arrayList;
    private RecyclerView recyclerViewTextColor;
    private String[] startColors;
    private String[] endColors;


    public GradientFragment() {
    }

    public GradientFragment(StickerOperation stickerOperation) {
        this.stickerOperation = stickerOperation;
    }


    public static Fragment newinstance(StickerOperation stickerOperation) {

        return new GradientFragment(stickerOperation);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.gradient_fragment,
                container, false);

        /////////////////////// color picker recycler
        arrayList = new ArrayList<>();
        recyclerViewTextColor = (RecyclerView) view.findViewById(R.id.recyclerViewTextColor);
        recyclerViewTextColor.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTextColor.setItemAnimator(new DefaultItemAnimator());

        startColors = getResources().getStringArray(R.array.gradientStartColors);
        endColors = getResources().getStringArray(R.array.gradientEndColors);

        for (int i = 0; i < startColors.length; i++) {
            TextColorModel textColorModel = new TextColorModel();
            textColorModel.setColor1(startColors[i]);
            textColorModel.setColor2(endColors[i]);
            //add in array list
            arrayList.add(textColorModel);
        }

        TextColorAdpater adapter = new TextColorAdpater(getActivity(), arrayList, this);
        adapter.setGradient(true);
        recyclerViewTextColor.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemTextColorClickListener(int position, TextColorModel model) {
        String starColor = model.getColor1();
        String endColor = model.getColor2();
        if (stickerOperation != null) {
            stickerOperation.setStickerGradientColor(starColor, endColor);
        }

    }
}

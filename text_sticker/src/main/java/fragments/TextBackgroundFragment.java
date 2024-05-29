package fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaopo.flying.sticker.R;

import java.util.ArrayList;

import adapters.TextColorAdpater;
import interfaces.StickerOperation;
import interfaces.TextColorClickListener;
import models.TextColorModel;

public class TextBackgroundFragment extends Fragment implements TextColorClickListener {


    String colors[] = {"#FFFFFF", "#D4D4D4", "#4E4E4E", "#3A3A3A", "#000000", "#FFCCD5",
            "#FFB3C1", "#FF4D6D", "#C9184A", "#A4133C", "#800F2F", "#590D22", "#FFEA00",
            "#FFDD00", "#FFDD00", "#FFD000", "#FFAA00", "#FF9500", "#FF8800", "#FFB703",
            "#4CC9F0", "#4895EF", "#4361EE", "#3F37C9", "#3A0CA3", "#480CA8", "#7209B7",
            "#B5179E", "#F72585", "#8338EC", "#EF476F", "#06D6A0", "#EF476F", "#073B4C",
            "#5F0F40", "#CCFF33", "#9EF01A", "#38B000", "#008000", "#006400", "#004B23",
            "#F7D1CD", "#A47148", "#892B64", "#3D2645", "#0d3b66", "#faf0ca", "#f4d35e",
            "#ee964b", "#f95738", "#6fffe9", "#5bc0be", "#41ead4","#011627", "#f7af9d",
            "#c08497", "#b0d0d3", "#ffcad4", "#456990"};

    String mSelectedColor = "#000000";
    int mSelectedColorOpacity = 255;
    private RecyclerView recyclerViewTextColor;
    private SeekBar seekBarTextColorOpacity;
    ArrayList<TextColorModel> arrayList;
    private StickerOperation stickerOperation;

    public TextBackgroundFragment() {
    }

    public TextBackgroundFragment(StickerOperation stickerOperation) {
        this.stickerOperation = stickerOperation;
    }
    public static Fragment newinstance(StickerOperation stickerOperation) {

        return new TextBackgroundFragment(stickerOperation);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.background_fragment,
                container, false);

        seekBarTextColorOpacity = (SeekBar) view.findViewById(R.id.seekBarTextColorOpacity);

        /////////////////////// color picker recycler
        arrayList = new ArrayList<>();
        recyclerViewTextColor = (RecyclerView) view.findViewById(R.id.recyclerViewTextColor);
        recyclerViewTextColor.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        recyclerViewTextColor.setItemAnimator(new DefaultItemAnimator());

        for (int i = 0; i < colors.length; i++) {
            TextColorModel textColorModel = new TextColorModel();
            textColorModel.setColor1(colors[i]);

            //add in array list
            arrayList.add(textColorModel);
        }

        TextColorAdpater adapter = new TextColorAdpater(getActivity(), arrayList, this);
        recyclerViewTextColor.setAdapter(adapter);


        seekBarTextColorOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mSelectedColorOpacity = i;

                textColorChanged(mSelectedColor, i);

                // Calculating Display Value
                double displayValueFloat = i / 2.55;
                int displayValue = (int) displayValueFloat;

//                textViewOpacityLevel.setText(String.valueOf(displayValue));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        return view;
    }

    @Override
    public void onItemTextColorClickListener(int position, TextColorModel model) {
        String color = model.getColor1();
        mSelectedColor = color;
        textColorChanged(color, mSelectedColorOpacity);
    }
    public void  textColorChanged(String color, int opacity) {

        Log.d("textOpacity ", String.valueOf(opacity));

        if (stickerOperation !=null){
            stickerOperation.setStickerBackgroundColor(color, opacity);
        }

//        TextSticker textEntity = currentTextEntity();
//        if (textEntity != null) {
//
//            textEntity.setTextColorOpacity(color, opacity);
//            textEntity.resizeText();
//            mStickerView.replace(textEntity, null);
//            mStickerView.invalidate();
//        }


    }
}

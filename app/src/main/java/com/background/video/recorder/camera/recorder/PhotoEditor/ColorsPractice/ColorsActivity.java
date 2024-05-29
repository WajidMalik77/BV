package com.background.video.recorder.camera.recorder.PhotoEditor.ColorsPractice;//package com.app.photo.collage.maker.picture.editor.ColorsPractice;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.app.photo.collage.maker.picture.editor.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ColorsActivity extends AppCompatActivity implements ImageModelInterface {
//
//    ImageView ivColors;
//    RecyclerView rvColors;
//    ColorAdapter colorAdapter;
//    List<ColorModel> colors;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_colors);
//
//        ivColors = findViewById(R.id.ivColors);
//        rvColors = findViewById(R.id.rvColors);
//        rvColors.setAdapter(colorAdapter);
//
//        colors = new ArrayList<>();
//        colors.add(new ColorModel(getResources().getColor(R.color.transparent1), "#00FF00"));
//        colors.add(new ColorModel(getResources().getColor(R.color.transparent2), "#00FF00"));
//        colors.add(new ColorModel(getResources().getColor(R.color.transparent3), "#00FF00"));
//        colors.add(new ColorModel(getResources().getColor(R.color.transparent4), "#00FF00"));
//        colors.add(new ColorModel(getResources().getColor(R.color.transparent5), "#00FF00"));
//        colors.add(new ColorModel(getResources().getColor(R.color.transparent6), "#00FF00"));
//        colors.add(new ColorModel(getResources().getColor(R.color.transparent7), "#00FF00"));
//        colors.add(new ColorModel(getResources().getColor(R.color.transparent8), "#00FF00"));
//        colors.add(new ColorModel(getResources().getColor(R.color.transparent9), "#00FF00"));
//        colors.add(new ColorModel(getResources().getColor(R.color.transparent10), "#00FF00"));
//        colors.add(new ColorModel(getResources().getColor(R.color.transparent11), "#00FF00"));
//        colors.add(new ColorModel(getResources().getColor(R.color.transparent12), "#00FF00"));
//        colors.add(new ColorModel(getResources().getColor(R.color.transparent13), "#00FF00"));
//        colors.add(new ColorModel(getResources().getColor(R.color.transparent14), "#00FF00"));
//        colors.add(new ColorModel(getResources().getColor(R.color.transparent15), "#00FF00"));
//
//
////        rvColors.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
////        colorAdapter = new ColorAdapter(colors, this, this, );
////        rvColors.setAdapter(colorAdapter);
////        colorAdapter.notifyDataSetChanged();
//
//
//    }
//
//    @Override
//    public void onClick(int position) {
////        Toast.makeText(this, "Click "+position, Toast.LENGTH_SHORT).show();
//        ivColors.setBackgroundColor(colors.get(position).getColorValue());
//
//
//    }
//}
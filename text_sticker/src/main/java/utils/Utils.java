package utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xiaopo.flying.sticker.StickerView;

import java.io.IOException;
import java.util.ArrayList;

import fragments.TextStickerFragment;

public class Utils {
    // slide the view from below itself to the current position
    public static void slideUpView(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public static void slideDownView(View view) {
        view.setVisibility(View.INVISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }
    // slide the view from its current position to below itself
    public static void slideDownViewGone(View view) {
        view.setVisibility(View.GONE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }


    public static void showFragment(StickerView stickerView, ViewGroup view, ViewGroup editorFragmentContainer, int containter, FragmentActivity activity) {
        TextStickerFragment textStickerFragment = new TextStickerFragment(stickerView,view,editorFragmentContainer, activity);
//        textStickerFragment.setStickerView(stickerView);
        String backStateName = textStickerFragment.getClass().getName();
        Log.e("backStateName",""+backStateName);
        FragmentManager manager = activity.getSupportFragmentManager();
      //  boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
//        if (!fragmentPopped) { //fragment not in back stack, create it.
//            Log.e("ShowFragment","Create TextStickerFragment");
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.replace(containter, textStickerFragment);
//            ft.addToBackStack(backStateName);
//            ft.commit();
//        }
        Log.e("ShowFragment","Create TextStickerFragment");
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(containter, textStickerFragment);
        ft.addToBackStack(backStateName);
        ft.commit();
    }

    public static void addFragment(int id, FragmentActivity activity, Fragment fragment) {
        try {
            String tag = fragment.getClass().getName();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            Fragment existingFragment = fragmentManager.findFragmentByTag(tag);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (existingFragment == null) {
                fragmentTransaction.replace(id, fragment, tag);
            }else {
                fragmentTransaction.replace(id, existingFragment, tag);
            }
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        }catch (Exception e){

        }
    }

    //
    public static void showKeyboard(View view, Context context) {
//        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//        }
    }

    public static void hideKeyboard(View view, Context context) {
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static ArrayList<String> fetchAssetsFontsPath(Context context,String folderName) {
        ArrayList<String> dataSetArrayList = new ArrayList<>();
        /////
        try {
            String[] files = context.getAssets()
                    .list(folderName);

            for (int i = 0; i < files.length; i++) { //files contain names of asset data
//                DataSet dataSet = new DataSet();
//                dataSet.setFontAssetPath(folderName + "/" + files[i]);
                dataSetArrayList.add(folderName + "/" + files[i]);
                Log.e("dataSet", "" + folderName + "/" + files[i]);
                ////////
//                String split = files[i].substring(files[i].indexOf("."));
//                if (!split.equals(".txt")) // not add .txt files
//                    dataSetArrayList.add(dataSet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataSetArrayList;
    }
}

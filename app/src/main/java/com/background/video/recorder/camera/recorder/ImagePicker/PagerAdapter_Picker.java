//package com.background.video.recorder.camera.recorder.ImagePicker;
//
//import android.content.Context;
//
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentPagerAdapter;
//
//import com.background.video.recorder.camera.recorder.R;
//
//
//public class PagerAdapter_Picker extends FragmentPagerAdapter {
//
//
//    String[] tab_titles;
//
//
//    public PagerAdapter_Picker(Context context, FragmentManager fm) {
//        super(fm);
//            tab_titles = context.getResources().getStringArray(R.array.tab_titles);
//
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return tab_titles[position];
//    }
//
//    @Override
//    public int getCount() {
//        return tab_titles.length;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//
//
//        switch (position) {
//            case 0:
//                return new GalleryFragment();
//
//
//            default:
//                return null;
//        }
//
//
//    }
//
//
//}

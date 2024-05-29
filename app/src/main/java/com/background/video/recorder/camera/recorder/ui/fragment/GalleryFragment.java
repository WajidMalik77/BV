package com.background.video.recorder.camera.recorder.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.background.video.recorder.camera.recorder.R;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.background.video.recorder.camera.recorder.ui.practice.ScreenRecordingFragment;
import com.google.android.material.tabs.TabLayout;

public class GalleryFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);



        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

//        tabLayout.setTabTextColors(R.color.black, R.color.pink_color_main);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF2159"));
        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#FF2159"));

        viewPager.setAdapter(new SectionsPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    // Method to switch tabs programmatically
    public void switchTab(int tabIndex) {
        if (viewPager != null && tabIndex >= 0 && tabIndex < viewPager.getAdapter().getCount()) {
            viewPager.setCurrentItem(tabIndex);
        }
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FilesFragment();
                case 1:
                    return new ScreenRecordingFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2; // Two tabs
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Videos";
                case 1:
                    return "ScreenRecordings";
            }
            return null;
        }
    }
}


package com.background.video.recorder.camera.recorder.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.background.video.recorder.camera.recorder.CollageMaker.MyAlbumAdapter;
import com.background.video.recorder.camera.recorder.CollageMaker.MyAlbumModel;
import com.background.video.recorder.camera.recorder.CollageMaker.recyclerViewInterfaces;
import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.ads.ActionOnAdClosedListener;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.ads.InterstitialClass;
import com.background.video.recorder.camera.recorder.databinding.FragmentAlbumBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AlbumFragment extends Fragment implements recyclerViewInterfaces {

    private File[] files;
    private ArrayList<String> list;
    private ConstraintLayout iv_back;
    private RecyclerView recyclerView;
    private LinearLayout tvNothing;
    private LinearLayout bannerLayout;
    private TextView tv;
    private MyAlbumAdapter adapter;
    private boolean empty = false;
    private MyAlbumModel getCupy;
    private List<MyAlbumModel> arrayListData = new ArrayList<>();

    FragmentAlbumBinding binding;

    private boolean album_native_admob = true;
    private boolean album_native_facebook = true;
    private boolean home_admob_home_inter = true;
    private boolean admob_album_inter = true;
    private boolean overlay_permission_admob_native = true;
    private boolean overlay_permission_facebook_native = true;
    Activity activity;
    private SharedPrefsHelper prefs = null;
    private String admob_native_id = "";
    private String facebook_native_ad_id = "";
    private String admob_interstitial_home_id = "";
    private AppCompatButton homeBtn;
    @Override
    public void onResume() {
        super.onResume();
        if (homeBtn == null) {
            homeBtn = requireActivity().findViewById(R.id.homeBtn);
            homeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    NavController navController = NavHostFragment.findNavController(AlbumFragment.this);
//                    navController.navigate(R.id.action_albumFragment_to_photoEditorCollageMakerFragment2);

                    if(SharePref.getBoolean(AdsKeys.InApp, false)) {

                        NavigationUtils.navigate(getActivity(), R.id.homeFragment);
                    } else {

                        if (admob_album_inter) {
                            InterstitialClass.requestInterstitial(getActivity(), photoeditorcollagemaker_home_admob_inter_id, new ActionOnAdClosedListener() {
                                @Override
                                public void ActionAfterAd() {

                                    NavigationUtils.navigate(getActivity(), R.id.homeFragment, new Bundle());
                                }
                            });

                        } else {
                            NavigationUtils.navigate(getActivity(), R.id.homeFragment, new Bundle());

                        }
                    }
                }
            });
        }
    }
    NavController navController;
    private String photoeditorcollagemaker_home_admob_inter_id = "";
    private boolean photoeditorcollagemaker_home_admob_inter = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentAlbumBinding.inflate(inflater, container, false);

        navController = NavHostFragment.findNavController(AlbumFragment.this);
        prefs = SharedPrefsHelper.getInstance(getActivity());
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {
            album_native_admob = localPrefs.getalbum_native_admobSwitch();
//            album_native_admob = true;
            album_native_facebook = localPrefs.getdashboard_native_facebookSwitch();
            overlay_permission_admob_native = localPrefs.getoverlay_permission_admob_nativeSwitch();
            overlay_permission_facebook_native = localPrefs.getoverlay_permission_facebook_nativeSwitch();
            admob_native_id = localPrefs.getadmob_native_idId();
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId();
            home_admob_home_inter = localPrefs.gethome_admob_home_interSwitch();
            admob_album_inter = localPrefs.getadmob_album_interSwitch();
            admob_interstitial_home_id = localPrefs.getadmob_interstitial_home_idId();
            photoeditorcollagemaker_home_admob_inter = localPrefs.getphotoeditorcollagemaker_home_admob_interSwitch();
            photoeditorcollagemaker_home_admob_inter_id = localPrefs.getphotoeditorcollagemaker_home_admob_inter_idId();
        }

        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.nativeAd.setVisibility(View.GONE);
        }  else if (album_native_admob) {
            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadMediationNative(
                        activity,
                        binding.nativeAd,
                        admob_native_id,
                        binding.nativeAdContainer,
                        R.layout.ad_native_layout_modified
                );
            }
        } else if (album_native_facebook) {
            // binding.nativeAd.setVisibility(View.GONE);

            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadNativeFacebookAd(
                        facebook_native_ad_id,
                        activity,
                        binding.nativeAdContainer
                );
            }
        } else {
            Log.d("checksswitcehs", "onViewCreated: both are off");
            binding.nativeAd.setVisibility(View.GONE);
        }



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        list = new ArrayList<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            // Background work here
            handler.post(() -> {
                for (int i = 0; i < getData().size(); i++) {
                    list.add(getData().get(i).getUri().toString());
                }
                isEmpty();
            });
        });

        init(view);
        getData();
//        iv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                NavController navController = NavHostFragment.findNavController(AlbumFragment.this);
//                navController.navigate(R.id.action_albumFragment_to_photoEditorCollageMakerFragment);
//
//            }
//        });


    }

    private void init(View view) {
//        iv_back = view.findViewById(R.id.iv_back_share_activity);
        recyclerView = view.findViewById(R.id.recyclerViewFont);
        tvNothing = view.findViewById(R.id.tvNothing);
    }

    private void isEmpty() {
        if (list.size() == 0) {
            tvNothing.setVisibility(View.VISIBLE);
        } else {
            tvNothing.setVisibility(View.GONE);
        }

        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                File file1 = new File(Uri.parse(lhs).getPath());
                File file2 = new File(Uri.parse(rhs).getPath());
                return Long.compare(file2.lastModified(), file1.lastModified());
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new MyAlbumAdapter(getActivity(), list, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private List<MyAlbumModel> getData() {
        List<MyAlbumModel> arrayList = new ArrayList<>();

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + getResources().getString(R.string.app_name));

        try {
            files = file.listFiles();
            if (files != null) {
                // Sort the files based on last modified date in descending order
                Arrays.sort(files, (file1, file2) -> Long.compare(file2.lastModified(), file1.lastModified()));
                for (File file1 : files) {
                    MyAlbumModel getCupy = new MyAlbumModel();
                    getCupy.setUri(Uri.fromFile(file1));
                    arrayList.add(getCupy);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return arrayList;
    }

    @Override
    public void onClick(int position) {
        if (position < list.size() && position >= 0) {
            String getCupy = list.get(position);
            if (getCupy != null) {


                NavController navController = NavHostFragment.findNavController(AlbumFragment.this);

                Bundle bundle = new Bundle();
                bundle.putString("imageViewZoom", getCupy);

                navController.navigate(R.id.action_albumFragment_to_imageViewFragment, bundle);

            }
        } else {
            Toast.makeText(getActivity(), " please wait", Toast.LENGTH_SHORT).show();
        }
    }
}
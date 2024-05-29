package com.background.video.recorder.camera.recorder.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.adapters.FavouriteFileAdapter;
import com.background.video.recorder.camera.recorder.adapters.MyFavouriteAdapter;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.databinding.LayoutFavFilesBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.model.MediaFiles;
import com.background.video.recorder.camera.recorder.ui.dialog.DeleteFilesDialog;
import com.background.video.recorder.camera.recorder.util.FirebaseEvents;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.background.video.recorder.camera.recorder.util.constant.Constants;
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils;
import com.background.video.recorder.camera.recorder.viewmodel.MediaFilesViewModel;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import roozi.app.ads.AdsManager;

public class FavFilesFragment extends Fragment {
    private static final String TAG = "FavFilesFragment";
    private final List<Integer> favIdList = new ArrayList<>(); // ids for delete multiple files
    private final List<String> FavNameList = new ArrayList<>(); // names for updating multiple files
    private LayoutFavFilesBinding favFilesBinding;
    private MediaFilesViewModel viewModel;
    private FavouriteFileAdapter favouriteFileAdapter;
    private FavouriteFileAdapter.OnLongClickedListenerFavFile longClickedListenerFavFile;
    private FavouriteFileAdapter.ListForOperationListenerFavFile listForOperationListenerFavFile;
    private List<MediaFiles> selectedFavItems = new ArrayList<>(); // selected items in view
    private DeleteFilesDialog deleteDialog;
    private DeleteFilesDialog.OnButtonClickedListener yesCallBack;
    boolean native_bg;
    SharedPreferences sharedPrefs;
    public FavFilesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isAdded())
            viewModel = new ViewModelProvider(requireActivity(),
                    (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                            .getInstance(requireActivity().getApplication()))
                    .get(MediaFilesViewModel.class);
    }


    private boolean fav_files_admob_native = true;
    private boolean fav_files_facebook_native = true;
    private boolean fav_files_video_preview_admob_native = true;
    private boolean fav_files_video_preview_facebook_native = true;
    private String facebook_native_ad_id = "";
    private String admob_native_id = "";
    private SharedPrefsHelper prefs = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        favFilesBinding = LayoutFavFilesBinding.inflate(getLayoutInflater());
        sharedPrefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        native_bg   = sharedPrefs.getBoolean("native_bg", false);
        prefs = SharedPrefsHelper.getInstance(getActivity());
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {
            fav_files_admob_native = localPrefs.getfav_files_admob_nativeSwitch();
//            fav_files_admob_native = true;

            fav_files_facebook_native = localPrefs.getasking_password_facebook_nativeSwitch();
            fav_files_video_preview_admob_native = localPrefs.getfav_files_video_preview_admob_nativeSwitch();
            fav_files_video_preview_facebook_native = localPrefs.getfav_files_video_preview_facebook_nativeSwitch();
            admob_native_id = localPrefs.getadmob_native_idId();
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId();


            Log.d(TAG, "onCreate:  admob_interstitial_splash_id  " + fav_files_admob_native);
            Log.d(TAG, "onCreate:  facebook_interstitial_splash_id  " + fav_files_facebook_native);
        }



        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            favFilesBinding.nativeAd.setVisibility(View.GONE);
        }  else if (fav_files_admob_native) {
            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadMediationNative(
                        activity,
                        favFilesBinding.nativeAd,
                        admob_native_id,
                        favFilesBinding.nativeAdContainer,
                        R.layout.ad_native_layout_modified
                );
            }
        }
        else if (fav_files_facebook_native) {
            // favFilesBinding.nativeAd.setVisibility(View.GONE);

            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadNativeFacebookAd(
                        facebook_native_ad_id,
                        activity,
                        favFilesBinding.nativeAdContainer
                );
            }
        }
        else {
            Log.d("checksswitcehs", "onViewCreated: both are off");
            favFilesBinding.nativeAd.setVisibility(View.GONE);
        }

        return favFilesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents();
        FirebaseEvents.Companion.logAnalytic("Favourite_Videos_Screen_Show");
    }

    private void showAd() {
//        if (getActivity() != null)
//            AdsManager.Companion.nativee(
//                    SplashTwoFragment.getBoolean(AdsKeys.Admob_Favourites_Native),
//                    SplashTwoFragment.getBoolean(AdsKeys.Facebook_Favourites_Native),
//                    getActivity(), favFilesBinding.nativeAd, new Function1<Boolean, Unit>() {
//                        @Override
//                        public Unit invoke(Boolean aBoolean) {
//                            return null;
//                        }
//                    }
//            );

//        AdsManager.Companion.interstitial(
//                SplashTwoFragment.getBoolean(AdsKeys.Admob_DashBoard_Favourites_Click_Inter),
//                SplashTwoFragment.getBoolean(AdsKeys.Facebook_DashBoard_Favourites_Click_Inter),
//                AdsKeys.Admob_DashBoard_Favourites_Click_Inter,
//                AdsKeys.Facebook_DashBoard_Favourites_Click_Inter,
//                requireActivity(), b -> null
//        );

    }

    private void initComponents() {
//        favFilesBinding.llAdViewPlaceholder.setVisibility(View.VISIBLE);
        getVideoFavFiles();
        setListeners();
    }


    private void setListeners() {
        listForOperationListenerFavFile = new FavouriteFileAdapter.ListForOperationListenerFavFile() {
            @Override
            public void selectedItemList(List<MediaFiles> list) {
                Log.e(TAG, "selectedFavItemList: " + "list from adapter  size ==" + list.size());
                if (list == null) {
                    Log.e(TAG, "selectedFAvItemList: " + "list is empty");
                } else {
                    showAd();
                    selectedFavItems = list;
                    Log.e(TAG, "selectedFavItemList: " + selectedFavItems.size());
                }
            }
        };
    }

    private void getVideoFavFiles() {
        viewModel.getAllFavouriteMediaFiles().observe(requireActivity(), mediaFiles -> {
            Log.d(TAG, "onChanged: Size==" + mediaFiles.size());
            ArrayList<Object> tempList = new ArrayList<>(mediaFiles);
            tempList.add(0, "ad");

            if (mediaFiles.isEmpty())
            {
                favFilesBinding.nativeAd.setVisibility(View.GONE);
//                favFilesBinding.nativeAd.setVisibility(View.VISIBLE);

                favFilesBinding.empty.setVisibility(View.VISIBLE);
            }
            else {
                favFilesBinding.empty.setVisibility(View.GONE);
            }



            if (isAdded()) {
                MyFavouriteAdapter myFavouriteAdapter = new MyFavouriteAdapter(requireContext(), (ArrayList<MediaFiles>) mediaFiles, mediaFiles1 -> {
                    viewModel.updateFavFile(Constants.MEDIA_TYPE_NON_FAVOURITE, mediaFiles1.getName());
                    return null;
                },fav_files_video_preview_admob_native , fav_files_video_preview_facebook_native, admob_native_id, facebook_native_ad_id, getActivity());
                favFilesBinding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                favFilesBinding.recyclerView.setAdapter(myFavouriteAdapter);
            }


        });
    }

}

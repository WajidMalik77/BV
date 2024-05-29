package com.background.video.recorder.camera.recorder.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.background.video.recorder.camera.recorder.R;
import com.background.video.recorder.camera.recorder.adapters.FileAdapter;
import com.background.video.recorder.camera.recorder.adapters.MyFileAdapter;
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils;
import com.background.video.recorder.camera.recorder.databinding.LayoutFilesBinding;
import com.background.video.recorder.camera.recorder.databinding.RenameDialogBinding;
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper;
import com.background.video.recorder.camera.recorder.model.MediaFiles;
import com.background.video.recorder.camera.recorder.model.MenuModel;
import com.background.video.recorder.camera.recorder.ui.dialog.DeleteAnimationDialog;
import com.background.video.recorder.camera.recorder.ui.dialog.DeleteFilesDialog;
import com.background.video.recorder.camera.recorder.util.FirebaseEvents;
import com.background.video.recorder.camera.recorder.util.SharePref;
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys;
import com.background.video.recorder.camera.recorder.util.constant.Constants;
import com.background.video.recorder.camera.recorder.viewmodel.MediaFilesViewModel;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import roozi.app.ads.AdsManager;

public class FilesFragment extends Fragment {
    private static final String TAG = "FilesFragment1234";
    private final List<Integer> idList = new ArrayList<>(); // ids for delete multiple files
    private final List<String> nameList = new ArrayList<>(); // names for updating multiple files
    List<MediaFiles> mediaFilesForAdapter = new ArrayList<>();
    private LayoutFilesBinding binding;
    private MediaFilesViewModel viewModel;
    private FileAdapter fileAdapter;
    private MyFileAdapter myFileAdapter;
    private FileAdapter.VideoClickListener videoClickListener;
    private FileAdapter.OnLongClickedListener longClickedListener;
    private FileAdapter.ListForOperationListener forOperationListener;
    private List<MediaFiles> selectedItems = new ArrayList<>(); // selected items in view
    private DeleteFilesDialog deleteDialog;
    private DeleteFilesDialog.OnButtonClickedListener yesCallBack;
    private DeleteAnimationDialog deleteAnimationDialog;
    boolean native_bg;
    SharedPreferences sharedPrefs;

    public FilesFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isAdded())
            viewModel = new ViewModelProvider(requireActivity(),
                    (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                            .getInstance(requireActivity()
                                    .getApplication())).get(MediaFilesViewModel.class);
    }

    private boolean saved_files_in_gallery_admob_native = true;
    private boolean saved_files_in_gallery_facebook_native = true;
    private boolean rename_dailog_admob_native = true;
    private boolean rename_dailog_facebook_native = true;
    private boolean files_video_preview_admob_native = true;
    private boolean files_video_preview_facebook_native = true;
    private String facebook_native_ad_id = "";
    private String admob_native_id = "";
    private SharedPrefsHelper prefs = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LayoutFilesBinding.inflate(inflater, container, false);
        sharedPrefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        native_bg = sharedPrefs.getBoolean("native_bg", false);


        prefs = SharedPrefsHelper.getInstance(getActivity());
        SharedPrefsHelper localPrefs = prefs;
        if (localPrefs != null) {
            saved_files_in_gallery_admob_native = localPrefs.getsaved_files_in_gallery_admob_nativeSwitch();

//           saved_files_in_gallery_admob_native = true;

            saved_files_in_gallery_facebook_native = localPrefs.getsaved_files_in_gallery_facebook_nativeSwitch();

//            saved_files_in_gallery_admob_native = true;
//            saved_files_in_gallery_facebook_native = true;
            files_video_preview_admob_native = localPrefs.getsaved_files_in_gallery_facebook_nativeSwitch();
            files_video_preview_facebook_native = localPrefs.getsaved_files_in_gallery_facebook_nativeSwitch();

//            files_video_preview_admob_native = true;
//            files_video_preview_facebook_native = true;

            rename_dailog_admob_native = localPrefs.getrename_dailog_admob_nativeSwitch();
            rename_dailog_facebook_native = localPrefs.getrename_dailog_facebook_nativeSwitch();
            admob_native_id = localPrefs.getadmob_native_idId();
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId();


            Log.d(TAG, "onCreate:  admob_interstitial_splash_id  " + saved_files_in_gallery_admob_native);
            Log.d(TAG, "onCreate:  facebook_interstitial_splash_id  " + saved_files_in_gallery_facebook_native);
        }


        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.nativeAd.setVisibility(View.GONE);
        } else if (saved_files_in_gallery_admob_native) {
            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadMediationNative(
                        activity,
                        binding.nativeAd,
                        admob_native_id,
                        binding.nativeAdContainer,
                        R.layout.ad_native_layout_collage
                );
            }
        } else if (saved_files_in_gallery_facebook_native) {
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
        Log.d("hoem_frag", "onCreateView: Recordings fargment");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showAd();
        initComponents();
        FirebaseEvents.Companion.logAnalytic("Recordings_Screen_Show");
    }

    private void showAd() {

//        if (getActivity() != null) {
//            AdsManager.Companion.nativee(
//                    SplashTwoFragment.getBoolean(AdsKeys.Admob_Recordings_Native),
//                    SplashTwoFragment.getBoolean(AdsKeys.Facebook_Recordings_Native),
//                    getActivity(), binding.nativeAd, new Function1<Boolean, Unit>() {
//                        @Override
//                        public Unit invoke(Boolean aBoolean) {
//                            return null;
//                        }
//                    }
//            );
//        }

//        AdsManager.Companion.interstitial(
//                SplashTwoFragment.getBoolean(AdsKeys.Admob_DashBoard_Recordings_Click_Inter),
//                SplashTwoFragment.getBoolean(AdsKeys.Facebook_DashBoard_Recordings_Click_Inter),
//                AdsKeys.Admob_DashBoard_Recordings_Click_Inter,
//                AdsKeys.Facebook_DashBoard_Recordings_Click_Inter,
//                requireActivity(), b -> null
//        );

    }

    private void initComponents() {
//        binding.llAdViewPlaceholder.setVisibility(View.VISIBLE);
        getVideoFiles();
        setListeners();
        forOperationListener = new FileAdapter.ListForOperationListener() {
            @Override
            public void selectedItemList(List<MediaFiles> list) {
                Log.e(TAG, "selectedItemList: " + "list from adapter  size ==" + list.size());
                if (list == null) {
                    Log.e(TAG, "selectedItemList: " + "list is empty");
                } else {
                    selectedItems = list;
                    Log.e(TAG, "selectedItemList: " + selectedItems.size());
                }
            }
        };
        Log.e(TAG, "initComponents: " + selectedItems.size());

    }


    private void setListeners() {
        videoClickListener = mediaFiles -> viewModel.updateFavFile(Constants.MEDIA_TYPE_FAVOURITE, mediaFiles.getName());
    }

    private void getVideoFiles() {
        if (isAdded())
            viewModel.getAllMediaFiles().observe(getActivity(), mediaFiles -> {
//                ArrayList<Object> tempList = new ArrayList<>(mediaFiles);
//                tempList.add(0, "ad");

                if (mediaFiles.isEmpty()) {
                    binding.nativeAd.setVisibility(View.GONE);
                    binding.empty.setVisibility(View.VISIBLE);
                } else {
                    binding.empty.setVisibility(View.GONE);
                }


                if (isAdded()) {
                    myFileAdapter = new MyFileAdapter(requireContext(), (ArrayList<MediaFiles>) mediaFiles, (mediaFiles1) -> {
                        viewModel.updateFavFile(Constants.MEDIA_TYPE_FAVOURITE, mediaFiles1.getName());
                        return null;
                    }, (mediaFiles1, pos) -> {
//                                deleteFilesFromStorage();
                        viewModel.deleteMediaFiles(mediaFiles1);
                        myFileAdapter.notifyItemRemoved(pos);
                        return null;
                    }, (oldName, pos) -> {
                        renameDialog(mediaFiles, oldName, pos);

                        return null;
                    }, files_video_preview_admob_native, files_video_preview_facebook_native, admob_native_id, facebook_native_ad_id, getActivity());
                    if (isAdded())
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    binding.recyclerView.setAdapter(myFileAdapter);
                }
            });


    }
//    private void getVideoFiles() {
//        if (isAdded())
//            viewModel.getAllMediaFiles().observe(requireActivity(), mediaFiles -> {
//                Log.d(TAG, "Received media files count: " + mediaFiles.size());
////                ArrayList<Object> tempList = new ArrayList<>(mediaFiles);
////                tempList.add(0, "ad");
//
//                if (mediaFiles.isEmpty()) {
//                    binding.nativeAd.setVisibility(View.GONE);
//                    binding.empty.setVisibility(View.VISIBLE);
//                    Log.d(TAG, "Media files list is empty");
//                } else {
//                    Log.d(TAG, "Updating adapter with media files");
//                    binding.empty.setVisibility(View.GONE);
////                    myFileAdapter = new MyFileAdapter(requireContext(), mediaFiles, this::onMediaFileClicked);
////                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
////                    binding.recyclerView.setAdapter(myFileAdapter);
//
//
//
//                }
//
//                if (isAdded()) {
//                    myFileAdapter = new MyFileAdapter(requireContext(), (ArrayList<MediaFiles>) mediaFiles, (mediaFiles1) -> {
//                        viewModel.updateFavFile(Constants.MEDIA_TYPE_FAVOURITE, mediaFiles1.getName());
//                        return null;
//                    }, (mediaFiles1, pos) -> {
////                                deleteFilesFromStorage();
//                        viewModel.deleteMediaFiles(mediaFiles1);
//                        myFileAdapter.notifyItemRemoved(pos);
//                        return null;
//                    }, (oldName, pos) -> {
//                        renameDialog(mediaFiles, oldName, pos);
//
//                        return null;
//                    },files_video_preview_admob_native , files_video_preview_facebook_native, admob_native_id, facebook_native_ad_id, getActivity());
//                    if (isAdded())
//                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//                    binding.recyclerView.setAdapter(myFileAdapter);
//                }
//
//            });
//
//
//    }

    private void renameDialog(List<MediaFiles> mediaFiles, String oldName, int pos) {
        LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RenameDialogBinding bind = RenameDialogBinding.inflate(inflater);
        Dialog dialog = new Dialog(requireContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(bind.getRoot());

        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            bind.nativeAd.setVisibility(View.INVISIBLE);
        } else if (rename_dailog_admob_native) {
            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadMediationNative(
                        activity,
                        bind.nativeAd,
                        admob_native_id,
                        bind.nativeAdContainer,
                        R.layout.ad_native_layout_collage
                );
            }
        } else if (rename_dailog_facebook_native) {
            // binding.nativeAd.setVisibility(View.GONE);

            Activity activity = getActivity();
            if (activity != null) {
                InterstitialAdUtils.INSTANCE.loadNativeFacebookAd(
                        facebook_native_ad_id,
                        activity,
                        bind.nativeAdContainer
                );
            }
        } else {
            Log.d("checksswitcehs", "onViewCreated: both are off");
            bind.nativeAd.setVisibility(View.INVISIBLE);
        }


        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        dialog.setOnShowListener(dialogInterface -> {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        });
        bind.et.requestFocus();
        bind.cancel.setOnClickListener(view -> dialog.dismiss());
        bind.save.setOnClickListener(view -> {
            if (bind.et.getText().toString().isEmpty()) {
                bind.et.setError("Name is Required");
            } else if (!bind.et.getText().toString().isEmpty()) {

                for (int i = 0; i < mediaFiles.size(); i++) {
                    Log.d(TAG, "renameDialog:" + i + mediaFiles.size());
                    if (bind.et.getText().toString().equalsIgnoreCase(mediaFiles.get(i).getName())) {
                        Toast.makeText(requireContext(), "Name is Already Exist", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                viewModel.renameFile(bind.et.getText().toString(), oldName);
                myFileAdapter.notifyItemChanged(pos);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Constants.LONG_CLICKED_ENABLED) {
            fileAdapter.unSelect();
        } else {
            Log.e(TAG, "onPause: " + "already Long click is disabled");
        }
    }


    public static List<MenuModel> getMenu(Context context) {
        List<MenuModel> listt = new ArrayList<>();
        listt.add(new MenuModel(R.drawable.rename, context.getString(R.string.rename)));
//        listt.add(new MenuModel(R.drawable.save_icon, "Save"));
        listt.add(new MenuModel(R.drawable.share, context.getString(R.string.share)));
        listt.add(new MenuModel(R.drawable.delete, context.getString(R.string.delete)));
        return listt;
    }


}

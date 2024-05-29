package com.background.video.recorder.camera.recorder.photoeditorcollagemaker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.background.video.recorder.camera.recorder.ImagePicker.Config
import com.background.video.recorder.camera.recorder.R
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils.adMobHomeInterstitialAd
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils.loadMediationNative
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils.loadNativeFacebookAd
import com.background.video.recorder.camera.recorder.databinding.FragmentPhotoEditorCollageMakerBinding
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper
import com.background.video.recorder.camera.recorder.ui.fragment.ImagePickerFragment
import com.background.video.recorder.camera.recorder.util.FirebaseEvents.Companion.logAnalytic
import com.background.video.recorder.camera.recorder.util.SharePref
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback


class PhotoEditorCollageMakerFragment : Fragment() {
    var sharedPrefs: SharedPreferences? = null
    private val binding by lazy {
        FragmentPhotoEditorCollageMakerBinding.inflate(layoutInflater)
    }
    var native_bg = false
//    private lateinit var binding: FragmentPhotoEditorCollageMakerBinding

    var photoeditorcollagemaker_home_admob_native = false
    var intentToPicker: Intent? = null


    private var photoeditorcollagemaker_admob_native = true
    private var photoeditorcollagemaker_facebook_native = true
    private var photoeditorcollagemaker_home_admob_inter = true
    private var facebook_native_ad_id = ""
    private var admob_native_id = ""
    private var prefs: SharedPrefsHelper? = null
    private var admob_interstitial_home_id = ""
    private var photoeditorcollagemaker_home_admob_inter_id = ""

    private lateinit var config: Config
    private lateinit var args: Bundle

    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_photo_editor_collage_maker, container, false)
        chkPermission()
        sharedPrefs = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        photoeditorcollagemaker_home_admob_native = sharedPrefs!!.getBoolean("home_inter", false)
        config =
            Config()

        navController = Navigation.findNavController(activity!!, R.id.fragmentContainerViewHome)
        prefs = SharedPrefsHelper.getInstance(activity)
        val localPrefs = prefs
        if (localPrefs != null) {
            photoeditorcollagemaker_admob_native =
                localPrefs.getphotoeditorcollagemaker_admob_nativeSwitch()
            photoeditorcollagemaker_admob_native =
                true
            photoeditorcollagemaker_facebook_native =
                localPrefs.getphotoeditorcollagemaker_facebook_nativeSwitch()
            photoeditorcollagemaker_home_admob_native =
                localPrefs.getphotoeditorcollagemaker_home_admob_nativeSwitch()
           photoeditorcollagemaker_home_admob_native =
                true
            photoeditorcollagemaker_home_admob_inter =
                localPrefs.getphotoeditorcollagemaker_home_admob_interSwitch()
         photoeditorcollagemaker_home_admob_inter =
                true
            admob_native_id = localPrefs.getadmob_native_idId()
            admob_interstitial_home_id = localPrefs.getadmob_interstitial_home_idId()
            photoeditorcollagemaker_home_admob_inter_id =
                localPrefs.getphotoeditorcollagemaker_home_admob_inter_idId()
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId()
            Log.d(
                "TAG",
                "onCreate:  admob_interstitial_splash_id  $photoeditorcollagemaker_admob_native"
            )
            Log.d(
                "",
                "onCreate:  facebook_interstitial_splash_id  $photoeditorcollagemaker_admob_native"
            )
        }


        if (photoeditorcollagemaker_home_admob_inter) {
//            Toast.makeText(getActivity(), "home is on", Toast.LENGTH_SHORT).show();
            activity?.let {
//                loadphotoCollagmakerInterstitial(
//                    it,
//                    photoeditorcollagemaker_home_admob_inter_id
//                )
            }
            Log.d("checkiyty", "home loaded on photoeditor colalge maekr : ")
        } else {
//                            Toast.makeText(getActivity(), "hoem is false", Toast.LENGTH_SHORT).show();
        }
        native_bg = sharedPrefs!!.getBoolean("native_bg", false)
        native_bg = sharedPrefs!!.getBoolean("native_bg", false)


        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.nativeAd.visibility = View.INVISIBLE
        } else if (photoeditorcollagemaker_admob_native) {
            val activity: Activity? = activity
            if (activity != null) {
                loadMediationNative(
                    activity,
                    binding.nativeAd,
                    admob_native_id,
                    binding.nativeAdContainer,
                    R.layout.ad_native_layout_collage
                )
            }
        } else if (photoeditorcollagemaker_facebook_native) {
            // binding.nativeAd.setVisibility(View.GONE);
            val activity: Activity? = activity
            if (activity != null) {
                loadNativeFacebookAd(
                    facebook_native_ad_id,
                    activity,
                    binding.nativeAdContainer
                )
            }
        } else {
            Log.d("checksswitcehs", "onViewCreated: both are off")
            binding.nativeAd.visibility = View.INVISIBLE
        }


        binding.mirrorBtn.setOnClickListener {
        }

        binding.photoEditorbtn.setOnClickListener {

            if (SharePref.getBoolean(AdsKeys.InApp, false)) {
//                config =
//                    Config()
                config.selectionMin = 1
                config.selectionLimit = 1
                ImagePickerFragment.setConfig(config)

                // Create a Bundle to pass arguments
                val args = Bundle()
                args.putString("FromActivity", "EditClick")

                // Get the NavController
//                val navController =
//                    findNavController(activity!!, R.id.fragmentContainerViewHome)

//                navController.navigate(
//                    R.id.action_photoEditorCollageMakerFragment_to_imagePickerFragment,
//                    args
//                )
            } else {
//                val navController =
//                    findNavController(activity!!, R.id.fragmentContainerViewHome)

                config.selectionMin = 1
                config.selectionLimit = 1

                val args = Bundle()
                args.putString("FromActivity", "EditClick")

                // Navigate to ImagePickerFragment using the action and pass the arguments

//                activity?.let { it1 ->
//                    InterstitialAdUtils.showadMobphotoCollagmakerInterstitialAd(
//                        it1,
//                        "",
//                        R.id.action_photoEditorCollageMakerFragment_to_imagePickerFragment,
//                        config,
//                        args,
//                        navController
//                    )
//                }
            }


//            val config =
//                Config()
//            config.selectionMin = 1
//            config.selectionLimit = 1
//            ImagePickerFragment.setConfig(config)
//
//            // Create a Bundle to pass arguments
//            val args = Bundle()
//            args.putString("FromActivity", "EditClick")
//
//            // Get the NavController
//            val navController =
//                findNavController(activity!!, R.id.fragmentContainerViewHome)
//
//            // Navigate to ImagePickerFragment using the action and pass the arguments
//            navController.navigate(
//                R.id.action_photoEditorCollageMakerFragment_to_imagePickerFragment,
//                args
//            )

        }

        binding.collageMakerBtn.setOnClickListener { v ->
            logAnalytic("photoEditor_Screen_collage_btn")

            if (SharePref.getBoolean(AdsKeys.InApp, false)) {
//                config =
//                    Config()
                config.selectionMin = 2
                config.selectionLimit = 9
                ImagePickerFragment.setConfig(config)

                // Create a Bundle to pass arguments
                args = Bundle()
                args.putString("FromActivity", "CollageClick")

                // Get the NavController
                val navController =
                    findNavController(activity!!, R.id.fragmentContainerViewHome)

                // Navigate to ImagePickerFragment using the action and pass the arguments
//                navController.navigate(
//                    R.id.action_photoEditorCollageMakerFragment_to_imagePickerFragment,
//                    args
//                )
            } else {
//                val navController =
//                    findNavController(activity!!, R.id.fragmentContainerViewHome)
                config.selectionMin = 2
                config.selectionLimit = 9


                args = Bundle()
                args.putString("FromActivity", "CollageClick")


//                activity?.let { it1 ->
//                    InterstitialAdUtils.showadMobphotoCollagmakerInterstitialAd(
//                        it1,
//                        "",
//                        R.id.action_photoEditorCollageMakerFragment_to_imagePickerFragment,
//                        config,
//                        args,
//                        navController
//                    )
//                }
            }
        }

        binding.myAlbum.setOnClickListener {

            logAnalytic("photoEditor_Screen_album_btn")
//            startActivityNext(
//                findNavController(requireView()),
//                R.id.action_photoEditorCollageMakerFragment_to_albumFragment
//            )

//            if (SharePref.getBoolean(AdsKeys.InApp, false)) {
//                NavigationUtils.navigate(
//                    requireActivity(),
//                    R.id.action_photoEditorCollageMakerFragment_to_albumFragment
//                )
//            } else {
//                val bundle = Bundle()
//                activity?.let { it1 ->
//                    InterstitialAdUtils.showadMobphotoCollagmakerInternalInterstitialAd(
//                        it1,
//                        "",
//                        R.id.action_photoEditorCollageMakerFragment_to_albumFragment,
//                        bundle,
//                        navController
//                    )
//                }
//            }


//            val navController = NavHostFragment.findNavController(this)
//            navController.navigate(R.id.action_photoEditorCollageMakerFragment_to_albumFragment)

        }


        return binding.root
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                // Handle the Intent data here
            }
        }


    fun startActivityNextIntent(
        intent: Intent
    ) {
        if (adMobHomeInterstitialAd != null) {
            adMobHomeInterstitialAd!!.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()

                        startForResult.launch(intent)
                        adMobHomeInterstitialAd = null

                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        super.onAdFailedToShowFullScreenContent(adError)
                        adMobHomeInterstitialAd = null
                        //                    AdsHelper_new.isInterstitialShowing = false;
                        //                    startFragment();
                        startForResult.launch(intent)
                        // AdsHelper.loadInterstitialAd(requireActivity());
                    }
                }
            adMobHomeInterstitialAd!!.show(requireActivity())
            //            AdsHelper_new.isShowingInterstitial = true;
        } else {
            startForResult.launch(intent)
        }
    }

    var checkPermission: Boolean = false
    private fun chkPermission() {
        val context = activity ?: return

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            // Android 10 (API 29) or above
//            Dexter.withContext(context)
//                .withPermissions(
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.CAMERA
//                )
//                .withListener(object : MultiplePermissionsListener {
//                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
//                        checkPermission = report.areAllPermissionsGranted()
//                        if (!checkPermission) {
//                            showSettingDialog()
//                        }
//                    }
//
//                    override fun onPermissionRationaleShouldBeShown(
//                        list: List<PermissionRequest>,
//                        token: PermissionToken
//                    ) {
//                        token.continuePermissionRequest()
//                    }
//                })
//                .withErrorListener {
//                    Toast.makeText(context, "Error occurred while granting permissions", Toast.LENGTH_SHORT).show()
//                }
//                .onSameThread()
//                .check()
//        } else {
//            // Android 9 (Pie) or below
//            // Continue with your existing code for lower Android versions
//        }
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE =
            1 // or any other unique integer
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission was granted
                } else {
                    // Permission denied
                }
            }
        }
    }


    private fun showSettingDialog() {
        val builder = activity?.let { AlertDialog.Builder(it) }
        if (builder != null) {
            builder.setTitle("Need Permissions")
        }
        if (builder != null) {
            builder.setMessage("This App Needs permission to use this feature. You can grant this fom app settings")
        }
        if (builder != null) {
            builder.setPositiveButton(
                "Goto Settings"
            ) { dialogInterface, i -> openSettings() }
        }
        if (builder != null) {
            builder.setNegativeButton(
                "Cancel"
            ) { dialogInterface, i -> dialogInterface.cancel() }
        }
        if (builder != null) {
            builder.show()
        }
    }

    private fun openSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", requireActivity().getPackageName(), null)
        intent.data = uri
        startActivity(intent)
    }
}
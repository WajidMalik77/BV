package com.background.video.recorder.camera.recorder.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.background.video.recorder.camera.recorder.R
import com.background.video.recorder.camera.recorder.adapters.TrimAdapter
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils.loadMediationNative
import com.background.video.recorder.camera.recorder.databinding.FragmentTrimBinding
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper
import com.background.video.recorder.camera.recorder.model.MediaFiles
import com.background.video.recorder.camera.recorder.ui.activitiy.VideoTrimmerActivity
import com.background.video.recorder.camera.recorder.util.FirebaseEvents
import com.background.video.recorder.camera.recorder.util.SharePref
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils
import com.background.video.recorder.camera.recorder.viewmodel.MediaFilesViewModel


class TrimFragment : Fragment() {
    private lateinit var binding: FragmentTrimBinding
    private var viewModel: MediaFilesViewModel? = null
    var native_bg = false
    var sharedPrefs: SharedPreferences? = null


    private var trim_admob_native = true
    private var trim_facebook_native = true
    private var facebook_native_ad_id = ""
    private var admob_native_id = ""
    private var prefs: SharedPrefsHelper? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTrimBinding.inflate(layoutInflater, container, false)

        Log.d("hoem_frag", "onCreateView: Trim  fargment")

        sharedPrefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//        native_bg = sharedPrefs.getBoolean("native_bg", false)
        native_bg = sharedPrefs!!.getBoolean("native_bg", false)

        prefs = SharedPrefsHelper.getInstance(activity)
        val localPrefs = prefs
        if (localPrefs != null) {
            trim_admob_native = localPrefs.gettrim_admob_nativeSwitch()
//            trim_admob_native = true
            trim_facebook_native = localPrefs.gettrim_facebook_nativeSwitch()
            admob_native_id = localPrefs.getadmob_native_idId()
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId()
            Log.d(
                "TAG",
                "onCreate:  admob_interstitial_splash_id  $trim_admob_native"
            )
            Log.d(
                "",
                "onCreate:  facebook_interstitial_splash_id  $trim_admob_native"
            )
        }



        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
           binding.nativeAd.visibility = View.INVISIBLE
        } else if (trim_admob_native) {
            val activity: Activity? = activity
            if (activity != null) {
                loadMediationNative(
                    activity,
                    binding.nativeAd,
                    admob_native_id,
                    binding.nativeAdContainer,
                    R.layout.ad_native_layout_home
                )
            }
        } else if (trim_facebook_native) {
            // binding.nativeAd.setVisibility(View.GONE);
            val activity: Activity? = activity
            if (activity != null) {
                InterstitialAdUtils.loadNativeFacebookAd(
                    facebook_native_ad_id,
                    activity,
                    binding.nativeAdContainer
                )
            }
        } else {
            Log.d("checksswitcehs", "onViewCreated: both are off")
            binding.nativeAd.visibility = View.GONE
        }



        if (isAdded) {
            viewModel = ViewModelProvider(
                requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(
                    requireActivity()
                        .application
                ) as ViewModelProvider.Factory
            )[MediaFilesViewModel::class.java]
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showAd()
        getVideoFiles()
        FirebaseEvents.Companion.logAnalytic("Trim_Screen_Show")
    }

    private fun showAd() {

//        if (activity != null)
//            AdsManager.nativee(
//                SplashTwoFragment.getBoolean(AdsKeys.Admob_Trim_Video_Native),
//                SplashTwoFragment.getBoolean(AdsKeys.Facebook_Trim_Video_Native),
//                requireActivity(), binding.nativeAd
//            )

//        AdsManager.interstitial(
//            SplashTwoFragment.getBoolean(AdsKeys.Admob_DashBoard_Trim_Video_Click_Inter),
//            SplashTwoFragment.getBoolean(AdsKeys.Facebook_DashBoard_Trim_Video_Click_Inter),
//            AdsKeys.Admob_DashBoard_Trim_Video_Click_Inter,
//            AdsKeys.Facebook_DashBoard_Trim_Video_Click_Inter,
//            requireActivity()
//        ) {}
    }

    private fun getVideoFiles() {
        if (isAdded)
            viewModel!!.allMediaFiles.observe(
                requireActivity()
            ) { mediaFiles ->

                val tempList = ArrayList<Any>(mediaFiles)
                tempList.add(0, "ad")

                if (mediaFiles.isEmpty())
                {
                    binding.nativeAd.setVisibility(View.GONE)
                    binding.empty.visibility = View.VISIBLE
                }
                else{

                    binding.empty.visibility = View.GONE
                }

                if (isAdded) {
                    val adapter = TrimAdapter(requireContext(),
                        mediaFiles as java.util.ArrayList<MediaFiles>
                    ) {
                        val intent = Intent(requireActivity(), VideoTrimmerActivity::class.java)
                        intent.putExtra("EXTRA_PATH", it.path)
                        startActivity(intent)

//                        val intent = Intent(requireContext(), VideoToGIFActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                        intent.putExtra("videoPath",it.path)
//                        startActivity(intent)



                    }
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerView.adapter = adapter
                }


            }
    }

}
package com.background.video.recorder.camera.recorder.onboarding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.background.video.recorder.camera.recorder.R
import com.background.video.recorder.camera.recorder.adapters.ScreenBoardingAdapter
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils
import com.background.video.recorder.camera.recorder.ads.InterstitialClass
import com.background.video.recorder.camera.recorder.databinding.ActivityOnboardingBinding
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper
import com.background.video.recorder.camera.recorder.ui.practice.SharePref
import com.background.video.recorder.camera.recorder.util.FirebaseEvents
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys
import com.background.video.recorder.camera.recorder.util.constant.Constants.isOnBoarding
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils
import roozi.app.ads.AdsHelper


class OnBoardingFragment : Fragment() {
    private val binding by lazy {
        ActivityOnboardingBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: ScreenBoardingAdapter
    var nativeSwitch = false
    var natvieAdID: String? = null
    var prefs: SharedPrefsHelper? = null

    private var onboarding_native = true
    private var onbaording_native_facebook = true
    private var onboarding_interstitial = true
    private var admob_interstitial_home_id = ""
    private var onboarding_native_admob_id = ""
    private var facebook_native_ad_id = ""
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        if (context != null)
            if (SharePref.getBoolean(AdsKeys.InApp, false) ||
                !AdsHelper.isNetworkAvailable(requireContext())
            ) {
                binding.nativeAdonBoarding.visibility = View.GONE
            }


        navController = Navigation.findNavController(activity!!, R.id.fragmentContainerViewHome)
        prefs = SharedPrefsHelper.getInstance(activity)
        val localPrefs = prefs
        if (localPrefs != null) {
            onboarding_native = localPrefs.getonboarding_nativeSwitch()
            onbaording_native_facebook = localPrefs.getonbaording_native_facebookSwitch()
            onboarding_interstitial = localPrefs.getonboarding_interstitialSwitch()
//            onboarding_interstitial = true
            admob_interstitial_home_id = localPrefs.getadmob_interstitial_home_idId()
            onboarding_native_admob_id = localPrefs.getonboarding_native_admob_idId()
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId()

            Log.d("langFrag", "onboarding_native: " + onboarding_native)
        }

        setViewPager()
        binding.skipBtn.setOnClickListener {

            if (onboarding_interstitial) {
                InterstitialClass.requestInterstitial(
                    activity, admob_interstitial_home_id
                ) { NavigationUtils.navigate(activity, R.id.homeFragment) }
            } else {
                NavigationUtils.navigate(activity, R.id.homeFragment)
            }

        }

        binding.nextBtn.setOnClickListener {

            FirebaseEvents.logAnalytic("onboarding_screen_btn")

            when (binding.viewPager.currentItem) {
                0 -> {
                    binding.viewPager.setCurrentItem(1, true)
                }

                1 -> {
                    binding.viewPager.setCurrentItem(2, true)
                }

                2 -> {

                    if (com.background.video.recorder.camera.recorder.util.SharePref.getBoolean(
                            AdsKeys.InApp,
                            false
                        )
                    ) {
                        NavigationUtils.navigate(activity, R.id.homeFragment)
                        SharePref.putBoolean(isOnBoarding, true)
                        FirebaseEvents.logAnalytic("onboarding_screen_finish_btn")
                    } else {

                        if (onboarding_interstitial) {
                            InterstitialClass.requestInterstitial(
                                activity, admob_interstitial_home_id
                            ) { NavigationUtils.navigate(activity, R.id.homeFragment) }
                        } else {
                            NavigationUtils.navigate(activity, R.id.homeFragment)
                        }
                        SharePref.putBoolean(isOnBoarding, true)
                        FirebaseEvents.logAnalytic("onboarding_screen_finish_btn")


                    }
                }
            }
        }
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                Log.d(
                    "PagePosition",
                    "onPageScrolled: $position :: $positionOffset :: $positionOffsetPixels"
                )
                if (position == 2) {
                    binding.nextBtn.setText(requireContext().getString(R.string.finish));
                    binding.nextBtn.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        null,
                        null
                    ); // Remove drawable
                } else {
                    binding.nextBtn.setText("Next");
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
        return binding.root
    }

    private fun loadAds() {
        context?.let { ctx ->
            if (SharePref.getBoolean(AdsKeys.InApp, false) ||
                !AdsHelper.isNetworkAvailable(ctx)
            ) {
                binding.nativeAdonBoarding.visibility = View.GONE
            } else {
                if (onboarding_native) {
                    activity?.let {
                        InterstitialAdUtils.loadMediationNative(
                            it,
                            binding.nativeAdonBoarding,
                            onboarding_native_admob_id,
                            binding.nativeAdContainer,
                            R.layout.ad_native_layout_modified
                        )
                    }

                } else if (onbaording_native_facebook) {
                    activity?.let {
                        InterstitialAdUtils.loadNativeFacebookAd(
                            facebook_native_ad_id,
                            it,
                            binding.nativeAdContainer
                        )
                    }
                } else {
                    binding.nativeAdonBoarding.visibility = View.GONE
                    Log.d("checksswitcehs", "onViewCreated: both are off")
                }
            }
        }
    }


    private lateinit var back: ImageView
    override fun onResume() {
        super.onResume()
        loadAds()

        if (!::back.isInitialized) {
            back = requireActivity().findViewById(R.id.back)
            back.setOnClickListener {
                FirebaseEvents.logAnalytic("onboarding_back_btn")

                if (binding.viewPager.currentItem > 0) {
                    // Move to the previous page
                    binding.viewPager.currentItem = binding.viewPager.currentItem - 1
                } else {
                    // Handle the back press as needed, e.g., close the fragment/activity
                    activity?.onBackPressed()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (SharePref.getBoolean(AdsKeys.InApp, false)
        ) {
            binding.nativeAdonBoarding.visibility = View.GONE
        } else if (onboarding_native) {
            activity?.let {
                InterstitialAdUtils.loadMediationNative(
                    it,
                    binding.nativeAdonBoarding,
                    onboarding_native_admob_id,
                    binding.nativeAdContainer,
                    R.layout.ad_native_layout_modified
                )
            }

        } else if (onbaording_native_facebook) {
            activity?.let {
                InterstitialAdUtils.loadNativeFacebookAd(
                    facebook_native_ad_id,
                    it,
                    binding.nativeAdContainer
                )
            }

        } else {
            Log.d("checksswitcehs", "onViewCreated: both are off")
            binding.nativeAdonBoarding.visibility = View.GONE
        }

    }

    private fun setViewPager() {
        if (isAdded) {
            adapter = ScreenBoardingAdapter(requireActivity())
            binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            binding.viewPager.adapter = adapter
            binding.viewPager.isSaveEnabled = false
            binding.viewPager.currentItem = 0
        }
    }
}
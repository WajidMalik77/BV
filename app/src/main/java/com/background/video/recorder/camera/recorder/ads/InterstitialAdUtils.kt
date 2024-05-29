package com.background.video.recorder.camera.recorder.ads

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.background.video.recorder.camera.recorder.R
import com.background.video.recorder.camera.recorder.ads.AdsHelperInternet.Companion.isNetworkAvailable
import com.background.video.recorder.camera.recorder.util.SharePref
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys
import com.facebook.ads.Ad
import com.facebook.ads.InterstitialAdListener
import com.facebook.ads.NativeAdLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.material.button.MaterialButton
import roozi.app.ads.AdsManager


object InterstitialAdUtils {
    val TAG = "TagInterstitialAd"
    var adMobHomeInterstitialAd: InterstitialAd? = null
    var adMobLanguageInterstitialAd: InterstitialAd? = null

    var interstitialFacebook: com.facebook.ads.InterstitialAd? = null
    var nativeAdLayout: com.facebook.ads.NativeAdLayout? = null
    var nativeAd: com.facebook.ads.NativeAd? = null

    var isInterstitialShowing = false
    val adLoaded = MutableLiveData<Boolean>()
    private var nativeAdLoaded = false

    private val isAdLoaded = MutableLiveData<Boolean>()


    fun isPurchased(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("IsPurchased", false)
    }

    private var isFacebookAdLoaded = false
    var isFirstTimeFacebookAdShown = false // Global flag
    fun loadInterstitialFacebookAd(
        context: Context,
        adUnitId: String,
        activity: Activity,
        intent: Intent
    ) {
        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            return
        }
        if (isPurchased(context)) {
            Log.d("checkiyty", "User has purchased. Skipping ad loading.")
            return  // Skip loading ads if user has purchased the premium version
        }


//        interstitialFacebook = com.facebook.ads.InterstitialAd(context, adUnitId)
        interstitialFacebook = com.facebook.ads.InterstitialAd(
            context,
//            "~VID_HD_16_9_15S_APP_INSTALL#YOUR_PLACEMENT_ID"
//            "VID_HD_16_9_15S_APP_INSTALL#YOUR_PLACEMENT_ID"
//            "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID"
            adUnitId
//            "1214499669410369_1220823585444644"
        )
        val adListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad) {
                // Code to be executed when the interstitial ad is displayed.
            }

            override fun onInterstitialDismissed(ad: Ad) {
                interstitialFacebook = null
                isFacebookAdLoaded = false
                Log.d("checkiyty", "Current Activity: ${activity::class.java.simpleName}")

                Log.d("checkiyty", "onFacebookInterstitialDismissed: ")

                loadInterstitialFacebookAd(context, adUnitId, activity, intent)

                if (isFirstTimeFacebookAdShown) {
                    // Navigate within the app
                    // For example, use a NavController to navigate to a specific fragment
                } else {
                    // First time ad dismissal, start a new activity
                    startMainActivity(activity, intent)
                    isFirstTimeFacebookAdShown = true // Update the flag
                }


//                startMainActivity(activity)
                Log.d("checkiyty", "onInterstitialDismissed: ad loaded again")
                // Code to be executed when the interstitial ad is dismissed.
            }


            override fun onError(p0: Ad?, p1: com.facebook.ads.AdError?) {

                interstitialFacebook = null
                isFacebookAdLoaded = false
                adLoaded.value = false

                isAdLoaded.postValue(false)  // Notify that ad is loaded
                Log.e("checkiyty", "Facebook Interstitial ad failed to load: ${p1!!.errorMessage}")
            }

            override fun onAdLoaded(ad: Ad) {
                Log.d("checkiyty", "facebook onAdLoaded")
                isFacebookAdLoaded = true
                adLoaded.value = true

                isAdLoaded.postValue(true)  // Notify that ad is loaded
                // Code to be executed when the ad finishes loading.
            }

            override fun onAdClicked(ad: Ad) {
                // Code to be executed when the ad is clicked.
            }

            override fun onLoggingImpression(ad: Ad) {
                // Code to be executed when an impression is logged.
            }
        }

        interstitialFacebook?.loadAd(
            interstitialFacebook!!.buildLoadAdConfig()
                .withAdListener(adListener)
                .build()
        )
    }


    private fun startMainActivity(activity: Activity?, intent: Intent) {
        activity?.let {
//            val intent = Intent(it, HomeActivity::class.java)
//            val intent = Intent(it, LanguageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            it.startActivity(intent)
            it.finish()
        } ?: run {
            Log.e("InterstitialAdUtils", "Activity context is null.")
            // Handle the null activity scenario, maybe by logging or a fallback mechanism
        }
    }


    fun loadMediationNative(
        activity: Activity,
        frameLayout: FrameLayout,
        nativeAdId: String,
        nativeAdLayout: NativeAdLayout,
        layoutId: Int // Pass the layout ID as a parameter
    ) {
        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            return
        }
        if (!isNetworkAvailable(activity)) {
            frameLayout.visibility = View.GONE
            return
        }
        if (isPurchased(activity)) {
            Log.d("racing", "User has purchased. Skipping ad loading.")
            return  // Skip loading ads if user has purchased the premium version
        }

        Log.d("racing", "loadMediationNative: ${activity.localClassName}")

//        if (activity.localClassName == "activities.StartActivity") {
//            val adLoader = AdLoader.Builder(activity, activity.getString(R.string.native_id_start))
//        val adLoader = AdLoader.Builder(activity, nativeAdId) //orignal
//        val adLoader = AdLoader.Builder(activity, "ca-app-pub-3940256099942544/2247696110~")
//        val adLoader = AdLoader.Builder(activity, "ca-app-pub-3940256099942544/2247696110")
        val adLoader = AdLoader.Builder(activity, nativeAdId)
            .forNativeAd { nativeAd ->
                nativeAdLoaded = true
                val adView = activity.layoutInflater.inflate(
//                    R.layout.ad_native_layout_new,
//                    R.layout.ad_native_layout_modified,
                    layoutId,
                    null
                ) as NativeAdView
                populateMediationNativeAdView(nativeAd, adView,activity)
                frameLayout.removeAllViews()
                frameLayout.addView(adView)

                Log.d("racing", "onAdFailedToLoad: ${nativeAdId}")
//                    Toast.makeText(activity, "native loaded", Toast.LENGTH_SHORT).show()
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    nativeAdLoaded = false
                    loadNativeFacebookAd(
                        "1214499669410369_1220823998777936",
                        activity,
                        nativeAdLayout
                    )
//                        Toast.makeText(activity, "native failed"+adError.message, Toast.LENGTH_SHORT).show()
                    Log.d("racing", "onAdFailedToLoad: ${adError.message}")
//                    ToastEnabled.showToast(activity, "Toast of native" + adError.message)
                }

                override fun onAdClicked() {
                    super.onAdClicked()

                    AdsManager.isInterShowing = true
                }


            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }


    private fun populateMediationNativeAdView(nativeAd: NativeAd, adView: NativeAdView, activity:Activity) {
        val mediaView = adView.findViewById<MediaView>(R.id.ad_media)
        adView.mediaView = mediaView
        adView.mediaView?.mediaContent = nativeAd.mediaContent

        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.iconView = adView.findViewById(R.id.ad_icon)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
//        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        (adView.headlineView as TextView).text = nativeAd.headline

        if (nativeAd.body == null) {
            adView.bodyView?.visibility = View.GONE
        } else {
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.icon == null) {
            adView.iconView?.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(nativeAd.icon!!.drawable)
        }

//        if (nativeAd.advertiser == null) {
//            adView.advertiserView?.visibility = View.GONE
//        } else {
//            (adView.advertiserView as TextView).text = nativeAd.advertiser
//        }

        adView.callToActionView?.setOnClickListener {
            Log.d(TAG, "populateMediationNativeAdView: Clicked")
            Toast.makeText(activity, "clciked", Toast.LENGTH_SHORT).show()
            AdsManager.isInterShowing = true
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView?.visibility = View.GONE
        } else {
            (adView.callToActionView as TextView).text = nativeAd.callToAction
        }


        adView.setNativeAd(nativeAd)
    }

    ////////////////////////////////////////Facebook Native AD///////////////////////////////////////////

    fun loadNativeFacebookAd(
        adUnitId: String,
        context: Activity,
        nativeAdLayout: NativeAdLayout
    ) {
        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            return
        }
        if (isPurchased(context)) {
            Log.d("racing", "User has purchased. Skipping ad loading.")
            return  // Skip loading ads if user has purchased the premium version
        }

//        nativeAd = com.facebook.ads.NativeAd(context, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID")
        nativeAd = com.facebook.ads.NativeAd(context, adUnitId)
        val nativeAdListener: com.facebook.ads.NativeAdListener =
            object : com.facebook.ads.NativeAdListener {
                override fun onMediaDownloaded(ad: Ad?) {}

                override fun onError(p0: Ad?, p1: com.facebook.ads.AdError?) {

                    Log.d("facebookis", "onError: " + p1!!.errorMessage)
//                    Toast.makeText(context, ""+p1.errorMessage.toString(), Toast.LENGTH_SHORT).show()
                }


                override fun onAdLoaded(ad: Ad) {
                    if (nativeAd == null || nativeAd !== ad) {
                        return
                    }

                    Log.d("facebookis", "onAdloaded: Facebook Native AD loaded")

//                isAdLoaded = true // Set the flag to true as the ad is now loaded

//                inflateAd(nativeAd);
//                ToastEnabled.Companion.showToast(context, "Facebook Loaded: Native")
//                    inflateAd(nativeAd!!, nativeAdLayout!!, context) // Use the stored NativeAdLayout
//                    nativeAdLayout?.let { layout ->
//                        inflateAd(nativeAd!!, layout, context)
//                    }

                    inflateAd(nativeAd!!, nativeAdLayout!!, context)
                }

                override fun onAdClicked(ad: Ad?) {}
                override fun onLoggingImpression(ad: Ad?) {}
            }
        nativeAd!!.loadAd(
            nativeAd!!.buildLoadAdConfig()
                .withAdListener(nativeAdListener)
                .build()
        )
    }

    private fun inflateAd(
        nativeAd: com.facebook.ads.NativeAd,
        nativeAdLayout: com.facebook.ads.NativeAdLayout,
        context: Context
    ) {
        nativeAd.unregisterView()
        val inflater = LayoutInflater.from(context)
        val adView: View = inflater.inflate(R.layout.item_native_ad_facebook, nativeAdLayout, false)
        nativeAdLayout.addView(adView)
        val adChoicesContainer = adView.findViewById<LinearLayout>(R.id.ad_choices_container)
        val adOptionsView = com.facebook.ads.AdOptionsView(context, nativeAd, nativeAdLayout)
        adChoicesContainer.removeAllViews()
        adChoicesContainer.addView(adOptionsView, 0)
        val nativeAdIcon: com.facebook.ads.MediaView = adView.findViewById(R.id.native_ad_icon)
        val nativeAdTitle = adView.findViewById<TextView>(R.id.native_ad_title)
        val nativeAdMedia: com.facebook.ads.MediaView = adView.findViewById(R.id.native_ad_media)
        val nativeAdSocialContext = adView.findViewById<TextView>(R.id.native_ad_social_context)
        val nativeAdBody = adView.findViewById<TextView>(R.id.native_ad_body)
        val sponsoredLabel = adView.findViewById<TextView>(R.id.native_ad_sponsored_label)
        val nativeAdCallToAction =
            adView.findViewById<MaterialButton>(R.id.native_ad_call_to_action)
        nativeAdTitle.setText(nativeAd.getAdvertiserName())
        nativeAdBody.setText(nativeAd.getAdBodyText())
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext())
        nativeAdCallToAction.visibility =
            if (nativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction())
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation())
        val clickableViews: MutableList<View> = ArrayList()
        clickableViews.add(nativeAdTitle)
        clickableViews.add(nativeAdCallToAction)
        clickableViews.add(nativeAdIcon)
        nativeAd.registerViewForInteraction(
            adView, nativeAdMedia, nativeAdIcon, clickableViews
        )
    }
}
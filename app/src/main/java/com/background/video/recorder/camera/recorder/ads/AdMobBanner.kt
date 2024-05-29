package com.background.video.recorder.camera.recorder.ads

import android.app.Activity
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper
import com.background.video.recorder.camera.recorder.util.SharePref
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys
import com.facebook.ads.Ad
import com.google.android.gms.ads.*
import roozi.app.ads.AdsManager


object AdMobBanner {

    private var mAdView: AdView? = null
    fun isPurchased(activity: Activity): Boolean {
        val prefs = SharedPrefsHelper.getInstance(activity.applicationContext)
        return prefs.getBoolean("has_purchased", false)
    }

    fun showAdMobBanner(
        activity: Activity,
        adMobContainer: RelativeLayout,
        adUnitId: String,
        adUnitIdFB: String
    ) {

        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            return
        }
        if (isPurchased(activity)) {
            Log.d("bannercheckit", "User has purchased. Skipping ad loading.")
            return
        }

        Log.d("bannercheckit", "showBannerAd after inpurhcase: AdMobBanner ")
        val prefs = SharedPrefsHelper.getInstance(activity.applicationContext)

        // Check if the banner ad switch is on
        val isBannerAdEnabled: Boolean = prefs.bannerSwitch
        val facebook_banner: Boolean = prefs.getfacebook_banner()

        val hasPurchased =
            prefs.getBoolean("has_purchased", false) // Retrieve has_purchased from SharedPrefs

//        if (isBannerAdEnabled) {
//            Log.d("bannercheckit", "isBannerAdEnabled: AdMobBanner ")
////             Get the banner ad ID from shared preferences
////            val bannerAdId = prefs.bannerAdId
//
////            if (bannerAdId.isNotEmpty()) {
//                loadAd(activity, adMobContainer, adUnitId, adUnitIdFB)
//            } else {
//                Log.e("bannercheckit", "Banner Ad ID not found in shared preferences")
////            }
//        } else if (facebook_banner) {
//            Log.d("bannercheckit", "facebook_banner: facebook_banner ")
//
            loadAdFifty(adMobContainer, activity, adUnitIdFB)
////            Log.e("AdMobBanner", "Banner Ad is disabled in shared preferences")
//        }

    }

//    open fun loadAd(
//        activity: Activity,
//        adMobContainer: RelativeLayout,
//        adUnitId: String,
//        adUnitIdFB: String
//    ) {
//        mAdView = AdView(activity)
//        mAdView!!.setAdSize(getAdSize(activity)!!)
////        mAdView!!.adUnitId = adUnitId
////        mAdView!!.adUnitId = "ca-app-pub-9304966727485663/4173431102"
//        mAdView!!.adUnitId = "ca-app-pub-3940256099942544/9214589741"
////        mAdView!!.adUnitId = "ca-app-pub-3940256099942544/6300978111~"
////        adMobContainer.addView(mAdView)
//        val adRequest = AdRequest.Builder().build()
//        mAdView!!.adListener = object : AdListener() {
//
//            override fun onAdLoaded() {
//                adMobContainer.visibility = View.VISIBLE
//                Log.e("bannercheckit", "Banner:  Loaded")
//            }
//
//            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                super.onAdFailedToLoad(loadAdError)
//                Log.e("bannercheckit", "Banner: " + loadAdError.message)
//                loadAdFifty(adMobContainer, activity, adUnitIdFB)
//            }
//        }
//        mAdView!!.loadAd(adRequest)
//    }


    fun loadAd(
        activity: Activity,
        adMobContainer: RelativeLayout,
        adUnitId: String,
        adUnitIdFB: String
    ) {
        if (!AdsHelperInternet.isNetworkAvailable(activity)) {
            adMobContainer.visibility = View.GONE
            return
        }
        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            return
        }

        mAdView = AdView(activity)
        mAdView!!.setAdSize(getAdSize(activity)!!)
        mAdView!!.adUnitId = adUnitId
//        mAdView!!.adUnitId = "ca-app-pub-3940256099942544/6300978111"

        val adRequest = AdRequest.Builder().build()
        mAdView!!.adListener = object : AdListener() {

            override fun onAdLoaded() {
                adMobContainer.visibility = View.VISIBLE
                Log.e("bannercheckit", "Banner:  Loaded")
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                Log.e("bannercheckit", "Banner: ${loadAdError.message}")
                // Implement loadAdFifty here or call if it's implemented elsewhere
            }

            override fun onAdClicked() {
                super.onAdClicked()

                AdsManager.isInterShowing = true
            }
        }
        adMobContainer.addView(mAdView)
        mAdView!!.loadAd(adRequest)
    }


    fun destroyAdMobBanner() {
        mAdView?.destroy()
        mAdView = null
    }

    private fun getAdSize(activity: Activity): AdSize? {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        val display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
    }

    open fun loadAdFifty(adContainer: RelativeLayout, activity: Activity, adUnitIdFB: String) {
        if (!AdsHelperInternet.isNetworkAvailable(activity)) {
            adContainer.visibility = View.GONE
            return
        }
        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            return
        }

        val adView: com.facebook.ads.AdView = com.facebook.ads.AdView(
            activity,
            adUnitIdFB,
//            "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID",
            com.facebook.ads.AdSize.BANNER_HEIGHT_50
        )
        adContainer.addView(adView)
        val adListener: com.facebook.ads.AdListener = object : com.facebook.ads.AdListener {
            override fun onError(ad: Ad, adError: com.facebook.ads.AdError) {
//                Toast.makeText(
//                    this@Ad_Banner,
//                    "Ad 50 Error: " + adError.errorMessage,
//                    Toast.LENGTH_SHORT
//                    Toast.LENGTH_SHORT
//                ).show()
//

                Log.e("bannercheckit", "Facebook banner: " + adError.errorMessage)
            }

            override fun onAdLoaded(ad: Ad) {

                Log.e("bannercheckit", "Banner: facebook loaded ")
//                Toast.makeText(this@Ad_Banner, "Ad Loaded", Toast.LENGTH_SHORT).show()
            }

            override fun onAdClicked(ad: Ad) {

            }

            override fun onLoggingImpression(ad: Ad) {

            }

        }
        val loadAdConfig = adView.buildLoadAdConfig()
            .withAdListener(adListener)
            .build()
        adView.loadAd(loadAdConfig)
    }
}


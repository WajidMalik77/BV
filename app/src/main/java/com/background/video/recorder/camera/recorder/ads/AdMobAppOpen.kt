//package com.background.video.recorder.camera.recorder.ads
//
//import android.app.Activity
//import android.content.Context
//import android.util.Log
//import com.background.video.recorder.camera.recorder.util.SharePref
//import com.background.video.recorder.camera.recorder.util.constant.AdsKeys
//import com.google.android.gms.ads.*
//import com.google.android.gms.ads.appopen.AppOpenAd
//
//object AdMobAppOpen {
//    fun isPurchased(context: Context): Boolean {
//        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
//        return sharedPreferences.getBoolean("IsPurchased", false)
//    }
//
//    private var isShowingOpenAd: Boolean = false
//    private var isLoadingOpenAd: Boolean =false
//    val TAG = "TAGappOpenAd"
//    var appOpenAd: AppOpenAd? = null
//
//    fun isOpenAdAvailable(): Boolean {
//        return appOpenAd != null
//    }
//
//    fun showOpnAd(activity: Activity){
//
//        if (!isOpenAdAvailable())
//        {
//            loadOpenAd(activity)
//            return
//        }
//
//        if (InterstitialAdUtils.isInterstitialShowing){
//            return
//        }
//
//        appOpenAd?.setFullScreenContentCallback(
//            object : FullScreenContentCallback() {
//
//                override fun onAdDismissedFullScreenContent() {
//                    // Called when full screen content is dismissed.
//                    // Set the reference to null so isAdAvailable() returns false.
//                    Log.d(TAG, "Ad dismissed fullscreen content.")
//                    appOpenAd = null
//                    isShowingOpenAd = false
//                    loadOpenAd(activity)
//
//                }
//
//                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
//                    // Called when fullscreen content failed to show.
//                    // Set the reference to null so isAdAvailable() returns false.
//                    Log.d(TAG,"ad failde to load"+ adError.message)
//                    appOpenAd = null
//                    isShowingOpenAd = false
//
//
//                }
//
//                override fun onAdShowedFullScreenContent() {
//                    // Called when fullscreen content is shown.
//                    Log.d(TAG, "Ad showed fullscreen content.")
//                }
//            })
//        isShowingOpenAd = true
//        appOpenAd?.show(activity)
//
//    }
//
//
//    fun loadOpenAd(context: Activity) {
//        if(SharePref.getBoolean(AdsKeys.InApp, false)) {
//            Log.d("tagaddzy", "InApp splash true: ")
//            return
//        }
//        if (AdMobBanner.isPurchased(context)) {
//            Log.d("checkiyty", "User has purchased. Skipping ad loading.")
//            return
//        }
//
//
//        // Fetch appOpen ad ID from SharedPreferences
//        val sharedPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//        val appOpenAdId = sharedPrefs.getString("appOpen_id", "default_appOpen_id") ?: "default_appOpen_id"
//
//        if (isLoadingOpenAd || isOpenAdAvailable()) {
//            Log.d(TAG, "appopenad: already loaded or loading")
//            return
//        }
//
//
//
//
//        isLoadingOpenAd = true
//        val request = AdRequest.Builder().build()
//        AppOpenAd.load(
//            context, appOpenAdId, request,
////            context, "ca-app-pub-3940256099942544/9257395921", request,
//            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
//            object : AppOpenAd.AppOpenAdLoadCallback() {
//                override fun onAdLoaded(ad: AppOpenAd) {
//                    Log.d(TAG, "app open Ad open was loaded.")
//                    appOpenAd = ad
//                    isLoadingOpenAd = false
//                }
//
//                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                    Log.d(TAG,"app open ad error"+ loadAdError.message)
//                    isLoadingOpenAd = false
//                    appOpenAd = null
//                }
//            })
//    }
//
//
//}
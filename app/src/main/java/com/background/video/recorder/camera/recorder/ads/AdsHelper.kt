package com.background.video.recorder.camera.recorder.ads

import android.app.Activity
import android.util.Log
import android.widget.RelativeLayout
import com.google.android.gms.ads.appopen.AppOpenAd

object AdsHelper {

    lateinit var adMobBannerId: String
    lateinit var adMobNativeId: String
    var adMobAppOpenId = "ca-app-pub-3940256099942544/3419835294"
    lateinit var adMobInterstitialId: String
    val TAG = "admob"
    var appOpenAdStatus: Boolean = false
    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    var isShowingAd = false
    var isInterstitialShowing = false


    fun showAdMobBanner(activity: Activity, rLayout: RelativeLayout, bannerKey:String, facebookKey:String) {
        AdMobBanner.showAdMobBanner(activity, rLayout, adUnitId = bannerKey, facebookKey)

        Log.d("checkiyty", "showBannerAd: AdsHelper ")
    }



    fun loadInterstitialAd(activity: Activity){
//        InterstitialAdUtils.loadAdMobAd(activity)
    }

}


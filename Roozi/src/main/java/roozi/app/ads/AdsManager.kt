package roozi.app.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
import roozi.app.BuildConfig
import roozi.app.ads.AdmobManager
import roozi.app.ads.AdmobManager.Companion.loadOpenAd
import roozi.app.ads.FBManager
import roozi.app.ads.FBManager.Companion.eventName
import roozi.app.interfaces.EventListener
import roozi.app.interfaces.FbListener
import roozi.app.models.AdData
import roozi.app.models.EventName

class AdsManager {
    companion object {
        @JvmStatic
        var isInterShowing = false
        private var splashInflated = false
        var clickCounter = 0

        lateinit var eventListener: EventListener


        var adData = AdData()


        fun initFB(context: Context, _adData: AdData, initialize: (Boolean) -> Unit) {
            adData = _adData
            Log.d("HEHEHE", "showFacebookSplashNative: $adData")
            if (AudienceNetworkAds.isInitialized(context)) {
                FBManager.loadInterstitial1(context)
                FBManager.loadInterstitial2(context)
                FBManager.loadNativeAd1(context)
                FBManager.loadNativeAd2(context)
                initialize.invoke(true)
            } else if (!AudienceNetworkAds.isInitialized(context)) {
                AudienceNetworkAds.initialize(context)
                initialize.invoke(true)
                if (BuildConfig.DEBUG) {
                    AdSettings.setTestMode(true)
//                    AdSettings.addTestDevice("a316a4db-277b-9211-760694506aff")
                }
                FBManager.loadInterstitial1(context)
                FBManager.loadInterstitial2(context)
                FBManager.loadNativeAd1(context)
                FBManager.loadNativeAd2(context)
            } else {
                initialize.invoke(false)
            }
        }

        /**
         * call this method to initialize admob
         */
        fun initAdmob(context: Context, _adData: AdData, initialized: (Boolean) -> Unit) {
            MobileAds.initialize(
                context
            ) {
                adData = _adData

                initialized.invoke(true)
                AdmobManager.loadInterstitialAd1(context)
                AdmobManager.loadInterstitialAd2(context)
                AdmobManager.loadNativeAd1(context)
                AdmobManager.loadNativeAd2(context)
                loadOpenAd(context) {}
            }
        }


        /**
         * method to check each instance every time and fill the inventary if any instance is found null or empty
         */
        fun checkInstances(activity: Activity) {
            if (AdmobManager.nativeAd1 == null && !AdmobManager.nativeLoading1)
                AdmobManager.loadNativeAd1(activity)

            if (AdmobManager.nativeAd2 == null && !AdmobManager.nativeLoading2)
                AdmobManager.loadNativeAd2(activity)


            if (AdmobManager.interstitialAd1 == null && !AdmobManager.interstitialAdLoad1)
                AdmobManager.loadInterstitialAd1(activity)

            if (AdmobManager.interstitialAd2 == null && !AdmobManager.interstitialAdLoad2)
                AdmobManager.loadInterstitialAd2(activity)

        }

        /**
         * Call this method on splash for loading and inflating Admob ad
         */
        fun showAdmobSplashNative(
            activity: Activity,
            nativeContainer: FrameLayout
        ) {
            Log.d("HEHEHEHEHEH", "showAdmobSplashNative: $adData")
            AdmobManager.loadSplashNative(activity) {
                if (it && !splashInflated) {
                    splashInflated = true
                    AdmobManager.inflateNativeAdWithNoAds(
                        activity,
                        adData,
                        AdmobManager.splashNative!!,
                        nativeContainer
                    )
                }
            }
        }

        fun showFacebookSplashNative(activity: Activity, container: FrameLayout) {
            FBManager.loadSplashNative(activity) {
                Log.d("FBNATICECHECK", "call back:$it ")
                if (it && !splashInflated) {
                    Log.d("FBNATICECHECK", "showAdmobSplashNative:true ")
                    splashInflated = true
                    FBManager.inflateNativeAdWithOutNoAds(
                        activity, FBManager.splashNative!!, container
                    )
                }
            }

        }


        /**
         * Call this method when you want to null all the instances
         * ideal case is when user purchase in app
         */
        fun clearInstances() {
            splashInflated = false
            AdmobManager.nativeAd1 = null
            AdmobManager.nativeAd2 = null
            AdmobManager.interstitialAd1 = null
            AdmobManager.interstitialAd2 = null
            AdmobManager.splashNative = null

            FBManager.nativeAd1 = null
            FBManager.nativeAd2 = null
            FBManager.interstitialAd1 = null
            FBManager.interstitialAd2 = null
            FBManager.splashNative = null
        }

        fun showInterstitialWithClick(
            admobKey: Boolean,
            facebookKey: Boolean,
            admobEventKey: String,
            facebookEventKey: String,
            activity: Activity,
            adEvent: (Boolean) -> Unit
        ) {
            checkInstances(activity)
            clickCounter += 1
            if (clickCounter % adData.CappingCounter != 0) {
                adEvent.invoke(false)
            } else
                interstitial(
                    admobKey,
                    facebookKey,
                    admobEventKey,
                    facebookEventKey,
                    activity,
                    adEvent
                )
        }

        fun banner(
            admobKey: Boolean,
            facebookKey: Boolean,
            activity: Activity, frameLayout: FrameLayout
        ) {
            if (adData.isAdmobEnabled && admobKey)
                AdmobManager.loadBannerAd(activity, facebookKey, frameLayout)
            else if (adData.isFbEnabled && facebookKey)
                FBManager.loadBanner(activity, frameLayout)

            loadOpenAd(activity) {}
        }

        fun interstitial(
            admobKey: Boolean,
            facebookKey: Boolean,
            admobEventKey: String,
            facebookEventKey: String,
            activity: Activity,
            adEvent: (Boolean) -> Unit
        ) {
            eventName.admobScreenKey = admobEventKey
            eventName.facebookScreenKey = facebookEventKey

            if (adData.isAdmobEnabled && admobKey)
                AdmobManager.showInterstitialAd(
                    activity,
                    facebookKey,
                    adEvent
                )
            else if (adData.isFbEnabled && facebookKey)
                FBManager.showInterstitial(activity, object : FbListener {
                    override fun onDismiss() {
                        adEvent.invoke(true)
                        eventListener.onDismiss(eventName.facebookScreenKey)
                    }

                    override fun onClick() {
                        eventListener.onAdClick(eventName.facebookScreenKey)
                    }

                    override fun onShow() {
                        eventListener.onAdShow(eventName.facebookScreenKey)
                    }
                })
            else
                adEvent.invoke(true)

            loadOpenAd(activity) {

            }
        }

        fun nativee(
            admobKey: Boolean,
            facebookKey: Boolean,
            activity: Activity,
            frameLayout: FrameLayout,
            close: (Boolean) -> Unit = {}
        ) {
            if (adData.isAdmobEnabled && admobKey)
                AdmobManager.showNative(activity, facebookKey, frameLayout, close)
            else if (adData.isFbEnabled && facebookKey)
                FBManager.showNative(activity, frameLayout, close)
            else frameLayout.visibility = View.INVISIBLE
            loadOpenAd(activity) {}
        }

    }
}
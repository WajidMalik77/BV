package roozi.app.ads
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.facebook.ads.*
import roozi.app.ads.AdmobManager.Companion.dismissLoading
import roozi.app.ads.AdmobManager.Companion.showLoading
import roozi.app.ads.AdsHelper.Companion.isNetworkAvailable
import roozi.app.ads.AdsManager.Companion.adData
import roozi.app.billing.BillingManager
import roozi.app.databinding.LayoutNativeAdViewBinding
import roozi.app.interfaces.FbListener
import roozi.app.R
import roozi.app.ads.AdsManager.Companion.eventListener
import roozi.app.ads.AdsManager.Companion.isInterShowing
import roozi.app.models.EventName

class FBManager {
    companion object {
        const val TAG = "FBKey"
        private lateinit var adevent: FbListener
        var adView: AdView? = null
        var nativeAd1: NativeAd? = null
        var nativeAd2: NativeAd? = null
        var nativeLoading1 = false
        var nativeLoading2 = false
        var splashNative: NativeAd? = null
        var tempNative: NativeAd? = null
        var interstitialAd1: InterstitialAd? = null
        var interstitialAd2: InterstitialAd? = null
        var interstitialAdLoad1 = false
        var interstitialAdLoad2 = false
        var firstAdLoaded = false
        var secondAdLoaded = false
        var eventName = EventName()

        fun loadBanner(context: Context, container: FrameLayout) {
            if (adData.inApp)
                return
            else if (!isNetworkAvailable(context))
                return
            else if (!adData.isFbEnabled)
                return
            else {
                adView = AdView(context, adData.fbBannerId, AdSize.BANNER_HEIGHT_50)
                Log.d(TAG, "loadBanner: ${adData.fbBannerId}")
                val adListener = object : AdListener {
                    override fun onError(p0: Ad?, p1: AdError?) {
                        Log.d("MyKey", "onError: faceBook Banner ${p1!!.errorMessage}")
                    }

                    override fun onAdLoaded(p0: Ad?) {
                        Log.d(TAG, "onAdLoaded")
                        adView?.let {
                            it.parent?.let {
                                (it as ViewGroup).removeView(adView)
                                it.removeAllViews()
                                Log.d(TAG, "onAdLoaded: child")
                            }
                        }
                        container.addView(adView)
                        container.visibility = View.VISIBLE
                    }

                    override fun onAdClicked(p0: Ad?) {
                        Log.d(TAG, "onAdClicked: ")
                    }

                    override fun onLoggingImpression(p0: Ad?) {
                        Log.d(TAG, "onLoggingImpression:")
                    }
                }
                adView!!.loadAd(adView!!.buildLoadAdConfig().withAdListener(adListener).build())

            }
        }

        fun loadNativeAd1(context: Context) {
            if (adData.inApp)
                return
            else if (!isNetworkAvailable(context))
                return
            else if (!adData.isFbEnabled)
                return
            else {
                nativeLoading1 = true
                nativeAd1 = NativeAd(context, adData.fbNativeId)
                val nativeAdListener = object : NativeAdListener {
                    override fun onError(p0: Ad?, p1: AdError?) {
                        Log.d(TAG, "onError: native1")
                        nativeLoading1 = false
                        nativeAd1 = null
                    }

                    override fun onAdLoaded(p0: Ad?) {
                        if (nativeAd1 == null || nativeAd1 != p0) {
                            Log.d(TAG, "onAdLoaded: native Loaded")
                            return
                        }
                        nativeLoading1 = false
                    }

                    override fun onAdClicked(p0: Ad?) {
                    }

                    override fun onLoggingImpression(p0: Ad?) {
                        Log.d(TAG, "onLoggingImpression: facebook impression")
                        if (nativeAd1 != null)
                            nativeAd1 = null

                        loadNativeAd1(context)
                    }

                    override fun onMediaDownloaded(p0: Ad?) {
                    }
                }
                nativeAd1!!.loadAd(
                    nativeAd1!!.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build()
                )
            }
        }

        fun loadNativeAd2(context: Context) {
            if (adData.inApp)
                return
            else if (!isNetworkAvailable(context))
                return
            else if (!adData.isFbEnabled)
                return
            else {
                nativeLoading1 = true
                nativeAd2 = NativeAd(context, adData.fbNativeId)
                val nativeAdListener = object : NativeAdListener {
                    override fun onError(p0: Ad?, p1: AdError?) {
                        Log.d(TAG, "onError: native2")
                        nativeLoading1 = false
                    }

                    override fun onAdLoaded(p0: Ad?) {
                        if (nativeAd2 == null || nativeAd2 != p0) {
                            return
                        }
                        nativeLoading1 = false

                        Log.d(TAG, "onAdLoaded: native Loaded")
                    }

                    override fun onAdClicked(p0: Ad?) {
                    }

                    override fun onLoggingImpression(p0: Ad?) {
                        if (nativeAd2 != null)
                            nativeAd2 = null
                        loadNativeAd2(context)
                    }

                    override fun onMediaDownloaded(p0: Ad?) {
                    }
                }
                nativeAd2!!.loadAd(
                    nativeAd2!!.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build()
                )
            }
        }

        fun showFbNativeAd(context: Context, mNativeAd: NativeAd, container: FrameLayout, close:(Boolean)->Unit) {
            tempNative = mNativeAd
            mNativeAd.unregisterView()
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val adLayout = LayoutNativeAdViewBinding.inflate(inflater)

            // Inflate a layout and add it to the parent ViewGroup.
            val adView = LayoutInflater.from(context)
                .inflate(R.layout.fb_native_layout, null, false) as NativeAdLayout
            adLayout.noAds.setOnClickListener {
                BillingManager.showPremium(context,close)
            }

            // Add the AdOptionsView

            // Add the AdOptionsView

            // Create native UI using the ad metadata.


            // Create native UI using the ad metadata.
            val nativeAdIcon: MediaView = adView.findViewById(R.id.fb_native_ad_icon)
            val nativeAdTitle: TextView = adView.findViewById(R.id.fb_native_ad_title)
            val nativeAdMedia: MediaView = adView.findViewById(R.id.fb_native_ad_media)
            val nativeAdSocialContext: TextView =
                adView.findViewById(R.id.fb_native_ad_social_context)
            val nativeAdBody: TextView = adView.findViewById(R.id.fb_native_ad_body)
            val sponsoredLabel: TextView = adView.findViewById(R.id.fb_native_ad_sponsored_label)
            val nativeAdCallToAction: Button = adView.findViewById(R.id.fb_native_ad_call_to_action)


            // Set the Text.
            nativeAdTitle.text = mNativeAd.advertiserName
            nativeAdBody.text = mNativeAd.adBodyText
            nativeAdSocialContext.text = mNativeAd.adSocialContext
            nativeAdCallToAction.visibility =
                if (mNativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
            nativeAdCallToAction.text = mNativeAd.adCallToAction
            sponsoredLabel.text = mNativeAd.sponsoredTranslation

            // Create a list of clickable views

            // Create a list of clickable views
            val clickableViews: MutableList<View> = ArrayList()
//            clickableViews.add(nativeAdTitle)
            clickableViews.add(nativeAdCallToAction)
//            clickableViews.add(nativeAdIcon)

            // Register the Title and CTA button to listen for clicks.

            // Register the Title and CTA button to listen for clicks.
            mNativeAd.registerViewForInteraction(
                adView, nativeAdMedia, nativeAdIcon, clickableViews
            )

            adLayout.nativeAdContainer.removeAllViews()
            adLayout.nativeAdContainer.addView(adView)
            // Ensure that the parent view doesn't already contain an ad view.
            container.removeAllViews()
            // Place the AdView into the parent.
            container.addView(adLayout.root)
            container.visibility = View.VISIBLE
        }

        fun loadSplashNative(context: Context, adLoaded: (Boolean) -> Unit) {
            if (!adData.isFbEnabled)
                adLoaded.invoke(false)
            else if (!isNetworkAvailable(context))
                adLoaded.invoke(false)
            else if (adData.inApp)
                adLoaded.invoke(false)
            else {
                Log.d(TAG, "loadSplashNative: else")
                val nativeAd = NativeAd(context, adData.fbNativeId)
                val nativeAdListener = object : NativeAdListener {
                    override fun onError(p0: Ad?, p1: AdError?) {
                        Log.d(TAG, "onError: SplashNative ${p1!!.errorMessage}")
                        nativeLoading1 = false
                        adLoaded.invoke(false)
                    }

                    override fun onAdLoaded(p0: Ad?) {
                        Log.d(TAG, "loadSplashNative: native Loaded")
                        splashNative = nativeAd
                        nativeAd.downloadMedia()
                        adLoaded.invoke(true)
                    }

                    override fun onAdClicked(p0: Ad?) {
                    }

                    override fun onLoggingImpression(p0: Ad?) {
                        if (splashNative != null)
                            splashNative = null
                    }

                    override fun onMediaDownloaded(p0: Ad?) {

                    }
                }
                nativeAd.loadAd(
                    nativeAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build()
                )
            }
        }

        fun showNative(context: Context, container: FrameLayout, close: (Boolean) -> Unit) {
            nativeAd1?.let {
                if (it.isAdLoaded)
                    showFbNativeAd(context, it, container, close)
            } ?: nativeAd2?.let {
                if (it.isAdLoaded)
                    showFbNativeAd(context, it, container,close)

            } ?: splashNative?.let {
                if (it.isAdLoaded)
                    showFbNativeAd(context, it, container,close)

            } ?: Log.d(TAG, "showNative: no ad")
            if (!nativeLoading1)
                loadNativeAd1(context)
            if (!nativeLoading2)
                loadNativeAd2(context)
        }


        fun showNativeWithoutNoAds(activity: Activity, container: FrameLayout) {
            if (nativeAd1 != null)
                inflateNativeAdWithOutNoAds(
                    activity,
                    nativeAd1!!,
                    container
                )
            else if (nativeAd2 != null) {
                inflateNativeAdWithOutNoAds(
                    activity,
                    nativeAd2!!,
                    container
                )
                if (!nativeLoading1)
                    loadNativeAd1(activity)
            } else if (splashNative != null) {
                inflateNativeAdWithOutNoAds(
                    activity,
                    splashNative!!,
                    container
                )
                if (!nativeLoading1)
                    loadNativeAd1(activity)
                if (!nativeLoading2)
                    loadNativeAd2(activity)
            } else {
                if (!nativeLoading1)
                    loadNativeAd1(activity)
                if (!nativeLoading2)
                    loadNativeAd2(activity)
            }
        }

        fun inflateNativeAdWithOutNoAds(
            activity: Activity,
            mNativeAd: NativeAd,
            container: FrameLayout
        ) {
            tempNative = mNativeAd
            mNativeAd.unregisterView()

            // Inflate a layout and add it to the parent ViewGroup.
            val adView = LayoutInflater.from(activity)
                .inflate(R.layout.fb_native_layout, null, false) as NativeAdLayout


            // Add the AdOptionsView

            // Add the AdOptionsView

            // Create native UI using the ad metadata.


            // Create native UI using the ad metadata.
            val nativeAdIcon: MediaView = adView.findViewById(R.id.fb_native_ad_icon)
            val nativeAdTitle: TextView = adView.findViewById(R.id.fb_native_ad_title)
            val nativeAdMedia: MediaView = adView.findViewById(R.id.fb_native_ad_media)
            val nativeAdSocialContext: TextView =
                adView.findViewById(R.id.fb_native_ad_social_context)
            val nativeAdBody: TextView = adView.findViewById(R.id.fb_native_ad_body)
            val sponsoredLabel: TextView = adView.findViewById(R.id.fb_native_ad_sponsored_label)
            val nativeAdCallToAction: Button = adView.findViewById(R.id.fb_native_ad_call_to_action)


            // Set the Text.
            nativeAdTitle.text = mNativeAd.advertiserName
            nativeAdBody.text = mNativeAd.adBodyText
            nativeAdSocialContext.text = mNativeAd.adSocialContext
            nativeAdCallToAction.visibility =
                if (mNativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
            nativeAdCallToAction.text = mNativeAd.adCallToAction
            sponsoredLabel.text = mNativeAd.sponsoredTranslation

            // Create a list of clickable views

            // Create a list of clickable views
            val clickableViews: MutableList<View> = ArrayList()
//            clickableViews.add(nativeAdTitle)
            clickableViews.add(nativeAdCallToAction)
//            clickableViews.add(nativeAdIcon)

            // Register the Title and CTA button to listen for clicks.

            // Register the Title and CTA button to listen for clicks.
            mNativeAd.registerViewForInteraction(
                adView, nativeAdMedia, nativeAdIcon, clickableViews
            )


            // Ensure that the parent view doesn't already contain an ad view.
            container.removeAllViews()
            container.addView(adView)
            // Place the AdView into the parent.
            container.visibility = View.VISIBLE

        }

        fun loadInterstitial1(context: Context) {
            if (!adData.isFbEnabled)
                return
            else if (!isNetworkAvailable(context))
                return
            else if (adData.inApp)
                return
            else {
                interstitialAd1 = InterstitialAd(context, adData.fbInInterId)

                interstitialAdLoad1 = true
                val listener = object : InterstitialAdListener {
                    override fun onError(p0: Ad?, p1: AdError?) {
                        interstitialAd1 = null
                        interstitialAdLoad1 = false
                        Log.d(TAG, "onError: facebook inter 1 error ${p1?.errorMessage} ")
                    }

                    override fun onAdLoaded(p0: Ad?) {
                        interstitialAdLoad1 = false
                        Log.d(TAG, "onAdLoaded: facebook inter loaded")
                    }

                    override fun onAdClicked(p0: Ad?) {
                        eventListener.onAdClick(eventName.facebookScreenKey)
                    }

                    override fun onLoggingImpression(p0: Ad?) {
                        firstAdLoaded = true
                        isInterShowing = true
                        Log.d(TAG, "onLoggingImpression: facebook inter $isInterShowing")
                    }

                    override fun onInterstitialDisplayed(p0: Ad?) {
                        firstAdLoaded = true
                        isInterShowing = true
                        eventListener.onAdShow(eventName.facebookScreenKey)
                        Log.d(TAG, "onInterstitialDisplayed: facebook inter $isInterShowing")
                    }

                    override fun onInterstitialDismissed(p0: Ad?) {
                        interstitialAd1 = null
                        adevent.onDismiss()
                        loadInterstitial1(context)
                        isInterShowing = false
                        eventListener.onDismiss(eventName.facebookScreenKey)
                        Log.d(TAG, "onInterstitialDismissed: facebook inter $isInterShowing")
                    }

                }
                interstitialAd1!!.loadAd(
                    interstitialAd1!!.buildLoadAdConfig()
                        .withAdListener(listener)
                        .build()
                )
            }
        }

        fun loadInterstitial2(context: Context) {
            if (!adData.isFbEnabled)
                return
            else if (!isNetworkAvailable(context))
                return
            else if (adData.inApp)
                return
            else {
                interstitialAd2 = InterstitialAd(context, adData.fbInInterId)
                interstitialAdLoad2 = true
                val listener = object : InterstitialAdListener {
                    override fun onError(p0: Ad?, p1: AdError?) {
                        interstitialAd2 = null
                        interstitialAdLoad2 = false
                        Log.d(TAG, "onError: facebook inter 2 Fail call ")
                    }

                    override fun onAdLoaded(p0: Ad?) {
                        interstitialAdLoad2 = false
                        Log.d(TAG, "onAdLoaded: facebook inter loaded")
                    }

                    override fun onAdClicked(p0: Ad?) {
                    }

                    override fun onLoggingImpression(p0: Ad?) {
                        secondAdLoaded = true
                        isInterShowing = true
                        Log.d(TAG, "onLoggingImpression: facebook inter $isInterShowing")
                    }

                    override fun onInterstitialDisplayed(p0: Ad?) {
                        isInterShowing = true
                        Log.d(TAG, "onInterstitialDisplayed: facebook inter $isInterShowing")
                    }

                    override fun onInterstitialDismissed(p0: Ad?) {
                        interstitialAd2 = null
                        adevent.onDismiss()
                        loadInterstitial2(context)
                        isInterShowing = false
                        Log.d(TAG, "onInterstitialDismissed: facebook inter $isInterShowing")
                    }
                }
                interstitialAd2!!.loadAd(
                    interstitialAd2!!.buildLoadAdConfig()
                        .withAdListener(listener)
                        .build()
                )
            }

        }

        fun showInterstitial(context: Context, adEvent: FbListener) {
            if (!isNetworkAvailable(context)){
                adEvent.onDismiss()
                return
            }
            else if (adData.inApp){
                adEvent.onDismiss()
                return
            }
            else {
                adevent = adEvent
                interstitialAd1?.let {
                    if (it.isAdLoaded) {
                        showLoading(context)
                        Handler(Looper.getMainLooper()).postDelayed({
                            dismissLoading()
                            it.show()
                            firstAdLoaded = true
                        }, 1500)
                    }else
                        adEvent.onDismiss()
                } ?: interstitialAd2?.let {
                    if (it.isAdLoaded) {
                        showLoading(context)
                        Handler(Looper.getMainLooper()).postDelayed({
                            dismissLoading()
                            it.show()
                            secondAdLoaded = true
                        }, 1500)
                    }else
                        adEvent.onDismiss()
                } ?: adEvent.onDismiss()
                if (!interstitialAdLoad1)
                    loadInterstitial1(context)

                if (!interstitialAdLoad2)
                    loadInterstitial2(context)
            }

        }


        fun showInterstitialSplash(context: Context, adEvent: FbListener) {
            if (!isNetworkAvailable(context))
                return
            else if (adData.inApp)
                return
            else {
                adevent = adEvent
                interstitialAd1?.let {
                    if (it.isAdLoaded) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            dismissLoading()
                            it.show()
                            firstAdLoaded = true
                        }, 1500)
                    }
                } ?: interstitialAd2?.let {
                    if (it.isAdLoaded) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            dismissLoading()
                            it.show()
                            secondAdLoaded = true
                        }, 1500)
                    }
                } ?: adEvent.onDismiss()

                if (!interstitialAdLoad1)
                    loadInterstitial1(context)

                if (!interstitialAdLoad2)
                    loadInterstitial2(context)
            }

        }
    }
}

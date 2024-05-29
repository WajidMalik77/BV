package roozi.app.ads

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import roozi.app.ads.AdsHelper.Companion.getColorValue
import roozi.app.ads.AdsHelper.Companion.isNetworkAvailable
import roozi.app.ads.AdsManager.Companion.adData
import roozi.app.ads.FBManager.Companion.TAG
import roozi.app.billing.BillingManager
import roozi.app.databinding.LayoutAdLoadingBinding
import roozi.app.databinding.LayoutNativeAdViewBinding
import roozi.app.interfaces.FbListener
import roozi.app.models.AdData
import roozi.app.R
import roozi.app.ads.AdsManager.Companion.eventListener
import roozi.app.ads.AdsManager.Companion.isInterShowing
import roozi.app.ads.FBManager.Companion.eventName
import roozi.app.databinding.OpenAdBgBinding
import java.lang.Exception


class AdmobManager {
    companion object {

        private var isOpenShow = false

        //Native add
        var tempNative: NativeAd? = null
        var splashNative: NativeAd? = null
        var nativeAd1: NativeAd? = null
        var nativeAd2: NativeAd? = null
        var nativeLoading1 = false
        var nativeLoading2 = false
        var myOpenAd: AppOpenAd? = null

        fun loadOpenAd(context: Context, adLoaded: (Boolean) -> Unit) {
            if (!isNetworkAvailable(context)) adLoaded.invoke(false)
            else if (!adData.isOpenAdEnabled) adLoaded.invoke(false)
            else if (myOpenAd != null) {
                Log.d(TAG, "loadOpenAd: no need")
                adLoaded.invoke(false)
                return
            } else {
                val request = AdRequest.Builder().build()
                AppOpenAd.load(
                    context,
                    adData.OpenAdId,
                    request,
                    object : AppOpenAd.AppOpenAdLoadCallback() {
                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            adLoaded.invoke(false)
                            Log.d(TAG, "onAdFailedToLoad: open ad fail to load $p0")
                        }

                        override fun onAdLoaded(ad: AppOpenAd) {
                            myOpenAd = ad
                            adLoaded.invoke(true)
                            Log.d(TAG, "onAdLoaded: open ad loaded success")
                        }
                    })
            }

        }

        fun loadAndShowOpenAd(activity: Activity, iD: String, shouldShowAd: Boolean) {

            if (!isNetworkAvailable(activity)) {
                Log.d(TAG, "loadAndShowOpenAd: no internet")
                return
            } else if (!adData.isOpenAdEnabled) {
                Log.d(TAG, "loadAndShowOpenAd: open ad off ha")
                return
            } else if (adData.inApp) {
                Log.d(TAG, "loadAndShowOpenAd: pro user")
                return
            } else if (isInterShowing) {
                Log.d(TAG, "showOpenAd: another ad is showing")
                return
            } else if (myOpenAd != null) {
                Log.d(TAG, "loadOpenAd: no need to load opoen ad")
                return
            } else if (shouldShowAd) {
                Log.d(TAG, "shouldShowAd is true to ni show hoga ")
                return
            }
//            else if (adData.OpenAdId.isEmpty() || adData.OpenAdId.isBlank()) {
//                Log.d(TAG, "loadAndShowOpenAd: id null ha")
//                return
//            }
            else {

                // Check if the app open ad switch is on

                showLoading(activity)
                val request = AdRequest.Builder().build()
                AppOpenAd.load(
                    activity,
//                    adData.OpenAdId,
                    iD,//orignal code
//                    "ca-app-pub-3940256099942544/9257395921",
//                    prefs.getAppopenAdId(),
                    request,
                    object : AppOpenAd.AppOpenAdLoadCallback() {
                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            dismissLoading()
                            Log.d(TAG, "onAdFailedToLoad: open ad fail to load $p0")
                        }

                        override fun onAdLoaded(ad: AppOpenAd) {
                            myOpenAd = ad
                            dismissLoading()
                            showOpenAd(activity)
                            Log.d(TAG, "onAdLoaded: open ad loaded success")
                        }
                    })
            }

        }

        fun showOpenAd(activity: Activity) {
            if (!adData.isOpenAdEnabled) {
                Log.d(TAG, "showOpenAd: open ad off")
                return
            } else if (isInterShowing) {
                Log.d(TAG, "showOpenAd: another ad is showing")
                return
            } else {
                myOpenAd?.let { ad ->
                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent()
                            showAdBg(activity)
                            isOpenShow = true
                        }

                        override fun onAdImpression() {
                            super.onAdImpression()
                            isOpenShow = true
                        }

                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            myOpenAd = null
                            dismissAdBgDialog()
                            isOpenShow = false
                        }

                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            super.onAdFailedToShowFullScreenContent(p0)
                            if (!isOpenShow)
                                showOpenAd(activity)
                            Log.d(TAG, "onAdFailedToShowFullScreenContent: ")
                        }
                    }
                    ad.show(activity)
                } ?: Log.d(TAG, "onAdLoaded:open ad null ha")
            }
        }


//        fun showOpenAd(activity: Activity, isShow: (Boolean) -> Unit) {
//            if (!isInterShowing)
//                if (adData.isOpenAdEnabled)
//                    myOpenAd?.let { ad ->
//                        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
//
//                            override fun onAdShowedFullScreenContent() {
//                                super.onAdShowedFullScreenContent()
//                                showAdBg(activity)
//                                AdsManager.clickCounter = 0
//                            }
//
//                            override fun onAdDismissedFullScreenContent() {
//                                super.onAdDismissedFullScreenContent()
//                                myOpenAd = null
//                                dismissAdBgDialog()
//                                loadOpenAd(activity) {}
//                                isShow.invoke(true)
//                            }
//
//                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
//                                super.onAdFailedToShowFullScreenContent(p0)
//                                myOpenAd = null
//                                dismissAdBgDialog()
//                                loadOpenAd(activity) {}
//                                isShow.invoke(false)
//                            }
//
//                        }
//                        ad.show(activity)
//                    } ?: Log.d(TAG, "onAdLoaded:open ad null ha")
//                else Log.d(TAG, "showOpenAd: open app off ha ")
//            else Log.d(TAG, "showOpenAd: open disabled")
//        }

        fun loadSplashNative(context: Context, adLoaded: (Boolean) -> Unit) {
            Log.d("CHECKADMOB", "loadSplashNative: ${adData.isAdmobEnabled}")
            if (!adData.isAdmobEnabled) adLoaded.invoke(false)
            else if (adData.isOn.lowercase() == "off") adLoaded.invoke(false)
            else if (!isNetworkAvailable(context)) {
                adLoaded.invoke(false)
            } else if (adData.inApp) adLoaded.invoke(false)
            else {
                val adLoader: AdLoader = AdLoader.Builder(
                    context, adData.AdmobNativeId
                ).forNativeAd { nativeAd ->
                    Log.i("MyKey", "loadNativeAd1: calling ad")
                    splashNative = nativeAd
                }.withAdListener(object : AdListener() {

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        Log.i("MyKey", "loadNativeAd1: Ad loaded")
                        adLoaded.invoke(true)
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.i("MyKey", "loadNativeAd1: ${adError.code}")
                        adLoaded.invoke(false)
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        if (splashNative != null) splashNative = null

                        Log.i("MyKey", "loadNativeAd1: calling impression")
                    }
                }).build()
                adLoader.loadAd(AdRequest.Builder().build())
            }
        }

        fun loadNativeAd1(context: Context) {
            if (!adData.isAdmobEnabled) return
            else if (adData.isOn.lowercase() == "off") return
            else if (!isNetworkAvailable(context)) {
                return
            } else if (adData.inApp) return
            else {
                nativeLoading1 = true


                val adLoader: AdLoader = AdLoader.Builder(
                    context, adData.AdmobNativeId
                ).forNativeAd { nativeAd ->
                    Log.i("MyKey", "loadNativeAd1: calling ad")
                    nativeAd1 = nativeAd
                }.withAdListener(object : AdListener() {

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        Log.i("MyKey", "loadNativeAd1: Ad loaded")
                        nativeLoading1 = false
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.i("MyKey", "loadNativeAd1: Admob Fail ${adError}")
                        nativeLoading1 = false
                        FBManager.loadNativeAd1(context)
//                                ALManager.loadNativeAd1(context as Activity)
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        if (nativeAd1 != null) nativeAd1 = null

                        Log.i("MyKey", "loadNativeAd1: calling impression")
                        loadNativeAd1(context)
                    }


                }).build()
                adLoader.loadAd(AdRequest.Builder().build())
            }
        }

        fun loadNativeAd2(context: Context) {
            if (!adData.isAdmobEnabled) return
            else if (adData.isOn.lowercase() == "off") return
            else if (!isNetworkAvailable(context)) return
            else if (adData.inApp) return
            else {

                nativeLoading2 = true
                val adLoader: AdLoader = AdLoader.Builder(
                    context, adData.AdmobNativeId
                ).forNativeAd { nativeAd ->

                    nativeAd2 = nativeAd
                }.withAdListener(object : AdListener() {


                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        nativeLoading2 = false
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        nativeLoading2 = false
                        Log.d(TAG, "onAdFailedToLoad: native2 fail $adError")
//                                ALManager.loadNativeAd2(context as Activity)
                        FBManager.loadNativeAd2(context)
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        if (nativeAd2 != null) nativeAd2 = null

                        loadNativeAd2(context)
                    }


                }).build()


                adLoader.loadAd(AdRequest.Builder().build())
            }
        }

        fun showNative(
            context: Context,
            facebookKey: Boolean,
            container: FrameLayout,
            close: (Boolean) -> Unit
        ) {
            nativeAd1?.let {
                showAdmobNativeAd(context, it, container, close)
            } ?: nativeAd2?.let {
                showAdmobNativeAd(context, it, container, close)
            } ?: splashNative?.let {
                showAdmobNativeAd(context, it, container, close)
            } ?: if (adData.isFbEnabled && facebookKey)
                FBManager.showNative(context, container, close)
            else Log.d(TAG, "showNative: no ad")
            if (!nativeLoading1) loadNativeAd1(context)
            if (!nativeLoading2) loadNativeAd2(context)
        }

        fun showRVNative(
            context: Context,
            container: FrameLayout,
            adLoaded: (Boolean) -> Unit,
            close: (Boolean) -> Unit
        ) {
            if (nativeAd1 != null) {
                adLoaded.invoke(true)
                showAdmobNativeAd(context, nativeAd1!!, container, close)
            } else if (nativeAd2 != null) {
                adLoaded.invoke(true)
                showAdmobNativeAd(context, nativeAd2!!, container, close)

                if (!nativeLoading1) loadNativeAd1(context)
            } else {
                adLoaded.invoke(false)
                if (!nativeLoading1) loadNativeAd1(context)
                if (!nativeLoading2) loadNativeAd2(context)
            }
        }

        /**
         * call this method if you don't want to include no ads button in native ad container
         */

        fun showNativeWithoutNoAds(context: Context, container: FrameLayout) {
            if (nativeAd1 != null) inflateNativeAdWithNoAds(context, adData, nativeAd1!!, container)
            else if (nativeAd2 != null) {
                inflateNativeAdWithNoAds(context, adData, nativeAd2!!, container)
                if (!nativeLoading1) loadNativeAd1(context)
            } else if (splashNative != null) {
                inflateNativeAdWithNoAds(context, adData, splashNative!!, container)
                if (!nativeLoading1) loadNativeAd1(context)
                if (!nativeLoading2) loadNativeAd2(context)
            } else {
                if (!nativeLoading1) loadNativeAd1(context)
                if (!nativeLoading2) loadNativeAd2(context)
            }
        }

        @SuppressLint("InflateParams")
        fun inflateNativeAdWithNoAds(
            context: Context, adData: AdData, mNativeAd: NativeAd, container: FrameLayout
        ) {
            tempNative = mNativeAd


            container.visibility = View.VISIBLE


            val adView = LayoutInflater.from(context)
//                .inflate(R.layout.layout_admob_native, null) as NativeAdView
                .inflate(R.layout.layout_admob_native_new, null) as NativeAdView


            // Inflate a layout and add it to the parent ViewGroup.
//            val adView = cardView.findViewById<NativeAdView>(R.id.ad_view)

            // Locate the view that will hold the headline, set its text, and use the
            // NativeAdView's headlineView property to register it.
            val headlineView = adView.findViewById<TextView>(R.id.my_ad_headline)
            headlineView.text = mNativeAd.headline
            adView.headlineView = headlineView

            val adBody = adView.findViewById<TextView>(R.id.my_ad_body)
            adBody.text = mNativeAd.body
            adView.bodyView = adBody


            val mediaView = adView.findViewById<MediaView>(R.id.my_ad_media)
            adView.mediaView = mediaView

            val iconView = adView.findViewById<ImageView>(R.id.my_ad_icon)
            if (mNativeAd.icon != null) {
                iconView.setImageDrawable(mNativeAd.icon!!.drawable)
                adView.iconView = iconView
            }


            val callToAction = adView.findViewById<TextView>(R.id.my_ad_call_to_action_button)
            callToAction.backgroundTintList = ColorStateList.valueOf(
                Color.parseColor(
                    getColorValue(adData.AdmobCtaColor, "#000000")
                )
            )
            callToAction.setTextColor(
                Color.parseColor(
                    getColorValue(AdsManager.adData.AdmobCtaTextColor, "#ffffff")
                )
            )
            adView.callToActionView = callToAction


            // Call the NativeAdView's setNativeAd method to register the
            // NativeAdObject.
            adView.setNativeAd(mNativeAd)

            // Ensure that the parent view doesn't already contain an ad view.
            container.removeAllViews()

            // Place the AdView into the parent.
            container.addView(adView)
            container.visibility = View.VISIBLE


        }

        @SuppressLint("InflateParams")
        private fun showAdmobNativeAd(
            context: Context, mNativeAd: NativeAd, container: FrameLayout, close: (Boolean) -> Unit
        ) {
            tempNative = mNativeAd
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val adLayout = LayoutNativeAdViewBinding.inflate(inflater)


            // Inflate a layout and add it to the parent ViewGroup.
            val adView = LayoutInflater.from(context)
//                .inflate(R.layout.layout_admob_native, null) as NativeAdView
                .inflate(R.layout.layout_admob_native_new, null) as NativeAdView

            adLayout.noAds.setOnClickListener {
                BillingManager.showPremium(context, close)
            }


            // Locate the view that will hold the headline, set its text, and use the
            // NativeAdView's headlineView property to register it.
            val headlineView = adView.findViewById<TextView>(R.id.my_ad_headline)
            headlineView.text = mNativeAd.headline
            adView.headlineView = headlineView

            val adBody = adView.findViewById<TextView>(R.id.my_ad_body)
            adBody.text = mNativeAd.body
            adView.bodyView = adBody


            val mediaView = adView.findViewById<MediaView>(R.id.my_ad_media)
            adView.mediaView = mediaView

            val iconView = adView.findViewById<ImageView>(R.id.my_ad_icon)
            if (mNativeAd.icon != null) {
                iconView.setImageDrawable(mNativeAd.icon!!.drawable)
                adView.iconView = iconView
            }


            val callToAction = adView.findViewById<TextView>(R.id.my_ad_call_to_action_button)
            callToAction.backgroundTintList = ColorStateList.valueOf(
                Color.parseColor(
                    getColorValue(adData.AdmobCtaColor, "#000000")
                )
            )
            callToAction.setTextColor(
                Color.parseColor(
                    getColorValue(adData.AdmobCtaTextColor, "#ffffff")
                )
            )
            adView.callToActionView = callToAction


            // Call the NativeAdView's setNativeAd method to register the
            // NativeAdObject.
            adView.setNativeAd(mNativeAd)

            adLayout.nativeAdContainer.removeAllViews()
            adLayout.nativeAdContainer.addView(adView)

            // Ensure that the parent view doesn't already contain an ad view.
            container.removeAllViews()

            // Place the AdView into the parent.
            container.addView(adLayout.root)
            container.visibility = View.VISIBLE

        }


        /**
         * Banner Ad
         */

        fun loadBannerAd(context: Context, facebookKey: Boolean, container: FrameLayout) {
            if (adData.isOn.lowercase() == "off") return
            else if (!isNetworkAvailable(context)) {
                return
            } else if (adData.inApp) {
                return
            } else {
                val adView = AdView(context)
                adView.adUnitId = adData.AdmobBannerId
                adView.setAdSize(AdSize.BANNER)

                adView.adListener = object : AdListener() {
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        Log.d(TAG, "onAdFailedToLoad: Admob Fail")
                        if (adData.isFbEnabled && facebookKey) FBManager.loadBanner(
                            context, container
                        )
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        adView.let {
                            it.parent?.let {
                                (it as ViewGroup).removeView(adView)
                                it.removeAllViews()
                                Log.d(TAG, "onAdLoaded: child")
                            }
                        }
                        container.addView(adView)
                        container.visibility = View.VISIBLE
                    }

                }

                // Create an ad request. Check your logcat output for the hashed device ID to
                // get test ads on a physical device, e.g.,
                // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this device."
                val adRequest = AdRequest.Builder().build()
                // Start loading the ad in the background.
                adView.loadAd(adRequest)
            }
        }


        /**
         * Interstitial Ad
         */
        var interstitialAd1: InterstitialAd? = null
        var interstitialAd2: InterstitialAd? = null
        var interstitialAdLoad1 = false
        var interstitialAdLoad2 = false

        //using these two variable to later check that which instance got the impression
        //and according to that we will load the instance again
        var firstAdLoaded = false
        var secondAdLoaded = false


        fun loadInterstitialAd1(context: Context) {
            if (adData.isOn.lowercase() == "off") return
            else if (!isNetworkAvailable(context)) return
            else if (adData.inApp) return
            else if (!adData.isAdmobEnabled) return
            else {
                val adRequest = AdRequest.Builder().build()
                interstitialAdLoad1 = true
                InterstitialAd.load(context,
                    adData.AdmobInterstitialId,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            interstitialAd1 = null
                            interstitialAdLoad1 = false
                            Log.d(TAG, "onAdFailedToLoad: Admob inter 1 Fail")
                            FBManager.loadInterstitial1(context)
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            interstitialAdLoad1 = false
                            interstitialAd1 = interstitialAd
                        }
                    })

            }
        }

        fun loadInterstitialAd2(context: Context) {
            if (adData.isOn.lowercase() == "off") return
            else if (!isNetworkAvailable(context)) return
            else if (adData.inApp) return
            else if (!adData.isAdmobEnabled) return
            else {
                val adRequest = AdRequest.Builder().build()
                interstitialAdLoad2 = true
                InterstitialAd.load(context,
                    adData.AdmobInterstitialId,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.d(TAG, "onAdFailedToLoad: Admob inter Fail 2")
                            interstitialAd2 = null
                            interstitialAdLoad2 = false
//                            ALManager.loadInterstitialAd2(context as Activity)
                            FBManager.loadInterstitial2(context)
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            interstitialAdLoad2 = false
                            interstitialAd2 = interstitialAd
                        }
                    })
            }

        }


        private var alertDialog: AlertDialog? = null
        private var dialog: Dialog? = null

        fun showAdBg(context: Context) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val adBgBinding = OpenAdBgBinding.inflate(layoutInflater)
            dialog = Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
//            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(adBgBinding.root)
            dialog?.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog?.show()

        }

        fun dismissAdBgDialog() {
            dialog?.let {
                if (it.isShowing)
                    it.dismiss()
            }
        }

        fun showLoading(context: Context) {
            Log.d("HuHu", "showLoading:")
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val adLoadingBinding = LayoutAdLoadingBinding.inflate(layoutInflater)

            Glide.with(context).load(R.drawable.loading_ad).into(adLoadingBinding.loading)

            alertDialog =
                AlertDialog.Builder(context).setView(adLoadingBinding.root).setCancelable(false)
                    .create()

            alertDialog?.let {
                if (!it.isShowing) {
                    it.show()
                }
            }
            alertDialog?.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
        }

        fun dismissLoading() {
            try {
                alertDialog?.let {
                    if (it.isShowing)
                        it.dismiss()
                }
            } catch (e: IllegalArgumentException) {
            }
        }

        fun showInterstitialAdSplash(
            activity: Activity, adEvent: (Boolean) -> Unit
        ) {
            if (!isNetworkAvailable(activity)) return
            else if (adData.inApp) return
            else {
                interstitialAd1?.let {
                    Handler(Looper.getMainLooper()).postDelayed({
                        dismissLoading()
                        it.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                interstitialAd1 = null
                                adEvent.invoke(true)
                                loadInterstitialAd1(activity)
                            }
                        }
                        it.show(activity)
                        firstAdLoaded = true
                    }, 1500)
                } ?: interstitialAd2?.let {
                    Handler(Looper.getMainLooper()).postDelayed({
                        dismissLoading()
                        it.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                interstitialAd2 = null
                                adEvent.invoke(true)
                                loadInterstitialAd2(activity)
                            }
                        }
                        it.show(activity)
                        secondAdLoaded = true
                    }, 1500)
                    if (!interstitialAdLoad1) loadInterstitialAd1(activity)
                } ?: if (adData.isFbEnabled) FBManager.showInterstitialSplash(activity,
                    object : FbListener {
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
//                ALManager.showInterstitialWithLoading(activity, adEvent)
                else adEvent.invoke(true)
                if (!interstitialAdLoad1) loadInterstitialAd1(activity)

                if (!interstitialAdLoad2) loadInterstitialAd2(activity)
            }
        }

        fun showInterstitialAd(
            activity: Activity,
            facebookKey: Boolean,
            adEvent: (Boolean) -> Unit
        ) {
            if (!isNetworkAvailable(activity)) {
                adEvent.invoke(false)
                return
            } else if (adData.inApp) {
                adEvent.invoke(false)
                return
            } else {
                interstitialAd1?.let {
                    showLoading(activity)
                    Handler(Looper.getMainLooper()).postDelayed({
                        dismissLoading()
                        it.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                interstitialAd1 = null
                                adEvent.invoke(true)
                                isInterShowing = false
                                loadInterstitialAd1(activity)
                                eventListener.onDismiss(eventName.admobScreenKey)

                            }

                            override fun onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent()
                                isInterShowing = true
                                eventListener.onAdShow(eventName.admobScreenKey)
                            }

                            override fun onAdClicked() {
                                super.onAdClicked()
                                eventListener.onAdClick(eventName.admobScreenKey)
                            }

                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                super.onAdFailedToShowFullScreenContent(p0)
                                interstitialAd1 = null
                                adEvent.invoke(true)
                                isInterShowing = false
                                loadInterstitialAd1(activity)
                            }
                        }
                        it.show(activity)
                        firstAdLoaded = true
                    }, 1500)
                } ?: interstitialAd2?.let {
                    showLoading(activity)
                    Handler(Looper.getMainLooper()).postDelayed({
                        dismissLoading()
                        it.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                interstitialAd2 = null
                                adEvent.invoke(true)
                                isInterShowing = false
                                loadInterstitialAd2(activity)
                                eventListener.onDismiss(eventName.admobScreenKey)
                            }

                            override fun onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent()
                                isInterShowing = true
                                eventListener.onAdShow(eventName.admobScreenKey)
                            }

                            override fun onAdClicked() {
                                super.onAdClicked()
                                eventListener.onAdClick(eventName.admobScreenKey)
                            }

                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                super.onAdFailedToShowFullScreenContent(p0)
                                interstitialAd2 = null
                                adEvent.invoke(true)
                                isInterShowing = false
                                loadInterstitialAd2(activity)
                            }
                        }
                        it.show(activity)
                        secondAdLoaded = true
                    }, 1500)
                    if (!interstitialAdLoad1) loadInterstitialAd1(activity)
                } ?: if (adData.isFbEnabled && facebookKey) FBManager.showInterstitial(activity,
                    object : FbListener {
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
                else adEvent.invoke(false)

                if (!interstitialAdLoad1)
                    loadInterstitialAd1(activity)

                if (!interstitialAdLoad2)
                    loadInterstitialAd2(activity)
            }
        }

        /**
         * an extra method that returns callback when ad is shown
         */
        fun showIntersAd(
            activity: Activity, adEvent: (Boolean) -> Unit
        ) {
            if (interstitialAd1 != null) {
                showLoading(activity)

                Handler(Looper.getMainLooper()).postDelayed({

                    dismissLoading()
                    interstitialAd1?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                interstitialAd1 = null
                                loadInterstitialAd1(activity)
                            }

                            override fun onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent()
                                adEvent.invoke(true)
                            }

                        }
                    interstitialAd1?.show(activity)

                    firstAdLoaded = true
                }, 1500)

            } else if (interstitialAd2 != null) {
                showLoading(activity)

                Handler(Looper.getMainLooper()).postDelayed({
                    dismissLoading()

                    interstitialAd2?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                interstitialAd2 = null

                                loadInterstitialAd2(activity)
                            }

                            override fun onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent()
                                adEvent.invoke(true)
                            }
                        }
                    interstitialAd2?.show(activity)


                    secondAdLoaded = true
                }, 1500)

                if (!interstitialAdLoad1) loadInterstitialAd1(activity)

            } else {

                adEvent.invoke(false)
                if (!interstitialAdLoad1) loadInterstitialAd1(activity)


                if (!interstitialAdLoad2) loadInterstitialAd2(activity)

            }
        }

    }
}

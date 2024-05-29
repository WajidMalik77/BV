package com.background.video.recorder.camera.recorder.ui.activitiy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.background.video.recorder.camera.recorder.R
import com.background.video.recorder.camera.recorder.adapters.LanguageAdapter
import com.background.video.recorder.camera.recorder.ads.AdMobBanner
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils.loadMediationNative
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils.loadNativeFacebookAd
import com.background.video.recorder.camera.recorder.ads.InterstitialClassActivity
import com.background.video.recorder.camera.recorder.databinding.ActivityLanguageBinding
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper
import com.background.video.recorder.camera.recorder.model.LanguageModel
import com.background.video.recorder.camera.recorder.util.FirebaseEvents
import com.background.video.recorder.camera.recorder.util.SharePref
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys
import com.background.video.recorder.camera.recorder.viewmodel.LanguageViewModel
import roozi.app.ads.AdsManager
import roozi.app.interfaces.EventListener

class LanguageActivity : AppCompatActivity(), LanguageAdapter.OnItemClickListener, EventListener {
    val binding by lazy {
        ActivityLanguageBinding.inflate(layoutInflater)
    }
    private val adapter by lazy {
        LanguageAdapter(modelArrayList, this@LanguageActivity)
    }
    private lateinit var checkActivity: String
    private lateinit var modelArrayList: ArrayList<LanguageModel>
    private lateinit var languageCheck: LanguageModel
    private val viewModel: LanguageViewModel by lazy { ViewModelProvider(this)[LanguageViewModel::class.java] }
    var getlangcode: String? = null
    private var language_admob_native = true
    private var language_interstitial = true
    private var language_facebook_native = true
    private var facebook_banner_enable = true
    private var admob_banner_enable = true
    private var facebook_native_ad_id = ""
    private var language_admob_native_id = ""
    private var language_interstitial_admob_id = ""
    private var prefs: SharedPrefsHelper? = null

    private var admob_banner_id = ""
    private lateinit var intent: Intent
    private var facebook_banner_ad_id = ""
    private var isSplashFirstTime = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        AdsManager.eventListener = this



        intent = Intent(this, HomeActivity::class.java)

        val intent = getIntent()
        isSplashFirstTime =
            intent.getBooleanExtra("isSplashFirstTime", false) // Default to false if not found


        prefs = SharedPrefsHelper.getInstance(this)
        val localPrefs = prefs
        if (localPrefs != null) {
            language_admob_native = localPrefs.getlanguage_admob_nativeSwitch()
//            language_admob_native = true
            language_interstitial = localPrefs.getlanguage_interstitialSwitch()
//            language_interstitial = false
            language_facebook_native = localPrefs.getlanguage_native_facebookSwitch()
            facebook_banner_enable = localPrefs.getfacebook_banner_enableSwitch()
            admob_banner_enable = localPrefs.getadmob_banner_enableSwitch()
            language_admob_native_id = localPrefs.getlanguage_admob_native_idId()
            admob_banner_id = localPrefs.getadmob_banner_idId()
            facebook_banner_ad_id = localPrefs.getfacebook_banner_ad_idId()
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId()
            language_interstitial_admob_id = localPrefs.getlanguage_interstitial_admob_idId()
            Log.d(
                "TAG",
                "onCreate:  admob_interstitial_splash_id  $language_facebook_native"
            )
            Log.d(
                "",
                "onCreate:  facebook_interstitial_splash_id  $language_facebook_native"
            )
        }

//        if (language_interstitial) {
//
//                InterstitialAdUtils.loadLanguageInterstitial(
//                    this,
//                    language_interstitial_admob_id
//                )
//
//        }


        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.nativeAd.visibility = View.GONE
        } else if (language_admob_native) {
            loadMediationNative(
                this,
                binding.nativeAd,
                language_admob_native_id,
                binding.nativeAdContainer,
                R.layout.ad_native_layout_language
            )
        } else if (language_facebook_native) {
            loadNativeFacebookAd(
                facebook_native_ad_id,
                this,
                binding.nativeAdContainer
            )

        } else {
            Log.d("checksswitcehs", "onViewCreated: both are off")
            binding.nativeAd.visibility = View.GONE
        }

        if (admob_banner_enable) {
            Log.e("bannercheckit", "Banner: admob_banner_enable loaded ")
            AdMobBanner.loadAd(this, binding.bannerAd, admob_banner_id, facebook_banner_ad_id)
        } else if (facebook_banner_enable) {
            Log.e("bannercheckit", "Banner: facebook_banner_enable loaded ")
            AdMobBanner.loadAdFifty(binding.bannerAd, this, facebook_banner_ad_id)
        } else {
            binding.bannerAd.visibility = View.GONE
        }

        initViews()
    }

    private fun initViews() {
        binding.languagesRvId.setHasFixedSize(true)
        binding.languagesRvId.layoutManager = LinearLayoutManager(this@LanguageActivity)
        viewModel.langList!!.observe(this) { languageModels ->
            modelArrayList = languageModels
            for (i in modelArrayList.indices) {
                getlangcode = SharePref.getSelectedLanguage()
                if ((modelArrayList[i].code == getlangcode)) modelArrayList[i].check = true
                binding.languagesRvId.adapter = adapter

            }
            adapter.setListener(this)
        }

        binding.back.setOnClickListener {
            onBackPressed()
        }
        binding.continueBtn.setOnClickListener {


            if (language_interstitial) {
                InterstitialClassActivity.requestInterstitial(
                    this, language_interstitial_admob_id
                ) {
                    moveToNext()
                }
            } else {

                moveToNext()


            }
//            startActivity(intent)
        }

    }

    fun moveToNext() {
        if (this::languageCheck.isInitialized) {
            SharePref.putString(
                SharePref.Locale_KeyValue,
                languageCheck.code
            )
            SharePref.putString(
                SharePref.countryCodeKey,
                languageCheck.countryCode
            )
            val locales = LocaleListCompat.forLanguageTags(SharePref.getSelectedLanguage()!!)
            AppCompatDelegate.setApplicationLocales(locales)
        } else {

            if (isSplashFirstTime) {
                startActivity(intent)
                finish()
            } else {
                onBackPressed()
                finish()
            }

        }
    }

    override fun onClick(view: View?, position: Int) {
        modelArrayList[adapter.sel].check = false
        adapter.notifyItemChanged(adapter.sel)
        Log.d("langTAG", "onClick: ${modelArrayList[position].code}")
        Log.d("langTAG", "onClick: ${modelArrayList[position].countryCode}")
        modelArrayList[position].check = true
        languageCheck = modelArrayList[position]
        adapter.notifyItemChanged(position)
        adapter.sel = position
    }

    override fun onDismiss(key: String) {
        FirebaseEvents.logAnalytic("${key}_Dismiss")
        Log.d("EventLogsss", "${key}_Dismiss")
    }

    override fun onAdShow(key: String) {
        FirebaseEvents.logAnalytic("${key}_Show")
        Log.d("EventLogsss", "${key}_Show")

    }

    override fun onAdClick(key: String) {
        FirebaseEvents.logAnalytic("${key}_Click")
        Log.d("EventLogsss", "${key}_Click")
    }
}
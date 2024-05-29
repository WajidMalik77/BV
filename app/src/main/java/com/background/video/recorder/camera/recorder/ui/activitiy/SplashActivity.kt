package com.background.video.recorder.camera.recorder.ui.activitiy

import com.background.video.recorder.camera.recorder.GoogleMobileAdsConsentManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.background.video.recorder.camera.recorder.R
import com.background.video.recorder.camera.recorder.databinding.ActivitySplashBinding
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.RemoteConfig
import com.background.video.recorder.camera.recorder.util.FirebaseEvents.Companion.logAnalytic
import com.background.video.recorder.camera.recorder.util.SharePref
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys.Companion.InApp
import com.background.video.recorder.camera.recorder.util.constant.Constants
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import roozi.app.ads.AdsManager
import roozi.app.billing.BillingManager
import roozi.app.interfaces.EventListener
import roozi.app.interfaces.IPurchaseListener
import java.util.concurrent.atomic.AtomicBoolean

class SplashActivity : AppCompatActivity(), IPurchaseListener, EventListener {
    private val count = 0
    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    private var mNavHostFragment: NavHostFragment? = null
    private val navController: NavController? = null
    private var view: View? = null
    private lateinit var googleMobileAdsConsentManager: GoogleMobileAdsConsentManager
    private val isMobileAdsInitializeCall = AtomicBoolean(false)
    override fun onCreate(savedInstanceState: Bundle?) {

        RemoteConfig.setAdData(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initConcentForm()

        BillingManager.purchaseListener = this
        AdsManager.eventListener = this

        BillingManager.init(this) {
            lifecycleScope.launch(Dispatchers.IO) {
                BillingManager.queryPurchases(RemoteConfig.adData)
                BillingManager.queryProductDetails()
            }
        }
        initComponents()
    }

    private fun initConcentForm() {
        googleMobileAdsConsentManager = GoogleMobileAdsConsentManager.getInstance(this@SplashActivity)
        googleMobileAdsConsentManager.gatherConsent(this@SplashActivity){ error->
            if (error!= null){
                Log.d("GoogleConcent", "onCreate: ${error.message}")
            }
            initAds()
            if (googleMobileAdsConsentManager.canRequestAds){
                if (isMobileAdsInitializeCall.getAndSet(true)){
                    return@gatherConsent
                }
                initAds()

            }

        }
    }

    fun initAds(){
        AudienceNetworkAds.initialize(this)
        MobileAds.initialize(
            this
        ) {

        }
    }

    private fun initComponents() {
        setUpNavGraph()
    }

    private fun setUpNavGraph() {
        // Initializing Host for navigating using Android Navigation Components
        try {
            mNavHostFragment =
                supportFragmentManager.findFragmentById(R.id.navContainerSplash) as NavHostFragment?
            if (mNavHostFragment != null) {
            } else {
                Log.d(TAG, "setUpNavGraph: " + "null")
            }
        } catch (nullPointerException: NullPointerException) {
            Log.e(
                TAG, "setUpNavGraph: e", nullPointerException.cause
            )
        }
    }

    override fun onResume() {
        super.onResume()
        RemoteConfig.getRCValues(this)
//        setLanguage(Locale(SharePref.getSelectedLanguage()!!, SharePref.countryCodeKey))
    }

    override fun onBackPressed() {
        if (Constants.ON_PRIVACY_FRAGMENT) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

//    override fun onBackPressed() {
//        val currentFragment =
//            supportFragmentManager.findFragmentById(R.id.navContainerSplash) // Assuming 'fragment_container' is your FrameLayout or container where you load your fragments
//        if (currentFragment is PreHomeFragment) {
//            (currentFragment as PreHomeFragment?)!!.handleBackPress()
//        }
//    }


    companion object {
        private const val TAG = "SplashActivity"
    }

    override fun activatePremiumVersion() {
        SharePref.putBoolean(InApp, true)
        if (this@SplashActivity.window.decorView.rootView.isShown) {
            BillingManager.showProgressDialog(
                this@SplashActivity,
                Intent(this@SplashActivity, SplashActivity::class.java)
            )
        }
    }

    override fun onDismiss(key: String) {
        logAnalytic("${key}_Dismiss")
        Log.d("EventLogsss", "${key}_Dismiss")
    }

    override fun onAdShow(key: String) {
        logAnalytic("${key}_Show")
        Log.d("EventLogsss", "${key}_Show")

    }

    override fun onAdClick(key: String) {
        logAnalytic("${key}_Click")
        Log.d("EventLogsss", "${key}_Click")
    }

}
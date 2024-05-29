package com.background.video.recorder.camera.recorder.ui.activitiy

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.background.video.recorder.camera.recorder.BuildConfig
import com.background.video.recorder.camera.recorder.R
import com.background.video.recorder.camera.recorder.ads.AdMobBanner
import com.background.video.recorder.camera.recorder.ads.AdsHelper.showAdMobBanner
import com.background.video.recorder.camera.recorder.ads.AdsHelperInternet
import com.background.video.recorder.camera.recorder.ads.InterstitialAdUtils
import com.background.video.recorder.camera.recorder.application.MyApp
import com.background.video.recorder.camera.recorder.application.MyApp.Companion.enableshouldshowappopenad
import com.background.video.recorder.camera.recorder.databinding.ActivityHomeBinding
import com.background.video.recorder.camera.recorder.databinding.RemovePasswordDialogBinding
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper
import com.background.video.recorder.camera.recorder.ui.fragment.HomeFragment
import com.background.video.recorder.camera.recorder.util.FirebaseEvents.Companion.logAnalytic
import com.background.video.recorder.camera.recorder.util.SharePref
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys
import com.background.video.recorder.camera.recorder.util.navigation.NavigationUtils
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import roozi.app.ads.AdsManager
import roozi.app.billing.BillingManager
import roozi.app.billing.BillingManager.Companion.showPrivacyDialog
import roozi.app.interfaces.IPurchaseListener


class HomeActivity : AppCompatActivity(), NavController.OnDestinationChangedListener,
    IPurchaseListener {
    //    IPurchaseListener, EventListener {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var mainHeading: TextView
    private var controller: NavController? = null
    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    var banner_id: String? = null
    var drawer: DrawerLayout? = null
    var banner_bg = false
    var sharedPrefs: SharedPreferences? = null

    private var remove_password_admob_native = true
    private var remove_password_facebook_native = true
    private var facebook_banner_enable = true
    private var admob_banner_enable = true
    private var facebook_native_ad_id = ""
    private var admob_native_id = ""
    private var prefs: SharedPrefsHelper? = null
    private var appUpdateManager: AppUpdateManager? = null
    private var installStateUpdatedListener: InstallStateUpdatedListener? = null
    private var app: MyApp? = null

    private var admob_banner_id = ""
    private var facebook_banner_ad_id = ""

    private val requestPermissionLauncher = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {

        } else {
        }
    }


    private fun checkForAppUpdate() {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo

        // Create a listener to track request state updates.
        installStateUpdatedListener =
            InstallStateUpdatedListener { installState ->
                // Show module progress, log state, or install the update.
                if (installState.installStatus() == InstallStatus.DOWNLOADED)
                // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                    popupSnackbarForCompleteUpdateAndUnregister()
            }

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // Request the update.
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                    // Before starting an update, register a listener for updates.
                    appUpdateManager!!.registerListener(installStateUpdatedListener!!)
                    // Start an update.
                    startAppUpdateFlexible(appUpdateInfo)
                }
            }
        }
    }

    private fun startAppUpdateFlexible(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager!!.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.FLEXIBLE,
                this,
                10101
            )
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
            unregisterInstallStateUpdListener()
        }
    }

    private fun popupSnackbarForCompleteUpdateAndUnregister() {
        val parentLayout: View = findViewById(android.R.id.content)
        val snackbar = Snackbar.make(
            parentLayout,
            getString(R.string.update_downloaded),
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction(
            R.string.restart
        ) { appUpdateManager!!.completeUpdate() }
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.purple_200))
        snackbar.show()
        unregisterInstallStateUpdListener()
    }

    private fun unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null) appUpdateManager!!.unregisterListener(
            installStateUpdatedListener!!
        )
    }

    override fun onDestroy() {
        unregisterInstallStateUpdListener()
        super.onDestroy()
    }

    private fun loadFragment(fragmentID: Int) {
        controller?.navigate(fragmentID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        sharedPreferences =
            this.getSharedPreferences("stateSavePreference", Context.MODE_PRIVATE)

        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        enableshouldshowappopenad()
        val visitCount = sharedPrefs.getInt("visit_count", 1)


//        if (visitCount >= 3) {
//
//            val manager = ReviewManagerFactory.create(this)
//            val request = manager.requestReviewFlow()
//            request.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val reviewInfo = task.result
//                    val flow = manager.launchReviewFlow(this, reviewInfo)
//                    flow.addOnCompleteListener { _ ->
//                        disableshouldshowappopenad()
//                        // The flow has finished. Continue your app flow.
//                    }
//                } else {
//                    val reviewErrorCode = (task.exception as ReviewException).errorCode
//                    Log.d(TAG, "reviewErrorCode: $reviewErrorCode")
//                }
//            }
//            enableshouldshowappopenad()
//            // Reset the visit count after showing the review flow
//            sharedPrefs.edit().putInt("visit_count", 1).apply()
//        } else {
//            // Increment the visit count
//
//            enableshouldshowappopenad()
//            sharedPrefs.edit().putInt("visit_count", visitCount + 1).apply()
//            Log.d(TAG, "reviewErrorCode: $visitCount")
//        }


        // BEst Inapp review
//        val manager = ReviewManagerFactory.create(this)
//        val request = manager.requestReviewFlow()
//        request.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                // We got the ReviewInfo object
//                val reviewInfo = task.result
//                Log.d(TAG, "reviewisSuccessful: "+reviewInfo.toString())
//                val flow = manager.launchReviewFlow(this, reviewInfo)
//                flow.addOnCompleteListener { _ ->
//                    // The flow has finished. The API does not indicate whether the user
//                    // reviewed or not, or even whether the review dialog was shown. Thus, no
//                    // matter the result, we continue our app flow.
//                }
//            } else {
//                // There was some problem, log or handle the error code.
//                @ReviewErrorCode val reviewErrorCode = (task.getException() as ReviewException).errorCode
//                Log.d(TAG, "reviewErrorCode: "+reviewErrorCode.toString())
//            }
//        }
//


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW
                )
            )
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (intent.extras != null) {
            for (key in intent.extras!!.keySet()) {
                val value = intent.extras!![key]
                Log.d(
                    TAG,
                    "Key: $key Value: $value"
                )
            }
        }
        setUpNavGraph()
        controller!!.addOnDestinationChangedListener(this)
        prefs = SharedPrefsHelper.getInstance(this)


        appUpdateManager = AppUpdateManagerFactory.create(this)
        checkForAppUpdate()
        val shouldLoadFragment = intent.getBooleanExtra("loadFragment", false)

        if (shouldLoadFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerViewHome, HomeFragment())
                .commit()
        }
        val shouldLoadFragment2 = intent.getBooleanExtra("loadFragment1", false)

        if (shouldLoadFragment2) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerViewHome, HomeFragment())
                .commit()
        }


//        chkPermission()


        val localPrefs = prefs
        if (localPrefs != null) {
            if (localPrefs.isFirstTimeLaunch()) {
                loadFragment(R.id.onBoardingFragment)
                Log.d(TAG, "onCreate: First time launch onbbaroding fragemtn ")
                // Set first time launch to false after showing Onboarding
                localPrefs.setFirstTimeLaunch(false)
//                }
            } else {
                Log.d(TAG, "onCreate: next time prehome fragemtnt  fragemtn ")
                // Regular launch, load Home Fragment directly
                loadFragment(R.id.homeFragment)
//                loadFragment(R.id.onBoardingFragment)
            }


//            if (localPrefs.isFirstTimeLaunch()) {
//                if (!localPrefs.isLanguageSet()) {
//                    // Start Language Activity if the language isn't set
//                    startActivity(Intent(this, LanguageActivity::class.java))
//                    finish()
//                    return
//                } else {
//                // Load Onboarding Fragment only the first time after Language is set
//                loadFragment(R.id.onBoardingFragment)
//                Log.d(TAG, "onCreate: First time launch onbbaroding fragemtn ")
//                // Set first time launch to false after showing Onboarding
//                localPrefs.setFirstTimeLaunch(false)
//                }
//            } else {
//                Log.d(TAG, "onCreate: next time prehome fragemtnt  fragemtn ")
//                // Regular launch, load Home Fragment directly
//                loadFragment(R.id.homeFragment)
//            }


        }

        if (localPrefs != null) {
            remove_password_admob_native = localPrefs.getremove_password_admob_nativeSwitch()
            remove_password_facebook_native = localPrefs.getremove_password_facebook_nativeSwitch()
            facebook_banner_enable = localPrefs.getfacebook_banner_enableSwitch()
            admob_banner_enable = localPrefs.getadmob_banner_enableSwitch()
//            admob_banner_enable = true
            admob_native_id = localPrefs.getadmob_native_idId()
            admob_banner_id = localPrefs.getadmob_banner_idId()
            facebook_banner_ad_id = localPrefs.getfacebook_banner_ad_idId()
            facebook_native_ad_id = localPrefs.getfacebook_native_ad_idId()
            Log.d(
                "TAG",
                "onCreate:  admob_interstitial_splash_id  $remove_password_facebook_native"
            )
            Log.d(
                "",
                "onCreate:  facebook_interstitial_splash_id  $remove_password_facebook_native"
            )
        }


//        native_bg = sharedPrefs.getBoolean("native_bg", false)
        banner_bg = sharedPrefs!!.getBoolean("banner_bg", false)
        banner_id = sharedPrefs!!.getString("banner_id", "")

        if (SharePref.getBoolean(AdsKeys.InApp, false)) {
            binding.bannerAd.visibility = View.GONE
        } else if (!AdsHelperInternet.isNetworkAvailable(this)) {
            binding.bannerAd.visibility = View.GONE
        } else if (admob_banner_enable) {
            Log.e("bannercheckit", "Banner: admob_banner_enable loaded ")
            AdMobBanner.loadAd(this, binding.bannerAd, admob_banner_id, facebook_banner_ad_id)
        } else if (facebook_banner_enable) {

            Log.e("bannercheckit", "Banner: facebook_banner_enable loaded ")
            AdMobBanner.loadAdFifty(binding.bannerAd, this, facebook_banner_ad_id)
        } else {
            binding.bannerAd.visibility = View.GONE
        }


        binding.updateicon.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://play.google.com/store/apps/details?id=" + "${BuildConfig.APPLICATION_ID}")))
        }

        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        BillingManager.purchaseListener = this

//        AdsManager.eventListener = this
//        AdsManager.banner(
//            SplashTwoFragment.getBoolean(AdsKeys.Admob_DashBoard_Banner),
//            SplashTwoFragment.getBoolean(AdsKeys.Facebook_DashBoard_Banner),
//            this, binding.bannerAd
//        )

        editor = sharedPreferences.edit()
        listeners()

        findViewById<View>(R.id.drawer_menu).setOnClickListener { view: View? ->
            if (drawer!!.isDrawerOpen(GravityCompat.START)) {
                drawer!!.closeDrawer(GravityCompat.START)
            } else {
                drawer!!.openDrawer(GravityCompat.START)
            }
        }
        findViewById<View>(R.id.setting_as_arewer_btn).setOnClickListener { view: View? ->
            if (drawer!!.isDrawerOpen(GravityCompat.START)) {
                drawer!!.closeDrawer(GravityCompat.START)
            } else {
                drawer!!.openDrawer(GravityCompat.START)
            }
        }

        navigationView.itemIconTintList = null
        val params = navigationView.layoutParams as DrawerLayout.LayoutParams
        navigationView.setNavigationItemSelectedListener { item ->
            Log.d("navigationClick", "onNavigationItemSelected: ")
            val id = item.itemId
            if (id == R.id.more_apps) {

                isOpenAdShow = true
                AdsManager.isInterShowing = true
                logAnalytic("drawer_more_apps_btn")
                try {
                    val uri =
                        Uri.parse("https://play.google.com/store/apps/developer?id=Barakat+Apps+Sole")
                    startActivity(Intent(Intent.ACTION_VIEW, uri))
                } catch (anfe: ActivityNotFoundException) {
                    Toast.makeText(
                        applicationContext,
                        "Google Play Store is not available.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else if (id == R.id.share_app) {

                isOpenAdShow = true
                AdsManager.isInterShowing = true
                logAnalytic("drawer_share_btn")
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                share.putExtra(Intent.EXTRA_SUBJECT, "background video recorder")
                share.putExtra(
                    Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id=$packageName&hl=en_US&gl=US"
                )
                share.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                shareActivityResultLauncher.launch(Intent.createChooser(share, "Share with"))



            } else if (id == R.id.language) {
                logAnalytic("drawer_language_btn")
                drawer!!.openDrawer(GravityCompat.START)
                startActivity(Intent(this, LanguageActivity::class.java))
            } else if (id == R.id.policy) {
                logAnalytic("drawer_privacy_policy_btn")
                showPrivacyDialog(
                    this,
                    "https://sites.google.com/view/barakatappssoleprivacypolicy/home",
                    "Privacy Policy"
                )
            } else if (id == R.id.pro_app) {
                logAnalytic("drawer_pro_btn")
                drawer!!.closeDrawer(GravityCompat.START)
                BillingManager.showPremium(this) {}
            } else if (id == R.id.remove_password) {
                logAnalytic("drawer_remove_password_btn")
                drawer!!.closeDrawer(GravityCompat.START)
                warningDialog()
            } else if (id == R.id.BackImageBtnDrawer) {

                logAnalytic("drawer_back_btn")
                drawer!!.closeDrawer(GravityCompat.START)
            }


            //                drawer.closeDrawer(GravityCompat.START);
            true
        }
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//
//        val toggle = ActionBarDrawerToggle(
//            this,
//            drawer,
//            toolbar,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        )
//        toggle.drawerArrowDrawable.color = Color.WHITE
//        drawer!!.addDrawerListener(toggle)


        val headerLayout = navigationView.getHeaderView(0) // 0-index header

        headerLayout.findViewById<View>(R.id.BackImageBtnDrawer).setOnClickListener {
            drawer!!.closeDrawer(
                GravityCompat.START
            )
        }

        askNotificationPermission()
    }

    var isOpenAdShow = false


    override fun onResume() {
        super.onResume()
        if (isOpenAdShow){
            Handler().postDelayed(
                {
                AdsManager.isInterShowing = false
            }, 2000
            )
        }
    }


    private val shareActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            AdsManager.isInterShowing = false
            Log.d("ShareIntent123", "Share dialog dismissed")
        }
    }


    private fun askNotificationPermission() {
        // This is only necessary for API Level > 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    var checkPermission = false

    fun warningDialog() {
//        if (isAdded) {
        val inflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val bind = RemovePasswordDialogBinding.inflate(inflater)
        val dialog = Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
        dialog.setContentView(bind.root)

        dialog.setOnShowListener {
            if (SharePref.getBoolean(AdsKeys.InApp, false)) {
                bind.nativeAdRemovepassword.visibility = View.INVISIBLE
            } else if (remove_password_admob_native) {
                InterstitialAdUtils.loadMediationNative(
                    this,
                    bind.nativeAdRemovepassword,
                    admob_native_id,
                    bind.nativeAdContainer,
                    R.layout.ad_native_layout_modified
                )
            } else if (remove_password_facebook_native) {
                InterstitialAdUtils.loadNativeFacebookAd(
                    facebook_native_ad_id,
                    this,
                    bind.nativeAdContainer
                )
            } else {
                Log.d("checksswitcehs", "onViewCreated: both are off")
                bind.nativeAdRemovepassword.visibility = View.GONE
            }
        }

        bind.back.setOnClickListener {
            dialog.dismiss()
        }
        bind.pro.setOnClickListener {
//                BillingManager.showPremium(requireContext()) {}
        }
        bind.cancel.setOnClickListener {
            dialog.dismiss()
        }
        bind.yesBtn.setOnClickListener {
            dialog.dismiss()
            val bundle = Bundle()
            bundle.putString("action", "settings")
            NavigationUtils.navigate(this, R.id.remove_password, bundle)
        }
        dialog.show()
//        }
    }

    private fun listeners() {


        binding.back.setOnClickListener {

                view1: View? ->
            onBackPressed()

        }


        binding.pro.setOnClickListener {

            MyApp.disableshouldshowappopenad()
            Log.d("TAGappd", "listeners: d ")

            logAnalytic("Main_screen_PRO_btn")
            BillingManager.showPremium(this) {
                Log.d("TAGappd", "listeners: dismiss ")
                MyApp.enableshouldshowappopenad()


            }
        }
//        binding.language.setOnClickListener {
//            startActivity(Intent(this, LanguageActivity::class.java))
//        }
        binding.lock.setOnClickListener {
            if (sharedPreferences.getBoolean("appLocked", false)) {

                controller?.navigate(R.id.verifyPassword)
            } else
                logAnalytic("set_password_btn")
            controller?.navigate(R.id.mySetPassword)
        }
    }

    private fun showBannerAd(mView: RelativeLayout, bannerKey: String, facebookkey: String) {
        Log.d("checkiyty", "showBannerAd: in banner method ")
        showAdMobBanner(
            this,
            mView,
            bannerKey,
            facebookkey
        )
    }

    private fun setUpNavGraph() {
        // Initializing Host for navigating using Android Navigation Components
        try {
            val mNavHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerViewHome) as NavHostFragment?
            if (mNavHostFragment != null) {
                controller = mNavHostFragment.navController
            } else {
                Log.d(TAG, "setUpNavGraph: " + "null")
            }
        } catch (nullPointerException: NullPointerException) {
            Log.e(
                TAG, "setUpNavGraph: e", nullPointerException.cause
            )
        }
    }

    override fun onBackPressed() {
        when (controller!!.currentDestination!!.id) {
            R.id.trimFragment -> {
                logAnalytic("back_btn_trim")
                controller!!.navigate(R.id.homeFragment)
            }

            R.id.galleryFragment -> {
                logAnalytic("back_btn_trim")
                controller!!.navigate(R.id.homeFragment)
            }

            R.id.shareFragment -> {
                logAnalytic("back_btn_trim")
                controller!!.navigate(R.id.homeFragment)
            }

            R.id.imagePickerFragment -> {
                logAnalytic("back_btn_trim")
                controller!!.navigate(R.id.homeFragment)
            }

            R.id.editImageFragment -> {
                logAnalytic("back_btn_trim")
                controller!!.navigate(R.id.homeFragment)
            }


            R.id.favFilesFragment -> {
                logAnalytic("back_btn_Favourite_files")
                controller!!.navigate(R.id.homeFragment)
            }


            R.id.filesFragment -> {
                logAnalytic("back_btn_files_btn")
                controller!!.navigate(R.id.homeFragment)
            }

            R.id.processFragment -> {
                logAnalytic("back_btn_photoEditorCollageMakerFragment_btn")
                controller!!.navigate(R.id.homeFragment)
            }

            R.id.screenRecordingFragment -> {
                logAnalytic("back_btn_screen_recorder_gallery_btn")
                controller!!.navigate(R.id.homeFragment)
            }

            R.id.videoViewFragment -> {
                logAnalytic("back_btn_screen_recorder_gallery_btn")
                controller!!.navigate(R.id.homeFragment)
            }

            R.id.screenRecordingFragment2 -> {
                logAnalytic("back_btn_screen_recorder_btn")
                controller!!.navigate(R.id.homeFragment)
            }

            R.id.albumFragment -> {
                logAnalytic("back_btn_album_fragment_btn")
                controller!!.navigate(R.id.homeFragment)
            }
            // Add more cases for other fragments as needed
            R.id.homeFragment -> {
                if (drawer?.isDrawerOpen(GravityCompat.START) == true) {
                    // Close the drawer if open
                    drawer!!.closeDrawer(GravityCompat.START)
                } else {
                    navigateToExitFragment()
                }
            }

//            R.id.homeFragment -> {
//                if (drawer?.isDrawerOpen(GravityCompat.START) == true) {
////                 Close the drawer if open
//                    drawer!!.closeDrawer(GravityCompat.START)
//                } else {
//                    navigateToExitFragment2()
//                }
//            }

            else -> super.onBackPressed()
        }
    }


    private fun navigateToExitFragment() {
        controller?.navigate(R.id.exitFragment)
        // Assuming you have the necessary action or destination in your NavGraph

    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        bundle: Bundle?
    ) {
        // Handling the mainHeading text
        binding.mainHeading.text = destination.label
        when (destination.id) {
            R.id.homeFragment -> {
                binding.homeBtn.visibility = View.GONE
                binding.topBar.visibility = View.VISIBLE
//                binding.bannerAd.visibility = View.VISIBLE


//                binding.languageBtnAinfrag.visibility = View.INVISIBLE
            }

            R.id.imageViewFragment -> {
                binding.lock.visibility = View.GONE
                binding.homeBtn.visibility = View.INVISIBLE
            }

            R.id.onBoardingFragment -> {
                binding.lock.visibility = View.GONE
                binding.homeBtn.visibility = View.INVISIBLE
                binding.topBar.visibility = View.GONE
                binding.bannerAd.visibility = View.GONE
            }

            R.id.galleryFragment -> {
            }

            R.id.editImageFragment -> {
                binding.lock.visibility = View.GONE
                binding.homeBtn.visibility = View.INVISIBLE

            }

            R.id.passLay -> {
                binding.nextBtn.visibility = View.INVISIBLE
            }


//            R.id.homeFragment -> {
//                binding.homeBtn.visibility = View.GONE
//
////                binding.languageBtnAinfrag.visibility = View.INVISIBLE
//            }

            R.id.trimFragment -> {

                binding.homeBtn.visibility = View.GONE

//                binding.languageBtnAinfrag.visibility = View.INVISIBLE

            }

            R.id.favFilesFragment -> {
                binding.homeBtn.visibility = View.GONE

                binding.pro.visibility = View.GONE
//                binding.languageBtnAinfrag.visibility = View.INVISIBLE
            }

            R.id.filesFragment -> {
                binding.homeBtn.visibility = View.GONE

//                binding.languageBtnAinfrag.visibility = View.INVISIBLE
            }

            R.id.settingsFragment -> {
                binding.homeBtn.visibility = View.GONE


//                binding.languageBtnAinfrag.visibility = View.INVISIBLE
            }

//            R.id.videoToGifFragment -> {
//
//                binding.languageBtnAinfrag.visibility = View.INVISIBLE
//            }

            R.id.exitFragment -> {

                binding.pro.visibility = View.VISIBLE
                binding.lock.visibility = View.GONE
                binding.back.visibility = View.GONE
                binding.drawerMenu.visibility = View.GONE
                binding.homeBtn.visibility = View.GONE
                binding.settingAsArewerBtn.visibility = View.VISIBLE
            }
        }

        // Handling visibility and other properties
        when (destination.id) {
            R.id.homeFragment -> {
                binding.pro.visibility = View.VISIBLE
                binding.back.visibility = View.GONE
                binding.lock.visibility = View.GONE
                binding.drawerMenu.visibility = View.GONE
                binding.nextBtn.visibility = View.GONE
                binding.settingAsArewerBtn.visibility = View.VISIBLE
                binding.topBar.visibility = View.VISIBLE
//                binding.bannerAd.visibility = View.VISIBLE
            }

            R.id.passLay -> {
                binding.nextBtn.visibility = View.INVISIBLE
            }


            R.id.onBoardingFragment -> {
                binding.lock.visibility = View.GONE
                binding.homeBtn.visibility = View.INVISIBLE
                binding.topBar.visibility = View.GONE
//                binding.bannerAd.visibility = View.GONE
            }

            R.id.mySetPassword, R.id.reEnterPasswordFragment, R.id.enterPasswordFragment,
            R.id.mySuccessFragment, R.id.mySecurityFragment -> {
                binding.pro.visibility = View.VISIBLE
                binding.back.visibility = View.GONE
                binding.lock.visibility = View.GONE
                binding.drawerMenu.visibility = View.GONE
                binding.settingAsArewerBtn.visibility = View.GONE
            }

            R.id.mySetting -> {
                binding.pro.visibility = View.VISIBLE
                binding.lock.visibility = View.GONE
                binding.back.visibility = View.VISIBLE
                binding.drawerMenu.visibility = View.GONE
            }


            R.id.imagePickerFragment -> {
                binding.lock.visibility = View.GONE
                binding.pro.visibility = View.GONE
                binding.nextBtn.visibility = View.VISIBLE
                binding.homeBtn.visibility = View.GONE
                binding.settingAsArewerBtn.visibility = View.GONE
            }

            R.id.editImageFragment -> {
                binding.lock.visibility = View.GONE
                binding.pro.visibility = View.GONE
                binding.nextBtn.visibility = View.VISIBLE
                binding.back.visibility = View.VISIBLE
                binding.homeBtn.visibility = View.GONE
                binding.settingAsArewerBtn.visibility = View.GONE
            }

            R.id.processFragment -> {
                binding.lock.visibility = View.GONE
                binding.pro.visibility = View.GONE
                binding.saveBtn.visibility = View.VISIBLE
                binding.nextBtn.visibility = View.GONE
            }

            R.id.shareFragment -> {
                binding.lock.visibility = View.GONE
                binding.pro.visibility = View.GONE
                binding.saveBtn.visibility = View.GONE
                binding.nextBtn.visibility = View.GONE
                binding.homeBtn.visibility = View.VISIBLE
                binding.settingAsArewerBtn.visibility = View.GONE
            }

            R.id.albumFragment -> {
                binding.lock.visibility = View.GONE
                binding.pro.visibility = View.GONE
                binding.saveBtn.visibility = View.GONE
                binding.nextBtn.visibility = View.GONE
                binding.homeBtn.visibility = View.VISIBLE
                binding.settingAsArewerBtn.visibility = View.GONE
            }

            else -> {
                if (sharedPreferences.getBoolean("appLocked", false)) {
                    binding.lock.visibility = View.GONE
                    binding.pro.visibility = View.VISIBLE
                } else {
                    binding.lock.visibility = View.VISIBLE
                    binding.settingAsArewerBtn.visibility = View.GONE
                    binding.pro.visibility = View.GONE
                }
                binding.drawerMenu.visibility = View.GONE
                binding.back.visibility = View.VISIBLE
            }
        }
    }


    companion object {
        const val BROADCAST_ACTION = "br.com.example.backgroundvideorecorder.BROADCAST"
        private const val TAG = "HomeActivity123"
    }

    override fun activatePremiumVersion() {
        SharePref.putBoolean(AdsKeys.InApp, true)
        if (this@HomeActivity.window.decorView.rootView.isShown) {
            BillingManager.showProgressDialog(
                this@HomeActivity,
                Intent(this@HomeActivity, SplashActivity::class.java)
            )
        }
    }
}
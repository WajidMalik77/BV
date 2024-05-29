package com.background.video.recorder.camera.recorder.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.background.video.recorder.camera.recorder.R
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.RemoteConfigManager
import com.background.video.recorder.camera.recorder.firebase.remoteconfig.SharedPrefsHelper
import com.facebook.ads.AdSettings
import com.facebook.ads.BuildConfig
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import roozi.app.ads.AdmobManager

class MyApp : Application(), LifecycleObserver, Application.ActivityLifecycleCallbacks {
    lateinit var sharedPrefs: SharedPreferences


    override fun onCreate() {
        super.onCreate()
        application = this
        sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val homeInter = sharedPrefs.getBoolean("home_inter", false)
        registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)


        RemoteConfigManager.getInstance().fetchRemoteConfigValues(this);


        //This will make the ad run on the test device, let's say your Android AVD emulator
        if (BuildConfig.DEBUG) {
            AdSettings.setTestMode(true);
        }

        FirebaseApp.initializeApp(this)
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(1) // Adjust as needed
            .build()
        FirebaseRemoteConfig.getInstance().setConfigSettingsAsync(configSettings)

        val remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults) // R.xml.remote_config_defaults points to your default values
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener((OnCompleteListener<Boolean?> { task: Task<Boolean?> ->
                if (task.isSuccessful) {
                    val appOpenAd = remoteConfig.getBoolean("app_open")
                    val homeInter = remoteConfig.getBoolean("home_inter")
                    val splashInter = remoteConfig.getBoolean("splash_inter")
                    val native = remoteConfig.getBoolean("native_bg")
                    val banner = remoteConfig.getBoolean("banner_bg")
                    val banner_id = remoteConfig.getString("banner_id")
                    val native_id = remoteConfig.getString("native_id")
                    val interSplash_id = remoteConfig.getString("interSplash_id")
                    val interHome_id = remoteConfig.getString("interHome_id")
                    val appOpen_id = remoteConfig.getString("appOpen_id")


                    val sharedPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    val editor = sharedPrefs.edit()
                    editor.putBoolean("appOpenAd", appOpenAd)
                    editor.putBoolean("home_inter", homeInter)
                    editor.putBoolean("splash_inter", splashInter)
                    editor.putBoolean("native_bg", native)
                    editor.putBoolean("banner_bg", banner)
                    editor.putString("banner_id", banner_id)
                    editor.putString("native_id", native_id)
                    editor.putString("interSplash_id", interSplash_id)
                    editor.putString("interHome_id", interHome_id)
                    editor.putString("appOpen_id", appOpen_id)
                    editor.apply()

                    Log.d("taggy1", "onCreate: 0appopen" + native_id)
//                    Log.d("taggy1", "onCreate: 0home"+homeInter)
//                    Log.d("taggy1", "onCreate: 0splash"+splashInter)

                } else {
                    // Handle fetch error
                }

            }))


    }


    var currentActivity: Activity? = null
//    fun setShouldNotShowAd(value: Int) {
//        shouldNotShowAd = value
//    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onMoveToForeground() {
//        if (shouldNotShowAd == 0 || shouldNotShowAd == 2 || shouldNotShowAd == 3 || shouldNotShowAd == 4) {



        if (!shouldshowappopenad) {
            val appOpenAd = sharedPrefs.getBoolean("appOpenAd", false)
            if (appOpenAd) {
                Log.d("TAGappd", "onMoveToForeground: ")
                val prefs = SharedPrefsHelper.getInstance(this)
//                val appOpenAdId = prefs.getAppopenAdId()

                val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val appOpenAdId = sharedPrefs.getString("appOpen_id", "default_appOpen_id") ?: "default_appOpen_id"

                val shouldShowAd = prefs.getBoolean("shouldShowAd", false)
                AdmobManager.loadAndShowOpenAd(application as Activity,appOpenAdId,shouldShowAd)
//                AdMobAppOpen.showOpnAd(application as Activity)
            } else {
//                Toast.makeText(applicationContext, "appopen is flase", Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
        private lateinit var application: Context
        var shouldshowappopenad: Boolean = false


        fun enableshouldshowappopenad() {
            Handler(Looper.getMainLooper()).postDelayed({

                shouldshowappopenad = false


            }, 200)

        }

        fun disableshouldshowappopenad() {
            shouldshowappopenad = true


        }

        fun getAppContext(): Context {
            return application.applicationContext
        }

//        lateinit var mContext: Context
//        fun myAppContext(): Context {
//            return mContext.applicationContext
//        }

//        fun getAppContext(): Context {
//            return instance.applicationContext
//        }

    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        application = p0

    }

    override fun onActivityStarted(p0: Activity) {
        application = p0
    }

    override fun onActivityResumed(p0: Activity) {
        application = p0

    }

    override fun onActivityPaused(p0: Activity) {
        application = p0

    }

    override fun onActivityStopped(p0: Activity) {
        application = p0

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        application = p0

    }

    override fun onActivityDestroyed(p0: Activity) {
        application = p0
    }
}
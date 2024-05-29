package com.background.video.recorder.camera.recorder

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm.OnConsentFormDismissedListener
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform

//*
// * The Google Mobile Ads SDK provides the User Messaging Platform (Google's IAB Certified consent
// * management platform) as one solution to capture consent for users in GDPR impacted countries.
// * This is an example and you can choose another consent management platform to capture consent.

class GoogleMobileAdsConsentManager private constructor(context: Context) {
    private val consentInformation: ConsentInformation =
        UserMessagingPlatform.getConsentInformation(context)

//    * Interface definition for a callback to be invoked when consent gathering is complete.
    fun interface OnConsentGatheringCompleteListener {
        fun consentGatheringComplete(error: FormError?)
    }

//    * Helper variable to determine if the app can request ads.
    val canRequestAds: Boolean
        get() = consentInformation.canRequestAds()

//    * Helper variable to determine if the privacy options form is required.
    val isPrivacyOptionsRequired: Boolean
        get() =
            consentInformation.privacyOptionsRequirementStatus ==
                    ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED

//    *
//     * Helper method to call the UMP SDK methods to request consent information and load/show a
//     * consent form if necessary.

    fun gatherConsent(
        activity: Activity,
        onConsentGatheringCompleteListener: OnConsentGatheringCompleteListener
    ) {
        // For testing purposes, you can force a DebugGeography of EEA or NOT_EEA.
        val debugSettings = ConsentDebugSettings.Builder(activity)
            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
            .addTestDeviceHashedId("B0189D762779B987074EE312CEA03D1C")
            .build()
        // Create a ConsentRequestParameters object.
        val params = ConsentRequestParameters
            .Builder()
            .setConsentDebugSettings(debugSettings)
            .build()

        // Requesting an update to consent information should be called on every app launch.
        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    // Consent has been gathered.
                    Log.d("conscastatus", "gatherConsent: ${consentInformation.consentStatus}")
                    onConsentGatheringCompleteListener.consentGatheringComplete(formError)
                }

            },
            { requestConsentError ->
                onConsentGatheringCompleteListener.consentGatheringComplete(requestConsentError)
            }
        )
//        if (BuildConfig.DEBUG){
        consentInformation.reset()
//        }

    }

//    * Helper method to call the UMP SDK method to show the privacy options form.
    fun showPrivacyOptionsForm(
        activity: Activity,
        onConsentFormDismissedListener: OnConsentFormDismissedListener
    ) {
        Log.d("conscastatus", "gatherConsent: ${consentInformation.consentStatus}")
        UserMessagingPlatform.showPrivacyOptionsForm(activity, onConsentFormDismissedListener)
    }

    companion object {
        @Volatile
        private var instance: GoogleMobileAdsConsentManager? = null

        fun getInstance(context: Context) =
            instance
                ?: synchronized(this) {
                    instance ?: GoogleMobileAdsConsentManager(context).also { instance = it }


                }
    }
}



//class GoogleMobileAdsConsentManager private constructor(context: Context) {
//    private val consentInformation: ConsentInformation =
//        UserMessagingPlatform.getConsentInformation(context)
//
//    fun interface OnConsentGatheringCompleteListener {
//        fun consentGatheringComplete(error: FormError?)
//    }
//
//    val canRequestAds: Boolean
//        get() = consentInformation.canRequestAds()
//
//    val isPrivacyOptionsRequired: Boolean
//        get() =
//            consentInformation.privacyOptionsRequirementStatus ==
//                    ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
//
//    fun gatherConsent(
//        activity: Activity,
//        onConsentGatheringCompleteListener: OnConsentGatheringCompleteListener
//    ) {
//        val debugSettings = ConsentDebugSettings.Builder(activity)
//            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
//            .addTestDeviceHashedId("B0189D762779B987074EE312CEA03D1C")
//            .build()
//
//        val params = ConsentRequestParameters
//            .Builder()
//            .setConsentDebugSettings(debugSettings)
//            .build()
//
//        consentInformation.requestConsentInfoUpdate(
//            activity,
//            params,
//            {
//                if (consentInformation.consentStatus == ConsentInformation.ConsentStatus.REQUIRED) {
//                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
//                        Log.d("conscastatus", "gatherConsent: ${consentInformation.consentStatus}")
//                        onConsentGatheringCompleteListener.consentGatheringComplete(formError)
//                    }
//                } else {
//                    // Consent is not required or already obtained.
//                    Log.d("conscastatus", "not required")
//                    onConsentGatheringCompleteListener.consentGatheringComplete(null)
//                }
//            },
//            { requestConsentError ->
//                onConsentGatheringCompleteListener.consentGatheringComplete(requestConsentError)
//            }
//        )
//
//        consentInformation.reset()
//    }
//
//    fun showPrivacyOptionsForm(
//        activity: Activity,
//        onConsentFormDismissedListener: OnConsentFormDismissedListener
//    ) {
//        Log.d("conscastatus", "gatherConsent: ${consentInformation.consentStatus}")
//        UserMessagingPlatform.showPrivacyOptionsForm(activity, onConsentFormDismissedListener)
//    }
//
//    companion object {
//        @Volatile
//        private var instance: GoogleMobileAdsConsentManager? = null
//
//        fun getInstance(context: Context) =
//            instance
//                ?: synchronized(this) {
//                    instance ?: GoogleMobileAdsConsentManager(context).also { instance = it }
//                }
//    }
//}

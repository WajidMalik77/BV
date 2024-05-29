package com.background.video.recorder.camera.recorder.firebase.remoteconfig

import android.content.Context
import android.util.Log
import com.background.video.recorder.camera.recorder.BuildConfig
import com.background.video.recorder.camera.recorder.R
import com.background.video.recorder.camera.recorder.util.SharePref
import com.background.video.recorder.camera.recorder.util.constant.AdsKeys
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import roozi.app.models.AdData


class RemoteConfig {
    companion object {
        private const val TAG = "RCKEY"
        val adData = AdData()
        fun getRCValues(context: Context) {
            val remoteConfig = Firebase.remoteConfig
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0L
            }
            remoteConfig.setConfigSettingsAsync(configSettings)

            remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
//                        if (BuildConfig.DEBUG) {
//                            SharePref.putString(
//                                AdsKeys.AdmobNativeId,
//                                context.getString(R.string.admob_native_id)
//                            )
//                            SharePref.putString(
//                                AdsKeys.AdmobInterId,
//                                context.getString(R.string.admob_interstitial_id)
//                            )
//                            SharePref.putString(
//                                AdsKeys.AdmobBannerId,
//                                context.getString(R.string.admob_banner_id)
//                            )
//
//                            SharePref.putString(
//                                AdsKeys.FaceBookInterId,
//                                context.getString(R.string.fbInter)
//                            )
//                            SharePref.putString(
//                                AdsKeys.FaceBookNativeId,
//                                context.getString(R.string.fbNative)
//                            )
//                            SharePref.putString(
//                                AdsKeys.FaceBookBannerId,
//                                context.getString(R.string.fbBanner)
//                            )
//                            SharePref.putString(
//                                AdsKeys.AdmobOpenAdId,
//                                context.getString(R.string.admob_openAd_id)
//                            )
//
//                        } else {
                            SharePref.putString(
                                AdsKeys.AdmobNativeId,
                                remoteConfig.getString(AdsKeys.AdmobNativeId)
                            )
                            SharePref.putString(AdsKeys.AdmobInterId, remoteConfig.getString(AdsKeys.AdmobInterId))
                            SharePref.putString(
                                AdsKeys.AdmobBannerId,
                                remoteConfig.getString(AdsKeys.AdmobBannerId)
                            )

                            SharePref.putString(
                                AdsKeys.FaceBookInterId,
                                remoteConfig.getString(AdsKeys.FaceBookInterId)
                            )
                            SharePref.putString(
                                AdsKeys.FaceBookNativeId,
                                remoteConfig.getString(AdsKeys.FaceBookNativeId)
                            )
                            SharePref.putString(
                                AdsKeys.FaceBookBannerId,
                                remoteConfig.getString(AdsKeys.FaceBookBannerId)
                            )
                            SharePref.putString(
                                AdsKeys.AdmobOpenAdId,
                                remoteConfig.getString(AdsKeys.AdmobOpenAdId)
                            )
//                        }

                        getFirebaseKeys(remoteConfig)


                        SharePref.putBoolean(
                            AdsKeys.IsOpenAdEnable,
                            remoteConfig.getBoolean(AdsKeys.IsOpenAdEnable))

                        SharePref.putBoolean(AdsKeys.IsAdmobEnable, remoteConfig.getBoolean(AdsKeys.IsAdmobEnable))
                        SharePref.putBoolean(
                            AdsKeys.IsFaceBookEnable,remoteConfig.getBoolean(AdsKeys.IsFaceBookEnable)
                        )
                        SharePref.putBoolean(
                            AdsKeys.IsAppLovinEnable,
                            remoteConfig.getBoolean(AdsKeys.IsAppLovinEnable)
                        )
                        setAdData(context)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "fetchValuesFromRc: $it")
                }
        }

        //
        private fun getFirebaseKeys(remoteConfig: FirebaseRemoteConfig) {

            //admob keys
            SharePref.putBoolean(AdsKeys.Admob_Remove_Password_Dialog_Native, remoteConfig.getBoolean(AdsKeys.Admob_Remove_Password_Dialog_Native))
            SharePref.putBoolean(AdsKeys.Admob_Verify_Password_Native, remoteConfig.getBoolean(AdsKeys.Admob_Verify_Password_Native))
            SharePref.putBoolean(AdsKeys.Admob_Back_On_Dashboard_Inter, remoteConfig.getBoolean(AdsKeys.Admob_Back_On_Dashboard_Inter))
            SharePref.putBoolean(AdsKeys.Admob_Splash_Inter, remoteConfig.getBoolean(AdsKeys.Admob_Splash_Inter))
            SharePref.putBoolean(AdsKeys.Admob_Splash_Native, remoteConfig.getBoolean(AdsKeys.Admob_Splash_Native))
            SharePref.putBoolean(AdsKeys.Admob_DashBoard_Native, remoteConfig.getBoolean(AdsKeys.Admob_DashBoard_Native))
            SharePref.putBoolean(AdsKeys.Admob_DashBoard_Banner, remoteConfig.getBoolean(AdsKeys.Admob_DashBoard_Banner))

            SharePref.putBoolean(
                AdsKeys.Admob_SetPassword_Screen_Native, remoteConfig.getBoolean(
                    AdsKeys.Admob_SetPassword_Screen_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Admob_SetPassword_Screen_One_Native, remoteConfig.getBoolean(
                    AdsKeys.Admob_SetPassword_Screen_One_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Admob_SetPassword_Screen_Two_Native, remoteConfig.getBoolean(
                    AdsKeys.Admob_SetPassword_Screen_Two_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Admob_Save_Password_Screen_Native, remoteConfig.getBoolean(
                    AdsKeys.Admob_Save_Password_Screen_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Admob_Save_Password_Screen_Inter, remoteConfig.getBoolean(
                    AdsKeys.Admob_Save_Password_Screen_Inter
                ))
            SharePref.putBoolean(
                AdsKeys.Admob_Forget_Password_Screen_Native, remoteConfig.getBoolean(
                    AdsKeys.Admob_Forget_Password_Screen_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Admob_Video_Saving_Screen_Native, remoteConfig.getBoolean(
                    AdsKeys.Admob_Video_Saving_Screen_Native
                ))
            SharePref.putBoolean(AdsKeys.Admob_Rename_Screen_Native, remoteConfig.getBoolean(AdsKeys.Admob_Rename_Screen_Native))
            SharePref.putBoolean(
                AdsKeys.Admob_DashBoard_Trim_Video_Click_Inter, remoteConfig.getBoolean(
                    AdsKeys.Admob_DashBoard_Trim_Video_Click_Inter
                ))
            SharePref.putBoolean(AdsKeys.Admob_Trim_Video_Native, remoteConfig.getBoolean(AdsKeys.Admob_Trim_Video_Native))
            SharePref.putBoolean(
                AdsKeys.Admob_DashBoard_Recordings_Click_Inter, remoteConfig.getBoolean(
                    AdsKeys.Admob_DashBoard_Recordings_Click_Inter
                ))
            SharePref.putBoolean(AdsKeys.Admob_Recordings_Native, remoteConfig.getBoolean(AdsKeys.Admob_Recordings_Native))
            SharePref.putBoolean(
                AdsKeys.Admob_DashBoard_Favourites_Click_Inter, remoteConfig.getBoolean(
                    AdsKeys.Admob_DashBoard_Favourites_Click_Inter
                ))
            SharePref.putBoolean(AdsKeys.Admob_Favourites_Native, remoteConfig.getBoolean(AdsKeys.Admob_Favourites_Native))

            SharePref.putBoolean(AdsKeys.Admob_Exit_Native, remoteConfig.getBoolean(AdsKeys.Admob_Exit_Native))
            SharePref.putBoolean(AdsKeys.Admob_Exit_Inter, remoteConfig.getBoolean(AdsKeys.Admob_Exit_Inter))
            SharePref.putBoolean(AdsKeys.Admob_Settings_Native, remoteConfig.getBoolean(AdsKeys.Admob_Settings_Native))

            SharePref.putBoolean(AdsKeys.Admob_TrimBtnClick_Inter, remoteConfig.getBoolean(AdsKeys.Admob_TrimBtnClick_Inter))
            SharePref.putBoolean(AdsKeys.Admob_TrimmerBack_Inter, remoteConfig.getBoolean(AdsKeys.Admob_TrimmerBack_Inter))
            SharePref.putBoolean(AdsKeys.Admob_save_question_inter, remoteConfig.getBoolean(AdsKeys.Admob_save_question_inter))
            SharePref.putBoolean(AdsKeys.Admob_Play_video_native, remoteConfig.getBoolean(AdsKeys.Admob_Play_video_native))


            // Facebook keys
            SharePref.putBoolean(AdsKeys.Facebook_Play_video_native, remoteConfig.getBoolean(AdsKeys.Facebook_Play_video_native))
            SharePref.putBoolean(AdsKeys.Facebook_save_question_inter, remoteConfig.getBoolean(AdsKeys.Facebook_save_question_inter))
            SharePref.putBoolean(AdsKeys.Facebook_Remove_Password_Dialog_Native, remoteConfig.getBoolean(AdsKeys.Facebook_Remove_Password_Dialog_Native))
            SharePref.putBoolean(AdsKeys.Facebook_Verify_Password_Native, remoteConfig.getBoolean(AdsKeys.Facebook_Verify_Password_Native))
            SharePref.putBoolean(AdsKeys.Facebook_Back_On_Dashboard_Inter, remoteConfig.getBoolean(AdsKeys.Facebook_Back_On_Dashboard_Inter))
            SharePref.putBoolean(AdsKeys.Facebook_Settings_Native, remoteConfig.getBoolean(AdsKeys.Facebook_Settings_Native))
            SharePref.putBoolean(AdsKeys.Facebook_Splash_Inter, remoteConfig.getBoolean(AdsKeys.Facebook_Splash_Inter))
            SharePref.putBoolean(AdsKeys.Facebook_Splash_Native, remoteConfig.getBoolean(AdsKeys.Facebook_Splash_Native))
            SharePref.putBoolean(AdsKeys.Facebook_DashBoard_Native, remoteConfig.getBoolean(AdsKeys.Facebook_DashBoard_Native))
            SharePref.putBoolean(AdsKeys.Facebook_DashBoard_Banner, remoteConfig.getBoolean(AdsKeys.Facebook_DashBoard_Banner))
            SharePref.putBoolean(
                AdsKeys.Facebook_SetPassword_Screen_Native, remoteConfig.getBoolean(
                    AdsKeys.Facebook_SetPassword_Screen_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Facebook_SetPassword_Screen_One_Native, remoteConfig.getBoolean(
                    AdsKeys.Facebook_SetPassword_Screen_One_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Facebook_SetPassword_Screen_Two_Native, remoteConfig.getBoolean(
                    AdsKeys.Facebook_SetPassword_Screen_Two_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Facebook_Save_Password_Screen_Native, remoteConfig.getBoolean(
                    AdsKeys.Facebook_Save_Password_Screen_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Facebook_Save_Password_Screen_Inter, remoteConfig.getBoolean(
                    AdsKeys.Facebook_Save_Password_Screen_Inter
                ))
            SharePref.putBoolean(
                AdsKeys.Facebook_Forget_Password_Screen_Native, remoteConfig.getBoolean(
                    AdsKeys.Facebook_Forget_Password_Screen_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Facebook_Video_Saving_Screen_Native, remoteConfig.getBoolean(
                    AdsKeys.Facebook_Video_Saving_Screen_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Facebook_Rename_Screen_Native, remoteConfig.getBoolean(
                    AdsKeys.Facebook_Rename_Screen_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Facebook_DashBoard_Trim_Video_Click_Inter, remoteConfig.getBoolean(
                    AdsKeys.Facebook_DashBoard_Trim_Video_Click_Inter
                ))
            SharePref.putBoolean(AdsKeys.Facebook_Trim_Video_Native, remoteConfig.getBoolean(AdsKeys.Facebook_Trim_Video_Native))
            SharePref.putBoolean(
                AdsKeys.Facebook_DashBoard_Recordings_Click_Inter, remoteConfig.getBoolean(
                    AdsKeys.Facebook_DashBoard_Recordings_Click_Inter
                ))
            SharePref.putBoolean(AdsKeys.Facebook_Recordings_Native, remoteConfig.getBoolean(AdsKeys.Facebook_Recordings_Native))
            SharePref.putBoolean(
                AdsKeys.Facebook_DashBoard_Favourites_Click_Inter, remoteConfig.getBoolean(
                    AdsKeys.Facebook_DashBoard_Favourites_Click_Inter
                ))
            SharePref.putBoolean(AdsKeys.Facebook_Favourites_Native, remoteConfig.getBoolean(AdsKeys.Facebook_Favourites_Native))
            SharePref.putBoolean(AdsKeys.Facebook_Exit_Native, remoteConfig.getBoolean(AdsKeys.Facebook_Exit_Native))
            SharePref.putBoolean(AdsKeys.Facebook_Exit_Inter, remoteConfig.getBoolean(AdsKeys.Facebook_Exit_Inter))

            SharePref.putBoolean(AdsKeys.Facebook_TrimBtnClick_Inter, remoteConfig.getBoolean(AdsKeys.Facebook_TrimBtnClick_Inter))
            SharePref.putBoolean(AdsKeys.Facebook_TrimmerBack_Inter, remoteConfig.getBoolean(AdsKeys.Facebook_TrimmerBack_Inter))

            // Applovin keys
            SharePref.putBoolean(AdsKeys.Applovin_Remove_Password_Dialog_Native, remoteConfig.getBoolean(AdsKeys.Applovin_Remove_Password_Dialog_Native))
            SharePref.putBoolean(AdsKeys.Applovin_Back_On_Dashboard_Inter, remoteConfig.getBoolean(AdsKeys.Applovin_Back_On_Dashboard_Inter))
            SharePref.putBoolean(AdsKeys.Applovin_Verify_Password_Native, remoteConfig.getBoolean(AdsKeys.Applovin_Verify_Password_Native))
            SharePref.putBoolean(AdsKeys.Applovin_Settings_Native, remoteConfig.getBoolean(AdsKeys.Applovin_Settings_Native))
            SharePref.putBoolean(AdsKeys.Applovin_Splash_Inter, remoteConfig.getBoolean(AdsKeys.Applovin_Splash_Inter))
            SharePref.putBoolean(AdsKeys.Applovin_Splash_Native, remoteConfig.getBoolean(AdsKeys.Applovin_Splash_Native))
            SharePref.putBoolean(AdsKeys.Applovin_DashBoard_Native, remoteConfig.getBoolean(AdsKeys.Applovin_DashBoard_Native))
            SharePref.putBoolean(AdsKeys.Applovin_DashBoard_Banner, remoteConfig.getBoolean(AdsKeys.Applovin_DashBoard_Banner))
            SharePref.putBoolean(
                AdsKeys.Applovin_SetPassword_Screen_Native, remoteConfig.getBoolean(
                    AdsKeys.Applovin_SetPassword_Screen_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Applovin_SetPassword_Screen_One_Native, remoteConfig.getBoolean(
                    AdsKeys.Applovin_SetPassword_Screen_One_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Applovin_SetPassword_Screen_Two_Native, remoteConfig.getBoolean(
                    AdsKeys.Applovin_SetPassword_Screen_Two_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Applovin_Save_Password_Screen_Native, remoteConfig.getBoolean(
                    AdsKeys.Applovin_Save_Password_Screen_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Applovin_Save_Password_Screen_Inter, remoteConfig.getBoolean(
                    AdsKeys.Applovin_Save_Password_Screen_Inter
                ))
            SharePref.putBoolean(
                AdsKeys.Applovin_Forget_Password_Screen_Native, remoteConfig.getBoolean(
                    AdsKeys.Applovin_Forget_Password_Screen_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Applovin_Video_Saving_Screen_Native, remoteConfig.getBoolean(
                    AdsKeys.Applovin_Video_Saving_Screen_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Applovin_Rename_Screen_Native, remoteConfig.getBoolean(
                    AdsKeys.Applovin_Rename_Screen_Native
                ))
            SharePref.putBoolean(
                AdsKeys.Applovin_DashBoard_Trim_Video_Click_Inter, remoteConfig.getBoolean(
                    AdsKeys.Applovin_DashBoard_Trim_Video_Click_Inter
                ))
            SharePref.putBoolean(AdsKeys.Applovin_Trim_Video_Native, remoteConfig.getBoolean(AdsKeys.Applovin_Trim_Video_Native))
            SharePref.putBoolean(
                AdsKeys.Applovin_DashBoard_Recordings_Click_Inter, remoteConfig.getBoolean(
                    AdsKeys.Applovin_DashBoard_Recordings_Click_Inter
                ))
            SharePref.putBoolean(AdsKeys.Applovin_Recordings_Native, remoteConfig.getBoolean(AdsKeys.Applovin_Recordings_Native))
            SharePref.putBoolean(
                AdsKeys.Applovin_DashBoard_Favourites_Click_Inter, remoteConfig.getBoolean(
                    AdsKeys.Applovin_DashBoard_Favourites_Click_Inter
                ))
            SharePref.putBoolean(AdsKeys.Applovin_Favourites_Native, remoteConfig.getBoolean(AdsKeys.Applovin_Favourites_Native))
            SharePref.putBoolean(AdsKeys.Applovin_Exit_Native, remoteConfig.getBoolean(AdsKeys.Applovin_Exit_Native))
            SharePref.putBoolean(AdsKeys.Applovin_Exit_Inter, remoteConfig.getBoolean(AdsKeys.Applovin_Exit_Inter))

            SharePref.putBoolean(AdsKeys.Applovin_TrimBtnClick_Inter, remoteConfig.getBoolean(AdsKeys.Applovin_TrimBtnClick_Inter))
            SharePref.putBoolean(AdsKeys.Applovin_TrimmerBack_Inter, remoteConfig.getBoolean(AdsKeys.Applovin_TrimmerBack_Inter))

        }

        //
        fun setAdData(context: Context) {
            adData.inApp = SharePref.getBoolean(AdsKeys.InApp, false)
            adData.OpenAdId = SharePref.getString(AdsKeys.AdmobOpenAdId, "")
            adData.fbNativeId = SharePref.getString(AdsKeys.FaceBookNativeId, "")
            adData.fbInInterId = SharePref.getString(AdsKeys.FaceBookInterId, "")
            adData.fbBannerId = SharePref.getString(AdsKeys.FaceBookBannerId, "")

            adData.AdmobNativeId = SharePref.getString(AdsKeys.AdmobNativeId, "")
            adData.AdmobInterstitialId = SharePref.getString(AdsKeys.AdmobInterId, "")
            adData.AdmobBannerId = SharePref.getString(AdsKeys.AdmobBannerId, "")


            adData.isOpenAdEnabled = SharePref.getBoolean(AdsKeys.IsOpenAdEnable, true)
            adData.isAdmobEnabled = SharePref.getBoolean(AdsKeys.IsAdmobEnable, true)
            adData.isFbEnabled = SharePref.getBoolean(AdsKeys.IsFaceBookEnable, true)
        }
    }
}
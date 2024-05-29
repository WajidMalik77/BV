package com.background.video.recorder.camera.recorder.firebase.remoteconfig;

import android.content.Context;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class RemoteConfigManager {
    private static final RemoteConfigManager instance = new RemoteConfigManager();

    public static RemoteConfigManager getInstance() {
        return instance;
    }


    //////////////////// BACK PRESSED /////////////////////


    private static final String backpress_dashboard_interstitial_admob = "backpress_dashboard_interstitial_admob";
    private static final String backpress_imagerecovery_interstitial_admob = "backpress_imagerecovery_interstitial_admob";
    private static final String backpress_audiosRecovery_interstitial_admob = "backpress_audiosRecovery_interstitial_admob";
    private static final String backpress_videosRecovery_interstitial_admob = "backpress_videosRecovery_interstitial_admob";
    private static final String backpress_documentsRecovery_interstitial_admob = "backpress_documentsRecovery_interstitial_admob";
    private static final String backpress_processing_interstitial_admob = "backpress_processing_interstitial_admob";
    private static final String backpress_recoveredFiles_interstitial_admob = "backpress_recoveredFiles_interstitial_admob";
    private static final String backpress_successFull_interstitial_admob = "backpress_successFull_interstitial_admob";
    private static final String backpress_fileShredder_interstitial_admob = "backpress_fileShredder_interstitial_admob";
    private static final String facebook_banner_enable = "facebook_banner_enable";
    private static final String screenrecordinggalleryfacebook_native = "screenrecordinggalleryfacebook_native";
    private static final String screenrecordinggalleryadmob_native = "screenrecordinggalleryadmob_native";
    private static final String language_admob_native = "language_admob_native";
    private static final String language_interstitial = "language_interstitial";
    private static final String language_facebook_native = "language_facebook_native";
    private static final String asking_password_setpassword_admob_interstitial = "asking_password_setpassword_admob_interstitial";
    private static final String asking_password_notnow_admob_interstitial = "asking_password_notnow_admob_interstitial";



    ///////////////////////////////////////////////////////
    private static final String SPLASH_ADMOB = "splash_admob";
    private static final String splash_facebook = "splash_facebook";
    private static final String splash_facebook_native = "splash_facebook_native";
    private static final String splash_admob_native = "splash_admob_native";
    private static final String saved_files_in_gallery_admob_native = "saved_files_in_gallery_admob_native";
    private static final String saved_files_in_gallery_facebook_native = "saved_files_in_gallery_facebook_native";
    private static final String verify_password_admob_native = "verify_password_admob_native";
    private static final String verify_password_facebook_native = "verify_password_facebook_native";
    private static final String asking_password_admob_native = "asking_password_admob_native";
    private static final String asking_password_facebook_native = "asking_password_facebook_native";
    private static final String exit_admob_native = "exit_admob_native";
    private static final String exit_facebook_native = "exit_facebook_native";
    private static final String exit_interstitial_admob = "exit_interstitial_admob";
    private static final String exit_interstitial_facebook  = "exit_interstitial_facebook";
    private static final String fav_files_admob_native  = "fav_files_admob_native";
    private static final String fav_files_facebook_native  = "fav_files_facebook_native";
    private static final String trim_admob_native  = "trim_admob_native";
    private static final String trim_facebook_native  = "trim_facebook_native";
    private static final String rename_dailog_admob_native  = "rename_dailog_admob_native";
    private static final String rename_dailog_facebook_native  = "rename_dailog_facebook_native";
    private static final String overlay_permission_admob_native  = "overlay_permission_admob_native";
    private static final String overlay_permission_facebook_native  = "overlay_permission_facebook_native";
    private static final String saved_successfull_admob_native  = "saved_successfull_admob_native";
    private static final String saved_successfull_facebook_native  = "saved_successfull_facebook_native";
    private static final String setuserpassword_admob_native  = "setuserpassword_admob_native";
    private static final String setuserpassword_facebook_native  = "setuserpassword_facebook_native";
    private static final String sahre_activity_admob_native  = "sahre_activity_admob_native";
    private static final String sahre_activity_admob_inter  = "sahre_activity_admob_inter";
    private static final String sahre_activity_admob_album  = "sahre_activity_admob_album";
    private static final String sahre_activity_admob_home  = "sahre_activity_admob_home";
    private static final String sahre_activity_facebook_native  = "sahre_activity_facebook_native";
    private static final String files_video_preview_admob_native  = "files_video_preview_admob_native";
    private static final String files_video_preview_facebook_native  = "files_video_preview_facebook_native";
    private static final String remove_password_admob_native  = "remove_password_admob_native";
    private static final String remove_password_facebook_native  = "remove_password_facebook_native";
    private static final String photoeditorcollagemaker_home_admob_native  = "photoeditorcollagemaker_home_admob_native";
    private static final String photoeditorcollagemaker_home_admob_inter  = "photoeditorcollagemaker_home_admob_inter";
    private static final String photoeditorcollagemaker_admob_native  = "photoeditorcollagemaker_admob_native";

    private static final String photoeditorcollagemaker_facebook_native  = "photoeditorcollagemaker_facebook_native";
    private static final String sahre_activity_album_admob_interstitial  = "sahre_activity_album_admob_interstitial";


    private static final String pre_home_facebook_collage_inter = "pre_home_facebook_collage_inter";
    private static final String emoji_admob_native = "emoji_admob_native";
    private static final String emoji_facebook_native = "emoji_facebook_native";
    private static final String sticker_facebook_native = "sticker_facebook_native";
    private static final String sticker_admob_native = "sticker_admob_native";
    private static final String pre_home_admob_collage_inter = "pre_home_admob_collage_inter";
    private static final String pre_home_facebook_home_inter = "pre_home_facebook_home_inter";
    private static final String pre_home_admob_home_inter = "pre_home_admob_home_inter";
    private static final String home_admob_home_inter = "home_admob_home_inter";
    private static final String admob_album_inter = "admob_album_inter";
    private static final String pre_home_admob_SR_inter = "pre_home_admob_SR_inter";
    private static final String pre_home_facebook_SR_inter = "pre_home_facebook_SR_inter";
    private static final String admob_banner_enable = "admob_banner_enable";


    private static final String pre_home_admob_native = "pre_home_admob_native";
    private static final String pre_home_facebook_native = "pre_home_facebook_native";
    private static final String album_native_admob = "album_native_admob";
    private static final String album_native_facebook = "album_native_facebook";
    private static final String imageview_native_admob = "imageview_native_admob";
    private static final String imageview_native_facebook = "imageview_native_facebook";


    private static final String admob_interstitial_splash_id = "admob_interstitial_splash_id";
    private static final String onboarding_native_admob_id = "admob_interstitial_splash_idonboarding_native_admob_id";
    private static final String facebook_interstitial_splash_id = "facebook_interstitial_splash_id";
































    ////////////////////////////////////////////////////////////////////////////


    private static final String language_native_admob = "language_native_admob";
    private static final String language_native_facebook = "language_native_facebook";
    private static final String onboarding_native = "onboarding_native";
    private static final String onbaording_native_facebook = "onbaording_native_facebook";
    private static final String dashboard_native_facebook = "dashboard_native_facebook";
    private static final String dashboard_native = "dashboard_native";
    private static final String user_consent_form = "user_consent_form";
    private static final String image_recovery_native = "image_recovery_native";
    private static final String image_recovery_native_facebook = "image_recovery_native_facebook";
    private static final String video_recovery_native = "video_recovery_native";
    private static final String audio_recovery_native = "audio_recovery_native";
    private static final String audio_recovery_native_facebook = "audio_recovery_native_facebook";
    private static final String document_recovery_native = "document_recovery_native";
    private static final String document_recovery_native_facebook = "document_recovery_native_facebook";
    private static final String recovered_files_image = "recovered_files_image";
    private static final String recovered_files_image_native_facebook = "recovered_files_image_native_facebook";
    private static final String recovered_files_video = "recovered_files_video";
    private static final String recovered_files_video_native_facebook = "recovered_files_video_native_facebook";
    private static final String recovered_files_document_native = "recovered_files_document_native";
    private static final String recovered_files_document_native_facebook = "recovered_files_document_native_facebook";
    private static final String recovered_files_audio = "recovered_files_audio";
    private static final String recovered_files_audio_native_facebook = "recovered_files_audio_native_facebook";
    private static final String processing_native = "processing_native";
    private static final String processing_native_facebook = "processing_native_facebook";
    private static final String successful_native = "successful_native";
    private static final String successful_native_facebook = "successful_native_facebook";
    private static final String file_shredder_native = "file_shredder_native";
    private static final String file_shredder_native_facebook = "file_shredder_native_facebook";
    private static final String file_shredder_image = "file_shredder_image";
    private static final String file_shredder_image_native_facebook = "file_shredder_image_native_facebook";
    private static final String file_shredder_video = "file_shredder_video";
    private static final String file_shredder_video_native_facebook = "file_shredder_video_native_facebook";
    private static final String file_shredder_audio = "file_shredder_audio";
    private static final String file_shredder_audio_native_facebook = "file_shredder_audio_native_facebook";
    private static final String file_shredder_document = "file_shredder_document";
    private static final String file_shredder_document_native_facebook = "file_shredder_document_native_facebook";
    private static final String exit_native = "exit_native";
    private static final String exit_native_facebook = "exit_native_facebook";
    private static final String exitDailogNativeFacebook = "exitDailogNativeFacebook";
    private static final String onboarding_interstitial = "onboarding_interstitial";
    private static final String dashboard_interstitial = "dashboard_interstitial";
    private static final String image_recovery_interstitial = "image_recovery_interstitial";
    private static final String successful_recovery_interstitial = "successful_recovery_interstitial";
    private static final String video_recovery_interstitial = "video_recovery_interstitial";
    private static final String audio_recovery_interstitial = "audio_recovery_interstitial";
    private static final String documents_recovery_interstitial = "documents_recovery_interstitial";
    private static final String is_Concent_FormEnable = "is_Concent_FormEnable";

    /////////////////////////////////////////String ID's
    private static final String IS_ADMOB_ENABLED = "isAdMobEnabled";
    private static final String TOAST_ENABLED = "toast_enabled";


    private static final String BANNER_SWITCH = "banner_switch";
    private static final String facebook_banner = "facebook_banner";
    private static final String APPOPEN_SWITCH = "appopen_switch";
    private static final String INTER_SWITCH = "inter_switch";
    private static final String NATIVE_SWITCH = "native_switch";
    private static final String INTER_SPLASH = "inter_splash";
    private static final String REWARDED_AD = "rewarded_ad";
    private static final String BANNER_AD_ID = "banner_ad_id";
    private static final String APPOPEN_AD_ID = "appopen_ad_id";
    private static final String INTER_AD_ID = "inter_ad_id";
    private static final String NATIVE_AD_ID = "native_ad_id";


    ////////////////////////////////////  IDS  //////////////////////////////////
    private static final String INTER_SPLASH_AD_ID = "inter_splash_ad_id";
    private static final String INTER_EXIT_AD_ID = "inter_exit_ad_id";

    private static final String admob_interstitial_home_id = "admob_interstitial_home_id";
    private static final String photoeditorcollagemaker_home_admob_inter_id = "photoeditorcollagemaker_home_admob_inter_id";
    private static final String admob_native_id = "admob_native_id";
    private static final String language_admob_native_id = "language_admob_native_id";
    private static final String language_interstitial_admob_id = "language_interstitial_admob_id";
    private static final String facebook_native_ad_id = "facebook_native_ad_id";
    private static final String admob_banner_id = "admob_banner_id";
    private static final String facebook_banner_ad_id = "facebook_banner_ad_id";





    /////////////////////////////////////////////////////////////////////
    private static final String INTER_EXIT_AD_SWITCH = "inter_exit_ad_switch";
    private static final String REWARDED_AD_ID = "rewarded_ad_id";
    private static final String GDPR_FORM = "GDPR_FORM";


    // New Constants
    private static final String REWARDED_AD_WATERMARK_SWITCH = "rewarded_watermark_switch";
    private static final String REWARDED_AD_WATERMARK_ID = "rewarded_watermark";
    private static final String REWARDED_AD_SAVE_SWITCH = "rewarded_ad_save_switch";
    private static final String REWARDED_AD_SAVE_ID = "rewarded_ad_save_id";
    private static final String INTERSTITIAL_UPLOAD_SWITCH = "interstitial_upload_switch";
    private static final String INTERSTITIAL_UPLOAD_ID = "interstitial_upload_id";
    private static final String NATIVE_ADS_SWITCH = "native_ad_switch";
    private static final String NATIVE_ADS_ID = "native_ad_id";


    public void fetchRemoteConfigValues(Context context) {
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SharedPrefsHelper prefs = SharedPrefsHelper.getInstance(context);


                /////////////////////////////BACK PRESSED //////////////////////////////////



                prefs.putBoolean(backpress_dashboard_interstitial_admob, firebaseRemoteConfig.getBoolean(backpress_dashboard_interstitial_admob));
                prefs.putBoolean(backpress_imagerecovery_interstitial_admob, firebaseRemoteConfig.getBoolean(backpress_imagerecovery_interstitial_admob));
                prefs.putBoolean(backpress_audiosRecovery_interstitial_admob, firebaseRemoteConfig.getBoolean(backpress_audiosRecovery_interstitial_admob));
                prefs.putBoolean(backpress_videosRecovery_interstitial_admob, firebaseRemoteConfig.getBoolean(backpress_videosRecovery_interstitial_admob));
                prefs.putBoolean(backpress_documentsRecovery_interstitial_admob, firebaseRemoteConfig.getBoolean(backpress_documentsRecovery_interstitial_admob));
                prefs.putBoolean(backpress_processing_interstitial_admob, firebaseRemoteConfig.getBoolean(backpress_processing_interstitial_admob));
                prefs.putBoolean(backpress_recoveredFiles_interstitial_admob, firebaseRemoteConfig.getBoolean(backpress_recoveredFiles_interstitial_admob));
                prefs.putBoolean(backpress_successFull_interstitial_admob, firebaseRemoteConfig.getBoolean(backpress_successFull_interstitial_admob));
                prefs.putBoolean(backpress_fileShredder_interstitial_admob, firebaseRemoteConfig.getBoolean(backpress_fileShredder_interstitial_admob));
                prefs.putBoolean(facebook_banner_enable, firebaseRemoteConfig.getBoolean(facebook_banner_enable));
                prefs.putBoolean(screenrecordinggalleryadmob_native, firebaseRemoteConfig.getBoolean(screenrecordinggalleryadmob_native));
                prefs.putBoolean(screenrecordinggalleryfacebook_native, firebaseRemoteConfig.getBoolean(screenrecordinggalleryfacebook_native));
                prefs.putBoolean(language_admob_native, firebaseRemoteConfig.getBoolean(language_admob_native));
                prefs.putBoolean(language_interstitial, firebaseRemoteConfig.getBoolean(language_interstitial));
                prefs.putBoolean(language_facebook_native, firebaseRemoteConfig.getBoolean(language_facebook_native));
                prefs.putBoolean(asking_password_setpassword_admob_interstitial, firebaseRemoteConfig.getBoolean(asking_password_setpassword_admob_interstitial));
                prefs.putBoolean(asking_password_notnow_admob_interstitial, firebaseRemoteConfig.getBoolean(asking_password_notnow_admob_interstitial));
                ///////////////////////////////////////////////////////////////////////////

                prefs.putBoolean(SPLASH_ADMOB, firebaseRemoteConfig.getBoolean(SPLASH_ADMOB));
                prefs.putBoolean(splash_facebook, firebaseRemoteConfig.getBoolean(splash_facebook));
                prefs.putBoolean(splash_admob_native, firebaseRemoteConfig.getBoolean(splash_admob_native));
                prefs.putBoolean(splash_facebook_native, firebaseRemoteConfig.getBoolean(splash_facebook_native));
                prefs.putBoolean(saved_files_in_gallery_admob_native, firebaseRemoteConfig.getBoolean(saved_files_in_gallery_admob_native));
                prefs.putBoolean(saved_files_in_gallery_facebook_native, firebaseRemoteConfig.getBoolean(saved_files_in_gallery_facebook_native));
                prefs.putBoolean(verify_password_admob_native, firebaseRemoteConfig.getBoolean(verify_password_admob_native));
                prefs.putBoolean(verify_password_facebook_native, firebaseRemoteConfig.getBoolean(verify_password_facebook_native));
                prefs.putBoolean(asking_password_admob_native, firebaseRemoteConfig.getBoolean(asking_password_admob_native));
                prefs.putBoolean(asking_password_facebook_native, firebaseRemoteConfig.getBoolean(asking_password_facebook_native));
                prefs.putBoolean(exit_admob_native, firebaseRemoteConfig.getBoolean(exit_admob_native));
                prefs.putBoolean(exit_facebook_native, firebaseRemoteConfig.getBoolean(exit_facebook_native));
                prefs.putBoolean(exit_interstitial_admob, firebaseRemoteConfig.getBoolean(exit_interstitial_admob));
                prefs.putBoolean(exit_interstitial_facebook, firebaseRemoteConfig.getBoolean(exit_interstitial_facebook));
                prefs.putBoolean(fav_files_admob_native, firebaseRemoteConfig.getBoolean(fav_files_admob_native));
                prefs.putBoolean(fav_files_facebook_native, firebaseRemoteConfig.getBoolean(fav_files_facebook_native));
                prefs.putBoolean(trim_admob_native, firebaseRemoteConfig.getBoolean(trim_admob_native));
                prefs.putBoolean(trim_facebook_native, firebaseRemoteConfig.getBoolean(trim_facebook_native));
                prefs.putBoolean(rename_dailog_admob_native, firebaseRemoteConfig.getBoolean(rename_dailog_admob_native));
                prefs.putBoolean(rename_dailog_facebook_native, firebaseRemoteConfig.getBoolean(rename_dailog_facebook_native));
                prefs.putBoolean(overlay_permission_admob_native, firebaseRemoteConfig.getBoolean(overlay_permission_admob_native));
                prefs.putBoolean(overlay_permission_facebook_native, firebaseRemoteConfig.getBoolean(overlay_permission_facebook_native));
                prefs.putBoolean(saved_successfull_admob_native, firebaseRemoteConfig.getBoolean(saved_successfull_admob_native));
                prefs.putBoolean(saved_successfull_facebook_native, firebaseRemoteConfig.getBoolean(saved_successfull_facebook_native));
                prefs.putBoolean(setuserpassword_admob_native, firebaseRemoteConfig.getBoolean(setuserpassword_admob_native));
                prefs.putBoolean(setuserpassword_facebook_native, firebaseRemoteConfig.getBoolean(setuserpassword_facebook_native));
                prefs.putBoolean(sahre_activity_admob_native, firebaseRemoteConfig.getBoolean(sahre_activity_admob_native));
                prefs.putBoolean(sahre_activity_admob_inter, firebaseRemoteConfig.getBoolean(sahre_activity_admob_inter));
                prefs.putBoolean(sahre_activity_admob_album, firebaseRemoteConfig.getBoolean(sahre_activity_admob_album));


                prefs.putBoolean(sahre_activity_admob_home, firebaseRemoteConfig.getBoolean(sahre_activity_admob_home));


                prefs.putBoolean(sahre_activity_facebook_native, firebaseRemoteConfig.getBoolean(sahre_activity_facebook_native));
                prefs.putBoolean(files_video_preview_admob_native, firebaseRemoteConfig.getBoolean(files_video_preview_admob_native));
                prefs.putBoolean(files_video_preview_facebook_native, firebaseRemoteConfig.getBoolean(files_video_preview_facebook_native));
                prefs.putBoolean(remove_password_admob_native, firebaseRemoteConfig.getBoolean(remove_password_admob_native));
                prefs.putBoolean(remove_password_facebook_native, firebaseRemoteConfig.getBoolean(remove_password_facebook_native));
                prefs.putBoolean(photoeditorcollagemaker_home_admob_native, firebaseRemoteConfig.getBoolean(photoeditorcollagemaker_home_admob_native));
                prefs.putBoolean(photoeditorcollagemaker_home_admob_inter, firebaseRemoteConfig.getBoolean(photoeditorcollagemaker_home_admob_inter));
                prefs.putBoolean(photoeditorcollagemaker_admob_native, firebaseRemoteConfig.getBoolean(photoeditorcollagemaker_admob_native));
                prefs.putBoolean(photoeditorcollagemaker_facebook_native, firebaseRemoteConfig.getBoolean(photoeditorcollagemaker_facebook_native));
                prefs.putBoolean(sahre_activity_album_admob_interstitial, firebaseRemoteConfig.getBoolean(sahre_activity_album_admob_interstitial));



                prefs.putBoolean(emoji_admob_native, firebaseRemoteConfig.getBoolean(emoji_admob_native));
                prefs.putBoolean(emoji_facebook_native, firebaseRemoteConfig.getBoolean(emoji_facebook_native));
                prefs.putBoolean(sticker_admob_native, firebaseRemoteConfig.getBoolean(sticker_admob_native));
                prefs.putBoolean(sticker_facebook_native, firebaseRemoteConfig.getBoolean(sticker_facebook_native));
                prefs.putBoolean(pre_home_facebook_collage_inter, firebaseRemoteConfig.getBoolean(pre_home_facebook_collage_inter));
                prefs.putBoolean(pre_home_admob_collage_inter, firebaseRemoteConfig.getBoolean(pre_home_admob_collage_inter));
                prefs.putBoolean(pre_home_facebook_home_inter, firebaseRemoteConfig.getBoolean(pre_home_facebook_home_inter));
                prefs.putBoolean(pre_home_admob_home_inter, firebaseRemoteConfig.getBoolean(pre_home_admob_home_inter));
                prefs.putBoolean(home_admob_home_inter, firebaseRemoteConfig.getBoolean(home_admob_home_inter));
                prefs.putBoolean(admob_album_inter, firebaseRemoteConfig.getBoolean(admob_album_inter));
                prefs.putBoolean(pre_home_admob_SR_inter, firebaseRemoteConfig.getBoolean(pre_home_admob_SR_inter));
                prefs.putBoolean(pre_home_facebook_SR_inter, firebaseRemoteConfig.getBoolean(pre_home_facebook_SR_inter));
                prefs.putBoolean(admob_banner_enable, firebaseRemoteConfig.getBoolean(admob_banner_enable));
                prefs.putBoolean(pre_home_admob_native, firebaseRemoteConfig.getBoolean(pre_home_admob_native));
                prefs.putBoolean(pre_home_facebook_native, firebaseRemoteConfig.getBoolean(pre_home_facebook_native));
                prefs.putBoolean(album_native_admob, firebaseRemoteConfig.getBoolean(album_native_admob));
                prefs.putBoolean(imageview_native_admob, firebaseRemoteConfig.getBoolean(imageview_native_admob));
                prefs.putBoolean(imageview_native_facebook, firebaseRemoteConfig.getBoolean(imageview_native_facebook));
                prefs.putBoolean(album_native_facebook, firebaseRemoteConfig.getBoolean(album_native_facebook));
                prefs.putBoolean(language_native_admob, firebaseRemoteConfig.getBoolean(language_native_admob));
                prefs.putBoolean(language_native_facebook, firebaseRemoteConfig.getBoolean(language_native_facebook));
                prefs.putBoolean(language_interstitial, firebaseRemoteConfig.getBoolean(language_interstitial));
                prefs.putBoolean(onboarding_native, firebaseRemoteConfig.getBoolean(onboarding_native));
                prefs.putBoolean(onbaording_native_facebook, firebaseRemoteConfig.getBoolean(onbaording_native_facebook));
                prefs.putBoolean(dashboard_native_facebook, firebaseRemoteConfig.getBoolean(dashboard_native_facebook));
                prefs.putBoolean(dashboard_native, firebaseRemoteConfig.getBoolean(dashboard_native));
                prefs.putBoolean(user_consent_form, firebaseRemoteConfig.getBoolean(user_consent_form));
                prefs.putBoolean(image_recovery_native, firebaseRemoteConfig.getBoolean(image_recovery_native));
                prefs.putBoolean(image_recovery_native_facebook, firebaseRemoteConfig.getBoolean(image_recovery_native_facebook));
                prefs.putBoolean(video_recovery_native, firebaseRemoteConfig.getBoolean(video_recovery_native));
                prefs.putBoolean(audio_recovery_native, firebaseRemoteConfig.getBoolean(audio_recovery_native));
                prefs.putBoolean(audio_recovery_native_facebook, firebaseRemoteConfig.getBoolean(audio_recovery_native_facebook));
                prefs.putBoolean(document_recovery_native, firebaseRemoteConfig.getBoolean(document_recovery_native));
                prefs.putBoolean(document_recovery_native_facebook, firebaseRemoteConfig.getBoolean(document_recovery_native_facebook));
                prefs.putBoolean(recovered_files_image, firebaseRemoteConfig.getBoolean(recovered_files_image));
                prefs.putBoolean(recovered_files_image_native_facebook, firebaseRemoteConfig.getBoolean(recovered_files_image_native_facebook));
                prefs.putBoolean(recovered_files_video, firebaseRemoteConfig.getBoolean(recovered_files_video));
                prefs.putBoolean(recovered_files_video_native_facebook, firebaseRemoteConfig.getBoolean(recovered_files_video_native_facebook));
                prefs.putBoolean(recovered_files_document_native, firebaseRemoteConfig.getBoolean(recovered_files_document_native));
                prefs.putBoolean(recovered_files_document_native_facebook, firebaseRemoteConfig.getBoolean(recovered_files_document_native_facebook));
                prefs.putBoolean(recovered_files_audio, firebaseRemoteConfig.getBoolean(recovered_files_audio));
                prefs.putBoolean(recovered_files_audio_native_facebook, firebaseRemoteConfig.getBoolean(recovered_files_audio_native_facebook));
                prefs.putBoolean(processing_native, firebaseRemoteConfig.getBoolean(processing_native));
                prefs.putBoolean(processing_native_facebook, firebaseRemoteConfig.getBoolean(processing_native_facebook));
                prefs.putBoolean(successful_native, firebaseRemoteConfig.getBoolean(successful_native));
                prefs.putBoolean(successful_native_facebook, firebaseRemoteConfig.getBoolean(successful_native_facebook));
                prefs.putBoolean(file_shredder_native, firebaseRemoteConfig.getBoolean(file_shredder_native));
                prefs.putBoolean(file_shredder_native_facebook, firebaseRemoteConfig.getBoolean(file_shredder_native_facebook));
                prefs.putBoolean(file_shredder_image, firebaseRemoteConfig.getBoolean(file_shredder_image));
                prefs.putBoolean(file_shredder_image_native_facebook, firebaseRemoteConfig.getBoolean(file_shredder_image_native_facebook));
                prefs.putBoolean(file_shredder_video, firebaseRemoteConfig.getBoolean(file_shredder_video));
                prefs.putBoolean(file_shredder_video_native_facebook, firebaseRemoteConfig.getBoolean(file_shredder_video_native_facebook));
                prefs.putBoolean(file_shredder_audio, firebaseRemoteConfig.getBoolean(file_shredder_audio));
                prefs.putBoolean(file_shredder_audio_native_facebook, firebaseRemoteConfig.getBoolean(file_shredder_audio_native_facebook));
                prefs.putBoolean(file_shredder_document, firebaseRemoteConfig.getBoolean(file_shredder_document));
                prefs.putBoolean(file_shredder_document_native_facebook, firebaseRemoteConfig.getBoolean(file_shredder_document_native_facebook));
                prefs.putBoolean(exit_native, firebaseRemoteConfig.getBoolean(exit_native));
                prefs.putBoolean(exit_native_facebook, firebaseRemoteConfig.getBoolean(exit_native_facebook));
                prefs.putBoolean(exitDailogNativeFacebook, firebaseRemoteConfig.getBoolean(exitDailogNativeFacebook));
                prefs.putBoolean(onboarding_interstitial, firebaseRemoteConfig.getBoolean(onboarding_interstitial));
                prefs.putBoolean(dashboard_interstitial, firebaseRemoteConfig.getBoolean(dashboard_interstitial));
                prefs.putBoolean(image_recovery_interstitial, firebaseRemoteConfig.getBoolean(image_recovery_interstitial));
                prefs.putBoolean(successful_recovery_interstitial, firebaseRemoteConfig.getBoolean(successful_recovery_interstitial));
                prefs.putBoolean(video_recovery_interstitial, firebaseRemoteConfig.getBoolean(video_recovery_interstitial));
                prefs.putBoolean(audio_recovery_interstitial, firebaseRemoteConfig.getBoolean(audio_recovery_interstitial));
                prefs.putBoolean(documents_recovery_interstitial, firebaseRemoteConfig.getBoolean(documents_recovery_interstitial));
                prefs.putBoolean(is_Concent_FormEnable, firebaseRemoteConfig.getBoolean(is_Concent_FormEnable));



                //////////////////////////////
                // Save switches to prefs
                prefs.putBoolean(BANNER_SWITCH, firebaseRemoteConfig.getBoolean(BANNER_SWITCH));
                prefs.putBoolean(facebook_banner, firebaseRemoteConfig.getBoolean(facebook_banner));
                prefs.putBoolean(APPOPEN_SWITCH, firebaseRemoteConfig.getBoolean(APPOPEN_SWITCH));
                prefs.putBoolean(INTER_SWITCH, firebaseRemoteConfig.getBoolean(INTER_SWITCH));
                prefs.putBoolean(NATIVE_SWITCH, firebaseRemoteConfig.getBoolean(NATIVE_SWITCH));
                prefs.putBoolean(INTER_SPLASH, firebaseRemoteConfig.getBoolean(INTER_SPLASH));
                prefs.putBoolean(REWARDED_AD, firebaseRemoteConfig.getBoolean(REWARDED_AD));
                prefs.putBoolean(GDPR_FORM, firebaseRemoteConfig.getBoolean(GDPR_FORM));
                prefs.putBoolean(INTER_EXIT_AD_SWITCH, firebaseRemoteConfig.getBoolean(INTER_EXIT_AD_SWITCH));
                prefs.putBoolean(IS_ADMOB_ENABLED, firebaseRemoteConfig.getBoolean(IS_ADMOB_ENABLED));
                prefs.putBoolean(TOAST_ENABLED, firebaseRemoteConfig.getBoolean(TOAST_ENABLED));


                // Save Ad IDs to prefs
                prefs.putString(BANNER_AD_ID, firebaseRemoteConfig.getString(BANNER_AD_ID));
                prefs.putString(APPOPEN_AD_ID, firebaseRemoteConfig.getString(APPOPEN_AD_ID));
                prefs.putString(INTER_AD_ID, firebaseRemoteConfig.getString(INTER_AD_ID));
                prefs.putString(NATIVE_AD_ID, firebaseRemoteConfig.getString(NATIVE_AD_ID));
                prefs.putString(INTER_SPLASH_AD_ID, firebaseRemoteConfig.getString(INTER_SPLASH_AD_ID));
                prefs.putString(REWARDED_AD_ID, firebaseRemoteConfig.getString(REWARDED_AD_ID));
                prefs.putString(INTER_EXIT_AD_ID, firebaseRemoteConfig.getString(INTER_EXIT_AD_ID));




                /////////////////////////////////////////////////// IDS //////////////////////////


                prefs.putString(admob_interstitial_splash_id, firebaseRemoteConfig.getString(admob_interstitial_splash_id));
                prefs.putString(onboarding_native_admob_id, firebaseRemoteConfig.getString(onboarding_native_admob_id));
                prefs.putString(facebook_interstitial_splash_id, firebaseRemoteConfig.getString(facebook_interstitial_splash_id));
                prefs.putString(admob_interstitial_home_id, firebaseRemoteConfig.getString(admob_interstitial_home_id));
                prefs.putString(photoeditorcollagemaker_home_admob_inter_id, firebaseRemoteConfig.getString(photoeditorcollagemaker_home_admob_inter_id));
                prefs.putString(admob_native_id, firebaseRemoteConfig.getString(admob_native_id));
                prefs.putString(language_admob_native_id, firebaseRemoteConfig.getString(language_admob_native_id));
                prefs.putString(language_interstitial_admob_id, firebaseRemoteConfig.getString(language_interstitial_admob_id));
                prefs.putString(facebook_native_ad_id, firebaseRemoteConfig.getString(facebook_native_ad_id));
                prefs.putString(admob_banner_id, firebaseRemoteConfig.getString(admob_banner_id));
                prefs.putString(facebook_banner_ad_id, firebaseRemoteConfig.getString(facebook_banner_ad_id));

// Logs for switches
//                Log.d("SharedPrefs", "banner_switch: " + prefs.getBoolean(BANNER_SWITCH, true));
//                Log.d("SharedPrefs", "GDPR_FORM: " + prefs.getBoolean(GDPR_FORM, false));
//                Log.d("SharedPrefs", "appopen_switch: " + prefs.getBoolean(APPOPEN_SWITCH, false));
//                Log.d("SharedPrefs", "inter_switch: " + prefs.getBoolean(INTER_SWITCH, false));
//                Log.d("SharedPrefs", "native_switch: " + prefs.getBoolean(NATIVE_SWITCH, false));
//                Log.d("SharedPrefs", "inter_splash: " + prefs.getBoolean(INTER_SPLASH, false));
//                Log.d("SharedPrefs", "rewarded_ad: " + prefs.getBoolean(REWARDED_AD, false));
//                Log.d("SharedPrefs", "INTER_EXIT_AD_SWITCH: " + prefs.getBoolean(INTER_EXIT_AD_SWITCH, false));
//                Log.d("SharedPrefs", "IS_ADMOB_ENABLED: " + prefs.getBoolean(IS_ADMOB_ENABLED, false));
//                Log.d("SharedPrefs", "TOAST_ENABLED: " + prefs.getBoolean(IS_ADMOB_ENABLED, false));


                /////////////////////////////////// BACK PRESSED ?///////////////////////////////////////////////////////
                Log.d("SharedPrefs", "backpress_dashboard_interstitial_admob: " + prefs.getBoolean(backpress_dashboard_interstitial_admob, true));
                Log.d("SharedPrefs", "backpress_imagerecovery_interstitial_admob: " + prefs.getBoolean(backpress_imagerecovery_interstitial_admob, true));




                //////////////////////////////////////////////////////////////////////////////////////////////////////////
                Log.d("SharedPrefs", "SPLASH_ADMOB: " + prefs.getBoolean(SPLASH_ADMOB, true));
                Log.d("SharedPrefs", "splash_facebook: " + prefs.getBoolean(splash_facebook, true));
                Log.d("SharedPrefs", "pre_home_facebook_collage_inter: " + prefs.getBoolean(pre_home_facebook_collage_inter, true));
                Log.d("SharedPrefs", "pre_home_admob_collage_inter: " + prefs.getBoolean(pre_home_admob_collage_inter, true));
                Log.d("SharedPrefs", "pre_home_facebook_home_inter: " + prefs.getBoolean(pre_home_facebook_home_inter, true));
                Log.d("SharedPrefs", "pre_home_admob_home_inter: " + prefs.getBoolean(pre_home_admob_home_inter, true));
                Log.d("SharedPrefs", "home_admob_home_inter: " + prefs.getBoolean(home_admob_home_inter, true));
                Log.d("SharedPrefs", "pre_home_admob_SR_inter: " + prefs.getBoolean(pre_home_admob_SR_inter, true));
                Log.d("SharedPrefs", "pre_home_facebook_SR_inter: " + prefs.getBoolean(pre_home_facebook_SR_inter, true));
                Log.d("SharedPrefs", "pre_home_admob_native: " + prefs.getBoolean(pre_home_admob_native, true));
                Log.d("SharedPrefs", "pre_home_facebook_native: " + prefs.getBoolean(pre_home_facebook_native, true));
                Log.d("SharedPrefs", "language_native_admob: " + prefs.getBoolean(language_native_admob, true));
                Log.d("SharedPrefs", "language_native_facebook: " + prefs.getBoolean(language_native_facebook, true));
                Log.d("SharedPrefs", "language_interstitial: " + prefs.getBoolean(language_interstitial, true));
                Log.d("SharedPrefs", "onboarding_native: " + prefs.getBoolean(onboarding_native, true));
                Log.d("SharedPrefs", "onbaording_native_facebook" + prefs.getBoolean(onbaording_native_facebook, true));
                Log.d("SharedPrefs", "dashboard_native_facebook" + prefs.getBoolean(dashboard_native_facebook, true));
                Log.d("SharedPrefs", "dashboard_native: " + prefs.getBoolean(dashboard_native, true));
                Log.d("SharedPrefs", "image_recovery_native: " + prefs.getBoolean(image_recovery_native, true));
                Log.d("SharedPrefs", "image_recovery_native_facebook: " + prefs.getBoolean(image_recovery_native_facebook, true));
                Log.d("SharedPrefs", "video_recovery_native: " + prefs.getBoolean(video_recovery_native, true));
                Log.d("SharedPrefs", "audio_recovery_native: " + prefs.getBoolean(audio_recovery_native, true));
                Log.d("SharedPrefs", "audio_recovery_native_facebook: " + prefs.getBoolean(audio_recovery_native_facebook, true));
                Log.d("SharedPrefs", "document_recovery_native: " + prefs.getBoolean(document_recovery_native, true));
                Log.d("SharedPrefs", "document_recovery_native_facebook: " + prefs.getBoolean(document_recovery_native_facebook, true));
                Log.d("SharedPrefs", "recovered_files_image: " + prefs.getBoolean(recovered_files_image, true));
                Log.d("SharedPrefs", "recovered_files_image_native_facebook: " + prefs.getBoolean(recovered_files_image_native_facebook, true));
                Log.d("SharedPrefs", "recovered_files_video: " + prefs.getBoolean(recovered_files_video, true));
                Log.d("SharedPrefs", "recovered_files_video_native_facebook: " + prefs.getBoolean(recovered_files_video_native_facebook, true));
                Log.d("SharedPrefs", "recovered_files_document_native: " + prefs.getBoolean(recovered_files_document_native, true));
                Log.d("SharedPrefs", "recovered_files_document_native_facebook: " + prefs.getBoolean(recovered_files_document_native_facebook, true));
                Log.d("SharedPrefs", "recovered_files_audio: " + prefs.getBoolean(recovered_files_audio, true));
                Log.d("SharedPrefs", "recovered_files_audio_native_facebook: " + prefs.getBoolean(recovered_files_audio_native_facebook, true));
                Log.d("SharedPrefs", "processing_native: " + prefs.getBoolean(processing_native, true));
                Log.d("SharedPrefs", "processing_native_facebook: " + prefs.getBoolean(processing_native_facebook, true));
                Log.d("SharedPrefs", "successful_native: " + prefs.getBoolean(successful_native, true));
                Log.d("SharedPrefs", "successful_native_facebook: " + prefs.getBoolean(successful_native_facebook, true));
                Log.d("SharedPrefs", "file_shredder_native: " + prefs.getBoolean(file_shredder_native, true));
                Log.d("SharedPrefs", "file_shredder_native_facebook: " + prefs.getBoolean(file_shredder_native_facebook, true));
                Log.d("SharedPrefs", "file_shredder_image: " + prefs.getBoolean(file_shredder_image, true));
                Log.d("SharedPrefs", "file_shredder_image_native_facebook: " + prefs.getBoolean(file_shredder_image_native_facebook, true));
                Log.d("SharedPrefs", "file_shredder_video: " + prefs.getBoolean(file_shredder_video, true));
                Log.d("SharedPrefs", "file_shredder_video_native_facebook: " + prefs.getBoolean(file_shredder_video_native_facebook, true));
                Log.d("SharedPrefs", "file_shredder_audio: " + prefs.getBoolean(file_shredder_audio, true));
                Log.d("SharedPrefs", "file_shredder_audio_native_facebook: " + prefs.getBoolean(file_shredder_audio_native_facebook, true));
                Log.d("SharedPrefs", "file_shredder_document: " + prefs.getBoolean(file_shredder_document, true));
                Log.d("SharedPrefs", "file_shredder_document_native_facebook: " + prefs.getBoolean(file_shredder_document_native_facebook, true));
                Log.d("SharedPrefs", "exit_native: " + prefs.getBoolean(exit_native, true));
                Log.d("SharedPrefs", "exit_native_facebook: " + prefs.getBoolean(exit_native_facebook, true));
                Log.d("SharedPrefs", "exitDailogNativeFacebook: " + prefs.getBoolean(exitDailogNativeFacebook, true));
                Log.d("SharedPrefs", "onboarding_interstitial: " + prefs.getBoolean(onboarding_interstitial, true));
                Log.d("SharedPrefs", "image_recovery_interstitial: " + prefs.getBoolean(image_recovery_interstitial, true));
                Log.d("SharedPrefs", "successful_recovery_interstitial: " + prefs.getBoolean(successful_recovery_interstitial, true));
                Log.d("SharedPrefs", "video_recovery_interstitial: " + prefs.getBoolean(video_recovery_interstitial, true));
                Log.d("SharedPrefs", "audio_recovery_interstitial: " + prefs.getBoolean(audio_recovery_interstitial, true));
                Log.d("SharedPrefs", "documents_recovery_interstitial: " + prefs.getBoolean(documents_recovery_interstitial, true));
                Log.d("SharedPrefs", "is_Concent_FormEnable: " + prefs.getBoolean(is_Concent_FormEnable, true));


                Log.d("SharedPrefs", "banner_switch: " + prefs.getBoolean(BANNER_SWITCH, true));
                Log.d("SharedPrefs", "facebook_banner: " + prefs.getBoolean(facebook_banner, true));
                Log.d("SharedPrefs", "GDPR_FORM: " + prefs.getBoolean(GDPR_FORM, true));
                Log.d("SharedPrefs", "appopen_switch: " + prefs.getBoolean(APPOPEN_SWITCH, true));
                Log.d("SharedPrefs", "inter_switch: " + prefs.getBoolean(INTER_SWITCH, true));
                Log.d("SharedPrefs", "native_switch: " + prefs.getBoolean(NATIVE_SWITCH, true));
                Log.d("SharedPrefs", "inter_splash: " + prefs.getBoolean(INTER_SPLASH, true));
                Log.d("SharedPrefs", "rewarded_ad: " + prefs.getBoolean(REWARDED_AD, true));
                Log.d("SharedPrefs", "INTER_EXIT_AD_SWITCH: " + prefs.getBoolean(INTER_EXIT_AD_SWITCH, true));
                Log.d("SharedPrefs", "IS_ADMOB_ENABLED: " + prefs.getBoolean(IS_ADMOB_ENABLED, true));
                Log.d("SharedPrefs", "TOAST_ENABLED: " + prefs.getBoolean(IS_ADMOB_ENABLED, true));

// Logs for Ad IDs
                Log.d("SharedPrefs", "banner_ad_id: " + prefs.getString(BANNER_AD_ID, ""));
                Log.d("SharedPrefs", "appopen_ad_id: " + prefs.getString(APPOPEN_AD_ID, ""));
                Log.d("SharedPrefs", "inter_ad_id: " + prefs.getString(INTER_AD_ID, ""));
                Log.d("SharedPrefs", "native_ad_id: " + prefs.getString(NATIVE_AD_ID, ""));
                Log.d("SharedPrefs", "inter_splash_ad_id: " + prefs.getString(INTER_SPLASH_AD_ID, ""));
                Log.d("SharedPrefs", "rewarded_ad_id: " + prefs.getString(REWARDED_AD_ID, ""));
                Log.d("SharedPrefs", "INTER_EXIT_AD_ID: " + prefs.getString(INTER_EXIT_AD_ID, ""));
                Log.d("SharedPrefs", "admob_interstitial_splash_id: " + prefs.getString(admob_interstitial_splash_id, ""));
                Log.d("SharedPrefs", "facebook_interstitial_splash_id: " + prefs.getString(facebook_interstitial_splash_id, ""));
                Log.d("SharedPrefs", "admob_interstitial_home_id: " + prefs.getString(admob_interstitial_home_id, ""));
                Log.d("SharedPrefs", "admob_native_id: " + prefs.getString(admob_native_id, ""));
                Log.d("SharedPrefs", "facebook_native_ad_id: " + prefs.getString(facebook_native_ad_id, ""));
                Log.d("SharedPrefs", "admob_banner_id: " + prefs.getString(admob_banner_id, ""));
                Log.d("SharedPrefs", "facebook_banner_ad_id: " + prefs.getString(facebook_banner_ad_id, ""));


                // New code for saving and logging the new switch and ID values
                prefs.putBoolean(REWARDED_AD_WATERMARK_SWITCH, firebaseRemoteConfig.getBoolean(REWARDED_AD_WATERMARK_SWITCH));
                prefs.putBoolean(REWARDED_AD_SAVE_SWITCH, firebaseRemoteConfig.getBoolean(REWARDED_AD_SAVE_SWITCH));
                prefs.putBoolean(INTERSTITIAL_UPLOAD_SWITCH, firebaseRemoteConfig.getBoolean(INTERSTITIAL_UPLOAD_SWITCH));
                prefs.putBoolean(NATIVE_ADS_SWITCH, firebaseRemoteConfig.getBoolean(NATIVE_ADS_SWITCH));

                prefs.putString(REWARDED_AD_WATERMARK_ID, firebaseRemoteConfig.getString(REWARDED_AD_WATERMARK_ID));
                prefs.putString(REWARDED_AD_SAVE_ID, firebaseRemoteConfig.getString(REWARDED_AD_SAVE_ID));
                prefs.putString(INTERSTITIAL_UPLOAD_ID, firebaseRemoteConfig.getString(INTERSTITIAL_UPLOAD_ID));
                prefs.putString(NATIVE_ADS_ID, firebaseRemoteConfig.getString(NATIVE_ADS_ID));

// New logs
                Log.d("SharedPrefs", "rewarded_ad_watermark_switch: " + prefs.getBoolean(REWARDED_AD_WATERMARK_SWITCH, false));
                Log.d("SharedPrefs", "rewarded_ad_save_switch: " + prefs.getBoolean(REWARDED_AD_SAVE_SWITCH, false));
                Log.d("SharedPrefs", "interstitial_upload_switch: " + prefs.getBoolean(INTERSTITIAL_UPLOAD_SWITCH, false));
                Log.d("SharedPrefs", "native_ads_switch: " + prefs.getBoolean(NATIVE_ADS_SWITCH, false));
                Log.d("SharedPrefs", "rewarded_ad_watermark_id: " + prefs.getString(REWARDED_AD_WATERMARK_ID, ""));
                Log.d("SharedPrefs", "rewarded_ad_save_id: " + prefs.getString(REWARDED_AD_SAVE_ID, ""));
                Log.d("SharedPrefs", "interstitial_upload_id: " + prefs.getString(INTERSTITIAL_UPLOAD_ID, ""));
                Log.d("SharedPrefs", "native_ads_id: " + prefs.getString(NATIVE_ADS_ID, ""));

            }
        });
    }
}

package com.background.video.recorder.camera.recorder.firebase.remoteconfig;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {


    //////////////////////////////////////////////////////////////////////////////////////////////

    private static final String KEY_SPLASH_ADMOB = "splash_admob";
    private static final String splash_facebook = "splash_facebook";
    private static final String splash_admob_native = "splash_admob_native";
    private static final String splash_facebook_native = "splash_facebook_native";
    private static final String saved_files_in_gallery_admob_native = "saved_files_in_gallery_admob_native";
    private static final String screenrecordinggalleryadmob_native = "screenrecordinggalleryadmob_native";
    private static final String screenrecordinggalleryfacebook_native = "screenrecordinggalleryfacebook_native";
    private static final String saved_files_in_gallery_facebook_native = "saved_files_in_gallery_facebook_native";
    private static final String files_video_preview_admob_native = "files_video_preview_admob_native";
    private static final String files_video_preview_facebook_native = "files_video_preview_facebook_native";
    private static final String sahre_activity_admob_native = "sahre_activity_admob_native";
    private static final String sahre_activity_admob_inter = "sahre_activity_admob_inter";
    private static final String sahre_activity_admob_album = "sahre_activity_admob_album";
    private static final String sahre_activity_admob_home = "sahre_activity_admob_home";
    private static final String sahre_activity_facebook_native = "sahre_activity_facebook_native";
    private static final String sahre_activity_album_admob_interstitial = "sahre_activity_album_admob_interstitial";
    private static final String saved_successfull_admob_native = "saved_successfull_admob_native";
    private static final String saved_successfull_facebook_native = "saved_successfull_facebook_native";
    private static final String setuserpassword_admob_native = "setuserpassword_admob_native";
    private static final String setuserpassword_facebook_native = "setuserpassword_facebook_native";
    private static final String asking_password_admob_native = "asking_password_admob_native";
    private static final String asking_password_facebook_native = "asking_password_facebook_native";
    private static final String asking_password_setpassword_admob_interstitial = "asking_password_setpassword_admob_interstitial";
    private static final String asking_password_notnow_admob_interstitial = "asking_password_notnow_admob_interstitial";
    private static final String fav_files_video_preview_admob_native = "fav_files_video_preview_admob_native";
    private static final String fav_files_video_preview_facebook_native = "fav_files_video_preview_facebook_native";
    private static final String fav_files_admob_native = "fav_files_admob_native";
    private static final String fav_files_facebook_native = "fav_files_facebook_native";
    private static final String exit_admob_native = "exit_admob_native";
    private static final String exit_facebook_native = "exit_facebook_native";
    private static final String exit_interstitial_admob = "exit_interstitial_admob";
    private static final String exit_interstitial_facebook = "exit_interstitial_facebook";
    private static final String verify_password_admob_native = "verify_password_admob_native";
    private static final String verify_password_facebook_native = "verify_password_facebook_native";
    private static final String trim_admob_native = "trim_admob_native";
    private static final String trim_facebook_native = "trim_facebook_native";
    private static final String remove_password_admob_native = "remove_password_admob_native";
    private static final String language_admob_native = "language_admob_native";
    private static final String language_interstitial = "language_interstitial";
    private static final String language_facebook_native = "language_facebook_native";
    private static final String remove_password_facebook_native = "remove_password_facebook_native";
    private static final String photoeditorcollagemaker_admob_native = "photoeditorcollagemaker_admob_native";
    private static final String photoeditorcollagemaker_facebook_native = "photoeditorcollagemaker_facebook_native";
    private static final String photoeditorcollagemaker_home_admob_native = "photoeditorcollagemaker_home_admob_native";
    private static final String photoeditorcollagemaker_home_admob_inter = "photoeditorcollagemaker_home_admob_inter";
    private static final String rename_dailog_admob_native = "rename_dailog_admob_native";
    private static final String rename_dailog_facebook_native = "rename_dailog_facebook_native";


    private static final String pre_home_admob_native = "pre_home_admob_native";
    private static final String pre_home_facebook_native = "pre_home_facebook_native";


    private static final String pre_home_facebook_collage_inter = "pre_home_facebook_collage_inter";
    private static final String pre_home_admob_collage_inter = "pre_home_admob_collage_inter";
    private static final String pre_home_admob_home_inter = "pre_home_admob_home_inter";
    private static final String home_admob_home_inter = "home_admob_home_inter";
    private static final String admob_album_inter = "admob_album_inter";
    private static final String pre_home_facebook_home_inter = "pre_home_facebook_home_inter";
    private static final String pre_home_admob_SR_inter = "pre_home_admob_SR_inter";
    private static final String pre_home_facebook_SR_inter = "pre_home_facebook_SR_inter";
    private static final String facebook_banner_enable = "facebook_banner_enable";
    private static final String admob_banner_enable = "admob_banner_enable";
    private static final String emoji_admob_native = "emoji_admob_native";
    private static final String emoji_facebook_native = "emoji_facebook_native";
    private static final String sticker_admob_native = "sticker_admob_native";
    private static final String sticker_facebook_native = "sticker_facebook_native";


    public String getadmob_interstitial_splash_idId() {
        return getString(admob_interstitial_splash_id, "");
    }

    public String getfacebook_interstitial_splash_idId() {
        return getString(facebook_interstitial_splash_id, "");
    }

    public boolean getSplashAdmobSwitch() {
        return getBoolean(KEY_SPLASH_ADMOB, false);
    }

    public boolean getSplashFacebookSwitch() {
        return getBoolean(splash_facebook, false);
    }

    public boolean getsplash_admob_nativeSwitch() {
        return getBoolean(splash_admob_native, false);
    }

    public boolean getsplash_facebook_nativeSwitch() {
        return getBoolean(splash_facebook_native, false);
    }

    public boolean getsaved_files_in_gallery_admob_nativeSwitch() {
        return getBoolean(saved_files_in_gallery_admob_native, false);
    }

    public boolean getscreenrecordinggalleryadmob_nativeSwitch() {
        return getBoolean(screenrecordinggalleryadmob_native, false);
    }

    public boolean getscreenrecordinggalleryfacebook_nativeSwitch() {
        return getBoolean(screenrecordinggalleryfacebook_native, false);
    }

    public boolean getsaved_files_in_gallery_facebook_nativeSwitch() {
        return getBoolean(saved_files_in_gallery_facebook_native, false);
    }

    public boolean getfiles_video_preview_admob_nativeSwitch() {
        return getBoolean(files_video_preview_admob_native, false);
    }

    public boolean getfiles_video_preview_facebook_nativeSwitch() {
        return getBoolean(files_video_preview_facebook_native, false);
    }

    public boolean getsahre_activity_admob_nativeSwitch() {
        return getBoolean(sahre_activity_admob_native, false);
    }

    public boolean getsahre_activity_admob_interSwitch() {
        return getBoolean(sahre_activity_admob_inter, false);
    }

    public boolean getsahre_activity_admob_albumSwitch() {
        return getBoolean(sahre_activity_admob_album, false);
    }

     public boolean getsahre_activity_admob_homeSwitch() {
        return getBoolean(sahre_activity_admob_home, false);
    }

    public boolean getsahre_activity_facebook_nativeSwitch() {
        return getBoolean(sahre_activity_facebook_native, false);
    }

    public boolean getsahre_activity_album_admob_interstitialSwitch() {
        return getBoolean(sahre_activity_album_admob_interstitial, false);
    }

    public boolean getsaved_successfull_admob_nativeSwitch() {
        return getBoolean(saved_successfull_admob_native, false);
    }

    public boolean getsaved_successfull_facebook_nativeSwitch() {
        return getBoolean(saved_successfull_facebook_native, false);
    }

    public boolean getsetuserpassword_admob_nativeSwitch() {
        return getBoolean(setuserpassword_admob_native, false);
    }

    public boolean getsetuserpassword_facebook_nativeSwitch() {
        return getBoolean(setuserpassword_facebook_native, false);
    }

    public boolean getasking_password_admob_nativeSwitch() {
        return getBoolean(asking_password_admob_native, false);
    }

    public boolean getasking_password_facebook_nativeSwitch() {
        return getBoolean(asking_password_facebook_native, false);
    }

    public boolean getasking_password_setpassword_admob_interstitialSwitch() {
        return getBoolean(asking_password_setpassword_admob_interstitial, false);
    }

    public boolean getasking_password_notnow_admob_interstitialSwitch() {
        return getBoolean(asking_password_notnow_admob_interstitial, false);
    }

    public boolean getfav_files_video_preview_admob_nativeSwitch() {
        return getBoolean(fav_files_video_preview_admob_native, false);
    }

    public boolean getfav_files_video_preview_facebook_nativeSwitch() {
        return getBoolean(fav_files_video_preview_facebook_native, false);
    }

    public boolean getfav_files_admob_nativeSwitch() {
        return getBoolean(fav_files_admob_native, false);
    }

    public boolean getfav_files_facebook_nativeSwitch() {
        return getBoolean(fav_files_facebook_native, false);
    }

    public boolean getexit_admob_nativeSwitch() {
        return getBoolean(exit_admob_native, false);
    }

    public boolean getexit_facebook_nativeSwitch() {
        return getBoolean(exit_facebook_native, false);
    }

    public boolean getexit_interstitial_admobSwitch() {
        return getBoolean(exit_interstitial_admob, false);
    }

    public boolean getexit_interstitial_facebookSwitch() {
        return getBoolean(exit_interstitial_facebook, false);
    }

    public boolean getverify_password_admob_nativeSwitch() {
        return getBoolean(verify_password_admob_native, false);
    }

    public boolean getverify_password_facebook_nativeSwitch() {
        return getBoolean(verify_password_facebook_native, false);
    }

    public boolean gettrim_admob_nativeSwitch() {
        return getBoolean(trim_admob_native, false);
    }

    public boolean gettrim_facebook_nativeSwitch() {
        return getBoolean(trim_facebook_native, false);
    }

    public boolean getremove_password_admob_nativeSwitch() {
        return getBoolean(remove_password_admob_native, false);
    }

    public boolean getlanguage_admob_nativeSwitch() {
        return getBoolean(language_admob_native, false);
    }

    public boolean getlanguage_interstitialSwitch() {
        return getBoolean(language_interstitial, false);
    }

    public boolean getlanguage_facebook_nativeSwitch() {
        return getBoolean(language_facebook_native, false);
    }

    public boolean getremove_password_facebook_nativeSwitch() {
        return getBoolean(remove_password_facebook_native, false);
    }

    public boolean getphotoeditorcollagemaker_admob_nativeSwitch() {
        return getBoolean(photoeditorcollagemaker_admob_native, false);
    }

    public boolean getphotoeditorcollagemaker_facebook_nativeSwitch() {
        return getBoolean(photoeditorcollagemaker_facebook_native, false);
    }

    public boolean getphotoeditorcollagemaker_home_admob_nativeSwitch() {
        return getBoolean(photoeditorcollagemaker_home_admob_native, false);
    }

    public boolean getphotoeditorcollagemaker_home_admob_interSwitch() {
        return getBoolean(photoeditorcollagemaker_home_admob_inter, false);
    }

    public boolean getrename_dailog_admob_nativeSwitch() {
        return getBoolean(rename_dailog_admob_native, false);
    }

    public boolean getrename_dailog_facebook_nativeSwitch() {
        return getBoolean(rename_dailog_facebook_native, false);
    }

    public boolean getpre_home_admob_nativeSwitch() {
        return getBoolean(pre_home_admob_native, false);
    }

    public boolean getpre_home_facebook_nativeSwitch() {
        return getBoolean(pre_home_facebook_native, false);
    }

    public boolean getpre_home_admob_SR_interSwitch() {
        return getBoolean(pre_home_admob_SR_inter, false);
    }

    public boolean getpre_home_facebook_SR_interSwitch() {
        return getBoolean(pre_home_facebook_SR_inter, false);
    }

    public boolean getfacebook_banner_enableSwitch() {
        return getBoolean(facebook_banner_enable, false);
    }

    public boolean getemoji_admob_nativeSwitch() {
        return getBoolean(emoji_admob_native, false);
    }

    public boolean getemoji_facebook_nativeSwitch() {
        return getBoolean(emoji_facebook_native, false);
    }

    public boolean getsticker_admob_nativeSwitch() {
        return getBoolean(sticker_admob_native, false);
    }

    public boolean getsticker_facebook_nativeSwitch() {
        return getBoolean(sticker_facebook_native, false);
    }

    public boolean getadmob_banner_enableSwitch() {
        return getBoolean(admob_banner_enable, false);
    }

    public boolean getpre_home_admob_home_interSwitch() {
        return getBoolean(pre_home_admob_home_inter, false);
    }

    public boolean gethome_admob_home_interSwitch() {
        return getBoolean(home_admob_home_inter, false);
    }
 public boolean getadmob_album_interSwitch() {
        return getBoolean(admob_album_inter, false);
    }

    public boolean getpre_home_facebook_home_interSwitch() {
        return getBoolean(pre_home_facebook_home_inter, false);
    }

    public boolean getpre_home_admob_collage_interSwitch() {
        return getBoolean(pre_home_admob_collage_inter, false);
    }

    public boolean getpre_home_facebook_collage_interSwitch() {
        return getBoolean(pre_home_facebook_collage_inter, false);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////


    private static final String language_native_admob = "language_native_admob";
    private static final String language_native_facebook = "language_facebook_native";
    private static final String onboarding_native = "onboarding_native";
    private static final String onbaording_native_facebook = "onbaording_native_facebook";
    private static final String dashboard_native_facebook = "dashboard_native_facebook";
    private static final String overlay_permission_admob_native = "overlay_permission_admob_native";
    private static final String overlay_permission_facebook_native = "overlay_permission_facebook_native";
    private static final String dashboard_native = "dashboard_native";
    private static final String user_consent_form = "user_consent_form";
    private static final String album_native_admob = "album_native_admob";
    private static final String imageview_native_admob = "imageview_native_admob";
    private static final String imageview_native_facebook = "imageview_native_facebook";
    private static final String album_native_facebook = "album_native_facebook";
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
    private static final String exit_native_facebook = "exit_native_facebook";
    private static final String exit_native = "exit_native";


    private static final String exitDailogNativeFacebook = "exitDailogNativeFacebook";
    private static final String onboarding_interstitial = "onboarding_interstitial";
    private static final String dashboard_interstitial = "dashboard_interstitial";
    private static final String image_recovery_interstitial = "image_recovery_interstitial";
    private static final String successful_recovery_interstitial = "successful_recovery_interstitial";
    private static final String video_recovery_interstitial = "video_recovery_interstitial";
    private static final String audio_recovery_interstitial = "audio_recovery_interstitial";
    private static final String documents_recovery_interstitial = "documents_recovery_interstitial";
    private static final String is_Concent_FormEnable = "is_Concent_FormEnable";

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////// BACK PRESSED //////////////////////////////////////////////

    private static final String backpress_dashboard_interstitial_admob = "backpress_dashboard_interstitial_admob";
    private static final String backpress_imagerecovery_interstitial_admob = "backpress_imagerecovery_interstitial_admob";
    private static final String backpress_audiosRecovery_interstitial_admob = "backpress_audiosRecovery_interstitial_admob";
    private static final String backpress_videosRecovery_interstitial_admob = "backpress_videosRecovery_interstitial_admob";
    private static final String backpress_documentsRecovery_interstitial_admob = "backpress_documentsRecovery_interstitial_admob";
    private static final String backpress_processing_interstitial_admob = "backpress_processing_interstitial_admob";
    private static final String backpress_recoveredFiles_interstitial_admob = "backpress_recoveredFiles_interstitial_admob";
    private static final String backpress_successFull_interstitial_admob = "backpress_successFull_interstitial_admob";
    private static final String backpress_fileShredder_interstitial_admob = "backpress_fileShredder_interstitial_admob";


    ////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String PREF_NAME = "RemoteConfigPrefs";
    private static final String KEY_TOAST_ENABLED = "toast_enabled";

    private static final String KEY_HAS_PURCHASED = "has_purchased";

    // Keys for switches
    private static final String KEY_BANNER_SWITCH = "banner_switch";
    private static final String facebook_banner = "facebook_banner";
    private static final String GDPR_FORM = "GDPR_FORM";
    private static final String KEY_APPOPEN_SWITCH = "appopen_switch";
    private static final String KEY_INTER_SWITCH = "inter_switch";
    private static final String KEY_NATIVE_SWITCH = "native_switch";
    private static final String KEY_INTER_SPLASH = "inter_splash";
    private static final String KEY_REWARDED_AD = "rewarded_ad";

    // Keys for Ad IDs
    private static final String KEY_BANNER_AD_ID = "banner_ad_id";
    private static final String KEY_APPOPEN_AD_ID = "appopen_ad_id";
    private static final String KEY_INTER_AD_ID = "inter_ad_id";
    private static final String KEY_NATIVE_AD_ID = "native_ad_id";
    private static final String KEY_INTER_SPLASH_AD_ID = "inter_splash_ad_id";
    private static final String KEY_REWARDED_AD_ID = "rewarded_ad_id";


    // New Keys for switches and Ad IDs
    private static final String KEY_REWARDED_AD_WATERMARK_SWITCH = "rewarded_watermark_switch";
    private static final String KEY_INTER_AD_EXIT_SWITCH = "inter_exit_ad_switch";
    private static final String KEY_INTER_AD_EXIT_ID = "inter_exit_ad_id";


    //////////////////////////////////////  IDS  /////////////////////////////
    private static final String admob_interstitial_splash_id = "admob_interstitial_splash_id";
    private static final String facebook_interstitial_splash_id = "facebook_interstitial_splash_id";
    private static final String admob_interstitial_home_id = "admob_interstitial_home_id";
    private static final String photoeditorcollagemaker_home_admob_inter_id = "photoeditorcollagemaker_home_admob_inter_id";
    private static final String admob_native_id = "admob_native_id";
    private static final String language_admob_native_id = "language_admob_native_id";
    private static final String onboarding_native_admob_id = "onboarding_native_admob_id";
    private static final String facebook_native_ad_id = "facebook_native_ad_id";
    private static final String language_interstitial_admob_id = "language_interstitial_admob_id";
    private static final String admob_banner_id = "admob_banner_id";
    private static final String facebook_banner_ad_id = "facebook_banner_ad_id";


    //////////////////////////////////////////////////////////////////////////
    private static final String KEY_REWARDED_AD_WATERMARK_ID = "rewarded_watermark";
    private static final String KEY_REWARDED_AD_SAVE_SWITCH = "rewarded_ad_save_switch";
    private static final String KEY_REWARDED_AD_SAVE_ID = "rewarded_ad_save_id";
    private static final String KEY_INTERSTITIAL_UPLOAD_SWITCH = "interstitial_upload_switch";
    private static final String KEY_INTERSTITIAL_UPLOAD_ID = "interstitial_upload_id";
    private static final String KEY_NATIVE_ADS_SWITCH = "native_ad_switch";
    private static final String KEY_NATIVE_ADS_ID = "native_ad_id";

    private static SharedPrefsHelper instance;
    private final SharedPreferences sharedPreferences;
    // Keys for storing preferences
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_LANGUAGE_SET = "IsLanguageSet";

    private SharedPrefsHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefsHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefsHelper(context);
        }
        return instance;
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        sharedPreferences.edit().putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime).apply();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setLanguageSet(boolean isLanguageSet) {
        sharedPreferences.edit().putBoolean(IS_LANGUAGE_SET, isLanguageSet).apply();
    }

    public boolean isLanguageSet() {
        return sharedPreferences.getBoolean(IS_LANGUAGE_SET, false);
    }

    // Method to get Toast Enabled
    public boolean getToastEnabled() {
        return getBoolean(KEY_TOAST_ENABLED, false);
    }

    private static final String KEY_IS_ADMOB_ENABLED = "isAdMobEnabled";

    // Method to set isAdMobEnabled
    public void setIsAdMobEnabled(boolean value) {
        putBoolean(KEY_IS_ADMOB_ENABLED, value);
    }

    // Method to get isAdMobEnabled
    public boolean getIsAdMobEnabled() {
        return getBoolean(KEY_IS_ADMOB_ENABLED, false);
    }

    // Switches methods
    public void setBannerSwitch(boolean value) {
        putBoolean(KEY_BANNER_SWITCH, value);
    }

    public boolean getBannerSwitch() {
        return getBoolean(KEY_BANNER_SWITCH, false);
    }

    public boolean getfacebook_banner() {
        return getBoolean(facebook_banner, false);
    }

    public boolean getNativeAdmobSwitch() {
        return getBoolean(language_native_admob, false);
    }

    public boolean getlanguage_native_facebookSwitch() {
        return getBoolean(language_native_facebook, false);
    }

    public boolean getLanguageInterstitialSwitch() {
        return getBoolean(language_interstitial, false);
    }

    public boolean getonboarding_nativeSwitch() {
        return getBoolean(onboarding_native, false);
    }

    public boolean getdashboard_native_facebookSwitch() {
        return getBoolean(dashboard_native_facebook, false);
    }

    public boolean getoverlay_permission_admob_nativeSwitch() {
        return getBoolean(overlay_permission_admob_native, false);
    }

    public boolean getoverlay_permission_facebook_nativeSwitch() {
        return getBoolean(overlay_permission_facebook_native, false);
    }

    public boolean getonbaording_native_facebookSwitch() {
        return getBoolean(onbaording_native_facebook, false);
    }

    public boolean getdashboard_nativeSwitch() {
        return getBoolean(dashboard_native, false);
    }

    public boolean getuser_consent_formSwitch() {
        return getBoolean(user_consent_form, false);
    }

    public boolean getalbum_native_admobSwitch() {
        return getBoolean(album_native_admob, false);
    }

    public boolean getalbum_native_facebookSwitch() {
        return getBoolean(album_native_admob, false);
    }
  public boolean getimageview_native_admobSwitch() {
        return getBoolean(imageview_native_admob, false);
    }

    public boolean getimageview_native_facebookSwitch() {
        return getBoolean(imageview_native_facebook, false);
    }

    public boolean getimage_recovery_nativeSwitch() {
        return getBoolean(image_recovery_native, false);
    }

    public boolean getimage_recovery_native_facebookSwitch() {
        return getBoolean(image_recovery_native_facebook, false);
    }

    public boolean getvideo_recovery_nativeSwitch() {
        return getBoolean(video_recovery_native, false);
    }

    public boolean getaudio_recovery_nativeSwitch() {
        return getBoolean(audio_recovery_native, false);
    }

    public boolean getaudio_recovery_native_facebookSwitch() {
        return getBoolean(audio_recovery_native_facebook, false);
    }

    public boolean getdocument_recovery_nativeSwitch() {
        return getBoolean(document_recovery_native, false);
    }

    public boolean getdocument_recovery_native_facebookSwitch() {
        return getBoolean(document_recovery_native_facebook, false);
    }

    public boolean getrecovered_files_imageSwitch() {
        return getBoolean(recovered_files_image, false);
    }

    public boolean getrecovered_files_image_native_facebookSwitch() {
        return getBoolean(recovered_files_image_native_facebook, false);
    }

    public boolean getrecovered_files_videoSwitch() {
        return getBoolean(recovered_files_video, false);
    }

    public boolean getrecovered_files_video_native_facebookSwitch() {
        return getBoolean(recovered_files_video_native_facebook, false);
    }

    public boolean getrecovered_files_document_nativeSwitch() {
        return getBoolean(recovered_files_document_native, false);
    }

    public boolean getrecovered_files_document_native_facebookSwitch() {
        return getBoolean(recovered_files_document_native_facebook, false);
    }

    public boolean getrecovered_files_audioSwitch() {
        return getBoolean(recovered_files_audio, false);
    }

    public boolean getrecovered_files_audio_native_facebookSwitch() {
        return getBoolean(recovered_files_audio_native_facebook, false);
    }

    public boolean getprocessing_nativeSwitch() {
        return getBoolean(processing_native, false);
    }

    public boolean getprocessing_native_facebookSwitch() {
        return getBoolean(processing_native_facebook, false);
    }

    public boolean getsuccessful_nativeSwitch() {
        return getBoolean(successful_native, false);
    }

    public boolean getsuccessful_native_facebookSwitch() {
        return getBoolean(successful_native_facebook, false);
    }

    public boolean getfile_shredder_nativeSwitch() {
        return getBoolean(file_shredder_native, false);
    }

    public boolean getfile_shredder_native_facebookSwitch() {
        return getBoolean(file_shredder_native_facebook, false);
    }

    public boolean getfile_shredder_imageSwitch() {
        return getBoolean(file_shredder_image, false);
    }

    public boolean getfile_shredder_image_native_facebookSwitch() {
        return getBoolean(file_shredder_image_native_facebook, false);
    }

    public boolean getfile_shredder_videoSwitch() {
        return getBoolean(file_shredder_video, false);
    }

    public boolean getfile_shredder_video_native_facebookSwitch() {
        return getBoolean(file_shredder_video_native_facebook, false);
    }

    public boolean getfile_shredder_audioSwitch() {
        return getBoolean(file_shredder_audio, false);
    }

    public boolean getfile_shredder_audio_native_facebookSwitch() {
        return getBoolean(file_shredder_audio_native_facebook, false);
    }

    public boolean getfile_shredder_documentSwitch() {
        return getBoolean(file_shredder_document, false);
    }

    public boolean getfile_shredder_document_native_facebookSwitch() {
        return getBoolean(file_shredder_document_native_facebook, false);
    }

    public boolean getexit_nativeSwitch() {
        return getBoolean(exit_native, false);
    }

    public boolean getexit_native_facebookSwitch() {
        return getBoolean(exit_native_facebook, false);
    }

    public boolean getbackpress_dashboard_interstitial_admobSwitch() {
        return getBoolean(backpress_dashboard_interstitial_admob, false);
    }

    public boolean getbackpress_imagerecovery_interstitial_admobSwitch() {
        return getBoolean(backpress_imagerecovery_interstitial_admob, false);
    }

    public boolean getbackpress_videosRecovery_interstitial_admobSwitch() {
        return getBoolean(backpress_videosRecovery_interstitial_admob, false);
    }

    public boolean getbackpress_documentsRecovery_interstitial_admobSwitch() {
        return getBoolean(backpress_documentsRecovery_interstitial_admob, false);
    }

    public boolean getbackpress_processing_interstitial_admobSwitch() {
        return getBoolean(backpress_processing_interstitial_admob, false);
    }

    public boolean getbackpress_fileShredder_interstitial_admobSwitch() {
        return getBoolean(backpress_fileShredder_interstitial_admob, false);
    }

    public boolean getbackpress_recoveredFiles_interstitial_admobSwitch() {
        return getBoolean(backpress_recoveredFiles_interstitial_admob, false);
    }

    public boolean getbackpress_successFull_interstitial_admobSwitch() {
        return getBoolean(backpress_successFull_interstitial_admob, false);
    }

    public boolean getbackpress_audiosRecovery_interstitial_admobSwitch() {
        return getBoolean(backpress_audiosRecovery_interstitial_admob, false);
    }

    public boolean getexitDailogNativeFacebook() {
        return getBoolean(exitDailogNativeFacebook, false);
    }

    public boolean getonboarding_interstitialSwitch() {
        return getBoolean(onboarding_interstitial, false);
    }

    public boolean getdashboard_interstitialSwitch() {
        return getBoolean(dashboard_interstitial, false);
    }

    public boolean getimage_recovery_interstitialSwitch() {
        return getBoolean(image_recovery_interstitial, false);
    }

    public boolean getsuccessful_recovery_interstitialSwitch() {
        return getBoolean(successful_recovery_interstitial, false);
    }

    public boolean getvideo_recovery_interstitialSwitch() {
        return getBoolean(video_recovery_interstitial, false);
    }

    public boolean getaudio_recovery_interstitialSwitch() {
        return getBoolean(audio_recovery_interstitial, false);
    }

    public boolean getdocuments_recovery_interstitialSwitch() {
        return getBoolean(documents_recovery_interstitial, false);
    }

    public boolean getis_Concent_FormEnableSwitch() {
        return getBoolean(is_Concent_FormEnable, false);
    }

    public boolean getGDPRSwitch() {
        return getBoolean(GDPR_FORM, false);
    }

    public void setAppopenSwitch(boolean value) {
        putBoolean(KEY_APPOPEN_SWITCH, value);
    }

    public boolean getAppopenSwitch() {
        return getBoolean(KEY_APPOPEN_SWITCH, false);
    }

    public void setInterSwitch(boolean value) {
        putBoolean(KEY_INTER_SWITCH, value);
    }

    public boolean getInterSwitch() {
        return getBoolean(KEY_INTER_SWITCH, false);
    }

    public void setNativeSwitch(boolean value) {
        putBoolean(KEY_NATIVE_SWITCH, value);
    }

    public boolean getNativeSwitch() {
        return getBoolean(KEY_NATIVE_SWITCH, false);
    }

    public void setInterSplashSwitch(boolean value) {
        putBoolean(KEY_INTER_SPLASH, value);
    }

    public boolean getInterSplashSwitch() {
        return getBoolean(KEY_INTER_SPLASH, false);
    }

    public void setRewardedAdSwitch(boolean value) {
        putBoolean(KEY_REWARDED_AD, value);
    }

    public boolean getRewardedAdSwitch() {
        return getBoolean(KEY_REWARDED_AD, false);
    }

    // Ad IDs methods
    public void setBannerAdId(String value) {
        putString(KEY_BANNER_AD_ID, value);
    }

    public String getBannerAdId() {
        return getString(KEY_BANNER_AD_ID, "");
    }

    public void setAppopenAdId(String value) {
        putString(KEY_APPOPEN_AD_ID, value);
    }

    public String getAppopenAdId() {
        return getString(KEY_APPOPEN_AD_ID, "");
    }

    public void setInterAdId(String value) {
        putString(KEY_INTER_AD_ID, value);
    }

    public String getInterAdId() {
        return getString(KEY_INTER_AD_ID, "");
    }

    public void setNativeAdId(String value) {
        putString(KEY_NATIVE_AD_ID, value);
    }

    public String getNativeAdId() {
        return getString(KEY_NATIVE_AD_ID, "");
    }

    public void setInterSplashAdId(String value) {
        putString(KEY_INTER_SPLASH_AD_ID, value);
    }

    public String getInterSplashAdId() {
        return getString(KEY_INTER_SPLASH_AD_ID, "");
    }

    public void setRewardedAdId(String value) {
        putString(KEY_REWARDED_AD_ID, value);
    }

    public String getRewardedAdId() {
        return getString(KEY_REWARDED_AD_ID, "");
    }


    // New methods for the additional switches
    public void setRewardedAdWatermarkSwitch(boolean value) {
        putBoolean(KEY_REWARDED_AD_WATERMARK_SWITCH, value);
    }

    public boolean getRewardedAdWatermarkSwitch() {
        return getBoolean(KEY_REWARDED_AD_WATERMARK_SWITCH, false);
    }

    public boolean getInterADExitSwitch() {
        return getBoolean(KEY_INTER_AD_EXIT_SWITCH, false);
    }

    public void setRewardedAdSaveSwitch(boolean value) {
        putBoolean(KEY_REWARDED_AD_SAVE_SWITCH, value);
    }

    public boolean getRewardedAdSaveSwitch() {
        return getBoolean(KEY_REWARDED_AD_SAVE_SWITCH, false);
    }

    public void setInterstitialUploadSwitch(boolean value) {
        putBoolean(KEY_INTERSTITIAL_UPLOAD_SWITCH, value);
    }

    public boolean getInterstitialUploadSwitch() {
        return getBoolean(KEY_INTERSTITIAL_UPLOAD_SWITCH, true);
    }

    public void setNativeAdsSwitch(boolean value) {
        putBoolean(KEY_NATIVE_ADS_SWITCH, value);
    }

    public boolean getNativeAdsSwitch() {
        return getBoolean(KEY_NATIVE_ADS_SWITCH, true);
    }

    // New methods for the additional Ad IDs
    public void setRewardedAdWatermarkId(String value) {
        putString(KEY_REWARDED_AD_WATERMARK_ID, value);
    }

    public String getRewardedAdWatermarkId() {
        return getString(KEY_REWARDED_AD_WATERMARK_ID, "");
    }

    public String getInterADExitId() {
        return getString(KEY_INTER_AD_EXIT_ID, "");
    }

    public String getadmob_interstitial_home_idId() {
        return getString(admob_interstitial_home_id, "");
    }

    public String getphotoeditorcollagemaker_home_admob_inter_idId() {
        return getString(photoeditorcollagemaker_home_admob_inter_id, "");
    }

    public String getadmob_native_idId() {
        return getString(admob_native_id, "");
    }

    public String getlanguage_admob_native_idId() {
        return getString(language_admob_native_id, "");
    }

    public String getonboarding_native_admob_idId() {
        return getString(onboarding_native_admob_id, "");
    }

    public String getfacebook_native_ad_idId() {
        return getString(facebook_native_ad_id, "");
    }

    public String getlanguage_interstitial_admob_idId() {
        return getString(language_interstitial_admob_id, "");
    }

    public String getadmob_banner_idId() {
        return getString(admob_banner_id, "");
    }

    public String getfacebook_banner_ad_idId() {
        return getString(facebook_banner_ad_id, "");
    }

    public void setRewardedAdSaveId(String value) {
        putString(KEY_REWARDED_AD_SAVE_ID, value);
    }

    public String getRewardedAdSaveId() {
        return getString(KEY_REWARDED_AD_SAVE_ID, "");
    }

    public void setInterstitialUploadId(String value) {
        putString(KEY_INTERSTITIAL_UPLOAD_ID, value);
    }

    public String getInterstitialUploadId() {
        return getString(KEY_INTERSTITIAL_UPLOAD_ID, "");
    }

    public void setNativeAdsId(String value) {
        putString(KEY_NATIVE_ADS_ID, value);
    }

    public String getNativeAdsId() {
        return getString(KEY_NATIVE_ADS_ID, "");
    }


    // Generic private methods
    public void putBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void setHasPurchased(boolean value) {
        putBoolean(KEY_HAS_PURCHASED, value);
    }

    // Method to get has_purchased
    public boolean getHasPurchased() {
        return getBoolean(KEY_HAS_PURCHASED, false);
    }
}

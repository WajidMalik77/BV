package roozi.app.models

data class AdData(
    var inApp: Boolean = false,
    var CappingCounter: Int = 2,
    var AdmobNativeId: String = "",
    var AdmobBannerId: String = "",
    var AdmobInterstitialId: String = "",
    var layoutTemplate: String = "bcta",
    var ApplovinCtaColor: String = "#000000",
    var ApplovinCtaTextColor: String = "#ffffff",
    var AdmobCtaColor: String = "#000000",
    var AdmobCtaTextColor: String = "#ffffff",
    var fbInInterId: String = "",
    var fbBannerId: String = "",
    var fbNativeId: String = "",
    var isAdmobEnabled: Boolean = false,
    var isFbEnabled: Boolean = true,
    var isOn: String = "on",
    var OpenAdId:String = "",
    var isOpenAdEnabled :Boolean = true
)
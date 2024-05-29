package roozi.app.interfaces

interface EventListener {
    fun onDismiss(key : String)
    fun onAdShow(key : String)
    fun onAdClick(key : String)
}
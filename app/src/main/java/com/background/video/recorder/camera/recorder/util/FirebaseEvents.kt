package com.background.video.recorder.camera.recorder.util

import android.util.Log
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class FirebaseEvents {
    companion object {
        private val TAG = "EventLogTrack"
        fun logAnalytic(screenName: String) {
            Log.d("events_firebase", "logAnalytic: $screenName")
            Firebase.analytics.logEvent(screenName, null)
        }
    }
}
package com.background.video.recorder.camera.recorder.screenRecordingListener

import android.util.Log
import com.hbisoft.hbrecorder.HBRecorderListener

class MyHBRecorderListener() : HBRecorderListener {
    private val TAG = "LOGEEEE"
    override fun HBRecorderOnStart() {
        Log.d(TAG, "HBRecorderOnResume: ")
    }

    override fun HBRecorderOnComplete() {
        Log.d(TAG, "HBRecorderOnComplete: ")
    }

    override fun HBRecorderOnError(errorCode: Int, reason: String?) {
        Log.d(TAG, "HBRecorderOnError: $reason" )
    }

    override fun HBRecorderOnPause() {
        Log.d(TAG, "HBRecorderOnPause: ")
    }

    override fun HBRecorderOnResume() {
        Log.d(TAG, "HBRecorderOnResume: ")
    }
}
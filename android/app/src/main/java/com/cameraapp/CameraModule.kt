package com.cameraapp

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule

class CameraModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    private var flashEnabled: Boolean = false

    override fun getName(): String {
        return "CameraModule"
    }

    @ReactMethod
    fun openCamera(promise: Promise) {
        val activity: Activity? = currentActivity
        if (activity != null) {
            try {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                activity.startActivityForResult(intent, 1)
                promise.resolve("Camera opened successfully")
            } catch (e: Exception) {
                promise.reject("CAMERA_ERROR", "Failed to open camera", e)
            }
        } else {
            promise.reject("NO_ACTIVITY", "No activity available")
        }
    }

    @ReactMethod
    fun toggleFlashMode(promise: Promise) {
        flashEnabled = !flashEnabled
        // Logic to handle flash toggle
        Log.d("CameraModule", "Flash mode: ${if (flashEnabled) "ON" else "OFF"}")
        promise.resolve(flashEnabled)
    }
}

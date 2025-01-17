package com.cameraapp
import android.net.Uri
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
//import androidx.core.content.ContextCompat
import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager


class CameraModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    companion object {
        private const val TAG = "MyClassTag"
    }
    private val reactContext: ReactApplicationContext = reactContext
    private var photoFilePath: String? = null
    private var flashEnabled: Boolean = false

    override fun getName(): String {
        return "CameraModule"
    }



    @ReactMethod
    fun openCamera(promise: Promise) {
        val currentActivity = currentActivity as? MainActivity
        if (currentActivity != null) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            Log.d("MyTag", "Launching camera with intent: $cameraIntent")
            currentActivity.launchCamera(cameraIntent)
            promise.resolve("Camera opened successfully")
        } else {
            Log.e("MyTag", "Current activity is null, cannot launch camera")
            promise.reject("Error", "Activity is null")
        }

//        val activity = currentActivity
//        Log.d("MyTag", "openCamera method invoked Pigs are cute")
//        Log.d("MyTag", "activity : $activity")
//        if (activity == null) {
//            promise.reject("NO_ACTIVITY", "No activity found")
//            return
//        }
//        Log.d("MyTag", "check this condition: ${(ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)}")
//        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            Log.d("MyTag", "inside this new IF BLOCK")
//            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), 101)
//            promise.reject("PERMISSION_DENIED", "Camera permission not granted")
//            return
//        }
//
//        try {
//            // Create an intent to open the camera
//            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            Log.d("MyTag", "cameraIntent piglest : $cameraIntent")
//            // Start the camera activity
//            activity.startActivityForResult(cameraIntent, 1)
//
//            // Resolve the promise immediately (the emulator doesn't save real images)
//            promise.resolve("Camera opened successfully")
//        } catch (e: Exception) {
//            promise.reject("CAMERA_ERROR", "Failed to open camera", e)
//        }
    }

    @ReactMethod
    fun addListener(eventName: String) {
        // This is required for React Native to manage event listeners
        Log.d("CameraModule", "Listener added for event:")
    }

    @ReactMethod
    fun removeListeners(count: Int) {
        // This is required for React Native to manage event listeners
        Log.d("CameraModule", "Listeners removed:")
    }

    fun sendEventToReact(eventName: String, params: String) {
        Log.d("CameraModule", "sendEventToReact called with event: $eventName and params: $params")
        reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
    }





    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return try {
            val file = File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            )
            photoFilePath = file.absolutePath
            file
        } catch (e: Exception) {
            Log.e("CameraModule", "Error creating file: ${e.message}")
            null
        }
    }

    @ReactMethod
    fun toggleFlashMode(promise: Promise) {
        Log.d(TAG,"openCamera method executed")
        Log.d("MyTag", "openCamera method invoked")
        Log.d("MyTag", "Debug message")
        Log.i("MyTag", "Info message")

        flashEnabled = !flashEnabled
        // Logic to handle flash toggle
        Log.d("MyTag","flashEnabled variable: $flashEnabled")
        Log.d("CameraModule", "Flash mode: ${if (flashEnabled) "ON" else "OFF"}")
        promise.resolve(flashEnabled)
    }
}

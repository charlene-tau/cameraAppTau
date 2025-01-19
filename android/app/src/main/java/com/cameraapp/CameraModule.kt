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

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.module.annotations.ReactModule
import android.os.Handler
import android.os.Looper


@ReactModule(name = CameraModule.NAME)
class CameraModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    companion object {
        private const val TAG = "MyClassTag"
        const val NAME = "CameraModule"
    }
    private val reactContext: ReactApplicationContext = reactContext
    private var photoFilePath: String? = null
    private var flashEnabled: Boolean = false

    override fun getName(): String {
        return "CameraModule"
    }



    @ReactMethod
    fun openCamera(promise: Promise) {
        val currentActivity = currentActivity 
        if (currentActivity == null) {
        Log.e("MyTag", "Current activity is null, cannot launch camera")
        promise.reject("Error", "Activity is null")
        return
    }

        if (ContextCompat.checkSelfPermission(currentActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
           Log.d("MyTag", "inside this new IF BLOCK")
           ActivityCompat.requestPermissions(currentActivity, arrayOf(Manifest.permission.CAMERA), 101)
           promise.reject("PERMISSION_DENIED", "Camera permission not granted")
           return
       }

       try{
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            Log.d("MyTag", "Launching camera with intent: $cameraIntent")
            if (currentActivity is MainActivity) {
            currentActivity.launchCamera(cameraIntent)

                promise.resolve("Camera opened successfully")}

            else {
            Log.e("MyTag", "Current activity is not MainActivity, cannot launch camera")
            promise.reject("Error", "Current activity is not MainActivity")
        }

       }
       catch (e: Exception) {
        Log.e("MyTag", "Error launching camera: ${e.message}")
        promise.reject("Error", "Failed to open camera: ${e.message}")
    }}
        


    @ReactMethod
    fun addListener(eventName: String) {
        // This is required for React Native to manage event listeners
        Log.d("CameraModule", "Listener added for event: $eventName")
    }

    @ReactMethod
    fun removeListeners(count: Int) {
        // This is required for React Native to manage event listeners
        Log.d("CameraModule", "Listeners removed:")
    }

    // fun sendEventToReact(eventName: String, params: String) {
    //     Log.d("CameraModule", "sendEventToReact called with event: $eventName and params: $params")
    //     reactApplicationContext
    //         .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
    //         .emit(eventName, params)
    //     Log.d("CameraModule", "Event emitted successfully.")
    // }

//     fun sendEventToReact(eventName: String, params: String) {
//     Log.d("CameraModule", "sendEventToReact called with event: $eventName and params: $params")
//     reactApplicationContext.runOnUiQueueThread {
//         Log.d("CameraModule", "About to emit event: $eventName with params: $params")
//         reactApplicationContext
//             .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
//             .emit("ImageCaptured", "success")
//         Log.d("CameraModule", "Event emitted successfully on UI thread.")
//     }
// }

fun sendEventToReact(eventName: String, params: String) {
    Log.d("CameraModule", "sendEventToReact called with event: $eventName and params: $params")

    // Use Handler to delay the emission
    Handler(Looper.getMainLooper()).postDelayed({
        reactApplicationContext.runOnUiQueueThread {
            Log.d("CameraModule", "About to emit event: $eventName with params: $params")
            reactApplicationContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit(eventName, params)
            Log.d("CameraModule", "Event emitted successfully on UI thread.")
        }
    }, 2000) // Delay of 2 seconds
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

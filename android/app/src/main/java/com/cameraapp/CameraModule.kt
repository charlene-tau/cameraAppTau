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
    private var photoFilePath: String? = null
    private var flashEnabled: Boolean = false

    override fun getName(): String {
        return "CameraModule"
    }

//    @ReactMethod
//    fun openCamera(promise: Promise) {
//        Log.d(TAG,"hi from openCamera Function")
//        Log.d(TAG, "Debug message")
//        Log.i(TAG, "Info message")
//        Log.e("MyTag", "Error message")
//        Log.d(TAG,"currentActivity var: $currentActivity")
//        val activity: Activity? = currentActivity
//        Log.d(TAG,"activity var: $activity")
//        if (activity != null) {
//            try {
//                Log.d(TAG,"inside try block")
//                // Create an intent for the camera
//                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                Log.d(TAG,"after having intent")
//                // Create a file for saving the photo
//                //val photoFile = createImageFile()
//                //Log.d(TAG,"photoFile looks like: $photoFile")
//                activity.startActivityForResult(intent, 1)
//                promise.resolve("Camera opened successfully")
////                if (photoFile != null) {
////                    val photoURI: Uri = FileProvider.getUriForFile(
////                        activity.applicationContext,
////                        "${activity.applicationContext.packageName}.fileprovider",
////                        photoFile
////                    )
////                    Log.d(TAG,"photoURI looks like: $photoURI")
////                    // Attach the file URI to the intent
////                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
////
////                    // Grant URI permissions
////                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
////
////                    // Add flash mode if needed
////                    if (flashEnabled) {
////                        intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false) // Example of a flash toggle
////                    }
////
////                    // Start the camera activity
////                    Log.d(TAG,"just before start activity: ")
////                    //Log.d(TAG, "Checking permissions: ${ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)}")
////                    Log.d(TAG, "Starting camera intent")
////                    activity.startActivityForResult(intent, 1)
////                    Log.d(TAG,"AFTER  start activity: ")
////                    // Resolve the promise with the file path
//////                    promise.resolve(photoFilePath)
////                } else {
////                    promise.reject("FileCreationError", "Failed to create photo file")
////                }
//            } catch (e: Exception) {
//                promise.reject("CAMERA_ERROR", "Failed to open camera", e)
//            }
//        } else {
//            promise.reject("NO_ACTIVITY", "No activity available")
//        }
//    }


    @ReactMethod
    fun openCamera(promise: Promise) {
        val activity = currentActivity
        Log.d("MyTag", "openCamera method invoked Pigs are cute")
        Log.d("MyTag", "activity : $activity")
        if (activity == null) {
            promise.reject("NO_ACTIVITY", "No activity found")
            return
        }
        Log.d("MyTag", "check this condition: ${(ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)}")
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MyTag", "inside this new IF BLOCK")
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), 101)
            promise.reject("PERMISSION_DENIED", "Camera permission not granted")
            return
        }

        try {
            // Create an intent to open the camera
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            Log.d("MyTag", "cameraIntent piglest : $cameraIntent")
            // Start the camera activity
            activity.startActivityForResult(cameraIntent, 1)

            // Resolve the promise immediately (the emulator doesn't save real images)
            promise.resolve("Camera opened successfully")
        } catch (e: Exception) {
            promise.reject("CAMERA_ERROR", "Failed to open camera", e)
        }
    }



//    private fun checkAndRequestPermissions(): Boolean {
//        val permissions = arrayOf(android.Manifest.permission.CAMERA)
//        val permissionsNeeded = permissions.filter {
//            ContextCompat.checkSelfPermission(currentActivity!!, it) != PackageManager.PERMISSION_GRANTED
//        }
//
//        if (permissionsNeeded.isNotEmpty()) {
//            ActivityCompat.requestPermissions(
//                currentActivity!!,
//                permissionsNeeded.toTypedArray(),
//                CAMERA_REQUEST_CODE
//            )
//            return false
//        }
//        return true
//    }



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

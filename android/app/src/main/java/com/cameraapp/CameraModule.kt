package com.cameraapp
import android.net.Uri
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule

class CameraModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    private var photoFilePath: String? = null
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
                val photoFile = createImageFile() // Create a file for saving the photo
                if (photoFile != null) {
                    val photoURI: Uri = Uri.fromFile(photoFile)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                    // Add flash mode if needed
                    if (flashEnabled) {
                        intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false) // Example of a flash toggle
                    }

                    activity.startActivityForResult(intent, 1)
                    promise.resolve(photoFilePath) // Return the photo path to JS
                } else {
                    promise.reject("FileCreationError", "Failed to create photo file")
                }
            //                activity.startActivityForResult(intent, 1)
            //                promise.resolve("Camera opened successfully")


            } catch (e: Exception) {
                promise.reject("CAMERA_ERROR", "Failed to open camera", e)
            }
        } else {
            promise.reject("NO_ACTIVITY", "No activity available")
        }
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
        flashEnabled = !flashEnabled
        // Logic to handle flash toggle
        Log.d("CameraModule", "Flash mode: ${if (flashEnabled) "ON" else "OFF"}")
        promise.resolve(flashEnabled)
    }
}

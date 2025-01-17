package com.cameraapp

import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import android.content.Intent
import android.app.Activity
import com.facebook.react.bridge.ReactContext
import androidx.activity.result.ActivityResultLauncher
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ReactActivity() {
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MyTag", "Camera result received: onCreate function in MainActivity.kt called")
        // Initialize the ActivityResultLauncher
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("MyTag", "Camera launcher Inside the variable cameraLauncher")
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("MyTag", "Camera result received: ${result.data}")
                Log.d("MyTag", "inside MainActivity file LOOK HEREEEE")
                // Handle the camera result
                val imageBitmap = result.data?.extras?.get("data")

                // Send an event back to React Native
                val reactContext = reactInstanceManager.currentReactContext
                if (reactContext != null) {
                val cameraModule = reactContext.getNativeModule(CameraModule::class.java)
                cameraModule?.sendEventToReact("ImageCaptured", "success")
                Log.d("MainActivity", "ImageCaptured event emitted successfully")}
                else {
                    Log.e("MainActivity", "ReactContext is null. Event not emitted.")
                }

            }
        }
    }
  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */

  fun launchCamera(intent: Intent) {
      cameraLauncher.launch(intent)
  }
  override fun getMainComponentName(): String = "CameraApp"

  /**
   * Returns the instance of the [ReactActivityDelegate]. We use [DefaultReactActivityDelegate]
   * which allows you to enable New Architecture with a single boolean flags [fabricEnabled]
   */
  override fun createReactActivityDelegate(): ReactActivityDelegate =
      DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)

  fun launchCamera() {
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(cameraIntent)
    }


}

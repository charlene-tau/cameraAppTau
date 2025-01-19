package com.cameraapp

import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import android.content.Intent
import android.app.Activity

import androidx.activity.result.ActivityResultLauncher
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import android.content.pm.PackageManager
import android.Manifest
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facebook.react.ReactApplication
import com.facebook.react.bridge.ReactContext
import android.os.Handler
import android.os.Looper
import com.facebook.react.ReactInstanceManager

class MainActivity : ReactActivity() {
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MyTag", "onCreate function in MainActivity.kt called")

        Log.d("MainActivity", "hi this is frustrating ddsfsd")
        // Initialize the ActivityResultLauncher
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("MainActivity", "hi this is frustrating")
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("MainActivity", "Camera result received successfully.")

                // Access the ReactInstanceManager from the application
                val reactInstanceManager = (application as ReactApplication).reactNativeHost.reactInstanceManager
                Log.d("MainActivity", "ReactInstanceManager: $reactInstanceManager")
                Log.d("MainActivity", "After disneysea Camera result received successfully.")
                // Check if the ReactContext is already initialized
                val reactContext = reactInstanceManager.currentReactContext
                Log.d("MainActivity","Just after initializing reactContext PIGLETS ")
                Log.d("MainActivity","Just after initializing reactContext PIGLETS, reactContext: $reactContext ") 
                // this reactContext keep being null, and this is the probelm
                Log.d("MainActivity","End of JuST after initializing reactContext PIGLETS ")
                if (reactContext != null) {
                    Log.d("MainActivity","reactContext PIGLETS is not null")
                    try {
                        val cameraModule = reactContext.getNativeModule(CameraModule::class.java)
                        if (cameraModule != null) {
                            Log.d("MainActivity", "CameraModule found.")
                            cameraModule.sendEventToReact("ImageCaptured", "success") // Send event to React Native
                        } else {
                            Log.e("MainActivity", "CameraModule is null.")
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error accessing CameraModule: ${e.message}", e)
                    }
                } else {
                    // ReactContext is not ready; retry after a delay
                    Log.e("MainActivity", "ReactContext is null. Retrying...")
                    retryAccessCameraModule(reactInstanceManager)
                }
            } else {
                Log.e("MainActivity", "Camera result not OK.")
            }
        }
    }

//    private fun navigateToNextScreenFallback() {
//        val intent = Intent(this, NextScreenActivity::class.java)
//        startActivity(intent)
//        Log.d("MainActivity", "Navigating to NextScreen fallback.")
//    }


    // override fun onResume() {
    //     super.onResume()
    //     Log.d("MainActivity", "onResume called")
    //     Log.d("MainActivity", "wondering if can see this")
    //     // Check if the React context is available
    //     val reactContext = reactInstanceManager.currentReactContext

    //     Log.d("MainActivity", "DID Y SEE THIS???")
    //     if (reactContext != null) {
    //         Log.d("MainActivity", "React context is available in onResume")
    //         // Proceed with your logic (e.g., send the camera result to React Native)
    //     } else {
    //         Log.d("MainActivity", "React context is null in onResume")
    //         // Handle the case where the React context is not available
    //     }
    // }


    private fun retryAccessCameraModule(reactInstanceManager: ReactInstanceManager, retries: Int = 5) {
        if (retries == 0) {
            Log.e("MainActivity", "Failed to access CameraModule after retries.")
//            navigateToNextScreenFallback()
            return
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val reactContext = reactInstanceManager.currentReactContext
            if (reactContext != null) {
                try {
                    val cameraModule = reactContext.getNativeModule(CameraModule::class.java)
                    if (cameraModule != null) {
                        Log.d("MainActivity", "CameraModule found after retry.")
                        cameraModule.sendEventToReact("ImageCaptured", "success") // Send event to React Native
                    } else {
                        Log.e("MainActivity", "CameraModule is null after retry.")
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error accessing CameraModule after retry: ${e.message}", e)
                }
            } else {
                Log.e("MainActivity", "ReactContext is still null. Retrying...")
                retryAccessCameraModule(reactInstanceManager, retries - 1)
            }
        }, 10000) // Retry every 1 second
    }


  fun launchCamera(intent: Intent) {
      cameraLauncher.launch(intent)
  }
  override fun getMainComponentName(): String = "CameraApp"

  
  override fun createReactActivityDelegate(): ReactActivityDelegate =
      DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)



override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("MyTag", "Camera permission granted")
            // Launch the camera after permission is granted
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            launchCamera(cameraIntent)
        } else {
            Log.e("MyTag", "Camera permission denied")
            // Optionally, send a rejection event back to React Native
            val reactContext = reactInstanceManager.currentReactContext
            reactContext?.getNativeModule(CameraModule::class.java)?.sendEventToReact(
                "PermissionDenied",
                "Camera permission was denied"
            )
        }
    }

fun checkAndLaunchCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, launch the camera
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            launchCamera(cameraIntent)
        } else {
            // Request camera permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                101
            )
        }
    }


}

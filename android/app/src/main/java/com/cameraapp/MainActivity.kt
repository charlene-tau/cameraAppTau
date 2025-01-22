package com.cameraapp

import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import android.content.Intent
import android.app.Activity
import androidx.activity.result.ActivityResult
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
import com.facebook.react.ReactInstanceEventListener

import com.cameraapp.MainApplication


class MainActivity : ReactActivity() {
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "onCreate called ctsj")
        Log.d("MyTag", "onCreate function in MainActivity.kt called")

        val reactInstanceManager = (application as MainApplication).reactNativeHost.reactInstanceManager 

        val reactContext = reactInstanceManager.currentReactContext
        Log.d("MainActivity","value of reactContext: $reactContext")

        fun handleReactContext(reactContext: ReactContext, result: ActivityResult) {
        try {
        val cameraModule = reactContext.getNativeModule(CameraModule::class.java)
        if (cameraModule != null) {
            cameraModule.sendEventToReact("ImageCaptured", "success")
            Log.d("MainActivity", "Event sent to React successfully.")
        } else {
            Log.e("MainActivity", "CameraModule is null.")
        }
         } catch (e: Exception) {
            Log.e("MainActivity", "Error processing camera result: ${e.message}", e)
        }
        }



            cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("MainActivity", "Camera result received successfully.")

            val reactContext = reactInstanceManager.currentReactContext
            Log.d("MainActivity", "ReactContext value: $reactContext")

            if (reactContext == null) {
                // ReactContext is null, attempt to initialize it
                Log.e("MainActivity", "ReactContext is null. Initializing ReactContext...")
        
        //start of replacement
        //                 reactInstanceManager.addReactInstanceEventListener(object : ReactInstanceManager.ReactInstanceEventListener {
        //         override fun onReactContextInitialized(reactContext: ReactContext) {
        // Log.d("MainActivity","jus abt to call handleReactContext")
        // Log.d("MainActivity","jus abt to call view reactContext: $reactContext")
        // handleReactContext(reactContext, result)
        //         }
        //         })
        //end of replacement

// start of either the above or this
        reactInstanceManager.addReactInstanceEventListener(
    ReactInstanceManager.ReactInstanceEventListener { reactContext ->
        Log.d("MainActivity", "NEWLY just about to call handleReactContext ctsj")
        Log.d("MainActivity", "NEWLY just about to call view reactContext: $reactContext")
        handleReactContext(reactContext, result)
    }
)
// end of either the above or this
        

                Log.d("MainActivity","Going to call createReactContextInBackground startssss")
                // Trigger ReactContext creation
                reactInstanceManager.createReactContextInBackground()
            } else {
                // ReactContext is already available, proceed with your logic
                Log.d("MainActivity", "ReactContext is available. Processing the result.")
                handleReactContext(reactContext, result)
            }
        } else {
            Log.e("MainActivity", "Camera result not OK.")
        }
                    }


    }


    

     override fun onStart() {
        // seems like onStart called when app start and return to react native app from camera app
        super.onStart()
        Log.d("TAG", "onStart called Mainz vfdgdgd")
    }

    override fun onResume() {
        // called when app starts 
        super.onResume()
        Log.d("TAG", "onResume called")
    //     if (reactInstanceManager.currentReactContext == null) {
    //     Log.w("MainActivity", "ReactContext is null on onResume. Retrying initialization.")
    //     waitForReactContext()
    // }
    }

    override fun onPause() {
        //onPause is also called when i pressed the open camera button on the HomeScreen.tsx
        super.onPause()
        Log.d("TAG", "onPause called")
        
    }

    override fun onStop() {
        //onStop function is called when i press the open camera button on the HomeScreen.tsx
        super.onStop()
        Log.d("TAG", "onStop called")
    }
    override fun onRestart() {
        super.onRestart()
        Log.d("MainActivityLifecycle", "onRestart called")
        // Add your cleanup logic here
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivityLifecycle", "onDestroy called")
        // Add your cleanup logic here
    }


    private fun retryAccessCameraModule(reactInstanceManager: ReactInstanceManager, retries: Int = 15) {
        if (retries == 0) {
            Log.e("MainActivity", "Failed to access CameraModule after retries.")

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
    //cameraLauncher is a private variable in this class
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

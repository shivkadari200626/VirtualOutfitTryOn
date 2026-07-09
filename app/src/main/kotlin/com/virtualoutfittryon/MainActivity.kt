package com.virtualoutfittryon

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var viewFinder: PreviewView
    private var poseLandmarker: PoseLandmarker? = null
    private lateinit var poseOverlay: PoseOverlay
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewFinder = findViewById(R.id.viewFinder)
        poseOverlay = PoseOverlay(this)
        
        // Add overlay on top of PreviewView
        (viewFinder.parent as androidx.constraintlayout.widget.ConstraintLayout).addView(poseOverlay)

        setupPoseLandmarker()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 10)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun setupPoseLandmarker() {
        try {
            val baseOptions = BaseOptions.builder()
               .setModelAssetPath("pose_landmarker_lite.task")
               .build()

            val options = PoseLandmarker.PoseLandmarkerOptions.builder()
               .setBaseOptions(baseOptions)
               .setRunningMode(RunningMode.LIVE_STREAM)
               .setMinPoseDetectionConfidence(0.5f)
               .setResultListener { result, _ ->
                    runOnUiThread { poseOverlay.updateResults(result) }
                }
               .setErrorListener { error: RuntimeException ->
                    Log.e("Pose", "MediaPipe error: ${error.message}")
                }
               .build()

            poseLandmarker = PoseLandmarker.createFromOptions(this, options)
            Log.d("Pose", "MediaPipe initialized successfully")
        } catch (e: Exception) {
            Log.e("Pose", "Failed to init MediaPipe. Did you add pose_landmarker_lite.task to assets?", e)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
               .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
               .build().also {
                    it.setAnalyzer(cameraExecutor, PoseAnalyzer())
                }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
                Log.d("Camera", "Camera started")
            } catch (exc: Exception) {
                Log.e("Camera", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private inner class PoseAnalyzer : ImageAnalysis.Analyzer {
        override fun analyze(imageProxy: ImageProxy) {
            // Disabled for now to prevent crash. We'll enable after app opens
            // TODO: Convert ImageProxy to Bitmap and run poseLandmarker.detectAsync()
            imageProxy.close()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Log.e("Camera", "Camera permission denied")
            }
        }
    }

    private fun allPermissionsGranted() = arrayOf(Manifest.permission.CAMERA).all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        poseLandmarker?.close()
    }
}

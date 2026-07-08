package com.virtualoutfittryon

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var viewFinder: PreviewView
    private lateinit var poseLandmarker: PoseLandmarker
    private lateinit var poseOverlay: PoseOverlay
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        viewFinder = findViewById(R.id.viewFinder)
        poseOverlay = PoseOverlay(this)
        
        setupPoseLandmarker()
        
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 10)
        }
        
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun setupPoseLandmarker() {
        val baseOptions = BaseOptions.builder()
            .setModelAssetPath("pose_landmarker_lite.task")
            .build()

        poseLandmarker = PoseLandmarker.builder()
            .setBaseOptions(baseOptions)
            .setRunningMode(PoseLandmarker.RunningMode.LIVE_STREAM)
            .setNumPoses(1)
            .setMinPoseDetectionConfidence(0.5f)
            .setResultListener { result, input ->
                runOnUiThread {
                    // This will draw on the overlay
                    // You need to add a custom View on top of PreviewView for this
                }
            }
            .setErrorListener { Log.e("Pose", it.message ?: "Error") }
            .build()
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
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, PoseAnalyzer())
                }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            } catch (exc: Exception) {
                Log.e("Camera", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private inner class PoseAnalyzer : ImageAnalysis.Analyzer {
        override fun analyze(imageProxy: ImageProxy) {
            // Convert ImageProxy to MediaPipe Image and run poseLandmarker
            // We'll wire this properly after build passes
            imageProxy.close()
        }
    }

    private fun allPermissionsGranted() = arrayOf(Manifest.permission.CAMERA).all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}

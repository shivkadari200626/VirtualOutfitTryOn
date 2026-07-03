package com.virtualoutfittryon

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.virtualoutfittryon.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var cameraExecutor: ExecutorService

    private var currentOutfitType = "shirt"
    private var shirtBitmap: Bitmap? = null
    private var pantsBitmap: Bitmap? = null

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) startCamera() else Log.e("Main", "Permission denied")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        loadClothingAssets()
        setupButtons()

        if (allPermissionsGranted()) startCamera() 
        else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun loadClothingAssets() {
        try {
            shirtBitmap = assets.open("clothing/shirt1.png").use { BitmapFactory.decodeStream(it) }
            pantsBitmap = assets.open("clothing/pants1.png").use { BitmapFactory.decodeStream(it) }
        } catch (e: Exception) {
            Log.e("Assets", "Failed to load clothing images", e)
        }
    }

    private fun setupButtons() {
        binding.btnShirt.setOnClickListener {
            currentOutfitType = "shirt"
            updateOverlay()
        }
        binding.btnPants.setOnClickListener {
            currentOutfitType = "pants"
            updateOverlay()
        }
        binding.btnClear.setOnClickListener {
            binding.overlayView.setClothing(null)
        }
    }

    private fun updateOverlay() {
        val bitmap = if (currentOutfitType == "shirt") shirtBitmap else pantsBitmap
        val width = binding.overlayView.width.toFloat()
        val height = binding.overlayView.height.toFloat()

        binding.overlayView.clothingRect.set(width*0.25f, height*0.2f, width*0.75f, height*0.6f)
        binding.overlayView.setClothing(bitmap)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { proxy ->
                        // MediaPipe landmarks would go here in full version
                        proxy.close()
                    }
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageAnalysis)
            } catch (e: Exception) {
                Log.e("Camera", "Failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}

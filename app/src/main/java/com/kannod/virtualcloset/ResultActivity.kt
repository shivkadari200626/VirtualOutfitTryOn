package com.virtualoutfittryon  // <-- CHANGE THIS

import com.virtualoutfittryon.databinding.ActivityMainBinding
import com.virtualoutfittryon.databinding.ActivityResultBinding


import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kannod.virtualcloset.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val byteArray = intent.getByteArrayExtra("result_image")
        if (byteArray != null && byteArray.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            if (bitmap != null) {
                binding.resultImageView.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "Failed to load generated image", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "No image data received", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.backButton.setOnClickListener { finish() }
    }
}

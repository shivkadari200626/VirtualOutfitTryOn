package com.kannod.virtualcloset

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.ImagePart  // Use ImagePart
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TryOnViewModel : ViewModel() {
class TryOnViewModel : ViewModel() {
    
    fun captureAndProcess(imageBitmap: Bitmap) {
        viewModelScope.launch {
            try {
                // Add the logs here
                val key = BuildConfig.GROQ_API_KEY
                Log.d("GROQ_DEBUG", "Key: '$key'") 
                Log.d("GROQ_DEBUG", "Key length: ${key.length}")
                Log.d("GROQ_DEBUG", "Starts with gsk_: ${key.startsWith("gsk_")}")
                
                if (key.isEmpty()) {
                    Log.e("GROQ_DEBUG", "KEY IS EMPTY!")
                    return@launch
                }
                
                val authHeader = "Bearer $key"
                Log.d("GROQ_DEBUG", "Sending header: Bearer ${key.take(7)}...")
                
                // Your actual GROQ API call
                val response = groqApi.chatCompletion(authHeader, request)
                
            } catch (e: Exception) {
                Log.e("GROQ_DEBUG", "API error", e)
            }
        }
    }
}
    fun clearError() {
        _error.value = null
    }

    fun clearResult() {
        _resultImage.value = null
    }
}

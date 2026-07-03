package com.kannod.virtualcloset

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TryOnViewModel : ViewModel() { // Only declare this once

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _resultImage = MutableLiveData<Bitmap?>()
    val resultImage: LiveData<Bitmap?> = _resultImage

    // You need to init your Retrofit API here
    // private val groqApi = RetrofitClient.groqApiService 

    fun captureAndProcess(imageBitmap: Bitmap) {
        viewModelScope.launch {
            try {
                val key = BuildConfig.GROQ_API_KEY
                Log.d("GROQ_DEBUG", "Key: '$key'") 
                Log.d("GROQ_DEBUG", "Key length: ${key.length}")
                Log.d("GROQ_DEBUG", "Starts with gsk_: ${key.startsWith("gsk_")}")
                
                if (key.isEmpty()) {
                    Log.e("GROQ_DEBUG", "KEY IS EMPTY!")
                    _error.postValue("API key missing")
                    return@launch
                }
                
                val authHeader = "Bearer $key"
                Log.d("GROQ_DEBUG", "Sending header: Bearer ${key.take(7)}...")
                
                // Replace this with your actual request object
                // val request = GroqRequest(...) 
                // val response = groqApi.chatCompletion(authHeader, request)
                
                // Log.d("GROQ_DEBUG", "Response code: ${response.code()}")
                
            } catch (e: Exception) {
                Log.e("GROQ_DEBUG", "API error", e)
                _error.postValue(e.message)
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

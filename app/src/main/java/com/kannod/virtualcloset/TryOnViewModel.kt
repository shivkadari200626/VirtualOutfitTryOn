package com.kannod.virtualcloset

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TryOnViewModel : ViewModel() {

    private val _resultImage = MutableLiveData<Bitmap?>()
    val resultImage: LiveData<Bitmap?> = _resultImage

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun generateOutfit(uri: Uri, apiKey: String, contentResolver: ContentResolver) {
        viewModelScope.launch {
            try {
                // TODO: Add your Gemini API call here
                // For now, just convert URI to bitmap and return it
                val inputStream = contentResolver.openInputStream(uri)
                val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                _resultImage.value = bitmap
            } catch (e: Exception) {
                _error.value = "Failed: ${e.message}"
            }
        }
    }
}

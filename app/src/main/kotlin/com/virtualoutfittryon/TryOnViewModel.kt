package com.virtualoutfittryon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TryOnViewModel : ViewModel() {
    private val repository = StyleSnapRepository()

    fun fetchSuggestion(poseData: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            val result = repository.getOutfitSuggestion(poseData)
            onResult(result)
        }
    }

    // If line 110 had inset error, delete that line or fix with:
    // import androidx.core.view.WindowInsetsCompat
}

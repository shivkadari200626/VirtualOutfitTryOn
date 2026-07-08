package com.virtualoutfittryon

class StyleSnapRepository {
    suspend fun getOutfitSuggestion(poseData: String): String {
        val apiKey = BuildConfig.GROQ_API_KEY
        if (apiKey.isEmpty()) return "Error: API Key not set"

        val request = GroqRequest(
            model = "llama3-8b-8192",
            messages = listOf(Message("user", "Based on this body pose data: $poseData, suggest 3 outfit styles"))
        )
        val response = GroqApi.service.getStyleSuggestion("Bearer $apiKey", request)
        return if (response.isSuccessful) {
            response.body()?.choices?.firstOrNull()?.message?.content?: "No suggestion"
        } else {
            "API Error: ${response.code()}"
        }
    }
}

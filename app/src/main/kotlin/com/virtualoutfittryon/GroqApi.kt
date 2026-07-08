package com.virtualoutfittryon

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

data class GroqRequest(
    @SerializedName("model") val model: String,
    @SerializedName("messages") val messages: List<Message>
)
data class Message(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)
data class GroqResponse(
    @SerializedName("choices") val choices: List<Choice>
)
data class Choice(
    @SerializedName("message") val message: Message
)

interface GroqApiService {
    @POST("chat/completions")
    suspend fun getStyleSuggestion(
        @Header("Authorization") auth: String,
        @Body request: GroqRequest
    ): Response<GroqResponse>
}

object GroqApi {
    private const val BASE_URL = "https://api.groq.com/openai/v1/"

    private val client = OkHttpClient.Builder()
       .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
       .connectTimeout(60, TimeUnit.SECONDS)
       .readTimeout(60, TimeUnit.SECONDS)
       .build()

    private val retrofit = Retrofit.Builder()
       .baseUrl(BASE_URL)
       .client(client)
       .addConverterFactory(GsonConverterFactory.create())
       .build()

    val service: GroqApiService = retrofit.create(GroqApiService::class.java)
}

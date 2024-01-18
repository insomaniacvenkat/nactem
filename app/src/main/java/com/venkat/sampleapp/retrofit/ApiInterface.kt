package com.venkat.sampleapp.retrofit

import com.venkat.sampleapp.model.MeaningsData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * This is ApiInterface, which provides Retrofit Client & OkHttp to call web API.
 */
interface ApiInterface {

    @GET("dictionary.py")
    suspend fun getMeaningsData(
        @Query("sf") sortForm: String
    ): Response<MeaningsData>

    companion object {
        private const val BASE_URL = "https://www.nactem.ac.uk/software/acromine/"

        // Create an OkHttpClient with an Interceptor for compression
        private val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor(CompressionInterceptor())
                .build()
        }

        private val retrofitService: ApiInterface by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient) // Set the OkHttpClient with compression
                .build()
                .create(ApiInterface::class.java)
        }

        fun getInstance(): ApiInterface = retrofitService
    }

    // Interceptor for enabling gzip compression
    class CompressionInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val originalRequest = chain.request()
            val compressedRequest = originalRequest.newBuilder()
                .header("Accept-Encoding", "gzip")
                .build()

            return chain.proceed(compressedRequest)
        }
    }

}

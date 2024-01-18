package com.venkat.sampleapp.repository

import com.venkat.sampleapp.model.MeaningsData
import com.venkat.sampleapp.retrofit.ApiInterface
import retrofit2.Response

/**
 * MainRepository is responsible for handling the network operations related to retrieving meanings data.
 */
class MainRepository(private val retrofitClient: ApiInterface) {

    suspend fun getMeaningsData(sortForm: String): NetworkState<MeaningsData> {
        val response = retrofitClient.getMeaningsData(sortForm)
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

    sealed class NetworkState<out T> {
        data class Success<out T>(val data: T) : NetworkState<T>()
        data class Error<T>(val response: Response<T>) : NetworkState<T>()
    }
}

package com.venkat.sampleapp.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venkat.sampleapp.model.MeaningsData
import com.venkat.sampleapp.repository.MainRepository
import com.venkat.sampleapp.retrofit.ApiInterface
import com.venkat.sampleapp.utils.ValidationUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException

/**
 * Business Logic happens here
 */
class MainViewModel : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage
    val largeFormList = MutableLiveData<List<String>>()
    val loading = MutableLiveData(View.GONE)
    val rvVisibility = MutableLiveData(View.GONE)
    private val retrofitClient by lazy { ApiInterface.getInstance() }
    private val mainRepository by lazy { MainRepository(retrofitClient) }

    //API call to fetch meanings data for sortForm provided by user.
    fun getMeaningsData(sortForm: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(View.VISIBLE)
            try {
                when (val response = mainRepository.getMeaningsData(sortForm)) {
                    is MainRepository.NetworkState.Success -> {
                        getLargeFormsList(response.data)
                        loading.postValue(View.GONE)
                    }
                    is MainRepository.NetworkState.Error -> {
                        onError(response.toString())
                    }
                }
            } catch (ex: UnknownHostException) {
                onError(ValidationUtil.NETWORK_ERROR_MESSAGE)
            } catch (ex: java.lang.Exception) {
                onError(ex.stackTraceToString())
            }
        }
    }

    //Segregating large form list from MeaningsData response.
    private fun getLargeFormsList(meaningsData: MeaningsData) {
        if ((meaningsData.isNotEmpty()) && (meaningsData[0].lfs.isNotEmpty())) {
            val tempLfArrayList = mutableListOf<String>()
            for (lfItem in meaningsData[0].lfs) {
                tempLfArrayList.add(lfItem.lf)
            }
            largeFormList.postValue(tempLfArrayList)
        } else {
            onError(ValidationUtil.RESPONSE_ERROR_MESSAGE)
        }
    }

    private fun onError(message: String) {
        _errorMessage.postValue(message)
        loading.postValue(View.GONE)
    }
}


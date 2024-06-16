package com.cataract.detection.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cataract.detection.connection.endpoint.ApiEndpoint
import com.cataract.detection.connection.model.HistoryModel
import com.cataract.detection.connection.service.ApiDashboardService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {
    private val _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> get() = _messageError

    private val _messageSuccess = MutableLiveData<String>()
    val messageSuccess: LiveData<String> get() = _messageSuccess

    private val _listHistory = MutableLiveData<List<HistoryModel.HistoryItem>>()
    val listHistory: LiveData<List<HistoryModel.HistoryItem>> get() = _listHistory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getHistory(context: Context) {
        _isLoading.postValue(true)
        val apiService = ApiDashboardService(context).retrofit.create(ApiEndpoint::class.java)

        apiService.getHistories().enqueue(object : Callback<HistoryModel.Success> {
            override fun onResponse(call: Call<HistoryModel.Success>, response: Response<HistoryModel.Success>) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    val successResponse = response.body()
                    if (successResponse?.data != null) {
                        successResponse.data.history?.let {
                            _listHistory.postValue(it as List<HistoryModel.HistoryItem>)
                        }
                    } else {
                        _messageError.postValue("Success but response body is null or data is null")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    if (!errorBody.isNullOrEmpty()) {
                        _messageError.postValue("Error: $errorBody")
                    } else {
                        _messageError.postValue("Request unsuccessful: Default error message")
                    }
                }
            }

            override fun onFailure(call: Call<HistoryModel.Success>, t: Throwable) {
                _messageError.postValue("Request failed: ${t.message}")
            }
        })
    }
}

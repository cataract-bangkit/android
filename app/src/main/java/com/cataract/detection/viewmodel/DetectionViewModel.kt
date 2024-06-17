package com.cataract.detection.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cataract.detection.connection.endpoint.ApiEndpoint
import com.cataract.detection.connection.model.DetectionModel
import com.cataract.detection.connection.service.ApiDashboardService
import com.google.gson.Gson
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetectionViewModel : ViewModel() {
    private val _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> get() = _messageError

    private val _messageSuccess = MutableLiveData<String>()
    val messageSuccess: LiveData<String> get() = _messageSuccess

    private val _resultPrediction = MutableLiveData<DetectionModel.Result?>()
    val resultPrediction: LiveData<DetectionModel.Result?> get() = _resultPrediction

    fun prediction(context: Context, image: MultipartBody.Part) {
        val apiService = ApiDashboardService(context).retrofit.create(ApiEndpoint::class.java)

        apiService.prediction(image).enqueue(object : Callback<DetectionModel.Success> {
            override fun onResponse(call: Call<DetectionModel.Success>, response: Response<DetectionModel.Success>) {
                if (response.isSuccessful) {
                    val successResponse = response.body()
                    if (successResponse != null) {
                        successResponse.data?.let {
                            _resultPrediction.postValue(it.result)
                        } ?: run {
                            _messageError.postValue("Success but response data is null")
                        }
                    } else {
                        _messageError.postValue("Success but response body is null")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    if (!errorBody.isNullOrEmpty()) {
                        try {
                            val errorResponse = Gson().fromJson(errorBody, DetectionModel.Error::class.java)
                            _messageError.postValue("Error: ${errorResponse.code}, Message: ${errorResponse.message}, Status: ${errorResponse.status}")
                        } catch (e: Exception) {
                            _messageError.postValue("Error parsing error response: ${e.message}")
                        }
                    } else {
                        _messageError.postValue("Request unsuccessful: Default error message")
                    }
                }
            }

            override fun onFailure(call: Call<DetectionModel.Success>, t: Throwable) {
                _messageError.postValue("Request failed: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "DetectionViewModel"
    }
}
